import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class DateTests {

	@Test
	public void test() {
		Date today = new Date();
		 SimpleDateFormat dateFormat = new SimpleDateFormat(
			        "EEEEEEEEEEE, MMMMMMMMMMMM dd, yyyy");
		String testdate = dateFormat.format(today);
		
		assertEquals(testdate, getServerTime());
	}
	
	public static String getServerTime() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_YEAR, 0);
		Date duedate = calendar.getTime();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEEEEEEEEEE, MMMMMMMMMMMM dd, yyyy");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
	    return dateFormat.format(duedate);
	}

}
