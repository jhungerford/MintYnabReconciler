package dev.budget.reconciler.service

import com.twitter.finagle.Service
import com.twitter.io.Charsets
import com.twitter.util.Future
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.{HttpResponseStatus, DefaultHttpResponse, HttpRequest, HttpResponse}

class HelloService extends Service[HttpRequest, HttpResponse] {

  override def apply(request: HttpRequest): Future[HttpResponse] = {
    val response: DefaultHttpResponse = new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)

    response.setContent(ChannelBuffers.copiedBuffer("Hello Scala Finagle!", Charsets.Utf8))

    return Future.value(response)
  }
}
