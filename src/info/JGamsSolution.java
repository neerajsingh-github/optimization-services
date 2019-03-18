
package info;
import java.lang.*;
import java.io.File;

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSGlobals.DebugLevel;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;


public class JGamsSolution {

	public void JGamsSolutionData (JGamsEnv jgEnv) {
		
		// Retrieve GAMSVariable "x" from GAMSJob's output databases
		System.out.println(" Retrieving optimal solution...");
        
        for (GAMSVariableRecord rec : info.JGamsEnv.getgJob().OutDB().getVariable("x")) {
            //System.out.print("x(" + rec.getKey(0) + ", " + rec.getKey(1) + "):");
        	System.out.print(" x(" + rec.getKey(0) + ", " + "):");
        	System.out.print(", scale = " + rec.getScale());
            System.out.print(", lower/upper = " + rec.getLower() + " / " + rec.getUpper());
        	System.out.print(", level = " + rec.getLevel());
            System.out.println(", marginal = " + rec.getMarginal());
            
        }
        for (GAMSVariableRecord rec : info.JGamsEnv.getgJob().OutDB().getVariable("Z")) { // Cost//
        	System.out.print(" Cost(1" +  ", " + "):");
        	System.out.print(", level = " + rec.getLevel());
            System.out.println(", marginal = " + rec.getMarginal());
            
        }
        for (GAMSVariableRecord rec : info.JGamsEnv.getgJob().OutDB().getVariable("E1")) { // Risk//
        	System.out.print(" E1(1"  + ", " + "):");
        	System.out.print(", level = " + rec.getLevel());
            System.out.println(", marginal = " + rec.getMarginal());
        }
        
	}
	
}//-- class JGamsSolution --//
