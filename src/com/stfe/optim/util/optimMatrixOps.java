
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
  

    public class optimMatrixOps {
    	    	    	
    	public static double[][] transposeMatrix(double [][] srcArray){
            double[][] tgtArray = new double[srcArray[0].length][srcArray.length];
            for (int i = 0; i < srcArray.length; i++)
                for (int j = 0; j < srcArray[0].length; j++)
                	tgtArray[j][i] = srcArray[i][j];
            return tgtArray;
        }
    	
    	
    	//-- diagonal matrix for square matrix--//
    	public static double[] diagonalMatrixVector(double [][] srcArray){
    		
    		double[] tgtArray;  
    		if ( (srcArray[0].length) != (srcArray.length) ) {
    			System.out.println("diagonalMatrixVector:  diagonalMatrix Source matrix is not a square matrix !");
    			return null;
    		}
    		tgtArray = new double[srcArray.length];	
    		            
            for (int i = 0; i < srcArray.length; i++)
                for (int j = 0; j < srcArray[0].length; j++) {
                	if (i==j) tgtArray[i] = srcArray[i][j];
                	else continue;
                }
            return tgtArray;
        }
    	
    	
    	//-- diagonal matrix for square matrix--//
    	public static double[][] get1ColDiagonalMatrix(double [][] srcArray){
    		
    		double[][] tgtArray;  
    		if ( (srcArray[0].length) != (srcArray.length) ) {
    			System.out.println("diagonalMatrixVector:  diagonalMatrix Source matrix is not a square matrix !");
    			return null;
    		}
    		tgtArray = new double[srcArray.length][1];	
    		            
            for (int i = 0; i < srcArray.length; i++)
                for (int j = 0; j < srcArray[0].length; j++) {
                	if (i==j) tgtArray[i][0] = srcArray[i][j];
                	else continue;
                }
            return tgtArray;
        }
    	
    	
    	//-- diagonal matrix for square matrix--//
    	public static double[][] get1RowDiagonalMatrix(double [][] srcArray){
    		
    		double[][] tgtArray;  
    		if ( (srcArray[0].length) != (srcArray.length) ) {
    			System.out.println("diagonalMatrixVector:  diagonalMatrix Source matrix is not a square matrix !");
    			return null;
    		}
    		tgtArray = new double[1][srcArray.length];	
    		            
            for (int i = 0; i < srcArray.length; i++)
                for (int j = 0; j < srcArray[0].length; j++) {
                	if (i==j) tgtArray[0][i] = srcArray[i][j];
                	else continue;
                }
            return tgtArray;
        }
    	
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    