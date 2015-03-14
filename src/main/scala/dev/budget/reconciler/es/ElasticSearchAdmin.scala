package dev.budget.reconciler.es

import java.io.IOException
import java.nio.charset.Charset

import com.google.common.io.Resources
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse
import org.elasticsearch.client.Client
import org.elasticsearch.index.query.QueryBuilders
import org.slf4j.Logger
import org.slf4j.LoggerFactory._
import scaldi.{Injector, Injectable}

class ElasticSearchAdmin(implicit val injector: Injector) extends Injectable {
  private val log: Logger = getLogger(getClass)

  private val client: Client = inject [Client]

  @throws(classOf[IOException])
  def createIndexIfNotExists(index: ESIndex) {
    if (!indexExists(index.name)) {
      log.info("Creating ES index '{}' from {}", index.name, index.indexFileName)
      val source: String = Resources.toString(Resources.getResource(index.indexFileName), Charset.defaultCharset)
      client.admin.indices.prepareCreate(index.name).setSource(source).execute.actionGet
      log.info("ES index {} created", index.name)
    } else {
      log.debug("Index {} already exists", index.name)
    }
  }

  def indexExists(name: String): Boolean = {
    client.admin.indices.prepareExists(name).execute.actionGet.isExists
  }

  def clusterHealthStatus: ClusterHealthStatus = {
    val clusterStatsResponse: ClusterStatsResponse = client.admin.cluster.prepareClusterStats.execute.actionGet
    clusterStatsResponse.getStatus
  }

  @throws(classOf[IOException])
  def clearIndex(index: ESIndex) {
    client.prepareDeleteByQuery(index.name).setQuery(QueryBuilders.matchAllQuery).execute.actionGet
  }

  @throws(classOf[IOException])
  def deleteIndex(index: ESIndex) {
    if (indexExists(index.name)) {
      log.info("Deleting ES index '{}'", index.name)
      client.admin.indices.prepareDelete(index.name).execute.actionGet
    }
  }

  def flush(index: ESIndex) {
    client.admin.indices.prepareFlush(index.name).execute.actionGet
  }
}
