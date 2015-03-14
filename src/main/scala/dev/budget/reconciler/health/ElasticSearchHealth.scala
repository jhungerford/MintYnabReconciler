package dev.budget.reconciler.health

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import dev.budget.reconciler.es.ElasticSearchAdmin
import org.elasticsearch.ElasticsearchException
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import scaldi.{Injectable, Injector}

class ElasticSearchHealth(implicit val injector: Injector) extends HealthCheck with Injectable {
  private val log: Logger = getLogger(getClass)

  private val esAdmin: ElasticSearchAdmin = inject [ElasticSearchAdmin]

  override def check(): Result = {
    try {
      esAdmin.clusterHealthStatus match {
        case ClusterHealthStatus.RED =>
          log.warn("ES cluster is red")
          Result.unhealthy("ES cluster is red")
        case _ => Result.healthy
      }
    } catch {
      case e: ElasticsearchException => {
        log.error("Cluster health check failed", e)
        Result.unhealthy("Cluster health threw an exception (%s)- ES is very unhappy", e.getMessage)
      }
    }
  }
}
