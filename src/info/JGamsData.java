
package info;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Vector;

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.gams.api.GAMSParameter;
import com.gams.api.GAMSSet;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;


public class JGamsData {

	public  String JGamsModelData () { //JGamsEnv jgEnv1//
		
		System.out.println(" Initializing jGamsData!");
		//--Fulfilling data requirements for $load js1 jp1 jp2 jc --//
		long jd_jc = 90;
		
		//-- List set--//
		List<String> jd_js0= Arrays.asList("1", "2", "3", "4", "5" ,"6" );
		
		List<String> jd_js15= Arrays.asList("0", "1", "2", "3", "4" ,"5" );
		
		//List<String> jd_js1= Arrays.asList("1", "2", "3", "4", "5" ,"6" );
		
		String[] jd_js1= {"11", "12", "13", "14", "15","16"};
		//List<String> jd_js1= Arrays.asList("11", "12", "13", "14", "15","16");
				
		//-- Map parameter--//
		//-- Map parameter--//
		////////////////
		Map<String, Double> jd_jp15 = new HashMap<String, Double>();
			    {
			    	for (int i=0; i <6; i++){
			    		jd_jp15.put(String.valueOf(i), Double.valueOf(i * 10.0));
			  }
		}
		////////////////
			    
		Map<String, Double> jd_jp1 = new HashMap<String, Double>();
	    {
	    	for (int i=1; i <7; i++){
	    		jd_jp1.put(String.valueOf(i), Double.valueOf(i * 50.0));
	    	}
	    }
	    System.out.println(" Out # Small 1d-matrix as parameter in java");
	    Iterator <Map.Entry<String, Double>> jd_jp1_iter = jd_jp1.entrySet().iterator(); 
        while (jd_jp1_iter.hasNext()) {
        	Map.Entry<String, Double> jd_jp1_entry = jd_jp1_iter.next();
        	System.out.println("### jd_jp1_1D: "+ jd_jp1_entry.getKey() + " => " + jd_jp1_entry.getValue());
        }
        
	    Map<Vector<String>, Double> jd_jp2 = new HashMap<Vector<String>, Double>();
	    {
	    	for (int i=1; i <7; i++){
	    		for (int j=11; j <17; j++) {
	    			
	    			jd_jp2.put( new Vector<String>( Arrays.asList(new String[]{String.valueOf(i), String.valueOf(j)}) ), Double.valueOf(i+j+10));
	    			
	    			//for (int k=1; k <7; k++)  jd_jp2.put( new Vector<Integer>( Arrays.asList(new Integer[]{i, j, k}) ), Double.valueOf(i+j+10));
	    		}
	    	}
	    } 
	    System.out.println(" Out # Small 2d-matrix as parameter in java");
        Iterator <Map.Entry<Vector<String>, Double>> jd_jp2_iter = jd_jp2.entrySet().iterator(); 
        while (jd_jp2_iter.hasNext()) {
        	Map.Entry<Vector<String>, Double> jd_jp2_entry = jd_jp2_iter.next();
        	System.out.println("### jd_jp2_Mat2D: "+ jd_jp2_entry.getKey() + " => " + jd_jp2_entry.getValue());
        }
	
	    info.JGamsEnv.setgDB( info.JGamsEnv.getWkSpace().addDatabase() );
	    //-scalar--//
	    GAMSParameter scalar1 = info.JGamsEnv.getgDB().addParameter("jc", "scalar constant jc");
	    scalar1.addRecord().setValue(90);
	    
	    //////////////////
	    //--set--//
	    GAMSSet js15 = info.JGamsEnv.getgDB().addSet("idx", 1, "set idx vidx");
	    for(String ele_jd_js0 : jd_js15) js15.addRecord(ele_jd_js0);
	    
	    //////////////////

	    GAMSSet js0 = info.JGamsEnv.getgDB().addSet("i", 1, "set vidx");
	    for(String ele_jd_js0 : jd_js0) js0.addRecord(ele_jd_js0);

	    GAMSSet js01 = info.JGamsEnv.getgDB().addSet("j", 1, "set vjdx");
	    for(String ele_jd_js01 : jd_js0) js01.addRecord(ele_jd_js01);

	    GAMSSet js1 = info.JGamsEnv.getgDB().addSet("k", 1, "set vkdx");
	    int cnt=0;  
        for(String ele_jd_js1 : jd_js1) {
        	//ele_jd_js1=strarr[cnt++];
            js1.addRecord(ele_jd_js1);
        }

        ////////////////
        //--parameter--//
        GAMSParameter vp15 = info.JGamsEnv.getgDB().addParameter("jp15",  "data jp15 : supplied from include datafile15", js15);
        //GAMSParameter vp15 = info.JGamsEnv.getgDB().addParameter("jp15",  "data jp15 : supplied from include datafile15");
        for(String ele_jd_js15 : jd_js15)
            vp15.addRecord(ele_jd_js15).setValue(jd_jp15.get(ele_jd_js15) ); 
        ////////////////
        
        
        GAMSParameter vp1 = info.JGamsEnv.getgDB().addParameter("jp1",  "data jp1 : supplied from include datafile", js0);
        for(String ele_jd_js0 : jd_js0)
            vp1.addRecord(ele_jd_js0).setValue(jd_jp1.get(ele_jd_js0) ); 
        
        
        GAMSParameter vp2 = info.JGamsEnv.getgDB().addParameter("jp2",  "data jp2 : supplied from include datafile", js0, js1);
        for(Vector<String> ele_vp2 : jd_jp2.keySet() )
            vp2.addRecord(ele_vp2).setValue( jd_jp2.get(ele_vp2).doubleValue() ); 
         
        
        // import a 3d matrix from java to GMS //
        System.out.println(" IN # Small 3d-matrix as parameter in java");
        Map<Vector<String>, Double> jd_jp_mat3d = new HashMap<Vector<String>, Double>();
	    {
	    	for (int i=1; i<7; i++){
	    		for (int j=1; j<7; j++) {
	    			for (int k=1; k<7; k++) {
	    				jd_jp_mat3d.put( new Vector<String>( Arrays.asList(new String[]{String.valueOf(i), String.valueOf(j),  String.valueOf(k) }) ), Double.valueOf(i+j+k+5));
	    			}
	    			
	    		}
	    	}
	    } 
	    System.out.println(" Out # Small 3d-matrix as parameter in java");
        Iterator <Map.Entry<Vector<String>, Double>> kvIter_3DSmall = jd_jp_mat3d.entrySet().iterator(); 
        while (kvIter_3DSmall.hasNext()) {
        	Map.Entry<Vector<String>, Double> entrySmall = kvIter_3DSmall.next();
        	System.out.println("### jd_jp_mat3d_Small: "+ entrySmall.getKey() + " => " + entrySmall.getValue());
        }
        
        System.out.println(" Small 3d-matrix as parameter in gms");
	    GAMSParameter vp3 = info.JGamsEnv.getgDB().addParameter("jp3mat",  "data jp3mat : supplied from include datafile", js0, js0, js0);
        for(Vector<String> ele_vp3 : jd_jp_mat3d.keySet() )
            vp3.addRecord(ele_vp3).setValue( jd_jp_mat3d.get(ele_vp3).doubleValue() ); 
        
       
        //System.out.println("=== Iterating over a HashMap using Java 8 forEach and lambda ===" + 
        //    jd_jp_mat3d.foreach() -> { System.out.println(employee + " => " + salary);  });
        
        
        // import a BIG-3d matrix from java to GMS //
        int limitMAX=30; // -- 2 147 483 648 --// //-- 300 works--//
        
        System.out.println(" Big Sequence in java");
        String[] jv_sMAX = new String[limitMAX];  // -- big sequenece --//
        for (int i=0; i<limitMAX; i++)
        	jv_sMAX[i]=String.valueOf(i+1);
        
        System.out.println(" Big Sequence in gms");
        GAMSSet jsMAX = info.JGamsEnv.getgDB().addSet("bigseq1", 1, "set MAX");
	    for(String ele_jd_js0 : jv_sMAX) jsMAX.addRecord(ele_jd_js0);
	    GAMSSet jsMAX2 = info.JGamsEnv.getgDB().addSet("bigseq2", 1, "set MAX");
	    for(String ele_jd_js0 : jv_sMAX) jsMAX2.addRecord(ele_jd_js0);
	    GAMSSet jsMAX3 = info.JGamsEnv.getgDB().addSet("bigseq3", 1, "set MAX");
	    for(String ele_jd_js0 : jv_sMAX) jsMAX3.addRecord(ele_jd_js0);
	    
	    System.out.println(" IN # Big 3d-matrix as parameter in java");
        Map<Vector<String>, Double> jd_jp_mat3dBIG = new HashMap<Vector<String>, Double>(); // --big 3d matrix as parameter --//
	    {
	    	for (long i=1; i<limitMAX; i++){
	    		for (long j=1; j<limitMAX; j++) {
	    			for (long k=1; k<limitMAX; k++) {
	    				jd_jp_mat3dBIG.put( new Vector<String>( Arrays.asList(new String[]{String.valueOf(i), String.valueOf(j),  String.valueOf(k) }) ), Double.valueOf(i+j+k+5));
	    			}
	    			
	    		}
	    	}
	    } 
	    System.out.println(" Out # Big 3d-matrix as parameter in java");
	    Iterator <Map.Entry<Vector<String>, Double>> kvIter_3DBig = jd_jp_mat3dBIG.entrySet().iterator(); 
        while (kvIter_3DBig.hasNext() && limitMAX < 50 ) {
        	Map.Entry<Vector<String>, Double> entryBig = kvIter_3DBig.next();
        	System.out.println("### jd_jp_mat3d_BIG : "+ entryBig.getKey() + " => " + entryBig.getValue());
        }
        	    
	    System.out.println(" Big 3d-matrix as parameter in gms");
	    GAMSParameter vp3BIG = info.JGamsEnv.getgDB().addParameter("jp3matBIG",  "data jp3matBIG : supplied from include datafile", jsMAX, jsMAX, jsMAX);
        for(Vector<String> ele_vp3B : jd_jp_mat3dBIG.keySet() )
            vp3BIG.addRecord(ele_vp3B).setValue( jd_jp_mat3dBIG.get(ele_vp3B).doubleValue() ); 
        
        System.out.println(" Big sequence & 3d-matrix imported into GMS from Java! ");
        
        
	    return info.JGamsEnv.getgDB().getName();
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	
	//-- Tensor [ 79 DKF x 120 Months x 100 Instr x 10 Years] = 9 million --//
	
	
} //-- class JGamsData --//
