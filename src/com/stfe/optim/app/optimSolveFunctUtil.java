
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

    public class optimSolveFunctUtil {
				    	
    	
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
		
		
		
		public void calcValueObjFunctForZero(OptimizationProblem op, optimSolveDataset dataSet, int numDecVar) {
			//--Calculate and compare the value of objective function --//			
			double [] initSolZero = dataSet.init0Sol7V;
			double[][] solInit = new double[1][numDecVar];
			solInit[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolZero), (numDecVar/7));
			op.setInputParameter("solInitOpt", new DoubleMatrixND(solInit));
			
			//System.out.println ("** Zero-Init : Schuld-Scalar:  Optimal Objective Function Value:  ** " + 
			//op.parseExpression(" sum ( (  (PCRP1Opt .*  (sum( (solInitOpt) *  SwapsRV2DOpt ,1) + BestandOpt ))  /ShuldVal)* 10000.0 ) ").evaluateConstant()   );
					
			System.out.println ("** Zero-Init: Schuld-Vector : Optimal Objective Function Value:  ** " + 
			op.parseExpression("sum ( (  (1/SchuldOpt') * (PCRP1Opt .*  (sum( (solInitOpt) *  SwapsRV2DOpt ,1) + BestandOpt )) )* 10000.0 )").evaluateConstant()   );
		
		}
		
		public String[][] setIPOPTOptionParamValues(OptimizationProblem op){
			//--IPOPT options--//
			String [][] IPOPTParamValues = new String[6][6];
			
			String solverfullfile = "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll";
			String solverLibraryName=solverfullfile;
			String print_level = "6";  // default 0
			String print_options_documentation = "yes";  // default no
			String max_iter = "4000"; // default 3000
			String max_cpu_time = "1000000"; // default 1 million seconds
			String derivative_test = "second-order"; // default first-order
			
			IPOPTParamValues[0][0]="solverLibraryName";
			IPOPTParamValues[0][1]="C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll";
			IPOPTParamValues[1][0]="print_level";
			IPOPTParamValues[1][1]="6";			
			IPOPTParamValues[2][0]="print_options_documentation";
			IPOPTParamValues[2][1]="yes";
			IPOPTParamValues[3][0]="max_iter";
			IPOPTParamValues[3][1]="4000";			
			IPOPTParamValues[4][0]="max_cpu_time";
			IPOPTParamValues[4][1]="1000000";
			IPOPTParamValues[5][0]="derivative_test";
			IPOPTParamValues[5][1]="second-order";
			
			//op.setInputParameter("IPOPTParamValuesOpt", IPOPTParamValues );
			//jom.setInputParameter("PCRP1Opt", new DoubleMatrixND(joPCRP1Eff) );
			
			return IPOPTParamValues;
		}
		
		
		public void solveObjectiveFunction(OptimizationProblem op, boolean traceOn ) {
			
			//--IPOPT options--//
			String solverfullfile = "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll";
			//String solverLibraryName=solverfullfile;
			
			String [][]IPOPTParamValues = new String[6][6];
			IPOPTParamValues[0][0]="solverLibraryName";
			IPOPTParamValues[0][1]="C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll";
			IPOPTParamValues[1][0]="print_level";
			IPOPTParamValues[1][1]="0";			
			IPOPTParamValues[2][0]="print_options_documentation";
			IPOPTParamValues[2][1]="No";
			IPOPTParamValues[3][0]="max_iter";
			IPOPTParamValues[3][1]="80000";			
			IPOPTParamValues[4][0]="max_cpu_time";
			IPOPTParamValues[4][1]="6000000";
			IPOPTParamValues[5][0]="derivative_test";
			IPOPTParamValues[5][1]="first-order";
			
			//-- Solve the function with debugger info --//
			if (traceOn) {
				op.solve("ipopt" ,  IPOPTParamValues[0][0], IPOPTParamValues[0][1],	IPOPTParamValues[1][0], IPOPTParamValues[1][1],IPOPTParamValues[2][0], IPOPTParamValues[2][1],	
					IPOPTParamValues[3][0], IPOPTParamValues[3][1],IPOPTParamValues[4][0], IPOPTParamValues[4][1], IPOPTParamValues[5][0], IPOPTParamValues[5][1]	);//-- works					
			} else {
			
				//-- Silently solve the function --//
				//op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll");  // -- works --Ipopt38x64//
				
				//op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//libipoptfort.dll");  // -- test -- does not work//
				
				op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//libipopt-1.dll");  // -- test -- does not work//
				
			}
			
			
			//-- the set may not work --//
			String[] IPOPTParamsSet= new String[IPOPTParamValues.length];
			String[] IPOPTValuesSet= new String[IPOPTParamValues.length];
			for (int i=0; i<IPOPTParamValues.length; i++) {
				IPOPTParamsSet[i]=IPOPTParamValues[i][0];
				IPOPTValuesSet[i]=IPOPTParamValues[i][1];
			}
			//op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll", "IPOPTParamsSet", "IPOPTValuesSet");
			//op.solve("ipopt" ,  "IPOPTParamsSet", "IPOPTValuesSet");
			
			
			//if (!optProb.solutionIsOptimal ()) throw new RuntimeException ("An optimal solution was not found");			
			if (!op.solutionIsOptimal ()) System.out.println("??? BAD --- An optimal solution was not found! ???  " + op.solutionIsOptimal() );				
			else System.out.println ("*** Good -  Solver found the optimal solution! - " + op.solutionIsOptimal()   );
									
			if (op.solutionIsFeasible ()) System.out.println("solutionIsFeasible True !");
			else System.out.println("solutionIsFeasible False !");
						
			//--to check if the solver proved that the problem has no feasible solutions--
			if (op.feasibleSolutionDoesNotExist()) System.out.println("feasibleSolutionDoesNotExist - No Solution : Bad !");
			else System.out.println("feasibleSolutionDoesNotExist - Solution exists : Good !");
			
						
		} //--solveObjectiveFunction--//
		
		
		
		public void printOptimalResult(OptimizationProblem op) {
						
			System.out.println ();
			System.out.println("*** System Optimal Value; Internal Optimal Value of Obj-Function =  "+ op.getOptimalCost ());
			System.out.println ("** Optimized Decision Variables = " +   op.getPrimalSolution("decisionVars").toString() );
			System.out.println ();
			
			int cnt=1;
			DoubleMatrixND sol = null;
			if (cnt > 0) {
					DoubleMatrixND solutionND =  op.getPrimalSolution("decisionVars");				
					double [][]solution =  (double [][]) solutionND.toArray();
					sol = new  DoubleMatrixND(solution);								
					//System.out.println ("Optimized Sol = " +  solutionND.toString() );
					
					//-- keep the solution data persistant --//
					System.out.println ("Optimal Solution Dimension  #size:row=" + solution.length + " # size:col= " + solution[0].length );					
			}
			
			System.out.println ("** JOM Time Report  ** : " + op.timeReport() );
			
			//-- Solver Checker --//-- Error - ava.lang.UnsupportedClassVersionError: com/jom/SolverTester : Unsupported major.minor version 52.0//			
			//SolverTester ipoptchk = new SolverTester() ;			
			//System.out.println (" *** Solver Check : " +  ipoptchk.check_ipopt(solverfullfile) );  			
			
			System.out.println (" *** Solver getNumNonLinearScalarConstraints() : " +  op.getNumNonLinearScalarConstraints() );
			System.out.println (" *** Solver isLinearProblem() : " +  op.isLinearProblem() );
			System.out.println (" *** Solver Version-about() : " +  op.about() );
			
			
		} //-- printOptimalResult--//
		
		/////////////////////////////////////////////////// 
		public void objfunc_displayJOMVarStruct(OptimizationProblem jom, String jomvar, double[][] arr2D,  int option) {
			
			//option 1 to dispaly data, option 2 to dislay data-arch, option 3 to dispály data of native expression like ones, eye, diag etc
			//System.out.println (" joSwapFxRiskEff: " + optimStaticType.arr2DArchSpec(joSwapFxRiskEff,true) );
			if (arr2D != null)  {
				System.out.println (" arr struct: " + optimStaticType.arr2DArchSpec(arr2D,true) );
			}
			
			
			if (arr2D == null) {			
				int ndim = (int) jom.parseExpression(jomvar).getNumDim();
				
				if (option<=1) {
					
					if (ndim==2) System.out.println ( jomvar + optimStaticType.arr2DArchSpec((double [][])jom.getInputParameter(jomvar).toArray(), true) );
					
					if (ndim==3) {
						double[][][] jvarr= (double[][][]) jom.getInputParameter(jomvar).toArray();
						System.out.println( jomvar + " data struct: Tab=" + jvarr.length + " / Row=" + jvarr[0].length + " / Col=" + jvarr[0][0].length);
					}
				}
				
				if (option==2) {
					
					if (ndim==2) {
						System.out.println ( jomvar + " data struct: RowSize = " + jom.parseExpression(jomvar+"(all,0)").evaluateConstant().getNumElements() + 
									" /ColSize:"+ jom.parseExpression(jomvar+"(0,all)").evaluateConstant().getNumElements() );
					}
					if (ndim==3) {
						System.out.println ( jomvar + " data struct: TabSize = " + jom.parseExpression(jomvar+"(all,0,0)").evaluateConstant().getNumElements() + 
									" /RowSize:"+ jom.parseExpression(jomvar+"(0,all,0)").evaluateConstant().getNumElements() + 
									" /ColSize:"+ jom.parseExpression(jomvar+"(0,0,all)").evaluateConstant().getNumElements() );
					}
					
				}				
				if (option==3) { //Evaluate Native Expression  
					System.out.println ( jom.parseExpression(jomvar).evaluateConstant() );
				}
				
			} // --jomvar -- arr2D null --//
			
		} //-- dispay jom dataStrcut --//

		
		
		public void ojbfunc_optimize_dispDVExpr(OptimizationProblem jom, String expr, String label) {
			if (label == null) label="DV Expr";
			//if (!jom.parseExpression(expr).isScalar()) { // -- crashes some times
			
			//int[] sizev = (int[]) jom.parseExpression(expr).evaluate().getNumDim();
			//int ndim = this.jom.parseExpression(expr).getNumDim();
			//int numDim = jom.parseExpression(expr).evaluate().getNumDim();

			//cern.colt.matrix.tint.IntMatrix1D im1d =  jom.parseExpression(expr).evaluate().getSize();
			//int[] sizev = (int[]) im1d.toArray();
			//int nDim = sizev.length;
			
			int ndim = jom.parseExpression(expr).getNumDim();
			if (ndim > 1) {
				System.out.println(label + ": dispDVExpr:  NumDim of expression: " + ndim);
				int[] sizev = (int[]) jom.parseExpression(expr).size();
				if (ndim == 3) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Tab=" + sizev[0] +  " /Row=" + sizev[1] +  " /Col=" + sizev[2] + " ] ");
				if (ndim == 2) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Row=" + sizev[0] +  " /Col=" + sizev[1] + " ] ");
			} else System.out.println(label + ": dispDVExpr: scalar quantity !");
			
		}
		
		public void ojbfunc_optimize_dispDVExpr_DV(OptimizationProblem jom, String expr, String label) {
			if (label == null) label="Vars-DV Expr";
			
			int ndim = jom.parseExpression(expr).evaluate().getNumDim();
			if (ndim > 1) {
				System.out.println(label + ": dispDVExpr:  NumDim of expression: " + ndim);
				int[] sizev = (int[]) jom.parseExpression(expr).size();
				if (ndim == 3) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Tab=" + sizev[0] +  " /Row=" + sizev[1] +  " /Col=" + sizev[2] + " ] ");
				if (ndim == 2) System.out.println(label + ": dispDVExpr: Size of expression: Vlegth=" + sizev.length + " [ / Row=" + sizev[0] +  " /Col=" + sizev[1] + " ] ");
			} else System.out.println(label + ": dispDVExpr: scalar quantity !");
			
		}		
		
		/////////////////////////////////////////////////// 
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
			
			//--java.cern.colt.matrix.impl.AbstractMatrix2D --   
			//viewSelection(int[] rowIndexes, int[] columnIndexes)
            //Constructs and returns a new selection view that is a matrix holding the indicated cells.
		*/
		
		 
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
		////////////////////////////////////////////////////////////
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    