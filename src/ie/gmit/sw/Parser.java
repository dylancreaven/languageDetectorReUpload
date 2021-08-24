package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Parser implements Runnable {

	private Database db = null;
	private String file;
	private int k;

	public Parser(String file, int k) {
		this.file = file;
		this.k = k;
	}

	public void setDb(Database db) {

		this.db = db;

	}

	@Override
	public void run() {
		/**
		 * takes in the lines from the language text files and removes the @ symbol
		 * 
		 * 
		 * */
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;

			while ((line = br.readLine()) != null) {
				String[] record = line.trim().split("@");
				if (record.length != 2)
					continue;
				parse(record[0], record[1]);// else
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void parse(String text, String lang, int... ks) {
		Language language = Language.valueOf(lang);
		for (int i = 0; i <= text.length() - k; i++) {
			CharSequence kmer = text.substring(i, i + k);
			db.add(kmer, language);
		}

	}
	/**
	 * @author Dylan Creaven
	 * @version 1.0
	 * @since 1.8
	 * Parses the query file into a map which is sorted
	 * creates map containing frequencies,ngrams, and ranks
	 * @param File query file
	 * @param kmer which ngram to use(1,2,3 etc)
	 * @return the queryMap of ngrams
	 * */
	public Map<Integer, LanguageEntry> queryParse(int k, String file) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		Map<Integer, LanguageEntry> queryMap = new ConcurrentHashMap<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			String words = null;
			ArrayList<String> wArray = new ArrayList<String>();
			String queryText = "";

			while ((line = br.readLine()) != null) {
				words = line.trim().replace("\n", "").replace("\r", "");
				wArray.add(words);
			}
			String word = String.join("", wArray);
			for (char c : word.toCharArray()) {
				queryText += c;
				if (queryText.length() >= 400)
					break;
				br.close();
			}
			for (int i = 0; i < queryText.length() - k; i++) {
				CharSequence kmer = queryText.substring(i, i + k);
				if (map.containsKey(kmer.hashCode())) {
					map.put(kmer.hashCode(), map.get(kmer.hashCode()) + 1);
				} else {
					map.put(kmer.hashCode(), 1);
				}
			}
			map = sortByValue(map);
			int current = 1;
			for (Map.Entry<Integer, Integer> couple : map.entrySet()) {// for every pair in the entrySet

				LanguageEntry lEntry = new LanguageEntry(couple.getKey(), couple.getValue());
				lEntry.setRank(current);

				queryMap.put(lEntry.getKmer(), lEntry);
				if (current <= 1)current++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryMap;
	}
	public static Map<Integer, Integer> sortByValue(final Map<Integer, Integer> wordCounts) {
		return wordCounts.entrySet().parallelStream()
				.sorted((Map.Entry.<Integer, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e, e2) -> e, LinkedHashMap::new));
	}
}
