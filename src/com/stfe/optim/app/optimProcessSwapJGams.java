
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


    public class optimProcessSwapJGams {
					
    	public String filepath;
    	public createOXLFromIBean oxl;
    	
    	public OptimizationProblem jom=null;
    	public optimSolveDataset dataSet=null;
		public optimSolveRuleConfig ruleConf=null;	
		public optimSolveFunctUtil functUtil=null;
		private boolean dataSetInitFlag=false;
		private boolean ruleConfigFlag=false;
		
		private double [][]jUPConstLtd;
		private double [][]jDNConstLtd;				
		
		
		private double[][][] joBondsRVDataEff; 
		private double[][][] joBondsRVAnnuitySensiEff;
		public double [][][] joBondsRVPVCleanEff;
		public double [][][] joBondsRVDebtCleanEff;
		
		private double[][][] joSwapsRVDataEff; 
		private double[][][] joSwapsRVAnnuitySensiEff;
		public double [][][] joSwapsRVPVCleanEff;
		public double [][][] joSwapsRVDebtCleanEff;
		
		private double [][] joBestandSwapTMEff;
		private double [][] joBestandBondTMEff;
		
		private double [][] joPVCleanSwapTMEff;
		private double [][] joPVCleanBondTMEff;
		
		private double [][] joBondRiskSensiTMEff;
		private double [][] joSwapRiskSensiTMEff;
		private double [][] joBondILBSwapFxRiskBondTMEff;
		private double [][] joBondILBSwapFxRiskSwapTMEff;
		private double [][] joSchuldBondTMEff;
		private double [][] joSchuldSwapTMEff;
		
		
		private double [][] joPVCleanEff;
		private double [][] joBestandEff; 
		
		
		private double stockVolLimitEff;
		private double[][]joSwapRiskLimitEff;
		
		private boolean setInitialSolutionFlag=false;
		private double[][] joInitDVSolSetJOM;
		private double[][] joInitDVSolSetXL;

		private double [][] joPCRP1Eff;
		private double [][] joSchuldEff;
		public double joSchuldVal;
		
		private double[][] joRefPCSensiEff;
		private double[][] joPCCov;
		
		private double[][] joSwapSensiRiskEff;
		private double[][] joSwapFxRiskEff;
		private double[][] joBondFxRiskEff;
		private double[][] joILBFxRiskEff;
		private double[][] joBondILBSwapFxRiskEff;
		
		private String SwapRiskConstStr;
		private String SwapFxRiskConstStr;
		
		private double [][]joPCCov_Mkt1;  
		private double [][]joPCRP_Mkt1;  
		private double [][]joFixRiskCov_Mkt1;
		private double [][]joPCCov_Mkt2;  
		private double [][]joPCRP_Mkt2;  
		private double [][]joFixRiskCov_Mkt2;
		private double [][]joPCCov_Mkt3;  
		private double [][]joPCRP_Mkt3;  
		private double [][]joFixRiskCov_Mkt3;
		private double [][]joPCCov_Mkt4;  
		private double [][]joPCRP_Mkt4;  
		private double [][]joFixRiskCov_Mkt4;
		private double [][]joPCCov_Mkt5;  
		private double [][]joPCRP_Mkt5;  
		private double [][]joFixRiskCov_Mkt5;
		
		
		public void objfunc_optimize_reduceRawData_MatrixStandard() {
			//-- Rule based Effective BONDS Baukasten : PV Sensi, Annuity Sensi, PV-Clean ---//
			//-- BONDS Baukasten - PV Sensi, Annuity Sensi, PV-Clean --//
			double [][][] bondsRVEffReduced= functUtil.buildRVDKFReduced (false, dataSet.bondsRVEff, ruleConf.numDateRows, ruleConf.numAnnuality, ruleConf.numYears, ruleConf.dfkColSize); 
			this.joBondsRVDataEff = new double[ruleConf.numDateRows][ruleConf.numDecisionVariables][ruleConf.dfkColSize];
			for (int i=0, slcStart=0, slcEnd=(ruleConf.numDecisionVariables-1); i<ruleConf.numDateRows; i++) {				
				//this.joSwapsRVDataEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.swapsRVEff[i], slcStart, slcEnd, 0, (ruleConf.dfkColSize - 1) );
				this.joBondsRVDataEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVEffReduced[i], slcStart, slcEnd, 0, (ruleConf.dfkColSize - 1) );
			}
				
			System.out.println(" joBondsRVDataEff: 3D-size: " + joBondsRVDataEff.length  + 
					"; 2D-Row_size: " +  joBondsRVDataEff[0].length + "; 2D-col-size: " + joBondsRVDataEff[0][0].length );
			System.out.println(" joBondsRVDataEff: 3D-size: 	 # 1st data = " + joBondsRVDataEff[0][0][0] 	+ " # Last data = " + joBondsRVDataEff[ruleConf.numDateRows-1][ruleConf.numDecisionVariables-1][0] );
			oxl.genTxtFileFrom2DData(filepath, "joBondsRVDataEff_first_0.txt", this.joBondsRVDataEff[0]);	
			oxl.genTxtFileFrom2DData(filepath, "joBondsRVDataEff_Last_numDateRows.txt", this.joBondsRVDataEff[ruleConf.numDateRows - 1]);
			
			//-- Baukasten - Bonds Raw value - Annuity Sensi --/ --// -- Should be  Reduced [nYears x numDKF x numEle ] --//
			double [][][] bondsRVAnnuitySensiEffReduced= functUtil.buildRVDKFReduced (false, dataSet.bondsRVAnnuitySensiEff, ruleConf.numDateRows, ruleConf.numAnnuality, ruleConf.numYears, ruleConf.dfkColSize);
			this.joBondsRVAnnuitySensiEff = new double[ruleConf.numDateRows][ruleConf.numDecisionVariables][ruleConf.dfkColSize];
			for (int i=0, slcStart=0, slcEnd=(ruleConf.numDecisionVariables-1); i<ruleConf.numDateRows; i++) {				
				this.joBondsRVAnnuitySensiEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVAnnuitySensiEffReduced[i], slcStart, slcEnd, 0, (ruleConf.dfkColSize - 1) );
			}	
			System.out.println(" joBondsRVAnnuitySensiEff: 3D-size: " + joBondsRVAnnuitySensiEff.length  + 
					"; 2D-Row_size: " +  joBondsRVAnnuitySensiEff[0].length + "; 2D-col-size: " + joBondsRVAnnuitySensiEff[0][0].length );
			oxl.genTxtFileFrom2DData(filepath, "joBondsRVAnnuitySensiEff_first_0.txt", this.joBondsRVAnnuitySensiEff[0]);	
			
			//-- Baukasten - Bonds Raw value - PV-CLEAN --/		--// -- Should be  Reduced [nYears x numDKF x numEle ] --//	
			double [][][] bondsRVPVCleanEffReduced= functUtil.buildRVDKFReduced (false, dataSet.bondsRVPVCleanEff, ruleConf.numDateRows, ruleConf.numAnnuality, ruleConf.numYears, 1);
			this.joBondsRVPVCleanEff = new double[ruleConf.numDateRows][ruleConf.numDecisionVariables][1];
			for (int i=0, slcStart=0, slcEnd=(ruleConf.numDecisionVariables-1); i<ruleConf.numDateRows; i++) {				
				this.joBondsRVPVCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVPVCleanEffReduced[i], slcStart, slcEnd, 0, 0 );
			}
			System.out.println(" joBondsRawPVEff: 3D-size: " + joBondsRVPVCleanEff.length  + 
					"; 2D-Row_size: " +  joBondsRVPVCleanEff[0].length + "; 2D-col-size: " + joBondsRVPVCleanEff[0][0].length );
			oxl.genTxtFileFrom2DData(filepath, "joBondsRVPVCleanEff_first_0.txt", this.joBondsRVPVCleanEff[0]);
			///--- BONDS Baukasten - PV Sensi, Annuity Sensi, PV-Clean ---////
			//-- Done! Rule based Effective BONDS Baukasten : PV Sensi, Annuity Sensi, PV-Clean ---//
			
			
			
			//-- Rule based Effective SWAPS Baukasten : PV Sensi, Annuity Sensi, PV-Clean ---//
			//-- Baukasten - Swaps Raw value - PV Sensi --/--// -- Should be  Reduced [nYears x numDKF x numEle ] --//
			double [][][] swapsRVEffReduced= functUtil.buildRVDKFReduced (false, dataSet.swapsRVEff, ruleConf.numDateRows, ruleConf.numAnnuality, ruleConf.numYears, ruleConf.dfkColSize); 
			this.joSwapsRVDataEff = new double[ruleConf.numDateRows][ruleConf.numDecisionVariables][ruleConf.dfkColSize];
			for (int i=0, slcStart=0, slcEnd=(ruleConf.numDecisionVariables-1); i<ruleConf.numDateRows; i++) {				
				//this.joSwapsRVDataEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.swapsRVEff[i], slcStart, slcEnd, 0, (ruleConf.dfkColSize - 1) );
				this.joSwapsRVDataEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVEffReduced[i], slcStart, slcEnd, 0, (ruleConf.dfkColSize - 1) );
			}
				
			System.out.println(" joSwapsRVDataEff: 3D-size: " + joSwapsRVDataEff.length  + 
					"; 2D-Row_size: " +  joSwapsRVDataEff[0].length + "; 2D-col-size: " + joSwapsRVDataEff[0][0].length );
				
			System.out.println(" joSwapsRVDataEff: 3D-size: 	 # 1st data = " + joSwapsRVDataEff[0][0][0] 	+ " # Last data = " + joSwapsRVDataEff[ruleConf.numDateRows-1][ruleConf.numDecisionVariables-1][0] );
							
			oxl.genTxtFileFrom2DData(filepath, "joSwapsRVDataEff_first_0.txt", this.joSwapsRVDataEff[0]);	
			oxl.genTxtFileFrom2DData(filepath, "joSwapsRVDataEff_Last_numDateRows.txt", this.joSwapsRVDataEff[ruleConf.numDateRows - 1]);
			
			
			
			//-- Baukasten - Swaps Raw value - Annuity Sensi --/ --// -- Should be  Reduced [nYears x numDKF x numEle ] --//
			double [][][] swapsRVAnnuitySensiEffReduced= functUtil.buildRVDKFReduced (false, dataSet.swapsRVAnnuitySensiEff, ruleConf.numDateRows, ruleConf.numAnnuality, ruleConf.numYears, ruleConf.dfkColSize);
			this.joSwapsRVAnnuitySensiEff = new double[ruleConf.numDateRows][ruleConf.numDecisionVariables][ruleConf.dfkColSize];
			for (int i=0, slcStart=0, slcEnd=(ruleConf.numDecisionVariables-1); i<ruleConf.numDateRows; i++) {				
				this.joSwapsRVAnnuitySensiEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVAnnuitySensiEffReduced[i], slcStart, slcEnd, 0, (ruleConf.dfkColSize - 1) );
			}
				
			System.out.println(" joSwapsRVAnnuitySensiEff: 3D-size: " + joSwapsRVAnnuitySensiEff.length  + 
					"; 2D-Row_size: " +  joSwapsRVAnnuitySensiEff[0].length + "; 2D-col-size: " + joSwapsRVAnnuitySensiEff[0][0].length );
			oxl.genTxtFileFrom2DData(filepath, "joSwapsRVAnnuitySensiEff_first_0.txt", this.joSwapsRVAnnuitySensiEff[0]);	
			
			
			//-- Baukasten - Swaps Raw value - PV-CLEAN --/		--// -- Should be  Reduced [nYears x numDKF x numEle ] --//	
			double [][][] swapsRVPVCleanEffReduced= functUtil.buildRVDKFReduced (false, dataSet.swapsRVPVCleanEff, ruleConf.numDateRows, ruleConf.numAnnuality, ruleConf.numYears, 1);
			this.joSwapsRVPVCleanEff = new double[ruleConf.numDateRows][ruleConf.numDecisionVariables][1];
			for (int i=0, slcStart=0, slcEnd=(ruleConf.numDecisionVariables-1); i<ruleConf.numDateRows; i++) {				
				this.joSwapsRVPVCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVPVCleanEffReduced[i], slcStart, slcEnd, 0, 0 );
			}
			System.out.println(" joSwapsRawPVEff: 3D-size: " + joSwapsRVPVCleanEff.length  + 
					"; 2D-Row_size: " +  joSwapsRVPVCleanEff[0].length + "; 2D-col-size: " + joSwapsRVPVCleanEff[0][0].length );
			oxl.genTxtFileFrom2DData(filepath, "joSwapsRVPVCleanEff_first_0.txt", this.joSwapsRVPVCleanEff[0]);	
			
		}
		
		
		
		public void objfunc_optimize_reduceRawData_TransferMatrix() {
			
			//--Load the transformed Swaps data - Swaps JO --//
			this.joSwapsRVDataEff=dataSet.swapsRVEff;
			this.joSwapsRVAnnuitySensiEff=dataSet.swapsRVAnnuitySensiEff;
			this.joSwapsRVPVCleanEff=dataSet.swapsRVPVCleanEff;
			this.joSwapsRVDebtCleanEff=dataSet.swapsRVDebtCleanEff;
			
			//--Load the transformed Bond data - Bonds JO --//
			this.joBondsRVDataEff=dataSet.bondsRVEff;
			this.joBondsRVAnnuitySensiEff=dataSet.bondsRVAnnuitySensiEff;
			this.joBondsRVPVCleanEff=dataSet.bondsRVPVCleanEff;
			this.joBondsRVDebtCleanEff=dataSet.bondsRVDebtCleanEff;
			
			//--Load the transformed Bestand for Swap and Bond - JO --//
			this.joBestandSwapTMEff=dataSet.BestandSwapTMEff;
			this.joBestandBondTMEff=dataSet.BestandBondTMEff;
			
			//--Load the transformed PVClean for Swap and Bond - JO --//
			this.joPVCleanSwapTMEff=dataSet.PVCleanSwapTMEff;
			this.joPVCleanBondTMEff=dataSet.PVCleanBondTMEff;
			
			//--Load the transformed RiskSensi for Swap and Bond - JO --//
			this.joBondRiskSensiTMEff=dataSet.BondRiskSensiTMEff;
			this.joSwapRiskSensiTMEff=dataSet.SwapRiskSensiTMEff;
			
			//--Load the transformed FixingRiskSensi for Swap and Bond - JO --//
			this.joBondILBSwapFxRiskBondTMEff=dataSet.BondILBSwapFxRiskBondTMEff;
			this.joBondILBSwapFxRiskSwapTMEff= dataSet.BondILBSwapFxRiskSwapTMEff;
			
			//--Load the transformed Schuld-debt for Swap and Bond - JO --//
			this.joSchuldBondTMEff=dataSet.SchuldBondTMEff;
			this.joSchuldSwapTMEff=dataSet.SchuldSwapTMEff;
			

			
			//double [] upConstraintVolBase = { 40.00,	60.00,	60.00,	1.00,	1.00,	1.00,	2.00}; // UP Vol limit per Quarter per Instrument //
			//double [] downConstraintVolBase = { -40.00,	-60.00,	-60.00,	-1.00,	-1.00,	-1.00,	-2.00}; // Down Vol limit per Quarter per Instrument //
			
			double [] upConstraintVolBase = 	{ 10.00,	50.00,	50.00,	0.25,	0.25,	0.25,	0.50}; // UP Vol limit per Quarter per Instrument // 
			double [] downConstraintVolBase = { -10.00,	-50.00,	-50.00,	-0.25,	-0.25,	-0.25,	-0.50}; // Down Vol limit per Quarter per Instrument //
			//--per year limit --//
			for (int i=0; i<upConstraintVolBase.length; i++) {
				upConstraintVolBase[i] = upConstraintVolBase[i] * 1.00;
				downConstraintVolBase[i] = downConstraintVolBase[i] * 1.00;
			}
			this.jUPConstLtd= new double[1][ruleConf.numDecisionVariables];
			this.jDNConstLtd= new double[1][ruleConf.numDecisionVariables];
			this.jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((upConstraintVolBase), (this.ruleConf.numDecisionVariables)/7 );
			this.jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintVolBase), (this.ruleConf.numDecisionVariables)/7);
						
		}
		
				
		
		public void objfunc_optimize_extractEffData() {
						
			//--Dataset --//
			if (this.dataSet == null) {
				this.dataSet= new optimSolveDataset(this.ruleConf);
			}
			if (dataSetInitFlag==false) {
				this.dataSet.optimLoadEffectiveData();
				this.dataSetInitFlag=true;
			}
			
			
			//--setup the effective data for rule-config--//	
			this.jUPConstLtd= new double[1][ruleConf.numDecisionVariables];
			this.jDNConstLtd= new double[1][ruleConf.numDecisionVariables];
			this.jUPConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((this.dataSet.upConstraintBase7V), (this.ruleConf.numDecisionVariables)/7 ); 								
			this.jDNConstLtd[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((this.dataSet.downConstraintBase7V), (this.ruleConf.numDecisionVariables)/7);
			
			
			this.joSwapRiskLimitEff= new double[1][ruleConf.numDateRows];
			this.joSwapRiskLimitEff[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((this.dataSet.jSwapRiskLimitEff12V), (this.ruleConf.numDateRows)/12);	
			
			System.out.println(" Rules -  ruleConfig: : numDateRows: " + ruleConf.numDateRows  + 
					"; numDecisionVariables: " +  ruleConf.numDecisionVariables + "; dfkColSize-size: " + ruleConf.dfkColSize );
				
			
			//-- Rule based Effective BONDS Baukasten : PV Sensi, Annuity Sensi, PV-Clean ---//
			//-- BONDS Baukasten - PV Sensi, Annuity Sensi, PV-Clean --//
			if (this.ruleConf.TRANSMAT == true) 
				objfunc_optimize_reduceRawData_TransferMatrix();
			else
				objfunc_optimize_reduceRawData_MatrixStandard();
			
			
			//-- check the vol per intsrument limit --//
			oxl.genTxtFileFrom2DData(filepath, "jUPConstLtd_VolPerInst.txt", this.jUPConstLtd);
			oxl.genTxtFileFrom2DData(filepath, "jDNConstLtd_VolPerInst.txt", this.jDNConstLtd);
			
			//--Bestand--//
			this.joBestandEff =  new double[ruleConf.numDateRows][ruleConf.dfkColSize];
			this.joBestandEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.BestandEff, 0, (ruleConf.numDateRows-1), 0, (ruleConf.dfkColSize-1) );
			System.out.println(" joBestandEff: Row_size: " +  joBestandEff.length + "; col-size: " +  joBestandEff[0].length  );
			
			this.joPVCleanEff =  new double[ruleConf.numDateRows][1];
			this.joPVCleanEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.PVCleanEff, 0, (ruleConf.numDateRows-1), 0, 0 );
			System.out.println(" joPVCleanEff: Row_size: " +  joPVCleanEff.length + "; col-size: " +  joPVCleanEff[0].length  );
			oxl.genTxtFileFrom2DData(filepath, "joPVCleanEff.txt", this.joPVCleanEff);	
			
			this.joPCRP1Eff=  new double[ruleConf.numDateRows][ruleConf.dfkColSize]; 
			this.joPCRP1Eff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.PCRP1Eff, 0, (ruleConf.numDateRows-1), 0, (ruleConf.dfkColSize-1) );
			System.out.println(" joPCRP1: size_ROW: " +  joPCRP1Eff.length + "; size_COL: " +  joPCRP1Eff[0].length    );
			
			this.joRefPCSensiEff=  new double[ruleConf.numDateRows][ruleConf.dfkColSize]; 
			this.joRefPCSensiEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.RefPCSensiEff, 0, (ruleConf.numDateRows-1), 0, (ruleConf.dfkColSize-1) );
			System.out.println(" joRefPCSensiEff: size_ROW: " +  joRefPCSensiEff.length + "; size_COL: " +  joRefPCSensiEff[0].length    );
						
			this.joPCCov = dataSet.PCCovEff;
			this.joSchuldVal = dataSet.SchuldVal;
			
			this.joSchuldEff =  new double[ruleConf.numDateRows][1];
			System.out.println(" joSchuldEff: Before slicing size_ROW: " +  joSchuldEff.length + "; size_COL: " +  joSchuldEff[0].length    );
			this.joSchuldEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.SchuldEff, 0, (ruleConf.numDateRows-1), 0, -1 );			
			System.out.println(" joSchuldEff: size_ROW: " +  joSchuldEff.length + "; size_COL: " +  joSchuldEff[0].length    );
			
			this.joPCCov_Mkt1 = dataSet.PCCov_Mkt1;
			this.joPCRP_Mkt1=dataSet.PCRP_Mkt1;  
			this.joFixRiskCov_Mkt1=dataSet.FixRiskCov_Mkt1;
			this.joPCCov_Mkt2 = dataSet.PCCov_Mkt2;
			this.joPCRP_Mkt2=dataSet.PCRP_Mkt2;  
			this.joFixRiskCov_Mkt2=dataSet.FixRiskCov_Mkt2;
			this.joPCCov_Mkt3 = dataSet.PCCov_Mkt3;
			this.joPCRP_Mkt3=dataSet.PCRP_Mkt3;  
			this.joFixRiskCov_Mkt3=dataSet.FixRiskCov_Mkt3;
			this.joPCCov_Mkt4 = dataSet.PCCov_Mkt4;
			this.joPCRP_Mkt4=dataSet.PCRP_Mkt4;  
			this.joFixRiskCov_Mkt4=dataSet.FixRiskCov_Mkt4;
			this.joPCCov_Mkt5 = dataSet.PCCov_Mkt5;
			this.joPCRP_Mkt5=dataSet.PCRP_Mkt5;  
			this.joFixRiskCov_Mkt5=dataSet.FixRiskCov_Mkt5;
			
			this.stockVolLimitEff=  ruleConf.stockVolLimit ; // dataSet.SchuldVal;
			
			//--- Setup the initial Solution set for some number of years computed by JOM  ---//
			this.joInitDVSolSetXL = new double[1][ruleConf.numDecisionVariables];				
			this.joInitDVSolSetXL[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(dataSet.initSolSetXL7V, (ruleConf.numDecisionVariables)/7);
			
			
			//-- Swap Risk --//    	
			this.joSwapSensiRiskEff=  new double[ruleConf.numDateRows][ruleConf.dfkColSize]; //[numDataRows x 10] -- ruleConf.swapSensiColSize=10;
			this.joSwapSensiRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.SwapSensiRiskEff, 0, (ruleConf.numDateRows-1), 0, (ruleConf.swapSensiColSize-1) );
			System.out.println(" joSwapSensiRiskEff: size_ROW: " +  joSwapSensiRiskEff.length + "; size_COL: " +  joSwapSensiRiskEff[0].length    );
			
			
			//-- Fixing Risk: Bond, ILB, Swap --//
			this.joBondFxRiskEff=  new double[ruleConf.numDateRows][ruleConf.dfkColSize];
			this.joBondFxRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.BondFxRiskEff, 0, (ruleConf.numDateRows-1), 0, (ruleConf.bondFxColSize-1) );
			System.out.println(" joBondFxRiskEff: size_ROW: " +  joBondFxRiskEff.length + "; size_COL: " +  joBondFxRiskEff[0].length    );
			
			this.joILBFxRiskEff=  new double[ruleConf.numDateRows][ruleConf.dfkColSize];
			this.joILBFxRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.ILBFxRiskEff, 0, (ruleConf.numDateRows-1), 0, (ruleConf.ILBFxColSize-1) );
			System.out.println(" joILBFxRiskEff: size_ROW: " +  joILBFxRiskEff.length + "; size_COL: " +  joILBFxRiskEff[0].length    );

			this.joSwapFxRiskEff=  new double[ruleConf.numDateRows][ruleConf.dfkColSize]; 
			this.joSwapFxRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(dataSet.SwapFxRiskEff, 0, (ruleConf.numDateRows-1), 0, (ruleConf.swapFxColSize-1) );
			System.out.println(" joSwapFxRiskEff: size_ROW: " +  joSwapFxRiskEff.length + "; size_COL: " +  joSwapFxRiskEff[0].length    );
			
			//this.joBondILBSwapFxRiskEff=  new double[ruleConf.numDateRows][(ruleConf.bondFxColSize + ruleConf.ILBFxColSize + ruleConf.swapFxColSize)];			
			double[][] BondILBFxRiskEff=com.stfe.optim.util.optimSliceArray.join2Array2DCols(this.joBondFxRiskEff, this.joILBFxRiskEff);
			objfunc_displayJOMVarStruct (null, BondILBFxRiskEff, 2);
			//oxl.genTxtFileFrom2DDataFmt(filepath, "BondILBFxRiskEff-122-413.txt", BondILBFxRiskEff, 10);
			
			this.joBondILBSwapFxRiskEff=com.stfe.optim.util.optimSliceArray.join2Array2DCols(BondILBFxRiskEff, this.joSwapFxRiskEff);			
			System.out.println(" joBondILBSwapFxRiskEff: size_ROW: " +  joBondILBSwapFxRiskEff.length + "; size_COL: " +  joBondILBSwapFxRiskEff[0].length    );
			objfunc_displayJOMVarStruct (null, this.joBondILBSwapFxRiskEff, 2);
			oxl.genTxtFileFrom2DDataFmt(filepath, "joBondILBSwapFxRiskEff-122-413.txt", this.joBondILBSwapFxRiskEff, 10);
					
		}
		
		
		
		public void objfunc_displayJOMVarStruct (String jomvar, double[][] arr2D,  int option) {
			
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

		
		
		public void ojbfunc_optimize_dispDVExpr(String expr, String label) {
			if (label == null) label="DV Expr";
			//if (!this.jom.parseExpression(expr).isScalar()) { // -- crashes some times
			int ndim = this.jom.parseExpression(expr).getNumDim();
			
			if (ndim > 1) {
				System.out.println(label + ": dispDVExpr:  NumDim of expression: " + ndim);
				int[] sizev = (int[]) this.jom.parseExpression(expr).size();
				if (ndim == 3) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Tab=" + sizev[0] +  " /Row=" + sizev[1] +  " /Col=" + sizev[2] + " ] ");
				if (ndim == 2) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Row=" + sizev[0] +  " /Col=" + sizev[1] + " ] ");
			} else System.out.println(label + ": dispDVExpr: scalar quantity !");
		}
		
		
		
		//-- Swap Risk --//		
		public void ojbfunc_optimize_setupJOMSwapRisk() {
			
			// slice the sensi of SwapsRV2DOpt from 31 col to 10 col // 
			this.jom.setInputParameter("SwapSensiRV2DOpt","  SwapsRV2DOpt(all,all,21:30)" );
			double [][][] SwapSensiRV2DOptArr= (double[][][]) jom.getInputParameter("SwapSensiRV2DOpt").toArray();			
			this.oxl.genTxtFileFrom2DData(filepath, "SwapSensiRV2DOptArr-21-20_Jan2017.txt", SwapSensiRV2DOptArr[0]);
			objfunc_displayJOMVarStruct ("SwapSensiRV2DOpt", null,0);			
		
			//PCCov_Mkt1
			this.jom.setInputParameter("PCCov_Mkt1_10_10", "PCCov_Mkt1(21:30 , 21:30) " ); // PCCov_Mkt1(21:30, 21:30) 
			double [][] PCCov_Mkt1_10_10Arr= (double[][]) jom.getInputParameter("PCCov_Mkt1_10_10").toArray();			
			this.oxl.genTxtFileFrom2DData(filepath, "PCCov_Mkt1_10_10Arr-10-10.txt", PCCov_Mkt1_10_10Arr);
			System.out.println (" PCCov_Mkt1_10_10 Dim = " + this.jom.parseExpression("PCCov_Mkt1_10_10").getNumDim());
			System.out.println (" PCCov_Mkt1_10_10 RowSize = " + this.jom.parseExpression("PCCov_Mkt1_10_10(all,0)").evaluateConstant().getNumElements() + " /Col:" + 
					this.jom.parseExpression("PCCov_Mkt1_10_10(0,all)").evaluateConstant().getNumElements() );			
			
			// Already setup - SwapSensiRiskOpt, SwapFxRiskOpt //
			//jom.setInputParameter("SwapSensiRiskOpt", new DoubleMatrixND(joSwapSensiRiskEff)  );
		 	//jom.setInputParameter("SwapFxRiskOpt", new DoubleMatrixND(joSwapFxRiskEff)  );
			System.out.println (" -->> SwapSensiRisk RowSize = " + this.jom.parseExpression("SwapSensiRiskOpt(all,0)").evaluateConstant().getNumElements() + 
					" /ColSize:"+ this.jom.parseExpression("SwapSensiRiskOpt(0,all)").evaluateConstant().getNumElements() );
			
		}
		
		
		public String  ojbfunc_optimize_formSwapRiskConstraintExp() {
			
			// Issue with non linear constraint -- addConstraint can not handle (DV * DV') 
			//Expected: String iexpr= new String(" ((sum( ((decisionVars * SwapSensiRV2DOpt) + SwapSensiRiskOpt) , 1)  ) * PCCov_Mkt1_10_10) * (sum( ((decisionVars * SwapSensiRV2DOpt)  + SwapSensiRiskOpt) ,1))'   " );
			
			//Gerrit Solution to replace the multiplication of DV-matrix and its transpose : (DV x DV')
			
			//String fexpr = " sum( (  (sum( (decisionVars * SwapSensiRV2DOpt),1) + SwapSensiRiskOpt)  .*    (  (sum( (decisionVars * SwapSensiRV2DOpt),1) + SwapSensiRiskOpt) * PCCov_Mkt1_10_10) ),2)";
			String fexpr = " sum( (  (sum( (decisionVars * SwapSensiRV2DOpt),1) + SwapSensiRiskFullOpt)  .*    (  (sum( (decisionVars * SwapSensiRV2DOpt),1) + SwapSensiRiskFullOpt) * PCCov_Mkt1_10_10) ),2)";
			
			ojbfunc_optimize_dispDVExpr("sum( (decisionVars * SwapSensiRV2DOpt),1)", "AddConstratintSwapRisk-Sensi-fexpr1");
			
			ojbfunc_optimize_dispDVExpr("SwapSensiRiskFullOpt", "AddConstratintSwapRisk-Sensi-fexpr1");
			
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapRisk-Sensi-fexpr1");
			
			
			// -- apply sqrt  for all values at diagonal [diagonal matrix] and 0 at rest  --/
			// -- Add small value to it, else might cause JOM exception: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 //
			fexpr= "sqrt( ("+ fexpr + ") + 0.0001d ) "; 
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapRisk-Sensi-fexpr2");
			
			ojbfunc_optimize_dispDVExpr("SchuldFullOpt", "AddConstratintSwapRisk-SchuldOpt-fexpr3");	
			fexpr= "(1/(SchuldFullOpt)) .* (" + fexpr +")";
			
			
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapRisk-Sensi-fexpr3");
			
			//--sum --//
			fexpr= "sum("+fexpr +")";
			
			//--mean --//
			//jom.setInputParameter("NumMonthlyRowsOpt",  this.ruleConf.numDateRows );
			//fexpr= fexpr + " /  NumMonthlyRowsOpt"; 
						
			fexpr= "("+fexpr +")' * 1000.0d ";
			
						 
			
			/*
			fexpr= "(1/SchuldOpt) .* (1/SchuldOpt) .* (" + fexpr +")";
			fexpr= "("+fexpr +") * 1000000.0d ";
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapRisk-Sensi-fexpr1");
			*/
			
			
			//fexpr= "("+fexpr +")'";
					
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapRisk-All-fexpr5");
			
			
			System.out.println(" AddConstratintSwapRisk -  Final Expression evaluation for SwapRisk Constraint before enabling active points: "+ jom.parseExpression(fexpr).evaluate());
			//-- constraint should be applied only for the first quarter - this.ruleConf.numYears--//
			//double[] initPoints12V={1.0d, 1, 1, 1, 1, 1, 1.0, 1, 1, 1, 1, 1.0};
			double[] initPoints12VY1={0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			double[] initPoints12VYX={0.0, 0.0, 1.0, 0.0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }; 
			double[][] constraintPointsVector=new double[1][this.ruleConf.numYears * 12];
			for (int i=0; i<this.ruleConf.numYears; i++) {
				for (int j=0; j<12; j++) {
					if (i==0) 
						constraintPointsVector[0][(i*12)+ j]= initPoints12VY1[j];  // ---Active constraint in the first year is different than other years--//
					else 
						constraintPointsVector[0][(i*12)+ j]= initPoints12VYX[j];
				}
			}
			//this.jom.setInputParameter("constraintPointsSwapRisk",new DoubleMatrixND(constraintPointsVector) );
			//fexpr= fexpr +" .* constraintPointsSwapRisk";
			
			
			//-- Swap Risk Limit Array  --
			//double [][] SwapRiskLimitArr= (double[][]) jom.getInputParameter("SwapRiskLimit").toArray();
			//System.out.println(" AddConstratintSwapRisk SwapRiskLimit evaluate" + this.jom.parseExpression("SwapRiskLimit").evaluate() );
			//this.oxl.genTxtFileFrom2DData(filepath, "SwapRiskLimitArr.txt", SwapRiskLimitArr);			
			//ojbfunc_optimize_dispDVExpr("SwapRiskLimit", "AddConstratintSwapRisk-SwapRiskLimit-fexpr2");
			
			
			// --- Sum is not required, as a vector can be compared with a scalar at RHS. //
			//-- To execute efficiently, sum over the column matrix for the year (0:11) i.e. monthly for an annum -- and add the constraint for [ var <= 120] -- 
			//fexpr= "sum( (" + fexpr +"), 2)";
			//-- set up finally the constraint in calling function -- //
						
			return fexpr;
		}
		

		public void  ojbfunc_optimize_expConstraintSwapRisk_jgExpGDX() {
			
			//--Setup the JOM  symbols to build expression --//
			ojbfunc_optimize_setupJOMSwapRisk();
			//--test the expression--//
			ojbfunc_optimize_formSwapRiskConstraintExp();
			
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapSensiRV2DOpt", "SwapSensiRV2DOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapSensiRiskOpt", "SwapSensiRiskOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapSensiRiskFullOpt", "SwapSensiRiskFullOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"PCCov_Mkt1_10_10", "PCCov_Mkt1_10_10 ");
						
		}
		
			
		
		//-- Swap Sensi - FX - Mehrkosten - Risk --//
		public void ojbfunc_optimize_addConstraintSwapRisk() {
			
			ojbfunc_optimize_setupJOMSwapRisk();
			
			//--Set up SwapRisk constraint with Decision-Vars:  - Fails with non-linear constraint. //			
			//-- ! Does not work with non-linear constraint! -Error-"main" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0//			
			//String expr= new String(" ((sum( (decisionVars) * SwapSensiRV2DOpt , 1)  ) * PCCov_Mkt1_10_10) * (sum( (decisionVars) * SwapSensiRV2DOpt ,1))'   " );
									
			String fexpr = ojbfunc_optimize_formSwapRiskConstraintExp();
			System.out.println(" AddConstratintSwapRisk -  Final Expression for SwapRisk Constraint: "+ fexpr);
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapRisk-All-fexpr3");
			System.out.println(" AddConstratintSwapRisk -  Final Expression evaluation for SwapRisk Expression: "+ jom.parseExpression(fexpr).evaluate());
			
			//--for 10 years accumulated SwapRiskLimit using JOM Limit--//
			double swapRiskLimitBase=1.200; // 1.9-GOOD; 1.8- May be ; 1.7 -May be ; 1.5-May be;  1.2 -Bad - Changes to get effect --//
			
			//double [][] allSwapRiskLimit = new double[1][this.ruleConf.numDateRows]; //--1.2=limit per month   -- --14.4 per year-- //
			//for (int i=0; i<this.ruleConf.numDateRows; i++) allSwapRiskLimit[0][i] = swapRiskLimitBase;
			//jom.setInputParameter("SwapRiskLimitOpt", new DoubleMatrixND (allSwapRiskLimit) );
			//jom.setInputParameter("MeanAllSwapRiskLimitOpt", new DoubleMatrixND (swapRiskLimitBase) );
			
			double [][] sumAllSwapRiskLimit = new double[1][1];
			sumAllSwapRiskLimit[0][0] = (swapRiskLimitBase * (this.ruleConf.numDateRows));
			jom.setInputParameter("SumAllSwapRiskLimitOpt", new DoubleMatrixND (sumAllSwapRiskLimit) );
			System.out.println(" AddConstratintSwapRisk -  SwapRisk Constrainted by SumAllSwapRiskLimitOpt: "+ jom.parseExpression("SumAllSwapRiskLimitOpt").evaluate());
						
			//this.SwapRiskConstStr= " (" + fexpr + ") <= 120.0" ;
			//this.SwapRiskConstStr= " (" + fexpr + ") <= 1.44d" ;
			//this.SwapRiskConstStr= " (" + fexpr + ") <= 1.2" ; //-- 1.2 for each month; Use 120-sum10Years; 1.44-Square monthly;//
			
			//--individual--//
			//this.SwapRiskConstStr= " (" + fexpr + ") <= 1.2" ;
			//---mean--//
			//this.SwapRiskConstStr= " (" + fexpr + ") <= MeanAllSwapRiskLimitOpt" ;
			//---sum--//
			this.SwapRiskConstStr= " (" + fexpr + ") <= SumAllSwapRiskLimitOpt" ;
			
			
			this.jom.addConstraint(this.SwapRiskConstStr);			
						
			
			// -- Add the relational operator --//
			//this.SwapRiskConstStr= " (" + fexpr + ") <= 1.2d" ; //--java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 -- Arising due to sqrt //
			//String fexpr1= "(" + fexpr + ")(0,0:6) <= SwapRiskLimit(0,0:6) " ; //--Illegal Argument Exception //			
			// --  java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 --//
			//String fexpr1= "( (" + fexpr + ")(0,0) )" + " <= (SwapRiskLimit(0,0)) " ; //--illegal Argument Exception //	
			
			// Excel- =WURZEL(MMULT(MTRANS($S$2:$S$11);MMULT(swapcov;$S$2:$S$11)))/'Kosten 31.07.2016'!$H$1*1000						  
			
						
		} //  -- ojbfunc_optimize_addConstraintSwapRisk Over -- //

		
		
		public void  ojbfunc_optimize_expConstraintSwapFxRisk_jgExpGDX() {
			//--Setup the JOM  symbols to build expression --//
			ojbfunc_optimize_setupJOMSwapFxRisk();
			//--Test the expression --//
			ojbfunc_optimize_formSwapFxRiskConstraintExp();
			
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapFxRV2DOpt", "SwapFxRV2DOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapFxRiskOpt", "SwapFxRiskOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"PCCov_Mkt1_1_31", "PCCov_Mkt1_1_31 ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapFxRiskFullOpt", "SwapFxRiskFullOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SchuldFullOpt", "SchuldFullOpt ");
		}
				
		
		//-- Swap Fixing Risk --//		
		public void ojbfunc_optimize_setupJOMSwapFxRisk() {
			
			// slice the sensi of SwapsRV2DOpt from 31 col  // 
			this.jom.setInputParameter("SwapFxRV2DOpt","  SwapsRV2DAnnSensiOpt(all,all,0:30)" );
			double [][][] SwapFxRV2DOptArr= (double[][][]) jom.getInputParameter("SwapFxRV2DOpt").toArray();			
			this.oxl.genTxtFileFrom2DData(filepath, "SwapFxRV2DOptArr-0-30_1stMonth.txt", SwapFxRV2DOptArr[0]);			
			objfunc_displayJOMVarStruct ("SwapFxRV2DOpt", null,0);			
						
			//-- SwapFxRiskOpt
			objfunc_displayJOMVarStruct ("SwapFxRiskOpt", null,0);
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
		
		
		public String  ojbfunc_optimize_formSwapFxRiskConstraintExp() {
			// *#+ Realise -- SchuldFullOpt    --+#*/ 
			
			// Issue with non linear constraint -- addConstraint can not handle (DV * DV')
			//Solution to replace the multiplication of DV-matrix and its transpose : (DV x DV')
			//String fexpr = " sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt ";
			
			//String fexpr = " sum( (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt)  .*    (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskOpt) * PCCov_Mkt1_1_31) ),2)";
			String fexpr = " sum( (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskFullOpt)  .*    (  (sum( (decisionVars * SwapFxRV2DOpt),1) + SwapFxRiskFullOpt) * PCCov_Mkt1_1_31) ),2)";
			
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapFxRisk-fexpr1");
			double [][] formSwapFxRiskConstraintArr= (double[][]) jom.parseExpression(fexpr).evaluate().toArray();
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "TEST-F-Mehrkosten-formSwapFxRiskConstraintArr", formSwapFxRiskConstraintArr,10);
						
			
			// -- apply sqrt  for all values at diagonal [diagonal matrix] and 0 at rest  --/
			// -- Add small value to it, else might cause JOM exception: java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 //
			fexpr= "sqrt( ("+ fexpr + ") + 0.0001d ) "; 
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapRisk-fexpr-sqrt");
			
			// -- divide the column vector fexpr with transpose of a row vector SchuldOpt to get a column  vector -- [1,n] x [n, n] = [1, n]--/
			fexpr= "(1/SchuldFullOpt) .* (" + fexpr +")";
			fexpr = "sum("+fexpr +")";
			fexpr= "("+fexpr +")' * 1000.0d";
			//fexpr= "("+fexpr +")'";
			
			//ojbfunc_optimize_dispDVExpr("SchuldOpt", "AddConstratintSwapFxRisk-SchuldOpt");
			
			
			
			/*
			// -- divide the column vector fexpr with transpose of a row vector SchuldOpt to get a column  vector -- [1,n] x [n, n] = [1, n]--/
			fexpr= "(1/SchuldOpt) .* (1/SchuldOpt) .* (" + fexpr +")";
			fexpr= "("+fexpr +") * 1000000.0d ";
			fexpr= "("+fexpr +")'";
			*/
				
			//-- constraint should be applied only for the first quarter - this.ruleConf.numYears--//
			double[] initPoints12V={1.0d, 1, 1, 1, 1, 1, 1.0, 1, 1, 1, 1, 1.0};
			//double[] initPoints12V={0.0, 0, 0, 0, 0, 0, 0.0, 0, 0, 0, 0, 0.0d };
			double[][] constraintPointsVector=new double[1][this.ruleConf.numYears * 12];
			for (int i=0; i<this.ruleConf.numYears; i++) {
				for (int j=0; j<12; j++) {
					constraintPointsVector[0][(i*12) + j]= initPoints12V[j];
				}
			}
			//constraintPointsVector[0][this.ruleConf.numDateRows -1]=1.0; //-- last point active to carry the sums of DVs//
			//this.jom.setInputParameter("constraintPointsSwapFix",new DoubleMatrixND(constraintPointsVector) );
			
			//// Handle the sum ///
			//String sumFexpr= fexpr + " * (ones([1;"+48]))'";
			
			//String sumFexpr= fexpr + " * (ones([1;"+ String.valueOf(this.ruleConf.numDateRows)+"]))'";
			//String sumFexpr = "sum("+fexpr +")";
			//this.jom.setInputParameter("constraintPointsSwapFix(0,23)",sumFexpr );
			//fexpr =  sumFexpr;
			
			//fexpr+"([0],[47]) = " + sumFexpr;
			//String sumFexprVec = sumFexpr + " * constraintPointsSwapFix(0, all)" ; 
			
			//fexpr= "("+ fexpr +") .* (constraintPointsSwapFix)";
			
			//ojbfunc_optimize_dispDVExpr(fexpr, "** Check Reduction: Sum - AddConstratintSwapFxRisk-fexpr-Sum");
			
			
			//Issue --- sum operator does not work -- fexpr= "sum( (" + fexpr + "), 2)";	 Exception derivative not found; IllgelaIndex Exception;
			//--- Alternatively transpose ones for sum also fail with JOM Exception Check feasibility error.
			/// --- Error Message: Check feasibility error. Constraint number 44 has a value (left hand side minus right hand side): 0.04030126239746323, 
			//--- 	, which is outside the feasibility limits [-1.7976931348623157E308,0.0]
			//fexpr= "sum("+fexpr +")";  --- Alternatively: fexpr= "sum("+fexpr +", 2)"; -- Alternatively : fexpr= fexpr + " * (ones([1;36]))'";
			
			
			//fexpr = "sum("+fexpr +")";
			//ojbfunc_optimize_dispDVExpr(fexpr, "** Check Reduction: Sum - AddConstratintSwapFxRisk-fexpr-AfterSum");
			
			
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapFxRisk-fexpr-normalized-tansposed");
			
			return fexpr;
		}
		
		
		
		
		public void ojbfunc_optimize_addConstraintSwapFxRisk() {
						
			ojbfunc_optimize_setupJOMSwapFxRisk();
			
			//--Set up SwapRisk constraint with Decision-Vars:  - Fails with non-linear constraint. //			
			//-- ! Does not work with non-linear constraint! -Error-"main" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0//
			String fexpr = ojbfunc_optimize_formSwapFxRiskConstraintExp();
			ojbfunc_optimize_dispDVExpr(fexpr, "AddConstratintSwapFxRisk-fexpr-all");
			System.out.println(" AddConstratintSwapFxRisk -  Expression evaluation for SwapFxRisk Constraint: "+ jom.parseExpression(fexpr).evaluate());
			
			// -- No Fx Limit ---//
			//double [][] SwapRiskLimitArr= (double[][]) jom.getInputParameter("SwapRiskLimit").toArray();						
			//this.oxl.genTxtFileFrom2DData(filepath, "SwapRiskLimitArr.txt", SwapRiskLimitArr);			
			//ojbfunc_optimize_dispDVExpr("SwapRiskLimit", "AddConstratintSwapRisk-SwapRiskLimit-fexpr2");
			//System.out.println(" AddConstratintSwapRisk SwapRiskLimit evaluate" + this.jom.parseExpression("SwapRiskLimit").evaluate() );
						
			
			//--for 10 years accumulated FxRisk using JOM Limit--//
			
			//double [][] allSwapFXLimit = new double[1][this.ruleConf.numDateRows]; //--0.74=limit per month   -- --8.88 per year-- //
			//for (int i=0; i<this.ruleConf.numDateRows; i++) allSwapFXLimit[0][i] = 0.74;
			//jom.setInputParameter("SwapFXLimitOpt", new DoubleMatrixND (allSwapFXLimit) );
			//jom.setInputParameter("MeanAllSwapFXLimitOpt", new DoubleMatrixND (0.74) );
			
			double [][] sumAllSwapFXLimit = new double[1][1];
			sumAllSwapFXLimit[0][0] = (0.74 * this.ruleConf.numDateRows);
			jom.setInputParameter("SumAllSwapFXLimitOpt", new DoubleMatrixND (sumAllSwapFXLimit) );
			
			
			//allSwapFXLimit[0][0] = allSwapFXLimit[0][0] * (ruleConf.numDateRows);
			
			
			//allSwapFXLimit[0][0] = allSwapFXLimit[0][0] * (this.ruleConf.numYears);
			//this.jom.setInputParameter("allSwapFXLimitOpt", allSwapFXLimit);
			//this.SwapFxRiskConstStr= " (" + fexpr + ") <= allSwapFXLimitOpt(0,0) " ;
			
			/*
			//--FxRisk using Java limit--//
			//-- Testing with 10 fold of increased FXRisk --//
			
			//this.SwapFxRiskConstStr=   fexpr + " <= 0.74d" ;
			//this.SwapFxRiskConstStr= " (" + fexpr + ") <= 7.40d " ;
			
			// Monthy- Squre of 0.74;   Sum-Square of NumYears;
			//this.SwapFxRiskConstStr= " (" + fexpr + ") <= 0.50d " ;
			
			//this.SwapFxRiskConstStr= " (" + fexpr + ") <= 5500.0 " ;
			
			*/
			
			//this.SwapFxRiskConstStr= " (" + fexpr + ") <= SwapFXLimitOpt(0,all) " ;
			
			this.SwapFxRiskConstStr= " (" + fexpr + ") <= SumAllSwapFXLimitOpt " ;
			System.out.println(" AddConstratintSwapFXRisk -  SwapFxRisk Constrainted by SumAllSwapFXLimitOpt: "+ jom.parseExpression("SumAllSwapFXLimitOpt").evaluate());
			System.out.println(" AddConstratintSwapFxRisk -  Final Expression for SwapFxRisk Constraint: ("+ fexpr + ") <= " + sumAllSwapFXLimit[0][0]);			
			System.out.println(" AddConstratintSwapFxRisk -  Final Raw Expression for SwapFxRisk Constraint: " + this.SwapFxRiskConstStr);
			this.jom.addConstraint(this.SwapFxRiskConstStr);
					
			
			// -- Add the relational operator --//
			//this.SwapFxRiskConstStr= " (" + fexpr + ") <= 1.2d" ; //--java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 -- Arising due to sqrt //
			//String fexpr1= "(" + fexpr + ")(0,0:6) <= SwapFxRiskLimit(0,0:6) " ; //--Illegal Argument Exception //			
			// --  java.lang.IndexOutOfBoundsException: Index: 0, Size: 0 --//
			//String fexpr1= "( (" + fexpr + ")(0,0) )" + " <= (SwapFxRiskLimit(0,0)) " ; //--illegal Argument Exception //	
								
		}
		// Swap Fixing Risk //
		
		
				
		//-- Set up the constraint for Stock-Volume like,  op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockVolLimit "); --//
		private void ojbfunc_optimize_addConstraintSwapVolume () {
				
			//Ex: StoclLimit=15.0 per quarter; numElements=7; numQuaterlYears=4;  // 
			//setVolConstraint (op, 15.0, 7, 12) 
			//op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockLimit1Base ");
			//op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2)  + 0.001d) ) <= StockLimit1Base ");
					
			System.out.println("------------------- Constraints for Stock-Volume --------------------");
			int instr = this.ruleConf.numElements;					
			double qtrs=  (this.ruleConf.numYears) * (this.ruleConf.numAnnualConstraints) ;
			
			System.out.println(" ojbfunc_optimize_addConstraintSwapVolume: numAnnualConstraints : " + this.ruleConf.numAnnualConstraints + "; Qtrs: " + qtrs +"; Instr: " + instr);
			
			boolean debtFlag=true;
			
				//--Set up Constraints  -- //
				for (int i=0, j=0,k=(instr-1); i<qtrs; i++) {
											
					String expr = " sum (  sqrt( (decisionVars(0," +j+":"+k  +  ") ^ 2) + 0.001d ) )";
				
					String debtExpr = "15.0";
					if (debtFlag==true) {
					
						debtExpr = "sum ( SchuldFullOpt(" + j +":" + k  +", 0)) ";  //SchuldFullOpt
						System.out.println("DebtExpr evaluate: " + this.jom.parseExpression(debtExpr).evaluate() );
						debtExpr = debtExpr + " / (15000000000000.0d)  "; ///---  (10.0 ^ 12.0)  "; --- does not work --///
						System.out.println("Debt expr Vol Constraint:" + debtExpr);
						//--- ojbfunc_optimize_dispDVExpr(debtExpr, "VolConstraint-DebtExpr"); //this.jom.parseExpression
						// -- Debt Constraints::  { Sum(DV) * ((10^12) / Sum(Debt)) < 15 }:  * 1/this.joSchuldSwapTMEff ---SchuldSwapTMOpt ; 	jom.setInputParameter("SchuldSwapTMOpt",this.joSchuldSwapTMEff);
						
						//-- Test with approx val --///
						debtExpr = "15.0 * 1.120";
					}
					
					
					if (this.ruleConf.TRANSMAT == true) {
					
						//expr = expr + "  <= " + debtExpr ; 
						expr = expr + "  <= 18.0 "; // -- 15 is correct! ----  60 works efficiently but not 15! --//
						
					} else expr = expr + "  <= 15.0 "; // yearly vol = 60 //
					
					this.jom.addConstraint(expr);
					if ( i<=4 || i>=(qtrs-2) ) System.out.println(expr);
					
					j=k+1;
					k=k + instr;				
									
				}
				
		} // -- setVolumeConstraint over --//
		
		
		private void ojbfunc_optimize_expConstraintSwapVolume_jgExpGDX() {
			//-- to be done at GAMS ---//
			//Debt Constraints::  { Sum(DV) * ((10^12) / Sum(Debt)) < 15 }
			//op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockLimit1Base ");
			//op.addConstraint(" sum (   sqrt( (decisionVars(0,7:13) ^ 2)  + 0.001d) ) <= StockLimit1Base ");
			
			//-- Nothing to export ---//
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"upConstraints", "upConstraints ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"downConstraints", "downConstraints ");
			
		}
		
		
		
		//--- Add Objective Function -- Mehrkosten --  --- //
		public String  ojbfunc_optimize_formSwapMehrkostenObjFunctExp() {
			
			//String PVTotalVec= " ((PVCleanOpt) + (sum( (decisionVars *  SwapsRV2DPVCleanOpt), 1)) ) ";	
			String PVTotalVec= " ((PVCleanFullOpt) + (sum( (decisionVars *  SwapsRV2DPVCleanOpt), 1)) ) ";
			
			PVTotalVec = "(" + PVTotalVec + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalVec, "Mehrkosten-pvTotalVec");
			
			//-- build a matrix of PVTotalOptArr, where 1st-col of 120 rows are copied to 31 cols --// -- [ ((this.ruleConf.numYears) * (this.ruleConf.monthsInYear)), 31] --//
			String PVTotalMatrix =  PVTotalVec + " *  ones([1;31])" ;
			PVTotalMatrix = "(" + PVTotalMatrix + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalMatrix, "Mehrkosten-PVTotalMatrix");
			
			
			String PVTotalRefSensi =  PVTotalMatrix + " .* RefPCSensiOpt " ;
			PVTotalRefSensi = "(" + PVTotalRefSensi + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalRefSensi, "Mehrkosten-PVTotalRefSensi");
			
			
			//-- Replace BestandOpt with BestandFull --// 
			String SwapMehrKosten=   "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* (      (sum( (decisionVars) *  SwapsRV2DOpt ,1)+ BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
			
			//-- Replace BestandOpt with Bestand Partly --// 
			//String SwapMehrKosten=   "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* (      (sum( (decisionVars) *  SwapsRV2DOpt ,1)+ BestandOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";		
			
			SwapMehrKosten= "(" + SwapMehrKosten + ") * 10000.0d";
			
			ojbfunc_optimize_dispDVExpr(SwapMehrKosten, "Mehrkosten-SwapMehrKosten");
			
			return SwapMehrKosten;
		}
		
		
		//--- Add Objective Function -- Mehrkosten -- Bond+Swap --- for future::::. //
		public String  ojbfunc_optimize_formBondSwapMehrkostenObjFunctExp() {
					
			//String PVTotalVec= " ((PVCleanOpt) + (sum( (decisionVarsBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (decisionVarsSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";		
			String PVTotalVec= " ((PVCleanFullOpt) + (sum( (decisionVarsBond *  BondsRV2DPVCleanOpt), 1)) + (sum( (decisionVarsSwap *  SwapsRV2DPVCleanOpt), 1)) ) ";
			
			PVTotalVec = "(" + PVTotalVec + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalVec, "Mehrkosten-pvTotalVec + BOndSwap");
					
			//-- build a matrix of PVTotalOptArr, where 1st-col of 120 rows are copied to 31 cols --// -- [ ((this.ruleConf.numYears) * (this.ruleConf.monthsInYear)), 31] --//
			String PVTotalMatrix =  PVTotalVec + " *  ones([1;31])" ;
			PVTotalMatrix = "(" + PVTotalMatrix + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalMatrix, "Mehrkosten-PVTotalMatrix");
					
					
			String PVTotalRefSensi =  PVTotalMatrix + " .* RefPCSensiOpt " ;
			PVTotalRefSensi = "(" + PVTotalRefSensi + ")";
			ojbfunc_optimize_dispDVExpr(PVTotalRefSensi, "Mehrkosten-PVTotalRefSensi");
					
			String BondSwapMehrKosten= "(  (1/"+ PVTotalVec+ ") .* (sum(   (PCRP1Opt .* ( (  ( sum( (decisionVarsBond) *  BondsRV2DOpt ,1) + sum( (decisionVarsSwap) *  SwapsRV2DOpt ,1)  )  + BestandFullOpt )  - ("+ PVTotalRefSensi + ")    ) )    ,2))  )";
			
			BondSwapMehrKosten= "(" + BondSwapMehrKosten + ") * 10000.0d";
			ojbfunc_optimize_dispDVExpr(BondSwapMehrKosten, "Mehrkosten-SwapMehrKosten");
					
			return BondSwapMehrKosten;
		}
		
		
		
		private void ojbfunc_optimize_expOBJFSwapMehrkosten_jgExpGDX() {
			//-- test the Mehrkosten expression --//
			ojbfunc_optimize_formBondSwapMehrkostenObjFunctExp();
			
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"PVCleanFullOpt", "PVCleanFullOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"BondsRV2DPVCleanOpt", "BondsRV2DPVCleanOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapsRV2DPVCleanOpt", "SwapsRV2DPVCleanOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"RefPCSensiOpt", "RefPCSensiOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"PCRP1Opt", "PCRP1Opt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"BondsRV2DOpt", "BondsRV2DOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"SwapsRV2DOpt", "SwapsRV2DOpt ");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSymbolInGDX(jom,"BestandFullOpt", "BestandFullOpt ");
			
		}
		
		
		
		private void  ojbfunc_optimize_addObjFunctSwapMehrkosten() {
						
			String SwapMehrKosten = ojbfunc_optimize_formSwapMehrkostenObjFunctExp();
			SwapMehrKosten= "sum(" + SwapMehrKosten + ", 1)";
			ojbfunc_optimize_dispDVExpr(SwapMehrKosten, "Mehrkosten-SwapMehrKosten");
			
			this.jom.setObjectiveFunction("minimize",  SwapMehrKosten);
		}
		//--- Add Objective Function -- Mehrkosten over!  --- //
		
		
		
		// -- Transfer Baukasten : ojbfunc_optimize_load --//
		public void ojbfunc_optimize_loadBaukasten_TransferMatrix() {
			jom.setInputParameter("BestandBondTMOpt", new DoubleMatrixND( this.joBestandBondTMEff )  );
			jom.setInputParameter("BestandSwapTMOpt", new DoubleMatrixND( this.joBestandSwapTMEff )  );
			jom.setInputParameter("PVCleanBondTMOpt", new DoubleMatrixND( this.joPVCleanBondTMEff )  );
			jom.setInputParameter("PVCleanSwapTMOpt", new DoubleMatrixND( this.joPVCleanSwapTMEff )  );
			
			//--Load the transformed RiskSensi for Swap and Bond --//
			jom.setInputParameter("BondRiskSensiTMOpt", new DoubleMatrixND( this.joBondRiskSensiTMEff) );//
			jom.setInputParameter("SwapRiskSensiTMOpt", new DoubleMatrixND(this.joSwapRiskSensiTMEff) );
			
			//--Load the transformed FixingRiskSensi for Swap and Bond --//
			jom.setInputParameter("FixingRiskBondTMOpt", new DoubleMatrixND(this.joBondILBSwapFxRiskBondTMEff) );
			jom.setInputParameter("FixingRiskSwapTMOpt", new DoubleMatrixND(this.joBondILBSwapFxRiskSwapTMEff) );
			
			//--Load the transformed Schuld for Swap and Bond --//
			jom.setInputParameter("SchuldBondTMOpt", new DoubleMatrixND(this.joSchuldBondTMEff) );
			jom.setInputParameter("SchuldSwapTMOpt", new DoubleMatrixND(this.joSchuldSwapTMEff) );
			
			ojbfunc_optimize_loadBaukastenRest();
		}
		
		
		// -- ojbfunc_optimize_ProcessSwapJOM --//
				// -- Baukasten - ojbfunc_optimize_ProcessSwapJOM --//
		public void ojbfunc_optimize_loadBaukasten_solveBondOnly() {
			
			if (this.jom == null) {
				this.jom = new OptimizationProblem();
				jom.resetTimer();
			}
			
			/*
			//--- Swaps-DKF ---//
			jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( this.joSwapsRVDataEff )  );
		 	jom.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
			jom.setInputParameter("SwapsRV2DAnnSensiOpt", new DoubleMatrixND( this.joSwapsRVAnnuitySensiEff )  );				 				
		 	jom.setInputParameter("SwapsRV2DAnnSensiOpt", "permute(SwapsRV2DAnnSensiOpt,[2;1;3] )" );
		 	double [][][] SwapsRV2DAnnSensiOptPermuted= (double[][][]) jom.getInputParameter("SwapsRV2DAnnSensiOpt").toArray();
		 	System.out.println(" Permuted SwapsRV2DAnnSensiOptPermuted: 3D-size: " + SwapsRV2DAnnSensiOptPermuted.length  + "; 2D-Row_size: " +  
		 			SwapsRV2DAnnSensiOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DAnnSensiOptPermuted[0][0].length );
		 
		 	
			jom.setInputParameter("SwapsRV2DDebtCleanOpt", new DoubleMatrixND( this.joSwapsRVDebtCleanEff )  );				 				
		 	jom.setInputParameter("SwapsRV2DDebtCleanOpt", "permute(SwapsRV2DDebtCleanOpt,[2;1;3] )" );
		 
		 	jom.setInputParameter("SwapsRV2DPVCleanOpt", new DoubleMatrixND( this.joSwapsRVPVCleanEff )  );				 				
		 	jom.setInputParameter("SwapsRV2DPVCleanOpt", "permute(SwapsRV2DPVCleanOpt,[2;1;3] )" );
		 	double [][][] SwapsRV2DPVCleanOptPermuted= (double[][][]) jom.getInputParameter("SwapsRV2DPVCleanOpt").toArray();
		 	System.out.println(" Permuted SwapsRV2DPVCleanOptPermuted: 3D-size: " + SwapsRV2DPVCleanOptPermuted.length  + "; 2D-Row_size: " +  
		 	SwapsRV2DPVCleanOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DPVCleanOptPermuted[0][0].length );
		 	
		 	
		 	//---Bonds - DKF---//
		 	jom.setInputParameter("BondsRV2DOpt", new DoubleMatrixND( this.joBondsRVDataEff )  );
		 	jom.setInputParameter("BondsRV2DOpt", "permute(BondsRV2DOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DOpt", "loadJOMSwap-BondsRV2DOpt");
		 			
			jom.setInputParameter("BondsRV2DAnnSensiOpt", new DoubleMatrixND( this.joBondsRVAnnuitySensiEff )  );				 				
		 	jom.setInputParameter("BondsRV2DAnnSensiOpt", "permute(BondsRV2DAnnSensiOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DAnnSensiOpt", "loadJOMSwap-BondsRV2DAnnSensiOpt-BondFxRV2DOpt");
		 	
		 	jom.setInputParameter("BondsRV2DDebtCleanOpt", new DoubleMatrixND( this.joBondsRVDebtCleanEff )  );				 				
		 	jom.setInputParameter("BondsRV2DDebtCleanOpt", "permute(BondsRV2DDebtCleanOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DDebtCleanOpt", "loadJOMSwap-BondsRV2DDebtCleanOpt");
		 	
		 	jom.setInputParameter("BondsRV2DPVCleanOpt", new DoubleMatrixND( this.joBondsRVPVCleanEff )  );				 				
		 	jom.setInputParameter("BondsRV2DPVCleanOpt", "permute(BondsRV2DPVCleanOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DPVCleanOpt", "loadJOMSwap-BondsRV2DPVCleanOpt");
		 	*/
		 	
		 	//-- TransferMatrix: FullOpt; PVOpt;  --//
		 	jom.setInputParameter("BestandOpt", new DoubleMatrixND( this.joBestandEff )  ); //--//
			jom.setInputParameter("PVCleanOpt", new DoubleMatrixND( this.joPVCleanEff )  );
			jom.setInputParameter("SchuldOpt", new DoubleMatrixND(this.joSchuldEff)  ); 
		 	jom.setInputParameter("SchuldVal", new DoubleMatrixND (this.joSchuldVal) );

		 	jom.setInputParameter("SwapSensiRiskOpt", new DoubleMatrixND(this.joSwapSensiRiskEff)  ); //--//
		 	this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-joSwapSensiRiskEff", this.joSwapSensiRiskEff,10);	
		 	
		 	//jom.setInputParameter("SwapFxRiskOpt", new DoubleMatrixND(joSwapFxRiskEff)  );
		 	jom.setInputParameter("SwapFxRiskOpt", new DoubleMatrixND(this.joBondILBSwapFxRiskEff)  ); //--//
		 	jom.setInputParameter("BondILBSwapFxRiskOpt", new DoubleMatrixND(this.joBondILBSwapFxRiskEff)  );

		 	ojbfunc_optimize_loadBaukasten_TransferMatrix();
		 	
		 	jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(joPCRP1Eff) );  				
		 	
			jom.setInputParameter("BestandFullOpt", "BestandOpt + BestandSwapTMOpt + BestandBondTMOpt"  );
			jom.setInputParameter("PVCleanFullOpt", "PVCleanOpt + PVCleanSwapTMOpt + PVCleanBondTMOpt" );
			
			jom.setInputParameter("SchuldFullOpt", "SchuldOpt + SchuldSwapTMOpt + SchuldBondTMOpt" );
			double[][] SchuldFullOptArray =  (double[][] ) jom.getInputParameter("SchuldFullOpt").toArray() ;
			this.oxl.genTxtFileFrom2DDataFmt(filepath, "SchuldFullOpt", SchuldFullOptArray,10);
			
			jom.setInputParameter("SwapSensiRiskFullOpt", "SwapSensiRiskOpt + BondRiskSensiTMOpt + SwapRiskSensiTMOpt" );
			jom.setInputParameter("SwapFxRiskFullOpt", "SwapFxRiskOpt + FixingRiskBondTMOpt + FixingRiskSwapTMOpt" );
			jom.setInputParameter("FxRiskFullOpt", "BondILBSwapFxRiskOpt + FixingRiskBondTMOpt + FixingRiskSwapTMOpt" );
			
			//-- Main --//
			jom.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(joRefPCSensiEff) ); 
		 	jom.setInputParameter("SwapCovar", new DoubleMatrixND(joPCCov)  ); 				
		 	
			jom.setInputParameter("PCCov_Mkt1", new DoubleMatrixND(joPCCov_Mkt1)  );
		 	jom.setInputParameter("PCRP_Mkt1", new DoubleMatrixND(joPCRP_Mkt1)  );
		 	jom.setInputParameter("FixRiskCov_Mkt1", new DoubleMatrixND(joFixRiskCov_Mkt1)  );
		 	
		}
		
		
		// -- ojbfunc_optimize_ProcessSwapJOM --//
		// -- Baukasten - ojbfunc_optimize_ProcessSwapJOM --//
		public void ojbfunc_optimize_loadBaukastenRest() {
			//Swap - Baukasten --//
			jom.setInputParameter("SwapsRV2DOpt", new DoubleMatrixND( this.joSwapsRVDataEff )  );
		 	jom.setInputParameter("SwapsRV2DOpt", "permute(SwapsRV2DOpt,[2;1;3] )" );
		 	double [][][] SwapsRV2DOptPermuted= (double[][][]) jom.getInputParameter("SwapsRV2DOpt").toArray();
		 	System.out.println(" Permuted swapsRVDataffQ3Y: 3D-size: " + SwapsRV2DOptPermuted.length  + "; 2D-Row_size: " +  
		 				SwapsRV2DOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DOptPermuted[0][0].length );
		 	
		 	jom.setInputParameter("SwapsRV2DAnnSensiOpt", new DoubleMatrixND( this.joSwapsRVAnnuitySensiEff )  );				 				
		 	jom.setInputParameter("SwapsRV2DAnnSensiOpt", "permute(SwapsRV2DAnnSensiOpt,[2;1;3] )" );
		 	double [][][] SwapsRV2DAnnSensiOptPermuted= (double[][][]) jom.getInputParameter("SwapsRV2DAnnSensiOpt").toArray();
		 	System.out.println(" Permuted SwapsRV2DAnnSensiOptPermuted: 3D-size: " + SwapsRV2DAnnSensiOptPermuted.length  + "; 2D-Row_size: " +  
		 			SwapsRV2DAnnSensiOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DAnnSensiOptPermuted[0][0].length );
		 
		 	
			jom.setInputParameter("SwapsRV2DDebtCleanOpt", new DoubleMatrixND( this.joSwapsRVDebtCleanEff )  );				 				
		 	jom.setInputParameter("SwapsRV2DDebtCleanOpt", "permute(SwapsRV2DDebtCleanOpt,[2;1;3] )" );
		 
		 	jom.setInputParameter("SwapsRV2DPVCleanOpt", new DoubleMatrixND( this.joSwapsRVPVCleanEff )  );				 				
		 	jom.setInputParameter("SwapsRV2DPVCleanOpt", "permute(SwapsRV2DPVCleanOpt,[2;1;3] )" );
		 	double [][][] SwapsRV2DPVCleanOptPermuted= (double[][][]) jom.getInputParameter("SwapsRV2DPVCleanOpt").toArray();
		 	System.out.println(" Permuted SwapsRV2DPVCleanOptPermuted: 3D-size: " + SwapsRV2DPVCleanOptPermuted.length  + "; 2D-Row_size: " +  
		 			SwapsRV2DPVCleanOptPermuted[0].length + "; 2D-col-size: " + SwapsRV2DPVCleanOptPermuted[0][0].length );
		 	//Swap - Baukasten --//
		 	
		 	
		 	//Bond - Baukasten --//
			jom.setInputParameter("BondsRV2DOpt", new DoubleMatrixND( this.joBondsRVDataEff )  );
		 	jom.setInputParameter("BondsRV2DOpt", "permute(BondsRV2DOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DOpt", "loadJOMSwap-BondsRV2DOpt");
		 	
		 	jom.setInputParameter("BondsRV2DAnnSensiOpt", new DoubleMatrixND( this.joBondsRVAnnuitySensiEff )  );				 				
		 	jom.setInputParameter("BondsRV2DAnnSensiOpt", "permute(BondsRV2DAnnSensiOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DAnnSensiOpt", "loadJOMSwap-BondsRV2DAnnSensiOpt-BondFxRV2DOpt");
		 	
		 	jom.setInputParameter("BondsRV2DDebtCleanOpt", new DoubleMatrixND( this.joBondsRVDebtCleanEff )  );				 				
		 	jom.setInputParameter("BondsRV2DDebtCleanOpt", "permute(BondsRV2DDebtCleanOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DDebtCleanOpt", "loadJOMSwap-BondsRV2DDebtCleanOpt");
		 	
		 	jom.setInputParameter("BondsRV2DPVCleanOpt", new DoubleMatrixND( this.joBondsRVPVCleanEff )  );				 				
		 	jom.setInputParameter("BondsRV2DPVCleanOpt", "permute(BondsRV2DPVCleanOpt,[2;1;3] )" );
		 	ojbfunc_optimize_dispDVExpr("BondsRV2DPVCleanOpt", "loadJOMSwap-BondsRV2DPVCleanOpt");
		 	
		 	//Bond - Baukasten --//
			
		}
		
		
		private void ojbfunc_optimize_loadSwapJOM(boolean loadOn) {
			
			if (!loadOn) return;
			
			if (this.jom == null) {
				this.jom = new OptimizationProblem();
				jom.resetTimer();
				//jom.addDecisionVariable("decisionVars", false, new int[] {1, (this.ruleConf.numDecisionVariables) } ); //357 for 51 years annually - 168
				//jom.addDecisionVariable("DVSwap", false, new int[] {1, (this.ruleConf.numDecisionVariables) } ); //357 for 51 years annually - 168
			}
			this.ruleConf.TRANSMAT = true;
			
			jom.setInputParameter("upConstraints", new DoubleMatrixND(this.jUPConstLtd) );				
			jom.setInputParameter("downConstraints",new DoubleMatrixND(this.jDNConstLtd) );
			jom.setInputParameter("StockLimit",new DoubleMatrixND(this.stockVolLimitEff) );
			
			jom.setInputParameter("SwapRiskLimit",new DoubleMatrixND(this.joSwapRiskLimitEff) );
			
			jom.setInputParameter("BestandOpt", new DoubleMatrixND( this.joBestandEff )  );
			
			jom.setInputParameter("PVCleanOpt", new DoubleMatrixND( this.joPVCleanEff )  );
			
			
		 	jom.setInputParameter("SchuldOpt", new DoubleMatrixND(this.joSchuldEff)  ); 
		 	jom.setInputParameter("SchuldVal", new DoubleMatrixND (this.joSchuldVal) );
		 	
		 	jom.setInputParameter("SwapSensiRiskOpt", new DoubleMatrixND(this.joSwapSensiRiskEff)  );
		 	this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-joSwapSensiRiskEff", this.joSwapSensiRiskEff,10);	
		 	
		 	//jom.setInputParameter("SwapFxRiskOpt", new DoubleMatrixND(joSwapFxRiskEff)  );
		 	jom.setInputParameter("SwapFxRiskOpt", new DoubleMatrixND(this.joBondILBSwapFxRiskEff)  );
		 	jom.setInputParameter("BondILBSwapFxRiskOpt", new DoubleMatrixND(this.joBondILBSwapFxRiskEff)  );
			
			// -- Load the data into JOM --//
			if (this.ruleConf.TRANSMAT == true)  {
				
				ojbfunc_optimize_loadBaukasten_TransferMatrix();
				jom.setInputParameter("BestandFullOpt", "BestandOpt + BestandSwapTMOpt + BestandBondTMOpt"  );
				jom.setInputParameter("PVCleanFullOpt", "PVCleanOpt + PVCleanSwapTMOpt + PVCleanBondTMOpt" );
			
				jom.setInputParameter("SchuldFullOpt", "SchuldOpt + SchuldSwapTMOpt + SchuldBondTMOpt" );
				double[][] SchuldFullOptArray =  (double[][] ) jom.getInputParameter("SchuldFullOpt").toArray() ;
				this.oxl.genTxtFileFrom2DDataFmt(filepath, "SchuldFullOpt", SchuldFullOptArray,10);
				
				jom.setInputParameter("SwapSensiRiskFullOpt", "SwapSensiRiskOpt + BondRiskSensiTMOpt + SwapRiskSensiTMOpt" );
				jom.setInputParameter("SwapFxRiskFullOpt", "SwapFxRiskOpt + FixingRiskBondTMOpt + FixingRiskSwapTMOpt" );
				jom.setInputParameter("FxRiskFullOpt", "BondILBSwapFxRiskOpt + FixingRiskBondTMOpt + FixingRiskSwapTMOpt" );
				
			}
			else { 
				ojbfunc_optimize_loadBaukastenRest();
			}
		 	
		 	jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(joPCRP1Eff) );  				
		 		
		 			 	
		 	jom.setInputParameter("RefPCSensiOpt", new DoubleMatrixND(joRefPCSensiEff) ); 
		 	this.oxl.genTxtFileFrom2DDataFmt(filepath, "Mehrkosten-joRefPCSensiEff", joRefPCSensiEff,10);	
		 	
		 	jom.setInputParameter("SwapCovar", new DoubleMatrixND(joPCCov)  ); 				
		 	
		 	System.out.println (" joRefPCSensiEff: " + optimStaticType.arr2DArchSpec(joRefPCSensiEff, true));
		 	System.out.println (" joPCCov: " + optimStaticType.arr2DArchSpec(joPCCov,true));
		 	
		 	
		 	
		 	System.out.println (" joSwapSensiRiskEff: " + optimStaticType.arr2DArchSpec(joSwapSensiRiskEff,true) );
		 	System.out.println (" joSwapFxRiskEff: " + optimStaticType.arr2DArchSpec(joSwapFxRiskEff,true) );
		 	System.out.println (" joPCCov_Mkt1: " + optimStaticType.arr2DArchSpec(joPCCov_Mkt1,true) );
		 	System.out.println (" joPCRP_Mkt1: " + optimStaticType.arr2DArchSpec(joPCRP_Mkt1,true) );
		 	System.out.println (" joFixRiskCov_Mkt1: " + optimStaticType.arr2DArchSpec(joFixRiskCov_Mkt1,true) );
		 	
		 	jom.setInputParameter("PCCov_Mkt1", new DoubleMatrixND(joPCCov_Mkt1)  );
		 	jom.setInputParameter("PCRP_Mkt1", new DoubleMatrixND(joPCRP_Mkt1)  );
		 	jom.setInputParameter("FixRiskCov_Mkt1", new DoubleMatrixND(joFixRiskCov_Mkt1)  );
		 	jom.setInputParameter("PCCov_Mkt2", new DoubleMatrixND(joPCCov_Mkt2)  );
		 	jom.setInputParameter("PCRP_Mkt2", new DoubleMatrixND(joPCRP_Mkt2)  );
		 	jom.setInputParameter("FixRiskCov_Mkt2", new DoubleMatrixND(joFixRiskCov_Mkt2)  );
		 	jom.setInputParameter("PCCov_Mkt3", new DoubleMatrixND(joPCCov_Mkt3)  );
		 	jom.setInputParameter("PCRP_Mkt3", new DoubleMatrixND(joPCRP_Mkt3)  );
		 	jom.setInputParameter("FixRiskCov_Mkt3", new DoubleMatrixND(joFixRiskCov_Mkt3)  );
		 	jom.setInputParameter("PCCov_Mkt4", new DoubleMatrixND(joPCCov_Mkt4)  );
		 	jom.setInputParameter("PCRP_Mkt4", new DoubleMatrixND(joPCRP_Mkt4)  );
		 	jom.setInputParameter("FixRiskCov_Mkt4", new DoubleMatrixND(joFixRiskCov_Mkt4)  );
		 	jom.setInputParameter("PCCov_Mkt5", new DoubleMatrixND(joPCCov_Mkt5)  );
		 	jom.setInputParameter("PCRP_Mkt5", new DoubleMatrixND(joPCRP_Mkt5)  );
		 	jom.setInputParameter("FixRiskCov_Mkt5", new DoubleMatrixND(joFixRiskCov_Mkt5)  );
			
		}
		
    	
		private void ojbfunc_optimize_setupRuleTM() {
			this.ruleConf.TRANSMAT= true;
			this.ruleConf.InstrDimForwardSquare= true; // may be false - no differenec//
			this.ruleConf.TimeDimForwardSquare=true; 
		}
    	
		
		
		///--- OF- JGANS ---//
		public void ojbfunc_optimize_ruleConfig_Swap_jgExpGDX() {   
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSAsScalarInGDX(this.jom, (double) this.ruleConf.numDecisionVariables, "numDV");
			//com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSAsScalarInGDX(this.jom, (double) this.ruleConf.numBondDecisionVariables, "numBondDV");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSAsScalarInGDX(this.jom, (double) this.ruleConf.numSwapDecisionVariables, "numSwapDV");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSAsScalarInGDX(this.jom, (double) this.ruleConf.numYears, "numYears_swap");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSAsScalarInGDX(this.jom, (double) (this.ruleConf.numYears * 12), "numMonths_swap");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSAsScalarInGDX(this.jom, (double) this.ruleConf.numDateRows, "numDates_swap");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportJOMSAsScalarInGDX(this.jom, (double) this.ruleConf.numSRVRawRowsPerDate, "numSRVRowsPerDate_swap");
					
			com.stfe.optim.jgams.jgUtilJomGams.jgExportIntAsSetInGDX((int) this.ruleConf.numDecisionVariables, "numDV");
			//com.stfe.optim.jgams.jgUtilJomGams.jgExportIntAsSetInGDX((int) this.ruleConf.numBondDecisionVariables, "numBondDV");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportIntAsSetInGDX((int) this.ruleConf.numSwapDecisionVariables, "numSwapDV");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportIntAsSetInGDX((int) this.ruleConf.numYears, "numYears_swap");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportIntAsSetInGDX((int) (this.ruleConf.numYears * 12), "numMonths_swap");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportIntAsSetInGDX((int) this.ruleConf.numDateRows, "numDates_swap");
			com.stfe.optim.jgams.jgUtilJomGams.jgExportIntAsSetInGDX((int) this.ruleConf.numSRVRawRowsPerDate, "numSRVRowsPerDate_swap"); 
		}
		
		
		
		public void ojbfunc_optimize_jgExpGDX_exportSwapsAll() {
			com.stfe.optim.jgams.jgUtilJomGams.jgInitGDXSessionForJOPS();
			
			
			/*
			//jom.addConstraint("decisionVars(0,all) <= upConstraints(0,all)");
		 	jom.addConstraint("decisionVars <= upConstraints");
		 	double [][] volUpConstraint =  (double[][]) this.jom.parseExpression("upConstraints").evaluate().toArray();
		 	System.out.println("Vol UP Limit Constraints - 1st Year: upConstraints size= " + volUpConstraint[0].length + "; Data = " + volUpConstraint[0][0] + " : "+ + volUpConstraint[0][1]+ " : "+ + volUpConstraint[0][2] +
		 			 " : "+ + volUpConstraint[0][3] +  " : "+ + volUpConstraint[0][4]+  " : "+ + volUpConstraint[0][5] +  " : "+ + volUpConstraint[0][6]);
		 	
		 	System.out.println("Vol UP Limit Constraints - Last Year: upConstraints size= " + volUpConstraint[0].length + "; Data = " + volUpConstraint[0][volUpConstraint[0].length-7] + " : "+ + volUpConstraint[0][volUpConstraint[0].length-6]+ " : "+ + volUpConstraint[0][volUpConstraint[0].length-5] +
		 			 " : "+ + volUpConstraint[0][volUpConstraint[0].length-4] +  " : "+ + volUpConstraint[0][volUpConstraint[0].length-3]+  " : "+ + volUpConstraint[0][volUpConstraint[0].length-2] +  " : "+ + volUpConstraint[0][volUpConstraint[0].length-1]);
		 	
			//jom.addConstraint("decisionVars(0,all) >= downConstraints(0,all)");
		 	jom.addConstraint("decisionVars >= downConstraints");
		 	double [][] volDownConstraint =  (double[][]) this.jom.parseExpression("downConstraints").evaluate().toArray();
		 	System.out.println("Vol Down Limit Constraints - 1st Year: downConstraints size= " + volDownConstraint[0].length + "; Data = " + volDownConstraint[0][0] + " : "+ + volDownConstraint[0][1]+ " : "+ + volDownConstraint[0][2] +
		 			 " : "+ + volDownConstraint[0][3] +  " : "+ + volDownConstraint[0][4]+  " : "+ + volDownConstraint[0][5] +  " : "+ + volDownConstraint[0][6]);
		 	
		 	System.out.println("Vol Down Limit Constraints - Last Year: downConstraints size= " + volDownConstraint[0].length + "; Data = " + volDownConstraint[0][volDownConstraint[0].length-7] + " : "+ + volDownConstraint[0][volDownConstraint[0].length-6]+ " : "+ + volDownConstraint[0][volDownConstraint[0].length-5] +
		 			 " : "+ + volDownConstraint[0][volDownConstraint[0].length-4] +  " : "+ + volDownConstraint[0][volDownConstraint[0].length-3]+  " : "+ + volDownConstraint[0][volDownConstraint[0].length-2] +  " : "+ + volDownConstraint[0][volDownConstraint[0].length-1]);
		 	*/
			
		 	
			//--Set up Constraints - Vol Limit -- //			
		 	//ojbfunc_optimize_addConstraintSwapVolume();
		 	ojbfunc_optimize_expConstraintSwapVolume_jgExpGDX();
		 	com.stfe.optim.jgams.JGamsEnv.GDXDump("opti_jops_GDX_ConstSwapVol");
			com.stfe.optim.jgams.JGamsEnv.DBClean();
						
			//-- Set up Swap Risk constraint --// 
		 	//ojbfunc_optimize_addConstraintSwapRisk();
		 	ojbfunc_optimize_expConstraintSwapRisk_jgExpGDX();
		 	com.stfe.optim.jgams.JGamsEnv.GDXDump("opti_jops_GDX_ConstSwapRisk");
			com.stfe.optim.jgams.JGamsEnv.DBClean();
						
			//-- Set up Swap Fx Risk constraint --//
		 	//ojbfunc_optimize_addConstraintSwapFxRisk();
		 	ojbfunc_optimize_expConstraintSwapFxRisk_jgExpGDX();
		 	com.stfe.optim.jgams.JGamsEnv.GDXDump("opti_jops_GDX_ConstSwapFXRisk");
			com.stfe.optim.jgams.JGamsEnv.DBClean();
			
			//-- Mehrkosten --//
		 	//ojbfunc_optimize_addObjFunctSwapMehrkosten();
		 	ojbfunc_optimize_expOBJFSwapMehrkosten_jgExpGDX();
		 	com.stfe.optim.jgams.JGamsEnv.GDXDump("opti_jops_GDX_OFSwapMehrkosten");
			com.stfe.optim.jgams.JGamsEnv.DBClean();
			
			
			//-- Config --//
			ojbfunc_optimize_ruleConfig_Swap_jgExpGDX();
			com.stfe.optim.jgams.JGamsEnv.GDXDump("opti_jops_GDX_CFSwapRuleConfig");
			com.stfe.optim.jgams.JGamsEnv.DBClean();
			
			com.stfe.optim.jgams.jgUtilJomGams.jgShutdownGDXSessionForJOPS();
			
			//////////////////////////////////////////////
			//PC - 1:31 =  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt )
			//Cost- =  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (decisionVars) *  SwapsRV2DOpt ,1) + BestandOpt ))
			
		}
		
		
		
		// -- ojbfunc_optimize_ProcessSwapJOM-GAMS --//
		public void ojbfunc_optimize_ProcessSwapJOM_JGams(optimSolveFunctApp appMainObj) {
    	
    		boolean invokeSolverFlag = false;
    		    		
    		// Setting up the appMainObj util //
			this.filepath = appMainObj.filepath;
			this.oxl = appMainObj.oxl;
	    	this.dataSet = appMainObj.dataSet;
			this.functUtil = appMainObj.functUtil;
			this.ruleConf= appMainObj.ruleConf;
			
			if (appMainObj.jom == null) {
				System.out.println("Initializing JOM for GAMS-GDX!");
				this.jom = new OptimizationProblem();
				jom.resetTimer();
				
			} else {
				System.out.println("Setting up JOM initialized for SWAP!");
				this.jom= appMainObj.jom;  // decisionVars//
			}
			jom.addDecisionVariable("decisionVars", false, new int[] {1, (this.ruleConf.numDecisionVariables) } );
			jom.addDecisionVariable("decisionVarsSwap", false, new int[] {1, (this.ruleConf.numDecisionVariables) } );
			jom.addDecisionVariable("decisionVarsBond", false, new int[] {1, (this.ruleConf.numDecisionVariables) } );
		
			
						
			//All GAMS GDX data //
			ojbfunc_optimize_jgExpGDX_exportSwapsAll();
						
			System.out.println(" Swaps jGAMS-GDX generation over! " );
			System.out.println(" Over & All!");			
			
    	} //-- ojbfunc_optimize_rule --//
    	
    	  
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    