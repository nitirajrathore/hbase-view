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
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.JobStatus;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.phoenix.ResultableAsyncPhoenixJob;
import org.apache.ambari.view.hbase.messages.FetchResultMessage;
import org.apache.ambari.view.hbase.messages.RemoveResultMapping;
import scala.PartialFunction;

@Slf4j
public class PhoenixResultActor extends AbstractActor {

  private final ResultableAsyncPhoenixJob job;
  private final ActorRef mapperActor;

  public PhoenixResultActor(ResultableAsyncPhoenixJob job, ActorRef mapperActor) {
    this.job = job;
    this.mapperActor = mapperActor;
  }

  public static Props props(ResultableAsyncPhoenixJob job, ActorRef mapperActor) {
    return Props.create(PhoenixJobActor.class, job, mapperActor);
  }

  public PartialFunction receive() {
    return ReceiveBuilder

      .match(FetchResultMessage.class, new FI.UnitApply<FetchResultMessage>() {
        @Override
        public void apply(FetchResultMessage fetchResultMessage) throws Exception {
          log.info("Received fetchResultMessage : {}", fetchResultMessage);
          sender().tell(job.getResult(), self());
          job.getPhoenixConnection().close();
          mapperActor.tell(new RemoveResultMapping(job.getPersistentResource().getId()), self());
          PhoenixJob pj = job.getPersistentResource();
          pj.setStatus(JobStatus.FINISHED.name());
          job.getViewServiceFactory().getPhoenixResourceManager().update(pj);
          getContext().stop(self());
        }
      })

      .build();
  }
}
