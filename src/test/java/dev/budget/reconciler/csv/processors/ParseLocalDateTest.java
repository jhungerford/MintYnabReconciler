package dev.budget.reconciler.csv.processors;

import org.joda.time.LocalDate;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ParseLocalDateTest {
	private final ParseLocalDate parseLocalDate = new ParseLocalDate();

	@DataProvider(name = "testCases")
	public Object[][] createTestCases() {
		return new Object[][] {
				testCase("9/30/2014", 9, 30, 2014),
				testCase("09/30/2014", 9, 30, 2014),
				testCase("10/01/2014", 10, 1, 2014)
		};
	}

	private Object[] testCase(String value, int month, int day, int year) {
		return new Object[] { value, month, day, year };
	}

	@Test(dataProvider = "testCases")
	public void testCents(String value, int month, int day, int year) {
		CsvContext context = mock(CsvContext.class);
		Object result = parseLocalDate.execute(value, context);

		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(LocalDate.class);

		LocalDate actualDate = (LocalDate) result;

		assertThat(actualDate.getDayOfMonth()).isEqualTo(day);
		assertThat(actualDate.getMonthOfYear()).isEqualTo(month);
		assertThat(actualDate.getYear()).isEqualTo(year);
	}

	@Test(expectedExceptions = SuperCsvCellProcessorException.class)
	public void invalidFormat() {
		CsvContext context = mock(CsvContext.class);
		parseLocalDate.execute("this is not a real date", context);
	}
}
