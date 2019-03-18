
package com.stfe.optim.util;


/**
Sorts the matrix rows according to the order induced by the specified comparator.
The returned view is backed by this matrix, so changes in the returned view are reflected in this matrix, and vice-versa.
The algorithm compares two rows (1-d matrices) at a time, determinining whether one is smaller, equal or larger than the other.
To sort ranges use sub-ranging views. To sort columns by rows, use dice views. To sort descending, use flip views ...
<p>
<b>Example:</b>
<pre>
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
//import java.util.*; 

//JOM
import com.jom.*;

//cern colt //
import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.function.tdouble.*;
import cern.colt.function.*;
import cern.colt.matrix.*;
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.algo.*;  //.+ .DoubleDoubleFunction
//import cern.colt.matrix.linalg.Algebra;

import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;


//Apache Commons Math
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.geometry.euclidean.threed.*;  


////////////////////////////////////////////////////////////
	
    public class optimReduceRawDataMatrix {
    	
    	
    	//-- Bestand generation from Vol --//
    	public static double[][] genBestandFromVolVector(double[] volColVector, double[][][]RawBuaKastenData ) {
    		
    		if (volColVector.length==0)  return null; 
    		if ( (RawBuaKastenData.length==0) || (RawBuaKastenData[0].length==0) || (RawBuaKastenData[0][0].length==0)) return null;
    		if ( volColVector.length != RawBuaKastenData.length ) return null; 
    		
    		double[][] volRowMatrix = new double[1][volColVector.length];
    		double[][] retMatrix = new double[volColVector.length][RawBuaKastenData[0][0].length];
    		
    		OptimizationProblem jom = new OptimizationProblem();
    		jom.setInputParameter("volMatrix", new DoubleMatrixND(volRowMatrix));
    		jom.setInputParameter("BaukastenMatrix", new DoubleMatrixND(RawBuaKastenData));
    		jom.setInputParameter("VolMultBaukastenMatrix", "volMatrix * VolMultBaukastenMatrix" );
    		
    		jom.setInputParameter("VolMultBaukastenMatrix", "sum(VolMultBaukastenMatrix,1)" );
    		
    		// New Bestand = Bestand_Opt + Vol-Swap + Vol-Bond
    		retMatrix = (double[][]) jom.getInputParameter("VolMultBaukastenMatrix").toArray();
    		
    		return retMatrix;
    		
    	} // -- Bestand Vol Swap - Bond-- //
    	
    	
    	
    	//http://commons.apache.org/proper/commons-math/userguide/linear.html	
    	public static double[][] generateVolumeRawMatrix(double[][] volRowVector, double[][][]RawBuaKastenData ) {
    		if ( (volRowVector.length==0) || (volRowVector[0].length==0) ) return null; 
    		if ( (RawBuaKastenData.length==0) || (RawBuaKastenData[0].length==0) || (RawBuaKastenData[0][0].length==0)) return null;
    		if ( (volRowVector[0].length) != (RawBuaKastenData.length) ) return null; 
    		
    		double[][] retMatrix = new double[volRowVector[0].length][RawBuaKastenData[0][0].length];
    		RealMatrix vrm = MatrixUtils.createRealMatrix(volRowVector);
    		
    		RealMatrix brmpart = MatrixUtils.createRealMatrix(RawBuaKastenData[0].length,RawBuaKastenData[0][0].length);
    		RealMatrix brm =  MatrixUtils.createRealMatrix(RawBuaKastenData.length,RawBuaKastenData[0].length);
    		//RealMatrix brm =  new RealMatrix(RawBuaKastenData);  //Array2DRowRealMatrix(RawBuaKastenData);
    		//RealMatrix brmpart =   brmpart.createMatrix(RawBuaKastenData[0].length,RawBuaKastenData[0][0].length);
    		//RealMatrix brm =  brm.createMatrix(RawBuaKastenData.length,RawBuaKastenData[0].length);
    		
    		for (int i=0; i< RawBuaKastenData.length; i++) {
    			
    			brmpart.setSubMatrix(RawBuaKastenData[i], RawBuaKastenData[0].length, RawBuaKastenData[0][0].length);
    			
    			for (int j=0; j< RawBuaKastenData[0].length; j++) {
    			
    				brm.setColumnMatrix(j,  brmpart);
    			}
    			
    		}
    		RealMatrix rtm = vrm.multiply(brm);
    		retMatrix = rtm.getData();		
    		
    				
    		RealMatrix X = new BlockRealMatrix(3, 4 ); 
    		Vector3D br3vm = new Vector3D(3, 4, 9);   
    		    		
    		//RealMatrix rtm = vrm.multiply(brm);
    		System.out.println(rtm.getRowDimension());    
    		System.out.println(rtm.getColumnDimension());
    		
    		return retMatrix;
    	}
    	
    	
    	
    	//https://www.programcreek.com/java-api-examples/index.php?source_dir=tdq-studio-se-master/main/plugins/org.talend.libraries.jung/libs_src/colt_src/cern/colt/Arrays.java#//
    	//-- https://www.programcreek.com/java-api-examples/index.php?api=cern.colt.matrix.DoubleMatrix2D ---//
    	public static double[][] generateVolumeRawMatrixColt(double[][] volRowVector, double[][][]RawBuaKastenData ) {
    		
    		
    		double[][] retMatrix = new double[10][];
    		//final cern.colt.function.tdouble.DoubleFunction coltFunc = cern.jet.math.tdouble.DoubleFunctions.abs;   //jet.math.PlusMult.plusMult(0);
    		final cern.colt.function.tdouble.DoubleDoubleFunction fMult  = cern.jet.math.tdouble.DoubleFunctions.mult;
    		final cern.colt.function.tdouble.DoubleDoubleFunction fSum  = cern.jet.math.tdouble.DoubleFunctions.plus;
    		
    		final cern.jet.math.tdouble.DoubleFunctions coltDFunc;
    		final cern.jet.math.tdouble.DoubleFunctions funct; 
    		
    		//DoubleMatrix2D matA = cern.colt.matrix.tdouble.DoubleFactory2D.dense.make(volRowVector);
    		//DoubleMatrix3D matB = cern.colt.matrix.tdouble.DoubleFactory3D.dense.make(RawBuaKastenData);
    		
    		//3D matrics
    		// -- http://dst.lbl.gov/ACSSoftware/colt/api/cern/colt/matrix/package-summary.html //
    		//matB.aggregate(matA, fSum, fMult);
    		
    		
    		//cern.jet.math.Functions F = cern.jet.math.Functions.functions;
    		//matA.aggregate(matB, coltDFunc.plus, coltDFunc.mult);
    		
    		 
    		//--Algebra --//
    		DenseDoubleAlgebra alg = new DenseDoubleAlgebra();
    		//DoubleMatrix2D result = alg.zMult(matB,matB);    
    		
    		
    		//https://dst.lbl.gov/ACSSoftware/colt/api/cern/colt/matrix/DoubleMatrix2D.html//
    		// C = A x B ; Matrix multiplication:- C=Ax B
    		//zMult(B,C,1,0,false,false). 
    		
    		
    		
    		//cern.jet.math.tdouble.DoubleFunctions F1 = cern.jet.math.tdouble.DoubleFunctions.mult;  
    		//cern.jet.math.Functions F = cern.jet.math.Functions.functions; 
    		cern.jet.math.tdouble.DoubleFunctions funct2;
    		
    		// http://incanter.org/docs/parallelcolt/api/cern/colt/matrix/doc-files/function1.html //
    		//matA.aggregate(matB, fSum, fMult);
    		
    		
    		
    		/*
    		
    		//A.zMult(B,retMatrix);
    		B.aggregate(A, cern.jet.math.tdouble.DoubleFunctions.plus, cern.jet.math.tdouble.DoubleFunctions.mult);
    		B.assign
    		
    		//-- http://incanter.org/docs/parallelcolt/api/cern/colt/matrix/package-summary.html__//
    		//  http://incanter.org/docs/parallelcolt/api/cern/jet/math/tdouble/DoubleFunctions.html //
    		DoubleMatrix3D.   //.plus,Functions.sin,Functions.chain(Functions.square,Functions.cos));
    		
    		cern.jet.math.tdouble.DoubleFunctions df;
    		double res = df.pow(3).apply(2);
    		cern.jet.math.tdouble.DoublePlusMultFirst   .DoubleFunctions.div  DoubleDoubleFunction ddf;
    		*/
    		
    		//cern.jet.math.tdouble.DoublePlusMultFirst 
    		
    		 //cern.colt.function.tdouble.DoubleDoubleFunction ddf = cern.colt.function.tdouble.DoubleDoubleFunction.mult;
    		//System.out.println(res);
    		//retMatrix = cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose(DoubleMatrix2D PCRP1Raw);
    		
    		//double [][] PCRP1RawTranspose = cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra.transpose(DoubleMatrix2D PCRP1Raw);

    		/*
    		DoubleDoubleFunction mult = new DoubleDoubleFunction() {
    		    public double apply(double a, double b) { return a*b; }
    		}; 
    		
    		DoubleMatrix2D aVector = new DenseDoubleMatrix2D(volRowVector);
    		*/
    		
    		return retMatrix;
    	}
    	
    	
    	
    	
    	
    	
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    