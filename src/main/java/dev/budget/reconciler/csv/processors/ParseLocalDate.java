package dev.budget.reconciler.csv.processors;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class ParseLocalDate extends CellProcessorAdaptor {

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("MM/dd/yyyy");

	public ParseLocalDate() {
		super();
	}

	public ParseLocalDate(CellProcessor next) {
		super(next);
	}

	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);
		try {
			return LocalDate.parse(value.toString(), DATE_FORMATTER);
		} catch (IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException("Invalid date format: " + value, context, this, e);
		}
	}
}
