import java.util.ArrayList;

public class BorrowOutController {
	private PatronStore pStore;
	private CopyStore cStore;
	private Patron currentPatron;
	private Copy copy;
	private ArrayList<Copy> copiesEntered;
	
	public BorrowOutController(PatronStore ps, CopyStore cs) {
		this.pStore = ps;
		this.cStore = cs;
		this.copiesEntered = new ArrayList<Copy>();
		}

	public boolean startOutTransaction() {
		return true;
	}

	public void clearCopiesEntered() {
		this.copiesEntered.clear();
	}

	public Patron enterPatronForCheckOut(String patronID){
		currentPatron = this.pStore.fetchPatrons(patronID);
		return currentPatron; 
	}

	public Copy enterCopyGoingOut(String copyID) {
		copy = this.cStore.fetchCopy(copyID);
		if (!copy.getForSale()) {
			this.copiesEntered.add(copy);
			currentPatron.checkCopyOut(copy);
			StdOut.println(currentPatron);
		}
		return copy;
	}
	
	public boolean endOutTransaction() {
		return false;
	}
	
	public void setCopiesEntered (ArrayList<Copy> copiesEntered){
	     this.copiesEntered = copiesEntered;
	} 
	
	
	public ArrayList<Copy> getCopiesEntered() {
		return copiesEntered;
	}
}


	