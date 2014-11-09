package dev.budget.reconciler.csv.handler;

import dev.budget.reconciler.model.Transaction;

import java.io.IOException;

public interface TransactionHandler<TRANSACTION extends Transaction> {

	void handle(TRANSACTION transaction) throws IOException;
}
