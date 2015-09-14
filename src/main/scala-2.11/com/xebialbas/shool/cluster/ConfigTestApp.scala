package com.xebialbas.shool.cluster

import com.typesafe.config.{Config, ConfigFactory}

object ConfigTestApp extends App {

  private val config: Config = ConfigFactory.load()

  println(config.hasPath("property"))
  println(config.hasPath("namespace.property"))
  println(config.getConfig("namespace").withFallback(ConfigFactory.defaultReference()).hasPath("property"))

}
