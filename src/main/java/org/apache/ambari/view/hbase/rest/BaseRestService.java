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

package org.apache.ambari.view.hbase.rest;

import com.google.inject.Inject;
import org.apache.ambari.view.ViewContext;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.service.IServiceFactory;
import org.apache.ambari.view.hbase.core.service.JobService;
import org.apache.ambari.view.hbase.core.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseRestService {
  private final static Logger LOG =
    LoggerFactory.getLogger(BaseRestService.class);

  @Inject
  protected ViewContext viewContext;

  protected IServiceFactory getServerFactory() throws ViewException {
    LOG.info("Creating serviceFactory..  ");
    return new ServiceFactory(viewContext).getInstance();
  }

  protected JobService getJobService() throws ViewException {
    return getServerFactory().getJobService();
  }
}
