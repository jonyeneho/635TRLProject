import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class FetchPatronsTest {
	private String pid;
	private String cid;
	private CopyPatronStore cpStore;
	private Patron p;
	private Copy c;
	private BorrowOutController outController;
	private BorrowInController inController;
	private SalesController sController;
	private ArrayList<Copy> copiesEnteredforCheckout;
	private ArrayList<Copy> copiesEnteredforSales;

	@Test
	public void testFetchPatrons() {
		cpStore = new CopyPatronStore();
		p = cpStore.fetchPatrons(pid);
		c = cpStore.fetchCopy(cid);
		outController = new BorrowOutController(cpStore);
		inController = new BorrowInController(cpStore);
		sController = new SalesController(cpStore);
		copiesEnteredforCheckout = outController.getCopiesEntered();
		copiesEnteredforSales = sController.getCopiesEntered();

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
		checkOutProcess("P103", "007");
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
		printLines();
		checkOutProcess("P101", "0");
		printLines();
		checkOutProcess("P105", "003");
		printLines();
		checkOutProcess("P107", "009");
		printLines();
		checkOutProcess("P103", "008");

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
		printLines();
		checkInProcess("P101", "0");
		printLines();
		checkInProcess("P102", "005");
		printLines();
		checkInProcess("P107", "005");
		printLines();
		checkInProcess("P105", "005");
		printLines();
		checkInProcess("P102", "009");
		printLines();
		checkInProcess("P101", "010");

		StdOut.println("END CHECK-INS");
		printLines();

		SalesProcess("P101", "004");
		printLines();
		SalesProcess("P103", "006");
		printLines();
		SalesProcess("P102", "005");
		printLines();
		SalesProcess("P106", "009");
		printLines();
		SalesProcess("P102", "010");
		printLines();
		SalesProcess("P108", "004");
		printLines();
		SalesProcess("P105", "007");
		printLines();
		SalesProcess("P101", "011");
		printLines();
		StdOut.println("END SALES");

		fetchPatronProcess("P101");
		printLines();
		fetchPatronProcess("P102");
		printLines();
		fetchPatronProcess("P103");
		printLines();
		fetchPatronProcess("P104");
		printLines();
		fetchPatronProcess("P105");
		printLines();
		fetchPatronProcess("P106");
		printLines();
		fetchPatronProcess("P107");
		printLines();
		fetchPatronProcess("P111");
		printLines();
		fetchPatronProcess("P113");
		printLines();
		fetchPatronProcess("0");
		printLines();
		fetchPatronProcess("P102");

		StdOut.println("END FETCH PATRONS");

		String namecheck = "John";
		pid = "P102";
		assertEquals(cpStore.fetchPatrons(pid).getName(), namecheck);
	}

	public void fetchPatronProcess(String pid) {

		while (!pid.equals("0") && pid != null) {

			StdOut.println("Enter a valid Patron ID, 0 to return to the Main Menu:");

			Patron patronInfo = cpStore.fetchPatrons(pid);
			if (!pid.equals("0") && patronInfo == null) {
				StdOut.println("Patron does not exist!");
				break;
			}

			if (!pid.equals("0") && patronInfo != null) {
				boolean hasHold = patronInfo.getHasHold();

				if (!pid.equals("0") && patronInfo != null && !hasHold) {
					StdOut.println(patronInfo);
				}

				else if (!pid.equals("0") && patronInfo != null && hasHold) {
					StdOut.println("Patron has a hold on record.\n");
					StdOut.println(patronInfo);
				}
			}

			break;
		}

		if (pid.equals("0")) {
			StdOut.println("FETCH LOOP END");
			printLines();
		}

	}

	public void checkOutProcess(String pid, String cid) {

		c = cpStore.fetchCopy(cid);
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
		c = cpStore.fetchCopy(cid);
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

	public void SalesProcess(String pid, String cid) {

		c = cpStore.fetchCopy(cid);
		p = sController.enterPatronForSale(pid);

		if (c == null && !cid.equals("0")) {
			StdOut.println("Copy does not exist! Cannot sell.");
			return;
		}

		if (p == null && c != null && !cid.equals("0")) {
			StdOut.println("Patron does not exist! Cannot start sales process.");
			return;
		}

		if (cid.equals("0")) {
			StdOut.println("SALES LOOP END");
			printLines();
			return;
		}

		boolean forSale = c.getForSale();
		boolean hasHold = p.getHasHold();

		if (!forSale) {
			StdOut.println("Copy is for checkouts only and not for sale!");
			return;
		}

		else {

			boolean scannedTwice = copiesEnteredforSales.contains(c);

			if (c != null && scannedTwice && !cid.equals("0") && forSale) {
				if (hasHold) {
					StdOut.println("Warning! Patron has a hold on record!");
				}
				System.out.println("Copy is already sold! Cannot sell.");
			}

			else if (c != null && !scannedTwice && !cid.equals("0") && forSale) {
				if (hasHold) {
					StdOut.println("Warning! Patron has a hold on record!");
				}
				StdOut.println("Selling Copy: " + cid);
				c = sController.enterCopySelling(cid);
				StdOut.println("Copy successfully sold.");
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
