
package com.stfe.optim.util;

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
//import java.util.*; 


////////////////////////////////////////////////////////////
  
	public class optimDeserializeBean {
  
  		private String StreamFilepath = null;
  		  		
  		public optimDeserializeBean(String filepath) {  			
  			setStreamFilePath(filepath);  			
  		}
  		 		
  		
  		public void setStreamFilePath(String filepath){  						
  			this.StreamFilepath=validatePath(filepath);
  		}
  		public String getStreamFilePath(){  						
  			return this.StreamFilepath; 
  		}
  		
  		
  		private String setTrailingSepChar(String filepath) {
  			if (filepath == null) return null;
  			if(filepath.charAt(filepath.length()-1)!=File.separatorChar)
				filepath += File.separator;
  			return filepath;
  		}
  		
  		//validate filename //
  		private boolean validFile(String filename) {
  			java.io.File vfile = new File(filename); 
  			if ( vfile.exists() && vfile.isFile() ) {
  				return true;
  			} else {
  				System.out.println("Error: validFile - not valid! ");
  				return false;  				
  			}
  		}
  		
  		//-- validate dir-path --//
  		private String validatePath(String filepath) {
  			if (filepath == null) {
  				System.out.println("Error: validatePath - Filepath null! ");
  				return null;
  			}
  			
  			java.io.File vfile = new File(filepath); 
  			if ( vfile.exists() && vfile.isDirectory() ) {  				  				
  				return setTrailingSepChar(filepath);  				
  			} else {
  				System.out.println("Error: validatePath - Filepath as a directory does not exist! ");
  				return null;
  			}
  		}
  		
  		
  		
		////////////////////////////////////////////////////////////
		//1- Create Bean from its defined architecture //	
		public Object[][] deserializeBean(String name, String filepath)  {
			
			//--Filepath Bean  --//			
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			
			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
			
			Object[][] beanImpl=null;
			//ObjectInputStream objInStream = null;
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			if (! validFile(beanFileName) ) return null;
			
			try(ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream(beanFileName));) {		
				
				synchronized (beanFileName) {
					
					//--Serialize Bean  --//									
					//if (name.equals("IBean-Meta-BeanArchSpecs-XL.ser")) { 				
					if (beanFileName.contains("IBean-Meta-BeanArchSpecs")) {
						beanImpl = (String[][]) objInStream.readObject();
					}
					else {
						beanImpl = (Double[][]) objInStream.readObject();
					}
					
					objInStream.close();
				}
				
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		
		
		
		////////////////////////////////////////////////////////////
		public String[][] deserializeBeanAsString(String name, String filepath)  {
			
			//--Filepath Bean  --//
			//--//String deffile="Sensis und Szenarios.xlsx";			
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";

			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
		
			
			String[][] beanImpl=null;
			//ObjectInputStream objInStream = null;
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			if (! validFile(beanFileName) ) return null;
			
			try(ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream(beanFileName));) {		
				synchronized (beanFileName) {
					
					beanImpl = (String[][]) objInStream.readObject();
					objInStream.close();				
				}
				
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		
		
		
		
		////////////////////////////////////////////////////////////
		public double[][] deserializeBeanAsPdouble(String name, String filepath)  {		
			
			//--Filepath Bean  --//
			//String deffile="Sensis und Szenarios.xlsx";
			
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";

			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
		
			
			double[][] beanImpl=null;
			//ObjectInputStream objInStream = null;
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			if (! validFile(beanFileName) ) return null;
			
			try(ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream(beanFileName));) {		
				
				synchronized (beanFileName) {
					
					//--Serialize Bean  --//									
					//if (name.equals("IBean-Meta-BeanArchSpecs-XL.ser")) { 				
					if (! beanFileName.contains("IBean-Meta-BeanArchSpecs")) {
						beanImpl = (double[][]) objInStream.readObject();
					}
								
					objInStream.close();
				}
				
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////
		public double[] deserializeBean1DAsPdouble(String name, String filepath)  {		
			
			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
			
			double[] beanImpl=null;
			//ObjectInputStream objInStream = null;
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			if (! validFile(beanFileName) ) return null;
			
			try(ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream(beanFileName));) {		
			
				synchronized (beanFileName) {
				
				//--Serialize Bean  --//									
				//if (name.equals("IBean-Meta-BeanArchSpecs-XL.ser")) { 				
				if (! beanFileName.contains("IBean-Meta-BeanArchSpecs")) {
					beanImpl = (double[]) objInStream.readObject();
				}
				
					objInStream.close();
				}
				
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////
		public double[][][][] deserializeBeanAsPdouble4D(String fullFileName)  {		
			
			double[][][][] beanImpl=null;
			if (! validFile(fullFileName) ) return null;
			
			try(ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream(fullFileName));) {		
			
				synchronized (fullFileName) {
				
					//--Deserialize Bean  --//
					beanImpl = (double[][][][]) objInStream.readObject();
					objInStream.close();
				}
			
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		////////////////////////////////////////////////////////////


		
	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    