
package com.stfe.optim.test;
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
import java.net.URL;
import java.util.*; 

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.helpers.Loader;
import org.junit.*;

import com.stfe.optim.config.initOptimAppConfig;
import com.stfe.optim.config.defSensiOptimXLSpecs;
import com.stfe.optim.config.defOptimAppXLSpecs;
//import com.sun.org.apache.bcel.internal.generic.LoadClass;


////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////
  
    public class testOptimComp {
  
  
		////////////////////////////////////////////////////////////
    	/*
		public static void STFE_initIBeanAndIXL()  {
			
			STFE_Int_InitExcelForBean();
			STFE_genBeanCovarFromSpecs();
			STFE_genBeanOptFromSpecs();
			STFE_getExcelFromBeanCovar();
			STFE_getExcelFromBeanOpt();
		}
		*/
		////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
    	static Logger LOG = Logger.getLogger(testOptimComp.class.getName());
    	
		public static void initApp() { // throws IOException {
						
			// BasicConfigurator replaced with PropertyConfigurator.
		    // BasicConfigurator.configure();
		    
			try {
				initOptimAppConfig.init();
			} catch (IOException ioex) {
				LOG.warn(" initOptimAppConfig.init() IOException !");
			}
			
			LOG.info(" Test the app prop : infilename - " +  initOptimAppConfig.appOptimProp.getProperty("infilename") );
			LOG.info(" Test the app prop : infilename - " +  initOptimAppConfig.infilename );
			
			String[][] optSpec= defSensiOptimXLSpecs.getBeanOptArchSpecs();
			String[][] cvSpec= defSensiOptimXLSpecs.getBeanCovarArchSpecs();
						
			String[][] rvDataAll=defOptimAppXLSpecs.getRawValuesDataAllArchSpecs();
			String[][] rvMetaRowVals=defOptimAppXLSpecs.getRawValuesMetaValsArchSpecs();
			
			LOG.info(" Test the spec - opt :" + optSpec );
			LOG.info(" Test the spec - opt :" + cvSpec );
			
			
		} // void main //
		
		
		
		//////////////////////////////////////////////////////////
		public static void filetest() {
			String deffile="J3-OXL_Sensis_Szenarios.xlsx";		
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel";
			//String fullfile = filepath+deffile;
			String fullfile = filepath + File.separator + deffile;
			
			String path = filepath + File.separator ;
			//String path = filepath ;
			//Check if filepath has fileseparator at end//
			if(path.charAt(path.length()-1)!=File.separatorChar){
			    path += File.separator;
			}
			System.out.println("++++ Check path : " +  path);
			    
			
			try {
				
				//File nfile = new File(deffile, filepath);
				File nfile = new File(fullfile);
				if ( nfile.exists() ) LOG.info(" File exists and valid " );
				else { 
						//String path="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";				
						//String path = "C:" + File.separator + "hello" + File.separator +  "J2" + deffile ;
						
						String ffile = filepath + File.separator + deffile;
						
						// Use relative path for Unix systems
						File f = new File(fullfile)  ; //File(fullfile);
						//f.getParentFile().mkdirs(); 
						f.createNewFile();				
						if ( f.exists() ) LOG.info(" File created and valid. " );
						else LOG.info(" File does not exist! Could not be created! " );
				}
				
				LOG.info(" Filepath :" + nfile.getPath()  + " # " + nfile.getCanonicalPath());
				LOG.info(" Filepath :" + nfile.getPath()  + " # " + nfile.getCanonicalPath());
				
				
					
			} catch (IOException ioex) {
				 LOG.warn(" File could not be created! IOException thrown! " );
			}
			
			
		}			
		//////////////////////////////////////////////////////////
		
		public static void loadResource() {
			//String res1 = getClass().getClassLoader().getResourceAsStream("path"+File.separator+"to"+File.separator+"resource");
			
							
			//C:\\devdrv\\JOM\\JOMPRJ\\resources\\app.optim.properties
			URL res = testOptimComp.class.getClassLoader().getResource("C:\\devdrv\\JOM\\JOMPRJ\\resources\\log4j.properties");	
			//System.out.println(" Resources file : " + res.toString() );
			
			
			InputStream res1 = testOptimComp.class.getClassLoader().getResourceAsStream("C:\\devdrv\\JOM\\JOMPRJ\\resources\\app.optim.properties");
			System.out.println(" Resources file : " + res1 );	
			
			//String res4 = getClass().getClassLoader().getResourceAsStream("path"+File.separator+"to"+File.separator+"resource");			
			//String res5 = getClass().getClassLoader().getResourceAsStream("path/to/resource");
		}
		
		
		public static void convColRowTest(){
		
			//public static String convColNumToString(int number);
			//public static String convColNumToStr(int i);
			//public static int convColCharToNum(String str);
			int colInt;
			String colStr;
			
			colStr="A";
			colInt = com.stfe.optim.util.optimColRowNum.convColCharsToNum(colStr);
			System.out.println("convColRowTest: conv col str to int: colStr " + colStr  + " = colInt:" + colInt);
			colInt = com.stfe.optim.util.optimColRowNum.convColCharsToNumId0(colStr);
			System.out.println("convColRowTest: Id0: conv col str to int: colStr " + colStr  + " = colInt:" + colInt);
			
			colStr="Z";
			colInt = com.stfe.optim.util.optimColRowNum.convColCharsToNum(colStr);
			System.out.println("convColRowTest: conv col str to int: colStr " + colStr  + " = colInt:" + colInt);
			
			colStr="AA";
			colInt = com.stfe.optim.util.optimColRowNum.convColCharsToNum(colStr);
			System.out.println("convColRowTest: conv col str to int: colStr " + colStr  + " = colInt:" + colInt);
			
			colStr="AU";
			colInt = com.stfe.optim.util.optimColRowNum.convColCharsToNum(colStr);
			System.out.println("convColRowTest: conv col str to int: colStr " + colStr  + " = colInt:" + colInt);
			
			colInt=1;
			colStr = com.stfe.optim.util.optimColRowNum.convColNumToStr(colInt);
			System.out.println("convColRowTest: conv col int->str: colStr " + colStr  + " = colInt:" + colInt);
			
			colInt=25;
			colStr = com.stfe.optim.util.optimColRowNum.convColNumToStr(colInt);
			System.out.println("convColRowTest: convColNumToStr: conv col int->str: colStr " + colStr  + " = colInt:" + colInt);
			colInt=25;
			colStr = com.stfe.optim.util.optimColRowNum.convColNumToStrId0(colInt);
			System.out.println("convColRowTest: convColNumToString: conv col int->str: colStr " + colStr  + " = colInt:" + colInt);
			
			colInt=27;
			colStr = com.stfe.optim.util.optimColRowNum.convColNumToStr(colInt);
			System.out.println("convColRowTest: conv col int->str: colStr " + colStr  + " = colInt:" + colInt);
			
			colInt=29;
			colStr = com.stfe.optim.util.optimColRowNum.convColNumToStr(colInt);
			System.out.println("convColRowTest: conv col int->str: colStr " + colStr  + " = colInt:" + colInt);
			
		}
		
		
		@Test
		public void slash() throws Exception {
			String deffile="J3-OXL_Sensis_Szenarios.xlsx";		
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel";
			String fullfile = filepath + File.separator + deffile;
			File file = new File(fullfile);
			//assertThat(file.exists(), is(true));
			
		}

		////////////////////////////////////////////////////////
		
		public static void main(String[] args) {
			initApp();
			filetest();
			convColRowTest();
			
			//loadResource();
		}
		//////////////////////////////////////////////////////////
		
		
} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    