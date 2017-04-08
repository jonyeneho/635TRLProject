
import java.util.HashMap;

public class PatronStore {
	private HashMap<String,Patron> patrons;
	
	public PatronStore() {
		patrons = new HashMap<String,Patron>();
		patrons.put("P101",new Patron("Ceasey", "P101", false));
		patrons.put("P102",new Patron("John", "P102", false));
		patrons.put("P103",new Patron("Casidy", "P103", false));
		patrons.put("P104",new Patron("Bancy", "P104", true));
		patrons.put("P105",new Patron("Daniels", "P105", true));
		patrons.put("P106",new Patron("Zorro", "P106", true));
		}
	
	public Patron fetchPatrons(String patronID){
		return patrons.get(patronID);
	}

	public static void main(String[] args) {
		PatronStore pStore = new PatronStore();
		Patron p = pStore.fetchPatrons("P101");
		System.out.println(p);

		Copy c = new Copy("7023", 49.99, false);
		p.checkCopyOut(c);
		System.out.println(p);
		
		Copy c2 = new Copy("7024", 59.99, true);
		p.sellCopy(c2);
		System.out.println(p);
	}

}
