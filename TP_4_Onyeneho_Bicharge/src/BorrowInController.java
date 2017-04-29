import java.util.*;

public class BorrowInController {
	private CopyPatronStore cpStore;
	private Patron currentPatron;
	private Copy copy;
	private ArrayList<Copy> copiesEntered;
	private BorrowOutController outController;
	

	public BorrowInController(CopyPatronStore cps) {
		this.cpStore = cps;
		outController = new BorrowOutController(cpStore);
		copiesEntered = new ArrayList<Copy>();
	}
	
	public void clearCopiesEntered() {
		this.copiesEntered.clear();
	}

	public boolean startInTransaction() {
		return true;
	}

	public Patron enterPatronForCheckIn(String patronID) {
		this.currentPatron = cpStore.fetchPatrons(patronID);
		return this.currentPatron;
	}

	public Copy enterCopyGoingIn(String copyID) {
		this.copiesEntered = outController.getCopiesEntered();
		copy = this.cpStore.fetchCopy(copyID);
		
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