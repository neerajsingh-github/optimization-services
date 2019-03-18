
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
import java.util.*;

import static java.lang.System.*;
////////////////////////////////////////////////////////////
  
//int tx_numBRVRawDates= optimArrayListUtil.sizeUniqueArr(this.bondsRVJulDateRaw);

	
    public class optimArrayListUtil {
    	

    	public static int sizeUniqueArr(double[] arr) {
    		
    		ArrayList<Double> store = new ArrayList<Double>();
    		for (int n = 0; n < arr.length; n++){
    			if (!store.contains(arr[n])){ // if numbers[n] is not in store, then add it
    				store.add(arr[n]);
    			}
    		}
    		return store.size();
    	}
    	
    	
    	public static int sizeUniqueArr2D(double[][] arr2D) {
    		if ( (arr2D.length < 1) || (arr2D[0].length != 1)) return -1;
    		double[] arr = new double[arr2D.length];
    		for (int i=0; i< arr2D.length; i++)
    			arr[i] = arr2D[i][0];
    				
    		ArrayList<Double> store = new ArrayList<Double>();
    		for (int n = 0; n < arr.length; n++){
    			if (!store.contains(arr[n])){ //-- if numbers[n] isn't in store, add ---//
    				store.add(arr[n]);
    			}
    		}
    		return store.size();
    	}
    	
    	
    	public static double[] getUniqueArr(double[] arr) {
    		
    		ArrayList<Double> store = new ArrayList<Double>();
    		for (int n = 0; n < arr.length; n++){
    			if (!store.contains(arr[n])){ // if numbers[n] is not in store, then add it
    				store.add(arr[n]);
    			}
    		}
    		 
    		double[] unique = new double[store.size()];
    			for (int n = 0; n < store.size(); n++){
    				unique[n] = store.get(n);
    			}
    		//double[] uniqueArr = new HashSet<double[]>(Arrays.asList(arr)).toArray(new double[0]);
    		return unique;
    	}
    	
    	
    	public static double[] getUniqueArrSet(double[] arr) {
    		
    		ArrayList<Double> list = new ArrayList<Double>();
    		
    		Set<Double> hash = new HashSet<>();
    		hash.addAll(list);
    		list.clear();
    		list.addAll(hash);
    		
    		double[] unique = new double[hash.size()];
    		Double[] uniqueCl = new Double[hash.size()];
    		uniqueCl = list.toArray(uniqueCl);
    		for (int i=0; i<hash.size(); i++)
    			unique[i] = (double) uniqueCl[i];		
    			
    		//double[] uniqueArr = new HashSet<double[]>(Arrays.asList(arr)).toArray(new double[0]);
    		return unique;
    	}
    	
    	
    	//int tx_numBRVRawRowsEachDate=optimArrayListUtil.numRedundantData(this.bondsRVJulDateRaw, this.bondsRVJulDateRaw[0]);
    	
    	public static int numRedundantData(double[] arr, double data) {
    		int count=0;
    		for (int i=0; i<arr.length; i++) {
    			if (arr[i] == data) count++;
    		}
    		return count;
    	}
    	
    	public static int numRedundantData2D(double[][] arr2D, double data) {
    		if ( (arr2D.length < 1) || (arr2D[0].length != 1)) return -1;
    		double[] arr = new double[arr2D.length];
    		for (int i=0; i< arr2D.length; i++)
    			arr[i] = arr2D[i][0];
    		
    		int count=0;
    		for (int i=0; i<arr.length; i++) {
    			if (arr[i] == data) count++;
    		}
    		return count;
    	}
    	
    	
    	//--- For primitive types ---//
    	public static boolean isValInArray(int[] arr, int value) {
   		 boolean flag=false;
   		 for(int i=0;i<arr.length;i++)
   			 if(arr[i]==value) {
   				 flag=true;
   				 break;
   			 }
   		 return flag;
    	}
    	
    	public static int getArrayIndexI(int[] arr, int value) {
    		int k=-1;
    		for(int i=0;i<arr.length;i++)
    			if(arr[i]==value) {
    				k=i;
    				break;
    			 }
    				 
    		return k;
    	}
    	
    	public static int getArrayIndexD(double[] array, double value) {
    		int k=-1;
    		for(int i=0; i<array.length; i++) 
    			if(array[i] == value) {
    		        k=i;
    		        break;
    			}
    		return k;
    	}
    	 
    	
    	/* --- only J-V8 ---/
    	public static <T> T[] makeUniqueObject(T... values)
    	{
    	    return Arrays.stream(values).distinct().toArray(new IntFunction<T[]>()
    	    {

    	        @Override
    	        public T[] apply(int length)
    	        {
    	            return (T[]) Array.newInstance(values.getClass().getComponentType(), length);
    	        }

    	    });
    	}
    	
    	public static double[] makeUnique(double[] arr)
    	{
    	    return Arrays.stream(arr).distinct().toArray();
    	}
    	
    	*/
    	
    	
    	public static boolean  isDistinctValues(Iterable<Object> objs){
    	    Set<Object> foundObjects = new HashSet<>();
    	    for(Object o : objs){
    	        if(foundObjects.contains(o)){
    	            return false;
    	        }
    	        foundObjects.add(o);
    	    }
    	    return true; 
    	}
    	
    	public static String[] getUniqueArr(String[] array) {
    		String[] unique = new HashSet<String>(Arrays.asList(array)).toArray(new String[0]);
    		return unique;
    	}
    	
    	
    	
    	
    	
    	
    	public static boolean isDistcintArray(final int[] arr) {
            
            int[] clone = arr.clone();
            Arrays.sort(clone);

            // search for a pair of equal values
            for (int i = 0; i < clone.length - 1; i++) {

                if (clone[i] == clone[i + 1]) {

                    return false;
                }
                
            }

            return true;
        }

    	
    	 
    	 
    	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    