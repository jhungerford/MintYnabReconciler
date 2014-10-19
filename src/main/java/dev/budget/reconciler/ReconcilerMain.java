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

	public static final String YNAB_FILE_NAME = "ynab.csv";
	public static final String MINT_FILE_NAME = "mint.csv";

	public static void main(String[] args) throws Exception {
		List<YnabTransaction> ynabTransactions = readTransactions(YNAB_FILE_NAME, new YnabTransactionsReader());
		List<MintTransaction> mintTransactions = readTransactions(MINT_FILE_NAME, new MintTransactionsReader());

		System.out.println(ynabTransactions);
		System.out.println(mintTransactions);
	}

	private static <T> List<T> readTransactions(String filename, TransactionsReader<T> transactionsReader) throws IOException, URISyntaxException {
		URL fileUrl = Resources.getResource(filename);
		try (Reader fileReader = new FileReader(new File(fileUrl.toURI()))) {
			return transactionsReader.read(fileReader);
		}
	}
}
