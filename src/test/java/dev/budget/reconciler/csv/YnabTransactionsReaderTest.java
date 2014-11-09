package dev.budget.reconciler.csv;

import com.google.common.collect.ComparisonChain;
import dev.budget.reconciler.csv.handler.ListTransactionHandler;
import dev.budget.reconciler.transaction.MintTransaction;
import dev.budget.reconciler.transaction.YnabTransaction;
import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static dev.budget.reconciler.csv.processors.ParseLocalDate.DATE_FORMATTER;
import static org.fest.assertions.api.Assertions.assertThat;

@Test
public class YnabTransactionsReaderTest {

	@Test
	public void readYnabTransactions() throws Exception {
		String transactionsString = "\"Account\",\"Flag\",\"Check Number\",\"Date\",\"Payee\",\"Category\",\"Master Category\",\"Sub Category\",\"Memo\",\"Outflow\",\"Inflow\",\"Cleared\",\"Running Balance\"\n" +
				"\"Bank Checking\",,,10/07/2014,\"Supermarket\",\"Food:Groceries\",\"Food\",\"Groceries\",,$3.50,$0.00,C,$1.49\n" +
				"\"Bank Checking\",,,09/29/2014,\"Amazon\",\"Entertainment:Amazon Things\",\"Entertainment\",\"Amazon Things\",\"Memo\",$3.94,$0.00,C,$10.79\n";

		ListTransactionHandler<YnabTransaction> handler = new ListTransactionHandler<>();
		YnabTransactionsReader transactionsReader = new YnabTransactionsReader();

		try (Reader reader = new StringReader(transactionsString)) {
			transactionsReader.read(reader, handler);
		}

		List<YnabTransaction> actualTransactions = handler.getTransactions();

		YnabTransaction[] expectedTransactions = {
				new YnabTransaction("Bank Checking", LocalDate.parse("10/07/2014", DATE_FORMATTER), "Supermarket", "Food", "Groceries", 350, 0),
				new YnabTransaction("Bank Checking", LocalDate.parse("9/29/2014", DATE_FORMATTER), "Amazon", "Entertainment", "Amazon Things", 394, 0)
		};

		assertThat(actualTransactions)
				.usingElementComparator((o1, o2) -> ComparisonChain.start()
								.compare(o1.getAccount(), o2.getAccount())
								.compare(o1.getDate(), o2.getDate())
								.compare(o1.getPayee(), o2.getPayee())
								.compare(o1.getMasterCategory(), o2.getMasterCategory())
								.compare(o1.getSubCategory(), o2.getSubCategory())
								.compare(o1.getOutflowCents(), o2.getOutflowCents())
								.compare(o1.getInflowCents(), o2.getInflowCents())
								.result()
				).containsExactly(expectedTransactions);
	}
}
