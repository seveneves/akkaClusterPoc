package com.xebialbas.shool.cluster

import akka.actor.{Actor, Props}
import akka.cluster.sharding.{ClusterShardingSettings, ClusterSharding, ShardRegion}
import com.xebialbas.shool.cluster.PingSupervisor.PingMessage

object PingSupervisor {

  case class PingMessage(id: String, payload: Any)

  val props = Props(new PingSupervisor)

  val name = "ping-supervisor"

}

class PingSupervisor extends Actor {

  val extractor: ShardRegion.ExtractEntityId = {
    case PingMessage(id, msg) => id -> msg
  }

  val shardResolver: ShardRegion.ExtractShardId = {
    case PingMessage(id, msg) => id(0).toString
  }

  val pingRegion = ClusterSharding(context.system).start(
    typeName = "ping",
    entityProps = PingActor.props,
    settings = ClusterShardingSettings(context.system),
    extractEntityId = extractor,
    extractShardId = shardResolver
  )

  override def receive: Receive = {
    case msg: String => pingRegion forward PingMessage(msg, msg)
  }
}
