
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

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
/*--- XSSF includes ---*/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*--- XML beans includes ---*/  
import org.apache.xmlbeans.*;

import com.jom.DoubleMatrixND;
import com.jom.OptimizationProblem;
import com.stfe.optim.config.*;
//import com.stfe.optim.util.*;
import com.stfe.optim.excel.*;
import com.stfe.optim.test.testOptimComp;
////////////////////////////////////////////////////////////
import com.stfe.optim.util.optimRestoreIOBean;
import com.stfe.optim.util.optimSerializeBean;
  
  

    public class sensiProtoOptimApp {
			
    	static Logger LOG = Logger.getLogger(sensiProtoOptimApp.class.getName());
		
    	
		////////////////////////////////////////////////////////////
		public static void STFE_optimize()
		{
			optimRestoreIOBean restoreIOBean = new optimRestoreIOBean();
			
			OptimizationProblem op = new OptimizationProblem();
			
			op.addDecisionVariable("C13ToI13Var", false, new int[] {1,7} );
		
			double shuld=1.11E+12;
			op.setInputParameter("ShuldVal", new DoubleMatrixND (new double[][] { {shuld} }) );
			
			double [][] JCovarianz = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Covar","Covarianz");		
			op.setInputParameter("SwapConv", new DoubleMatrixND(JCovarianz)  );
			//System.out.println(" Test the data JCovarianz: " + JCovarianz[4][4]);		
			
			double [][] JAll7Segments10PCs = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","AllSevPC-1-10"); 
			//System.out.println(" Test the data : " + JAll7Segments10PCs[0][2]);
			op.setInputParameter("All7Segments10PCs", new DoubleMatrixND(JAll7Segments10PCs)  );
			
			double [][] JBestand = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","Bestand"); 
			op.setInputParameter("Bestand", new DoubleMatrixND(JBestand)  );
			System.out.println(" Test the data - JBestand.length : Bestand " + JBestand.length + " : " + JBestand[0][0] + " : " + JBestand[1][0] + " : " + JBestand[2][0] );
			
			op.setInputParameter("SummeBestand", "sum(Bestand,1)"  );
			System.out.println(" Test the data : SummeBestand : " + op.getInputParameter("SummeBestand")  +  " ... Done!");
			
			
			//PCRP4 and PCRPZAp1 for 10 rows PC1-10:		
			double [][] JPCRP4 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","PCRP4"); 
			op.setInputParameter("PCRP4", new DoubleMatrixND(JPCRP4)  );
			
			double [][] JPCRPZAp1 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-Opt","PCRPZAp1"); 
			op.setInputParameter("PCRPZAp1", new DoubleMatrixND(JPCRPZAp1)  );
			
			//op.setInputParameter("PCRP4",new DoubleMatrixND (new double[][] {{-9.09E-04}, {-2.92E-05}, {-4.74E-04}, {-1.31E-04}, {7.90E-05}, {1.23E-05}, {-2.26E-05}, {4.41E-06}, {-3.08E-07}, {-2.66E-06} } ) );
			//op.setInputParameter("PCRPZAp1",new DoubleMatrixND (new double[][]{{3.29E-03}, {-9.61E-04}, {-7.86E-05}, {-1.09E-05}, {-3.12E-04}, {1.13E-04}, {-1.32E-04}, {6.74E-05}, {-3.06E-05}, {8.46E-06} } ) );
			
			
			
			/* Add the constraints */  		
			//F13 <= F16 , G13 <= G16,  H13 <= H16, I13 <= I16//
			op.addConstraint("C13ToI13Var(0,0) <= 10.0");
	  		op.addConstraint("C13ToI13Var(0,1) <= 10.0");
	  		op.addConstraint("C13ToI13Var(0,2) <= 10.0");
			op.addConstraint("C13ToI13Var(0,3) <= 0.25");
	  		op.addConstraint("C13ToI13Var(0,4) <= 0.25");
	  		op.addConstraint("C13ToI13Var(0,5) <= 0.25");
	  		op.addConstraint("C13ToI13Var(0,6) <= 0.25");
	  		
	  		op.addConstraint("C13ToI13Var(0,0) >= -10.0");
	  		op.addConstraint("C13ToI13Var(0,1) >= -10.0");
	  		op.addConstraint("C13ToI13Var(0,2) >= -10.0");
			op.addConstraint("C13ToI13Var(0,3) >= -0.25");
	  		op.addConstraint("C13ToI13Var(0,4) >= -0.25");
	  		op.addConstraint("C13ToI13Var(0,5) >= -0.25");
	  		op.addConstraint("C13ToI13Var(0,6) >= -0.25");
	  		
	  		
	  		op.addConstraint("  sum(((PCRP4 .* ((All7Segments10PCs * C13ToI13Var') + Bestand) )/ShuldVal)* 10000)  <= -1.5"); 	//N -1.5;  	MTProgBstdRP4
	  		
			
	  		//-- Optimization --//
	  		op.setObjectiveFunction("minimize", "sum(((PCRPZAp1 .* ((All7Segments10PCs * C13ToI13Var')+Bestand) )/ShuldVal) * 10000)");
	  		  		
	  		op.solve("ipopt" ,  "solverLibraryName" , "C://devdrv//JOM//JOMPRJ//extlib//win32-x86-64//Ipopt38x64.dll");//-- works
	  		
	  		//op.solve("ipopt" ,  "solverLibraryName" , "Ipopt38");
	  		//op.solve("Ipopt" ,  "solverLibraryName" , "Ipopt39-x64.dll");
							
			if (!op.solutionIsOptimal ()) throw new RuntimeException ("An optimal solution was not found");
			System.out.println (" Solver done the job! - " + op.solutionIsOptimal() );
			
					
			
			// Check the arch of solution //
			DoubleMatrixND sol =  op.getPrimalSolution("C13ToI13Var");		
			op.setInputParameter("C13ToI13Val", sol );
			
			
			System.out.println ("Solution Dim = " + sol.getNumDim());
			System.out.println ("Solution NumEle = " + sol.getNumElements()); 
			System.out.println ("Solution String = " + sol.toString());
			double[] solarr = sol.to1DArray();
			System.out.println ("Solution solarr = " + solarr[0] +" : "+ solarr[1] +" : "+ solarr[2]);
			
			//System.out.println ("VARP(0,0) = " + op.parseExpression("VARP(0,0)").evaluate()); 
			System.out.println ("Optimized Sol = " +  op.parseExpression("C13ToI13Val").evaluate() );
			
		
			// -- serialize the result: OptimalSolution --//
			String filepath=initOptimAppConfig.outfiledir;						
			optimSerializeBean serialBean = new optimSerializeBean(filepath);			
			serialBean.serializeResultBeanAsPdouble( (double[][]) sol.toArray(), "OptimalSolution", null);
						
			
		} // function Over
		///////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////
		public void publishResultBean() {
			
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
		/////////////////////////////////////////////////
		
				
				
		////////////////////////////////////////////////////////////
		public void sensiInitIBeanAndIXL()  {
								
			genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs();
			
			//beanSpecName=Covar,Opt ;  sheetName=Kovarianz/Optimierung
	  		//ibean.genBeanTypeFromSpecs("Covar","Kovarianz");
	  		//ibean.genBeanTypeFromSpecs("Opt","Optimierung"); //-- Change the dim to Opt- set to Covar yet! --//	
	  		
	  		ibean.genBeanCovarFromSpecs();
			ibean.genBeanOptFromSpecs();
			
			//-- create OXL and persist IBeans --//
			createOXLFromIBean oxl = new createOXLFromIBean();
			oxl.InitOXLForBean();
			oxl.genOXLFromBeanCovar();
			oxl.genOXLFromBeanOpt();
			
						
		}
		
		////////////////////////////////////////////////////////////
		public void sensiSolveAndPublish()  {
			STFE_optimize();			
			publishResultBean();
			publishResultOXL();
		}
		
		
		
		
		////////////////////////////////////////////////////////////
		//public static void main_poiGenExcelXSSF(String[] args) {
		public static void main(String[] args) {
			
			try {
				
				initOptimAppConfig.init();
			} catch (IOException ioex) {
				LOG.warn(" initOptimAppConfig.init() IOException !");
			}
			
			LOG.info(" Sensi App starts..."  );
			
			sensiProtoOptimApp sensiApp = new sensiProtoOptimApp();
			sensiApp.sensiInitIBeanAndIXL();
			sensiApp.sensiSolveAndPublish();
			
			
			
			LOG.info(" Sensi App over!"  );
			
		} // void main //
		
	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////

	

    