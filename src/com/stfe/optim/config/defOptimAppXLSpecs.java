
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
    public class defOptimAppXLSpecs {
    	
    	// ################### XL Java Config   ##################
    	//ExcelName: OptiConfi_from_DKF_Opti_v1.24_Bonds.xlsx; -- SheetName: EmplanVogaben-->JavaInput; --// 
    	private static final String[][] EmplanVogabenArchSpecs = {
    		{"jix_EV_Data_Year","B","4","AZ","4", "NUM"},    // EmplanVorganbe: Year:B4:AZ4;NKB:B5:AZ5;WishedChangesinEB:B6:AZ6;NteTilgung:B7:AZ7;(NUM); --//
    		{"jix_EV_Data_NKB","B","5","AZ","5", "NUM"},  
    		{"jix_EV_Data_ChangedEB","B","6","AZ","6", "NUM"},
    		{"jix_EV_Data_NetTilgung","B","7","AZ","7", "NUM"}
    	};
    	public static String[][] getEmplanVogabenArchSpecs() {							
    		return EmplanVogabenArchSpecs;
    	}
    	//ExcelName: OptiConfi_from_DKF_Opti_v1.24_Bonds.xlsx; -- SheetName: JavaInput; --//
    	private static final String[][] JavaInputXLArchSpecs = {
    		{"jix_Config_SwapProgramType","A","16","A","1443", "String"}, // Swap:A16:A1443(String);Vol-Vars:E/F16:E/F1443(NUM); --//
    		{"jix_Config_SwapVols","E","16","E","1443", "NUM"},
    		{"jix_Config_SwapVars","F","16","F","1443", "NUM"},
    		{"jix_Config_BondProgramType","H","16","H","1443", "String"}, // Bond:H16:H1443(String);Vol-Vars:M/N16:M/N1443(NUM); --//
    		{"jix_Config_BondVols","M","16","M","1443", "NUM"},
    		{"jix_Config_BondVars","N","16","N","1443", "NUM"},
    		{"jix_Config_BondWeights","O","16","O","1443", "NUM"}
    	};
    	public static String[][] getJavaInputXLArchSpecs() {							
    		return JavaInputXLArchSpecs;
    	}
    	// ################### XL Java Config   ##################
    	
    	
    	
    	// ################### New From Dec,2017 till date ##################
    	//Bonds - Baukasten //
    	private static final String[][] bondsBauKastenRawValuesArchSpecs = {    		
    		{"srv_MetaColVals_BONDS_RawValues","A","1","CD","1", "STRING"}, //Starting from 31.01.2017
    		{"srv_DataPort_BONDS_RawValues","B","2","B","416977", "STRING"},  // till 31.12.2067 row=416977-1(one-header)
    		{"srv_Date_BONDS_RawValues","A","2","A","416977", "DATE"},
    		{"srv_Data_BONDS_RawValues","C","2","CD","416977", "NUM"}  
    	};
    	//public static String[][] getBondsBauKastenRawValuesArchSpecs() {							
    	//	return bondsBauKastenRawValuesArchSpecs;
    	//}
    	
    	//-- Parts - BauKastenRawValuesArchSpecs -- //
    	private static final String[][] bondsBauKastenRawValuesArchSpecs_Part1 = {    	// till 31.12.2067 row=416977-1(one-header)
    		{"srv_MetaColVals_Bk_BONDS_RawValues_Part1","A","1","CD","1", "STRING"}, //Starting from 31.01.2017
    		{"srv_DataPort_Bk_BONDS_RawValues_Part1","B","2","B","75000", "STRING"},  
    		{"srv_Date_Bk_BONDS_RawValues_Part1","A","2","A","75000", "STRING"}, //DATE
    		{"srv_Data_Bk_BONDS_RawValues_Part1","C","2","CC","75000", "NUM"}
    	};
    	public static String[][] getBondsBauKastenRawValuesArchSpecs_Part1() {							
    		return bondsBauKastenRawValuesArchSpecs_Part1;
    	}
    	private static final String[][] bondsBauKastenRawValuesArchSpecs_Part2 = { //Total rows=416977-1(one-header)    		
    		{"srv_DataPort_Bk_BONDS_RawValues_Part2","B","1","B","75000", "STRING"},  
    		{"srv_Date_Bk_BONDS_RawValues_Part2","A","1","A","75000", "DATE"},
    		{"srv_Data_Bk_BONDS_RawValues_Part2","C","1","CC","75000", "NUM"}  
    	};
    	public static String[][] getBondsBauKastenRawValuesArchSpecs_Part2() {							
    		return bondsBauKastenRawValuesArchSpecs_Part2;
    	}
    	private static final String[][] bondsBauKastenRawValuesArchSpecs_Part3 = { //Total rows=416977    	 
    		{"srv_DataPort_Bk_BONDS_RawValues_Part3","B","1","B","75000", "STRING"},  // till 31.12.2067 row=416977-1(one-header)
    		{"srv_Date_Bk_BONDS_RawValues_Part3","A","1","A","75000", "DATE"},
    		{"srv_Data_Bk_BONDS_RawValues_Part3","C","1","CC","75000", "NUM"}  
    	};
    	public static String[][] getBondsBauKastenRawValuesArchSpecs_Part3() {							
    		return bondsBauKastenRawValuesArchSpecs_Part3;
    	}
    	private static final String[][] bondsBauKastenRawValuesArchSpecs_Part4 = { //Total rows=416977-1(one-header)    	 
    		{"srv_DataPort_Bk_BONDS_RawValues_Part4","B","1","B","75000", "STRING"},  // till 31.12.2067 row=416977-1(one-header)
    		{"srv_Date_Bk_BONDS_RawValues_Part4","A","1","A","75000", "DATE"},
    		{"srv_Data_Bk_BONDS_RawValues_Part4","C","1","CC","75000", "NUM"}  
    	};
    	public static String[][] getBondsBauKastenRawValuesArchSpecs_Part4() {							
    		return bondsBauKastenRawValuesArchSpecs_Part4;
    	}
    	
    	private static final String[][] bondsBauKastenRawValuesArchSpecs_Part5 = { //Total rows=416977-1(one-header)    	 
    		{"srv_DataPort_Bk_BONDS_RawValues_Part5","B","1","B","60000", "STRING"},  // till 31.12.2067 row=416977-1(one-header)
    		{"srv_Date_Bk_BONDS_RawValues_Part5","A","1","A","60000", "DATE"},
    		{"srv_Data_Bk_BONDS_RawValues_Part5","C","1","CC","60000", "NUM"}  
    	};
    	public static String[][] getBondsBauKastenRawValuesArchSpecs_Part5() {							
    		return bondsBauKastenRawValuesArchSpecs_Part5;
    	}
    	private static final String[][] bondsBauKastenRawValuesArchSpecs_Part6 = { //Total rows=416977-1(one-header)    	 
    		{"srv_DataPort_Bk_BONDS_RawValues_Part6","B","1","B","56977", "STRING"},  // till 31.12.2067 row=416977-1(one-header)
    		{"srv_Date_Bk_BONDS_RawValues_Part6","A","1","A","56977", "DATE"},
    		{"srv_Data_Bk_BONDS_RawValues_Part6","C","1","CC","56977", "NUM"}  
    	};
    	public static String[][] getBondsBauKastenRawValuesArchSpecs_Part6() {							
    		return bondsBauKastenRawValuesArchSpecs_Part6;
    	}
    	
    	// ###################################################################
    	
    	
    	
    	
    	// ################### New From Aug,2017 till date ##################
    	//1- Baukasten : Swap_RawValues: Exact mapping of sheet //
    	private static final String[][] bauKastenRawValuesArchSpecs = {    		
    		{"srv_MetaColVals_SWAPS_RawValues","A","1","CD","1", "STRING"}, //Starting from 31.01.2017-1(one-header)
    		{"srv_DataPort_SWAPS_RawValues","B","2","B","416977", "STRING"},  // till 31.12.2067 row=416977-1(one-header)
    		{"srv_Date_SWAPS_RawValues","A","2","A","416977", "DATE"},
    		{"srv_Data_SWAPS_RawValues","C","2","CD","416977", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs() {							
    		return bauKastenRawValuesArchSpecs;
    	}
    	
    	//-- Parts - BauKastenRawValuesArchSpecs -- //
    	private static final String[][] bauKastenRawValuesArchSpecs_Part1 = {    	// till 31.12.2067 row=416977-1(one-header)
    		{"srv_MetaColVals_Bk_SWAPS_RawValues_Part1","A","1","CD","1", "STRING"}, //Starting from 31.01.2017
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part1","B","2","B","75000", "STRING"},  //Total rows=75000/416977  (XL-ROWS:1-75000 = 75000)  
    		{"srv_Date_Bk_SWAPS_RawValues_Part1","A","2","A","75000", "STRING"}, //DATE
    		{"srv_Data_Bk_SWAPS_RawValues_Part1","C","2","CC","75000", "NUM"}  
    		//{"srv_Data_Bk_SWAPS_RawValues_Part1","C","2","C","50000", "NUM"}
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part1() {							
    		return bauKastenRawValuesArchSpecs_Part1;
    	}
    	private static final String[][] bauKastenRawValuesArchSpecs_Part2 = { //Total rows=75000/416977  (XL-ROWS:75001-150000 = 75000)  	 		
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part2","B","1","B","75000", "STRING"},  
    		{"srv_Date_Bk_SWAPS_RawValues_Part2","A","1","A","75000", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part2","C","1","CC","75000", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part2() {							
    		return bauKastenRawValuesArchSpecs_Part2;
    	}
    	private static final String[][] bauKastenRawValuesArchSpecs_Part3 = { //Total rows=75000/416977  (XL-ROWS:150001-225000 = 75000)  	 
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part3","B","1","B","75000", "STRING"},  // till 31.12.2067 row=416977
    		{"srv_Date_Bk_SWAPS_RawValues_Part3","A","1","A","75000", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part3","C","1","CC","75000", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part3() {							
    		return bauKastenRawValuesArchSpecs_Part3;
    	}
    	private static final String[][] bauKastenRawValuesArchSpecs_Part4 = { //Total rows=75000/416977  (XL-ROWS:225001-300000 = 75000)  
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part4","B","1","B","75000", "STRING"},  // till 31.12.2067 row=416977
    		{"srv_Date_Bk_SWAPS_RawValues_Part4","A","1","A","75000", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part4","C","1","CC","75000", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part4() {							
    		return bauKastenRawValuesArchSpecs_Part4;
    	}
    	
    	private static final String[][] bauKastenRawValuesArchSpecs_Part5 = { //Total rows=60000/416977  (XL-ROWS:300001-360000 = 60000)    	 
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part5","B","1","B","60000", "STRING"},  // till 31.12.2067 row=416977
    		{"srv_Date_Bk_SWAPS_RawValues_Part5","A","1","A","60000", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part5","C","1","CC","60000", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part5() {							
    		return bauKastenRawValuesArchSpecs_Part5;
    	}
    	private static final String[][] bauKastenRawValuesArchSpecs_Part6 = { //Total rows=56977/416977  (XL-ROWS:360001-416977 = 56977)    	 
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part6","B","1","B","56977", "STRING"},  // till 31.12.2067 row=416977
    		{"srv_Date_Bk_SWAPS_RawValues_Part6","A","1","A","56977", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part6","C","1","CC","56977", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part6() {							
    		return bauKastenRawValuesArchSpecs_Part6;
    	}
    	
    	
    	/*
    	private static final String[][] bauKastenRawValuesArchSpecs_Part7 = { //Total rows=416977    	 
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part7","B","1","B","50000", "STRING"},  // till 31.12.2067 row=416977
    		{"srv_Date_Bk_SWAPS_RawValues_Part7","A","1","A","50000", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part7","C","1","CC","50000", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part7() {							
    		return bauKastenRawValuesArchSpecs_Part7;
    	}
    	private static final String[][] bauKastenRawValuesArchSpecs_Part8 = { //Total rows=416977    	 
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part8","B","1","B","50000", "STRING"},  // till 31.12.2067 row=416977
    		{"srv_Date_Bk_SWAPS_RawValues_Part8","A","1","A","50000", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part8","C","1","CC","50000", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part8() {							
    		return bauKastenRawValuesArchSpecs_Part8;
    	}
    	
    	private static final String[][] bauKastenRawValuesArchSpecs_Part9 = { //Total rows=416977    	 
    		{"srv_DataPort_Bk_SWAPS_RawValues_Part9","B","1","B","16977", "STRING"},  // till 31.12.2067 row=416977
    		{"srv_Date_Bk_SWAPS_RawValues_Part9","A","1","A","16977", "DATE"},
    		{"srv_Data_Bk_SWAPS_RawValues_Part9","C","1","CC","16977", "NUM"}  
    	};
    	public static String[][] getBauKastenRawValuesArchSpecs_Part9() {							
    		return bauKastenRawValuesArchSpecs_Part9;
    	}
    	*/
    	
    	
    	//2- ################### RawValues ################## 
    	//0- ################### BM_SWAPS_PLAN ##################
    	//--Excel: RawValues.xlsx--//
    	//Combined ArchSpec    	-- BM_PLANCURRENT -- //
    	private static final String[][] rawValuesArchSpecs = {    		
    		{"rvMetaColVals","A","1","CD","1", "STRING"}, // AU -> CD
    		{"rvDataPort","B","2","B","413", "String"},   //315 -->413
    		{"rvData","C","2","CC","413", "NUM"},  // AU-> CC  		
    		{"rvDate","A","2","A","413", "Date"}
    	};
    	public static String[][] getRawValuesArchSpecs() {							
    		return rawValuesArchSpecs;
    	} 
    	private static final String[][] rawValues_BM_PLANCURRENT = {    		
    		{"rvMetaColVals_BM_PLANCURRENT","A","1","CD","1", "STRING"}, // AU -> CD
    		{"rvDataPort_BM_PLANCURRENT","B","2","B","413", "String"},   //315 -->413
    		{"rvData_BM_PLANCURRENT","C","2","CC","413", "NUM"},  // AU-> CC  		
    		{"rvDate_BM_PLANCURRENT","A","2","A","413", "Date"}
    	};
    	public static String[][] getArchSpecs_RV_BM_PLANCURRENT() {							
    		return rawValues_BM_PLANCURRENT;
    	} 
    	
    	//Bestand: BM_PLANCURRENT_DEBT_CLEAN // -- IMP-OptV1 --    	
    	private static final String[][] rawValues_BM_PLANCURRENT_DC_Shuld = {
    		{"rvShuld_BM_PLANCURRENT_DebtClean","D","2","D","413", "NUM"}  
    	};
    	public static String[][] getArchSpecs_rawValues_BM_PLANCURRENT_DC_Schuld() {							
    		return rawValues_BM_PLANCURRENT_DC_Shuld;
    	} 
    	//-- Bond + ILB + Swap Fixing Risk --//    	
    	// Bond Fixing Risk
    	private static final String[][] rawValues_BM_PLANCURRENT_BondFxRisk = {
    		{"rv_BM_PLANCURRENT_BondFxRisk","AT","2","BC","413", "NUM"}  
    	};
    	public static String[][] getArchSpecs_rawValues_BM_PLANCURRENT_BondFXRisk() {							
    		return rawValues_BM_PLANCURRENT_BondFxRisk;
    	}
    	// ILB Fixing Risk
    	private static final String[][] rawValues_BM_PLANCURRENT_ILBFxRisk = {
    		{"rv_BM_PLANCURRENT_ILBFxRisk","BD","2","BN","413", "NUM"}	//-- 11cols BD-BN--//  
    	};
    	public static String[][] getArchSpecs_rawValues_BM_PLANCURRENT_ILBFXRisk() {							
    		return rawValues_BM_PLANCURRENT_ILBFxRisk;
    	}
    	// Swap Fixing Risk
    	private static final String[][] rawValues_BM_PLANCURRENT_SwapFxRisk = {
    		{"rv_BM_PLANCURRENT_SwapFxRisk","BO","2","BX","413", "NUM"}  
    	};
    	public static String[][] getArchSpecs_rawValues_BM_PLANCURRENT_SwapFXRisk() {							
    		return rawValues_BM_PLANCURRENT_SwapFxRisk;
    	} 
    	
    	
    	// Swap Risk
    	private static final String[][] rawValues_BM_PLANCURRENT_SwapRisk = {
    		{"rv_BM_PLANCURRENT_SwapRisk","AC","2","AL","413", "NUM"}  
    	};
    	public static String[][] getArchSpecs_rawValues_BM_PLANCURRENT_SwapRisk() {							
    		return rawValues_BM_PLANCURRENT_SwapRisk;
    	}
    	// Plan BM_PLANCURRENT Over //   
    	// --- RawValuesIXL.xlsx - contains swap with bond background; whereas  RawValues_BondsSwaps_IXL.xlsx contains separately. ---//
    	//Combined ArchSpec    	-- BM_PLANCURRENT -- Done //
    	
    	
    	
    	//-- New Parameters + MarketValues --//
    	//4- ################### ParametersAndMarketValues - FAST Generated ##################    	
    	//4.1-Parameters -- ParametersAndMarketValues // -- //FixingRisk-Covariance, Pseudo-PC Covariance, Pseudo-PC RP//
    	private static final String[][] Parameters_PMV = {    
    		{"p_pmv_FixingRiskCov_Meta","A","5","C","5", "STRING"}, 
    		{"p_pmv_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"p_pmv_PCCov","A","10","AE","40", "NUM"},    		
    		{"p_pmv_PCRP","A","42","A","72", "NUM"} 
    	};
    	public static String[][] getArchSpecs_Parameters() {							
    		return Parameters_PMV;
    	} 
    	
    	//4.2-MarketValues -- ParametersAndMarketValues -- PortOpti+MA // -- //Short-Rate, REF_SYNTH Sensis, Perpetual-Sensis //
    	private static final String[][] MarketValues_PMV = {   
    		{"m_pmv_Date_REF_PC_SENSI","A","2","A", "413", "DATE"},  // 325--> 413 	
    		{"m_pmv_Col_REF_PC_SENSI" ,"A","1","FF","1", "STRING"},  //BN->FF    		    		
    		{"m_pmv_REF_PC_SENSI","EA","2","FE","413", "NUM"}		// Ref-PC-Sensi  325--> 413: 	AI:BM->EA:FE 
    	};    	
    	public static String[][] getArchSpecs_MarketValues() {							
    		return MarketValues_PMV;
    	}

    	//4.3- ################### MarktParameterX - PM + MA Generated ##################    	
    	//4.1- MarktParameterX // -- IMP-OptV1 --
    	private static final String[][] Marktparameter1 = {    
    		{"mp_1_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_1_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_1_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_1_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter1() {							
    		return Marktparameter1;
    	}     	    	    	
    	private static final String[][] Marktparameter2 = {    
    		{"mp_2_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_2_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_2_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_2_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter2() {							
    		return Marktparameter2;
    	}
    	private static final String[][] Marktparameter3 = {    
    		{"mp_3_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_3_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_3_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_3_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter3() {							
    		return Marktparameter3;
    	}
    	private static final String[][] Marktparameter4 = {    
    		{"mp_4_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_4_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_4_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_4_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter4() {							
    		return Marktparameter4;
    	}
    	private static final String[][] Marktparameter5 = {    
    		{"mp_5_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_5_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_5_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_5_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter5() {							
    		return Marktparameter5;
    	}    	
    	//4-3 ################### DONE: MarktParameterX ##################
    	
    	
    	//--Old ZApp obsolete --//
    	//5- ################### MarktParameterX - PM + MA Generated ##################    	
    	//5.1- MarktParameterZApX // -- IMP-OptV1 --
    	private static final String[][] MarktparameterZAp1 = {    
    		{"mpZAp_1_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mpZAp_1_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mpZAp_1_PCCov","A","10","AE","40", "NUM"},    		
    		{"mpZAp_1_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_MarktparameterZAp1() {							
    		return MarktparameterZAp1;
    	}     	    	    	
    	private static final String[][] MarktparameterZAp2 = {    
    		{"mpZAp_2_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mpZAp_2_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mpZAp_2_PCCov","A","10","AE","40", "NUM"},    		
    		{"mpZAp_2_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_MarktparameterZAp2() {							
    		return MarktparameterZAp2;
    	}
    	private static final String[][] MarktparameterZAp3 = {    
    		{"mpZAp_3_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mpZAp_3_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mpZAp_3_PCCov","A","10","AE","40", "NUM"},    		
    		{"mpZAp_3_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_MarktparameterZAp3() {							
    		return MarktparameterZAp3;
    	}    	    	
    	//5- ################### DONE: MarktParameterX ##################
    	
    	// ################### Over New From Aug,2017 till date ##################
    	
    	
    	
    	
    	// Old below-- till Aug2017 ---//
    	//  OLD PLAN //
    	//3- ################### RawValues_PLAN ##################
    	//1- ################### BM_SWAPS_PLAN ##################
    	//1-BM_SWAPS_PLAN -- RawValues //
    	private static final String[][] rawValues_BM_SWAPS_PLAN = {    		
    		{"rvMetaColVals_BM_SWAPS_PLAN","A","1","AU","1", "STRING"},  
    		{"rvDataPort_BM_SWAPS_PLAN","B","2","B","325", "String"},
    		{"rvData_BM_SWAPS_PLAN","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_SWAPS_PLAN","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_SWAPS_PLAN() {							
    		return rawValues_BM_SWAPS_PLAN;
    	} 
    	
    	//2-BM_SWAPS_PLAN_CURRENTQUARTER -- RawValues //
    	private static final String[][] rawValues_BM_SWAPS_PLAN_CURRENTQUARTER = {    		
    		{"rv_MetaColVals_BM_SWAPS_PLAN_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rv_DataPort_BM_SWAPS_PLAN_CURRENTQUARTER","B","2","B","325", "String"},
    		{"rv_Data_BM_SWAPS_PLAN_CURRENTQUARTER","C","2","AT","325", "NUM"},    		
    		{"rv_Date_BM_SWAPS_PLAN_CURRENTQUARTER","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_SWAPS_PLAN_CURRENTQUARTER() {							
    		return rawValues_BM_SWAPS_PLAN_CURRENTQUARTER;
    	} 
    	
    	//2-B--rawValues_BM_PLAN_CURRENTQUARTER_F -- RawValues //
    	private static final String[][] rawValues_BM_PLAN_CURRENTQUARTER_F = {    		
    		{"rv_MetaColValsF_BM_PLAN_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rv_DataPortF_BM_PLAN_CURRENTQUARTER","B","2","B","325", "String"},
    		{"rv_DataF_BM_PLAN_CURRENTQUARTER","C","2","AT","325", "NUM"},    		
    		{"rv_DateF_BM_PLAN_CURRENTQUARTER","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER_F() {							
    		return rawValues_BM_PLAN_CURRENTQUARTER_F;
    	} 
    	

    	//3-REF -- RawValues //
    	private static final String[][] rawValues_REF = {    		
    		{"rvMetaColVals_REF","A","1","AU","1", "STRING"},
    		{"rvDataPort_REF","B","2","B","325", "String"},
    		{"rvData_REF","C","2","AT","325", "NUM"},    		
    		{"rvDate_REF","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_REF() {							
    		return rawValues_REF;
    	} 
    		
    	//4-BM -- RawValues //
    	private static final String[][] rawValues_BM = {    		
    		{"rvMetaColVals_BM","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM","B","2","B","325", "String"},
    		{"rvData_BM","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM() {							
    		return rawValues_BM;
    	} 
    	//5-BM_PLAN -- RawValues //
    	private static final String[][] rawValues_BM_PLAN = {    		
    		{"rvMetaColVals_BM_PLAN","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM_PLAN","B","2","B","325", "String"},
    		{"rvData_BM_PLAN","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_PLAN","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_PLAN() {							
    		return rawValues_BM_PLAN;
    	} 
    	
    	//6-BM_PLAN_CURRENTQUARTER -- RawValues //
    	private static final String[][] rawValues_BM_PLAN_CURRENTQUARTER = {    		
    		{"rvMetaColVals_BM_PLAN_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM_PLAN_CURRENTQUARTER","B","2","B","325", "String"},
    		{"rvData_BM_PLAN_CURRENTQUARTER","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_PLAN_CURRENTQUARTER","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER() {							
    		return rawValues_BM_PLAN_CURRENTQUARTER;
    	} 
    	

    	//6-2- Bestand: BM_PLAN_CURRENTQUARTER:DEBT_CLEAN // -- IMP-OptV1 --    	
    	private static final String[][] rawValues_BM_PLAN_CURRENTQUARTER_DC_Shuld = {
    		{"rvShuld_BM_PLAN_CURRENTQUARTER_DebtClean","D","2","D","325", "NUM"}  
    	};
    	public static String[][] getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER_DC_Schuld() {							
    		return rawValues_BM_PLAN_CURRENTQUARTER_DC_Shuld;
    	} 
    	
    	//7-BM_WP -- RawValues //
    	private static final String[][] rawValues_BM_WP = {    		
    		{"rvMetaColVals_BM_WP","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM_WP","B","2","B","325", "String"},
    		{"rvData_BM_WP","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_WP","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_WP() {							
    		return rawValues_BM_WP;
    	} 
    	
    	//8-PMMOD_CURRENTQUARTER -- RawValues // -- IMP-OptV1 --
    	private static final String[][] rawValues_PMMOD_CURRENTQUARTER = {    		
    		{"rv_MetaColVals_PMMOD_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rv_DataPort_PMMOD_CURRENTQUARTER","B","2","B","325", "String"},    		
    		{"rv_Data_PMMOD_CURRENTQUARTER","C","2","AT","325", "NUM"},
    		{"rv_Date_PMMOD_CURRENTQUARTER","A","2","A","325", "DATE"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_PMMOD_CURRENTQUARTER() {							
    		return rawValues_PMMOD_CURRENTQUARTER;
    	} 
    	//  OLD PLAN //
    	//3- ################### RawValues_PLAN ##################    	
    	
    	
    	
    	
    	
    	
    	/*
    	// ################### Old Till July2017 ##################
    	//0- ################### BM_SWAPS_PLAN ##################
    	//--Excel: RawValues.xlsx--//
    	//Combined ArchSpec    	
    	private static final String[][] rawValuesArchSpecs = {    		
    		{"rvMetaColVals","A","1","AU","1", "STRING"},
    		{"rvDataPort","B","2","B","325", "String"},
    		{"rvData","C","2","AT","325", "NUM"},    		
    		{"rvDate","A","2","A","325", "Date"}
    	};
    	public static String[][] getRawValuesArchSpecs() {							
    		return rawValuesArchSpecs;
    	} 

    	//1- ################### BM_SWAPS_PLAN ##################
    	//1-BM_SWAPS_PLAN -- RawValues //
    	private static final String[][] rawValues_BM_SWAPS_PLAN = {    		
    		{"rvMetaColVals_BM_SWAPS_PLAN","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM_SWAPS_PLAN","B","2","B","325", "String"},
    		{"rvData_BM_SWAPS_PLAN","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_SWAPS_PLAN","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_SWAPS_PLAN() {							
    		return rawValues_BM_SWAPS_PLAN;
    	} 
    	
    	//2-BM_SWAPS_PLAN_CURRENTQUARTER -- RawValues //
    	private static final String[][] rawValues_BM_SWAPS_PLAN_CURRENTQUARTER = {    		
    		{"rv_MetaColVals_BM_SWAPS_PLAN_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rv_DataPort_BM_SWAPS_PLAN_CURRENTQUARTER","B","2","B","325", "String"},
    		{"rv_Data_BM_SWAPS_PLAN_CURRENTQUARTER","C","2","AT","325", "NUM"},    		
    		{"rv_Date_BM_SWAPS_PLAN_CURRENTQUARTER","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_SWAPS_PLAN_CURRENTQUARTER() {							
    		return rawValues_BM_SWAPS_PLAN_CURRENTQUARTER;
    	} 
    	
    	//2-B--rawValues_BM_PLAN_CURRENTQUARTER_F -- RawValues //
    	private static final String[][] rawValues_BM_PLAN_CURRENTQUARTER_F = {    		
    		{"rv_MetaColValsF_BM_PLAN_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rv_DataPortF_BM_PLAN_CURRENTQUARTER","B","2","B","325", "String"},
    		{"rv_DataF_BM_PLAN_CURRENTQUARTER","C","2","AT","325", "NUM"},    		
    		{"rv_DateF_BM_PLAN_CURRENTQUARTER","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER_F() {							
    		return rawValues_BM_PLAN_CURRENTQUARTER_F;
    	} 
    	

    	//3-REF -- RawValues //
    	private static final String[][] rawValues_REF = {    		
    		{"rvMetaColVals_REF","A","1","AU","1", "STRING"},
    		{"rvDataPort_REF","B","2","B","325", "String"},
    		{"rvData_REF","C","2","AT","325", "NUM"},    		
    		{"rvDate_REF","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_REF() {							
    		return rawValues_REF;
    	} 
    	
    	//4-BM -- RawValues //
    	private static final String[][] rawValues_BM = {    		
    		{"rvMetaColVals_BM","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM","B","2","B","325", "String"},
    		{"rvData_BM","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM() {							
    		return rawValues_BM;
    	} 
    	//5-BM_PLAN -- RawValues //
    	private static final String[][] rawValues_BM_PLAN = {    		
    		{"rvMetaColVals_BM_PLAN","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM_PLAN","B","2","B","325", "String"},
    		{"rvData_BM_PLAN","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_PLAN","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_PLAN() {							
    		return rawValues_BM_PLAN;
    	} 
    	
    	//6-BM_PLAN_CURRENTQUARTER -- RawValues //
    	private static final String[][] rawValues_BM_PLAN_CURRENTQUARTER = {    		
    		{"rvMetaColVals_BM_PLAN_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM_PLAN_CURRENTQUARTER","B","2","B","325", "String"},
    		{"rvData_BM_PLAN_CURRENTQUARTER","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_PLAN_CURRENTQUARTER","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER() {							
    		return rawValues_BM_PLAN_CURRENTQUARTER;
    	} 
    	

    	//6-2- Bestand: BM_PLAN_CURRENTQUARTER:DEBT_CLEAN // -- IMP-OptV1 --    	
    	private static final String[][] rawValues_BM_PLAN_CURRENTQUARTER_DC_Shuld = {
    		{"rvShuld_BM_PLAN_CURRENTQUARTER_DebtClean","D","2","D","325", "NUM"}  
    	};
    	public static String[][] getArchSpecs_rawValues_BM_PLAN_CURRENTQUARTER_DC_Schuld() {							
    		return rawValues_BM_PLAN_CURRENTQUARTER_DC_Shuld;
    	} 
    	
    	//7-BM_WP -- RawValues //
    	private static final String[][] rawValues_BM_WP = {    		
    		{"rvMetaColVals_BM_WP","A","1","AU","1", "STRING"},
    		{"rvDataPort_BM_WP","B","2","B","325", "String"},
    		{"rvData_BM_WP","C","2","AT","325", "NUM"},    		
    		{"rvDate_BM_WP","A","2","A","325", "Date"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_BM_WP() {							
    		return rawValues_BM_WP;
    	} 
    	
    	//8-PMMOD_CURRENTQUARTER -- RawValues // -- IMP-OptV1 --
    	private static final String[][] rawValues_PMMOD_CURRENTQUARTER = {    		
    		{"rv_MetaColVals_PMMOD_CURRENTQUARTER","A","1","AU","1", "STRING"},
    		{"rv_DataPort_PMMOD_CURRENTQUARTER","B","2","B","325", "String"},    		
    		{"rv_Data_PMMOD_CURRENTQUARTER","C","2","AT","325", "NUM"},
    		{"rv_Date_PMMOD_CURRENTQUARTER","A","2","A","325", "DATE"}
    	};    	
    	public static String[][] getArchSpecs_rawValues_PMMOD_CURRENTQUARTER() {							
    		return rawValues_PMMOD_CURRENTQUARTER;
    	} 

    	//1- ################### DONE:BM_SWAPS_PLAN ##################
    	
    	
    	
    	
    	//2- ################### SWAPS_RawValues ##################
    	//2.1-SWAPS_RawValues -- SWAPS_RawValues // -- IMP-OptV1 --
    	private static final String[][] SWAPS_RawValues = {    		
    		{"srv_MetaColVals_SWAPS_RawValues","A","1","AU","1", "STRING"},
    		{"srv_DataPort_SWAPS_RawValues","B","2","B","86017", "STRING"},
    		{"srv_Date_SWAPS_RawValues","A","2","A","86017", "DATE"},
    		{"srv_Data_SWAPS_RawValues","C","2","AT","86017", "NUM"}  };    	
    	public static String[][] getArchSpecs_SWAPS_RawValues() {							
    		return SWAPS_RawValues;
    	} 
    	
    	//2- ################### DONE:SWAPS_RawValues ##################
    	
    	
    	
    	
    	
    	//3- ################### ParametersAndMarketValues - FAST Generated ##################    	
    	//3.1-Parameters -- ParametersAndMarketValues // -- IMP-OptV1 --
    	private static final String[][] Parameters_PMV = {    
    		{"p_pmv_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"p_pmv_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"p_pmv_PCCov","A","10","AE","40", "NUM"},    		
    		{"p_pmv_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Parameters() {							
    		return Parameters_PMV;
    	} 
    	
    	//3.2-MarketValues -- ParametersAndMarketValues -- PortOpti+MA // -- IMP-OptV1 --
    	private static final String[][] MarketValues_PMV = {   
    		{"m_pmv_Date_REF_PC_SENSI","A","2","A", "325", "DATE"},
    		{"m_pmv_Col_REF_PC_SENSI" ,"A","1","BN","1", "STRING"},    		    		
    		{"m_pmv_REF_PC_SENSI","AI","2","BM","325", "NUM"}
    	};    	
    	public static String[][] getArchSpecs_MarketValues() {							
    		return MarketValues_PMV;
    	}
    	//3- ################### DONE:ParametersAndMarketValues ##################
    	
    	
    	
    	

    	//4- ################### MarktParameterX - PM + MA Generated ##################    	
    	//4.1- MarktParameterX // -- IMP-OptV1 --
    	private static final String[][] Marktparameter1 = {    
    		{"mp_1_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_1_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_1_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_1_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter1() {							
    		return Marktparameter1;
    	}     	    	    	
    	private static final String[][] Marktparameter2 = {    
    		{"mp_2_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_2_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_2_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_2_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter2() {							
    		return Marktparameter2;
    	}
    	private static final String[][] Marktparameter3 = {    
    		{"mp_3_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_3_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_3_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_3_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter3() {							
    		return Marktparameter3;
    	}
    	private static final String[][] Marktparameter4 = {    
    		{"mp_4_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_4_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_4_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_4_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter4() {							
    		return Marktparameter4;
    	}
    	private static final String[][] Marktparameter5 = {    
    		{"mp_5_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mp_5_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mp_5_PCCov","A","10","AE","40", "NUM"},    		
    		{"mp_5_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_Marktparameter5() {							
    		return Marktparameter5;
    	}    	
    	//4- ################### DONE: MarktParameterX ##################
    	
    	
    	//5- ################### MarktParameterX - PM + MA Generated ##################    	
    	//5.1- MarktParameterZApX // -- IMP-OptV1 --
    	private static final String[][] MarktparameterZAp1 = {    
    		{"mpZAp_1_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mpZAp_1_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mpZAp_1_PCCov","A","10","AE","40", "NUM"},    		
    		{"mpZAp_1_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_MarktparameterZAp1() {							
    		return MarktparameterZAp1;
    	}     	    	    	
    	private static final String[][] MarktparameterZAp2 = {    
    		{"mpZAp_2_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mpZAp_2_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mpZAp_2_PCCov","A","10","AE","40", "NUM"},    		
    		{"mpZAp_2_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_MarktparameterZAp2() {							
    		return MarktparameterZAp2;
    	}
    	private static final String[][] MarktparameterZAp3 = {    
    		{"mpZAp_3_FixingRiskCov_Meta","A","5","C","5", "STRING"},
    		{"mpZAp_3_FixingRiskCov_Data","A","6","C","8", "NUM"},    		    		
    		{"mpZAp_3_PCCov","A","10","AE","40", "NUM"},    		
    		{"mpZAp_3_PCRP","A","42","A","72", "NUM"}
    	};
    	public static String[][] getArchSpecs_MarktparameterZAp3() {							
    		return MarktparameterZAp3;
    	}    	    	
    	//5- ################### DONE: MarktParameterX ##################
    	
    	
    	*/
    	
    	
    	
    	//X- ################### TEST ##################    	
    	///////////////////////////////////////////////////
    	//test - single
    	private static final String[][] rvMetaColVals = {
    		{"rvMetaColVals","A","1","AU","1", "STRING"}
    	};    	
    	private static final String[][] rvDataAll = {
    		{"rvDataAll","C","2","AT","325", "NUM"}
    	};
    			
    	public static String[][] getRawValuesMetaValsArchSpecs() {							
    		return rvMetaColVals;
    	} 
    	public static String[][] getRawValuesDataAllArchSpecs() {							
    		return rvDataAll;
    	} 
    	
    	/*
    	//-- Sensi:Related --//
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
		*/
	
	} // class over  //
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	

    