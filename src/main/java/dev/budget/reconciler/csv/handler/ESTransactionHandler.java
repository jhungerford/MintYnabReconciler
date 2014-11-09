package dev.budget.reconciler.csv.handler;

import dev.budget.reconciler.es.ESIndex;
import dev.budget.reconciler.model.Transaction;
import org.elasticsearch.client.Client;

import java.io.IOException;

public class ESTransactionHandler<T extends Transaction> implements TransactionHandler<T> {

	private final ESIndex index;
	private final Client client;

	public ESTransactionHandler(ESIndex index, Client client) {
		this.index = index;
		this.client = client;
	}

	public void handle(Transaction transaction) throws IOException {
		client.prepareIndex(index.name, index.type).setSource(transaction.esJson()).execute().actionGet();
	}
}
