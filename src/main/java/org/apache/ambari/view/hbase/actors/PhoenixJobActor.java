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
import org.apache.ambari.view.hbase.jobs.PersistablePhoenixJob;
import org.apache.ambari.view.hbase.jobs.PersistableQueryPhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.GetTablesJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.PartialFunction;

import java.sql.Connection;
import java.sql.ResultSet;

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
      match(PersistableQueryPhoenixJob.class, new FI.UnitApply<PersistableQueryPhoenixJob>() {
        @Override
        public void apply(PersistableQueryPhoenixJob job) throws Exception {
          LOG.info("Persisting : {}", job);
          PersistablePhoenixJob persistedJob = viewServiceFactory.getPhoenixResourceManager().create(job);
          LOG.info("Persisted Object : {}", job);
          PhoenixJobActor.this.sender().tell(persistedJob.getId(), ActorRef.noSender());

          boolean executed = new PhoenixJobHelper().execute(connection, job);
          LOG.info("execution success? :{}", executed);
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
}