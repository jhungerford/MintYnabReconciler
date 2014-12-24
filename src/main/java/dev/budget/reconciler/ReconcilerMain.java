package dev.budget.reconciler;

import com.google.common.io.Resources;
import dev.budget.reconciler.csv.MintTransactionsReader;
import dev.budget.reconciler.csv.TransactionsReader;
import dev.budget.reconciler.csv.YnabTransactionsReader;
import dev.budget.reconciler.csv.handler.ESTransactionHandler;
import dev.budget.reconciler.csv.handler.ListTransactionHandler;
import dev.budget.reconciler.es.ESIndex;
import dev.budget.reconciler.es.ElasticSearchAdmin;
import dev.budget.reconciler.es.EmbeddedElasticSearch;
import dev.budget.reconciler.model.MintTransaction;
import dev.budget.reconciler.model.TopHit;
import dev.budget.reconciler.model.Transaction;
import dev.budget.reconciler.model.YnabTransaction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ReconcilerMain {
	private static final Logger log = getLogger(ReconcilerMain.class);

	private final EmbeddedElasticSearch es;
	private final Client client;
	private final ElasticSearchAdmin esAdmin;

	public ReconcilerMain(EmbeddedElasticSearch es) {
		this.es = es;
		this.client = es.client();
		this.esAdmin = new ElasticSearchAdmin(es.client());
	}

	public void recreateIndexes(ESIndex... indexes) throws IOException {
		for (ESIndex index : indexes) {
			esAdmin.deleteIndex(index);
			esAdmin.createIndexIfNotExists(index);
		}
	}

	public <T extends Transaction> void streamTransactionsToES(String transactionsFile, ESIndex index, TransactionsReader<T> reader) throws Exception {
		ESTransactionHandler<T> handler = new ESTransactionHandler<>(index, client);
		try (Reader fileReader = createFileReader(transactionsFile)) {
			reader.read(fileReader, handler);
		}

		esAdmin.flush(index);
	}

	public <T extends Transaction> List<T> readTransactions(String transactionsFile, TransactionsReader<T> reader) throws Exception {
		ListTransactionHandler<T> handler = new ListTransactionHandler<>();
		try (Reader fileReader = createFileReader(transactionsFile)) {
			reader.read(fileReader, handler);
		}

		return handler.getTransactions();
	}

	public TopHit<MintTransaction> findNearestMintTransaction(YnabTransaction ynab) {
		String type;
		long amountCents;

		if (ynab.getOutflowCents() > 0) {
			type = "debit";
			amountCents = ynab.getOutflowCents();
		} else {
			type = "credit";
			amountCents = ynab.getInflowCents();
		}

		SearchRequestBuilder request = client.prepareSearch(ESIndex.MINT.name).setQuery(
				QueryBuilders.boolQuery()
						.should(QueryBuilders.fuzzyQuery("date", ynab.getDate()))
						.should(QueryBuilders.fuzzyQuery("amountCents", amountCents))
						.should(QueryBuilders.termQuery("type", type))
		);

		SearchResponse response = request.execute().actionGet();

		if (response.getHits().getTotalHits() == 0) {
			return null;
		}

		SearchHit topHit = response.getHits().getHits()[0];

		MintTransaction mint = new MintTransaction();

		return new TopHit<>(mint, topHit.score());
	}

	private Reader createFileReader(String file) throws IOException, URISyntaxException {
		URL fileUrl = Resources.getResource(file);
		return new FileReader(new File(fileUrl.toURI()));
	}

	private long countTransactions(ESIndex index) {
		return client.prepareCount(index.name).execute().actionGet().getCount();
	}

	public static void main(String[] args) throws Exception {
		try (EmbeddedElasticSearch es = new EmbeddedElasticSearch()) {
			es.start();

			ReconcilerMain reconciler = new ReconcilerMain(es);

			reconciler.recreateIndexes(ESIndex.MINT, ESIndex.YNAB);
			reconciler.streamTransactionsToES("transactions/mint.csv", ESIndex.MINT, new MintTransactionsReader());

			List<YnabTransaction> ynabTransactions = reconciler.readTransactions("transactions/ynab.csv", new YnabTransactionsReader());
			for (YnabTransaction ynab : ynabTransactions) {
				TopHit<MintTransaction> mint = reconciler.findNearestMintTransaction(ynab);

				log.info("{} - {}", ynab, mint);
			}
		}
	}
}
