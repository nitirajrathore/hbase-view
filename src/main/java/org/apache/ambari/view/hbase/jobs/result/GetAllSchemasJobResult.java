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

package org.apache.ambari.view.hbase.jobs.result;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.ViewException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Data
public class GetAllSchemasJobResult implements Result<GetAllSchemasJobResult> {
  private List<String> schemas = new LinkedList<>();

  @Override
  public GetAllSchemasJobResult populateFromResultSet(ResultSet rs) throws ViewException {
    if (null == rs) return this;
    try {
      while (rs.next()) {
        this.schemas.add(rs.getString("TABLE_SCHEM"));
      }
    } catch (SQLException e) {
      throw new ViewException("Exception while reading result : " + e.getMessage(), e);
    }
    return this;
  }
}