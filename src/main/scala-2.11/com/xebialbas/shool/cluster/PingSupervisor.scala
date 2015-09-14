package com.xebialbas.shool.cluster

import akka.actor.{Actor, Props}
import akka.cluster.sharding.{ClusterShardingSettings, ClusterSharding, ShardRegion}
import com.xebialbas.shool.cluster.PingSupervisor.PingMessage

object PingSupervisor {

  case class PingMessage(id: String, payload: Any)

  def props(ids: Seq[String]) = Props(new PingSupervisor(ids))

  val name = "ping-supervisor"
}

class PingSupervisor(ids: Seq[String]) extends Actor {

  var i = 0

  val extractor: ShardRegion.ExtractEntityId = {
    case PingMessage(id, msg) => id -> msg
  }

  val shardResolver: ShardRegion.ExtractShardId = {
    case PingMessage(id, _) => (Math.abs(id.hashCode) % 10 * 2).toString
  }

  val pingRegion = ClusterSharding(context.system).start(
    typeName = "ping",
    entityProps = PingActor.props,
    settings = ClusterShardingSettings(context.system),
    extractEntityId = extractor,
    extractShardId = shardResolver
  )

  override def receive: Receive = {
    case msg: String =>
      pingRegion forward PingMessage(ids(i % ids.size), msg)
      i += 1
  }
}
