import static org.junit.Assert.*;
import org.junit.Test;

public class PatronTests {

	@Test
	public void testPatronFound() {
	
		String pid = "P102";	
		PatronStore pStore = new PatronStore();
		CopyStore cStore = new CopyStore();
		BorrowOutController outController = new BorrowOutController(pStore, cStore);
		
		Patron p = outController.enterPatronForCheckOut(pid);
		
		assertNotNull(p);
		
		assertEquals(p, outController.enterPatronForCheckOut(pid));
		
		assertFalse(p.getHasHold());
		
		StdOut.println("Patron found.");
	}
	
	@Test
	public void testPatronNotFound() {
		
		String pid = "P109";	
		PatronStore pStore = new PatronStore();
		CopyStore cStore = new CopyStore();
		Patron p = pStore.fetchPatrons(pid);
		BorrowOutController outController = new BorrowOutController(pStore, cStore);
	
		outController.enterPatronForCheckOut(pid);
		
		p = outController.enterPatronForCheckOut(pid);
		
		assertNull(p);
		StdOut.println("Patron not found.");
		
	}
	
	@Test
	public void testPatronHasHold() {
		
		String pid = "P106";	
		PatronStore pStore = new PatronStore();
		CopyStore cStore = new CopyStore();
		Patron p = pStore.fetchPatrons(pid);
		BorrowOutController outController = new BorrowOutController(pStore, cStore);
	
		outController.enterPatronForCheckOut(pid);
		
		p = outController.enterPatronForCheckOut(pid);
		
		assertNotNull(p);
		assertTrue(p.getHasHold());
		StdOut.println("Patron found but has a hold on record.");
		
	}
}
	
