package dev.budget.reconciler;

import com.google.common.io.Resources;
import dev.budget.reconciler.csv.MintTransactionsReader;
import dev.budget.reconciler.csv.TransactionsReader;
import dev.budget.reconciler.csv.YnabTransactionsReader;
import dev.budget.reconciler.csv.handler.ListTransactionHandler;
import dev.budget.reconciler.model.MintTransaction;
import dev.budget.reconciler.model.Transaction;
import dev.budget.reconciler.model.YnabTransaction;
import org.slf4j.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ReconcilerMain {
	private static final Logger log = getLogger(ReconcilerMain.class);

	public static void main(String[] args) throws Exception {
		String transactionsDir = "transactions";

		List<YnabTransaction> ynabTransactions = readTransactions(transactionsDir, "ynab.csv", new YnabTransactionsReader());
		List<MintTransaction> mintTransactions = readTransactions(transactionsDir, "mint.csv", new MintTransactionsReader());

		log.info("Ynab transactions: {}", ynabTransactions);
		log.info("Mint transactions: {}", mintTransactions);
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
