
import java.util.HashMap;

public class CopyStore {
	private HashMap<String, Copy> copies;

	public CopyStore() {
		copies = new HashMap<String, Copy>();

		copies.put("001", new Copy("001", 78.75, false));
		copies.put("002", new Copy("002", 63.57, false));
		copies.put("003", new Copy("003", 62.83, false));
		copies.put("004", new Copy("004", 83.33, false));
		copies.put("005", new Copy("005", 63.88, false));
		copies.put("006", new Copy("006", 51.12, true));
		copies.put("007", new Copy("007", 99.41, true));
		copies.put("008", new Copy("008", 100.43, true));
		copies.put("009", new Copy("009", 99.91, true));
		copies.put("010", new Copy("010", 58.68, true));
	}

	public Copy fetchCopy(String CopyID)
	{
		return copies.get(CopyID);
	}

	public static void main(String[] args) {
		CopyStore cStore = new CopyStore();
		Copy c = cStore.fetchCopy("001");
		Patron p = new Patron("George", "7023", false);
		System.out.println(p);

		c = new Copy("7023", 39.99, false);
		p.checkCopyOut(c);

		System.out.println(p);
	}

}