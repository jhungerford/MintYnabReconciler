package dev.budget.reconciler.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface TransactionsReader<T> {
	List<T> read(Reader reader) throws IOException;
}
