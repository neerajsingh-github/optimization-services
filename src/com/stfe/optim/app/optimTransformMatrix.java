
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

    public class optimTransformMatrix {
		
     	private createOXLFromIBean oxl; 
     	private String filepath;

     	public optimTransformMatrix() {
     		setoxl();
     	}
     	
     	private void setoxl() {
	    	this.oxl = new createOXLFromIBean();
			this.filepath=initOptimAppConfig.outfiledir;
			this.filepath = optimValidateFile.validatePath(filepath);
     	}
     	

     
     	
    	//-- Bestand generation - Bond, Swap - from Config-Vol and Baukasten--//
    	public double[][] genRawvaluesTransformationMatrix(String Instrtype, double[][] volColVector, double[][][] RawBaukasten3DData ) {
    		
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
    	
    	
    	
    	//-- DV Transformation Matrix generation  --//
    	public double[][] genDeltaTransformationMatrix(double[][] varColVector, double[][][] DeltaInstr3DData ) {
    		if ((varColVector.length==0) || (varColVector[0].length==0))  return null; 
    		if ( (DeltaInstr3DData.length==0) || (DeltaInstr3DData[0].length==0) || (DeltaInstr3DData[0][0].length==0)) return null;
    		
    		int[] varVector = new int[varColVector.length];
    		for (int i=0; i<varColVector.length; i++) {
    			varVector[i] = (int) varColVector[i][0];
    			//System.out.println(" +++>>>>>---- VarVec data:" +  varVector[i]);
    		}
    			
    		int NumSliceRowsBK= DeltaInstr3DData[0].length;
    		
    		double[][] TMKey = genDVTransformationMatrixKey(varVector, NumSliceRowsBK);
    		
    		OptimizationProblem jom = new OptimizationProblem();
    		jom.setInputParameter("TMKey", new DoubleMatrixND(TMKey));
    		jom.setInputParameter("DeltaMatrix", new DoubleMatrixND(DeltaInstr3DData)); ////   1428 x 51 x 51
    		jom.setInputParameter("ReducedDeltaMatrix", "TMKey * DeltaMatrix ");
    		jom.setInputParameter("ReducedDeltaMatrix", "sum(ReducedDeltaMatrix,3) "); /// 1428 x 51
    		
    		//--Return back to original matrix---//
    		double[][] retDeltaTransformedMatrix = (double[][]) jom.getInputParameter("ReducedDeltaMatrix").toArray(); /// 1428 x 51
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrixKey-DeltaTransformedMatrix.txt", retDeltaTransformedMatrix);
    		
    		return retDeltaTransformedMatrix;
    	}
    	
    	
    	//-- DV Transformation Swap Matrix generation  --//
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
    		jom.setInputParameter("TMKeyTNS", " TMKey' ");
    		double[][] SwapTMKeyTNSArr = (double[][]) jom.getInputParameter("TMKeyTNS").toArray(); // 14 x 292 x 79
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrixKey-Swap-TMKeyTNS.txt", SwapTMKeyTNSArr);
    		
    		jom.setInputParameter("BaukastenMatrix", new DoubleMatrixND(RawBaukasten3DData)); ////  292 x 1428 x  79
    		jom.setInputParameter("BaukastenMatrix", "permute(BaukastenMatrix[2;1;3] )" );
    		jom.setInputParameter("ReducedBaukastenMatrix", "TMKey * BaukastenMatrix ");
    		
    		//--Return back to original matrix---//
    		jom.setInputParameter("ReducedBaukastenMatrix", "permute(ReducedBaukastenMatrix[2;1;3] )" ); //  292 x 14 x  79
    		double[][][] retTransformedMatrix = (double[][][]) jom.getInputParameter("ReducedBaukastenMatrix").toArray(); // 14 x 292 x 79
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrix-TX-Swap.txt", retTransformedMatrix[0]);
    		
    		
    		return retTransformedMatrix;
    	}
    	//-- DV Transformation Key Matrix  --//
    	private double[][] genDVTransformationMatrixKey(int[] varVector, int NumSliceRowsBK) {
    		if (varVector.length==0)  return null; 
    		
    		int numDV = getMaxIntArray(varVector);
    		double[][] TMKey = new double[numDV][NumSliceRowsBK];
    		double[][] TMKeyTNS = new double[NumSliceRowsBK][numDV];
    		
    		//System.out.println(" ----------- VarVec size: " + varVector.length + " : NumSliceRowsBK Size: " + NumSliceRowsBK + " : numDV:"+ numDV);
    		
    		for (int i=0; i<NumSliceRowsBK; i++) {
    			if (varVector[i] != 0) {   
    				TMKey[varVector[i]-1][i]=1;
    				TMKeyTNS[i][varVector[i]-1]=1;
    			}
    		}
    		 
    		//oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrixKey-TMKey.txt", TMKey);
    		//oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrixKey-TMKeyTNS.txt", TMKeyTNS);
    		return TMKey;
    	}
    	
    	
    	//-- DV Transformation Matrix generation  --//
    	public double[][][] genBondDVTransformationMatrix(double[][] varColVector, double [][] weightColVector, double[][][]RawBaukasten3DData, double[][] TransformedBondWeightKey) {
    		if ((varColVector.length==0) || (varColVector[0].length==0))  return null; 
    		if ( (RawBaukasten3DData.length==0) || (RawBaukasten3DData[0].length==0) || (RawBaukasten3DData[0][0].length==0)) return null;
    		oxl.genTxtFileFrom2DData(filepath, "genBondDVTransformationMatrix-varColVector.txt", varColVector);
    		oxl.genTxtFileFrom2DData(filepath, "genBondDVTransformationMatrix-varColVector.txt", weightColVector);
    		
    		int[] varVector = new int[varColVector.length];
    		double[] weightVector = new double[varColVector.length];
    		for (int i=0; i<varColVector.length; i++) {
    			varVector[i] = (int) varColVector[i][0];
    			weightVector[i] =  weightColVector[i][0];
    			//System.out.println(" +++>>>>>---- VarVec data:" +  varVector[i]);
    		}
    			
    		int NumSliceRowsBK= RawBaukasten3DData[0].length;
    		double[][] TMKey = genBondDVTransformationMatrixKey(varVector,weightVector, NumSliceRowsBK, TransformedBondWeightKey);
    		
    		OptimizationProblem jom = new OptimizationProblem();
    		jom.setInputParameter("TMKey", new DoubleMatrixND(TMKey));
    		
    		jom.setInputParameter("TMKeyTNS", " TMKey' ");
    		double[][] BondTMKeyTNSArr = (double[][]) jom.getInputParameter("TMKeyTNS").toArray(); // 14 x 292 x 79
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrixKey-Bond-TMKeyTNS.txt", BondTMKeyTNSArr);
    		
    		
    		jom.setInputParameter("BaukastenMatrix", new DoubleMatrixND(RawBaukasten3DData)); ////  292 x 1428 x  79
    		jom.setInputParameter("BaukastenMatrix", "permute(BaukastenMatrix[2;1;3] )" ); // 1428 x 292 x 79
    		jom.setInputParameter("ReducedBaukastenMatrix", "TMKey * BaukastenMatrix "); // [35 x 1428] x [1428 x 292 x 79] = [35 x 292 x  79]
    		
    		//--Return back to original matrix---//
    		jom.setInputParameter("ReducedBaukastenMatrix", "permute(ReducedBaukastenMatrix[2;1;3] )" ); //  [292 x 35 x  79] //
    		double[][][] retTransformedMatrix = (double[][][]) jom.getInputParameter("ReducedBaukastenMatrix").toArray(); // [291 x 35 x 79] //
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrix-TX-BOND.txt", retTransformedMatrix[0]);
    		return retTransformedMatrix;
    	}
    	    	
    	
    	//-- BondDeltaBkstTensorTXMatrix Transformation Matrix generation  --//
    	public double[][][][] genBondDeltaBkstTensorTXMatrix(double[][] varColVector, double [][] weightColVector, double[][][][]DeltaBkstTensor4D, double[][] TransformedBondWeightKey) {
    		//double [][][][]bondsRVDataTransformed4DAll= otm.genBondDeltaBkstTensorTXMatrix(this.jix_Config_BondVars, this.jix_Config_BondWeights, this.bondsRVDataRaw3D, this.TransformedBondWeightKey );
    		if ((varColVector.length==0) || (varColVector[0].length==0))  return null; 
    		if ( (DeltaBkstTensor4D.length==0) || (DeltaBkstTensor4D[0].length==0) || (DeltaBkstTensor4D[0][0].length==0)) return null;
    		oxl.genTxtFileFrom2DData(filepath, "genBondDVTransformationMatrix-varColVector.txt", varColVector);
    		oxl.genTxtFileFrom2DData(filepath, "genBondDVTransformationMatrix-weightColVector.txt", weightColVector);
    		
    		int[] varVector = new int[varColVector.length];
    		double[] weightVector = new double[varColVector.length];
    		for (int i=0; i<varColVector.length; i++) {
    			varVector[i] = (int) varColVector[i][0];
    			weightVector[i] =  weightColVector[i][0];
    			//System.out.println(" +++>>>>>---- VarVec data:" +  varVector[i]);
    		}
    			
    		int NumSliceRowsBK= DeltaBkstTensor4D[0].length;
    		double[][] TMKey = genBondDVTransformationMatrixKey(varVector,weightVector, NumSliceRowsBK, TransformedBondWeightKey);
    		
    		OptimizationProblem jom = new OptimizationProblem();
    		jom.setInputParameter("TMKey", new DoubleMatrixND(TMKey));
    		jom.setInputParameter("DeltaBkstTensor", new DoubleMatrixND(DeltaBkstTensor4D)); ////  292 x 1428 x  79
    		jom.setInputParameter("DeltaBkstTensor", "permute(DeltaBkstTensor[2;1;3;4] )" ); // 1428 x 292 x 79
    		jom.setInputParameter("ReducedDeltaBkstTensor", "TMKey * DeltaBkstTensor "); // [35 x 1428] x [1428 x 292 x 79 x 51] = [35 x 292 x  79 x 51]
    		
    		//--Return back to original matrix---//
    		jom.setInputParameter("ReducedDeltaBkstTensor", "permute(ReducedDeltaBkstTensor[2;1;3;4] )" ); //  [292 x 35 x  79 x 51] //
    		double[][][][] ReducedDeltaBkstTensorArr = (double[][][][]) jom.getInputParameter("ReducedDeltaBkstTensor").toArray(); // [291 x 35 x 79 x 51] //
    		oxl.genTxtFileFrom2DData(filepath, "genDVTransformationMatrix-TX-DeltaBkstTensor.txt", ReducedDeltaBkstTensorArr[0][0]);
    		return ReducedDeltaBkstTensorArr;
    	}
    	
    	
    	//-- DV Transformation Key Matrix  --//
    	private double[][] genBondDVTransformationMatrixKey(int[] varVector,double[] weightVector, int NumSliceRowsBK, double[][] TransformedBondWeightKey) {
    		if (varVector.length==0)  return null; 
    		int numDV = getMaxIntArray(varVector);
    		
    		System.out.println(" ----------- VarVec size: " + varVector.length + " : NumSliceRowsBK Size: " + NumSliceRowsBK + " : numDV:"+ numDV + " : WeightVector size: " + weightVector.length);
    		
    		for (int i=0; i<NumSliceRowsBK; i++) {
    			if (varVector[i] != 0)   
    				TransformedBondWeightKey[varVector[i]-1][i]= 1 * weightVector[i];
    		}
    		
    		//--- Already assigned in parent class-- TransformedBondWeightKey = new double[numDV][NumSliceRowsBK]; //
    		//TransformedBondWeightKey = TMKey;  
    		
    		oxl.genTxtFileFrom2DData(filepath, "genBondDVTransformationBondWeight-TMKey.txt", TransformedBondWeightKey);
    		return TransformedBondWeightKey;
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
						
		
		
		//-- Set up the constraint for Stock-Volume like,  op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockVolLimit "); --//
		public void setVolumeConstraint (OptimizationProblem op, double stockVolLimit,  int numYears, double numAnnualConstraint){
		
			//Ex: StoclLimit=15.0 per quarter; numElements=7; numQuaterlYears=4;  // 
			//setVolConstraint (op, 15.0, 7, 12) 
			//op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockLimit1Base ");
			//op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2)  + 0.001d) ) <= StockLimit1Base ");
									
			int numElements = 7;			
			double StockLimitQ2Y =  stockVolLimit;
			int instr = numElements;
			//int numQuaterAllYears = numQuater * numYears ;
			double qtrs= numAnnualConstraint * numYears;
			
			//double StockLimitQ2Y = 15.0;
			op.setInputParameter("StockLimitQ2Y",new DoubleMatrixND(StockLimitQ2Y) );
			op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimitQ2Y) );
											
			//--Set up Constraints quarterly for 3 years, i.e. 4x3=12 -- //
			
			//for (int i=0, j=0,k=6; i<12; i++) {
			for (int i=0, j=0,k=(instr-1); i<qtrs; i++) {
								
				String expr = " sum (  sqrt( (decisionVars(0," +j+":"+k  +  ") ^ 2) + 0.001d ) )";
				op.addConstraint(expr + " <= StockLimitQ2Y "); //com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimitQ2Y ");									
				j=k+1;
				k=k + instr;				
				//System.out.println(" Constraint sum-absolute-allEleQuaters<15: " + expr + " <= " + StockLimitQ2Y  +  " #toString: " );				
			}
			
		} // -- setVolumeConstraint over --//
		
		
		********************/
		
    	
						
		
		////////////////////////////////////////////////////////////
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    