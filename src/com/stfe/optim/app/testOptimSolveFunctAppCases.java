
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



    public class testOptimSolveFunctAppCases {
		
	/*			
    	private String filepath;
    	private createOXLFromIBean oxl;
    	
    	private OptimizationProblem jom=null;
    	private optimSolveDataset dataSet=null;
		private optimSolveRuleConfig ruleConf=null;	
		private optimSolveFunctUtil functUtil=null;
		
		private double [][]jUPConstLtd;
		private double [][]jDNConstLtd;							
		private double[][][] joSwapsRVDataEff; 
		private double [][] joBestandEff; 
		private double [][] joPCRP1Eff;
		private double [][] joSchuldEff;
		private double stockVolLimitEff;
		
		private double[][] joInitDVSolSetJOM;
		private double[][] joInitDVSolSetXL;

		private double[][] joRefPCSensiEff;
		private double[][] joPCCov;
		
		
	*/	
		
    	
    		
		public void test_ojbfunc_optimize_cases() 	{
			
			
			System.out.println(" Test the ojbfunc_optimize");
			
			
			String filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			//optimDeserializeBean restore = new optimDeserializeBean(filepath);
			//optimRestoreIOBean restoreIOBean = new optimRestoreIOBean();
			createOXLFromIBean oxl = new createOXLFromIBean();
					
						
			
			//--Dataset --//
			optimSolveRuleConfig ruleConf = new optimSolveRuleConfig();
			//ruleConf.setDKFRuleConfig(numYears,  addDKF,  addDKFForYears);
			
			optimSolveDataset dataSet= new optimSolveDataset(ruleConf);
			dataSet.optimLoadEffectiveData();
			
			int numDateRows = dataSet.numDates;  
			double [][] jBestandEff = dataSet.BestandEff;
			
			double [][] PCRP1=dataSet.PCRP1Eff;			
			double[][][] swapsRVDataEff = dataSet.swapsRVEff;
						
			double[][] jRefPCSensiEff = dataSet.RefPCSensiEff;
			double[][] jPCCov = dataSet.PCCovEff;
			
			double[][] jSchuldEff = dataSet.SchuldEff;
			double shuld=dataSet.SchuldVal;;
			
			double [] upConstraintBase = dataSet.upConstraintBase7V;;
			double [] downConstraintBase = dataSet.downConstraintBase7V;    
			double [] initSolZero = dataSet.init0Sol7V;
			
			double [][]jConstAbsLimitBase= new double[1][];			
			jConstAbsLimitBase[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((dataSet.StockLimit1Yr7V), 4);
						
						
			double [][]jUPConst= new double[1][448];
			jUPConst[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(upConstraintBase, 64);
			double [][]jDNConst= new double[1][];			
			jDNConst[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 64);
			
						
			
			optimSolveFunctUtil functUtil= new optimSolveFunctUtil();
						
						
			//-- Solver Checker --//
			//SolverTester ipoptchk = new SolverTester() ;
			//System.out.println (" Solver Check : " +  ipoptchk.check_ipopt("C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll") );  
						
			
			final long startTime = System.currentTimeMillis();			
			OptimizationProblem jom = new OptimizationProblem();
			jom.resetTimer();
			
			int flag2d=102;			
			
			
			// Quarterly - Only for 6 years starting from 31.01.2016 till 31.12.2021: -- Works for 6 x 4  x 7 = 168 DV - good//
			if (flag2d==106)
				{
									
				System.out.println("Quarterly - Only for 16 years starting from 31.01.2016 till 31.12.2018: -- Works for 6 x 4  x 7 = 168 DV ");
							
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((dataSet.upConstraintBase7V), 24);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((dataSet.downConstraintBase7V), 24);
				//System.out.println(" ### Check length Constraint Up: " + jUPConstLtd[0].length + " Down:" + jDNConstLtd[0].length + " Done."+ jUPConstLtd[0][76] +"; " + jUPConstLtd[0][83]);
							
							
				jom.addDecisionVariable("decisionVars", false, new int[] {1, 168} );			
				jom.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );				
				jom.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				jom.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				jom.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");	
							
							
				double StockLimitQ2Y = 15.0;
				jom.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimitQ2Y) );
										
																		
				//--Set up Constraints quarterly for 6 years, i.e. 4x6=24 -- //				
				functUtil.setVolumeConstraint(jom, 15.0d, 6, 4);
										
							
				// Reduce Data+DIM as per 6YQ // 
				int numDateRowsQ6Y = 72;
				int dfkColSize=31;
				int numSRVperDateRowsQ6Y=168; //6 x 4 x 7 = 168				
				double[][][] swapsRVDataffQ6Y= new double[numDateRowsQ6Y][448][dfkColSize];				
				for (int i=0, slcStart=0, slcEnd=(numSRVperDateRowsQ6Y-1); i<numDateRowsQ6Y; i++) {				
					swapsRVDataffQ6Y[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataEff[i], slcStart, slcEnd, 0, (dfkColSize-1) );
				}
				System.out.println(" swapsRVDatEffQ6Y: 3D-size: " + swapsRVDataffQ6Y.length  + "; 2D-Row_size: " +  swapsRVDataffQ6Y[0].length + "; 2D-col-size: " + swapsRVDataffQ6Y[0][0].length  
							+ " # 1st data = " + swapsRVDataffQ6Y[0][0][0] 	+ " # Last data = " + swapsRVDataffQ6Y[numDateRowsQ6Y-1][numSRVperDateRowsQ6Y-1][0] );
							
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataffQ6Y_first_0.txt", swapsRVDataffQ6Y[0]);	
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataffQ6Y_last_71.txt", swapsRVDataffQ6Y[71]);
							
							
				double [][] jBestandEffQ6Y= new double[numDateRowsQ6Y][dfkColSize]; 
				jBestandEffQ6Y = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestandEff, 0, (numDateRowsQ6Y-1), 0, (dfkColSize-1) );
				System.out.println(" jBestandEffQ6Y: Row_size: " +  jBestandEffQ6Y.length + "; col-size: " +  jBestandEffQ6Y[0].length  );
											
				double [][] PCRP1Q6Y= new double[numDateRowsQ6Y][dfkColSize]; 
				PCRP1Q6Y = com.stfe.optim.util.optimSliceArray.slice2DArray(PCRP1, 0, (numDateRowsQ6Y-1), 0, (dfkColSize-1) );
				System.out.println(" PCRP1Q6Y: size_ROW: " +  PCRP1Q6Y.length + "; size_COL: " +  PCRP1Q6Y[0].length    );
							
				double [][] jSchuldEffQ6Y =  new double[numDateRowsQ6Y][1];
				jSchuldEffQ6Y = com.stfe.optim.util.optimSliceArray.slice2DArray(jSchuldEff, 0, (numDateRowsQ6Y-1), 0, -1 );
				System.out.println(" jSchuldEffQ6Y: size_ROW: " +  jSchuldEffQ6Y.length + "; size_COL: " +  jSchuldEffQ6Y[0].length    );
							
				//-- Setup the Excel-result as intial solution --//
				double[][] initDVSolSetJOM = new double[1][168];
				double [] initSolSetJOM = {0.00,	-0.28,	-15.15,	-0.20,	-0.25,	-0.25,	-0.37};
				//initDVSolSetJOM[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolSetJOM), 24);
							
				double[][] initDVSolSetXL = new double[1][168];
				double [] initSolSetXL = {0.00d, -4.87d, -10.44d,	-0.20,	-0.25,	-0.25, -0.50};
				initDVSolSetXL[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolSetXL), 24);
				jom.setInitialSolution("decisionVars", new DoubleMatrixND(initDVSolSetXL));
							
			 	// -- Load the data into JOM --//
				jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataffQ6Y )  );				 				
			 	jom.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
			 	double [][][] SwapsRV2DOptPermuted= (double[][][]) jom.getInputParameter("SwapsRV2DOpt").toArray();
			 	System.out.println(" Permuted swapsRVDataffQ3Y: 3D-size: " + SwapsRV2DOptPermuted.length  + "; 2D-Row_size: " +  
			 				SwapsRV2DOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DOptPermuted[0][0].length );
			 				
			 	jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1Q6Y) );  				
			 	jom.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEffQ6Y )  );
			 			
			 	jom.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
			 				
			 	jom.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
			 	jom.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  ); 				
			 				
			 	jom.setInputParameter("SchuldOpt", new DoubleMatrixND(jSchuldEffQ6Y)  ); 
			 				
			 				
			 	//--TE Optimization Optim-SensiSz combined together --//
			 	//jom.setObjectiveFunction("minimize", " sum (  (   (PCRP1Opt .* (sum(decisionVars *  SwapsRV2DOpt, 1)+ BestandOpt))  /ShuldVal)  * 10000.0   ) ");
			 		  		
			 	//Schuld-vector Miminize optim//
			 	jom.setObjectiveFunction("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" ); //-- //Works
			 			 	
			 		  		
			 		  		
			 	//Schuld-Scalar Miminize optim//
			 	//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); //--works--//
							
				System.out.println ("**  Objective Function ** : " + jom.getObjectiveFunction().getModel().toString() + " # Evaluate : " + jom.getObjectiveFunction().toString() );
						
							
				//--Calculate and compare the value of objective function --//
				double[][] solInit = new double[1][168];
				solInit[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolZero), 24);
				jom.setInputParameter("solInitOpt", new DoubleMatrixND(solInit));
				//jom.setInitialSolution("decisionVars", new DoubleMatrixND (solInit));
				System.out.println ("** Zero-Init : Schuld-Scalar: Quarterly for 6 years (2016-2021) Solution: Optimal Objective Function Value:  ** " + 
				jom.parseExpression(" sum ( (  (PCRP1Opt .*  (sum( (solInitOpt) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 ) ").evaluateConstant()   );
						
				System.out.println ("** Zero-Init: Schuld-Vector : Quarterly for 3 years (2016-2021) Solution: Optimal Objective Function Value:  ** " + 
				jom.parseExpression("sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (solInitOpt) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )").evaluateConstant()   );
							
							 				 
				//-- solve--//				
				System.out.println ("**Quarterly for 6 years (2016-2021) Solution ** ");
				System.out.println ("### Solver optimizing for 6Y-Qtr ###  "  );
							
			}			
			// --  Done for Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2019: -- Works for 3 x 4  x 7 = 84 DV - good -- //
			// -- Done for if (flag2d==12) -- //     
						
			
			
			
						
			// Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2018: -- Works for 3 x 4  x 7 = 84 DV - good//
			if (flag2d==103)
			{
						
				System.out.println("Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2018: -- Works for 3 x 4  x 7 = 84 DV ");
				
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((dataSet.upConstraintBase7V), 12);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((dataSet.downConstraintBase7V), 12);
				System.out.println(" ### Check length Constraint Up: " + jUPConstLtd[0].length + " Down:" + jDNConstLtd[0].length + " Done."+ jUPConstLtd[0][76] +"; " + jUPConstLtd[0][83]);
				
				
				jom.addDecisionVariable("decisionVars", false, new int[] {1, 84} );			
				jom.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );				
				jom.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				jom.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				jom.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");	
				
				
				double StockLimitQ2Y = 15.0;
				jom.setInputParameter("StockLimitQ2Y",new DoubleMatrixND(StockLimitQ2Y) );
				jom.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimitQ2Y) );
								
															
				//--Set up Constraints quarterly for 3 years, i.e. 4x3=12 -- //				
				functUtil.setVolumeConstraint(jom, 15.0d, 3, 4);
				
				
				
				// Reduce Data+DIM as per 3YQ // 
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
				
				//-- Setup the Excel-result as intial solution --//
				double[][] initDVSolSetJOM = new double[1][84];
				double [] initSolSetJOM = {0.00,	-0.28,	-15.15,	-0.20,	-0.25,	-0.25,	-0.37};
				//initDVSolSetJOM[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolSetJOM), 12);
				
				double[][] initDVSolSetXL = new double[1][84];
				double [] initSolSetXL = {0.00d, -4.87d, -10.44d,	-0.20,	-0.25,	-0.25, -0.50};
				initDVSolSetXL[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolSetXL), 12);
				jom.setInitialSolution("decisionVars", new DoubleMatrixND(initDVSolSetXL));
				//jom.setInputParameter("decisionVarsInit", new DoubleMatrixND( initDVSolSet )  );
				//jom.setInitialSolution("decisionVars", jom.getInputParameter("decisionVarsInit"));
				
				
 				// -- Load the data into JOM --//
				jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataffQ3Y )  );				 				
 				jom.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
 				double [][][] SwapsRV2DOptPermuted= (double[][][]) jom.getInputParameter("SwapsRV2DOpt").toArray();
 				System.out.println(" Permuted swapsRVDataffQ3Y: 3D-size: " + SwapsRV2DOptPermuted.length  + "; 2D-Row_size: " +  
 						SwapsRV2DOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DOptPermuted[0][0].length );
 				
 				jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1Q3Y) );  				
 				jom.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEffQ3Y )  );
 				
 				jom.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				jom.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				jom.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  ); 				
 				
 				jom.setInputParameter("SchuldOpt", new DoubleMatrixND(jSchuldEffQ3Y)  ); 
 				
 				
 				//--TE Optimization Optim-SensiSz combined together --//
 		  		//jom.setObjectiveFunction("minimize", " sum (  (   (PCRP1Opt .* (sum(decisionVars *  SwapsRV2DOpt, 1)+ BestandOpt))  /ShuldVal)  * 10000.0   ) ");
 		  		
 				//Schuld-vector Miminize optim//
 				jom.setObjectiveFunction("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" ); //-- //Works
 			 	
 		  		
 		  		
 		  		//Schuld-Scalar Miminize optim//
 				//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); //--works--//
				
				System.out.println ("**  Objective Function ** : " + jom.getObjectiveFunction().getModel().toString() + " # Evaluate : " + jom.getObjectiveFunction().toString() );
			
				
				//--Calculate and compare the value of objective function --//
				double[][] solInit = new double[1][84];
				solInit[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolZero), 12);
				jom.setInputParameter("solInitOpt", new DoubleMatrixND(solInit));
				//jom.setInitialSolution("decisionVars", new DoubleMatrixND (solInit));
				System.out.println ("** Zero-Init : Schuld-Scalar: Quarterly for 3 years (2016-2018) Solution: Optimal Objective Function Value:  ** " + 
						jom.parseExpression(" sum ( (  (PCRP1Opt .*  (sum( (solInitOpt) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 ) ").evaluateConstant()   );
			
				System.out.println ("** Zero-Init: Schuld-Vector : Quarterly for 3 years (2016-2018) Solution: Optimal Objective Function Value:  ** " + 
						jom.parseExpression("sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (solInitOpt) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )").evaluateConstant()   );
				
				 				 
				//-- solve--//				
				System.out.println ("### Solver optimizing for 3Y-Qtr ###  "  );
				
			}			
			// --  Done for Quarterly - Only for 3 years starting from 31.01.2016 till 31.12.2019: -- Works for 3 x 4  x 7 = 84 DV - good -- //
			// -- Done for if (flag2d==12) -- //     
			
							
			
			
			// Quarterly - Only for 2 years starting from 31.01.2016 till 31.12.2019: -- Works for 2 x 4  x 7 = 56 DV - good//
			if (flag2d==102)
			{
				System.out.println("Quarterly - Only for 2 years starting from 31.01.2016 till 31.12.2019: -- Works for 2 x 4  x 7 = 56 DV ");
				
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintBase), 8);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 8);
				
				
				jom.addDecisionVariable("decisionVars", false, new int[] {1, 56} );			
				jom.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );				
				jom.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				jom.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				jom.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");	
				
				
				double StockLimitQ2Y = 15.0,  StockLimitQ2YPerYear = 60.0;;
				jom.setInputParameter("StockLimitQ2Y",new DoubleMatrixND(StockLimitQ2YPerYear) );
				jom.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimitQ2Y) );
				
				
				functUtil.setVolumeConstraint(jom, 15.0d, 2, 4);
				
				
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
				oxl.genTxtFileFrom2DData(filepath, "jSchuldEff-Qtr-2Y.txt", jSchuldEffQ2Y);
				
				
 				// -- Load the data into JOM --//
				jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataffQ2Y )  );				 				
 				jom.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
 				
 				jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1Q2Y) );  				
 				jom.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEffQ2Y )  );
 				
 				jom.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				jom.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				jom.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  ); 				
 				jom.setInputParameter("SchuldOpt", new DoubleMatrixND(jSchuldEffQ2Y)  );
 				
 				
 				//Vector-Schuld - Works --// 				
 				//jom.setObjectiveFunction("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" ); //-- //Works
 			 	 		  		
 				jom.setObjectiveFunction("minimize",  "sum ( ( (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" );
 				
 				
 				//Scalar Schuld - Works --//
 				//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); //--works--//
				
 				
				System.out.println ("**  Objective Function ** : " + jom.getObjectiveFunction().getModel().toString() + " # Evaluate : " + jom.getObjectiveFunction().toString() );
				
				//-- solve--//				
				System.out.println ("### Solver optimizing for 2Y[16-17]-Qtr ###  "  );
				
			}			
			// --  Done for Quarterly - Only for 2 years starting from 31.01.2016 till 31.12.2017: -- Works for 2 x 4  x 7 = 56 DV - good -- //
			// -- Done for if (flag2d==11) -- //
			
			
			
			
			// Biannually : Addded DKF for two years -- Works for  8 x 7 = 56 DV - good//
			flag2mod:
			if (flag2d==2)
			{
				
				double [][]decVarVal = new double[1][56];
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintBase), 8);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 8);
				
				jom.addDecisionVariable("decisionVars", false, new int[] {1, 56} );			
				
				jom.setInputParameter("decVarVal",new DoubleMatrixND(decVarVal) );
				
				jom.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );				
				jom.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				
				jom.setInputParameter("StockLimitBase",new DoubleMatrixND(jConstAbsLimitBase) );
				
				
				jom.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				jom.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");	
				
				
				double StockLimit1Base = 120.0;
				jom.setInputParameter("StockLimit1Base",new DoubleMatrixND(StockLimit1Base) );
				
				
				functUtil.setVolumeConstraint(jom, 120.0d, 8, 0.5);
				
				
				//double [][][] swapsRVDataEffReduced= buildRVDKFReduced (false, swapsRVDataEff,  numDateRows, int numAnnuality=2 //Biannual, int numYears=16); --//
				double [][][] swapsRVDataEffRed= functUtil.buildRVDKFReduced (false, swapsRVDataEff,  numDateRows, 2, 16,31); 
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataEffRed_Biannual.txt", swapsRVDataEffRed[17]); 
						
				
				if (false) break flag2mod; 
				
				//jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEff )  );
				jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEffRed )  );
				
 				jom.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
 				
 				
 				//jom.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
 				jom.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1) );
 				jom.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				jom.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEff )  );
 				jom.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  );
 				jom.setInputParameter("SchuldOpt", new DoubleMatrixND(jSchuldEff)  ); 				
 				
 				//DoubleMatrixND  SwapsRV2DOptEff = jom.getInputParameter("SwapsRV2DOpt");
 				
 				//--Check Schuld Data --//
 				DoubleMatrixND SchuldOptEff = jom.getInputParameter("SchuldOpt"); 							
 				double[][] SchuldOptEffArr= (double [][]) SchuldOptEff.toArray();
 				com.stfe.optim.util.optimStaticType.printType(SchuldOptEffArr);
 				oxl.genTxtFileFrom2DData(filepath, "SchuldOptEffArr_JOMSession.txt", SchuldOptEffArr);
 				
 				// -- jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); -- //Works
				//jom.setObjectiveFunction("minimize",  "sum ( (  (RefPCSensiOpt .*  (( (decisionVars) * SwapsRV2DOpt) + BestandOpt ))  /ShuldVal)* 10000.0) "); //-- works
				
 				//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  )  ) "); //-- does not work
				//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) * SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0) ");
				
 				
 				//-- Objective Function --//
 				//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" );
				
 				jom.setObjectiveFunction("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" ); //-- //Works
 				 								
 			 
 				System.out.println ("**  Objective Function ** : " + jom.getObjectiveFunction().getModel().toString() + " # Evaluate : " + jom.getObjectiveFunction().toString() );
				
				//-- solve--//				
				System.out.println ("### Biannual -Solver optimizing for opt-cnt:  "  );
						
								
			} //- done for 8 x 7 = 56 DV //
			/////////////////////////////////
			
			
			
			

			//-- Annually: Added DKF for one year:  Works for  16 x 7 = 112 DV - good//
			flag1mod: 
			
			if (flag2d==1)
			{
				
				double [][]decVarVal = new double[1][112];
				double [][]jUPConstLtd= new double[1][];				
				jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintBase), 16);				
				double [][]jDNConstLtd= new double[1][];				
				jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), 16);
								
				jom.addDecisionVariable("decisionVars", false, new int[] {1, 112} );			
				
				jom.setInputParameter("decVarVal",new DoubleMatrixND(decVarVal) );
				
				jom.setInputParameter("upConstraints",new DoubleMatrixND(jUPConstLtd) );
				//jom.setInputParameter("upConstraints3D",new DoubleMatrixND(jUPConst3D) );
				
				jom.setInputParameter("downConstraints",new DoubleMatrixND(jDNConstLtd) );
				//jom.setInputParameter("downConstraints","-1 * upConstraints" );
				//jom.setInputParameter("downConstraints3D","-1 * upConstraints3D" );
				
				
				jom.setInputParameter("StockLimitBase",new DoubleMatrixND(jConstAbsLimitBase) );
				
				
				jom.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
				jom.addConstraint("decisionVars(0,0:111) >= downConstraints(0,0:111)");	
				
				
				//--Constraint -- Sum of absolute values of all decVar should be smaller than 15 --//
				// There are 448 rows for each date(2016-2031) // 7 elements x 4 quarterly x 16 years = 448  //; Total rows = 192 x 448 = 86016								
				functUtil.setVolumeConstraint(jom, 60.0d, 16, 1);
								
								
				//-- reconstruct the data-struct of IN_Data --//
 				//double [][]jBestandSlice = new double[1][31];
 				//jBestandSlice[0] = jBestandEff[cnt];
 				
 				//double [][]swapRV2DSlice = new double[448][31]; 
 				//swapRV2DSlice = swapsRVDataEff[cnt];
 				
 				//double [][] RefPCSensi2DSlice =  new double[1][31]; 
 				//RefPCSensi2DSlice[0] =	jRefPCSensiEff[cnt];
 						
				
				//permute (x , [2;3;1]) 
				
 				//jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( permute(swapsRVDataEff,[2,1,3] )   )  );
				
				
				//double [][][] swapsRVDataEffReduced= buildRVDKFReduced (false, swapsRVDataEff,  numDateRows, int numAnnuality=2 //Biannual, int numYears=16); --//
				double [][][] swapsRVDataEffReduced= functUtil.buildRVDKFReduced (false, swapsRVDataEff,  numDateRows, 1, 16, 31); 
				oxl.genTxtFileFrom2DData(filepath, "swapsRVDataEffRed_Biannual.txt", swapsRVDataEffReduced[17]); 
				
				
				if (false) break flag1mod; 
				
				
				//jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEff )  );
				jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( swapsRVDataEffReduced )  );
				
 				jom.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" ); 				
 				
 				//jom.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
 				jom.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {1.11E+12} }) );
 				
 				jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(PCRP1) );
 				jom.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(jRefPCSensiEff) ); 
 				jom.setInputParameter("BestandOpt", new DoubleMatrixND( jBestandEff )  );
 				jom.setInputParameter("SwapConv", new DoubleMatrixND(jPCCov)  );
 				 				
 				
 				DoubleMatrixND  SwapsRV2DOptEff = jom.getInputParameter("SwapsRV2DOpt"); 				
 				
 								
 				// -- jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" ); -- //Works
				//jom.setObjectiveFunction("minimize",  "sum ( (  (RefPCSensiOpt .*  (( (decisionVars) * SwapsRV2DOpt) + BestandOpt ))  /ShuldVal)* 10000.0) "); //-- works
				
 				//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  )  ) "); //-- does not work
				//jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) * SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0) ");
				
 				
 				//-- Objective Function --//
 				jom.setObjectiveFunction("minimize",  "sum ( (  (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 )" );
								
 				
 				System.out.println ("**  Objective Function  ** : " + jom.getObjectiveFunction().getModel().toString() + " # Evaluate : " + jom.getObjectiveFunction().toString() );
				
				//-- solve--//				
				System.out.println ("### Solver optimizing :  "  );
				
								
			} //- done for 16 x 7 = 112 DV //
			
		
			
			functUtil.solveObjectiveFunction(jom, false);
			functUtil.printOptimalResult(jom);
						
			System.out.println ("Optimization Over!"); 
			
			
			/////////////////////////////////			
			final long endTime   = System.currentTimeMillis();
			double totalTime = (endTime - startTime)/ (60000.0);
			System.out.println(" Time taken (in minutes) to execute the optimization : " + totalTime);			
			//oxl.genTxtFileFrom2DData(initOptimAppConfig.outfiledir, "OptSolution.txt", dVarValStore);
			
				
			
		} // optimApp_optimize-method Over						
		///////////////////////////////////////////////////
		
		
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    