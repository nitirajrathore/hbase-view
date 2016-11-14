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

import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.jobs.QueryJob;
import org.apache.ambari.view.hbase.jobs.ResultSetQueryJob;
import org.apache.ambari.view.hbase.jobs.UpdateQueryJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class PhoenixJobHelper {
  private static final String TABLES = "TABLE";

  public ResultSet getTables(Connection connection, GetTablesJob getTablesJob) throws PhoenixException {
    try {
      ResultSet rs = connection.getMetaData().getTables(getTablesJob.getCatalog(), getTablesJob.getSchemaPattern(),
        getTablesJob.getTableNamePattern(), new String[]{TABLES});
      return rs;
    } catch (SQLException e) {
      log.error("Exception occurred while getting Tables.", e);
      throw new PhoenixException(e);
    }
  }

  public ResultSet getSchemas(Connection connection) throws PhoenixException {
    try {
      ResultSet rs = connection.getMetaData().getSchemas();
      return rs;
    } catch (SQLException e) {
      log.error("Exception occurred while getting Schemas.", e);
      throw new PhoenixException(e);
    }
  }

  public int executeUpdate(Connection connection, UpdateQueryJob job) throws PhoenixException {
    try {
      Statement statement = connection.createStatement();
      job.setStatement(statement);
      return statement.executeUpdate(job.getQuery());
    } catch (SQLException e) {
      log.error("Exception occurred while executing job : {}", job, e);
      throw new PhoenixException(e);
    }
  }

  public boolean execute(Connection connection, QueryJob job) throws PhoenixException {
    try {
      Statement statement = connection.createStatement();
      job.setStatement(statement);
      return statement.execute(job.getQuery());
    } catch (SQLException e) {
      log.error("Exception occurred while executing job {}", job, e);
      throw new PhoenixException(e);
    }
  }

  public ResultSet executeQuery(Connection connection, ResultSetQueryJob job) throws PhoenixException {
    try {
      Statement statement = connection.createStatement();
      job.setStatement(statement);
      ResultSet resultSet = statement.executeQuery(job.getQuery());
      job.setResultSet(resultSet);
      return resultSet;
    } catch (SQLException e) {
      log.error("Exception occurred while executing job {}", job, e);
      throw new PhoenixException(e);
    }
  }

}
