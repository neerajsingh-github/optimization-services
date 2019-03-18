
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
////////////////////////////////////////////////////////////
  
	
    public class OptimUtilLLTreeNodeIteration<T> implements Iterator<OptimUtilLLTreeNode<T>> {
    	
		enum ProcessStages {
			ProcessParent, ProcessChildCurNode, ProcessChildSubNode
		}

		private OptimUtilLLTreeNode<T> OptimUtilLLTreeNode;

		public OptimUtilLLTreeNodeIteration(OptimUtilLLTreeNode<T> OptimUtilLLTreeNode) {
			this.OptimUtilLLTreeNode = OptimUtilLLTreeNode;
			this.doNext = ProcessStages.ProcessParent;
			this.childrenCurNodeIter = OptimUtilLLTreeNode.children.iterator();
		}

		private ProcessStages doNext;
		private OptimUtilLLTreeNode<T> next;
		private Iterator<OptimUtilLLTreeNode<T>> childrenCurNodeIter;
		private Iterator<OptimUtilLLTreeNode<T>> childrenSubNodeIter;

		@Override
		public boolean hasNext() {

			if (this.doNext == ProcessStages.ProcessParent) {
				this.next = this.OptimUtilLLTreeNode;
				this.doNext = ProcessStages.ProcessChildCurNode;
				return true;
			}

			if (this.doNext == ProcessStages.ProcessChildCurNode) {
				if (childrenCurNodeIter.hasNext()) {
					OptimUtilLLTreeNode<T> childDirect = childrenCurNodeIter.next();
					childrenSubNodeIter = childDirect.iterator();
					this.doNext = ProcessStages.ProcessChildSubNode;
					return hasNext();
				}

				else {
					this.doNext = null;
					return false;
				}
			}
			
			if (this.doNext == ProcessStages.ProcessChildSubNode) {
				if (childrenSubNodeIter.hasNext()) {
					this.next = childrenSubNodeIter.next();
					return true;
				}
				else {
					this.next = null;
					this.doNext = ProcessStages.ProcessChildCurNode;
					return hasNext();
				}
			}

			return false;
		}

		@Override
		public OptimUtilLLTreeNode<T> next() {
			return this.next;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		 
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    