
package com.stfe.optim.config;

 
import java.io.*;
import java.util.*; 

import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.InputStream; 
import java.util.Properties; 

/*
 * (c) Copyright BRDF. All rights reserved. 
 *
 * Created on 10.03.2017
 *
 * @author NS
 *
 */

 
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
    public class defSensiOptimXLSpecs {
    	
		//--Define the size of beans based on row-cols--//
		private static final String[][] optDRL = {
					{"AllSevPC-1-10","C","2","I","11"},
					{"Bestand","J","2","J","11"},
					{"OptimalSolution","C","13","I","13"}, // Optimal - Solution//
					{"SumBestand","J","13","J","13"},
					{"UpLimit","C","16","J","16"},
					{"LowLimit","C","17","I","17"},
					
					//-- Risk In Data --//
					{"PCRP4","L","2","L","11"},
					{"PCRPZAp1","O","2","O","11"},
					
					//-- SumProd In Data --//
					{"Program","S","2","S","11"},
					{"BestandProgram","T","2","T","11"},
					
					//--Risk- Calc Out Data --//
					{"PCRP4Program","M","2","M","11"},
					{"PCRP4BestdProgram","N","2","N","11"},
					{"PCRPZAp1Program","P","2","P","11"},
					{"PCRPZAp1BestdProgram","Q","2","Q","11"},
					
					{"SumPCRP4Program","M","13","M","13"},
					{"SumPCRP4BestdProgram","N","13","N","13"},
					{"SumPCRPZAp1Program","P","13","P","13"},
					{"SumPCRPZAp1BestdProgram","Q","13","Q","13"},
									
					//--Risk-Covar Calc Out Data --//
					{"RiskCVProg","S","13","S","13"},
					{"RiskCVBestdProg","T","13","T","13"},
					
					//--Limit-Covar Calc Out Data --//
					{"LimitPCRP4BestdProgram","N","16","N","16"},
					
					//--Limit-Covar Calc Out Data --//
					{"LimitCVProgram","S","16","S","16"},
					{"LimitCVBestdProgram","T","16","T","16"}
					
		};		
		
		private static final String[][] covarDRL = {
					//-- Covariance --//
					{"Covarianz","A","1","J","10"}
					
		};	
	
	
		//--get the Excel-specs --//		
		public static String[][] getBeanOptArchSpecs() {							
			return optDRL;
		} 
		
		//--get the Excel-specs --//				
		public static String[][] getBeanCovarArchSpecs() {						
			return covarDRL;
		} 
		////////////////////////////////////////////////////////////
		
	
	} // class over  //
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	

    