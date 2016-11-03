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
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.jobs.impl.CreateSchemaJob;
import org.apache.ambari.view.hbase.jobs.impl.GetAllSchemasJob;
import org.apache.ambari.view.hbase.pojos.result.Schema;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@Path("/schemas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SchemaRestService extends BaseRestService{

  @GET
  public List<Schema> getAllSchemas(){
    try {
      return getJobService().getAllSchemas(new GetAllSchemasJob());
    } catch (ViewException | ServiceException | PhoenixException e) {
      log.error("Exceptoin while getting all schemas.", e);
      throw new WebApplicationException(e);
    }
  }

  @PUT
  public String createSchema(CreateSchemaJob createSchemaJob){
    try {
      return getJobService().createSchema(createSchemaJob);
    } catch (ViewException | ServiceException e) {
      log.error("Exceptoin while getting all schemas.", e);
      throw new WebApplicationException(e);
    }
  }

//  @POST
//  @Path("/{schemaName}")
//  public String createSchema(@PathParam("schemaName") String schemaName, Schema schema){
//
//  }

//
//  @GET
//  @Path("/{schemaName}")
//  public Schema getSchema(@PathParam("schemaName") String schemaName){
//
//  }
//
//  @DELETE
//  @Path("/{schemaName}")
//  public void deleteSchema(@PathParam("schemaName") String schemaName){
//
//  }

}
