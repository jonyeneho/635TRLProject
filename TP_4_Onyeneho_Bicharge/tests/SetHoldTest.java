import static org.junit.Assert.*;

import org.junit.Test;

public class SetHoldTest {

private CopyPatronStore cpStore;
private Patron p;

	@Test
	public void setHoldTesting() {
		cpStore = new CopyPatronStore();
		setHold("P101", "1");
		printLines();
		setHold("0","1");
		printLines();
		setHold("P105", "not 0");
		printLines();
		setHold("P102", "1");
		printLines();
		setHold("P102", "1");
		printLines();
		setHold("P106", "1");
		printLines();
		setHold("P106", "1");
		printLines();
		String patronID = "P106";
		p = cpStore.fetchPatrons(patronID);
		assertTrue(p.getHasHold());
		printLines();
		StdOut.println("END SET HOLDS TEST.");
		
	}
	
	public void setHold(String pid, String holdChoiceNumber) {
		String patronID = pid;

			
			Patron patronInfo = cpStore.fetchPatrons(patronID);
			
			
			if (patronID.equals("0")) {
				StdOut.println("Pressed 0, Ended method.");
				return;
			}
			
			else if (!patronID.equals("0") && patronInfo == null) {
				StdOut.println("Patron does not exist!");
			}
			
			else if (!patronID.equals("0") && patronInfo != null) {
			
				boolean hasHold = patronInfo.getHasHold();
				String yesorno = hasHold ? " has a hold and cannot check out copies." 
						: " does not have any holds and can check out copies.";
				
				StdOut.println(patronInfo.getName() + yesorno);
				
				StdOut.println("Enter 1 to change hold status, enter any other key to return to the Main Menu:");
				String setholdchoice = holdChoiceNumber;

				if (setholdchoice.equals("1") && hasHold) {
					hasHold = patronInfo.turnHoldOff();
					yesorno = hasHold ? " has a hold and cannot check out copies." 
							: " does not have any holds and can check out copies.";
					StdOut.println(patronInfo.getName() + yesorno);
				}

				else if (setholdchoice.equals("1") && !hasHold) {
					hasHold = patronInfo.turnHoldOn();
					yesorno = hasHold ? " has a hold and cannot check out copies." 
							: " does not have any holds and can check out copies.";
					StdOut.println(patronInfo.getName() + yesorno);
				}
						
				else if (!setholdchoice.equals("1")) {
					StdOut.println("Any other key pressed. Status not changed.");
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

}
