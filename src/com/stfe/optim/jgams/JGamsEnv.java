
package com.stfe.optim.jgams;
/*
 * (c) Copyright BRDF. All rights reserved. 
 *
 * Created on 10.12.2018
 *
 * @author NS
 *
 */
import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSGlobals.DebugLevel;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.gams.api.GAMSDatabase;

import java.util.*;
import java.text.SimpleDateFormat;

public class JGamsEnv {

	private static JGamsEnv JGamsEnv_instance = null;
	
	private static GAMSJob gJob = null; 
	private static GAMSOptions gOption = null;
	private static GAMSDatabase gDB = null;
	private static GAMSWorkspace wkSpace= null; 
	private static String sesionGDXId=null;
	private static boolean DBActive = false;
	
	public static String getsessionGDXId() {
		return sesionGDXId;
	}

	public static void setsessionGDXId(String sesionGDXId) {
		JGamsEnv.sesionGDXId = sesionGDXId;
	}
	
	public static GAMSWorkspace getWkSpace() {
		return wkSpace;
	}

	public static void setWkSpace(GAMSWorkspace wkSpace) {
		JGamsEnv.wkSpace = wkSpace;
	}

	public static GAMSJob getgJob() {
		return gJob;
	}

	public static void setgJob(GAMSJob gJob) {
		JGamsEnv.gJob = gJob;
	}

	public static GAMSOptions getgOption() {
		return gOption;
	}

	public static void setgOption(GAMSOptions gOption) {
		JGamsEnv.gOption = gOption;
	}

	public static GAMSDatabase getgDB() {
		return gDB;
	}

	public static void setgDB(GAMSDatabase gDB) {
		JGamsEnv.gDB = gDB;
	}

		
	public static JGamsEnv getInstance() { 
		if (JGamsEnv_instance == null) {
			JGamsEnv_instance = new JGamsEnv();
			JGamsEnv_instance.initWorkSpace();
		}
		return JGamsEnv_instance; 
	} 
	
	public void initWorkSpace() {
		// Workspace //
		System.out.println(" Setup the GAMS Workspace ");
        	
		String GAMSSysDir= "C:\\GAMS\\win64\\25.1";  
	    String GAMSWorkDir= "C:\\devdrv\\JOPS\\tmp\\";  
	    GAMSWorkDir= GAMSWorkDir + new Object(){}.getClass().getEnclosingClass().getSimpleName(); 
	    
	    //--System.out.println(" Default working dir: " + System.getProperty("java.io.tmpdir")); //-- C:\Users\SinghN\AppData\Local\Temp\--//
	    System.out.println(" Setup working dir: " + GAMSWorkDir);
	    System.out.println(" Setup system dir: " + GAMSSysDir);
	    GAMSWorkspaceInfo wsInfo = new GAMSWorkspaceInfo(GAMSWorkDir, GAMSSysDir, DebugLevel.OFF);
	    
	    com.stfe.optim.jgams.JGamsEnv.setWkSpace(  new GAMSWorkspace(wsInfo) );
	    
	    System.out.println(" GAMS Workspace initialized successfully!");
	}
	
	//-- LIfecycle - GDX/DB Export & Dump -- //
	public static void GDXSessionON(String sesionGDXId) { 
		//setup GAMS-DB session//
		com.stfe.optim.jgams.JGamsEnv.setgOption( com.stfe.optim.jgams.JGamsEnv.getWkSpace().addOptions() );
		com.stfe.optim.jgams.JGamsEnv.setgDB(com.stfe.optim.jgams.JGamsEnv.getWkSpace().addDatabase() );
		com.stfe.optim.jgams.JGamsEnv.DBActive = true;
		com.stfe.optim.jgams.JGamsEnv.setsessionGDXId(sesionGDXId);
	}
	public static void GDXDump(String GDXName) { 
		com.stfe.optim.jgams.JGamsEnv.getgDB().export(GDXName);
	}
	public static void GDXDump() {
		String GDXfileName ="opti_jops_GDX_";
		GDXfileName = GDXfileName + new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
		com.stfe.optim.jgams.JGamsEnv.getgDB().export(GDXfileName);
	}
	public static void DBClean() {
		if (com.stfe.optim.jgams.JGamsEnv.DBActive) {
			com.stfe.optim.jgams.JGamsEnv.getgDB().clear();
			com.stfe.optim.jgams.JGamsEnv.getgDB().dispose();
			com.stfe.optim.jgams.JGamsEnv.DBActive = false;
		}
		com.stfe.optim.jgams.JGamsEnv.setgOption( com.stfe.optim.jgams.JGamsEnv.getWkSpace().addOptions() );
		com.stfe.optim.jgams.JGamsEnv.setgDB(com.stfe.optim.jgams.JGamsEnv.getWkSpace().addDatabase() );
		com.stfe.optim.jgams.JGamsEnv.DBActive = true;
	}
	
	public static void GDXSessionOFF() {
		if (com.stfe.optim.jgams.JGamsEnv.DBActive) {
			com.stfe.optim.jgams.JGamsEnv.getgDB().clear();
			com.stfe.optim.jgams.JGamsEnv.getgDB().dispose();
			com.stfe.optim.jgams.JGamsEnv.DBActive = false;
		}
		com.stfe.optim.jgams.JGamsEnv.DBActive = false;
		com.stfe.optim.jgams.JGamsEnv.sesionGDXId = null;
	}
	public static boolean GDXSessionExist() {
		if (com.stfe.optim.jgams.JGamsEnv.sesionGDXId == null) return false;
		else return true;
	}
	
	
	
}//-- class JGamsEnv --//
