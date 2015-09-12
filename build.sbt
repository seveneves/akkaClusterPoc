name := "akka-cluster-poc"

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.0-RC2"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion
libraryDependencies += "org.iq80.leveldb"            % "leveldb"          % "0.7"
libraryDependencies += "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8"
