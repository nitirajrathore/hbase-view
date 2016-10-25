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

import akka.actor.ActorRef;
import akka.util.Timeout;
import com.google.common.base.Optional;
import org.apache.ambari.view.hbase.actors.PhoenixJobActor;
import org.apache.ambari.view.hbase.core.PhoenixException;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.jobs.IPhoenixJob;
import org.apache.ambari.view.hbase.jobs.Job;
import org.apache.ambari.view.hbase.jobs.JobInfo;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.CreateTableJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;
import org.apache.ambari.view.hbase.pojos.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static akka.pattern.Patterns.ask;

public class JobService {
  private final static Logger LOG =
    LoggerFactory.getLogger(JobService.class);

  private final ViewServiceFactory factory;

  public JobService(ViewServiceFactory factory) {
    this.factory = factory;
  }

//  public String submitJob(JobInfo job) throws ServiceException {
//    if (job instanceof IPhoenixJob)
//      return factory.getPhoenixService().submitPhoenixJob((IPhoenixJob) job);
//    else throw new ServiceException("Illegal Argument");
//  }

  public String submitJob(Job job) throws ServiceException {
    if (job instanceof IPhoenixJob)
      return factory.getPhoenixService().submitPhoenixJob(job);
    else throw new ServiceException("Illegal Argument");
  }

  public List<Table> getTables(GetTablesJob getTablesJob) throws ServiceException, ViewException, PhoenixException {
    List<Table> tables = new LinkedList<Table>();
    try {
      try (
        Connection connection = PhoenixConnectionManager.getInstance()
          .getConnection(factory.getConfigurator().getPhoenixConfig())
      ) {
        Optional<ResultSet> result = factory.getPhoenixService().submitSyncJob(connection, getTablesJob);
        if (result.isPresent()) {
          tables.addAll(convertToTable(result.get()));
        } else throw new ServiceException("Tables not found.");
      }
    } catch (SQLException e) {
      LOG.error("Error while closing connection.", e);
    }

    return tables;
  }

  public List<Table> getTablesActor(GetTablesJob getTablesJob) throws ServiceException, ViewException, PhoenixException {
    List<Table> tables = new LinkedList<Table>();
    try {
      try (
        Connection connection = PhoenixConnectionManager.getInstance()
          .getConnection(factory.getConfigurator().getPhoenixConfig())
      ) {
        ActorRef actorRef = factory.getActorSystem().actorOf(PhoenixJobActor.props(factory, connection));
        Timeout timeout = new Timeout(Duration.create(5000, "seconds"));
        Future<Object> future = ask(actorRef, getTablesJob, timeout);
        try {
          Optional<ResultSet> optionalResult = (Optional<ResultSet>) Await.result(future, timeout.duration());
          if (optionalResult.isPresent()) {
            tables.addAll(convertToTable(optionalResult.get()));
          } else throw new ServiceException("Tables not found.");
        } catch (Exception e) {
          LOG.error("Error while getting results.", e);
          throw new ViewException(e);
        }

//        Future<ResultSet> future = ask(actorRef, getTablesJob, 50000).mapTo(new ClassTag<ResultSet>());
//        Option<Try<Object>> result = future.value();
//        if (result.nonEmpty()) {
//          Try<Object> res = result.get();
//          Optional<ResultSet> optionalResult = (Optional<ResultSet>) res.get();
//          if(optionalResult.isPresent())
//            tables.addAll(convertToTable(optionalResult.get()));
//        } else throw new ServiceException("Tables not found.");
      }
    } catch (SQLException e) {
      LOG.error("Error while closing connection.", e);
    }

    return tables;
  }

  public List<Table> createTable(CreateTableJob createTableJob) throws ServiceException, ViewException, PhoenixException {
    List<Table> tables = new LinkedList<Table>();
    try {
      try (
        Connection connection = PhoenixConnectionManager.getInstance()
          .getConnection(factory.getConfigurator().getPhoenixConfig())
      ) {
        ActorRef actorRef = factory.getActorSystem().actorOf(PhoenixJobActor.props(factory, connection));
        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
        Future<Object> future = ask(actorRef, createTableJob, timeout);
        try {
          Optional<ResultSet> optionalResult = (Optional<ResultSet>) Await.result(future, timeout.duration());
          if (optionalResult.isPresent()) {
            tables.addAll(convertToTable(optionalResult.get()));
          } else throw new ServiceException("Tables not found.");
        } catch (Exception e) {
          LOG.error("Error while getting results.", e);
          throw new ViewException(e);
        }

//        Future<ResultSet> future = ask(actorRef, getTablesJob, 50000).mapTo(new ClassTag<ResultSet>());
//        Option<Try<Object>> result = future.value();
//        if (result.nonEmpty()) {
//          Try<Object> res = result.get();
//          Optional<ResultSet> optionalResult = (Optional<ResultSet>) res.get();
//          if(optionalResult.isPresent())
//            tables.addAll(convertToTable(optionalResult.get()));
//        } else throw new ServiceException("Tables not found.");
      }
    } catch (SQLException e) {
      LOG.error("Error while closing connection.", e);
    }

    return tables;
  }

  private List<Table> convertToTable(ResultSet resultSet) {
    return new ArrayList<Table>();
  }

  public JobInfo getJob(String id) throws JobNotFoundException, ServiceException {
    return null;
  }

  public List<JobInfo> getJobs() throws ServiceException, PersistenceException {
    List<PhoenixJob> jobs = this.factory.getPhoenixResourceManager().readAll();
    return new ArrayList<JobInfo>(jobs);
  }

}
