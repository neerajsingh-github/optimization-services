
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
	

    public class optimValidateBondsProto {
		
    	//-- Bonds Opti Config / Flag--//
		private final int numYear = 3;
		private final int numInstr = 2;
		private final int numDV = numYear * numInstr;
		private double[][] EBYearly;
    	private String[][] EBInstrYearly;
    	private String[][] YIEBInstrYearly;
    	private String[][] ExprDV;
    	private String[][] ExprYIDV;
    	private String[][] ExprDVEB;
    	private String[][] ExprYIDeltaDVEB;
    	
    	
    	
    	private String[][] DispEBYearly;
    	private String[][] DispYIEBInstrYearly;
    	private String[][] DispExprYIDV;
    	
    	private String[][] DispExprYIDeltaDVEB;
    	
    	private String[][] ExprYIDeltaDVEBAddYearly; 
    	private String[][] DispExprYIDeltaDVEBAddYearly;
    	
    	
    	private String[][] InitExprDVEB;
    	private String[][] InitExprYIDVEB;
    	private String[][] DispInitExprDVEB;
    	private String[][] DispInitExprYIDVEB;
    	private String[][] DVVarsBondNetExp;
    	
    	private String[][] SumDVVarsBondNetExp; 
    	
    	
    	
    	//-- Extended DV -  Vars --//
    	private String[][] ExtDVExprYIDeltaDVEB; //String[numYear][numInstr];
    	private String[][] ExtDVDispExprYIDeltaDVEB; //String[numYear][numInstr];

    	private String[][] ExtDVExprYIDeltaDVEBAddYearly; //  String[numYear][1];
    	private String[][] ExtDVDispExprYIDeltaDVEBAddYearly; //String[numYear][1];

    	
    	private String[][] ExtDVInitExprDVEB;
    	private String[][] ExtDVInitExprYIDVEB;
    	private String[][] ExtDVDispInitExprDVEB;
    	private String[][] ExtDVDispInitExprYIDVEB;
    	
    	private String[][] ExtDVExprDV;
    	private String[][] ExtDVExprYIDV;
    	private String[][] ExtDVDispExprDV; 
    	private String[][] ExtDVDispExprYIDV;
    	

    	private String[][] ExtDVDVarsBondNetExp;
    	private String[][] ExtDVSumDVVarsBondNetExp;

    	private String[][] ExtDVExtConstraint;
    	private String[][] ExtDVDispExtConstraint;
    	
		//-- object config--//
    	private OptimizationProblem jom=null;
    	private optimSolveDataset dataSet=null;
		private optimSolveRuleConfig ruleConf=null;	
		private optimSolveFunctUtil functUtil=null;
		private optimSolveFunctApp appMain=null;
		
		//-- Local config--//
		private String filepath;
    	private createOXLFromIBean oxl;
    	
		
		
    	// --- Validation  ---//    	
    	//-- Stream out the values of core functions for validation - do not invoke Optimizer --//
    	// --- Validation SRV Data ---//
		
    	public void ojbfunc_optimize_BondsProto_setupEnv(optimSolveFunctApp appMainObj) 	{
    		
    		this.appMain = appMainObj;
    		this.jom=appMainObj.jom;
        	this.dataSet=appMainObj.dataSet;
    		this.ruleConf=appMainObj.ruleConf;	
    		this.functUtil=appMainObj.functUtil;
    		
    		//--Dataset --//
			if (this.dataSet == null) {
				this.dataSet= new optimSolveDataset(this.ruleConf);
			}			
			
			//---this.dataSet.optimLoadEffectiveSRVData();---			
			//if (this.appMain != null) this.appMain.objfunc_optimize_extractEffData();
			
			
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
    	//--- Add Objective Function -- Mehrkosten -- Bond+Swap --- //
		public String  ojbfunc_optimize_formBondSwapMehrkostenObjFunctExpValidate() {
			
			//String PVTotalVec= " (   (PVCleanFullOpt) + (sum( (decisionVarsBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (decisionVarsSwap *  SwapsRV2DPVCleanOpt), 1))   ) ";		
			String PVTotalVec= " (   (PVCleanFullOpt) + (sum( (DVarsBond *  BondsRV2DPVCleanOpt), 1))    ) ";
			PVTotalVec = "(" + PVTotalVec + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalVec, "Mehrkosten-pvTotalVec + BondSwap");
			
			//-- build a matrix of PVTotalOptArr, where 1st-col of 120 rows are copied to 31 cols --// -- [ ((this.ruleConf.numYears) * (this.ruleConf.monthsInYear)), 31] --//
			String PVTotalMatrix =  PVTotalVec + " *  ones([1;31])" ;
			PVTotalMatrix = "(" + PVTotalMatrix + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalMatrix, "Mehrkosten-PVTotalMatrix");
			
			
			String PVTotalRefSensi =  PVTotalMatrix + " .* RefPCSensiOpt " ;
			PVTotalRefSensi = "(" + PVTotalRefSensi + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalRefSensi, "Mehrkosten-PVTotalRefSensi");
										
			
						
			
			//String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( sum( (decisionVarsBond) *  BondsRV2DOpt ,1) + sum( (decisionVarsSwap) *  SwapsRV2DOpt ,1)  )  + BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
			String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( sum( (decisionVarsBond) *  BondsRV2DOpt ,1)   - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
			
			
			BondSwapMehrKosten= "(" + BondSwapMehrKosten + ") * 10000.0d";
			ojbfunc_optimize_dispDVExpr(BondSwapMehrKosten, "Mehrkosten-BondSwapMehrKosten");
			
			return BondSwapMehrKosten;
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
///////////////LATER --<<<<<<<<<<<<<<<<<<
		
		private String exprRecurseEBDV(int numYear, int numInstr){
			String prevEBDV="";
			
			//--this.EBYearly[i][0] --//
			if (numYear==0){ 
				prevEBDV=" EBYearopt(cYear) ";
				return prevEBDV;
			}
			else {
				for (int i=0; i<numInstr; i++)
					prevEBDV += "+" + this.EBInstrYearly[numYear][i];
			}
			
			System.out.println(">><< exprRecurseEBDV - recursive expr:" + prevEBDV);
			return  prevEBDV + exprRecurseEBDV(numYear-1, numInstr);
		}
		
		private String exprRecurseDirectEBDV(int numYear, int numInstr){
			String prevEBDV="";
			
			for(int y=1; y<numYear; y++) {
				//prevEBDV="";
				//if (y==0) prevEBDV=" EBYearopt(cYear) ";
				for (int i=0; i<numInstr; i++) {
						prevEBDV += " EBYearopt(cYear) + " + this.EBInstrYearly[y-1][i];
				}
			}
						
			System.out.println(">>>>>> exprRecurseDirectEBDV - recursdirect expr:" + prevEBDV);
			return  prevEBDV ;
		}		//++++++++++++LATER-ON---
		
		private void testTree() {
			// --- TEST TREE--
			OptimUtilLLTreeNodeValidate.IterMain(new String[]{"TreeNodeValidate", "IterMain"});
			OptimUtilLLTreeNodeValidate.searchMain(new String[] {"TreeNodeValidate","SearchMain"});
		}
		private void recurseStack(int s, int f){
			if (s==f) {
				System.out.println(" s eq f");
				testTree();
			} else {
				
				System.out.println(" Before recurse s = " + s +" / f =" + f);
				testToyProto(s+1,f);
				System.out.println(" After recurse s = " + s +" / f ="  + f);
			}
		}
		private void testToyProto(int s, int f){
			testTree();
		}
		///////////////////////////////////////////////////////////>>>>>>>>>>>>>>>>>	
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//--- Bonds Prototypes, Troubleshooting, Test & Validation ---//	
		private void buildNetEigenBestandDVF(){
			String exprDVNet="";
			
			//-- YIEBInstrYearly--;  
			for (int i=0, cnt=0; i<this.InitExprYIDVEB.length; i++ ) { // this.EBInstrYearly[i][j] = exprEBIT;
				for (int j=0; j<this.InitExprYIDVEB[0].length; j++ ) { 
					jom.setInputParameter("cYear", i);
					jom.setInputParameter("cInstr", j);
					jom.setInputParameter("cYearInstr", i+j);
					exprDVNet="(   ( DVarsBond(0,cYearInstr) ) - (" + this.InitExprYIDVEB[i][j] +" )   )";
				
					jom.setInputParameter("cntBondNetExp", cnt);
					//jom.setInputParameter("DVVarsBondNetExp(0,cntBondNetExp)", exprDVNet);
										
					this.DVVarsBondNetExp[cnt++][0] =exprDVNet;
					
					System.out.println(" EXPR buildNetEigenBestandDV  Year/Instr:"+ i + "/" + j + " = "+ exprDVNet);
				}
			}
			//System.out.println(" EXPR buildNetEigenBestandDV = "+ exprDVNet);
		}
		private void buildSumDVVarsBondNetExpF() {
			this.SumDVVarsBondNetExp = new String[1][1];
			this.SumDVVarsBondNetExp[0][0]  = this.DVVarsBondNetExp[0][0] ;
			for (int i=1; i<this.DVVarsBondNetExp.length; i++ ) {
				this.SumDVVarsBondNetExp[0][0] += " + " + this.DVVarsBondNetExp[i][0] ;
			}
			this.SumDVVarsBondNetExp[0][0]  = "sum ( " + this.SumDVVarsBondNetExp[0][0] + " )";
		}
		///////////////////////////////////////////////////////////
		private String getExprDVF(int numYear, int numInstr){
			
			this.ExprDV = new String[1][numYear * numInstr];
			this.ExprYIDV = new String[numYear][numInstr];
			this.DispExprYIDV = new String[numYear][numInstr];
			String dvExpr= "";

			for (int i=0; i<numYear; i++) {
				jom.setInputParameter("cYear", i);
				int shStartIdx= (i*numInstr);
    			jom.setInputParameter("cDVYearlyShiftStartIdx", shStartIdx );
    			int shEndIdx= (i*numInstr) + (numInstr-1);
    			jom.setInputParameter("cDVYearlyShiftEndIdx", shEndIdx );
    			
    			//System.out.println(" ID_RANGE_TIME: EXPR exprEBWithDV: cYear:" + i + " //cDVYearlyShiftStartIdx: " +shStartIdx + " //cDVYearlyShiftEndIdx: " +shEndIdx ); 
				
    			
				for (int j=0; j<numInstr; j++) {
					// -- for each DV Ratio --//
					jom.setInputParameter("cInstr", j);
    				jom.setInputParameter("cDVYearlyInstrShiftCurrIdx", (shStartIdx + j) );
    				//System.out.println(" ID_RANGE_TIME_INSTR: EXPR InstrShiftCurrIdx: cInstr:" +j + "/cYear:" +i + " //cDVYearlyInstrShiftCurrIdx: "+ (shStartIdx + j) );
    				
    				
    				dvExpr= " DVarsBond(cDVYearlyInstrShiftCurrIdx) / sum( DVarsBond(cDVYearlyShiftStartIdx:cDVYearlyShiftEndIdx)) ";
    				this.ExprDV[0][shStartIdx + j] = "( " + dvExpr + " )"; 
    				this.ExprYIDV[i][j]= "( " + dvExpr + " )";
    				System.out.println(" EXPR exprEBWithDV: cInstr:" +j + "/cYear:" +i + " //ExprYIDV: dvExpr= "+ this.ExprYIDV[i][j] );
    				
    				DispExprYIDV[i][j]= "( DVarsBond(" + (shStartIdx + j) + ")/ sum( DVarsBond( " +shStartIdx + ":" + shEndIdx  +") )";
    				System.out.println(" EXPR exprEBWithDV# " + DispExprYIDV[i][j] );
    				//System.out.println(" EXPR exprEBWithDV# ( DVarsBond(" + (shStartIdx + j) + ")/ sum( DVarsBond( " +shStartIdx + ":" + shEndIdx  +") )" );
				}
			}
			return dvExpr;
		}
		///////////////////////////////////////////////////////////
		
		private void exprDeltaMatF(int cyear, int numInstr) {
			//---DeltaMatrix Expression --//
			boolean setDelta1=true;
			if (cyear<1) return;
			
			//this.ExprYIDeltaDVEB = new String[numYear][numInstr];
			//for (int j=0; j<numInstr; j++) this.ExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			//this.DispExprYIDeltaDVEB = new String[numYear][numInstr];
			//for (int j=0; j<numInstr; j++) this.DispExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			
			int deltaMatSIdx=cyear-1;
			int deltaMatEIdx=cyear;
			
			for (int j=0; j<numInstr; j++) {
				String exprDeltaMat="";
				exprDeltaMat = "DeltaMat"+ Integer.toString(j) + "-"+ Integer.toString(deltaMatSIdx)+":"+ Integer.toString(deltaMatEIdx);
				if (setDelta1 == true) exprDeltaMat = " 1.1 ";
				String exprec =  " (" + this.InitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat + ") ";
				this.ExprYIDeltaDVEB[cyear][j]= exprec;
				
				System.out.println("** 1++++exprDeltaMatF:  exprRecurseCurrent : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + exprec);
				
				this.DispExprYIDeltaDVEB[cyear][j]= " (" + this.DispInitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat  + ") ";
				System.out.println("** 1>>>exprDeltaMatF:  DispExprRecurseCurrent : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + this.DispExprYIDeltaDVEB[cyear][j]);
			}
			
			return;
		} //-- exprDeltaMatF --//
				
		
		
		
		private void exprRecurseCurrentEBWithDVF(int cyear, int numInstr) {
			if (cyear<1) return;
			
			//this.ExprYIDeltaDVEBAddYearly= new String[numYear][1];
			//this.DispExprYIDeltaDVEBAddYearly=new String[numYear][1];
			
			exprDeltaMatF(cyear, numInstr);
			
			for (int j=0; j<numInstr; j++) {
				
				//--- iterate through instruments ---//
				if (j==0) { 
					this.ExprYIDeltaDVEBAddYearly[cyear][0] = this.ExprYIDeltaDVEB[cyear][j]; 
					this.DispExprYIDeltaDVEBAddYearly[cyear][0] = this.DispExprYIDeltaDVEB[cyear][j]; 
					System.out.println("**2 exprRecurseCurrentEBWithDVF>>> DispExprYIDeltaDVEBAddYearly : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + this.DispExprYIDeltaDVEBAddYearly[cyear][j]);
				} else {
					this.ExprYIDeltaDVEBAddYearly[cyear][0] += " + " + this.ExprYIDeltaDVEB[cyear][j];
					this.DispExprYIDeltaDVEBAddYearly[cyear][0] += " + " + this.DispExprYIDeltaDVEB[cyear][j];
					System.out.println("**2 exprRecurseCurrentEBWithDVF >>> DispExprYIDeltaDVEBAddYearly : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + this.DispExprYIDeltaDVEBAddYearly[cyear][0]);
				}
				
			}
			return;     
		}
		
		
		private String initExprEBWithDVF(int numYear, int numInstr){
			String exprEBDV="";
			//String expreRecurse ="";
			
			this.EBInstrYearly = new String[1][numYear * numInstr];
			this.YIEBInstrYearly = new String[numYear][numInstr];
			this.DispYIEBInstrYearly = new String[numYear][numInstr];
			
			this.ExprDVEB = new String[1][numYear * numInstr];
			this.InitExprDVEB = new String[1][numYear * numInstr];
			this.InitExprYIDVEB = new String[numYear][numInstr];
			this.DispInitExprDVEB = new String[1][numYear * numInstr];
			this.DispInitExprYIDVEB = new String[numYear][numInstr];
			
			
			this.ExprYIDeltaDVEBAddYearly= new String[numYear][1];
			this.DispExprYIDeltaDVEBAddYearly=new String[numYear][1];
			
			this.ExprYIDeltaDVEB = new String[numYear][numInstr];
			for (int j=0; j<numInstr; j++) this.ExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			this.DispExprYIDeltaDVEB = new String[numYear][numInstr];
			for (int j=0; j<numInstr; j++) this.DispExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			
			
			getExprDVF(numYear,numInstr); // access ExprYIDV(numYear, numInstr) ---//
			
			for (int i=0; i<numYear; i++) {
				
				int shStartIdx= (i*numInstr);
				
				for (int j=0; j<numInstr; j++) {
					// -- for each DV Ratio --//
					
    				if (j==0) exprRecurseCurrentEBWithDVF(i,numInstr);
					
    				//////////
    				//---Start: attach EBYearlyOpt with DV ---//
    				//exprEBDV = dvExpr + " * EBYearopt(cYear)";
    				//System.out.println(" EXPR exprEBWithDV: " + exprEBDV );
    				//System.out.println(" EXPR exprEBWithDV# ( DVarsBond(" + (shStartIdx + j) + ")/ sum( DVarsBond( " +shStartIdx + ":" + shEndIdx  +") ) * EBYearopt(" + i +")" );
    				if (i==0) { 
    					
						//this.YIEBInstrYearly[i][j] = "EBYearopt(cYear)";
						//this.EBInstrYearly[0][shStartIdx + j] = "EBYearopt(cYear)";
						
    					exprEBDV = this.ExprYIDV[i][j]  + " * (" + this.EBYearly[i][0] +" )";
						this.InitExprYIDVEB[i][j] = exprEBDV;
						this.InitExprDVEB[0][shStartIdx+j] = exprEBDV;
						System.out.println("**3- 1st YEAR: exprRecurseEBDV - recursive expr:cInstr:" +j + "/cYear:" +i +" =Expr:" + exprEBDV);
						
											  
						String dispExprEBDV = this.DispExprYIDV[i][j]  + " * (" + this.DispEBYearly[i][0] +" )";
						this.DispInitExprYIDVEB[i][j] = dispExprEBDV;
						this.DispInitExprDVEB[0][shStartIdx+j] = dispExprEBDV;
						//System.out.println("** CHK--- 1st YEAR:" + dispExprEBDV + " CHK-DispEBYearly=" + this.DispEBYearly[i][0]);
						
						System.out.println("**3- 1st YEAR: DispexprRecurseEBDV - recursive expr:cInstr:" +j + "/cYear:" +i +" =Expr:" + this.DispInitExprYIDVEB[i][j]);
						
					} 
    				else { 
    					
						//this.YIEBInstrYearly[i][j] = "EBYearopt(cYear)";
						//this.EBInstrYearly[0][shStartIdx + j] = "EBYearopt(cYear)";
    					
    					//System.out.println("**3 CHK- 2nd YEAR ON: exprRecurseEBDV :cInstr:" +j + "/cYear:" + i );
						exprEBDV = this.ExprYIDV[i][j]  + " * ( " +  this.EBYearly[i][0] + " + " + ExprYIDeltaDVEBAddYearly[i][0] + " )";
						
						this.InitExprYIDVEB[i][j] = exprEBDV;   
						this.InitExprDVEB[0][shStartIdx+j] = exprEBDV;
						//this.ExprDVEB[0][shStartIdx+j] = exprEBDV;
						System.out.println("**3- 2nd YEAR ON: exprRecurseEBDV :cInstr:" +j + "/cYear:" +i +" =Expr:" + this.InitExprYIDVEB[i][j]);
						
												
						String dispExprEBDV = this.DispExprYIDV[i][j]  + " * ( " + this.DispEBYearly[i][0] + " + " + this.DispExprYIDeltaDVEBAddYearly[i][0] + " )";
						this.DispInitExprYIDVEB[i][j] = dispExprEBDV;
						this.DispInitExprDVEB[0][shStartIdx+j] = dispExprEBDV;
						System.out.println("**3- 2nd YEAR ON: DispExprRecurseEBDV :cInstr:" +j + "/cYear:" +i +" =Expr:" + this.DispInitExprYIDVEB[i][j]);
						
					}
    				
				} //-- for each Instr--//
				
			} //-- for each year--//
			
			return exprEBDV;
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//--- Standard DV  ---// 
    	//public void ojbfunc_optimize_BondsProto_TestMain(optimSolveFunctApp appMainObj, int numYears, boolean addDKF, double addDKFForYears ) {
    	public void ojbfunc_optimize_BondsProto_Standard_TestMain(optimSolveFunctApp appMainObj ) {
    				
    		System.out.println(" ### Standard DV ####");
    		ojbfunc_optimize_BondsProto_setupEnv(appMainObj);
    		int numDV= this.numInstr * this.numYear ; // // 4 instruments x 5 years = 20
			jom.setInputParameter("numDV", numDV);
			
			jom.addDecisionVariable("DVarsBond", false, new int[] { 1, numDV } );	
			//jom.addDecisionVariable("DVVarsBondNetExp", false, new int[] { 1, numDV } );
			jom.addDecisionVariable("decisionVars", false, new int[] { 1, numDV } );	
			System.out.println(" BONDs Proto-JOM-Env ... with numDV = " + numDV);
				
			ojbfunc_optimize_BondsProto_Eigenbestand();
			ojbfunc_optimize_BondsProto_BondsBkt();
			String finalExpr = initExprEBWithDVF(this.numYear, this.numInstr);
			System.out.println(" ### Final buildExprEBWithDVF recv = " + finalExpr);
			
			//if (true) return;
			
			// Build NettoEigenBestand Matrix with DV
			this.DVVarsBondNetExp = new String[numDV][1];
			buildNetEigenBestandDVF();
			
			//************* ---- ISSSUE ------*****//
			//this.jom.setInputParameter("DVVarsBondNetExpOpt",  DVVarsBondNetExp);
			//this.jom.setInputParameter("DVarsBond", DVVarsBondNetExp);
			//this.jom.setInputParameter("NetDVTimesBbktOpt",  "DVVarsBondNetExp *  BondsBbktOpt");
			
			//this.jom.setInputParameter("NetDVTimesBbktOpt",  "DVVarsBondNetExpOpt *  BondsBbktOpt");
			
			if (false) {
				String[][] fExprOF = new String[numDV][1];
				for (int m=0; m< numDV; m++) {
					jom.setInputParameter("cntBondsNetExp", m);
					
					fExprOF[m][0]=  this.DVVarsBondNetExp[m][0] ;
					//fExprOF[m][0]= "DVVarsBondNetExp(0,cntBondsNetExp)   =  DVarsBond(0,cntBondsNetExp) - ( " + this.DVVarsBondNetExp[m][0] + " )" ;
					
					//jom.setInputParameter("DVVarsBondNetExp(0,cntBondsNetExp)", "DVarsBond(0,cntBondsNetExp)");
					//jom.setInputParameter("DVVarsBondNetExp", "DVarsBond(0,cntBondsNetExp) - ( " + this.DVVarsBondNetExp[m][0] + " )"); //;DVVarsBondNetExp
					
					System.out.println(" Expr : " + fExprOF[m][0] );
					if (m<0) {
						System.out.println(" Expr eval: cYearInstr= " + jom.parseExpression("cYearInstr").evaluate() );
						System.out.print(" : cDVYearlyInstrShiftCurrIdx= " + jom.parseExpression("cDVYearlyInstrShiftCurrIdx").evaluate() );
						System.out.print(" : cDVYearlyShiftStartIdx= " + jom.parseExpression("cDVYearlyShiftStartIdx").evaluateConstant() ); 
						System.out.print(" : cDVYearlyShiftEndIdx= " + jom.parseExpression("cDVYearlyShiftEndIdx").evaluateConstant() );
					}
				}		
			}
			//-- Some other tests ---//
			if (false) {
				System.out.println("###########+ TREE Recursive Test --- Later on  +#############");
				testToyProto(1,3);
			} //---else for second year on--//
			
			//************* ---- ISSSUE ------*****//
			
			
			//--setup constraints  --//
			jom.addConstraint("decisionVars == DVarsBond");
			jom.addConstraint(" DVarsBond >= 1.0");
			jom.addConstraint(" DVarsBond <= 1000.0");
			
			//-- Objective Function --- sum of final expr -- SumDVVarsBondNetExp --//
			buildSumDVVarsBondNetExpF();
			
			//jom.setObjectiveFunction("minimize",  this.DVVarsBondNetExp[numDV-1][0]);
			//jom.setObjectiveFunction("minimize",  this.DVVarsBondNetExp[numDV-1][0]  );
			jom.setObjectiveFunction("minimize",  this.SumDVVarsBondNetExp[0][0]  );
			
			//--- Run Solver ---//
			functUtil.solveObjectiveFunction(jom, false);
			
			
			//--- Display Result ---//
			//ojbfunc_optimize_dispDVExpr("decisionVars", "DVTest-1");		
			//System.out.println(" *** Objective Function *** " + jom.parseExpression("decisionVars").evaluate());
			
			functUtil.printOptimalResult(jom);
			
			System.out.println ("** STANDARD: BONDS PROTO  OVER! ***"); 
				
    	}
    	
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	// --- Invoke the BondProto ---//
    	public void ojbfunc_optimize_BondsProto_TestMain(optimSolveFunctApp appMainObj ) {
    	
    		//ojbfunc_optimize_BondsProto_Standard_TestMain(appMainObj );
    		ojbfunc_optimize_BondsProto_ExtendedDV_TestMain(appMainObj);
    		
    	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    	
    	
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//--- Extended DV  ---//
    	
    	//Concept: ExtDV;  Ext DV Ratio Expr // -- //-- not being used at the moment --//
		private void ExtDVgetExprExtDVRatio_OLD(int numYear, int numInstr){ //  not being used --//
			this.ExtDVExprDV = new String[1][numYear * numInstr];
			//this.ExprYIDV = new String[numYear][numInstr];
			this.ExtDVDispExprDV = new String[1][numYear * numInstr];
			String dvExpr= "";

			for (int i=0; i<numYear; i++) {
				jom.setInputParameter("cYear", i);
				int shStartIdx= (i*numInstr);
    			jom.setInputParameter("cDVYearlyShiftStartIdx", shStartIdx );
    			int shEndIdx= (i*numInstr) + (numInstr-1);
    			jom.setInputParameter("cDVYearlyShiftEndIdx", shEndIdx );
    			
    			//System.out.println(" ID_RANGE_TIME: EXPR exprEBWithDV: cYear:" + i + " //cDVYearlyShiftStartIdx: " +shStartIdx + " //cDVYearlyShiftEndIdx: " +shEndIdx ); 
				
				for (int j=0; j<numInstr; j++) {
					// -- for each DV Ratio --//
					jom.setInputParameter("cInstr", j);
    				jom.setInputParameter("cDVYearlyInstrShiftCurrIdx", (shStartIdx + j) );
    				//System.out.println(" ID_RANGE_TIME_INSTR: EXPR InstrShiftCurrIdx: cInstr:" +j + "/cYear:" +i + " //cDVYearlyInstrShiftCurrIdx: "+ (shStartIdx + j) );
    				
    				
    				dvExpr= " ExtDVarsBond(cDVYearlyInstrShiftCurrIdx) / sum( ExtDVarsBond(cDVYearlyShiftStartIdx:cDVYearlyShiftEndIdx)) ";
    				this.ExtDVExprDV[0][shStartIdx + j] = "( " + dvExpr + " )";
    				System.out.println(" EXPR ExtDVExprDV: cInstr:" +j + "/cYear:" +i + " //ExprYIDV: dvExpr= "+ this.ExtDVExprDV[0][shStartIdx + j] );
    				
    				this.ExtDVDispExprDV[0][shStartIdx + j]= "( ExtDVarsBond(" + (shStartIdx + j) + ")/ sum( ExtDVarsBond( " +shStartIdx + ":" + shEndIdx  +") )";
    				System.out.println(" EXPR ExtDVDispExprDV# " + this.ExtDVDispExprDV[0][shStartIdx + j] );
    				
				}
			}
			return;
		}


		
		
		//Concept: ExtDV;  Ext DV  Expr // -- //-- not being used at the moment --//
		private void ExtDVgetExprExtDVF(int numYear, int numInstr){ 
			this.ExtDVExprDV = new String[1][numYear * numInstr];   // ExtDVarsBond 
			this.ExtDVExprYIDV = new String[numYear][numInstr];
			this.ExtDVDispExprDV = new String[1][numYear * numInstr];
			this.ExtDVDispExprYIDV=new String[numYear][numInstr];
			String dvExpr= "";

			for (int i=0; i<numYear; i++) {
				
				int shStartIdx= (i*numInstr);
    			int shEndIdx= (i*numInstr) + (numInstr-1);
    			
    			//System.out.println(" ID_RANGE_TIME: EXPR exprEBWithDV: cYear:" + i + " //cDVYearlyShiftStartIdx: " +shStartIdx + " //cDVYearlyShiftEndIdx: " +shEndIdx ); 
				
				for (int j=0; j<numInstr; j++) {
					
					dvExpr= " ExtDVarsBond(0," + (shStartIdx+j) + ")" ;   /// No Ratio ---sum( ExtDVarsBond(cDVYearlyShiftStartIdx:cDVYearlyShiftEndIdx))
    				this.ExtDVExprDV[0][shStartIdx + j] =  dvExpr ;
    				this.ExtDVExprYIDV[i][j] = dvExpr;
    				System.out.println(" EXPR ExtDVExprDV: cInstr:" +j + "/cYear:" +i + " //ExprYIDV: dvExpr= "+ this.ExtDVExprDV[0][shStartIdx + j] );
    				
    				this.ExtDVDispExprDV[0][shStartIdx + j]= dvExpr; ///   --- No Ratio ---/ sum( ExtDVarsBond( " +shStartIdx + ":" + shEndIdx  +") )";
    				this.ExtDVDispExprYIDV[i][j] = dvExpr;
    				System.out.println(" EXPR ExtDVDispExprDV# " + this.ExtDVDispExprDV[0][shStartIdx + j] );
    				
				}
			}
			return;
		}
		
		///////////////////////////////////////////////////////////
		//Concept: ExtDV;  EB_Prime= E/Sum(DV) // -- load with ExtEBDVarsBond
		private void ExtDVgetExprExtEBDVF(int numYear, int numInstr){ 
			this.ExtDVInitExprDVEB = new String[1][numYear * numInstr];
			this.ExtDVInitExprYIDVEB = new String[numYear][numInstr];
			this.ExtDVDispInitExprDVEB = new String[1][numYear * numInstr];
			this.ExtDVDispInitExprYIDVEB = new String[numYear][numInstr];
			
			for (int i=0; i < numYear; i++) {
				int shStartIdx= (i * numInstr);
				
				for (int j=0; j<numInstr; j++) {
					
					this.ExtDVInitExprDVEB[0][shStartIdx + j]= "ExtEBDVarsBond(0," + (shStartIdx+j) + ")";
					this.ExtDVInitExprYIDVEB[i][j]= "ExtEBDVarsBond(0," + (shStartIdx + j) + ")";
					
					//this.ExtDVInitExprDVEB[0][shStartIdx + j]= this.InitExprDVEB[0][shStartIdx + j] + " / sum(DVarsBond)";
					//this.ExtDVInitExprYIDVEB[i][j]= this.InitExprDVEB[0][shStartIdx + j] + " / sum(DVarsBond)";
					
					this.ExtDVDispInitExprDVEB[0][shStartIdx + j]= "ExtEBDVarsBond(0," + (shStartIdx + j) + ")";
					this.ExtDVDispInitExprYIDVEB[i][j]= "ExtEBDVarsBond(0," + (shStartIdx+j) + ")";
					
					//this.ExtDVDispInitExprDVEB[0][shStartIdx + j]= this.DispInitExprDVEB[0][shStartIdx + j] + " / sum(DVarsBond)";
					//this.ExtDVDispInitExprYIDVEB[i][j]= this.DispInitExprDVEB[0][shStartIdx + j] + " / sum(DVarsBond)";
					
					
					//System.out.println("**11- Ext: DispExprRecurseEBDV :cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVInitExprDVEB[0][shStartIdx + j]);
					//System.out.println("***##*** 11- Ext: DispExprRecurseEBDV :cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVDispInitExprDVEB[0][shStartIdx + j]);
				}
			}
		}
		

		///////////////////////////////////////////////////////////////
    	private void ExtDVExprDeltaMatF(int cyear, int numInstr) {
			//---DeltaMatrix Expression --//
			boolean setDelta1=true;
			if (cyear<1) return;
			
			//this.ExtDVExprYIDeltaDVEB = new String[numYear][numInstr];
			//for (int j=0; j<numInstr; j++) this.ExtDVExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			//this.ExtDVDispExprYIDeltaDVEB = new String[numYear][numInstr];
			//for (int j=0; j<numInstr; j++) this.ExtDVDispExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			
			int deltaMatSIdx=cyear-1;
			int deltaMatEIdx=cyear;
			
			for (int j=0; j<numInstr; j++) {
				String exprDeltaMat="";
				exprDeltaMat = "DeltaMat"+ Integer.toString(j) + "-"+ Integer.toString(deltaMatSIdx)+":"+ Integer.toString(deltaMatEIdx);
				if (setDelta1 == true) exprDeltaMat = " 173.431 ";
				
				//this.ExtDVExprYIDV[i][j]  + " * "+ ExtDVInitExprYIDVEB[i][j] + " * " +
				//String exprec =  " (" + this.ExtDVInitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat + ") ";
				//String exprec =  " ( " + ExtDVExprYIDV[cyear-1][j] + " * " + ExtDVInitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat + ") ";
				//String exprec =  " ( " + ExtDVExprYIDV[cyear-1][j] + " * " + this.ExtDVInitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat + ") ";
				String exprec =  " ( "  + this.ExtDVInitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat + ") ";
				this.ExtDVExprYIDeltaDVEB[cyear][j]= exprec;
				
				//this.ExtDVDispExprYIDeltaDVEB[cyear][j]= " (" + ExtDVDispExprYIDV[cyear-1][j] + " * " + ExtDVDispInitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat  + ") ";
				this.ExtDVDispExprYIDeltaDVEB[cyear][j]= " (" + ExtDVDispInitExprYIDVEB[cyear-1][j] + " * " + exprDeltaMat  + ") ";
				
				//System.out.println("** 1++++exprDeltaMatF:  exprRecurseCurrent : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + this.ExtDVExprYIDeltaDVEB[cyear][j]);
				System.out.println("** 1>>>exprDeltaMatF:  DispExprRecurseCurrent : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + this.ExtDVDispExprYIDeltaDVEB[cyear][j]);
			}
			
			return;
		} //-- ExtDVExprDeltaMatF --//
		private void ExtDVExprRecurseCurrentEBWithDVF(int cyear, int numInstr) {
			if (cyear<1) return;
			
			//this.ExtDVExprYIDeltaDVEBAddYearly= new String[numYear][1];
			//this.ExtDVDispExprYIDeltaDVEBAddYearly=new String[numYear][1];
			
			ExtDVExprDeltaMatF(cyear, numInstr);
			
			for (int j=0; j<numInstr; j++) {
				
				//--- iterate through instruments ---//
				if (j==0) { 
					this.ExtDVExprYIDeltaDVEBAddYearly[cyear][0] = this.ExtDVExprYIDeltaDVEB[cyear][j]; 
					this.ExtDVDispExprYIDeltaDVEBAddYearly[cyear][0] = this.ExtDVDispExprYIDeltaDVEB[cyear][j]; 
					System.out.println("**2 J == 0: ExtDVExprRecurseCurrentEBWithDVF>>> ExtDVDispExprYIDeltaDVEBAddYearly : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + this.ExtDVDispExprYIDeltaDVEBAddYearly[cyear][j]);
				} else {
					this.ExtDVExprYIDeltaDVEBAddYearly[cyear][0] += " + " + this.ExtDVExprYIDeltaDVEB[cyear][j];
					this.ExtDVDispExprYIDeltaDVEBAddYearly[cyear][0] += " + " + this.ExtDVDispExprYIDeltaDVEB[cyear][j];
					System.out.println("**2 J != 0: ExtDVExprRecurseCurrentEBWithDVF >>> ExtDVDispExprYIDeltaDVEBAddYearly : cInstr:" +j + "/cYear:" +cyear +" =recursiveExpr:" + this.ExtDVDispExprYIDeltaDVEBAddYearly[cyear][0]);
				}
				
			}
			return;     
		}
		
    	private String ExtDVInitExprEBWithDVF(int numYear, int numInstr){ //Duplicated -- standard //
			String exprEBDV="";
			//String expreRecurse ="";
			
			/*
			this.EBInstrYearly = new String[1][numYear * numInstr];
			this.YIEBInstrYearly = new String[numYear][numInstr];
			this.DispYIEBInstrYearly = new String[numYear][numInstr];
			
			this.ExprDVEB = new String[1][numYear * numInstr];
			this.InitExprDVEB = new String[1][numYear * numInstr];
			this.InitExprYIDVEB = new String[numYear][numInstr];
			this.DispInitExprDVEB = new String[1][numYear * numInstr];
			this.DispInitExprYIDVEB = new String[numYear][numInstr];
			*/
			
			////
			this.ExtDVExprYIDeltaDVEBAddYearly= new String[numYear][1];
			this.ExtDVDispExprYIDeltaDVEBAddYearly=new String[numYear][1];
			
			this.ExtDVExprYIDeltaDVEB = new String[numYear][numInstr];
			for (int j=0; j<numInstr; j++) this.ExtDVExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			this.ExtDVDispExprYIDeltaDVEB = new String[numYear][numInstr];
			for (int j=0; j<numInstr; j++) this.ExtDVDispExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			//
			this.ExprYIDeltaDVEBAddYearly= new String[numYear][1];
			this.DispExprYIDeltaDVEBAddYearly=new String[numYear][1];
			
			this.ExprYIDeltaDVEB = new String[numYear][numInstr];
			for (int j=0; j<numInstr; j++) this.ExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			this.DispExprYIDeltaDVEB = new String[numYear][numInstr];
			for (int j=0; j<numInstr; j++) this.DispExprYIDeltaDVEB[0][j]="?NullDeltaMat?";
			
			
			ExtDVgetExprExtDVF(numYear,numInstr); // access ExprYIDV //
			ExtDVgetExprExtEBDVF(numYear, numInstr); //-- Access Extended EB -- //
			
			for (int i=0; i<numYear; i++) {
				
				int shStartIdx= (i*numInstr);
				
				for (int j=0; j<numInstr; j++) {
					// -- for each DV Ratio --//
					
    				if (j==0) ExtDVExprRecurseCurrentEBWithDVF(i,numInstr);
					
    				//////////
    				//---Start: attach EBYearlyOpt with DV ---//
    				//exprEBDV = dvExpr + " * EBYearopt(cYear)";
    				//System.out.println(" EXPR exprEBWithDV: " + exprEBDV );
    				//System.out.println(" EXPR exprEBWithDV# ( DVarsBond(" + (shStartIdx + j) + ")/ sum( DVarsBond( " +shStartIdx + ":" + shEndIdx  +") ) * EBYearopt(" + i +")" );
    				if (i==0) { 
    					
    					//exprEBDV = this.ExprYIDV[i][j]  + " * (" + this.EBYearly[i][0] +" )";
    					//exprEBDV =  this.EBYearly[i][0]  + " + ( "+ this.ExtDVExprYIDV[i][j]  + " * "+ ExtDVInitExprYIDVEB[i][j] + " )  "; 
    					exprEBDV = "( " + this.EBYearly[i][0]  + " ) + ( " + this.ExtDVExprYIDV[i][j]  + " * "+ ExtDVInitExprYIDVEB[i][j] + " )   ";
    					
						this.ExtDVInitExprDVEB[0][shStartIdx+j] = exprEBDV;
						this.ExtDVInitExprYIDVEB[i][j] = exprEBDV;
						
						
						//String dispExprEBDV = this.DispEBYearly[i][0]  + " + ( "+ this.ExtDVDispExprYIDV[i][j]  + " * "+ ExtDVDispInitExprYIDVEB[i][j] + " )  "; //// Wrong recursive ---
						String dispExprEBDV = "( " + this.DispEBYearly[i][0]  + " ) + ( "+ this.ExtDVExprYIDV[i][j] + " * "+ ExtDVDispInitExprYIDVEB[i][j] + " )  "; //// Wrong recursive ---
						
						this.ExtDVDispInitExprYIDVEB[i][j] = dispExprEBDV;
						this.ExtDVDispInitExprDVEB[0][shStartIdx+j] = dispExprEBDV;
						//System.out.println("** CHK--- 1st YEAR:" + dispExprEBDV + " CHK-DispEBYearly=" + this.DispEBYearly[i][0]);
						
						//System.out.println("**3- 1st YEAR: ExtDVExprRecurseEBDV - recursive expr:cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVInitExprYIDVEB[i][j]);
						System.out.println("**3- ***>>> 1st YEAR: ExtDVDispexprRecurseEBDV - recursive expr:cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVDispInitExprYIDVEB[i][j]);
						
					} 
    				else { 
    					
    					System.out.println("**3 CHK- 2nd YEAR ON: exprRecurseEBDV :cInstr:" +j + "/cYear:" + i );
						//exprEBDV = this.ExprYIDV[i][j]  + " * ( " +  this.EBYearly[i][0] + " + " + ExprYIDeltaDVEBAddYearly[i][0] + " )";
    					exprEBDV = "( " + this.EBYearly[i][0]  + " ) + ( "+ this.ExtDVExprYIDeltaDVEB[i][j]  + " )   "; //---- ExtDVExprYIDeltaDVEBAddYearly[i][0]
    					
						this.ExtDVInitExprYIDVEB[i][j] = exprEBDV;   
						this.ExtDVInitExprDVEB[0][shStartIdx+j] = exprEBDV;
						
						
												
						String dispExprEBDV = "( " + this.DispEBYearly[i][0] + " ) + ( "+ this.ExtDVDispExprYIDeltaDVEB[i][j]  + " )   "; //  --- //ExtDVDispExprYIDeltaDVEBAddYearly[i][0]
						this.ExtDVDispInitExprYIDVEB[i][j] = dispExprEBDV;
						this.ExtDVDispInitExprDVEB[0][shStartIdx+j] = dispExprEBDV;
						
						//System.out.println("** 3- 2nd YEAR ON: ExtDVExprRecurseEBDV :cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVInitExprYIDVEB[i][j]);
						System.out.println("**3- ***>>> 2nd YEAR ON: ExtDVDispExprRecurseEBDV :cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVDispInitExprYIDVEB[i][0]);
						
					}
    				
				} //-- for each Instr--//
				
			} //-- for each year--//
			
			return exprEBDV;
		}
    	///////////////////////////////////////////////////////////
    	
		
		///////////////////////////////////////////////////////////
    	private void ExtDVbuildExtConstraint(int numYear, int numInstr){
    		String exprEBDV="";
    		
    		this.ExtDVExtConstraint = new String[1][numYear * numInstr];
    		this.ExtDVDispExtConstraint = new String[1][numYear * numInstr];
    			
    		for (int i=0; i<numYear; i++) {
    				
    			int shStartIdx= (i*numInstr);
    				
    			for (int j=0; j<numInstr; j++) {
    				// -- for each DV Ratio --//
    				
    				exprEBDV =  " (" + this.ExtDVInitExprYIDVEB[i][j] +" ) == 0 ";
    				this.ExtDVExtConstraint[0][shStartIdx + j] = exprEBDV;
					//System.out.println("**5- Constraint: exprRecurseEBDV - recursive expr:cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVExtConstraint[0][shStartIdx+j]);
				
					String dispExprEBDV = " (" + this.ExtDVDispInitExprYIDVEB[i][j] +" ) == 0";
					this.ExtDVDispExtConstraint[0][shStartIdx+j] = dispExprEBDV;
					System.out.println("**5- ***>>> Constraint: DispexprRecurseEBDV - recursive expr:cInstr:" +j + "/cYear:" +i +" =Expr:" + this.ExtDVDispExtConstraint[0][shStartIdx+j]);
					
				} //-- for each Instr--//
				
			} //-- for each year--//
			
			return;
		}
    	///////////////////////////////////////////////////////////
    	private void ExtDVBuildNetEigenBestandDV(){
			String exprDVNet="";
			
			//-- YIEBInstrYearly--;  
			for (int i=0, cnt=0; i<this.ExtDVExprYIDV.length; i++ ) { // this.EBInstrYearly[i][j] = exprEBIT;
				for (int j=0; j<this.ExtDVExprYIDV[0].length; j++ ) { 
					jom.setInputParameter("cYear", i);
					jom.setInputParameter("cInstr", j);
					jom.setInputParameter("cYearInstr", i+j);
					// -- OLD STANDARD -- exprDVNet="(   ( DVarsBond(0,cYearInstr) ) - (" + this.EBInstrYearly[i][j] +" )   )";
					
					exprDVNet="(  DVarsBond(0,cYearInstr)  -  ( DVarsBond(0,cYearInstr) * ExtEBDVarsBond(0,cYearInstr) )   )"; ///ExtEBDVarsBond
					
					jom.setInputParameter("cntBondNetExp", cnt);
					//jom.setInputParameter("DVVarsBondNetExp(0,cntBondNetExp)", exprDVNet);
										
					this.ExtDVDVarsBondNetExp[cnt++][0] =exprDVNet;
					
					System.out.println(" >>>>> *** 10: ***  EXPR buildNetEigenBestandDV  Year/Instr:"+ i + "/" + j + " = "+ exprDVNet);
				}
			}
			//System.out.println(" EXPR buildNetEigenBestandDV = "+ exprDVNet);
		}
    	private void ExtDVBuildSumDVVarsBondNetExpF() {
			this.ExtDVSumDVVarsBondNetExp = new String[1][1];
			this.ExtDVSumDVVarsBondNetExp[0][0]  = this.ExtDVDVarsBondNetExp[0][0] ;
			for (int i=1; i<this.ExtDVDVarsBondNetExp.length; i++ ) {
				this.ExtDVSumDVVarsBondNetExp[0][0] += " + " + this.ExtDVDVarsBondNetExp[i][0] ;
			}
			this.ExtDVSumDVVarsBondNetExp[0][0]  = "sum ( " + this.ExtDVSumDVVarsBondNetExp[0][0] + " )";
		}
    	///////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	public void ojbfunc_optimize_BondsProto_ExtendedDV_TestMain(optimSolveFunctApp appMainObj ) {
    	
    		System.out.println(" ### Extended DV ####");
    		
    		ojbfunc_optimize_BondsProto_setupEnv(appMainObj);
    		int numDV= this.numInstr * this.numYear ; // // 4 instruments x 5 years = 20
			jom.setInputParameter("numDV", numDV);
			
			jom.addDecisionVariable("ExtDVarsBond", false, new int[] { 1, numDV } );	
			jom.addDecisionVariable("ExtEBDVarsBond", false, new int[] { 1, numDV } );
			jom.addDecisionVariable("DVarsBond", false, new int[] { 1, numDV } );	
			
			//jom.addDecisionVariable("DVVarsBondNetExp", false, new int[] { 1, numDV } );
			jom.addDecisionVariable("decisionVars", false, new int[] { 1, numDV } );	
			System.out.println(" BONDs Proto-JOM-Env ... with numDV = " + numDV);
				
			ojbfunc_optimize_BondsProto_Eigenbestand();
			ojbfunc_optimize_BondsProto_BondsBkt();
			
			
			//--Init Extended DV --//
			ExtDVInitExprEBWithDVF(numYear,numInstr);
			
			if (false) return;
			
			// Build NettoEigenBestand Matrix with DV
			this.ExtDVDVarsBondNetExp = new String[numDV][1];
			ExtDVBuildNetEigenBestandDV();
			
			//-- Expr of Objective Function --- sum of final expr -- SumDVVarsBondNetExp --//
			ExtDVBuildSumDVVarsBondNetExpF();
			
			//-- Expr Constraints --//
			ExtDVbuildExtConstraint(numYear,numInstr);
			
			//--setup constraints  --//
			jom.addConstraint("ExtDVarsBond == DVarsBond");
			jom.addConstraint("decisionVars == DVarsBond");
			jom.addConstraint(" DVarsBond >= 1.0");
			jom.addConstraint(" DVarsBond <= 1000.0");
			
			if (true) {
				//jom.addConstraint(this.ExtDVExtConstraint[0][0]);
				String SumExtDVConstraint = this.ExtDVExtConstraint[0][0];
				for (int i=0; i<numYear; i++) { 
	    			int shStartIdx= (i*numInstr); 
	    			for (int j=0; j<numInstr; j++) {
	    				SumExtDVConstraint += " + " + this.ExtDVExtConstraint[0][shStartIdx+j];
	    				jom.addConstraint(this.ExtDVExtConstraint[0][shStartIdx + j]);
	    			}
				}
			}
			
			//************* ---- DEBUG ISSSUE ------*****//
			String[][] fExprOF = new String[numDV][1];
			for (int m=0; m< 2; m++) {
				jom.setInputParameter("cntBondsNetExp", m);
				fExprOF[m][0]=  this.ExtDVDVarsBondNetExp[m][0] ;
				System.out.println(" Expr : " + fExprOF[m][0] );
				if (m<0) {
					System.out.println(" Expr eval: cYearInstr= " + jom.parseExpression("cYearInstr").evaluate() );
					System.out.print(" : cDVYearlyInstrShiftCurrIdx= " + jom.parseExpression("cDVYearlyInstrShiftCurrIdx").evaluate() );
					System.out.print(" : cDVYearlyShiftStartIdx= " + jom.parseExpression("cDVYearlyShiftStartIdx").evaluateConstant() ); 
					System.out.print(" : cDVYearlyShiftEndIdx= " + jom.parseExpression("cDVYearlyShiftEndIdx").evaluateConstant() );
				}
			}			
			//-- Some other tests ---//
			if (true) {
				System.out.println("###########++TREE  Recursive Test --- Later on  +#############");
				//testToyProto(1,3);
			} //---else for second year on--//
			//************* ---- DEBUG ISSSUE ------*****//
			
			//-- Objective Function  --//
			//jom.setObjectiveFunction("minimize",  this.DVVarsBondNetExp[numDV-1][0]  );
			jom.setObjectiveFunction("minimize",  this.ExtDVSumDVVarsBondNetExp[0][0]  );
			
			
			//--- Some more prototyping ---//
			//buildOptimDetlaMatrix();
			
			
			//--- Run Solver ---//
			//functUtil.solveObjectiveFunction(jom);
			
			
			//--- Display Result ---//
			//----System.out.println(" *** Objective Function *** " + jom.parseExpression("decisionVars").evaluate());
			//functUtil.printOptimalResult(jom);
			
			
			
			
			
			System.out.println ("** ExtDV - BONDS PROTO  OVER! ***"); 
    	}
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    	//-- Test Alt Mult --//
    	
    	
    	//-- Build data-set for testing --//
   	
		//-- test issues of non-linear optimization --//
		public void ojbfunc_optimize_BondsProto_Eigenbestand() {
			
			// 5 years:rows  data for 4 instruments:cols // 
			
			double[][] EBGEW2= {  
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{5.0d,5.2,5.3,5.4}
			};
			double[][] EBGEW= {  
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{5.2}
			};
			double[][] EBALT2= {  
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{5.0d,5.2,5.3,5.4}
			};
			double[][] EBALT= {  
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{5.2}
			};
			double[][] EDELTA2= {  
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4}
			};
			double[][] EDELTA= {  
					{1.2},
					{2.3}, 
					{3.4}, 
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{1.2},
					{2.2}, 
					{3.2}, 
					{4.2},
					{4.5},
					
			};
						
			this.EBYearly = new double[this.numYear][1];
			this.DispEBYearly = new String[this.numYear][1];
			
			for (int i=0; i <this.numYear; i++) {
				if (i==0) { 
					this.EBYearly[i][0] = EBGEW[i][0] + EBALT[i][0];
					
					DispEBYearly[i][0] = "EBGEW[" + i +"][" + 0 +"] + EBALT[" + i +"][" + 0 +"]";
				}
				else {
					//this.EBYearly[i][0] = EBGEW[i][0] + EBALT[i][0] + EBYearly[i-1][0] * EDELTA[i-1][0];
					this.EBYearly[i][0] = EBGEW[i][0] + EBALT[i][0] ;
					
					//DispEBYearly[i][0] = "this.EBYearly[" + i +"][" + 0 +"]";
					DispEBYearly[i][0] = "EBGEW[" + i +"][" + 0 +"] + EBALT[" + i +"][" + 0 +"]";
				}
				
				System.out.println(" EB Yearly: this.EBYearly:i" + i + " = Val:" + this.EBYearly[i][0] );			
				System.out.println(" Disp EB Yearly: " + this.DispEBYearly[i][0] );
			}
			
			
			this.jom.setInputParameter("EBGEWopt", new DoubleMatrixND(EBGEW));
			this.jom.setInputParameter("EBALTopt", new DoubleMatrixND(EBALT));
			this.jom.setInputParameter("EDELTAopt", new DoubleMatrixND(EDELTA));
			this.jom.setInputParameter("EBYearopt", new DoubleMatrixND(EBYearly));
			
			
			
			//double [][]EBDELTA;
			//Exp1:  fro i=1: EBDELTA(i,y) = DV1(y) * (EBGEW(i,y) + EBALT(i,y) );
			
			//Exp2: EBDELTA(2) = DV2 * ((EBGEW(i) + EBALT(i) + EBDELTA(i,y) )
			
			//exprEB[i] =  " EBGEWopt " + [i];
			
		}	//--BondsProto_Eigenbestand--//	
		
		
		
		//-- test issues of non-linear optimization --//
		public void ojbfunc_optimize_BondsProto_BondsBkt() {
			double[][] BondsBaukasten= {  
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{5.0d,5.2,5.3,5.4},
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{10.0d,5.2,5.3,5.4},
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{15.0d,5.2,5.3,5.4},
					{1.0d,1.2,1.3,1.4},
					{2.0d,2.2,2.3,2.4}, 
					{3.0d,3.2,3.3,3.4}, 
					{4.0d,4.2,4.3,4.4},
					{20.0d,5.2,5.3,5.4}	
			};
			
			this.jom.setInputParameter("BondsBbktOpt", new DoubleMatrixND(BondsBaukasten));
			
			//Exp1: Matrix structure:  DVNET = DV(i,y) - EBDELTA(i,Y)
			
			//Exp2: DVNET * BondsBaukasten; 
		}		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
		//--- Tools utility--/
		public int EB_Builder(int n) {
			double[][] EBGEW = (double[][]) this.jom.getInputParameter("EBGEWopt").toArray(); 
			double[][] EBALT = (double[][]) this.jom.getInputParameter("EBALTopt").toArray(); 
			double[][] EBDELTA = (double[][]) this.jom.getInputParameter("EDELTAopt").toArray();
			//double[][] EBGEW = this.jom.setInputParameter("EBYearopt", new DoubleMatrixND(EBYear))
			
		    if(n <= 1) {
		    	EBYearly[n][0] = EBGEW[n][0] + EBALT[n][0];
		        return n;
		    } else {
		    	this.EBYearly[n][0] = EBGEW[n][0] + EBALT[n][0] + ( this.EBYearly[n-1][0] * EBDELTA[n-1][0] );
		        return EB_Builder(n - 1) ;
		    }
		}
		
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
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		//--- Prototypes, Troubleshooting, Test & Validation ---//
		//public void ojbfunc_optimize_BondsProto_BondsBkt(optimSolveFunctApp appMainObj, int numYears, boolean addDKF, double addDKFForYears )
    	private void refCodeJOM() {
    		int numDV= this.numInstr * this.numYear ; // // 4 instruments x 5 years = 20
			jom.setInputParameter("numDV", numDV);
			jom.addDecisionVariable("decisionVars", false, new int[] { 1, numDV } );			
			
			System.out.println(" BONDs Proto-JOM-Env ... with numDV = " + numDV);

			double[][] covar= {  
					{1.0d,2,3,4},
					{2.0d,2,3,4}, 
					{3.0d,3,3,4}, 
					{4.0d,4,4,4},
					{5.0d,4,4,5}
			};
			
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
			
			String gCTExpr = " sum( (  (sum( (decisionVars * sensiOP),1))  .*    (sum( (decisionVars * sensiOP),1) * covarOP) ),2)"; //--Good--//
			
			com.jom.Expression jgtexpr = jom.parseExpression(gCTExpr);			
			ojbfunc_optimize_dispDVExpr(gCTExpr, "GT-Proto-JOM-Constraint-Non-Linear");
			System.out.println(" GT- Proto-JOM-Constraint-Non-Linear:  "+ jgtexpr.evaluate());
			//double[][] LimitOpt = { {12, 13, 14, 15, 16, 17} }; //new double[1][3];
			double[][] LimitOpt = { {10}, {20}, {30}, {40}, {50}, {60}  }; //new double[1][3];
			jom.setInputParameter("LimitOp", new DoubleMatrixND(LimitOpt));
			jom.addConstraint(gCTExpr + " <= LimitOp");	
						
			

			//Other Constraints//
			jom.addConstraint(" decisionVars^2  <= 60");			
			//jom.addConstraint(" sum((decisionVars')^2, 1)  <= 10");
			
			
			//--- Decision Variables ---/ 
			
			ojbfunc_optimize_dispDVExpr("decisionVars", "DVTest-1");		
			System.out.println(" *** Objective Function *** " + jom.parseExpression("decisionVars").evaluate());
						
			//jom.setObjectiveFunction("minimize",  " sum( (decisionVars * decisionVars'),2) " ); //-- //Works
			//jom.setObjectiveFunction("minimize",  " (decisionVars * decisionVars') " ); //-- //Works
			jom.setObjectiveFunction("minimize",  "sum(  sum( sum( (decisionVars) *  sensiOP ,1),2) )");
			
			//("minimize",  "sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )" );
			
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
			
		
			
			System.out.println ("** TRY BONDS PROTO:  Objective Function ** : " + jom.getObjectiveFunction().getModel().toString() + " # Evaluate : " + jom.getObjectiveFunction().toString() );
			functUtil.solveObjectiveFunction(jom, false);
			functUtil.printOptimalResult(jom);
			System.out.println ("** BONDS PROTO  OVER! ***"); 
			//jom.setInputParameter("DVTest", "decisionVars^2 ");
			//double [][] DVTestArr= (double[][]) jom.getInputParameter("DVTest").toArray();						
			//this.oxl.genTxtFileFrom2DData(filepath, "DVTestArr.txt", DVTestArr);
    		
    	}
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    	
    	// --- Bonds - Instruments Lists ---//
		private void applyJOMEyeMatrix() {
						
			int[] sizev = (int[]) this.jom.parseExpression("CalcSwapSensiRisk").size();
			
			this.jom.setInputParameter("nRowdiagSwapSensi", sizev[0]  );
			System.out.println(this.jom.parseExpression("nRowdiagSwapSensi").size() );			
			
			this.jom.setInputParameter("diagSwapSensiOpt","  CalcSwapSensiRisk .* eye(nRowdiagSwapSensi)" );
			double [][] diagSwapSensiOptArr= (double[][]) jom.getInputParameter("diagSwapSensiOpt").toArray();						
			this.oxl.genTxtFileFrom2DData(filepath, "diagSwapSensiOptArr.txt", diagSwapSensiOptArr);
			
		}
			
		// --- Bonds - Instruments Lists ---//

        
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	
	
    