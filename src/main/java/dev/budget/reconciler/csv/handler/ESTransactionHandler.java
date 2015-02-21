package dev.budget.reconciler.csv.handler;

import dev.budget.reconciler.dao.ESTransactionDao;
import dev.budget.reconciler.es.ESIndex;
import dev.budget.reconciler.model.Transaction;
import org.elasticsearch.client.Client;

import java.io.IOException;

public class ESTransactionHandler<T extends Transaction> implements TransactionHandler<T> {

	private final ESIndex index;
	private final ESTransactionDao dao;

	public ESTransactionHandler(ESIndex index, ESTransactionDao dao) {
		this.index = index;
		this.dao = dao;
	}

	public void handle(T transaction) throws IOException {
		dao.index(index, transaction);
	}
}
