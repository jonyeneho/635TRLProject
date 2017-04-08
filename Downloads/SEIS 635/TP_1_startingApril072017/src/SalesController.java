import java.util.ArrayList;

public class SalesController {
	private PatronStore pStore;
	private CopyStore cStore;
	private Patron currentPatron;
	private Copy copy;
	private ArrayList<Copy> copiesEntered;
	
	public SalesController(PatronStore ps, CopyStore cs) {
		this.pStore = ps;
		this.cStore = cs;
		this.copiesEntered = new ArrayList<Copy>();
		}

	public boolean startSaleTransaction() {
		return true;
	}

	public void clearCopiesEntered() {
		this.copiesEntered.clear();
	}

	public Patron enterPatronForSale(String patronID){
		currentPatron = this.pStore.fetchPatrons(patronID);
		return currentPatron; 
	}

	public Copy enterCopySelling(String copyID) {
		copy = this.cStore.fetchCopy(copyID);
		if (copy.getForSale()) {
			this.copiesEntered.add(copy);
			currentPatron.sellCopy(copy);
			StdOut.println(currentPatron);
		}
		return copy;
	}
	
	public boolean endSaleTransaction() {
		return false;
	}
	
	public void setCopiesEntered (ArrayList<Copy> copiesEntered){
	     this.copiesEntered = copiesEntered;
	} 
		
	public ArrayList<Copy> getCopiesEntered() {
		return copiesEntered;
	}
}


	