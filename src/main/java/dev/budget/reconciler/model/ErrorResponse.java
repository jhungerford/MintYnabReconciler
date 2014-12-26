package dev.budget.reconciler.model;

public class ErrorResponse {
	public String reason;

	public ErrorResponse() {}

	public ErrorResponse(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
