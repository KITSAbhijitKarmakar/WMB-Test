/*
 * DateTime.java
 *
 * Created on August 12, 2003, 1:25 PM
 */

package com.bq.mb.utils;

/**
 *
 * @author  stantr01
 * @version 
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime 
{
    //This might seem odd, but it means that the calling class / user has to have no awareness of the Calendar Object.
    public static final int FRIDAY = Calendar.FRIDAY;
    public static final int THURSDAY = Calendar.THURSDAY;
    public static final int WEDNESDAY = Calendar.WEDNESDAY;
    public static final int TUESDAY = Calendar.TUESDAY;
    public static final int MONDAY = Calendar.MONDAY;
    public static final int SUNDAY = Calendar.SUNDAY;
    public static final int SATURDAY = Calendar.SATURDAY;
    
    /** Creates new DateTime */
    public DateTime() 
    {
    }

    /**
     * Given a day (as int, based on Calenday day), will return the date that the next occurance of that day falls on. Ie, if you
     * pass FRIDAY, the method returns the date of the next friday.
     *
     * @param day The Day to be used to find the next occurence of. Use the Static Constants defined as members of this class
     * @param format The format the date should be returned in.
     * @param todayAsNext Boolean to indicate whether today can / should be returned as the next occurance of the specified day
     * @return Returns a string containing the date in the requested format.
     * @exception Throws Exception when a problem occurs
     */
    public String getDateForNext(int day, String format, boolean todayAsNext) throws Exception
    {
        try
        {
            Calendar c = Calendar.getInstance();
            
            //If today actually is an occurnace of the day of the week the user is after, move us on one day.
            if(!todayAsNext)
                if(c.get(Calendar.DAY_OF_WEEK)==day)
                    c.add(Calendar.DAY_OF_WEEK, 1);
            
            while(c.get(Calendar.DAY_OF_WEEK)!=day)
            {
                c.add(Calendar.DAY_OF_WEEK, 1);
            }
            
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(c.getTime());
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    /**
     * returns the current date / time in the format requested by the user. If you just want time specify "HH:mm:ss" (or anyother valid
     * format). If you just required date, specift "yyyyMMdd" (or anyother valid format). Or you can specify both.
     *
     * @param format The Format the time should be returned in "HH:mm:ss" etc
     * @return Returns the current date / time as a string
     */
    public String get(String format) throws Exception
    {
        try
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(c.getTime());
        }
        catch(Exception e)
        {
            throw new Exception("Exception Occured Whilst Generating Current Time/Date In Format "+format+". Error Was "+e.toString());
        }
    }
    public String getShortTime() throws Exception
    {
        try
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("HHmm");
            String time = format.format(c.getTime());
            return time;
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     * getShortDate() returns the current date in the format yyMMdd  (030814)
     *
     * @return  Returns a String containing the date in short format
     * @exception   Throws Exception when a date conversion goes wrong
     */
    public String getShortDate() throws Exception
    {
        try
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
            return format.format(c.getTime());
        }
        catch(Exception e)
        {
            throw new Exception("Exception getting short time: "+e.toString());
        }
    }
    
    /**
     * getSolveTimeFromSystemTime()
     */
    public String getSolveTimeFromSystemTime() throws Exception
    {
        try
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(c.getTime());
            date = date.substring(0,date.indexOf(" "))+"T"+date.substring(date.indexOf(" ")+1,date.length());
            return date;
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     * getSolveTime returns the date/time in form at yyyy-MM-ddTHH:mm:ss when given time in format yyyyMMdd
     *
     * @param   time The Time 
     */
    public String getSolveTime(String time) throws Exception
    {
        try
        {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            Date d = inputFormat.parse(time);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String newDate = format.format(d);
            newDate = newDate.substring(0,newDate.indexOf(" "))+"T"+newDate.substring(newDate.indexOf(" ")+1, newDate.length());
            return newDate;
        }
        catch(Exception e)
        {
            throw new Exception("Exception Generating Solve Date");
        }
    }
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) 
    {
        try
        {
            DateTime dt = new DateTime();
            String currentDate = "20130809";
            System.out.println(dt.addDays(currentDate,"yyyyMMdd", 1));
            //System.out.println(dt.getDateForNext(dt.MONDAY, "yyyyMMdd", true));
            //System.out.println(dt.getDateForNext(dt.MONDAY, "yyyyMMdd", false));
            
            //System.out.println(dt.formatDate("27-10-09","dd-MM-yy", "yyyyMMdd"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * compareDates takes 2 dates and their respective formats and returns -1 if Date 1 Before Date2, 0 if Date 1 the same as Date 2 and 1 if Date1 after Date2
     *
     * @param   date1 - the first date
     * @param   format1 - the First Dates Format
     * @param   date2 - the second date
     * @param   format2 - The Second Dates Format
     * @return  returns -1, 0 or 1 depending on whether date 1 is before, the same as or after date 2
     * @throws  Throws Exception
     */
    public int compareDate(String date1, String format1, String date2, String format2) throws Exception
    {
        try
        {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat(format1);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat(format2);
            Date d1 = dateFormat1.parse(date1);
            Date d2 = dateFormat2.parse(date2);
            return d1.compareTo(d2);
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     * formatDate takes a date, and and input format, and returns it in the specified output format
     *
     * @param   date - the date to be converted
     * @param   format1 - the input format
     * @param   format2 - the output format
     * @return  returns a newly formatted date
     * @exception   Throws Exception should anything go wrond
     */
    public String formatDate(String date, String format1, String format2) throws Exception
    {
        try
        {
            SimpleDateFormat dFormat1 = new SimpleDateFormat(format1);
            SimpleDateFormat dFormat2 = new SimpleDateFormat(format2);
            Date d = dFormat1.parse(date);
            return dFormat2.format(d);
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     * subtractDays removes specified number of days from a date which must be passed 
     * in the specified format
     *
     * @param date
     * @param format
     * @param numDays
     * @return Returns date n days before passed date
     * @exception Throws Exceptions
     */
    public String subtractDays(String date, String format, int numDays) throws Exception   
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date d = dateFormat.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            for(int n=0; n<numDays; n++)
                c.add(Calendar.DAY_OF_WEEK,-1);

            return dateFormat.format(c.getTime());
        }
        catch(Exception e)
        {
            throw e;
        }
    }

    /**
     * addMonths increments specified number of months from a date which must be passed 
     * in the specified format
     *
     * @param date
     * @param format
     * @param numMonths
     * @return Returns date n months after passed date
     * @exception Throws Exceptions
     */
    public String addMonths(String date, String format, int numMonths) throws Exception   
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date d = dateFormat.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.MONTH, numMonths);

            return dateFormat.format(c.getTime());
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     * addDays removes specified number of days from a date which must be passed 
     * in the specified format
     *
     * @param date
     * @param format
     * @param numDays
     * @return Returns date n days before passed date
     * @exception Throws Exceptions
     */
    public String addDays(String date, String format, int numDays) throws Exception   
    {
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date d = dateFormat.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            for(int n=0; n<numDays; n++)
                c.add(Calendar.DAY_OF_WEEK,+1);

            return dateFormat.format(c.getTime());
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     *  Works out the difference in milliseconds between two dates given the format the dates are in.
     *
     * @param start The Start Date / Time
     * @param end The End Date / Time
     * @param fmt The Format of the provided date / times
     * @return Returns a long which is the difference in milliseconds.
     */
    public long getDifference(String start, String end, String fmt) throws Exception
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat(fmt);
            Date startDate = format.parse(start);
            Date endDate = format.parse(end);
            
            return endDate.getTime() - startDate.getTime();
        }
        catch(Exception e)
        {
            throw new Exception("Exception occured in DateTime().getDifference(). Error Was: "+e.toString());
        }
    }
    /**
     * calculateDaysDifference calculates the number of days between two dates. You can specify
     * whether or not to include weekends.
     *
     * @param start Start Date
     * @param end End Date
     * @param dateFormat Format of Dates
     * @param includeWeekends Boolean to indicate whether or not to include weekends
     * @exception Throws Exception
     */
    public int calculateDaysDifference(String start, String end, String dateFormat, boolean includeWeekends) throws Exception
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            Date startDate = format.parse(start);
            Date endDate = format.parse(end);
            
            Calendar strCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            strCal.setTime(startDate);
            endCal.setTime(endDate);

            int counter =1;
            while(strCal.before(endCal))
            {
                strCal.add(Calendar.DAY_OF_WEEK, 1);
                if(includeWeekends)
                    counter++;
                else
                {
                    if(!(strCal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || strCal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY))
                    {
                        counter++;
                    }
                }
            }
            return counter;
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    /**
     * Given a date (in format also provided) method determines how many days are in the month. eg "200408","yyyyMM" will return 31
     *
     *@param month Date in format provided
     *@param format Date format
     */
    public String getDaysInMonth(String month, String fmt) throws Exception
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat(fmt);
            Date d = format.parse(month);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        catch(Exception e)
        {
            throw new Exception("Error whilst attempting to get Days In Month. Error Was "+e.toString());
        }
    }
    
   /**
     * Given a date (in format also provided) method determines what the day is in numeric format e.g 1 = Sunday, 2 = Monday.....
     *
     *@param month Date in format provided
     *@param format Date format
     */
    public String getDay(String month, String fmt) throws Exception
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat(fmt);
            Date d = format.parse(month);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return String.valueOf(cal.get(cal.DAY_OF_WEEK));
        }
        catch(Exception e)
        {
            throw new Exception("Error whilst attempting to get Day. Error Was "+e.toString());
        }
    }
    
    /**
     * Returns numeric representation of day in week (ie, 1 = sunday) for a given date
     *
     * @param date The Date to return the day of week
     * @param format The Format the date is passed in
     * @return Returns an int representing the day of the week, where sunday is 1
     * @exception Throws Exception when an error occurs
     */
    public int getDayInWeek(String date, String format) throws Exception
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date d = formatter.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.get(Calendar.DAY_OF_WEEK);
        }
        catch(Exception e)
        {
            throw e;
        }
    }
}
