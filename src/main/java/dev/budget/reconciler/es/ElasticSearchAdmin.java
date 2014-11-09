package dev.budget.reconciler.es;

import com.google.common.io.Resources;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.slf4j.LoggerFactory.getLogger;

public class ElasticSearchAdmin {
	private static final Logger log = getLogger(ElasticSearchAdmin.class);

	private final Client client;

	public ElasticSearchAdmin(Client client) {
		this.client = client;
	}

	public void createIndexIfNotExists(ESIndex index) throws IOException {
		if (! indexExists(index.name)) {
			log.info("Creating ES index '{}' from {}", index.name, index.fileName);

			String source = Resources.toString(Resources.getResource(index.fileName), Charset.defaultCharset());
			client.admin().indices().prepareCreate(index.name).setSource(source).execute().actionGet();
			log.info("ES index {} created", index.name);
		} else {
			log.debug("Index {} already exists", index.name);
		}
	}

	public boolean indexExists(String name) {
		return client.admin().indices().prepareExists(name).execute().actionGet().isExists();
	}

	public ClusterHealthStatus clusterHealthStatus() {
		ClusterStatsResponse clusterStatsResponse = client.admin().cluster().prepareClusterStats().execute().actionGet();
		return clusterStatsResponse.getStatus();
	}
}
