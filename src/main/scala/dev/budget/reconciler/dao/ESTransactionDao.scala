package dev.budget.reconciler.dao

import java.io.IOException

import com.fasterxml.jackson.databind.ObjectMapper
import dev.budget.reconciler.es.ESIndex
import dev.budget.reconciler.model.Transaction
import org.elasticsearch.action.index.{IndexRequestBuilder, IndexResponse}
import org.elasticsearch.action.search.{SearchRequestBuilder, SearchResponse, SearchType}
import org.elasticsearch.client.Client
import org.elasticsearch.index.query.{FilterBuilders, QueryBuilders}
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.range.date.DateRange
import org.elasticsearch.search.aggregations.metrics.stats.Stats
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}
import org.joda.time.{LocalDate, DateTime}
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import scaldi.{Injectable, Injector}

class ESTransactionDao(implicit val injector: Injector) extends Injectable {
  private val log: Logger = getLogger(getClass)

  private val client: Client = inject [Client]
  private val objectMapper: ObjectMapper = inject [ObjectMapper]

  def index[T <: Transaction](index: ESIndex, transaction: T): Boolean = {
    transaction.esJson match {
      case None => false
      case Some(json) =>
        val request: IndexRequestBuilder = client.prepareIndex(index.name, index.esType).setSource(json)
        val response: IndexResponse = request.execute.actionGet
        response.isCreated
    }
  }

  @throws(classOf[IOException])
  def allThisMonth[T <: Transaction](index: ESIndex, transactionType: Class[T]): Seq[T] = {
    val startOfMonth: DateTime = DateTime.now.withDayOfMonth(1).withTimeAtStartOfDay
    val endOfMonth: DateTime = DateTime.now.dayOfMonth.withMaximumValue

    val request: SearchRequestBuilder = client
      .prepareSearch(index.name)
      .setTypes(index.esType)
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

  def dateRange(index: ESIndex): (LocalDate, LocalDate) = {
    val request: SearchRequestBuilder = client
      .prepareSearch(index.name)
      .setTypes(index.esType)
      .setSearchType(SearchType.COUNT)
      .addAggregation(AggregationBuilders.stats("date").field("date"))

    val response: SearchResponse = request.execute.actionGet

    val dateStats: Stats = response.getAggregations.get("date")

    (new LocalDate(dateStats.getMin.toLong), new LocalDate(dateStats.getMax.toLong))
  }
}
