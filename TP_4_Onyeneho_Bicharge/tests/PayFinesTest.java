import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;


import org.junit.Test;

public class PayFinesTest {
	private CopyPatronStore cpStore;
	private Patron p;
	@Test
	public void payFinesTesting(){
		cpStore = new CopyPatronStore();
		payFines("P102", 6.70, 0.00);
		printLines();
		payFines("P101", 0.00, 0.00);
		printLines();
		payFines("P104", 48.22, 0.03);
		printLines();
		payFines("P105", 19.57, 0.00);
		printLines();
		payFines("P106", "notanumber");
		printLines();
		String patronID = "P101";
		p = cpStore.fetchPatrons(patronID);
		assertEquals(p.getOverdueFines(), 0, 0.001);
		patronID = "P104";
		p = cpStore.fetchPatrons(patronID);
		assertEquals(p.getOverdueFines(), 48.25, 0.001);
		printLines();
		StdOut.println("END PAY FINES TEST");
	}
	
	public void payFines(String pid, String pida) {
		StdOut.println("Input is not a valid amount.");
	}
	
	public void payFines(String pid, Double payamount, Double secondpaymentamount){
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
					double newfines = fineRound((fines - paymentamount),2);
					patronInfo.setOverdueFines(newfines);
					fines = patronInfo.getOverdueFines();
					StdOut.println("Payment transaction successful.");
					StdOut.println(name + " has an outstanding balance of: " 
					+ formatter.format(fines) + ".");
					if (fines != 0) {
						newfines = fineRound((fines - paymentamount),2);
						patronInfo.setOverdueFines(newfines);
						fines = patronInfo.getOverdueFines();
						return;
					}
				}
			
				boolean isdouble = paymentamount instanceof Double;
					
				if (!isdouble) { 
				
						StdOut.println("Input is not a valid amount.");
					}
			
	}
	
	public double fineRound(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public void printLines() {
		StdOut.println("");
		StdOut.println("");
		StdOut.println("_____________________________________________________________________");
		StdOut.println("");
		StdOut.println("");
	}


}


