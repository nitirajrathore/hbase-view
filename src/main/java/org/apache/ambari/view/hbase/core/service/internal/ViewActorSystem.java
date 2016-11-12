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

package org.apache.ambari.view.hbase.core.service.internal;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import lombok.Data;
import org.apache.ambari.view.hbase.actors.PhoenixActor;
import org.apache.ambari.view.hbase.actors.PhoenixConnectionActor;
import org.apache.ambari.view.hbase.actors.PhoenixJobActor;
import org.apache.ambari.view.hbase.actors.PhoenixResultActor;
import org.apache.ambari.view.hbase.actors.ResultMapperActor;
import org.apache.ambari.view.hbase.core.PhoenixConnection;
import org.apache.ambari.view.hbase.jobs.phoenix.ResultableAsyncPhoenixJob;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ViewActorSystem {
  private static ViewActorSystem instance;
  private final ActorRef phoenixConnectionActor;
  private final ActorRef resultMapperActor;
  private final Map<PhoenixConnection, Date> connectionMap = new HashMap<>();
  private final Map<String, ActorRef> resultSetMap = new HashMap<>();
  private ActorSystem actorSystem;
  private ActorRef phoenixJobActor;
  private ActorRef phoenixActor;

  private ViewActorSystem(ActorSystem actorSystem) {
    this.actorSystem = actorSystem;
    this.phoenixActor = actorSystem.actorOf(PhoenixActor.props());
    this.phoenixJobActor = actorSystem.actorOf(PhoenixJobActor.props(this.phoenixActor)
                              .withRouter(new RoundRobinPool(40))
                              .withDispatcher("akka.actor.phoenix-job-dispatcher"));
    this.phoenixConnectionActor = actorSystem.actorOf(PhoenixConnectionActor.props(connectionMap));
    this.resultMapperActor = actorSystem.actorOf(ResultMapperActor.props(resultSetMap));
  }

  public static ViewActorSystem get() {
    if (null == instance) {
      synchronized (ViewActorSystem.class) {
        if (null == instance) {
          instance = new ViewActorSystem(ActorSystem.create("hbase-view-actor-system", null, ViewActorSystem.class.getClassLoader()));
        }
      }
    }

    return instance;
  }

  public ActorRef newPhoenixResultActor(ResultableAsyncPhoenixJob originalJob, ActorRef resultMapperActor){
    return actorSystem.actorOf(PhoenixResultActor.props(originalJob, resultMapperActor));
  }
}
