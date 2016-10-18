package org.apache.ambari.view.hbase.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Status;
import akka.japi.pf.FI;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;

public class JavaPongActor extends AbstractActor {
  public PartialFunction receive() {
    return ReceiveBuilder.
      matchEquals("Ping", new FI.UnitApply<String>() {
        @Override
        public void apply(String s) throws Exception {
          JavaPongActor.this.sender().tell("Pong", ActorRef.
            noSender());
        }
      }).
      matchAny(new FI.UnitApply<Object>() {
        @Override
        public void apply(Object x) throws Exception {
          JavaPongActor.this.sender().tell(
            new Status.Failure(new Exception("unknown message")), JavaPongActor.this.self()
          );
        }
      }).
      build();
  }
}