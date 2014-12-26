package dev.budget.reconciler.api;

import dev.budget.reconciler.model.ErrorResponse;
import dev.budget.reconciler.model.UploadResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/transactions")
public class TransactionsResource {

	@POST
	@Path("/mint")
	@Consumes("text/csv")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadMint(String mintTransactions) {
		return Response.ok(new UploadResponse(0)).build();
	}

	@POST
	@Path("/ynab")
	@Consumes("text/csv")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadYnab(String ynabTransactions) {
		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(new ErrorResponse("Ynab always fails for testing"))
				.build();
	}
}
