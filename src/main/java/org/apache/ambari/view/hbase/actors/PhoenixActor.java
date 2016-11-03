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
import akka.actor.Props;
import akka.actor.Status;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.service.internal.ViewActorSystem;
import org.apache.ambari.view.hbase.jobs.phoenix.IPhoenixJob;
import org.apache.ambari.view.hbase.messages.HoldResultMessage;
import scala.PartialFunction;

@Slf4j
public class PhoenixActor extends AbstractActor {

  public PhoenixActor() {
  }

  public static Props props() {
    return Props.create(PhoenixActor.class);
  }

  public PartialFunction receive() {
    return ReceiveBuilder.

      match(IPhoenixJob.class, new FI.UnitApply<IPhoenixJob>() {
        @Override
        public void apply(IPhoenixJob job) throws Exception {
          log.info("Got IPhoenixJob for {}. forwarding it.", job);
          ViewActorSystem.get().getPhoenixJobActor().forward(job, getContext());
        }
      }).

      // receive new HoldResultMessage message and forward it
      match(HoldResultMessage.class, new FI.UnitApply<HoldResultMessage>() {
        @Override
        public void apply(HoldResultMessage holdResultMessage) throws Exception {
          log.info("forwarding holdResultMessage : {}", holdResultMessage);
          ViewActorSystem.get().getResultMapperActor().forward(holdResultMessage, getContext());
        }
      }).

      matchAny(new FI.UnitApply<Object>() {
        @Override
        public void apply(Object x) throws Exception {
          sender().tell(
            new Status.Failure(new Exception("unknown message")), self()
          );
        }
      }).
      build();


  }
}
