package com.xebialbas.shool.cluster

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ClusterShardingApp extends App {

  val actorSystem = ActorSystem("ClusterSystem")

  Cluster(actorSystem).registerOnMemberUp {
    val ids = Seq("alpha", "beta", "gamma", "delta", "epsilon", "zeta", "eta", "theta", "iota", "kappa")

    val port = AddressExtension(actorSystem).address.port.get
    val pingSupervisor = actorSystem.actorOf(PingSupervisor.props(ids), PingSupervisor.name)

    1 to 100 foreach { i =>
      implicit val timeout: Timeout = 2.seconds
      val result = pingSupervisor ? s"Msg from $port"
      result.onComplete(println)
      Thread.sleep(1000)
    }
  }
}
