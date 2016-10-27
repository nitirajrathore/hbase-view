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

package org.apache.ambari.view.hbase.core.service.impl;

import akka.actor.ActorRef;
import akka.util.Timeout;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixJobService;
import org.apache.ambari.view.hbase.core.service.internal.ViewServiceFactory;
import org.apache.ambari.view.hbase.jobs.IPhoenixJob;
import org.apache.ambari.view.hbase.jobs.Job;
import org.apache.ambari.view.hbase.jobs.result.Result;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;

import static akka.pattern.Patterns.ask;

@Slf4j
public class PhoenixJobServiceImpl implements PhoenixJobService {
  private final ViewServiceFactory viewServiceFactory;

  public PhoenixJobServiceImpl(ViewServiceFactory ViewServiceFactory) {
    this.viewServiceFactory = ViewServiceFactory;
  }

  private ViewServiceFactory getViewServiceFactory() {
    return this.viewServiceFactory;
  }

  @Override
  public String submitAsyncPhoenixJob(Job job) throws ServiceException {
//    try {
//      if (job instanceof IPhoenixJob) {
//        try (
//          Connection connection = PhoenixConnectionManagerImpl.getInstance()
//            .getConnection(this.factory.getConfigurator().getPhoenixConfig())
//        ) {
//          ActorRef actorRef = this.factory.getActorSystem().actorOf(PhoenixJobActor.props(factory));
//          Timeout timeout = new Timeout(Duration.create(5000, "seconds"));
//          Future<Object> future = ask(actorRef, job, timeout);
//          String result = (String) Await.result(future, timeout.duration());
//          return result;
//        } catch (SQLException | PhoenixException | ViewException e) {
//          log.error("error : ", e);
//          throw new ServiceException(e);
//        }
//      }
//      throw new ServiceException("Should not have come here..");
//    } catch (Exception e) {
//      log.error("Exception : ", e);
//      throw new ServiceException(e);
//    }
    return null;
  }

  @Override
  public IPhoenixJob getPhoenixJob(String id) throws ServiceException {
//    try {
//      return this.factory.getPhoenixResourceManager().read(id);
//    } catch (PersistenceException e) {
//      throw new ServiceException(e.getMessage(), e);
//    }
    return null;
  }

  @Override
  public List<IPhoenixJob> getPhoenixJobs() throws ServiceException {
//    try {
//      return phoenixJobResourceManager.readAll();
//    } catch (PersistenceException e) {
//      throw new ServiceException(e);
//    }

    return null;
  }

  @Override
  public <T extends Result<T>> T submitSyncJob(Job<T> job) throws ServiceException {
    log.info("Executing job : {}", job);
    job.setViewServiceFactory(this.getViewServiceFactory());
    ActorRef actorRef = this.getViewServiceFactory().getActorSystem().getPhoenixJobActor();
    Timeout timeout = new Timeout(Duration.create(5000, "seconds"));
    Future<Object> future = ask(actorRef, job, timeout);
    T result = null;
    try {
      result = (T) Await.result(future, timeout.duration());
    } catch (Exception e) {
      throw new ServiceException("Exception while getting result from backend :" + e.getMessage(), e);
    }
    return result;
  }

//
//  @Override
//  <T extends Result<T>> public T submitSyncJob(Connection connection, Job<T> job) throws
//    ServiceException, PhoenixException {
//    if (job instanceof GetTablesJob)
//      return new PhoenixJobHelper().getTables(connection, (GetTablesJob) job);
//    else return Optional.absent();
//  }

//  @Override
//  public Optional<ResultSet> submitSyncJob(Connection connection, Job job) throws
//    ServiceException, PhoenixException {
//    if (job instanceof GetTablesJob)
//      return new PhoenixJobHelper().getTables(connection, (GetTablesJob) job);
//    else return Optional.absent();
//  }
}
