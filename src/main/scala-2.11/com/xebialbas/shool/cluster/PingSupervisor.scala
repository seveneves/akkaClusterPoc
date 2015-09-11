package com.xebialbas.shool.cluster

import akka.actor.{Actor, Props}
import akka.contrib.pattern.{ClusterSharding, ShardRegion}
import com.xebialbas.shool.cluster.PingSupervisor.PingMessage

object PingSupervisor {

  case class PingMessage(id: String, payload: Any)

  val props = Props(new PingSupervisor)

  val name = "ping-supervisor"

}

class PingSupervisor extends Actor {

  val extractor: ShardRegion.IdExtractor = {
    case PingMessage(id, msg) => id -> msg
  }

  val shardResolver: ShardRegion.ShardResolver = {
    case PingMessage(id, msg) => id(0).toString
  }

  val pingRegion = ClusterSharding(context.system).start(
    typeName = "ping",
    entryProps = Option(PingActor.props),
    idExtractor = extractor,
    shardResolver = shardResolver
  )

  override def receive: Receive = {
    case msg: String => pingRegion forward PingMessage(msg, msg)
  }
}
