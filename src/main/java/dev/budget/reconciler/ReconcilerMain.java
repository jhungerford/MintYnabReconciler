package dev.budget.reconciler;

import com.google.common.io.Resources;
import dev.budget.reconciler.csv.MintTransactionsReader;
import dev.budget.reconciler.csv.TransactionsReader;
import dev.budget.reconciler.csv.YnabTransactionsReader;
import dev.budget.reconciler.transaction.MintTransaction;
import dev.budget.reconciler.transaction.YnabTransaction;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class ReconcilerMain {

	public static void main(String[] args) throws Exception {
		String transactionsDir = "transactions";

		List<YnabTransaction> ynabTransactions = readTransactions(transactionsDir, "ynab.csv", new YnabTransactionsReader());
		List<MintTransaction> mintTransactions = readTransactions(transactionsDir, "mint.csv", new MintTransactionsReader());

		System.out.println(ynabTransactions);
		System.out.println(mintTransactions);
	}

	private static <T> List<T> readTransactions(String directory, String filename, TransactionsReader<T> transactionsReader) throws IOException, URISyntaxException {
		URL fileUrl = Resources.getResource(directory + File.separator + filename);
		try (Reader fileReader = new FileReader(new File(fileUrl.toURI()))) {
			return transactionsReader.read(fileReader);
		}
	}
}
