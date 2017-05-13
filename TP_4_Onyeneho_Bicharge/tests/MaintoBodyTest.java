import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.logging.Level;

import org.junit.Test;

public class MaintoBodyTest {

	@Test
	public void mainBodyTest() {
		
		MaintoBodyTest mtob = new MaintoBodyTest();
		mtob.body();
		assertNotNull(mtob);
	}



public void body() {
	
	StdOut.println("Welcome to the Textbook Rental Library Project Application (TRLPJApp)!\n");
	mainMenu();
}
public void mainMenu() {
	StdOut.println("Main Menu");
	StdOut.println("~~~~~~~~~~~~~~~");
	StdOut.println("Select option:\n");
	StdOut.println("1 => Begin Checkout Transaction");
	StdOut.println("2 => Begin Check-in Transaction");
	StdOut.println("3 => Begin Sales Transaction");
	StdOut.println("4 => Display Patron Info");
	StdOut.println("5 => Pay Overdue Fines");
	StdOut.println("6 => Change Patron Hold Status");
	StdOut.println("H => Help and Documentation");
	StdOut.println("0 => Quit");
	
	
}
}
