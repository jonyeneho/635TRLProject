import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class CopyTests {

	@Test
	public void testCopyNotFound(){
		String cid = "019";
		String pid = "P102";	
		CopyPatronStore cpStore = new CopyPatronStore();
		Copy c = cpStore.fetchCopy(cid);
		Patron p = cpStore.fetchPatrons(pid);
		BorrowOutController outController = new BorrowOutController(cpStore);
		
		outController.enterPatronForCheckOut(pid);
				
		assertEquals(p,outController.enterPatronForCheckOut(pid));
		
		assertNull(c);
				
	}

	@Test
	public void testCopyFound() {
		String cid = "002";
		String pid = "P102";	
		
		CopyPatronStore cpStore = new CopyPatronStore();
		BorrowOutController outController = new BorrowOutController(cpStore);
		ArrayList<Copy> copiesEntered = outController.getCopiesEntered();
		
		outController.enterPatronForCheckOut(pid);
		
		Copy c = outController.enterCopyGoingOut(cid);
		
		StdOut.println(copiesEntered);
		
		assertTrue(copiesEntered.contains(c));	
		
	}
	
	@Test
	public void testCopyForSale() {
		String cid = "008";
		String pid = "P102";	
		
		CopyPatronStore cpStore = new CopyPatronStore();
		BorrowOutController outController = new BorrowOutController(cpStore);
		ArrayList<Copy> copiesEntered = outController.getCopiesEntered();
		
		outController.enterPatronForCheckOut(pid);
		
		Copy c = outController.enterCopyGoingOut(cid);
		
		StdOut.println(copiesEntered);
		
		assertFalse(copiesEntered.contains(c));
		assertTrue(c.getForSale());
		
	}
}
