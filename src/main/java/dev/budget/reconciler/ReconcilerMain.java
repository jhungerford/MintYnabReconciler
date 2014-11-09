package dev.budget.reconciler;

import com.google.common.io.Resources;
import dev.budget.reconciler.csv.MintTransactionsReader;
import dev.budget.reconciler.csv.TransactionsReader;
import dev.budget.reconciler.csv.YnabTransactionsReader;
import dev.budget.reconciler.csv.handler.ESTransactionHandler;
import dev.budget.reconciler.es.ESIndex;
import dev.budget.reconciler.es.ElasticSearchAdmin;
import dev.budget.reconciler.es.EmbeddedElasticSearch;
import dev.budget.reconciler.model.MintTransaction;
import dev.budget.reconciler.model.Transaction;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

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

	public void recreateIndexes() throws IOException {
		for (ESIndex index : new ESIndex[] { ESIndex.MINT, ESIndex.YNAB }) {
			esAdmin.deleteIndex(index);
			esAdmin.createIndexIfNotExists(index);
		}
	}

	private <T extends Transaction> void streamTransactionsToES(String transactionsFile, ESIndex index, TransactionsReader<T> reader) throws Exception {
		ESTransactionHandler<T> handler = new ESTransactionHandler<>(index, client);

		URL fileUrl = Resources.getResource(transactionsFile);
		try (Reader fileReader = new FileReader(new File(fileUrl.toURI()))) {
			reader.read(fileReader, handler);
		}
	}

	private long countTransactions(ESIndex index) {
		return client.prepareCount(index.name).execute().actionGet().getCount();
	}

	public static void main(String[] args) throws Exception {
		try (EmbeddedElasticSearch es = new EmbeddedElasticSearch()) {
			es.start();

			ReconcilerMain reconciler = new ReconcilerMain(es);

			reconciler.recreateIndexes();
			reconciler.streamTransactionsToES("transactions/mint.csv", ESIndex.MINT, new MintTransactionsReader());
			reconciler.streamTransactionsToES("transactions/ynab.csv", ESIndex.YNAB, new YnabTransactionsReader());

			// TODO: wait until entries are indexed.

			log.info("# mint ES entries: {}", reconciler.countTransactions(ESIndex.MINT));
			log.info("# ynab ES entries: {}", reconciler.countTransactions(ESIndex.YNAB));
		}
	}
}
