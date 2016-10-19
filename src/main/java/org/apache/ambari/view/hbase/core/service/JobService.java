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

package org.apache.ambari.view.hbase.core.service;

import com.google.common.base.Optional;
import org.apache.ambari.view.hbase.core.PhoenixException;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.jobs.Job;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.TableJob;
import org.apache.ambari.view.hbase.pojos.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JobService {
  private final static Logger LOG =
    LoggerFactory.getLogger(JobService.class);

  private final ViewServiceFactory factory;

  public JobService(ViewServiceFactory factory){
    this.factory = factory;
  }

  public String submitJob(Job job) throws ServiceException{
    if( job instanceof PhoenixJob )
      return factory.getPhoenixService().submitPhoenixJob((PhoenixJob) job);
    else throw new ServiceException("Illegal Argument");
  }

  public List<Table> getTables(TableJob tableJob) throws ServiceException, ViewException, PhoenixException {
    List<Table> tables = new LinkedList<Table>();
    try {
      try(
        Connection connection = PhoenixConnectionManager.getInstance()
        .getConnection(factory.getConfigurator().getPhoenixConfig())
      ){
        Optional<ResultSet> result = factory.getPhoenixService().submitSyncJob(connection, tableJob);
        if (result.isPresent()) {
          tables.addAll(convertToTable(result.get()));
        } else throw new ServiceException("Tables not found.");
      }
    } catch (SQLException e) {
      LOG.error("Error while closing connection.",e);
    }

    return tables;
  }

  private List<Table> convertToTable(ResultSet resultSet) {
    return new ArrayList<Table>();
  }

  public Job getJob(String id) throws JobNotFoundException, ServiceException{
    return null;
  }

  public List<Job> getJobs() throws ServiceException{
    return null;
  }

}
