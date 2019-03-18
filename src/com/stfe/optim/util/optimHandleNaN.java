
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

 
import java.io.*;
//import java.util.*; 

////////////////////////////////////////////////////////////
  

    public class optimHandleNaN {
    	  	
    	
    	//-- checks the NaN of 2D double array  -- //
    	public static boolean arr2DContainsNaN(double [][] arr) {
  			
    		if ( (arr.length==0) || (arr[0].length==0) ) 
  				return false;
  				
  			for (int i=0; i<arr.length; i++) {
  				for (int j=0; j<arr[0].length; j++) {
  					if ( Double.isNaN(arr[i][j]) ) {
  						if ( (i<2) || (i>(arr.length-2) ))
  						if ( (j<2) || (j>(arr[0].length-2) )) System.out.println (" ??? 2DArray contains NaN value ! At row: "+ (i+1) + " /col:" + (j+1) );  
  						return true;
  					}
  				}
  			}
  			return false;
    	}
    		
    	//-- checks if 2D  array is negative -- //
    	public static boolean is2DArrNegative(double [][] arr) {
  			
    		if ( (arr.length==0) || (arr[0].length==0) ) 
  				return false;
  				
  			for (int i=0; i<arr.length; i++) {
  				for (int j=0; j<arr[0].length; j++) {
  					if ( arr[i][j] < 0 ) {  						
  						System.out.println (" ??? 2DArray contains negative value ! At row: "+ (i+1) + " /col:" + (j+1) );  
  						return true;
  					}
  				}
  			}
  			return false;
    	}
    	
    	
    	
    	//-- checks if 1D  array is negative -- //
    	public static boolean isVectorNegative(double [] arr) {
  			
    		if ( (arr.length==0)  ) 
  				return false;
  				
  			for (int i=0; i<arr.length; i++) {  				
  				if ( arr[i] < 0 ) {  						
  					System.out.println (" ??? Vector contains negative value ! At row: "+ (i+1)  );  
  					return true;  			
  				}
  			}
  			return false;
    	}
    	
    	
    	//-- replaces the NaN of 2D double array with value -- //
    	public static double[][] arr2DNaNReplacedVal(double [][] arr, double val) {
      			
        	if ( (arr.length==0) || (arr[0].length==0) ) 
      			return null;
      				
      		for (int i=0; i<arr.length; i++) {
      			for (int j=0; j<arr[0].length; j++) {
      				if ( Double.isNaN(arr[i][j]) ) {
      					arr[i][j]=val;
      				}
      			}
      		}
      		return arr;
        }
        		
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    