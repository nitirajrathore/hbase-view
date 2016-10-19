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

import org.apache.ambari.view.hbase.core.internal.PhoenixJobService;
import org.apache.ambari.view.hbase.core.internal.PhoenixJobServiceImpl;
import org.apache.ambari.view.hbase.core.persistence.ResourceManager;
import org.apache.ambari.view.hbase.core.persistence.Storage;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.PhoenixJobImpl;

public class ViewServiceFactory {
  private final PhoenixJobService phoenixJobService;
  private Configurator configurator;
  private Storage storage;

  public ViewServiceFactory(Configurator configurator, Storage storage) {
    this.configurator = configurator;
    this.storage = storage;
    this.phoenixJobService = new PhoenixJobServiceImpl(configurator, new ResourceManager<PhoenixJob>( PhoenixJobImpl.class, storage));
  }

  public PhoenixJobService getPhoenixService(){
    return this.phoenixJobService;
  }

  public Configurator getConfigurator() {
    return configurator;
  }
}
