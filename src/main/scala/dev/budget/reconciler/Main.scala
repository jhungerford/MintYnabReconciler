package dev.budget.reconciler

import dev.budget.reconciler.modules.{JsonModule, ApplicationModule, ElasticSearchModule}
import scaldi.Injectable

object Main {

  def main(args: Array[String]) {
    implicit val mainInjector = new ApplicationModule :: new ElasticSearchModule :: new JsonModule
    val application: ReconcilerApplication = Injectable.inject [ReconcilerApplication]

    application.run(args)
  }
}
