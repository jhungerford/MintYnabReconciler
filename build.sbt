scalaVersion := "2.10.4"

val dropwizardVersion = "0.7.1"
val elasticsearchVersion = "1.4.4"
val finagleVersion = "6.24.0"

libraryDependencies += "io.dropwizard" % "dropwizard-core" % dropwizardVersion

libraryDependencies += "io.dropwizard" % "dropwizard-assets" % dropwizardVersion

libraryDependencies += "com.massrelevance" %% "dropwizard-scala" % dropwizardVersion

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.2.1"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.11"

libraryDependencies += "org.json4s" %% "json4s-ext" % "3.2.11"

libraryDependencies += "com.google.guava" % "guava" % "18.0"

libraryDependencies += "org.scaldi" %% "scaldi" % "0.3.2"

libraryDependencies += "org.elasticsearch" % "elasticsearch" % elasticsearchVersion

libraryDependencies += "com.twitter" %% "finagle-http" % finagleVersion

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "io.dropwizard" % "dropwizard-testing" % dropwizardVersion % "test"

libraryDependencies += "org.testng" % "testng" % "6.8.8" % "test"

libraryDependencies += "org.easytesting" % "fest-assert-core" % "2.0M10" % "test"

resolvers += Resolver.sonatypeRepo("public")
