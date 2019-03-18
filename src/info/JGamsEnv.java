
package info;

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSGlobals.DebugLevel;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.gams.api.GAMSDatabase;

public class JGamsEnv {

	private static JGamsEnv JGamsEnv_instance = null;
	
	private static GAMSJob gJob = null; 
	private static GAMSOptions gOption = null;
	private static GAMSDatabase gDB = null;
	private static GAMSWorkspace wkSpace= null; 
	
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
		if (JGamsEnv_instance == null) 
			JGamsEnv_instance = new JGamsEnv(); 
		return JGamsEnv_instance; 
	} 
	
	
}//-- class JGamsEnv --//
