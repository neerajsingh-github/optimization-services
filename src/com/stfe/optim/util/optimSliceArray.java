
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
  
    public class optimSliceArray {
    	
    	public static double[] replicate1DArrayIntoBigArray(double[] smallArray, int times) {
    		
    		if (smallArray.length < 1) return null;
    		if (times <= 0) times=1;
    		double[] retArray = new double[(smallArray.length) * times];
    		
    		//--public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)--//
    		try {
	    		for (int idx = 0, retArrPos=0 ; idx < times; idx++) {	    			
	    			System.arraycopy(smallArray, 0, retArray, retArrPos, (smallArray.length));
	    			retArrPos = retArrPos + smallArray.length;
	    		}
    		}
    		catch (Exception e) { e.printStackTrace(); }
    		
    		return retArray;
    	}
    	
    	
    	
    	public static void testReplicate1DArray(){
    		double [] upConstraintBase = { 10.0, 10,	10,	0.25,	0.25,	0.25,	0.25 };
						
    		System.out.println("\n" + " Small Old 1D-Array size (row)# : "+ upConstraintBase.length);
			//copyArrayIntoBigArray(double[] smallArray, int times)						
			double [] upConstraints = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(upConstraintBase, 64);
		
			System.out.println("\n" + " Big New 1D-Array size (row)# : "+ upConstraints.length + "  Data 1st#last: " + upConstraints[0] +" # " +upConstraints[(upConstraints.length)-1] );
			
    	}
    	 
    	///////////////////////////////////////////////
    	// -- join two 2D-arrays of same row but different col size -- //
    	public static double[][] join2Array2DCols(double [][] Array1, double [][] Array2) {
    		
    		//-- check if arrays are valid, & numRow of both are same --//
    		if ( (Array1== null) || (Array2== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}
    		if (Array1.length != Array2.length) {
    			System.out.println("\n The rows of two arrays are not same! Cols Can not be joined together" );
    			return null;
    		}
    		    		
    		int a1rows=Array1.length; int a1cols=Array1[0].length; 
    		int a2rows=Array2.length; int a2cols=Array2[0].length;    		
    		//System.out.println("\n Struct Array: /Arr1Row=" + a1rows + "/Arr1Col= " + a1cols + " /Arr2Row=" + a2rows + "/Arr2Col= " + a2cols + "/A1colsIdx= " + a1colsIdx);
    		
    		double[][] retArray = new double[a1rows][(a1cols + a2cols)];
    		
    		
    		//-- array copy should not be used as it reduces the shape of DestArray same as SourceArray --//
    		//-- Buggy System.arraycopy(Array1, 0, retArray, 0, a1rows);
    		
    		for (int i=0; i<a1rows; i++ ) 
    			for (int j=0; j<a1cols; j++ ) {
    				retArray[i][j] = Array1[i][j];
    		}
    		for (int i=0; i<a2rows; i++ ) 
    			for (int j=0; j<a2cols; j++ ) {
    				retArray[i][(a1cols + j)] = Array2[i][j];
    		}
    		
    		return retArray;
    	}
        		
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////
		// -- join two 2D-arrays of same row but different col size -- //
		public static double[][] join2Array2DColsTimes(double [][] Array1, double [][] Array2, int times) {
		
			//-- check if arrays are valid, & numRow of both are same --//
			if ( (Array1== null) || (Array2== null) ) {
				System.out.println("\n Array supplied is null! Can not be joined together" );
				return null;
			}
			if (Array1.length != Array2.length) {
				System.out.println("\n The rows of two arrays are not same! Cols Can not be joined together" );
				return null;
			}
			if (times < 1) times = 1;
			if (times > 99) times = 1;
			int a1rows=Array1.length; int a1cols=Array1[0].length; 
			int a2rows=Array2.length; int a2cols=Array2[0].length;
			int a2colstimes= times *  a2cols;
			//System.out.println("\n Struct Array: /Arr1Row=" + a1rows + "/Arr1Col= " + a1cols + " /Arr2Row=" + a2rows + "/Arr2Col= " + a2cols + "/A1colsIdx= " + a1colsIdx);
			
			double[][] retArray = new double[a1rows][(a1cols + a2colstimes)];
			
			
			//-- array copy should not be used as it reduces the shape of DestArray same as SourceArray --//
			//-- Buggy System.arraycopy(Array1, 0, retArray, 0, a1rows);
			
			for (int i=0; i<a1rows; i++ ) 
				for (int j=0; j<a1cols; j++ ) {
					retArray[i][j] = Array1[i][j];
				}
			
			for (int t=0; t<times; t++) {
				for (int i=0; i<a2rows; i++ ) { 
					for (int j=0; j<a2cols; j++ ) {
						retArray[i][(t + a1cols + j)] = Array2[i][j];
					}
				}
			}
			return retArray;
		}



		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////////////////////
    	// -- join two 2D-arrays of same col size -- //
    	public static double[][] join2Array2D(double [][] Array1, double [][] Array2) {
    		//-- check if numCol of both are same --//    		
    		//System.out.println("\n Array1 Row:Col="+ Array1.length + ":" + Array1[0].length);
    		//System.out.println("\n Array2 Row:Col="+ Array2.length + ":" + Array2[0].length);
    		
    		if ( (Array1== null) || (Array2== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}
    		if (Array1[0].length != Array2[0].length) {
    			System.out.println("\n The columns of two arrays are not same! Can not be joined together" );
    			return null;
    		}
    		    		
    		double[][] retArray = new double[Array1.length + Array2.length ][Array1[0].length];
    		//void java.lang.System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		
    		return retArray;
    	}    	
    	
    	// -- join four 2D-arrays of same col size -- //
    	public static double[][] join4Array2D(double [][] Array1, double [][] Array2, double [][] Array3, double [][] Array4) {
    		//-- check if numCol of both are same --//
    		
    		//System.out.println("\n Array1 Row:Col="+ Array1.length + ":" + Array1[0].length);
    		//System.out.println("\n Array2 Row:Col="+ Array2.length + ":" + Array2[0].length);
    		//System.out.println("\n Array3 Row:Col="+ Array3.length + ":" + Array3[0].length);
    		//System.out.println("\n Array4 Row:Col="+ Array4.length + ":" + Array4[0].length);
    		
    		if ( (Array1== null) || (Array2== null) || (Array3== null) || (Array4== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}
    		if (Array1[0].length != Array2[0].length) {
    			System.out.println("\n The columns of two arrays1-2 are not same! Can not be joined together" );
    			return null;
    		}
    		if (Array2[0].length != Array3[0].length) {
    			System.out.println("\n The columns of two arrays2-3 are not same! Can not be joined together" );
    			return null;
    		}
    		if (Array3[0].length != Array4[0].length) {
    			System.out.println("\n The columns of two arrays3-4 are not same! Can not be joined together" );
    			return null;
    		}
    		    		
    		double[][] retArray = new double[Array1.length + Array2.length + Array3.length + Array4.length ][Array1[0].length];
    		//void java.lang.System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		System.arraycopy(Array3, 0, retArray, Array1.length + Array2.length, Array3.length);
    		System.arraycopy(Array4, 0, retArray, Array1.length + Array2.length + Array3.length, Array4.length);
    		return retArray;
    	}
        	
    	// -- join two 2D-arrays of same col size -- //
    	public static String[][] join2Array2DString(String [][] Array1, String [][] Array2) {
    		//-- check if numCol of both are same --//
    		
    		//System.out.println("\n Array1 Row:Col="+ Array1.length + ":" + Array1[0].length);
    		//System.out.println("\n Array2 Row:Col="+ Array2.length + ":" + Array2[0].length);
    		
    		if ( (Array1== null) || (Array2== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}
    		if (Array1[0].length != Array2[0].length) {
    			System.out.println("\n The columns of two arrays are not same! Can not be joined together" );
    			return null;
    		}
    		    		
    		String[][] retArray = new String[Array1.length + Array2.length ][Array1[0].length];
    		//void java.lang.System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		
    		return retArray;
    	}    	
    	
    	// -- join four 2D-arrays of same col size -- //
    	public static String[][] join4Array2DString(String [][] Array1, String [][] Array2, String [][] Array3, String [][] Array4) {
    		//-- check if numCol of both are same --//
    		
    		//System.out.println("\n Array1 Row:Col="+ Array1.length + ":" + Array1[0].length);
    		//System.out.println("\n Array2 Row:Col="+ Array2.length + ":" + Array2[0].length);
    		//System.out.println("\n Array3 Row:Col="+ Array3.length + ":" + Array3[0].length);
    		//System.out.println("\n Array4 Row:Col="+ Array4.length + ":" + Array4[0].length);
    		
    		if ( (Array1== null) || (Array2== null) || (Array3== null) || (Array4== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}
    		if (Array1[0].length != Array2[0].length) {
    			System.out.println("\n The columns of two arrays1-2 are not same! Can not be joined together" );
    			return null;
    		}
    		if (Array2[0].length != Array3[0].length) {
    			System.out.println("\n The columns of two arrays2-3 are not same! Can not be joined together" );
    			return null;
    		}
    		if (Array3[0].length != Array4[0].length) {
    			System.out.println("\n The columns of two arrays3-4 are not same! Can not be joined together" );
    			return null;
    		}
    		    		
    		String[][] retArray = new String[Array1.length + Array2.length + Array3.length + Array4.length ][Array1[0].length];
    		//void java.lang.System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		System.arraycopy(Array3, 0, retArray, Array1.length + Array2.length, Array3.length);
    		System.arraycopy(Array4, 0, retArray, Array1.length + Array2.length + Array3.length, Array4.length);
    		return retArray;
    	}
        
    	
    	
    	
    	// -- join two 1D-arrays -- //
    	public static double[] join2Array1D(double[] Array1, double[] Array2) {
    		//-- check if numCol of both are same --//    		
    		//System.out.println("\n Array1 Rows="+ Array1.length  + " Array2 Rows=" + Array2.length );
    		
    		if ( (Array1== null) || (Array2== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}
    		    		
    		double[] retArray = new double[Array1.length + Array2.length ];
    		//void java.lang.System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		
    		return retArray;
    	}    	
    	
    	// -- join four 1D-arrays  -- //
    	public static double[] join4Array1D(double[] Array1, double[] Array2, double[] Array3, double[] Array4) {
    		//-- check if numCol of both are same --//    		
    		//System.out.println("\n Array1 Rows="+ Array1.length  + " Array2 Rows=" + Array2.length + " Array3 Rows="+ Array3.length  + " Array4 Rows=" + Array4.length);
    		
    		if ( (Array1== null) || (Array2== null)|| (Array3== null) || (Array4== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}    		    		
    		double[] retArray = new double[Array1.length + Array2.length + Array3.length + Array4.length ];
    		//void java.lang.System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		System.arraycopy(Array3, 0, retArray, Array1.length + Array2.length, Array3.length);
    		System.arraycopy(Array4, 0, retArray, Array1.length + Array2.length + Array3.length, Array4.length);
    		return retArray;
    	}
    	
    	// -- join two 1D-arrays String -- //
    	public static String[] join2Array1DString(String[] Array1, String[] Array2) {
    		//-- check if numCol of both are same --//    		
    		//System.out.println("\n Array1 Rows="+ Array1.length  + " Array2 Rows=" + Array2.length );
    		
    		if ( (Array1== null) || (Array2== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}
    		    		
    		String[] retArray = new String[Array1.length + Array2.length ];    		
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		
    		return retArray;
    	}    	
    	// -- join four 1D-arrays  -- //
    	public static String[] join4Array1DString(String[] Array1, String[] Array2, String[] Array3, String[] Array4) {
    		//-- check if numCol of both are same --//    		
    		//System.out.println("\n Array1 Rows="+ Array1.length  + " Array2 Rows=" + Array2.length + " Array3 Rows="+ Array3.length  + " Array4 Rows=" + Array4.length);
    		
    		if ( (Array1== null) || (Array2== null)|| (Array3== null) || (Array4== null) ) {
    			System.out.println("\n Array supplied is null! Can not be joined together" );
    			return null;
    		}    		    		
    		String[] retArray = new String[Array1.length + Array2.length + Array3.length + Array4.length];    		
    		System.arraycopy(Array1, 0, retArray, 0, Array1.length);
    		System.arraycopy(Array2, 0, retArray, Array1.length, Array2.length);
    		System.arraycopy(Array3, 0, retArray, Array1.length + Array2.length, Array3.length);
    		System.arraycopy(Array4, 0, retArray, Array1.length + Array2.length + Array3.length, Array4.length);
    		return retArray;
    	}
    	
    	
    	
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//- Slicing Array -- //
    	//-- Returns a slice of 2D data specified by rows and cols --//
    	public static double[][] slice2DArray(double [][] bigArray, int ridxStart, int ridxEnd, int cidxStart, int cidxEnd) {
    		boolean dbg=false;
    		if (bigArray.length < 1) return null;
    		if (ridxEnd < 0) ridxEnd=(bigArray.length)-1;
    		if (ridxStart < 0) ridxStart=0;
    		if (bigArray[0].length < 1) return null;
    		if (cidxEnd < 0) cidxEnd=(bigArray[0].length)-1;
    		if (cidxStart < 0) cidxStart=0;
    		
    		int nrows =  (ridxEnd - ridxStart) + 1;
    		int ncols =  (cidxEnd - cidxStart) + 1;    		
    		double[][] retArray = new double[nrows][ncols];
    		
    		if (dbg==true) System.out.println(" optimSliceArray: slice2DArray: nrows=" + nrows + " ; ncols=" + ncols);
    			
    		//--public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)--//
    		try {
	    		for (int idx = 0, rowcnt=ridxStart; idx < nrows; idx++, rowcnt++) {
	    			if (dbg==true) System.out.println(" System.arraycopy: slice2DArray: rowcnt=" + rowcnt + " ; idx=" + idx);
	    	        System.arraycopy(bigArray[rowcnt], cidxStart, retArray[idx], 0, ncols);
	    	    }
    		}    		
    		catch (IndexOutOfBoundsException e) { e.printStackTrace(); }
    		catch (ArrayStoreException e) { e.printStackTrace(); }
    		catch (NullPointerException e) { e.printStackTrace(); }
    		catch (Exception e) { e.printStackTrace(); }
    		
    		return retArray;
    	}
    	    
    	
    	//-- Returns a slice of 2D Array of String type specified by rows and cols --//
    	public static String[][] slice2DArrayString(String [][] bigArray, int ridxStart, int ridxEnd, int cidxStart, int cidxEnd) {
    		
    		if (bigArray.length < 1) return null;
    		if (ridxEnd < 0) ridxEnd=(bigArray.length)-1;
    		if (ridxStart < 0) ridxStart=0;
    		if (bigArray[0].length < 1) return null;
    		if (cidxEnd < 0) cidxEnd=(bigArray[0].length)-1;
    		if (cidxStart < 0) cidxStart=0;
    		
    		int nrows =  (ridxEnd - ridxStart) + 1;
    		int ncols =  (cidxEnd - cidxStart) + 1;    		
    		String[][] retArray = new String[nrows][ncols];
    		
    		//--public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)--//
    		try {
	    		for (int idx = 0, rowcnt=ridxStart; idx < nrows; idx++, rowcnt++) {
	    	        System.arraycopy(bigArray[rowcnt], cidxStart, retArray[idx], 0, ncols);
	    	    }
    		}    		
    		catch (IndexOutOfBoundsException e) { e.printStackTrace(); }
    		catch (ArrayStoreException e) { e.printStackTrace(); }
    		catch (NullPointerException e) { e.printStackTrace(); }
    		catch (Exception e) { e.printStackTrace(); }
    		
    		return retArray;
    	}
    	    
    	
    	
    	public static void testSliceArray(){
    		int nrow=5, ncol=7;
    		    		
    		double [][] bigArr=new double[nrow][ncol];
    		//--init values--//
    		for (int i=0, k=0; i<nrow; i++) {
    			k=k*10;
    			for (int j=0; j<ncol; j++)
    				bigArr[i][j]= (double) ++k;
    		}
    		//--display the bigArr --//
    		for (int i=0; i<bigArr.length; i++) {
    			System.out.print(" BigArr Row= " + (i+1) );
    			for (int j=0; j<bigArr[0].length; j++) {
    				System.out.print(" : " + bigArr[i][j]);
    			}
    			System.out.println();
    		}
    		    		
    		//-- slice array - row(3:4), col(2:5) 
    		int rowStart=3,  rowEnd=4,  colStart=1,  colEnd=6;
    		//int rowStart=-1,  rowEnd=-1,  colStart=-1,  colEnd=-1;
    		//int rowStart=3,  rowEnd=-1,  colStart=2,  colEnd=-1;
    		
    		System.out.println("\n" + " Slice Array # Row-start-end:" + rowStart + "-" + rowEnd +  " # Col-start-end:" + colStart + "-" + colEnd );
    		
    		double [][] sliceArr = slice2DArray(bigArr, rowStart-1, rowEnd-1, colStart-1, colEnd-1 );
    		//--display the slice --//
    		for (int i=0; i<sliceArr.length; i++) {
    			System.out.print(" Slice Row= " + (i+1) );
    			for (int j=0; j<sliceArr[0].length; j++) {
    				System.out.print(" : " + sliceArr[i][j]);
    			}
    			System.out.println();
    		}
    		
    	}
    	
    	
    	//- Coping Array into BigArray-- //
    	public static double[] copy1DArrayIntoBigArray(double[] bigArray, double[] srcArray, int startIdx) {
    		if (bigArray.length < 1) return null;
    		if (srcArray.length < 1) return bigArray;
    		if (bigArray.length < srcArray.length) return bigArray;
    		if (bigArray.length < (srcArray.length + startIdx ) ) return bigArray;
    		
    		for (int i=0; i<srcArray.length; i++) {
    			bigArray[startIdx+i] = srcArray[i];
    		}
    		
    		return bigArray;
    	}
    	//this.joInitDVSolSetXL[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(dataSet.initSolSetJOM2Y14, 1);
    	
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    