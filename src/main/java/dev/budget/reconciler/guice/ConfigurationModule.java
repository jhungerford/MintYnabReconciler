package dev.budget.reconciler.guice;

import com.google.inject.AbstractModule;
import dev.budget.reconciler.config.ReconcilerConfiguration;

public class ConfigurationModule extends AbstractModule {

	private final ReconcilerConfiguration configuration;

	public ConfigurationModule(ReconcilerConfiguration configuration) {
		this.configuration = configuration;
	}

	protected void configure() {
		bind(ReconcilerConfiguration.class).toInstance(configuration);
	}
}
