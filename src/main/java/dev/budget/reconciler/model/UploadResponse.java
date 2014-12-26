package dev.budget.reconciler.model;

public class UploadResponse {
	private int numRecords;

	public UploadResponse() {}

	public UploadResponse(int numRecords) {
		this.numRecords = numRecords;
	}

	public int getNumRecords() {
		return numRecords;
	}

	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}
}
