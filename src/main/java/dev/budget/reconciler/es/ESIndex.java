package dev.budget.reconciler.es;

public enum ESIndex {
	MINT("mint", "transaction", "es/mint_index.json"),
	YNAB("ynab", "transaction", "es/ynab_index.json");

	public final String name;
	public final String type;
	public final String fileName;

	ESIndex(String name, String type, String fileName) {
		this.name = name;
		this.type = type;
		this.fileName = fileName;
	}
}
