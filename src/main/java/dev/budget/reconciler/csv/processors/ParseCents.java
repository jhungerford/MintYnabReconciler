package dev.budget.reconciler.csv.processors;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseCents extends CellProcessorAdaptor {

	public static final Pattern MONEY_PATTERN = Pattern.compile("^[$]?(\\d+)[.](\\d{2})"); // $1 is dollars, $2 is cents

	public ParseCents() {
		super();
	}

	public ParseCents(CellProcessor next) {
		super(next);
	}

	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);

		Matcher matcher = MONEY_PATTERN.matcher(value.toString());
		if (! matcher.matches()) {
			throw new SuperCsvCellProcessorException("Money amount '" + value + "' does not match the regex.", context, this);
		}

		try {
			return Integer.parseInt(matcher.group(1)) * 100 + Integer.parseInt(matcher.group(2));
		} catch (NumberFormatException e) {
			throw new SuperCsvCellProcessorException("Dollar or cent amount in '" + value + "' is non-numeric", context, this, e);
		}
	}
}
