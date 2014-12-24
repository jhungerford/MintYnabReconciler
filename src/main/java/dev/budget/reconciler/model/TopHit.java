package dev.budget.reconciler.model;

public class TopHit<T extends Transaction> {
	public final T transaction;
	public final float score;

	public TopHit(T transaction, float score) {
		this.transaction = transaction;
		this.score = score;
	}

	public String toString() {
		return "TopHit{" +
				"transaction=" + transaction +
				", score=" + score +
				'}';
	}
}
