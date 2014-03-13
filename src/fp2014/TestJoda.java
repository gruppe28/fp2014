package fp2014;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class TestJoda {
	
	public static void main(String[] args) {
		LocalDate dato = new LocalDate();
		LocalDate dato2 = new LocalDate(2014, 3, 12);
		LocalDate dato3 = new LocalDate(2014, 3, 14);
		System.out.println(dato);

		System.out.println(dato2.compareTo(dato));
		System.out.println(dato.compareTo(dato));
		System.out.println(dato3.compareTo(dato));
		
	}

}
