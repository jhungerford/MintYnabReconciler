package dev.budget.reconciler.es;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.io.Closeable;
import java.io.IOException;

public class EmbeddedElasticSearch implements Closeable {

	public static final String CLUSTER_NAME = "mint-ynab-reconciler";

	private Node node;

	public synchronized void start() {
		if (node != null) {
			throw new IllegalStateException("ElasticSearch is already running");
		}

		node =  NodeBuilder.nodeBuilder()
				.clusterName(CLUSTER_NAME)
				.local(true)
				.data(true)
				.loadConfigSettings(false)
				.build();

		node.start();
	}

	public synchronized void stop() {
		if (node == null) {
			throw new IllegalStateException("ElasticSearch isn't running");
		}

		node.stop();
	}

	public void close() throws IOException {
		stop();
	}

	public synchronized Client client() {
		if (node == null) {
			throw new IllegalStateException("ElasticSearch isn't running");
		}

		return node.client();
	}
}
