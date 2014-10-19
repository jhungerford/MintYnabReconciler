package dev.budget.reconciler.csv;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionsReader<T> {
	public List<T> read(Reader reader) throws IOException {
		try (ICsvBeanReader beanReader = new CsvBeanReader(reader, CsvPreference.EXCEL_PREFERENCE)) {
			// Skip the header - we'll define our own
			beanReader.getHeader(true);

			Class<T> transactionClass = getTransactionClass();
			String[] headers = getHeaders();
			CellProcessor[] processors = getCellProcessors();

			List<T> transactions = new ArrayList<>();

			T transaction;
			while ((transaction = beanReader.read(transactionClass, headers, processors)) != null) {
				transactions.add(transaction);
			}

			return transactions;
		}
	}

	protected abstract Class<T> getTransactionClass();
	protected abstract String[] getHeaders();
	protected abstract CellProcessor[] getCellProcessors();
}
