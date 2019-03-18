
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
import com.jom.Expression;
import com.jom.OptimizationProblem;
//import com.jom.SolverTester;


//--- Apache commons math--//
import org.apache.commons.math3.fitting.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.*;

//cern colt //
import cern.colt.matrix.tdouble.*;

import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.function.tdouble.*;
import cern.colt.function.*;
import cern.colt.matrix.*;
import cern.colt.matrix.tdouble.DoubleFactory2D;

import com.stfe.optim.config.initOptimAppConfig;
import com.stfe.optim.util.*;
import com.stfe.optim.excel.*;



    public class optimValidateFunctApp {
					
    	private String filepath;
    	private createOXLFromIBean oxl;
    	
    	private OptimizationProblem jom=null;
    	private optimSolveDataset dataSet=null;
		private optimSolveRuleConfig ruleConf=null;	
		private optimSolveFunctUtil functUtil=null;
		private optimSolveFunctApp appMain=null;
		
		
    	// --- Validation  ---//    	
    	//-- Stream out the values of core functions for validation - do not invoke Optimizer --//
    	// --- Validation SRV Data ---//
    	public void ojbfunc_optimize_rule_validateSRVData(optimSolveFunctApp appMainObj) 	{
    		
    		this.appMain = appMainObj;
    		this.jom=appMainObj.jom;
        	this.dataSet=appMainObj.dataSet;
    		this.ruleConf=appMainObj.ruleConf;	
    		this.functUtil=appMainObj.functUtil;
    		
    		//--Dataset --//
			if (this.dataSet == null) {
				this.dataSet= new optimSolveDataset(this.ruleConf);
			}
						
			//this.dataSet.optimLoadEffectiveSRVData();			
			if (this.appMain != null) this.appMain.objfunc_optimize_extractEffData();
			
    	}
    	
    	
    	// --- Validation calculated DKF Data ---//
    	public void ojbfunc_optimize_rule_validateCoreDKF(optimSolveFunctApp appMainObj) 	{
    		
    		this.appMain = appMainObj;
    		this.jom=appMainObj.jom;
        	this.dataSet=appMainObj.dataSet;
    		this.ruleConf=appMainObj.ruleConf;	
    		this.functUtil=appMainObj.functUtil;
    		
    		this.filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			this.oxl = new createOXLFromIBean();
    		
			/*
    		double [] initValDV70V = { 
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					0, 0, 0, 0, 0, 0,	0,
					2, 2, 2, 2, 2, 2,	2,
				};
    		*/
			
    		int numDV = this.ruleConf.numDecisionVariables;
    		//double [] initValDV7V = { 8.0, 8, 8, 8, 8, 8,	8 };
    		double [] initValDV7V = { 2.0, 2, 2, 2, 2, 2,	2 };
    		//double [] initValDV7V = { 0.0, 0, 0, 0, 0, 0,	0 };
			double[][] initDV = new double[1][numDV];
			initDV[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDV7V), (numDV / 7));
			this.jom.setInputParameter("initDVOpt", new DoubleMatrixND(initDV));
			String fnamePrefix="Validate-DKF-KeyParam-";
			String fnameSuffix=".txt";
			
			
			// Resets later all DV 0.0//- jom.setInitialSolution("decisionVars", new DoubleMatrixND(initDV));
			//jom.setInputParameter("decisionVals", new DoubleMatrixND(initDV));
			jom.setInputParameter("decisionVars", new DoubleMatrixND(initDV));
			jom.setInputParameter("decisionVarsBond", new DoubleMatrixND(initDV));
			jom.setInputParameter("decisionVarsSwap", new DoubleMatrixND(initDV));
			
			
			String swapRisk = ojbfunc_optimize_formSwapRiskExpValidate();
			double [][] swapRiskArr= (double[][]) jom.parseExpression(swapRisk).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, fnamePrefix + "swapRisk"+fnameSuffix, swapRiskArr, 10);	
			
			String swapFixing = ojbfunc_optimize_formSwapFixingExpValidate();
			double [][] swapFixingArr= (double[][]) jom.parseExpression(swapFixing).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, fnamePrefix+"swapFixing"+fnameSuffix, swapFixingArr,10);	
			
			String swapKosten= ojbfunc_optimize_formSwapMehrkostenExpValidate();
			double [][] swapKostenArr= (double[][]) jom.parseExpression(swapKosten).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, fnamePrefix+"swapKosten"+fnameSuffix, swapKostenArr,10);
			
			
			String bondSwapMehrKosten= ojbfunc_optimize_formBondSwapMehrkostenObjFunctExpValidate();
			double [][] bondSwapKostenArr= (double[][]) jom.parseExpression(bondSwapMehrKosten).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, fnamePrefix+"bondSwapMehrKosten"+fnameSuffix, bondSwapKostenArr,10);
			
			
			
    	}
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
    	//--- Add Objective Function -- Mehrkosten -- Bond+Swap --- //
		public String  ojbfunc_optimize_formBondSwapMehrkostenObjFunctExpValidate() {
			
			String PVTotalVec= " (   (PVCleanFullOpt) + (sum( (decisionVarsBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (decisionVarsSwap *  SwapsRV2DPVCleanOpt), 1))   ) ";		
			PVTotalVec = "(" + PVTotalVec + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalVec, "Mehrkosten-pvTotalVec + BondSwap");
			
			//-- build a matrix of PVTotalOptArr, where 1st-col of 120 rows are copied to 31 cols --// -- [ ((this.ruleConf.numYears) * (this.ruleConf.monthsInYear)), 31] --//
			String PVTotalMatrix =  PVTotalVec + " *  ones([1;31])" ;
			PVTotalMatrix = "(" + PVTotalMatrix + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalMatrix, "Mehrkosten-PVTotalMatrix");
			
			
			String PVTotalRefSensi =  PVTotalMatrix + " .* RefPCSensiOpt " ;
			PVTotalRefSensi = "(" + PVTotalRefSensi + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalRefSensi, "Mehrkosten-PVTotalRefSensi");
			
							
			
			//String SwapMehrKosten=   "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* (      (  sum( (decisionVars) *  SwapsRV2DOpt ,1)+ BestandOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";			
			
			String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( sum( (decisionVarsBond) *  BondsRV2DOpt ,1) + sum( (decisionVarsSwap) *  SwapsRV2DOpt ,1)  )  + BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
			
			
			BondSwapMehrKosten= "(" + BondSwapMehrKosten + ") * 10000.0d";
			ojbfunc_optimize_dispDVExpr(BondSwapMehrKosten, "Mehrkosten-BondSwapMehrKosten");
			
			return BondSwapMehrKosten;
		}
		
		
		
		
		//-- Mehrkosten - Swap --//
		private void  ojbfunc_optimize_setupJOMSwapKostenValidateValue() {
			String PVTotalVec= " ((PVCleanOpt) + (sum( (initDVOpt *  SwapsRV2DPVCleanOpt), 1)) ) ";			
			PVTotalVec= "("+PVTotalVec +")";	
			//ojbfunc_optimize_dispDVExpr(pvtotal, "fexpr-Mehrkosten");								
			this.jom.setInputParameter("PVTotalOpt", PVTotalVec);			
			this.appMain.ojbfunc_optimize_dispDVExpr("PVTotalOpt", "Mehrkosten-PVTotalOpt");
			double[][] PVTotalOptArr = (double[][]) jom.getInputParameter("PVTotalOpt").toArray(); 
			System.out.println(" PVTotalOptArr: Row-size: " + PVTotalOptArr.length + "; Col_size: " + PVTotalOptArr[0].length);
			
			//-- build a matrix of PVTotalOptArr using abstract vars, where 1st-col of 120 rows are copied to 31 cols --//
			String PVTotalMatrix =  PVTotalVec + " *  ones([1;31])" ;
			PVTotalMatrix = "(" + PVTotalMatrix + ")";
			this.jom.setInputParameter("PVTotalMatrixOpt", PVTotalMatrix);	
			this.appMain.ojbfunc_optimize_dispDVExpr(PVTotalMatrix, "Mehrkosten-PVTotalMatrixAbs");
			double[][] PVTotalMatrixOptArr = (double[][]) jom.getInputParameter("PVTotalMatrixOpt").toArray(); 
			oxl.genTxtFileFrom2DDataFmt(filepath, "PVTotalMatrixAbstractArr.txt", PVTotalMatrixOptArr, 10);
			System.out.println(" PVTotalMatrixOptArr: Row-size: " + PVTotalMatrixOptArr.length + "; Col_size: " + PVTotalMatrixOptArr[0].length);
			
			
			//-- build a matrix of PVTotalOptArr using data, where 1st-col of 120 rows are copied to 31 cols --//
			double[][] PVTotalDataMatrix=com.stfe.optim.util.optimSliceArray.join2Array2DColsTimes(PVTotalOptArr, PVTotalOptArr,30);
			this.appMain.objfunc_displayJOMVarStruct (null, PVTotalDataMatrix, 2);
			oxl.genTxtFileFrom2DDataFmt(filepath, "PVTotalDataMatrix.txt", PVTotalDataMatrix, 10);
			
			this.jom.setInputParameter("ExtPVTotalOpt", PVTotalDataMatrix );
			this.appMain.ojbfunc_optimize_dispDVExpr("ExtPVTotalOpt", "Mehrkosten-extPVTotal");
			
			// joBestandEff: Row_size: 120; col-size: 31 +  joRefPCSensiEff: size_ROW: 120; size_COL: 31
			
			//RefPCSensiOpt + BestandTotalPV		
			this.jom.setInputParameter("BestandTotalPV", "BestandOpt");			
			this.appMain.ojbfunc_optimize_dispDVExpr("BestandTotalPV", "Mehrkosten-BestandTotalPV");
			double [][] BestandTotalPVOptArr= (double[][]) jom.parseExpression("BestandTotalPV").evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-BestandTotalPVOptArr", BestandTotalPVOptArr,10);	
			
			double [][] RefPCSensiOptArr= (double[][]) jom.parseExpression("RefPCSensiOpt").evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-RefPCSensiOptArr", RefPCSensiOptArr,10);	
			
			
			//this.jom.setInputParameter("PVTotalRefSensi", " PVTotalOpt .* RefPCSensiOpt");
			this.jom.setInputParameter("PVTotalRefSensi", " ExtPVTotalOpt .* RefPCSensiOpt ");
			this.appMain.ojbfunc_optimize_dispDVExpr("PVTotalRefSensi", "Mehrkosten-PVTotalRefSensi");
			
			/*
			double [][] PVTotalRefSensiArr= (double[][]) jom.parseExpression("PVTotalRefSensi").evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-PVTotalRefSensiArr", PVTotalRefSensiArr,10);	
			
			
			String kostenDiff="(  (sum( (initDVOpt) *  SwapsRV2DOpt ,1)+ BestandTotalPV )  - (PVTotalRefSensi)  )" ;
			ojbfunc_optimize_dispDVExpr(kostenDiff, "Mehrkosten-kostenDiff");
			
			double [][] kostenDiffArr= (double[][]) jom.parseExpression(kostenDiff).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-kostenDiffArr", kostenDiffArr,10);	
			
			
			String diffTimesPCRP1= " (PCRP1Opt .* (      (sum( (initDVOpt) *  SwapsRV2DOpt ,1)+ BestandTotalPV )  - (PVTotalRefSensi)    ) )";
			ojbfunc_optimize_dispDVExpr(diffTimesPCRP1, "Mehrkosten-diffTimesPCRP1");
			
			double [][] diffTimesPCRP1Arr= (double[][]) jom.parseExpression(diffTimesPCRP1).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-diffTimesPCRP1Arr", diffTimesPCRP1Arr,10);	
			
			
			String diffTimesPCRP1Norm= "(  (1/PVTotalOpt) .* sum(   (PCRP1Opt .* (      (sum( (initDVOpt) *  SwapsRV2DOpt ,1)+ BestandTotalPV )  - (PVTotalRefSensi)    ) )    ,2) ) * 10000.0d ";
			ojbfunc_optimize_dispDVExpr(diffTimesPCRP1Norm, "Mehrkosten-diffTimesPCRP1Norm");
			
			
			//double [][] diffTimesPCRP1NormArr= (double[][]) jom.parseExpression(diffTimesPCRP1Norm).evaluate().toArray();
			//this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-diffTimesPCRP1NormArr", diffTimesPCRP1NormArr,10);	
			 
			 */
			
		}
		
		
				
		private String  ojbfunc_optimize_formSwapMehrkostenExpValidate() {
			
			boolean testOptimValidate = false;
			String SwapMehrKosten;
			
			if (testOptimValidate) {
				SwapMehrKosten = this.appMain.ojbfunc_optimize_formSwapMehrkostenObjFunctExp();
				
			} else {
			
				
				System.out.println(" **** VALIDATE - TM - SWAP MEHRKOSTEN **** ");
				
				
				//-- Replace BestandOpt with BestandFull  +   PVCleapOpt with PVCleanFullOpt --//
				String PVTotalVec= " ((PVCleanFullOpt) + (sum( (initDVOpt *  SwapsRV2DPVCleanOpt), 1)) ) ";		
				PVTotalVec = "(" + PVTotalVec + ")";
				//-- build a matrix of PVTotalOptArr, where 1st-col of 120 rows are copied to 31 cols --// -- [ ((this.ruleConf.numYears) * (this.ruleConf.monthsInYear)), 31] --//
				String PVTotalMatrix =  PVTotalVec + " *  ones([1;31])" ;
				PVTotalMatrix = "(" + PVTotalMatrix + ")";
				String PVTotalRefSensi =  PVTotalMatrix + " .* RefPCSensiOpt " ;
				PVTotalRefSensi = "(" + PVTotalRefSensi + ")";
				
				//SwapMehrKosten=   "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* (      (sum( (decisionVars) *  SwapsRV2DOpt ,1)+ BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
				SwapMehrKosten=   "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* (      (sum( (initDVOpt) *  SwapsRV2DOpt ,1)+ BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
				
				SwapMehrKosten= "(" + SwapMehrKosten + ") * 10000.0d";
				
				//--validate setup --//
				//ojbfunc_optimize_setupJOMSwapKostenValidate();
				//SwapMehrKosten= "(  (1/PVTotalOpt) .* sum(   (PCRP1Opt .* (      (sum( (initDVOpt) *  SwapsRV2DOpt ,1)+ BestandTotalPV )  - (PVTotalRefSensi)    ) )    ,2) ) * 10000.0d";
				//--setupDV-- SwapMehrKosten= "(  (1/PVTotalOpt) .* sum(   (PCRP1Opt .* (      (sum( (decisionVars) *  SwapsRV2DOpt ,1)+ BestandTotalPV )  - (PVTotalRefSensi)    ) )    ,2) ) * 10000.0d";
			
			}
			
			//double [][] swapKostenArr= (double[][]) jom.parseExpression(swapKosten).evaluate().toArray();
			//this.oxl.genTxtFileFrom2DDataFmt(filepath, "swapKosten"+fnameSuffix, swapKostenArr,10);
			return SwapMehrKosten;
		}
		
		
		
		
			
		private String  ojbfunc_optimize_formSwapRiskExpValidate() {	
						
			boolean testOptimValidate = false;
			String fexpr;
			
			this.appMain.ojbfunc_optimize_setupJOMSwapRisk();
			
			if (testOptimValidate) {
				fexpr = this.appMain.ojbfunc_optimize_formSwapRiskConstraintExp();
				fexpr= "("+fexpr +")'";
			} else {
			
				fexpr = " sum( (  (sum( (initDVOpt * SwapSensiRV2DOpt),1) + SwapSensiRiskFullOpt)  .*    (  (sum( (initDVOpt * SwapSensiRV2DOpt),1) + SwapSensiRiskFullOpt) * PCCov_Mkt1_10_10) ),2)";
				//fexpr = " sum( (  (sum( (decisionVars * SwapSensiRV2DOpt),1) + SwapSensiRiskFullOpt)  .*    (  (sum( (decisionVars * SwapSensiRV2DOpt),1) + SwapSensiRiskFullOpt) * PCCov_Mkt1_10_10) ),2)";			
				
				fexpr= "sqrt( ("+ fexpr + ") + 0.000000001d ) "; 
				fexpr= "(1/(SchuldFullOpt)) .* (" + fexpr +")";
				fexpr= "("+fexpr +") * 1000.0d ";					
			}
			
			double [][] SwapRiskExpArr= (double[][]) jom.parseExpression(fexpr).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Validate-SwapRiskExpArr.txt", SwapRiskExpArr,10);
			return fexpr;
		}
		
		
		
		
		private String  ojbfunc_optimize_formSwapFixingExpValidate() {	
			
			boolean testOptimValidate = false;
			String fexpr;
			this.appMain.ojbfunc_optimize_setupJOMSwapFxRisk();
			
			if (testOptimValidate) {
				fexpr = this.appMain.ojbfunc_optimize_formSwapRiskConstraintExp();
				fexpr= "("+fexpr +")'";
				
			} else {
				fexpr = " sum( (  (sum( (initDVOpt * SwapFxRV2DOpt),1) + SwapFxRiskFullOpt)  .*    (  (sum( (initDVOpt * SwapFxRV2DOpt),1) + SwapFxRiskFullOpt) * PCCov_Mkt1_1_31) ),2)";
				//fexpr = " sum( (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt)  .*    (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt) * PCCov_Mkt1_1_31) ),2)";
				
				fexpr= "sqrt( ("+ fexpr + ") + 0.00001d ) ";			
				fexpr= "(1/(SchuldFullOpt)) .* (" + fexpr +")";
				fexpr= "("+fexpr +") * 1000.0d ";
			}
					
			double [][] SwapFixRiskExpArr= (double[][]) jom.parseExpression(fexpr).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Validate-SwapFixRiskExpArr.txt", SwapFixRiskExpArr,10);
			return fexpr;
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	private void setup_rule_Proto_env(optimSolveFunctApp appMainObj) {
    		this.appMain = appMainObj;
			this.jom=appMainObj.jom;
			this.dataSet=appMainObj.dataSet;
    		this.ruleConf=appMainObj.ruleConf;	
    		this.functUtil=appMainObj.functUtil;
    		
			this.filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			this.jom = new OptimizationProblem();			
			this.functUtil= new optimSolveFunctUtil();
			
			this.filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			this.oxl = new createOXLFromIBean();
    	}
    	
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//-- test issues of non-linear optimization --//
    	private String ojbfunc_optimize_replicate_GenVecMatDim3D(String rowColVec, int VecLen, int rowColVecFlag, int OMatDimTab, int OMatDimRow, int OMatDimCol, boolean display){
    		String expr=""; 
			String fexpr="";
			int validDim=0;
			
    		// First dim of other matrix must match with row-col-vector. //
    		// If row of mat2 equals to vecDim, then proceed, else return; //
    		if (VecLen == OMatDimTab)  // --[3, 7, 3] --//
    		validDim=1;
    		else return fexpr;
    		
    		jom.setInputParameter("VecDim", VecLen );
			
			//-- replicate a rowVec along a square matrix : -- create a unit square matrix U; create a diagonal matrix D with rowVec along diagonal; matrix multiply D X U  --//
			String osMatrix = "ones(["+ String.valueOf(VecLen) + ";" + String.valueOf(VecLen) +"])" ; //--[3,3]--//
			
			String onesMatrixDim = "ones(["+ String.valueOf(OMatDimTab) + ";" + String.valueOf(OMatDimRow) + ";" + String.valueOf(OMatDimCol) +"])" ; //-- [3, 7, 3] --// 
			
			String diagMatrix = "diag("+ rowColVec + ")" ; //- 3, 3-//
			
			if (display) {
				double[][] onesArr = (double[][]) jom.parseExpression(osMatrix).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_OnesMat.txt", onesArr);
					
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, onesMatrixDim, "Replicate_VecSMat-Replicate_expr_OnesMat.txt");
				double[][][] onesMatrixDimArr = (double[][][]) jom.parseExpression(onesMatrixDim).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_onesMatrixDimArr-0-same-1.txt", onesMatrixDimArr[0]);
				
				double[][] diagArr = (double[][]) jom.parseExpression(diagMatrix).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_DiagMat.txt", diagArr);
			}
			
			if (rowColVecFlag==0) {
				//-- create a square matrix - transpose for colVec and normal for rowVec --// 
				expr = diagMatrix + " * " + osMatrix; // [3x3] * [3, 3] = [3, 3]  

				if (display) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_DiagMultOnes");
					double[][] diagMultOnesArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_DiagMultOnes.txt", diagMultOnesArr);
				}
				
				//-- create a rowcolvecMatrix of equal dim to mat2  --//
				if (OMatDimTab == VecLen)
					fexpr = expr + " * " + onesMatrixDim; // [3, 3] * [3, 7, 3] = [3, 7, 3]
						
			} 
			/*
			if (rowColVecFlag==1) {
				expr = diagMatrix  + " * " + osMatrix; 

				if (display) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_OnesMultDiag");
					double[][] onesMultDiagArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_OnesMultDiag.txt", onesMultDiagArr);
				}
				
				//-- create a rowcolvecMatrix of equal dim to mat2  --//
				if (OMatDimTab == VecLen)
					fexpr =  "("+ expr + ") * (" +  onesMatrixDim + ")";
				
				if (OMatDimCol == VecLen)
					fexpr = "("+ onesMatrixDim + ") * (" + expr + ")";
					
			}
			*/
			if (validDim==1) {
				fexpr = " 1/VecDim * ("+ fexpr + ")";
				if (display) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_DiagMultOnes-MultOnesMatrixDim");
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, onesMatrixDim, "Replicate_VecSMat-onesMatrixDim");
					
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, fexpr, "Replicate_VecSMat-exprDisp");
					double[][][] replicatedRowColVecArr = (double[][][]) jom.parseExpression(fexpr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_RowColVecF_0.txt", replicatedRowColVecArr[0]);
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_RowColVecF_1.txt", replicatedRowColVecArr[1]);
				}
			}
    		
    		return fexpr;
    	}
    	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    	private String ojbfunc_optimize_GenAltMatMult_VecOMat_3D(String vecOMat, String oMatrix, int rcVec, boolean display){
			//-- if rcVec is 0 its row vector else its column vector --//
			//-- vecSMAT is transpose of vector replicated along square-matrix ---//
			//-- Do dot product of VecSmat and oMatrix ; and take sum along col/row for rowVector/colVector. ---//
			String expr;
			
			expr = vecOMat;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMat");
			
			expr = oMatrix;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_oMatrix");
			
			expr = vecOMat + " .* " + oMatrix;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMatInOMatrix-exprDisp");
			
			if (display) {
				double[][][] dotVecmatOMatArr = (double[][][]) jom.parseExpression(expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMat_0.txt", dotVecmatOMatArr[0]);
				this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMat_1.txt", dotVecmatOMatArr[1]);
			}
			
			if ( (rcVec==0) || (rcVec==1) ) {
				expr = "sum ("  + expr + " , 1)"; // should be summed up along cols- 2//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMatInOMatrixSum-exprDispFinal");
			} 
			
			if (display) {
				double[][] dotVecmatOMatArrFinal = (double[][]) jom.parseExpression(expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMatFinal_0.txt", dotVecmatOMatArrFinal);
			}
			
			return expr;
		}
    	
    	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    	private void testAltGenMatMult3D() {
	
			double[][][] mat2 =  //-- [ 3, 7, 3]
				{
					{ // Bkst=(0, 7,3)
						{0,1,2},
						{1,5,7},
						{2,3,9},
						{3,6,8},
						{4,7,9},
						{5,4,7},
						{6,1,7}, 
					},	
					{ // Bkst=(1, 7,3)
						{0,1,1},
						{1,5,2},
						{2,4,4},
						{3,2,5},
						{4,3,6},
						{5,7,7},
						{6,6,8}, 
					}, 
					{ // Bkst=(2, 7,3)
						{0,2,9},
						{1,1,8},
						{2,3,7},
						{3,4,6},
						{4,5,5},
						{5,6,4},
						{6,7,3}, 
					}
				};
			double[][][] mat21 =  //-- [ 2, 2, 2]
				{
					{ // Bkst=(1, 2,3)
						{1,3},
						{2,4}
					},	
					{ // Bkst=(2, 2,3)
						{3,2},
						{4,4}
					} 
					
				};
				int[] Index = {0,1, 2, 3, 4, 5, 6};
				//double[][][] mat2=Bkst;
				
				double[][] rowVec= { {3, 2, 4} };
				double[][] colVec= { {3}, {2}, {4} };
				
				int rowColVec=0; // -- 0 is for rowVector; else colVector--//
				int validDim=0;
				boolean display=true;
				
				System.out.println(" Multipltaion of rowVector with other 3D-matrix: testAltGenMatMult3D begins! ");
				
				this.jom.setInputParameter("MatrixA", new DoubleMatrixND(mat2)  );
				jom.setInputParameter("RowVecMat", new DoubleMatrixND(rowVec)  );
				jom.setInputParameter("ColVecMat", new DoubleMatrixND(colVec)  );
				
				jom.setInputParameter("Test_JOM_MultMatAB", "RowVecMat * MatrixA");
				jom.setInputParameter("Test_JOM_MultMatAB", "sum(Test_JOM_MultMatAB, 1)"); 
				double[][] Test_Validate_MultMatABArr = (double[][]) jom.parseExpression("Test_JOM_MultMatAB").evaluate().toArray() ;
				this.oxl.genTxtFileFrom2DData(filepath, "Test_Validate_MultMatAB.txt", Test_Validate_MultMatABArr);
				
				System.out.println(" 3D oMat Dim: // mat2.length = " + mat2.length + " // mat2[0].length = " + mat2[0].length + " // rowVecLen = " + rowVec.length );
				
				// First dim of other matrix must match with row-col-vector. //
				if ( mat2.length == rowVec[0].length) validDim=1;
				
				if (validDim == 1) {
					String vecOMat;
					if (rowColVec == 0) {
						vecOMat = ojbfunc_optimize_replicate_GenVecMatDim3D("RowVecMat", rowVec[0].length, rowColVec , mat2.length, mat2[0].length, mat2[0][0].length, display );
					} else { 
						vecOMat = ojbfunc_optimize_replicate_GenVecMatDim3D("ColVecMat", rowVec[0].length, rowColVec , mat2.length, mat2[0].length, mat2[0][0].length, display );
					}
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecOMat, "Test-rowVecSMat-Disp");
					jom.setInputParameter("VecIntoOMAT", vecOMat  );
					double[][][] vecSMatArr = (double[][][]) jom.parseExpression(vecOMat).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Test-ReplVecOMatArr.txt", vecSMatArr[0]);
					
					//-- apply alternate method of matrix multiplication with dot-product and sum ---/// 
					//String vecMatMult =ojbfunc_optimize_GenAltMatMult_VecOMat_3D("VecIntoOMAT", "MatrixA", rowColVec, display );
					String vecMatMult =ojbfunc_optimize_GenAltMatMult_VecOMat_3D(vecOMat, "MatrixA", rowColVec, display );
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult, "Test-oMatMult-Disp");
					
					double[][] altVecMatMultArr = (double[][]) jom.parseExpression(vecMatMult).evaluate().toArray() ;
					this.oxl.genTxtFileFrom2DData(filepath, "Test-GenAltVecMatMultArr_F.txt", altVecMatMultArr);
					
	    		} //-- Multiply the rowColVec with Matrix --//
				else System.out.println(" Multipltaion of rowVector with other 3D-matrix is not possible! Dim mismatch ???");
				
				
				//-- Test Big Array -- Proto - used in Opti --//
				//-- Test Big Array -- Proto - used in Opti --//
				// Create a 3-D matrix --//
				double[][][] oMat3D = new double[70][120][79]; //[DV, TG, DKF]-- [2101][1010][1001] -- 
				double[][] oRowVec = new double[1][oMat3D.length];
				for (int i=0; i< oMat3D.length; i++) {
					oRowVec[0][i] = i + 1;
					for (int j=0; j< oMat3D[0].length; j++) {
						for (int k=0; k< oMat3D[0][0].length; k++) {
							oMat3D[i][j][k] = i+j+k +1;
						}
					}
				}
				//-- validate result with mat-mult JOM --//
				this.jom.setInputParameter("oMatrix3D", new DoubleMatrixND(oMat3D)  );
				this.jom.setInputParameter("oRowVec", new DoubleMatrixND(oRowVec)  );
				this.jom.setInputParameter("oMultVecMat", "sum( (oRowVec * oMatrix3D), 1)"  );
				double[][] Test_Validate_oMultVecMatArr = (double[][]) jom.parseExpression("oMultVecMat").evaluate().toArray() ;
				this.oxl.genTxtFileFrom2DData(filepath, "Test_Validate_oMultVecMatArr_F.txt", Test_Validate_oMultVecMatArr);
				this.jom.setInputParameter("oMultVecMat", 0 );
				
				//-- Test alt-mult --//
				rowColVec=0;
				String oVecOMat = ojbfunc_optimize_replicate_GenVecMatDim3D("oRowVec", oRowVec[0].length, rowColVec , oMat3D.length, oMat3D[0].length, oMat3D[0][0].length, display );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, oVecOMat, "###oTest-rowVecSMat-Disp");
				String oVecMatMult =ojbfunc_optimize_GenAltMatMult_VecOMat_3D(oVecOMat, "oMatrix3D", rowColVec, display );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, oVecMatMult, "###oTest-oMatMult-Disp");
				double[][] oAltVecMatMultArr = (double[][]) jom.parseExpression(oVecMatMult).evaluate().toArray() ;
				this.oxl.genTxtFileFrom2DData(filepath, "Test-oVecMatMultArr_F.txt", oAltVecMatMultArr);
				
				System.out.println(" Multipltaion of rowVector with other 3D-matrix: testAltGenMatMult3D Over!");
		}
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    	
    	
    	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    			
    	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    	    	
		private String ojbfunc_optimize_replicate_GenVecMatDim(String rowColVec, int VecLen, int rowColVecFlag, int OMatDimRow, int OMatDimCol, boolean display){
			
			String expr=""; 
			String fexpr="";
			int validDim=0;
			
			// First dim of other matrix must match with row-col-vector. //
			// If row of mat2 equals to vecDim, then proceed, else return; //
			if (VecLen == OMatDimRow)  
				validDim=1;
			else return fexpr;
						
			jom.setInputParameter("VecDim", VecLen );
			
			//-- replicate a rowVec along a square matrix : -- create a unit square matrix U; create a diagonal matrix D with rowVec along diagonal; matrix multiply D X U  --//
			String osMatrix = "ones(["+ String.valueOf(VecLen) + ";" + String.valueOf(VecLen) +"])" ;
			
			String onesMatrixDim = "ones(["+ String.valueOf(OMatDimRow) + ";" + String.valueOf(OMatDimCol) +"])" ;
			
			String diagMatrix = "diag("+ rowColVec + ")" ;
			
			if (display) {
				double[][] onesArr = (double[][]) jom.parseExpression(osMatrix).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_OnesMat.txt", onesArr);
					
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, onesMatrixDim, "Replicate_VecSMat-Replicate_expr_OnesMat.txt");
				double[][] onesMatrixDimArr = (double[][]) jom.parseExpression(onesMatrixDim).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_onesMatrixDimArr.txt", onesMatrixDimArr);
				
				double[][] diagArr = (double[][]) jom.parseExpression(diagMatrix).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_DiagMat.txt", diagArr);
			}
			
			if (rowColVecFlag==0) {
				//-- create a square matrix - transpose for colVec and normal for rowVec --// 
				expr = diagMatrix + " * " + osMatrix;

				if (display) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_DiagMultOnes");
					double[][] diagMultOnesArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_DiagMultOnes.txt", diagMultOnesArr);
				}
				
				//-- create a rowcolvecMatrix of equal dim to mat2  --//
				if (OMatDimRow == VecLen)
					fexpr = expr + " * " + onesMatrixDim;
				//else if (OMatDimCol == VecLen)
					//fexpr = onesMatrixDim + " * " + expr;
						
			} 
			if (rowColVecFlag==1) {
				expr = diagMatrix  + " * " + osMatrix;

				if (display) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_OnesMultDiag");
					double[][] onesMultDiagArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_OnesMultDiag.txt", onesMultDiagArr);
				}
				
				//-- create a rowcolvecMatrix of equal dim to mat2  --//
				if (OMatDimRow == VecLen)
					fexpr =  "("+ expr + ") * (" +  onesMatrixDim + ")";
				
				if (OMatDimCol == VecLen)
					fexpr = "("+ onesMatrixDim + ") * (" + expr + ")";
					
			}
			
			if (validDim==1) {
				fexpr = " 1/VecDim * ("+ fexpr + ")";
				if (display) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, fexpr, "Replicate_VecSMat-exprDisp");
					double[][] replicatedRowColVecArr = (double[][]) jom.parseExpression(fexpr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_RowColVecF.txt", replicatedRowColVecArr);
				}
			}
			return fexpr;
		}
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		private String ojbfunc_optimize_GenAltMatMult_VecOMat(String vecOMat, String oMatrix, int rcVec, boolean display){
			//-- if rcVec is 0 its row vector else its column vector --//
			//-- vecSMAT is transpose of vector replicated along square-matrix ---//
			//-- Do dot product of VecSmat and oMatrix ; and take sum along col/row for rowVector/colVector. ---//
			String expr;
			
			expr = vecOMat;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMat");
			
			expr = oMatrix;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_oMatrix");
			
			expr = vecOMat + " .* " + oMatrix;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMatInOMatrix-exprDisp");
			if (display) {
				double[][] dotVecmatOMatArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMat.txt", dotVecmatOMatArr);
			}
			
			if ( (rcVec==0) || (rcVec==1) ) {
				expr = "sum ("  + expr + " , 1)"; // should be summed up along cols- 2//
			} 
			
			//if (display) {
				double[][] dotVecmatOMatArrFinal = (double[][]) jom.parseExpression(expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMatFinal.txt", dotVecmatOMatArrFinal);
			//}
			return expr;
		}
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		private void testAltGenMatMult() {
    		//double[][] mat2= { {4,1,5}, {2,1,3}, {4,1,2}  };
    		//double[][] mat2= { {7,3}, {15,6}, {18,9}, {1,2} };
			double[][] mat2= { {1,2,1,2,1}, {2,1,2,2,2}, {2,1,2,2,1}, {2,1,2,2,1} }; // First dim of other matrix must match with row-col-vector. // 
    		//double[][] mat2= { {2,3}, {1,2}, {2,4} };
			
    		//double[][] rvmatB= { {5,5,5},  {4,4,4} };
			//double[][] cvmatB= { {5, 4}, {5,4}, {5,4} };
			double[][] rowVec= { {5,4,5,2} };
			double[][] colVec= { {5},{4}, {5}, {2} };
			
			int rowColVec=1; // -- 0 is for rowVector; else colVector--//
			int validDim=0;
			boolean display=true;
			
			
			this.jom.setInputParameter("MatrixA", new DoubleMatrixND(mat2)  );
			jom.setInputParameter("RowVecMat", new DoubleMatrixND(rowVec)  );
			jom.setInputParameter("ColVecMat", new DoubleMatrixND(colVec)  );
			
			// First dim of other matrix must match with row-col-vector. //
			if ( mat2.length == rowVec.length) validDim=1;
			
			if (validDim == 1) {
				String vecOMat;
				if (rowColVec == 0) {
					vecOMat = ojbfunc_optimize_replicate_GenVecMatDim("RowVecMat", rowVec[0].length, rowColVec , mat2.length, mat2[0].length, display );
				} else { 
					vecOMat = ojbfunc_optimize_replicate_GenVecMatDim("ColVecMat", rowVec[0].length, rowColVec , mat2.length, mat2[0].length, display );
				}
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecOMat, "Test-rowVecSMat-Disp");
				jom.setInputParameter("VecIntoOMAT", vecOMat  );
				double[][] vecSMatArr = (double[][]) jom.parseExpression(vecOMat).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Test-ReplVecOMatArr.txt", vecSMatArr);
				
				//-- apply alternate method of matrix multiplication with dot-product and sum ---/// 
				String vecMatMult =ojbfunc_optimize_GenAltMatMult_VecOMat("VecIntoOMAT", "MatrixA", rowColVec, display );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult, "Test-oMatMult-Disp");
				
				double[][] altVecMatMultArr = (double[][]) jom.parseExpression(vecMatMult).evaluate().toArray() ;
				this.oxl.genTxtFileFrom2DData(filepath, "Test-GenAltVecMatMultArr.txt", altVecMatMultArr);
    		} //-- Multiply the rowColVec with Matrix --//
			
		}
		
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		
		

		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		
		
    	private void testAltGenMatMult1_OLD() {
    		//double[][] mat2= { {4,1,5}, {2,1,3}, {4,1,2}  };
    		//double[][] mat2= { {7,3}, {15,6}, {18,9}, {1,2} };
    		
			double[][] mat2= { {1,2,1,2,1}, {2,1,2,2,2} };
    		//double[][] mat2= { {2,3}, {1,2}, {2,4} };
			
    		//double[][] rvmatB= { {5,5,5},  {4,4,4} };
			//double[][] cvmatB= { {5, 4}, {5,4}, {5,4} };
			double[][] rowVec= { {5,4} };
			double[][] colVec= { {5},{4} };
			
    		/*
    		double[][] rvmatB= { {2,2,2}, {3,3,3}, {4,4,4} };
			double[][] cvmatB= { {2,3,4}, {2,3,4}, {2,3,4} };
			double[][] rowVec= { {2,3,4} };
			double[][] colVec= { {2},{3},{4} };
			*/
			
			this.jom.setInputParameter("MatrixA", new DoubleMatrixND(mat2)  ); 
			this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixA-Arr.txt", mat2);
			
			int rowColVec=0; // -- 0 is for rowVector; else colVector--//
			int validDim=0;
			boolean display=true;
			
			//-- Check the dim validity of rowColVec and Matrix for matmult--//
			if (rowColVec == 0) {
				
				//jom.setInputParameter("MatrixB", new DoubleMatrixND(rvmatB)  );
				//this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixB-Arr.txt", rvmatB);
				
				jom.setInputParameter("RowVecMat", new DoubleMatrixND(rowVec)  );
				this.oxl.genTxtFileFrom2DData(filepath, "Test-RowVec-Arr.txt", rowVec);
				
				if (rowVec[0].length == mat2.length ) { 
					this.jom.setInputParameter("MatMultAB"," RowVecMat * MatrixA " );
					validDim=1;
				}
				else {
					validDim=0;
					System.out.println(" Multipltaion of rowVector with other matrix is not possible! Dim mismatch ???");
					//this.jom.setInputParameter("MatMultAB"," RowVecMat * MatrixA' " );
				}
			} //--- rowVector done --//
			
			if (rowColVec == 1) { 
				//jom.setInputParameter("MatrixB", new DoubleMatrixND(cvmatB)  );
				//this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixB-Arr.txt", cvmatB);
				
				jom.setInputParameter("ColVecMat", new DoubleMatrixND(colVec)  );
				this.oxl.genTxtFileFrom2DData(filepath, "Test-ColVec-Arr.txt", colVec);
				
			
				if (colVec.length == mat2.length ) {
					validDim=1;
					jom.setInputParameter("MatMultAB","  MatrixA' * ColVecMat " );
				}
				else {
					validDim=0;
					System.out.println(" Multipltaion of colVector with other matrix is not possible! Dim mismatch ???");
					//jom.setInputParameter("MatMultAB","  MatrixA * ColVecMat " );
				}
				
				if (validDim == 1){
					double[][] matMultArr = (double[][]) jom.getInputParameter("MatMultAB").toArray() ;
					this.oxl.genTxtFileFrom2DData(filepath, "Test-MatMultABArr.txt", matMultArr);
				}
			} //-- colVector done  --//
			
			
			//-- Multiply the rowColVec with Matrix --//
			if (validDim == 1){
				
				String vecOMat;
				if (rowColVec == 0) {
					vecOMat = ojbfunc_optimize_replicate_GenVecMatDim("RowVecMat", rowVec[0].length, rowColVec , mat2.length, mat2[0].length, display );
				} else { 
					vecOMat = ojbfunc_optimize_replicate_GenVecMatDim("ColVecMat", rowVec[0].length, rowColVec , mat2.length, mat2[0].length, display );
				}
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecOMat, "Test-rowVecSMat-Disp");
				jom.setInputParameter("VecIntoOMAT", vecOMat  );
				double[][] vecSMatArr = (double[][]) jom.parseExpression(vecOMat).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Test-ReplVecOMatArr.txt", vecSMatArr);
				
				
				//-- apply alternate method of matrix multiplication with dot-product and sum ---/// 
				String vecMatMult =ojbfunc_optimize_GenAltMatMult_VecOMat("VecIntoOMAT", "MatrixA", rowColVec, display );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult, "Test-oMatMult-Disp");
				
				double[][] altVecMatMultArr = (double[][]) jom.parseExpression(vecMatMult).evaluate().toArray() ;
				this.oxl.genTxtFileFrom2DData(filepath, "Test-GenAltVecMatMultArr.txt", altVecMatMultArr);
    		} //-- Multiply the rowColVec with Matrix --//
			
    	}
    	
    	
    	
		public void ojbfunc_optimize_rule_Proto_UtilTest(optimSolveFunctApp appMainObj) {
			setup_rule_Proto_env(appMainObj);
			
			//-- old --//
			//testAltMatMult_Old();
			//testAltMatMult_Old3D();
			
			//-- good --//
			//testAltGenMatMult();
			testAltGenMatMult3D();
			
			// ---Slicing --
			//String sliceEBDVBondDelta2 = ojbfunc_optimize_SliceDVMatRowCol(EBDVBondDelta2, 1, 1, 51, 5 );
			
			//--good--//
			//testSliceRowColDV();
			
			//--good -//
			//testDeltaBaukasten();
		}
		
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//String sliceEBDVBondDelta2 = ojbfunc_optimize_SliceDVMatRowCol(EBDVBondDelta2, 1, 1, 51, 5 ); 

		private String ojbfunc_optimize_SliceDVMatRowCol(String DVMat, int selectFromDVRow, int selectFromDVCol, int multDimRow, int multDimCol ) {
						String retMat="";
						if ( (multDimRow < 1) || (multDimCol < 1) )
							return retMat;
						
						//if ( (selectFromDVRow > multDimRow) || (selectFromDVCol > multDimCol) ) return retMat;
						
						double[][] retOnesMat = new double[multDimRow][multDimCol]; 
						for (int r=0; r<multDimRow; r++) {
							for (int c=0; c<multDimCol; c++) {
								retOnesMat[r][c] = 0;
								if ( ((r+1) >= selectFromDVRow) && ((c+1) >= selectFromDVCol)  ) {
									if (r == c) 
										retOnesMat[r][c] = 1;
								}
							}
						}
						jom.setInputParameter("retOnesMatrix", retOnesMat);
						//retMat = "decisionVars  * retOnesMatrix";
						retMat =  DVMat + " * (retOnesMatrix)";
						this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, retMat, "retMat-exprDisp");
						double[][] retMatDVSliceArr = (double[][]) jom.parseExpression(retMat).evaluate().toArray();
						this.oxl.genTxtFileFrom2DData(filepath, "retMatDVSliceArr.txt", retMatDVSliceArr);
						return retMat;
					}
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private void testDeltaBaukasten() {
			// Baukasten=(3,3); Delta=(3,4); TXKey=(3,2);
			
			
			double[][][] Bkst = 
			{
				{ // Bkst=(1, 7,3)
					{0,1,2},
					{1,5,7},
					{2,11,9},
					{3,6,8},
					{4,7,9},
					{5,6,7},
					{6,6,7}, 
				},	
				{ // Bkst=(2, 7,3)
					{0,1,1},
					{1,5,2},
					{2,11,4},
					{3,6,5},
					{4,7,6},
					{5,6,7},
					{6,6,8}, 
				} 
			};
			int[] Index = {0,1, 2, 3, 4, 5, 6};
			
			double [][][] BkstCont1= new double[Bkst.length][Bkst[0].length][Bkst[0][0].length]; // i,j,k
			double [][][] BkstCont2= new double[Bkst[0][0].length][Bkst[0].length][Bkst.length]; // k,j,i
			double [][][][] SumBkstCont= new double[Bkst[0][0].length][Bkst[0].length][Bkst.length][Index.length]; // k, j, i, idx
			
			for (int idx=0; idx<Index.length; idx++) {
				
			for (int j=0; j<Bkst[0].length; j++) {
			
			for (int k=0; k<Bkst[0][0].length; k++) { 
				
			for (int i=0; i<Bkst.length; i++) {
				
				//for (int j=0; j<Bkst[0].length; j++) {
				//	for (int k=0; k<Bkst[0][0].length; k++) {
						BkstCont1[i][j][k] = Bkst[i][j][k];
						BkstCont2[k][j][i] = Bkst[i][j][k];
						
						//for (int idx=0; idx<Index.length; idx++) {
							SumBkstCont[k][j][i][idx] += (Bkst[i][j][k] + Index[idx] ) ;
						} //-- sum over the loop --//
						
					}
				}
			}
			System.out.println(" Selecting Baukasten in container 1 ...");
			disp2DD(BkstCont1[0]);
			disp2DD(BkstCont1[1]);
			System.out.println(" Selecting Baukasten in container 2 ...");
			disp2DD(BkstCont2[0]);
			disp2DD(BkstCont2[1]);
			System.out.println(" Summing and collecting in Baukasten container  ...");
			disp2DD(SumBkstCont[0][0]);
			disp2DD(SumBkstCont[0][1]);
			disp2DD(SumBkstCont[1][0]);
			disp2DD(SumBkstCont[1][1]);
			
			System.out.println(" Bkst Container --- Over & All !");
			
			//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
			double[][][] SelectBkst = new double[Bkst.length][Index.length][Bkst[0][0].length];
			for (int i=0; i<Bkst.length; i++) {
				
				//--CHANGE__//
				for (int j=0, idx=0; j<Bkst[0].length; j++) {
				//--NO--NO--for (int j=0, idx=0; j < Index.length; j++) {
					
    				//--copy the data only if stratom is active --//
    				
    				// -- match with index of active stratoms --//
					
					
					//--CHANGE__//
					//int indexArr = Arrays.asList(Index).indexOf(j);
					int indexArr = com.stfe.optim.util.optimArrayListUtil.getArrayIndexI(Index, j);
					System.out.println(" #### Found indexArray conatining j " + j + " at indexPos-Arr: " + indexArr);
					
					//--CHANGE__//
    				if (indexArr > -1) {
    					
    					System.out.println(" J= "+ j + " idx=" + idx + " indexArr= " + indexArr + " Index[indexArr]=" + Index[indexArr]  + " Index[idx]=" + Index[idx]);
    					if (indexArr == idx ) System.out.println(" Correct idx at right position!");
    					else System.out.println(" ??? Incorrect idx. Not found at right position!");
    					
    					System.arraycopy(Bkst[i][j], 0, SelectBkst[i][idx++], 0, Bkst[0][0].length);
    					
    					//this.bondsRVDataRawEffDV3D[i][idx++]  = this.bondsRVDataRaw3D[i][j];
    				} else if (i<2) System.out.println("  ??? ***** ???? Selected 3D-Restructured BondBaukasten not created! Date-point " + i);
    				
    			}
    		}
			System.out.println(" Selecting Baukasten with active stratoms! ...");
			disp2DD(SelectBkst[0]);
			disp2DD(SelectBkst[1]);
			System.out.println(" Selected Baukasten with active stratoms! Done & Over.");
			
			double[][] Baukasten = { // Baukasten=(3,3)
									{1,5,7},
									{2,11,9},
									{3,6,8}
								};
			
			double[][] Delta = { // Delta=(3,4)
									
									//{1,4,5,2},
									//{2,3,3,4},
									//{3,1,5,4}
									
									{0,0,1,0},
									{1,0,0,0},
									{0,1,0,0}
								};
			
			
			
			double[][] Tx = { // TXKey=(2,3)
								//{0,2,3},
								//{2,3,1}
								{1,0,0},
								{0,1,0},
								{0,0,1}
							} ;   
				
			double[][] TxPrime= { // TXKey=(3,3)
					{1,0,0},
					{1,1,0},
					{0,0,1}
				}; 
			double[][] TxPrimeInv= { // TXKey=(3,3)
					{1,0,0},
					{-1,1,0},
					{0,0,1}
				} ; 
			
			//--find inverse  of given matrix--//
			//double[][] TxPrimeInverse = org.apache.commons.math3.linear.MatrixUtils.inverse(TxPrime, double 0.00001d); //ininverse (TxPrime);
			//RealMatrix pInverse = MatrixUtils.getInverse();
			RealMatrix m = MatrixUtils.createRealMatrix(TxPrime);
			//RealMatrix pInverse =  MatrixUtils.blockInverse(m, 0);
			DecompositionSolver LUP = new  LUDecomposition(m).getSolver();
			RealMatrix pInverse = LUP.getInverse();
			//RealMatrix pInverse = new LUDecomposition(m).getSolver().getInverse();
			double[][] invMat = pInverse.getData();
			disp2DD(invMat);

			
			//-- validate the size--//
			if ( Baukasten.length != Delta.length ) 
				System.out.println("???  Matrices inconsistent! Baukasten.length != Delta.length");
			else System.out.println(" Matrices are consistent! Baukasten.length = Delta.length");
			if ( Baukasten.length != Tx[0].length ) 
				System.out.println("???  Matrices inconsistent! Baukasten.length != Tx[0].length");
			
				/*
				{		// new double[3][3];
							{ // Baukasten=(3,3)
									{1,2,3,},
									{2,3,4},
									{3,2,3}
								}
							};
				 */
								
			//-- associate the list lined with first-dim key --//
			Boolean ignoreFirstColDelta = false;
			int mixlen=0;
			if (ignoreFirstColDelta)
				mixlen=Baukasten[0].length + (Delta[0].length) -1;
			else
				mixlen=Baukasten[0].length + (Delta[0].length) ;
			
			double[][] DeltaBaukasten = new double[Baukasten.length][mixlen ]; 
			
			for (int i=0; i<Baukasten.length; i++) {
				
				for (int j=0, deltaidx=0; j<mixlen; j++) {
					
					if (j<(Baukasten[0].length))
						DeltaBaukasten[i][j] = Baukasten[i][j];
					else {
						if (ignoreFirstColDelta)
							if (j == Baukasten[0].length) deltaidx++; // Ignore the first col of Delta ; Include only from second row; //
							
						DeltaBaukasten[i][j] = Delta[i][deltaidx++];
					}
					
					///DeltaBaukasten[]
				}
			}
			//display the DeltaBaukasten//
			System.out.println("******* Delta-Baukasten Manual Matrix *********");
			disp2DD(DeltaBaukasten);
			
			this.jom.setInputParameter("BKst", new DoubleMatrixND(Baukasten)  );
			this.jom.setInputParameter("Delta", new DoubleMatrixND(Delta)  );
			this.jom.setInputParameter("DeltaBKst", new DoubleMatrixND(DeltaBaukasten)  );
			this.jom.setInputParameter("Tx", new DoubleMatrixND(Tx)  );
			this.jom.setInputParameter("TxPrime", new DoubleMatrixND(TxPrime)  );
			this.jom.setInputParameter("TxPrimeInv", new DoubleMatrixND(TxPrimeInv)  );
			
			this.jom.setInputParameter("BKstTx", " Tx * BKst "  );
			double[][] BKstTxArr = (double[][]) jom.parseExpression("BKstTx").evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Test-BKstTxArr.txt", BKstTxArr);
			
			this.jom.setInputParameter("DeltaTx", " Tx * Delta "  );
			double[][] DeltaTxArr = (double[][]) jom.parseExpression("DeltaTx").evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Test-DeltaTxArr.txt", DeltaTxArr);
			
			this.jom.setInputParameter("DeltaBKstTx", " Tx * DeltaBKst "  );
			double[][] DeltaBKstTxArr = (double[][]) jom.parseExpression("DeltaBKstTx").evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Test-DeltaBKstTxArr.txt", DeltaBKstTxArr);
			
			this.jom.setInputParameter("ReducedTxDeltaTxBKst", " DeltaTx' * BKstTx "  );
			double[][] ReducedTxDeltaTxBKstArr = (double[][]) jom.parseExpression("ReducedTxDeltaTxBKst").evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Test-ReducedTxDeltaTxBKstArr.txt", ReducedTxDeltaTxBKstArr);
			disp2DD(ReducedTxDeltaTxBKstArr);
			
			this.jom.setInputParameter("DeltaBKst", "  Delta' * BKst  "  );
			double[][] DeltaBKstArr = (double[][]) jom.parseExpression("DeltaBKst").evaluate().toArray();
			System.out.println("******* Delta * Baukasten : Matrix Multilication *********");
			disp2DD(DeltaBKstArr);
			
			this.jom.setInputParameter("DeltaBKstTxPrimeReduce", "  DeltaBKst * TxPrime"  );
			double[][] DeltaBKstTxPrimeReduceArr = (double[][]) jom.parseExpression("DeltaBKstTxPrimeReduce").evaluate().toArray();
			//disp2DD(DeltaBKstTxPrimeReduceArr);
			
			this.jom.setInputParameter("DeltaBKstTxPrimeInvReduce", " DeltaBKstTxPrimeReduce * TxPrimeInv "  );
			double[][] DeltaBKstTxPrimeInvReduceArr = (double[][]) jom.parseExpression("DeltaBKstTxPrimeInvReduce").evaluate().toArray();
			disp2DD(DeltaBKstTxPrimeInvReduceArr);
			
			this.jom.setInputParameter("IdentityMatrix", " TxPrime * TxPrimeInv"  );
			double[][] IdentityMatrixArr = (double[][]) jom.parseExpression("IdentityMatrix").evaluate().toArray();
			//disp2DD(IdentityMatrixArr);
		
			//-- Select the right matrix based on keys --//
			
		}
				
		private void disp2DD(double[][] data){
			for (int i=0; i<data.length; i++) {
				System.out.println(""); System.out.print("    ");
				for (int j=0; j<data[0].length; j++) {
					System.out.print("   " + data[i][j] + "   ");
				}
			}
			System.out.println("");
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//-- test issues of non-linear optimization --//
		private String sliceDVMatRowCol(String DVMat, int selectFromDVRow, int selectFromDVCol, int multDimRow, int multDimCol ) {
			String retMat="";
			if ( (multDimRow < 1) || (multDimCol < 1) )
				return retMat;
			
			//if ( (selectFromDVRow > multDimRow) || (selectFromDVCol > multDimCol) ) return retMat;
			
			double[][] retOnesMat = new double[multDimRow][multDimCol]; 
			for (int r=0; r<multDimRow; r++) {
				for (int c=0; c<multDimCol; c++) {
					retOnesMat[r][c] = 0;
					if ( ((r+1) >= selectFromDVRow) && ((c+1) >= selectFromDVCol)  ) {
						if (r == c) 
							retOnesMat[r][c] = 1;
					}
				}
			}
			jom.setInputParameter("retOnesMatrix", retOnesMat);
			//retMat = "decisionVars  * retOnesMatrix";
			retMat =  DVMat + " * (retOnesMatrix)";
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, retMat, "retMat-exprDisp");
			double[][] retMatDVSliceArr = (double[][]) jom.parseExpression(retMat).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "retMatDVSliceArr.txt", retMatDVSliceArr);
			return retMat;
		}
		
		
		private void testSliceRowColDV( ) {
			int numDV=49;
			jom.setInputParameter("numDV", numDV);
			jom.addDecisionVariable("decisionVars", false, new int[] { 1, numDV } );
			
			double [] initValDV7V = { 2.0, 2, 2, 2, 2, 2,	2 };
    		//double [] initValDV7V = { 0.0, 0, 0, 0, 0, 0,	0 };
			double[][] initDV = new double[1][numDV];
			initDV[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDV7V), (numDV / 7));
			//this.jom.setInputParameter("initDVOpt", new DoubleMatrixND(initDV));
			
			// Resets later all DV 0.0//- 
			jom.setInitialSolution("decisionVars", new DoubleMatrixND(initDV));
			this.jom.setInputParameter("decisionVars", new DoubleMatrixND(initDV));
			
			//make a slice of of dim (1,5) from (1,51)
			sliceDVMatRowCol("decisionVars", 1, 4, numDV, 5 );
		}
				
		/////////////////////////////////////////////////////////////////////	
		private String ojbfunc_optimize_AltMatMult_VecSMat(String vecSMat, String oMatrix, int rcVec){
			//-- if rcVec is 0 its row vector else its column vector --//
			//-- vecSMAT is transpose of vector replicated along square-matrix ---//
			//-- Do dot product of VecSmat and oMatrix ; and take sum along col/row for rowVector/colVector. ---//
			String expr;
			
			expr = vecSMat;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMat");
			
			expr = oMatrix;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_oMatrix");
			
			expr = vecSMat + " .* " + oMatrix;
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMatInOMatrix-exprDisp");
			double[][] dotVecmatOMatArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMat.txt", dotVecmatOMatArr);
			
			
			if (rcVec==0) {
				expr = "sum ("  + expr + " , 1)"; // should be summed up along cols- 2//
			} else expr = "sum ("  + expr + " , 2)";
			
			double[][] dotVecmatOMatArrFinal = (double[][]) jom.parseExpression(expr).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMatFinal.txt", dotVecmatOMatArrFinal);
			
			return expr;
		}
		
		
		//-- replicate a rowVec along a square matrix : -- create a unit square matrix U; create a diagonal matrix D with rowVec along diagonal; matrix multiply D X U  --//
		// -- vecSMat = ojbfunc_optimize_replicate_VecSMat("ColVecMat", rowVec[0].length=3, rowColVec=1 );--//
		private String ojbfunc_optimize_replicate_VecSMat(String rowColVec, int VecLen, int rcVec){
			String expr;
			//-- replicate a rowVec along a square matrix : -- create a unit square matrix U; create a diagonal matrix D with rowVec along diagonal; matrix multiply D X U  --//
			String osMatrix = "ones(["+ String.valueOf(VecLen) + ";" + String.valueOf(VecLen) +"])" ;
			double[][] onesArr = (double[][]) jom.parseExpression(osMatrix).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_OnesMat.txt", onesArr);
			
			String diagMatrix = "diag("+ rowColVec + ")" ;
			double[][] diagArr = (double[][]) jom.parseExpression(diagMatrix).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Replicate_expr_DiagMat.txt", diagArr);
			
			if (rcVec==0) {
				expr = diagMatrix + " * " + osMatrix;
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_DiagMultOnes");
				double[][] diagMultOnesArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_DiagMultOnes.txt", diagMultOnesArr);
				
			} else {
				expr = osMatrix + " * " + diagMatrix;
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_OnesMultDiag");
				double[][] onesMultDiagArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_OnesMultDiag.txt", onesMultDiagArr);
			}
			
			expr = "("+expr + ")";
			 
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-exprDisp");
			return expr;
		}
		
		
		private void testAltMatMult_Old() {
			//double[][] matA= { {7,3,5}, {15,6,17}, {18,9,23} };
			double[][] matA= { {6,3,5}, {5,6,7} };
			
			/////
			double[][] rvmatB= { {2,2,2}, {3,3,3}, {4,4,4} };
			double[][] cvmatB= { {2,3,4}, {2,3,4}, {2,3,4} };
			double[][] rowVec= { {2,3,4} };
			double[][] colVec= { {2},{3},{4} };
			
			int rowColVec=0; // -- 0 is for rowVector; else colVector--//
			
			this.jom.setInputParameter("MatrixA", new DoubleMatrixND(matA)  ); 
			this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixA-Arr.txt", matA);
			
			if (rowColVec == 0) { // Row Vector//
				jom.setInputParameter("MatrixB", new DoubleMatrixND(rvmatB)  );
				jom.setInputParameter("RowVecMat", new DoubleMatrixND(rowVec)  );
				this.oxl.genTxtFileFrom2DData(filepath, "Test-RowVec-Arr.txt", rowVec);
				this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixB-Arr.txt", rvmatB);
				this.jom.setInputParameter("MatMultAB"," RowVecMat * MatrixA " );
			}
			if (rowColVec == 1) { // Col Vector//
				jom.setInputParameter("MatrixB", new DoubleMatrixND(cvmatB)  );
				jom.setInputParameter("ColVecMat", new DoubleMatrixND(colVec)  );
				this.oxl.genTxtFileFrom2DData(filepath, "Test-ColVec-Arr.txt", colVec);
				this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixB-Arr.txt", cvmatB);
				jom.setInputParameter("MatMultAB","  MatrixA * ColVecMat ");
			}
			
			double[][] matMultArr = (double[][]) jom.getInputParameter("MatMultAB").toArray() ;
			this.oxl.genTxtFileFrom2DData(filepath, "Test-MatMultABArr.txt", matMultArr);
			
			
			String vecSMat;
			if (rowColVec == 0) {
				vecSMat = ojbfunc_optimize_replicate_VecSMat("RowVecMat", rowVec[0].length, rowColVec );
			} else { 
				vecSMat = ojbfunc_optimize_replicate_VecSMat("ColVecMat", rowVec[0].length, rowColVec );
			}
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecSMat, "Test-rowVecSMat-Disp");
			jom.setInputParameter("VecIntoSMAT", vecSMat  );
			double[][] vecSMatArr = (double[][]) jom.parseExpression(vecSMat).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Test-vecSMatArr.txt", vecSMatArr);
			
			
			//-- apply alternate method of matrix multiplication with dot-product and sum ---/// 
			String vecMatMult =ojbfunc_optimize_AltMatMult_VecSMat("VecIntoSMAT", "MatrixA", rowColVec );
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult, "Test-oMatMult-Disp");
			
			double[][] altVecMatMultArr = (double[][]) jom.parseExpression(vecMatMult).evaluate().toArray() ;
			this.oxl.genTxtFileFrom2DData(filepath, "Test-AltVecMatMultArr.txt", altVecMatMultArr);
			/* */
			
		}
		/////////////////////////////////////////////////////////////////////
		
		private void testAltMatMult_Old3D() {
			//double[][] matA= { {7,3,5}, {15,6,17}, {18,9,23} };
			double[][] matA= { {6,3,5}, {5,6,7} };
			
			/////
			double[][] rvmatB= { {2,2,2}, {3,3,3}, {4,4,4} };
			double[][] cvmatB= { {2,3,4}, {2,3,4}, {2,3,4} };
			double[][] rowVec= { {2,3,4} };
			double[][] colVec= { {2},{3},{4} };
			
			int rowColVec=1; // -- 0 is for rowVector; else colVector--//
			
			this.jom.setInputParameter("MatrixA", new DoubleMatrixND(matA)  ); 
			this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixA-Arr.txt", matA);
			
			if (rowColVec == 0) { // -- RowVec --//
				jom.setInputParameter("MatrixB", new DoubleMatrixND(rvmatB)  );
				jom.setInputParameter("RowVecMat", new DoubleMatrixND(rowVec)  );
				this.oxl.genTxtFileFrom2DData(filepath, "Test-RowVec-Arr.txt", rowVec);
				this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixB-Arr.txt", rvmatB);
				this.jom.setInputParameter("MatMultAB"," RowVecMat * MatrixA " );
			}
			if (rowColVec == 1) { // -- ColVec --// 
				jom.setInputParameter("MatrixB", new DoubleMatrixND(cvmatB)  );
				jom.setInputParameter("ColVecMat", new DoubleMatrixND(colVec)  );
				this.oxl.genTxtFileFrom2DData(filepath, "Test-ColVec-Arr.txt", colVec);
				this.oxl.genTxtFileFrom2DData(filepath, "Test-MatrixB-Arr.txt", cvmatB);
				jom.setInputParameter("MatMultAB","  MatrixA * ColVecMat " );
			}
			
			double[][] matMultArr = (double[][]) jom.getInputParameter("MatMultAB").toArray() ;
			this.oxl.genTxtFileFrom2DData(filepath, "Test-MatMultABArr.txt", matMultArr);
			
			
			String vecSMat;
			if (rowColVec == 0) {
				vecSMat = ojbfunc_optimize_replicate_VecSMat("RowVecMat", rowVec[0].length, rowColVec );
			} else { 
				vecSMat = ojbfunc_optimize_replicate_VecSMat("ColVecMat", rowVec[0].length, rowColVec );
			}
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecSMat, "Test-rowVecSMat-Disp");
			jom.setInputParameter("VecIntoSMAT", vecSMat  );
			double[][] vecSMatArr = (double[][]) jom.parseExpression(vecSMat).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "Test-vecSMatArr.txt", vecSMatArr);
			
			//-- apply alternate method of matrix multiplication with dot-product and sum ---/// 
			String vecMatMult =ojbfunc_optimize_AltMatMult_VecSMat("VecIntoSMAT", "MatrixA", rowColVec );
			this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult, "Test-oMatMult-Disp");
			
			double[][] altVecMatMultArr = (double[][]) jom.parseExpression(vecMatMult).evaluate().toArray() ;
			this.oxl.genTxtFileFrom2DData(filepath, "Test-AltVecMatMultArr.txt", altVecMatMultArr);
			
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//--- Prototypes, Troubleshooting, Test & Validation ---//
    	//--- Prototypes, Troubleshooting, Test & Validation ---//

		//-- test issues of non-linear optimization --//
		public void ojbfunc_optimize_rule_Proto_Test(optimSolveFunctApp appMainObj, int numYears, boolean addDKF, double addDKFForYears ) {
			
			this.appMain = appMainObj;
			this.jom=appMainObj.jom;
        	this.dataSet=appMainObj.dataSet;
    		this.ruleConf=appMainObj.ruleConf;	
    		this.functUtil=appMainObj.functUtil;
    		
			this.filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			this.jom = new OptimizationProblem();			
			this.functUtil= new optimSolveFunctUtil();
			
			this.filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			this.oxl = new createOXLFromIBean();
    		
			
			int numDV=3;
			jom.setInputParameter("numDV", numDV);
			jom.addDecisionVariable("decisionVars", false, new int[] { 1, numDV } );			
			
			System.out.println(" Proto-JOM-Env ... Standalone!");
			
			int numCV=4;
			double[][] covar= {  
					{1.0d,2,3,4},
					{2.0d,2,3,4}, 
					{3.0d,3,3,4}, 
					{4.0d,4,4,4}					
			};
			// sensi DIM [numDV, 6, numCV ]
			double[][][] sensi = new double[numDV][6][4];
			for (int nDV=0; nDV<numDV; nDV++) {
				for (int i=0; i<6; i++) {
					sensi[nDV][i] = covar[0]; 
				}
			}
			double DVval=1.2d;
			double [][] DV= { { DVval, DVval, DVval}, { DVval, DVval, DVval}, { DVval, DVval, DVval}  };
			jom.setInputParameter("DVOP", new DoubleMatrixND(DV));
			jom.setInputParameter("covarOP", new DoubleMatrixND(covar)); 
			jom.setInputParameter("sensiOP", new DoubleMatrixND(sensi));
			
			
			// Original Expression//
			//" ((sum( (decisionVars) * SwapSensiRV2DOpt , 1)  ) * PCCov_Mkt1_10_10) * (sum(SwapSensiRV2DOpt ,1))'   "
			//String oExpr = " (sum( (DVOP * sensiOP),1) * covarOP) * (sum( (DVOP * sensiOP),1))' ";
			
			String oExpr = " (sum( (DVOP * sensiOP),1) * covarOP) * (sum( (DVOP * sensiOP),1))'";
			
			oExpr =  "( " + oExpr + " ) .* (eye(6))" ;	
			
			double [][] DVNLConstrArr= (double[][]) jom.parseExpression(oExpr.toString()).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "DVNLConstrArr.txt", DVNLConstrArr);			
			ojbfunc_optimize_dispDVExpr(oExpr, "Original-JOM-Constraint-Non-Linear");					
			
			
			String gExpr = " sum( (  (sum( (DVOP * sensiOP),1))  .*    (sum( (DVOP * sensiOP),1) * covarOP) ),2)";
			double [][] gDVNLConstrArr= (double[][]) jom.parseExpression(gExpr.toString()).evaluate().toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "gDVNLConstrArr.txt", gDVNLConstrArr);			
			ojbfunc_optimize_dispDVExpr(gExpr, "Gerrit-JOM-Constraint-Non-Linear");
			
			/*
			// Original Constraint//
			String csExpr = " (sum( (decisionVars * sensiOP),1) * covarOP) * (sum( (decisionVars * sensiOP),1))' "; 
			//com.jom.Expression jexpr = jom.parseExpression(csExpr);			
			ojbfunc_optimize_dispDVExpr(csExpr, "Proto-JOM-Constraint-Non-Linear");
			System.out.println(" Proto-JOM-Constraint-Non-Linear:  "+ jom.parseExpression(csExpr).evaluate());			
			//jom.addConstraint(csExpr + " <= 12");	
			*/
			
			// GT Constraint//
			//String fexpr = " sum( (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt)  .*    (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt) * PCCov_Mkt1_1_31) ),2)";
			
			//String gCTExpr = " sum( (  (sum( (DVOP * sensiOP),1))  .*    (sum( (DVOP * sensiOP),1) * covarOP) ),2)";
			
			
			//-- Works Well--//
			String gCTExpr = " sum( (  (sum( (decisionVars * sensiOP),1))  .*    (sum( (decisionVars * sensiOP),1) * covarOP) ),2)"; //--Good--//
			
			
			//String gCTExpr = " (sum( (decisionVars * sensiOP),1) * covarOP) * (sum( (decisionVars * sensiOP),1))' ";
			//String gCTExpr = " (sum( (decisionVars * sensiOP),1) * covarOP) * (sum( (decisionVars * sensiOP),1))'";
				
			
			com.jom.Expression jgtexpr = jom.parseExpression(gCTExpr);			
			ojbfunc_optimize_dispDVExpr(gCTExpr, "GT-Proto-JOM-Constraint-Non-Linear");
			System.out.println(" GT- Proto-JOM-Constraint-Non-Linear:  "+ jgtexpr.evaluate());
			//double[][] LimitOpt = { {12, 13, 14, 15, 16, 17} }; //new double[1][3];
			double[][] LimitOpt = { {10}, {20}, {30}, {40}, {50}, {60}  }; //new double[1][3];
			jom.setInputParameter("LimitOp", new DoubleMatrixND(LimitOpt));
			jom.addConstraint(gCTExpr + " <= LimitOp");	
						
			
			
			
			// ---Try the constraint and ObjFunc --//
			//jom.addDecisionVariable("decisionVars2", false, new int[] {1, 2 } );						
			//jom.addConstraint(" decisionVars  == decisionVars2");
			
			//- does not work -- jom.addConstraint(" decisionVars * decisionVars'  <= 10");
			//-- does not work -- jom.addConstraint(" sum(decisionVars^2, 2)  <= 10");
			//-- does not work -- jom.addConstraint(" sum(decisionVars' * decisionVars, 1)  >= 10");
			// --- d n w-- jom.addConstraint("sum(decisionVars * permute(decisionVars,[2;1]), 1)  >= 10");
			//jom.addConstraint("decisionVars * permute(decisionVars,[2;1])  >= 10");
			
			//jom.addConstraint(" decisionVars' * decisionVars <= 10", "BuggyConstraint");
			
			//jom.addConstraint(" sum( decisionVars .* (decisionVars * eye(numDV)) ,2) >= 10");
			
			
			//--Gerrit solution--//
			//jom.addConstraint(" decisionVars .* (decisionVars * eye(numDV)) >= 10");			
			//jom.addConstraint(" (decisionVars * eye(numDV)) .*  decisionVars >= 10");			
			//jom.addConstraint(" decisionVars .* (decisionVars * eye(numDV)) >= 10");
			
			
			//Other Constraints//
			jom.addConstraint(" decisionVars^2  <= 60");			
			//jom.addConstraint(" sum((decisionVars')^2, 1)  <= 10");
			
			
			ojbfunc_optimize_dispDVExpr("decisionVars", "DVTest-1");		
			System.out.println(" *** Objective Function *** " + jom.parseExpression("decisionVars").evaluate());
						
			//jom.setObjectiveFunction("minimize",  " sum( (decisionVars * decisionVars'),2) " ); //-- //Works
			//jom.setObjectiveFunction("minimize",  " (decisionVars * decisionVars') " ); //-- //Works
			jom.setObjectiveFunction("minimize",  "sum(  sum( sum( (decisionVars) *  sensiOP ,1),2) )");
			
			//("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" );
			
			System.out.println ("** TRY NON-LIENAR:  Objective Function ** : " + jom.getObjectiveFunction().getModel().toString() + " # Evaluate : " + jom.getObjectiveFunction().toString() );
			functUtil.solveObjectiveFunction(jom, false);
			functUtil.printOptimalResult(jom);
			System.out.println ("** TRY NON-LIENAR OVER! ***"); 
			//jom.setInputParameter("DVTest", "decisionVars^2 ");
			//double [][] DVTestArr= (double[][]) jom.getInputParameter("DVTest").toArray();						
			//this.oxl.genTxtFileFrom2DData(filepath, "DVTestArr.txt", DVTestArr);
			
			System.out.println(" Proto-JOM-Env ... Standalone Over.");
						
		}
		
		
		
		
		public void ojbfunc_optimize_checkSwapFxRiskFromOptimizedDV_Test() {
			System.out.println ("checkSwapFxRiskFromOptimizedDV_Test: CalcSwapFxRisk");
			//EXCEL: =WURZEL(MMULT($AD139:$AM139;MMULT(INDIREKT($BA139);MTRANS($AD139:$AM139))))/E139*1000
			//System.out.println (" -->> SwapFxRisk = " + this.jom.parseExpression("SwapFxRisk").evaluateConstant());
						
			//-- check if SwapSensiRisk contains NaN --//
			double [][] SwapFxSensiRiskArr= (double[][]) jom.getInputParameter("SwapFxRiskOpt").toArray();
			if (optimHandleNaN.arr2DContainsNaN(SwapFxSensiRiskArr)){
				System.out.println (" +++++++++++++-->> SwapFxSensiRiskArr NAN ERROR ");
				optimHandleNaN.arr2DNaNReplacedVal(SwapFxSensiRiskArr, 0.0);
			}
			
			
			//Error -- Caused by: com.jom.JOMException: Operator '*'  (matrix multiplication): Wrong size of the arrays for multiplication. Size left matrix: [31, 31]. Size right matrix: [10, 24]
			this.jom.setInputParameter("CalcSwapFxRisk", "  SwapFxRiskOpt * (PCCov_Mkt1_1_31 *  SwapFxRiskOpt') " ); /// 
			
					
			
			System.out.println (" CalcSwapFxRisk RowSize = " + this.jom.parseExpression("CalcSwapFxRisk(all,0)").evaluateConstant().getNumElements() + " /Col:" + 
					this.jom.parseExpression("CalcSwapFxRisk(0,all)").evaluateConstant().getNumElements() );
			
			
			//-- replace NaN with 0.0 // CalcSwapFxRiskArr 292x292//
			double [][] CalcSwapFxRiskArr= (double[][]) jom.getInputParameter("CalcSwapFxRisk").toArray();			
			this.oxl.genTxtFileFrom2DData(filepath, "CalcSwapFxRiskArr-292-292.txt", CalcSwapFxRiskArr);
			if (optimHandleNaN.arr2DContainsNaN(CalcSwapFxRiskArr)){
				System.out.println (" ??? CalcSwapFxRiskArr NAN! ERROR ");
				//optimHandleNaN.arr2DNaNReplacedVal(CalcSwapSensiRiskArr, 0.0);
			}
			
			double[] diagVectorSwapFxRisk = optimMatrixOps.diagonalMatrixVector(CalcSwapFxRiskArr);						
			this.oxl.genTxtFileFrom1DData(filepath, "diagVectorSwapFxRisk", diagVectorSwapFxRisk);			
			if (optimHandleNaN.isVectorNegative(diagVectorSwapFxRisk)) {
				System.out.println (" ??? diagVectorSwapFxRisk contains negative value! ERROR ");
			}
			
			double[][] diagSwapFxRiskArr = optimMatrixOps.get1ColDiagonalMatrix(CalcSwapFxRiskArr);			
			this.jom.setInputParameter("diagSwapFxRisk",new DoubleMatrixND(diagSwapFxRiskArr) );
			this.jom.setInputParameter("diagSwapFxRisk","sqrt(diagSwapFxRisk)" ); // /1.1E+12 /  /(1.1012 * (10 ^ 12))   / /(1.1*1000000000000d)
			
			//-- jom div with vector is / and with scalar is ./ --//
			//this.jom.setInputParameter("diagSwapFxRisk","(sqrt(diagSwapFxRisk)) ./(1000000000000d)" ); // /1.1E+12 /  /(1.1012 * (10 ^ 12))   / /(1.1*1000000000000d)
			this.jom.setInputParameter("diagSwapFxRisk","(sqrt(diagSwapFxRisk)) /(SchuldOpt)" ); // /1.1E+12 /  /(1.1012 * (10 ^ 12))   / /(1.1*1000000000000d)
			
			System.out.println (" >>> diagSwapFxRisk Final  = " + this.jom.parseExpression("diagSwapFxRisk(0:5,all)").evaluateConstant());
			double [][] diagSwapFxRiskArrF= (double[][]) jom.getInputParameter("diagSwapFxRisk").toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "diagMatrixSwapFxRiskF", diagSwapFxRiskArrF);
			//System.out.println (" -->>>> CalcSwapFxRiskM = " + this.jom.parseExpression("CalcSwapFxRiskM(0:5,all)").evaluateConstant());
			
		}
	    	
		
		
		public void ojbfunc_optimize_checkSwapRiskFromOptimizedDV_Test() {
    		
			System.out.println ("checkSwapRiskFromOptimizedDV_Test: CalcSwapRisk");
			    		
			//EXCEL: =WURZEL(MMULT($AD139:$AM139;MMULT(INDIREKT($BA139);MTRANS($AD139:$AM139))))/E139*1000
			//System.out.println (" -->> SwapSensiRisk = " + this.jom.parseExpression("SwapSensiRisk").evaluateConstant());
						
			//-- check if SwapSensiRisk contains NaN --//
			double [][] SwapSensiRiskArr= (double[][]) jom.getInputParameter("SwapSensiRiskOpt").toArray();
			if (optimHandleNaN.arr2DContainsNaN(SwapSensiRiskArr)){
				System.out.println (" +++++++++++++-->> SwapSensiRiskArr NAN ERROR ");
				optimHandleNaN.arr2DNaNReplacedVal(SwapSensiRiskArr, 0.0);
			}
			
			//this.jom.setInputParameter("CalcSwapSensiRisk", "  sqrt( SwapSensiRisk * (PCCov_Mkt1_10_10 *  SwapSensiRisk') )" ); /// 1.1E+12 //* (1/SchuldOpt')
			this.jom.setInputParameter("CalcSwapSensiRisk", "  SwapSensiRiskOpt * (PCCov_Mkt1_10_10 *  SwapSensiRiskOpt') " ); /// 
			
			System.out.println (" CalcSwapSensiRisk RowSize = " + this.jom.parseExpression("CalcSwapSensiRisk(all,0)").evaluateConstant().getNumElements() + " /Col:" + 
					this.jom.parseExpression("CalcSwapSensiRisk(0,all)").evaluateConstant().getNumElements() );
			
			
			//-- replace NaN with 0.0 // CalcSwapFxRiskArr 292x292//
			double [][] CalcSwapSensiRiskArr= (double[][]) jom.getInputParameter("CalcSwapSensiRisk").toArray();			
			this.oxl.genTxtFileFrom2DData(filepath, "CalcSwapSensiRiskArr-292-292.txt", CalcSwapSensiRiskArr);
			if (optimHandleNaN.arr2DContainsNaN(CalcSwapSensiRiskArr)){
				System.out.println (" ??? CalcSwapFxRiskArr NAN! ERROR ");
				//optimHandleNaN.arr2DNaNReplacedVal(CalcSwapSensiRiskArr, 0.0);
			}
			
			double[] diagVectorSwapSensiRisk = optimMatrixOps.diagonalMatrixVector(CalcSwapSensiRiskArr);						
			this.oxl.genTxtFileFrom1DData(filepath, "diagVectorSwapSensiRisk", diagVectorSwapSensiRisk);			
			if (optimHandleNaN.isVectorNegative(diagVectorSwapSensiRisk)) {
				System.out.println (" ??? diagVectorSwapSensiRisk contains negative value! ERROR ");
			}
			
			double[][] diagSwapSensiRiskArr = optimMatrixOps.get1ColDiagonalMatrix(CalcSwapSensiRiskArr);			
			this.jom.setInputParameter("diagSwapSensiRisk",new DoubleMatrixND(diagSwapSensiRiskArr) );
			this.jom.setInputParameter("diagSwapSensiRisk","sqrt(diagSwapSensiRisk)" ); // /1.1E+12 /  /(1.1012 * (10 ^ 12))   / /(1.1*1000000000000d)
			
			//-- jom div with vector is / and with scalar is ./ --//
			//this.jom.setInputParameter("diagSwapSensiRisk","(sqrt(diagSwapSensiRisk)) ./(1000000000000d)" ); // /1.1E+12 /  /(1.1012 * (10 ^ 12))   / /(1.1*1000000000000d)
			this.jom.setInputParameter("diagSwapSensiRisk","(sqrt(diagSwapSensiRisk)) /(SchuldOpt)" ); // /1.1E+12 /  /(1.1012 * (10 ^ 12))   / /(1.1*1000000000000d)
			
			System.out.println (" >>> diagSwapSensiRisk Final  = " + this.jom.parseExpression("diagSwapSensiRisk(0:5,all)").evaluateConstant());
			double [][] diagSwapSensiRiskArrF= (double[][]) jom.getInputParameter("diagSwapSensiRisk").toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "diagMatrixSwapSensiRiskF", diagSwapSensiRiskArrF);
			//System.out.println (" -->>>> CalcSwapFxRiskM = " + this.jom.parseExpression("CalcSwapFxRiskM(0:5,all)").evaluateConstant());
			
    	}
		
		
		
		//convert the matrix into diagonal vector //
		private void  ojbfunc_optimize_formSwapRiskConstraintExp_Proto_Test() {
				
					
					//SwapSensiRV2DOpt
					this.jom.setInputParameter("SwapSensiRiskOptTest", "SwapSensiRV2DOpt(0, all, all) " );
					this.appMain.ojbfunc_optimize_dispDVExpr("SwapSensiRiskOptTest", "getSwapRiskMatrixDiagV - SwapSensiRiskOptTest");			
					
					this.jom.setInputParameter("SwapSensiRiskOptTest", "sum( SwapSensiRiskOptTest, 1) " );
					this.appMain.ojbfunc_optimize_dispDVExpr("SwapSensiRiskOptTest", "getSwapRiskMatrixDiagV - SwapSensiRiskOptTest - Sum");			
					
					
					//this.jom.setInputParameter("CalcSwapSensiRisk", "  sqrt( SwapSensiRisk * (PCCov_Mkt1_10_10 *  SwapSensiRisk') )" ); /// 1.1E+12 //* (1/SchuldOpt')
					this.jom.setInputParameter("CalcSwapSensiRisk", "  SwapSensiRiskOptTest * (PCCov_Mkt1_10_10 *  SwapSensiRiskOptTest') " ); /// 
					this.appMain.ojbfunc_optimize_dispDVExpr("CalcSwapSensiRisk", "getSwapRiskMatrixDiagV : CalcSwapSensiRisk");
					
					double [][] CalcSwapSensiRiskArr= (double[][]) jom.getInputParameter("CalcSwapSensiRisk").toArray();						
					this.oxl.genTxtFileFrom2DData(filepath, "CalcSwapSensiRiskArr-Required.txt", CalcSwapSensiRiskArr);
					
					///////////////////
					// -- Create a diagonal matrix --//
					int[] sizev = (int[]) this.jom.parseExpression("CalcSwapSensiRisk").size();
					//-- create a diagonal-matrix (only diagonal, rest all zero) from value of diagonals of a matrix //
					this.jom.setInputParameter("nRowdiagSwapSensi", sizev[0]  );
					System.out.println(this.jom.parseExpression("nRowdiagSwapSensi").size() );			
					
					this.jom.setInputParameter("diagSwapSensiOpt","  CalcSwapSensiRisk .* eye(nRowdiagSwapSensi)" );
					double [][] diagSwapSensiOptArr= (double[][]) jom.getInputParameter("diagSwapSensiOpt").toArray();						
					this.oxl.genTxtFileFrom2DData(filepath, "diagSwapSensiOptArr.txt", diagSwapSensiOptArr);
					
					
					//-- NOT REQUIRED --- create a diagonal matrix (only diagonal, rest all zero) from value of diagonal of a matrix -- //
					/*
					ojbfunc_optimize_dispDVExpr("SchuldOpt","test-SchuldOpt");
					this.jom.setInputParameter("diagSchuldOpt","diag(SchuldOpt)" );
					ojbfunc_optimize_dispDVExpr("diagSchuldOpt","test-diagSchuldOpt");
					double [][] diagSchuldOptArr= (double[][]) jom.getInputParameter("diagSchuldOpt").toArray();						
					this.oxl.genTxtFileFrom2DData(filepath, "diagSchuldOptArr.txt", diagSchuldOptArr);
					*/
					
					//-- SwapSensiRiskOpt operation full --//				
					this.jom.setInputParameter("diagSwapSensiOpt","  sqrt(diagSwapSensiOpt)" );
					double [][] diagSwapSensiOptSqrtArr= (double[][]) jom.getInputParameter("diagSwapSensiOpt").toArray();						
					this.oxl.genTxtFileFrom2DData(filepath, "diagSwapSensiOptSqrtArr.txt", diagSwapSensiOptSqrtArr);
					
					this.jom.setInputParameter("diagSwapSensiOpt"," (1/SchuldOpt') * (diagSwapSensiOpt) " );
					double [][] diagSwapSensiOptSqrtDivShuldArr= (double[][]) jom.getInputParameter("diagSwapSensiOpt").toArray();						
					this.oxl.genTxtFileFrom2DData(filepath, "diagSwapSensiOptSqrtDivShuldArr.txt", diagSwapSensiOptSqrtDivShuldArr);
					
					this.jom.setInputParameter("oValSwapSensiOpt"," sum(diagSwapSensiOpt,2) " );
					this.appMain.ojbfunc_optimize_dispDVExpr("oValSwapSensiOpt", "getSwapRiskMatrixDiagV : oValSwapSensiOpt");
					System.out.println( " oValSwapSensiOpt : " + this.jom.parseExpression("oValSwapSensiOpt").evaluate() );;
					
					
					//Validation - DiagonlaMatrix
					double[][] diagSwapSensiRiskArr = optimMatrixOps.get1ColDiagonalMatrix(CalcSwapSensiRiskArr);			
					this.jom.setInputParameter("diagSwapSensiRisk",new DoubleMatrixND(diagSwapSensiRiskArr) );
					this.jom.setInputParameter("diagSwapSensiRisk","sqrt(diagSwapSensiRisk)" );			
					//this.jom.setInputParameter("diagSwapSensiRisk","(sqrt(diagSwapSensiRisk)) /(SchuldOpt)" );			
					double [][] diagSwapSensiRiskArrF= (double[][]) jom.getInputParameter("diagSwapSensiRisk").toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "diagMatrixSwapSensiRiskValidation", diagSwapSensiRiskArrF);
					
					//return fexpr;
		} // -- ojbfunc_optimize_getSwapRiskMatrixDiagV_Proto -- //
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		

		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//--- Prototypes, Troubleshooting, Test & Validation ---//		
		
		private void objfunc_displayJOMVarStruct (String jomvar, double[][] arr2D,  int option) {
			
			//option 1 to dispaly data, option 2 to dislay data-arch, option 3 to dispály data of native expression like ones, eye, diag etc
			//System.out.println (" joSwapFxRiskEff: " + optimStaticType.arr2DArchSpec(joSwapFxRiskEff,true) );
			if (arr2D != null)  {
				System.out.println (" arr struct: " + optimStaticType.arr2DArchSpec(arr2D,true) );
			}
			
			
			if (arr2D == null) {			
				int ndim = (int) this.jom.parseExpression(jomvar).getNumDim();
				
				if (option<=1) {
					
					if (ndim==2) System.out.println ( jomvar + optimStaticType.arr2DArchSpec((double [][])jom.getInputParameter(jomvar).toArray(), true) );
					
					if (ndim==3) {
						double[][][] jvarr= (double[][][]) this.jom.getInputParameter(jomvar).toArray();
						System.out.println( jomvar + " data struct: Tab=" + jvarr.length + " / Row=" + jvarr[0].length + " / Col=" + jvarr[0][0].length);
					}
				}
				
				if (option==2) {
					
					if (ndim==2) {
						System.out.println ( jomvar + " data struct: RowSize = " + this.jom.parseExpression(jomvar+"(all,0)").evaluateConstant().getNumElements() + 
									" /ColSize:"+ this.jom.parseExpression(jomvar+"(0,all)").evaluateConstant().getNumElements() );
					}
					if (ndim==3) {
						System.out.println ( jomvar + " data struct: TabSize = " + this.jom.parseExpression(jomvar+"(all,0,0)").evaluateConstant().getNumElements() + 
									" /RowSize:"+ this.jom.parseExpression(jomvar+"(0,all,0)").evaluateConstant().getNumElements() + 
									" /ColSize:"+ this.jom.parseExpression(jomvar+"(0,0,all)").evaluateConstant().getNumElements() );
					}
					
				}				
				if (option==3) { //Evaluate Native Expression  
					System.out.println ( this.jom.parseExpression(jomvar).evaluateConstant() );
				}
				
			} // --jomvar -- arr2D null --//
			
		} //-- dispay jom dataStrcut --//

		
		
		
		private void ojbfunc_optimize_dispDVExpr(String expr, String label) {
			if (label == null) label="DV Expr";
			//if (!this.jom.parseExpression(expr).isScalar()) { // -- crashes some times
			if (this.jom.parseExpression(expr).getNumDim() > 1) {
				int ndim = this.jom.parseExpression(expr).getNumDim();			
				System.out.println(label + ": dispDVExpr:  NumDim of expression: " + ndim);
				int[] sizev = (int[]) this.jom.parseExpression(expr).size();
				if (ndim == 3) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Tab=" + sizev[0] +  " /Row=" + sizev[1] +  " /Col=" + sizev[2] + " ] ");
				if (ndim == 2) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Row=" + sizev[0] +  " /Col=" + sizev[1] + " ] ");
			} else System.out.println(label + ": dispDVExpr: scalar quantity !");
		}
		
		
		
		
		//--- Prototypes, Troubleshooting, Test & Validation ---//
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    