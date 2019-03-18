
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
  

    public class optimValidateFile {
    	  				
  		private static String setTrailingSepChar(String filepath) {
  			if (filepath == null) return null;
  			if(filepath.charAt(filepath.length()-1)!=File.separatorChar)
				filepath += File.separator;
  			return filepath;
  		}
  		
  	    //validate filename //
  		public static boolean validFile(String filename) {
  			java.io.File vfile = new File(filename); 
  			if ( vfile.exists() && vfile.isFile() ) {
  				return true;
  			} else {
  				System.out.println("Error: validFile - not valid! " + filename);
  				return false;  				
  			}
  		}
  		
  		//-- validate dir-path --//
  		public static String validatePath(String filepath) {
  			if (filepath == null) {
  				System.out.println("Error: validatePath - Filepath null! " + filepath);
  				return null;
  			}  			
  			java.io.File vfile = new File(filepath); 
  			if ( vfile.exists() && vfile.isDirectory() ) {  				  				
  				return setTrailingSepChar(filepath);  				
  			} else {
  				System.out.println("Error: validatePath - Filepath as a directory does not exist! " + filepath);
  				return null;
  			}
  		}
  		  			
  		
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    