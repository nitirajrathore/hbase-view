package org.apache.ambari.view.hbase.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import scala.PartialFunction;

public class PhoenixJobActor extends AbstractActor {
  public PartialFunction receive() {
    return ReceiveBuilder.
      match(PhoenixJob.class, new FI.UnitApply<PhoenixJob>() {
        @Override
        public void apply(PhoenixJob s) throws Exception {
          PhoenixJobActor.this.sender().tell("Pong", ActorRef.
            noSender());
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