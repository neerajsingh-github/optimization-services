
package com.stfe.optim.config;


 
import java.io.*;
import java.util.*; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.InputStream; 
import java.net.URL;
import java.util.Properties; 

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;


/*
 * (c) Copyright BRDF. All rights reserved. 
 *
 * Created on 10.03.2017
 *
 * @author NS
 *
 */
////////////////////////////////////////////////////////////


    public class initOptimAppConfig {
    	
		private static boolean confDone=false;
		private static final org.apache.log4j.Logger LOG = Logger.getLogger("AppOptim.log");
		public static Properties appOptimProp;
				
		public static String infilename = "infilename"; 
		public static String infile_rawvalues_name = "rawvalues.infilename";
		public static String outfile_rawvalues_name = "OXL_rawvalues.infilename";
		
		public static String infile_rawvalues_IXL_name ="rawvalues_IXL_name";
		public static String outfile_rawvalues_IXL_name ="OXL_rawvalues_IXL_name";
		
		public static String DKF_opti_IXL_filename="DKF_opti_IXL_filename";
		
		public static String outfilename = "outfilename"; 
		
		public static String infiledir = "infiledir"; 
		public static String outfiledir = "outfiledir";		
		public static String workingdir = "workingdir"; 
		
		public static String resourcesdir = "resourcesdir"; 
		public static String ipoptdir = "ipoptdir";
		
		//String deffile="OXL_Sensis_Szenarios.xlsx";		
		//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
		//String beanFileName=filepath+"IBean-"+name+"-XL.ser";
		//String deffile="Sensis und Szenarios.xlsx";				
	
		
		
		/**
		 * Initializes Context for app-optim profile
		 * 
		 * @return
		 * @throws IOException
		*/
		public static void init() throws IOException {
			
			try {
				if (!initOptimAppConfig.confDone) {
					loadOptimAppProperties();
					LOG.info("initOptimAppConfig: Successfully loaded app properties.");
				}
			} catch (IOException e) {
				initOptimAppConfig.confDone=false;
				LOG.error("initOptimAppConfig: Error while loading properties !");
			}						
		}
		
		
		
		private static void loadOptimAppProperties() throws IOException {
			 
			try {
				appOptimProp = new Properties();
				String propFileName = "app.optim.properties";
					
				//URL resourceURL = Loader.getResource(propFileName);
				FileReader propfile = 	new FileReader("resources\\"+ propFileName);
												
				if (propfile != null) {
									
					appOptimProp.load(propfile);
													
					propfile.close();
					initOptimAppConfig.DKF_opti_IXL_filename 	= 	appOptimProp.getProperty("DKF_opti_IXL_filename");
					
					initOptimAppConfig.infile_rawvalues_name 	= 	appOptimProp.getProperty("infile_rawvalues_name");
					
					initOptimAppConfig.infile_rawvalues_name 	= 	appOptimProp.getProperty("infile_rawvalues_name");
					initOptimAppConfig.outfile_rawvalues_name 	= 	appOptimProp.getProperty("outfile_rawvalues_name");
					
					initOptimAppConfig.infile_rawvalues_IXL_name = appOptimProp.getProperty("infile_rawvalues_IXL_name");
					initOptimAppConfig.outfile_rawvalues_IXL_name = appOptimProp.getProperty("outfile_rawvalues_IXL_name");
					
					initOptimAppConfig.outfilename 	= 	appOptimProp.getProperty("outfilename");
					initOptimAppConfig.infiledir 	= 	appOptimProp.getProperty("infiledir");
					initOptimAppConfig.outfiledir 	= 	appOptimProp.getProperty("outfiledir");
					initOptimAppConfig.workingdir 	= 	appOptimProp.getProperty("workingdir");
					initOptimAppConfig.resourcesdir = 	appOptimProp.getProperty("resourcesdir");
					initOptimAppConfig.ipoptdir 	= 	appOptimProp.getProperty("ipoptdir");
					
					//log4j configuration
					PropertyConfigurator.configure("resources\\log4j.properties");	
					
					//configure only once
					initOptimAppConfig.confDone=true;
				}
				//else throw new FileNotFoundException("App optim property file " + propFileName + " not found in the classpath");
				//else System.out.println("App optim property file " + propFileName + " not found in the classpath");
				
			} catch (Exception exc) {
				initOptimAppConfig.confDone=false;
				LOG.error("Error during loadProperties", exc.getCause());
			} 
			
		}

	
} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    