package dev.budget.reconciler.finagle.filter

import com.twitter.finagle.{Filter, Service}
import com.twitter.io.Charsets
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponse

class HttpResponseToIntFilter[REQUEST] extends Filter[REQUEST, Integer, REQUEST, HttpResponse] {
  override def apply(request: REQUEST, service: Service[REQUEST, HttpResponse]): Future[Integer] = {
    service.apply(request).flatMap{ httpResponse =>
      val responseBody: String = httpResponse.getContent.toString(Charsets.Utf8)
      Future.value(Integer.valueOf(responseBody)) // TODO: return a promise instead of Future.value?
    }
  }
}
