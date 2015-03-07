package dev.budget.reconciler.finagle.service

import com.twitter.finagle.Service
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpRequest

class HelloService extends Service[HttpRequest, String] {

  override def apply(request: HttpRequest): Future[String] = {
    Future.value("Hello filtered scala finagle!")
  }

}
