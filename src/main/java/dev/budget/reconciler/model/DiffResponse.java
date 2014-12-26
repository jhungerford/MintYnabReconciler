package dev.budget.reconciler.model;

import java.util.List;

public class DiffResponse {
	private List<TransactionDifference> differences;

	public DiffResponse() {}

	public DiffResponse(List<TransactionDifference> differences) {
		this.differences = differences;
	}

	public List<TransactionDifference> getDifferences() {
		return differences;
	}

	public void setDifferences(List<TransactionDifference> differences) {
		this.differences = differences;
	}
}
