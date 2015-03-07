import sbt._
import sbt.Keys._

object MintYnabReconcilerBuild extends Build {

  scalaVersion := "2.10.4"

  lazy val mintYnabReconciler = Project(
    id = "MintYnabReconciler",
    base = file("."))
  .settings(
    name := "MintYnabReconciler",
    version := "0.0.1",
    parallelExecution in Test := false)
}
