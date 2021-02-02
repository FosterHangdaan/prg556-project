package com.spamassassins.common;
// Authors: Foster Hangdaan and Mark Brierley

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

/* 	A class for simplifying the creation and handling
* 	of date objects.
*/
public class DateHelper {

	static private SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
	
	public DateHelper() { }
	
	// Creates a java util date object based on date formate string
	public static Date parseDate(String dateFormat) {
		try {
			return formatter.parse(dateFormat);
		}
		catch (ParseException pe) {
			return null;
		}
	}
	
	// Converts a date object to timestamp
	public static Timestamp dateToTimestamp(Date d)
	{
		return new Timestamp(d.getTime());
	}
	
	// Converts a date object to a string
	public static String dateToString(Date d)
	{
		return formatter.format(d);
	}
	
	// Returns the current date and time
	public static Date getCurrentDate() {
			return new Date();
	}
	
	
}
