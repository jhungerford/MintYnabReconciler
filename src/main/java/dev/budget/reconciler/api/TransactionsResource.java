package dev.budget.reconciler.api;

import dev.budget.reconciler.model.UploadResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/transactions")
public class TransactionsResource {

	@PUT
	@Path("/mint")
	@Consumes("text/csv")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadMint(String mintTransactions) {
		return Response.ok(new UploadResponse(0)).build();
	}

	@PUT
	@Path("/ynab")
	@Consumes("text/csv")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadYnab(String ynabTransactions) {
		return Response.ok(new UploadResponse(0)).build();
	}

	@GET
	@Path("/diff")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiff() {
		return Response.noContent().build();
	}
}
