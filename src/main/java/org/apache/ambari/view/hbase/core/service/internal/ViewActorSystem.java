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
import lombok.Data;
import org.apache.ambari.view.hbase.actors.PhoenixJobActor;

@Data
public class ViewActorSystem {
  private static ViewActorSystem instance;

  private ActorSystem actorSystem;
  private ActorRef phoenixJobActor;

  private ViewActorSystem(ActorSystem actorSystem) {
    this.actorSystem = actorSystem;
    this.phoenixJobActor = actorSystem.actorOf(PhoenixJobActor.props());
  }

  public static ViewActorSystem get() {
    if (null == instance) {
      synchronized (ViewActorSystem.class) {
        if (null == instance) {
          instance = new ViewActorSystem(ActorSystem.create("hbase-view-actor-system"));
        }
      }
    }

    return instance;
  }
}
