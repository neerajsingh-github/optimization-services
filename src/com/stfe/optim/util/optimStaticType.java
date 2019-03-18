
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
  

    public class optimStaticType {
    	  				
	
	    public static  void printType(byte x) {
	        System.out.println(x + " is a byte");
	    }
	 
	    public static void printType(int x) {
	        System.out.println(x + " is an int");
	    }
	    	    
	    public static void  printType(float x) {
	        System.out.println(x + " is a float");
	    }
	    public static void  printType(double x) {
	        System.out.println(x + " is a double");
	    }
	
	    public static void  printType(char x) {
	        System.out.println(x + " is a char");
	    }
	
	    public static void  printType(Integer x) {
	        System.out.println(x + " is of class type Integer");
	    }
	    public static void  printType(Double x) {
	        System.out.println(x + " is of class type Double");
	    }
	    public static void  printType(String x) {
	        System.out.println(x + " is of class type String");
	    }
	    
	    public static void  printType(double[] x) {
	        System.out.println(x + " is of type double[] ;  Num-Elements" +  x.length);
	    }
	    public static void  printType(double[][] x) {
	        System.out.println(x + " is of type double[][] : 2D Array ; Row:"+  x.length + ", Col:" + x[0].length );
	    }
	    public static void  printType(double[][][] x) {
	        System.out.println(x + " is of type double[][][] : 3D Array ; 3D: " + x.length + ", Row:"+  x[0].length + ", Col:" + x[0][0].length);
	    }
	    public static void  printType(String[] x) {	    	
	        System.out.println(x + " is of type String[];  Num-Elements" +  x.length);
	        //Class<String> c = obj.getClass(); c.equals(String.class) || c.equals(String[].class) || c.isArray() && c.getComponentType().isPrimitive();
	    }
	    public static void  printType(String[][] x) {	    	
	        System.out.println(x + " is of type String[][]: 2D Array; Row:"+  x.length + ", Col:" + x[0].length);	     
	    }
	    public static void  printType(Double[] x) {
	        System.out.println(x + " is of type Double[] ;  Num-Elements" +  x.length);
	    }
	    public static void  printType(Double[][] x) {
	        System.out.println(x + " is of type Double[][]: 2D Array; Row:"+  x.length + ", Col:" + x[0].length);
	    }
	    public static void printType(Object x) {
	    	System.out.println(x + " is an Object named : " + x.getClass().getSimpleName());
		}
	
	    public static void printType(int[] x) {
	    	System.out.println(x + " is an Object named : " + x.getClass().getSimpleName()  + ";  Num-Elements" +  x.length);
		}
	
	    
	    public static void printType(Object[] x){
			System.out.println(x + " is an Object vector named : " + x.getClass().getSimpleName());
			if (x.getClass().isArray() )	System.out.println(x + " is an Object array.");
			else System.out.println(x + " is NOT an Object array !");
		}
	    
	    
	    public static String arr2DArchSpec(double [][] arr, boolean quite) {
    		
    		int[]  arrsize= {0, 0};
    		String spec = new String();
    		
    		if ( (arr.length==0) || (arr[0].length==0) ) { 
    			System.out.println (" ??? arr2DArch:Array supplied is not a 2D array! ");
    			
    			if (arr.length==0) {  
    				System.out.println (" arr2DArch: Array supplied is Invalid ! ");
    				return null;
    			}
    			if ((arr.length>0) && (arr[0].length==0)) {
    				if (!quite) System.out.println (" arr2DArch: Array supplied is 1D array! NumElements = "+ arr.length );
    				arrsize[0]=arr.length;
    				spec="arr2DArch: Array supplied is 1D array! NumElements = "+ arr.length;
          			return spec;
    			}
    			
    		} else {
    			arrsize[0]=arr.length;
    			arrsize[1]=arr[0].length;
    			spec="arr2DArch: 2D Array Rows= " + arr.length +  " /Cols=" + arr[0].length;
    			if (!quite) System.out.println (" arr2DArch: 2D Array Rows= " + arr.length +  " /Cols=" + arr[0].length);
    			 
    		}
    		
    		return spec;
    	}

	    
	    public static int[] arr2DArch(double [][] arr, boolean brief) {
    		
    		int[] arrsize= {0,0};
    		if ( (arr.length==0) || (arr[0].length==0) ) { 
    			System.out.println (" ??? arr2DArch:Array supplied is not a 2D array! ");
    			
    			if (arr.length==0) {  
    				System.out.println (" arr2DArch: Array supplied is Invalid ! ");
    				return null;
    			}
    			if ((arr.length>0) && (arr[0].length==0)) {
    				System.out.println (" arr2DArch: Array supplied is 1D array! NumElements = "+ arr.length );
    				arrsize[0]=arr.length;
          			return arrsize;
    			}
    			
    		} else {
    			if (brief)
    				System.out.println (" arr2DArch: 2D Array Rows= " + arr.length +  " /Cols=" + arr[0].length);
    			else {
	    			System.out.println (" arr2DArch: 2D-Array: ");
	    			System.out.println (" arr2DArch: Number of Rows = " + arr.length);
	    			System.out.println (" arr2DArch: Number of Cols = " + arr[0].length);
    			}
    			 
    			arrsize[0]=arr.length;
    			arrsize[1]=arr[0].length;
    		}
    		
    		return arrsize;
    	}
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	


    