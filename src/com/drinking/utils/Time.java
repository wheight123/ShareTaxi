package com.drinking.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public static String getDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        
        return dateFormat.format(date);
    }
    
    public static long getMillSecs() {
        Date date = new Date();
        return date.getTime();
    }
    public static boolean after(String time1,String time2) {
    	  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = dateFormat.parse(time1);
			Date date2=dateFormat.parse(time2);
			return date2.after(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	
    	
    }
}
