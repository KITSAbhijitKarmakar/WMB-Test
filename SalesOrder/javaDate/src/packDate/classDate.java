package packDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class classDate {

public static String dateDiffCalculation (String firstDate ,String lastDate){
		
	    Long lDateDiff = (long) 000;
		
		try {
			
			/*SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddhh:mm:ss");*/
			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHH:mm:ss");
			Calendar cal1 = new GregorianCalendar();
			Calendar cal2 = new GregorianCalendar();
			
		
			Date dt1 = df1.parse(firstDate);
			Date dt2 = df1.parse(lastDate);
			cal1.setTime(dt1);
			cal2.setTime(dt2);
			
			Long  milis1 = cal1.getTimeInMillis(); 
			Long milis2 = cal2.getTimeInMillis(); 

			// Calculate difference in milliseconds 
			lDateDiff = milis2 - milis1; 

			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lDateDiff.toString();   
		
	}
}


