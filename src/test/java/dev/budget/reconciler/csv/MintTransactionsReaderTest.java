package dev.budget.reconciler.csv;

import com.google.common.collect.ComparisonChain;
import dev.budget.reconciler.csv.handler.ListTransactionHandler;
import dev.budget.reconciler.model.MintTransaction;
import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static dev.budget.reconciler.csv.processors.ParseLocalDate.DATE_FORMATTER;
import static org.fest.assertions.api.Assertions.assertThat;

@Test
public class MintTransactionsReaderTest {

	@Test
	public void readMintTransactions() throws Exception {
		String transactionsString = "\"Date\",\"Description\",\"Original Description\",\"Amount\",\"Transaction Type\",\"Category\",\"Account Name\",\"Labels\",\"Notes\"\n" +
				"\"10/08/2014\",\"Supermarket\",\"SUPERMARKET STORE  219 CITY STORE 1234567890\",\"3.50\",\"debit\",\"Groceries\",\"Credit Card\",\"\",\"\"\n" +
				"\"10/07/2014\",\"Check 1234\",\"CHECK # 1234\",\"350.17\",\"debit\",\"Check\",\"CHECKING\",\"\",\"\"\n" +
				"\"10/07/2014\",\"Credit Card Payment\",\"CARDMEMBER SERV WEB PYMT 1234 ***1234\",\"35.10\",\"debit\",\"Credit Card Payment\",\"CHECKING\",\"\",\"\"\n" +
				"\"9/29/2014\",\"Amazon\",\"SOMETHING FROM AMAZON\",\"3.95\",\"debit\",\"Shopping\",\"Credit Card\",\"\",\"\"\n";

		ListTransactionHandler<MintTransaction> handler = new ListTransactionHandler<>();
		MintTransactionsReader transactionsReader = new MintTransactionsReader();

		try (Reader reader = new StringReader(transactionsString)) {
			transactionsReader.read(reader, handler);
		}

		List<MintTransaction> actualTransactions = handler.getTransactions();

		MintTransaction[] expectedTransactions = {
				new MintTransaction(LocalDate.parse("10/08/2014", DATE_FORMATTER), "Supermarket", "SUPERMARKET STORE  219 CITY STORE 1234567890", 350, "debit", "Groceries", "Credit Card"),
				new MintTransaction(LocalDate.parse("10/07/2014", DATE_FORMATTER), "Check 1234", "CHECK # 1234", 35017, "debit", "Check", "CHECKING"),
				new MintTransaction(LocalDate.parse("10/07/2014", DATE_FORMATTER), "Credit Card Payment", "CARDMEMBER SERV WEB PYMT 1234 ***1234", 3510, "debit", "Credit Card Payment", "CHECKING"),
				new MintTransaction(LocalDate.parse("9/29/2014", DATE_FORMATTER), "Amazon", "SOMETHING FROM AMAZON", 395, "debit", "Shopping", "Credit Card")
		};

		assertThat(actualTransactions)
				.usingElementComparator((o1, o2) -> ComparisonChain.start()
								.compare(o1.date(), o2.date())
								.compare(o1.description(), o2.description())
								.compare(o1.originalDescription(), o2.originalDescription())
								.compare(o1.amountCents(), o2.amountCents())
								.compare(o1.transactionType(), o2.transactionType())
								.compare(o1.category(), o2.category())
								.compare(o1.account(), o2.account())
								.result()
				).containsExactly(expectedTransactions);
	}
}
