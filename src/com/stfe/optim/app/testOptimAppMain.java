
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

import com.stfe.optim.config.*;
import com.stfe.optim.excel.*;
import com.stfe.optim.util.optimDeserializeBean;
import com.stfe.optim.util.optimRestoreIOBean;
////////////////////////////////////////////////////////////
import com.stfe.optim.util.optimSerializeBean;
import com.stfe.optim.util.optimValidateFile;  
import com.stfe.optim.config.initOptimAppConfig;
import com.stfe.optim.util.*;
import com.stfe.optim.excel.*;


    public class testOptimAppMain {
			
    	static Logger LOG = Logger.getLogger(optimAppMain.class.getName());
		    	
    	
    	public void initIBeanFromIXL_RawValues_BM_Swaps_Plan_OLD()  {
    		
			LOG.info(" 0- Persisting the rawValuesArchSpecs."  );			
			com.stfe.optim.util.optimSerializeBean serRVArchSpecs= new optimSerializeBean(initOptimAppConfig.infiledir);
			
			
			//1-Generate each Bean for IXL-data as per Arch-Specs and persist them//
			//Meta-spec Bean is added with IBean-Meta-BeanArchSpecs//
			LOG.info(" 1- Generating/persisting all the IBeans for Spec+IXL-data as per Arch-Specs."  );
			LOG.info(" FullFilename of IXL to generate IBean : " + initOptimAppConfig.infiledir +"\\"+ initOptimAppConfig.infile_rawvalues_IXL_name  );
			genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);
			
			
			//1-BM_SWAPS_PLAN//			 
			String[][] BM_SWAPS_PLAN=defOptimAppXLSpecs.getArchSpecs_rawValues_BM_SWAPS_PLAN();			
			ibean.genBeanTypeFromSpecs(BM_SWAPS_PLAN,"RV_BM_SWAPS_PLAN","BM_SWAPS_PLAN", null);
			
			//2-BM_SWAPS_PLAN_CURRENTQUARTER//			 
			String[][] BM_SWAPS_PLAN_CURRENTQUARTER=defOptimAppXLSpecs.getArchSpecs_rawValues_BM_SWAPS_PLAN_CURRENTQUARTER();			
			ibean.genBeanTypeFromSpecs(BM_SWAPS_PLAN_CURRENTQUARTER,"RV_BM_SWAPS_PLAN_CURRENTQUARTER","BM_SWAPS_PLAN_CURRENTQUARTER", null);
			
			//3-REF//			 
			String[][] REF =defOptimAppXLSpecs.getArchSpecs_rawValues_REF();			
			ibean.genBeanTypeFromSpecs(REF,"RV_REF","REF", null);
			
			//4-BM//			 
			String[][] BM =defOptimAppXLSpecs.getArchSpecs_rawValues_BM();			
			ibean.genBeanTypeFromSpecs(BM,"RV_BM","BM", null);
			
			//5-BM_PLAN//			 
			String[][] BM_PLAN =defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLAN();			
			ibean.genBeanTypeFromSpecs(BM_PLAN,"RV_BM_PLAN","BM_PLAN", null);
			
			//6-BM_PLAN_CURRENTQUARTER//			 
			String[][] BM_PLAN_CURRENTQUARTER =defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER();			
			ibean.genBeanTypeFromSpecs(BM_PLAN_CURRENTQUARTER,"RV_BM_PLAN_CURRENTQUARTER","BM_PLAN_CURRENTQUARTER", null);
			
			
			//7-BM_WP//			 
			String[][] BM_WP =defOptimAppXLSpecs.getArchSpecs_rawValues_BM_WP();			
			ibean.genBeanTypeFromSpecs(BM_WP,"RV_BM_WP","BM_WP", null);
			
			//8-PMMOD_CURRENTQUARTER//			 
			String[][] PMMOD_CURRENTQUARTER =defOptimAppXLSpecs.getArchSpecs_rawValues_PMMOD_CURRENTQUARTER();			
			ibean.genBeanTypeFromSpecs(PMMOD_CURRENTQUARTER,"RV_PMMOD_CURRENTQUARTER","PMMOD_CURRENTQUARTER", null);
			
    	}
    	

    	
    	public void genOXLFromIBean_RawValues_BM_Swaps_Plan_OLD()  {    		
    		String deffile=initOptimAppConfig.outfile_rawvalues_IXL_name;			
			String filepath=initOptimAppConfig.outfiledir;			
			filepath = optimValidateFile.validatePath(filepath);			
			String filename=filepath+deffile;	
			
			createOXLFromIBean oxl = new createOXLFromIBean();
			oxl.initOXLSheetForIBean(filepath,  deffile,  "BM_SWAPS_PLAN", false);
			
			//0--Sample--
			//oxl.initOXLSheetForIBean(filepath,  deffile,  "BM_SWAPS_PLAN", false); //forceinit=false//
			//String[][] rawValuesArchSpecs=defOptimAppXLSpecs.getRawValuesArchSpecs();
			//oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_SWAPS_PLAN","RV_BM_SWAPS_PLAN");
						
			
			//1-BM_SWAPS_PLAN//
			oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_SWAPS_PLAN","RV_BM_SWAPS_PLAN");
			
			//2-BM_SWAPS_PLAN_CURRENTQUARTER//			
			oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_SWAPS_PLAN_CURRENTQUARTER","RV_BM_SWAPS_PLAN_CURRENTQUARTER");
			
			//3-REF//			 
			oxl.genOXLDataFromIBean(filepath,  deffile,  "REF","RV_REF");		
			
			
			//4-BM//			 
			oxl.genOXLDataFromIBean(filepath,  deffile,  "BM","RV_BM");
			
			
			//5-BM_PLAN//			 
			oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_PLAN","RV_BM_PLAN");
			
			
			//6-BM_PLAN_CURRENTQUARTER//			 
			oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_PLAN_CURRENTQUARTER","RV_BM_PLAN_CURRENTQUARTER");
			
			
			//7-BM_WP//			 
			oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_WP","RV_BM_WP");	
			
			
			//8-PMMOD_CURRENTQUARTER//			 
			oxl.genOXLDataFromIBean(filepath,  deffile,  "PMMOD_CURRENTQUARTER","RV_PMMOD_CURRENTQUARTER");	
					
    	}

    	
    	public void OLD_appOtimInitIBeanAndIXL_RawValues_BM_Swaps_Plan()  {		
    		initIBeanFromIXL_RawValues_BM_Swaps_Plan_OLD();
    		//genOXLFromIBean_RawValues_BM_Swaps_Plan_OLD();    			
    	}
    	//-- V1-App-- Over!//
    	////////////////////////////////////////////////////////////////////
    	
    	
    	
    	////////////////////////////////////////////////////////////////////
    	//-- V2-App--//
    
    	// -- BM_PLANCURRENT --//
    	public void initIBeanFromIXL_RawValues_BM_PlanCurrent()  {
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);
    		
    		//-- BM_PLAN_CURRENTQUARTER is included in BM_PLANCURRENT --//    		
    		//String[][] BM_PlanCurrent = defOptimAppXLSpecs.getRawValuesArchSpecs();			
    		String[][] BM_PlanCurrent = defOptimAppXLSpecs.getArchSpecs_RV_BM_PLANCURRENT();
			ibean.genBeanTypeFromSpecs(BM_PlanCurrent,"rv_BM_PLANCURRENT","BM_PlanCurrent", null);
    	}
    	
    	
    	
    	//--old RawValues : Plan_CurrentQuarter ---//
    	/*
    	public void initIBeanFromIXL_BM_Swaps_Plan_CurrentQuarter()  {
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);
    		
    		String[][] BM_SWAPS_PLAN_CURRENTQUARTER =defOptimAppXLSpecs.getArchSpecs_rawValues_BM_SWAPS_PLAN_CURRENTQUARTER();			
			ibean.genBeanTypeFromSpecs(BM_SWAPS_PLAN_CURRENTQUARTER,"rv_BM_SWAPS_PLAN_CURRENTQUARTER","BM_SWAPS_PLAN_CURRENTQUARTER", null);
    	}
    	
    	public void initIBeanFromIXL_BM_Plan_CurrentQuarter_F()  {
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);
    		
    		String[][] BM_PLAN_CURRENTQUARTER_F =defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER_F();			
			ibean.genBeanTypeFromSpecs(BM_PLAN_CURRENTQUARTER_F,"rv_BM_PLAN_CURRENTQUARTER_F","BM_PLAN_CURRENTQUARTER", null);
    	}
    	    	
    	    	
    	public void initIBeanFromIXL_BM_Plan_CurrentQuarter_DC_Schuld(){
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);    		
    		String[][] Schuld_BM_SWAPS_PLAN_CURRENTQUARTER_DC = defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER_DC_Schuld();			
			ibean.genBeanTypeFromSpecs(Schuld_BM_SWAPS_PLAN_CURRENTQUARTER_DC,"rvSchuld_BM_PLAN_CURRENTQUARTER_DebtClean","BM_PLAN_CURRENTQUARTER", null);			
    	}
    	*/
    	
    	
    	
    	
    	
    	public void initIBeanFromIXL_MarketAndParameters()  {
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);
    		
    		String MarketParameters_file = "ParametersAndMarketValuesIXL.xlsx";
    		
    		String[][] Parameters = defOptimAppXLSpecs.getArchSpecs_Parameters();			
			ibean.genBeanTypeFromSpecs(Parameters,"p_MarketAndParameters","Parameters", MarketParameters_file);
						
			String[][] MarketValues = defOptimAppXLSpecs.getArchSpecs_MarketValues();			
			ibean.genBeanTypeFromSpecs(MarketValues,"mv_MarketAndParameters","MarketValues", MarketParameters_file);			
    	}
    	
    	
    	
    	
    	public void initIBeanFromIXL_BaukastenSwapsRawValues()  {
    		String Baukasten_SwapsRawValues_file = "BAUKASTEN_RawValuesIXL.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_file);
    		
    		String[][] BauKasten_SWAPS_RawValues = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues,"bean_BAUKASTEN_RawValues","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_file);
			System.gc();
    	}
    	
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part1()  {
    		String Baukasten_SwapsRawValues_part1_file = "BAUKASTEN_RawValues_F_IXL_Part1.xlsx";
    		    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part1_file);
    		
    		String[][] BauKasten_SWAPS_RawValues_part1 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part1();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part1,"bean_BAUKASTEN_RawValues_Part1","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part1_file);
			
			
			System.gc();
    	}
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part2()  {
    		String Baukasten_SwapsRawValues_part2_file = "BAUKASTEN_RawValues_F_IXL_Part2.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part2_file);
    		
    		String[][] BauKasten_SWAPS_RawValues_part2 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part2();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part2,"bean_BAUKASTEN_RawValues_Part2","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part2_file);
			
			System.gc();
    	}
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part3()  {
    		String Baukasten_SwapsRawValues_part3_file = "BAUKASTEN_RawValues_F_IXL_Part3.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part3_file);
    		
    		String[][] BauKasten_SWAPS_RawValues_part3 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part3();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part3,"bean_BAUKASTEN_RawValues_Part3","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part3_file);
			
			System.gc();
    	}
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part4()  {
    		//String Baukasten_SwapsRawValues_part4_file = "BAUKASTEN_RawValuesIXL_Part4.xlsx";
    		String Baukasten_SwapsRawValues_part4_file = "BAUKASTEN_RawValues_F_IXL_Part4.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part4_file);
    		
    		String[][] BauKasten_SWAPS_RawValues_part4 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part4();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part4,"bean_BAUKASTEN_RawValues_Part4","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part4_file);
			
			System.gc();
    	}
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part5()  {
    		String Baukasten_SwapsRawValues_part5_file = "BAUKASTEN_RawValues_F_IXL_Part5.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part5_file);    		
    		String[][] BauKasten_SWAPS_RawValues_part5 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part5();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part5,"bean_BAUKASTEN_RawValues_Part5","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part5_file);
						
			System.gc();
    	}
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part6()  {
    		String Baukasten_SwapsRawValues_part6_file = "BAUKASTEN_RawValues_F_IXL_Part6.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part6_file);    		
    		String[][] BauKasten_SWAPS_RawValues_part6 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part6();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part6,"bean_BAUKASTEN_RawValues_Part6","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part6_file);
						
			System.gc();
    	}
    	/*
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part7()  {
    		String Baukasten_SwapsRawValues_part7_file = "BAUKASTEN_RawValuesIXL_Part7.xlsx";    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part7_file);    		
    		String[][] BauKasten_SWAPS_RawValues_part7 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part7();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part7,"bean_BAUKASTEN_RawValues_Part7","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part7_file);						
    	}
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part8()  {
    		String Baukasten_SwapsRawValues_part8_file = "BAUKASTEN_RawValuesIXL_Part8.xlsx";    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part8_file);    		
    		String[][] BauKasten_SWAPS_RawValues_part8 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part8();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part8,"bean_BAUKASTEN_RawValues_Part8","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part8_file);						
    	}
    	public void initIBeanFromIXL_BaukastenSwapsRawValues_Part9()  {
    		String Baukasten_SwapsRawValues_part9_file = "BAUKASTEN_RawValuesIXL_Part9.xlsx";    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part9_file);    		
    		String[][] BauKasten_SWAPS_RawValues_part9 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part9();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part9,"bean_BAUKASTEN_RawValues_Part9","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part9_file);						
    	}
    	*/
    	
    	
    	
    	public void initIBeanFromIXL_DKFOptiMarktparameterX()  {
    		
    		String DKF_opti_file = "DKF_Port_v1.25IXL.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, DKF_opti_file);
    		
    		String[][] Marktparameter1 = defOptimAppXLSpecs.getArchSpecs_Marktparameter1();			
			ibean.genBeanTypeFromSpecs(Marktparameter1,"mp_1_Marktparameter1","Marktparameter1", DKF_opti_file);
			
			String[][] Marktparameter2 = defOptimAppXLSpecs.getArchSpecs_Marktparameter2();			
			ibean.genBeanTypeFromSpecs(Marktparameter2,"mp_2_Marktparameter2","Marktparameter2", DKF_opti_file);
			
			String[][] Marktparameter3 = defOptimAppXLSpecs.getArchSpecs_Marktparameter3();			
			ibean.genBeanTypeFromSpecs(Marktparameter3,"mp_3_Marktparameter3","Marktparameter3", DKF_opti_file);
			
			String[][] Marktparameter4 = defOptimAppXLSpecs.getArchSpecs_Marktparameter4();
			ibean.genBeanTypeFromSpecs(Marktparameter4,"mp_4_Marktparameter4","Marktparameter4", DKF_opti_file);
			
			String[][] Marktparameter5 = defOptimAppXLSpecs.getArchSpecs_Marktparameter5();			
			ibean.genBeanTypeFromSpecs(Marktparameter5,"mp_5_Marktparameter5","Marktparameter5", DKF_opti_file);
    	}
    	
    	/*
    	public void OLD_XXX_initIBeanFromIXL_MarktparameterZApX()  {    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, "DynamicKeyFigures_Optimization_v0.993branchIXL.xlsx");
    		
    		String[][] MarktparameterZAp1 = defOptimAppXLSpecs.getArchSpecs_MarktparameterZAp1();			
			ibean.genBeanTypeFromSpecs(MarktparameterZAp1,"mpZap_1_MarktparameterZAp1","MarktparameterZAp1", "DynamicKeyFigures_Optimization_v0.993branchIXL.xlsx");
			
			String[][] MarktparameterZAp2 = defOptimAppXLSpecs.getArchSpecs_MarktparameterZAp2();			
			ibean.genBeanTypeFromSpecs(MarktparameterZAp2,"mpZap_2_MarktparameterZAp2","MarktparameterZAp2", "DynamicKeyFigures_Optimization_v0.993branchIXL.xlsx");
			
			String[][] MarktparameterZAp3 = defOptimAppXLSpecs.getArchSpecs_MarktparameterZAp3();			
			ibean.genBeanTypeFromSpecs(MarktparameterZAp3,"mpZap_3_MarktparameterZAp3","MarktparameterZAp3", "DynamicKeyFigures_Optimization_v0.993branchIXL.xlsx");
						
    	}
    	*/
    	
    	
    	//-- V2-App--//
    	public void initIBeanFromIXL_Solve()  {    		 
    		   
    		
    		// -- Baukasten --//
    		//-- All -- initIBeanFromIXL_BaukastenSwapsRawValues();
    		/*
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part1();    		
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part2();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part3();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part4();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part5();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part6();
    		*/
    		
    		// -- RawValues --//
    		initIBeanFromIXL_RawValues_BM_PlanCurrent();
    		
    		
    		// -- Market Parameter --//
    		//initIBeanFromIXL_MarketAndParameters();
    		
    		// -- Market-Parameter1-5-Z --//
    		//initIBeanFromIXL_DKFOptiMarktparameterX();
    		
    		
 		
    		///// OLD ///
    		//initIBeanFromIXL_BM_Plan_CurrentQuarter_F();
    		//--changed to BM_Plan_CurrentQuarter--// initIBeanFromIXL_BM_Plan_CurrentQuarter_DC_Schuld();
    		//initIBeanFromIXL_BM_Plan_CurrentQuarter_DC_Schuld();
    		//initIBeanFromIXL_MarktparameterZApX();
    		
    		
    		
    	}
    	////////////////////////////////
    	
    	
    	////////////////////////////////    	
    	public void genOXLFromIBean_Solve()  {
    		String deffile=initOptimAppConfig.outfile_rawvalues_IXL_name;			
			String filepath=initOptimAppConfig.outfiledir;			
			filepath = optimValidateFile.validatePath(filepath);			
			String filename=filepath+deffile;	
			
			createOXLFromIBean oxl = new createOXLFromIBean();
			
			//0-Schuld: BM_SWAPS_PLAN_CURRENTQUARTER_DC_Schuld//
			oxl.initOXLSheetForIBean(filepath,  deffile,  "BM_PLAN_CURRENTQUARTER", false);
			
			//oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_PLAN_CURRENTQUARTER","rvSchuld_BM_PLAN_CURRENTQUARTER_DebtClean");		
			//oxl.genTxtDataFromIBean(filepath,  "OXL_RawValues_BM_PLAN_CURRENTQUARTER_Schuld_IXL.xlsx",  "DebtClean_Schuld","rvSchuld_BM_PLAN_CURRENTQUARTER_DebtClean");
			
			
			
			/*
			 
			//1-BM_SWAPS_PLAN//
			oxl.initOXLSheetForIBean(filepath,  deffile,  "BM_SWAPS_PLAN_CURRENTQUARTER", false);
			oxl.initOXLSheetForIBean(filepath,  deffile,  "BM_SWAPS_PLAN_CURRENTQUARTER", false);
			oxl.genOXLDataFromIBean(filepath,  deffile,  "BM_SWAPS_PLAN_CURRENTQUARTER","rv_BM_SWAPS_PLAN_CURRENTQUARTER");
		
			
				
			//2-MarketAndParameters//			
			oxl.initOXLSheetForIBean(filepath,  "OXL_ParametersAndMarketValuesIXL.xlsx",  "MarketValues", false);
			oxl.genOXLDataFromIBean(filepath,  "OXL_ParametersAndMarketValuesIXL.xlsx",  "Parameters","p_MarketAndParameters");
			oxl.genOXLDataFromIBean(filepath,  "OXL_ParametersAndMarketValuesIXL.xlsx",  "MarketValues","mv_MarketAndParameters");						
		
			//--test--//
			//oxl.genTxtDataFromIBean(filepath,  "OXL_ParametersAndMarketValuesIXL.xlsx",  "MarketValues","mv_MarketAndParameters");
			
			
			//3-SWAPS_RawValues//		
			oxl.initOXLSheetForIBean(filepath,  "OXL_SWAPS_RawValuesIXL.xlsx",  "SWAPS_RawValues", false);
			//RESOURCE-REquired // -oxl.genOXLDataFromIBean(filepath,  "OXL_SWAPS_RawValuesIXL.xlsx",  "SWAPS_RawValues","srv_SWAPS_RawValues");
			
			oxl.genTxtDataFromIBean(filepath,  "OXL_SWAPS_RawValuesIXL.xlsx",  "SWAPS_RawValues","srv_SWAPS_RawValues");
			
			*/
			
			
    	}    	
    	
    	////////////////////////////////////////////////////////////////////////////////////////////
    	////////////////////////////////////////////////////////////////////////////////////////////
    	/// TEST USE CASE
    	//--- Pack 1 - Init Bean - OXL - Validation -->//
    	
    	private static final String[][] bauKastenRawValuesArchSpecs_Part1 = {    	// till 31.12.2067 row=416977
    		{"test_p1_srv_MetaColVals_Bk_SWAPS_RawValues_Part1","A","1","CD","1", "STRING"}, //Starting from 31.01.2017
    		{"test_p1_srv_DataPort_Bk_SWAPS_RawValues_Part1","B","2","B","75000", "STRING"},  
    		{"test_p1_srv_Date_Bk_SWAPS_RawValues_Part1","A","2","A","75000", "DATE"}, //DATE
    		{"test_p1_srv_Data_Bk_SWAPS_RawValues_Part1","C","2","CC","75000", "NUM"}  
    		//{"srv_Data_Bk_SWAPS_RawValues_Part1","C","2","C","50000", "NUM"}
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part1() {							
    		return bauKastenRawValuesArchSpecs_Part1;
    	}
    	public void test_InitIBeanFromIXL_BaukastenSwapsRawValues_Part1()  {
    		String Baukasten_SwapsRawValues_part1_file = "BAUKASTEN_RawValues_F_IXL_Part1.xlsx";
    		    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part1_file);
    		
    		String[][] BauKasten_SWAPS_RawValues_part1 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part1();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part1,"bean_BAUKASTEN_RawValues_Part1","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part1_file);
    	}
    	private static final String[][] bauKastenRawValuesArchSpecs_Part2 = { //Total rows=416977    		
    		{"test_p2_srv_DataPort_Bk_SWAPS_RawValues_Part2","B","1","B","75000", "STRING"},  
    		{"test_p2_srv_Date_Bk_SWAPS_RawValues_Part2","A","1","A","75000", "DATE"},
    		{"test_p2_srv_Data_Bk_SWAPS_RawValues_Part2","C","1","CC","75000", "NUM"}  
    	};
    	public void test_InitIBeanFromIXL_BaukastenSwapsRawValues_Part2()  {
    		String Baukasten_SwapsRawValues_part2_file = "BAUKASTEN_RawValues_F_IXL_Part2.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, Baukasten_SwapsRawValues_part2_file);
    		
    		String[][] BauKasten_SWAPS_RawValues_part2 = defOptimAppXLSpecs.getBauKastenRawValuesArchSpecs_Part2();			
			ibean.genBeanTypeFromSpecs(BauKasten_SWAPS_RawValues_part2,"bean_BAUKASTEN_RawValues_Part2","BAUKASTEN_RawValues", Baukasten_SwapsRawValues_part2_file);
		}
    	private optimDeserializeBean restore;
    	private optimRestoreIOBean restoreIOBean;
    	private String filepath;
    	
    	public void testOXLIBeanFromIXL_BaukastenSwapsRawValues_Part1() {
    		
    		//configure global env //
    		String filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			
    		
    		optimDeserializeBean restore = new optimDeserializeBean(filepath);
			this.restore=restore;
			optimRestoreIOBean restoreIOBean = new optimRestoreIOBean();
			this.restoreIOBean=restoreIOBean;
			
    	    		createOXLFromIBean oxl = new createOXLFromIBean();
    	    		
    	    		//--Add up the 5 slices of Srv  --//
    	    		
    	    		// -- Swaps_RawValues -- //
    	    		//-- Swaps_RawValues Date--//
    	    		String [][]swapsRVDateP1 = (String[][]) this.restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part1",filepath);
    	    		//-- Swaps_RawValues element --//
    	    		String [][]swapsRVEleP1 = (String[][]) this.restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part1",filepath);					
    	    		//-- Swaps_RawValues Data --//
    	    		double [][]swapsRVDataP1 = this.restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part1","srv_Data_Bk_SWAPS_RawValues_Part1");			
    	    		
    	    		//System.out.println(" Swaps_RawValues P1: size_data: " +  swapsRVDataP1.length + "; size_date: " +  swapsRVDateP1.length + "; size_ele: " +  swapsRVEleP1.length  );
    	    					 
    	    		
    	    		//--sample//
    	    		//works  -- oxl.genTxtFileFrom2DStringData(filepath, "Baukasten_SRV_Date_Raw_All.txt", this.swapsRVDateRaw);
    	    		// oxl.genTxtFileFrom2DData(filepath, "TEST-P1-Baukasten_SRV_Data_P1.txt", swapsRVDataP1);
    	    		 //oxl.genTxtFileFrom2DStringData(filepath, "TEST-P1-Baukasten_SRV_Date_P1.txt", swapsRVDateP1);    	    		 
    	    		     	    		
    	    		 //BEAN=IBean-Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part1-XL// - Element=Bean-srv_Date_Bk_SWAPS_RawValues_Part1
    	    		 //oxl.genTxtDataFromIBean(filepath,  "TxtOXL_P1_Baukasten_SRV_Data_P1.txt",  "Baukasten_RawValues","bean_BAUKASTEN_RawValues_Part1");
    	    		
    	    		 oxl.genTxtFileFrom2DDateAsString(filepath, "TEST-P1-Baukasten_SRV_Date_P1.txt", swapsRVDateP1);   
    	    		 
    	    		  		 
    	 }
    	
    	public void testOXLIBeanFromIXL_BaukastenSwapsRawValues_Part2() {  
    		
    		String filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			
    		optimDeserializeBean restore = new optimDeserializeBean(filepath);
			this.restore=restore;
			optimRestoreIOBean restoreIOBean = new optimRestoreIOBean();
			this.restoreIOBean=restoreIOBean;
			
			
			createOXLFromIBean oxl = new createOXLFromIBean();
    		
			//-- Swaps_RawValues P2 --//
     		String [][]swapsRVDateP2 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part2",filepath);			
     		String [][]swapsRVEleP2 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part2",filepath);								
     		double [][]swapsRVDataP2 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part2","srv_Data_Bk_SWAPS_RawValues_Part2");			
     		System.out.println(" Swaps_RawValues P2: size_data: " +  swapsRVDataP2.length + "; size_date: " +  swapsRVDateP2.length + "; size_ele: " +  swapsRVEleP2.length  );
     		
     		//works  -- oxl.genTxtFileFrom2DStringData(filepath, "Baukasten_SRV_Date_Raw_All.txt", this.swapsRVDateRaw);
     		//oxl.genTxtFileFrom2DData(filepath, "TEST-P2-Baukasten_SRV_Data_P2.txt", swapsRVDataP2);    		
     		//oxl.genTxtFileFrom2DStringData(filepath, "TEST-P2-Baukasten_SRV_Date_P2.txt", swapsRVDateP2);
     		//oxl.genTxtFileFrom2DStringData(filepath, "TEST-P2-Baukasten_SRV_Meta_P2.txt", swapsRVEleP2);
     		
     		//Bean=IBean-Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part1-XL -- Element=srv_Date_Bk_SWAPS_RawValues_Part2//     		
     		//oxl.genTxtDataFromIBean(filepath,  "TxtOXL_P2_Baukasten_SRV_Data_P2.txt",  "Baukasten_RawValues","bean_BAUKASTEN_RawValues_Part2");
     		 
     		oxl.genTxtFileFrom2DDateAsString(filepath, "TEST-P2-Baukasten_SRV_Date_P2.txt", swapsRVDateP2);   
   		 
     		
    	}
    	//--- PAck 1 - Init Bean - OXL - Validation -->//
    	//////////////////////////////////////////////////////////////////////////////////////////////
    	//////////////////////////////////////////////////////////////////////////////////////////////
    	
    	public void doTest()  {
    		
    		//-- generate-Init IBean --//
    		//test_InitIBeanFromIXL_BaukastenSwapsRawValues_Part1();
    		//test_InitIBeanFromIXL_BaukastenSwapsRawValues_Part2();
    		
    		//-- generate-OXL for  validation --//
    		testOXLIBeanFromIXL_BaukastenSwapsRawValues_Part1();
    		testOXLIBeanFromIXL_BaukastenSwapsRawValues_Part2();
    	}
    	
    	
    	public void appOtimSolveAndPublish()  {
    		
    		//Optmize for numYears
    		int numYears=16; 
    		boolean addDKF=true;
    		double addDKFForYears=0.50; //2
    		int solveTestValidate=1;
    		
    		optimSolveFunctApp optsol = new optimSolveFunctApp();
    		optsol.ojbfunc_optimize_rule(numYears, addDKF,addDKFForYears, solveTestValidate );	
    		
			//optsol.functUtil.publishResultBean();
			//optsol.functUtil.publishResultOXL();
		}
    	
    	
    	
    	
    	public void appOtimInitIBeanAndIXL_Solve()  {
    		//--ixl, bean, oxl -  done--
    		//initIBeanFromIXL_Solve();
    		//genOXLFromIBean_Solve();
    		
    		
    		//-- load beans into JOM//
    		//??? Implement later!
			
    		//-- optimize - solve --//
    		//appOtimSolveAndPublish();
    		
    		
    	}
    	////////////////////////////////
		
    	
    
		////////////////////////////////////////////////////////////
		//public static void main_poiGenExcelXSSF(String[] args) {
		public static void main(String[] args) {
			
			try {
				
				initOptimAppConfig.init();
			} catch (IOException ioex) {
				LOG.warn(" initOptimAppConfig.init() IOException !");
			}
			
			LOG.info(" optim App V2 starts..."  );
			
			testOptimAppMain appV3 = new testOptimAppMain();
			
			//--- Read IXL , Persist Beans, Generate OXL --//
			appV3.doTest();
			
			
			
			//-- old test--- Optimize --//
			//sensiApp.appOtimSolveAndPublish();
					
			LOG.info(" optimAppMain over!"  );
			
		} // void main //
		
	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////

	

    