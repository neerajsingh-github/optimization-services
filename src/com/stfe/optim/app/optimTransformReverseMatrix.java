
package com.stfe.optim.app;
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
import java.util.*; 

import org.apache.poi.ss.usermodel.*;
/*--- XSSF includes ---*/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*--- XML beans includes ---*/  
import org.apache.xmlbeans.*;

import com.jom.DoubleMatrixND;
import com.jom.OptimizationProblem;


import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.function.tdouble.*;
import cern.colt.function.*;
import cern.colt.matrix.*;

import com.stfe.optim.config.initOptimAppConfig;
import com.stfe.optim.util.*;
import com.stfe.optim.excel.*;

    public class optimTransformReverseMatrix {
		
     	private createOXLFromIBean oxl; 
     	private String filepath;

     	public optimTransformReverseMatrix() {
     		setoxl();
     	}
     	
     	private void setoxl() {
	    	this.oxl = new createOXLFromIBean();
			this.filepath=initOptimAppConfig.outfiledir;
			this.filepath = optimValidateFile.validatePath(filepath);
     	}
     	
     	
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
     	///////////////////////////////////////////////////////////////////////////////////////////////////
     	
     	// REVERSE
     	
     	//-- DV Transformation Matrix generation  --//
    	//public double[][][] genDVReverseTransformationMatrix(double[][] varColVector, double[][][]RawBaukasten3DData ) {
     	public double[][][] genDVReverseTransformationMatrix(double[][] DVVector, double[][][]ReducedRawBaukasten3DData ) {
     		double[][][] retReverseTransformedMatrix = new double[10][][];
    		return retReverseTransformedMatrix;
    	}
    	
    	/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
    	
     	
     	
     	
     	
     	/////////////////////////////////////////////////////////////////////////////////////////////////
     	
    	//-- Bestand generation - Bond, Swap - from Config-Vol and Baukasten--//
    	public double[][] genBestandTransformationMatrix(String Instrtype, double[][] volColVector, double[][][] RawBaukasten3DData ) {
    		
    		//  volColVector = 1428 x 1; RawBaukasten3DData = 292 x 1428 x 79;
    		boolean invalidFlag=false;
    		if ((volColVector.length==0) || (volColVector[0].length==0))  invalidFlag=true;
    		if ( (RawBaukasten3DData.length==0) || (RawBaukasten3DData[0].length==0) || (RawBaukasten3DData[0][0].length==0)) invalidFlag=true;
    		if ( volColVector.length != RawBaukasten3DData[0].length ) invalidFlag=true;
    		
    		if ( invalidFlag==true ) {
        		System.out.println("??? genBestandTransformationMatrix: volColVector.length: " + volColVector.length  + ";  RawBaukasten3DData.length:" + RawBaukasten3DData.length);
        		System.out.println("??? genBestandTransformationMatrix: volColVector[0].length: " + volColVector[0].length  + ";  RawBaukasten3DData[0].length:" + RawBaukasten3DData[0].length+ "; 3d:"+RawBaukasten3DData[0][0].length);
       			return null; 
    		}
    		
    		//double[][] volRowMatrix = new double[1][volColVector.length];
    		//double[][] retVolBestand = new double[volColVector.length][RawBaukasten3DData[0][0].length];
    		
    		OptimizationProblem jom = new OptimizationProblem();
    		jom.setInputParameter("volMatrix", new DoubleMatrixND(volColVector));
    		jom.setInputParameter("volMatrix",  "volMatrix'" ); // volColVector' = (1 x 1428)  
    		jom.setInputParameter("BaukastenMatrix", new DoubleMatrixND(RawBaukasten3DData));
    		jom.setInputParameter("BaukastenMatrix", "permute(BaukastenMatrix[2;1;3] )"); //  P-RawBaukasten3DData =  1428 x 292 x 79;
    		jom.setInputParameter("VolMultBaukastenMatrix", "volMatrix * BaukastenMatrix" ); // (1, 1428) * (1428, 292, 79) = (1,292,79)
    		jom.setInputParameter("VolMultBaukastenMatrix", "sum(VolMultBaukastenMatrix,1)" ); // (292,79)
    		
    		// New Bestand = Bestand_Opt + Vol-Swap + Vol-Bond
    		double[][] retVolBestand = (double[][]) jom.getInputParameter("VolMultBaukastenMatrix").toArray();
    		//oxl.genTxtFileFrom2DData(filepath,"genBestandTransformationMatrix-retVolBestand_" + Instrtype +".txt", retVolBestand);
    		
    		//System.out.println("genBestandTransformationMatrix: VolMultBaukastenMatrix" +  Instrtype + " done!" );
    		return retVolBestand;
    		
    	} // -- Bestand Vol Swap - Bond-- //
    	
    	/////////////////////////////////////////////////////////////////////////////////////
    	
    	
    	
    	
    	/////////////////////////////////////////////////////////////////////////////////////
    	
    	//-- DV Transformation Matrix generation  --//
    	public double[][][] genDVTransformationMatrix(double[][] varColVector, double[][][]RawBaukasten3DData ) {
    		if ((varColVector.length==0) || (varColVector[0].length==0))  return null; 
    		if ( (RawBaukasten3DData.length==0) || (RawBaukasten3DData[0].length==0) || (RawBaukasten3DData[0][0].length==0)) return null;
    		
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrix-varColVector.txt", varColVector);
    		int[] varVector = new int[varColVector.length];
    		for (int i=0; i<varColVector.length; i++) {
    			varVector[i] = (int) varColVector[i][0];
    			//System.out.println(" +++>>>>>---- VarVec data:" +  varVector[i]);
    		}
    			
    		int NumSliceRowsBK= RawBaukasten3DData[0].length;
    		
    		double[][] TMKey = genDVTransformationMatrixKey(varVector, NumSliceRowsBK);
    		
    		OptimizationProblem jom = new OptimizationProblem();
    		jom.setInputParameter("TMKey", new DoubleMatrixND(TMKey));
    		jom.setInputParameter("BaukastenMatrix", new DoubleMatrixND(RawBaukasten3DData)); ////  292 x 1428 x  79
    		jom.setInputParameter("BaukastenMatrix", "permute(BaukastenMatrix[2;1;3] )" );
    		jom.setInputParameter("ReducedBaukastenMatrix", "TMKey * BaukastenMatrix ");
    		
    		//--Return back to original matrix---//
    		jom.setInputParameter("ReducedBaukastenMatrix", "permute(ReducedBaukastenMatrix[2;1;3] )" ); //  292 x 14 x  79
    		double[][][] retTransformedMatrix = (double[][][]) jom.getInputParameter("ReducedBaukastenMatrix").toArray(); // 14 x 292 x 79
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrixKey-retTransformedMatrix.txt", retTransformedMatrix[0]);
    		
    		
    		return retTransformedMatrix;
    	}
    	
    	
    	
    	
    	/////////////////////////////////////////////////////////////////////////////////////
    	    	
    	//-- DV Transformation Key Matrix  --//
    	private double[][] genDVTransformationMatrixKey(int[] varVector, int NumSliceRowsBK) {
    		if (varVector.length==0)  return null; 
    		
    		int numDV = getMaxIntArray(varVector);
    		double[][] TMKey = new double[numDV][NumSliceRowsBK];
    		
    		//System.out.println(" ----------- VarVec size: " + varVector.length + " : NumSliceRowsBK Size: " + NumSliceRowsBK + " : numDV:"+ numDV);
    		
    		for (int i=0; i<NumSliceRowsBK; i++) {
    			if (varVector[i] != 0)   
    				TMKey[varVector[i]-1][i]=1;
    		}
    		 
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrixKey-TMKey.txt", TMKey);
    		
    		return TMKey;
    	}
    	
    	public int getMaxDVFromConfigVars(double[][] jix_Config_VarColVector) {
    		if (jix_Config_VarColVector.length==0)  return 0;
    		
    		int[] varVector = new int[jix_Config_VarColVector.length];
    		for (int i=0; i<jix_Config_VarColVector.length; i++)
    			varVector[i] = (int) jix_Config_VarColVector[i][0];
    	
    		int maxDV = getMaxIntArray(varVector);
    		
    		//System.out.println(" **** getMaxDVFromConfigVars:  VarVec size: " + varVector.length + " ; Max-DV : " + maxDV +" ****");
    		return maxDV;
    	}
    	
    	
    	
    	public int getNumDVFromConfigVars(double[][] jix_Config_VarColVector) {
    		if (jix_Config_VarColVector.length==0)  return 0;
    		
    		int[] varVector = new int[jix_Config_VarColVector.length];
    		for (int i=0; i<jix_Config_VarColVector.length; i++)
    			varVector[i] = (int) jix_Config_VarColVector[i][0];
    	
    		int numDV = getNumPostiveElementsIntArray(varVector);
    		return numDV;
    	}
    	
    	
    	//-- Utility functions --//
    	//-- Count non-zero element of array --//
    	private int getNumPostiveElementsIntArray(int[] arr) {
    		if (arr.length==0)  return -1; 
    		int count=0;
    		for (int i=0; i<arr.length; i++) {
    			if (arr[i] != 0) count++;
    		}
            return count;
    	}
    	
    	    	
    	//-- Max Min Value of array --//
    	private int getMaxIntArray(int[] arr) {
    		if (arr.length==0)  return -1; 
    		int[] clone = arr.clone();
            Arrays.sort(clone);
            
           // for (int i=0; i<clone.length ; i++)
           // 	System.out.println(" **** getMaxDVFromConfigVars: getMaxIntArray:  VarVec  IntArr sorted: " + clone[i] +" ****");
            
            int retMin=(int) clone[0];
    		int retMax=(int) clone[clone.length-1];
            return retMax;
    	}
    	
    	/////////////////////////////////////////////////////////////////////////////////////
    	
    	
    	
    	
    	/********************
    	
		//-- set up the SwapRawData in 192(dates) x 7(elements) x 8(TwiceYearly) form;  numSRVperDateRows=448;numDateRows=192;  swapsRVDataEff Dim:192x448x31 --//
		// -- buildRVDKFReduced (false, swapsRVDataEff, int numDateRows=116, int numAnnuality=2 //Biannual, int numYears=16); --//
		// -- buildRVDKFReduced (false, swapsRVDataEff, int numDateRows=116, int numAnnuality=1 //Annual, int numYears=16); --//
		public double [][][] buildRVDKFReduced (boolean reducedFlag, double [][][]  swapsRVDataEff, int numDateRows, double numAnnuality, int numYears, int numCols){
			
			boolean debugFlag=false;	int sevLevel=1;					
			int iterYears= (int)(numYears/numAnnuality); // biannual ->8
			int numAddSRV = (int) (4 * numAnnuality); 
			int numElements = 7; //fixed constant // 
			
			double groupRawIdx= numElements * numAddSRV; // Biannul=56; Annual=28;
			
			int reducedSRV = (int) (iterYears * numElements);
			
			//double [][][] swapsRVDataEffReduced= new double [192][reducedSRV][31]; 
			double [][][] swapsRVDataEffReduced= new double [numDateRows][reducedSRV][numCols];		
			
			reducedFlag=false;
			if (debugFlag) System.out.println("*** buildRVDKFReduced : SRV reduced - Debugging Data accuracy! ***");
			
			
			for (int date=0, reduced2DIdx=0; date<numDateRows; date++, reduced2DIdx=0) {
				if (debugFlag) System.out.println(" **** MonthRow: For every month till End(12xNumYears): All-NumDateRows=" + numDateRows + "; Month-Num"+ (date+1) );
				
				for (int yearGroup=0, rawIdx=0; yearGroup<iterYears; yearGroup++, rawIdx += groupRawIdx) {
					if (debugFlag) System.out.println(" ***###[[ Annual iteration: Reduced Swap RV-  Year Group Num: "+ (yearGroup+1) );
					
					for (int srvEle=0; srvEle<7; srvEle++) {			
						if (debugFlag && (sevLevel>5) && ((srvEle<1) || (srvEle>5)) ) System.out.println(" ##**[[[  Element Num: "+ (srvEle+1) );
										
						int numEle=0;
						for (int srvQuat=0; srvQuat<numAddSRV; srvQuat++) {		
											
							if (debugFlag && (sevLevel>6) ) System.out.println(" [[[[*# srvQuat Test IDX: "+ (rawIdx+srvEle+numEle) );
											
							for (int dkf=0; dkf<numCols; dkf++) {
								swapsRVDataEffReduced[date][reduced2DIdx][dkf] +=  swapsRVDataEff[date][rawIdx+srvEle+numEle][dkf] ;	
							}
							numEle += numElements;
						}								
						reduced2DIdx++;	
					} // done for 7 elements //
									
				} // done for a year in a date//
								
								
			} // done for all date//
								
			reducedFlag=true;
			System.out.println(" Reduced Swaps RV:  swapsRVDataEffReduced Dim-XYZ: Tabs-X=" + swapsRVDataEffReduced.length + " : Rows-Y=" + swapsRVDataEffReduced[0].length + " : Cols-Z=" + swapsRVDataEffReduced[0][0].length);
			return swapsRVDataEffReduced;
		}  //-- buildRVDKFReduced over --//
		
		********************/
						
		
		////////////////////////////////////////////////////////////
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    