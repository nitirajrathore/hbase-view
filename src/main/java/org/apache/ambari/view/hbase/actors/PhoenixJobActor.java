package org.apache.ambari.view.hbase.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixJobHelper;
import org.apache.ambari.view.hbase.jobs.ExecutablePhoenixJob;
import org.apache.ambari.view.hbase.jobs.Job;
import org.apache.ambari.view.hbase.jobs.QueryJob;
import org.apache.ambari.view.hbase.jobs.impl.GetAllSchemasJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;
import scala.PartialFunction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

@Slf4j
public class PhoenixJobActor extends AbstractActor {
  public static Props props() {
    return Props.create(PhoenixJobActor.class);
  }

  public PhoenixJobActor() {
  }

  public PartialFunction receive() {
    return ReceiveBuilder.
      match(GetTablesJob.class, new FI.UnitApply<GetTablesJob>() {
        @Override
        public void apply(GetTablesJob job) throws Exception {
//          Optional<ResultSet> resultSet = new PhoenixJobHelper().getTables(connection, job);
//          PhoenixJobActor.this.sender().tell(resultSet, ActorRef.noSender());
        }
      }).
      match(GetAllSchemasJob.class, new FI.UnitApply<GetAllSchemasJob>() {
        @Override
        public void apply(GetAllSchemasJob job) throws Exception {
          log.info("Executing {}", job);
          Connection phoenixConnection = job.getPhoenixConnection();
          Object result = executeJob(job, phoenixConnection);

          PhoenixJobActor.this.sender().tell(result, ActorRef.noSender());
        }
      }).

      match(ExecutablePhoenixJob.class, new FI.UnitApply<ExecutablePhoenixJob>() {
        @Override
        public void apply(ExecutablePhoenixJob job) throws Exception {
          log.info("Persisting : {}", job);
          if (job.isAsync()) {
            PhoenixJob phoenixJob = createPersistable(job);
            PhoenixJob persistedJob = job.getViewServiceFactory().getPhoenixResourceManager().create(phoenixJob);
            job.setPersistentResource(persistedJob);
            log.info("Persisted Object : {}", job);
            PhoenixJobActor.this.sender().tell(persistedJob.getId(), ActorRef.noSender());
          }

          Connection phoenixConnection = job.getPhoenixConnection();
          if (job instanceof QueryJob) {
            ResultSet executed = new PhoenixJobHelper().executeQuery(phoenixConnection,(QueryJob) job);
            log.info("execution success? :{}", executed);
          }
        }
      }).
      matchAny(new FI.UnitApply<Object>() {
        @Override
        public void apply(Object x) throws Exception {
          PhoenixJobActor.this.sender().tell(
            new Status.Failure(new Exception("unknown message")), PhoenixJobActor.this.self()
          );
        }
      }).
      build();
  }

  private Object executeJob(GetAllSchemasJob job, Connection phoenixConnection) throws PhoenixException, org.apache.ambari.view.hbase.core.ViewException {
    ResultSet resultSet = new PhoenixJobHelper().getSchemas(phoenixConnection);
    job.setResultSet(resultSet);
    return job.getResult();
  }

  private PhoenixJob createPersistable(Job job) {
    PhoenixJob phoenixJob = new PhoenixJob();
    phoenixJob.setData(job.serializeData());
    phoenixJob.setSubmittedDate(new Date());
    phoenixJob.setOwner("some owner");
    phoenixJob.setJobType(job.getJobType());
    return phoenixJob;
  }
}