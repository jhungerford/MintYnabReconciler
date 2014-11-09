package dev.budget.reconciler.csv;

import dev.budget.reconciler.csv.handler.TransactionHandler;
import dev.budget.reconciler.model.Transaction;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;

public abstract class TransactionsReader<T extends Transaction> {

	public void read(Reader reader, TransactionHandler<T> handler) throws IOException {
		try (ICsvBeanReader beanReader = new CsvBeanReader(reader, CsvPreference.EXCEL_PREFERENCE)) {
			// Skip the header - we'll define our own
			beanReader.getHeader(true);

			Class<T> transactionClass = getTransactionClass();
			String[] headers = getHeaders();
			CellProcessor[] processors = getCellProcessors();

			T transaction;
			while ((transaction = beanReader.read(transactionClass, headers, processors)) != null) {
				handler.handle(transaction);
			}
		}
	}

	protected abstract Class<T> getTransactionClass();
	protected abstract String[] getHeaders();
	protected abstract CellProcessor[] getCellProcessors();
}
