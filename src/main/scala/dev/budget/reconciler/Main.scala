package dev.budget.reconciler

import java.net.InetSocketAddress

import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.Http
import dev.budget.reconciler.guice.ElasticSearchModule
import dev.budget.reconciler.service.HelloService

object Main {

  def main(args: Array[String]) {
    ServerBuilder.safeBuild(new HelloService,
      ServerBuilder.get().name("hello").codec(Http.get()).bindTo(new InetSocketAddress(8082)))

    new ReconcilerApplication(new ElasticSearchModule).run(args)
  }
}
