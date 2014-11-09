package dev.budget.reconciler.model;

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public interface Transaction {
	XContentBuilder esJson() throws IOException;
}
