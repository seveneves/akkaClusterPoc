package com.xebialbas.shool.cluster

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.RoundRobinPool
import akka.util.Timeout
import akka.pattern.ask
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration._

object SimplePingApp extends App {

  val actorSystem = ActorSystem("ClusterSystem")

  Cluster(actorSystem).registerOnMemberUp {
    val pigners = actorSystem.actorOf(ClusterRouterPool(RoundRobinPool(3), ClusterRouterPoolSettings(
      totalInstances = 3,
      maxInstancesPerNode = 1,
      allowLocalRoutees = true,
      useRole = None
    )).props(PingActor.props), PingActor.name)

    1 to 100 foreach { _ =>
      implicit val timeout: Timeout = 2.seconds
      val result = pigners ? "Who are you?"
      result.onComplete(println)
      Thread.sleep(1000)
    }
  }
}
