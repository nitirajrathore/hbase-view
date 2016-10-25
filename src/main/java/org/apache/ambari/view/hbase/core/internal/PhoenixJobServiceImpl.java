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

import akka.actor.ActorRef;
import akka.util.Timeout;
import com.google.common.base.Optional;
import org.apache.ambari.view.hbase.actors.PhoenixJobActor;
import org.apache.ambari.view.hbase.core.PhoenixException;
import org.apache.ambari.view.hbase.core.PhoenixJobHelper;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.persistence.PersistenceException;
import org.apache.ambari.view.hbase.core.service.PhoenixConnectionManager;
import org.apache.ambari.view.hbase.core.service.ServiceException;
import org.apache.ambari.view.hbase.core.service.ViewServiceFactory;
import org.apache.ambari.view.hbase.jobs.PersistablePhoenixJob;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static akka.pattern.Patterns.ask;

public class PhoenixJobServiceImpl implements PhoenixJobService {
  private final static Logger LOG =
    LoggerFactory.getLogger(PhoenixJobServiceImpl.class);

  private final ViewServiceFactory factory;

  public PhoenixJobServiceImpl(ViewServiceFactory factory) {
    this.factory = factory;
  }

  @Override
  public String submitPhoenixJob(PhoenixJob job) throws ServiceException {
    try {
      if (job instanceof PersistablePhoenixJob) {
        try (
          Connection connection = PhoenixConnectionManager.getInstance()
            .getConnection(this.factory.getConfigurator().getPhoenixConfig())
        ) {
          ActorRef actorRef = this.factory.getActorSystem().actorOf(PhoenixJobActor.props(factory, connection));
          Timeout timeout = new Timeout(Duration.create(5000, "seconds"));
          Future<Object> future = ask(actorRef, job, timeout);
          String result = (String) Await.result(future, timeout.duration());
          return result;
        } catch (SQLException | PhoenixException | ViewException e) {
          LOG.error("error : ", e);
          throw new ServiceException(e);
        }
      }
      throw new ServiceException("Should not have come here..");
    } catch (Exception e) {
      LOG.error("Exception : ", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PhoenixJob getPhoenixJob(String id) throws ServiceException {
    try {
      return this.factory.getPhoenixResourceManager().read(id);
    } catch (PersistenceException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }

  @Override
  public List<PhoenixJob> getPhoenixJobs() throws ServiceException {
//    try {
//      return phoenixJobResourceManager.readAll();
//    } catch (PersistenceException e) {
//      throw new ServiceException(e);
//    }

    return null;
  }

  @Override
  public Optional<ResultSet> submitSyncJob(Connection connection, PhoenixJob job) throws
    ServiceException, PhoenixException {
    if (job instanceof GetTablesJob)
      return new PhoenixJobHelper().getTables(connection, (GetTablesJob) job);
    else return Optional.absent();
  }
}
