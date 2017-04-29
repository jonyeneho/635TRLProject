import java.util.*;

public class SalesController {
	private CopyPatronStore cpStore;
	private Patron currentPatron;
	private Copy copy;
	private ArrayList<Copy> copiesEntered;
	
	public SalesController(CopyPatronStore cps) {
		this.cpStore = cps;
		this.copiesEntered = new ArrayList<Copy>();
		}

	public boolean startSaleTransaction() {
		return true;
	}

	public void clearCopiesEntered() {
		this.copiesEntered.clear();
	}

	public Patron enterPatronForSale(String patronID){
		currentPatron = this.cpStore.fetchPatrons(patronID);
		return currentPatron; 
	}

	public Copy enterCopySelling(String copyID) {
		copy = this.cpStore.fetchCopy(copyID);
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


	