package dev.budget.reconciler.finagle.filter

import com.twitter.finagle.{Service, Filter}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpRequest

class JsonDecodeFilter[DECODED_REQUEST, RESPONSE] extends Filter[HttpRequest, RESPONSE, DECODED_REQUEST, RESPONSE] {
  override def apply(request: HttpRequest, service: Service[DECODED_REQUEST, RESPONSE]): Future[RESPONSE] = ???
}
