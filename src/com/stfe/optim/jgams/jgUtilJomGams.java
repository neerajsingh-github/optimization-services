
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


import com.jom.DoubleMatrixND;
import com.jom.Expression;
import com.jom.OptimizationProblem;




import java.util.*;
import java.text.SimpleDateFormat;

public class jgUtilJomGams {

	
	public static void jgInitGDXSessionForJOPS() {
		if (com.stfe.optim.jgams.JGamsEnv.GDXSessionExist())
			com.stfe.optim.jgams.JGamsEnv.GDXSessionOFF();
		
		com.stfe.optim.jgams.JGamsEnv jgEnv = com.stfe.optim.jgams.JGamsEnv.getInstance();
		jgEnv.initWorkSpace();
		com.stfe.optim.jgams.JGamsEnv.GDXSessionON("opti_jops_GDX");
	}
	public static void jgShutdownGDXSessionForJOPS() {
		if (! com.stfe.optim.jgams.JGamsEnv.GDXSessionExist()) {
			System.out.println(" ? Nothing to shutdown. GDXSession disabled: Activate with com.stfe.optim.jgams.JGamsEnv.GDXSessionON(GDXSessionName) ");
			return;
		}
		com.stfe.optim.jgams.JGamsEnv.GDXDump("opti_jops_GDX");
		com.stfe.optim.jgams.JGamsEnv.GDXDump();
		com.stfe.optim.jgams.JGamsEnv.GDXSessionOFF();
	}
	
	
	public static void jgExportJOMSymbolInGDX(OptimizationProblem jom, String constExpJOM, String expGDXName) {
		int[] arrsize = jom.parseExpression(constExpJOM).size();
		int ndim  =  jom.parseExpression(constExpJOM).getNumDim();
		
		String expGDXNameUpd = expGDXName.replaceAll("\\s+","");
		
		if (ndim==1) {
			double[] constExpJOMData =  (double[]) jom.parseExpression(constExpJOM).evaluateConstant().toArray();  
			com.stfe.optim.jgams.optimExportGDXJGams.exportGDXVector(constExpJOMData,  expGDXNameUpd,  false);
		}
		if (ndim==2) {
			double[][] constExpJOMData =  (double[][]) jom.parseExpression(constExpJOM).evaluateConstant().toArray();
			com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix2D(constExpJOMData, expGDXNameUpd,  false);
		}
		if (ndim==3) {
			double[][][] constExpJOMData =  (double[][][]) jom.parseExpression(constExpJOM).evaluateConstant().toArray();
			com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix3D(constExpJOMData, expGDXNameUpd,  false);
		}
		if (ndim==4) {
			double[][][][] constExpJOMData =  (double[][][][]) jom.parseExpression(constExpJOM).evaluateConstant().toArray();
			com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix4D(constExpJOMData, expGDXNameUpd,  false);
		}
	}//-- ojbfunc_optimize_jgExpportJOMSymbolInGDX --//
	
	
	public static void jgExportIntAsSetInGDX(int idata, String expGDXName) {
		String expGDXNameUpd = expGDXName.replaceAll("\\s+","");
		String[] constStrData= new String[idata];
		for (int i=0; i<idata; i++) {
			constStrData[i] = Integer.toString(i);
		}
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXSet(constStrData, expGDXNameUpd,  false);
	}
	
	
	public static void jgExportJOMSArr1DAsSetInGDX(OptimizationProblem jom, String constExpJOM, String expGDXName) {
		String expGDXNameUpd = expGDXName.replaceAll("\\s+","");
		double[] constExpJOMData =  (double[]) jom.parseExpression(constExpJOM).evaluateConstant().toArray();
		String[] constStrData= new String[constExpJOMData.length];
		for (int i=0; i<constExpJOMData.length; i++) {
			constStrData[i] = Double.toString(constExpJOMData[i]);
		}
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXSet(constStrData, expGDXNameUpd,  false);
	}
	
	public static void jgExportJOMSAsScalarInGDX(OptimizationProblem jom, double data, String expGDXName) {
		String expGDXNameUpd = expGDXName.replaceAll("\\s+","");
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXScalar(data, expGDXNameUpd);
		
	}  
	
	
	
	
	/*
	public static void jgExportArray1DInGDX(double[] data, String expGDXName) {
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXVector(data,  expGDXName,  false);
	}
	public static void jgExportArray2DInGDX(double[][] data, String expGDXName) {
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix2D(data,  expGDXName,  false);
	}
	public static void jgExportArray3DInGDX(double[][][] data, String expGDXName) {
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix3D(data,  expGDXName,  false);
	}
	public static void jgExportArray4DInGDX(double[][][][] data, String expGDXName) {
		com.stfe.optim.jgams.optimExportGDXJGams.exportGDXMatrix4D(data,  expGDXName,  false);
	}
	*/
	
}//-- class jgUtilJomGams --//
