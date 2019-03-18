
package com.stfe.optim.util;

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
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;
import java.util.*;
import static java.lang.System.*;

////////////////////////////////////////////////////////////
  
    public class OptimUtilLLTreeNodeValidate {
    	
		//*************** Validate Tree for OptimApp Data ***************//
		public static OptimUtilLLTreeNode<String> initScalarOptimTree(int numYear, int numInstr) {
		
			OptimUtilLLTreeNode<String> root = new OptimUtilLLTreeNode<String>("root");
			{
				OptimUtilLLTreeNode<String> ndyr1 = root.addChild("year1");
				{
					OptimUtilLLTreeNode<String> gebexp1y1 = ndyr1.addChild("GEB-YR1");
					OptimUtilLLTreeNode<String> nkbexp2y1 = ndyr1.addChild("NKB-YR1");
					OptimUtilLLTreeNode<String> dti1exp3y1 = ndyr1.addChild("ZERO");
					OptimUtilLLTreeNode<String> dti2exp3y1 = ndyr1.addChild("ZERO");
					OptimUtilLLTreeNode<String> dvi1exp3y1 = ndyr1.addChild("DV-INST1-YR1");
					OptimUtilLLTreeNode<String> dvi2exp3y1 = ndyr1.addChild("DV-INST1-YR1");
					OptimUtilLLTreeNode<String> dyi1exp3y1 = ndyr1.addChild("DY-INST1-YR1");
					OptimUtilLLTreeNode<String> dyi2exp3y1 = ndyr1.addChild("DY-INST2-YR1");
				}
				//OptimUtilLLTreeNode<String> ndyr2 = root.addChild("year2");
				OptimUtilLLTreeNode<String> ndyr2 = ndyr1.addChild("year2");
				{
					OptimUtilLLTreeNode<String> gebexp1y2 = ndyr1.addChild("GEB-YR2");
					OptimUtilLLTreeNode<String> nkbexp2y2 = ndyr1.addChild("NKB-YR2");
					OptimUtilLLTreeNode<String> dti1exp3y2 = ndyr1.addChild("DELTA-INST1-YR2");
					OptimUtilLLTreeNode<String> dti2exp3y2 = ndyr1.addChild("DELTA-INST2-YR2");
					OptimUtilLLTreeNode<String> dvi1exp3y2 = ndyr1.addChild("DV-INST1-YR2");
					OptimUtilLLTreeNode<String> dvi2exp3y2 = ndyr1.addChild("DV-INST2-YR2");
					OptimUtilLLTreeNode<String> dyi1exp3y2 = ndyr1.addChild("DY-INST1-YR2");
					OptimUtilLLTreeNode<String> dyi2exp3y2 = ndyr1.addChild("DY-INST2-YR2");
				}
				// Build the dynamic tree //
				OptimUtilLLTreeNode<String> ndyr3 = ndyr1.addChild("year3");
				{
					OptimUtilLLTreeNode<String> gebexp1y3 = ndyr1.addChild("GEB-YR3");
					OptimUtilLLTreeNode<String> nkbexp2y3 = ndyr1.addChild("NKB-YR3");
					OptimUtilLLTreeNode<String> dti1exp3y3 = ndyr1.addChild("DELTA-INST1-YR3");
					OptimUtilLLTreeNode<String> dti2exp3y3 = ndyr1.addChild("DELTA-INST2-YR3");
					OptimUtilLLTreeNode<String> dvi1exp3y3 = ndyr1.addChild("DV-INST1--YR3");
					OptimUtilLLTreeNode<String> dvi2exp3y3 = ndyr1.addChild("DV-INST2-YR3");
					OptimUtilLLTreeNode<String> dyi1exp3y3 = ndyr1.addChild("DY-INST1-YR3");
					OptimUtilLLTreeNode<String> dyi2exp3y3 = ndyr1.addChild("DY-INST2-YR3");
				}
			
			}
			return root;
		}
		
		
		
		
		/*
		/// START - Dynamically building the parents and children ///
		private static OptimUtilLLTreeNode<String> buildChildExp( OptimUtilLLTreeNode<String>[]  parentyears, int numYear, int[] numInstr, int level) {					
			OptimUtilLLTreeNode<String>[][] expkids=null;
			if (level == 0) {
				for (int i=0; i<numYear<i++) {
					OptimUtilLLTreeNode<String>[][] expstatkids= new OptimUtilLLTreeNode<String>[numYear][2]; // for GEB and NKB //	
					OptimUtilLLTreeNode<String>[][] expkids= new OptimUtilLLTreeNode<String>[numYear][numInstr[i] * 3];// for each instruments - DELTA, DV, DY // 
					for (int j=0; j<numInstr[i]; j++) {
						OptimUtilLLTreeNode<String> expkids[level][j] = parentyears[level].addChild("DELTA-INST"+ Integer.toStringValue(j+1) +"-YR" + Integer.toStringValue(level+1));
						OptimUtilLLTreeNode<String> expkids[level][j] = parentyears[level].addChild("DV-INST"+ Integer.toStringValue(j+1) +"-YR" + Integer.toStringValue(level+1));
						OptimUtilLLTreeNode<String> expkids[level][j] = parentyears[level].addChild("DY-INST"+ Integer.toStringValue(j+1) +"-YR" + Integer.toStringValue(level+1));
					}
					OptimUtilLLTreeNode<String> expstatkids[level][j] = parentyears[level].addChild("GEB-YR" + Integer.toStringValue(level+1));
					OptimUtilLLTreeNode<String> expstatkids[level][j] = parentyears[level].addChild("NKB-YR" + Integer.toStringValue(level+1));
				}
				
			}
			else if (level < numYear) {
				for (int i=0; i<numYear<i++) {
					for (int j=0; j<numInstr[i]; j++) {
						OptimUtilLLTreeNode<String> expkids[level][j] = parentyears[level].addChild("DELTA-INST"+ Integer.toStringValue(j+1) +"-YR" + Integer.toStringValue(level+1));
						OptimUtilLLTreeNode<String> expkids[level][j] = parentyears[level].addChild("DV-INST"+ Integer.toStringValue(j+1) +"-YR" + Integer.toStringValue(level+1));
						OptimUtilLLTreeNode<String> expkids[level][j] = parentyears[level].addChild("DY-INST"+ Integer.toStringValue(j+1) +"-YR" + Integer.toStringValue(level+1));
					}
					OptimUtilLLTreeNode<String> expstatkids[level][j] = parentyears[level].addChild("GEB-YR" + Integer.toStringValue(level+1));
					OptimUtilLLTreeNode<String> expstatkids[level][j] = parentyears[level].addChild("NKB-YR" + Integer.toStringValue(level+1));
				}
				buildChildExp(parentyears, numYear, numInstr, level+1);
			}
			return expkids;
		}
		private static OptimUtilLLTreeNode<String> buildParentYear( OptimUtilLLTreeNode<String> root, int numYear, int[] numInstr, int level) {					
			OptimUtilLLTreeNode<String>[] ndyr= null;
			if (level == 0) {
				OptimUtilLLTreeNode<String>[] ndyr= new OptimUtilLLTreeNode<String>[numYear];
				ndyr[level] = root.addChild("year1");
				OptimUtilLLTreeNode<String> expstatkids[level][j] = parentyears[level].addChild("GEB-YR" + Integer.toStringValue(level+1));
					OptimUtilLLTreeNode<String> expstatkids[level][j] = parentyears[level].addChild("NKB-YR" + Integer.toStringValue(level+1));
			}
			else {
				if (level < numYear) {
					OptimUtilLLTreeNode<String> ndyr[level] = ndyr[level-1].addChild("year"+ Integer.toStringValue(level+1));
					buildParent(OptimUtilLLTreeNode<String>  root, numYear, numInstr,level+1);
				}
			}
			return ndyr;
		}
		public static OptimUtilLLTreeNode<String> getSetOptimTree(int numYear, int[] numInstr) {
			if (numYear < 1) return null;
			if (numInstr.length != numYear) return null;
			OptimUtilLLTreeNode<String> root = new OptimUtilLLTreeNode<String>("root");
			OptimUtilLLTreeNode<String>[] ndyr=buildParentYear(root, numYear, 0);
			OptimUtilLLTreeNode<String>[][] expkids=buildChildExp(ndyr, numYear, numInstr, 0); 			
			return root;
		}
		/// END - Dynamically building the parents and children ///
		
		*/
		
		
		
		//*************** Validate Tree with Data ***************//
		//--1--//
		public static OptimUtilLLTreeNode<String> setupTree() {
			
			OptimUtilLLTreeNode<String> root = new OptimUtilLLTreeNode<String>("root");
			{
				OptimUtilLLTreeNode<String> node0 = root.addChild("year1");
				OptimUtilLLTreeNode<String> node1 = root.addChild("year2");
				OptimUtilLLTreeNode<String> node2 = root.addChild("year3");
				{
					OptimUtilLLTreeNode<String> node20 = node2.addChild(null);
					OptimUtilLLTreeNode<String> node21 = node2.addChild("node21");
					{
						OptimUtilLLTreeNode<String> node210 = node21.addChild("node210");
						OptimUtilLLTreeNode<String> node211 = node21.addChild("node211");
					}
				}
				OptimUtilLLTreeNode<String> node3 = root.addChild("node3");
				{
					OptimUtilLLTreeNode<String> node30 = node3.addChild("node30");
				}
			}

			return root;
		}

		/*
		public static OptimUtilLLTreeNode<String> setupTreeYet() {
			OptimUtilLLTreeNode<String> root = new OptimUtilLLTreeNode<String>("root");
			{
				OptimUtilLLTreeNode<String> node0 = root.addChild("year1");
				OptimUtilLLTreeNode<String> node1 = root.addChild("year2");
				OptimUtilLLTreeNode<String> node2 = root.addChild("year3");
				{
					OptimUtilLLTreeNode<String> node20 = node2.addChild(null);
					OptimUtilLLTreeNode<String> node21 = node2.addChild("node21");
					{
						OptimUtilLLTreeNode<String> node210 = node20.addChild("node210");
					}
				}
			}

			return root;
		}
		*/
		
		
		// ************** Validate Tree with Iteration ***************//
		//--2--//
		public static void IterMain(String[] args) {
			//OptimUtilLLTreeNode<String> treeRoot = setupTree();
			OptimUtilLLTreeNode<String> treeRoot = initScalarOptimTree(3,2);
			
			for (OptimUtilLLTreeNode<String> node : treeRoot) {
				String indent = createIndent(node.getLevel());
				System.out.println(indent + node.data);
			}
		}
		private static String createIndent(int depth) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < depth; i++) {
				sb.append(' ');
			}
			return sb.toString();
		}
		
		
		// ************** Validate Tree with Searching ***************//
		//--3--//
		public static void searchMain(String[] args) {
		
			Comparable<String> searchCriteria = new Comparable<String>() {
				@Override
				public int compareTo(String treeData) {
					if (treeData == null)
						return 1;
					//boolean nodeOk = treeData.contains("210"); //--DV-INST2-YR3
					boolean nodeOk = treeData.contains("DY-INST2-YR3"); //--DV-INST2-YR3
					return nodeOk ? 0 : 1;
				}
			};

			//OptimUtilLLTreeNode<String> treeRoot = setupTree();
			OptimUtilLLTreeNode<String> treeRoot = initScalarOptimTree(3,2);
			OptimUtilLLTreeNode<String> found = treeRoot.findOptimUtilLLTreeNode(searchCriteria);

			System.out.println("Build Dynamic Exp for: " + found);
		}
			
			
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    