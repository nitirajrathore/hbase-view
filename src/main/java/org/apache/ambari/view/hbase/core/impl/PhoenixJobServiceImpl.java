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

package org.apache.ambari.view.hbase.core.impl;

import org.apache.ambari.view.hbase.core.PhoenixJobService;
import org.apache.ambari.view.hbase.core.persistence.FilteringStrategy;
import org.apache.ambari.view.hbase.core.persistence.IResourceManager;
import org.apache.ambari.view.hbase.core.persistence.Indexed;
import org.apache.ambari.view.hbase.core.persistence.ItemNotFound;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;

import java.util.List;

public class PhoenixJobServiceImpl implements PhoenixJobService {

  private IResourceManager<PhoenixJob> phoenixJobIResourceManager;

  public PhoenixJobServiceImpl(IResourceManager<PhoenixJob> resourceManager){
    this.phoenixJobIResourceManager = resourceManager;
  }

  @Override
  public String submitPhoenixJob(PhoenixJob job){
    PhoenixJob pj = phoenixJobIResourceManager.create(job);
    return pj.getId();
  }

  @Override
  public PhoenixJob getPhoenixJob(String id) throws ItemNotFound {
    return phoenixJobIResourceManager.read(id);
  }

  @Override
  public List<PhoenixJob> getPhoenixJobs() {
    return phoenixJobIResourceManager.readAll(new FilteringStrategy() {
      @Override
      public boolean isConform(Indexed item) {
        return true;
      }

      @Override
      public String whereStatement() {
        return "1=1";
      }
    });
  }

//  public PhoenixJob updatePhoenixJob(PhoenixJob phoenixJob){
//    return null;
//  }
}
