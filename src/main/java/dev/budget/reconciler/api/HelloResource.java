package dev.budget.reconciler.api;

import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.slf4j.LoggerFactory.getLogger;

@Path("/v1/hello")
public class HelloResource {
	private static final Logger log = getLogger(HelloResource.class);

//	@Inject
//	@Named("helloService")
//	private Service<HttpRequest, String> helloService;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getHello() {
		// TODO: helloService should already know it's location.
//		HttpRequest request = RequestBuilder.create().url("http://localhost:8082").withoutContent(HttpMethod.GET);
//		return helloService.apply(request).get();

		return "Hello scaldi";
	}
}
