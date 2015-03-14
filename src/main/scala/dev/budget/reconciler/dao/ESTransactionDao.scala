package dev.budget.reconciler.dao

import java.io.IOException

import com.fasterxml.jackson.databind.ObjectMapper
import dev.budget.reconciler.es.ESIndex
import dev.budget.reconciler.model.Transaction
import org.elasticsearch.action.search.{SearchRequestBuilder, SearchResponse}
import org.elasticsearch.client.Client
import org.elasticsearch.common.joda.time.DateTime
import org.elasticsearch.index.query.{FilterBuilders, QueryBuilders}
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import scaldi.{Injectable, Injector}

class ESTransactionDao(implicit val injector: Injector) extends Injectable {
  private val log: Logger = getLogger(getClass)

  private val client: Client = inject [Client]
  private val objectMapper: ObjectMapper = inject [ObjectMapper]

  @throws(classOf[IOException])
  def index[T <: Transaction](index: ESIndex, transaction: T) {
    client.prepareIndex(index.name, index.`type`).setSource(transaction.esJson).execute.actionGet
  }

  @throws(classOf[IOException])
  def allThisMonth[T <: Transaction](index: ESIndex, transactionType: Class[T]): Seq[T] = {
    val startOfMonth: DateTime = DateTime.now.withDayOfMonth(1).withTimeAtStartOfDay
    val endOfMonth: DateTime = DateTime.now.dayOfMonth.withMaximumValue

    val request: SearchRequestBuilder = client
      .prepareSearch(index.name)
      .setTypes(index.`type`)
      .setQuery(QueryBuilders.filteredQuery(
        QueryBuilders.matchAllQuery,
        FilterBuilders.rangeFilter("date").gte(startOfMonth).lte(endOfMonth))
      ).addSort(SortBuilders.fieldSort("date")
      .order(SortOrder.DESC))
      .setSize(1000)

    log.info("allThisMonth query for index {}: {}", index.name, request)

    val response: SearchResponse = request.execute.actionGet

    val hits: Seq[SearchHit] = response.getHits.getHits
    hits.map( hit => objectMapper.readValue(hit.getSourceAsString, transactionType))
  }
}