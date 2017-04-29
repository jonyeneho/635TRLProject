import static org.junit.Assert.*;
import org.junit.Test;

public class PatronTests {

	@Test
	public void testPatronFound() {
	
		String pid = "P102";	
		CopyPatronStore cpStore = new CopyPatronStore();
		BorrowOutController outController = new BorrowOutController(cpStore);
		
		Patron p = outController.enterPatronForCheckOut(pid);
		
		assertNotNull(p);
		
		assertEquals(p, outController.enterPatronForCheckOut(pid));
		
		assertFalse(p.getHasHold());
		
		StdOut.println("Patron found.");
	}
	
	@Test
	public void testPatronNotFound() {
		
		String pid = "P109";	
		CopyPatronStore cpStore = new CopyPatronStore();
		Patron p = cpStore.fetchPatrons(pid);
		BorrowOutController outController = new BorrowOutController(cpStore);
	
		outController.enterPatronForCheckOut(pid);
		
		p = outController.enterPatronForCheckOut(pid);
		
		assertNull(p);
		StdOut.println("Patron not found.");
		
	}
	
	@Test
	public void testPatronHasHold() {
		CopyPatronStore cpStore = new CopyPatronStore();
		String pid = "P106";	
		Patron p = cpStore.fetchPatrons(pid);
		BorrowOutController outController = new BorrowOutController(cpStore);
	
		outController.enterPatronForCheckOut(pid);
		
		p = outController.enterPatronForCheckOut(pid);
		
		assertNotNull(p);
		assertTrue(p.getHasHold());
		StdOut.println("Patron found but has a hold on record.");
		
	}
}
	
