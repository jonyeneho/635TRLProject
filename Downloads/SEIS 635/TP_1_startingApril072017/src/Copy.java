
public class Copy
{
	private String copyID;
	private Patron outTo;
	private double copyPrice;
	private boolean forSale;

	// following generated in Eclipse Source menu

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
	
	public double getCopyPrice()
	{
		return copyPrice;
	}
	
	public boolean getForSale()
	{
		return forSale;
	}
	
	public Copy(String cid)
	{
		this.copyID = cid;
		
	}

	public Copy(String cid, double cPrice, boolean forSale)
	{
		this.copyID = cid;
		this.copyPrice = cPrice;
		this.forSale = forSale;
	}

	public String toString()
	{
		return "Copy w/id= " + this.copyID;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Copy))
			return false;

		return ((Copy) o).getCopyID().equals(this.copyID); // yuck.
	}

	public static void main(String[] args)
	{
		Copy c1 = new Copy("0047", 30.98, false);
		Patron p1 = new Patron("James", "007", false);

		System.out.println(c1);
		System.out.println(p1);
	}
}
