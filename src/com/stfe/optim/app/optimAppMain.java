
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
////////////////////////////////////////////////////////////
import com.stfe.optim.util.optimSerializeBean;
import com.stfe.optim.util.optimValidateFile;  



    public class optimAppMain {
			
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
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
    	
    	
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	//-- V2-App--//
    
    	// -- BM_PLANCURRENT Bestand--//
    	public void initIBeanFromIXL_RawValues_BM_PlanCurrent()  {
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);
    		
    		//-- BM_PLAN_CURRENTQUARTER is included in BM_PLANCURRENT --//    		
    		//String[][] BM_PlanCurrent = defOptimAppXLSpecs.getRawValuesArchSpecs();			
    		String[][] BM_PlanCurrent = defOptimAppXLSpecs.getArchSpecs_RV_BM_PLANCURRENT();
			ibean.genBeanTypeFromSpecs(BM_PlanCurrent,"rv_BM_PLANCURRENT","BM_PlanCurrent", null);
			
			String[][] BM_PlanCurrent_DC = defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLANCURRENT_DC_Schuld();			
			ibean.genBeanTypeFromSpecs(BM_PlanCurrent_DC,"rv_BM_PLANCURRENT_DC","BM_PlanCurrent", null);
    	}
    	
    	
    	// -- BM_PLANCURRENT Swap Risk--//
    	public void initIBeanFromIXL_RawValues_BM_PlanCurrent_SwapRisk()  {
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.infile_rawvalues_IXL_name);
    		    	    		
    		//Swap Sensi
    		String[][] BM_PlanCurrent_SwapSensi = defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLANCURRENT_SwapRisk();
			ibean.genBeanTypeFromSpecs(BM_PlanCurrent_SwapSensi,"rv_BM_PLANCURRENT_SwapRisk","BM_PlanCurrent", null);
			
			//Swap Fixing Risk
			String[][] BM_PlanCurrent_SwapFxRisk = defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLANCURRENT_SwapFXRisk();			
			ibean.genBeanTypeFromSpecs(BM_PlanCurrent_SwapFxRisk,"rv_BM_PLANCURRENT_SwapFxRisk","BM_PlanCurrent", null);
			
			//Bond Fixing Risk
			String[][] BM_PlanCurrent_BondFxRisk = defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLANCURRENT_BondFXRisk();			
			ibean.genBeanTypeFromSpecs(BM_PlanCurrent_BondFxRisk,"rv_BM_PLANCURRENT_BondFxRisk","BM_PlanCurrent", null);
			
			//ILB Fixing Risk
			String[][] BM_PlanCurrent_ILBFxRisk = defOptimAppXLSpecs.getArchSpecs_rawValues_BM_PLANCURRENT_ILBFXRisk();			
			ibean.genBeanTypeFromSpecs(BM_PlanCurrent_ILBFxRisk,"rv_BM_PLANCURRENT_ILBFxRisk","BM_PlanCurrent", null);
			
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
    	
    	// -- Bond Baukasten --//
    	public void initIBeanFromIXL_BondsBaukastenRawValues_Part1()  {
    		String BondBaukasten_RawValues_part1_file = "BONDBAUKASTEN_RawValues_F_IXL_Part1.xlsx";
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, BondBaukasten_RawValues_part1_file);
    		String[][] BondBaukasten_RawValues_part1 = defOptimAppXLSpecs.getBondsBauKastenRawValuesArchSpecs_Part1();			
			ibean.genBeanTypeFromSpecs(BondBaukasten_RawValues_part1,"bean_BONDBAUKASTEN_RawValues_Part1","BONDBAUKASTEN_RawValues", BondBaukasten_RawValues_part1_file);
		}
    	public void initIBeanFromIXL_BondsBaukastenRawValues_Part2()  {
    		String BondBaukasten_RawValues_part2_file = "BONDBAUKASTEN_RawValues_F_IXL_Part2.xlsx";
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, BondBaukasten_RawValues_part2_file);
    		String[][] BondBaukasten_RawValues_part2 = defOptimAppXLSpecs.getBondsBauKastenRawValuesArchSpecs_Part2();			
			ibean.genBeanTypeFromSpecs(BondBaukasten_RawValues_part2,"bean_BONDBAUKASTEN_RawValues_Part2","BONDBAUKASTEN_RawValues", BondBaukasten_RawValues_part2_file);
		}
    	public void initIBeanFromIXL_BondsBaukastenRawValues_Part3()  {
    		String BondBaukasten_RawValues_part3_file = "BONDBAUKASTEN_RawValues_F_IXL_Part3.xlsx";
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, BondBaukasten_RawValues_part3_file);
    		String[][] BondBaukasten_RawValues_part3 = defOptimAppXLSpecs.getBondsBauKastenRawValuesArchSpecs_Part3();			
			ibean.genBeanTypeFromSpecs(BondBaukasten_RawValues_part3,"bean_BONDBAUKASTEN_RawValues_Part3","BONDBAUKASTEN_RawValues", BondBaukasten_RawValues_part3_file);
		}
    	public void initIBeanFromIXL_BondsBaukastenRawValues_Part4()  {
    		String BondBaukasten_RawValues_part4_file = "BONDBAUKASTEN_RawValues_F_IXL_Part4.xlsx";
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, BondBaukasten_RawValues_part4_file);
    		String[][] BondBaukasten_RawValues_part4 = defOptimAppXLSpecs.getBondsBauKastenRawValuesArchSpecs_Part4();			
			ibean.genBeanTypeFromSpecs(BondBaukasten_RawValues_part4,"bean_BONDBAUKASTEN_RawValues_Part4","BONDBAUKASTEN_RawValues", BondBaukasten_RawValues_part4_file);
		}
    	public void initIBeanFromIXL_BondsBaukastenRawValues_Part5()  {
    		String BondBaukasten_RawValues_part5_file = "BONDBAUKASTEN_RawValues_F_IXL_Part5.xlsx";
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, BondBaukasten_RawValues_part5_file);
    		String[][] BondBaukasten_RawValues_part5 = defOptimAppXLSpecs.getBondsBauKastenRawValuesArchSpecs_Part5();			
			ibean.genBeanTypeFromSpecs(BondBaukasten_RawValues_part5,"bean_BONDBAUKASTEN_RawValues_Part5","BONDBAUKASTEN_RawValues", BondBaukasten_RawValues_part5_file);
		}
    	public void initIBeanFromIXL_BondsBaukastenRawValues_Part6()  {
    		String BondBaukasten_RawValues_part6_file = "BONDBAUKASTEN_RawValues_F_IXL_Part6.xlsx";
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, BondBaukasten_RawValues_part6_file);
    		String[][] BondBaukasten_RawValues_part6 = defOptimAppXLSpecs.getBondsBauKastenRawValuesArchSpecs_Part6();			
			ibean.genBeanTypeFromSpecs(BondBaukasten_RawValues_part6,"bean_BONDBAUKASTEN_RawValues_Part6","BONDBAUKASTEN_RawValues", BondBaukasten_RawValues_part6_file);
		}
    	// -- Bond Baukasten --//
    	
    	
    	
    	public void initIBeanFromIXL_DKFOptiMarktparameterX()  {
    		
    		String DKF_opti_file = "DKF_Port_v1.25IXL.xlsx";
    		//DKF_opti_IXL_filename= "DKF_Port_v1.25IXL.xlsx";
    		
    		genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, initOptimAppConfig.DKF_opti_IXL_filename);
    		//genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, DKF_opti_file);
    		
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

    	// -- XL Config JAVA - jix  --//
    	public void initIBeanFromIXL_OptiConfi_DKF_Opti_Bonds()  {
	    	//-- File: OptiConfi_from_DKF_Opti_v1.24_Bonds ; Woksheet: JavaInput ---//
			//-- EmplanVorganbe: Year:B4:AZ4;NKB:B5:AZ5;WishedChangesinEB:B6:AZ6;NteTilgung:B7:AZ7;(NUM); Vector  size (1 x 52) ---//
	
	    	String DKF_opti_Bonds_file = "OptiConfi_from_DKF_Opti_v1.24_Bonds.xlsx";
	    	genIBeanFromXLSpecs ibean = new genIBeanFromXLSpecs(initOptimAppConfig.infiledir, DKF_opti_Bonds_file);
	    	
	    	//ExcelName: OptiConfi_from_DKF_Opti_v1.24_Bonds.xlsx; -- SheetName: EmplanVogaben-->JavaInput; --// 
	    	String[][] jix_EmplanVogabenArchSpecs = defOptimAppXLSpecs.getEmplanVogabenArchSpecs();			
			ibean.genBeanTypeFromSpecs(jix_EmplanVogabenArchSpecs,"EmplanVogabenArchSpecs","JavaInput", DKF_opti_Bonds_file);
	    	
	    	//-- Swaps Vol:Var - A16:A1443(String);Vol-Vars:E/F16:E/F1443(NUM); --//
	    	//Bonds Vol:Var - H16:H1443(String);Vol-Vars:M/N16:M/N1443(NUM); --//
			String[][] jix_JavaInputXLArchSpecs = defOptimAppXLSpecs.getJavaInputXLArchSpecs();			
			ibean.genBeanTypeFromSpecs(jix_JavaInputXLArchSpecs,"JavaInputXLArchSpecs","JavaInput", DKF_opti_Bonds_file);

    	}
    	// -- XL Config JAVA - jix  --//
    	
    	
    	
    	//-- New Sep2017 - V2-App--//
    	public void initIBeanFromBondBaukastenIXL()  {
    		//-- All -- Baukasten Bonds RawValues --- //
    		/*
    		initIBeanFromIXL_BondsBaukastenRawValues_Part1();   		    		
    		initIBeanFromIXL_BondsBaukastenRawValues_Part2();
    		initIBeanFromIXL_BondsBaukastenRawValues_Part3();
    		initIBeanFromIXL_BondsBaukastenRawValues_Part4();
    		initIBeanFromIXL_BondsBaukastenRawValues_Part5();
    		initIBeanFromIXL_BondsBaukastenRawValues_Part6();
    		*/
    	}
    	
    	
    	public void initIBeanFromSwapBaukastenIXL()  {
    		//-- All -- Baukasten Swaps RawValues --- //
    		/*
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part1();    		
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part2();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part3();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part4();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part5();
    		initIBeanFromIXL_BaukastenSwapsRawValues_Part6();
    		*/
    	}
    	
    	
    	
    	public void initIBeanFromIXLBean()  {    	
    		
    		
    		// -- XL Config JAVA - jix  --//
        	initIBeanFromIXL_OptiConfi_DKF_Opti_Bonds(); 
    		
        	
    		// -- Baukasten --//
    		//initIBeanFromBondBaukastenIXL();
    		//initIBeanFromSwapBaukastenIXL();
    		
    		    		    		
    		// -- Market Parameter --//
    		//initIBeanFromIXL_MarketAndParameters();
    		
    		// -- Market-Parameter1-5 --//
    		//initIBeanFromIXL_DKFOptiMarktparameterX();
    		
    		
    		// -- RawValues --//
    		//initIBeanFromIXL_RawValues_BM_PlanCurrent();
    		
    		//-- Swap Risk + Fixing Risk -- //
    		//initIBeanFromIXL_RawValues_BM_PlanCurrent_SwapRisk();
    		
    	}
    	////////////////////////////////
    	
    	
    	////////////////////////////////    	
    	public void genOXLFromIBean()  {
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
			//oxl.initOXLSheetForIBean(filepath,  deffile,  "BM_SWAPS_PLAN_CURRENTQUARTER", false);
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
    	////////////////////////////////
    	
    	public void appOtimSolveAndPublish()  {
    		
    		//Optmize for numYears    		
    		//int solveTestValidate=1; //-- solve --// 
    		
    		//-- Transform Matrix --//
    		int numYears=51;
    		boolean addDKF=false;
    		double addDKFForYears=0.25; //-- Quaterly=0.25, Half-yearly=0.5; Yearly=1; Biannual =2 --//
    		
    		
    		//-- Standard --//
    		/*
    		int numYears=2;
    		boolean addDKF=true;
    		double addDKFForYears=1;
    		*/
    		
    		optimSolveFunctApp optsol = new optimSolveFunctApp();
    		int solveTestValidate=0;
    		
    		//-- solveTestValidate=1 Solve Swap ; =10 DKF validate;;; =100 JOM-Env Test; =110 SRV Data Test --//
    		//solveTestValidate=100; // Test JOM Env
    		
    		//solveTestValidate=110; // Test SRV Data -- Swap Raw Values + Other Data --//
    		
    		//solveTestValidate=111; // Test Util -- AltMult -- sliceDV//
    		//solveTestValidate=111; // Test Util -- AltMult -- sliceDV -- optimValidateFunctApp.ojbfunc_optimize_rule_Proto_UtilTest()//
    		//solveTestValidate=111; // Test Util -- AltMult -- sliceDV -- optimValidateFunctApp.ojbfunc_optimize_rule_Proto_UtilTest()//
    		
    		//solveTestValidate=10; // Test computed Data - DKF
    		//solveTestValidate=1; // Execute Swap//
    		    		
    		//solveTestValidate=115; // Test JOM  Optimization for Bond - Expr test - Opti test ---//
    		
    		//solveTestValidate=120; // Real Bond Expr for Bond optimization - Bond Expr - Opti test ---//
    		//solveTestValidate=120; // Real Bond Expr for Bond optimization - Bond Expr - Opti test ---//
    		
    		//solveTestValidate=500; // GAMS Bond Expr for Bond optimization - Bond Expr - Opti test ---//
    		
    		solveTestValidate=600; // GAMS Swap Expr for Swap optimization - Swap Expr - Opti test ---//
    		
    		solveTestValidate=700; // Bonds+Swaps GAMS Expr for Swap optimization - Bonds+Swap Expr - Opti test ---//
    		
    		optsol.ojbfunc_optimize_rule(numYears, addDKF, addDKFForYears, solveTestValidate );	
    		
			//optsol.functUtil.publishResultBean();
			//optsol.functUtil.publishResultOXL();
		}
    	
    	
    	
    	
    	public void appOtimInitIBeanAndIXL_Solve()  {
    		//--ixl, bean, oxl -  done--
    		initIBeanFromIXLBean();
    		    		
    		//-- required only for validation -- efficiently done by genTXT -- //
    		//-- genOXLFromIBean();
    		
    		
    		//-- TEST load beans into JOM//
    		//--TEST -- optimSolveDataset dataSet= new optimSolveDataset();
			//--TEST -- dataSet.optimLoadEffectiveData();
    		
			
    		//-- optimize - solve --//
    		appOtimSolveAndPublish();
    		
    		
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
			
			optimAppMain appV2 = new optimAppMain();
			
			//--- Read IXL , Persist Beans, Generate OXL --//
			appV2.appOtimInitIBeanAndIXL_Solve();
			
			
			
			//-- old test--- Optimize --//
			//sensiApp.appOtimSolveAndPublish();
					
			LOG.info(" optimAppMain over!"  );
			
		} // void main //
		
	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////

	

    