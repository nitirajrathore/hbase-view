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

package org.apache.ambari.view.hbase.jobs;

import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.jobs.result.Result;

import java.sql.Connection;
import java.sql.ResultSet;

public class ExecutablePhoenixJob<T extends Result<T>> extends Job<T, PhoenixJob> implements IPhoenixJob {
  private Connection phoenixConnection;
  private ResultSet resultSet;

  protected ExecutablePhoenixJob(T result, boolean isAsync) {
    super(result, isAsync);
  }

  @Override
  public T getResult() throws ViewException {
    return this.getResultObject().populateFromResultSet(resultSet);
  }

  @Override
  public void setResultSet(ResultSet rs){
    this.resultSet = rs;
  }
  /**
   * Lazily creates and saves new connection.
   *
   * @return
   */
  @Override
  public synchronized Connection getPhoenixConnection() throws PhoenixException {
    if (null == this.phoenixConnection) {
      this.phoenixConnection = this.getViewServiceFactory().getPhoenixConnectionManager()
        .getConnection(this.getViewServiceFactory().getConfigurator().getPhoenixConfig());
    }

    return this.phoenixConnection;
  }
}
