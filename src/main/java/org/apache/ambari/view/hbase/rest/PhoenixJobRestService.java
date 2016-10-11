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

import org.apache.ambari.view.hbase.core.IServiceFactory;
import org.apache.ambari.view.hbase.core.PhoenixJobService;
import org.apache.ambari.view.hbase.core.ServiceFactory;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.PhoenixJobImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

public class PhoenixJobRestService {
  private final static Logger LOG =
    LoggerFactory.getLogger(PhoenixJobRestService.class);

  @GET
  @Path("/hello")
  public String hello(){
    return "Hello";
  }

  @GET
  @Path("/phoenixJobs")
  public List<PhoenixJob> getPhoenixJobs() {
    LOG.info("getPhoenixJobs Getting serviceFactory..  ");
    List<PhoenixJob> jobs = ServiceFactory.getInstance().getPhoenixJobService().getPhoenixJobs();
    LOG.info("all jobs : " + jobs);
    return jobs;
  }


  @POST
  @Path("/phoenixJob")
  public String submitPhoenixJob(PhoenixJobImpl phoenixJob) {
    LOG.info("getPhoenixJobs Getting serviceFactory..  ");
    IServiceFactory serviceFactory = ServiceFactory.getInstance();
    LOG.info("getPhoenixJobs Got serviceFactory : " + serviceFactory);
    PhoenixJobService phoenixJobService = serviceFactory.getPhoenixJobService();
    LOG.info("PhoenixJobService : {} ", phoenixJobService);

    String id = phoenixJobService.submitPhoenixJob(phoenixJob);
    return id;
  }

  @GET
  @Path("/phoenixJob/{id}")
  public PhoenixJob getPhoenixJob(@PathParam("id") String id) {
    LOG.info("Getting serviceFactory..  ");
    IServiceFactory serviceFactory = ServiceFactory.getInstance();
    LOG.info("Got serviceFactory : " + serviceFactory);
    return null;
  }
}