package dev.budget.reconciler.finagle.filter

import com.twitter.finagle.{Service, Filter}
import com.twitter.io.Charsets
import com.twitter.util.{Await, Future}
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponseStatus, DefaultHttpResponse, HttpResponse}

class PlainResponseFilter extends Filter[HttpRequest, HttpResponse, HttpRequest, String] {

  override def apply(request: HttpRequest, service: Service[HttpRequest, String]): Future[HttpResponse] = {
    service.apply(request).flatMap{ str =>
      val response: DefaultHttpResponse = new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
      response.setContent(ChannelBuffers.copiedBuffer(str, Charsets.Utf8))
      Future.value(response)
    }
  }
}
