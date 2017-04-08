import java.util.ArrayList;

public class TRLPJApp {
	private static CopyStore cStore;
	private static PatronStore pStore;
	private static In in = new In();
	private static BorrowOutController outController;
	private static BorrowInController inController;
	private static SalesController sController;
	private static ArrayList<Copy> copiesEntered;

	public static void main(String[] args) {
		pStore = new PatronStore();
		cStore = new CopyStore();
		outController = new BorrowOutController(pStore, cStore);
		inController = new BorrowInController(pStore, cStore);
		sController = new SalesController(pStore, cStore);
		copiesEntered = new ArrayList<Copy>();
		StdOut.println("Welcome to TRLPJApp....:\n");
		mainMenu();
	}

	public static void mainMenu() {
		StdOut.println("Main Menu");
		StdOut.println("~~~~~~~~~~~~~~~");
		StdOut.println("Select option:\n");
		StdOut.println("1 => Begin Checkout transaction");
		StdOut.println("2 => Begin Check-in transaction");
		StdOut.println("3 => Begin Sales transaction");
		StdOut.println("4 => Display Patron Info");
		StdOut.println("0 => Quit");

		String cmd = getCommand();

		boolean quitting = false;
		while (!quitting) {
			switch (cmd) {

			case "1":
				StdOut.println("Checking copies out .....");
				doCheckOut();
				return;
			case "2":
				StdOut.println("Checking copies in .....");
				doCheckIn();
				return;
			case "3":
				StdOut.println("Selling copies to Patron .....");
				doSellCopy();
				return;
			case "4":
				fetchPatrons("String");
				quitting = true;
				return;
			case "0":
				StdOut.println("Exiting...");
				quitting = true;
				break;
			default:
				StdOut.println("Invalid option! Returning to the Main Menu .....");
				mainMenu();
				return;
			}
		}

	}

	public static String getCommand() {
		return in.readString();
	}

	public static void doCheckOut() {
		StdOut.println("Enter Patron ID, 0 to return to the Main Menu: ");
		String pid = StdIn.readString();
		Patron p = outController.enterPatronForCheckOut(pid);
		
		if (p == null && !pid.equals("0")) {
			StdOut.println("Patron does not exist! Please reenter.");
			doCheckOut();
			return;
		}

		else if (pid.equals("0")) {
			StdOut.println("Returning to the Main Menu .....");
			mainMenu();
			return;
		}

		boolean hasHold = p.getHasHold();
		
		if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("Patron has a hold on record. Please clear hold before checking out.");
			StdOut.println("Exiting Checkout process and Returning to the Main Menu .....");
			StdOut.println();
			mainMenu();
			return;
		}
		
		else if (p != null && !pid.equals("0") && !hasHold) {
			StdOut.println("You entered: " + pid);
		}

		boolean result = outController.startOutTransaction();
		StdOut.println("Issuing Out Copies to Patron: " + p);

		while (result == true) {
			String copyID = getCopyIDForCheckOut();
			Copy cinfo = cStore.fetchCopy(copyID);

			if (cinfo == null && !copyID.equals("0")) {
				StdOut.println("Copy does not exist! Cannot check out.");
			}

			else if (copyID.equals("0")) {
				result = outController.endOutTransaction();
				StdOut.println("Returning to the Main Menu .....");
				mainMenu();
				break;
			}

			else {
				boolean forSale = cinfo.getForSale();
				double copyPrice = cinfo.getCopyPrice();
				Copy c = new Copy(copyID, copyPrice, forSale);
				copiesEntered = outController.getCopiesEntered();
				boolean scannedTwice = copiesEntered.contains(c);

				if (cinfo != null && scannedTwice && !copyID.equals("0") && !forSale) {
					System.out.println("Copy is already checked out! Cannot check out.");
				}

				else if (cinfo != null && !scannedTwice && !copyID.equals("0") && !forSale) {
					StdOut.println("Checking out Copy: " + copyID);
					c = outController.enterCopyGoingOut(copyID);
					StdOut.println("Copy successfully checked out.");
				}

				else if (cinfo != null && !copyID.equals("0") && forSale) {
					StdOut.println("Copy is for sale only and is not available for checkout.");
				}
			}
		}
	}

	public static String getCopyIDForCheckOut() {
		StdOut.println("Enter copyID to check out, 0 to return to the Main Menu:");
		String copyID = StdIn.readString();
		return copyID;
	}

	public static void doCheckIn() {
		StdOut.println("Enter a valid Patron ID, 0 to return to the Main Menu:");
		String pid = StdIn.readString();

		Patron p = inController.enterPatronForCheckIn(pid);
		if (p == null && !pid.equals("0")) {
			StdOut.println("Patron does not exist!");
			doCheckIn();
			return;
		}

		else if (pid.equals("0")) {
			StdOut.println("Returning to the Main Menu .....");
			mainMenu();
			return;
		}
		
		boolean hasHold = p.getHasHold();
		
		if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("\nWarning! Patron has a hold on record.\n" +
		"Patron may not check out any copies until hold is cleared.\n");
			StdOut.println("You entered: " + pid);
		}

		else if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("You entered: " + pid);
		}

		boolean result = inController.startInTransaction();
		StdOut.println("Checking in Copies from Patron: " + p);
		copiesEntered = inController.getCopiesEntered();
		copiesEntered.addAll(outController.getCopiesEntered());

		while (result == true) {
			String copyID = getCopyIDForCheckIn();
			Copy cinfo = cStore.fetchCopy(copyID);

			if (cinfo == null && !copyID.equals("0")) {
				StdOut.println("Copy does not exist! Cannot check in.");
			}

			else if (copyID.equals("0")) {
				result = inController.endInTransaction();
				StdOut.println("Returning to the Main Menu .....");
				mainMenu();
			}

			else {
				boolean forSale = cinfo.getForSale();
				double copyPrice = cinfo.getCopyPrice();
				Copy c = new Copy(copyID, copyPrice, forSale);
				boolean scannedTwice = !outController.getCopiesEntered().contains(c);

				if (cinfo != null && p.getCopiesOut().contains(c) && !scannedTwice
						&& !copyID.equals("0") && !forSale) {
					StdOut.println("Checking in Copy: " + copyID);
					c = inController.enterCopyGoingIn(copyID);
					copiesEntered.remove(c);
					outController.getCopiesEntered().remove(c);
					StdOut.println("Copy successfully checked in.");
				}

				else if (cinfo != null && !p.getCopiesOut().contains(c) && !scannedTwice
						&& !copyID.equals("0") && !forSale) {
					StdOut.println("Copy belongs to another Patron! Cannot check in.");
				}

				else if (cinfo != null && scannedTwice && !copyID.equals("0") && !forSale) {
					StdOut.println("Copy already checked in! Cannot check in.");
				}
				else if (cinfo != null && !copyID.equals("0") && forSale) {
					StdOut.println("Copy is for sale only and cannot be checked in.");
				}
			}
		}
	}

	public static String getCopyIDForCheckIn() {
		StdOut.println("Enter copyID to check in, 0 to return to the Main Menu:");
		String copyID = StdIn.readString();
		return copyID;
	}

	public static void doSellCopy() {
		StdOut.println("Enter Patron ID, 0 to return to the Main Menu: ");
		String pid = StdIn.readString();
		Patron p = sController.enterPatronForSale(pid);
		if (p == null && !pid.equals("0")) {
			StdOut.println("Patron does not exist! Please reenter.");
			doSellCopy();
			return;
		}

		else if (pid.equals("0")) {
			StdOut.println("Returning to the Main Menu .....");
			mainMenu();
			return;
		}
		
		boolean hasHold = p.getHasHold();
		
		if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("\nWarning! Patron has a hold on record.\n" +
		"Patron may not check out any copies until hold is cleared.\n");
			StdOut.println("You entered: " + pid);
		}

		else if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("You entered: " + pid);
		}

		boolean result = sController.startSaleTransaction();
		StdOut.println("Selling Copies to Patron: " + p);

		while (result == true) {
			String copyID = getCopyIDForSale();

			Copy cinfo = cStore.fetchCopy(copyID);

			if (cStore.fetchCopy(copyID) == null && !copyID.equals("0")) {
				StdOut.println("Copy does not exist! Cannot sell.");
			}

			else if (copyID.equals("0")) {
				result = sController.endSaleTransaction();
				StdOut.println("Returning to the Main Menu .....");
				mainMenu();
				break;
			}

			else {

				boolean forSale = cinfo.getForSale();
				double copyPrice = cinfo.getCopyPrice();
				Copy c = new Copy(copyID, copyPrice, forSale);
				copiesEntered = sController.getCopiesEntered();
				boolean scannedTwice = copiesEntered.contains(c);

				if (cStore.fetchCopy(copyID) != null && scannedTwice && !copyID.equals("0") && forSale) {
					System.out.println("Copy is already sold! Cannot sell.");
				}

				else if (cStore.fetchCopy(copyID) != null && !scannedTwice && !copyID.equals("0") && forSale) {
					StdOut.println("Selling Copy: " + copyID);
					c = sController.enterCopySelling(copyID);
					StdOut.println("Copy is successfully sold for $" + copyPrice + ".");
				}

				else if (cStore.fetchCopy(copyID) != null && !copyID.equals("0") && !forSale) {
					StdOut.println("Copy is for checkout only and is not for sale.");
				}
			}
		}
	}

	public static String getCopyIDForSale() {
		StdOut.println("Enter copyID to buy, 0 to return to the Main Menu:");
		String copyID = StdIn.readString();
		return copyID;
	}

	public static void fetchPatrons(String pid) {
		String patronID = pid;

		while (!patronID.equals("0") && patronID != null) {

			StdOut.println("Enter a valid Patron ID, 0 to return to the Main Menu:");
			patronID = StdIn.readString();
			Patron patronInfo = pStore.fetchPatrons(patronID);

			if (!patronID.equals("0") && patronInfo != null) {
				StdOut.println(patronInfo);
			}

			if (!patronID.equals("0") && patronInfo == null) {
				StdOut.println("Patron does not exist!");
			}
		}

		StdOut.println("Returning to the Main Menu .....");
		mainMenu();
	}
}
