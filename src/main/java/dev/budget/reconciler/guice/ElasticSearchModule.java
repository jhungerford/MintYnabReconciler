package dev.budget.reconciler.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class ElasticSearchModule extends AbstractModule {

	protected void configure() {}

	@Provides
	@Singleton
	private Node createNode() {
		// Create a local node that persists data to 'data/' between restarts
		return NodeBuilder.nodeBuilder()
				.clusterName("mint-ynab-reconciler")
				.local(true)
				.data(true)
				.loadConfigSettings(false)
				.build();
	}

	@Provides
	@Singleton
	private Client createClient(Node node) {
		return node.client();
	}
}
