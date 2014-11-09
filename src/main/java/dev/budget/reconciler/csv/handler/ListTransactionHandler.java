package dev.budget.reconciler.csv.handler;

import dev.budget.reconciler.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/** Transaction handler that saves all transactions to a list */
public class ListTransactionHandler<T extends Transaction> implements TransactionHandler<T> {

	private List<T> transactions = new ArrayList<>();

	public void handle(T transaction) {
		transactions.add(transaction);
	}

	public List<T> getTransactions() {
		return transactions;
	}
}
