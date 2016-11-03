/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.apache.ambari.view.hbase.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.service.internal.ViewActorSystem;
import org.apache.ambari.view.hbase.messages.HoldResultMessage;
import org.apache.ambari.view.hbase.messages.RemoveResultMapping;
import org.apache.ambari.view.hbase.messages.ResultMessage;
import scala.PartialFunction;

import java.util.Map;

@Slf4j
public class ResultMapperActor extends AbstractActor {

  private final Map<String, ActorRef> resultSetMap;

  public ResultMapperActor(Map<String, ActorRef> resultSetMap) {
    this.resultSetMap = resultSetMap;
  }

  public static Props props(Map<String, ActorRef> resultSetMap) {
    return Props.create(ResultMapperActor.class, resultSetMap);
  }

  public PartialFunction receive() {
    return ReceiveBuilder
      // receive new HoldResultMessage message and forward it
      .match(HoldResultMessage.class, new FI.UnitApply<HoldResultMessage>() {
        @Override
        public void apply(HoldResultMessage holdResultMessage) throws Exception {
          log.info("forwarding holdResultMessage : {}", holdResultMessage);
          ActorRef actorRef = ViewActorSystem.get().newPhoenixResultActor(holdResultMessage.getOriginalJob(), self());
          actorRef.forward(holdResultMessage, getContext());
          resultSetMap.put(holdResultMessage.getJobId(), actorRef);
        }
      })

      .match(RemoveResultMapping.class, new FI.UnitApply<RemoveResultMapping>() {
        @Override
        public void apply(RemoveResultMapping removeResultMapping) throws Exception {
          log.info("removing resultset mapping for job : {}", removeResultMapping.getJobId());
          resultSetMap.remove(removeResultMapping.getJobId());
        }
      })

      .match(ResultMessage.class, new FI.UnitApply<ResultMessage>() {
        // always forward it to respective actor
        @Override
        public void apply(ResultMessage resultMessage) throws Exception {
          ActorRef actorRef = resultSetMap.get(resultMessage.getJobId());
          if (null != actorRef) {
            log.info("forwarding message {} ", resultMessage);
            actorRef.forward(resultMessage, getContext());
          } else {
            // TODO : confirm this implementation. Will work only in case of ask pattern.
            log.info("Actor not found for id : {} sending message.", resultMessage.getJobId());
            sender().tell(new Status.Failure(new ViewException("Job not found with id : " + resultMessage.getJobId())), self());
          }
        }
      })

      .build();

  }
}
