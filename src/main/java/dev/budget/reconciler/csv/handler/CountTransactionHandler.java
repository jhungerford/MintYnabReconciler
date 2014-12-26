package dev.budget.reconciler.csv.handler;

import dev.budget.reconciler.model.Transaction;

import java.io.IOException;

/** TransactionHandler that counts the number of transactions processed. */
public class CountTransactionHandler<T extends Transaction> implements TransactionHandler<T> {

	private int count = 0;

	public void handle(T ignored) throws IOException {
		count ++;
	}

	public int getCount() {
		return count;
	}
}
