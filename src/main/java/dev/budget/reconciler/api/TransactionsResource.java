package dev.budget.reconciler.api;

import dev.budget.reconciler.model.UploadResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/transactions")
public class TransactionsResource {

	@POST
	@Path("/mint")
	@Consumes("text/csv")
	@Produces(MediaType.APPLICATION_JSON)
	public UploadResponse uploadMint(String mintTransactions) {
		return new UploadResponse(0);
	}
}
