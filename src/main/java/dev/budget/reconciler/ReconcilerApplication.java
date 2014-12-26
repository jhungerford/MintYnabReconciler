package dev.budget.reconciler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import dev.budget.reconciler.api.HelloResource;
import dev.budget.reconciler.api.TransactionsResource;
import dev.budget.reconciler.config.ReconcilerConfiguration;
import dev.budget.reconciler.guice.ConfigurationModule;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ReconcilerApplication extends Application<ReconcilerConfiguration> {
	private static final Logger log = getLogger(ReconcilerApplication.class);

	/**
	 * List of API classes.
	 *
	 * ADD NEW API RESOURCE CLASSES HERE
	 */
	private static final Class<?>[] RESOURCE_CLASSES = {
			HelloResource.class,
			TransactionsResource.class
	};

	private final Module[] modules;

	public ReconcilerApplication(Module... modules) {
		this.modules = modules;
	}

	public void initialize(Bootstrap<ReconcilerConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle("/web", "/web", "", "web"));
		bootstrap.addBundle(new AssetsBundle("/html", "", "index.html", "html"));
	}

	public void run(ReconcilerConfiguration configuration, Environment environment) throws Exception {
		List<Module> allModules = new ArrayList<>(Arrays.asList(modules));
		allModules.add(new ConfigurationModule(configuration));

		Injector injector = Guice.createInjector(allModules);

		environment.jersey().setUrlPattern("/api/*");

		for (Class<?> resourceClass : RESOURCE_CLASSES) {
			environment.jersey().register(injector.getInstance(resourceClass));
		}
	}

	public static void main(String[] args) {
		try {
			new ReconcilerApplication().run(args);
		} catch (Throwable t) {
			log.error("Fatal exception in ReconcilerApplication", t);
		}
	}
}
