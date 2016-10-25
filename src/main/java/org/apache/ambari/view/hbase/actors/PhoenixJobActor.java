package org.apache.ambari.view.hbase.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import com.google.common.base.Optional;
import org.apache.ambari.view.hbase.core.PhoenixJobHelper;
import org.apache.ambari.view.hbase.core.service.ViewServiceFactory;
import org.apache.ambari.view.hbase.jobs.Job;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.QueryJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.PartialFunction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

public class PhoenixJobActor extends AbstractActor {
  private final static Logger LOG =
    LoggerFactory.getLogger(PhoenixJobActor.class);

  private final Connection connection;
  private final ViewServiceFactory viewServiceFactory;

  public static Props props(ViewServiceFactory viewServiceFactory, Connection connection) {
    return Props.create(PhoenixJobActor.class, viewServiceFactory, connection);
  }

  public PhoenixJobActor(ViewServiceFactory viewServiceFactory, Connection connection){
    this.viewServiceFactory = viewServiceFactory;
    this.connection = connection;
  }

  public PartialFunction receive() {
    return ReceiveBuilder.
      match(GetTablesJob.class, new FI.UnitApply<GetTablesJob>() {
        @Override
        public void apply(GetTablesJob job) throws Exception {
          Optional<ResultSet> resultSet = new PhoenixJobHelper().getTables(connection, job);
          PhoenixJobActor.this.sender().tell(resultSet, ActorRef.noSender());
        }
      }).
      match(Job.class, new FI.UnitApply<Job>() {
        @Override
        public void apply(Job job) throws Exception {
          LOG.info("Persisting : {}", job);
          if( job.isAsync() ){
            PhoenixJob phoenixJob = createPersistable(job);
            PhoenixJob persistedJob = viewServiceFactory.getPhoenixResourceManager().create(phoenixJob);
            LOG.info("Persisted Object : {}", job);
            PhoenixJobActor.this.sender().tell(persistedJob.getId(), ActorRef.noSender());
          }

          if( job instanceof QueryJob){
            boolean executed = new PhoenixJobHelper().execute(connection, (QueryJob)job);
            LOG.info("execution success? :{}", executed);
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

  private PhoenixJob createPersistable(Job job) {
    PhoenixJob phoenixJob = new PhoenixJob();
    phoenixJob.setData(job.serializeData());
    phoenixJob.setSubmittedDate(new Date());
    phoenixJob.setOwner("some owner");
    phoenixJob.setJobType(job.getJobType());
    return phoenixJob;
  }
}