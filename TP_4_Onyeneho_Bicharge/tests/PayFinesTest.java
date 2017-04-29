import java.text.NumberFormat;


import org.junit.Test;

public class PayFinesTest {
	private CopyPatronStore cpStore;
	
	@Test
	public void payFinesTesting(){
		payFines("P102", 6.70);
		printLines();
		payFines("P101", 0.00);
		printLines();
		payFines("P104", 48.25);
		printLines();
		payFines("P105", 19.57);
		printLines();
		payFines("P106", "notanumber");
		StdOut.println("END PAY FINES TEST");
	}
	
	public void payFines(String pid, String pida) {
		StdOut.println("Input is not a valid amount.");
	}
	
	public void payFines(String pid, Double payamount){
			cpStore = new CopyPatronStore();
			String patronID = pid;
			Patron patronInfo = cpStore.fetchPatrons(patronID);
	
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			formatter.setMinimumIntegerDigits(1);
			String name = patronInfo.getName();
			Double fines = patronInfo.getOverdueFines();
			StdOut.println("Patron: " + name + ", Overdue fines: " + formatter.format(fines) + ".");
			StdOut.println("Enter the amount you wish to pay: ");
				Double paymentamount = payamount;
		
				
				if (paymentamount > fines) {
					StdOut.println("\nAmount exceeds overdue fine amount\n" +
							"Payment transaction not processed.\n");
				} 
				
				else if (paymentamount <= 0) {
					StdOut.println("\nPayment amount is less than or equal to $0.00.\n" +
							"Payment transaction not processed.\n");
				}
				
				else {
					double newfines = fines - paymentamount;
					patronInfo.setOverdueFines(newfines);
					fines = patronInfo.getOverdueFines();
					StdOut.println("Payment transaction successful.");
					StdOut.println(name + " has an outstanding balance of: " 
					+ formatter.format(fines) + ".");
				}
			
				boolean isdouble = paymentamount instanceof Double;
					
				if (!isdouble) { 
				
						StdOut.println("Input is not a valid amount.");
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


