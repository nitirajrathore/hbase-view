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

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixJobService;
import org.apache.ambari.view.hbase.core.service.internal.ViewServiceFactory;
import org.apache.ambari.view.hbase.jobs.JobImpl;
import org.apache.ambari.view.hbase.jobs.impl.CreateSchemaJob;
import org.apache.ambari.view.hbase.jobs.impl.CreateTableJob;
import org.apache.ambari.view.hbase.jobs.impl.GetAllSchemasJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;
import org.apache.ambari.view.hbase.jobs.phoenix.AsyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.result.GetAllSchemasJobResult;
import org.apache.ambari.view.hbase.jobs.result.Result;
import org.apache.ambari.view.hbase.pojos.PhoenixJobInfo;
import org.apache.ambari.view.hbase.pojos.TableRef;
import org.apache.ambari.view.hbase.pojos.result.Schema;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JobService {
  private final ViewServiceFactory factory;
  private final Context context;

  public JobService(ViewServiceFactory factory, Context context) {
    this.factory = factory;
    this.context = context;
  }

//  public String submitJob(JobInfo job) throws ServiceException {
//    if (job instanceof IPhoenixJob)
//      return factory.getPhoenixService().submitAsyncPhoenixJob((IPhoenixJob) job);
//    else throw new ServiceException("Illegal Argument");
//  }

  public String submitJob(JobImpl job) throws ServiceException {
    if (job instanceof AsyncPhoenixJob)
      return factory.getPhoenixJobService().submitJob((AsyncPhoenixJob)job);
    else throw new ServiceException("Illegal Argument");
  }

  public List<TableRef> getTables(GetTablesJob getTablesJob) throws ServiceException, PhoenixException {
//    List<TableRef> tables = new LinkedList<TableRef>();
//    try {
//      try (
//        Connection connection = PhoenixConnectionManagerImpl.getInstance()
//          .getConnection(factory.getConfigurator().getPhoenixConfig())
//      ) {
//        Optional<ResultSet> result = factory.getPhoenixJobService().submitSyncJob(getTablesJob);
//        if (result.isPresent()) {
//          tables.addAll(convertToTable(result.get()));
//        } else throw new ServiceException("Tables not found.");
//      }
//    } catch (SQLException e) {
//      LOG.error("Error while closing connection.", e);
//    }
//
    return null;
  }

  public List<TableRef> getTablesActor(GetTablesJob getTablesJob) throws ServiceException, PhoenixException {
//    List<TableRef> tables = new LinkedList<TableRef>();
//    try {
//      try (
//        Connection connection = PhoenixConnectionManagerImpl.getInstance()
//          .getConnection(factory.getConfigurator().getPhoenixConfig())
//      ) {
//        ActorRef actorRef = factory.getActorSystem().actorOf(PhoenixJobActor.props(factory));
//        Timeout timeout = new Timeout(Duration.create(5000, "seconds"));
//        Future<Object> future = ask(actorRef, getTablesJob, timeout);
//        try {
//          Optional<ResultSet> optionalResult = (Optional<ResultSet>) Await.result(future, timeout.duration());
//          if (optionalResult.isPresent()) {
//            tables.addAll(convertToTable(optionalResult.get()));
//          } else throw new ServiceException("Tables not found.");
//        } catch (Exception e) {
//          log.error("Error while getting results.", e);
//          throw new ViewException(e);
//        }

//        Future<ResultSet> future = ask(actorRef, getTablesJob, 50000).mapTo(new ClassTag<ResultSet>());
//        Option<Try<Object>> result = future.value();
//        if (result.nonEmpty()) {
//          Try<Object> res = result.get();
//          Optional<ResultSet> optionalResult = (Optional<ResultSet>) res.get();
//          if(optionalResult.isPresent())
//            tables.addAll(convertToTable(optionalResult.get()));
//        } else throw new ServiceException("Tables not found.");
//      }
//    } catch (SQLException e) {
//      LOG.error("Error while closing connection.", e);
//    }

    return null;
  }

  public List<TableRef> createTable(CreateTableJob createTableJob) throws ServiceException, PhoenixException {
//    List<TableRef> tables = new LinkedList<TableRef>();
//    try {
//      try (
//        Connection connection = PhoenixConnectionManagerImpl.getInstance()
//          .getConnection(factory.getConfigurator().getPhoenixConfig())
//      ) {
//        ActorRef actorRef = factory.getActorSystem().actorOf(PhoenixJobActor.props(factory));
//        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
//        Future<Object> future = ask(actorRef, createTableJob, timeout);
//        try {
//          Optional<ResultSet> optionalResult = (Optional<ResultSet>) Await.result(future, timeout.duration());
//          if (optionalResult.isPresent()) {
//            tables.addAll(convertToTable(optionalResult.get()));
//          } else throw new ServiceException("Tables not found.");
//        } catch (Exception e) {
//          LOG.error("Error while getting results.", e);
//          throw new ViewException(e);
//        }
//
////        Future<ResultSet> future = ask(actorRef, getTablesJob, 50000).mapTo(new ClassTag<ResultSet>());
////        Option<Try<Object>> result = future.value();
////        if (result.nonEmpty()) {
////          Try<Object> res = result.get();
////          Optional<ResultSet> optionalResult = (Optional<ResultSet>) res.get();
////          if(optionalResult.isPresent())
////            tables.addAll(convertToTable(optionalResult.get()));
////        } else throw new ServiceException("Tables not found.");
//      }
//    } catch (SQLException e) {
//      LOG.error("Error while closing connection.", e);
//    }
//
//    return tables;

    return null;
  }

  private List<TableRef> convertToTable(ResultSet resultSet) {
    return new ArrayList<TableRef>();
  }

  public PhoenixJobInfo getPhoenixJob(String id) throws JobNotFoundException, ServiceException {
    PhoenixJob job = getPhoenixJobService().getPhoenixJob(id);

    if(null == job) throw new JobNotFoundException(String.format("phoenix job with id %s not found.", id));

    return new PhoenixJobInfo(job);
  }

  public List<PhoenixJobInfo> getPhoenixJobs() throws ServiceException, PersistenceException {
    List<PhoenixJob> jobs = this.factory.getPhoenixResourceManager().readAll();
    ImmutableList<PhoenixJobInfo> pjobs = FluentIterable.from(jobs).transform(new Function<PhoenixJob, PhoenixJobInfo>() {
      @Override
      public PhoenixJobInfo apply(PhoenixJob input) {
        return new PhoenixJobInfo(input);
      }
    }).toList();
    return pjobs;
  }

  public Result getJobResult(String jobId) throws ServiceException {
    return this.getPhoenixJobService().getResult(jobId);
  }

  public List<Schema> getAllSchemas(GetAllSchemasJob getAllSchemasJob) throws ServiceException, PhoenixException {
    getAllSchemasJob.setOwner(this.context.getUser());
    GetAllSchemasJobResult result = this.factory.getPhoenixJobService().executeJob(getAllSchemasJob);
    return result.getSchemas();
  }

  public String createSchema(CreateSchemaJob createSchemaJob) throws ServiceException {
    fillDetails(createSchemaJob);
    return this.factory.getPhoenixJobService().submitJob(createSchemaJob);
  }

  private void fillDetails(CreateSchemaJob createSchemaJob) {
     createSchemaJob.setOwner(this.context.getUser());
  }

  private PhoenixJobService getPhoenixJobService() {
    return this.factory.getPhoenixJobService();
  }

  private ViewServiceFactory getFactory(){
    return factory;
  }
}
