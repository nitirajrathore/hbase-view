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

package org.apache.ambari.view.hbase.core.service.impl;

import org.apache.ambari.view.hbase.core.PhoenixConnection;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.configs.PhoenixConfig;
import org.apache.ambari.view.hbase.core.service.PhoenixConnectionManager;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.core.service.internal.ViewActorSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class PhoenixConnectionManagerImpl extends PhoenixConnectionManager {
  private final static Logger LOG =
    LoggerFactory.getLogger(PhoenixConnectionManagerImpl.class);
  private static PhoenixConnectionManagerImpl manager;

  @Override
  protected Connection createConnection(PhoenixConfig configs) throws PhoenixException {
    String url = configs.getUrl();
    try {
      return new PhoenixConnection(DriverManager.getConnection(url), ViewActorSystem.get().getPhoenixConnectionActor());
    }catch (Exception e) {
      LOG.error("Error while creating phoenix connection : ", e);
      throw new PhoenixException(String.format("Cannot get connection of url : %s", url));
    }
  }

  public static PhoenixConnectionManagerImpl getInstance() throws ViewException {
    if( null == manager ){
      synchronized (PhoenixConnectionManagerImpl.class){
        if(null == manager){
          manager = new PhoenixConnectionManagerImpl();
          try {
            Class<?> klass2 = Class.forName("org.apache.phoenix.queryserver.client.Driver");
          } catch (ClassNotFoundException e) {
            LOG.error("Cannot register the phoenix Driver", e);
            throw new ViewException(e);
          }
        }
      }
    }

    return manager;
  }
}
