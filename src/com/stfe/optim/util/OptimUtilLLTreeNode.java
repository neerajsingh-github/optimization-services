
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
////////////////////////////////////////////////////////////
  
	
    public class OptimUtilLLTreeNode<T> implements Iterable<OptimUtilLLTreeNode<T>> {
    	
		public T data;
		public OptimUtilLLTreeNode<T> parent;
		public List<OptimUtilLLTreeNode<T>> children;

		public boolean isRoot() {
			return parent == null;
		}

		public boolean isLeaf() {
			return children.size() == 0;
		}

		private List<OptimUtilLLTreeNode<T>> elementsIndex;

		public OptimUtilLLTreeNode(T data) {
			this.data = data;
			
			//search is inefficient with LinkedList -- ArrayList might be better option --//
			//this.children = new ArrayList<Node<T>>();
			
			this.children = new LinkedList<OptimUtilLLTreeNode<T>>();
			this.elementsIndex = new LinkedList<OptimUtilLLTreeNode<T>>();
			this.elementsIndex.add(this);
			
		}

		public OptimUtilLLTreeNode<T> addChild(T child) {
			OptimUtilLLTreeNode<T> childNode = new OptimUtilLLTreeNode<T>(child);
			childNode.parent = this;
			this.children.add(childNode);
			this.registerChildForSearch(childNode);
			return childNode;
		}

		public int getLevel() {
			if (this.isRoot())
				return 0;
			else
				return parent.getLevel() + 1;
		}

		private void registerChildForSearch(OptimUtilLLTreeNode<T> node) {
			elementsIndex.add(node);
			if (parent != null)
				parent.registerChildForSearch(node);
		}

		public OptimUtilLLTreeNode<T> findOptimUtilLLTreeNode(Comparable<T> cmp) {
			for (OptimUtilLLTreeNode<T> element : this.elementsIndex) {
				T elData = element.data;
				if (cmp.compareTo(elData) == 0)
					return element;
			}

			return null;
		}

		@Override
		public String toString() {
			return data != null ? data.toString() : "[data null]";
		}

		@Override
		public Iterator<OptimUtilLLTreeNode<T>> iterator() {
			OptimUtilLLTreeNodeIteration<T> iter = new OptimUtilLLTreeNodeIteration<T>(this);
			return iter;
		}
		 
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    