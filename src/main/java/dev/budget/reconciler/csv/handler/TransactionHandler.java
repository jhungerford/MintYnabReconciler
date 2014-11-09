package dev.budget.reconciler.csv.handler;

import dev.budget.reconciler.model.Transaction;

public interface TransactionHandler<TRANSACTION extends Transaction> {

	void handle(TRANSACTION transaction);
}
