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

package org.apache.ambari.view.hbase.core.service.impl;

import org.apache.ambari.view.hbase.core.internal.ViewActorSystem;
import org.apache.ambari.view.hbase.core.persistence.DatabaseServiceFactory;
import org.apache.ambari.view.hbase.core.service.Configurator;
import org.apache.ambari.view.hbase.core.service.IServiceFactory;
import org.apache.ambari.view.hbase.core.service.JobService;
import org.apache.ambari.view.hbase.core.service.ViewServiceFactory;

import java.util.Properties;

public class StandAloneServiceFactory implements IServiceFactory {
  private Configurator configurator;
  private Properties viewProperties;
  private JobService jobService;
  DatabaseServiceFactory dsf = DatabaseServiceFactory.getInstance();
  public StandAloneServiceFactory(Properties viewProperties){
    this.viewProperties = viewProperties;
    configurator = new ConfiguratorImpl(viewProperties);
    jobService = new JobService(new ViewServiceFactory(configurator, new StorageImpl(dsf.getEntityManagerFactory()), ViewActorSystem.get()));
  }

  @Override
  public JobService getJobService() {
    return jobService;
  }

}
