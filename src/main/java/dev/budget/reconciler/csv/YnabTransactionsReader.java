package dev.budget.reconciler.csv;

import dev.budget.reconciler.csv.processors.ParseCents;
import dev.budget.reconciler.csv.processors.ParseLocalDate;
import dev.budget.reconciler.transaction.YnabTransaction;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class YnabTransactionsReader extends TransactionsReader<YnabTransaction> {
	// "Account","Flag","Check Number","Date","Payee","Category","Master Category","Sub Category","Memo","Outflow","Inflow","Cleared","Running Balance"

	public Class<YnabTransaction> getTransactionClass() {
		return YnabTransaction.class;
	}

	public String[] getHeaders() {
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

	public CellProcessor[] getCellProcessors() {
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
