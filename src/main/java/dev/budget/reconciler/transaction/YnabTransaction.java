package dev.budget.reconciler.transaction;

import org.joda.time.LocalDate;

public class YnabTransaction implements Transaction {

	private String account;
	private LocalDate date;
	private String payee;
	private String masterCategory;
	private String subCategory;
	private int outflowCents;
	private int inflowCents;

	public YnabTransaction() {}

	public YnabTransaction(String account, LocalDate date, String payee, String masterCategory, String subCategory, int outflowCents, int inflowCents) {
		this.account = account;
		this.date = date;
		this.payee = payee;
		this.masterCategory = masterCategory;
		this.subCategory = subCategory;
		this.outflowCents = outflowCents;
		this.inflowCents = inflowCents;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getMasterCategory() {
		return masterCategory;
	}

	public void setMasterCategory(String masterCategory) {
		this.masterCategory = masterCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public int getOutflowCents() {
		return outflowCents;
	}

	public void setOutflowCents(int outflowCents) {
		this.outflowCents = outflowCents;
	}

	public int getInflowCents() {
		return inflowCents;
	}

	public void setInflowCents(int inflowCents) {
		this.inflowCents = inflowCents;
	}

	public String toString() {
		return "YnabTransaction{" +
				"account='" + account + '\'' +
				", date=" + date +
				", payee='" + payee + '\'' +
				", masterCategory='" + masterCategory + '\'' +
				", subCategory='" + subCategory + '\'' +
				", outflowCents=" + outflowCents +
				", inflowCents=" + inflowCents +
				'}';
	}
}
