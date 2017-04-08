import java.util.ArrayList;

public class BorrowInController {
	private PatronStore pStore;
	private CopyStore cStore;
	private Patron currentPatron;
	private Copy copy;
	private ArrayList<Copy> copiesEntered;
	private BorrowOutController outController;
	

	public BorrowInController(PatronStore ps, CopyStore cs) {
		this.pStore = ps;
		this.cStore = cs;
		outController = new BorrowOutController(pStore, cStore);
		copiesEntered = new ArrayList<Copy>();
	}
	
	public void clearCopiesEntered() {
		this.copiesEntered.clear();
	}

	public boolean startInTransaction() {
		return true;
	}

	public Patron enterPatronForCheckIn(String patronID) {
		this.currentPatron = pStore.fetchPatrons(patronID);
		return this.currentPatron;
	}

	public Copy enterCopyGoingIn(String copyID) {
		this.copiesEntered = outController.getCopiesEntered();
		copy = this.cStore.fetchCopy(copyID);
		
		if (!copy.getForSale()) {
			currentPatron.checkCopyIn(copy);
			this.copiesEntered.remove(copy);
			StdOut.println(currentPatron);
		}
		return copy;
	}

	public boolean endInTransaction() {
		return false;
	}
	
	public void setCopiesEntered(ArrayList<Copy> copiesEntered) {
		this.copiesEntered = copiesEntered;
	}
	public ArrayList<Copy> getCopiesEntered() {
		return copiesEntered;
	}

	public static void main(String[] args) {
	}
}