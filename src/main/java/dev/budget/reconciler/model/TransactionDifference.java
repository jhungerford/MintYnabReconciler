package dev.budget.reconciler.model;

public class TransactionDifference {
	private String mintDate;
	private String mintTransaction;
	private int mintCents;
	private String ynabDate;
	private String ynabTransaction;
	private int ynabCents;

	public String getMintDate() {
		return mintDate;
	}

	public void setMintDate(String mintDate) {
		this.mintDate = mintDate;
	}

	public String getMintTransaction() {
		return mintTransaction;
	}

	public void setMintTransaction(String mintTransaction) {
		this.mintTransaction = mintTransaction;
	}

	public int getMintCents() {
		return mintCents;
	}

	public void setMintCents(int mintCents) {
		this.mintCents = mintCents;
	}

	public String getYnabDate() {
		return ynabDate;
	}

	public void setYnabDate(String ynabDate) {
		this.ynabDate = ynabDate;
	}

	public String getYnabTransaction() {
		return ynabTransaction;
	}

	public void setYnabTransaction(String ynabTransaction) {
		this.ynabTransaction = ynabTransaction;
	}

	public int getYnabCents() {
		return ynabCents;
	}

	public void setYnabCents(int ynabCents) {
		this.ynabCents = ynabCents;
	}
}
