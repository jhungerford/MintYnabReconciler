package dev.budget.reconciler.finagle.filter

import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponse

class IntToHttpResponseFilter[REQUEST] extends Filter[REQUEST, HttpResponse, REQUEST, Integer] {
  override def apply(request: REQUEST, service: Service[REQUEST, Integer]): Future[HttpResponse] = ???
}
