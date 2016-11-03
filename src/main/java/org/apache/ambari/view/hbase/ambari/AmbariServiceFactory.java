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

package org.apache.ambari.view.hbase.ambari;

import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.service.IServiceFactory;
import org.apache.ambari.view.hbase.core.service.JobService;
import org.apache.ambari.view.hbase.core.service.internal.ViewServiceFactory;

import java.util.Properties;

public class AmbariServiceFactory implements IServiceFactory {

  private final AmbariConfigurator configurator;
  private final AmbariStorage storage;
  private ViewContext viewContext = null;
  private Properties properties = null;
  private JobService jobService;

  public AmbariServiceFactory(ViewContext viewContext, Properties properties) throws ViewException {
    this.viewContext = viewContext;
    this.configurator = new AmbariConfigurator(viewContext, properties);
    this.storage = new AmbariStorage(viewContext);
    this.properties = properties;
    this.jobService = new JobService(new ViewServiceFactory(configurator, storage, AmbariPhoenixConnectionManager.getInstance()), new AmbariContext(viewContext));
  }

  @Override
  public JobService getJobService() {
    return jobService;
  }
}
