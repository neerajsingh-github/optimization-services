
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

    public class optimSolveRuleConfig {
					
    	public boolean ruleConfigFlag=false;
    	//constant//
    	public int numElements; //--7 constant - fixed elements--//		
    	public int numQuartersInYear; // -- 4 constant - fixed quarters//	
    	public int monthsInYear;  //-- 12 constant --//
		

		//--Reduce raw data to get effective Data --//
    	public int numYears; //- for how many years
    	public int numDateRows;
    	public int dfkColSize;
    	public int dfkRowsAll;
    	public double numSRVperDateRows;
    	public int numSRVRawRowsPerDate;

		//-- Decision Variables --//		
    	public int numDecisionVariables ;//-- same as numSRVperDateRows-- //
    	public int numSwapDecisionVariables ;//-- same as numSRVperDateRows-- //
    	public int numBondDecisionVariables ;//-- same as numSRVperDateRows-- //
    	
				
		//-- function addDKF --//								
    	public boolean addDKF ; // -- true /false --//
    	public double addDKFForYears;  //-- if addDKF then 0.25 is quaterly, 0.5 is half-yearly, 1 is annually, 2 is biannually --//						
    	public double numAnnuality;      //-- if addDKF then 0.25 is quaterly, 0.5 is half-yearly, 1 is annually, 2 is biannually --//
				
		// - setup constraints --//
    	public double stockVolLimit ; // --15 for quarterly, 60 for annually, 120 for bianuually --//				 
    	public double numAnnualConstraints; //--without addDKF always 4;;  if addDKF then annually=1; biannually=1/2 ; triannually=1/3;
		
		//-- risk setup --//
    	public int swapSensiColSize;
    	public int bondFxColSize, ILBFxColSize, swapFxColSize ;
		
    	
		//--use Transformation Matrix --//
    	public boolean TRANSMAT= false;
    	public boolean InstrDimForwardSquare= false;
    	public boolean TimeDimForwardSquare=false;
		
    	
		
		//-- constructor for rule config --//
		public void optimSolveRuleConfig(){
			if (this.ruleConfigFlag==false) implicitInitAddDKFYearZero();
		} //-- optimSolveRuleConfig --//   
		
		
		public void optimSolveRuleConfig(int nYears, boolean addDKF, double addDKFForYears ){	
			if (this.ruleConfigFlag==false) implicitInitAddDKFYearZero();
			setDKFRuleConfig(nYears, addDKF, addDKFForYears );	
			if (ruleConfigValidate() == false) 
				System.out.println(" optimSolveRuleConfig failed! ");
		} //--optimSolveRuleConfig--set param--//
		
		
		
		//--- Default and Explicit Setup ---//
		private void implicitInitAddDKFYearZero() {
			this.numElements=7;						
			this.numQuartersInYear=4;
			this.monthsInYear=12;
			
			//*** -- start with any initial value-- Re-setup later --- ***//
			this.numYears=6; 
			this.addDKF = false; 	 
			this.addDKFForYears=0.25;
			this.numAnnuality=0.25;  // -- (1.0) / (this.numAnnualConstraints);-- //
			this.numAnnualConstraints= 4; //--1 / (this.addDKFForYears); --//							  
			this.stockVolLimit=15.0 ; // -- (60.0) * (this.addDKFForYears);--//
			this.swapSensiColSize=10; // -- Swap Risk: Sensi, Fixing risk col 10 --//
			this.bondFxColSize=10;
			this.ILBFxColSize=11;
			this.swapFxColSize=10;
						
			//-- DV setup -- //
			//this.numDateRows = this.monthsInYear * this.numYears;
			// For 51 years -- this.numDateRows = 291; //-- dates are monthly before 3.12.2027, thereadfter quaterly; --
			// General rules -- //this.numDateRows = this.monthsInYear * this.numYears;
			//- Specific rules for numDateRows --/
			//-- dates are monthly before 3.12.2027, thereadfter quaterly; --//			
			if (this.numYears<=11) {
				this.numDateRows = 12 * this.numYears;
			} else {
				this.numDateRows = (12 * 11) + (4 * (this.numYears - 11)) ;
			}
			if (this.numYears==51) this.numDateRows = 292; 
						
			
			this.dfkColSize=31;
			//this.dfkRowsAll=448; //--old--//
			this.dfkRowsAll=1428;
			this.numSRVRawRowsPerDate=  (this.numYears) * (this.numQuartersInYear) * (this.numElements); 
			this.numSRVperDateRows= (this.numYears) * (this.numQuartersInYear) * (this.numElements);
			this.numDecisionVariables= (this.numYears) * (this.numQuartersInYear) * (this.numElements);
			
			this.ruleConfigFlag=true;
			

			if (ruleConfigValidate() == false) 
				System.out.println(" optimSolveRuleConfig failed! ");
		} //-- implicitDKFYearZero--//
		
		
		//-- getNumDateRows from Rule Config--//
		public int getNumDateRowsRule(int numYears) {
			int numDR=0;
			if (this.numYears<=11) {
				numDR = 12 * this.numYears;
			} else {
				numDR = (12 * 11) + (4 * (this.numYears - 11)) ;
			}
			if (this.numYears==51) numDR = 292; 
			return numDR;
		}
		
		
		//-- Explicit Setup Rules  --//
		//-- setDKFRuleConfig - Explicit Setup Rules  --//
		public void setDKFRuleConfig(int nYears, boolean addDKF, double addDKFForYears ){
			
			if (this.ruleConfigFlag==false) implicitInitAddDKFYearZero();
			
			if (  (nYears<=51) | (nYears>=1) )  this.numYears=nYears;
			else { 
				System.out.println(" Number of years can not be less than 1 or more than 51 !");
				System.out.println("Seeting it up to 6 years!");
				this.numYears=6;
			}
			
			// General rules -- //this.numDateRows = this.monthsInYear * this.numYears;
			//- Specific rules for numDateRows --/
			//-- dates are monthly before 3.12.2027, thereafter quarterly; --//			
			if (this.numYears<=11) {
				this.numDateRows = 12 * this.numYears;
			} else {
				this.numDateRows = (12 * 11) + (4 * (this.numYears - 11)) ;
			}
			if (this.numYears==51) this.numDateRows = 292; 
			
			
			this.dfkColSize=31;
			//this.dfkRowsAll=448; // -- old --
			this.dfkRowsAll=1428; // for 51 years.
			
			this.numSRVRawRowsPerDate=  (this.numYears) * (this.numQuartersInYear) * (this.numElements); // valid only for 51 years; 51 * 4 * 7 = 1428;
			this.numSRVperDateRows= (this.numYears) * (this.numQuartersInYear) * (this.numElements); 			
			this.numDecisionVariables= (this.numYears) * (this.numQuartersInYear) * (this.numElements);
			
			//-- handle addDKF --//
			if (addDKF == true) {
				this.addDKF = true;
				implicitAddDKFYearMult(addDKFForYears);
			} 
			 
			this.numSRVperDateRows= (this.numYears) * (this.numAnnualConstraints) * (this.numElements);
			this.numDecisionVariables= (int) (this.numSRVperDateRows);
			
			if ( (this.numSRVperDateRows % 1) != 0) {
				System.out.println(" numSRVperDateRows and  numDecisionVariables are not consistent. DKF inconsistent!");	
			} 			
			
			if (ruleConfigValidate() == true) {
				System.out.println(" Number of Decision-Variables :" + this.numDecisionVariables );
				System.out.println(" optimSolveRuleConfig successful! ");
			}
			
			System.out.println("optimSolveRuleConfig: Config Setup : " + "numYears: " + this.numYears + "; addDKF: " + this.addDKF + ", Years:" + this.addDKFForYears + " ; StockLimit:"+ this.stockVolLimit );
			System.out.println("optimSolveRuleConfig: Config Setup : " + "numDateRows: " + this.numDateRows + "; numSRVRawRowsPerDate: " + this.numSRVRawRowsPerDate );
			System.out.println("optimSolveRuleConfig: Config Setup : " + "numDecisionVariables: " + this.numDecisionVariables + "; numSRVperDateRows" + this.numSRVperDateRows); 
		}	
		//-- setDKFRuleConfig Over -- //
		
		
		//-- Helper function: implicitAddDKFYearMult - Explicit Setup Rules  --//
		private void implicitAddDKFYearMult(double addDKFForYears) {
			
			this.addDKFForYears=1; //- default setup--//
			
			if (addDKFForYears==0.25 || addDKFForYears==0.50 || addDKFForYears==1 || addDKFForYears==2 || addDKFForYears==3 || addDKFForYears==4 ) {
				if (this.addDKF == true) this.addDKFForYears=addDKFForYears;
			} else  {
				System.out.println(" ??? optimSolveRuleConfig: addDKFForYears can have valid values as 0.25, 0.5, 1, 2, 3 or 4 !");
				System.out.println("optimSolveRuleConfig:: Config setup to default 1 !");	
			}
			
			this.numAnnuality= this.addDKFForYears;
			this.numAnnualConstraints= 1 / (this.addDKFForYears);			
			this.stockVolLimit = (60.0) * (this.addDKFForYears);
			System.out.println("optimSolveRuleConfig: Config Setup : " + "addDKFForYears: " + this.addDKFForYears + "; numAnnualConstraints: " +this.numAnnualConstraints + "; stockVolLimit: " + this.stockVolLimit );
						
		} //  -- implicitAddDKFYearMult --//
		
		
		
		
		
		
		//-- Extra function --//
		//-- Set up the constraint for Stock-Volume like,  op.addConstraint(" sum (   sqrt( (decisionVars(0,0:6) ^ 2)   + 0.001d) ) <= StockVolLimit "); --//
		public void setVolumeConstraint (double stockVolLimit,  int numYears, double numAnnualConstaint){				
			double oldStockVolLimit = this.stockVolLimit; 
			this.stockVolLimit=stockVolLimit;					
			if (ruleConfigValidate() == true) 
				System.out.println(" setVolumeConstraint successful! ");
			else this.stockVolLimit=oldStockVolLimit;
		} // -- setVolumeConstraint over --//
				
		
		
		
		// -- Validity Check --//
		private int implicitRuleDKFConfigValidate() {
			int validFlag=0;			
			if (this.addDKF == true) {
				if (this.addDKFForYears==0.25 || this.addDKFForYears==0.50 || this.addDKFForYears==1 || this.addDKFForYears==2 || this.addDKFForYears==3 || this.addDKFForYears==4  ) validFlag++;
				if (this.numAnnuality == this.addDKFForYears) validFlag++;
				if (this.numAnnualConstraints == 1 / (this.addDKFForYears) ) validFlag++;			
				if (this.stockVolLimit == (60.0) * (this.addDKFForYears) ) validFlag++;
				if (this.numSRVperDateRows == (this.numYears) * (this.numAnnualConstraints) * (this.numElements) ) validFlag++;
				if (this.numDecisionVariables == (int)this.numSRVperDateRows ) validFlag++;
				if ( (this.numSRVperDateRows % 1) == 0) validFlag++;
			}
			if (this.addDKF == false) {
				if ( (this.addDKFForYears==0.25) || (this.addDKFForYears==0.0) ) validFlag++;
				if (this.numAnnuality==0.25)  validFlag++; 
				if (this.numAnnualConstraints== 4) validFlag++; 							  
				if (this.stockVolLimit==15.0) validFlag++; 
				if (this.numSRVperDateRows == (this.numYears) * (this.numQuartersInYear) * (this.numElements) ) validFlag++; 			
				if (this.numDecisionVariables == (this.numYears) * (this.numQuartersInYear) * (this.numElements) ) validFlag++;	 
				if ( (this.numSRVperDateRows % 1) == 0) validFlag++;
			}			
			if (validFlag != 7) 
				System.out.println(" ruleConfigValidate : implicitRuleDKFConfigValidate validity check failed ! Check the DKF ???  ");
			return validFlag; // should be 6 //
		} // -- implicitRuleDKFConfigValidate over --//
		
		
		private boolean ruleConfigValidate() {			
			int invalidFlag=0;
			
			if ( implicitRuleDKFConfigValidate() != 7 )  invalidFlag++;			
			//if ( (this.numYears>16) | (this.numYears<1) ) invalidFlag++;  
			if ( (this.numYears>61) | (this.numYears<1) ) invalidFlag++;
						
			if (invalidFlag>0) {
				System.out.println(" ruleConfigValidate : Validity check failed !  Check the consistency !");
				return false;
			} else { 
				System.out.println(" ruleConfigValidate : Validity check successful! ");
			}
			
			return true;
		}//-- ruleConfigValidate --//
		
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    