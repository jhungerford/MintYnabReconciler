package dev.budget.reconciler.service;

import com.twitter.finagle.Service;
import com.twitter.io.Charsets;
import com.twitter.util.Future;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class HelloService extends Service<HttpRequest, HttpResponse> {

	public Future<HttpResponse> apply(HttpRequest request) {
		DefaultHttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);

		response.setContent(ChannelBuffers.copiedBuffer("Hello Finagle!", Charsets.Utf8()));

		return Future.value(response);
	}
}
