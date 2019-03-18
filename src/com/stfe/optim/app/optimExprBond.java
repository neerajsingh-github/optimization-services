
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





//--- Apache commons math--//
import org.apache.commons.math3.fitting.*;
import org.apache.commons.math3.linear.*;

/*
//cern colt //
import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.function.tdouble.*;
import cern.colt.function.*;
import cern.colt.matrix.*;
import cern.colt.matrix.tdouble.DoubleFactory2D;
*/

import com.stfe.optim.config.initOptimAppConfig;
import com.stfe.optim.util.*;
import com.stfe.optim.excel.*;

    public class optimExprBond {
    	
    	private String filepath;
    	private createOXLFromIBean oxl;
    	
    	private boolean initAppRule=false;
    	private boolean initJOMDV=false;
    	private boolean initTensor=false;
    	
    	private OptimizationProblem jom=null;
    	private optimSolveDataset dataSet=null;
		private optimSolveRuleConfig ruleConf=null;	
		private optimSolveFunctUtil functUtil=null;
		private optimSolveFunctApp appMain=null;
		private int numYearOpti=51;
		
		private boolean  OnConstBondFinLuecke = true;
		private boolean  OnConstBondEigenBestand = true;
		private boolean  OnConstBondSwapFxRisk = true;
		private boolean  OnConstBondVol = true;
				
		private String test_fileIdent = "Init_";
		
		//TX Transform Bond Delta EB //
		public double[][][] bondsRVData3DTXSelect;
		public double[][] Delta2DTXSelect;
		
		//-- Bond Expr Specific --//
		private double[][] julDeltaYearPeriod; // julDeltaYearPeriod
		private String[][] strDeltaYearPeriod; // strDeltaYearPeriod
		private String[][] issueMatYearForAllInstr;
		private String[][] reportIssueMatYearForAllInstr;
		
		private double[][][] deltaMatrixInstr; 
		private double[][][] deltaMatrixInstrEff;
		
		private double[][][] deltaTwiddleMatrixInstr;
		private double[][][] deltaTwiddleMatrixInstrEff;
		
		private double[][][] deltaHeadMatrixInstr;
		private double[][][] deltaHeadMatrixInstrEff;
		private double[][][] deltaMatrixInstrAllIn; 
		
		private double[][][] deltaMatrixInstrTM; 
		private double[][][] deltaHeadMatrixInstrTM;
		private double[][][] deltaMatrixInstrAllInTM; 
		
		private double[][][][] BondDeltaBkstTensor4DTM;
		private double[][][][] BondDeltaBkstTensor4DTMEff;
		
		
		private double[][][] unitMatrixDelta;
		private double[][] unitMatrixDVBond;
		private double[][] matDiagNumYearRedArr;
		private double[][] matColVecNumYearRedArr;
		private double[][] matColVecMonthsNumYearRedArr;
		
		private double[][] EV_Data_EBAuslaufend;
		
		private String NetBondDV;
		private String BondDeltaBkstTensor_BondRV_Opt; 
		private String BondDeltaBkstTensor_BondFx_Opt;
		private String BondDeltaBkstTensor_BondPVClean_Opt;
		private String BondDeltaBkstTensor_BondDebtClean_Opt;
		
		
		private double UpLimitBondVol[][]; 
		private double DnLimitBondVol[][];
				
		//-- Instrument Exclude List ---//
		private boolean excludedInstrFlag=false;
		
		private final String[] excludeInstrFromDeltaMatrix={"_VILB_", "_BUBILL","_103055","_103056","_103057" };
		//private final String[] excludeInstrFromDeltaMatrix={"_TTTTT_"  };
	 
		//"BONDS PROGRAM 2017_103055", "BONDS PROGRAM 2017_103056", "BONDS PROGRAM 2017_103057","BONDS PROGRAM 2018_103055", "BONDS PROGRAM 2018_103056", "BONDS PROGRAM 2018_103057",
		//"BONDS PROGRAM 2019_103055", "BONDS PROGRAM 2019_103056", "BONDS PROGRAM 2019_103057","BONDS PROGRAM 2020_103055", "BONDS PROGRAM 2020_103056", "BONDS PROGRAM 2020_103057" };
		
		
		
    	// --- Validation  ---//    	
    	//-- Stream out the values of core functions for validation - do not invoke Optimizer --//
    	// --- Validation SRV Data ---//
		public void ojbfunc_optimize_rule_optimExprBond(optimSolveFunctApp appMainObj) 	{
			
			this.initAppRule=false;
			
			if (appMainObj == null) {
				System.out.println(" ???? AppMainObj NULL - Can not procedd further ! ????");
				return;
			}
    	
			this.appMain = appMainObj;
    		this.oxl =  appMainObj.oxl;
    		this.filepath = appMainObj.filepath;
    		this.functUtil=appMainObj.functUtil;
    		this.ruleConf=appMainObj.ruleConf;
			this.numYearOpti = appMainObj.ruleConf.numDecisionVariables;
			this.jom = appMainObj.jom;
			
    		if (appMainObj.dataSet == null) {
    			this.appMain.objfunc_optimize_extractEffData();
    		}
    		
			//this.oxl.genTxtFileFrom2DData(filepath, "VALIDAT- AppMainObjdataSet.TransformedBondWeightKey-Arr.txt", appMainObj.dataSet.TransformedBondWeightKey);
			
    		
    		this.dataSet=appMainObj.dataSet;
    		this.dataSet.optimLoadEffectiveBondDataSetupEnv_TranformMatrix();
    		
			this.initAppRule=true;
    	}
    	///////////////////////////////////////////////////////////////////////////////
    	
    	
    	
    	private void displayDeltaDatesPeriod() {
    		System.out.println(" displayDatesPeriod nYear: " + this.dataSet.jix_EV_Data_Year[0].length );
    		for (int i=0; i< this.dataSet.jix_EV_Data_Year[0].length; i++) {
    			System.out.println(" displayDatesPeriod StartDate : Fmt / Jul : " +" Year="+ i + " : "+ this.strDeltaYearPeriod[0][i] +" / " + this.julDeltaYearPeriod[0][i] 
    						+ " EndDate : Fmt / Jul : " + this.strDeltaYearPeriod[1][i] +" / " + this.julDeltaYearPeriod[1][i]);    		
    			//System.out.println(" storeDatePeriod EndDate : Fmt / Jul : " + this.strDeltaYearPeriod[i][1] +" / " + this.julDeltaYearPeriod[i][1] );    		
    		}
    	}
    	
    	
    	private void storeDeltaDatesPeriod() {
    		//this.julDeltaYearPeriod = new double[this.dataSet.jix_EV_Data_Year[0].length][2]; // -- 51 Years = this.dataSet.jix_EV_Data_Year[0].length--//
    		//this.strDeltaYearPeriod = new String[this.dataSet.jix_EV_Data_Year[0].length][2];
    		this.julDeltaYearPeriod = new double[2][this.dataSet.jix_EV_Data_Year[0].length]; // -- 51 Years = this.dataSet.jix_EV_Data_Year[0].length--//
    		this.strDeltaYearPeriod = new String[2][this.dataSet.jix_EV_Data_Year[0].length];
    		
    		for (int i=0, scnt=0, ecnt=0; i < this.dataSet.bondsRVDataRaw3D.length; i++ ) {
    		//for (int i=0, scnt=0, ecnt=0; i < this.dataSet.jix_EV_Data_Year[0].length; i++ ) {
    			// if date contains 31.02.xxx store as startDate -- if 31.12.20xx store as EndDate
    			//-- Use "31.03" as startDate, as for many years, the date starts from 31.03 --//
    			int idxStartDate = com.stfe.optim.util.optimHandleString.getIdxContainStringInArray( this.dataSet.bondsRVFmtDateRaw2D[i], "31.03.");
    			if (idxStartDate != -1) {
    				this.julDeltaYearPeriod[0][scnt] = this.dataSet.bondsRVJulDateRaw2D[i][idxStartDate];
    				this.strDeltaYearPeriod[0][scnt] = this.dataSet.bondsRVFmtDateRaw2D[i][idxStartDate];
    				//System.out.println(" storeDeltaDatesPeriod - idxStart : MonthlyTime " + i + " idxStart" + idxStartDate );
    				scnt++;
    			}
    			int idxEndDate = com.stfe.optim.util.optimHandleString.getIdxContainStringInArray( this.dataSet.bondsRVFmtDateRaw2D[i], "31.12.");
    			if (idxEndDate != -1) {
    				this.strDeltaYearPeriod[1][ecnt] = this.dataSet.bondsRVFmtDateRaw2D[i][idxEndDate];
    				this.julDeltaYearPeriod[1][ecnt] = this.dataSet.bondsRVJulDateRaw2D[i][idxEndDate];
    				//System.out.println(" storeDeltaDatesPeriod - idxEnd : MonthlyTime " + i + " idxEnd" + idxEndDate );
        			ecnt++;
    			}
    		}
    		//displayDeltaDatesPeriod();
    	} // -- storeDatePeriod --//
    	
    	
    	
    	private boolean validateBaukastenRawArray() {
    		boolean inValid = false;
    		if (this.dataSet.bondsRVFmtDateRaw.length !=  this.dataSet.bondsRVInstrRaw.length)  
    			return inValid;
    		if (this.dataSet.bondsRVFmtDateRaw.length !=  this.dataSet.bondsRVDataRaw.length)  
    			return inValid;
    		
    		return true;
    	}
    	
    	
    	
    	public int[] getIdxArrayMatchDateWithFmtDateRaw(String[][] bdRVFmtDateRaw, String strDeltaDecYearPeriod) {
    		
    		int[] idxArrRet;
    	
    		int[] idxArr = new int[bdRVFmtDateRaw.length] ;
    		String[] dateArr = new String[bdRVFmtDateRaw.length];
    		for (int k=0; k<bdRVFmtDateRaw.length; k++) {
    			dateArr[k] = bdRVFmtDateRaw[k][0];
    		}
    		
    		int cntArr=0;
			for (int i=0;i<dateArr.length;i++) {
			    if (dateArr[i].contains(strDeltaDecYearPeriod)) { // --- Subs indexOf, equal --//
			    	idxArr[cntArr++] = i;
			    }
			}
						
			idxArrRet = new int[cntArr] ;
			for (int i=0;i<idxArrRet.length;i++) 
				idxArrRet[i]= idxArr[i];
			
    		return idxArrRet;
    	}
    	
    	    	
    	public int getIdxMatchInstrWith2DInstrArrInd(String[][] bondsRVInstrRaw, int[] idxArrayBkstForDecDates, String Instr ) {
    		int idx=-1;
    		    		
    		for (int i=0;i<bondsRVInstrRaw.length;i++) {
			    if (bondsRVInstrRaw[idxArrayBkstForDecDates[i]][0].contains(Instr)) { // --- Subs indexOf, equal --//
			    	idx = idxArrayBkstForDecDates[i];
			    	break;
			    }
			}
    		return idx;
    	}
    	
    	
    	
    	
    	private  void calculateDeltaTwiddleForBonds(double[][] deltaTwiddleArr) {
    		//double[][] deltaTwiddleArr = new double[51][51]; //-- this.numYearOpti --//
    		for (int dtrow=0; dtrow<deltaTwiddleArr.length; dtrow++) { // --- Row represents the issuance --//
    			for (int dtcol=0; dtcol<deltaTwiddleArr.length; dtcol++) { // -- col represents the maturity ---///
    				deltaTwiddleArr[dtrow][dtcol]=0;
    				if (dtrow == dtcol)
    					deltaTwiddleArr[dtrow][dtcol]=-1;
    					
    			}
    		}
    	} //-- DeltaTwiddle Matrix Over---///
    	
    	
    	//private void calculateDeltaHeadForBonds(double[][] deltaHeadArr, double[][] delatArr) {
    	private void calculateDeltaHeadForBonds(double[][] deltaHeadArr, double[][] delatArr, double[][] delatTwiddleArr) {
    		
    		//int DltDim=this.ruleConf.numYears;
    		//int DltDim=51;
    		int DltDim = this.dataSet.jix_EV_Data_Year[0].length;
    		
    		if (  (deltaHeadArr.length != DltDim ) ||(deltaHeadArr[0].length != DltDim ) ){
    			return;
    		}
    		if (  (delatArr.length != DltDim ) ||(delatArr[0].length != DltDim ) ){
    			return;
    		}
    		
    		//double[][] deltaTwiddle = new double[DltDim][DltDim];
    		//calculateDeltaTwiddleForBonds(deltaTwiddle);
    		
    		for (int dtrow=0; dtrow<deltaHeadArr.length; dtrow++) { // --- Row represents the issuance --//
    			for (int dtcol=0; dtcol<deltaHeadArr.length; dtcol++) { // -- col represents the maturity ---///
    				deltaHeadArr[dtrow][dtcol]=0;
    				deltaHeadArr[dtrow][dtcol]=delatArr[dtrow][dtcol] + delatTwiddleArr[dtrow][dtcol];
    			}
    		}
    	}//-- DeltaHead Matrix Over---///

    	
    	
    	//private void buildBondsExpDS(double[][] delatArr, String Instr,  int idxInstr) {
    	private void calculateDeltaForBonds(double[][] delatArr, String Instr,  int idxInstr) {
    		//this.bondsRVDataRaw; 	bondsRVDateRaw; bondsRVInstrRaw; bondsRVJulDateRaw //
    		// public String[][] bondsRVDateRaw2D; public String[][] bondsRVInstrRaw2D; public double[][] bondsRVJulDateRaw2D;
    		// Final DS - YearIssue-YearMat; String[][]  issueMatYearForAllInstr = newString[][]; 
    		
    		boolean flagIssue=false, flagMat=false;
    		int idxIssueYearDelta=-1, idxMatYearDelta=-1;
    		int idxBkstForInstr=-1;
    		
    		boolean debug=false;
    		for (int yearCnt=0; yearCnt<this.strDeltaYearPeriod[1].length; yearCnt++)  { 
    			
    			//-- Find for julDeltaEndMatch match in a big array -- returns an array of 1428 indices - Use this indices to find the Instr --// 
    			//int[] idxArrayBkstForDecDates = com.stfe.optim.util.optimHandleString.getIdxArrayMatchDateWithFmtDateRaw( this.dataSet.bondsRVFmtDateRaw, this.strDeltaYearPeriod[1][yearCnt]);
    			int[] idxArrayBkstForDecDates = getIdxArrayMatchDateWithFmtDateRaw( this.dataSet.bondsRVFmtDateRaw, this.strDeltaYearPeriod[1][yearCnt]);
    			if (debug) {
    				System.out.println("+++buildBondsExpDS: idxArrayBkstForDecDates length: " + idxArrayBkstForDecDates.length + " // array elements1,2,n: "+ idxArrayBkstForDecDates[0] +":"+ idxArrayBkstForDecDates[1]+ ":" + idxArrayBkstForDecDates[idxArrayBkstForDecDates.length-1]);
    			}	
    			// --- Search  Instruments from supplied indices to find the correct match for Instr  ###//
    			//idxBkstForInstr = com.stfe.optim.util.optimHandleString.getIdxMatchInstrWith2DInstrArrInd( this.dataSet.bondsRVInstrRaw, idxArrayBkstForDecDates, Instr );
    			idxBkstForInstr = getIdxMatchInstrWith2DInstrArrInd( this.dataSet.bondsRVInstrRaw, idxArrayBkstForDecDates, Instr );
    			if (debug) {
    				System.out.println("buildBondsExpDS: idxBkstForInstr :" + idxBkstForInstr + " Instrumemt: " + Instr);
    				System.out.println("buildBondsExpDS: Check Data : " + this.strDeltaYearPeriod[1][yearCnt] + " Instrumemt: " + Instr + " : Debt/Nom-PVClean:"+ this.dataSet.bondsRVDataRaw[idxBkstForInstr][0] +":"+ this.dataSet.bondsRVDataRaw[idxBkstForInstr][2]);
    			}
    			
    			//-- find the actual debt for this idxdateInstr from bondsRVDataRaw --- Logic: when non-zero debt starts and when non-zero debt ends---//
    			if (flagIssue==false && flagMat==false) {
	        		if 	(this.dataSet.bondsRVDataRaw[idxBkstForInstr][0] > 0.000000001) { // --- check non-zero Debt --//
	        			flagIssue=true;
	        			issueMatYearForAllInstr[1][idxInstr]=this.strDeltaYearPeriod[1][yearCnt];
	        			idxIssueYearDelta=yearCnt;
	        			String issueOut = String.format("++buildBondsExpDS: IssueDate Found : " + this.strDeltaYearPeriod[1][yearCnt] + " Instrumemt: " + Instr + " : Debt/Nom-PVClean: %f : %f #", this.dataSet.bondsRVDataRaw[idxBkstForInstr][0] , this.dataSet.bondsRVDataRaw[idxBkstForInstr][2]);
	        			this.reportIssueMatYearForAllInstr[idxInstr][1]= issueOut;
	        			//System.out.println("<<buildBondsExpDS: IssueDate Found : " + this.strDeltaYearPeriod[1][yearCnt] + " Instrumemt: " + Instr + " : Debt/Nom-PVClean:"+ this.dataSet.bondsRVDataRaw[idxBkstForInstr][0] +":"+ this.dataSet.bondsRVDataRaw[idxBkstForInstr][2]);
	        			continue;
	        		}
    			}
    			if (flagIssue==true && flagMat==false) {
	        		if 	(this.dataSet.bondsRVDataRaw[idxBkstForInstr][0] == 0.0) {  // --- check non-zero Debt --//
	        			flagMat=true;
	        			issueMatYearForAllInstr[2][idxInstr]=this.strDeltaYearPeriod[1][yearCnt];
	        			idxMatYearDelta=yearCnt;
	        			String matOut = String.format("--buildBondsExpDS: MaturityDate: " + this.strDeltaYearPeriod[1][yearCnt] + " Instrumemt: " + Instr + " : Debt/Nom-PVClean: %f :  %f #", this.dataSet.bondsRVDataRaw[idxBkstForInstr][0] , this.dataSet.bondsRVDataRaw[idxBkstForInstr][2]);
	        			this.reportIssueMatYearForAllInstr[idxInstr][2]= matOut;
	        			//System.out.println(">>buildBondsExpDS: MaturityDate Found : " + this.strDeltaYearPeriod[1][yearCnt] + " Instrumemt: " + Instr + " : Debt/Nom-PVClean:"+ this.dataSet.bondsRVDataRaw[idxBkstForInstr][0]+":"+ this.dataSet.bondsRVDataRaw[idxBkstForInstr][2]);
	        			break;
	        		}
    			}
    		} //--- done for each dec date ---//
    		//--- short term Instr for issue and mat in same year ---//
    		if (flagIssue==true && flagMat==false) {
    			//issueMatYearForAllInstr[2][idxInstr]=issueMatYearForAllInstr[1][idxInstr];
    			issueMatYearForAllInstr[2][idxInstr]="31.12.2099";
    			flagMat=true;
    			String matissOut = String.format("**buildBondsExpDS: MaturityDate extended to: " + issueMatYearForAllInstr[2][idxInstr] + " Instrumemt: " + Instr + " : Debt/Nom-PVClean: %f :  %f #", this.dataSet.bondsRVDataRaw[idxBkstForInstr][0] , this.dataSet.bondsRVDataRaw[idxBkstForInstr][2]);
    			this.reportIssueMatYearForAllInstr[idxInstr][2]= matissOut;
    			//System.out.println("*** buildBondsExpDS: MaturityDate extended to: " + issueMatYearForAllInstr[2][idxInstr] + " Instrumemt: " + Instr); 
    			// If instrument matures after the last timepoint of timegrid  //---- set to 31.12.2099 ---//
    		}
    		if (flagIssue==false && flagMat==false) {
    			System.out.println("??? buildBondsExpDS: IssueDate Not found for Instrumemt: " + Instr);
    			String missOut = String.format("??? buildBondsExpDS: IssueDate & MaturityDate Not found for Instrumemt : " + Instr);
    			this.reportIssueMatYearForAllInstr[idxInstr][1]= missOut;
    			this.reportIssueMatYearForAllInstr[idxInstr][2]= missOut;
    		}
    		
    		if (this.excludedInstrFlag)
    			this.reportIssueMatYearForAllInstr[idxInstr][2] += " #Valid ExcludedList#";
    		
    		//-- DeltaMatrix: find the actual debt for this idxdateInstr from bondsRVDataRaw --//
    		//--- Build Delta-Matrix for the Instr ---//
    		for (int dtrow=0; dtrow<delatArr.length; dtrow++) { // --- Row represents the issuance --//
    			for (int dtcol=0; dtcol<delatArr.length; dtcol++) { // -- col represents the maturity ---///
    				
    				delatArr[dtrow][dtcol]=0;
    				
    				//if ( (dtrow == idxIssueYearDelta) && (dtcol == idxMatYearDelta) && (!this.excludedInstrFlag) ) //  deltaMatrixInstrAllIn does not work ; deltaMatrixInstr only works ; 
    				if ( (dtrow == idxIssueYearDelta) && (dtcol == idxMatYearDelta)  )
    					delatArr[dtrow][dtcol]=1;
    					
    			}
    		} //-- Delta Matrix Over---///
    		
    		//-- Display Delta-Matrix for a specific Instrument --//
    		//String comprInstr =  com.stfe.optim.util.optimHandleString.compressString(Instr);
    		//this.oxl.genTxtFileFrom2DData(filepath, "DelatArr"+ comprInstr +".txt", delatArr);
    	}//-- done Delta Netrix for each Instr --//
    	
    	
    	
    	
    	private void excludedBondInstr(String instr) {
    		//private final String[] excludeInstrFromDeltaMatrix={"_VILB_", "_BUBILL","_103055","_103056","_103057" };
    		//private boolean excludedInstrFlag=false;
    		
    		this.excludedInstrFlag=false;
    		for (String instrPattern:this.excludeInstrFromDeltaMatrix) {
    			if  ( instr.contains(instrPattern) ) {
    				this.excludedInstrFlag=true;
			        //System.out.println( "The Instrument is found to be exccluded list! - " + instr +" // conatains the pattern: " + instrPattern);
			        break;
    			}
    		}
    	} // -- done excludedBondInstr --//
    	
    	
    	
    	
    	
		// --- Bonds - Instruments Lists ---//
		//private void ojbfunc_optimize_ProcessBondJOM() {}
		public void buildBondInstrsDeltaMatrices(optimSolveFunctApp appMainObj) {
			
			
			if (!this.initAppRule) ojbfunc_optimize_rule_optimExprBond(appMainObj); 
			
			// -- do for all instruments of bonds supplied by opti-conf --//
			// -- build a zero matrix --//
			//-- For each instrument in every 50 year, check the condition --//
			//-- Build the delta matrix for first instrument: <BONDS PROGRAM 2017_103055> --//
			System.out.println("Bond EB Delta Static -  NumElements: Year=" + appMainObj.dataSet.jix_EV_Data_Year[0].length );
			 
			this.numYearOpti = this.ruleConf.numYears; 
			//int numYear = collectBondsYearEBDeltaConfig();
			int numInstr = this.dataSet.jix_Config_BondProgramType.length;
			int numDV = this.ruleConf.numDecisionVariables;
			
			//-- Store Delta Date Periods --//
			storeDeltaDatesPeriod();
						
			if (!validateBaukastenRawArray()) {
				System.out.println("Invalid Data structure of dates, instr, and dkf!");
				return;
			}
			
			this.issueMatYearForAllInstr = new String[3][this.dataSet.jix_Config_BondProgramType.length] ; // --String[][] issueMatYearInstr--//
			this.reportIssueMatYearForAllInstr = new String[this.dataSet.jix_Config_BondProgramType.length][3] ;
			
			//-- Build Detla Matrices for each Instrument --//
			/*
			this.deltaMatrixInstrAllIn = new double[numInstr][this.ruleConf.numYears][this.ruleConf.numYears];
			this.deltaMatrixInstr = new double[numInstr][this.ruleConf.numYears][this.ruleConf.numYears];  // numYear ; numDV ; this.ruleConf.numDecisionVariables
			this.deltaHeadMatrixInstr = new double[numInstr][this.ruleConf.numYears][this.ruleConf.numYears];
			
			this.deltaMatrixInstrEff = new double[this.ruleConf.numDecisionVariables][this.ruleConf.numYears][this.ruleConf.numYears];
			this.deltaHeadMatrixInstrEff = new double[this.ruleConf.numDecisionVariables][this.ruleConf.numYears][this.ruleConf.numYears];
			*/
			
			this.deltaMatrixInstrAllIn = new double[numInstr][this.dataSet.jix_EV_Data_Year[0].length][this.dataSet.jix_EV_Data_Year[0].length]; //this.dataSet.jix_EV_Data_Year[0].length : [ 1x51] // 
			this.deltaMatrixInstr = new double[numInstr][this.dataSet.jix_EV_Data_Year[0].length][this.dataSet.jix_EV_Data_Year[0].length];  // 1428 x 51 x 51 //
			this.deltaTwiddleMatrixInstr=new double[numInstr][this.dataSet.jix_EV_Data_Year[0].length][this.dataSet.jix_EV_Data_Year[0].length]; // 1428 x 51 x 51 //
			this.deltaHeadMatrixInstr = new double[numInstr][this.dataSet.jix_EV_Data_Year[0].length][this.dataSet.jix_EV_Data_Year[0].length]; // 1428 x 51 x 51 //
						
			this.deltaMatrixInstrEff = new double[this.ruleConf.numDecisionVariables][this.dataSet.jix_EV_Data_Year[0].length][this.dataSet.jix_EV_Data_Year[0].length]; // numDV x 51 x 51 //
			this.deltaTwiddleMatrixInstrEff = new double[this.ruleConf.numDecisionVariables][this.dataSet.jix_EV_Data_Year[0].length][this.dataSet.jix_EV_Data_Year[0].length];
			this.deltaHeadMatrixInstrEff = new double[this.ruleConf.numDecisionVariables][this.dataSet.jix_EV_Data_Year[0].length][this.dataSet.jix_EV_Data_Year[0].length];
			
			boolean dummyTest=false;
			
			for (int i=0; i<numInstr; i++) {
			//for (int i=0; i<4; i++) {
				
				this.issueMatYearForAllInstr[0][i]= this.dataSet.jix_Config_BondProgramType[i][0];
				this.issueMatYearForAllInstr[1][i]= "00.00.0000";
				this.issueMatYearForAllInstr[2][i]= "11.11.1111";
				this.reportIssueMatYearForAllInstr[i][0]= this.dataSet.jix_Config_BondProgramType[i][0];
				excludedBondInstr(this.dataSet.jix_Config_BondProgramType[i][0]);
								
				if (!dummyTest) {
					calculateDeltaForBonds(this.deltaMatrixInstrAllIn[i], this.dataSet.jix_Config_BondProgramType[i][0], i);
					
					if (!this.excludedInstrFlag) {
						calculateDeltaForBonds(this.deltaMatrixInstr[i], this.dataSet.jix_Config_BondProgramType[i][0], i);
					}
					
					calculateDeltaTwiddleForBonds(this.deltaTwiddleMatrixInstr[i]);
					calculateDeltaHeadForBonds(this.deltaHeadMatrixInstr[i], this.deltaMatrixInstr[i], this.deltaTwiddleMatrixInstr[i]);
					
					if (i < this.ruleConf.numDecisionVariables) {
						this.deltaMatrixInstrEff[i] = this.deltaMatrixInstr[i];
						this.deltaTwiddleMatrixInstrEff[i] = this.deltaTwiddleMatrixInstr[i];
						this.deltaHeadMatrixInstrEff[i] = this.deltaHeadMatrixInstr[i];
					}
						
				}
				
				
				///////////////////////////
				if (dummyTest) {
					if (i<4) { 
						calculateDeltaForBonds(this.deltaMatrixInstrAllIn[i], this.dataSet.jix_Config_BondProgramType[i][0], i);
						calculateDeltaForBonds(deltaMatrixInstr[i], this.dataSet.jix_Config_BondProgramType[i][0], i);
						calculateDeltaTwiddleForBonds(deltaTwiddleMatrixInstr[i]);
						calculateDeltaHeadForBonds(deltaHeadMatrixInstr[i], deltaMatrixInstr[i], deltaTwiddleMatrixInstr[i]);
						if (i < this.ruleConf.numDecisionVariables)
							this.deltaHeadMatrixInstrEff[i] = this.deltaHeadMatrixInstr[i];
						
					} else {
						deltaMatrixInstrAllIn[i] = deltaMatrixInstrAllIn[2];
						deltaMatrixInstr[i] = deltaMatrixInstr[2];
						deltaTwiddleMatrixInstr[i] = deltaTwiddleMatrixInstr[2];
						deltaHeadMatrixInstr[i] = deltaHeadMatrixInstr[2];
						if (i < this.ruleConf.numDecisionVariables)
							this.deltaHeadMatrixInstrEff[i] = this.deltaHeadMatrixInstr[i];
						
					}
				} // -- dummyTest done --///
				///////////////////////////
								
				if (i<13) {
					String comprInstr =  com.stfe.optim.util.optimHandleString.compressString(this.dataSet.jix_Config_BondProgramType[i][0]);
					String numVal= Integer.toString(i);
					this.oxl.genTxtFileFrom2DData(filepath, "DelatMatrixArrAllIn-"+  comprInstr + "-" + numVal + ".txt", deltaMatrixInstrAllIn[i]);
		    		this.oxl.genTxtFileFrom2DData(filepath, "DelatMatrixArr-"+  comprInstr + "-" + numVal + ".txt", deltaMatrixInstr[i]);
		    		this.oxl.genTxtFileFrom2DData(filepath, "DelatMatrixHeadArr-" + comprInstr + "-" + numVal + ".txt", deltaHeadMatrixInstr[i]);
				}
				
				
				if (i<0) {
					String idx = Integer.toString(i+1);
					String deltaName =  com.stfe.optim.util.optimHandleString.compressString(idx+"_DeltaMat_"+ this.dataSet.jix_Config_BondProgramType[i][0]);  // com.stfe.optim.util.optimHandleString.compressString()
					System.out.println("BondInstrIdx: "+ deltaName);
				}
				
			} //-- done for loop --//
			
			this.oxl.genTxtFileFrom2DStringData(filepath, "IssueMatYearForAllInstr.txt", this.issueMatYearForAllInstr);
			this.oxl.genTxtFileFrom2DStringData(filepath, "ReportInstrIssueMatYearForDeltaMatrix.txt", this.reportIssueMatYearForAllInstr);
			
		} // --- buildBondInstrsDeltaMatrices ---//
		///////////////////////////////////////////////////////////////////////////////
		
		///////////////////////////////////////////////////////////////////////////////
		private void buildReducedDeltaBaukasten() {
			
			
		}// --- buildReducedDeltaBaukasten ---//
		///////////////////////////////////////////////////////////////////////////////
		
		
		
    	//--Collect Time-Dimension-Year, + EBDelta List from Config -- returns num Years ---//
		private int collectBondsYearEBDeltaConfig() {
			System.out.println("Bond EB Delta Static -  NumElements: this.jix_Config_BondProgramType=" + this.dataSet.jix_Config_BondProgramType.length ); // [ 1x51]
			System.out.println("Bond EB Delta Static -  NumElements: Year=" + this.dataSet.jix_EV_Data_Year[0].length ); // [ 1x51]
			System.out.println("Bond EB Delta Static -  NumElements: jix_EV_Data_NKB=" + this.dataSet.jix_EV_Data_NKB[0].length ); // [ 1x51]
			System.out.println("Bond EB Delta Static -  NumElements: jix_EV_Data_ChangedEB=" + this.dataSet.jix_EV_Data_ChangedEB[0].length ); // [ 1x51]
			System.out.println("Bond EB Delta Static -  NumElements: jix_EV_Data_NetTilgung=" + this.dataSet.jix_EV_Data_NetTilgung[0].length );// [ 1x51]
			
			System.out.println("Bond Transformation Matrix NumYear: =" + this.numYearOpti );// 10 years or numDV supplied in config file --//
			return this.dataSet.jix_EV_Data_Year[0].length ;
		}
		

    	
		
		private void collectBondsBaukasten() {
			
			System.out.println("Bonds baukasten: BondsBKSRawDates" + this.dataSet.bondsRVDateRaw.length  + " // BondsBKSRawData:" + this.dataSet.bondsRVDataRaw.length);
			
			int tx_numBRVRawDates= optimArrayListUtil.sizeUniqueArr2D(this.dataSet.bondsRVJulDateRaw);
    		int tx_numBRVRawRowsEachDate=optimArrayListUtil.numRedundantData2D(this.dataSet.bondsRVJulDateRaw, this.dataSet.bondsRVJulDateRaw[0][0]);
    		System.out.println("+++ 3D-Restructured:  BOND tx_numSRVRawDates : "+ tx_numBRVRawDates  +" --- tx_numSRVRawRowsEachDate:" + tx_numBRVRawRowsEachDate);
    		
    		System.out.println("Validate Dates Structure:  // Double bondsRVJulDateRaw : " + this.dataSet.bondsRVJulDateRaw.length  +" // String bondsRVDateRaw : " + this.dataSet.bondsRVDateRaw.length);
			
			System.out.println("Bonds Bauskasten Transformed -  bondsRVDataRaw3D:[Tab][Row][Col]: " + this.dataSet.bondsRVDataRaw3D.length + " / " + 
									this.dataSet.bondsRVDataRaw3D[0].length  + " / "+ this.dataSet.bondsRVDataRaw3D[0][0].length);
			
		}
		
		
		///////////////////////////////////////////////////////////////////////////////
		private void applyJOMEyeMatrix() {
			
			double[][] arrData = new double[51][51]; //--- int DltDim=this.ruleConf.numDecisionVariables; ---//
			this.jom.setInputParameter("arrData",new DoubleMatrixND(arrData) );
			
			int[] sizev = (int[]) this.jom.parseExpression("CalcSwapSensiRisk").size();
			
			this.jom.setInputParameter("nRowdiagSwapSensi", sizev[0]  );
			System.out.println(this.jom.parseExpression("nRowdiagSwapSensi").size() );	
			
			this.jom.setInputParameter("diagSwapSensiOpt","  CalcSwapSensiRisk .* eye(nRowdiagSwapSensi)" );
			double [][] diagSwapSensiOptArr= (double[][]) jom.getInputParameter("diagSwapSensiOpt").toArray();						
			this.oxl.genTxtFileFrom2DData(filepath, "diagSwapSensiOptArr.txt", diagSwapSensiOptArr);
			
		}
		///////////////////////////////////////////////////////////////////////////////	
		///////////////////////////////////////////////////////////////////////////////  
		///////////////////////////////////////////////////////////////////////////////
		
			public void ojbfunc_optimize_loadBondExprJOM() {
				//--- setup JOM --//
				
				jom.setInputParameter("NumYear", this.ruleConf.numYears );
				jom.setInputParameter("NumYearIdx", (this.ruleConf.numYears - 1) );
				jom.setInputParameter("NumDVIdx", (this.ruleConf.numDecisionVariables - 1) );
				
				
				//this.dataSet.jix_EV_Data_Year =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.dataSet.jix_EV_Data_Year, -1, -1, 0, (this.ruleConf.numYears-1) );
				jom.setInputParameter("EBYearly", new DoubleMatrixND(this.dataSet.jix_EV_Data_Year )  );
				
				//this.dataSet.jix_EV_Data_NKB =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.dataSet.jix_EV_Data_NKB, -1, -1, 0, (this.ruleConf.numYears-1) );
				jom.setInputParameter("NKBYearly", new DoubleMatrixND(this.dataSet.jix_EV_Data_NKB )  );
				jom.setInputParameter("NKBYearly", "NKBYearly * 1E6"  );
				double [][] NKBYearlyArr= (double[][]) jom.parseExpression("NKBYearly").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "NKBYearlyArr.txt", NKBYearlyArr );
				
				//this.dataSet.jix_EV_Data_ChangedEB =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.dataSet.jix_EV_Data_ChangedEB, -1, -1, 0, (this.ruleConf.numYears-1) );
				jom.setInputParameter("DeltaEBGwsYearly", new DoubleMatrixND(this.dataSet.jix_EV_Data_ChangedEB )  );
				jom.setInputParameter("DeltaEBGwsYearly", "DeltaEBGwsYearly * 1E6"  );
				double [][] DeltaEBGwsYearlyArr= (double[][]) jom.parseExpression("DeltaEBGwsYearly").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "DeltaEBGwsYearlyArr.txt", DeltaEBGwsYearlyArr );
				
				
				//this.dataSet.jix_EV_Data_NetTilgung =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.dataSet.jix_EV_Data_NetTilgung, -1, -1, 0, (this.ruleConf.numYears-1) );
				jom.setInputParameter("BruttoTilgungYearly", new DoubleMatrixND(this.dataSet.jix_EV_Data_NetTilgung )  );
				this.oxl.genTxtFileFrom2DData(filepath, "BruttoTilgungYearlyArr.txt", this.dataSet.jix_EV_Data_NetTilgung );
								

				//--Eigenbestand_Auslaufend Ea = (BrutoTilgung) - (Debt-Clean from BM_PLANCURRENT) --//
				this.EV_Data_EBAuslaufend = new double[1][this.ruleConf.numYears];
				double [][] DeltaDebtClean = new double[1][(this.ruleConf.numYears)];
				for (int i=0, decYearDebtIdx=0, prevDecYearDebtIdx=0; i<this.ruleConf.numYears; i++ ) {
					
					decYearDebtIdx=( (i+1) * 12) - 1;
					prevDecYearDebtIdx=( i * 12) - 1;
					
					if (i==0) {
						///--- REF Data : Year 2016 Dec Debt: 1093370162540,01 ---///
						DeltaDebtClean[0][i] = this.dataSet.SchuldEff[decYearDebtIdx][0] - 1093370162540.01;  //this.julDeltaYearPeriod[];	 // Dec.2016_Debt_Clean= 1.344199E+12  ;  Correct data 1093370162540.01//
						System.out.println(" Delta Debt Calc : Year 2017: Final / Initial = " + this.dataSet.SchuldEff[decYearDebtIdx][0]  + " // " + 1093370162540.01   );
					}
					else { 
						DeltaDebtClean[0][i] = this.dataSet.SchuldEff[decYearDebtIdx][0] - this.dataSet.SchuldEff[prevDecYearDebtIdx][0]; // Year 2016 Dec Debt: 1093370162540.01

						if (i<3 || i==this.ruleConf.numYears-1)
							System.out.println(" Delta Debt Calc : Year " + (2017+i) +  " :Final // Initial = " +  this.dataSet.SchuldEff[decYearDebtIdx][0]  + " //-// " + this.dataSet.SchuldEff[prevDecYearDebtIdx][0] );
					}
					
					//--- take mod of DeltaDebtClean --//
					/*
					if (DeltaDebtClean[0][i] < 0)  {
						//System.out.println(" ??? WARNING: EV_Data_EBAuslaufend can not be negative : "+ this.EV_Data_EBAuslaufend[0][i]  + " // NumYear= "+ (i+1) );
						DeltaDebtClean[0][i] = (-1) * DeltaDebtClean[0][i];
					}
					*/
				
					
					// Differenece NetTigung and DebtClean-BMPC ; and convert it to billion from Euro;
					this.EV_Data_EBAuslaufend[0][i] = (this.dataSet.jix_EV_Data_NetTilgung[0][i] -  DeltaDebtClean[0][i]) * (0.000000001);
					//--- do not take mod of EV_Data_EBAuslaufend --//
					if (this.EV_Data_EBAuslaufend[0][i] < 0)  {
							System.out.println(" ??? WARNING: EV_Data_EBAuslaufend can not be negative : "+ this.EV_Data_EBAuslaufend[0][i]  + " // NumYear= "+ (i+1) );
							//this.EV_Data_EBAuslaufend[0][i] = (-1) * this.EV_Data_EBAuslaufend[0][i];
					}
					
					
					/*
					/////////////////////////////////////////
					/////////////For Test, setup EBAuslaufend as 0 for year 2017 and as 5 billion for next years..///
					//-- just set for test purpose --//
					if (i==0) this.EV_Data_EBAuslaufend[0][i] =0.0; //--for year 2017--//
					else this.EV_Data_EBAuslaufend[0][i] =5.0; 
					//-- just set for test purpose --//
					/////////////////////////////////////////
					*/
				}
				
				
				
				
				jom.setInputParameter("EBAuslaufendYearly", new DoubleMatrixND(this.EV_Data_EBAuslaufend )  );
				this.oxl.genTxtFileFrom2DData(filepath, "EBAuslaufendYearlyArr.txt", this.EV_Data_EBAuslaufend);
				
				jom.setInputParameter("DeltaDebtCleanBMPCYearly", new DoubleMatrixND(DeltaDebtClean )  );
				this.oxl.genTxtFileFrom2DData(filepath, "DeltaDebtCleanBMPCArr.txt", DeltaDebtClean);
				
				
				jom.setInputParameter("TransformedBondWeightKey", new DoubleMatrixND(this.dataSet.TransformedBondWeightKey)  );
								
				//this.dataSet.bondsRVDataTransformed3D; // --- bondsRVDataTransformed3D Tab=120 -- ROW=70 --- Col: 79 ---//
	    		//this.dataSet.bondsRVDataRaw3D; //--- 292 (monthly )x 1428 (Instr) x 79 (dkf)    ---//
				
				jom.setInputParameter("EBAuslaufendYearlyNumYear", "EBAuslaufendYearly(all, 0:NumYearIdx)"  );
				
				jom.setInputParameter("BondsRVDataTransformed3D", new DoubleMatrixND(this.dataSet.bondsRVDataTransformed3D )  );  //-- [120x70x79] --//
				jom.setInputParameter("BondsRVDataTransformed3D", " permute(BondsRVDataTransformed3D,[2;1;3]) "); //--- restructured [70x120x79] ---
				
				this.jom.setInputParameter("BondFxRV2DOpt","BondsRV2DAnnSensiOpt(all,all,0:30)" );
				
				//-- DeltaMatrixInstrEff --//
				jom.setInputParameter("DeltaMatrixInstrAllIn", new DoubleMatrixND(this.deltaMatrixInstrAllIn )  ); // --- [70, 51, 51] --//
				jom.setInputParameter("DeltaMatrixInstrAllInNumYear", "DeltaMatrixInstrAllIn(all, 0:NumYearIdx, 0:NumYearIdx)" ); // --- [70, 10, 10] --//				
				
				jom.setInputParameter("DeltaMatrixInstr", new DoubleMatrixND(this.deltaMatrixInstr )  ); // --- [1428, 51, 51] --//
				jom.setInputParameter("DeltaMatrixInstrNumYear", "DeltaMatrixInstr(all, 0:NumYearIdx, 0:NumYearIdx)"  ); // --- [1428, 10, 10] --//
				

				jom.setInputParameter("DeltaMatrixInstrEff", new DoubleMatrixND(this.deltaMatrixInstrEff )  ); // --- [70, 10, 10] --//
				jom.setInputParameter("DeltaMatrixInstrEff", "permute(DeltaMatrixInstrEff,[2;1;3])"  ); // --- [51, 70, 51] --//
				jom.setInputParameter("DeltaMatrixInstrEffNumYear", "DeltaMatrixInstrEff(0:NumYearIdx, all, 0:NumYearIdx)");	
				
				//----jom.setInputParameter("DeltaMatrixInstrRawTNS", "permute(DeltaMatrixInstr,[1;3;2])"  );// --- [1428, 51, 51] --//
				
				
				//-- DeltaTwiddleMatrixInstrEff --//
				jom.setInputParameter("DeltaTwiddleMatrixInstr", new DoubleMatrixND(this.deltaTwiddleMatrixInstr )  );
				jom.setInputParameter("DeltaTwiddleMatrixInstr",   "permute(DeltaTwiddleMatrixInstr,[2;1;3])");  // --- [51, 70, 51] --//
				jom.setInputParameter("DeltaTwiddleMatrixInstrNumYear","DeltaTwiddleMatrixInstr(0:NumYearIdx, all, 0:NumYearIdx)");// --- [10, 70, 10] --//
				
				jom.setInputParameter("DeltaTwiddleMatrixInstrEff", new DoubleMatrixND(this.deltaTwiddleMatrixInstrEff )  );
				jom.setInputParameter("DeltaTwiddleMatrixInstrEff",   "permute(DeltaTwiddleMatrixInstrEff,[2;1;3])");  // --- [10, 70, 10] --//
				jom.setInputParameter("DeltaTwiddleMatrixInstrEffNumYear", "DeltaTwiddleMatrixInstrEff(0:NumYearIdx, all, 0:NumYearIdx)");			
				
				jom.setInputParameter("DeltaTwiddleMatrixInstrRaw", new DoubleMatrixND(this.deltaTwiddleMatrixInstr )  );// --- [70, 51, 51] --//
				jom.setInputParameter("DeltaTwiddleMatrixInstrRawTNS", "permute(DeltaTwiddleMatrixInstrRaw,[1;3;2])"  );
				jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYear", "DeltaTwiddleMatrixInstrRaw(all,0:NumYearIdx, 0:NumYearIdx)");
				jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYearTNS", "permute(DeltaTwiddleMatrixInstrRawNumYear,[1;3;2])");
				
				
				//-- DeltaHeadMatrixInstrEff --//
				jom.setInputParameter("DeltaHeadMatrixInstrRaw", new DoubleMatrixND(this.deltaHeadMatrixInstr )  );// --- [70, 51, 51] --//
				jom.setInputParameter("DeltaHeadMatrixInstrRawNumYear", "DeltaHeadMatrixInstrRaw(all,0:NumYearIdx, 0:NumYearIdx)");
								
				jom.setInputParameter("DeltaHeadMatrixInstr", new DoubleMatrixND(this.deltaHeadMatrixInstr )  );
				jom.setInputParameter("DeltaHeadMatrixInstr",   "permute(DeltaHeadMatrixInstr,[2;1;3])");  // --- [51, 70, 51] --//
				jom.setInputParameter("DeltaHeadMatrixInstrNumYear","DeltaHeadMatrixInstr(0:NumYearIdx, all, 0:NumYearIdx)");// --- [10, 70, 10] --//
				
				//-- DeltaHeadMatrixInstrEff --//
				jom.setInputParameter("DeltaHeadMatrixInstrEff", new DoubleMatrixND(this.deltaHeadMatrixInstrEff )  );
				jom.setInputParameter("DeltaHeadMatrixInstrEff",   "permute(DeltaHeadMatrixInstrEff,[2;1;3])");  // --- [10, 70, 10] --//
				jom.setInputParameter("DeltaHeadMatrixInstrEffNumYear", "DeltaHeadMatrixInstrEff(0:NumYearIdx, all, 0:NumYearIdx)");			
				
				
				//jom.setInputParameter("UnitMatrixDelta",   "ones([70;10;10])");
				//jom.setInputParameter("UnitMatrixDVBond",   "ones([1;70])");
				this.unitMatrixDVBond = new double [1][this.ruleConf.numDecisionVariables]; // //--deltaHeadMatrixInstr.length
				this.unitMatrixDelta = new double [deltaHeadMatrixInstr.length][deltaHeadMatrixInstr[0].length][deltaHeadMatrixInstr[0][0].length]; //--- [70, 10, 10] --//
				for (int i=0; i<deltaHeadMatrixInstr.length ; i++) {
					
					if (i<this.ruleConf.numDecisionVariables)
						unitMatrixDVBond[0][i] = 1.0;
					
					for (int j=0; j<deltaHeadMatrixInstr[0].length ; j++) {
						for (int k=0; k<deltaHeadMatrixInstr[0][0].length ; k++) {
							unitMatrixDelta[i][j][k]=1.0;
						}
					}
				} // --- unit matrix delta--//
				jom.setInputParameter("UnitMatrixDVBond",   new DoubleMatrixND(this.unitMatrixDVBond));
				jom.setInputParameter("UnitMatrixDelta",   new DoubleMatrixND(this.unitMatrixDelta));
				
				//--Test-- MatrixA, MatrixB //
			}
			///////////////////////////////////////////////////////
			
			
			
			//////////////////////////  Issue - does not work! ////////////////////////////
			public String  ojbfunc_optimize_formBondSwapNetDV() {
				
				boolean activeNetDV = false;
				/**/
				//String bondExpr =  " sum( (EBDVBond * DeltaHeadMatrixInstrEff(0:NumYearIdx, all, 0:NumYearIdx )), 3)  "; //-- DeltaHead reduced from [51,70,51] to [10, 70, 10]--// //-- [1,10] - [1,10] * [[10x70x10]] = 1,70,10
				
				//---  Use DeltaMatrixInstr ; Do not use DeltaHeadMatrix or DeltaTwiddleMatrix --//
				
				//--- ("DeltaHeadMatrixInstrEff",  initiated with [NumDV, 51, 51] // [DVInstr, AllYear, AllYear] ==> permuted to [51, NumDV, 51]//[AllYear, DVInstr, AllYear] ---//
				/*
				jom.setInputParameter("DeltaTwiddleMatrixEBDV", "DeltaTwiddleMatrixInstrEff(0:NumYearIdx, all,  0:NumYearIdx)"  ); //-- [NumYear, NumDV, 51]//[NumYear, DVInstr, NumYear] --//
				jom.setInputParameter("DeltaTwiddleMatrixEBDV"," permute(DeltaTwiddleMatrixEBDV, [1;2;3]) ");
				jom.setInputParameter("DeltaTwiddleTNSMatrixEBDV"," permute(DeltaTwiddleMatrixEBDV, [3;2;1]) ");
				*/
				String bondExpr;
				
				if (activeNetDV) {
					// -- DeltaMatrixInstrNumYear = -- [NumInstr, NumYear, NumYear] = 1428,10,10--//
					// -- DeltaMatrixInstrEffNumYear = -- [ NumYear,NumDV, NumYear] = 10, 70, 10 --//
					jom.setInputParameter("DeltaMatrixNetDV", "DeltaMatrixInstrEffNumYear"  ); //-- [NumYear, NumDV, NumYear] = 10, 70,10,--//
					jom.setInputParameter("DeltaMatrixNetDVTNS", "permute(DeltaMatrixInstrEffNumYear,[3;2;1])"  ); //-- [NumInstr, NumYear, NumYear] = 1428,10,10--//
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaMatrixNetDV", "ojbfunc_optimize_formBondSwapNetDV-DeltaMatrixNetDV");
					
					bondExpr =  " sum( (EBDVBond * DeltaMatrixNetDV), 3)  ";
									
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "ojbfunc_optimize_formBondSwapNetDV-ObjFunct111111");
					
					bondExpr = "( UnitMatrixDVBond - " + bondExpr + " ) "; //-- [1,70] - [1,70] * [[70x120x79]]
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "ojbfunc_optimize_formBondSwapNetDV-ObjFunct2");
					
					bondExpr =  " DVBond .* ( " +  bondExpr + " )";
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "ojbfunc_optimize_formBondSwapNetDV-ObjFunct3");
	
					//double [][] formBondSwapNetDVArr= (double[][]) jom.parseExpression(bondExpr).evaluate().toArray();
					//this.oxl.genTxtFileFrom2DDataFmt(filepath, "formBondSwapNetDV_DV0.txt", formBondSwapNetDVArr,10);
					
					this.NetBondDV = bondExpr;
				}
				
				//-- NetDV concept is not to be used anymore; replaced with new Tensor;  --//
				bondExpr =  " DVBond ";
				this.NetBondDV = bondExpr;
				
				return this.NetBondDV;
			}
			
			
			
			public String  ojbfunc_optimize_formBondSwapSchuldTotal() {
				//String DebtTotalVec = " sum( (  (sum( (DVSwap * SwapFxRV2DOpt),1) + sum( (DVBond * BondFxRV2DOpt),1) + SchuldFullOpt)  .*    (  (sum( (DVSwap * SwapFxRV2DOpt),1) + sum( (DVBond * BondFxRV2DOpt),1) + FxRiskFullOpt)  * PCCov_Mkt1_1_31) ),2)";
				
				//String PVTotalVec=     " ((PVCleanFullOpt) + (sum( (DVBond * BondsRV2DPVCleanOpt), 1)) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				
				//String SchuldTotalVec = " ((SchuldFullOpt) + (sum( (DVBond * BondsRV2DDebtCleanOpt), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				
				//--good--//String SchuldTotalVec = " ((SchuldFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" * BondsRV2DDebtCleanOpt), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				//String SchuldTotalVec = " ((SchuldFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" * " + this.BondDeltaBkstTensor_BondDebtClean_Opt + "), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				String SchuldTotalVec = " ((SchuldFullOpt) + (  " + this.BondDeltaBkstTensor_BondDebtClean_Opt + ") +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				
				
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DVBond", "ojbfunc_optimize_formBondSwapSchuldTotal-DVBond"); // 5 years --> [1, 35] //
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "SchuldBondTMOpt", "ojbfunc_optimize_formBondSwapSchuldTotal-SchuldBondTMOpt"); // 5 years --> [60, 1] //
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "SchuldSwapTMOpt", "ojbfunc_optimize_formBondSwapSchuldTotal-SchuldSwapTMOpt");
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "SchuldFullOpt", "ojbfunc_optimize_formBondSwapSchuldTotal-SchuldFullOpt"); // 5 years --> [60, 1] //
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalVec, "ojbfunc_optimize_formBondSwapSchuldTotal-SchuldTotalVec"); 
				
				double [][] formBondSwapSchuldTotalArr= (double[][]) jom.parseExpression(SchuldTotalVec).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "formBondSwapSchuldTotalArr", formBondSwapSchuldTotalArr,10);
				
				return SchuldTotalVec;
			}
			
			public String ojbfunc_optimize_formBondSwapPVTotal() {
				//String PVTotalVec= " ((PVCleanFullOpt) + (sum( (DVBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				
				//--good--// String PVTotalVec= " ((PVCleanFullOpt) + (sum( ("+ ojbfunc_optimize_formBondSwapNetDV() +" *  BondsRV2DPVCleanOpt), 1)) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				//String PVTotalVec= " ((PVCleanFullOpt) + (sum( ("+ ojbfunc_optimize_formBondSwapNetDV() +" *  " + this.BondDeltaBkstTensor_BondPVClean_Opt + "), 1)) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				
				String PVTotalVec= " ((PVCleanFullOpt) + (  " + this.BondDeltaBkstTensor_BondPVClean_Opt + "  ) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, PVTotalVec, "ojbfunc_optimize_formBondSwapPVTotal-PVTotalVec");
				double [][] formBondSwapPVTotalArr= (double[][]) jom.parseExpression(PVTotalVec).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "formBondSwapPVTotalArr", formBondSwapPVTotalArr,10);
				
				return PVTotalVec; 
			}
			
			public void objfunc_optimize_MatVecNumYearForReduction() {
				// build a matrix of dim [51,5], so that
				int numMonths= (this.ruleConf.numYears) * 12;
				this.matColVecMonthsNumYearRedArr = new double[numMonths][1];
				this.matColVecNumYearRedArr = new double[1][this.ruleConf.numYears];
				this.matDiagNumYearRedArr = new double[51][this.ruleConf.numYears];
				
				for (int i=0; i<51; i++) {
					for (int j=0; j<this.ruleConf.numYears; j++) {
						
						if (this.dataSet.initStartRefYear == 2018) {
							if (j==0) continue;
						}
						if (this.dataSet.initStartRefYear == 2019) {
							if (j<=1) continue;
						}
						if (i==j) this.matDiagNumYearRedArr[i][j]=1.0;
						else this.matDiagNumYearRedArr[i][j]=0.0; 
					}
				}
				this.jom.setInputParameter("MatrixDiagNumYearReduction", new DoubleMatrixND(this.matDiagNumYearRedArr) );
				
				for (int v=0; v<this.ruleConf.numYears; v++) {
					
					this.matColVecNumYearRedArr[0][v]=1.0;
					if (this.dataSet.initStartRefYear == 2018) {
						if (v==0) this.matColVecNumYearRedArr[0][v]=0.0;
					}
					if (this.dataSet.initStartRefYear == 2019) {
						if (v<=1) this.matColVecNumYearRedArr[0][v]=0.0;
					}
				}
				this.jom.setInputParameter("MatrixColVecNumYearReduction", new DoubleMatrixND(this.matColVecNumYearRedArr) );
				
				
				for (int m=0; m<numMonths; m++) {
					
					this.matColVecMonthsNumYearRedArr[m][0]=1.0;
				
					if (this.dataSet.initStartRefYear == 2018) {
						if (m<=11) this.matColVecMonthsNumYearRedArr[m][0]=0.0;
					}
					if (this.dataSet.initStartRefYear == 2019) {
						if (m<=23) this.matColVecMonthsNumYearRedArr[m][0]=0.0;
					}
				}
				this.jom.setInputParameter("MatrixColVecMonthsNumYearReduction", new DoubleMatrixND(this.matColVecMonthsNumYearRedArr) );
				
				
				
				//--Compress the matrix by selecting 12th value from data and eliminating the rest ..//
				//--A row vector where only 12th element is 1 and rest are 0 ; This row-vector is used for dot product with another row vector to enable the 12th element and disable others. --//
				// -- Init dec-2016 val (2016-Dec)  1093370162540.01 --//
				double [][] rowVec12EleON = new double[numMonths][1];
				double [][] rowVec12EleONPrevYear = new double[numMonths][1];
				double [][] rowVec12EleONPrevYearInit2016 = new double[1][this.ruleConf.numYears];
				for (int t=0, nyr=0; t<numMonths; t++) {
					
					rowVec12EleON[t][0]=0.0;
					rowVec12EleONPrevYear[t][0]=0.0;
					
					if ( (t % 12) == 11) { 
						
						rowVec12EleON[t][0]=1.0;
						rowVec12EleONPrevYear[t][0]=1.0;
						rowVec12EleONPrevYearInit2016[0][nyr]=0.0;
						
						if (t == 11) {
							//rowVec12EleONPrevYear[t][0]=0.0; //-- select also the 12th month in rowVec12EleONPrevYear --//
							rowVec12EleONPrevYearInit2016[0][nyr]=1093370162540.01;
						} 
						nyr++;
					} //-- done for each year --//
				} //-- done for all months point--//
				
				this.jom.setInputParameter("MatrixRowVecMonths12EleON", new DoubleMatrixND(rowVec12EleON) );
				this.jom.setInputParameter("MatrixRowVecMonths12EleONPrevYear", new DoubleMatrixND(rowVec12EleONPrevYear) );
				this.jom.setInputParameter("MatrixRowVecMonths12EleONPrevYearInit2016", new DoubleMatrixND(rowVec12EleONPrevYearInit2016) );
				
				//--compress the matrix by selecting 12th value from data and eliminating the rest ..//
				double [][] matSelect12EleOnRed = new double[this.ruleConf.numYears][numMonths];
				double [][] matSelect12EleOnRedPrevYear = new double[this.ruleConf.numYears][numMonths];
			
				for (int t=0; t<numMonths; t++) {
					for (int y=0; y<this.ruleConf.numYears; y++) {
						
						matSelect12EleOnRed[y][t]=0.0;
						matSelect12EleOnRedPrevYear[y][t]=0.0;
						
						int idxYear = ( (t) / 12) ;
						if ( ( (t+1) % 12) == 0) {
							
							if ( (idxYear > 0)   )
								matSelect12EleOnRedPrevYear[idxYear][t-12]=1.0;
								
							matSelect12EleOnRed[idxYear][t]=1.0;
						}
						
					}
				}
				this.jom.setInputParameter("MatrixMonthsYear12EleON", new DoubleMatrixND(matSelect12EleOnRed) );
				this.jom.setInputParameter("MatrixMonthsYear12EleONPrevYear", new DoubleMatrixND(matSelect12EleOnRedPrevYear) );
				
				double[][] MatrixRowVecMonths12EleONArr = (double[][]) jom.parseExpression("MatrixRowVecMonths12EleON").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "MatrixRowVecMonths12EleONArr.txt", MatrixRowVecMonths12EleONArr);
				double[][] MatrixRowVecMonths12EleONPrevYearArr = (double[][]) jom.parseExpression("MatrixRowVecMonths12EleONPrevYear").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "MatrixRowVecMonths12EleONPrevYearArr.txt", MatrixRowVecMonths12EleONPrevYearArr);
				double[][] MatrixRowVecMonths12EleONPrevYearInit2016Arr = (double[][]) jom.parseExpression("MatrixRowVecMonths12EleONPrevYearInit2016").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "MatrixRowVecMonths12EleONPrevYearInit2016Arr.txt", MatrixRowVecMonths12EleONPrevYearInit2016Arr);
				
				double[][] MatrixMonthsYear12EleONArr = (double[][]) jom.parseExpression("MatrixMonthsYear12EleON").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "MatrixMonthsYear12EleONArr.txt", MatrixMonthsYear12EleONArr);
				double[][] MatrixMonthsYear12EleONArrTNS = (double[][]) jom.parseExpression("MatrixMonthsYear12EleON'").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "MatrixMonthsYear12EleONArrTNS.txt", MatrixMonthsYear12EleONArrTNS);
				
				double[][] matSelect12EleOnRedPrevYearArr = (double[][]) jom.parseExpression("MatrixMonthsYear12EleONPrevYear").evaluate().toArray();
				this.oxl.genTxtFileFrom2DData(filepath, "MatrixMonthsYear12EleONPrevYearArr.txt", matSelect12EleOnRedPrevYearArr);
				
			} //--Helper Matrices for Reduction Done! --// 
			/////////////////////////////////////////////////////////////////////

			
			public void ojbfunc_optimize_formBondComplexExpr() {
				objfunc_optimize_MatVecNumYearForReduction();
				String NetBondDV = ojbfunc_optimize_formBondSwapNetDV();
				String SchuldTotalVec = ojbfunc_optimize_formBondSwapSchuldTotal();
				String PVTotalVec = ojbfunc_optimize_formBondSwapPVTotal();
				//jom.setInputParameter("NetBondDV",   NetBondDV ); //-- Can not be setup in JOM --//
				//jom.setInputParameter("SchuldTotalVec",  SchuldTotalVec );
				//jom.setInputParameter("PVTotalVec",   PVTotalVec );
			}
			/////////////////////////////////////////////////////////////////////
			
						
			/////////////////////////////////////////////
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
				
				if (rcVec==0) {
					expr = "sum ("  + expr + " , 1)"; // should be summed up along cols- 2//
				} else expr = "sum ("  + expr + " , 2)";
				
				return expr;
			}
			/////////////////////////////////////////////////////////////////////
			
			private String ojbfunc_optimize_replicate_VecSMat(String rowColVec, int VecDim, int rcVec){
				String expr;
				//-- replicate a rowVec along a square matrix : -- create a unit square matrix U; create a diagonal matrix D with rowVec along diagonal; matrix multiply D X U  --//
				String osMatrix = "ones(["+ String.valueOf(VecDim) + ";" + String.valueOf(VecDim) +"])" ;
				String diagMatrix = "diag("+ rowColVec + ")" ;
				
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
			
		
			
			
			/////////////////////////////////////////////////////////////////////
			private String ojbfunc_optimize_replicate_GenVecMatDim(String rowColVec, int VecDim, int rcVec, int OMatDimRow, int OMatDimCol, boolean display){
				
				String expr=""; 
				String fexpr="";
				int validDim=0;
				
				// If row of mat2 equals to vecDim, then proceed, else return; //
				if (VecDim == OMatDimRow)  
					validDim=1;
				else return fexpr;
							
				jom.setInputParameter("VecDim", VecDim );
				
				//-- replicate a rowVec along a square matrix : -- create a unit square matrix U; create a diagonal matrix D with rowVec along diagonal; matrix multiply D X U  --//
				String osMatrix = "ones(["+ String.valueOf(VecDim) + ";" + String.valueOf(VecDim) +"])" ;
				
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
				
				if (rcVec==0) {
					//-- create a square matrix - transpose for colVec and normal for rowVec --// 
					expr = diagMatrix + " * " + osMatrix;

					if (display) {
						this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_DiagMultOnes");
						double[][] diagMultOnesArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
						this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_DiagMultOnes.txt", diagMultOnesArr);
					}
					
					//-- create a rowcolvecMatrix of equal dim to mat2  --//
					if (OMatDimRow == VecDim)
						fexpr = expr + " * " + onesMatrixDim;
					if (OMatDimCol == VecDim)
						fexpr = onesMatrixDim + " * " + expr;
							
				} 
				if (rcVec==1) {
					expr = diagMatrix  + " * " + osMatrix;

					if (display) {
						this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_OnesMultDiag");
						double[][] onesMultDiagArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
						this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_OnesMultDiag.txt", onesMultDiagArr);
					}
					
					//-- create a rowcolvecMatrix of equal dim to mat2  --//
					if (OMatDimRow == VecDim)
						fexpr =  "("+ expr + ") * (" +  onesMatrixDim + ")";
					
					if (OMatDimCol == VecDim)
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
			/////////////////////////////////////////////////////////////////////
			
			//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
			private String ojbfunc_optimize_replicate_GenVecMatDim_3D(String rowColVec, int VecLen, int rowColVecFlag, int OMatDimTab, int OMatDimRow, int OMatDimCol, boolean display){
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
					expr = "(" + diagMatrix + ") * (" + osMatrix + ")"; // [3x3] * [3, 3] = [3, 3]  
					
					if (display) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "Replicate_VecSMat-expr_DiagMultOnes");
					double[][] diagMultOnesArr = (double[][]) jom.parseExpression(expr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Replicate_VecSMat-expr_DiagMultOnes.txt", diagMultOnesArr);
				}
					
					//-- create a rowcolvecMatrix of equal dim to mat2  --//
					if (OMatDimTab == VecLen)
						fexpr = "("+ expr + ") * (" + onesMatrixDim + ")"; // [3, 3] * [3, 7, 3] = [3, 7, 3]
					
				} 
					
				if (validDim==1) {
					fexpr = " (1/VecDim) * ("+ fexpr + ")";
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
				
				expr = "(" + vecOMat + ") .* (" + oMatrix + ")";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMatInOMatrix-exprDisp");
				
				if (display) {
					double[][][] dotVecmatOMatArr = (double[][][]) jom.parseExpression(expr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMat_0.txt", dotVecmatOMatArr[0]);
					this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMat_1.txt", dotVecmatOMatArr[1]);
				}
				
				if ( (rcVec==0) || (rcVec==1) ) {
					expr = "sum( ("  + expr + ") , 1)"; // should be summed up along cols- 2//
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, expr, "AltMatMult_VecSMatInOMatrixSum-exprDispFinal");
				} 
				
				if (display) {
					double[][] dotVecmatOMatArrFinal = (double[][]) jom.parseExpression(expr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DData(filepath, "Altmult_dotVecmatOMatFinal_0.txt", dotVecmatOMatArrFinal);
				}
				
				return expr;
			}
			//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
			
			
			
			/////////////////////////////////////////////////////////////////////
			
			/////////////////////////////////////////////////////////////////////
			private String ojbfunc_optimize_formBondFinLueckeConstraint_TheoreticalMtd(){
				// --- No NETDV should be used ---/
				// Phy = NKS + B + E +  Sum( Sum(  DVBond * DVBond  ) ) 
				// constraint:  Phy - Sum( Sum(  DELTA * DVBond ) ) == 0.0 ; //--DeltaMatrixInstrAllIn --/
				// -- NOTE: 30.Apr.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				
				String BondFinLckConst="";
				
				String vld1Phy0Expr =  " NKBYearly(all, 0:NumYearIdx) + DeltaDebtCleanBMPCYearly " ;
				double [][] vld1Phy0ExprArr= (double[][]) jom.parseExpression(vld1Phy0Expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-VLD1PHY0expr-NKB-DeltaDebtCleanBMPCYearly.txt", vld1Phy0ExprArr,10);				
				
				
				//String phy1Expr = " ( NKBYearly * 1000000.0 ) + (DeltaEBGwsYearly * 1000000.0 ) + BruttoTilgungYearly ";
				String phy1Expr = " NKBYearly + DeltaEBGwsYearly + BruttoTilgungYearly ";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, phy1Expr, "phy1Expr-Constraint-Disp1");
				double [][] phyExpr1FinLckArr= (double[][]) jom.parseExpression(phy1Expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-PHYexpr1-NKB_DEB_BrTilgung.txt", phyExpr1FinLckArr,10);				
				
				
				// -- NOTE: 30.Apr.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				//
				//--NC--//
				String phy2Expr;
				
				phy2Expr = " sum(DVBond * TransformedBondWeightKey, 1) " ; //-- [1,35] x [35, 1428] -- ///
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, phy2Expr, "phy2Expr-Constraint-Disp2");
				double [][] phyExpr2FinLckArr_3= (double[][]) jom.parseExpression(phy2Expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-PHYexpr-DVBondINBondWeightKey.txt", phyExpr2FinLckArr_3,10);
				
				
				phy2Expr = " sum( ((DVBond * TransformedBondWeightKey) * DeltaMatrixInstrAllIn), 1)" ; 
				phy2Expr = " sum(" + phy2Expr + ", 1)" ;
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, phy2Expr, "phy2Expr-Constraint-Disp2");
				double [][] phyExpr2FinLckArr= (double[][]) jom.parseExpression(phy2Expr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-PHYexpr-DVBondINBondWeightKeyINDeltaMatAllIN.txt", phyExpr2FinLckArr,10);
				
				
				//-- BUG SUM --//
				String phy2ExprExt = " sum ( sum( ((DVBond * TransformedBondWeightKey) * DeltaMatrixInstrAllIn), 1), 1) " ; 
				phy2ExprExt = " sum(" + phy2ExprExt + ", 1)" ;
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, phy2ExprExt, "phy2Expr-Constraint-Disp3");
				double [][] phyExpr2ExtFinLckArr= (double[][]) jom.parseExpression(phy2ExprExt).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-PHYexpr-SUMDVBondINBondWeightKeyINDeltaMatAllINSUM.txt", phyExpr2ExtFinLckArr,10);
				
				
				
				String phyExpr = phy1Expr + " + ( "+ phy2Expr +" )";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, phyExpr, "phy2Expr-Constraint-Disp4");
				double [][] phyExpr12FinLckArr= (double[][]) jom.parseExpression(phyExpr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-PHYexpr-AllPhy.txt", phyExpr12FinLckArr,10);
				
				
				
				//-- Prepare the FinLuecke constraint --//
				String finlckExpr1= " ( DeltaMatrixInstrAllIn)" ; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExpr1, "finlckExpr1-Constraint-Disp5-0");
				
				finlckExpr1= " ( DVBond)" ; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExpr1, "finlckExpr1-Constraint-Disp5-00");
				
				
				
				//jom.setInputParameter("DeltaMatrixInstrAllIn","permute(DeltaMatrixInstrAllIn,[1;3;2])" ); // --- [1428, 5, 5] --// --> [35,5,5]
				//jom.setInputParameter("DeltaMatrixInstrAllIn","permute(DeltaMatrixInstrAllIn,[1;3;2])" ); // --- [1428, 5, 5] --// --> [35,5 <--> 5] //
				//jom.setInputParameter("DeltaMatrixInstrAllIn","permute(DeltaMatrixInstrAllIn,[1;2;3])" ); 
				
				
				/*
				finlckExpr1= " ( DeltaMatrixInstrAllIn)" ; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExpr1, "finlckExpr1-Constraint-Disp5-1");
				
				finlckExpr1= "DVBond * DeltaMatrixInstrAllIn" ; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExpr1, "finlckExpr1-Constraint-Disp5-2");
				
				finlckExpr1= "sum( (DVBond * DeltaMatrixInstrAllIn), 1)" ; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExpr1, "finlckExpr1-Constraint-Disp6");
				
				//--final expression--//
				finlckExpr1= " sum ( sum( (DVBond * DeltaMatrixInstrAllIn), 1), 1)" ;
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExpr1, "finlckExpr1-Constraint-Disp7");
				*/
				
				//--final expression--//
				jom.setInputParameter("DeltaMatrixInstrAllInTNS","permute(DeltaMatrixInstrAllIn,[1;3;2])" );  //--DeltaMatrixInstrRawTNS//
				
				finlckExpr1 = " sum ( sum( ((DVBond * TransformedBondWeightKey) * DeltaMatrixInstrAllInTNS), 1), 1) " ; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExpr1, "finlckExpr1-Constraint-Disp10");
				double [][] phyExpr2FinLckTNSArr= (double[][]) jom.parseExpression(finlckExpr1).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-TNSexpr-DVBondINTransposeBondWeightKeyINDeltaMatAllIN.txt", phyExpr2FinLckTNSArr,10);
				
				String finlckExprConstraint = phyExpr + " - (" + finlckExpr1 + ")"; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, finlckExprConstraint, "finlckExprConstraint-Constraint-Disp8");
				double [][] finlckExprConstraintArr= (double[][]) jom.parseExpression(finlckExprConstraint).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-PHYMinusTNS-ExprConstraintArr.txt", finlckExprConstraintArr,10);
								
				// BUGGY to setup JOM with >>DV -- Do it in JAVA ??? --//
				//jom.setInputParameter("finlckExprConstraint", finlckExprConstraint );
				//jom.setInputParameter("finlckExprConstraint", "finlckExprConstraint(all, 0:NumYearIdx)" );
				
				//Reduce FinLuecke from [1,51] to [1, NumYear] rowVector// 
				boolean reduceFinLckNumYears=true;
				String finlckExprConstraintRedNY="";
				if (reduceFinLckNumYears) {
					
					// build a matrix of dim [51,5], [51, this.rule^COnf.NumYears]    so that
					double[][] matDiagNumYear = new double[51][this.ruleConf.numYears];
					for (int i=0; i<51; i++) {
						for (int j=0; j<5; j++) { // 5 years --- this.ruleConf.NumYears;  
							if (i==j) matDiagNumYear[i][j]=1.0;
							else matDiagNumYear[i][j]=0.0; 
						}
					}
					jom.setInputParameter("MatrixDiagNumYear", new DoubleMatrixND(matDiagNumYear) ); 
					
					// To bring the data at diagonal matrix
					//String eyeExpr = "diag([51;5])" ;
					//double [][] diagExprArr= (double[][]) jom.getInputParameter("MatrixDiagNumYear").toArray();  //  .parseExpression(MatrixDiagNumYear).evaluate().toArray();
					
					//-- reduce the constraint for NumYear and initStartRefYear --// 
					double [][] diagExprArr= (double[][]) jom.getInputParameter("MatrixDiagNumYearReduction").toArray();
					this.oxl.genTxtFileFrom2DDataFmt(filepath, "FinLcke-Reduce-diagExprArr.txt", diagExprArr,10);
					
					finlckExprConstraintRedNY = "( "+ finlckExprConstraint + " ) * ( MatrixDiagNumYearReduction )" ;
					double [][] finlckExprConstraintRedNYArr= (double[][]) jom.parseExpression(finlckExprConstraintRedNY).evaluate().toArray();
					this.oxl.genTxtFileFrom2DDataFmt(filepath, "finlckExprConstraintRedNYArr.txt", finlckExprConstraintRedNYArr,10);
				
					//--- This method of FinLucke will not be used --- Used the method used in ValidateFinLucke below ---//
					BondFinLckConst = finlckExprConstraintRedNY;
				}
				
				return BondFinLckConst;
			}
			/////////////////////////////////////////////////////////////////////
			
			
			
			/////////////////////////////////////////////////////////////////////
			//private void ojbfunc_optimize_finLuecke_addConstraintBond_V1(){
			private String ojbfunc_optimize_formBondFinLueckeConstraint(){
				
				// --- No NETDV should be used ---/
				// Phy = NKS + B + E +  Sum( Sum(  DVBond * DVBond  ) ) 
				// constraint:  Phy - Sum( Sum(  DELTA * DVBond ) ) == 0.0 ; //--DeltaMatrixInstrAllIn --/
				// -- NOTE: 30.Apr.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				
				String BondFinLckConst="";
				
				//--good--//String SchuldTotalVec = " ((SchuldFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" * BondsRV2DDebtCleanOpt), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				//String SchuldTotalVec = " ((SchuldFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" * " + this.BondDeltaBkstTensor_BondDebtClean_Opt + "), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				String SchuldTotalVec = " ((SchuldFullOpt) + (  " + this.BondDeltaBkstTensor_BondDebtClean_Opt + ") +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				
				
				double [][] SchuldTotalVecArr= (double[][]) jom.parseExpression(SchuldTotalVec).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "SchuldTotalVecArr.txt", SchuldTotalVecArr,10); // -- size: 60X1 --//
				
				double [][] DeltaSchuldTotalVecArr = new double[1][SchuldTotalVecArr.length]; // Dec.2016_Debt_Clean= 1.344199E+12  ;
				for (int i=0, j=0; j< (SchuldTotalVecArr.length)/12 ; i++, j++) {
					if (i==0)
						DeltaSchuldTotalVecArr[0][j] = SchuldTotalVecArr[11+i][0] - 1093370162540.01; //-- Setup the reference val for  Dec_PrevYear (2016-Dec)  1093370162540.01// --//
					else
						DeltaSchuldTotalVecArr[0][j] = SchuldTotalVecArr[11+i][0] - SchuldTotalVecArr[i-1][0] ; //-- Delta from Dec_Year to Dec_PrevYear --//
					i=i+11;
				}
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "DeltaSchuldTotalVecArr.txt", DeltaSchuldTotalVecArr,10); // -- size: 60X1 --// good
									
				
				//--   dot product to enable only 12th element, and disable other elements --//
				String SchuldTotalVec12On = SchuldTotalVec + " .* MatrixRowVecMonths12EleON " ; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalVec12On, "finlckExprConstraint-SchuldTotalVec12On");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "MatrixRowVecMonths12EleON", "finlckExprConstraint-MatrixRowVecMonths12EleON");
				double [][] SchuldTotalVec12OnArr = (double[][]) jom.parseExpression(SchuldTotalVec12On).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "SchuldTotalVec12OnArr.txt", SchuldTotalVec12OnArr,10); // -- size: 60X1 --//
				
				
				//String SchuldTotalVec12OnPrevYear = SchuldTotalVec + " .* MatrixRowVecMonths12EleONPrevYear " ;
				String SchuldTotalVec12OnPrevYear = SchuldTotalVec + " .* MatrixRowVecMonths12EleONPrevYear " ;
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalVec12OnPrevYear, "finlckExprConstraint-SchuldTotalVec12OnPrevYear");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "MatrixMonthsYear12EleON", "finlckExprConstraint-MatrixMonthsYear12EleON");
				double [][] SchuldTotalVec12OnPrevArr = (double[][]) jom.parseExpression(SchuldTotalVec12OnPrevYear).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "SchuldTotalVec12OnPrevArr.txt", SchuldTotalVec12OnPrevArr,10); // -- size: 60X1 --//
				
				//String SchuldTotalMY12Red =   " (MatrixMonthsYear12EleON) * (" + SchuldTotalVec12On + ")"; //-- array [NumYear, NumMonths] *  [NumMonths, 1] = [NumYear, 1] --//
				String SchuldTotalMY12Red =  "( (" + SchuldTotalVec12On + ")' * " + " (MatrixMonthsYear12EleON)' )" ;  //-- array [NumYear, NumMonths] *  [NumMonths, 1] = [NumYear, 1] --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalMY12Red, "finlckExprConstraint-SchuldTotalMY12Red");
				double [][] SchuldTotalMY12RedArr = (double[][]) jom.parseExpression(SchuldTotalMY12Red).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "SchuldTotalMY12RedArr.txt", SchuldTotalMY12RedArr,10); // -- size: 60X1 --//
				
				
				//String SchuldTotalMY12RedPrevYear =   " ( MatrixMonthsYear12EleON * " + SchuldTotalVec12OnPrevYear + ") + MatrixRowVecMonths12EleONPrevYearInit2016 ";
				String SchuldTotalMY12RedPrevYear =   "( ( (" + SchuldTotalVec12OnPrevYear + ")' * " + " (MatrixMonthsYear12EleONPrevYear)' ) + MatrixRowVecMonths12EleONPrevYearInit2016)" ; //--Init=1093370162540.01;//
				//String SchuldTotalMY12RedPrevYear =   "( ( (" + SchuldTotalVec12OnPrevYear + ")' * " + " (MatrixMonthsYear12EleONPrevYear)' ) )" ; //--Init=1093370162540.01; //-- ??? Add Init--//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalMY12RedPrevYear, "finlckExprConstraint-SchuldTotalMY12RedPrevYear");
				double [][] SchuldTotalMY12RedPrevYearArr= (double[][]) jom.parseExpression(SchuldTotalMY12RedPrevYear).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "SchuldTotalMY12RedPrevYearArr.txt", SchuldTotalMY12RedPrevYearArr,10); // -- size: 60X1 --//
				
				
				String DeltaSchuldTotalVecRed = " (" + SchuldTotalMY12Red + ") - (" +  SchuldTotalMY12RedPrevYear + ") "; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DeltaSchuldTotalVecRed, "finlckExprConstraint-DeltaSchuldTotalVecRed"); //??
				double [][] DeltaSchuldTotalVecRedArr= (double[][]) jom.parseExpression(DeltaSchuldTotalVecRed).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent +"DeltaSchuldTotalVecRedArr.txt", DeltaSchuldTotalVecRedArr,10); // -- size:1 x NumYear --//
				
				//-- Do not set the  DeltaSchuldTotalVecRed into JOM, as it contains DV --//
				//jom.setInputParameter("DeltaSchuldTotalVec", new DoubleMatrixND(DeltaSchuldTotalVec)  );
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DeltaSchuldTotalVecRed, "finlckExprConstraint-DeltaSchuldTotalVec");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "NKBYearly", "finlckExprConstraint-NKBYearly");
				
				//jom.setInputParameter("DeltaSchuldTotalVec", "DeltaSchuldTotalVec(0, 0:50)"  );
				jom.setInputParameter("NKBYearly", "NKBYearly(0, 0:NumYearIdx)"  );
			    String vldFinLucke = " ( NKBYearly) - (" + DeltaSchuldTotalVecRed + ") ";
			    				    
			    //-- reduce the constraint for NumYear and initStartRefYear --//
			    // --- Already reduced to [1, 5] ---//
			    //vldFinLucke = "( "+ vldFinLucke + " ) * ( MatrixDiagNumYearReduction )" ;
			    double [][] vldFinLuckeVecArr= (double[][]) jom.parseExpression(vldFinLucke).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "ExprBondFinLuckeVec-F.txt", vldFinLuckeVecArr,10); // -- size: 60X1 --//
				
				BondFinLckConst = vldFinLucke;
				// -- validation over--//
				return BondFinLckConst;
			}
			
			
			private void ojbfunc_optimize_addConstraintBondFinLuecke() {
			
				String BondFinLueckeConst = ojbfunc_optimize_formBondFinLueckeConstraint();
				
				String finlckConstraint1 = "( "+ BondFinLueckeConst + " ) <=  1000000000000.0 ";
				//String finlckConstraint1 = "( "+ BondFinLueckeConst + " ) <=  1E12 ";
				//String finlckConstraint1 = "( "+ BondFinLueckeConst + " ) <=  1E11 ";
				this.jom.addConstraint(finlckConstraint1);
				
				String finlckConstraint2 = "( "+ BondFinLueckeConst + " ) >=  -1000000000000.0 ";
				//String finlckConstraint2 = "( "+ BondFinLueckeConst + " ) >=  -1E12 ";
				//String finlckConstraint2 = "( "+ BondFinLueckeConst + " ) >=  -1E11 ";
				this.jom.addConstraint(finlckConstraint2);
			}
			/////////////////////////////////////////////////////////////////////
			
				
			/////////////////////////////////////////////////////////////////////
			//private void ojbfunc_optimize_EigenBestand_addConstraintBond_V1(){
			private String ojbfunc_optimize_formEigenBestandConstraintZBond_OLD_ABSURD(){
				
				// --- Not NETDV should be used ---//
				// -- NOTE: 30.Apr.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				
				//-- BondExpr V1 --// 
				String NetVolDV = "(DVBond - EBDVBond)";
				String EBDVHeadData = " EBDVBond / sum(DVBond) ";
				
				//String BondExpr = "(  sum(     sum( (DVBond .* (EBDVBond)) * DeltaHeadMatrixInstr)   ) )"; //--- [1, 70]. Size right matrix: [70, 10, 10] ; [1428, 51, 51] ---//
				
				//String BondExprConstraint = " EBYearly + NKBYearly + BruttoTilgungYearly + BruttoTilgungYearly  + sum( ((DVBond .* EBDVBond) * DeltaHeadMatrixInstr) , 3)";
				
				String BondExprConstraint = "(EBDVBond)";
				
				
				/*
				BondExprConstraint = "(EBDVBond * DeltaHeadMatrixInstr)";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-Disp0");
				
				BondExprConstraint = "sum( (EBDVBond * DeltaHeadMatrixInstr), 1)";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-Disp1");
				
				BondExprConstraint = "(DVBond * " + BondExprConstraint + " )";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-Disp2");
				
				BondExprConstraint = " DeltaEBGwsYearly +  ( " + BondExprConstraint + "  )";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-Disp3");
				
				*/
				
				//BondExprConstraint = "( DVBond * DeltaHeadMatrixInstrRaw )"; // -- DeltaMatrixInstr -- //UnitMatrixDelta
				
				// -- NOTE: 30.Apr.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				
				// -- Add Eg, which is  Gewünschte_EB  from config file --//
				//-- Ea = Bruto_Tilgung - Change in Yearly Debt (accessed from BMPlanCurrent:DebtClan)  --// 
				
				
				BondExprConstraint = "( DVBond * TransformedBondWeightKey )";  // [35x1428]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-DVBondINTransformedBondWeightKey-Disp0");
				
				
				BondExprConstraint = "( DeltaHeadMatrixInstrRawNumYear )"; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExprConstraint-DeltaHeadMatrixInstrRawNumYear");
				
				String DVBondDelta1 = "sum( ((DVBond * TransformedBondWeightKey) * DeltaTwiddleMatrixInstrRawNumYear), 1)";
				String DVBondDelta2 = "sum( ((DVBond * TransformedBondWeightKey) * DeltaTwiddleMatrixInstrRawNumYearTNS), 1)";
				String DVBondDelta =  DVBondDelta1 + " - " + DVBondDelta2; 
 				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DVBondDelta1, "BondExprConstraint-DVBondDelta1");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DVBondDelta2, "BondExprConstraint-DVBondDelta2");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DVBondDelta, "BondExprConstraint-DVBondDelta");
				
				//BondExprConstraint = "sum( (DVBond * TransformedBondWeightKey) * DeltaHeadMatrixInstrRaw, 1)";
				//BondExprConstraint = "sum ( sum( ((DVBond * TransformedBondWeightKey) * DeltaMatrixInstrAllInNumYear), 1), 1) ";
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-BondWt-???-Disp1");
				
				
				//--- wrong -- do not sum DVBondDelta to make it scalar --//
				// --- Apply sumproduct instead of matrikx multiplication to get rid of issue -- "main" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 ---///
				// -- BondExprConstraint = "EBDVBond * ( "+ BondExprConstraint + ")"; --- should be replaced ---//
				
				//-- replicate a row Vector along diagonal of a square matrix --//
				
				int rowColVec=0; // -- 0 is for rowVector; else colVector--//
				String vecSMat = ojbfunc_optimize_replicate_VecSMat("EBDVBond", this.ruleConf.numYears, rowColVec);
				//String vecSMat = ojbfunc_optimize_replicate_VecSMat("EBDVBond", this.dataSet.jix_EV_Data_Year[0].length, rowColVec);
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecSMat, "BondExpr-Constraint-vecSMat-Disp2");
				//-- apply alternate method of matrix multiplication with dot-product and sum ---/// 
				String oMatMult =ojbfunc_optimize_AltMatMult_VecSMat(vecSMat, DVBondDelta, rowColVec );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, oMatMult, "BondExpr-Constraint-oMatMult-Disp2");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaEBGwsYearly(all, 0:NumYearIdx)", "BondExpr-Constraint-DeltaEBGwsYearly");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "EBAuslaufendYearlyNumYear", "BondExpr-Constraint-EBAuslaufendYearlyNumYear");
				
				//-- Start from 2018 --; disbale the first year ---//
				jom.setInputParameter("EBAuslaufendYearlyNumYear", "EBAuslaufendYearly(all, 0:NumYearIdx)"  );
				jom.setInputParameter("DeltaEBGwsYearly", "DeltaEBGwsYearly(all, 0:NumYearIdx)"  );
				
				if (this.dataSet.initStartRefYear == 2018) { 
					double[][] initZero = new double[1][1];
					initZero[0][0] = 0.0;
					jom.setInputParameter("EBAuslaufendYearlyNumYear(0,0)", new DoubleMatrixND(initZero)  );
					jom.setInputParameter("DeltaEBGwsYearly(0,0)", new DoubleMatrixND(initZero)  );
				}
				if (this.dataSet.initStartRefYear == 2019) { 
					double[][] initZero = new double[1][2];
					initZero[0][0] = 0.0; initZero[0][1] = 0.0;
					jom.setInputParameter("EBAuslaufendYearlyNumYear(0,0:1)", new DoubleMatrixND(initZero)  );
					jom.setInputParameter("DeltaEBGwsYearly(0,0:1)", new DoubleMatrixND(initZero)  );
				}
				
				BondExprConstraint = "DeltaEBGwsYearly + EBAuslaufendYearlyNumYear + ( " + oMatMult + " )";	
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-DispFinal");
				
				//-- reduce the constraint for NumYear and initStartRefYear --// 
				BondExprConstraint = "( "+ BondExprConstraint + " ) .* ( MatrixColVecNumYearReduction )" ;  //-- dim should be [1,5] --//
			    double [][] BondExprConstraintArr= (double[][]) jom.parseExpression(BondExprConstraint).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "BondExprEBConstraintArr.txt", BondExprConstraintArr,10); // -- size: 60X1 --//
				
				
				
				return BondExprConstraint;
			}
			
			
			//-- test issues of non-linear optimization --//
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
			///////////////////////////////////////////////////////////////////////
			
			
			///////////////////////////////////////////////////////////////////////
			private void ojbfunc_optimize_initBondDeltaBaukastenEBTensor(){
				// Get the 3D-Bond-Baukasten and trasform  diagonally (without weights) to select the Baukasten with right stratom //
				// Get the Delta [1428,51,51] and trasform  diagonally (without weights) to select the Delta with right stratom //
				// Multiply each element of Tx-Baukasten with each element of Tx-Delta ; Apply element to element multiplication (scalar mult)
				// Then apply the Transformation using TXKey with weights.
				
				if (initTensor) return; //-- run the initTensor only once --//
				System.out.println(" ojbfunc_optimize_initformBondDeltaBaukastenTensor: Begin to build DeltaBaukasten Tensor ... ");
				
				//-- Keep all false for efficiency --//
				boolean forceGenTensor = false;
				boolean changedConfig = false;
				boolean debug=false;
				
	    		String fullFileTensor = this.filepath + "DeltaBaukastenRawTensor4D.serdat";
    			String fullFileTensorTX = this.filepath + "BondDeltaBkstTensor4DTxAll.serdat";
    			String fullFileReducedTensorTXTM = this.filepath + "BondDeltaBkstTensor4DTxTM.serdat";
    			
				optimTransformMatrix otm = new optimTransformMatrix();
				
				int methodDBKTensor=1;
				if (methodDBKTensor == 1) {
					
					// --- All BaukastenRaw3D, DeltaInstr Tensor --//
					this.jom.setInputParameter("DeltaRawMatrix", new DoubleMatrixND(this.deltaMatrixInstr)); ////   1428 x 51 x 51
		    		this.jom.setInputParameter("DeltaRawMatrix", "sum(DeltaRawMatrix,3) "); /// 1428 x 51
		    		double[][] DeltaRawMatrixArr = (double[][]) jom.getInputParameter("DeltaRawMatrix").toArray(); /// 1428 x 51
		    		oxl.genTxtFileFrom2DData(filepath, "DeltaRawMatrixArr.txt", DeltaRawMatrixArr);

		    		//-- Generate DeltaBondBaukastenTensor4D --// -- fails due to out of memory exception --//
		    		//-- Exception in thread "main" java.lang.OutOfMemoryError: Java heap space --//
					
		    		//-- Build RawTensor of dim -- this.dataSet.bondsRVDataRaw3D[ 292, 1428, 79 ] and Delta[1428, 51 ] --//
		    		//double[][][][] DeltaBaukastenRawTensor4D = new double[this.dataSet.bondsRVDataRaw3D.length][this.dataSet.bondsRVDataRaw3D[0].length][this.dataSet.bondsRVDataRaw3D[0][0].length][DeltaRawMatrixArr[0].length]; //-- [ 292, 1428, 79, 51 ] --//
		    		//-- Build RawTensor of dim -- [ 1428, 292,  79, 51 ] --//
		    		double[][][][] DeltaBaukastenRawTensor4D = new double[this.dataSet.bondsRVDataRaw3D[0].length][this.dataSet.bondsRVDataRaw3D.length][this.dataSet.bondsRVDataRaw3D[0][0].length][DeltaRawMatrixArr[0].length]; //-- [ 292, 1428, 79, 51 ] --//
		    		double[][][][] BondDeltaBkstTensor4DTxAll = new double[this.ruleConf.numBondDecisionVariables][this.dataSet.bondsRVDataRaw3D.length][this.dataSet.bondsRVDataRaw3D[0][0].length][DeltaRawMatrixArr[0].length]; //-- [ 292, 1428, 79, 51 ] --//
		    		
		    		this.BondDeltaBkstTensor4DTM= new double[this.ruleConf.numDateRows][this.ruleConf.numBondDecisionVariables][][]; //-- [60 x 35 x 79 x 51] --//
		    		double[][][][] TxReducedBondDeltaBkstTensor4DTM = new double[this.ruleConf.numBondDecisionVariables][this.ruleConf.numDateRows][][]; //--[35 x 60 x 79 x 51] --//
		    		
		    		
		    		if (!forceGenTensor) {
		    			
		    			if ( optimValidateFile.validFile(fullFileTensorTX) ) {
		    				
		    				//DeltaBaukastenRawTensor4D = (double[][][][]) this.dataSet.restore.deserializeBeanAsPdouble4D(fullFileTensor);
		    				
		    				if (changedConfig) 
		    					BondDeltaBkstTensor4DTxAll = (double[][][][]) this.dataSet.restore.deserializeBeanAsPdouble4D(fullFileTensorTX);
		    				
		    				//--serialize the Tensor4DTM --//
		    				if ( optimValidateFile.validFile(fullFileReducedTensorTXTM) )
		    					this.BondDeltaBkstTensor4DTM = (double[][][][]) this.dataSet.restore.deserializeBeanAsPdouble4D(fullFileReducedTensorTXTM);
				    		
				    		forceGenTensor=false;
				    		
		    			} else forceGenTensor=true;
	    			}
	    			
		    		
		    		/*
		    		for (int i=0; i<DeltaBaukastenRawTensor4D.length; i++) { // --- 1428 stratoms --//
		    			for (int j=0; j<DeltaBaukastenRawTensor4D[0].length; j++) { // --- 292 time periods --//
		    				for (int k=0; k<DeltaBaukastenRawTensor4D[0][0].length; k++) {  // --- 79 DKF --//
		    					for (int d=0; d<DeltaBaukastenRawTensor4D[0][0][0].length; d++) {  // --- 51 Delta --//
		    						
		    						DeltaBaukastenRawTensor4D[i][j][k][d] = this.dataSet.bondsRVDataRaw3D[j][i][k] *  DeltaRawMatrixArr[i][d];
		    						
		    					}
			    			}
		    			}
		    		}
		    		
	    			//--Transform the DeltaBaukastenRawTensor4D --//
		    		System.out.println("To Be TRANSFORMED:  DeltaBaukastenRawTensor4D: Row=" + DeltaBaukastenRawTensor4D.length + " -- RAW-BOND-DIM: Col=" + DeltaBaukastenRawTensor4D[0].length + 
		    				" // 3DNum:" + DeltaBaukastenRawTensor4D[0][0].length + " // 4DNum: " + DeltaBaukastenRawTensor4D[0][0][0].length);
		    		
		    		System.out.println("Data-Struct Validate: NumBondDV=" + this.ruleConf.numBondDecisionVariables + " -- RawRVRaw-DIM: Tab=" + this.dataSet.bondsRVDataRaw3D.length +  " : Row=" + this.dataSet.bondsRVDataRaw3D[0].length );
		    		System.out.println("Data-Struct Validate: TxBondWtKey-DIM: Row=" + this.dataSet.TransformedBondWeightKey.length +  " : Col=" + this.dataSet.TransformedBondWeightKey[0].length );
		    		*/
		    		
		    		
		    		
		    		
		    		if (forceGenTensor) {
		    		
		    		//--Handle the transformation-matrix computaion manually to build dim [ 292, 1428, 79, 51 ]  --// 
					// -- this.dataSet.TransformedBondWeightKey [1428, NumDV] // Effective ops [NumDV, 1428] x [ 1428, 292, 79, 51 ] = [ NumDV, 292, 79, 51 ]
		    			for (int dv=0; dv<this.ruleConf.numBondDecisionVariables; dv++) {
			    			
			    				for (int tp=0; tp<DeltaBaukastenRawTensor4D[0].length; tp++) {
				    				for (int dkf=0; dkf<DeltaBaukastenRawTensor4D[0][0].length; dkf++) {
				    					for (int dt=0; dt<DeltaBaukastenRawTensor4D[0][0][0].length; dt++) {
				    						
				    						//BondDeltaBkstTensor4DTxAll[dv][tp][dkf][dt]=0.0;
				    						
				    						for (int stratom=0; stratom<DeltaBaukastenRawTensor4D.length; stratom++) {
				    							DeltaBaukastenRawTensor4D[stratom][tp][dkf][dt] = this.dataSet.bondsRVDataRaw3D[tp][stratom][dkf] *  DeltaRawMatrixArr[stratom][dt]; //-- [ 1428, 292,  79, 51 ] --//
				    							
				    							BondDeltaBkstTensor4DTxAll[dv][tp][dkf][dt] += ( this.dataSet.TransformedBondWeightKey[dv][stratom] *   DeltaBaukastenRawTensor4D[stratom][tp][dkf][dt] ); 
				    						} //--for each delta--//
				    						//System.out.println("  ## LOOP CNT -   DV" + dv );
				    						//BondDeltaBkstTensor4DTxAll[dv][tp][dkf][dt] = sum;   sum=0.0;
				    						
				    				} //--for each dkf --//
			    				} //-- for each time-period--//
			    			} //-- for each stratom --//
			    			
			    		}//-- for each dv --//
			    		
			    		//-- serialize the Tensor--//
			    		this.dataSet.serialize.serializeResultBeanAsPdouble4D(DeltaBaukastenRawTensor4D,fullFileTensor) ;
			    		this.dataSet.serialize.serializeResultBeanAsPdouble4D(BondDeltaBkstTensor4DTxAll,fullFileTensorTX) ;
			    		
			    		//--release some sobjects to overcome 30gb of heap space--//
			    		this.dataSet.bondsRVDataRaw3D=null;
			    		this.dataSet.swapsRVDataRaw3D=null;
			    		DeltaBaukastenRawTensor4D=null;
			    		System.gc();  
		    		} // -- forceGen --//
		    		
		    		
		    		if (forceGenTensor || changedConfig )  {
		    			for (int i=0; i<this.ruleConf.numBondDecisionVariables; i++) {
			    			for (int j=0; j<this.ruleConf.numDateRows; j++) {
			    			//for (int j=0; j<this.ruleConf.numBondDecisionVariables; j++) {
			    				//TxReducedBondDeltaBkstTensor4DTM[i][j] = com.stfe.optim.util.optimSliceArray.slice2DArray(BondDeltaBkstTensor4DTxAll[i][j], -1, -1, -1, -1 );
			    				this.BondDeltaBkstTensor4DTM[j][i] = com.stfe.optim.util.optimSliceArray.slice2DArray(BondDeltaBkstTensor4DTxAll[i][j], -1, -1, -1, -1 );
			    			}
			    		}
		    		
			    		//--serialize the Tensor4DTM --//
			    		this.dataSet.serialize.serializeResultBeanAsPdouble4D(this.BondDeltaBkstTensor4DTM,fullFileReducedTensorTXTM) ;
			    		
			    		//--release some sobjects to overcome 30gb of heap space--//
			    		BondDeltaBkstTensor4DTxAll=null;
						System.gc();
					}	
		    		
		    		
		    		if (debug) {
			    		oxl.genTxtFileFrom2DData(filepath, "LocalTxReducedBondDeltaBkstTensor4DTM-00.txt", TxReducedBondDeltaBkstTensor4DTM[0][0]);
			    		oxl.genTxtFileFrom2DData(filepath, "TxReducedBondDeltaBkstTensor4DTM-00.txt", this.BondDeltaBkstTensor4DTM[0][0]);
			    		
			    		oxl.genTxtFileFrom2DData(filepath, "LocalTxReducedBondDeltaBkstTensor4DTM-dv1tp0.txt", TxReducedBondDeltaBkstTensor4DTM[0][1]);
			    		oxl.genTxtFileFrom2DData(filepath, "TxReducedBondDeltaBkstTensor4DTM-tp0dv1.txt", this.BondDeltaBkstTensor4DTM[1][0]);
			    		
			    		oxl.genTxtFileFrom2DData(filepath, "LocalTxReducedBondDeltaBkstTensor4DTM-dv2tp0.txt", TxReducedBondDeltaBkstTensor4DTM[0][2]);
			    		oxl.genTxtFileFrom2DData(filepath, "TxReducedBondDeltaBkstTensor4DTM-tp0dv2.txt", this.BondDeltaBkstTensor4DTM[2][0]);
			    		
			    		oxl.genTxtFileFrom2DData(filepath, "LocalTxReducedBondDeltaBkstTensor4DTM-dv1tp1.txt", TxReducedBondDeltaBkstTensor4DTM[1][1]);
			    		oxl.genTxtFileFrom2DData(filepath, "TxReducedBondDeltaBkstTensor4DTM-tp1dv1.txt", this.BondDeltaBkstTensor4DTM[1][1]);
			    		
			    		oxl.genTxtFileFrom2DData(filepath, "LocalTxReducedBondDeltaBkstTensor4DTM-dv2tp1.txt", TxReducedBondDeltaBkstTensor4DTM[1][2]);
			    		oxl.genTxtFileFrom2DData(filepath, "TxReducedBondDeltaBkstTensor4DTM-tp1dv2.txt", this.BondDeltaBkstTensor4DTM[2][1]);
		    		}
		    	
		    		// -- Tab=60 -- ROW=35 --- Col: 79 --Delta51 --//
		    		System.out.println("*** TRANSFORMED: TxReduced: BondDeltaBkstTensor4DTM Tab=" + this.BondDeltaBkstTensor4DTM.length + " -- ROW=" + this.BondDeltaBkstTensor4DTM[0].length  + " --- Col: "+ this.BondDeltaBkstTensor4DTM[0][0].length + " --Delta" + BondDeltaBkstTensor4DTM[0][0][0].length);
		    		
		    		
		    		// --Final reduce - reduce further to slice data for Delta 51 cols to NumYear --//
		    		this.BondDeltaBkstTensor4DTMEff= new double[this.ruleConf.numDateRows][this.ruleConf.numBondDecisionVariables][][]; //-- [60 x 35 x 79 x 51] --//
		    		for (int i=0; i<this.ruleConf.numDateRows; i++) {
		    			for (int j=0; j<this.ruleConf.numBondDecisionVariables; j++) {
		    				this.BondDeltaBkstTensor4DTMEff[i][j] = com.stfe.optim.util.optimSliceArray.slice2DArray(this.BondDeltaBkstTensor4DTM[i][j], -1, -1, 0, (this.ruleConf.numYears-1) );
		    			}
		    		}
		    		
		    		this.BondDeltaBkstTensor4DTM=null;
		    		
		    		// -- Tab=60 -- ROW=35 --- Col: 79 --Delta: NumYear --// ///--- Sliced from  [60, 35, 79, 51] to [60, 35, 79, NumYear] ---///
		    		System.out.println("*** TRANSFORMED: TxReduced: BondDeltaBkstTensor4DTMEff Tab=" + this.BondDeltaBkstTensor4DTMEff.length + " -- ROW=" + this.BondDeltaBkstTensor4DTMEff[0].length  + " --- Col: "+ this.BondDeltaBkstTensor4DTMEff[0][0].length + " --Delta" + this.BondDeltaBkstTensor4DTMEff[0][0][0].length);
		    			
		    				
		    		// -- Exception in thread "main" java.lang.OutOfMemoryError: Java heap space --//
	    		}
	    		
				 
				
				if (methodDBKTensor == 2) { //-- WARNING: DO NOT USE: JOM can not handle tensor // --Exception in thread "main" java.lang.OutOfMemoryError: Java heap space --//
					//-- Exception in thread "main" java.lang.OutOfMemoryError: Java heap space --//
					//-- done selecet inverse DeltaBondBaukastenTensor4D --//
					//-- Transform select (diagonal matrix) BondBaukasten --//
					
					double[][][] bondsRVDataTransformed3DAll= otm.genDVTransformationMatrix(this.dataSet.jix_Config_BondVars, this.dataSet.bondsRVDataRaw3D ); 
					this.bondsRVData3DTXSelect= new double[this.ruleConf.numDateRows][][];
		    		for (int i=0; i<this.ruleConf.numDateRows; i++) {
		    			this.bondsRVData3DTXSelect[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVDataTransformed3DAll[i], -1, -1, -1, -1 );	
		    		}
		    		//-- Transform select (diagonal matrix) Delta --//
		    		double[][] DeltaTransformed2DAll = otm.genDeltaTransformationMatrix(this.dataSet.jix_Config_BondVars, this.deltaMatrixInstr );
		    		this.Delta2DTXSelect= new double[this.ruleConf.numDateRows][];
		    		this.Delta2DTXSelect = com.stfe.optim.util.optimSliceArray.slice2DArray(DeltaTransformed2DAll, 0, (this.ruleConf.numDateRows-1), 0, (this.ruleConf.numYears-1) );
		    		
		    		//-- Generate DeltaBondBaukastenTensor4D --//
		    		double[][][][] DeltaBaukastenTensor4D = new double[this.bondsRVData3DTXSelect.length][this.bondsRVData3DTXSelect[0].length][this.bondsRVData3DTXSelect[0][0].length][this.Delta2DTXSelect[0].length];
		    		for (int i=0; i<this.bondsRVData3DTXSelect.length; i++) {
		    			for (int j=0; j<this.bondsRVData3DTXSelect[0].length; j++) {
		    				for (int k=0; k<this.bondsRVData3DTXSelect[0][0].length; k++) {
		    					for (int d=0; d<this.Delta2DTXSelect[0].length; d++) {
		    						DeltaBaukastenTensor4D[i][j][k][d] = this.bondsRVData3DTXSelect[i][j][k] *  Delta2DTXSelect[i][d];
		    					}
			    			}
		    			}
		    		} //-- done selecet inverse DeltaBondBaukastenTensor4D --//
		    		
		    	} // -- method==2 --//
				
				initTensor=true;
				
				System.out.println(" ojbfunc_optimize_initformBondDeltaBaukastenTensor: Building DeltaBaukasten Tensor Over! ");
			}
			///////////////////////////////////////////////////////////////////////
			
			
			///////////////////////////////////////////////////////////////////////
			private void ojbfunc_optimize_loadJOM_EigenBestandBondTensorOpt(){
				
				boolean debug = true;
				int methodType=2;
				//-- DV * (BondFxRV2DOpt - BondFxRV2DTensorOpt ) //
				
				// -- JOM data- BondsRVDataTransformed3D --//  [70x120x79] --- [ NumDV, 292, 79, 51 ]
				this.jom.setInputParameter("BondDeltaBkstTensor", new DoubleMatrixND(this.BondDeltaBkstTensor4DTMEff) ); 
				this.jom.setInputParameter("BondDeltaBkstTensor", "BondDeltaBkstTensor(all, all, all, 0:NumYearIdx)" ); //--[ NumDV, 292, 79, 51 ] --> [60 x 35 x 79 x 5] --//
				this.jom.setInputParameter("BondDeltaBkstTensor", "permute(BondDeltaBkstTensor, [4;  2;  1; 3]) " ); //-- [5 x 35 x 60 x 79 ] --//
				
				String reducedTensor;
				String BondDeltaBkstTensor_BondRV="";
				String BondDeltaBkstTensor_BondFx="";
				String BondDeltaBkstTensor_BondPVClean=""; 
				String BondDeltaBkstTensor_BondDebtClean=""; 
			
				if (methodType==2) {
					this.jom.setInputParameter("BondDeltaBkstTensor_BondRV_5_35", "BondDeltaBkstTensor(all, all, all, 5:35)" );
					this.jom.setInputParameter("BondDeltaBkstTensor_BondFx_43_73", "BondDeltaBkstTensor(all, all, all, 43:73)" ); 
					this.jom.setInputParameter("BondDeltaBkstTensor_BondPVClean_2_2", "BondDeltaBkstTensor(all, all, all, 2:2)" );
					this.jom.setInputParameter("BondDeltaBkstTensor_BondDebtClean_1_1", "BondDeltaBkstTensor(all, all, all, 1:1)" );
					
					BondDeltaBkstTensor_BondRV =  "( sum ( EBDVBond * (BondDeltaBkstTensor_BondRV_5_35), 1) )" ; //-- EBDVBond * BondDeltaBkstTensor4D =[1,5] * [5x35x60x31] =[1x35x60x31]==[35x60x31 ] -- //
					BondDeltaBkstTensor_BondFx =  "( sum ( EBDVBond * (BondDeltaBkstTensor_BondFx_43_73), 1) )" ; //-- EBDVBond * BondDeltaBkstTensor4D =[1,5] * [5x35x60x31] =[1x35x60x31]==[35x60x31 ] -- //
					BondDeltaBkstTensor_BondPVClean =  "( sum ( EBDVBond * (BondDeltaBkstTensor_BondPVClean_2_2), 1) )" ; //-- EBDVBond * BondDeltaBkstTensor4D =[1,5] * [5x35x60x1] =[1x35x60x1]==[35x60x1 ] -- //
					BondDeltaBkstTensor_BondDebtClean =  "( sum ( EBDVBond * (BondDeltaBkstTensor_BondDebtClean_1_1), 1) )" ; //-- EBDVBond * BondDeltaBkstTensor4D =[1,5] * [5x35x60x1] =[1x35x60x1]==[35x60x1 ] -- //
					
				} 
				if (methodType==1) {
					reducedTensor =  "sum( EBDVBond * (BondDeltaBkstTensor), 1)";
					
					BondDeltaBkstTensor_BondRV = "reducedTensor" + "(all, all, 5:35)";
					BondDeltaBkstTensor_BondFx = "reducedTensor" + "(all, all, 43:73)";
					BondDeltaBkstTensor_BondPVClean =  "reducedTensor" + "(all, all, 2:2)"; 
					BondDeltaBkstTensor_BondDebtClean =  "reducedTensor" + "(all, all, 1:1)"; 
				}
				
				/*
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondRV, "bondExpr_this.BondDeltaBkstTensor_BondRV+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondFx, "bondExpr_this.BondDeltaBkstTensor_BondFx+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondPVClean, "bondExpr_this.BondDeltaBkstTensor_BondPVClean+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondDebtClean, "bondExpr_this.BondDeltaBkstTensor_BondDebtClean+###");
				*/
				
				/* --- Wrong to evaluate --- Can not be evaluated --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr_DV(this.jom, "BondDeltaBkstTensor_BondPVClean", "BondExpr-BondDeltaBkstTensor_BondPVClean_2_2+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr_DV(this.jom, "BondDeltaBkstTensor_BondDebtClean", "BondExpr-BondDeltaBkstTensor_BondDebtClean_1_1+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr_DV(this.jom, "BondDeltaBkstTensor_BondRV", "BondExpr-BondDeltaBkstTensor_BondRV_5_35+###"); //--Error --//
				this.functUtil.ojbfunc_optimize_dispDVExpr_DV(this.jom, "BondDeltaBkstTensor_BondFx", "BondExpr-BondDeltaBkstTensor_BondFx+###");
				*/
				
				
				//-- Required in Env --//
				String BondDeltaBkstTensor_BondRV_Net =  "( (BondsRV2DOpt) - (" + BondDeltaBkstTensor_BondRV + ") )"; //-- BondRV - BondTensor  -- [35x60x31 ] //
				String BondDeltaBkstTensor_BondFx_Net =  "( (BondFxRV2DOpt) - (" + BondDeltaBkstTensor_BondFx + ") )";  //[35x60x31 ] --//
				String BondDeltaBkstTensor_BondPVClean_Net = "( (BondsRV2DPVCleanOpt) - (" + BondDeltaBkstTensor_BondPVClean + ") )"; //-- [35x60x1 ] --//
				String BondDeltaBkstTensor_BondDebtClean_Net = "( (BondsRV2DDebtCleanOpt) - (" + BondDeltaBkstTensor_BondDebtClean + ") )"; // -- [35x60x1 ] --//
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondRV_Net, "bondExpr_this.BondDeltaBkstTensor_BondRV_Net+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondFx_Net, "bondExpr_this.BondDeltaBkstTensor_BondFx_Net+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondPVClean_Net, "bondExpr_this.BondDeltaBkstTensor_BondPVClean_Net+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondDeltaBkstTensor_BondDebtClean_Net, "bondExpr_this.BondDeltaBkstTensor_BondDebtClean_Net+###");
				
				
				
				//-- Quadtratic DV with matmult crashes in JOM:  Exception in thread "main" java.lang.IndexOutOfBoundsException: Index: 1, Size: 0 --//
				// -- DVBondEBDVDeltaTensor = " DVBond *  (" + DVBondEBDVDeltaTensor + " ) ; // =[1, 35] * [35 x 60 x 31 ] --//
				this.BondDeltaBkstTensor_BondRV_Opt ="sum( DVBond *  ( " + BondDeltaBkstTensor_BondRV_Net + "), 1)"; // [60 x 31]  //
				this.BondDeltaBkstTensor_BondFx_Opt = "sum(DVBond *  ( " + BondDeltaBkstTensor_BondFx_Net + "),1)"; // [60 x 31]  //;
				this.BondDeltaBkstTensor_BondPVClean_Opt = "sum(DVBond *  ( " + BondDeltaBkstTensor_BondPVClean_Net + "),1)"; // [60 x 1]  //;
				this.BondDeltaBkstTensor_BondDebtClean_Opt = "sum(DVBond *  ( " + BondDeltaBkstTensor_BondDebtClean_Net + "),1)"; // [60 x 1]  //;   
				
				
				//-- JOM Issue: AltMult for DV1 and DV2 ::: this.BondDeltaBkstTensor_BondRV_Opt = "sum( DVBond * (BondDeltaBkstTensor_BondRV_Net) , 1 )"; --//
				// --- Alternative Mult for DV * EBVD --//		
				int rowColVec=0; // -- 0 is for rowVector; else colVector -- do not use colVector--//
				String vecOMat_BondRV_Col31 = ojbfunc_optimize_replicate_GenVecMatDim_3D("DVBond", this.ruleConf.numBondDecisionVariables, rowColVec , this.ruleConf.numBondDecisionVariables, this.ruleConf.numDateRows, 31, false );
				String vecOMat_BondFx_Col31 = ojbfunc_optimize_replicate_GenVecMatDim_3D("DVBond", this.ruleConf.numBondDecisionVariables, rowColVec , this.ruleConf.numBondDecisionVariables, this.ruleConf.numDateRows, 31, false );
				String vecOMat_BondPV_Col1 = ojbfunc_optimize_replicate_GenVecMatDim_3D("DVBond", this.ruleConf.numBondDecisionVariables, rowColVec , this.ruleConf.numBondDecisionVariables, this.ruleConf.numDateRows, 1, false );
				String vecOMat_BondDebt_Col1 = ojbfunc_optimize_replicate_GenVecMatDim_3D("DVBond", this.ruleConf.numBondDecisionVariables, rowColVec , this.ruleConf.numBondDecisionVariables, this.ruleConf.numDateRows, 1, false );
				
				this.BondDeltaBkstTensor_BondRV_Opt =ojbfunc_optimize_GenAltMatMult_VecOMat_3D(vecOMat_BondRV_Col31, BondDeltaBkstTensor_BondRV_Net, rowColVec, false ); // [ 60 x 31]  //
				//this.BondDeltaBkstTensor_BondRV_Opt = "sum( (" + this.BondDeltaBkstTensor_BondRV_Opt + "), 1)"; // [60 x 31]  //
				this.BondDeltaBkstTensor_BondFx_Opt = ojbfunc_optimize_GenAltMatMult_VecOMat_3D(vecOMat_BondFx_Col31, BondDeltaBkstTensor_BondFx_Net, rowColVec, false ); // [ 60 x 31]  //;
				//this.BondDeltaBkstTensor_BondFx_Opt = "sum( (" + this.BondDeltaBkstTensor_BondFx_Opt + "), 1)"; // [60 x 31]  //
				this.BondDeltaBkstTensor_BondPVClean_Opt = ojbfunc_optimize_GenAltMatMult_VecOMat_3D(vecOMat_BondPV_Col1, BondDeltaBkstTensor_BondPVClean_Net, rowColVec, false ); // [ 60 x 1]  //;
				//this.BondDeltaBkstTensor_BondPVClean_Opt = "sum( (" + this.BondDeltaBkstTensor_BondPVClean_Opt + "), 1)"; // [60 x 31]  //
				this.BondDeltaBkstTensor_BondDebtClean_Opt = ojbfunc_optimize_GenAltMatMult_VecOMat_3D(vecOMat_BondDebt_Col1, BondDeltaBkstTensor_BondDebtClean_Net, rowColVec, false ); // [ 60 x 1]  //;
				//this.BondDeltaBkstTensor_BondDebtClean_Opt = "sum( (" + this.BondDeltaBkstTensor_BondDebtClean_Opt + "), 1)"; // [60 x 31]  //
						
				
				
				//-- Test with dummy data --//
				/*
				this.BondDeltaBkstTensor_BondRV_Opt ="sum(" + vecOMat_BondRV_Col31 + ", 1)"; // [60 x 31]  //
				this.BondDeltaBkstTensor_BondFx_Opt = "sum(" + vecOMat_BondFx_Col31 + ",1)"; // [60 x 31]  //;
				this.BondDeltaBkstTensor_BondPVClean_Opt = "sum(" + vecOMat_BondPV_Col1 + ",1)"; // [60 x 1]  //;
				this.BondDeltaBkstTensor_BondDebtClean_Opt = "sum(" + vecOMat_BondDebt_Col1 + ",1)"; // [60 x 1]  //;   
				*/
				
				
				//this.BondDeltaBkstTensor_BondRV_Opt = "sum(BondsRV2DOpt,1)"; // [60 x 31]  //;
				//this.BondDeltaBkstTensor_BondFx_Opt = "sum(BondFxRV2DOpt,1)"; // [60 x 31]  //;
				//this.BondDeltaBkstTensor_BondPVClean_Opt = "sum(BondsRV2DPVCleanOpt,1)"; // [60 x 1]  //;
				//this.BondDeltaBkstTensor_BondDebtClean_Opt = "sum(BondsRV2DDebtCleanOpt,1)"; // [60 x 1]  //;   
				
				
				
				// Reclaim memory -- Clean the JOM , else it does not work --// 
				this.jom.setInputParameter("BondDeltaBkstTensor", 0 ); //-- reclaim memory consumed by heavy tensor --// 
				
				this.BondDeltaBkstTensor4DTM=null;
				//this.jom.setInputParameter("BondsRV2DOpt", 0 );
				//this.jom.setInputParameter("BondsRV2DPVCleanOpt", 0 );
				
				//ojbfunc_optimize_bond_cleanJOM();
				//////////////////////////////////////////////
				
				if (debug) {
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, this.BondDeltaBkstTensor_BondRV_Opt, "bondExpr_this.BondDeltaBkstTensor_BondRV_Opt+###");
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, this.BondDeltaBkstTensor_BondFx_Opt, "bondExpr_this.BondDeltaBkstTensor_BondFx_Opt+###");
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, this.BondDeltaBkstTensor_BondPVClean_Opt, "bondExpr_this.BondDeltaBkstTensor_BondPVClean_Opt+###");
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, this.BondDeltaBkstTensor_BondDebtClean_Opt, "bondExpr_this.BondDeltaBkstTensor_BondDebtClean_Opt+###");
					
					String SchuldTotalVecBond = " (" +  this.BondDeltaBkstTensor_BondDebtClean_Opt +  ") " ; 
					String SchuldTotalVecSwap = " (" +  " (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
					String SchuldTotalVec = " ((SchuldFullOpt) + (  " + this.BondDeltaBkstTensor_BondDebtClean_Opt + ") +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "SchuldFullOpt", "bondExpr_Tensor_SchuldFullOpt+###");
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalVecSwap, "bondExpr_Tensor_SchuldTotalVecSwap+###");
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalVecBond, "bondExpr_Tensor_SchuldTotalVecBond+###");
					this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalVec, "bondExpr_Tensor_SchuldTotalVec+###");
				}
				
			}
			
			
			private void ojbfunc_optimize_loadJOM_EigenBestandBondTensor_NOTUsed(){
				
				String EBConstraint="";
				//--- DVReduced_DVBondDeltaTwiddle_1 --//
				// -- JOM data- BondsRVDataTransformed3D --//  [70x120x79] --- [ NumDV, 292, 79, 51 ]
				String DVBondReducedTxWtRawBond = " sum( (DVBond * BondsRVDataTransformed3D), 1) " ; //--  [1, 35] * [35, 60, 79] = [ 60, 79  ] --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DVBondReducedTxWtRawBond, "BondExprConstraint-DVBondReducedTxWt");
				
				
				//--- DVReduced_EBDVBondDeltaTwiddle_2 --//
				//--this.BondDeltaBkstTensor4DTM [DateRows, DV, DKF, Delta]=[60 x 35 x 79 x 51] --//
				this.jom.setInputParameter("BondDeltaBkstTensor", new DoubleMatrixND(this.BondDeltaBkstTensor4DTMEff) ); 
				this.jom.setInputParameter("BondDeltaBkstTensor", "BondDeltaBkstTensor(all, all, all, 0:NumYearIdx)" ); //--[ NumDV, 292, 79, 51 ] --> [60 x 35 x 79 x 5] --//
				this.jom.setInputParameter("BondDeltaBkstTensor", "permute(BondDeltaBkstTensor, [4;  2;  1; 3]) " ); //-- [5 x 35 x 60 x 79 ] --//
				//this.jom.setInputParameter("BondDeltaBkstTensor", "EBDVBond * BondDeltaBkstTensor ");  // --  EBDVBond * BondDeltaBkstTensor4D =[1, 5] * [5 x 35 x 60 x 79 ] =[1 x 35 x 60 x 79] -- //
				//this.jom.setInputParameter("BondDeltaBkstTensor", "sum(BondDeltaBkstTensor ,1)");  // -- [35 x 60 x 79 ] --//
				//String DVBondEBDVDeltaTensor = " sum(DVBond * BondDeltaBkstTensor, 1)" ; // --  DVBond * BondDeltaBkstTensor4D =[1, 35] * [35 x 60 x 79 ] =[1 x 60 x 79] -->[60, 79] -- //
				
				String DVBondEBDVDeltaTensor =  " sum ( EBDVBond * BondDeltaBkstTensor, 1) " ; //-- EBDVBond * BondDeltaBkstTensor4D =[1,5] * [5x35x60x79] =[1x35x60x79]==[35x60x79 ] -- //
				DVBondEBDVDeltaTensor = " (" + DVBondEBDVDeltaTensor + ") ";
				
				// -- DV x EBDV is not possible in JOM -- DO alternative multiplication --//
				// -- DVBondEBDVDeltaTensor = " DVBond *  (" + DVBondEBDVDeltaTensor + " )  " ; --//   //--  DVBond * BondDeltaBkstTensor4D =[1, 35] * [35 x 60 x 79 ]  --// 
				
				int rowColVec=0; // -- 0 is for rowVector; else colVector -- do not use colVector--//
				String vecOMat = ojbfunc_optimize_replicate_GenVecMatDim_3D("DVBond", this.ruleConf.numBondDecisionVariables, rowColVec , this.ruleConf.numBondDecisionVariables, this.ruleConf.numDateRows, 79, false );
				String vecMatMult1 =ojbfunc_optimize_GenAltMatMult_VecOMat_3D(vecOMat, DVBondEBDVDeltaTensor, rowColVec, false ); // [ 60 x 79] -->[60, 79] //
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult1, "BondExpr-Constraint-vecMatMult-Alt1");
				//double [][] EBDVBondDelta1Arr= (double[][]) jom.parseExpression(vecMatMult1).evaluate().toArray(); 
				//this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta1Arr.txt", EBDVBondDelta1Arr,10);
				
				//-- release BondDeltaBkstTensor from JOM Memeory -- //
				this.jom.setInputParameter("BondDeltaBkstTensor", 0 );
				
				//-- Final Expresion for EBCOnstraint --//
				EBConstraint = "( " + DVBondReducedTxWtRawBond + ") - ("  + vecMatMult1 +" )"; // --[ / Row=60 /Col=79 ] --// 
				
				//Convert EBConstarint [ / Row=60 /Col=79 ] to size [ / Row=1 /Col=5 ]  :
				
				
				return;
				
			} // -- formEigenBestandBondConstraint --// --- ReducedEBDeltaBkstTensor --//
			
			
			
			/*
			private String ojbfunc_optimize_formEigenBestandConstraintBond_V_OLD(){
				
				String BondExprEBConstraint; ????
						
						
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaEBGwsYearly(all, 0:NumYearIdx)", "BondExpr-Constraint-DeltaEBGwsYearly");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "EBAuslaufendYearlyNumYear", "BondExpr-Constraint-EBAuslaufendYearlyNumYear");

				//BondExprConstraint = "( DeltaEBGwsYearly(all, 0:NumYearIdx)) + ( EBAuslaufendYearlyNumYear) + ( " + EBDVBondDelta + " )";
				String BondEBCombined = "((0.000000001) * DeltaEBGwsYearly(all, 0:NumYearIdx)) + ((0.000000001) * EBAuslaufendYearlyNumYear) + ( " + EBDVBondDelta + " )";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondEBCombined, "BondExpr-Constraint-DispFinal");


				//-- reduce the constraint for NumYear and initStartRefYear --// 
				BondExprEBConstraint = "( "+ BondEBCombined + " ) .* ( MatrixColVecNumYearReduction )" ;  //-- dim should be [1,5] --//
			    double [][] BondExprEBConstraintArr= (double[][]) jom.parseExpression(BondExprEBConstraint).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "BondExprEBConstraint-F.txt", BondExprEBConstraintArr,10); // -- size: 60X1 --//

				return BondExprEBConstraint;
				
			}
			*/
			///////////////////////////////////////////////////////////////////////	
			
			
			private String ojbfunc_optimize_formEigenBestandConstraintBond_V_TRY(){
				
				// --- Not NETDV should be used ---//
				// -- NOTE: 30.Apr.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				
				//-- BondExpr V1 --// 
				//String BondExpr = "(  sum(     sum( (DVBond .* (EBDVBond)) * DeltaHeadMatrixInstr)   ) )"; //--- [1, 70]. Size right matrix: [70, 10, 10] ; [1428, 51, 51] ---//
				//String BondExprConstraint = " EBYearly + NKBYearly + BruttoTilgungYearly + BruttoTilgungYearly  + sum( ((DVBond .* EBDVBond) * DeltaHeadMatrixInstr) , 3)";
				
				// -- NOTE: 2.Aug.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				// -- Add Eg, which is  Gewünschte_EB  from config file --//
				//-- Ea = Bruto_Tilgung - Change in Yearly Debt (accessed from BMPlanCurrent:DebtClan)  --// 
				//" EBGew + EBchg  +   sum( DVBond .* EBDVDeltaNet  ) ;;;  EBDVDeltaNet = (EBDV * DeltaTwiddleInstr) - (EBDV * DeltaTwiddleInstrTNS)   
				
				String BondExprEBConstraint="";
				String BondExprConstraint = "(EBDVBond)";
				
				
				String DeltaReduced = " ( TransformedBondWeightKey * DeltaMatrixInstrNumYear) " ; //--  / Row=35 /Col=5 x 5 ] --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DeltaReduced, "BondExprConstraint-DeltaReduced"); // --- / Row=35 /Col=5 x 5 ] --//
				
				String DeltaReducedDV =  " sum( DVBond * (" + DeltaReduced + ") , 1)"; // --- / Col=5 x 5 ] --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DeltaReducedDV, "BondExprConstraint-DeltaReducedDVEB");
				
				
				/*
				String DeltaReducedDVEB =  " sum( EBDVBond * " + DeltaReducedDV + ", 1)"; //  --[1,5] ---
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DeltaReducedDVEB, "BondExprConstraint-DeltaReducedDVEB");
				*/
				
				////////////////////////////////////////////////////////////////////
				
				
				//String vecOMat = ojbfunc_optimize_replicate_GenVecMatDim(DVBondReduced,this.ruleConf.numSRVRawRowsPerDate, rowColVec , 1428, 5, true );
				String vecOMat = ojbfunc_optimize_replicate_GenVecMatDim("EBDVBond", 5, 0 , 5, 5, false );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecOMat, "BondExpr-Constraint-vecOMat-Repl");
								
				String vecMatMult1 =ojbfunc_optimize_GenAltMatMult_VecOMat(vecOMat, DeltaReducedDV, 0, false ); //[5, 5]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult1, "BondExpr-Constraint-vecMatMult-Alt1");
				double [][] EBDVBondDelta1Arr= (double[][]) jom.parseExpression(vecMatMult1).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta1Arr.txt", EBDVBondDelta1Arr,10);
				
				
				////////////////////////////////////////////////////////////////////
				
				
				/*
				
				//-- generate data file for some of DataTwiddleMatrix --//
				this.jom.setInputParameter("DeltaMatrixInstrNumYearEB", "DeltaMatrixInstrNumYear" );
				this.jom.setInputParameter("DeltaMatrixInstrNumYearEB", "permute(DeltaMatrixInstrNumYear, [2;1;3] )" ); //-- [10r, 1428, 10c] --//
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaMatrixInstrNumYearEB", "BondExprConstraint-DeltaMatrixInstrNumYearEBApply");
				
				this.jom.setInputParameter("DeltaMatrixInstrNumYearEBTNS", "permute(DeltaMatrixInstr,[1;3;2] )" ); //-- [51c, 1428, 51r] --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaMatrixInstrNumYearEBTNS", "BondExprConstraint-DeltaMatrixInstrNumYearEBTNSApply");
				
				int rowColVec=0; // -- 0 is for rowVector; else colVector--//
				
				//--- DVReduced_EBDVBondDeltaTwiddle_1 --//
				String EBDVBondDelta1 = "sum( (EBDVBond * DeltaMatrixInstrNumYearEB), 1)"; //  EBDVBond=[1, 5]. DeltaTwiddleMatrixInstrRawNumYear matrix= [5, 1428, 5]-- //
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, EBDVBondDelta1, "BondExpr-Constraint-EBDVBondDelta1");
				/// CHeck the Struct--//
				double [][] EBDVBondDelta1StructArr= (double[][]) jom.parseExpression(EBDVBondDelta1).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta1StructArr-Raw_1428_NumYear.txt", EBDVBondDelta1StructArr,10);
				 
				
				//--- wrong -- do not sum DVBondDelta to make it scalar --//
				// --- Apply sumproduct instead of matrikx multiplication to get rid of issue -- "main" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 ---///
				// -- BondExprConstraint = "EBDVBond * ( "+ BondExprConstraint + ")"; --- should be replaced ---//
				//-- replicate a row Vector along diagonal of a square matrix --//
				
				//String vecOMat = ojbfunc_optimize_replicate_GenVecMatDim(DVBondReduced,this.ruleConf.numSRVRawRowsPerDate, rowColVec , 1428, 5, true );
				String vecOMat = ojbfunc_optimize_replicate_GenVecMatDim(DVBondReduced, 1428, rowColVec , 1428, 5, false );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecOMat, "BondExpr-Constraint-vecOMat-Repl");
								
				String vecMatMult1 =ojbfunc_optimize_GenAltMatMult_VecOMat(vecOMat, EBDVBondDelta1, rowColVec, false ); //[5, 5]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult1, "BondExpr-Constraint-vecMatMult-Alt1");
				double [][] EBDVBondDelta1Arr= (double[][]) jom.parseExpression(vecMatMult1).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta1Arr.txt", EBDVBondDelta1Arr,10);
				
				*/
				
				
////////////////////////////////////////////////////////////////////
				//--- DVReduced_EBDVBondDeltaTwiddle_2 : Consider all 51 rows of DeltaTwiddle --//
				//this.jom.setInputParameter("DeltaMatrixInstrNumYearEBTNS", "permute(DeltaMatrixInstr,[1;3;2] )" ); //-- [51c, 1428, 51r] --// 
				this.jom.setInputParameter("DeltaMatrixInstrNumYearEBTNS", "permute(DeltaMatrixInstr,[1;3;2] )" ); //-- [ 1428, 51c, 51r] --//
				
				String DeltaReducedTNS = " sum(  (TransformedBondWeightKey * DeltaMatrixInstrNumYearEBTNS), 2) " ;  //-- [ 35, 51c, 51r] -->> [ 35, 51r]  //
				
				String EBDVBondDelta2 = "sum (DVBond *  (" + DeltaReducedTNS + ") , 1)" ; // -- [1, 51] --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, EBDVBondDelta2, "BondExpr-Constraint-EBDVBondDelta2");
				
				
				//--- From EBDVBondDelta2 of Dim (1x51) , make a slicing of (1x5) -- use matrix operation (1,51)x(51,5)=(1,5) to make slice from DVs ---//
				//make a slice of of dim (1,5) from (1,51) --//
				String sliceEBDVBondDelta2 = ojbfunc_optimize_SliceDVMatRowCol(EBDVBondDelta2, 1, 1, 51, 5 );  //-- Slice the matrix of dim (51x5) --//
				//sliceEBDVBondDelta2 = " (" + sliceEBDVBondDelta2 + "')";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, sliceEBDVBondDelta2, "BondExpr-Constraint-sliceEBDVBondDelta2"); // --- replicate mat 2-dime (2,1)  --//
						
				String vecDotMat2 = "(EBDVBond) .* (" + sliceEBDVBondDelta2 +")"; //-- Slice the matrix of dim (5x1) --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecDotMat2, "BondExpr-Constraint-vecDotMat-F2"); // --- replicate mat 2-dime (2,1)  --//
				
				double [][] EBDVBondDelta2Arr= (double[][]) jom.parseExpression(vecDotMat2).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta2Arr.txt", EBDVBondDelta2Arr,10); 
////////////////////////////////////////////////////////////////////
				
				
				
////////////////////////////////////////////////////////////////////
				//String EBDVBondDelta =  "( (" + vecMatMult1 + ") - (" + vecDotMat2 + ") )";  // -- [1,5] --//
				
				//String EBDVBondDelta =  "(" + vecMatMult1 + ")";  // -- [1,5] --//
				String EBDVBondDelta =  "(" + vecDotMat2 + ")";  // -- [1,5] --//
				
				
 				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult1, "BondExprConstraint-vecMatMult1"); // -- / Row=1428 /Col=5 ]  --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecDotMat2, "BondExprConstraint-vecDotMat2"); // -- / Row=1428 /Col=5 ]  --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, EBDVBondDelta, "BondExprConstraint-EBDVBondDelta");   // -- / Row=1428 /Col=5 ]  --// 
				
				double [][] EBDVBondDeltaArr= (double[][]) jom.parseExpression(EBDVBondDelta).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "BondExprEBConstEBDVBondDeltatArr.txt", EBDVBondDeltaArr,10); // -- size: 60X1 --//
				
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaEBGwsYearly(all, 0:NumYearIdx)", "BondExpr-Constraint-DeltaEBGwsYearly");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "EBAuslaufendYearlyNumYear", "BondExpr-Constraint-EBAuslaufendYearlyNumYear");
				
				/*
				//BondExprConstraint = "( DeltaEBGwsYearly(all, 0:NumYearIdx)) + ( EBAuslaufendYearlyNumYear) + ( " + EBDVBondDelta + " )";
				BondExprConstraint = "((0.001) * DeltaEBGwsYearly(all, 0:NumYearIdx)) + ((0.001) * EBAuslaufendYearlyNumYear) + ( " + EBDVBondDelta + " )";  // -- ?? Million /Billion --???//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-DispFinal");
				
				double [][] EGEAEBDVBondDeltaArr= (double[][]) jom.parseExpression(BondExprConstraint).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EG_EA_EB_ConstEBDVBondDeltatArr.txt", EGEAEBDVBondDeltaArr,10);
				
				//-- reduce the constraint for NumYear and initStartRefYear --// 
				String BondExprEBConstraint = "( "+ BondExprConstraint + " ) .* ( MatrixColVecNumYearReduction )" ;  //-- dim should be [1,5] --//
			    double [][] BondExprEBConstraintArr= (double[][]) jom.parseExpression(BondExprEBConstraint).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "BondExprEBConstraint-F.txt", BondExprEBConstraintArr,10); // -- size: 60X1 --//
				
				*/
				
				/////////////////////////
				String FunnyExpr = " DVBond(0, 0:1) .* EBDVBond(0, 0:1) " ;
				//FunnyExpr = " DVBond(0, 0:1) .* DVBond(0, 1:2) " ;
				FunnyExpr = " DVBond(0, 0:1) .* EBDVBond(0, 0:1) " ;
				BondExprEBConstraint=FunnyExpr;
				/////////////////////////
				
				//BondExprEBConstraint= EBDVBondDelta;
				
				return BondExprEBConstraint;  
			}
			

			
			
			///////////////////////////////////////////////////////////////////
			
			//  -- TO BE USED  --// 
			private String ojbfunc_optimize_formEigenBestandConstraintBond(){
				
				// --- Not NETDV should be used ---//
				// -- NOTE: 30.Apr.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				
				//-- BondExpr V1 --// 
				//String BondExpr = "(  sum(     sum( (DVBond .* (EBDVBond)) * DeltaHeadMatrixInstr)   ) )"; //--- [1, 70]. Size right matrix: [70, 10, 10] ; [1428, 51, 51] ---//
				//String BondExprConstraint = " EBYearly + NKBYearly + BruttoTilgungYearly + BruttoTilgungYearly  + sum( ((DVBond .* EBDVBond) * DeltaHeadMatrixInstr) , 3)";
				
				// -- NOTE: 2.Aug.2018: DVBond should be replaced with ( TranformationKey * DVBond) -- Add it as a pre-step ---// 
				// -- Add Eg, which is  Gewünschte_EB  from config file --//
				//-- Ea = Bruto_Tilgung - Change in Yearly Debt (accessed from BMPlanCurrent:DebtClan)  --// 
				//" EBGew + EBchg  +   sum( DVBond .* EBDVDeltaNet  ) ;;;  EBDVDeltaNet = (EBDV * DeltaTwiddleInstr) - (EBDV * DeltaTwiddleInstrTNS)   
				
				String BondExprConstraint = "(EBDVBond)";
				String DVBondReduced = " (DVBond * TransformedBondWeightKey) " ; //--  / Row=1 /Col=1428 ] --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, DVBondReduced, "BondExprConstraint-DVBondReducedWt");
				
				/*
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaHeadMatrixInstrRawNumYear", "BondExprConstraint-DeltaHeadMatrixInstrRawNumYear"); // --- [1428, 10, 10] --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaTwiddleMatrixInstrRawNumYear", "BondExprConstraint-DeltaTwiddleMatrixInstrRawNumYear"); // --- [1428, 10, 10] --//
				jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYear", new DoubleMatrixND(this.deltaTwiddleMatrixInstr )  ); //-- [1428, 51, 51] --//
				jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYear", "DeltaTwiddleMatrixInstrRawNumYear(all,0:NumYearIdx, 0:NumYearIdx)");//-- [1428, 10, 10] --//
				
				jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYearTNS", "permute(DeltaTwiddleMatrixInstrRawNumYear,[1;3;2])");
				this.jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYear", "permute(DeltaTwiddleMatrixInstrRawNumYear, [2;1;3] )" ); //-- [10, 1428, 10] --//
				this.jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYearTNS", "permute(DeltaTwiddleMatrixInstrRawNumYearTNS,[3;1;2] )" ); //-- [10, 1428, 10] --// 
				*/
								
				//-- generate data file for some of DataTwiddleMatrix --//
				this.jom.setInputParameter("DeltaMatrixInstrNumYearEB", "DeltaMatrixInstrNumYear" );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaMatrixInstrNumYearEB", "BondExprConstraint-DeltaMatrixInstrNumYearEB"); // --- [1428, 10, 10] --//
				if (test_fileIdent == "DV0_") {
					double [][][] DeltaMatrixInstrNumYearEBArr= (double[][][]) jom.parseExpression("DeltaMatrixInstrNumYearEB").evaluate().toArray();
					for (int t=0; t<10; t++) {
						String tval= String.valueOf(t);
						this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "DeltaMatrixInstrNumYearEBArr-"+tval+"-F.txt", DeltaMatrixInstrNumYearEBArr[t],10);
						////-- this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "DeltaTwiddleMatrixInstrRawNumYearArr0-F.txt", DeltaTwiddleMatrixInstrRawNumYearArr[0],10);// --//
					}
				}
				this.jom.setInputParameter("DeltaMatrixInstrNumYearEB", "permute(DeltaMatrixInstrNumYear, [2;1;3] )" ); //-- [10r, 1428, 10c] --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaMatrixInstrNumYearEB", "BondExprConstraint-DeltaMatrixInstrNumYearEBApply");
				this.jom.setInputParameter("DeltaMatrixInstrNumYearEBTNS", "permute(DeltaMatrixInstr,[1;3;2] )" ); //-- [51c, 1428, 51r] --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaMatrixInstrNumYearEBTNS", "BondExprConstraint-DeltaMatrixInstrNumYearEBTNSApply");
				
				int rowColVec=0; // -- 0 is for rowVector; else colVector--//
				
				//--- DVReduced_EBDVBondDeltaTwiddle_1 --//
				String EBDVBondDelta1 = "sum( (EBDVBond * DeltaMatrixInstrNumYearEB), 1)"; //  EBDVBond=[1, 5]. DeltaTwiddleMatrixInstrRawNumYear matrix= [5, 1428, 5]-- //
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, EBDVBondDelta1, "BondExpr-Constraint-EBDVBondDelta1");
				/// CHeck the Struct--//
				double [][] EBDVBondDelta1StructArr= (double[][]) jom.parseExpression(EBDVBondDelta1).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta1StructArr-Raw_1428_NumYear.txt", EBDVBondDelta1StructArr,10);

				
				//--- wrong -- do not sum DVBondDelta to make it scalar --//
				// --- Apply sumproduct instead of matrikx multiplication to get rid of issue -- "main" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 ---///
				// -- BondExprConstraint = "EBDVBond * ( "+ BondExprConstraint + ")"; --- should be replaced ---//
				//-- replicate a row Vector along diagonal of a square matrix --//
				
				//String vecOMat = ojbfunc_optimize_replicate_GenVecMatDim(DVBondReduced,this.ruleConf.numSRVRawRowsPerDate, rowColVec , 1428, 5, true );
				String vecOMat = ojbfunc_optimize_replicate_GenVecMatDim(DVBondReduced, 1428, rowColVec , 1428, this.ruleConf.numYears, false );
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecOMat, "BondExpr-Constraint-vecOMat-Repl");
								
				String vecMatMult1 =ojbfunc_optimize_GenAltMatMult_VecOMat(vecOMat, EBDVBondDelta1, rowColVec, false ); //[5, 5]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult1, "BondExpr-Constraint-vecMatMult-Alt1");
				double [][] EBDVBondDelta1Arr= (double[][]) jom.parseExpression(vecMatMult1).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta1Arr.txt", EBDVBondDelta1Arr,10);
				
				
				//--- DVReduced_EBDVBondDeltaTwiddle_2 : Consider all 51 rows of DeltaTwiddle --//
				String EBDVBondDelta2 = "sum( (  (" + DVBondReduced + ")  * (DeltaMatrixInstrNumYearEBTNS) ), 1)";  // -- / Row=1428 /Col=51 x 51 --// DeltaTwiddleMatrixInstrRawTNS
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, EBDVBondDelta2, "BondExpr-Constraint-EBDVBondDelta2");
				EBDVBondDelta2 = "sum ( " + EBDVBondDelta2 + ", 1)" ;// -- / Row=1 /Col=51 ---//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, EBDVBondDelta2, "BondExpr-Constraint-EBDVBondDelta2-SumRowTNS");
				
				//--- From EBDVBondDelta2 of Dim (1x51) , make a slicing of (1x5) -- use matrix operation (1,51)x(51,5)=(1,5) to make slice from DVs ---//
				//make a slice of of dim (1,5) from (1,51) --//
				String sliceEBDVBondDelta2 = ojbfunc_optimize_SliceDVMatRowCol(EBDVBondDelta2, 1, 1, 51, this.ruleConf.numYears );  //-- Slice the matrix of dim (51x5) --//
				//sliceEBDVBondDelta2 = " (" + sliceEBDVBondDelta2 + "')";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, sliceEBDVBondDelta2, "BondExpr-Constraint-sliceEBDVBondDelta2"); // --- replicate mat 2-dime (2,1)  --//
						
				String vecDotMat2 = "(EBDVBond) .* (" + sliceEBDVBondDelta2 +")"; //-- Slice the matrix of dim (5x1) --//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecDotMat2, "BondExpr-Constraint-vecDotMat-F2"); // --- replicate mat 2-dime (2,1)  --//
				
				double [][] EBDVBondDelta2Arr= (double[][]) jom.parseExpression(vecDotMat2).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EBDVBondDelta2Arr.txt", EBDVBondDelta2Arr,10); 
				
				String EBDVBondDelta =  "( (" + vecMatMult1 + ") - (" + vecDotMat2 + ") )";  // -- [1,5] --//
 				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecMatMult1, "BondExprConstraint-vecMatMult1"); // -- / Row=1428 /Col=5 ]  --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, vecDotMat2, "BondExprConstraint-vecDotMat2"); // -- / Row=1428 /Col=5 ]  --// 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, EBDVBondDelta, "BondExprConstraint-EBDVBondDelta");   // -- / Row=1428 /Col=5 ]  --// 
				
				double [][] EBDVBondDeltaArr= (double[][]) jom.parseExpression(EBDVBondDelta).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "BondExprEBConstEBDVBondDeltatArr.txt", EBDVBondDeltaArr,10); // -- size: 60X1 --//
				
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "DeltaEBGwsYearly(all, 0:NumYearIdx)", "BondExpr-Constraint-DeltaEBGwsYearly");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "EBAuslaufendYearlyNumYear", "BondExpr-Constraint-EBAuslaufendYearlyNumYear");
				
				
				//BondExprConstraint = "( DeltaEBGwsYearly(all, 0:NumYearIdx)) + ( EBAuslaufendYearlyNumYear) + ( " + EBDVBondDelta + " )";
				BondExprConstraint = "((0.001) * DeltaEBGwsYearly(all, 0:NumYearIdx)) + ((0.001) * EBAuslaufendYearlyNumYear) + ( " + EBDVBondDelta + " )";  // -- ?? Million /Billion --???//
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-DispFinal");
				
				
				////////////////////// TRY TOY MODEL+++++++++++
				//String EBDVIntoSumDV = "(EBDVBond) .* sum(DVBond,2)";
				//BondExprConstraint = " ( " + EBDVIntoSumDV + " )";
				//BondExprConstraint = "((0.001) * DeltaEBGwsYearly(all, 0:NumYearIdx)) + ((0.001) * EBAuslaufendYearlyNumYear) - ( " + EBDVIntoSumDV + " )";  // -- ?? Million /Billion --???//
				
				BondExprConstraint = " ( " + EBDVBondDelta + " )";  // -- ?? Million /Billion --???//
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondExprConstraint, "BondExpr-Constraint-DispFinal");
				////////////////////////////////+++++++++++++++++
				
				
				double [][] EGEAEBDVBondDeltaArr= (double[][]) jom.parseExpression(BondExprConstraint).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "EG_EA_EB_ConstEBDVBondDeltatArr.txt", EGEAEBDVBondDeltaArr,10);
				
				//-- reduce the constraint for NumYear and initStartRefYear --// 
				String BondExprEBConstraint = "( "+ BondExprConstraint + " ) .* ( MatrixColVecNumYearReduction )" ;  //-- dim should be [1,5] --//
			    double [][] BondExprEBConstraintArr= (double[][]) jom.parseExpression(BondExprEBConstraint).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, test_fileIdent + "BondExprEBConstraint-F.txt", BondExprEBConstraintArr,10); // -- size: 60X1 --//
				
				return BondExprEBConstraint;
			}
			
			

			private void ojbfunc_optimize_addConstraintEigenBestandBond(){
				
				
				String EigenBestandConstraint = ojbfunc_optimize_formEigenBestandConstraintBond();
				
				
				
				//String BondExprConstraint1 = "( "+ EigenBestandConstraint + " ) >= -1.0 ";
				String BondExprConstraint1 = "( "+ EigenBestandConstraint + " ) >= -1E12 ";
				this.jom.addConstraint(BondExprConstraint1);
				
				//String BondExprConstraint2 = "( "+ EigenBestandConstraint + " ) <= 1.0 "; //-- 1000.0 fails with feasibility error. --//
				String BondExprConstraint2 = "( "+ EigenBestandConstraint + " ) <= 1E12 "; //-- 1000.0 fails with feasibility error. --//
				this.jom.addConstraint(BondExprConstraint2);
				
				//String BondExprConstraint = "( "+ EigenBestandConstraint + " ) == 0.0 ";
				//this.jom.addConstraint(BondExprConstraint);
			}
			
			
			
			
			private void ojbfunc_optimize_addConstraintEigenBestandBond_v0_OLD(){
				
				String EigenBestandConstraint = ojbfunc_optimize_formEigenBestandConstraintBond();
				
				//String BondExprEBConstraint = " ( "+ BondExprConstraint + " ) ==  0.0 ";
				
				//String BondExprConstraint1 = "( "+ EigenBestandConstraint + " ) >= -1E8 ";
				String BondExprConstraint1 = "( "+ EigenBestandConstraint + " ) >= -1.0 "; // -0.1 //
				this.jom.addConstraint(BondExprConstraint1);
				
				String BondExprConstraint2 = "( "+ EigenBestandConstraint + " ) <= 1.0 "; //  0.1 // 
				//String BondExprConstraint2 = "( "+ EigenBestandConstraint + " ) <= 1E8 "; //-- 1000.0 fails with feasibility error. --//
				this.jom.addConstraint(BondExprConstraint2);
				
				if (this.dataSet.initStartRefYear == 2018) 
					this.jom.addConstraint("EBDVBond(0,0) == 0.0 ");
				if (this.dataSet.initStartRefYear == 2019) {
					this.jom.addConstraint("EBDVBond(0,0) == 0.0 ");
					this.jom.addConstraint("EBDVBond(0,1) == 0.0 ");
				}
				
			}
			
			/////////////////////////////////////////////////////////////////////
			
			
			
			/////////////////////////////////////////////////////////////////////
			//-- Bond Fixing Risk --//		
			public void ojbfunc_optimize_setupJOMBondSwapFxRisk() {
				
				// slice the sensi of SwapsRV2DOpt from 31 col  // 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "SwapsRV2DAnnSensiOpt", "BondExpr-SwapsRV2DAnnSensiOpt+###");
				this.jom.setInputParameter("SwapFxRV2DOpt","SwapsRV2DAnnSensiOpt(all,all,0:30)" );  
				double [][][] SwapFxRV2DOptArr= (double[][][]) jom.getInputParameter("SwapFxRV2DOpt").toArray();			
				this.oxl.genTxtFileFrom2DData(filepath, "SwapFxRV2DOptArr-0-30_1stMonth.txt", SwapFxRV2DOptArr[0]);			
				this.functUtil.objfunc_displayJOMVarStruct (this.jom, "SwapFxRV2DOpt", null,0);			
							
				//-- BondFxRV2DOpt already in Env : --- this.jom.setInputParameter("BondFxRV2DOpt","BondsRV2DAnnSensiOpt(all,all,0:30)" );
				double [][][] BondFxRV2DOptArr= (double[][][]) jom.getInputParameter("BondFxRV2DOpt").toArray();			
				this.oxl.genTxtFileFrom2DData(filepath, "BondFxRV2DOptArr-0-30_DV1AllMonths.txt", BondFxRV2DOptArr[0]);
				//this.oxl.genTxtFileFrom2DData(filepath, "BondFxRV2DOptArr-0-30_DV2AllMonths.txt", BondFxRV2DOptArr[1]);
				//this.oxl.genTxtFileFrom2DData(filepath, "BondFxRV2DOptArr-0-30_DV3AllMonths.txt", BondFxRV2DOptArr[2]);			
				//this.oxl.genTxtFileFrom2DData(filepath, "BondFxRV2DOptArr-0-30_DV7AllMonths.txt", BondFxRV2DOptArr[6]);
				//this.oxl.genTxtFileFrom2DData(filepath, "BondFxRV2DOptArr-0-30_DV10AllMonths.txt", BondFxRV2DOptArr[9]);
				//this.oxl.genTxtFileFrom2DData(filepath, "BondFxRV2DOptArr-0-30_DV30AllMonths.txt", BondFxRV2DOptArr[29]);
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "BondFxRV2DOpt", "BondExpr-BondFxRV2DOpt+###");
				
				//-- SwapFxRiskOpt
				this.functUtil.objfunc_displayJOMVarStruct (this.jom, "SwapFxRiskOpt", null,0);
				double [][] SwapFxRiskOptArr= (double[][]) jom.getInputParameter("SwapFxRiskOpt").toArray();			
				this.oxl.genTxtFileFrom2DData(filepath, "SwapFxRiskOptArr.txt", SwapFxRiskOptArr);
				System.out.println (" SwapFxRiskOpt RowSize = " + this.jom.parseExpression("SwapFxRiskOpt(all,0)").evaluateConstant().getNumElements() + " /Col:" + 
						this.jom.parseExpression("SwapFxRiskOpt(0,all)").evaluateConstant().getNumElements() );	
				
				//slice market covar //			
				this.jom.setInputParameter("PCCov_Mkt1_1_31", "PCCov_Mkt1(0:30 , 0:30) " ); // PCCov_Mkt1(21:30, 21:30) 
				double [][] PCCov_Mkt1_1_31Arr= (double[][]) jom.getInputParameter("PCCov_Mkt1_1_31").toArray();			
				this.oxl.genTxtFileFrom2DData(filepath, "PCCov_Mkt1_1_31Arr.txt", PCCov_Mkt1_1_31Arr);
				System.out.println (" PCCov_Mkt1_1_31 Dim = " + this.jom.parseExpression("PCCov_Mkt1_1_31").getNumDim());
				System.out.println (" PCCov_Mkt1_1_31 RowSize = " + this.jom.parseExpression("PCCov_Mkt1_1_31(all,0)").evaluateConstant().getNumElements() + " /Col:" + 
						this.jom.parseExpression("PCCov_Mkt1_1_31(0,all)").evaluateConstant().getNumElements() );			
				
				// Already setup - SwapSensiRiskOpt, SwapFxRiskOpt //
				//jom.setInputParameter("SwapSensiRiskOpt", new DoubleMatrixND(joSwapSensiRiskEff)  );
			 	//jom.setInputParameter("SwapFxRiskOpt", new DoubleMatrixND(joSwapFxRiskEff)  );
				System.out.println (" SwapFxRisk RowSize = " + this.jom.parseExpression("SwapFxRiskOpt(all,0)").evaluateConstant().getNumElements() + 
						" /ColSize:"+ this.jom.parseExpression("SwapFxRiskOpt(0,all)").evaluateConstant().getNumElements() );

			}
			/////////////////////////////////////////////////////////////////////
			
			
			
			
			/////////////////////////////////////////////////////////////////////
			public String  ojbfunc_optimize_formBondSwapFxRiskConstraintExp() {
				// Added : *#+ Realise -- SchuldFullOpt    --+#*/ 
				
				//Solution to replace the multiplication of DV-matrix and its transpose : (DV x DV')
				//String fexpr = " sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt ";  ///---SwapFxRV2DOpt=[35,60,30]---///
				
				boolean validateFXRisk=false;
				
				
				//String fexpr = " sum( (  (sum( (DVSwap * SwapFxRV2DOpt),1) + sum( (DVBond * BondFxRV2DOpt),1) + FxRiskFullOpt)  .*    (  (sum( (DVSwap * SwapFxRV2DOpt),1) + sum( (DVBond * BondFxRV2DOpt),1) + FxRiskFullOpt)  * PCCov_Mkt1_1_31) ),2)";
				
				//--good--//String fexpr = " sum( (  (sum( ( DVSwap  * SwapFxRV2DOpt),1) + sum( (" + ojbfunc_optimize_formBondSwapNetDV() + " * BondFxRV2DOpt),1) + FxRiskFullOpt)  .*    (  (sum( (DVSwap * SwapFxRV2DOpt),1) + sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +"  * BondFxRV2DOpt),1) + FxRiskFullOpt)  * PCCov_Mkt1_1_31) ),2)";
				//String fexpr = " sum( (  (sum( ( DVSwap  * SwapFxRV2DOpt),1) + sum( (" + ojbfunc_optimize_formBondSwapNetDV() + " * "+ this.BondDeltaBkstTensor_BondFx_Opt +"),1) + FxRiskFullOpt)  .*    (  (sum( (DVSwap * SwapFxRV2DOpt),1) + sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +"  * "+ this.BondDeltaBkstTensor_BondFx_Opt +"),1) + FxRiskFullOpt)  * PCCov_Mkt1_1_31) ),2)";
				
				//-- String fexpr = " sum( (  (sum( ( DVSwap  * SwapFxRV2DOpt),1) + sum( (" +  this.BondDeltaBkstTensor_BondFx_Opt +"),1) + FxRiskFullOpt)  .*    (  (sum( (DVSwap * SwapFxRV2DOpt),1) + sum( ( "+  this.BondDeltaBkstTensor_BondFx_Opt +"),1) + FxRiskFullOpt)  * PCCov_Mkt1_1_31) ),2)";
				
				String fexpr = " sum( (  (sum( ( DVSwap  * SwapFxRV2DOpt),1) + " + this.BondDeltaBkstTensor_BondFx_Opt + " + FxRiskFullOpt)  .*    (  (sum( (DVSwap * SwapFxRV2DOpt),1) + " + this.BondDeltaBkstTensor_BondFx_Opt + " + FxRiskFullOpt)  * PCCov_Mkt1_1_31) ),2)";
				
				
				fexpr = "sum( " + fexpr + ",2)";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, fexpr, "AddConstratintBondSwapFxRisk-fexpr1");
				
				double [][] formBondSwapFxRiskConstraintArr= (double[][]) jom.parseExpression(fexpr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "FX-InitDV0-FormBondSwapFxRiskConstraintArr", formBondSwapFxRiskConstraintArr,10);
				
				if (validateFXRisk) {
					//double [] initValDVBond7V = { 20.0, 20, 20, 20, 20, 20, 20 }; //--- 20 per Year; ---//
					double [] initValDVBond7V = { 0.0, 0, 20.0, 0, 0, 0, 0 }; //--- 20 per Year; ---//
					
					//double [] initValDVSwap7V = { 1.0, 1, 1, 1, 1, 1, 1 }; // --- 4 per Year; ---//
					double [] initValDVSwap7V = { 0.0, 0, 0, 0, 0, 0, 0 }; //
					
					double[][] initDVBond = new double[1][this.ruleConf.numDecisionVariables];
					double[][] initDVSwap = new double[1][this.ruleConf.numDecisionVariables];
					initDVBond[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVBond7V), this.ruleConf.numDecisionVariables/ 7 );
					initDVSwap[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVSwap7V), this.ruleConf.numDecisionVariables/ 7 );
					jom.setInputParameter("DVBond", new DoubleMatrixND(initDVBond));
					jom.setInputParameter("DVSwap", new DoubleMatrixND(initDVSwap));
					
					
					double [][] valBondSwapFxRiskConstraintArr= (double[][]) jom.parseExpression(fexpr).evaluate().toArray();
					this.oxl.genTxtFileFrom2DDataFmt(filepath, "FX-DV-B0-3-20-S0-FormBondSwapFxRiskConstraintArr-Vld-Init", valBondSwapFxRiskConstraintArr,10);
					//this.oxl.genTxtFileFrom2DDataFmt(filepath, "FX-DV-BDV3-20-BDVall-0-S0-FormBondSwapFxRiskConstraintArr-Vld", valBondSwapFxRiskConstraintArr,10);
				}
		
				// -- apply sqrt  for all values at diagonal [diagonal matrix] and 0 at rest  --/
				// -- Add small value to it, else might cause JOM exception: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 //
				fexpr= "sqrt( ("+ fexpr + ") + 0.0001d ) "; 
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, fexpr, "AddConstratintBondSwapFXRisk-fexpr-sqrt");
				
				
				// -- divide the column vector fexpr with transpose of a row vector SchuldOpt to get a column  vector -- [1,n] x [n, n] = [1, n]--/
				//-- divide by total-schuld vector--//
				String PVTotalVec=ojbfunc_optimize_formBondSwapPVTotal();
			
				//String SchuldTotalVec = " ((SchuldFullOpt) + (sum( (DVBond * BondsRV2DDebtCleanOpt), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				//String SchuldTotalVec = ojbfunc_optimize_formBondSwapSchuldTotal();
				
				//--good--//String SchuldTotalVec = " ((SchuldFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" * BondsRV2DDebtCleanOpt), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				//--String SchuldTotalVec = " ((SchuldFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" * " + this.BondDeltaBkstTensor_BondDebtClean_Opt + "), 1)) +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				String SchuldTotalVec = " ((SchuldFullOpt) + (  " + this.BondDeltaBkstTensor_BondDebtClean_Opt + ") +  (sum( (DVSwap *  SwapsRV2DDebtCleanOpt), 1)) ) ";
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, SchuldTotalVec, "AddConstratintBondSwapFXRisk-SchuldTotalVec");
				fexpr= "(1/" + SchuldTotalVec + ") .* (" + fexpr +")";
				fexpr= "("+fexpr +") * 1000.0d";
				
				
				//-- reduce the constraint for NumYear and initStartRefYear --// 
				fexpr = "( "+ fexpr + " ) .* ( MatrixColVecMonthsNumYearReduction )" ;
			    //double [][] valBondSwapFxRiskConstraintArr= (double[][]) jom.parseExpression(fexpr).evaluate().toArray();
				//this.oxl.genTxtFileFrom2DDataFmt(filepath, "DV0-exprBondSwapFxRiskConstraintArr-F", valBondSwapFxRiskConstraintArr,10);
				
				//--//this.oxl.genTxtFileFrom2DDataFmt(filepath, "FX-DV-B0-S0-FormBondSwapFxRiskConstraintArr-F", valBondSwapFxRiskConstraintArr,10);
				
				
				
				//Issue --- sum operator does not work -- fexpr= "sum( (" + fexpr + "), 2)";	 Exception derivative not found; IllgelaIndex Exception;
				//--- Alternatively transpose ones for sum also fail with JOM Exception Check feasibility error.
				/// --- Error Message: Check feasibility error. Constraint number 44 has a value (left hand side minus right hand side): 0.04030126239746323, 
				//--- 	, which is outside the feasibility limits [-1.7976931348623157E308,0.0]
				//fexpr= "sum("+fexpr +")";  --- Alternatively: fexpr= "sum("+fexpr +", 2)"; -- Alternatively : fexpr= fexpr + " * (ones([1;36]))'";
				
				
				//fexpr = "sum("+fexpr +")";
				//ojbfunc_optimize_dispDVExpr(fexpr, "** Check Reduction: Sum - AddConstratintSwapFxRisk-fexpr-AfterSum");
				
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, fexpr, "AddConstratintBondSwapFxRisk-fexpr-normalized-tansposed");
				
				return fexpr;
			}
			/////////////////////////////////////////////////////////////////////
			
			
			
			/////////////////////////////////////////////////////////////////////
			public void ojbfunc_optimize_addConstraintBondSwapFxRisk() {
							
				ojbfunc_optimize_setupJOMBondSwapFxRisk();
				
				
				//--Set up SwapRisk constraint with Decision-Vars:  - Fails with non-linear constraint. //			
				//-- ! Does not work with non-linear constraint! -Error-"main" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0//
				String fexpr = ojbfunc_optimize_formBondSwapFxRiskConstraintExp();
				
				//-- sum and multiply with 1000.0d, if not already done  --//
				fexpr = "sum("+fexpr +")";
				//fexpr= "("+fexpr +")' * 1000.0d";
				
				
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, fexpr, "AddConstratintBondSwapFxRisk-fexpr-all");
				System.out.println(" AddConstratintBondSwapFxRisk -  Expression evaluation for BondSwapFxRisk Constraint: "+ jom.parseExpression(fexpr).evaluate());
				
				
				// -- No Fx Limit ---//
				//double [][] SwapRiskLimitArr= (double[][]) jom.getInputParameter("SwapRiskLimit").toArray();						
				//this.oxl.genTxtFileFrom2DData(filepath, "SwapRiskLimitArr.txt", SwapRiskLimitArr);			
				//ojbfunc_optimize_dispDVExpr("SwapRiskLimit", "AddConstratintSwapRisk-SwapRiskLimit-fexpr2");
				//System.out.println(" AddConstratintSwapRisk SwapRiskLimit evaluate" + this.jom.parseExpression("SwapRiskLimit").evaluate() );
							
				
				//--For 10 years accumulated FxRisk using JOM Limit--//
				//double [][] allSwapFXLimit = new double[1][this.ruleConf.numDateRows]; //--0.74=limit per month   -- --8.88 per year-- //
				//for (int i=0; i<this.ruleConf.numDateRows; i++) allSwapFXLimit[0][i] = 0.74;
				//jom.setInputParameter("SwapFXLimitOpt", new DoubleMatrixND (allSwapFXLimit) );
				//jom.setInputParameter("MeanAllSwapFXLimitOpt", new DoubleMatrixND (0.74) );
				
				double [][] sumAllSwapFXLimit = new double[1][1];
				sumAllSwapFXLimit[0][0] = (0.74 * this.ruleConf.numDateRows);
				jom.setInputParameter("SumAllSwapFXLimitOpt", new DoubleMatrixND (sumAllSwapFXLimit) );
				
				//allSwapFXLimit[0][0] = allSwapFXLimit[0][0] * (ruleConf.numDateRows);
				//this.SwapFxRiskConstStr= " (" + fexpr + ") <= SwapFXLimitOpt(0,all) " ;
				
				String SwapFxRiskConstStr= " (" + fexpr + ") <= SumAllSwapFXLimitOpt " ;
				System.out.println(" AddConstratintBondSwapFXRisk -  SwapFxRisk Constrainted by SumAllSwapFXLimitOpt: "+ jom.parseExpression("SumAllSwapFXLimitOpt").evaluate());
				System.out.println(" AddConstratintBondSwapFxRisk -  Final Expression for SwapFxRisk Constraint: ("+ fexpr + ") <= " + sumAllSwapFXLimit[0][0]);			
				System.out.println(" AddConstratintBondSwapFxRisk -  Final Raw Expression for SwapFxRisk Constraint: " + SwapFxRiskConstStr);
				this.jom.addConstraint(SwapFxRiskConstStr);
						
				
				// -- Add the relational operator --//
				//this.SwapFxRiskConstStr= " (" + fexpr + ") <= 1.2d" ; //--java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 -- Arising due to sqrt //
				//String fexpr1= "(" + fexpr + ")(0,0:6) <= SwapFxRiskLimit(0,0:6) " ; //--Illegal Argument Exception //			
				// --  java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 --//
				//String fexpr1= "( (" + fexpr + ")(0,0) )" + " <= (SwapFxRiskLimit(0,0)) " ; //--illegal Argument Exception //	
									
			}
			// Bond+Swap Fixing Risk //
			/////////////////////////////////////////////////////////////////////
			
			
			/////////////////////////////////////////////////////////////////////
			public void ojbfunc_optimize_addConstraintBondVol() {
				//30	0	45	34	44	6	0	5	0	2	200	0	62	46	54	16	0	9	0	3
				//DnLimit		30		45	34	44	6		5		2	;
				//UpLimit = 	200		62	46	54	16		9		3  ;
				
				double [] zeroLimitBondVol7 = { 0, 0, 0, 0, 0, 0, 0 };
				
				double [] UpLimitBondVol7 = { 200, 62, 46, 54, 16, 9, 3 };
				double [] DnLimitBondVol7 = { 30, 45, 34, 44, 6, 5, 2 }; 
												
				this.UpLimitBondVol = new double[1][ruleConf.numDecisionVariables];;
				this.DnLimitBondVol = new double[1][ruleConf.numDecisionVariables];;
				this.UpLimitBondVol[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((UpLimitBondVol7), (this.ruleConf.numDecisionVariables)/7 );
				this.DnLimitBondVol[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((DnLimitBondVol7), (this.ruleConf.numDecisionVariables)/7);
				
				if (this.dataSet.initStartRefYear == 2018) {
					for (int i=0; i<7; i++) {
						this.UpLimitBondVol[0][i]= 0.0001;
						this.DnLimitBondVol[0][i]= -0.0001;
					}
				}
				if (this.dataSet.initStartRefYear == 2019) {
					for (int i=0; i<14; i++) {
						this.UpLimitBondVol[0][i]= 0.0001;
						this.DnLimitBondVol[0][i]= -0.0001;
					}
				}
				
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "DV0_UpBondVolConstraintArr.txt", this.UpLimitBondVol, 10);
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "DV0_DownBondVolConstraintArr.txt", this.DnLimitBondVol, 10);
				
				jom.setInputParameter("upBondVolConstraint", new DoubleMatrixND(this.UpLimitBondVol) );				
				jom.setInputParameter("downBondVolConstraint",new DoubleMatrixND(this.DnLimitBondVol) );
				
				this.jom.addConstraint("DVBond <= upBondVolConstraint");
				this.jom.addConstraint("DVBond >= downBondVolConstraint");
			}
			/////////////////////////////////////////////////////////////////////
			
			
			
			/////////////////////////////////////////////////////////////////////
			private void  ojbfunc_optimize_SomeExpr_FunctionBond() {
				//String BondExprConstraint = ojbfunc_optimize_formBondV1ObjFunctExp();
				
				//-- old --//String bondExpr = "(DVBond - EBDVBond) * BondsRVDataTransformed3D"; //-- [1,70] - [1,70] * [[70x120x79]]
				
				String bondExpr = "UnitMatrixDVBond"; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "UnitMatrixDVBond-bondExpr-ObjFunct0-1");
				double[][] UnitMatrixDVBondArr = (double[][]) jom.getInputParameter("UnitMatrixDVBond").toArray(); 
				this.oxl.genTxtFileFrom2DData(filepath, "UnitMatrixDVBondArr.txt", UnitMatrixDVBondArr);
				
				bondExpr = "( EBDVBond  ) "; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct0-2");
								
				bondExpr = "( DeltaHeadMatrixInstr  ) "; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct0-3");
				
				
				
				///--- Optimization - Objective Function ---///
				
				//bondExpr =  " sum( (EBDVBond * DeltaHeadMatrixInstr),1)  "; //-- [1,70] - [1,70] * [[70x120x79]]
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct1");
				
				
				//jom.setInputParameter("DeltaMatrixInstrAllIn","permute(DeltaMatrixInstrAllIn,[1;3;2])" );
				//TransformedBondWeightKey * DeltaHeadMatrixInstrRaw
				/*
				bondExpr =  " (TransformedBondWeightKey * DeltaHeadMatrixInstrRaw)  "; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-WtDelta-ObjFunct1");
				
				bondExpr =  "( (TransformedBondWeightKey * DeltaHeadMatrixInstrRaw) * EBDVBond' )"; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-WtDelta-ObjFunct11");
				
				
				bondExpr =  "sum( ( (TransformedBondWeightKey * DeltaHeadMatrixInstrRaw) * EBDVBond' ), 3)"; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-WtDelta-ObjFunct111");
				
				bondExpr =  "sum( sum( ( (TransformedBondWeightKey * DeltaHeadMatrixInstrRaw) * EBDVBond' ), 3), 2)"; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-WtDelta-ObjFunct1111");
				*/
				
				/////////////////////////
				
				bondExpr =  " EBDVBond "; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-EBDVBond");

				bondExpr =  " DeltaHeadMatrixInstrEff "; //--  [[10x70x10]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-DeltaHeadMatrixInstrEff");
								
				bondExpr = "( UnitMatrixDVBond ) "; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-UnitMatrixDVBond");
								
				bondExpr =  " sum( (EBDVBond * DeltaHeadMatrixInstrEffNumYear), 3)  "; //-- [1,10] - [1,10] * [[10x70x10]] = 1,70,10
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-EBDVBondINTODeltaHeadMatrixInstrEff");
				
				/////////////////////////
				
				
				bondExpr = "( UnitMatrixDVBond - " + bondExpr + " ) "; //-- [1,70] - [1,70] * [[70x120x79]]
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct2");
								
				bondExpr =  " DVBond .* ( " +  bondExpr + " )";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct3");
				
				double [][] NetBondVolWithoutBaukstArr= (double[][]) jom.parseExpression(bondExpr).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "NetBondVolWithoutBaukstArr-F", NetBondVolWithoutBaukstArr,10);
				
				bondExpr = "( " + bondExpr + " ) * BondsRVDataTransformed3D";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct4");
				
				
				
				bondExpr= "sum( " + bondExpr + ") ";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct5");
				
				
				// --  Equation 44 -- optimization.pdf ---//
				///----------Scalar function as OF : Sum --------------///
				//bondExpr= "sum( " + bondExpr + ", 3 )";
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct4");
				//bondExpr= "sum( " + bondExpr + ", 2 ) + 0.0000000001";
				//this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, bondExpr, "bondExpr-ObjFunct5");
				
			}
			/////////////////////////////////////////////////////////////////////
			
			/////////////////////////////////////////////////////////////////////
			//--- Add Objective Function -- Mehrkosten -- Bond+Swap --- for future::::. //
			public String  ojbfunc_optimize_formBondSwapMehrkostenObjFunctExp() {
						
				//--old--//String PVTotalVec= " ((PVCleanOpt) + (sum( (decisionVarsBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (decisionVarsSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";		
				
				//String PVTotalVec= " ((PVCleanFullOpt) + (sum( (decisionVarsBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (decisionVarsSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				//String PVTotalVec= " ((PVCleanFullOpt) + (sum( (DVBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				
				//--good--//String PVTotalVec= " ((PVCleanFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" *  BondsRV2DPVCleanOpt), 1)) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				//String PVTotalVec= " ((PVCleanFullOpt) + (sum( ( "+ ojbfunc_optimize_formBondSwapNetDV() +" *  " + this.BondDeltaBkstTensor_BondPVClean_Opt	+ "), 1)) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				String PVTotalVec= " ((PVCleanFullOpt) + (  " + this.BondDeltaBkstTensor_BondPVClean_Opt + "  ) + (sum( (DVSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
				
				
				PVTotalVec = "(" + PVTotalVec + ")";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, PVTotalVec, "Mehrkosten-pvTotalVec + BOndSwap");
						
				//-- build a matrix of PVTotalOptArr, where 1st-col of 120 rows are copied to 31 cols --// -- [ ((this.ruleConf.numYears) * (this.ruleConf.monthsInYear)), 31] --//
				String PVTotalMatrix =  PVTotalVec + " *  ones([1;31])";
				PVTotalMatrix = "(" + PVTotalMatrix + ")";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, PVTotalMatrix, "Mehrkosten-PVTotalMatrix");
						
						
				String PVTotalRefSensi =  PVTotalMatrix + " .* RefPCSensiOpt " ;
				PVTotalRefSensi = "(" + PVTotalRefSensi + ")";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, PVTotalRefSensi, "Mehrkosten-PVTotalRefSensi");
						
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "BondsRV2DOpt", "BondExpr-BondsRV2DOpt+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "BondsRV2DPVCleanOpt", "BondExpr-BondsRV2DPVCleanOpt+###");
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, "BondsRV2DDebtCleanOpt", "BondExpr-BondsRV2DDebtCleanOpt+###");
				
				
				//String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( sum( (DVBond) *  BondsRV2DOpt ,1) + sum( (DVSwap) *  SwapsRV2DOpt ,1)  )  + BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
				
				//--good--//String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( sum( ( " + ojbfunc_optimize_formBondSwapNetDV() + "  ) *  BondsRV2DOpt ,1) + sum( (DVSwap) *  SwapsRV2DOpt ,1)  )  + BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
				
				//String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( sum( ( " + ojbfunc_optimize_formBondSwapNetDV() + "  ) *  "+ this.BondDeltaBkstTensor_BondRV_Opt +" ,1) + sum( (DVSwap) *  SwapsRV2DOpt ,1)  )  + BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
				
				String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( " + this.BondDeltaBkstTensor_BondRV_Opt + " + sum( (DVSwap) *  SwapsRV2DOpt ,1)  )  + BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
				
				
				BondSwapMehrKosten= "(" + BondSwapMehrKosten + ") * 10000.0d";
				this.functUtil.ojbfunc_optimize_dispDVExpr(this.jom, BondSwapMehrKosten, "Mehrkosten-SwapMehrKosten");
				
				double [][] BondSwapMehrKostenArr= (double[][]) jom.parseExpression(BondSwapMehrKosten).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "MK_DV-BDV3-20-BDVall-0-S0-BondSwapMehrKostenArr-F", BondSwapMehrKostenArr,10);
				
				return BondSwapMehrKosten;
			}
			/////////////////////////////////////////////////////////////////////		
			
			
				
			/////////////////////////////////////////////////////////////////////
			private void  ojbfunc_optimize_Kosten_objectiveFunctionBond() {
				
				String BondSwapMehrKosten = ojbfunc_optimize_formBondSwapMehrkostenObjFunctExp();
				BondSwapMehrKosten= "sum( " + BondSwapMehrKosten + ") ";
				this.jom.setObjectiveFunction("minimize",  BondSwapMehrKosten);
				
			}
			//--- Add Objective Function -- Net Volume -- over!  --- //
			
				    	
		//clean JOM - Extra data loaded during init to buil the expressions; NOTE: Final expression should not be deleted. //
		public void ojbfunc_optimize_swap_cleanJOM() 	{
			jom.setInputParameter("BondsRV2DOpt", 0  );
			//jom.setInputParameter("SwapsRV2DOpt", 0  );
			
			jom.setInputParameter("SwapsRV2DAnnSensiOpt", 0  ); 
			jom.setInputParameter("SwapFxRiskOpt", 0  ); 
			jom.setInputParameter("SwapSensiRiskOpt", 0  ); 
			jom.setInputParameter("BondILBSwapFxRiskOpt", 0  ); 
			
			jom.setInputParameter("SwapSensiRiskFullOpt", 0  ); 
			jom.setInputParameter("SwapFxRiskFullOpt", 0  ); 
			jom.setInputParameter("FxRiskFullOpt", 0  ); 
			
			jom.setInputParameter("BondsRV2DAnnSensiOpt", 0  ); 
			//jom.setInputParameter("PCRP1Opt", 0  );
			//jom.setInputParameter("SchuldFullOpt", 0  ); 
			//jom.setInputParameter("BestandFullOpt", 0  ); 
		}
		
		public void ojbfunc_optimize_bond_cleanJOM() 	{
			
			System.out.println ("************** Cleaning JOM for running Solver **************");
			this.jom.setInputParameter("BondDeltaBkstTensor", 0 ); 
			this.jom.setInputParameter("DeltaMatrixInstrAllIn", 0  ); // --- [70, 51, 51] --//
			
			this.jom.setInputParameter("DeltaMatrixInstrAllInNumYear", 0 ); // --- [70, 10, 10] --//				
			
			//jom.setInputParameter("TransformedBondWeightKey", 0  );
			
			//jom.setInputParameter("DeltaMatrixInstr", 0  ); // --- [1428, 51, 51] --//
			//jom.setInputParameter("DeltaMatrixInstrNumYear", 0 ); // --- [1428, 10, 10] --//
			//jom.setInputParameter("DeltaMatrixInstrEff", 0 ); // --- [70, 10, 10] --//
			//jom.setInputParameter("DeltaMatrixInstrEffNumYear", 0);	
			
			
			//-- DeltaTwiddleMatrixInstrEff --//
			this.jom.setInputParameter("DeltaTwiddleMatrixInstr", 0);
			
			this.jom.setInputParameter("DeltaTwiddleMatrixInstrNumYear", 0);// --- [10, 70, 10] --//
			this.jom.setInputParameter("DeltaTwiddleMatrixInstrEff", 0 );
			this.jom.setInputParameter("DeltaTwiddleMatrixInstrEffNumYear", 0 );			
			
			this.jom.setInputParameter("DeltaTwiddleMatrixInstrRaw", 0  );// --- [70, 51, 51] --//
			this.jom.setInputParameter("DeltaTwiddleMatrixInstrRawTNS", 0 );
			this.jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYear", 0);
			this.jom.setInputParameter("DeltaTwiddleMatrixInstrRawNumYearTNS", 0);
			
			this.jom.setInputParameter("DeltaHeadMatrixInstrRaw", 0 );// --- [70, 51, 51] --//
			this.jom.setInputParameter("DeltaHeadMatrixInstrRawNumYear", 0 );
							
			this.jom.setInputParameter("DeltaHeadMatrixInstr", 0  );
			this.jom.setInputParameter("DeltaHeadMatrixInstrNumYear", 0);// --- [10, 70, 10] --//
			
			//-- DeltaHeadMatrixInstrEff --//
			this.jom.setInputParameter("DeltaHeadMatrixInstrEff", 0 );
			this.jom.setInputParameter("DeltaHeadMatrixInstrEffNumYear", 0);

		}
		public void ojbfunc_optimize_bond_cleanJOM_Final() 	{
			this.jom.setInputParameter("TransformedBondWeightKey", 0  );
			this.jom.setInputParameter("DeltaMatrixInstr", 0  ); // --- [1428, 51, 51] --//
			this.jom.setInputParameter("DeltaMatrixInstrNumYear", 0 ); // --- [1428, 10, 10] --//
			this.jom.setInputParameter("DeltaMatrixInstrEff", 0 ); // --- [70, 10, 10] --//
			this.jom.setInputParameter("DeltaMatrixInstrEffNumYear", 0);	
			
			this.jom.setInputParameter("BondDeltaBkstTensor_BondRV_5_35", 0 );
			this.jom.setInputParameter("BondDeltaBkstTensor_BondFx_43_73", 0 ); 
			this.jom.setInputParameter("BondDeltaBkstTensor_BondPVClean_2_2", 0 );
			this.jom.setInputParameter("BondDeltaBkstTensor_BondDebtClean_1_1", 0 );
			
		}
		
		//-- Invoke bond Optimizer --//
    	public void ojbfunc_optimize_bond_invokeSolver() 	{    		
    		//-- invoke the optimizer to solve the objective function --//			
			//functUtil.calcValueObjFunctForZero(this.jom, this.dataSet, this.ruleConf.numDecisionVariables);
			System.out.println ("************** Optimization for Years: " +  (this.ruleConf.numYears) + " **************");
			functUtil.solveObjectiveFunction(this.jom, false);
			
			functUtil.printOptimalResult(this.jom);			
			
			System.out.println ();
			
			/*
			//--Test code --
			int numDV = this.ruleConf.numDecisionVariables;
    			//double [] initValDV7V = { 2.0, 2, 2, 2, 2, 2,	2 };
			double [] initValDV7V = { 3.0, 3, 3, 3, 3, 3,	3 };
			double[][] initDV = new double[1][numDV];
			initDV[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDV7V), (numDV / 7));
			this.jom.setInputParameter("initDVOpt", new DoubleMatrixND(initDV));
			this.jom.setInputParameter("decisionVars", "initDVOpt");
		
			this.jom.setInputParameter("dvSolution", this.jom.getPrimalSolution("decisionVars"));
			this.jom.setInputParameter("decisionVars", "dvSolution");
			double [][] DVObjFuncArr = (double[][]) this.jom.getInputParameter("dvSolution").toArray();
			this.oxl.genTxtFileFrom2DData(filepath, "DV-ObjFunc.txt", DVObjFuncArr);
			*/
		}
    	
    	
    	
		private void ojbfunc_optimize_printBondOptimalVal() {
		
			//-- Print the optimized Val --///
			System.out.println("************************ OPTIMIZED DV ***********************"); 
			System.out.println("*** System Optimal Value; Internal Optimal Value of Obj-Function =  "+ this.jom.getOptimalCost ());
			System.out.println ("** Optimized Decision Variables: DVBond = " +   this.jom.getPrimalSolution("DVBond").toString() );
			System.out.println ("** Optimized Decision Variables: DVSwap = " +   this.jom.getPrimalSolution("DVSwap").toString() );
			System.out.println ("** Optimized Decision Variables: EBDVBond  = " +   this.jom.getPrimalSolution("EBDVBond").toString() );
			
			double[] optimalDVArr = (double[]) this.jom.getPrimalSolution("DVBond").to1DArray();  
			this.oxl.genTxtFileFrom1DData(filepath, "Optimized-DV-F.txt", optimalDVArr);  
			
			//--- Use the optimal soltuiona to find the value of constraints and objective function ---//
			this.jom.setInputParameter("DVBondSolution", this.jom.getPrimalSolution("DVBond"));
			this.jom.setInputParameter("DVSwapSolution", this.jom.getPrimalSolution("DVSwap"));
			this.jom.setInputParameter("EBDVBondSolution", this.jom.getPrimalSolution("EBDVBond"));
			

			this.jom.setInputParameter("DVBond", "DVBondSolution");
			this.jom.setInputParameter("DVSwap", "DVSwapSolution");
			this.jom.setInputParameter("EBDVBond", "EBDVBondSolution");
			this.jom.setInputParameter("decisionVars", "DVBondSolution");
						
			//-- replace expression-Val with DVSolution   --//
			test_fileIdent="Optimal_";
			ojbfunc_optimize_formBondComplexExpr();
						
			System.out.println("************************ OPTIMIZED NetDVBond ***********************");
			//-- optimized Expression  --//
			String NetBondDVExpr = ojbfunc_optimize_formBondSwapNetDV();
			
			double[][] NetDVBondArr = (double[][]) jom.parseExpression(NetBondDVExpr).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath,"Optimized-BondSwapNetDV-F.txt", NetDVBondArr,10);
			System.out.println ("** Optimized NetBondDV  = " +   jom.parseExpression(ojbfunc_optimize_formBondSwapNetDV()).evaluate().toArray() );
			
			
			
			
			System.out.println("************************ OPTIMIZED CONSTRAINTS ***********************"); 
				
			// --- print the optimal constraints and objfunct --//
			// Constraint Bond-FinLucke --//
			if (OnConstBondFinLuecke) {
				String BondFinLuecke = ojbfunc_optimize_formBondFinLueckeConstraint();
				double [][] BondFinLueckeConstArr = (double[][]) jom.parseExpression(BondFinLuecke).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath,"Optimized-BondFinLueckeConstArr-F.txt", BondFinLueckeConstArr,10);
	
				BondFinLuecke= "sum( " + BondFinLuecke + ") ";
				double [][] BondFinLueckeVal = (double[][]) jom.parseExpression(BondFinLuecke).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "Optimized-BondFinLueckeVal-F.txt", BondFinLueckeVal, 10);
			}
			
			// Constraint Bond-EigenBestand --//
			if (OnConstBondEigenBestand) {
				String BondEigenBestand = ojbfunc_optimize_formEigenBestandConstraintBond();
				double [][] EigenBestandConstraintArr= (double[][]) jom.parseExpression(BondEigenBestand).evaluate().toArray(); 
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "Optimized-EigenBestandConstraintArr-F.txt", EigenBestandConstraintArr, 10); // -- size: 60X1 --//
				
				BondEigenBestand= "sum( " + BondEigenBestand + ") ";
				double [][] BondEigenBestandVal = (double[][]) jom.parseExpression(BondEigenBestand).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "Optimized-BondEigenBestandVal-F.txt", BondEigenBestandVal, 10);
			}
			
			// Constraint BondSwap FX Risk --//
			if (OnConstBondSwapFxRisk) {
				String BondSwapFxRisk = ojbfunc_optimize_formBondSwapFxRiskConstraintExp();
				//BondSwapFxRisk  = "sum( " + BondSwapFxRisk + ") " ;
				double [][] BondSwapFxRiskArr = (double[][]) jom.parseExpression(BondSwapFxRisk).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "Optimized-BondSwapFxRiskConstArr-F.txt", BondSwapFxRiskArr, 10);
				
				BondSwapFxRisk= "sum( " + BondSwapFxRisk + ") ";
				double [][] BondSwapFxRiskVal = (double[][]) jom.parseExpression(BondSwapFxRisk).evaluate().toArray();
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "Optimized-BondSwapFxRiskConstVal-F.txt", BondSwapFxRiskVal, 10);
				
			}
			
			System.out.println("************************ OPTIMIZED OBJECTIVE FUNCTION ***********************");
			// Objective Function -  BondSwap Mehrkosten --//
			String BondSwapMehrKosten = ojbfunc_optimize_formBondSwapMehrkostenObjFunctExp();
			double [][] BondSwapMehrKostenArr= (double[][]) jom.parseExpression(BondSwapMehrKosten).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Optimized-BondSwapMehrKostenArr-F.txt", BondSwapMehrKostenArr, 10);

			BondSwapMehrKosten= "sum( " + BondSwapMehrKosten + ") ";
			double [][] BondSwapMehrKostenVal= (double[][]) jom.parseExpression(BondSwapMehrKosten).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "Optimized-BondSwapMehrKostenVal-F.txt", BondSwapMehrKostenVal, 10);
			
			//-- final display of val --//
			System.out.println ("**  Objective Function : Calculated Val ** : " +  " # System Evaluattion # : " + BondSwapMehrKostenVal[0][0] );
			System.out.println ();
			//System.out.println ("**  Objective Function : System Theoretical Val ** : " +  " # System Evaluattion # : " + jom.getObjectiveFunction().toString() );
			System.out.println ("**  Objective Function : System Theoretical Val ** : " +  " # System Evaluattion # : " + jom.getOptimalCost() );
			System.out.println ();
			
		} //-- printBondOptimalVal --//
    	
    	
		
    	public void setUpBondJOM(){
    			
    		if (this.jom == null)
    			this.jom = new OptimizationProblem();
    		
			jom.resetTimer();
			jom.addDecisionVariable("decisionVars", false, new int[] {1, (this.ruleConf.numDecisionVariables) } ); //357 for 51 years annually - 168
			jom.addDecisionVariable("DVBond", false, new int[] {1, (this.ruleConf.numDecisionVariables) } ); //357 for 51 years annually - 168
			jom.addDecisionVariable("EBDVBond", false, new int[] {1, (this.ruleConf.numYears ) } ); //357 for 51 years annually - 168
			jom.addDecisionVariable("DVSwap", false, new int[] {1, (this.ruleConf.numDecisionVariables) } ); //357 for 51 years annually - 168
			initJOMDV = false;
			
			//-- validated DV ---//
			boolean validateSetupDV=false;
			if (validateSetupDV) {
				//double [] initValDVBond7V = { 20.0, 20, 20, 20, 20, 20, 20 }; //--- 20 per Year; ---//
				double [] initValDVBond7V = { 0.0, 0, 20.0, 0, 0, 0, 0 }; //--- 20 per Year; ---//
				
				//double [] initValDVSwap7V = { 1.0, 1, 1, 1, 1, 1, 1 }; // --- 4 per Year; ---//
				double [] initValDVSwap7V = { 0.0, 0, 0, 0, 0, 0, 0 }; //
				
				double[][] initDVBond = new double[1][this.ruleConf.numDecisionVariables];
				double[][] initEDVBond = new double[1][this.ruleConf.numYears];
				double[][] initDVSwap = new double[1][this.ruleConf.numDecisionVariables];
				initDVBond[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVBond7V), this.ruleConf.numDecisionVariables/ 7 );
				initDVSwap[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVSwap7V), this.ruleConf.numDecisionVariables/ 7 );
				//initEDVBond[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVBond7V), this.ruleConf.numDecisionVariables/ 1 );
				
				jom.setInputParameter("DVBond", new DoubleMatrixND(initDVBond));
				jom.setInputParameter("EBDVBond", new DoubleMatrixND(initEDVBond));
				jom.setInputParameter("DVSwap", new DoubleMatrixND(initDVSwap));
				
			}	
			
			//-- Activate-Deactivate BondDV ---//
			boolean setupBondDV=true;  
			if (setupBondDV) {
				double [] initValDVBond7V = { 0.0, 0, 0, 0, 0, 0, 0 }; 
				//double[][] initDVBond = new double[1][this.ruleConf.numDecisionVariables];
				double[][] initDVBond = new double[1][7];
				initDVBond[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVBond7V), 1 );
				jom.setInputParameter("DVBond(0, 0:6)", new DoubleMatrixND(initDVBond));
			}
			
			//-- Activate-Deactivate setupEBDVBond  ---//
			boolean setupEBDVBond=true;  
			if (setupEBDVBond) {
				    
				if (this.dataSet.initStartRefYear == 2018) {
					double [][] initValEDVBondPartly = new double[1][1];
					initValEDVBondPartly[0][0] = 0.0;
					jom.setInputParameter("EBDVBond([0],[0])", new DoubleMatrixND(initValEDVBondPartly)); //-- to setup first year EBDVBond to zero, Eingebsatnd constraint must be active --///
				}
				if (this.dataSet.initStartRefYear == 2019) {
					double [][] initValEDVBondPartly = new double[1][2];
					initValEDVBondPartly[0][0] = 0.0; initValEDVBondPartly[0][1] = 0.0;
					jom.setInputParameter("EBDVBond(0,0:1)", new DoubleMatrixND(initValEDVBondPartly)); //-- to setup first year EBDVBond to zero, Eingebsatnd constraint must be active --///
				} 
				
				//-- to setup all EDVBond to zero, Eingebsatnd constraint must be disabled --///
				boolean AllEBDVZero= true;
				//if (!OnConstBondEigenBestand) {
				if (AllEBDVZero) {
					double[][] initEBDVBond = new double[1][this.ruleConf.numYears]; 
					for (int i=0; i< this.ruleConf.numYears; i++) {
						initEBDVBond[0][i] = 0.0;
					}
					//jom.setInputParameter("EBDVBond", new DoubleMatrixND(initEBDVBond));
				}
								
			}
			
			
			
			//-- Activate-Deactivate SwapDV ---//
			boolean setupSwapDV=true;  
			if (setupSwapDV) {
				double [] initValDVSwap7V = { 0.0, 0, 0, 0, 0, 0, 0 }; 
				double[][] initDVSwap = new double[1][this.ruleConf.numDecisionVariables];
				initDVSwap[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVSwap7V), this.ruleConf.numDecisionVariables/ 7 );
				jom.setInputParameter("DVSwap", new DoubleMatrixND(initDVSwap));
			}
			
		}
    	///////////////////// Init BondDV , EBDV, SwapDV //////////////////////
    	
    	
    	private void testRoutineReturn(){
    		
    		double[][] initEBDVBond = new double[1][this.ruleConf.numYears]; 
			for (int i=0; i< this.ruleConf.numYears; i++) {
				initEBDVBond[0][i] = 1.0;
			}
			jom.setInputParameter("EBDVBond", new DoubleMatrixND(initEBDVBond));
			System.out.println ("** Optimized EBDVBond  = " +   jom.parseExpression("EBDVBond").evaluate().toString() );
			boolean setupBondDV=true;  
			if (setupBondDV) {
				double [] initValDVBond7V = { 1.0, 1, 1, 1, 1, 1, 1 }; 
				//double[][] initDVBond = new double[1][this.ruleConf.numDecisionVariables];
				double[][] initDVBond = new double[1][this.ruleConf.numDecisionVariables];
				initDVBond[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initValDVBond7V), this.ruleConf.numYears );
				jom.setInputParameter("DVBond", new DoubleMatrixND(initDVBond));
			}
			System.out.println ("** Optimized DV  = " +   jom.parseExpression("DVBond").evaluate().toString() );
			
			System.out.println("************************ Test Routine Return : NetDVBond ***********************");
			//-- optimized Expression  --//
			String NetBondDVExpr = ojbfunc_optimize_formBondSwapNetDV();
			
			double[][] NetDVBondArr = (double[][]) jom.parseExpression(NetBondDVExpr).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath,"TestReturn-BondSwapNetDV-F.txt", NetDVBondArr,10);
			System.out.println ("** Test Return NetBondDV  = " +   jom.parseExpression(ojbfunc_optimize_formBondSwapNetDV()).evaluate().toString() );
			
			System.out.println ("** Optimized Decision Variables: DVBond = " +   this.jom.parseExpression("DVBond").evaluate().toString() );
			System.out.println ("** Optimized Decision Variables: DVSwap = " +   this.jom.parseExpression("DVSwap").evaluate().toString() );
			System.out.println ("** Optimized Decision Variables: EBDVBond  = " +   this.jom.parseExpression("EBDVBond").evaluate().toString());
    	}
    	
    	
		// -- ojbfunc_optimize_ProcessBondJOM --//
		public void ojbfunc_optimize_ProcessBondJOM(optimSolveFunctApp appMainObj) {
			
			//-- InvokeSolver; configure the constraints, invokeSolver --//
			//-- Not required -- 
			boolean onTestRoutineReturn = false;/// 
			///////////////////////////////////////
			
			boolean onInvokeSolver=false;
			
			OnConstBondVol=true;
			OnConstBondSwapFxRisk= true;
			
			OnConstBondEigenBestand=true; 
			OnConstBondFinLuecke=true;
			
			
			test_fileIdent="DV0_";
			
			//-- init with rule setup--//
			if (!this.initAppRule) ojbfunc_optimize_rule_optimExprBond(appMainObj);
			
			//-- initailize JOM Process --///
			 if (!initJOMDV)	setUpBondJOM();
			 //if (!initJOMDV) if (appMainObj.jom == null) setUpBondJOM();
			 
			//-- calculate delta matrix ---//
			buildBondInstrsDeltaMatrices(appMainObj);
			buildReducedDeltaBaukasten();
			
			//--clean the 2D object to release some memory--//
    		this.dataSet.bondsRVDataRaw=null;
    		this.dataSet.swapsRVDataRaw=null;
    		System.gc();
									
			//-- Load JOM Expr --//
			ojbfunc_optimize_loadBondExprJOM();
			ojbfunc_optimize_initBondDeltaBaukastenEBTensor();
			ojbfunc_optimize_loadJOM_EigenBestandBondTensorOpt();
			
			ojbfunc_optimize_formBondComplexExpr();
			
			//--pre setup for FinLuecke , Eigenbestand,  FixRisk, NetDV, Mehrkosten --//
			String exprBondSwapNetDV = ojbfunc_optimize_formBondSwapNetDV(); // -- Required for the FxLuecke --//
			double [][] DV0_BondSwapNetDVArr= (double[][]) jom.parseExpression(exprBondSwapNetDV).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "DV0_BondSwapNetDV_DV0.txt", DV0_BondSwapNetDVArr,10);
			
			if (onTestRoutineReturn) testRoutineReturn();
			
			//----------------------------------------------//
			//-- Bond Vol Contraints --//
			if (OnConstBondVol) {
				ojbfunc_optimize_addConstraintBondVol();
			}
			
			//-- Set up Bond FinLuecke constraint --//
			if (OnConstBondFinLuecke) {
				ojbfunc_optimize_addConstraintBondFinLuecke();
				
				//String exprFinLueckeConstraint =ojbfunc_optimize_formBondFinLueckeConstraint();
				//double [][] DV0_FinLueckeConstraintArr= (double[][]) jom.parseExpression(exprFinLueckeConstraint).evaluate().toArray();
				//this.oxl.genTxtFileFrom2DDataFmt(filepath, "DV0_FinLueckeConstraint_F.txt", DV0_FinLueckeConstraintArr,10); 
			}
			
			//-- set up Bond Fx Risk  Constraint --//
			if (OnConstBondSwapFxRisk) {
				ojbfunc_optimize_addConstraintBondSwapFxRisk(); 
				
				//String exprFXRiskConstraint = ojbfunc_optimize_formBondSwapFxRiskConstraintExp();
				//double [][] DV0_BondSwapFxRiskConstraintArr = (double[][]) jom.parseExpression(exprFXRiskConstraint ).evaluate().toArray(); 
				//this.oxl.genTxtFileFrom2DDataFmt(filepath, "DV0_BondSwapFxRiskConstraint_F.txt", DV0_BondSwapFxRiskConstraintArr,10);
				
			}
						
			//---- set up EigenBestand Constraint ----///
			if (OnConstBondEigenBestand) {
				ojbfunc_optimize_addConstraintEigenBestandBond(); // -- Deactivate EigenBestand Constraint setting all EDVBOnd  to zero --//
				
				//String exprEigenBestandConstraint = ojbfunc_optimize_formEigenBestandConstraintBond();
				//double [][] DV0_BondExprEBConstraintArr= (double[][]) jom.parseExpression(exprEigenBestandConstraint).evaluate().toArray(); 
				//this.oxl.genTxtFileFrom2DDataFmt(filepath, "DV0_BondExprEBConstraint_F.txt", DV0_BondExprEBConstraintArr, 10);
			}
			
			
						
			//-- for default display ---//
			this.jom.addConstraint("( decisionVars == DVBond )");
			
			//-- set up Mehrkosten --//
			ojbfunc_optimize_formBondSwapMehrkostenObjFunctExp();
			
			//----------------------------------------------//
			if (onInvokeSolver) {
				
				//-- Set up Objective Function - BondExpr  --//
				ojbfunc_optimize_Kosten_objectiveFunctionBond();
				
				//-- Clean JOM -- else solver will crash --//
				//ojbfunc_optimize_bond_cleanJOM();
				//ojbfunc_optimize_swap_cleanJOM();
				
				//ojbfunc_optimize_bond_cleanJOM_Final();
				
				//--- invoke solver ---//
				ojbfunc_optimize_bond_invokeSolver();	
				
				//-- Display result --//
				ojbfunc_optimize_printBondOptimalVal();
				
			}
			//----------------------------------------------//
			
			//--Over & All--//
			ojbfunc_optimize_bond_cleanJOM_Final();
			
			System.out.println ("** Optimiztion for Years: " +  (this.numYearOpti) + " Done!");
			System.out.println ("**  Over & All! ** ");
			
			
		} //---  ojbfunc_optimize_ProcessBondJOM ---//
		///////////////////////////////////////////////////////////////////////////////
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	
    

    