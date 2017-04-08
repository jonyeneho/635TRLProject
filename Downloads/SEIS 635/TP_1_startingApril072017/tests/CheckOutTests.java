import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;



public class CheckOutTests {
	private String pid;
	private String cid;
	private PatronStore pStore;
	private CopyStore cStore;
	private Patron p;
	private Copy c;
	private BorrowOutController outController;
	private ArrayList<Copy> copiesEnteredforCheckout;

	@Test
	public void testCopiesCheckedOut() {
		pStore = new PatronStore();
		cStore = new CopyStore();
		p = pStore.fetchPatrons(pid);
		c = cStore.fetchCopy(cid);
		outController = new BorrowOutController(pStore, cStore);
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
	
	public void printLines() {
		StdOut.println("");
		StdOut.println("");
		StdOut.println("_____________________________________________________________________");
		StdOut.println("");
		StdOut.println("");
	}
}