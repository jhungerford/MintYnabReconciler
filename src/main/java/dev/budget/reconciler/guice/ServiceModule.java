package dev.budget.reconciler.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.twitter.finagle.Http;
import com.twitter.finagle.Service;
import com.twitter.finagle.ServiceFactory;
import dev.budget.reconciler.finagle.filter.HttpResponseToStringFilter;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class ServiceModule extends AbstractModule {

	protected void configure() {}

	@Provides
	private Service<HttpRequest, String> createHelloClient() {
		ServiceFactory<HttpRequest, String> client = new HttpResponseToStringFilter().andThen(Http.newClient("localhost:8082", "hello"));
		return client.toService();
	}
}
