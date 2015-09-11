package com.xebialbas.shool.cluster

import akka.actor._

import scala.util.Random

object PingActor {
  val name = "ping"
  val props = Props(new PingActor)
}

class PingActor extends Actor with ActorLogging {

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    import concurrent.duration._
//    context.setReceiveTimeout(1.seconds)
    println(s"I live on $address with ${self.path}")
  }

  def address = AddressExtension(context.system).address

  override def receive: Receive = {
    case ReceiveTimeout =>
      println(s"Killing ${self.path}")
      self ! PoisonPill
    case msg: String =>
      val sleep = new Random().nextInt(1000) + 1000
      println(s"Sleeping $sleep")
      Thread.sleep(sleep)
      sender() ! s"Executing $msg on $address and path ${self.path}"
  }
}
