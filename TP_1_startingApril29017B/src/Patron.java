import java.text.NumberFormat;
import java.util.*;

public class Patron
{
	private String name;
	private String patronID;
	private boolean hasHold;
	private boolean copyover;
	private double overdueFines;
	private ArrayList<Copy> copiesOut;
	private ArrayList<Copy> copiesSold;
	private ArrayList<Boolean> overdueCopies;

	public Patron(String n, String id, boolean hasHold, double overdueFines)
	{
		this.name = n;
		this.patronID = id;
		this.hasHold = hasHold;
		this.overdueFines = overdueFines;
		this.copiesOut = new ArrayList<Copy>();
		this.copiesSold = new ArrayList<Copy>();
		this.overdueCopies = new ArrayList<Boolean>();
	}

	public boolean checkCopyOut(Copy c)
	{
		if (!c.getForSale() && !hasHold) {
			c.setOutTo(this);
			copiesOut.add(c);
			
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public boolean sellCopy(Copy c)
	{
		if (c.getForSale()) {
			c.setOutTo(this);
			copiesSold.add(c);
			return true;
		}
		
		else {
			return false;
		}	
	}

	public boolean checkCopyIn(Copy c)
	{
		c.setOutTo(null);
		if (copiesOut.contains(c))
		{
			copiesOut.remove(c);
			return true;
		}
		else
			return false;
	}

	public String toString()
	{
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		formatter.setMinimumIntegerDigits(1);
		String toReturn = "Patron w/ name: " + this.name + ", id: " + this.patronID + "\n"
				+ "Overdue fines: " + formatter.format(this.overdueFines) + ".\n";

		if (this.copiesOut.isEmpty() && this.copiesSold.isEmpty())
		{
			toReturn = toReturn + "\nNo copies checked out.\n" + "\nNo copies sold.\n";
		}
		
		else if (!this.copiesOut.isEmpty() && this.copiesSold.isEmpty())
		{
		for (Copy copy : this.copiesOut)
			{
				toReturn = toReturn + "\nCopies checked out:";
				toReturn = toReturn + "\n\t" + copy.toString() + "\n";
			}
			
			toReturn = toReturn + "\nNo copies sold.\n";
		}
		
		else if (this.copiesOut.isEmpty() && !this.copiesSold.isEmpty())
		{
			toReturn = toReturn + "\nNo copies checked out.\n";
			
			for (Copy copy : this.copiesSold)
			{
				toReturn = toReturn + "\nCopies sold:";
				toReturn = toReturn + "\n\t" + copy.toString() + "\n";
			
			}
		}
		
		else {
			for (Copy copy : this.copiesOut)
			{
				toReturn = toReturn + "\nCopies checked out:";
				toReturn = toReturn + "\n\t" + copy.toString() + "\n";
			}
		
			for (Copy copy : this.copiesSold)
			{
				toReturn = toReturn + "\nCopies sold:";
				toReturn = toReturn + "\n\t" + copy.toString() + "\n";
			}
		}
		return toReturn;
	}
	
	public void setCopiesOut(ArrayList<Copy> copiesOut){
		this.copiesOut = copiesOut;
	}
	
	public ArrayList<Copy> getCopiesOut() {
		return copiesOut;
	}
	
	public void setCopiesSold(ArrayList<Copy> copiesSold){
		this.copiesSold = copiesSold;
	}
	
	public ArrayList<Copy> getCopiesSold() {
		return copiesSold;
	}

	public static void main(String[] args)
	{
		Patron p1 = new Patron("James", "007", false, 0.00);

		System.out.println(p1);
		
		
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean getHasHold() {
		return this.hasHold;
	}
	
	public void setOverdueFines(double overdueFines) {
		this.overdueFines = overdueFines;
	}
	
	public double getOverdueFines() {
		return this.overdueFines;
	}
	
	public void setOverdueCopies(ArrayList<Boolean> overdueCopies) {
		this.overdueCopies = overdueCopies;
	}
	
	public ArrayList<Boolean> getOverdueCopies() {
		return this.overdueCopies;
	}
	
	public boolean turnHoldOn() {
		this.hasHold = true;
		return this.hasHold;
	}
	
	public boolean turnOverdueHoldOn() {
		this.hasHold = true;
		setOverdueFines(overdueFines + 20.00);
		return this.hasHold;
		
	}
	
	public boolean turnHoldOff() {
		this.hasHold = false;
		return this.hasHold;
	}
	
	public boolean checkPatronOverdue(Copy c){
		this.copyover = c.checkCopyOverdue();
		overdueCopies.add(copyover);
			
			if (getOverdueCopies().contains(Boolean.TRUE)) {
				this.hasHold = true;
			}
			
			else {
				this.hasHold = false;
				}
			
			return this.hasHold;
	}
		 
	

}
