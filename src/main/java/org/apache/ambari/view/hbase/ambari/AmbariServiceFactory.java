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
import org.apache.ambari.view.hbase.core.AmbariConfig;
import org.apache.ambari.view.hbase.core.DatabaseConfig;
import org.apache.ambari.view.hbase.core.HbaseConfig;
import org.apache.ambari.view.hbase.core.IServiceFactory;
import org.apache.ambari.view.hbase.core.PhoenixConfig;
import org.apache.ambari.view.hbase.core.PhoenixJobService;
import org.apache.ambari.view.hbase.core.impl.PhoenixJobServiceImpl;

public class AmbariServiceFactory implements IServiceFactory {

  private ViewContext viewContext = null;
  private PhoenixJobResourceManager phoenixJobResourceManager;
  private PhoenixJobService phoenixJobService;
  public AmbariServiceFactory(ViewContext viewContext){
    this.viewContext = viewContext;
    phoenixJobResourceManager = new PhoenixJobResourceManager(viewContext);
    phoenixJobService = new PhoenixJobServiceImpl(phoenixJobResourceManager);
  }
  @Override
  public DatabaseConfig getDatabaseConfig() {
    return null; // no op as in ambari database is managed by Ambari
  }

  @Override
  public PhoenixConfig getPhoenixConfig() {
    return null;
  }

  @Override
  public HbaseConfig getHbaseConfig() {
    return null;
  }

  @Override
  public AmbariConfig getAmbariConfig() {
    return null;
  }

  @Override
  public PhoenixJobService getPhoenixJobService() {
    return phoenixJobService;
  }
}