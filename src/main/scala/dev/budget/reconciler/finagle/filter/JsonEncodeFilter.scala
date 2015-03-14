package dev.budget.reconciler.finagle.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.finagle.http.RequestBuilder
import com.twitter.finagle.{Filter, Service}
import com.twitter.io.Charsets
import com.twitter.util.Future
import org.jboss.netty.buffer.{ChannelBuffer, ChannelBuffers}
import org.jboss.netty.handler.codec.http.HttpRequest

class JsonEncodeFilter[REQUEST, RESPONSE] extends Filter[REQUEST, RESPONSE, HttpRequest, RESPONSE] {
  override def apply(request: REQUEST, service: Service[HttpRequest, RESPONSE]): Future[RESPONSE] = {
    val mapper: ObjectMapper = new ObjectMapper()
    val json: String = mapper.writeValueAsString(request)

    val buffer: ChannelBuffer = ChannelBuffers.copiedBuffer(json, Charsets.Utf8)
    val post: HttpRequest = RequestBuilder.create().url("localhost:8083").buildPost(buffer)

    service.apply(post)
  }
}
