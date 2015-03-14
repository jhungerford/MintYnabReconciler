package dev.budget.reconciler.es

import io.dropwizard.lifecycle.Managed
import org.elasticsearch.node.Node
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import scaldi.{Injector, Injectable}

class ManagedElasticSearch(implicit val injector: Injector) extends Managed with Injectable {
  val log: Logger = getLogger(getClass)

  val node: Node = inject [Node]
  val esAdmin: ElasticSearchAdmin = inject [ElasticSearchAdmin]

  override def start() = {
    log.info("Starting ElasticSearch...")
    node.start()

    esAdmin.createIndexIfNotExists(MintESIndex)
    esAdmin.createIndexIfNotExists(YnabESIndex)

    log.info("Started ElasticSearch")
  }

  override def stop() = {
    log.info("Stopping ElasticSearch...")
    node.stop()
    log.info("ElasticSearch stopped")
  }
}
