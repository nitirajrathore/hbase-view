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

package org.apache.ambari.view.hbase.core.service.internal;

import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.core.service.JobNotFoundException;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.jobs.phoenix.AsyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.phoenix.SyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.result.Result;
import org.apache.ambari.view.hbase.jobs.result.ResultSetResult;

import java.util.List;

public interface PhoenixJobService {
  String submitJob(AsyncPhoenixJob job) throws ServiceException;

  PhoenixJob getPhoenixJob(String id) throws JobNotFoundException, ServiceException;

  Result getResult(String id) throws ServiceException;

  List<PhoenixJob> getPhoenixJobs() throws ServiceException;

  <T extends ResultSetResult<T>> T executeJob(SyncPhoenixJob<T> job) throws ServiceException;
}
