package dev.budget.reconciler.model;

import org.joda.time.LocalDate;

public class MintTransaction implements Transaction {

	private LocalDate date;
	private String description;
	private String originalDescription;
	private long amountCents;
	private String type;
	private String category;
	private String account;

	public MintTransaction() {}

	public MintTransaction(LocalDate date, String description, String originalDescription, int amountCents, String type, String category, String account) {
		this.date = date;
		this.description = description;
		this.originalDescription = originalDescription;
		this.amountCents = amountCents;
		this.type = type;
		this.category = category;
		this.account = account;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOriginalDescription() {
		return originalDescription;
	}

	public void setOriginalDescription(String originalDescription) {
		this.originalDescription = originalDescription;
	}

	public long getAmountCents() {
		return amountCents;
	}

	public void setAmountCents(long amountCents) {
		this.amountCents = amountCents;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String toString() {
		return "MintTransaction{" +
				"date=" + date +
				", description='" + description + '\'' +
				", originalDescription='" + originalDescription + '\'' +
				", amountCents=" + amountCents +
				", type='" + type + '\'' +
				", category='" + category + '\'' +
				", account='" + account + '\'' +
				'}';
	}
}
