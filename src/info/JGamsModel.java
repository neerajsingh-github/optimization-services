
package info;
import java.lang.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.gams.api.GAMSCheckpoint;
import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;


public class JGamsModel {

	
	
	//public void loadToyModel (GAMSWorkspace wkSpace, GAMSJob gJob, GAMSOptions gOption) {
	public void loadToyModel (JGamsEnv jgEnv) {
		final String toyModelFile="C:\\devdrv\\JOPS\\Model\\JOPSToyModel";
		
		System.out.println(" Toy Model for Bond Optimization ");
	    
		info.JGamsEnv.setgJob(  info.JGamsEnv.getWkSpace().addJobFromFile(toyModelFile) ); // C:\devdrv\JOPS\Model\JOPSToyModel
	    
		 
	    // Solver options: gOption2 for CONCOPT3 -- 3 or 4 //
		System.out.println("\n Invoked Solver: CONOPT 4: \n");
		info.JGamsEnv.setgOption( info.JGamsEnv.getWkSpace().addOptions() );
		info.JGamsEnv.getgOption().setAllModelTypes( "concopt" );
		info.JGamsEnv.getgOption().setOptFile(4);
    	
		/*
		 // write file "concopt.opt" under GAMSWorkspace's working directory
        try {
            BufferedWriter optFile = new BufferedWriter(new FileWriter(jgEnv.wkSpace.workingDirectory() + GAMSGlobals.FILE_SEPARATOR + "conopt4.opt"));
            //optFile.write("algorithm=barrier");
            optFile.close();
         } catch(IOException e) {
              e.printStackTrace();
              System.exit(-1);
         }
         */
		
	} //--loadToyModel --//
	

	//public void loadToyModel (GAMSWorkspace wkSpace, GAMSJob gJob, GAMSOptions gOption) {
	public void loadToyModelIncludeData (JGamsEnv jgEnv) {
		final String toyModelFile="C:\\devdrv\\JOPS\\Model\\JOPSToyModel2";
		
		System.out.println(" Toy Model for Bond Optimization ");
	    
		info.JGamsEnv.setgJob(  info.JGamsEnv.getWkSpace().addJobFromFile(toyModelFile) ); // C:\devdrv\JOPS\Model\JOPSToyModel
	    
		 
	    // Solver options: gOption2 for CONCOPT3 -- 3 or 4 //
		System.out.println("\n Invoked Solver: CONOPT 4: \n");
		
    	
		
	} //--loadToyModel --//
	
	
		
}//-- JGamsModel --//

	
