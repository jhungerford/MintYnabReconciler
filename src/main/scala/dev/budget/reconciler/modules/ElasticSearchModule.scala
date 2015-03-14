package dev.budget.reconciler.modules

import dev.budget.reconciler.dao.ESTransactionDao
import dev.budget.reconciler.es.{ElasticSearchAdmin, ManagedElasticSearch}
import dev.budget.reconciler.health.ElasticSearchHealth
import org.elasticsearch.client.Client
import org.elasticsearch.node.{Node, NodeBuilder}
import scaldi.Module

class ElasticSearchModule extends Module {

  bind [Node] to {
    NodeBuilder.nodeBuilder()
      .clusterName("mint-ynab-reconciler")
      .local(true)
      .data(true)
      .loadConfigSettings(false)
      .build()
  }

  bind [Client] to inject[Node].client()

  bind [ManagedElasticSearch] to new ManagedElasticSearch
  bind [ElasticSearchAdmin] to new ElasticSearchAdmin
  bind [ElasticSearchHealth] to new ElasticSearchHealth

  bind [ESTransactionDao] to new ESTransactionDao
}
