package dev.budget.reconciler.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.twitter.finagle.Http;
import com.twitter.finagle.Service;
import com.twitter.finagle.ServiceFactory;
import dev.budget.reconciler.finagle.filter.HttpResponseToIntFilter;
import dev.budget.reconciler.finagle.filter.HttpResponseToStringFilter;
import dev.budget.reconciler.finagle.filter.JsonEncodeFilter;
import dev.budget.reconciler.model.MintTransaction;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.util.List;

public class ServiceModule extends AbstractModule {

	protected void configure() {}

	@Provides
	@Named("helloService")
	private Service<HttpRequest, String> createHelloService() {
		ServiceFactory<HttpRequest, String> serviceFactory = new HttpResponseToStringFilter().andThen(Http.newClient("localhost:8082", "hello"));
		return serviceFactory.toService();
	}

	@Provides
	@Named("mintUploadService")
	private Service<List<MintTransaction>, Integer> createMintUploadService() {
		return new JsonEncodeFilter<List<MintTransaction>, Integer>()
				.andThen(new HttpResponseToIntFilter<>())
				.andThen(Http.newClient("localhost:8083", "mintUpload").toService());
	}
}
