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

import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.core.service.JobNotFoundException;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.jobs.result.Result;
import org.apache.ambari.view.hbase.pojos.PhoenixJobInfo;
import org.apache.commons.lang.NotImplementedException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@Path("/phoenixJobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PhoenixJobRestService extends BaseRestService {

  @GET
  @Path("/")
  public List<PhoenixJobInfo> getPhoenixJobs() throws ServiceException, PersistenceException {
    log.info("getPhoenixJobs Getting serviceFactory..  ");
    List<PhoenixJobInfo> jobs = getServerFactory().getJobService().getPhoenixJobs();
    log.info("all jobs : " + jobs);
    return jobs;
  }


  @POST
  @Path("/")
  public String submitPhoenixJob(PhoenixJob phoenixJob) {
    throw new NotImplementedException("Submission of generic phoenixJob not implemented.");
  }

  @GET
  @Path("/{id}")
  public Response getPhoenixJob(@PathParam("id") String id) {
    try {
      return Response.ok(this.getJobService().getPhoenixJob(id)).build();
    } catch (JobNotFoundException e) {
      log.error("Error while getting jobId : {}.", id, e);
      throw new ViewException(e, Response.Status.NOT_FOUND);
    } catch (ServiceException e) {
      log.error("Error while getting jobId : {}.", id, e);
      throw new ViewException(e);
    }
  }

  @GET
  @Path("/{id}/result")
  public Response getPhoenixJobResult(@PathParam("id") String id){
    log.info("Getting result for jobId : {}", id);
    try {
      Result result = this.getJobService().getJobResult(id);
      log.debug("Returning result : {}", result);
      return Response.ok(result).build();
    } catch (ServiceException e) {
      log.error("Error while getting result for jobId : {}.", id, e);
      throw new ViewException(e);
    }
  }
}
