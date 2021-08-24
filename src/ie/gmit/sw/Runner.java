package ie.gmit.sw;

import java.util.Map;
import java.util.Scanner;

public class Runner {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Throwable {
		Menu m = new Menu();
		m.displayMenu();

		Scanner console = new Scanner(System.in);
		String textData;
		String queryFile;
		int ngram;

		ngram = console.nextInt();
		m.enterDataLocation();
		textData = console.next();
		try {
			Database db = new Database();
			Parser p = new Parser(textData, ngram);
			p.setDb(db);

			Thread t1 = new Thread(p);
			t1.start();
			t1.join();
			m.enterQueryFile();
			queryFile = console.next();
			Map<Integer, LanguageEntry> q = p.queryParse(ngram, queryFile);
			m.resultOutput(db, q);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}