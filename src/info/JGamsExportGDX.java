
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


public class JGamsExportGDX {

	//public static void exportGDXSet(int vecsize, String setfinalname, List<String> jg_set_arrlist, GAMSSet  jg_set_gdx, boolean display);
	
	public  void testExportGDXSet() {
		String[] strvec= {"0", "1", "2", "3", "4"};
		//public static void exportGDXSet(String[] data, String vargdx,  boolean display) {
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXSet(strvec, "testGDXSet", true); 
	}
		
	public  void testExportGDXScalar() {
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXScalar(111.0, "tcGDXScalar"); 
	}
	public  void testExportGDXVector() {
		double[] data={111,112,113,114,115};
		//com.stfe.optim.jgams.optimExportGDXJGams.exportGDXVector(data, 5, "tcGDXVector",  true);
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXVector(data, "tcGDXVector",  true);
	}
	public  void testExportGDX2D() {
		double[][] data={  {1, 111}, {1, 112}, {1, 113}, {1, 114}, {1, 115}  };
		//com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix2D(data, 5, 2,  "tcGDXArr2D",  true);
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix2D(data, "tcGDXArr2D",  true);
		
	}
		
	public  void testExportGDX3D() { //JGamsEnv jgEnv1//
		
		double[][][] data = new double[5][3][2];  
		for (int i=0; i<5; i++) {
			for (int j=0; j<3; j++) {
				for (int k=0; k<2; k++) {
					
					data[i][j][k]= 100+i+j+k;
				}
			}
		}
		//com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix3D(data, 5, 3, 2,  "tcGDXArr3D",  true);
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix3D(data, "tcGDXArr3D",  true);
	}
	
	
	public  void testExportGDX4D() { //JGamsEnv jgEnv1//
		
		double[][][][] data = new double[5][4][3][2];  
		for (int i=0; i<5; i++) {
			for (int j=0; j<4; j++) {
				for (int k=0; k<3; k++) {
					for (int l=0; l<2; l++) {
						data[i][j][k][l] = 100+i+j+k+l;	
					}
				}
			}
		}
		//com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix4D(data, 5, 4, 3, 2,  "tcGDXArr4D",  true);
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix4D(data, "tcGDXArr4D",  true);
	}
	
	public void testsuitesExportGDXTDArray() {
		System.out.println(" Initializing Workspace and processing further ...");

		
		this.testExportGDXSet();
		this.testExportGDXScalar();
		this.testExportGDXVector();
		this.testExportGDX2D();
		this.testExportGDX3D();
		this.testExportGDX4D();		
	}
	
	
	public void testsuitesExportGDXAll() {
		com.stfe.optim.jgams.JGamsEnv jgEnv = com.stfe.optim.jgams.JGamsEnv.getInstance();
		jgEnv.initWorkSpace();
		com.stfe.optim.jgams.JGamsEnv.GDXSessionON("TDArray");
		//com.stfe.optim.jgams.optimExportGDXJGams expGDX= new  com.stfe.optim.jgams.optimExportGDXJGams();
		this.testsuitesExportGDXTDArray();
		com.stfe.optim.jgams.JGamsEnv.GDXDump("tcGDXDataTest");
		com.stfe.optim.jgams.JGamsEnv.GDXDump();
		com.stfe.optim.jgams.JGamsEnv.GDXSessionOFF();
	}
	
	//-- Tensor [ 79 DKF x 120 Months x 100 Instr x 10 Years] = 9 million --//
	
	
} //-- class JGamsData --//
