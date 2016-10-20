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
import org.apache.ambari.view.hbase.core.service.JobNotFoundException;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public interface PhoenixJobService {
  String submitPhoenixJob(PhoenixJob job) throws ServiceException;

  PhoenixJob getPhoenixJob(String id) throws JobNotFoundException, ServiceException;

  List<PhoenixJob> getPhoenixJobs() throws ServiceException;

  Optional<ResultSet> submitSyncJob(Connection connection, PhoenixJob job) throws ServiceException, PhoenixException;
}