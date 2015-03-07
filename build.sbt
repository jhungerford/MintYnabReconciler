import sbtassembly.Plugin.AssemblyKeys._

scalaVersion := "2.10.4"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.7"

resolvers += Resolver.sonatypeRepo("public")
