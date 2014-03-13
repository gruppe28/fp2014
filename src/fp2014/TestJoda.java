package fp2014;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class TestJoda {
	
	public static void main(String[] args) {
		
		LocalTime time = new LocalTime();
		LocalTime time2 = new LocalTime(13,13);
		
		System.out.println(time2);
		
		System.out.println(time.toString());
		
	}

}
