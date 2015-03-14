package dev.budget.reconciler

import dev.budget.reconciler.modules.{JsonModule, ApplicationModule, ElasticSearchModule}
import scaldi.Injectable

object Main {

  def main(args: Array[String]) {
//    val helloService = new HelloService
//    val plainResponseFilter = new StringToHttpResponseFilter
//
//    val httpHelloService: Service[HttpRequest, HttpResponse] = plainResponseFilter andThen helloService
//
//    ServerBuilder.safeBuild(httpHelloService,
//      ServerBuilder.get().name("hello").codec(Http.get()).bindTo(new InetSocketAddress(8082)))

    implicit val mainInjector = new ApplicationModule :: new ElasticSearchModule :: new JsonModule
    val application: ReconcilerApplication = Injectable.inject [ReconcilerApplication]

    application.run(args)
  }
}
