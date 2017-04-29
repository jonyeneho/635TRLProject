import java.text.*;
import java.util.*;

public class Copy
{
	private String copyID;
	private Patron outTo;
	private double copyPrice;
	private boolean forSale;
	private Date duedate;
	private String title;


	public Patron getOutTo()
	{
		return outTo;
	}

	public void setOutTo(Patron outTo)
	{
		this.outTo = outTo;
	}

	public String getCopyID()
	{
		return copyID;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public double getCopyPrice()
	{
		return copyPrice;
	}
	
	public boolean getForSale()
	{
		return forSale;
	}
	
	public Date getDueDate() {
		
		duedate = setDueDate();
		return duedate;
	}
	
	public Copy(String cid)
	{
		this.copyID = cid;
		
	}

	public Copy(String cid, String title, double cPrice, boolean forSale, Date duedate)
	{
		this.copyID = cid;
		this.title = title;
		this.copyPrice = cPrice;
		this.forSale = forSale;
		this.duedate = duedate;
	}

	public String toString()
	{
		if(!forSale) {
			return "Copy w/id= " + this.copyID + ", \"" + this.title + "\", Date due: " + DateFormat();
		} 
		
		else {
			return "Copy w/id= " + this.copyID;
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Copy))
			return false;

		return ((Copy) o).getCopyID().equals(this.copyID); // yuck.
	}
	
	public Date setDueDate(){
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_YEAR, 120);
		Date duedate = calendar.getTime();
	    return duedate;
	}
	
	public String DateFormat() {
		   DateFormat dateFormat = new SimpleDateFormat(
		       "EEEEEEEEEE, MMMMMMMMMM dd, yyyy", Locale.US);
		    dateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
		   return dateFormat.format(getDueDate());
	}
	
	public boolean checkCopyOverdue(){
		Calendar calendar = Calendar.getInstance();
		Date currentdate = calendar.getTime();
		if (currentdate.after(getDueDate())) {
			return true;
		}
		
		else {
			return false;
		}
		
	}
	
	public static void main(String[] args)
	{
		Copy c1 = new Copy("0047", "A", 30.98, false, null);
		Copy c2 = new Copy("0057", "B", 31.02, false, null);
		Patron p1 = new Patron("James", "007", false, 0.00);

		System.out.println(c1);
		System.out.println(p1);
		System.out.println(p1.checkPatronOverdue(c1));
		System.out.println(p1.checkPatronOverdue(c2));
		System.out.println(c1.checkCopyOverdue());
		
	}
	

}
