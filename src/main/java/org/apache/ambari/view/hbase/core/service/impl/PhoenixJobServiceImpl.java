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
import org.apache.ambari.view.hbase.Constants;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixJobService;
import org.apache.ambari.view.hbase.core.service.internal.ViewServiceFactory;
import org.apache.ambari.view.hbase.jobs.phoenix.AsyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.phoenix.SyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.result.Result;
import org.apache.ambari.view.hbase.jobs.result.ResultSetResult;
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
  public String submitJob(AsyncPhoenixJob job) throws ServiceException {
    log.info("Executing job : {}", job);
    job.setViewServiceFactory(this.getViewServiceFactory());
    ActorRef actorRef = this.getViewServiceFactory().getActorSystem().getPhoenixActor();
    Timeout timeout = new Timeout(Duration.create(Constants.JOB_SUBMISSION_TIMEOUT_SECS, "seconds"));
    Future<Object> future = ask(actorRef, job, timeout);
    try {
      return (String) Await.result(future, timeout.duration());
    } catch (Exception e) {
      throw new ServiceException("Exception while getting result from backend :" + e.getMessage(), e);
    }
  }

  @Override
  public PhoenixJob getPhoenixJob(String id) throws ServiceException {
    try {
      return this.getViewServiceFactory().getPhoenixResourceManager().read(id);
    } catch (PersistenceException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }

  @Override
  public Result getResult(String id) throws ServiceException {
    return null;
  }

  @Override
  public List<PhoenixJob> getPhoenixJobs() throws ServiceException {
    try {
      return this.getViewServiceFactory().getPhoenixResourceManager().readAll();
    } catch (PersistenceException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public <T extends ResultSetResult<T>> T executeJob(SyncPhoenixJob<T> job) throws ServiceException {
    log.info("Executing job : {}", job);
    job.setViewServiceFactory(this.getViewServiceFactory());
    ActorRef actorRef = this.getViewServiceFactory().getActorSystem().getPhoenixActor();
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
}
