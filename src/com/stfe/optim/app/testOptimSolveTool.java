
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



    public class testOptimSolveTool {
			
			
    	
    	   	
    	    	
		public void ojbfunc_optimize_util() 	{
			
			
			System.out.println(" Test the ojbfunc_optimize");
			
			//OptimizationProblem op = new OptimizationProblem();						
			//op.addDecisionVariable("decisionVars3D", false, new int[] {192,1, 448} );
			//op.addDecisionVariable("decisionVars", false, new int[] {1, 448} );
			
			
			String filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			optimDeserializeBean restore = new optimDeserializeBean(filepath);
			optimRestoreIOBean restoreIOBean = new optimRestoreIOBean();
					
			
			//- Test txt file--//
			createOXLFromIBean oxl = new createOXLFromIBean();
			//foxl.genTxtDataFromIBean(filepath,  "OXL_SWAPS_RawValuesIXL.xlsx",  "SWAPS_RawValues","srv_SWAPS_RawValues");
			//oxl.genTxtFileFrom2DData(String filedir, String deffilename, double [][]data) ;
			
			
			//--Bestand - BM_SWAPS_PLAN_CURRENTQUARTER --//
			double [][] jBestand = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-rv_BM_SWAPS_PLAN_CURRENTQUARTER","rv_Data_BM_SWAPS_PLAN_CURRENTQUARTER");
			//double [][]bestand = restore.deserializeBeanAsPdouble("rv_Data_BM_SWAPS_PLAN_CURRENTQUARTER",filepath);
			String [][]jBestandDate = (String[][])restore.deserializeBeanAsString("rv_Date_BM_SWAPS_PLAN_CURRENTQUARTER",filepath);
			String [][]jBestandEle = (String[][])restore.deserializeBeanAsString("rv_DataPort_BM_SWAPS_PLAN_CURRENTQUARTER",filepath); //324 rows
			//-- conv date to julian date //
			double[] jBestandJulDate = new double[jBestandDate.length]; 
			for (int i=0; i<jBestandDate.length; i++) {							
				if  ( com.stfe.optim.util.optimConvDateFmt.isDouble(jBestandDate[i][0]) ) {
					jBestandJulDate[i]= Double.parseDouble(jBestandDate[i][0]);
				} else {				
					jBestandJulDate[i]= com.stfe.optim.util.optimConvDateFmt.convertToJulian(jBestandDate[i][0]);
				}
				//System.out.println(" Bestand- dateStr: " + jBestandDate[i][0] + " JulDate: " + jBestandJulDate[i] );				
			}
			
			
			//--Loading the Bestand-slice into JOM - 2D//
			//--Sheet:  324 rows for each date starting from 31.01.2005(should be 31.01.2016)  till 31.12.2031 = total 324 rows --//
			//--31.1.2016 starts at row 134=>133 =>idx=132;;;  (((Row 143=>142=>idx 141 value at AC=-4.97E9))) Slice rows=192
			//Bestand colStart AC:29=>27=>idx 26 ; colSEnd AL:38=>36=>idx 35 -- // slice2DArray(bigArr, rowStart-1, rowEnd-1, colStart-1, colEnd-1 );
			//Final COl H(8=>6):AL(38=>36) 
			double [][] jBestandEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestand, 132, -1, 6, 36 );	
			int numDateRows=jBestandEff.length ;
			
			//op.setInputParameter("Bestand", new DoubleMatrixND(jBestandEff)  );
					 
			System.out.println(" Bestand: size_data: row " +  jBestand.length + "; col-size: " +  jBestand[0].length + "; size_ele: " +  jBestandEle.length  );
			System.out.println(" BestandEff: Row_size: " +  jBestandEff.length + "; col-size: " +  jBestandEff[0].length  + " # 1st data = " + jBestandEff[9][0]
					+ " # 2nd data = " + jBestandEff[9][1] );
			
			
			//-- PCRP1  : mp_1_PCRP --numDateRows = 192//
			double [][] PCRP1= new double[numDateRows][31]; 
			double [][] PCRP1Raw= (double[][])restore.deserializeBeanAsPdouble("mp_1_PCRP",filepath);
			double [][] PCRP1RawTranspose = com.stfe.optim.util.optimMatrixOps.transposeMatrix(PCRP1Raw);			
			//double [][] PCRP1RawTranspose = cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose(DoubleMatrix2D PCRP1Raw);
					
			System.out.println(" PCRP1: size_ROW: " +  PCRP1.length + "; size_COL: " +  PCRP1[0].length    );
			
			
			for (int i =0; i<numDateRows; i++)
				PCRP1[i]=PCRP1RawTranspose[0];
			//oxl.genTxtFileFrom2DData(filepath, "Marketparameter1_PCRP1.txt", PCRP1); 
			
			// -- Zap_PCRP : mpZAp_1_PCRP --//
			double [][] Zap1_PCRP1= new double[numDateRows][31];
			double [][]Zap1_PCRP1Raw = (double[][])restore.deserializeBeanAsPdouble("mpZAp_1_PCRP",filepath);
			double [][] Zap1_PCRP1RawTranspose = com.stfe.optim.util.optimMatrixOps.transposeMatrix(Zap1_PCRP1Raw);
			for (int i =0; i<numDateRows; i++)
				Zap1_PCRP1[i]=Zap1_PCRP1RawTranspose[0];
			//oxl.genTxtFileFrom2DData(filepath, "Marketparameter1_Zap1_PCRP1.txt", Zap1_PCRP1);
			
			
			// -- Swaps_RawValues -- //
			//-- Swaps_RawValues Date--//
			String [][]swapsRVDate = (String[][]) restore.deserializeBeanAsString("srv_Date_SWAPS_RawValues",filepath);
			//-- Swaps_RawValues element --//
			String [][]swapsRVEle = (String[][]) restore.deserializeBeanAsString("srv_DataPort_SWAPS_RawValues",filepath);					
			//-- Swaps_RawValues Data --//
			double [][]swapsRVData = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-srv_SWAPS_RawValues","srv_Data_SWAPS_RawValues");
			
			System.out.println(" Swaps_RawValues: size_data: " +  swapsRVData.length + "; size_date: " +  swapsRVDate.length + "; size_ele: " +  swapsRVEle.length  );
			 
			//-- store date as julian date //
			double[] swapsRVJulDate = new double[swapsRVDate.length];
			for (int i=0; i<swapsRVDate.length; i++) {								
				if  ( com.stfe.optim.util.optimConvDateFmt.isDouble(swapsRVDate[i][0]) ) {
					swapsRVJulDate[i]= Double.parseDouble(swapsRVDate[i][0]);
				} else {				
					swapsRVJulDate[i]= com.stfe.optim.util.optimConvDateFmt.convertToJulian(swapsRVDate[i][0]);
				}
				//System.out.println(" swapsRVDate- dateStr: " + swapsRVDate[i][0] + " JulDate: " + swapsRVJulDate[i] );				
			}
			
			//--Loading the SRV-slice into JOM - 3D//
			// Number of unique dates starting from  31.01.2016  till 31.12.2031; total dates=192 ; 
			// There are 448 rows for each date(2016-2031) // 7 elements x 4 quaterly x 16 years = 448  //; Total rows = 192 x 448 = 86016
			//Final COl H(8=>6=>5):AL(38=>36=>35) 
			int numSRVperDateRows=448;
			double[][][] swapsRVDataEff= new double[numDateRows][][];
			for (int i=0, slcStart=0, slcEnd=(numSRVperDateRows-1); i<numDateRows; i++) {				
				swapsRVDataEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVData, slcStart, slcEnd, 5, 35 );
				slcStart = slcStart + numSRVperDateRows;
				slcEnd= slcEnd + numSRVperDateRows;
			}
			System.out.println(" swapsRVDataEff: 3D-size" + swapsRVDataEff.length  + "; 2D-Row_size: " +  swapsRVDataEff[0].length + "; 2D-col-size: " + swapsRVDataEff[0][0].length  
					+ " # 1st data = " + swapsRVDataEff[0][0][0] 	+ " # Last data = " + swapsRVDataEff[numDateRows-1][numSRVperDateRows-1][0] );
			
			//op.setInputParameter("SwapsRV", new DoubleMatrixND(swapsRVDataEff)  );
			
			//--SchuldVec--//
			double [][]jSchuldRaw = (double[][])restore.deserializeBeanAsPdouble("rvShuld_BM_PLAN_CURRENTQUARTER_DebtClean",filepath); // size 31 x 31
			
			// -- ParametersAndMarketValues -- //									
			//--Parameter-specs--//			
			double [][]jPCCov = (double[][])restore.deserializeBeanAsPdouble("p_pmv_PCCov",filepath); // size 31 x 31
			double [][]jPCRP = (double[][])restore.deserializeBeanAsPdouble("p_pmv_PCRP",filepath); // size 31
			
			System.out.println(" jPCRP: 2D-size-row: " + jPCRP.length  + "; 2D-Row_size-col: " +  jPCRP[0].length + "; " );
			
			//op.setInputParameter("PCCov", new DoubleMatrixND(jPCCov)  );
			//op.setInputParameter("PCRP", new DoubleMatrixND(jPCRP)  );
			
			double [][]jFixRiskCov = (double[][])restore.deserializeBeanAsPdouble("p_pmv_FixingRiskCov_Data",filepath);
			
			//Market
			//--Bean-name as specified in market-specs--//			
			String [][]jRefPCSensiDate = (String[][])restore.deserializeBeanAsString("m_pmv_Date_REF_PC_SENSI",filepath);			
			double [][]jRefPCSensi = (double[][])restore.deserializeBeanAsPdouble("m_pmv_REF_PC_SENSI",filepath);
			double[] jRefPCSensiDateNum = new double[jRefPCSensiDate.length];
			for (int i=0; i<jRefPCSensiDate.length; i++) {								
				if  ( com.stfe.optim.util.optimConvDateFmt.isDouble(jRefPCSensiDate[i][0]) ) {
					jRefPCSensiDateNum[i]= Double.parseDouble(jRefPCSensiDate[i][0]);
				} else {				
					jRefPCSensiDateNum[i]= com.stfe.optim.util.optimConvDateFmt.convertToJulian(jRefPCSensiDate[i][0]);
				}
				//System.out.println(" jRefPCSensiDate- dateStr: " + jRefPCSensiDate[i][0] + " JulDate: " + jRefPCSensiDateNum[i] );				
			}
			//--Loading the MarketValues-slice into JOM - 2D//
			//--Sheet:  324 rows for each date starting from 31.01.2005(should be 31.01.2016)  till 31.12.2031 = total 324 rows --//
			//--31.1.2016 starts at row 134=>133 =>idx=132;;;  (((Row 143=>142=>idx 141 value at AC=-4.97E9))) Slice rows=192
			//jRefPCSensi all cols required colStart -1 ; colSEnd -1 -- // slice2DArray(bigArr, rowStart-1, rowEnd-1, colStart-1, colEnd-1 );
			double [][] jRefPCSensiEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jRefPCSensi, 132, -1, -1, -1 );	
			if (numDateRows == jRefPCSensiEff.length) System.out.println(" jRefPCSensiEff consistent with Bestand data!");
			
			System.out.println(" jRefPCSensiEff: 2D-size-row: " + jRefPCSensiEff.length  + "; 2D-Row_size-col: " +  jRefPCSensiEff[0].length + "; " );					
			//op.setInputParameter("RefPCSensi", new DoubleMatrixND(jRefPCSensiEff)  );
			
			//-- Schuld variables --//
			double[][] jSchuldEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jSchuldRaw, 132, -1, -1, -1 ); 
			if (numDateRows == jSchuldEff.length) System.out.println(" jSchuldEff consistent with Bestand data! RC=192x1");
			System.out.println(" jSchuldEff: 2D-size-row: " + jSchuldEff.length  + "; 2D-Row_size-col: " +  jSchuldEff[0].length );			
			//System.out.println(" jSchuldEff: Data-1 = "	+  jSchuldEff[0][0]+ ", Data-2 = "	+  jSchuldEff[1][0]+ ", Data-3 = "+  jSchuldEff[2][0] + ", Data-Last = " + jSchuldEff[191][0] );
					
					
			double shuld=1.11E+12;
			//op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
			
			
			//constraints-variables  -- //10	10	10	0.25	0.25	0.25	0.25 ///
			double [] upConstraintBase = { 10.00,	50.00,	50.00,	0.25,	0.25,	0.25,	0.50};
			double [] downConstraintBase = { -10.00,	-50.00,	-50.00,	-0.25,	-0.25,	-0.25,	-0.50};
			
			//double [] upConstraintBase = { 10.0, 10,	10,	0.25,	0.25,	0.25,	0.25 };
			//double [] downConstraintBase = { -10.0, -10,	-10,	-0.25,	-0.25,	-0.25,	-0.25 };
			//double [] downConstraintBase = { 1.0, 1,	1,	0.25,	0.25,	0.25,	0.25 };
			
			double [][]jUPConst= new double[1][448];
			//double [][][]jUPConst3D= new double[1][1][448];
			jUPConst[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(upConstraintBase, 64);
			//jUPConst3D[0][0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(upConstraintBase, 64);			
			//double [] upConstraints = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(upConstraintBase, 64);
						 
			double [][]jDNConst= new double[1][];
			double [][][]jDNConst3D= new double[1][1][];
			jDNConst[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 64);
			//jDNConst3D[0][0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 64);			
			//double [] downConstraints = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 64);			
			
			
			double [] constAbsLimitBase = { 60.0, 60, 60, 60, 60,	60, 60 };
			double [] constStkYearLimit60 = { 60.0, 60, 60, 60, 60,	60,	60 };
			double [] constStkQtrLimit15 = { 15.0, 15, 15, 15, 15,	15,	15 };
			double [][]jConstAbsLimitBase= new double[1][];			
			jConstAbsLimitBase[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((constAbsLimitBase), 4);
			
						
			//-- Solver Checker --//
			//SolverTester ipoptchk = new SolverTester() ;
			//System.out.println (" Solver Check : " +  ipoptchk.check_ipopt("C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll") );  
						
			
			final long startTime = System.currentTimeMillis();
			double [][]dVarValStore = new double[192][448]; 
			int flag2d=11;			
			

			
			
			// Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2018: -- Works for 3 x 4  x 7 = 84 DV - good//
			if (flag2d==12)
			{
						
				System.out.println("Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2018: -- Works for 3 x 4  x 7 = 84 DV ");
				
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintBase), 12);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 12);
				System.out.println(" ### Check length Constraint Up: " + jUPConstLtd[0].length + " Down:" + jDNConstLtd[0].length + " Done.");
				
				OptimizationProblem op = new OptimizationProblem();
				op.resetTimer();
				
				op.addDecisionVariable("decisionVars", false, new int[] {1, 84} );			
				op.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );				
				op.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				op.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				op.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");	
				
				
				double StockLimitQ2Y = 15.0;
				op.setInputParameter("StockLimitQ2Y",new DoubleMatrixND(StockLimitQ2Y) );
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimitQ2Y) );
								
				
				//op.addConstraint(" sum (sqrt( decisionVars(0,0:111) ^ 2)  ) 	<= 15 ");								
				//--Set up Constraints quarterly for 3 years, i.e. 4x3=12 -- //
				/*
				for (int i=0, j=0,k=6; i<12; i++) {
									
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimitQ2Y ");
					
					j=k+1;
					k=k+7;
					
					System.out.println(" Constraint sum-absolute-allEleQuaters<15: " + expr + " <= " + StockLimitQ2Y  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
				}
				*/
							
				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2)  + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,14:20) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,21:27) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,28:34) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,35:41) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,42:48) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,49:55) ^ 2) + 0.001d) ) <= StockLimit1Base ");				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,56:62) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,63:69) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,70:76) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,77:83) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				
				
				// Reduce Data+DIM as per 2YQ // 
				int numDateRowsQ3Y = 36;
				int dfkColSize=31;
				int numSRVperDateRowsQ3Y=84; //3 x 4 x 7 = 84				
				double[][][] swapsRVDataffQ3Y= new double[numDateRowsQ3Y][448][dfkColSize];				
				for (int i=0, slcStart=0, slcEnd=(numSRVperDateRowsQ3Y-1); i<numDateRowsQ3Y; i++) {				
					swapsRVDataffQ3Y[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataEff[i], slcStart, slcEnd, 0, (dfkColSize-1) );
				}
				System.out.println(" swapsRVDataffQ3Y: 3D-size: " + swapsRVDataffQ3Y.length  + "; 2D-Row_size: " +  swapsRVDataffQ3Y[0].length + "; 2D-col-size: " + swapsRVDataffQ3Y[0][0].length  
						+ " # 1st data = " + swapsRVDataffQ3Y[0][0][0] 	+ " # Last data = " + swapsRVDataffQ3Y[numDateRowsQ3Y-1][numSRVperDateRowsQ3Y-1][0] );
				
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataffQ3Y_first_0.txt", swapsRVDataffQ3Y[0]);	
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataffQ3Y_last_35.txt", swapsRVDataffQ3Y[35]);
				
				
				double [][] jBestandEffQ3Y= new double[numDateRowsQ3Y][dfkColSize]; 
				jBestandEffQ3Y = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestandEff, 0, (numDateRowsQ3Y-1), 0, (dfkColSize-1) );
				System.out.println(" jBestandEffQ3Y: Row_size: " +  jBestandEffQ3Y.length + "; col-size: " +  jBestandEffQ3Y[0].length  );
								
				double [][] PCRP1Q3Y= new double[numDateRowsQ3Y][dfkColSize]; 
				PCRP1Q3Y = com.stfe.optim.util.optimSliceArray.slice2DArray(PCRP1, 0, (numDateRowsQ3Y-1), 0, (dfkColSize-1) );
				System.out.println(" PCRP1Q3Y: size_ROW: " +  PCRP1Q3Y.length + "; size_COL: " +  PCRP1Q3Y[0].length    );
				
				double [][] jSchuldEffQ3Y =  new double[numDateRowsQ3Y][1];
				jSchuldEffQ3Y = com.stfe.optim.util.optimSliceArray.slice2DArray(jSchuldEff, 0, (numDateRowsQ3Y-1), 0, -1 );
				System.out.println(" jSchuldEffQ3Y: size_ROW: " +  jSchuldEffQ3Y.length + "; size_COL: " +  jSchuldEffQ3Y[0].length    );
				
				
 				// -- Load the data into JOM --//
				op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataffQ3Y )  );				 				
 				op.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
 				double [][][] SwapsRV2DOptPermuted= (double[][][]) op.getInputParameter("SwapsRV2DOpt").toArray();
 				System.out.println(" Permuted swapsRVDataffQ3Y: 3D-size: " + SwapsRV2DOptPermuted.length  + "; 2D-Row_size: " +  
 						SwapsRV2DOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DOptPermuted[0][0].length );
 				
 				op.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1Q3Y) );  				
 				op.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEffQ3Y )  );
 				
 				op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				op.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				op.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  ); 				
 				
 				op.setInputParameter("SchuldOpt", new DoubleMatrixND(jSchuldEffQ3Y)  ); 
 				
 				
 				//--TE Optimization Optim-SensiSz--//
 		  		//op.setObjectiveFunction("minimize", " sum (  (   (PCRP1Opt .* (sum(decisionVars *  SwapsRV2DOpt, 1)+ BestandOpt))  /ShuldVal)  * 10000.0   ) ");
 		  		
 				op.setObjectiveFunction("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" ); //-- //Works
 			 	
 		  		
 		  		
 		  		//Miminize optim//
 				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" );
				
				System.out.println ("**  Objective Function ** : " + op.getObjectiveFunction().getModel().toString() + " # Evaluate : " + op.getObjectiveFunction().toString() );
				
				
				
				 				 
				//-- solve--//				
				System.out.println ("### Solver optimizing for 3Y-Qtr ###  "  );
				op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll");//-- works					
				//if (!optProb.solutionIsOptimal ()) throw new RuntimeException ("An optimal solution was not found");
				if (!op.solutionIsOptimal ()) System.out.println("??? BAD --- An optimal solution was not found! ???  " + op.solutionIsOptimal() );				
				else System.out.println ("#GOOD# -----  Solver done the job! - " + op.solutionIsOptimal()   );
				
				if (op.solutionIsFeasible ()) System.out.println("solutionIsFeasible True");
				else System.out.println("solutionIsFeasible False");
				
				System.out.println(" ++++ getOptimalCost: "+ op.getOptimalCost ());
				
				
				System.out.println ("** JOM Time Report  ** : " + op.timeReport() ); 

				//--compute the objective function --
				op.setInputParameter("dvSolution", op.getPrimalSolution("decisionVars"));			
				System.out.println ("**Quarterly for 3 years (2016-2019) Solution: Optimal Objective Function Value:  ** " + 
						op.parseExpression(" sum ( (  (PCRP1Opt .*  (sum( (dvSolution) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 ) ").evaluateConstant()   );
			
				System.out.println ("Optimized Solution = " +   op.getPrimalSolution("decisionVars").toString() );
				
				
				
			}			
			// --  Done for Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2019: -- Works for 3 x 4  x 7 = 84 DV - good -- //
			// -- Done for if (flag2d==12) -- //
			
			
			
			
			
			// Quarterly - Only for 2 years starting from 31.01.2016 till 31.12.2019: -- Works for 2 x 4  x 7 = 56 DV - good//
			if (flag2d==11)
			{
				System.out.println("Quarterly - Only for 2 years starting from 31.01.2016 till 31.12.2019: -- Works for 2 x 4  x 7 = 56 DV ");
				
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintBase), 8);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 8);
				
				
				OptimizationProblem op = new OptimizationProblem();
				op.resetTimer();
				
				op.addDecisionVariable("decisionVars", false, new int[] {1, 56} );			
				op.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );				
				op.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				op.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				op.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");	
				
				
				double StockLimitQ2Y = 15.0,  StockLimitQ2YPerYear = 60.0;;
				op.setInputParameter("StockLimitQ2Y",new DoubleMatrixND(StockLimitQ2YPerYear) );
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimitQ2Y) );
				
				//op.addConstraint(" sum (sqrt( decisionVars(0,0:111) ^ 2)  ) 	<= 15 ");								
				/*
				for (int i=0, j=0,k=27; i<2; i++) {
									
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.001d ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimitQ2Y ");
					
					j=k+1;
					k=k+28;
					
					System.out.println(" Constraint sum-absolute-allEleQuaters<15: " + expr + " <= " + StockLimitQ2Y  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
				}
				*/
				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2)  + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,14:20) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,21:27) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,28:34) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,35:41) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,42:48) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,49:55) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				
				
				// Reduce Data+DIM as per 2YQ // 
				int numDateRowsQ2Y = 24;
				int dfkColSize=31;
				int numSRVperDateRowsQ2Y=56; //2 x 4 x 7 = 56				
				double[][][] swapsRVDataffQ2Y= new double[numDateRowsQ2Y][448][dfkColSize];				
				for (int i=0, slcStart=0, slcEnd=(numSRVperDateRowsQ2Y-1); i<numDateRowsQ2Y; i++) {				
					swapsRVDataffQ2Y[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataEff[i], slcStart, slcEnd, 0, (dfkColSize-1) );
				}
				System.out.println(" swapsRVDataffQ2Y: 3D-size: " + swapsRVDataffQ2Y.length  + "; 2D-Row_size: " +  swapsRVDataffQ2Y[0].length + "; 2D-col-size: " + swapsRVDataffQ2Y[0][0].length  
						+ " # 1st data = " + swapsRVDataffQ2Y[0][0][0] 	+ " # Last data = " + swapsRVDataffQ2Y[numDateRowsQ2Y-1][numSRVperDateRowsQ2Y-1][0] );
				
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataffQ2Y_first_0.txt", swapsRVDataffQ2Y[0]);	
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataffQ2Y_last_47.txt", swapsRVDataffQ2Y[23]);
				
				
				double [][] jBestandEffQ2Y= new double[numDateRowsQ2Y][dfkColSize]; 
				jBestandEffQ2Y = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestandEff, 0, (numDateRowsQ2Y-1), 0, (dfkColSize-1) );
				System.out.println(" jBestandEffQ2Y: Row_size: " +  jBestandEffQ2Y.length + "; col-size: " +  jBestandEffQ2Y[0].length  );
								
				double [][] PCRP1Q2Y= new double[numDateRowsQ2Y][dfkColSize]; 
				PCRP1Q2Y = com.stfe.optim.util.optimSliceArray.slice2DArray(PCRP1, 0, (numDateRowsQ2Y-1), 0, (dfkColSize-1) );
				System.out.println(" PCRP1Q2Y: size_ROW: " +  PCRP1Q2Y.length + "; size_COL: " +  PCRP1Q2Y[0].length    );
				
				double [][] jSchuldEffQ2Y =  new double[numDateRowsQ2Y][1];
				jSchuldEffQ2Y = com.stfe.optim.util.optimSliceArray.slice2DArray(jSchuldEff, 0, (numDateRowsQ2Y-1), 0, -1 );
				System.out.println(" jSchuldEffQ2Y: size_ROW: " +  jSchuldEffQ2Y.length + "; size_COL: " +  jSchuldEffQ2Y[0].length    );
				
				
				
 				// -- Load the data into JOM --//
				op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataffQ2Y )  );				 				
 				op.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
 				
 				op.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1Q2Y) );  				
 				op.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEffQ2Y )  );
 				
 				op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				op.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				op.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  ); 				
 				op.setInputParameter("SchuldOpt", new DoubleMatrixND(jSchuldEffQ2Y)  );
 				
 				
 				//Vector-Schuld - Works --//
 				
 				op.setObjectiveFunction("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" ); //-- //Works
 			 	 		  		
 				
 				
 				//Scalar Schuld - Works --//
 				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" );
				
 				
				System.out.println ("**  Objective Function ** : " + op.getObjectiveFunction().getModel().toString() + " # Evaluate : " + op.getObjectiveFunction().toString() );
				
				//-- solve--//				
				System.out.println ("### Solver optimizing for 2Y[16-17]-Qtr ###  "  );
				op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll");//-- works					
				//if (!op.solutionIsOptimal ()) throw new RuntimeException ("An optimal solution was not found");
				if (!op.solutionIsOptimal ()) System.out.println("??? BAD --- An optimal solution was not found! ???  " );				
				else System.out.println ("#GOOD#  -----  Solver done the job! - " + op.solutionIsOptimal()   );
				
				if (op.solutionIsFeasible ()) System.out.println("solutionIsFeasible True");
				else System.out.println("solutionIsFeasible False");
				
				System.out.println(" ++++ getOptimalCost: "+ op.getOptimalCost ());
				
				
				System.out.println ("** JOM Time Report  ** : " + op.timeReport() ); 

				//--compute the objective function --
				op.setInputParameter("dvSolution", op.getPrimalSolution("decisionVars"));			
				System.out.println ("**Quarterly for 2 years (2016-2017) Solution: Optimal Objective Function Value:  ** " + 
						op.parseExpression(" sum ( (  (PCRP1Opt .*  (sum( (dvSolution) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 ) ").evaluateConstant()   );
			
				double[][] primalSol =  (double[][]) op.getPrimalSolution("decisionVars").toArray();
				com.stfe.optim.util.optimStaticType.printType(primalSol);
				System.out.println ("Raw Optimized Solution = " +   op.getPrimalSolution("decisionVars").toString() );
				
				//System.out.print("Quaterly distributed Raw Optimized Solution :");
				//for (int i=0; i<primalSol[0].length; i++) {
					//System.out.print( primalSol[0][i]/4 + "; ");
				//}
			}			
			// --  Done for Quarterly - Only for 2 years starting from 31.01.2016 till 31.12.2017: -- Works for 2 x 4  x 7 = 56 DV - good -- //
			// -- Done for if (flag2d==11) -- //
			
			
			
			
			// Biannually : -- Works for  8 x 7 = 56 DV - good//
			if (flag2d==2)
			{
				
				double [][]decVarVal = new double[1][56];
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintBase), 8);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 8);
				
				
				OptimizationProblem op = new OptimizationProblem();
				op.resetTimer();
				
				op.addDecisionVariable("decisionVars", false, new int[] {1, 56} );			
				
				op.setInputParameter("decVarVal",new DoubleMatrixND(decVarVal) );
				
				op.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );				
				op.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				
				op.setInputParameter("StockLimitBase",new DoubleMatrixND(jConstAbsLimitBase) );
				
				
				op.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				op.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");	
				
				
				double StockLimit1Base = 120.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				
				//op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");								
				for (int i=0, j=0,k=6; i<8; i++) {
					//-- expr = sum (sqrt( decisionVars(0,0:447) ^ 2); -- 0: //					
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimit1Base ");
										
					System.out.println(" Constraint sum-absolute-allElePerTwoYear<120: " + expr + " <= " + StockLimit1Base  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
					
					j=k+1;
					k=k+7;					
				}
						
				
				//-- set up the SwapRawData in 192(dates) x 7(elements) x 8(TwiceYearly) form;  numSRVperDateRows=448;numDateRows=192;  swapsRVDataEff Dim:192x448x31 --//
				double [][][] swapsRVDataEffReduced= new double [192][56][31]; 
				for (int date=0, reduced2DIdx=0; date<numDateRows; date++, reduced2DIdx=0) {
										
					for (int twoYear=0, rawIdx=0; twoYear<8; twoYear++, rawIdx += 56) {
					
						//System.out.println(" ****#### Biannual Test IDX: "+ (twoYear+1) );
						for (int srvEle=0; srvEle<7; srvEle++) {					
							
							//System.out.println(" **#### Element: "+ (srvEle+1) );
							
							int numEle=0;
							for (int srvQuat=0; srvQuat<8; srvQuat++) {		
								
								//System.out.println(" *#### Test IDX: "+ (rawIdx+srvEle+numEle) );
								
								for (int dkf=0; dkf<31; dkf++) {
									swapsRVDataEffReduced[date][reduced2DIdx][dkf] +=  swapsRVDataEff[date][rawIdx+srvEle+numEle][dkf] ;	
								}
								numEle += 7;
							}								
							reduced2DIdx++;	
						} // done for 7 elements //
						
					} // done for a year in a date//
										
				} // done for all date//
				System.out.println(" Reduced swapsRVDataEffReduced Dim-XYZ: X=" + swapsRVDataEffReduced.length + " : Y=" + swapsRVDataEffReduced[0].length + " : Z=" + swapsRVDataEffReduced[0][0].length);
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataEffReduced_Biannual.txt", swapsRVDataEffReduced[17]); //-- 16th date has most of values //
				////////
				
				//op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEff )  );
				op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEffReduced )  );
				
 				op.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
 				
 				
 				//op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
 				op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				op.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1) );
 				op.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				op.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEff )  );
 				op.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  );
 				op.setInputParameter("SchuldOpt", new DoubleMatrixND(jSchuldEff)  ); 				
 				
 				//DoubleMatrixND  SwapsRV2DOptEff = op.getInputParameter("SwapsRV2DOpt");
 				
 				//--Check Schuld Data --//
 				DoubleMatrixND SchuldOptEff = op.getInputParameter("SchuldOpt"); 							
 				double[][] SchuldOptEffArr= (double [][]) SchuldOptEff.toArray();
 				com.stfe.optim.util.optimStaticType.printType(SchuldOptEffArr);
 				oxl.genTxtFileFrom2DData(filepath, "SchuldOptEffArr_JOMSession.txt", SchuldOptEffArr);
 				
 				// -- op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); -- //Works
				//op.setObjectiveFunction("minimize",  "sum ( (  (RefPCSensiOpt .*  (( (decisionVars) * SwapsRV2DOpt) + BestandOpt ))  /ShuldVal)* 10000.0) "); //-- works
				
 				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  )  ) "); //-- does not work
				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) * SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0) ");
				
 				
 				//-- Objective Function --//
 				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" );
				
 				op.setObjectiveFunction("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" ); //-- //Works
 				
 				
 				
 			 
 				System.out.println ("**  Objective Function ** : " + op.getObjectiveFunction().getModel().toString() + " # Evaluate : " + op.getObjectiveFunction().toString() );
				
				//-- solve--//				
				System.out.println ("### Biannual -Solver optimizing for opt-cnt:  "  );
				op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll");//-- works					
				//if (!optProb.solutionIsOptimal ()) throw new RuntimeException ("An optimal solution was not found");
				if (!op.solutionIsOptimal ()) System.out.println("??? BAD --- An optimal solution was not found! ???  " );				
				else System.out.println ("#GOOD# TRY 3D -----  Solver done the job! - " + op.solutionIsOptimal()   );
				
				System.out.println ("** JOM Time Report  ** : " + op.timeReport() ); 

				//--compute the objective function --
				op.setInputParameter("dvSolution", op.getPrimalSolution("decisionVars"));			
				System.out.println ("**Biannual Solution: Optimal Objective Function Value:  ** " + 
						op.parseExpression("sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (dvSolution) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )").evaluateConstant()   );
			
				System.out.println ("Optimized Solution = " +   op.getPrimalSolution("decisionVars").toString() );
			
				
				int cnt=1;
				DoubleMatrixND sol = null;
				if (cnt < 0) {
					DoubleMatrixND solutionND =  op.getPrimalSolution("decisionVars");				
					double [][]solution =  (double [][]) solutionND.toArray();
					sol = new  DoubleMatrixND(solution);								
					System.out.println ("Optimized Sol = " +  solutionND.toString() );
					
					//if (cnt==1) { solution[0][0]=123456789.0; solution[0][1]=987654321.0; solution[0][2]=54321.0;}
					
					//-- keep the solution data persistant --//
					if (cnt < 2 ) System.out.println ("ND-Array #size:row=" + solution.length + " # size:col= " + solution[0].length );
					dVarValStore[cnt] = solution[0];
				}
								
			} //- done for 8 x 7 = 56 DV //
			/////////////////////////////////
			
			
			
			

			//-- Annually: Works for  16 x 7 = 112 DV - good//
			if (flag2d==1)
			{
				
				double [][]decVarVal = new double[1][112];
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintBase), 16);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 16);
				
				
				OptimizationProblem op = new OptimizationProblem();
				op.resetTimer();
				
				op.addDecisionVariable("decisionVars", false, new int[] {1, 112} );			
				
				op.setInputParameter("decVarVal",new DoubleMatrixND(decVarVal) );
				
				op.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );
				//op.setInputParameter("upConstraints3D",new DoubleMatrixND(jUPConst3D) );
				
				op.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				//op.setInputParameter("downConstraints","-1 * upConstraints" );
				//op.setInputParameter("downConstraints3D","-1 * upConstraints3D" );
				
				
				op.setInputParameter("StockLimitBase",new DoubleMatrixND(jConstAbsLimitBase) );
				
				
				op.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				op.addConstraint("decisionVars(0,0:111) >= downConstraints(0,0:111)");	
				
				
				//--Constraint -- Sum of absolute values of all decVar should be smaller than 15 --//
				// There are 448 rows for each date(2016-2031) // 7 elements x 4 quarterly x 16 years = 448  //; Total rows = 192 x 448 = 86016
				

				
				
				double StockLimit1Base = 60.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
								
				/*				
				//op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				for (int i=0, j=0,k=6; i<16; i++) {
					//-- expr = sum (sqrt( decisionVars(0,0:447) ^ 2); -- 0: //					
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimit1Base ");
										
					System.out.println(" Constraint sum-absolute-allEleYearly<60: " + expr + " <= " + StockLimit1Base  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
					
					j=k+1;
					k=k+7;					
				}
				*/
				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,14:20) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,21:27) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,28:34) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,35:41) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,42:48) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,49:55) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,56:62) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,63:69) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,70:76) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,77:83) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,84:90) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,91:97) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,98:104) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,105:111) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				
				
				
				//-- reconstruct the data-struct of IN_Data --//
 				//double [][]jBestandSlice = new double[1][31];
 				//jBestandSlice[0] = jBestandEff[cnt];
 				
 				//double [][]swapRV2DSlice = new double[448][31]; 
 				//swapRV2DSlice = swapsRVDataEff[cnt];
 				
 				//double [][] RefPCSensi2DSlice =  new double[1][31]; 
 				//RefPCSensi2DSlice[0] =	jRefPCSensiEff[cnt];
 						
				
				//permute (x , [2;3;1]) 
				
 				//op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( permute(swapsRVDataEff,[2,1,3] )   )  );
				
				//-- set up the SwapRawData in 192(dates) x 7(elements) x 16(years) form;  numSRVperDateRows=448;numDateRows=192;  swapsRVDataEff Dim:192x448x31 --//
				boolean debugSwapRVReduced=false;
				double [][][] swapsRVDataEffReduced= new double [192][112][31]; 
				System.out.println(" ### TEST-DEBUG Reduced swapsRVDataEffReduced");
				for (int date=0, reduced2DIdx=0; date<numDateRows; date++, reduced2DIdx=0) {
										
					if (date==0 ) debugSwapRVReduced=true; else debugSwapRVReduced=false;
					
					for (int aYear=0, rawIdx=0; aYear<16; aYear++, rawIdx += 28) {
						
						if (debugSwapRVReduced) System.out.println(" ****#### Test-Debug IDX for Year : "+ (aYear+1) );
						for (int srvEle=0; srvEle<7; srvEle++) {					
							
							if (debugSwapRVReduced) System.out.print(" **##** Element Nr. : "+ (srvEle+1) + " : At reduced2DIdx="+ reduced2DIdx);
							
							int numEle=0;
							for (int srvQuat=0; srvQuat<4; srvQuat++) {		
								
								if (debugSwapRVReduced) System.out.print(" *#* Quarterly IDX: "+ (rawIdx+srvEle+numEle) );
								
								for (int dkf=0; dkf<31; dkf++) {
									swapsRVDataEffReduced[date][reduced2DIdx][dkf] +=  swapsRVDataEff[date][rawIdx+srvEle+numEle][dkf] ;	
								}
								numEle += 7;
							}	
							if (debugSwapRVReduced) System.out.println("");							 
							reduced2DIdx++;	
						} // done for 7 elements //
						
					} // done for a year in a date//
										
				} // done for all date//
				System.out.println(" Reduced swapsRVDataEffReduced Dim-XYZ: X=" + swapsRVDataEffReduced.length + " : Y=" + swapsRVDataEffReduced[0].length + " : Z=" + swapsRVDataEffReduced[0][0].length);
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataEffReduced_Annual.txt", swapsRVDataEffReduced[17]); //-- 16th date has most of values //
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataEff_Raw.txt", swapsRVDataEff[17]); //-- 16th date has most of values //
				////////
				
				//op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEff )  );
				op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEffReduced )  );
				
 				op.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" ); 				
 				
 				//op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
 				op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				op.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1) );
 				op.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				op.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEff )  );
 				op.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  );
 				 				
 				
 				DoubleMatrixND  SwapsRV2DOptEff = op.getInputParameter("SwapsRV2DOpt"); 				
 				
 								
 				// -- op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); -- //Works
				//op.setObjectiveFunction("minimize",  "sum ( (  (RefPCSensiOpt .*  (( (decisionVars) * SwapsRV2DOpt) + BestandOpt ))  /ShuldVal)* 10000.0) "); //-- works
				
 				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  )  ) "); //-- does not work
				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) * SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0) ");
				
 				
 				//-- Objective Function --//
 				op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" );
								
 				
 				System.out.println ("**  Objective Function  ** : " + op.getObjectiveFunction().getModel().toString() + " # Evaluate : " + op.getObjectiveFunction().toString() );
				
				//-- solve--//				
				System.out.println ("### Solver optimizing :  "  );
				op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll");//-- works					
				//if (!optProb.solutionIsOptimal ()) throw new RuntimeException ("An optimal solution was not found");
				if (!op.solutionIsOptimal ()) System.out.println("??? BAD --- An optimal solution was not found! ???  " );				
				else System.out.println ("#GOOD# TRY 3D -----  Solver done the job! - " + op.solutionIsOptimal() + " for opt-ct: "  );
				
				System.out.println ("** JOM Time Report  ** : " + op.timeReport() ); 

				//--compute the objective function --
				op.setInputParameter("dvSolution", op.getPrimalSolution("decisionVars"));			
				System.out.println ("** Solution: Annual - Optimal Objective Function Value:  ** " + 
						op.parseExpression(" sum ( (  (PCRP1Opt .*  (sum( (dvSolution) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 ) ").evaluateConstant()   );
			
				System.out.println ("** Annual - Optimized Sol = " +  op.getPrimalSolution("decisionVars").toString() );
				
				
				int cnt=1;
				DoubleMatrixND sol = null;
				if (cnt < 0) {
					DoubleMatrixND solutionND =  op.getPrimalSolution("decisionVars");				
					double [][]solution =  (double [][]) solutionND.toArray();
					sol = new  DoubleMatrixND(solution);								
					System.out.println ("Optimized Sol = " +  solutionND.toString() );
					
					//if (cnt==1) { solution[0][0]=123456789.0; solution[0][1]=987654321.0; solution[0][2]=54321.0;}
					
					//-- keep the solution data persistant --//
					if (cnt < 2 ) System.out.println ("ND-Array #size:row=" + solution.length + " # size:col= " + solution[0].length );
					dVarValStore[cnt] = solution[0];
				}
								
			} //- done for 16 x 7 = 112 DV //
			/////////////////////////////////
			
			
			
			/////////////////////////////////
			//-- Works for 448 DV - good//
			if (flag2d==0)
			{
							
				double [][]decVarVal = new double[1][448];
				OptimizationProblem op = new OptimizationProblem();
				op.resetTimer();
				
				op.setInputParameter("upConstraints",new DoubleMatrixND(jUPConst) );
				//op.setInputParameter("upConstraints3D",new DoubleMatrixND(jUPConst3D) );
				
				op.setInputParameter("downConstraints",new DoubleMatrixND(jDNConst) );
				//op.setInputParameter("downConstraints","-1 * upConstraints" );
				//op.setInputParameter("downConstraints3D","-1 * upConstraints3D" );
				
				
				op.setInputParameter("StockLimitBase",new DoubleMatrixND(jConstAbsLimitBase) );
				
				
				op.addDecisionVariable("decisionVars", false, new int[] {1, 448} );			
				
				op.setInputParameter("decVarVal",new DoubleMatrixND(decVarVal) );
				
				
				//op.addConstraint("decisionVars <= upConstraints");
				//op.addConstraint("decisionVars >= downConstraints");
				
				op.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				op.addConstraint("decisionVars(0,0:447) >= downConstraints(0,0:447)");	
				
				
				//--Constraint -- Sum of absolute values of all decVar should be smaller than 15 --//
				// There are 448 rows for each date(2016-2031) // 7 elements x 4 quarterly x 16 years = 448  //; Total rows = 192 x 448 = 86016
				

				//Apply function
				DoubleMatrixND decVarsAbs =  op.getInputParameter("decisionVars");
				//DoubleMatrixND decVarsEleRows=decVars.reshape( new int[] {7,64});								
				//System.out.println("yyyyy   Function Apply: Abs INdCons : " +  decVars.toString()  );	
				//decVarsAbs.assign(0.0);
				final cern.colt.function.tdouble.DoubleFunction coltFunc = cern.jet.math.tdouble.DoubleFunctions.abs;   //jet.math.PlusMult.plusMult(0);;				
				//decVarsAbs = decVarsAbs.assign(coltFunc) ;
				//System.out.println("xxxxx   Function Apply: Abs OutdCons : " +  decVars.toString()  );

				
				//--Constraint for DecVar limit per Quater < 15 --//
				//op.addConstraint(" decisionVars(0,27).assign(coltFunc) <= 15");
				
				//com.jom.Expression decVarAbs = op.parseExpression(" sum(  sqrt( decisionVars(0,0:27) ^ 2) , 1)  " );
				//System.out.println(" +++++ Check the EXPRESSION:" + decVarAbs.toString());
				
				//com.jom.Expression decVarAbsSlc1 = op.parseExpression( "sum( decisionVars(0,0:27) )" );
				
				
				double StockLimit1Base = 60.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				
				//op.addConstraint(" sum (sqrt( decisionVars(0,0:447) ^ 2)  ) 	<= 15 ");
				//for (int i=0, j=0,k=27; i<16; i++) {				
				for (int i=0, j=0,k=27; i<16; i++) {
					//-- expr = sum (sqrt( decisionVars(0,0:447) ^ 2); -- 0:					
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimit1Base ");
					
					
					//System.out.println(" Constraint sum-absolute-allEleQuaters<15: " + expr + " <= " + StockLimit1Base  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
					
					j=k+1;
					k=k+28;					
				}
							
				
				//Final - Works now  ---//
				/*
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:27) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");				
				op.addConstraint(" sum (sqrt( (decisionVars(0,28:55) ^ 2)  + 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,56:83) ^ 2)  + 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,84:111) ^ 2) + 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,112:139) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,140:167) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,168:195) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,196:223) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,224:251) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,252:279) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,280:307) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,308:335) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,336:363) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,364:391) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,392:419) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,420:447) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				*/
				
				
				
				//-- reconstruct the data-struct of IN_Data --//
 				//double [][]jBestandSlice = new double[1][31];
 				//jBestandSlice[0] = jBestandEff[cnt];
 				
 				//double [][]swapRV2DSlice = new double[448][31]; 
 				//swapRV2DSlice = swapsRVDataEff[cnt];
 				
 				//double [][] RefPCSensi2DSlice =  new double[1][31]; 
 				//RefPCSensi2DSlice[0] =	jRefPCSensiEff[cnt];
 						
				
				//permute (x , [2;3;1]) 
				
 				//op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( permute(swapsRVDataEff,[2,1,3] )   )  );
				op.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEff )  );
				 				
 				op.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
 				
 				
 				//op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
 				op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				op.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1) );
 				op.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				op.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEff )  );
 				op.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  );
 				 				
 				
 				DoubleMatrixND  SwapsRV2DOptEff = op.getInputParameter("SwapsRV2DOpt");
 				
 				
 				op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" );
				
								
 				// -- op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); -- //Works
				//op.setObjectiveFunction("minimize",  "sum ( (  (RefPCSensiOpt .*  (( (decisionVars) * SwapsRV2DOpt) + BestandOpt ))  /ShuldVal)* 10000.0) "); //-- works
				
 				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  )  ) "); //-- does not work
				//op.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) * SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0) ");
				
 				
				
				//--display --
				//System.out.println ("** OF1- decVarVal Function ** " +  op.parseExpression(" (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ) ").toString()  );
				//System.out.println ("** OF2- decVarVal Function ** " +  op.parseExpression("(RefPCSensiOpt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))").toString()  );
				//System.out.println ("** OF3- decVarVal Function ** " +  op.parseExpression("(  (RefPCSensiOpt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)").toString()  );
				//System.out.println ("** OF4- decVarVal Function ** " +  op.parseExpression(" sum ( (  (RefPCSensiOpt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )  ").toString()  );
				
				
 				
				System.out.println ("**  Objective Function ** : " + op.getObjectiveFunction().getModel().toString() + " # Evaluate : " + op.getObjectiveFunction().toString() );
				
				
				
				//-- solve--//				
				System.out.println ("### Solver optimizing for opt-cnt:  "  );
				op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll");//-- works					
				//if (!optProb.solutionIsOptimal ()) throw new RuntimeException ("An optimal solution was not found");
				if (!op.solutionIsOptimal ()) System.out.println("??? BAD --- An optimal solution was not found! ???  " );				
				else System.out.println ("#GOOD# TRY 3D -----  Solver done the job! - " + op.solutionIsOptimal() + " for opt-ct: "  );
				
				System.out.println ("** JOM Time Report  ** : " + op.timeReport() ); 
				
				//--compute the objective function --
				op.setInputParameter("dvSolution ", op.getPrimalSolution("decisionVars"));			
				System.out.println ("** Solution: Optimal Objective Function Value:  ** " + 
						op.parseExpression(" sum ( (  (PCRP1Opt .*  (sum( (dvSolution) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 ) ")   );
			
				
				int cnt=1;
				DoubleMatrixND sol = null;
				if (cnt >= 0) {
					DoubleMatrixND solutionND =  op.getPrimalSolution("decisionVars");				
					double [][]solution =  (double [][]) solutionND.toArray();
					sol = new  DoubleMatrixND(solution);								
					System.out.println ("Optimized Sol = " +  solutionND.toString() );
					
					
					
					//if (cnt==1) { solution[0][0]=123456789.0; solution[0][1]=987654321.0; solution[0][2]=54321.0;}
					
					//-- keep the solution data persistant --//
					if (cnt < 2 ) System.out.println ("ND-Array #size:row=" + solution.length + " # size:col= " + solution[0].length );
					dVarValStore[cnt] = solution[0];
				}
				
			}
			/////////////////////////////////
						
			final long endTime   = System.currentTimeMillis();
			double totalTime = (endTime - startTime)/ (60000.0);
			System.out.println(" Time taken (in minutes) to execute the optimization : " + totalTime);			
			//oxl.genTxtFileFrom2DData(initOptimAppConfig.outfiledir, "OptSolution.txt", dVarValStore);
			
			
		} // optimApp_optimize_util-method Over						
		///////////////////////////////////////////////////
		
			
			
		
		///////////////////////////////////////////////////    	
		public void persistResultBean() {
			
			optimRestoreIOBean restoreIOBean = new optimRestoreIOBean();
			
			OptimizationProblem op = new OptimizationProblem();
			
			double shuld=1.11E+12;
			op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
			
			double [][] JCovarianz = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Covar","Covarianz");		
			op.setInputParameter("SwapConv", new DoubleMatrixND(JCovarianz)  );
			
			double [][] JAll7Segments10PCs = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","AllSevPC-1-10"); 	
			op.setInputParameter("All7Segments10PCs", new DoubleMatrixND(JAll7Segments10PCs)  );
			//System.out.println("AllSevPC-1-10 : " +  op.getInputParameter("AllSevPC-1-10").toString() );
			
			double [][] JBestand = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","Bestand"); 
			op.setInputParameter("Bestand", new DoubleMatrixND(JBestand)  );
			
			double [][] JOSolution = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","OptimalSolution"); 
			op.setInputParameter("OptmialSolution", new DoubleMatrixND(JOSolution)  );
			//System.out.println("OptmialSolution : " +  op.getInputParameter("OptmialSolution").toString() );	

			double [][] JPCRP4 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","PCRP4"); 
			op.setInputParameter("PCRP4", new DoubleMatrixND(JPCRP4)  );
			
			
			double [][] JPCRPZAp1 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","PCRPZAp1"); 
			op.setInputParameter("PCRPZAp1", new DoubleMatrixND(JPCRPZAp1)  );

			//-- Calculated out data -- //
			op.setInputParameter("MTProgramm", "All7Segments10PCs * OptmialSolution'");
			op.setInputParameter("MTProgrammBestand", "MTProgramm + Bestand");
			
			
			// -- serialize the calculated data based on solution --//
			String filepath=initOptimAppConfig.outfiledir;						
			optimSerializeBean serialBean = new optimSerializeBean(filepath);			
			//serialBean.serializeResultBeanAsPdouble( (double[][]) sol.toArray(), "OptimalSolution", null);
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("MTProgramm").toArray(), "Program", null );
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("MTProgrammBestand").toArray(), "BestandProgram", null);
			
			op.setInputParameter("MTProgRP4", "( (PCRP4(0:9,0) .* MTProgramm(0:9,0))/ShuldVal) * 10000  ");
			op.setInputParameter("MTProgBstdRP4", "( (PCRP4(0:9,0) .* MTProgrammBestand(0:9,0))/ShuldVal) * 10000  ");		
			op.setInputParameter("SummeMTProgRP4", " sum(MTProgRP4,1) ");
			op.setInputParameter("SummeMTProgBstdRP4", "sum(MTProgBstdRP4,1)");

			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("MTProgRP4").toArray(), "PCRP4Program", null);
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("MTProgBstdRP4").toArray(), "PCRP4BestdProgram", null);
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("SummeMTProgRP4").toArray(), "SumPCRP4Program", null);
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("SummeMTProgBstdRP4").toArray(), "SumPCRP4BestdProgram", null);
			
			
			op.setInputParameter("MTProgRPZAp1", "( (PCRPZAp1 .* MTProgramm)/ShuldVal) * 10000  ");	
			op.setInputParameter("MTProgBstdRPZAp1", "( (PCRPZAp1 .* MTProgrammBestand)/ShuldVal) * 10000  ");
			op.setInputParameter("SummeMTProgRPZAp1", " sum(MTProgRPZAp1,1) ");
			op.setInputParameter("SummeMTProgBstdRPZAp1", "sum(MTProgBstdRPZAp1)");
			
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("MTProgRPZAp1").toArray(), "PCRPZAp1Program", null);
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("MTProgBstdRPZAp1").toArray(), "PCRPZAp1BestdProgram", null);
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("SummeMTProgRPZAp1").toArray(), "SumPCRPZAp1Program", null);
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("SummeMTProgBstdRPZAp1").toArray(), "SumPCRPZAp1BestdProgram", null);
					
				
			// S13 - T13 : SD-Risk: Programm + BestandProgramm - Matrix // 	//=WURZEL(MMULT(MTRANS($S$2:$S$11);MMULT(swapcov;$S$2:$S$11)))/'Kosten 31.07.2016'!$H$1*1000 		
			op.setInputParameter("RiskProgramm"," ( sqrt(MTProgramm' *  (SwapConv * MTProgramm)) /ShuldVal) * 1000 ");
			System.out.println("RiskProgramm : " +  op.getInputParameter("RiskProgramm").toString() );
				
			op.setInputParameter("RiskProgrammBestand"," ( sqrt(MTProgrammBestand' *  (SwapConv * MTProgrammBestand))  / ShuldVal) * 1000 ");
			System.out.println("RiskProgrammBestand : " +  op.getInputParameter("RiskProgrammBestand").toString() );	
			
			serialBean.serializeResultBeanAsPdouble((double[][]) op.getInputParameter("RiskProgramm").toArray(), "RiskCVProg", null );
			serialBean.serializeResultBeanAsPdouble( (double[][]) op.getInputParameter("RiskProgrammBestand").toArray(), "RiskCVBestdProg", null );

		}		
		/////////////////////////////////////////////////
		
		/*
		//-- publish in OXL -- //
		public void publishResultOXL() {
								
			publishOBeanToOXL pubOXL = new publishOBeanToOXL();
									
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","OptimalSolution");  
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","Program");
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","BestandProgram");
			
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","PCRP4Program");
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","PCRP4BestdProgram");
					
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","SumPCRP4Program");
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","SumPCRP4BestdProgram");
			
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","PCRPZAp1Program");
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","PCRPZAp1BestdProgram");
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","SumPCRPZAp1Program");
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","SumPCRPZAp1BestdProgram");
			
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","RiskCVProg");
			pubOXL.publishSolutionOXL("Meta-BeanArchSpecs-Opt","RiskCVBestdProg");
			
		}
		*/
		/////////////////////////////////////////////////
    	
		
		
		/////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		
		public void appOtimSolveAndPublish()  {
			ojbfunc_optimize_util();			
			//publishResultBean();
			//publishResultOXL();
		}
						
			
		
		//AccessBean - Utility function call//		
    	public void getPeristedBeanToOptimize(){
    	
    		//optimUtilAccessBean getBean = new optimUtilAccessBean();
    		//
    		//double [][] rvData = getBean.accessBeanDataToSolve("rvData");    		
    		//for (int i=0; i<3; i++) 
    		//	System.out.println(rvData[i][0] + " : " + rvData[i][1] + " : "+ rvData[i][2]);
    		//
    		
    		String [][] rvDate = new String[3][4] ; //= getBean.accessBeanDateToSolve("rvDate");
    		for (int row=0; row<rvDate.length; row++) {
				for (int col=0; col<rvDate[0].length; col++) {
					if (row < 13) System.out.println(rvDate[row][col] + " : ");
				} 
				//System.out.println("");
    		}
    	}
      
    	
	
    	//-- Apply cern functions --// 
		 public void cernApplyMod(OptimizationProblem op) {
				
				DoubleMatrixND dCons = op.getInputParameter("downConstraints");
				System.out.println("yyyyy   Function Apply: Abs INdCons : " +  dCons.toString()  );
				
				final cern.colt.function.tdouble.DoubleFunction coltFunc = cern.jet.math.tdouble.DoubleFunctions.abs;   //jet.math.PlusMult.plusMult(0);;
				//DoubleMatrixND OutdCons= 
						dCons.assign(coltFunc) ;			
				System.out.println("yyyyy   Function Apply: Abs INdCons : " +  dCons.toString()  );
				//System.out.println("xxxxx   Function Apply: Abs OutdCons : " +  OutdCons.toString()  );
				
				op.setInputParameter("caplDownConstraints", dCons);
		}
		 
		 /*
			final cern.colt.function.tdouble.DoubleFunction coltFunc = 
					cern.jet.math.tdouble.DoubleFunctions.abs; 
			
			//final cern.colt.function.tdouble.DoubleFunction coltFunc =  				
			cern.colt.matrix.tdouble.DoubleMatrix2D mat2D, mat2D2;
			
			
			final cern.colt.function.tdouble.DoubleFunction  matAlgebraFunc = 
					cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose;
				
			
			//mat2D2 = cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose(mat2D).copy();
			
			mat2D2 = cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose(mat2D);
					
			//transpose;  result = transpose(A).copy(); cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose
			//(cern.colt.matrix.tdouble.DoubleMatrix2D);
			 
		*/
		 
		 
		 ////////////////////////////////////////////////////////////////////////
		 //--JOM Contraints Setup --//
				 
		// Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2018: -- Works for 3 x 4  x 7 = 84 DV - good//
		 public void setupJOMCOnstraints3YQtr(OptimizationProblem op) {
							
				double StockLimit1Base = 15.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				//op.addConstraint(" sum (sqrt( decisionVars(0,0:111) ^ 2)  ) 	<= 15 ");								
				//--Set up Constraints quarterly for 3 years, i.e. 4x3=12 -- //
				/*
				for (int i=0, j=0,k=6; i<12; i++) {
									
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimitQ2Y ");
					
					j=k+1;
					k=k+7;
					
					System.out.println(" Constraint sum-absolute-allEleQuaters<15: " + expr + " <= " + StockLimitQ2Y  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
				}
				*/
							
				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2)  + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,14:20) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,21:27) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,28:34) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,35:41) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,42:48) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,49:55) ^ 2) + 0.001d) ) <= StockLimit1Base ");				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,56:62) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,63:69) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,70:76) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,77:83) ^ 2) + 0.001d) ) <= StockLimit1Base ");
		 }
		 
		 
		// Quarterly - Only for 2 years starting from 31.01.2016 till 31.12.2019: -- Works for 2 x 4  x 7 = 56 DV - good//
		 public void setupJOMCOnstraints2YQtr(OptimizationProblem op) {
			
				double StockLimit1Base = 15.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				//op.addConstraint(" sum (sqrt( decisionVars(0,0:111) ^ 2)  ) 	<= 15 ");								
				/*
				for (int i=0, j=0,k=27; i<2; i++) {
									
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.001d ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimitQ2Y ");
					
					j=k+1;
					k=k+28;
					
					System.out.println(" Constraint sum-absolute-allEleQuaters<15: " + expr + " <= " + StockLimitQ2Y  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
				}
				*/
				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2)  + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,14:20) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,21:27) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,28:34) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,35:41) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,42:48) ^ 2) + 0.001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,49:55) ^ 2) + 0.001d) ) <= StockLimit1Base ");
		 }

		 
		 //-- Annually: Works for  16 x 7 = 112 DV - good//
		 public void setupJOMCOnstraints16YAnnually(OptimizationProblem op) {
			
				double StockLimit1Base = 60.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				/*				
				//op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				for (int i=0, j=0,k=6; i<16; i++) {
					//-- expr = sum (sqrt( decisionVars(0,0:447) ^ 2); -- 0: //					
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimit1Base ");
										
					System.out.println(" Constraint sum-absolute-allEleYearly<60: " + expr + " <= " + StockLimit1Base  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
					
					j=k+1;
					k=k+7;					
				}
				*/
				
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,14:20) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,21:27) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,28:34) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,35:41) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,42:48) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,49:55) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,56:62) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,63:69) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,70:76) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,77:83) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,84:90) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,91:97) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,98:104) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				op.addConstraint(" sum (   sqrt( (decisionVars(0,105:111) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");
				
		 }
		 
		 
		// 56 DV - Biannual
		 public void setupJOMCOnstraints16YBiannually(OptimizationProblem op) {
			
				double StockLimit1Base = 120.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				//op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");								
				for (int i=0, j=0,k=6; i<8; i++) {
					//-- expr = sum (sqrt( decisionVars(0,0:447) ^ 2); -- 0: //					
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimit1Base ");
										
					System.out.println(" Constraint sum-absolute-allElePerTwoYear<120: " + expr + " <= " + StockLimit1Base  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
					
					j=k+1;
					k=k+7;					
				}
						
		 }
		 
		 
		 
		 //-- Quaterly - Works for   16 x 7 x 4 = 448 DV //
		 public void setupJOMCOnstraints16YQuaterly(OptimizationProblem op) {
			
				double StockLimit1Base = 15.0;
				op.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				//op.addConstraint(" sum (sqrt( decisionVars(0,0:447) ^ 2)  ) 	<= 15 ");
				//for (int i=0, j=0,k=27; i<16; i++) {				
				for (int i=0, j=0,k=27; i<16; i++) {
					//-- expr = sum (sqrt( decisionVars(0,0:447) ^ 2); -- 0:					
					String expr = " sum (sqrt( decisionVars(0," +j+":"+k  +  ") ^ 2 + 0.000001 ) )";
					com.jom.Expression exprtrc = op.addConstraint(expr + " <= StockLimit1Base ");
					
					
					//System.out.println(" Constraint sum-absolute-allEleQuaters<15: " + expr + " <= " + StockLimit1Base  + " ### Expression: " + exprtrc.evaluate() + " #toString: " + exprtrc.toString() );
					
					j=k+1;
					k=k+28;					
				}
				
				
				/*
				op.addConstraint(" sum (   sqrt( (decisionVars(0,0:27) ^ 2) + 0.00000001d) ) <= StockLimit1Base ");				
				op.addConstraint(" sum (sqrt( (decisionVars(0,28:55) ^ 2)  + 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,56:83) ^ 2)  + 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,84:111) ^ 2) + 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,112:139) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,140:167) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,168:195) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,196:223) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,224:251) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,252:279) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,280:307) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,308:335) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,336:363) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,364:391) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,392:419) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				op.addConstraint(" sum (sqrt( (decisionVars(0,420:447) ^ 2)+ 0.00000001d)  ) <= StockLimit1Base ");
				*/
		 }
		 
		 
		 ////////////////////////////////////////////////////////////////////////
		 
		 
		 
		 ////////////////////////////////////////////////////////////////////////		
		//-- set up reduced-SRV for the SwapRawData in 192(dates); x 7(elements) x 4 Qtr + 16(years)=448  form;  numSRVperDateRows=448;numDateRows=192;  swapsRVDataEff Dim:192x448x31 --//
		//--swapsRVDataEffReduced  in 192(dates); x 7(elements) x 1 Year + 16(years)=112; All Qarterly-DKF transformed into 1 Year adding the DKF linearly; --//
		public double[][][] reduceSRVaddingDKF(double [][][]swapsRVDataEff, int numDateRows, int reducedForYear )  {
		
			boolean debugSwapRVReduced=false;
			int numDKFperDate=112; // --7 * 16;//
			int reducedYearperDate=16;
			
			//--reduce atleast annually--//
			if (reducedForYear <= 1) { 
				reducedForYear=1;
				reducedYearperDate=16;
				numDKFperDate= 7 * 16;
			}
						
			//--reduce atmost biannually--//
			if (reducedForYear >= 2 ) { 
				reducedForYear=2;
				reducedYearperDate=8;
				numDKFperDate= 7 * 8;
			}
			
			
			double [][][] swapsRVDataEffReduced= new double [192][numDKFperDate][31]; 
			
			System.out.println(" ### TEST-DEBUG Reduced swapsRVDataEffReduced");
			for (int date=0, reduced2DIdx=0; date<numDateRows; date++, reduced2DIdx=0) {
									
				if (date==0 ) debugSwapRVReduced=true; else debugSwapRVReduced=false;
				
				for (int aYear=0, rawIdx=0; aYear<16; aYear++, rawIdx += 28) {
					
					if (debugSwapRVReduced) System.out.println(" ****#### Test-Debug IDX for Year : "+ (aYear+1) );
					for (int srvEle=0; srvEle<7; srvEle++) {					
						
						if (debugSwapRVReduced) System.out.print(" **##** Element Nr. : "+ (srvEle+1) + " : At reduced2DIdx="+ reduced2DIdx);
						
						int numEle=0;
						for (int srvQuat=0; srvQuat<4; srvQuat++) {		
							
							if (debugSwapRVReduced) System.out.print(" *#* Quarterly IDX: "+ (rawIdx+srvEle+numEle) );
							
							for (int dkf=0; dkf<31; dkf++) {
								swapsRVDataEffReduced[date][reduced2DIdx][dkf] +=  swapsRVDataEff[date][rawIdx+srvEle+numEle][dkf] ;	
							}
							numEle += 7;
						}	
						if (debugSwapRVReduced) System.out.println("");							 
						reduced2DIdx++;	
					} // done for 7 elements //
					
				} // done for a year in a date//
									
			} // done for all date//
			System.out.println(" Reduced swapsRVDataEffReduced Dim-XYZ: X=" + swapsRVDataEffReduced.length + " : Y=" + swapsRVDataEffReduced[0].length + " : Z=" + swapsRVDataEffReduced[0][0].length);
			//oxl.genTxtFileFrom2DData(filepath, "swapsRVDataEffReduced_Annual.txt", swapsRVDataEffReduced[17]); //-- 16th date has most of values //
			//oxl.genTxtFileFrom2DData(filepath, "swapsRVDataEff_Raw.txt", swapsRVDataEff[17]); //-- 16th date has most of values //
			
			return swapsRVDataEffReduced;
	    } //--redSRV done--//
		
	    
		
    
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    