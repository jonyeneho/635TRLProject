import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;



public class CheckInTests {
	private String pid;
	private String cid;
	private PatronStore pStore;
	private CopyStore cStore;
	private Patron p;
	private Copy c;
	private BorrowOutController outController;
	private BorrowInController inController;
	private ArrayList<Copy> copiesEnteredforCheckout;

	@Test
	public void testCopiesCheckedIn() {
		pStore = new PatronStore();
		cStore = new CopyStore();
		p = pStore.fetchPatrons(pid);
		c = cStore.fetchCopy(cid);
		outController = new BorrowOutController(pStore, cStore);
		inController = new BorrowInController(pStore, cStore);
		copiesEnteredforCheckout = outController.getCopiesEntered();	

		checkOutProcess("P101", "001");
		printLines();
		checkOutProcess("P101", "003");
		printLines();
		checkOutProcess("P101", "005");
		printLines();
		checkOutProcess("P102", "001");
		printLines();
		checkOutProcess("P102", "002");
		printLines();
		checkOutProcess("P106", "006");
		printLines();
		checkOutProcess("P102", "003");
		printLines();
		checkOutProcess("P102", "004");
		printLines();
		checkOutProcess("P102", "005");
		printLines();
		checkOutProcess("P102", "002");
		printLines();
		checkOutProcess("P104", "004");
		printLines();
		checkOutProcess("P101", "004");
		
		StdOut.println("END CHECKOUTS");
		printLines();
				
		checkInProcess("P101", "001");
		printLines();
		checkInProcess("P101", "002");
		printLines();
		checkInProcess("P101", "003");
		printLines();
		checkInProcess("P101", "004");
		printLines();
		checkInProcess("P101", "005");
		StdOut.println("END CHECK-INS");
		
		pid = "P107";
		p = pStore.fetchPatrons(pid);
		assertNull(p);
		
		cid = "018";
		c = cStore.fetchCopy(cid);
		assertNull(c);	
		
	}
	
	public void checkOutProcess(String pid, String cid) {

		c = cStore.fetchCopy(cid);
		p = outController.enterPatronForCheckOut(pid);

		if (c == null && !cid.equals("0")) {
			StdOut.println("Copy does not exist! Cannot check out.");
			return;
		}

		if (p == null && c != null && !cid.equals("0")) {
			StdOut.println("Patron does not exist! Cannot start checkout process.");
			return;
		}

		if (cid.equals("0")) {
			StdOut.println("CHECKOUT LOOP END");
			printLines();
			return;
		}

		boolean forSale = c.getForSale();
		boolean hasHold = p.getHasHold();

		if (hasHold) {
			StdOut.println("Patron has a hold. Cannot checkout.");
			return;
		}

		else if (forSale) {
			StdOut.println("Copy is for sale only and not for checkout!");
		}

		else {

			boolean scannedTwice = copiesEnteredforCheckout.contains(c);

			if (c != null && scannedTwice && !cid.equals("0") && !hasHold && !forSale) {
				System.out.println("Copy is already checked out! Cannot check out.");
			}

			else if (c != null && !scannedTwice && !cid.equals("0") && !hasHold && !forSale) {
				StdOut.println("Checking out Copy: " + cid);
				c = outController.enterCopyGoingOut(cid);
				StdOut.println("Copy successfully checked out.");
			}

		}

	}

	public void checkInProcess(String pid, String cid) {
		c = cStore.fetchCopy(cid);
		p = inController.enterPatronForCheckIn(pid);

		if (c == null && !cid.equals("0")) {
			StdOut.println("Copy does not exist! Cannot check in.");
		}

		else if (p == null && c != null && !cid.equals("0")) {
			StdOut.println("Patron does not exist! Cannot start checkout process.");
			return;
		}

		else if (cid.equals("0")) {
			StdOut.println("CHECK-IN LOOP END");
			printLines();
			return;
		}

		copiesEnteredforCheckout = inController.getCopiesEntered();
		copiesEnteredforCheckout.addAll(outController.getCopiesEntered());

		boolean forSale = c.getForSale();
		boolean hasHold = p.getHasHold();

		if (hasHold) {
			StdOut.println("Warning! Patron has a hold on record!");
		}

		if (forSale) {
			StdOut.println("Copy is for sale only and not for check-in!");
			return;
		}

		else {

			boolean scannedTwice = !outController.getCopiesEntered().contains(c);

			if (c != null && p.getCopiesOut().contains(c) && !scannedTwice && !cid.equals("0")) {
				StdOut.println("Checking in Copy: " + cid);
				c = inController.enterCopyGoingIn(cid);
				copiesEnteredforCheckout.remove(c);
				outController.getCopiesEntered().remove(c);
				StdOut.println("Copy successfully checked in.");
			}

			else if (c != null && !p.getCopiesOut().contains(c) && !scannedTwice && !cid.equals("0")) {
				StdOut.println("Copy belongs to another Patron! Cannot check in.");
			}

			else if (c != null && scannedTwice && !cid.equals("0")) {
				StdOut.println("Copy already checked in! Cannot check in.");
			}
		}
	}
	
	public void printLines() {
		StdOut.println("");
		StdOut.println("");
		StdOut.println("_____________________________________________________________________");
		StdOut.println("");
		StdOut.println("");
	}
}

