import java.util.ArrayList;

public class Patron
{
	private String name;
	private String patronID;
	private boolean hasHold;
	private ArrayList<Copy> copiesOut;
	private ArrayList<Copy> copiesSold;

	public Patron(String n, String id, boolean hasHold)
	{
		this.name = n;
		this.patronID = id;
		this.hasHold = hasHold;
		this.copiesOut = new ArrayList<Copy>();
		this.copiesSold = new ArrayList<Copy>();
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
		String toReturn = "Patron w/ name: " + this.name + ", id: " + this.patronID;

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
		Patron p1 = new Patron("James", "007", false);

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

}
