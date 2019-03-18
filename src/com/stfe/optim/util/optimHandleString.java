
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

 
import java.io.*;
import java.util.*; 

import org.apache.commons.lang3.ArrayUtils;

////////////////////////////////////////////////////////////
  

    public class optimHandleString {
    	  	
    	
    	//-- compress a string  -- //
    	public static String compressString(String line) {
			//String compressedString = line.replaceAll("\\s+","");
			String compressedString = line.replaceAll(" ","");
			return compressedString;
		}
		
    	
    	public static int getIdxContainStringInArray(String[] valueArray, String word) {
    		int idx=-1;
    		
    		for (int i=0; i< valueArray.length; i++) {
    			if (valueArray[i].contains(word)) {
    				idx = i;
    				break;
    			}
    				
    		}
    		return idx;
		}
		
    	
    	
    	public static int getIdxMatchStringInArray(String[] valueArray, String word) {
    		int idx=-1;
    		
    		//idx = Arrays.asList(valueArray).lastIndexOf(word);
    		idx = Arrays.asList(valueArray).indexOf(word);
    		return idx;
		}
    	
    	public static int getIdxSearchStringInArray(String[] valueArray, String word) {
    		int idx=-1;
    		Arrays.sort(valueArray);
    		idx = Arrays.binarySearch(valueArray, word);
    		return idx;
		}
    	
    	
    	public static boolean matchStringInArray(String[] valueArray, String word) {
    		
    		boolean found = Arrays.asList(valueArray).contains(word);
    		
    		//-- use org.apache.commons.lang3.ArrayUtils ---//
			//boolean found = ArrayUtils.contains( valueArray, word );
    		
			 return found;
    	}
    	
    	
    	
    	////////////////////////////////////////////////////////////////////////////////
    	public interface IArrayUtility<T> {

    	    int find(T[] list, T item);

    	}// -- embedded interface ArrayUtility--//
    	public class ArrayUtility<T> implements IArrayUtility<T> {

    	    @Override
    	    public int find(T[] array, T search) {
    	        if(array == null || array.length == 0 || search == null) {
    	            return -1;
    	        }

    	        int position = 0;

    	        for(T item : array) {

    	            if(item.equals(search)) {
    	                return position;
    	            } else {
    	                ++position;
    	            }
    	        }

    	        return -1;
    	    }

    	} // -- embedded class ArrayUtility--//
    	////////////////////////////////////////////////////////////////////////////////
    	
    	
    	
	} // class over  //
    
 

////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    