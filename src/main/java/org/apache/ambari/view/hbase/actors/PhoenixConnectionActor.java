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
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.PhoenixConnection;
import org.apache.ambari.view.hbase.messages.CloseConnectionMessage;
import org.apache.ambari.view.hbase.messages.ConnectionCreatedMessage;
import scala.PartialFunction;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PhoenixConnectionActor extends AbstractActor{
  private HashMap<Connection, Date> connectionMap;

  public PhoenixConnectionActor(HashMap<Connection, Date> connectionMap) {
    this.connectionMap = connectionMap;
  }

  public static Props props(Map<PhoenixConnection, Date> connectionMap){
    return Props.create(PhoenixConnectionActor.class, connectionMap);
  }

  public PartialFunction receive(){
    return ReceiveBuilder
      .match(ConnectionCreatedMessage.class, new FI.UnitApply<ConnectionCreatedMessage>(){

        @Override
        public void apply(ConnectionCreatedMessage connectionCreatedMessage) throws Exception {
          connectionMap.put(connectionCreatedMessage.getConnection(), new Date());
        }
      })

      .match(CloseConnectionMessage.class, new FI.UnitApply<CloseConnectionMessage>(){

        @Override
        public void apply(CloseConnectionMessage closeConnectionMessage) throws Exception {
          if(!closeConnectionMessage.getConnection().isClosed()){
            try {
              closeConnectionMessage.getConnection().close();
            }catch(Exception e){
              log.error("Error while closing the connection {} :", closeConnectionMessage.getConnection(), e);
            }
          }

          connectionMap.remove(closeConnectionMessage.getConnection());
        }
      })

      .build();
  }
}
