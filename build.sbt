import sbtassembly.Plugin.AssemblyKeys._

scalaVersion := "2.10.4"

val dropwizardVersion = "0.7.1"
val elasticsearchVersion = "1.4.4"
val finagleVersion = "6.24.0"

libraryDependencies += "io.dropwizard" % "dropwizard-core" % dropwizardVersion

libraryDependencies += "io.dropwizard" % "dropwizard-assets" % dropwizardVersion

libraryDependencies += "net.sf.supercsv" % "super-csv" % "2.3.0"

libraryDependencies += "com.google.guava" % "guava" % "18.0"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

libraryDependencies += "org.elasticsearch" % "elasticsearch" % elasticsearchVersion

libraryDependencies += "com.twitter" %% "finagle-http" % finagleVersion

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "io.dropwizard" % "dropwizard-testing" % dropwizardVersion % "test"

libraryDependencies += "org.testng" % "testng" % "6.8.8" % "test"

libraryDependencies += "org.easytesting" % "fest-assert-core" % "2.0M10" % "test"

resolvers += Resolver.sonatypeRepo("public")
