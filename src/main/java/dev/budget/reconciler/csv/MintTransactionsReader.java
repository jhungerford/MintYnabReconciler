package dev.budget.reconciler.csv;

import dev.budget.reconciler.csv.processors.ParseCents;
import dev.budget.reconciler.csv.processors.ParseLocalDate;
import dev.budget.reconciler.model.MintTransaction;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class MintTransactionsReader extends TransactionsReader<MintTransaction> {
	// "Date","Description","Original Description","Amount","Transaction Type","Category","Account Name","Labels","Notes"

	protected Class<MintTransaction> getTransactionClass() {
		return MintTransaction.class;
	}

	protected String[] getHeaders() {
		return new String[] {
				"date",					// Date
				"description",			// Description
				"originalDescription",	// Original Description
				"amountCents",			// Amount
				"type",					// Transaction Type
				"category",				// Category
				"account",				// Account Name
				null,					// Labels
				null					// Notes
		};
	}

	protected CellProcessor[] getCellProcessors() {
		return new CellProcessor[] {
				new ParseLocalDate(new NotNull()),	// Date
				new Optional(),						// Description
				new Optional(),						// Original Description
				new ParseCents(new NotNull()),		// Amount
				new NotNull(),						// Transaction Type
				new Optional(),						// Category
				new NotNull(),						// Account Name
				null,								// Labels
				null								// Notes
		};
	}
}
