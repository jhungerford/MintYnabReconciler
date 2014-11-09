package dev.budget.reconciler.csv.processors;

import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

@Test
public class ParseCentsTest {

	private final ParseCents parseCents = new ParseCents();

	@DataProvider(name = "testCases")
	public Object[][] createTestCases() {
		return new Object[][] {
				testCase("$3.50", 350L),
				testCase("3.50", 350L),
				testCase("12.95", 1295L),
				testCase("$0.00", 0L),
		};
	}

	private Object[] testCase(String value, Long expectedCents) {
		return new Object[] { value, expectedCents };
	}

	@Test(dataProvider = "testCases")
	public void testCents(String value, Long expectedCents) {

	}

	@Test(expectedExceptions = SuperCsvCellProcessorException.class)
	public void invalidFormat() {
		CsvContext context = mock(CsvContext.class);
		parseCents.execute("this is not a money value", context);
	}
}
