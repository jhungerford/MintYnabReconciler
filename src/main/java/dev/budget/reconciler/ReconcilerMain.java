package dev.budget.reconciler;

import com.google.common.io.Resources;
import dev.budget.reconciler.csv.MintTransactionsReader;
import dev.budget.reconciler.csv.TransactionsReader;
import dev.budget.reconciler.csv.handler.ESTransactionHandler;
import dev.budget.reconciler.csv.handler.ListTransactionHandler;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ReconcilerMain {
	private static final Logger log = getLogger(ReconcilerMain.class);

	public static void main(String[] args) throws Exception {
		try (EmbeddedElasticSearch es = new EmbeddedElasticSearch()) {
			es.start();

			Client client = es.client();
			ElasticSearchAdmin esAdmin = new ElasticSearchAdmin(client);

			esAdmin.deleteIndex(ESIndex.MINT);
			esAdmin.createIndexIfNotExists(ESIndex.MINT);

			ESTransactionHandler<MintTransaction> mintESHandler = new ESTransactionHandler<>(ESIndex.MINT, client);

			URL fileUrl = Resources.getResource("transactions/mint.csv");
			try (Reader fileReader = new FileReader(new File(fileUrl.toURI()))) {
				new MintTransactionsReader().read(fileReader, mintESHandler);
			}

			long numMint = client.prepareCount(ESIndex.MINT.name).execute().actionGet().getCount();
			log.info("# entries in ES: {}", numMint);
		}
	}

	private static <T extends Transaction> List<T> readTransactions(String directory, String filename, TransactionsReader<T> transactionsReader) throws IOException, URISyntaxException {
		ListTransactionHandler<T> handler = new ListTransactionHandler<>();

		String resourceName = directory + File.separator + filename;
		log.debug("Loading transactions from {}", resourceName);

		URL fileUrl = Resources.getResource(resourceName);
		try (Reader fileReader = new FileReader(new File(fileUrl.toURI()))) {
			transactionsReader.read(fileReader, handler);
		}

		return handler.getTransactions();
	}
}
