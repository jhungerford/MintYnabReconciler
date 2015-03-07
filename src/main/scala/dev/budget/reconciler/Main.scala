package dev.budget.reconciler

import java.net.InetSocketAddress

import com.twitter.finagle.{Filter, Service}
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.Http
import dev.budget.reconciler.finagle.filter.PlainResponseFilter
import dev.budget.reconciler.guice.ElasticSearchModule
import dev.budget.reconciler.finagle.service.HelloService
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

object Main {

  def main(args: Array[String]) {
    val helloService = new HelloService
    val plainResponseFilter = new PlainResponseFilter

    val httpHelloService: Service[HttpRequest, HttpResponse] = plainResponseFilter andThen helloService

    ServerBuilder.safeBuild(httpHelloService,
      ServerBuilder.get().name("hello").codec(Http.get()).bindTo(new InetSocketAddress(8082)))

    new ReconcilerApplication(new ElasticSearchModule).run(args)
  }
}
