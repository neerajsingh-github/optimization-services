
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



    public class optimSolveDataset {
    	
    	// -- setup env --//
    	private boolean initLoadRawBondBRVDataFlag=false;
    	private boolean initLoadRawSRVDataFlag=false;
    	private boolean loadDataSetupEnvFlag=false;
    	private createOXLFromIBean oxl;
    	
    	public optimDeserializeBean restore;
    	private optimRestoreIOBean restoreIOBean;
    	public String filepath;
    	public optimSerializeBean serialize;
    	
    	//-- Init Start Date + Opti Flags --//
    	public int initStartRefYear=2018;
    	public boolean SwapOptiFlag=true;
    	public boolean BondOptiFlag=true;
    	
    	
    	//-- Env --//
    	public optimSolveRuleConfig ruleConf=null;
    	//private int numYears;
    	public int numDates;
    	public int numSRVRawRowsPerDate;
    	
    	//private int numDecisionVariables=7;
    	//private int numSwapDecisionVariables=7;
    	//private int numBondDecisionVariables=7;
    	
    	
    	private final int TX_AllBaukastenSRVRawRowsPerDate = 1428; 
    	private final int TX_AllBestandRawRows  = 412; //  [From 31.01.2007-Till 31.12.2067]
    	private final int idxEffStartBestandRows = 120; // [Starting date 31.01.2017]   
        private final int numEffAllBestandRows = 292; // [Starting date 31.01.2017 - till End]
    	private int numEffBestandRows = 292;  // Select the time grid slice //
    	
    	
    	
    	//XLConfigJava
    	public double [][] jix_EV_Data_Year; 
    	public double [][] jix_EV_Data_NKB; 
    	public double [][] jix_EV_Data_ChangedEB ;
    	public double [][] jix_EV_Data_NetTilgung;

    	public String[][] jix_Config_BondProgramType;
    	public String[][] jix_Config_SwapProgramType;
    	
    	public double [][] jix_Config_SwapVols ;
    	public double [][] jix_Config_SwapVars ;
    	public double [][] jix_Config_BondVols ;
    	public double [][] jix_Config_BondVars; 
    	public double [][] jix_Config_BondWeights;
		
    	public int [] jix_config_BondDV_IdxActive;
    	public int [] jix_config_SwapDV_IdxActive;
    	
    	public double [][][] bondsRVDataRawEffDV3D;
    	public double [][][] swapsRVDataRawEffDV3D;
    	
    	public double [][] TransformedBondWeightKey;
    	
    	// data
    	public double [][] BestandEff;
    	public String [] BestandEffStrDate;
    	public double [] BestandEffJulDate;
    	
    	    	
    	public double[][][] bondsRVDataRaw3D;
    	public String[][] bondsRVInstrRaw2D;
    	public String[][] bondsRVDateRaw2D;
    	public String[][] bondsRVFmtDateRaw2D;
    	public double[][] bondsRVJulDateRaw2D;
    	public double[][] bondsRVDataRaw;
    	public String[][] bondsRVDateRaw;
    	public String[][] bondsRVInstrRaw;
    	public double[][] bondsRVJulDateRaw;  
    	public String[][] bondsRVFmtDateRaw;

    	public double[][][] bondsRVDataTransformed3D;
    	
    	public double[][][] bondsRVEff;
    	public double [][][] bondsRVAnnuitySensiEff;
    	public double [][][]bondsRVPVCleanEff;
    	public double [][][]bondsRVDebtCleanEff;
    	
    	//private boolean loadRawSRVDataFlag=false;
    	public double[][][] swapsRVDataRaw3D;
    	public double[][] swapsRVDataRaw;
    	public String[][] swapsRVDateRaw;
    	private String[][] swapsDataPortRaw;
    	private double[] swapsRVJulDateRaw;
    	private String[] swapsRVFmtDateRaw;

    	private double[][][] swapsRVDataTransformed3D;
    	
    	public double[][][] swapsRVEff;
    	public double [][][] swapsRVAnnuitySensiEff;
    	public double [][][]swapsRVPVCleanEff;
    	public double [][][]swapsRVDebtCleanEff;
    	
    	//TM _Data//
    	public double[][] RawValuesBondTM;
    	public double[][] RawValuesSwapTM;
    	public double [][] BestandBondTMEff;
    	public double [][] BestandSwapTMEff;
    	
    	public double[][] PVCleanBondTMEff ;
    	public double[][] PVCleanSwapTMEff ;
    	public double [][]PVCleanEff;
    	
    	public double [][] BondRiskSensiTMEff;
    	public double [][] SwapRiskSensiTMEff;
    	public double [][] BondILBSwapFxRiskBondTMEff;
    	public double [][] BondILBSwapFxRiskSwapTMEff;
    	public double [][] SchuldBondTMEff;
    	public double [][] SchuldSwapTMEff;
    	
    	
    	public double [][] PCRP1Eff;
    	public double [][] SchuldEff; 
    	public double SchuldVal;

    	
    	//-- Swap Risk + Fixing Risk --//
    	public double [][] SwapSensiRiskEff;
    	public double [][] SwapFxRiskEff;    	
    	public double [][] BondFxRiskEff;
    	public double [][] ILBFxRiskEff;
    	
    	
    	//-- not used-- check the data---//
    	public double[][] Zap1_PCRP1Eff;
    	public double[][] RefPCSensiEff;
    	public double [][] FixRiskCovEff ;
    	public double [][] PCCovEff; 
    	public double [][] PCRPEff;
    	
    	//-- Market1-5 --//
    	public double [][]PCCov_Mkt1; // 31 x 31  
    	public double [][]PCRP_Mkt1; // size 31	// -- 
    	public double [][]FixRiskCov_Mkt1; 
    	public double [][]PCCov_Mkt2; 
    	public double [][]PCRP_Mkt2;  
    	public double [][]FixRiskCov_Mkt2; 
    	public double [][]PCCov_Mkt3;  
    	public double [][]PCRP_Mkt3;  
    	public double [][]FixRiskCov_Mkt3; 
    	public double [][]PCCov_Mkt4;  
    	public double [][]PCRP_Mkt4;  
    	public double [][]FixRiskCov_Mkt4; 
    	public double [][]PCCov_Mkt5;  
    	public double [][]PCRP_Mkt5;  
    	public double [][]FixRiskCov_Mkt5; 
    	
    	
    	
    	//-- Constraints & defined vars --//
    	public double[] upConstraintBase7V;
    	public double[] downConstraintBase7V;    	
    	public double [][]jUPConstLtd84V;
		public double [][]jDNConstLtd84V;				
		
		public double[] jSwapRiskLimitEff12V;
    	
    	public double StockLimitQtr;
    	public double StockLimit1Yr;
    	public double StockLimit2Yr;

    	public double[] StockLimitQtr7V;
    	public double[] StockLimit1Yr7V;
    	public double[] StockLimit2Yr7V;

    	public double[] init0Sol7V;
    	public double[] init1Sol7V;
    	public double[] initN1Sol7V;
    	
    	public double[][] initDVSolSet;
    	
    	public double[] initSolSetXL7V;
    	
    	public boolean dataFlag=false;
    	
    	
    	public optimSolveDataset(optimSolveRuleConfig ruleConfig) {
    		this.ruleConf=ruleConfig ;
    		if (this.loadDataSetupEnvFlag==false) optimLoadEffectiveDataSetupEnv();
    		this.numDates=this.ruleConf.numDateRows;
    		//this.numDecisionVariables=this.ruleConf.numDecisionVariables;
			
    		this.numSRVRawRowsPerDate=  this.ruleConf.numSRVRawRowsPerDate; 
			numEffBestandRows = this.numDates;
			System.out.println(" optimEffectiveDataset: optimLoadEffectiveDataSetupEnv setup done: numDates=" + this.numDates + "; numSRVRawRowsPerDate=" + this.numSRVRawRowsPerDate);
    	}
    	

    	public void optimLoadEffectiveDataSetupEnv() {
    		System.out.println(" optimEffectiveDataset: optimLoadEffectiveDataSetupEnv : set up the env !");
    		
    		//configure global env //
    		this.oxl = new createOXLFromIBean();
    		this.filepath=initOptimAppConfig.outfiledir;
			this.filepath = optimValidateFile.validatePath(filepath);
			
			this.restore = new optimDeserializeBean(filepath);
			this.restoreIOBean = new optimRestoreIOBean();
			this.serialize = new optimSerializeBean(filepath);
			
			//-- setup initially ---- Re-setup later based on data --//
			if (this.ruleConf == null) {
				this.numDates=292;
				this.numSRVRawRowsPerDate= 1428;  //old was 448 //  // new: numDateRows=292 --- 412//
				//this.numDecisionVariables=7;
				this.ruleConf.numYears=51;
			}
			//this.numDates=292;
			//this.numSRVRawRowsPerDate= 1428;
			
			this.loadDataSetupEnvFlag=true;
			
    	}
    	
    	    	
    	
    	
    	
    	private void initLoadAndJoinBondBuakastenBRVDataRaw(boolean forceInit) 	{
        	
    		
    		//**** -- initLoadAndJoinBondBuakastenBRVDataRaw - file should be generated , even if serdat does not exit and flag is false; --***//
    		
    		if (forceInit == false) {
    			//-- check if file exists: "AllBaukastenSwapsRVDataRaw.serdat"--//
    			
    			String beanName = "AllBaukastenBondsRVDataRaw.serdat";
    			String beanFileName=this.filepath+"IBean-"+beanName+"-XL.ser";
    			System.out.println(" SRVDataRaw FullFileName: " + beanFileName);
    			String beanDateName = "AllBaukastenBondsRVDatesRaw.serdat";
    			String beanDateFileName=this.filepath+"IBean-"+beanDateName+"-XL.ser";
    			
    			if ( optimValidateFile.validFile(beanFileName) ) {
    				this.initLoadRawSRVDataFlag=true;
    				return;
    			}
    		}
    		//--Add up the 5 slices of Srv Swaps_RawValues --//
    		// Part1=74999=75K-1; Part2=75K; Part3=75K; Part4=75K; Part5=60K; Part6=56977;  TOTAL = 416976 Rows// 
    		
    		//-- Bonds_RawValues Date--//
    		String [][]bondsRVDateP1 = (String[][]) this.restore.deserializeBeanAsString("srv_Date_Bk_BONDS_RawValues_Part1",filepath);  //IBean-srv_Data_Bk_BONDS_RawValues_Part1-XL
    		//-- Bonds_RawValues element --//
    		String [][]bondsRVEleP1 = (String[][]) this.restore.deserializeBeanAsString("srv_DataPort_Bk_BONDS_RawValues_Part1",filepath);					
    		//-- Bonds_RawValues Data --//
    		double [][]bondsRVDataP1 = this.restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BONDBAUKASTEN_RawValues_Part1","srv_Data_Bk_BONDS_RawValues_Part1");			
    		System.out.println(" Swaps_RawValues P1: size_data: " +  bondsRVDataP1.length + "; size_date: " +  bondsRVDateP1.length + "; size_ele: " +  bondsRVEleP1.length  );
    		 oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P1.txt", bondsRVDateP1);
				
     		//-- Bonds_RawValues P2 --//
     		String [][]bondsRVDateP2 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_BONDS_RawValues_Part2",filepath);			
     		String [][]bondsRVEleP2 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_BONDS_RawValues_Part2",filepath);								
     		double [][]bondsRVDataP2 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BONDBAUKASTEN_RawValues_Part2","srv_Data_Bk_BONDS_RawValues_Part2");			
     		System.out.println(" Bonds_RawValues P2: size_data: " +  bondsRVDataP2.length + "; size_date: " +  bondsRVDateP2.length + "; size_ele: " +  bondsRVEleP2.length  );
     		oxl.genTxtFileFrom2DDateAsString(filepath, "BeanSpec-P2-BONDSBaukasten_BRV_Date_P2.txt", bondsRVDateP2); 
     		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-BONDSBaukasten_BRV_Date_P2.txt", bondsRVDateP2);
      		    		
     		//-- Bonds_RawValues P3 --//
     		String [][]bondsRVDateP3 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_BONDS_RawValues_Part3",filepath);			
     		String [][]bondsRVEleP3 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_BONDS_RawValues_Part3",filepath);
     		double [][]bondsRVDataP3 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BONDBAUKASTEN_RawValues_Part3","srv_Data_Bk_BONDS_RawValues_Part3");			
     		System.out.println(" BONDS_RawValues P3: size_data: " +  bondsRVDataP3.length + "; size_date: " +  bondsRVDateP3.length + "; size_ele: " +  bondsRVEleP3.length  );
     		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-BONDSBaukasten_SRV_Date_P3.txt", bondsRVDateP3);			
     		
     		//-- Bonds_RawValues P4 --//
     		String [][]bondsRVDateP4 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_BONDS_RawValues_Part4",filepath);
     		String [][]bondsRVEleP4 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_BONDS_RawValues_Part4",filepath);
     		double [][]bondsRVDataP4 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BONDBAUKASTEN_RawValues_Part4","srv_Data_Bk_BONDS_RawValues_Part4");			
     		System.out.println(" BONDS_RawValues P4: size_data: " +  bondsRVDataP4.length + "; size_date: " +  bondsRVDateP4.length + "; size_ele: " +  bondsRVEleP4.length  );
     		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P4.txt", bondsRVDateP4);			
     		
     		//-- Bonds_RawValues P5 --//
     		String [][]bondsRVDateP5 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_BONDS_RawValues_Part5",filepath);			
     		String [][]bondsRVEleP5 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_BONDS_RawValues_Part5",filepath);
     		double [][]bondsRVDataP5 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BONDBAUKASTEN_RawValues_Part5","srv_Data_Bk_BONDS_RawValues_Part5");
     		System.out.println(" BONDS_RawValues P5: size_data: " +  bondsRVDataP5.length + "; size_date: " +  bondsRVDateP5.length + "; size_ele: " +  bondsRVEleP5.length  );
     		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P5.txt", bondsRVDateP5);
     		
     		//-- Bonds_RawValues_Data P6 --//
     		String [][]bondsRVDateP6 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_BONDS_RawValues_Part6",filepath);			
     		String [][]bondsRVEleP6 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_BONDS_RawValues_Part6",filepath);
     		double [][]bondsRVDataP6 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BONDBAUKASTEN_RawValues_Part6","srv_Data_Bk_BONDS_RawValues_Part6");
     		System.out.println(" BONDS_RawValues P6: size_data: " +  bondsRVDataP6.length + "; size_date: " +  bondsRVDateP6.length + "; size_ele: " +  bondsRVEleP6.length  );
     		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P6.txt", bondsRVDateP6);
     		
     		
     		
     		//-- Bonds_RawValues_Data P1-6 --//    		
     		double [][] srvData1234= com.stfe.optim.util.optimSliceArray.join4Array2D(bondsRVDataP1, bondsRVDataP2, bondsRVDataP3, bondsRVDataP4);
     		double [][] srvData56= com.stfe.optim.util.optimSliceArray.join2Array2D(bondsRVDataP5, bondsRVDataP6);   
     		this.bondsRVDataRaw= com.stfe.optim.util.optimSliceArray.join2Array2D(srvData1234, srvData56);
     		
     		System.out.println(" Bonds_RawValues BRVDataRaw:size Row: " +  this.bondsRVDataRaw.length + "; bondsRVDataRaw:size Col: " +  this.bondsRVDataRaw[0].length );
     		oxl.genTxtFileFrom2DData(filepath, "All_BondsBaukasten_BRV_Data_Raw_416976Rows.txt", this.swapsRVDataRaw);
     		
     		
     		//-- Bonds_RawValues_Dates P1-6 --//
     		String [][] srvDates1234= com.stfe.optim.util.optimSliceArray.join4Array2DString(bondsRVDateP1, bondsRVDateP2, bondsRVDateP3, bondsRVDateP4);
     		String [][] srvDates56= com.stfe.optim.util.optimSliceArray.join2Array2DString(bondsRVDateP5, bondsRVDateP6);   
     		this.bondsRVDateRaw= com.stfe.optim.util.optimSliceArray.join2Array2DString(srvDates1234, srvDates56);
     		System.out.println(" Bonds_RawValues BRVDates Raw:size Row: " +  this.bondsRVDateRaw.length + "; bondsRVDateRaw:size Col: " +  this.bondsRVDateRaw[0].length );
     		
     		//-- Bond_RawValues_Portnames_Instr P1-6 --//
     		String [][] brvEle1234= com.stfe.optim.util.optimSliceArray.join4Array2DString(bondsRVEleP1, bondsRVEleP2, bondsRVEleP3, bondsRVEleP4);
     		String [][] brvEle56= com.stfe.optim.util.optimSliceArray.join2Array2DString(bondsRVEleP5, bondsRVEleP6);   
     		this.bondsRVInstrRaw= com.stfe.optim.util.optimSliceArray.join2Array2DString(brvEle1234, brvEle56);
     		System.out.println(" Bonds_RawValuesInstr bondsRVInstrRaw Raw:size Row: " +  this.bondsRVInstrRaw.length + "; bondsRVInstrRaw:size Col: " +  this.bondsRVInstrRaw[0].length );
     		
     		//-- store bond date as julian date --   Test the date --^//
			this.bondsRVJulDateRaw = new double[this.bondsRVDateRaw.length][1];
			this.bondsRVFmtDateRaw = new String[this.bondsRVDateRaw.length][1];
			java.util.Date date = null;
			  
			for (int i=0; i<this.bondsRVDateRaw.length; i++) {								
				this.bondsRVJulDateRaw[i][0]= 0;
								
				if (bondsRVDateRaw[i][0] == null) {
					  this.bondsRVJulDateRaw[i][0]= 0;
					  System.out.println(" Idx: "+ i +" >>> TEST bondsRVDate- dateStr: null" + bondsRVDateRaw[i][0] );
					 // continue;
				}
				  
				// java.txt.SimpleData
				java.text.SimpleDateFormat datestrfmt = new java.text.SimpleDateFormat("dd.MM.yyyy");
				date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(this.bondsRVDateRaw[i][0]) );
				this.bondsRVFmtDateRaw[i][0] = (datestrfmt.format(date)).toString();
					 
				//if (swapsRVDateRaw[i][0] != null)
				if  ( com.stfe.optim.util.optimConvDateFmt.isDouble(this.bondsRVDateRaw[i][0]) ) {
					this.bondsRVJulDateRaw[i][0]= Double.parseDouble(this.bondsRVDateRaw[i][0]);
				} else {				
					this.bondsRVJulDateRaw[i][0]= com.stfe.optim.util.optimConvDateFmt.convertToJulian(this.bondsRVDateRaw[i][0]);
				}
				//System.out.println(" Idx: "+ i +" >>> TEST bondsRVDate- fmtDate: " +  this.bondsRVFmtDateRaw[i] + " JulDate: " + bondsRVJulDateRaw[i] );
				
			
			}//-- setup bonds julian dates for SRV-Date --//
     		
     		//-- save the Bonds-Baukasten stream java data --  
     		this.serialize.serializeBeanAsPdouble(this.bondsRVDataRaw,"AllBaukastenBondsRVDataRaw.serdat", this.filepath) ;
     		
     		this.serialize.serializeBeanAsString((String[][])this.bondsRVInstrRaw,"AllBaukastenBondsRVInstrRaw.serdat", this.filepath) ;
     		
     		this.serialize.serializeBeanAsString((String[][])this.bondsRVDateRaw,"AllBaukastenBondsRVDatesRaw.serdat", this.filepath) ;
     		this.serialize.serializeBeanAsString((String[][])this.bondsRVFmtDateRaw,"AllBaukastenBondsRVFmtDatesRaw.serdat", this.filepath) ;
     		this.serialize.serializeBeanAsPdouble(this.bondsRVJulDateRaw,"AllBaukastenBondsRVJulDatesRaw.serdat", this.filepath) ;
     		this.initLoadRawBondBRVDataFlag=true;
     		
    	}
    	
    	
    	
    	private void initLoadAndJoinSwapsSRVDataRaw(boolean forceInit) 	{
    		
    		if (forceInit == false) {
    			//-- check if file exists: "AllBaukastenSwapsRVDataRaw.serdat"--//
    			String beanName = "AllBaukastenSwapsRVDataRaw.serdat";
    			String beanFileName=this.filepath+"IBean-"+beanName+"-XL.ser";
    			System.out.println(" SRVDataRaw FullFileName: " + beanFileName);
    			
    			if ( optimValidateFile.validFile(beanFileName) ) {
    				this.initLoadRawSRVDataFlag=true;
    				return;
    			}
    			
    		}
    		
    		//--Add up the 5 slices of Srv Swaps_RawValues --//
    		// Part1=74999=75K-1; Part2=75K; Part3=75K; Part4=75K; Part5=60K; Part6=56977;  TOTAL = 416976 Rows// 
    		
    		//-- Swaps_RawValues Date--//
    		String [][]swapsRVDateP1 = (String[][]) this.restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part1",filepath);
    		//-- Swaps_RawValues element --//
    		String [][]swapsRVEleP1 = (String[][]) this.restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part1",filepath);					
    		//-- Swaps_RawValues Data --//
    		double [][]swapsRVDataP1 = this.restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part1","srv_Data_Bk_SWAPS_RawValues_Part1");			
    		System.out.println(" Swaps_RawValues P1: size_data: " +  swapsRVDataP1.length + "; size_date: " +  swapsRVDateP1.length + "; size_ele: " +  swapsRVEleP1.length  );
    					 
    		
    		//works  -- oxl.genTxtFileFrom2DStringData(filepath, "Baukasten_SRV_Date_Raw_All.txt", this.swapsRVDateRaw);
    		 //oxl.genTxtFileFrom2DData(filepath, "P1-Baukasten_SRV_Data_P1.txt", swapsRVDataP1);    		
    		 //oxl.genTxtFileFrom2DStringData(filepath, "P1-Baukasten_SRV_Date_P1.txt", swapsRVDateP1);
    		 //oxl.genTxtFileFrom2DStringData(filepath, "P1-Baukasten_SRV_Meta_P1.txt", swapsRVEleP1);
    		
    		//oxl.genTxtFileFrom2DStringData(filepath, "TEST-P1-Baukasten_SRV_Date_P1.txt", swapsRVDateP1);  
    		//BEAN=IBean-Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part1-XL// - Element=Bean-srv_Date_Bk_SWAPS_RawValues_Part1
    		//oxl.genTxtDataFromIBean(filepath,  "TxtOXL_P1_Baukasten_SRV_Data_P1.txt",  "Baukasten_RawValues","bean_BAUKASTEN_RawValues_Part1");
    		 //oxl.genTxtDataFromIBean(filepath,  "BeanSpec_P1_Baukasten_SRV_Data_P1.txt",  "TxtOXL_P1_Baukasten_SRV_Data_P1","srv_Date_Bk_SWAPS_RawValues_Part1");
    		
    		 oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P1.txt", swapsRVDateP1);
    		 
    		     		 				
    		//-- Swaps_RawValues P2 --//
    		String [][]swapsRVDateP2 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part2",filepath);			
    		String [][]swapsRVEleP2 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part2",filepath);								
    		double [][]swapsRVDataP2 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part2","srv_Data_Bk_SWAPS_RawValues_Part2");			
    		System.out.println(" Swaps_RawValues P2: size_data: " +  swapsRVDataP2.length + "; size_date: " +  swapsRVDateP2.length + "; size_ele: " +  swapsRVEleP2.length  );
    		oxl.genTxtFileFrom2DDateAsString(filepath, "BeanSpec-P2-Baukasten_SRV_Date_P2.txt", swapsRVDateP2); 
    		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P2.txt", swapsRVDateP2);
    		 
     		    		
    		//-- Swaps_RawValues P3 --//
    		String [][]swapsRVDateP3 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part3",filepath);			
    		String [][]swapsRVEleP3 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part3",filepath);
    		double [][]swapsRVDataP3 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part3","srv_Data_Bk_SWAPS_RawValues_Part3");			
    		System.out.println(" Swaps_RawValues P3: size_data: " +  swapsRVDataP3.length + "; size_date: " +  swapsRVDateP3.length + "; size_ele: " +  swapsRVEleP3.length  );
    		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P3.txt", swapsRVDateP3);			
    		
    		//-- Swaps_RawValues P4 --//
    		String [][]swapsRVDateP4 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part4",filepath);
    		String [][]swapsRVEleP4 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part4",filepath);
    		double [][]swapsRVDataP4 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part4","srv_Data_Bk_SWAPS_RawValues_Part4");			
    		System.out.println(" Swaps_RawValues P4: size_data: " +  swapsRVDataP4.length + "; size_date: " +  swapsRVDateP4.length + "; size_ele: " +  swapsRVEleP4.length  );
    		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P4.txt", swapsRVDateP4);			
    		
    		//-- Swaps_RawValues P5 --//
    		String [][]swapsRVDateP5 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part5",filepath);			
    		String [][]swapsRVEleP5 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part5",filepath);
    		double [][]swapsRVDataP5 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part5","srv_Data_Bk_SWAPS_RawValues_Part5");
    		System.out.println(" Swaps_RawValues P5: size_data: " +  swapsRVDataP5.length + "; size_date: " +  swapsRVDateP5.length + "; size_ele: " +  swapsRVEleP5.length  );
    		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P5.txt", swapsRVDateP5);
    		
    		//-- Swaps_RawValues_Data P6 --//
    		String [][]swapsRVDateP6 = (String[][]) restore.deserializeBeanAsString("srv_Date_Bk_SWAPS_RawValues_Part6",filepath);			
    		String [][]swapsRVEleP6 = (String[][]) restore.deserializeBeanAsString("srv_DataPort_Bk_SWAPS_RawValues_Part6",filepath);
    		double [][]swapsRVDataP6 = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-bean_BAUKASTEN_RawValues_Part6","srv_Data_Bk_SWAPS_RawValues_Part6");
    		System.out.println(" Swaps_RawValues P6: size_data: " +  swapsRVDataP6.length + "; size_date: " +  swapsRVDateP6.length + "; size_ele: " +  swapsRVEleP6.length  );
    		oxl.genTxtFileFrom2DDateAsString(filepath, "Bean-Baukasten_SRV_Date_P6.txt", swapsRVDateP6);
    		
    		//-- Swaps_RawValues_Data P1-6 --//    		
    		double [][] srvData1234= com.stfe.optim.util.optimSliceArray.join4Array2D(swapsRVDataP1, swapsRVDataP2, swapsRVDataP3, swapsRVDataP4);
    		double [][] srvData56= com.stfe.optim.util.optimSliceArray.join2Array2D(swapsRVDataP5, swapsRVDataP6);   
    		this.swapsRVDataRaw= com.stfe.optim.util.optimSliceArray.join2Array2D(srvData1234, srvData56);
    		
    		System.out.println(" Swaps_RawValues swapsRVDataRaw:size Row: " +  this.swapsRVDataRaw.length + "; swapsRVDataRaw:size Col: " +  this.swapsRVDataRaw[0].length );
    		oxl.genTxtFileFrom2DData(filepath, "Baukasten_SRV_Data_Raw_All.txt", this.swapsRVDataRaw);
    		
    		
    		
    		
    		//-- Swaps_RawValues_Date P1-6 --//    		
    		//String [][] srvDate1234= com.stfe.optim.util.optimSliceArray.join4Array2DString(swapsRVDateP1, swapsRVDateP2, swapsRVDateP3, swapsRVDateP4);
    		//String [][] srvDate56= com.stfe.optim.util.optimSliceArray.join2Array2DString(swapsRVDateP5, swapsRVDateP6);    		
    		//this.swapsRVDateRaw= com.stfe.optim.util.optimSliceArray.join2Array2DString(srvDate1234, srvDate56);
    		
    		
    		String [][] srvDate12= com.stfe.optim.util.optimSliceArray.join2Array2DString(swapsRVDateP1, swapsRVDateP2);
    		String [][] srvDate23= com.stfe.optim.util.optimSliceArray.join2Array2DString(srvDate12, swapsRVDateP3);
    		String [][] srvDate34= com.stfe.optim.util.optimSliceArray.join2Array2DString(srvDate23, swapsRVDateP4);
    		
    		//oxl.genTxtFileFrom2DStringData(filepath, "Funny-NS-TEST_Baukasten_SRV_Date_Raw_P3_P4.txt", srvDate34); 
    		//oxl.genTxtFileFrom2DDateAsString(filepath, "Funny-StringDate-Bean-Baukasten_SRV_Date_P3_P4.txt", srvDate34);
    		    		
    		String [][] srvDate45= com.stfe.optim.util.optimSliceArray.join2Array2DString(srvDate34, swapsRVDateP5);
    		String [][] srvDate56All= com.stfe.optim.util.optimSliceArray.join2Array2DString(srvDate45, swapsRVDateP6);    		
    		    		
    		this.swapsRVDateRaw= new String[srvDate56All.length][1]; 
    		for (int i=0; i<srvDate56All.length; i++)
    			this.swapsRVDateRaw[i][0] =	srvDate56All[i][0];
    		
    		System.out.println(" Swaps_RawValues swapsRVDateRaw:size Row: " +  this.swapsRVDateRaw.length + "; swapsRVDateRaw:size Col: " +  this.swapsRVDateRaw[0].length );
    		
    		oxl.genTxtFileFrom2DStringData(filepath, "AsofNow-All-CHK-swapsRVDateRaw:TextJulian-Baukasten_SRV_Date_Raw_All.txt", this.swapsRVDateRaw);
    		
    		//-- Swaps_RawValues_DataPort P1-6 --//
    		String [][] srvDataPort1234= com.stfe.optim.util.optimSliceArray.join4Array2DString(swapsRVEleP1, swapsRVEleP2, swapsRVEleP3, swapsRVEleP4);
    		String [][] srvDataPort56= com.stfe.optim.util.optimSliceArray.join2Array2DString(swapsRVEleP5, swapsRVEleP6);    		
    		this.swapsDataPortRaw= com.stfe.optim.util.optimSliceArray.join2Array2DString(srvDataPort1234, srvDataPort56);
    		System.out.println(" Swaps_RawValues swapsRVDataPortRaw:size Row: " +  this.swapsDataPortRaw.length + "; swapsRVDataPortRaw:size Col: " +  this.swapsDataPortRaw[0].length );
    		    		
    		
    		
    		//-- store date as julian date --   Test the date --^//
			this.swapsRVJulDateRaw = new double[this.swapsRVDateRaw.length];
			this.swapsRVFmtDateRaw = new String[this.swapsRVDateRaw.length];
			java.util.Date date = null;
			  
			for (int i=0; i<this.swapsRVDateRaw.length; i++) {								
				this.swapsRVJulDateRaw[i]= 0;
				
				if (srvDate56All[i][0] == null) {
					  System.out.println(" Idx: "+ i +" ??? Error srvDate56All- dateStr: null" + srvDate56All[i][0] );
				}
				
				  if (swapsRVDateRaw[i][0] == null) {
					  this.swapsRVJulDateRaw[i]= 0;
					  // -- srvDate56All -- // null//
					  //System.out.println(" Idx: "+ i +" >>> TEST srvDate56All- dateStr: null" + srvDate56All[i][0] );
					  
					  System.out.println(" Idx: "+ i +" ??? Error swapsRVDate- dateStr: null" + swapsRVDateRaw[i][0] );
					 // continue;
				  }
				
				  /*
				  java.util.Date date = null;
				  String fmtDate; String xlDate=this.swapsRVDateRaw[i][0];
				  java.text.SimpleDateFormat datestrfmt = new java.text.SimpleDateFormat("dd.MM.yyyy");
				  if ( isDouble() isDouble(xlDate) ) {
					  date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf( xlDate) );
				  } else {
					  date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(xlDate));
				  }
				  fmtDate = (datestrfmt.format(date)).toString();
				  */
				
				  
				  // java.txt.SimpleData
				  java.text.SimpleDateFormat datestrfmt = new java.text.SimpleDateFormat("dd.MM.yyyy");
				  // if ( isDouble(String.valueOf(xlDate) ) ) { }
				  date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(this.swapsRVDateRaw[i][0]) );
				  this.swapsRVFmtDateRaw[i] = (datestrfmt.format(date)).toString();
					 
								
				
				//if (swapsRVDateRaw[i][0] != null)
				if  ( com.stfe.optim.util.optimConvDateFmt.isDouble(this.swapsRVDateRaw[i][0]) ) {
					this.swapsRVJulDateRaw[i]= Double.parseDouble(this.swapsRVDateRaw[i][0]);
				} else {				
					this.swapsRVJulDateRaw[i]= com.stfe.optim.util.optimConvDateFmt.convertToJulian(this.swapsRVDateRaw[i][0]);
				}
				//System.out.println(" Idx: "+ i +" >>> TEST swapsRVDate- fmtDate: " +  this.swapsRVFmtDateRaw[i] + " JulDate: " + swapsRVJulDateRaw[i] );
			
			}//-- setup julian dates for SRV-Date --//
			
			
			//-- save the Baukasten stream java data --  
    		this.serialize.serializeBeanAsPdouble(this.swapsRVDataRaw,"AllBaukastenSwapsRVDataRaw.serdat", this.filepath) ;
    		this.serialize.serializeBean1DAsPdouble(this.swapsRVJulDateRaw,"AllBaukastenSwapsRVDatesRaw.serdat", this.filepath) ;
			this.initLoadRawSRVDataFlag=true;			
    		
    	} // -- initLoadAndJoinSRVDataRaw --//
    	
    	
    	
    	public void optimLoadEffectiveSRVDataInit() {
    		//--set up Env --//
    		if (this.loadDataSetupEnvFlag==false) optimLoadEffectiveDataSetupEnv();
    		
    		//-- load full raw SRV data --//
    		if (this.initLoadRawSRVDataFlag==false) initLoadAndJoinSwapsSRVDataRaw(false);
    		else initLoadAndJoinSwapsSRVDataRaw(true);
    		this.swapsRVDataRaw = (double[][])this.restore.deserializeBeanAsPdouble("AllBaukastenSwapsRVDataRaw.serdat",filepath);
    		oxl.genTxtFileFrom2DData(filepath, "AllBaukastenSwapsRVDataRaw_Rows416976_All.txt", this.swapsRVDataRaw);
    		this.swapsRVJulDateRaw=(double[])this.restore.deserializeBean1DAsPdouble("AllBaukastenSwapsRVDatesRaw.serdat",filepath);
    		//this.serialize.serializeBean1DAsPdouble(this.swapsRVJulDateRaw,"AllBaukastenSwapsRVDatesRaw.serdat", this.filepath) ;
    		double[][] swapsRVJulDateRawMat= new double[this.swapsRVJulDateRaw.length][1];
    		for (int i=0; i<swapsRVJulDateRawMat.length; i++)
    			swapsRVJulDateRawMat[i][0] = swapsRVJulDateRaw[i];
    		oxl.genTxtFileFrom2DData(filepath, "AllBaukastenSwapsRVDatesRaw_Rows416976_All.txt", swapsRVJulDateRawMat);
						
			
			
			if (this.initLoadRawBondBRVDataFlag==false) initLoadAndJoinBondBuakastenBRVDataRaw(false);
			else initLoadAndJoinBondBuakastenBRVDataRaw(true);
    		this.bondsRVDataRaw = (double[][])this.restore.deserializeBeanAsPdouble("AllBaukastenBondsRVDataRaw.serdat",filepath);
    		oxl.genTxtFileFrom2DData(filepath, "AllBaukastenBondsRVDataRaw_Rows416976_All.txt", this.bondsRVDataRaw);
    		
    		this.bondsRVDateRaw = (String[][])this.restore.deserializeBeanAsString("AllBaukastenBondsRVDatesRaw.serdat",filepath);
    		this.bondsRVFmtDateRaw = (String[][])this.restore.deserializeBeanAsString("AllBaukastenBondsRVFmtDatesRaw.serdat",filepath);
    		this.bondsRVInstrRaw = (String[][])this.restore.deserializeBeanAsString("AllBaukastenBondsRVInstrRaw.serdat",filepath);
    		
    		this.bondsRVJulDateRaw = (double[][])this.restore.deserializeBeanAsPdouble("AllBaukastenBondsRVJulDatesRaw.serdat",filepath);
    		double[][] bondsRVJulDateRawMat= new double[this.bondsRVJulDateRaw.length][1];
    		for (int i=0; i<bondsRVJulDateRawMat.length; i++)
    			bondsRVJulDateRawMat[i][0] = bondsRVJulDateRaw[i][0];
    		oxl.genTxtFileFrom2DData(filepath, "AllBaukastenBondsRVJulDatesRaw_Rows416976_All.txt", bondsRVJulDateRawMat);
						
		}

    	
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	public void optimLoadEffectiveBondDataSetupEnv_TranformMatrix(){
    		//-- setup the DV---//
    		optimTransformMatrix otm = new optimTransformMatrix();
    		this.ruleConf.numBondDecisionVariables=otm.getMaxDVFromConfigVars(this.jix_Config_BondVars); 
    		//this.ruleConf.numDecisionVariables=this.ruleConf.numBondDecisionVariables;
    		
    		//this.ruleConf.numYears = otm.getNumDVFromConfigVars(this.jix_Config_BondVars) / 28;
    		this.ruleConf.numYears = this.ruleConf.numBondDecisionVariables / 7;
    		
    		if (this.ruleConf.numDecisionVariables != (this.ruleConf.numYears * 7) ) 
    			System.out.println("??? Inconsistent numBondDecisionVariables: numYears! " + "numYears: " + ruleConf.numYears + "; this.numBondDecisionVariables: " +  this.ruleConf.numBondDecisionVariables);
    		
    		//ruleConf.TRANSMAT= true;
			
			if (this.ruleConf.TimeDimForwardSquare==false) {
				this.ruleConf.numDateRows = optimArrayListUtil.sizeUniqueArr(this.swapsRVJulDateRaw);
				this.numDates = optimArrayListUtil.sizeUniqueArr(this.swapsRVJulDateRaw);
			} else {
				this.ruleConf.numDateRows =  this.ruleConf.getNumDateRowsRule(this.ruleConf.numYears);  // this.numYears * 12;
				this.numDates = this.ruleConf.getNumDateRowsRule(this.ruleConf.numYears);
	    		if (this.ruleConf.numDateRows != this.numDates) 
	    			System.out.println("??? Inconsistent numDateRows! " + "ruleConf.numDateRows: " + ruleConf.numDateRows + "; this.numDates: " +  this.numDates);
			}
			
			
			if (ruleConf.InstrDimForwardSquare==false || ruleConf.InstrDimForwardSquare==true) {
	    		ruleConf.numSRVRawRowsPerDate=otm.getMaxDVFromConfigVars(this.jix_Config_BondVars);
	    		this.numSRVRawRowsPerDate=otm.getMaxDVFromConfigVars(this.jix_Config_BondVars);
	    		
	    		if (this.numSRVRawRowsPerDate != this.ruleConf.numBondDecisionVariables ) {
	    			System.out.println("??? Inconsistent numSRVRawRowsPerDate! " + "Different than numDV - ruleConf.numSRVRawRowsPerDate: " + ruleConf.numSRVRawRowsPerDate  );
	    			this.numSRVRawRowsPerDate = this.ruleConf.numBondDecisionVariables;
	    			this.ruleConf.numSRVRawRowsPerDate = this.ruleConf.numBondDecisionVariables;
	    		}
	    		//if (this.numSRVRawRowsPerDate != this.swapsRVDataTransformed3D[0].length )
	    			//System.out.println("??? Inconsistent numSRVRawRowsPerDate! " + "Different than this.swapsRVDataTransformed3D[0].length - ruleConf.numSRVRawRowsPerDate: " + ruleConf.numSRVRawRowsPerDate  );
	    	} else 	{
				
	    		this.ruleConf.numSRVRawRowsPerDate= 1428;
	    		this.numSRVRawRowsPerDate =  1428; 
	    		
	    	} 
			
    		this.ruleConf.numAnnualConstraints=1; //-- jix - config is annual-may change; Constraint stock vol quaterly  <= 15.0 ;  use addDKF from config file --// 
    		System.out.println("optimLoadEffectiveDataSetupEnv_TranformMatrix: numYears: " + this.ruleConf.numYears + "; numBondDecisionVariables: " + this.ruleConf.numBondDecisionVariables + " ; numDateRows: " 
    																					+ this.ruleConf.numDateRows + " ; numSRVRawRowsPerDate: "+ ruleConf.numSRVRawRowsPerDate );
    		
    	}
    	
    	
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
    	public void optimLoadEffectiveSwapDataSetupEnv_TranformMatrix(){
    		//-- setup the DV---//
    		optimTransformMatrix otm = new optimTransformMatrix();
    		
    		
    		this.ruleConf.numSwapDecisionVariables=otm.getMaxDVFromConfigVars(this.jix_Config_SwapVars);
    		
    		//this.ruleConf.numYears = otm.getNumDVFromConfigVars(this.jix_Config_SwapVars) / 28;
    		
    		//this.ruleConf.numYears = otm.getNumDVFromConfigVars(this.jix_Config_SwapVars) / 28;
    		this.ruleConf.numYears = this.ruleConf.numSwapDecisionVariables / 7;
    		
    		if (this.ruleConf.numDecisionVariables != (this.ruleConf.numYears * 7) ) 
    			System.out.println("??? Inconsistent numSwapDecisionVariables: numYears! " + "numYears: " + ruleConf.numYears + "; this.numSwapDecisionVariables: " +  this.ruleConf.numSwapDecisionVariables);
    		
    		//ruleConf.TRANSMAT= true;
			
			if (this.ruleConf.TimeDimForwardSquare==false) {
				this.ruleConf.numDateRows = optimArrayListUtil.sizeUniqueArr(this.swapsRVJulDateRaw);
				this.numDates = optimArrayListUtil.sizeUniqueArr(this.swapsRVJulDateRaw);
			} else {
				this.ruleConf.numDateRows =  this.ruleConf.getNumDateRowsRule(this.ruleConf.numYears);  // this.numYears * 12;
				this.numDates = this.ruleConf.getNumDateRowsRule(this.ruleConf.numYears);
	    		if (this.ruleConf.numDateRows != this.numDates) 
	    			System.out.println("??? Inconsistent numDateRows! " + "ruleConf.numDateRows: " + ruleConf.numDateRows + "; this.numDates: " +  this.numDates);
			}
			
			
			if (ruleConf.InstrDimForwardSquare==false || ruleConf.InstrDimForwardSquare==true) {
	    		ruleConf.numSRVRawRowsPerDate=otm.getMaxDVFromConfigVars(this.jix_Config_SwapVars);
	    		this.numSRVRawRowsPerDate=otm.getMaxDVFromConfigVars(this.jix_Config_SwapVars);
	    		if (this.numSRVRawRowsPerDate != this.ruleConf.numSwapDecisionVariables )
	    			System.out.println("??? Inconsistent numSRVRawRowsPerDate! " + "Different than numDV - ruleConf.numSRVRawRowsPerDate: " + ruleConf.numSRVRawRowsPerDate  );
	    		//if (this.numSRVRawRowsPerDate != this.swapsRVDataTransformed3D[0].length )
	    			//System.out.println("??? Inconsistent numSRVRawRowsPerDate! " + "Different than this.swapsRVDataTransformed3D[0].length - ruleConf.numSRVRawRowsPerDate: " + ruleConf.numSRVRawRowsPerDate  );
	    	} else 	{
				
	    		ruleConf.numSRVRawRowsPerDate=  optimArrayListUtil.numRedundantData(this.swapsRVJulDateRaw, this.swapsRVJulDateRaw[0]);
				this.numSRVRawRowsPerDate=  optimArrayListUtil.numRedundantData(this.swapsRVJulDateRaw, this.swapsRVJulDateRaw[0]);
				if (this.numSRVRawRowsPerDate !=  1428 ) 
					System.out.println("??? Inconsistent numSRVRawRowsPerDate! - ruleConf.numSRVRawRowsPerDate: " + ruleConf.numSRVRawRowsPerDate );
	    	} 
			
			
    		this.ruleConf.numAnnualConstraints=1; //-- jix - config is annual-may change; Constraint stock vol quaterly  <= 15.0 ;  use addDKF from config file --// 
    		System.out.println("optimLoadEffectiveDataSetupEnv_TranformMatrix: numYears: " + this.ruleConf.numYears + "; numSwapDecisionVariables: " + this.ruleConf.numSwapDecisionVariables + " ; numDateRows: " 
    																					+ this.ruleConf.numDateRows + " ; numSRVRawRowsPerDate: "+ ruleConf.numSRVRawRowsPerDate );
    		
    		
    		//-- numDV of swaps and bonds are different -- Separate explicitly BondDV and SwapDV --//
    		if (this.ruleConf.numSwapDecisionVariables == 0) {
    			optimLoadEffectiveBondDataSetupEnv_TranformMatrix();
    			this.jix_Config_SwapVars = this.jix_Config_BondVars;
    			this.ruleConf.numSwapDecisionVariables = this.ruleConf.numBondDecisionVariables;
    		}
    		
    	}
    	
    	
    	
    	private void optimLoadEffectiveData_LoadBond3D_TranformMatrix(){  
    		
    		int tx_numBRVRawDates= optimArrayListUtil.sizeUniqueArr2D(this.bondsRVJulDateRaw);
    		int tx_numBRVRawRowsEachDate=optimArrayListUtil.numRedundantData2D(this.bondsRVJulDateRaw, this.bondsRVJulDateRaw[0][0]);
    		System.out.println("+++ TRANSFORMED:  tx_numBRVRawDates : "+ tx_numBRVRawDates  +" --- tx_numBRVRawRowsEachDate:" + tx_numBRVRawRowsEachDate);
    		this.bondsRVDataRaw3D=new double[tx_numBRVRawDates ][tx_numBRVRawRowsEachDate][];
    		this.bondsRVDateRaw2D = new String[tx_numBRVRawDates][tx_numBRVRawRowsEachDate];
    		this.bondsRVFmtDateRaw2D = new String[tx_numBRVRawDates][tx_numBRVRawRowsEachDate];
    		this.bondsRVInstrRaw2D = new String[tx_numBRVRawDates][tx_numBRVRawRowsEachDate];
    		this.bondsRVJulDateRaw2D = new double[tx_numBRVRawDates][tx_numBRVRawRowsEachDate];
    		
    		for (int i=0, slcStart=0, slcEnd=(tx_numBRVRawRowsEachDate-1); i<tx_numBRVRawDates ; i++) {
    			
    			this.bondsRVDataRaw3D[i] =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.bondsRVDataRaw, slcStart, slcEnd, -1, -1 );
    			
    			//this.bondsRVJulDateRaw2D[i] =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.bondsRVJulDateRaw, slcStart, slcEnd, -1, -1 );
    			//this.bondsRVInstrRaw2D =  com.stfe.optim.util.optimSliceArray.slice2DArrayString(this.bondsRVInstrRaw, slcStart, slcEnd, -1, -1 );
    			//this.bondsRVDateRaw2D =  com.stfe.optim.util.optimSliceArray.slice2DArrayString(this.bondsRVDateRaw, slcStart, slcEnd, -1, -1 );
    			
    			slcStart = slcStart + tx_numBRVRawRowsEachDate;
				slcEnd= slcStart + tx_numBRVRawRowsEachDate -1;
    		}
    		// --- build up othere 2Ds for dates and instruments ---//
    		{ 
    			for (int t=0, cnt=0; t<tx_numBRVRawDates ; t++) {
    				for (int i=0; i<tx_numBRVRawRowsEachDate ; i++) {
    					this.bondsRVJulDateRaw2D[t][i] = this.bondsRVJulDateRaw[cnt][0];
    					this.bondsRVInstrRaw2D[t][i] = this.bondsRVInstrRaw[cnt][0];
    					this.bondsRVDateRaw2D[t][i] = this.bondsRVDateRaw[cnt][0];
    					this.bondsRVFmtDateRaw2D[t][i] = this.bondsRVFmtDateRaw[cnt][0];
    					cnt++;
    				}
    			}
    		} 
    		// --- build up othere 2Ds for dates and instruments ---//
    		

    		//--slice effective for active-Stratoms --//
    		this.bondsRVDataRawEffDV3D = new double[tx_numBRVRawDates][this.jix_config_BondDV_IdxActive.length][this.bondsRVDataRaw3D[0][0].length];
    		for (int i=0; i<tx_numBRVRawDates; i++) {
    			for (int j=0, idx=0; j<this.jix_Config_BondVars.length; j++) {
    				//--copy the data only if stratom is active --//
    				int indexArr = com.stfe.optim.util.optimArrayListUtil.getArrayIndexI(this.jix_config_BondDV_IdxActive, j);
    				if (indexArr > -1) {
    					if (indexArr != idx ) System.out.println(" ? Error idxArr not consistenet with counter idx");
    					else System.arraycopy(this.bondsRVDataRaw3D[i][j], 0, this.bondsRVDataRawEffDV3D[i][idx++], 0, this.bondsRVDataRaw3D[0][0].length);
    				} 
    				
    			}
    		}
    		
    		
    		System.out.println("+++ 3D-Restructured:  BOND tx_numSRVRawDates : "+ tx_numBRVRawDates  +" --- tx_numSRVRawRowsEachDate:" + tx_numBRVRawRowsEachDate);
    		System.out.println("+++ 3D-Restructured BondBaukasten:  tab : "+ this.bondsRVDataRaw3D.length  +" --- rows:" + this.bondsRVDataRaw3D[0].length + " --- cols" + this.bondsRVDataRaw3D[0][0].length);
    		System.out.println("+++ 3D-Restructured BondBaukasten RawEffDV3D:  tab : "+ this.bondsRVDataRawEffDV3D.length  +" --- rows:" + this.bondsRVDataRawEffDV3D[0].length + " --- cols" + this.bondsRVDataRawEffDV3D[0][0].length);
    	}
    	
    	
    	private void optimLoadEffectiveData_LoadSwap3D_TranformMatrix(){
    		int tx_numSRVRawDates= optimArrayListUtil.sizeUniqueArr(this.swapsRVJulDateRaw);
    		int tx_numSRVRawRowsEachDate=optimArrayListUtil.numRedundantData(this.swapsRVJulDateRaw, this.swapsRVJulDateRaw[0]);
    		this.swapsRVDataRaw3D=new double[tx_numSRVRawDates ][tx_numSRVRawRowsEachDate][];
    		//for (int i=0; i<tx_numSRVRawDates ; i++ ) {
    		for (int i=0, slcStart=0, slcEnd=(tx_numSRVRawRowsEachDate-1); i<tx_numSRVRawDates ; i++) {
    			
    			this.swapsRVDataRaw3D[i] =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.swapsRVDataRaw, slcStart, slcEnd, -1, -1 );
    			slcStart = slcStart + tx_numSRVRawRowsEachDate;
				slcEnd= slcStart + tx_numSRVRawRowsEachDate -1;
    		}
    		//System.out.println("???>>>+++ 3D-Restructured: SWAP tx_numSRVRawDates : "+ tx_numSRVRawDates  +" --- tx_numSRVRawRowsEachDate:" + tx_numSRVRawRowsEachDate + " //  this.swapsRVDataRaw3D[0][0].length="+  this.swapsRVDataRaw3D[0][0].length +"// this.jix_config_SwapDV_IdxActive.length=" + this.jix_config_SwapDV_IdxActive.length );

    		//--slice effective for active-Stratoms --//
    		this.swapsRVDataRawEffDV3D = new double[tx_numSRVRawDates][this.jix_config_SwapDV_IdxActive.length][this.swapsRVDataRaw3D[0][0].length]; //292, 280, 79 //
    		for (int i=0; i<tx_numSRVRawDates; i++) {
    			for (int j=0, idx=0; j<this.jix_Config_SwapVars.length; j++) {
    						
    				//--copy the data only if stratom is active --//
    				int indexArr = com.stfe.optim.util.optimArrayListUtil.getArrayIndexI(this.jix_config_SwapDV_IdxActive, j);
    				
    				//System.out.println("????? indexArr = " + indexArr + " // idx=" + idx + " //");
    				//System.out.println("??????????? i = " + i + " // j = " + j + "  this.swapsRVDataRaw3D[i][j]: "+ this.swapsRVDataRaw3D[i][j] + " // this.swapsRVDataRawEffDV3D[i][idx]: " + this.swapsRVDataRawEffDV3D[i][idx] + " // Done //" );
    				
    				if ( indexArr > -1 ) {
    					if (indexArr != idx ) System.out.println(" ? Error idxArr not consistenet with counter idx");
    					else {
    						System.arraycopy(this.swapsRVDataRaw3D[i][j], 0, this.swapsRVDataRawEffDV3D[i][idx++], 0, this.swapsRVDataRaw3D[0][0].length);
    					}
    				} //else if (i<1) System.out.print(" ???? 3D-Restructured SwapBaukasten RawEffDV3D not created! Date-point : " + i);
    				
    			}
    		}
    		
    		System.out.println(">>>+++ 3D-Restructured: SWAP tx_numSRVRawDates : "+ tx_numSRVRawDates  +" --- tx_numSRVRawRowsEachDate:" + tx_numSRVRawRowsEachDate);
    		if (this.jix_config_SwapDV_IdxActive.length > 0)
    			System.out.println("+++ 3D-Restructured SwapBaukasten RawEffDV3D:  tab : "+ this.swapsRVDataRawEffDV3D.length  +" --- rows:" + this.swapsRVDataRawEffDV3D[0].length + " --- cols" + this.swapsRVDataRawEffDV3D[0][0].length);
    	}
    	
    	
    	
    	
    	public void optimLoadEffectiveSRVData_TranformMatrix() 	{
    	 	
    		//--SRVDataInit --//
    		optimLoadEffectiveSRVDataInit();

    		//////////////////////////////////////////////////////////
			//--setup Env Transform --//
    		optimLoadEffectiveBondDataSetupEnv_TranformMatrix();
			optimLoadEffectiveSwapDataSetupEnv_TranformMatrix();
			if (this.ruleConf.numBondDecisionVariables > this.ruleConf.numSwapDecisionVariables)
				this.ruleConf.numDecisionVariables=this.ruleConf.numBondDecisionVariables;
			else this.ruleConf.numDecisionVariables=this.ruleConf.numSwapDecisionVariables; 
    		
			System.out.println("STANDARD: numYears: " + this.ruleConf.numYears + " --- this.numDates: " + this.numDates + " --- this.numSRVRawRowsPerDate: " + this.numSRVRawRowsPerDate );
			//System.out.println("TRANSFORMED:  TX_AllBestandRawRows : "+ TX_AllBestandRawRows  +" --- TX_AllBaukastenSRVRawRowsPerDate:" + TX_AllBaukastenSRVRawRowsPerDate);
			//////////////////////////////////////////////////////////

    		
    		//--apply transformation ---//    		
    		//-- private final int TX_AllBaukastenSRVRawRowsPerDate = 1428; 
        	//-- private final int TX_AllBestandRawRows  = 412;         	
    		optimTransformMatrix otm = new optimTransformMatrix();
    		    		
    		/////////////////////////////////////////////////////////
    		System.out.println("*** optimLoadEffectiveSRVData_TranformMatrix :numDecisionVariables " + this.ruleConf.numDecisionVariables + " // numBondDecisionVariables:"+ this.ruleConf.numBondDecisionVariables + " // numSwapDecisionVariables: " + this.ruleConf.numSwapDecisionVariables);
    		//--Bonds-- this.ruleConf.numBondDecisionVariables ---//
    		if (this.ruleConf.numBondDecisionVariables > 0) {  //--numBondDecisionVariables--//
    			
    			optimLoadEffectiveData_LoadBond3D_TranformMatrix();
    		
	    		System.out.println("TRANSFORMED:  RAW-BOND-2DIM: Row=" + this.bondsRVDataRaw.length + " -- RAW-BOND-DIM: Col=" + this.bondsRVDataRaw[0].length );
	    		
	    		this.TransformedBondWeightKey = new double[this.ruleConf.numBondDecisionVariables][this.bondsRVDataRaw3D[0].length ];
	    		double [][][]bondsRVDataTransformed3DAll= otm.genBondDVTransformationMatrix(this.jix_Config_BondVars, this.jix_Config_BondWeights, this.bondsRVDataRaw3D, this.TransformedBondWeightKey );
	    		oxl.genTxtFileFrom2DData(filepath, "DATASET-genBondDVTransformationBondWtMatrix-TMKey.txt", this.TransformedBondWeightKey);
	    		
	    		System.out.println("*** TRANSFORMED: DATASET - TransformedBondWeightKey set up done for BondTXMatKeys!");
	    		System.out.println("*** TRANSFORMED: Raw- bondsRVDataTransformed3DAll Tab:" + bondsRVDataTransformed3DAll.length + " ---rows"+ bondsRVDataTransformed3DAll[0].length + "-- cols"+ bondsRVDataTransformed3DAll[0][0].length);
	    		
	    		
	    		//-- slice the transformed Baukasten to get effective numDateRoaws --//
	    		this.bondsRVDataTransformed3D= new double[this.ruleConf.numDateRows][][];
	    		for (int i=0; i<this.ruleConf.numDateRows; i++) {
	    			this.bondsRVDataTransformed3D[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVDataTransformed3DAll[i], -1, -1, -1, -1 );	
	    		}
				
	    		System.out.println("*** TRANSFORMED: Reduced: bondsRVDataTransformed3D Tab=" + this.bondsRVDataTransformed3D.length + " -- ROW=" + this.bondsRVDataTransformed3D[0].length  + " --- Col: "+ this.bondsRVDataTransformed3D[0][0].length);
	    		
	    		//this.bondsRVEff= new double[this.bondsRVDataTransformed3D.length][][];
	    		
	    		this.bondsRVEff= new double[this.bondsRVDataTransformed3D.length][][];
	    		this.bondsRVAnnuitySensiEff=new double[this.bondsRVDataTransformed3D.length][][];
	    		this.bondsRVPVCleanEff=new double[this.bondsRVDataTransformed3D.length][][];
	    		this.bondsRVDebtCleanEff=new double[this.bondsRVDataTransformed3D.length][][];
	    		for (int i=0; i<bondsRVDataTransformed3D.length; i++) {
		    		this.bondsRVEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(this.bondsRVDataTransformed3D[i], -1, -1, 5, 35 );
		    		this.bondsRVAnnuitySensiEff[i] =  com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVDataTransformed3D[i], -1, -1, 43, 73 );
		    		this.bondsRVPVCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVDataTransformed3D[i], -1, -1, 2, 2 );
		    		this.bondsRVDebtCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(bondsRVDataTransformed3D[i], -1, -1, 1, 1 );
	    		}
	    		
				
				//////////////////////////////////////////////////////////
				// -- RawValues-Bond= Vol-Bond *  BondBaukasten -- //
				this.RawValuesBondTM= otm.genRawvaluesTransformationMatrix("Bond", this.jix_Config_BondVols, this.bondsRVDataRaw3D );
				oxl.genTxtFileFrom2DData(filepath, "RawValuesBondTM-All.txt", this.RawValuesBondTM );
				
				// ---Mehrkosten- Sensi (31 cols H_AL) - bestand Done - PVCleanFullOpt + BestandFullOpt --//
				//--Bestand Bond Swap --//;
				//this.BestandBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, -1, 5, 35 );
				this.BestandBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, 0, this.ruleConf.numDateRows-1, 5, 35 );
				oxl.genTxtFileFrom2DData(filepath, "BestandBond-TM-Eff.txt", this.BestandBondTMEff );	
				System.out.println("TRANSFORMED:  Bestand Bond!");
				
				//--PVClean for Bond and Swap --//
				this.PVCleanBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 2, 2 );
				oxl.genTxtFileFrom2DData(filepath, "PVCleanBond-TM-Eff.txt", this.PVCleanBondTMEff );
				
				//////////////////////////////////////////////////////////
				// ---SwapRisk: AC_AL: Swap-Sensi (10Cols) ----AC(29-3)_AL(38-3)- SwapRisk---SwapSensiRiskOpt-joSwapSensiRiskEff---//
				this.BondRiskSensiTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 26, 35 );
				oxl.genTxtFileFrom2DData(filepath, "BondRiskSensiTMEff.txt", this.BondRiskSensiTMEff );
				
				
				//--FixingRisk: SwapFxRiskOpt-joBondILBSwapFxRiskEff; --- BondFxRisk: AT(46-3)-BC/BD(76-3) = 10; ---- ILDFxRisk: BD-BN = 10; --- SwapFXRisk: BO_BX = 10--// // AT_BX: Swap-PV-Sensi (31Cols)
				this.BondILBSwapFxRiskBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 43, 73 );
				oxl.genTxtFileFrom2DData(filepath, "BondILBSwapFxRiskBondTMEff.txt", this.BondILBSwapFxRiskBondTMEff );
				
				//-- SchuldFullOpt: -Col:D (4-3) --idx=1-- //
				this.SchuldBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 1, 1 );
				oxl.genTxtFileFrom2DData(filepath, "SchuldBondTMEff.txt", this.SchuldBondTMEff );
				//////////////////////////////////////////////////////////
	    		
    		} //--- Bond transformation done ---//
    		
    		
    		
    		/////////////////////////////////////////////////////////
    		//--Swaps-- this.ruleConf.numSwapDecisionVariables --//
    		if (this.ruleConf.numSwapDecisionVariables > 0)  {
    			
    		
    			optimLoadEffectiveData_LoadSwap3D_TranformMatrix(); 
	    		System.out.println("TRANSFORMED:  RAW-SWAP-DIM: Row=" + this.swapsRVDataRaw.length + " -- RAW-SWAP-DIM: Col=" + this.swapsRVDataRaw[0].length );
	    		
	    		//public double[][][] genDVTransfortionMatrix(double[][] varColVector, double[][][]RawBuaKastenRawData )
	    		///this.swapsRVDataTransformed3D= otm.genDVTransformationMatrix(this.jix_Config_SwapVars, this.swapsRVDataRaw3D );
	    		double [][][]swapsRVDataTransformed3DAll= otm.genDVTransformationMatrix(this.jix_Config_SwapVars, this.swapsRVDataRaw3D );
	    		this.swapsRVDataTransformed3D= new double[this.ruleConf.numDateRows][][];
	    		for (int i=0; i<this.ruleConf.numDateRows; i++) {
	    			this.swapsRVDataTransformed3D[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataTransformed3DAll[i], -1, -1, -1, -1 );	
	    		}
	    		
	    		
	    		System.out.println("***TRANSFORMED: swapsRVDataTransformed3D Tab=" + this.swapsRVDataTransformed3D.length + " -- ROW=" + this.swapsRVDataTransformed3D[0].length  + " --- Col: "+ this.swapsRVDataTransformed3D[0][0].length);
	    		
	    		//--- Two years: swapsRVDataTransformed3D: Tab=292  (this.ruleConf.numDateRows) :Row=14:Col=79  ---//
	    		this.swapsRVEff= new double[this.swapsRVDataTransformed3D.length][][];
	    		this.swapsRVAnnuitySensiEff=new double[this.swapsRVDataTransformed3D.length][][];
	    		this.swapsRVPVCleanEff=new double[this.swapsRVDataTransformed3D.length][][];
	    		this.swapsRVDebtCleanEff=new double[this.swapsRVDataTransformed3D.length][][];
	    		for (int i=0; i<this.swapsRVDataTransformed3D.length; i++) {
		    		this.swapsRVEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(this.swapsRVDataTransformed3D[i], -1, -1, 5, 35 );
		    		this.swapsRVAnnuitySensiEff[i] =  com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataTransformed3D[i], -1, -1, 43, 73 );
		    		this.swapsRVPVCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataTransformed3D[i], -1, -1, 2, 2 );
		    		this.swapsRVDebtCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataTransformed3D[i], -1, -1, 1, 1 );
	    		}
	    		
				//////////////////////////////////////////////////////////
				//////////////////////////////////////////////////////////
				// -- RawValues-Swap= Vol-Swap *  SwapBaukasten -- //
				this.RawValuesSwapTM= otm.genRawvaluesTransformationMatrix("Swap",this.jix_Config_SwapVols, this.swapsRVDataRaw3D );
				oxl.genTxtFileFrom2DData(filepath, "RawValuesSwapTM-All.txt", this.RawValuesSwapTM );
				
				// ---Mehrkosten- Sensi (31 cols H_AL) - bestand Done - PVCleanFullOpt + BestandFullOpt --//
				//--Bestand Bond Swap --//;
				this.BestandSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, 0, this.ruleConf.numDateRows-1, 5, 35 );
				oxl.genTxtFileFrom2DData(filepath, "bestandSwap-TM-Eff.txt", this.BestandSwapTMEff );	
				System.out.println("TRANSFORMED:  Bestand Swap!");
				
				//--PVClean for Bond and Swap --//
				this.PVCleanSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 2, 2 );
				oxl.genTxtFileFrom2DData(filepath, "PVCleanSwap-TM-Eff.txt", this.PVCleanSwapTMEff );
				
				//////////////////////////////////////////////////////////
				// ---SwapRisk: AC_AL: Swap-Sensi (10Cols) ----AC(29-3)_AL(38-3)- SwapRisk---SwapSensiRiskOpt-joSwapSensiRiskEff---//
				this.SwapRiskSensiTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 26, 35 );
				oxl.genTxtFileFrom2DData(filepath, "SwapRiskSensiTMEff.txt", this.SwapRiskSensiTMEff );
				
				
				//--FixingRisk: SwapFxRiskOpt-joBondILBSwapFxRiskEff; --- BondFxRisk: AT(46-3)-BC/BD(76-3) = 10; ---- ILDFxRisk: BD-BN = 10; --- SwapFXRisk: BO_BX = 10--// // AT_BX: Swap-PV-Sensi (31Cols)
				this.BondILBSwapFxRiskSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 43, 73 );
				oxl.genTxtFileFrom2DData(filepath, "BondILBSwapFxRiskSwapTMEff.txt", this.BondILBSwapFxRiskSwapTMEff );
				
				//-- SchuldFullOpt: -Col:D (4-3) --idx=1-- //
				this.SchuldSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 1, 1 );
				oxl.genTxtFileFrom2DData(filepath, "SchuldSwapTMEff.txt", this.SchuldSwapTMEff );
				//////////////////////////////////////////////////////////
    	
    		} // -- Swap transformation done --//
		
    		
    		
    		/*
    		//////////////////////////////////////////////////////////
    		//////////////////////////////////////////////////////////
    		// -- RawValues-Bond= Vol-Bond *  BondBaukasten -- //
    		this.RawValuesBondTM= otm.genRawvaluesTransformationMatrix("Bond", this.jix_Config_BondVols, this.bondsRVDataRaw3D );
    		oxl.genTxtFileFrom2DData(filepath, "RawValuesBondTM-All.txt", this.RawValuesBondTM );
    		// -- RawValues-Swap= Vol-Swap *  SwapBaukasten -- //
    		this.RawValuesSwapTM= otm.genRawvaluesTransformationMatrix("Swap",this.jix_Config_SwapVols, this.swapsRVDataRaw3D );
    		oxl.genTxtFileFrom2DData(filepath, "RawValuesSwapTM-All.txt", this.RawValuesSwapTM );
    		    		
    		// ---Mehrkosten- Sensi (31 cols H_AL) - bestand Done - PVCleanFullOpt + BestandFullOpt --//
    		//--Bestand Bond Swap --//;
    		//this.BestandBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, -1, 5, 35 );
    		this.BestandBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, 0, this.ruleConf.numDateRows-1, 5, 35 );
			oxl.genTxtFileFrom2DData(filepath, "BestandBond-TM-Eff.txt", this.BestandBondTMEff );	
    		System.out.println("TRANSFORMED:  Bestand Bond!");
    		this.BestandSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, 0, this.ruleConf.numDateRows-1, 5, 35 );
			oxl.genTxtFileFrom2DData(filepath, "bestandSwap-TM-Eff.txt", this.BestandSwapTMEff );	
    		System.out.println("TRANSFORMED:  Bestand Swap!");
    		
			
			//--PVClean for Bond and Swap --//
    		this.PVCleanBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 2, 2 );
			oxl.genTxtFileFrom2DData(filepath, "PVCleanBond-TM-Eff.txt", this.PVCleanBondTMEff );
			this.PVCleanSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 2, 2 );
			oxl.genTxtFileFrom2DData(filepath, "PVCleanSwap-TM-Eff.txt", this.PVCleanSwapTMEff );
			
			//////////////////////////////////////////////////////////
			// ---SwapRisk: AC_AL: Swap-Sensi (10Cols) ----AC(29-3)_AL(38-3)- SwapRisk---SwapSensiRiskOpt-joSwapSensiRiskEff---//
			this.BondRiskSensiTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 26, 35 );
			oxl.genTxtFileFrom2DData(filepath, "BondRiskSensiTMEff.txt", this.BondRiskSensiTMEff );
			this.SwapRiskSensiTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 26, 35 );
			oxl.genTxtFileFrom2DData(filepath, "SwapRiskSensiTMEff.txt", this.SwapRiskSensiTMEff );
			
			 
			//--FixingRisk: SwapFxRiskOpt-joBondILBSwapFxRiskEff; --- BondFxRisk: AT(46-3)-BC/BD(76-3) = 10; ---- ILDFxRisk: BD-BN = 10; --- SwapFXRisk: BO_BX = 10--// // AT_BX: Swap-PV-Sensi (31Cols)
			this.BondILBSwapFxRiskBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 43, 73 );
			oxl.genTxtFileFrom2DData(filepath, "BondILBSwapFxRiskBondTMEff.txt", this.BondILBSwapFxRiskBondTMEff );
			this.BondILBSwapFxRiskSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 43, 73 );
			oxl.genTxtFileFrom2DData(filepath, "BondILBSwapFxRiskSwapTMEff.txt", this.BondILBSwapFxRiskSwapTMEff );
						
			
			//-- SchuldFullOpt: -Col:D (4-3) --idx=1-- //
			this.SchuldBondTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesBondTM, -1, this.ruleConf.numDateRows-1, 1, 1 );
			oxl.genTxtFileFrom2DData(filepath, "SchuldBondTMEff.txt", this.SchuldBondTMEff );
			
			this.SchuldSwapTMEff = com.stfe.optim.util.optimSliceArray.slice2DArray(this.RawValuesSwapTM, -1, this.ruleConf.numDateRows-1, 1, 1 );
			oxl.genTxtFileFrom2DData(filepath, "SchuldSwapTMEff.txt", this.SchuldSwapTMEff );
			//////////////////////////////////////////////////////////
			//////////////////////////////////////////////////////////
			*/
			
    	} // -- optimLoadEffectiveSRVData_TranformMatrix done --//
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	
    	
    	
    	
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	public void optimLoadEffectiveSRVData_Standard() 	{
    		//--SRVDataInit --//
    		optimLoadEffectiveSRVDataInit();
    		
			// -- BondsRV Baukasten Effective: data handling --//
			//--Loading the SRV-slice into JOM - 3D//
			// -- BondsRV Should be  Reduced [nYears x numDKF x numEle ] --//
			this.bondsRVEff= new double[this.numDates][numSRVRawRowsPerDate][];			
			for (int i=0, slcStart=0, slcEnd=(numSRVRawRowsPerDate-1); i<this.numDates; i++) {	
				this.bondsRVEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(this.bondsRVDataRaw, slcStart, slcEnd, 5, 35 );
				
				//System.out.println(" DBG- swapsRVDataEff - 1-st Slicing : DateRowCnt" + (i) + ";  Slice Start: " + slcStart + "; SlcEnd: "+ slcEnd + " ; NumDates: " + this.numDates);
				
				slcStart = slcStart + numSRVRawRowsPerDate;
				slcEnd= slcStart + numSRVRawRowsPerDate -1;  // --slcEnd= slcStart + slcEnd;--//
				
				//slcStart = slcStart + numSRVRawRowsPerDate;
				//slcEnd= slcEnd + numSRVRawRowsPerDate;
			}
			
			System.out.println(" bondsRVEff - 3D-Tab: " + this.bondsRVEff.length  + "; 2D-Row_size: " +  this.bondsRVEff[0].length + "; 2D-col-size: " + this.bondsRVEff[0][0].length  
					+ " # 1st data = " + this.bondsRVEff[0][0][0] 	+ " # Last data = " + this.bondsRVEff[this.numDates - 1][numSRVRawRowsPerDate-1][0] );
			
			oxl.genTxtFileFrom2DData(filepath, "Raw-bondsRVEff-H-AL-5-35_first0.txt", this.bondsRVEff[0]);
			oxl.genTxtFileFrom2DData(filepath, "Raw-bondsRVEff-H-AL-5-35_lastX.txt", this.bondsRVEff[this.numDates - 1]);
			

			//-- Annuity Sensi starting at col:AT(44) till col:BX(74) --// -- Should be  Reduced [nYears x numDKF x numEle ] --//
			//Final COl AT(46=>44=>43):BX(76=>74=>73)
			this.bondsRVAnnuitySensiEff=new double[this.numDates][numSRVRawRowsPerDate][];
			for (int i=0, slcStart=0, slcEnd=(numSRVRawRowsPerDate-1); i<this.numDates; i++) {	
				this.bondsRVAnnuitySensiEff[i] =  com.stfe.optim.util.optimSliceArray.slice2DArray(this.bondsRVDataRaw, slcStart, slcEnd, 43, 73 );
				slcStart = slcStart + TX_AllBaukastenSRVRawRowsPerDate;
				slcEnd= slcStart + numSRVRawRowsPerDate -1;
			}
			oxl.genTxtFileFrom2DData(filepath, "Raw-BondsRVAnnuitySensiEff-AT-BX-43-73-first0.txt", this.bondsRVAnnuitySensiEff[0]);
			oxl.genTxtFileFrom2DData(filepath, "Raw-BondsRVAnnuitySensiEff-AT-BX-43-73-lastX.txt", this.bondsRVAnnuitySensiEff[this.numDates - 1]);
			
			
			//--PV of Swaps - PV_CLEAN-Baukasten --// -- Should be  Reduced [nYears x numDKF x numEle ] --//
			//Final COl E(5=>3=>2);
			this.bondsRVPVCleanEff=new double[this.numDates][numSRVRawRowsPerDate][1];
			for (int i=0, slcStart=0, slcEnd=(numSRVRawRowsPerDate-1); i<this.numDates; i++) {
				this.bondsRVPVCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(this.bondsRVDataRaw, slcStart, slcEnd, 2, 2 );
				slcStart = slcStart + numSRVRawRowsPerDate;
				slcEnd= slcStart + numSRVRawRowsPerDate -1;
				
				//slcStart = slcStart + numSRVRawRowsPerDate;
				//slcEnd= slcEnd + numSRVRawRowsPerDate;
			}
			oxl.genTxtFileFrom2DData(filepath, "Raw-BondsRVPVCleanEff-E-2-first0.txt", this.bondsRVPVCleanEff[0]);
			oxl.genTxtFileFrom2DData(filepath, "Raw-BondsRVPVCleanEff-E-2-lastX.txt", this.bondsRVPVCleanEff[this.numDates - 1]);			
			// -- Done For Bonds Baukasten -- //
			
			
			
			// -- SwapRV Should be  Reduced [nYears x numDKF x numEle ] --//
			//--Loading the SRV-slice into JOM - 3D//
			// OLD - SRV SLice Data Description --//
			// Number of unique dates starting from  31.01.2016  till 31.12.2031; total dates=192 ; 
			// There are 448 rows for each date(2016-2031) // 7 elements x 4 quarterly x 16 years = 448  //; Total rows = 192 x 448 = 86016
			//Final COl H(8=>6=>5):AL(38=>36=>35)
			
			// NEW - SRV SLice Data Description --//
			// Number of unique dates starting from  31.01.2016  till 31.03.2058; total dates=292 ; 
			// There are 1428 rows for each date(2016-2058) // 7 elements x 4 quarter x 51 years = 1428  //; Total rows = 292 x 1428 = 86016
			//Final COl H(8=>6=>5):AL(38=>36=>35)		
			this.swapsRVEff= new double[this.numDates][numSRVRawRowsPerDate][];			
			for (int i=0, slcStart=0, slcEnd=(numSRVRawRowsPerDate-1); i<this.numDates; i++) {	
				this.swapsRVEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(this.swapsRVDataRaw, slcStart, slcEnd, 5, 35 );
				
				//System.out.println(" DBG- swapsRVDataEff - 1-st Slicing : DateRowCnt" + (i) + ";  Slice Start: " + slcStart + "; SlcEnd: "+ slcEnd + " ; NumDates: " + this.numDates);
				
				slcStart = slcStart + numSRVRawRowsPerDate;
				slcEnd= slcStart + numSRVRawRowsPerDate -1;  // --slcEnd= slcStart + slcEnd;--//
				
				//slcStart = slcStart + numSRVRawRowsPerDate;
				//slcEnd= slcEnd + numSRVRawRowsPerDate;
			}
			
			System.out.println(" swapsRVDataEff - 3D-Tab: " + this.swapsRVEff.length  + "; 2D-Row_size: " +  this.swapsRVEff[0].length + "; 2D-col-size: " + this.swapsRVEff[0][0].length  
					+ " # 1st data = " + this.swapsRVEff[0][0][0] 	+ " # Last data = " + this.swapsRVEff[this.numDates - 1][numSRVRawRowsPerDate-1][0] );
			
			oxl.genTxtFileFrom2DData(filepath, "Raw-SwapsRVDataEff-H-AL-5-35_first0.txt", this.swapsRVEff[0]);
			oxl.genTxtFileFrom2DData(filepath, "Raw-SwapsRVDataEff-H-AL-5-35_lastX.txt", this.swapsRVEff[this.numDates - 1]);
			
			
			//-- Annuity Sensi starting at col:AT(44) till col:BX(74) --// -- Should be  Reduced [nYears x numDKF x numEle ] --//
			//Final COl AT(46=>44=>43):BX(76=>74=>73)
			this.swapsRVAnnuitySensiEff=new double[this.numDates][numSRVRawRowsPerDate][];
			for (int i=0, slcStart=0, slcEnd=(numSRVRawRowsPerDate-1); i<this.numDates; i++) {	
				this.swapsRVAnnuitySensiEff[i] =  com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataRaw, slcStart, slcEnd, 43, 73 );
				slcStart = slcStart + TX_AllBaukastenSRVRawRowsPerDate;
				slcEnd= slcStart + numSRVRawRowsPerDate -1;
				
				//slcStart = slcStart + numSRVRawRowsPerDate;
				//slcEnd= slcEnd + numSRVRawRowsPerDate;
			}
			oxl.genTxtFileFrom2DData(filepath, "Raw-SwapsRVAnnuitySensiEff-AT-BX-43-73-first0.txt", this.swapsRVAnnuitySensiEff[0]);
			oxl.genTxtFileFrom2DData(filepath, "Raw-SwapsRVAnnuitySensiEff-AT-BX-43-73-lastX.txt", this.swapsRVAnnuitySensiEff[this.numDates - 1]);
			
			
			//--PV of Swaps - PV_CLEAN-Baukasten --// -- Should be  Reduced [nYears x numDKF x numEle ] --//
			//Final COl E(5=>3=>2);
			this.swapsRVPVCleanEff=new double[this.numDates][numSRVRawRowsPerDate][1];
			for (int i=0, slcStart=0, slcEnd=(numSRVRawRowsPerDate-1); i<this.numDates; i++) {
				this.swapsRVPVCleanEff[i] = com.stfe.optim.util.optimSliceArray.slice2DArray(swapsRVDataRaw, slcStart, slcEnd, 2, 2 );
				slcStart = slcStart + TX_AllBaukastenSRVRawRowsPerDate;
				slcEnd= slcStart + numSRVRawRowsPerDate -1;
				
				//slcStart = slcStart + numSRVRawRowsPerDate;
				//slcEnd= slcEnd + numSRVRawRowsPerDate;
			}
			oxl.genTxtFileFrom2DData(filepath, "Raw-SwapsRVPVCleanEff-E-2-first0.txt", this.swapsRVPVCleanEff[0]);
			oxl.genTxtFileFrom2DData(filepath, "Raw-SwapsRVPVCleanEff-E-2-lastX.txt", this.swapsRVPVCleanEff[this.numDates - 1]);			
			// -- Done For swap Baukasten -- //
			
			return;
    	}
    	
    	
    	
    	
    	public void optimLoadXLconfigJava() {
    		
    		//--- File:OptiConfi_from_DKF_Opti_v1.24_Bonds ; Woksheet: JavaInput ---//
    		//-- EmplanVorganbe: Year:B4:AZ4;NKB:B5:AZ5;WishedChangesinEB:B6:AZ6;NteTilgung:B7:AZ7;(NUM); Vector  size (1 x 52) ---//
			
    		double [][]jix_EV_Data_Year_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_EV_Data_Year",filepath);   
			double [][]jix_EV_Data_NKB_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_EV_Data_NKB",filepath);   
			double [][]jix_EV_Data_ChangedEB_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_EV_Data_ChangedEB",filepath);   
			double [][]jix_EV_Data_NetTilgung_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_EV_Data_NetTilgung",filepath);   
			
			
			//this.jix_EV_Data_Year = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_Year_Raw, 0, 0, -1, (this.numYears-1) );
			//this.jix_EV_Data_NKB = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_NKB_Raw, 0, 0, -1, (this.numYears-1) );
			//this.jix_EV_Data_ChangedEB = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_ChangedEB_Raw, 0, 0, -1, (this.numYears-1) );
			//this.jix_EV_Data_NetTilgung = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_NetTilgung_Raw, 0, 0, -1, (this.numYears-1) );
			
			this.jix_EV_Data_Year = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_Year_Raw, 0, 0, -1, -1 );
			this.jix_EV_Data_NKB = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_NKB_Raw, 0, 0, -1, -1 );
			this.jix_EV_Data_ChangedEB = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_ChangedEB_Raw, 0, 0, -1, -1 );
			this.jix_EV_Data_NetTilgung = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_EV_Data_NetTilgung_Raw, 0, 0, -1, -1 );
			
			boolean devdebug = true;
			if (devdebug) {
				String[][] jix_Config_EV_Data = new String[4][this.jix_EV_Data_Year[0].length];
				for (int i=0; i<this.jix_EV_Data_Year[0].length; i++ ) {
					jix_Config_EV_Data[0][i] = String.valueOf(this.jix_EV_Data_Year[0][i]);
					jix_Config_EV_Data[1][i] = String.valueOf(this.jix_EV_Data_NKB[0][i]);
					jix_Config_EV_Data[2][i] = String.valueOf(this.jix_EV_Data_ChangedEB [0][i]);
					jix_Config_EV_Data[3][i] = String.valueOf(this.jix_EV_Data_NetTilgung[0][i]);
				}
				oxl.genTxtFileFrom2DStringData(filepath, "jix_XLConfigJava_EVData_Eff.txt", jix_Config_EV_Data);
			}
			
			//-- Swaps Vol:Var - A16:A1443(String);Vol-Vars:E/F16:E/F1443(NUM); --//
			String[][] jix_Config_SwapProgramType_Raw = (String[][])restore.deserializeBeanAsString("jix_Config_SwapProgramType",filepath);  
			double[][] jix_Config_SwapVols_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_Config_SwapVols",filepath);   
			double[][] jix_Config_SwapVars_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_Config_SwapVars",filepath);
			
			// Bonds Vol:Var - H16:H1443(String);Vol-Vars:M/N16:M/N1443(NUM); --//
			String[][] jix_Config_BondProgramType_Raw = (String[][])restore.deserializeBeanAsString("jix_Config_BondProgramType",filepath);  
			double[][] jix_Config_BondVols_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_Config_BondVols",filepath);   
			double[][] jix_Config_BondVars_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_Config_BondVars",filepath);   
			double[][] jix_Config_BondWeights_Raw = (double[][])restore.deserializeBeanAsPdouble("jix_Config_BondWeights",filepath);
		
			//--- Do not reduce the vector jix conataining vol, vars of Bond and Swaps ---// //Redo with (this.numSRVRawRowsPerDate-1); (this.numYears-1)
			this.jix_Config_SwapProgramType = com.stfe.optim.util.optimSliceArray.slice2DArrayString(jix_Config_SwapProgramType_Raw, 0, -1, -1, -1 );
			this.jix_Config_SwapVols = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_Config_SwapVols_Raw, 0, -1, -1, -1 );
			this.jix_Config_SwapVars = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_Config_SwapVars_Raw, 0, -1, -1, -1 );
		
			this.jix_Config_BondProgramType = com.stfe.optim.util.optimSliceArray.slice2DArrayString(jix_Config_BondProgramType_Raw, 0, -1, -1, -1 );
			this.jix_Config_BondVols = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_Config_BondVols_Raw, 0, -1, -1, -1 );
			this.jix_Config_BondVars = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_Config_BondVars_Raw, 0, -1, -1, -1 );
			this.jix_Config_BondWeights = com.stfe.optim.util.optimSliceArray.slice2DArray(jix_Config_BondWeights_Raw, 0, -1, -1, -1 );
			
			
			//-- Load the index of Active Stratoms if DV (vars) is non-zero--//
			//--Count the active SwapDV & load the active index --//
			int numActiveSwapDV = 0;
			int[] idxActiveSwapDV = new int[this.jix_Config_SwapVars.length];
			for (int i=0, j=0; i<this.jix_Config_SwapVars.length; i++) {
		    	if (this.jix_Config_SwapVars[i][0] != 0) { 
		    		numActiveSwapDV++;
		    		idxActiveSwapDV[j++] = i;
		    	}
		    }
			this.jix_config_SwapDV_IdxActive= new int[numActiveSwapDV];
			for (int i=0; i< numActiveSwapDV ; i++) {
				this.jix_config_SwapDV_IdxActive[i] = idxActiveSwapDV[i];
			}
			System.out.println("Active Swap Stratoms: " + numActiveSwapDV );
			if (numActiveSwapDV > 0) System.out.println("Active Swap Stratoms: " + " idx: "+ this.jix_config_SwapDV_IdxActive[0] +" / "+ this.jix_config_SwapDV_IdxActive[1] + " / "+ this.jix_config_SwapDV_IdxActive[2]);
					
					
			//--Count the active BondDV & load the active index --//
			int numActiveBondDV = 0; 
			int[] idxActiveBondDV = new int[this.jix_Config_BondVars.length]; 
			for (int i=0, j=0; i<this.jix_Config_BondVars.length; i++) {
		    	if (this.jix_Config_BondVars[i][0] != 0) { 
		    		numActiveBondDV++;
		    		idxActiveBondDV[j++] = i;
		    	}
		    }
			this.jix_config_BondDV_IdxActive= new int[numActiveBondDV];
			for (int i=0; i< numActiveBondDV ; i++) {
				this.jix_config_BondDV_IdxActive[i] = idxActiveBondDV[i];
			}
			System.out.println("Active Bond Stratoms: " + numActiveBondDV );
			if (numActiveBondDV > 0) System.out.println("Active Bond Stratoms: " + " idx[0-2]: "+ this.jix_config_BondDV_IdxActive[0] +" / "+ this.jix_config_BondDV_IdxActive[1] + " / "+ this.jix_config_BondDV_IdxActive[2]);
			if (numActiveBondDV > 0) System.out.println("Active Bond Stratoms: " + " idx[3-5]: "+ this.jix_config_BondDV_IdxActive[3] +" / "+ this.jix_config_BondDV_IdxActive[4] + " / "+ this.jix_config_BondDV_IdxActive[5]);
			// Loaded the activeDV //
			
			
			if (devdebug){
				String[][] jix_Config_Data = new String[this.jix_Config_BondVars.length][8];
				for (int i=0; i<this.jix_Config_BondVols.length; i++ ) {
					jix_Config_Data[i][0] = this.jix_Config_SwapProgramType[i][0];
					jix_Config_Data[i][1] = String.valueOf(this.jix_Config_SwapVols[i][0]);
					jix_Config_Data[i][2] = String.valueOf(this.jix_Config_SwapVars [i][0]);
					jix_Config_Data[i][3] = this.jix_Config_BondProgramType[i][0];
					jix_Config_Data[i][4] = String.valueOf(this.jix_Config_BondVols[i][0]);
					jix_Config_Data[i][5] = String.valueOf(this.jix_Config_BondVars [i][0]);
					jix_Config_Data[i][6] = String.valueOf(this.jix_Config_BondWeights [i][0]);
					if (i<numActiveBondDV) 
						jix_Config_Data[i][7] = " :Idx "+String.valueOf(this.jix_config_BondDV_IdxActive[i]);
				}
				oxl.genTxtFileFrom2DStringData(filepath, "jix_XLConfigJava_Data_Eff.txt", jix_Config_Data);
			}
			
			 return;
    	}
    	
    	
    	
    	
    	//-- Load Effective Data --//
    	public void optimLoadEffectiveData() 	{
    	    		
    		System.out.println(" optimEffectiveDataset: optimLoadEffectiveData : Load Effective Data !");
    		
    		//--set up Env --//
    		if (this.loadDataSetupEnvFlag==false) optimLoadEffectiveDataSetupEnv();
    		int idxEffEndBestandRows = idxEffStartBestandRows +  numEffBestandRows -1; 
    		
    		//--- jix- XL Config java ---//
    		optimLoadXLconfigJava();
    		
    		
    		//--Bestand - RawValues: BM_PlanCurrent replaces the BM_SWAPS_CURRENTQUARTER  --// 
			//--old--// double [][] jBestand = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-rv_BM_SWAPS_PLAN_CURRENTQUARTER","rv_Data_BM_SWAPS_PLAN_CURRENTQUARTER");
			double [][] jBestand = restoreIOBean.restoreABeanFromMeta("Meta-BeanArchSpecs-rv_BM_PLANCURRENT","rvData_BM_PLANCURRENT");
			oxl.genTxtFileFrom2DData(filepath, "jBestand_BMPlanCurrent_All.txt", jBestand);
			
			//double [][]bestand = restore.deserializeBeanAsPdouble("rv_Data_BM_SWAPS_PLAN_CURRENTQUARTER",filepath);
			String [][]jBestandDate = (String[][])restore.deserializeBeanAsString("rvDate_BM_PLANCURRENT",filepath);			
			String [][]jBestandEle = (String[][])restore.deserializeBeanAsString("rvDataPort_BM_PLANCURRENT",filepath); //324 rows
			
			//-- conv date to julian date //
			String[] jBestandStrDate = new String[jBestandDate.length];
			double[] jBestandJulDate = new double[jBestandDate.length]; 
			for (int i=0; i<jBestandDate.length; i++) {	
				jBestandStrDate[i] = jBestandDate[i][0];
				if  ( com.stfe.optim.util.optimConvDateFmt.isDouble(jBestandDate[i][0]) ) {
					jBestandJulDate[i]= Double.parseDouble(jBestandDate[i][0]);
				} else {				
					jBestandJulDate[i]= com.stfe.optim.util.optimConvDateFmt.convertToJulian(jBestandDate[i][0]);
				}
				//System.out.println(" Bestand- dateStr: " + jBestandDate[i][0] + " JulDate: " + jBestandJulDate[i] );				
			}
			
			
			//--Old: Loading the Bestand-slice into JOM - 2D//
			//--Sheet:  324 rows for each date starting from 31.01.2005(should be 31.01.2016)  till 31.12.2031 = total 324 rows --//
			//--31.1.2016 starts at row 134=>133 =>idx=132;;;  (((Row 143=>142=>idx 141 value at AC=-4.97E9))) Slice rows=192
			
			//New -- Rows: from row 2=>1=>idx=0, to 413=>412=>idx=411 : Total rows = 412--// 			
			//Bestand colStart AC:29=>27=>idx 26 ; colSEnd AL:38=>36=>idx 35 -- // slice2DArray(bigArr, rowStart-1, rowEnd-1, colStart-1, colEnd-1 );
			//Final COl H(7=>5):AL(37=>35) 
			//double [][] jBestandEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestand, 0, -1, 5, 35 );
			//-- Besand starts at 1.1.2007, so slice the Bestand from date 31.01.2017 at Row 122->121: Idx->120   --//
			
			//double [][] jBestandEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestand, 120, -1, 5, 35 ); 
			double [][] jBestandEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestand, this.idxEffStartBestandRows, idxEffEndBestandRows, 5, 35 );
			oxl.genTxtFileFrom2DData(filepath, "jBestandEff_BMPlanCurrent_R120_Al_Col5_35_id_412_All.txt", jBestandEff);	
			
			int numBestandDateRows=jBestandEff.length ; // Should be 292 rows starting 31.01.2017 among  all 412 rows // size: [292 x 31]
			this.BestandEff =jBestandEff;
			this.BestandEffJulDate=jBestandJulDate;    	
			this.BestandEffStrDate = jBestandStrDate; 

			System.out.println(" Bestand:numDateRows " + numBestandDateRows);
			System.out.println(" Bestand: size_data: row " +  jBestand.length + "; col-size: " +  jBestand[0].length + "; size_ele: " +  jBestandEle.length  );
			System.out.println(" BestandEff: Row_size: " +  jBestandEff.length + "; col-size: " +  jBestandEff[0].length  + " # 1st data = " + jBestandEff[9][0]
					+ " # 2nd data = " + jBestandEff[9][1] );
			
			// PV-Clean (COl:E-5->3->2) from BMPlanCurrent --//			
			this.PVCleanEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jBestand, this.idxEffStartBestandRows, idxEffEndBestandRows, 2, 2 ); 
			System.out.println(" PVCleanEff: Row_size: " +  this.PVCleanEff.length + "; /Col-size: " +  this.PVCleanEff[0].length  );
			oxl.genTxtFileFrom2DData(filepath, "jBestandEff_BMPlanCurrent_PVCleanEff_id_412_All.txt", this.PVCleanEff);	
			
			
			
			//-- Baukasten - Bonds + Swaps RV Data --//
			if (this.ruleConf.TRANSMAT == true)
				optimLoadEffectiveSRVData_TranformMatrix();
			else 
				optimLoadEffectiveSRVData_Standard();
						
			
			
			//-- PCRP1  : mp_1_PCRP -- numEffAllBestandRows  = 192// - this.numDates
			//double [][] PCRP1= new double[numDateRows][31];
			double [][] PCRP1= new double[this.numDates][31]; 
			double [][] PCRP1Raw= (double[][])restore.deserializeBeanAsPdouble("mp_1_PCRP",filepath);
			double [][] PCRP1RawTranspose = com.stfe.optim.util.optimMatrixOps.transposeMatrix(PCRP1Raw);			
			//double [][] PCRP1RawTranspose = cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose(DoubleMatrix2D PCRP1Raw);
					
			System.out.println(" PCRP1: size_ROW: " +  PCRP1.length + "; size_COL: " +  PCRP1[0].length    );
			
			
			for (int i =0; i<this.numDates; i++)
				PCRP1[i]=PCRP1RawTranspose[0];
			oxl.genTxtFileFrom2DData(filepath, "Marketparameter1_PCRP1.txt", PCRP1); 
			
			
			
			
			/*// -- obsolete outdated -- 
			// -- Zap_PCRP : mpZAp_1_PCRP --//
			double [][] Zap1_PCRP1= new double[numDateRows][31];
			double [][]Zap1_PCRP1Raw = (double[][])restore.deserializeBeanAsPdouble("mpZAp_1_PCRP",filepath);
			double [][] Zap1_PCRP1RawTranspose = com.stfe.optim.util.optimMatrixOps.transposeMatrix(Zap1_PCRP1Raw);
			for (int i =0; i<numDateRows; i++)
				Zap1_PCRP1[i]=Zap1_PCRP1RawTranspose[0];
			//oxl.genTxtFileFrom2DData(filepath, "Marketparameter1_Zap1_PCRP1.txt", Zap1_PCRP1);
			*/
			
	
			//Marktparameter1-5
			double [][]jPCCov_Market1 = (double[][])restore.deserializeBeanAsPdouble("mp_1_PCCov",filepath); // size 31 x 31 ----  
			double [][]jPCRP_Market1 = (double[][])restore.deserializeBeanAsPdouble("mp_1_PCRP",filepath); // size 31	// -- 
			double [][]jFixRiskCov_Market1 = (double[][])restore.deserializeBeanAsPdouble("mp_1_FixingRiskCov_Data",filepath); // 
			
			System.out.println(" jPCCov_Market1: 2D-size-row: " + jPCCov_Market1.length  + "; 2D-Row_size-col: " +  jPCCov_Market1[0].length + "; " );
			System.out.println(" jPCRP_Market1: 2D-size-row: " + jPCRP_Market1.length  + "; 2D-Row_size-col: " +  jPCRP_Market1[0].length + "; " );
			System.out.println(" jFixRiskCov_Market1: 2D-size-row: " + jFixRiskCov_Market1.length  + "; 2D-Row_size-col: " +  jFixRiskCov_Market1[0].length + "; " );
			
			double [][]jPCCov_Market2 = (double[][])restore.deserializeBeanAsPdouble("mp_2_PCCov",filepath); // size 31 x 31 ----  
			double [][]jPCRP_Market2 = (double[][])restore.deserializeBeanAsPdouble("mp_2_PCRP",filepath); // size 31	// -- 
			double [][]jFixRiskCov_Market2 = (double[][])restore.deserializeBeanAsPdouble("mp_2_FixingRiskCov_Data",filepath); // 
			
			double [][]jPCCov_Market3 = (double[][])restore.deserializeBeanAsPdouble("mp_3_PCCov",filepath); // size 31 x 31 ----  
			double [][]jPCRP_Market3 = (double[][])restore.deserializeBeanAsPdouble("mp_3_PCRP",filepath); // size 31	// -- 
			double [][]jFixRiskCov_Market3 = (double[][])restore.deserializeBeanAsPdouble("mp_3_FixingRiskCov_Data",filepath); // 
			
			double [][]jPCCov_Market4 = (double[][])restore.deserializeBeanAsPdouble("mp_4_PCCov",filepath); // size 31 x 31 ----  
			double [][]jPCRP_Market4 = (double[][])restore.deserializeBeanAsPdouble("mp_4_PCRP",filepath); // size 31	// -- 
			double [][]jFixRiskCov_Market4 = (double[][])restore.deserializeBeanAsPdouble("mp_4_FixingRiskCov_Data",filepath); // 
			
			double [][]jPCCov_Market5 = (double[][])restore.deserializeBeanAsPdouble("mp_5_PCCov",filepath); // size 31 x 31 ----  
			double [][]jPCRP_Market5 = (double[][])restore.deserializeBeanAsPdouble("mp_5_PCRP",filepath); // size 31	// -- 
			double [][]jFixRiskCov_Market5 = (double[][])restore.deserializeBeanAsPdouble("mp_5_FixingRiskCov_Data",filepath); // 
			
			this.PCCov_Mkt1=jPCCov_Market1;  
	    	this.PCRP_Mkt1=jPCRP_Market1;  
	    	this.FixRiskCov_Mkt1=jFixRiskCov_Market1;
	    	this.PCCov_Mkt2=jPCCov_Market2;  
	    	this.PCRP_Mkt2=jPCRP_Market2;  
	    	this.FixRiskCov_Mkt2=jFixRiskCov_Market2;
	    	this.PCCov_Mkt3=jPCCov_Market3;  
	    	this.PCRP_Mkt3=jPCRP_Market3;  
	    	this.FixRiskCov_Mkt3=jFixRiskCov_Market3;
	    	this.PCCov_Mkt4=jPCCov_Market4;  
	    	this.PCRP_Mkt4=jPCRP_Market4;  
	    	this.FixRiskCov_Mkt4=jFixRiskCov_Market4;
			this.PCCov_Mkt5=jPCCov_Market5;  
	    	this.PCRP_Mkt5=jPCRP_Market5;  
	    	this.FixRiskCov_Mkt5=jFixRiskCov_Market5;
			
			
			// -- ParametersAndMarketValues -- //									
			//--Parameter-specs--//			
			double [][]jPCCov = (double[][])restore.deserializeBeanAsPdouble("p_pmv_PCCov",filepath); // size 31 x 31
			double [][]jPCRP = (double[][])restore.deserializeBeanAsPdouble("p_pmv_PCRP",filepath); // size 31
			double [][]jFixRiskCov = (double[][])restore.deserializeBeanAsPdouble("p_pmv_FixingRiskCov_Data",filepath);
			System.out.println(" jPCRP: 2D-size-row: " + jPCRP.length  + "; 2D-Row_size-col: " +  jPCRP[0].length + "; " );
	
			
	    	this.FixRiskCovEff= jFixRiskCov;
	    	this.PCCovEff=jPCCov; 
	    	this.PCRPEff=jPCRP;
			 
	    	
			//Market
			//--Bean-name as specified in market-specs--//			
			String [][]jRefPCSensiDate = (String[][])restore.deserializeBeanAsString("m_pmv_Date_REF_PC_SENSI",filepath);			
			double [][]jRefPCSensi = (double[][])restore.deserializeBeanAsPdouble("m_pmv_REF_PC_SENSI",filepath);
			double[] jRefPCSensiDateNum = new double[jRefPCSensiDate.length];
			for (int i=0; i<jRefPCSensiDate.length; i++) {								
				if  ( com.stfe.optim.util.optimConvDateFmt.isDouble(jRefPCSensiDate[i][0]) ) {
					jRefPCSensiDateNum[i]= Double.parseDouble(jRefPCSensiDate[i][0]);
				} else {				
					jRefPCSensiDateNum[i]= com.stfe.optim.util.optimConvDateFmt.convertToJulian(jRefPCSensiDate[i][0]);
				}
				//System.out.println(" jRefPCSensiDate- dateStr: " + jRefPCSensiDate[i][0] + " JulDate: " + jRefPCSensiDateNum[i] );				
			}
			//--Loading the MarketValues-slice into JOM - 2D//
			//--Sheet:  324 rows for each date starting from 31.01.2005(should be 31.01.2016)  till 31.12.2031 = total 324 rows --//
			//--31.1.2016 starts at row 134=>133 =>idx=132;;;  (((Row 143=>142=>idx 141 value at AC=-4.97E9))) Slice rows=192
			//jRefPCSensi all cols required colStart -1 ; colSEnd -1 -- // slice2DArray(bigArr, rowStart-1, rowEnd-1, colStart-1, colEnd-1 );
			//double [][] jRefPCSensiEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jRefPCSensi, 132, -1, -1, -1 );	
			//double [][] jRefPCSensiEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jRefPCSensi, 120, -1, -1, -1 );	//-- 120 --//
			double [][] jRefPCSensiEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jRefPCSensi, this.idxEffStartBestandRows, idxEffEndBestandRows, -1, -1 );	//-- 120 --//
						
			if (this.numEffBestandRows  == jRefPCSensiEff.length) System.out.println(" jRefPCSensiEff consistent with Bestand data!");
			
			this.RefPCSensiEff=jRefPCSensiEff;
			
			System.out.println(" jRefPCSensiEff: 2D-size-row: " + jRefPCSensiEff.length  + "; 2D-Row_size-col: " +  jRefPCSensiEff[0].length + "; " );			
			oxl.genTxtFileFrom2DData(filepath, "jRefPCSensiEff-122-413.txt", jRefPCSensiEff);
								
			//op.setInputParameter("RefPCSensi", new DoubleMatrixND(jRefPCSensiEff)  );
			
			
			
			//-- Schuld variables ----SchuldVec--//
			double [][]jSchuldRaw = (double[][])restore.deserializeBeanAsPdouble("rvShuld_BM_PLANCURRENT_DebtClean",filepath); // size 412 x 1
			
			//double[][] jSchuldEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jSchuldRaw, 132, -1, -1, -1 ); 
			double[][] jSchuldEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jSchuldRaw, this.idxEffStartBestandRows, idxEffEndBestandRows, -1, -1 );
			
			//-- remove the 0 from Debt clean --//
			for (int i=0; i<jSchuldEff.length; i++) {				
				// get the previous row-data for schuld //
				if (jSchuldEff[i][0]==0.0) {
						if (i==0) jSchuldEff[0][0]=1.0934299208731251E12;
						else jSchuldEff[i][0]=jSchuldEff[i-1][0]; // else --//if (jSchuldEff[i][0]==0.0) jSchuldEff[i][0]=1.11E+12;
				}
			}
			
			if (this.numEffBestandRows  == jSchuldEff.length) System.out.println(" jSchuldEff consistent with Bestand data! RC=292x1");
			this.SchuldEff = jSchuldEff;
			System.out.println(" jSchuldEff: 2D-size-row: " + jSchuldEff.length  + "; 2D-Row_size-col: " +  jSchuldEff[0].length );			
			//System.out.println(" jSchuldEff: Data-1 = "	+  jSchuldEff[0][0]+ ", Data-2 = "	+  jSchuldEff[1][0]+ ", Data-3 = "+  jSchuldEff[2][0] + ", Data-Last = " + jSchuldEff[191][0] );
			oxl.genTxtFileFrom2DData(filepath, "jSchuldEff-122-413.txt", jSchuldEff);
			
			
			double jSchuld = 1.11E+12;
			//op.setInputParameter("SchuldVal", new DoubleMatrixND (new double[][] { {Schuld} }) );
			
			//-- Swap Risk + Swap Fx Risk --//
			double [][]jSwapSensiRiskRaw = (double[][])restore.deserializeBeanAsPdouble("rv_BM_PLANCURRENT_SwapRisk",filepath); //412 x 10
			this.SwapSensiRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jSwapSensiRiskRaw, this.idxEffStartBestandRows, idxEffEndBestandRows, -1, -1 );
			oxl.genTxtFileFrom2DData(filepath, "jSwapSensiRiskEff-122-413.txt", this.SwapSensiRiskEff);
			
			
			//Fixing Risk//
			double [][]jSwapFxRiskRaw = (double[][])restore.deserializeBeanAsPdouble("rv_BM_PLANCURRENT_SwapFxRisk",filepath); //412 x 10
			this.SwapFxRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jSwapFxRiskRaw, this.idxEffStartBestandRows, idxEffEndBestandRows, -1, -1 );
			oxl.genTxtFileFrom2DData(filepath, "jSwapFxRiskEff-122-413.txt", this.SwapFxRiskEff);
			
			double [][]jBondFxRiskRaw = (double[][])restore.deserializeBeanAsPdouble("rv_BM_PLANCURRENT_BondFxRisk",filepath); //412 x 10
			this.BondFxRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jBondFxRiskRaw, this.idxEffStartBestandRows, idxEffEndBestandRows, -1, -1 );
			oxl.genTxtFileFrom2DData(filepath, "jBondFxRiskEff-122-413.txt", this.BondFxRiskEff);
			
			double [][]jILBFxRiskRaw = (double[][])restore.deserializeBeanAsPdouble("rv_BM_PLANCURRENT_ILBFxRisk",filepath); //412 x 10
			this.ILBFxRiskEff = com.stfe.optim.util.optimSliceArray.slice2DArray(jILBFxRiskRaw, this.idxEffStartBestandRows, idxEffEndBestandRows, -1, -1 );
			oxl.genTxtFileFrom2DData(filepath, "jILBFxRiskEff-122-413.txt", this.ILBFxRiskEff);
			
			
			
			//Constraints & defined variables //
			//constraints-variables  -- //10	10	10	0.25	0.25	0.25	0.25 ///
			
			
			double [] upConstraintBase = 	{ 10.00,	50.00,	50.00,	0.25,	0.25,	0.25,	0.50};
			double [] downConstraintBase = { -10.00,	-50.00,	-50.00,	-0.25,	-0.25,	-0.25,	-0.50};
			
			//double [] upConstraintBase = 	{ 10.00,	50.00,	50.00,	0.25,	0.25,	0.25,	0.50};
			//double [] downConstraintBase = { -10.00,	-50.00,	-50.00,	-0.25,	-0.25,	-0.25,	-0.50};
						
			
			//MAx Threshold 2-Ymaturity:  33 - for good solution
			
			//double [] upConstraintBase = 	{ 33.00,	60.00,	60.00,	1.0,	1.0,	1.0,	2};
			//double [] downConstraintBase = { -33.00,	-60.00,	-60.00,	-1.0,	-1.0,	-1.0,	-2};
			
			
			// rule - up/down constraints for a quarter; adjustment based on  addDKFForYears//
			for (int i=0; i<upConstraintBase.length; i++) {
				upConstraintBase[i] *= (this.ruleConf.addDKFForYears * 1);	
				downConstraintBase[i] *= (this.ruleConf.addDKFForYears * 1);
			}
			
			this.upConstraintBase7V = upConstraintBase;			
			this.downConstraintBase7V = downConstraintBase;
			
			//double [][]jUPConst= new double[1][];
			//jUPConst[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray(upConstraintBase, this.numDecisionVariables/7);
			//double [][]jDNConst= new double[1][];
			//jDNConst[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((downConstraintBase), this.numDecisionVariables/7);
			
			
			
			
			double [] constAbsLimitBase = { 60.0, 60, 60, 60, 60,	60, 60 };
			double [] constStkQtrLimit15 = { 15.0, 15, 15, 15, 15,	15,	15 };
			double [] constStk1YearLimit60 = { 60.0, 60, 60, 60, 60,	60,	60 };			
			double [] constStk2YearLimit120 = { 120.0, 120, 120, 120, 120,	120,	120 };
			
			double [] initSolZero = { 0.0, 0, 0, 0, 0,	0,	0 };
			double [] initSolOne = { 1.0, 1, 1, 1, 1,	1,	1 };
			double [] initSolMinusOne = { -1.0, -1, -1, -1, -1,	-1,	-1 };
			
			
			double [][]jConstAbsLimitBase= new double[1][];			
			jConstAbsLimitBase[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((constAbsLimitBase), 4);
			

			//Excel-result-2.June.2017
			this.initDVSolSet = new double[1][84];
			double [] initSolSet = {0.00,	-0.28,	-15.15,	-0.20,	-0.25,	-0.25,	-0.37}; 
			this.initDVSolSet[0] = com.stfe.optim.util.optimSliceArray.replicate1DArrayIntoBigArray((initSolSet), 12);	
			
			
			//-- set up the data fields of the class--//
			this.BestandEff =jBestandEff;
			this.BestandEffJulDate=jBestandJulDate;    	
			this.BestandEffStrDate = jBestandStrDate; 
			

			this.PCRP1Eff= PCRP1;
			this.SchuldEff=jSchuldEff;
	    	this.SchuldVal=jSchuld;   
 		
	    	//this.Zap1_PCRP1Eff= Zap1_PCRP1;
			this.RefPCSensiEff=jRefPCSensiEff;			
			this.FixRiskCovEff = jFixRiskCov ;
			this.PCCovEff= jPCCov;
			this.PCRPEff= jPCRP;


    		this.StockLimitQtr = 15.0;
    		this.StockLimit1Yr = 60.0;
    		this.StockLimit2Yr = 120.0;
    		
    		this.StockLimitQtr7V = constStkQtrLimit15;
    		this.StockLimit1Yr7V = constStk1YearLimit60;
    		this.StockLimit2Yr7V = constStk2YearLimit120;
    		
    		
			
			
			double [] jSwapRiskLimitEff = { 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.2};
			this.jSwapRiskLimitEff12V=jSwapRiskLimitEff;
			
			

			this.init0Sol7V=initSolZero;
			this.init1Sol7V=initSolOne;
			this.initN1Sol7V=initSolMinusOne;
			 
			 
			// Excel Test Solution: June.2017
			//double[] initSolSetXL = {0.01d, -4.86d, -10.44d,	-0.20,	-0.25,	-0.25, -0.50};
			//double[] initSolSetXL = {0.001d, -0.001d, -0.001d,	-0.001d,	-0.001d,	-0.001d, -0.001d};

			//double[] initSolSetXL = {5.001d, 10.001d, 5.001d,	2.001d,	2.001d,	2.001d, 2.001d};
			
			//double[] initSolSetXL = {4.64d,	-9.95,	0.69,	0.25,	0.25,	0.25,	0.50 }; // Excel - first DV solution
			
			//--Real Solution:--// 
			//double[] initSolSetXL = {10.000000009879914, 4.314653808662299, -6.745485535594006, 0.20000000998782613, 0.25000000999657335, 0.2500000099972552, 0.5000000099963843 }; // Excel - first DV solution
			
			double[] initSolSetXL = {0.0d,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0 }; // Excel - first DV solution
			
			this.initSolSetXL7V=initSolSetXL; 				
			
			setResultSetJOM();
			
			this.dataFlag=true;
    	} // function optimLoadEffectiveData over.//
    	
    	
    	
    	// --- setup Resultset --- //
    	public double[] initSolSetJOM2Y14;
    	public double[] initSolSetJOM4Y28;
    	public double[] initSolSetJOM6Y42;
    	
    	private void setResultSetJOM() {
    		//-- addDKF=true; addDKFForYears=1; --//
    		//double addDKFForYears=0.25; //-- Quaterly=0.25, Half-yearly=0.5; Yearly=1; Biannual =2 --//
    		
    		//2 years
    		double[] initSolSetJOM2Y14I = {
    				10.00000000992377, -3.1186147687858323, 9.050646914478515, 0.2000000099886093, 0.2500000099956515, 0.2500000099961828, 0.5000000099947702, 
    				-0.0036029721626552433, -9.663862251040795, -50.000000009976795, -0.200000009948572, -0.016982649514220335, -0.005631330725663386, -0.011790638536652864 
    		};
    		this.initSolSetJOM2Y14 = initSolSetJOM2Y14I;
    		    		
    		
    		
    		//4 years
    		double[] initSolSetJOM4Y28I = {
    				10.000000009985703, 17.05491872026764, -21.748429249969732, 0.20000000999792333, 0.2500000099992319, 0.25000000999948946, 0.5000000099995741, 
    				10.000000009988167, -8.332218617448582, 3.5450631728159614, 0.20000000999698297, 0.2500000099988934, 0.25000000999925726, 0.5000000099993755, 
    				10.000000009989746, 0.10334475991590084, -7.1049293082367, 0.20000000999518566, 0.250000009998206, 0.2500000099987837, 0.5000000099989718, 
    				10.000000009976532, -12.651485541176175, -5.380939087914441, 0.20000000998741185, 0.25000000999517225, 0.2500000099967046, 0.5000000099972137
    		};	
    		this.initSolSetJOM4Y28 = initSolSetJOM4Y28I;
    				
    		// 6 years: 
    		double[] initSolSetJOM6Y42I = {
    				10.000000009897025, -3.4322966625190854, -5.743073044927396, 0.2000000099880727, 0.25000000999762706, 0.25000000999825617, 0.5000000099972726, 
    				10.000000009877349, 1.947683389444422, -4.5850374420328475, 0.2000000099879113, 0.25000000999746, 0.25000000999806554, 0.5000000099971319, 
    				10.00000000985761, 2.668969029813101, -6.319806287617731, 0.20000000998820325, 0.250000009997172, 0.2500000099977841, 0.5000000099969074, 
    				10.000000009879914, 4.314653808662299, -6.745485535594006, 0.20000000998782613, 0.25000000999657335, 0.2500000099972552, 0.5000000099963843, 
    				10.000000009915455, 5.981289953008393, -7.5754721600188475, 0.20000000998489156, 0.25000000999503885, 0.25000000999595823, 0.5000000099949347, 
    				10.000000009821493, 5.333178016229622, -7.8257581674448256, 0.20000000996643072, 0.2500000099874965, 0.25000000998970684, 0.5000000099876672
    		};
    		this.initSolSetJOM6Y42 = initSolSetJOM6Y42I;
    		
    	}
    	
    	   	
    	    	
		
		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    