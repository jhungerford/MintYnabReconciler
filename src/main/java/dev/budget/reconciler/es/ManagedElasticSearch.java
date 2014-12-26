package dev.budget.reconciler.es;

import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class ManagedElasticSearch implements Managed {
	private static final Logger log = getLogger(ManagedElasticSearch.class);

	@Inject
	private Node node;

	@Inject
	private ElasticSearchAdmin esAdmin;

	public void start() throws Exception {
		log.info("Starting ElasticSearch...");
		node.start();

		for (ESIndex index : ESIndex.values()) {
			esAdmin.createIndexIfNotExists(index);
		}

		log.info("Started ElasticSearch.");
	}

	public void stop() throws Exception {
		log.info("Stopping ElasticSearch...");
		node.stop();
		log.info("ElasticSearch stopped.");
	}
}
