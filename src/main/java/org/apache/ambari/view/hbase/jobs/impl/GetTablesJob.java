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
import org.apache.ambari.view.hbase.jobs.phoenix.ResultableSyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.result.GetTablesJobResult;

import java.util.HashMap;
import java.util.Map;

public class GetTablesJob extends ResultableSyncPhoenixJob<GetTablesJobResult> {
  private String catalog;
  private String schemaPattern;
  private String tableNamePattern;

  public GetTablesJob() {
    super(new GetTablesJobResult());
  }

  public GetTablesJob(String catalog, String schemaPattern, String tableNamePattern) {
    super(new GetTablesJobResult());
    this.catalog = catalog;
    this.schemaPattern = schemaPattern;
    this.tableNamePattern = tableNamePattern;
  }

  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public String getSchemaPattern() {
    return schemaPattern;
  }

  public void setSchemaPattern(String schemaPattern) {
    this.schemaPattern = schemaPattern;
  }

  public String getTableNamePattern() {
    return tableNamePattern;
  }

  public void setTableNamePattern(String tableNamePattern) {
    this.tableNamePattern = tableNamePattern;
  }

  @Override
  public String serializeData() {
    Gson gson = new Gson();
    Map<String, Object> map = new HashMap<>();
    map.put("catalog", this.catalog);
    map.put("schemaPattern", this.schemaPattern);
    map.put("tableNamePattern", this.tableNamePattern);
    return gson.toJson(map);
  }

  @Override
  public GetTablesJobResult getResult() {
    return this.getResultObject().populateFromResultSet(this.getResultSet());
  }
}
