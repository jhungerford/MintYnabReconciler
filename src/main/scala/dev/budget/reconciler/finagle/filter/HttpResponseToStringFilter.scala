package dev.budget.reconciler.finagle.filter

import com.twitter.finagle.{Filter, Service}
import com.twitter.io.Charsets
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

class HttpResponseToStringFilter extends Filter[HttpRequest, String, HttpRequest, HttpResponse] {
  override def apply(request: HttpRequest, service: Service[HttpRequest, HttpResponse]): Future[String] = {
    service.apply(request).flatMap{ httpResponse =>
      Future.value(httpResponse.getContent.toString(Charsets.Utf8)) // TODO: return a promise instead of Future.value?
    }
  }
}
