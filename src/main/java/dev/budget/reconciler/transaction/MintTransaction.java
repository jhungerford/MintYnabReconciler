package dev.budget.reconciler.transaction;

import org.joda.time.LocalDate;

public class MintTransaction {

	private LocalDate date;
	private String description;
	private String originalDescription;
	private int amountCents;
	private String type;
	private String category;
	private String account;

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

	public int getAmountCents() {
		return amountCents;
	}

	public void setAmountCents(int amountCents) {
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
