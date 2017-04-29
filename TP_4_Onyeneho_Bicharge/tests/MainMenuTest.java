import static org.junit.Assert.*;

import org.junit.Test;

public class MainMenuTest {

	private CheckInTests chkin;
	private CheckOutTests chkout;
	private PatronTests ptst;
	private FetchPatronsTest fetchpat;
	private boolean quitting;
	
	@Test
	public void testMainMenu() {
		chkin = new CheckInTests();
		chkout = new CheckOutTests();
		ptst = new PatronTests();
		fetchpat = new FetchPatronsTest();
		mainMenu("1");
		assertFalse(quitting);
		mainMenu("2");
		assertFalse(quitting);
		mainMenu("3");
		assertTrue(quitting);
		mainMenu("4");
		assertFalse(quitting);
		mainMenu("0");
		assertTrue(quitting);
		mainMenu("9");
		assertFalse(quitting);
		
		String cmd = "2";
		
		mainMenu(cmd);
		assertFalse(quitting);
		
	}
	
	public void mainMenu(String cmd) {
		StdOut.println("Main Menu");
		StdOut.println("~~~~~~~~~~~~~~~");
		StdOut.println("Select option:\n");
		StdOut.println("1 => Test Checkouts");
		StdOut.println("2 => Test Checkout & Check-in");
		StdOut.println("3 => Test Patron Found");
		StdOut.println("4 => Test Fetch Patrons");
		StdOut.println("0 => Quit");
		
		
		quitting = false;
		while (!quitting) {
			switch (cmd) {
			case "1":
				StdOut.println("Checking copies out .....");
				chkout.testCopiesCheckedOut();
				StdOut.println("END TEST");
				printLines();
				return;
			case "2":
				StdOut.println("Checking copies in .....");
				chkin.testCopiesCheckedIn();
				StdOut.println("END TEST");
				printLines();
				return;
			case "3":
				ptst.testPatronFound();
				ptst.testPatronNotFound();
				StdOut.println("END TEST");
				printLines();
				quitting = true;
				return;
			case "4":
				StdOut.println("Testing Fetch Copy Process .....");
				fetchpat.testFetchPatrons();
				StdOut.println("END TEST");
				printLines();
				return;
			case "0":
				StdOut.println("Exiting...");
				StdOut.println("END TEST");
				printLines();
				quitting = true;
				break;
			default:
				StdOut.println("Invalid option! Returning to the Main Menu .....");
				StdOut.println("END TEST");
				printLines();
				return;
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
	
	public void setQuitting(boolean quitting) {
		this.quitting = quitting;
	}
	
	public boolean getQuitting() {
		return quitting;
	}
}
