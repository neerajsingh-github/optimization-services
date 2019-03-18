
package info;
import java.lang.*;
import java.io.File;

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSGlobals.DebugLevel;
import com.gams.api.GAMSCheckpoint;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;


public class JGamsProcess {

	
	
	public void JGamsAppProcess (JGamsEnv jgEnv) {
		boolean modelIncludeData= true;
		boolean loadData= true;
		boolean loadModel= true;
				
		boolean tcDataValid = true;
		
		System.out.println(" Executing JGamsProcess...");

		System.out.println(" Loading the  Model...");
		
		
		//Options
		info.JGamsEnv.setgOption( info.JGamsEnv.getWkSpace().addOptions() );
		info.JGamsEnv.getgOption().setAllModelTypes( "concopt" );
		info.JGamsEnv.getgOption().setOptFile(4);
		
		//-- Initialize a checkpoint before running a job --//
        GAMSCheckpoint chkPoint = info.JGamsEnv.getWkSpace().addCheckpoint();
        
        if (tcDataValid) {
        	JGamsExportGDX tcDataGDX = new JGamsExportGDX();
        	tcDataGDX.testsuitesExportGDXAll();
        }
        
		//--data processing--//
		if (loadData) {
			if (modelIncludeData) {
				//--Data--//
				JGamsData jgData = new JGamsData();
				String dbName = jgData.JGamsModelData();
				info.JGamsEnv.getgDB().export(dbName);
				info.JGamsEnv.getgDB().export("GDXModelData.gdx");
				info.JGamsEnv.getgOption().defines("gdxincname", dbName);
			}
		}//--loadData--//
		
		
		//--model processing--//
		if (loadModel) {
		
			//-- If needed, explicitly import the gdx-data into database --//
			//GAMSDatabase gdxdb = ws.addDatabaseFromGDX("gdxincname.gdx");
			
			JGamsModel jgModel = new JGamsModel();
			if (!modelIncludeData) {
				jgModel.loadToyModel(jgEnv); // (wkSpace, gJob, gOption);
			} else {
				jgModel.loadToyModelIncludeData(jgEnv);
			}
			
			//--Running job--//        
			System.out.println(" Running optimization...");
			
			if (modelIncludeData) {
				info.JGamsEnv.getgJob().run(info.JGamsEnv.getgOption(),  chkPoint, info.JGamsEnv.getgDB() ); // info.JGamsEnv.gDB
			} else {
				info.JGamsEnv.getgJob().run(info.JGamsEnv.getgOption(), chkPoint);
			}
			
			// Retrieve GAMSVariable "x" from GAMSJob's output databases
			System.out.println(" Optimization over. Checking Optimal results ...");
			JGamsSolution jgSol = new JGamsSolution();
			jgSol.JGamsSolutionData(jgEnv);
			
		}//--loadModel, running job--//
		
	}
	
	
}//-- class JGamsProcess --//


