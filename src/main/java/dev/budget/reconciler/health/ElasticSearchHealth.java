package dev.budget.reconciler.health;

import com.codahale.metrics.health.HealthCheck;
import dev.budget.reconciler.es.ElasticSearchAdmin;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.slf4j.Logger;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

public class ElasticSearchHealth extends HealthCheck {
	private static final Logger log = getLogger(ElasticSearchHealth.class);

	@Inject
	private ElasticSearchAdmin esAdmin;

	protected Result check() throws Exception {
		try {
			ClusterHealthStatus clusterHealth = esAdmin.clusterHealthStatus();

			if (clusterHealth == ClusterHealthStatus.RED) {
				return Result.unhealthy("ES cluster is red");
			}
		} catch (ElasticsearchException e) {
			log.error("Cluster health check failed", e);
			return Result.unhealthy("Cluster health threw an exception (%s)- ES is very unhappy", e.getMessage());
		}

		return Result.healthy();
	}
}
