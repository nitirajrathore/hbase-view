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

import org.apache.ambari.view.hbase.jobs.IPhoenixJob;
import org.apache.ambari.view.hbase.jobs.Job;

public class GetTablesJob extends Job implements IPhoenixJob {
  private String catalog;
  private String schemaPattern;
  private String tableNamePattern;

  public GetTablesJob() {
  }

  public GetTablesJob(String catalog, String schemaPattern, String tableNamePattern) {
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
  public char[] serializeData() {
    return new char[0];
  }
}
