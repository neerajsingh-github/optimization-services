
package com.stfe.optim.util;

/*
 * (c) Copyright BRDF. All rights reserved. 
 *
 * Created on 10.03.2017
 *
 * @author NS
 *
 */
////////////////////////////////////////////////////////////

 
//import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
//import java.util.*; 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
////////////////////////////////////////////////////////////
  
	
    public class optimConvDateFmt {
    
    	
    	public static boolean isLeapYear(int year) {
    		//  -- if (year % 400 == 0) -- redundnacy multiples --
    		// -- redundancy supported to increase readability --
    		//return ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
    		
    		return ( (year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0)) );
    	}

    	
    	public static boolean isDouble(String str) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    	
    	
    	public static void convertDateStrToGreg(String fmtDate) {
    		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    		Date date=null;
			try {
				date = df.parse(fmtDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//Calendar cal = Calendar.getInstance();
    		Calendar cal = new GregorianCalendar();
    		cal.setTime(date);    				
    	}
        	
    	    	
    	
    	public static long convertToJulian(String unformattedDate) {    		 
    		 //Unformatted Date: ddmmyyyy//
    	    long resultJulian = 0;
    	    
    	    if(unformattedDate.length() > 9) {
    	     
	    	    //Days of month//
	    	     int[] monthValues = {31,28,31,30,31,30,31,31,30,31,30,31};
	
	    	     String dayS, monthS, yearS;
	    	     dayS = unformattedDate.substring(0,2);
	    	     monthS = unformattedDate.substring(3, 5);
	    	     yearS = unformattedDate.substring(6, 10);
	
	    	     //Convert to Integer//
	    	     int day = Integer.valueOf(dayS);
	    	     int month = Integer.valueOf(monthS);
	    	     int year = Integer.valueOf(yearS); 
	
	    	     	    	     
	    	     if (isLeapYear(year)) {
	    	          monthValues[1] = 29;    
	    	     }
	    	     
	    	     //Start building Julian date
	    	     String julianDate = "1";
	    	     //last two digit of year: 2012 ==> 12
	    	     julianDate += yearS.substring(2,4);
	
	    	     int julianDays = 0;
	    	     for (int i=0; i < month-1; i++) {
	    	          julianDays += monthValues[i];
	    	     }
	    	     julianDays += day;
	
	    	     if(String.valueOf(julianDays).length() < 2) {
	    	    	 julianDate += "00";
	    	     }
	    	     if(String.valueOf(julianDays).length() < 3) {
	    	    	 julianDate += "0";
	    	     }
	
	    	        julianDate += String.valueOf(julianDays);
	    	        resultJulian = (long) Integer.valueOf(julianDate);    
	    	}
    	    return resultJulian;
    	 }
    	 
    	public static double dateToJulian(Calendar date) {
    	    int year = date.get(Calendar.YEAR);
    	    int month = date.get(Calendar.MONTH)+1;
    	    int day = date.get(Calendar.DAY_OF_MONTH);
    	    int hour = date.get(Calendar.HOUR_OF_DAY);
    	    int minute = date.get(Calendar.MINUTE);
    	    int second = date.get(Calendar.SECOND);

    	    double extra = (100.0 * year) + month - 190002.5;
    	    return (367.0 * year) -
    	      (Math.floor(7.0 * (year + Math.floor((month + 9.0) / 12.0)) / 4.0)) + 
    	      Math.floor((275.0 * month) / 9.0) +  
    	      day + ((hour + ((minute + (second / 60.0)) / 60.0)) / 24.0) +
    	      1721013.5 - ((0.5 * extra) / Math.abs(extra)) + 0.5;
    	  }
    	 
    	
    	
    	
    	
    	 //-- Conv num date to String -- "110317=>11.03.2017"//
    	 public static String convertNumDateToString(long numDate) {
    		 String sJulDate = Long.toString(numDate); 
    		 Date date = null;
    		 String strDate = null;
			try {
				date = new SimpleDateFormat("ddMMyy").parse(sJulDate);
				strDate = new SimpleDateFormat("dd.MM.yyyy").format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		 
    		 return strDate;
    	 }
    	 
    	 
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    