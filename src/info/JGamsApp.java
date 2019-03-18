
package info;
import java.lang.*;
import java.io.File;
import java.util.*; 

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.gams.api.GAMSGlobals.DebugLevel;


public class JGamsApp {

	
	public static void main (String[] args) {
	
		System.out.println(" Welcome to project JOPS ");
		final long startTime = System.currentTimeMillis();
		
		
		/*
		
		//-- Initializing Workspace and processing further--//
		JGamsApp jgApp = new JGamsApp();
		jgApp.executeJGApp();		
		
		
		final long endTime   = System.currentTimeMillis();
		double totalTime = (endTime - startTime)/ (60000.0);
		System.out.println("\n Time taken (in minutes) to execute the optimization : " + totalTime);	

        System.out.println("\n  JGamsApp Over!");
        */

        JGamsBlockChain blockChain= new JGamsBlockChain();
        blockChain.processBlockChain();

	}
	
	
	private void executeJGApp() {

		System.out.println(" Initializing Workspace and processing further ...");

		JGamsEnv jgEnv = JGamsEnv.getInstance();
		this.initWorkSpace();
	    
        JGamsProcess JGPS_Obj = new JGamsProcess();
		JGPS_Obj.JGamsAppProcess(jgEnv);

	}
	
	
	private void initWorkSpace() {
		// Workspace //
		System.out.println(" Setup the GAMS Workspace ");
        	
		String GAMSSysDir= "C:\\GAMS\\win64\\25.1";  
	    String GAMSWorkDir= "C:\\devdrv\\JOPS\\tmp\\";  
	    //GAMSWorkDir= GAMSWorkDir + new Object(){}.getClass().getEnclosingClass().getSimpleName(); 
	    GAMSWorkDir= GAMSWorkDir + this.getClass().getSimpleName();
	    //--System.out.println(" Default working dir: " + System.getProperty("java.io.tmpdir")); //-- C:\Users\SinghN\AppData\Local\Temp\--//
	    System.out.println(" Setup working dir: " + GAMSWorkDir);
	    System.out.println(" Setup system dir: " + GAMSSysDir);
	    GAMSWorkspaceInfo wsInfo = new GAMSWorkspaceInfo(GAMSWorkDir, GAMSSysDir, DebugLevel.OFF);
	    
	    info.JGamsEnv.setWkSpace(  new GAMSWorkspace(wsInfo) );   
	    System.out.println(" Workspace initialized successfully!");
	}
	
	
}//-- class JGamsApp --//
