package org.apache.ambari.view.hbase.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ambari.view.hbase.core.JobStatus;
import org.apache.ambari.view.hbase.core.ViewException;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJob;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixException;
import org.apache.ambari.view.hbase.core.service.internal.PhoenixJobHelper;
import org.apache.ambari.view.hbase.jobs.JobImpl;
import org.apache.ambari.view.hbase.jobs.QueryJob;
import org.apache.ambari.view.hbase.jobs.ResultSetQueryJob;
import org.apache.ambari.view.hbase.jobs.UpdateQueryJob;
import org.apache.ambari.view.hbase.jobs.impl.GetAllSchemasJob;
import org.apache.ambari.view.hbase.jobs.phoenix.AsyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.phoenix.IPhoenixJob;
import org.apache.ambari.view.hbase.jobs.phoenix.ResultableAsyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.phoenix.SyncPhoenixJob;
import org.apache.ambari.view.hbase.jobs.result.Result;
import org.apache.ambari.view.hbase.jobs.result.UpdateResult;
import org.apache.ambari.view.hbase.messages.HoldResultMessage;
import scala.PartialFunction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Slf4j
public class PhoenixJobActor extends AbstractActor {
  private final ActorRef phoenixActor;

  public static Props props(ActorRef phoenixActor) {
    return Props.create(PhoenixJobActor.class, phoenixActor);
  }

  public PhoenixJobActor(ActorRef phoenixActor) {
    this.phoenixActor = phoenixActor;
  }

  public PartialFunction receive() {
    return ReceiveBuilder.

      match(SyncPhoenixJob.class, new FI.UnitApply<SyncPhoenixJob>() {
        @Override
        public void apply(SyncPhoenixJob job) throws Exception {
          log.info("Executing {}", job);
          Connection phoenixConnection = null;
          try {
            phoenixConnection = job.getPhoenixConnection();
            executeJob(job, phoenixConnection);
            Result result = job.getResult();
            phoenixConnection.close();
            PhoenixJobActor.this.sender().tell(result, ActorRef.noSender());
          } catch (Exception e) {
            log.error("exception occurred.", e);
            PhoenixJobActor.this.sender().tell(
              new Status.Failure(new ViewException("exception occurred while executing job : " + e.getMessage(), e)), PhoenixJobActor.this.self()
            );
          } finally {
            if (null != phoenixConnection && !phoenixConnection.isClosed()) {
              try {
                phoenixConnection.close();
              } catch (Exception e) {
                log.error("Error occurred while closing connection. {}", e.getMessage(), e);
              }
            }
          }
        }
      }).

      match(AsyncPhoenixJob.class, new FI.UnitApply<AsyncPhoenixJob>() {
        @Override
        public void apply(AsyncPhoenixJob job) throws Exception {
          log.info("Persisting : {}", job);
          PhoenixJob phoenixJob = createPersistable(job);
          PhoenixJob persistedJob = job.getViewServiceFactory().getPhoenixResourceManager().create(phoenixJob);
          job.setPersistentResource(persistedJob);
          log.info("Persisted Object : {}", job);
          PhoenixJobActor.this.sender().tell(persistedJob.getId(), self());

          Connection phoenixConnection = null;
          JobStatus currStatus = JobStatus.COMPLETED;
          boolean success = true;
          int progress = 0;
          String errorMsg = null;

          try {
             phoenixConnection = job.getPhoenixConnection();
            try {
              executeJob(job, phoenixConnection);
            } catch (Exception e) {
              log.error("Error while executing job : {}", job, e);
              currStatus = JobStatus.ERROR;
              success = false;
              errorMsg = e.getMessage();
              progress = 0;
              phoenixConnection.close();
            }
          } catch (Exception e) {
            log.error("Error while submitting job {}", job, e);
            success = false;
            errorMsg = e.getMessage();
            progress = 0;
            currStatus = JobStatus.FAILED;
          }

          // if success then hold the result
          if (success) {
            if (job instanceof ResultableAsyncPhoenixJob) {
              // hand over to resultSet handler Actor
              phoenixActor.tell(new HoldResultMessage((ResultableAsyncPhoenixJob) job), self());
              currStatus = JobStatus.COMPLETED;
              progress = 100;
            } else {
              phoenixConnection.close();
              progress = 100;
              currStatus = JobStatus.FINISHED;
            }
          }

          // save the job status to db.
          PhoenixJob pj = job.getPersistentResource();
          long duration = System.currentTimeMillis() - pj.getSubmittedDate().getTime();
          pj.setStatus(currStatus.name());
          if( null != errorMsg ){
            pj.setError(errorMsg.getBytes());
          }
          pj.setProgress(progress);
          pj.setDuration(duration);
          PhoenixJob pjob = job.getViewServiceFactory().getPhoenixResourceManager().update(pj);
          log.info("job persisted into db : {}", pjob);
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

  private void executeJob(IPhoenixJob job, Connection phoenixConnection) throws PhoenixException, ViewException, SQLException {
    if (job instanceof GetAllSchemasJob) {
      ResultSet resultSet = new PhoenixJobHelper().getSchemas(phoenixConnection);
      ((GetAllSchemasJob) job).setResultSet(resultSet);// keep connection alive till someone retrives job
    } else if (job instanceof ResultSetQueryJob) {
      ResultSetQueryJob j = (ResultSetQueryJob) job;
      ResultSet resultSet = new PhoenixJobHelper().executeQuery(phoenixConnection, j);
      j.setResultSet(resultSet); // keep connection alive till someone retrives job
    } else if (job instanceof UpdateQueryJob) {
      UpdateQueryJob j = (UpdateQueryJob) job;
      int updateCount = new PhoenixJobHelper().executeUpdate(phoenixConnection, j);
      j.setResult(new UpdateResult(updateCount));
    } else if (job instanceof QueryJob) {
      QueryJob j = (QueryJob) job;
      new PhoenixJobHelper().execute(phoenixConnection, j);
    } else {
      throw new ViewException("Cannot handle this type of job. Job = " + job);
    }
  }

  private PhoenixJob createPersistable(JobImpl job) throws SQLException {
    PhoenixJob phoenixJob = new PhoenixJob();
    phoenixJob.setData(job.serializeData().getBytes());
    phoenixJob.setSubmittedDate(new Date());
    phoenixJob.setOwner(job.getOwner());
    phoenixJob.setJobType(job.getJobType());
    phoenixJob.setStatus(JobStatus.NEW.name());
    return phoenixJob;
  }
}