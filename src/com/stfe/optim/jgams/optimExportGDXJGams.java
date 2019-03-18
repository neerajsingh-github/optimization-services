
package com.stfe.optim.jgams;

/*
 * (c) Copyright BRDF. All rights reserved. 
 *
 * Created on 10.03.2017
 *
 * @author NS
 *
 */
////////////////////////////////////////////////////////////

 
//import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
//import java.util.*; 
import java.text.SimpleDateFormat;
import java.util.*;

import com.gams.api.GAMSParameter;
import com.gams.api.GAMSSet;

import static java.lang.System.*;
////////////////////////////////////////////////////////////
  
	//int tx_numBRVRawDates= optimArrayListUtil.sizeUniqueArr(this.bondsRVJulDateRaw);
	//com.stfe.optim.jgams.JGamsEnv.GDXSessionON//
	//com.stfe.optim.jgams.optimExportGDXJGams//

    public class optimExportGDXJGams {
    	
    	private static  List<String> jg_set_arr1list = null;
    	private static  List<String> jg_set_arr2list = null;
    	private static  List<String> jg_set_arr3list = null;
    	private static  List<String> jg_set_arr4list = null;;
    	private static GAMSSet  jg_set1_gdx = null; 
    	private static GAMSSet  jg_set2_gdx = null;
    	private static GAMSSet  jg_set3_gdx = null;
    	private static GAMSSet  jg_set4_gdx = null;
    	
    	
    	public static void exportGDXSet(String[] data, String vargdx,  boolean display) {
    		if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
    			System.out.println(" ? GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
    			return;
    		}
    		//--List for set--//
    		List<String> jg_set_arrlist = new ArrayList<>();
    		jg_set_arrlist = Arrays.asList(data);
    		
    		//--Set in GDX--//
    		int setsize = data.length;
    		if (setsize <1) return;
    		int ndim=1;
    		String setName = nameBuilderSet(vargdx, "idx", ndim,  setsize );
    		GAMSSet jg_set_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(setName, 1, setName);
    	    for(String ele_jg_set_arrlist : jg_set_arrlist) 
    	    	jg_set_gdx.addRecord(ele_jg_set_arrlist);
    	    
    	}
    	
    	
    	public static void exportGDXScalar(double data, String vargdx) {
    		
    		if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
    			System.out.println(" ? GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
    			return;
    		}
    		int ndim=1;
    		String scalarName = nameBuilderScalarParam(vargdx);
    	    GAMSParameter scalarVar = com.stfe.optim.jgams.JGamsEnv.getgDB().addParameter(scalarName, scalarName);
    	    scalarVar.addRecord().setValue(data); 
    	}
    	
    	
    	
    	//public static void exportGDXVector(double[] data, int vecsize, String vargdx,  boolean display) {
    	public static void exportGDXVector(double[] data, String vargdx,  boolean display) {
    		//Check if GDX-session already exists - JGams Env//
    		if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
    			System.out.println(" ? GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
    			return;
    		}
    		int vecsize = data.length;
    		if ( (vecsize <1) ) return;
    		int ndim=1;
    		//-- create empty arrXlist ; create empty set --//
    		com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr1list = new ArrayList<>();
    		
    		String vargdx_row1d_set_name = nameBuilderSetForParam(vargdx, "vec", ndim, vecsize );
    		com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_row1d_set_name, 1, vargdx_row1d_set_name);
    		
    		//-- create set for parameter --//
    		com.stfe.optim.jgams.optimExportGDXJGams.paramSetBuilder(vargdx, ndim, vecsize, 0, 0, 0);
    		    	    
    	    //--Map for parameter--//
	    	Map<String, Double> jg_param_map = new HashMap<String, Double>();
			{
				for (int i=0; i <vecsize; i++) {
					jg_param_map.put(String.valueOf(i), Double.valueOf(data[i]));
			    }
		    }
			//--Parameter in GDX--//
			String vargdx_prm_name = nameBuilderParam(vargdx, "vec", ndim, 1);
			GAMSParameter jg_param_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addParameter(vargdx_prm_name, vargdx_prm_name, 
	        																com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx);
	        for(String ele_jg_set_arrlist : com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr1list)
	        	jg_param_gdx.addRecord(ele_jg_set_arrlist).setValue(jg_param_map.get(ele_jg_set_arrlist) ); 
	        
	        if (display) {
	        	int cnt=0;
	        	System.out.println(" Displaying " + vargdx_prm_name);
	     	    Iterator <Map.Entry<String, Double>> jg_param_map_iter = jg_param_map.entrySet().iterator(); 
	             while (jg_param_map_iter.hasNext() && (cnt++ < 20) ) {
	             	Map.Entry<String, Double> jg_param_map_entry = jg_param_map_iter.next();
	             	System.out.println(vargdx_prm_name+ ": "+ jg_param_map_entry.getKey() + " => " + jg_param_map_entry.getValue());
	             }
	        }
	            
	    } //-- eportGDXVector --//
    	
		    	
		//public static void exportGDXMatrix2D(double[][] data, int rowsize, int colsize, String vargdx, boolean display){
    	public static void exportGDXMatrix2D(double[][] data, String vargdx, boolean display){
			if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
    			System.out.println(" ? GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
    			return;
    		}
			int rowsize = data.length;
    		int colsize = data[0].length;
    	
			if ( (rowsize <1) || (colsize <1) ) return;
			int ndim=2;
			//-- create empty arrXlist ; create empty set --//
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr1list = new ArrayList<>();
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr2list = new ArrayList<>();
			
			String vargdx_row1d_set_name = nameBuilderSetForParam(vargdx, "row", ndim, rowsize );
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_row1d_set_name, 1, vargdx_row1d_set_name);

			String vargdx_col2d_set_name = nameBuilderSetForParam(vargdx, "col", ndim, colsize );
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set2_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_col2d_set_name, 1, vargdx_col2d_set_name);
    		
	        //-- create set for parameter --//
	        com.stfe.optim.jgams.optimExportGDXJGams.paramSetBuilder(vargdx, ndim, rowsize, colsize, 0, 0);
			
    		
    	    //--Map for parameter--//
	    	Map<Vector<String>, Double> jg_param_map = new HashMap<Vector<String>, Double>();
			{
				for (int i=0; i < rowsize; i++) {
					for (int j=0; j < colsize; j++) {
						jg_param_map.put( new Vector<String>( Arrays.asList(new String[]{String.valueOf(i), String.valueOf(j)}) ), Double.valueOf(data[i][j]));
					}
					
			    }
		    }
			//--Parameter in GDX--//
			String vargdx_prm_name = nameBuilderParamXD(vargdx, "arr", ndim, rowsize, colsize, 0, 0); 
	        GAMSParameter jg_param_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addParameter(vargdx_prm_name, vargdx_prm_name, 
	        		com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx,com.stfe.optim.jgams.optimExportGDXJGams.jg_set2_gdx);
	        for(Vector<String> ele_jg_param_map : jg_param_map.keySet() )
	        	jg_param_gdx.addRecord(ele_jg_param_map).setValue(jg_param_map.get(ele_jg_param_map).doubleValue() );
	        
	        
	        if (display) {
	        	int cnt=0;
	        	System.out.println(" Displaying " + vargdx_prm_name);
	     	    Iterator <Map.Entry<Vector<String>, Double>> jg_param_map_iter = jg_param_map.entrySet().iterator(); 
	             while (jg_param_map_iter.hasNext() && (cnt++ < 20) ) {
	             	Map.Entry<Vector<String>, Double> jg_param_map_entry = jg_param_map_iter.next();
	             	System.out.println(vargdx_prm_name+ ": "+ jg_param_map_entry.getKey() + " => " + jg_param_map_entry.getValue());
	             }
	        }
	        
		}//-- exportGDXMatrix2D--//
		
		
    	//public static void exportGDXMatrix3D(double[][][] data,  int rowsize, int colsize, int tabsize, String vargdx, boolean display){
		public static void exportGDXMatrix3D(double[][][] data, String vargdx, boolean display){
    		if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
    			System.out.println(" ? GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
    			return;
    		}
    		int rowsize = data.length;
    		int colsize = data[0].length;
    		int tabsize = data[0][0].length;
    		if ( (tabsize <1) || (rowsize <1) || (colsize <1) ) return;
    		int ndim=3;
    		//-- create empty arrXlist ; create empty set --//
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr1list = new ArrayList<>();
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr2list = new ArrayList<>();
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr3list = new ArrayList<>();
			
			String vargdx_row1d_set_name = nameBuilderSetForParam(vargdx, "row", ndim, rowsize );
    		com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_row1d_set_name, 1, vargdx_row1d_set_name);
    		
			String vargdx_col2d_set_name = nameBuilderSetForParam(vargdx, "col", ndim, colsize );
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set2_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_col2d_set_name, 1, vargdx_col2d_set_name);
    		
			String vargdx_tab3d_set_name = 	nameBuilderSetForParam(vargdx, "tab", ndim, tabsize );
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set3_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_tab3d_set_name, 1, vargdx_tab3d_set_name);
					
			
	        //-- create set for parameter --//
	        com.stfe.optim.jgams.optimExportGDXJGams.paramSetBuilder(vargdx, ndim, rowsize, colsize, tabsize, 0);
    		
    		//--Map for parameter--//
	    	Map<Vector<String>, Double> jg_param_map = new HashMap<Vector<String>, Double>();
			{
				for (int i=0; i < rowsize; i++) {
					for (int j=0; j < colsize; j++) {
						for (int k=0; k < tabsize; k++) {
							jg_param_map.put( new Vector<String>( Arrays.asList(new String[]{String.valueOf(i), String.valueOf(j), String.valueOf(k)}) ), Double.valueOf(data[i][j][k]));
						}
					}
					
			    }
		    }
			//--Parameter in GDX--//
			String vargdx_prm_name = nameBuilderParamXD(vargdx, "arr", ndim, rowsize, colsize, tabsize, 0); 
	        GAMSParameter jg_param_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addParameter(vargdx_prm_name, vargdx_prm_name, 
	        				com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx,com.stfe.optim.jgams.optimExportGDXJGams.jg_set2_gdx , 
	        				com.stfe.optim.jgams.optimExportGDXJGams.jg_set3_gdx);
	        for(Vector<String> ele_jg_param_map : jg_param_map.keySet() )
	        	jg_param_gdx.addRecord(ele_jg_param_map).setValue(jg_param_map.get(ele_jg_param_map).doubleValue() );
	        
	        
	        if (display) {
	        	int cnt=0;
	        	System.out.println(" Displaying " + vargdx_prm_name);
	     	    Iterator <Map.Entry<Vector<String>, Double>> jg_param_map_iter = jg_param_map.entrySet().iterator(); 
	             while (jg_param_map_iter.hasNext() && (cnt++ < 20) ) {
	             	Map.Entry<Vector<String>, Double> jg_param_map_entry = jg_param_map_iter.next();
	             	System.out.println(vargdx_prm_name+ ": "+ jg_param_map_entry.getKey() + " => " + jg_param_map_entry.getValue());
	             }
	        }
    		
		}
    	
    	//public static void exportGDXMatrix4D(double[][][][] data,  int rowsize, int colsize, int tabsize, int fd4size, String vargdx, boolean display){
    	public static void exportGDXMatrix4D(double[][][][] data, String vargdx, boolean display){
    		if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
    			System.out.println(" ? GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
    			return;
    		}	
    		int rowsize = data.length;
    		int colsize = data[0].length;
    		int tabsize = data[0][0].length;
    		int fd4size = data[0][0][0].length;
    		if ( (fd4size <1) || (tabsize <1) || (rowsize <1) || (colsize <1) ) return;
    		int ndim=4;
    		
    		//-- create empty arrXlist ; create empty set --//
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr1list = new ArrayList<>();
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr2list = new ArrayList<>();
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr3list = new ArrayList<>();
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr4list = new ArrayList<>();
			
			String vargdx_row1d_set_name = nameBuilderSetForParam(vargdx, "row", ndim, rowsize );
    		com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_row1d_set_name, 1, vargdx_row1d_set_name);
    		
			String vargdx_col2d_set_name = nameBuilderSetForParam(vargdx, "col", ndim, colsize );
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set2_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_col2d_set_name, 1, vargdx_col2d_set_name);
    		
			String vargdx_tab3d_set_name = 	nameBuilderSetForParam(vargdx, "tab", ndim, tabsize );
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set3_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_tab3d_set_name, 1, vargdx_tab3d_set_name);
			
			String vargdx_fd4d_set_name =	nameBuilderSetForParam(vargdx, "fd", ndim, fd4size );
			com.stfe.optim.jgams.optimExportGDXJGams.jg_set4_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(vargdx_fd4d_set_name, 1, vargdx_fd4d_set_name);
			
			com.stfe.optim.jgams.optimExportGDXJGams.paramSetBuilder(vargdx, ndim, rowsize, colsize, tabsize, fd4size);
			
    		//--Map for parameter--//
	    	Map<Vector<String>, Double> jg_param_map = new HashMap<Vector<String>, Double>();
			{
				
				for (int i=0; i < rowsize; i++) {
					for (int j=0; j < colsize; j++) {
						for (int k=0; k < tabsize; k++) {
							for (int l=0; l < fd4size; l++) {
								jg_param_map.put( new Vector<String>( Arrays.asList(new String[]{String.valueOf(i), String.valueOf(j), String.valueOf(k), String.valueOf(l)}) ), 
																		Double.valueOf(data[i][j][k][l]));
							}
							
						}
					}
					
			    }
		    }
			//--Parameter in GDX--//
			String vargdx_prm_name = nameBuilderParamXD(vargdx, "arr", ndim, rowsize, colsize, tabsize, fd4size);
	        GAMSParameter jg_param_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addParameter(vargdx_prm_name, vargdx_prm_name, 
	        				com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx,com.stfe.optim.jgams.optimExportGDXJGams.jg_set2_gdx , 
	        				com.stfe.optim.jgams.optimExportGDXJGams.jg_set3_gdx, com.stfe.optim.jgams.optimExportGDXJGams.jg_set4_gdx);
	        for(Vector<String> ele_jg_param_map : jg_param_map.keySet() )
	        	jg_param_gdx.addRecord(ele_jg_param_map).setValue(jg_param_map.get(ele_jg_param_map).doubleValue() );
	        
	        
	        if (display) {
	        	int cnt=0;
	        	System.out.println(" Displaying " + vargdx_prm_name);
	     	    Iterator <Map.Entry<Vector<String>, Double>> jg_param_map_iter = jg_param_map.entrySet().iterator(); 
	             while (jg_param_map_iter.hasNext() && (cnt++ < 20) ) {
	             	Map.Entry<Vector<String>, Double> jg_param_map_entry = jg_param_map_iter.next();
	             	System.out.println(vargdx_prm_name+ ": "+ jg_param_map_entry.getKey() + " => " + jg_param_map_entry.getValue());
	             }
	        }	
        		
    		
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
    	
    	private static void paramSetBuilder(String vargdx, int ndim, int rowsize, int colsize, int tabsize, int fd4size) {
    		
    		if ( (ndim < 1) && (ndim > 4) ) {
    			System.out.println("paramSetBuilder: Invalid dimension ? : Supported only <1-4> !");
    			return;
    		}
    		
    		if (ndim>0) {
    		String vargdx_row1d_set_name = nameBuilderSetForParam(vargdx, "row", ndim, rowsize );
    		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXSetForParam(rowsize, vargdx_row1d_set_name, 
    					com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr1list, com.stfe.optim.jgams.optimExportGDXJGams.jg_set1_gdx,  false);
    		}
    		if (ndim > 1) {
    		String vargdx_col2d_set_name = nameBuilderSetForParam(vargdx, "col", ndim, colsize );
    		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXSetForParam(colsize, vargdx_col2d_set_name, 
    					com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr2list, com.stfe.optim.jgams.optimExportGDXJGams.jg_set2_gdx,  false);
    		}
    		if (ndim > 2) {
    		String vargdx_tab3d_set_name = 	nameBuilderSetForParam(vargdx, "tab", ndim, tabsize );
    		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXSetForParam(tabsize, vargdx_tab3d_set_name, 
    					com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr3list, com.stfe.optim.jgams.optimExportGDXJGams.jg_set3_gdx,  false);
    		}
    		if (ndim > 3) {
    		String vargdx_fd4d_set_name =	nameBuilderSetForParam(vargdx, "fd", ndim, fd4size );
    		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXSetForParam(fd4size, vargdx_fd4d_set_name, 
    					com.stfe.optim.jgams.optimExportGDXJGams.jg_set_arr4list, com.stfe.optim.jgams.optimExportGDXJGams.jg_set4_gdx,  false);
    		}
    	}
		
    	private static void exportGDXSetForParam(int vecsize, String setfinalname, List<String> jg_set_arrlist, GAMSSet  jg_set_gdx, boolean display) {
    		if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
    			System.out.println(" ? GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
    			return;
    		}
    		//--List for set--//
    		String[] jg_set_arr = new String[vecsize];
    		for (int i=0; i<vecsize; i++) {
    			jg_set_arr[i]= Integer.toString(i);
    		}
    		
    		//jg_set_arrlist = new ArrayList<>(); // Initialized from caller//
    		jg_set_arrlist = Arrays.asList(jg_set_arr);
    		
    		//--Set in GDX--//
    		//jg_set_gdx = com.stfe.optim.jgams.JGamsEnv.getgDB().addSet(setfinalname, 1, setfinalname); // Initialized from caller//
    	    for(String ele_jg_set_arrlist : jg_set_arrlist) 
    	    	jg_set_gdx.addRecord(ele_jg_set_arrlist);
    	    
    	}
    	
    	
    	private static String nameBuilderSetForParam(String vargdx, String fldtype, int ndim, int dsize ) { //-- fldtype= row/col/tab/fld4/ --//
    		//vargdx + "_vec1d_" + String.valueOf(vecsize)+"_param";
    		String fname = vargdx+"_"+ fldtype + String.valueOf(ndim) + "D_" + String.valueOf(dsize) +"_pSet";   
    		return fname;
    	}
    	private static String nameBuilderParam(String vargdx, String dtype, int ndim, int dsize ) {
    		String fname = vargdx+"_"+ dtype + String.valueOf(ndim) + "D_" + String.valueOf(dsize) +"_P";   
    		return fname;
    	}
    	private static String nameBuilderParamXD(String vargdx, String dtype, int ndim, int rowsize,  int colsize, int tabsize, int fd4size  ) {
    		String fname="";
    		if (ndim == 2)
    			fname = vargdx+"_"+ dtype + String.valueOf(ndim) + "D_" + String.valueOf(rowsize)+ "_" + String.valueOf(colsize) +"_P";
    		if (ndim == 3)
    			fname = vargdx+"_"+ dtype + String.valueOf(ndim) + "D_" + String.valueOf(rowsize)+ "_" + String.valueOf(colsize) + "_" + String.valueOf(tabsize) +"_P";
    		if (ndim == 4)
    			fname = vargdx+"_"+ dtype + String.valueOf(ndim) + "D_" + String.valueOf(rowsize)+ "_" + String.valueOf(colsize) + 
    																	"_" + String.valueOf(tabsize) + "_" + String.valueOf(fd4size) +"_P";
    		return fname;
    	}
    	
    	private static String nameBuilderSet(String vargdx, String dtype, int ndim, int dsize ) {
    		//String fname = vargdx+"_"+ dtype + String.valueOf(ndim) + "D_" + String.valueOf(dsize) +"_S";   
    		String fname = vargdx+"_"+ String.valueOf(dsize) +"_S";
    		return fname;
    	}
    	private static String nameBuilderScalarParam(String vargdx ) {
    		String fname = vargdx+"_SC";   
    		return fname;
    	}
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    	 
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	