package dev.budget.reconciler.api;

import com.twitter.finagle.Http;
import com.twitter.io.Charsets;
import com.twitter.util.Future;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.slf4j.LoggerFactory.getLogger;

@Path("/v1/hello")
public class HelloResource {
	private static final Logger log = getLogger(HelloResource.class);

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getHello() {
		Future<HttpResponse> httpResponseFuture = Http.fetchUrl("http://localhost:8082");

		HttpResponse httpResponse = httpResponseFuture.get();
		return httpResponse.getContent().toString(Charsets.Utf8());
	}
}
