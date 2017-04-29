import java.util.*;

public class CopyPatronStore {
	private HashMap<String, Copy> copies;
	private HashMap<String, Patron> patrons;

	public CopyPatronStore() {
		copies = new HashMap<String, Copy>();
		copies.put("001", new Copy("001", "Spanish", 78.75, false, null));
		copies.put("002", new Copy("002", "English", 63.57, false, null));
		copies.put("003", new Copy("003", "Calculus", 62.83, false, null));
		copies.put("004", new Copy("004", "Linear Algebra", 83.33, false, null));
		copies.put("005", new Copy("005", "Physics", 63.88, false, null));
		copies.put("006", new Copy("006","Dynamics", 51.12, true, null));
		copies.put("007", new Copy("007", "Contemporary History", 99.41, true, null));
		copies.put("008", new Copy("008","Thermodynamics", 100.43, true, null));
		copies.put("009", new Copy("009", "Software", 99.91, true, null));
		copies.put("010", new Copy("010", "Statistics", 58.68, true, null));

		patrons = new HashMap<String, Patron>();
		patrons.put("P101", new Patron("Ceasey", "P101", false, 0.00));
		patrons.put("P102", new Patron("John", "P102", false, 0.00));
		patrons.put("P103", new Patron("Casidy", "P103", false, 0.00));
		patrons.put("P104", new Patron("Bancy", "P104", true, 48.25));
		patrons.put("P105", new Patron("Daniels", "P105", true, 19.58));
		patrons.put("P106", new Patron("Zorro", "P106", true, 31.84));
	}

	public Copy fetchCopy(String CopyID) {
		return copies.get(CopyID);
	}

	public Patron fetchPatrons(String patronID) {
		return patrons.get(patronID);
	}

}
