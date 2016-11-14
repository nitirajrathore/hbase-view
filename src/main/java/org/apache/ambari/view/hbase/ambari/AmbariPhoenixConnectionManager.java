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

package org.apache.ambari.view.hbase.ambari;

import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.Constants;
import org.apache.ambari.view.hbase.core.PhoenixConnection;
import org.apache.ambari.view.hbase.core.configs.PhoenixConfig;
import org.apache.ambari.view.hbase.core.service.PhoenixConnectionManager;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.core.service.internal.ViewActorSystem;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
public class AmbariPhoenixConnectionManager extends PhoenixConnectionManager {
  private static AmbariPhoenixConnectionManager manager;

  @Override
  protected Connection createConnection(PhoenixConfig configs) throws PhoenixException {
    String url = configs.getUrl();
    try {
      return new PhoenixConnection(DriverManager.getConnection(url), ViewActorSystem.get().getPhoenixConnectionActor());
    } catch (Exception e) {
      log.error("Error while creating phoenix connection : ", e);
      throw new PhoenixException(String.format("Cannot get connection of url : %s", url));
    }
  }

  public static AmbariPhoenixConnectionManager getInstance() throws ServiceException {
    if (null == manager) {
      synchronized (AmbariPhoenixConnectionManager.class) {
        if (null == manager) {
          manager = new AmbariPhoenixConnectionManager();
          try {
            Class<?> klass2 = Class.forName(Constants.PHOENIX_QUERYSERVER_CLIENT_DRIVER);
          } catch (ClassNotFoundException e) {
            log.error("Cannot register the phoenix Driver", e);
            throw new ServiceException(e);
          }

        }
      }
    }

    return manager;
  }
}
