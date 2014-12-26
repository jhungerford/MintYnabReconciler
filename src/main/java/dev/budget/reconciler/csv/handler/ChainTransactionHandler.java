package dev.budget.reconciler.csv.handler;

import dev.budget.reconciler.model.Transaction;

import java.io.IOException;

/** TransactionHandler that runs a series of TransactionHandlers, in the order they were passed to the constructor. */
public class ChainTransactionHandler<T extends Transaction> implements TransactionHandler<T> {

	private final TransactionHandler<T>[] handlers;


	@SafeVarargs
	public ChainTransactionHandler(TransactionHandler<T>... handlers) {
		this.handlers = handlers;
	}

	public void handle(T transaction) throws IOException {
		for (TransactionHandler<T> handler : handlers) {
			handler.handle(transaction);
		}
	}
}
