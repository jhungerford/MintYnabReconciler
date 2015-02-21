package dev.budget.reconciler.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import dev.budget.reconciler.es.ESIndex;
import dev.budget.reconciler.model.Transaction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ESTransactionDao {
	private static final Logger log = getLogger(ESTransactionDao.class);

	@Inject
	private Client client;
	@Inject
	private ObjectMapper objectMapper;

	public <T extends Transaction> void index(ESIndex index, T transaction) throws IOException {
		client.prepareIndex(index.name, index.type).setSource(transaction.esJson()).execute().actionGet();
	}

	public <T extends Transaction> List<T> allThisMonth(ESIndex index, Class<T> transactionType) throws IOException {
		DateTime startOfMonth = DateTime.now().withDayOfMonth(1).withTimeAtStartOfDay();
		DateTime endOfMonth = DateTime.now().dayOfMonth().withMaximumValue(); // TODO: time?

		SearchRequestBuilder request = client.prepareSearch(index.name)
				.setTypes(index.type)
				.setQuery(QueryBuilders.filteredQuery(
						QueryBuilders.matchAllQuery(),
						FilterBuilders.rangeFilter("date")
								.gte(startOfMonth)
								.lte(endOfMonth)
				))
				.addSort(SortBuilders.fieldSort("date").order(SortOrder.DESC))
				.setSize(1000); // TODO: don't hardcode date and results size

		log.info("allThisMonth query for index {}: {}", index.name, request);

		SearchResponse response = request.execute().actionGet();

		List<T> transactions = new ArrayList<>(response.getHits().hits().length);

		for (SearchHit hit : response.getHits()) {
			transactions.add(objectMapper.readValue(hit.source(), transactionType));
		}

		return transactions;
	}

	public void closestOther() throws IOException {
		/*
		SearchRequestBuilder request = client.prepareSearch(ESIndex.MINT.name).setQuery(
				QueryBuilders.boolQuery()
						.should(QueryBuilders.fuzzyQuery("date", ynab.getDate()))
						.should(QueryBuilders.fuzzyQuery("amountCents", amountCents))
						.should(QueryBuilders.termQuery("type", type))
		);
		*/
	}
}
