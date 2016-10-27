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

import org.apache.ambari.view.hbase.jobs.QueryJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PhoenixJobHelper {
  private static final String TABLES = "TABLE";

  public ResultSet getTables(Connection connection, GetTablesJob getTablesJob) throws PhoenixException {
    try {
      ResultSet rs = connection.getMetaData().getTables(getTablesJob.getCatalog(), getTablesJob.getSchemaPattern(),
        getTablesJob.getTableNamePattern(), new String[]{TABLES});
      return rs;
    } catch (SQLException e) {
      throw new PhoenixException(e);
    }
  }

  public ResultSet getSchemas(Connection connection) throws PhoenixException {
    try {
      ResultSet rs = connection.getMetaData().getSchemas();
      return rs;
    } catch (SQLException e) {
      throw new PhoenixException(e);
    }
  }

  public boolean execute(Connection connection, QueryJob job) throws SQLException {
    Statement statement = connection.createStatement();
    return statement.execute(job.getQuery());
  }

  public ResultSet executeQuery(Connection connection, QueryJob job) throws SQLException {
    Statement statement = connection.createStatement();
    return statement.executeQuery(job.getQuery());
  }

}
