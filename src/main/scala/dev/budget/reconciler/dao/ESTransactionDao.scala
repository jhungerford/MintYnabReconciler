package dev.budget.reconciler.dao

import com.fasterxml.jackson.databind.ObjectMapper
import dev.budget.reconciler.es.{MintESIndex, ESIndex}
import dev.budget.reconciler.model.{MintTransaction, Transaction, YnabTransaction}
import dev.budget.reconciler.util.DateUtil
import org.elasticsearch.action.index.{IndexRequestBuilder, IndexResponse}
import org.elasticsearch.action.search.{SearchRequestBuilder, SearchResponse, SearchType}
import org.elasticsearch.client.Client
import org.elasticsearch.index.query.{FilterBuilders, QueryBuilders}
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.metrics.stats.Stats
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone, LocalDate}
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import scaldi.{Injectable, Injector}

class ESTransactionDao(implicit val injector: Injector) extends Injectable {
  private val log: Logger = getLogger(getClass)

  private implicit val formats = DefaultFormats + LocalDateSerializer

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

  def allForMonth[T <: Transaction](index: ESIndex, year: Int, month: Int)(implicit m: Manifest[T]): Seq[T] = {
    val startOfMonth: DateTime = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC)
    val endOfMonth: DateTime = new DateTime(year, month + 1, 1, 0, 0, DateTimeZone.UTC)

    val request: SearchRequestBuilder = client
      .prepareSearch(index.name)
      .setTypes(index.esType)
      .setQuery(QueryBuilders.filteredQuery(
        QueryBuilders.matchAllQuery,
        FilterBuilders.rangeFilter("date").gte(startOfMonth).lt(endOfMonth))
      ).addSort(SortBuilders.fieldSort("date")
      .order(SortOrder.DESC))
      .setSize(1000)

    log.info(s"allThisMonth query for index ${index.name}: $request")

    val response: SearchResponse = request.execute.actionGet


    val hits: Seq[SearchHit] = response.getHits.getHits
    hits.map { hit => hitToTransaction(hit) }
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

  def closestMintTransaction(ynab: YnabTransaction): Option[MintTransaction] = {
    val amount = if (ynab.outflowCents > 0) ynab.outflowCents else ynab.inflowCents

    val request: SearchRequestBuilder = client
      .prepareSearch(MintESIndex.name)
      .setTypes(MintESIndex.esType)
      .setSize(1) // Only interested in the closest match
      .setQuery(QueryBuilders.boolQuery()
        .should(QueryBuilders.fuzzyQuery("date", ynab.date))
        .should(QueryBuilders.fuzzyQuery("amountCents", amount))
        .should(QueryBuilders.matchQuery("description", ynab.payee))
        .should(QueryBuilders.matchQuery("originalDescription", ynab.payee))
      )

    val response: SearchResponse = request.execute.actionGet

    log.info(s"Closest mint transaction to $ynab: $response")

    response.getHits match {
      case hits if hits.getTotalHits == 0 => None
      case hits if hits.getAt(0).score() < 1.5 => None
      case hits => Some(hitToTransaction[MintTransaction](hits.getAt(0)))
    }
  }

  def hitToTransaction[T <: Transaction](hit: SearchHit)(implicit m: Manifest[T]): T = {
    val json: JValue = JsonMethods.parse(StringInput(hit.getSourceAsString)) merge JObject("id" -> JString(hit.getId))
    json.extract[T]
  }
}

case object LocalDateSerializer extends CustomSerializer[LocalDate](format => ({
  case JString(str) => DateUtil.parse(str)
  case JNull => null
},{
  case date: LocalDate => JString(DateUtil.format(date))
}))
