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

import org.apache.ambari.view.hbase.core.PhoenixException;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.jobs.impl.AlterTableJob;
import org.apache.ambari.view.hbase.jobs.impl.CreateTableJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;
import org.apache.ambari.view.hbase.pojos.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tables")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DDLRestService extends BaseRestService{

  private final static Logger LOG =
    LoggerFactory.getLogger(DDLRestService.class);

  @GET
  @Path("/")
  public List<Table> getAllTables(){
    try {
//      return getJobService().getTables(new GetTablesJob(null, null, null));
      return getJobService().getTablesActor(new GetTablesJob(null,null,null));
    } catch (ServiceException | ViewException | PhoenixException e) {
      LOG.error("Error while getting tables.", e);
      throw new WebApplicationException(e);
    }
  }

  @POST
  @Path("/")
  public String createTable(CreateTableJob createTableJob){
    try {
      return getJobService().submitJob(createTableJob);
    } catch (ServiceException | ViewException e) {
      LOG.error("Error while getting tables.", e);
      throw new WebApplicationException(e);
    }
  }

  @POST
  @Path("/alter")
  public String alterTable(AlterTableJob alterTableJob){
    try {
      return getJobService().submitJob(alterTableJob);
    } catch (ServiceException | ViewException e) {
      LOG.error("Error while getting tables.", e);
      throw new WebApplicationException(e);
    }
  }

}
