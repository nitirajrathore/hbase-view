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

package org.apache.ambari.view.hbase.jobs.impl;

import com.google.gson.Gson;
import org.apache.ambari.view.hbase.jobs.IPhoenixJob;
import org.apache.ambari.view.hbase.jobs.Job;
import org.apache.ambari.view.hbase.jobs.QueryJob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlterTableJob extends Job implements QueryJob, IPhoenixJob{
  transient private String schemaName;
  transient private String tableName;
//  transient private List<String> columnDefinitions;

  public AlterTableJob() {
  }

  public AlterTableJob(String schemaName, String tableName, List<String> columnDefinitions) {
    this.schemaName = schemaName;
    this.tableName = tableName;
//    this.columnDefinitions = columnDefinitions;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

//  public List<String> getColumnDefinitions() {
//    return columnDefinitions;
//  }
//
//  public void setColumnDefinitions(List<String> columnDefinitions) {
//    this.columnDefinitions = columnDefinitions;
//  }

  public String getSchemaName() {
    return schemaName;
  }

  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  @Override
  public char[] serializeData() {
    Map<String,Object> data = new HashMap<>();
    data.put("schemaName", schemaName);
    data.put("tableName", tableName);
//    data.put("columnDefinitions", columnDefinitions);
    Gson gson = new Gson();
    return gson.toJson(data).toCharArray();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CreateTableJob{");
    sb.append("schemaName='").append(schemaName).append('\'');
    sb.append("tableName='").append(tableName).append('\'');
//    sb.append(", columnDefinitions=").append(columnDefinitions);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public String getQuery() {
    return "Query String";
  }

  @Override
  public void setQuery(String query) {

  }
}
