
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
  

    public class optimSerializeBean {
    	
    	private String StreamFilepath = null;
	  		
  		public optimSerializeBean(String filepath) {  			
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
		public void serializeBeanAsPdouble(double[][] beanImpl, String name, String filepath)  {
		
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			//--String deffile="Sensis und Szenarios.xlsx";	
			
			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
			
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			
			//--Serialize Bean  --//
			try(ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(beanFileName));) {
							
				synchronized (beanFileName) {
					
					objOutStream.writeObject(beanImpl);
					objOutStream.close();
				}
				if (! validFile(beanFileName) ) {
					System.out.println("Error: optimSerializeBean-serializeBeanAsPdouble : Could not serialize - unkown reason!"); 
				}
			}
			
			catch (Exception e) { e.printStackTrace(); }
			
		} //--class STFE_Int_serializeBean over --//
		////////////////////////////////////////////////////////////


		////////////////////////////////////////////////////////////
		//1- Create Bean from its defined architecture //	
		public void serializeBean1DAsPdouble(double[] beanImpl, String name, String filepath)  {
			
			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			
			//--Serialize Bean  --//
			try(ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(beanFileName));) {
							
				synchronized (beanFileName) {
					
					objOutStream.writeObject(beanImpl);
					objOutStream.close();
				}
				if (! validFile(beanFileName) ) {
					System.out.println("Error: optimSerializeBean-serializeBeanAsPdouble : Could not serialize - unkown reason!"); 
				}
			}
			
			catch (Exception e) { e.printStackTrace(); }
			
		} //--class STFE_Int_serializeBean over --//
		////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////
		//1- Create Bean from its defined architecture //	
		public void serializeBean(Object[][] beanImpl, String name, String filepath)  {

			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			//--String deffile="Sensis und Szenarios.xlsx";
			
			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
			
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			
			//--Serialize Bean  --//
			try(ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(beanFileName));) {
												
				synchronized (beanFileName) {					
					objOutStream.writeObject(beanImpl);
					objOutStream.close();
				}
				if (! validFile(beanFileName) ) {
					System.out.println("Error: optimSerializeBean-serializeBean : Could not serialize - unkown reason!"); 
				}
			}
			
			catch (Exception e) { e.printStackTrace(); }
			
		} //--class STFE_Int_serializeBean over --//
		////////////////////////////////////////////////////////////
		
		//1- Create Bean from its defined architecture //	
		public void serializeBeanAsString(String[][] beanImpl, String name, String filepath)  {

			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			//--String deffile="Sensis und Szenarios.xlsx";
			
			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
			
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			
			//--Serialize Bean  --//
			try(ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(beanFileName));) {
												
				synchronized (beanFileName) {					
					objOutStream.writeObject(beanImpl);
					objOutStream.close();
				}
				if (! validFile(beanFileName) ) {
					System.out.println("Error: optimSerializeBean-serializeBean : Could not serialize - unkown reason!"); 
				}
			}
			
			catch (Exception e) { e.printStackTrace(); }
			
		} //--class STFE_Int_serializeBean over --//
		////////////////////////////////////////////////////////////
		
		
		//////////////////////////////////////////////////////////
		public void serializeResultBeanAsPdouble(double[][] beanImpl, String beanName, String filepath) {
		
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			//--String deffile="Sensis und Szenarios.xlsx";				
			
			if (filepath == null) filepath=this.StreamFilepath;			
			else filepath=validatePath(filepath);
			
			String beanFileName=filepath+"IBean-"+beanName+"-XL.ser";
						
			try(ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(beanFileName));) {
			//--Serialize Bean  --//			
						
				synchronized (beanFileName) {					
					objOutStream.writeObject(beanImpl);
					objOutStream.close();
				}
				if (! validFile(beanFileName) ) {
					System.out.println("Error: optimSerializeBean-serializeBeanAsPdouble : Could not serialize - unkown reason!"); 
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		//////////////////////////////////////////////////////////
		

		//////////////////////////////////////////////////////////
		public void serializeResultBeanAsPdouble2D(double[][] beanImpl, String fullFileName) {
			
			try(ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(fullFileName));) {
				synchronized (fullFileName) {					
					objOutStream.writeObject(beanImpl);
					objOutStream.close();
				}
				if (! validFile(fullFileName) ) {
					System.out.println("Error: optimSerializeBean-serializeBeanAsPdouble-2D : Could not serialize - unkown reason!"); 
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		public void serializeResultBeanAsPdouble4D(double[][][][] beanImpl, String fullFileName) {
		
			try(ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(fullFileName));) {
			
				synchronized (fullFileName) {					
					objOutStream.writeObject(beanImpl);
					objOutStream.close();
				}
				if (! validFile(fullFileName) ) {
					System.out.println("Error: optimSerializeBean-serializeBeanAsPdouble-4D : Could not serialize - unkown reason!"); 
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		//////////////////////////////////////////////////////////

	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    