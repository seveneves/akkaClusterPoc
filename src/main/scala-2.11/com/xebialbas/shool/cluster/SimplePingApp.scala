package com.xebialbas.shool.cluster

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.routing.{RandomPool, BroadcastPool, RoundRobinPool}
import akka.util.Timeout
import akka.pattern.ask
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration._

object SimplePingApp extends App {

  val actorSystem = ActorSystem("ClusterSystem")

  Cluster(actorSystem).registerOnMemberUp {
    val pigners = actorSystem.actorOf(ClusterRouterPool(RandomPool(3), ClusterRouterPoolSettings(
      totalInstances = 3,
      maxInstancesPerNode = 1,
      allowLocalRoutees = false,
      useRole = None
    )).props(PingActor.props), PingActor.name)

    1 to 100 foreach { n =>
      implicit val timeout: Timeout = 12.seconds
      val p = AddressExtension(actorSystem).address.port
      val result = pigners ? s"Who are you $n [$p]?"
      result.onComplete(println)
      Thread.sleep(1000)
    }
  }
}
