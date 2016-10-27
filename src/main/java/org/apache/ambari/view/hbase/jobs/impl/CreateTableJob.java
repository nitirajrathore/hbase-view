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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.gson.Gson;
import lombok.Data;
import org.apache.ambari.view.hbase.jobs.ExecutablePhoenixJob;
import org.apache.ambari.view.hbase.jobs.QueryJob;
import org.apache.ambari.view.hbase.pojos.ColumnDef;
import org.apache.ambari.view.hbase.pojos.Constraint;
import org.apache.ambari.view.hbase.pojos.TableRef;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CreateTableJob extends ExecutablePhoenixJob implements QueryJob {
  private TableRef tableRef;
  private List<ColumnDef> columnDefs;
  private Constraint constraint;
  private HashMap<String, String> tableOptions;
  private List<String> splitPoints;

  public CreateTableJob() {
    super(null, true);
  }

  @Override
  public char[] serializeData() {
    Map<String, Object> data = new HashMap<>();
    data.put("schemaName", this.getTableRef().getSchemaName());
    data.put("tableName", this.getTableRef().getTableName());
    data.put("columnDefs", this.getColumnDefs());
    Gson gson = new Gson();
    return gson.toJson(data).toCharArray();
  }

  @Override
  public String getQuery() {
    return "" + "CREATE TABLE" +
      tableRef.getQuery() + "(" +
      getColumnDefsQuery() + getConstraint().getQuery() + ")" +
      getTableOptionsQuery() + getSplitPointsQuery();
  }

  private String getSplitPointsQuery() {
    return FluentIterable.from(this.getSplitPoints()).join(Joiner.on(","));
  }

  private String getTableOptionsQuery() {
    return FluentIterable.from(this.getTableOptions().entrySet())
      .transform(new Function<Map.Entry<String, String>, String>() {
        @Override
        public String apply(Map.Entry<String, String> entry) {
          return entry.getKey() + "=" + entry.getValue();
        }
      }).join(Joiner.on(","));
  }

  private String getColumnDefsQuery() {
    return FluentIterable.from(columnDefs).transform(new Function<ColumnDef, String>() {
      @Override
      public String apply(ColumnDef columnDef) {
        return columnDef.getQuery();
      }
    }).join(Joiner.on(","));
  }
}
