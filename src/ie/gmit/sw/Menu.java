package ie.gmit.sw;

import java.util.*;

public class Menu {
/**
 * Displays the Menu to the user
 * 
 * 
 * */
	void displayMenu() {

		System.out.println("***************************************************");
		System.out.println("***********TEXT LANGUAGE DETECTOR******************");
		System.out.println("***************************************************");
		System.out.println("Enter as many ngrams as you want (3 is recommended)");
		System.out.println("***************************************************");

	}

	public void enterDataLocation() {
		System.out.println("Enter WILI Data location: ");

	}

	public void enterQueryFile() {
		System.out.println("Enter Query File location: ");

	}

	/**
	 * Outputs what the program think the query file is written in
	 * 
	 * */
	public void resultOutput(Database db, Map<Integer, LanguageEntry> q) {
		System.out.println("Processing Query......PLEASE WAIT");
		System.out.println("Text appears to be written in :     " + db.getLanguage(q));
	}

}
