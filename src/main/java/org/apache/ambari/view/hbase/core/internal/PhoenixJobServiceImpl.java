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

package org.apache.ambari.view.hbase.core.internal;

import com.google.common.base.Optional;
import org.apache.ambari.view.hbase.core.PhoenixException;
import org.apache.ambari.view.hbase.core.PhoenixJobHelper;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.core.persistence.ResourceManager;
import org.apache.ambari.view.hbase.core.service.Configurator;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.TableJob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class PhoenixJobServiceImpl implements PhoenixJobService {

  private final Configurator configurator;
  private ResourceManager<PhoenixJob> phoenixJobResourceManager;

  public PhoenixJobServiceImpl(Configurator configurator, ResourceManager<PhoenixJob> resourceManager){
    this.configurator = configurator;
    this.phoenixJobResourceManager = resourceManager;
  }

  @Override
  public String submitPhoenixJob(PhoenixJob job) throws ServiceException {
    try {
      PhoenixJob pj = phoenixJobResourceManager.create(job);
      return pj.getId();
    } catch (PersistenceException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public PhoenixJob getPhoenixJob(String id) throws ServiceException {
    try {
      return phoenixJobResourceManager.read(id);
    } catch (PersistenceException e) {
      throw new ServiceException( e.getMessage(), e);
    }
  }

  @Override
  public List<PhoenixJob> getPhoenixJobs() throws ServiceException {
    try {
      return phoenixJobResourceManager.readAll();
    } catch (PersistenceException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<ResultSet> submitSyncJob(Connection connection, PhoenixJob job) throws ServiceException, PhoenixException {
    if( job instanceof TableJob)
       return new PhoenixJobHelper().getTables(connection, (TableJob)job);
    else return Optional.absent();
  }
}
