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
import org.apache.ambari.view.hbase.jobs.impl.TableJob;
import scala.PartialFunction;

import java.sql.Connection;
import java.sql.ResultSet;

public class PhoenixJobActor extends AbstractActor {

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
      match(TableJob.class, new FI.UnitApply<TableJob>() {
        @Override
        public void apply(TableJob s) throws Exception {
          Optional<ResultSet> resultSet = new PhoenixJobHelper().getTables(connection, (TableJob) s);
          PhoenixJobActor.this.sender().tell(resultSet, ActorRef.noSender());
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