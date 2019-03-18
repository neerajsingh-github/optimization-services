
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
//import java.util.*; 

////////////////////////////////////////////////////////////
  

    public class optimColRowNum {
    	  				
    	public static int convColCharsToNum(String str) {
    	    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	    if(str.length() == 1) {
    	        return alphabet.indexOf(str)+1;
    	    }
    	    if(str.length() == 2) {
    	        return ( alphabet.indexOf(str.substring(1)) + 26*(1+alphabet.indexOf(str.substring(0,1)))+1) ;
    	    }
    	    return -1;
    	}		
    	public static int convColCharsToNumId0(String str) {
    	    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	    if(str.length() == 1) {
    	        return alphabet.indexOf(str);
    	    }
    	    if(str.length() == 2) {
    	        return ( alphabet.indexOf(str.substring(1)) + 26*(1+alphabet.indexOf(str.substring(0,1)))) ;
    	    }
    	    return -1;
    	}	
    	
    	
    	public static String convColNumToStr(int number) {
    	    String sres = "";
    	    if (number <= 0) {
    	        return null;
    	    }
    	    while (number > 0) {    	        
    	    	sres = Character.toString((char) ('A' + ((number - 1) % 26))) + sres;
    	    	number = number / 26;
    	    }
    	    return sres;
    	}
    	
    	public static String convColNumToStrId0(int number){
    	    String sres = "";  
    	    if (number <= 0) {
    	        return null;
    	    }
    	    while (number >= 0)
    	    {
    	        int remainder = number % 26;
    	        sres = (char)(remainder + 'A') + sres;
    	        number = (number / 26) - 1;
    	    }
    	    return sres;
    	}
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    