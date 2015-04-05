package dev.budget.reconciler

import dev.budget.reconciler.modules.{TestApplicationModule, JsonModule, ElasticSearchModule, ApplicationModule}
import scaldi.Injectable

object TestMain {
  def main(args: Array[String]) = {
    implicit val mainInjector = new ApplicationModule :: new ElasticSearchModule :: new JsonModule :: new TestApplicationModule
    val application: TestApplication = Injectable.inject [TestApplication]

    application.run(args)
  }
}
