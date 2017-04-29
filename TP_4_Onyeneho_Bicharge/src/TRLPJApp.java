import java.text.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TRLPJApp {
	private static CopyPatronStore cpStore;
	private static In in = new In();
	private static BorrowOutController outController;
	private static BorrowInController inController;
	private static SalesController sController;
	private static ArrayList<Copy> copiesEntered;
	private final static Logger logger = Logger.getLogger(TRLPJApp.class.getName());
	private static FileHandler fh = null;
	

	public static void main(String[] args) {
			init();
			cpStore = new CopyPatronStore();
			outController = new BorrowOutController(cpStore);
			inController = new BorrowInController(cpStore);
			sController = new SalesController(cpStore);
			copiesEntered = new ArrayList<Copy>();
			StdOut.println("Welcome to TRLPJApp....:\n");
			mainMenu();
	}

	public static void mainMenu() {
		init();
		StdOut.println("Main Menu");
		StdOut.println("~~~~~~~~~~~~~~~");
		StdOut.println("Select option:\n");
		StdOut.println("1 => Begin Checkout transaction");
		StdOut.println("2 => Begin Check-in transaction");
		StdOut.println("3 => Begin Sales transaction");
		StdOut.println("4 => Display Patron Info");
		StdOut.println("5 => Pay Overdue Fines");
		StdOut.println("6 => Change Patron Hold Status");
		StdOut.println("H => Help and Documentation");
		StdOut.println("0 => Quit");
		logger.log(Level.INFO, "Main Menu Log.");
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
			case "5":
				payFines("String");
				quitting = true;
				return;
			case "6":
				setHold("String");
				quitting = true;
				return;
			case "H":
				helpandDocumentation();
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
		logger.log(Level.INFO, "Menu Choice Picked.");
	}

	public static String getCommand() {
		return in.readString();
	}

	public static void doCheckOut() {
		init();
		StdOut.println("Enter Patron ID, 0 to return to the Main Menu: ");
		String pid = StdIn.readString();
		Patron p = outController.enterPatronForCheckOut(pid);
		
		if (p == null && !pid.equals("0")) {
			StdOut.println("Patron does not exist! Please reenter.");
			logger.log(Level.WARNING, "Reentered Patron.");
			doCheckOut();
			return;
		}

		else if (pid.equals("0")) {
			StdOut.println("Returning to the Main Menu .....");
			mainMenu();
			logger.log(Level.INFO, "Exited Checkout Session Voluntarily.");
			return;
		}

		boolean hasHold = p.getHasHold();
		
		if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("Patron has a hold on record. Please clear hold before checking out.");
			StdOut.println("Exiting Checkout process and Returning to the Main Menu .....");
			StdOut.println();
			logger.log(Level.SEVERE, "Exited Checkout Session due to hold.");
			mainMenu();
			return;
		}
		
		else if (p != null && !pid.equals("0") && !hasHold) {
			StdOut.println("You entered: " + pid);
			logger.log(Level.INFO, "Patron hold is correct.");
		}

		boolean result = outController.startOutTransaction();
		StdOut.println("Issuing Out Copies to Patron: " + p);
		logger.log(Level.INFO, "Issuing Copies to Patron.");

		while (result == true) {
			String copyID = getCopyIDForCheckOut();
			Copy cinfo = cpStore.fetchCopy(copyID);

			if (cinfo == null && !copyID.equals("0")) {
				StdOut.println("Copy does not exist! Cannot check out.");
				logger.log(Level.WARNING, "Invalid Copy Entry.");
			}

			else if (copyID.equals("0")) {
				result = outController.endOutTransaction();
				logger.log(Level.INFO, "Exited Checkout Voluntarily.");
				StdOut.println("Returning to the Main Menu .....");
				mainMenu();
				break;
			}

			else {
				if (p.checkPatronOverdue(cinfo)) {
					p.turnOverdueHoldOn();
					logger.log(Level.SEVERE, "Exited Checkout Session due to overdue hold.");
					StdOut.println("Patron has a hold on record due to overdue copies.\n"
							+ "Please clear hold before checking out.");
					StdOut.println("Exiting Checkout process and Returning to the Main Menu .....");
					StdOut.println();
					mainMenu();
					return;
				}
				boolean forSale = cinfo.getForSale();
				String title = cinfo.getTitle();
				double copyPrice = cinfo.getCopyPrice();
				Date duedate = cinfo.getDueDate();
				Copy c = new Copy(copyID, title, copyPrice, forSale, duedate);
				copiesEntered = outController.getCopiesEntered();
				boolean scannedTwice = copiesEntered.contains(c);

				if (cinfo != null && scannedTwice && !copyID.equals("0") && !forSale) {
					StdOut.println("Copy is already checked out! Cannot check out.");
					logger.log(Level.WARNING, "Copy checked out already.");
				}

				else if (cinfo != null && !scannedTwice && !copyID.equals("0") && !forSale) {
					StdOut.println("Checking out Copy: " + copyID);
					c = outController.enterCopyGoingOut(copyID);
					StdOut.println("Copy successfully checked out.");
					StdOut.println("Please return by the end of the day on: " + setDueDate());
					logger.log(Level.INFO, "Copy Checked Out.");
				}

				else if (cinfo != null && !copyID.equals("0") && forSale) {
					StdOut.println("Copy is for sale only and is not available for checkout.");
					logger.log(Level.WARNING, "Copy for sale only.");
				}
			}
		}
	}

	public static String getCopyIDForCheckOut() {
		StdOut.println("Enter copyID to check out, 0 to return to the Main Menu:");
		String copyID = StdIn.readString();
		return copyID;
	}
	
	public static String setDueDate() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_YEAR, 120);
		Date duedate = calendar.getTime();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEEEEEEEEEE, MMMMMMMMMMMM dd, yyyy");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
	    return dateFormat.format(duedate);
	}

	public static void doCheckIn() {
		init();
		StdOut.println("Enter a valid Patron ID, 0 to return to the Main Menu:");
		String pid = StdIn.readString();

		Patron p = inController.enterPatronForCheckIn(pid);
		if (p == null && !pid.equals("0")) {
			StdOut.println("Patron does not exist!");
			logger.log(Level.INFO, "Invalid Patron Input.");
			doCheckIn();
			return;
		}

		else if (pid.equals("0")) {
			StdOut.println("Returning to the Main Menu .....");
			mainMenu();
			logger.log(Level.INFO, "Exited Check-in Session Voluntarily.");
			return;
		}
		
		boolean hasHold = p.getHasHold();
		
		if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("\nWarning! Patron has a hold on record.\n" +
					"Patron may not check out any copies until hold is cleared.\n");
			StdOut.println("You entered: " + pid);
			logger.log(Level.WARNING, "Hold on record warning.");
		}

		else if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("You entered: " + pid);
			logger.log(Level.INFO, "Entered Patron Info.");
		}

		boolean result = inController.startInTransaction();
		StdOut.println("Checking in Copies from Patron: " + p);
		copiesEntered = inController.getCopiesEntered();
		copiesEntered.addAll(outController.getCopiesEntered());

		while (result == true) {
			String copyID = getCopyIDForCheckIn();
			Copy cinfo = cpStore.fetchCopy(copyID);

			if (cinfo == null && !copyID.equals("0")) {
				StdOut.println("Copy does not exist! Cannot check in.");
				logger.log(Level.WARNING, "Invalid copy input.");
			}

			else if (copyID.equals("0")) {
				result = inController.endInTransaction();
				StdOut.println("Returning to the Main Menu .....");
				mainMenu();
			}

			else {
				boolean forSale = cinfo.getForSale();
				String title = cinfo.getTitle();
				double copyPrice = cinfo.getCopyPrice();
				Date duedate = cinfo.getDueDate();
				Copy c = new Copy(copyID, title, copyPrice, forSale, duedate);
				boolean scannedTwice = !outController.getCopiesEntered().contains(c);

				if (cinfo != null && p.getCopiesOut().contains(c) && !scannedTwice
						&& !copyID.equals("0") && !forSale) {
					StdOut.println("Checking in Copy: " + copyID);
					c = inController.enterCopyGoingIn(copyID);
					copiesEntered.remove(c);
					outController.getCopiesEntered().remove(c);
					StdOut.println("Copy successfully checked in.");
					logger.log(Level.INFO, "Copy Checked In.");
				}

				else if (cinfo != null && !p.getCopiesOut().contains(c) && !scannedTwice
						&& !copyID.equals("0") && !forSale) {
					StdOut.println("Copy belongs to another Patron! Cannot check in.");
					logger.log(Level.WARNING, "Copy belongs to another Patron.");
				}

				else if (cinfo != null && scannedTwice && !copyID.equals("0") && !forSale) {
					StdOut.println("Copy already checked in! Cannot check in.");
					logger.log(Level.WARNING, "Copy already checked in.");
				}
				else if (cinfo != null && !copyID.equals("0") && forSale) {
					StdOut.println("Copy is for sale only and cannot be checked in.");
					logger.log(Level.WARNING, "Copy for sale only.");
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
		init();
		StdOut.println("Enter Patron ID, 0 to return to the Main Menu: ");
		String pid = StdIn.readString();
		Patron p = sController.enterPatronForSale(pid);
		if (p == null && !pid.equals("0")) {
			StdOut.println("Patron does not exist! Please reenter.");
			logger.log(Level.WARNING, "Invalid Patron Input.");
			doSellCopy();
			return;
		}

		else if (pid.equals("0")) {
			StdOut.println("Returning to the Main Menu .....");
			logger.log(Level.INFO, "Exited Sale Session Voluntarily.");
			mainMenu();
			return;
		}
		
		boolean hasHold = p.getHasHold();
		
		if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("\nWarning! Patron has a hold on record.\n" +
		"Patron may not check out any copies until hold is cleared.\n");
			logger.log(Level.WARNING, "Patron cannot check out copies.");
			StdOut.println("You entered: " + pid);
		}

		else if (p != null && !pid.equals("0") && hasHold) {
			StdOut.println("You entered: " + pid);
			logger.log(Level.INFO, "Patron input.");
		}

		boolean result = sController.startSaleTransaction();
		StdOut.println("Selling Copies to Patron: " + p);

		while (result == true) {
			String copyID = getCopyIDForSale();

			Copy cinfo = cpStore.fetchCopy(copyID);

			if (cpStore.fetchCopy(copyID) == null && !copyID.equals("0")) {
				StdOut.println("Copy does not exist! Cannot sell.");
				logger.log(Level.WARNING, "Invalid Copy Input.");
			}

			else if (copyID.equals("0")) {
				result = sController.endSaleTransaction();
				StdOut.println("Returning to the Main Menu .....");
				logger.log(Level.INFO, "Exited Sale Session Voluntarily.");
				mainMenu();
				break;
			}

			else {

				boolean forSale = cinfo.getForSale();
				String title = cinfo.getTitle();
				double copyPrice = cinfo.getCopyPrice();
				Date duedate = cinfo.getDueDate();
				Copy c = new Copy(copyID, title, copyPrice, forSale, duedate);
				copiesEntered = sController.getCopiesEntered();
				boolean scannedTwice = copiesEntered.contains(c);

				if (cpStore.fetchCopy(copyID) != null && scannedTwice && !copyID.equals("0") && forSale) {
					logger.log(Level.SEVERE, "Copy sold already.");
					StdOut.println("Copy is already sold! Cannot sell.");
				}

				else if (cpStore.fetchCopy(copyID) != null && !scannedTwice && !copyID.equals("0") && forSale) {
					NumberFormat formatter = NumberFormat.getCurrencyInstance();
					formatter.setMinimumIntegerDigits(1);
					StdOut.println("Copy price: " + formatter.format(copyPrice) + ".");
					StdOut.println("Enter the amount you wish to pay: ");
					String numinput = StdIn.readString();
					@SuppressWarnings("unused")
					boolean parsable;

					try {
						double paymentamount = Double.parseDouble(numinput);
						parsable = true;
						if (paymentamount > copyPrice) {
							StdOut.println("\nAmount exceeds copy price.\n" +
									"Payment transaction not processed.\n");
							logger.log(Level.SEVERE, "Payment does not equal price.");
							doSellCopy();
							return;
						} 
						
						else if (paymentamount <= 0) {
							StdOut.println("\nPayment amount is less than or equal to $0.00.\n" +
									"Payment transaction not processed.\n");
							logger.log(Level.SEVERE, "Payment does not equal price.");
							doSellCopy();
							return;
						}
						
						
						else if (paymentamount > 0 && paymentamount < copyPrice) {
							StdOut.println("\nPayment amount is less than copy price.\n" +
									"Payment transaction not processed.\n");
							logger.log(Level.SEVERE, "Payment does not equal price.");
							doSellCopy();
							return;
						}
						
						else if (paymentamount == copyPrice){
							StdOut.println("Payment transaction successful.");
							logger.log(Level.INFO, "Payment transaction made.");
						}
					}

					catch (NumberFormatException e) {
						parsable = false;
						StdOut.println("Input is not a valid amount.");
						logger.log(Level.SEVERE, "Invalid price input.");
						doSellCopy();
						return;
					}
					
					StdOut.println("Selling Copy: " + copyID);
					c = sController.enterCopySelling(copyID);
					StdOut.println("Copy is successfully sold for $" + copyPrice + ".");
					logger.log(Level.INFO, "Copy Sold to Patron.");
				}

				else if (cpStore.fetchCopy(copyID) != null && !copyID.equals("0") && !forSale) {
					StdOut.println("Copy is for checkout only and is not for sale.");
					logger.log(Level.SEVERE, "Copy is not for sale.");
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
		init();
		String patronID = pid;

		while (!patronID.equals("0") && patronID != null) {

			StdOut.println("Enter a valid Patron ID, 0 to return to the Main Menu:");
			patronID = StdIn.readString();
			Patron patronInfo = cpStore.fetchPatrons(patronID);
			

			if (!patronID.equals("0") && patronInfo != null) {
				StdOut.println(patronInfo);
				logger.log(Level.INFO, "Patron info.");
			}

			if (!patronID.equals("0") && patronInfo == null) {
				StdOut.println("Patron does not exist!");
				logger.log(Level.WARNING, "Invalid Patron Input.");
			}
		}

		StdOut.println("Returning to the Main Menu .....");
		mainMenu();
	}
	
	public static void payFines(String pid) {
		init();
		String patronID = pid;

		while (!patronID.equals("0") && patronID != null) {
			StdOut.println("Enter a valid Patron ID, 0 to return to the Main Menu:");
			patronID = StdIn.readString();
			Patron patronInfo = cpStore.fetchPatrons(patronID);
			

			if (!patronID.equals("0") && patronInfo == null) {
				StdOut.println("Patron does not exist!");
				logger.log(Level.WARNING, "Invalid Patron Input.");
				
			}

			else if (!patronID.equals("0") && patronInfo != null) {
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				formatter.setMinimumIntegerDigits(1);
				String name = patronInfo.getName();
				double fines = patronInfo.getOverdueFines();
				StdOut.println("Patron: " + name + ", Overdue fines: " + formatter.format(fines) + ".");
				StdOut.println("Enter the amount you wish to pay: ");
				String numinput = StdIn.readString();
				@SuppressWarnings("unused")
				boolean parsable;

				try {
					double paymentamount = Double.parseDouble(numinput);
					parsable = true;
					if (paymentamount > fines) {
						StdOut.println("\nAmount exceeds overdue fine amount\n" +
								"Payment transaction not processed.\n");
						logger.log(Level.WARNING, "Payment too much.");
					} 
					
					else if (paymentamount <= 0) {
						StdOut.println("\nPayment amount is less than or equal to $0.00.\n" +
								"Payment transaction not processed.\n");
						logger.log(Level.WARNING, "Invalid fine payment.");
					}
					
					else {
						double newfines = fines - paymentamount;
						patronInfo.setOverdueFines(newfines);
						fines = patronInfo.getOverdueFines();
						StdOut.println("Payment transaction successful.");
						StdOut.println(name + " has an outstanding balance of: " 
						+ formatter.format(fines) + ".");
						logger.log(Level.INFO, "Successful fine payment.");
					}
				}

				catch (NumberFormatException e) {
					parsable = false;
					StdOut.println("Input is not a valid amount.");
				}

			}
		}

		StdOut.println("Returning to the Main Menu .....");
		mainMenu();
	}

	public static void setHold(String pid) {
		String patronID = pid;

		while (!patronID.equals("0") && patronID != null) {

			StdOut.println("Enter a valid Patron ID, 0 to return to the Main Menu:");
			patronID = StdIn.readString();
			Patron patronInfo = cpStore.fetchPatrons(patronID);
			@SuppressWarnings("unused")
			Copy copyt = new Copy("1002");
			
			if (!patronID.equals("0") && patronInfo == null) {
				StdOut.println("Patron does not exist!");
				logger.log(Level.WARNING, "Invalid Patron Input.");
			}
			
			
			else if (!patronID.equals("0") && patronInfo != null) {
			
				boolean hasHold = patronInfo.getHasHold();
				String yesorno = hasHold ? " has a hold and cannot check out copies." 
						: " does not have any holds and can check out copies.";
				logger.log(Level.WARNING, "Patron has a hold and cannot check out.");
				
				StdOut.println(patronInfo.getName() + yesorno);
				
				StdOut.println("Enter 1 to change hold status, enter any other key to return to the Main Menu:");
				String setholdchoice = StdIn.readString();

				if (setholdchoice.equals("1") && hasHold) {
					hasHold = patronInfo.turnHoldOff();
					yesorno = hasHold ? " has a hold and cannot check out copies." 
							: " does not have any holds and can check out copies.";
					logger.log(Level.INFO, "Patron can check out.");
					StdOut.println(patronInfo.getName() + yesorno);
				}

				else if (setholdchoice.equals("1") && !hasHold) {
					hasHold = patronInfo.turnHoldOn();
					yesorno = hasHold ? " has a hold and cannot check out copies." 
							: " does not have any holds and can check out copies.";
					logger.log(Level.WARNING, "Patron has a hold and cannot check out.");
					StdOut.println(patronInfo.getName() + yesorno);
				}
						
				else if (!setholdchoice.equals("1")) {
					mainMenu();
					logger.log(Level.INFO, "Returns to main menu.");
					return;
				}
			}
		}
		
		StdOut.println("Returning to the Main Menu .....");
		mainMenu();
	}
	
	public static void init() {
		try {
			fh = new FileHandler("files/log.txt", true);
		}

		catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		Logger l = Logger.getLogger("");
		fh.setFormatter(new SimpleFormatter());
		l.addHandler(fh);
		l.setLevel(Level.CONFIG);
	}
	
	public static void helpandDocumentation() {
		init();
	    Charset encoding = Charset.forName("UTF-8");
	    Path file = Paths.get("files/trlpjapphelpfile.txt");  
	  try(BufferedReader br = Files.newBufferedReader(file, encoding)) {
	        String line;
	        while((line = br.readLine()) != null) {
	            System.out.println(line);
	        }
	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	  logger.log(Level.INFO, "Displayed Help and Documentation File.");
	  StdOut.println("Returning to the Main Menu .....");
		mainMenu();
	}
	

}
