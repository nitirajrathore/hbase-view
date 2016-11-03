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

package org.apache.ambari.view.hbase.core.service;

import akka.util.Timeout;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.Constants;
import org.apache.ambari.view.hbase.core.configs.PhoenixConfig;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.core.service.internal.ViewActorSystem;
import org.apache.ambari.view.hbase.messages.ConnectionCreatedMessage;
import scala.concurrent.duration.Duration;

import java.sql.Connection;
import java.sql.SQLException;

import static akka.pattern.Patterns.ask;

@Slf4j
public abstract class PhoenixConnectionManager {

  public Connection getConnection(PhoenixConfig configs) throws PhoenixException {
    Connection conn = this.createConnection(configs);
    ConnectionCreatedMessage msg = new ConnectionCreatedMessage(conn);
    try {
      ask(ViewActorSystem.get().getPhoenixConnectionActor(), msg, new Timeout(Duration.create(Constants.CONNECTION_INIT_TIMEOUT, "seconds")));
    } catch (Exception e) {
      log.error("Exception occurred while initializing the connection lifecycle.", e);
      try {
        conn.close();
      } catch (SQLException e1) {
        log.error("Exception while closing the connection : {}", e1.getMessage(), e1);
        throw new PhoenixException("Exception occurred while closing the connection.", e);
      }
    }
    return conn;
  }

  protected abstract Connection createConnection(PhoenixConfig configs) throws PhoenixException;

}
