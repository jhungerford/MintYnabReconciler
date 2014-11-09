package dev.budget.reconciler.csv;

import com.google.common.io.Resources;

import java.io.*;
import java.net.URL;

public class IdColumnAdder {

	public static void main(String[] args) throws Exception {
		String inFile = "transactions/ynab.csv";
		String outFile = "ynab_ids.csv";

		URL fileUrl = Resources.getResource(inFile);
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileUrl.toURI())));
				PrintWriter out = new PrintWriter(outFile)) {

			String headers = reader.readLine();
			out.println("\"ID\"," + headers);

			int id = 1;
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				out.println(id + "," + line);
				id ++;
			}
		}
	}
}
