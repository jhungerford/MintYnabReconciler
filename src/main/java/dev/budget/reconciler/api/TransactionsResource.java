package dev.budget.reconciler.api;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.twitter.finagle.Http;
import com.twitter.finagle.Service;
import com.twitter.util.Future;
import dev.budget.reconciler.csv.MintTransactionsReader;
import dev.budget.reconciler.csv.TransactionsReader;
import dev.budget.reconciler.csv.YnabTransactionsReader;
import dev.budget.reconciler.csv.handler.ChainTransactionHandler;
import dev.budget.reconciler.csv.handler.CountTransactionHandler;
import dev.budget.reconciler.csv.handler.ESTransactionHandler;
import dev.budget.reconciler.csv.handler.ListTransactionHandler;
import dev.budget.reconciler.dao.ESTransactionDao;
import dev.budget.reconciler.es.ESIndex;
import dev.budget.reconciler.es.ElasticSearchAdmin;
import dev.budget.reconciler.model.MintTransaction;
import dev.budget.reconciler.model.Transaction;
import dev.budget.reconciler.model.UploadResponse;
import dev.budget.reconciler.model.YnabTransaction;
import org.elasticsearch.client.Client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

@Path("/v1/transactions")
public class TransactionsResource {

	@Inject
	private ElasticSearchAdmin esAdmin;
	@Inject
	private ESTransactionDao esDao;
	@Inject
	@Named("mintUploadService")
	private Service<List<MintTransaction>, Integer> mintUploadService;

	@PUT
	@Path("/mint")
	@Consumes("text/csv")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadMint(String mintTransactionsCsv) throws IOException {
		esAdmin.clearIndex(ESIndex.MINT);

		List<MintTransaction> mintTransactions = transactionsToList(mintTransactionsCsv, new MintTransactionsReader());
		Future<Integer> uploadedFuture = mintUploadService.apply(mintTransactions);

		return Response.ok(new UploadResponse(uploadedFuture.get())).build();
	}

	@PUT
	@Path("/ynab")
	@Consumes("text/csv")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadYnab(String ynabTransactions) throws IOException {
		esAdmin.clearIndex(ESIndex.YNAB);

		int count = writeTransactions(ESIndex.YNAB, ynabTransactions, new YnabTransactionsReader());
		UploadResponse uploadResponse = new UploadResponse(count);

		return Response.ok(uploadResponse).build();
	}

	@GET
	@Path("/diff")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiff() throws IOException {
		List<MintTransaction> mintTransactions = esDao.allThisMonth(ESIndex.MINT, MintTransaction.class);
		List<YnabTransaction> ynabTransactions = esDao.allThisMonth(ESIndex.YNAB, YnabTransaction.class);

		return Response.noContent().build();
	}

	private <T extends Transaction> List<T> transactionsToList(String csv, TransactionsReader<T> transactionsReader) throws IOException {
		ListTransactionHandler<T> listHandler = new ListTransactionHandler<>();

		try (Reader in = new StringReader(csv)) {
			transactionsReader.read(in, listHandler);
		}

		return listHandler.getTransactions();
	}

	private <T extends Transaction> int writeTransactions(ESIndex index, String transactions, TransactionsReader<T> transactionsReader) throws IOException {

		ESTransactionHandler<T> esHandler = new ESTransactionHandler<>(index, esDao);
		CountTransactionHandler<T> countHandler = new CountTransactionHandler<>();
		ChainTransactionHandler<T> transactionHandler = new ChainTransactionHandler<>(esHandler, countHandler);

		try (Reader in = new StringReader(transactions)) {
			transactionsReader.read(in, transactionHandler);
		}

		return countHandler.getCount();
	}
}
