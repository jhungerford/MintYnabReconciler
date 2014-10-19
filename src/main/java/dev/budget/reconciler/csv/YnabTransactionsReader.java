package dev.budget.reconciler.csv;

import dev.budget.reconciler.csv.processors.ParseCents;
import dev.budget.reconciler.csv.processors.ParseLocalDate;
import dev.budget.reconciler.transaction.YnabTransaction;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class YnabTransactionsReader implements TransactionsReader<YnabTransaction> {
	// "Account","Flag","Check Number","Date","Payee","Category","Master Category","Sub Category","Memo","Outflow","Inflow","Cleared","Running Balance"

	public List<YnabTransaction> read(Reader reader) throws IOException {
		try (ICsvBeanReader beanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE)) {
			// Skip the header - we'll define our own
			beanReader.getHeader(true);

			String[] header = createHeader();
			CellProcessor[] processors = createProcessors();

			List<YnabTransaction> transactions = new ArrayList<>();

			YnabTransaction transaction;
			while ((transaction = beanReader.read(YnabTransaction.class, header, processors)) != null) {
				transactions.add(transaction);
			}

			return transactions;
		}
	}

	private String[] createHeader() {
		return new String[] {
				"account",			// Account
				null,				// Flag
				null,				// Check Number
				"date",				// Date
				"payee",			// Payee
				null,				// Category
				"masterCategory",	// Master Category
				"subCategory",		// Sub Category
				null,				// Memo
				"outflowCents",		// Outflow
				"inflowCents",		// Inflow
				null,				// Cleared
				null				// Running Balance
		};
	}

	private CellProcessor[] createProcessors() {
		return new CellProcessor[] {
				new NotNull(),						// Account
				null,								// Flag
				null,								// Check Number
				new ParseLocalDate(new NotNull()),	// Date
				new NotNull(),						// Payee
				null,								// Category
				new Optional(),						// Master Category
				new Optional(),						// Sub Category
				null,								// Memo
				new ParseCents(new NotNull()),		// Outflow
				new ParseCents(new NotNull()),		// Inflow
				null,								// Cleared
				null								// Running Balance
		};
	}
}
