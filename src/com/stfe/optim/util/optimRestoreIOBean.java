
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
//import java.util.*; 

import com.stfe.optim.config.initOptimAppConfig;
import com.stfe.optim.util.optimDeserializeBean;

////////////////////////////////////////////////////////////
  
	public class optimRestoreIOBean {
		
		public optimRestoreIOBean(){}
				
		//- Create Opt/Covar-Bean from its defined architecture wrt meta-specs-name: Meta-BeanArchSpecs-Covar: Meta-BeanArchSpecs-Opt //
		public double[][][] restoreAllBeansFromMeta(String metaname) {
		
			//String deffile="OXL_Sensis_Szenarios.xlsx";		
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			//String filename=filepath+deffile;
			
			//String filename=ofilepath+ofilename;
			
			String ofilepath = initOptimAppConfig.outfiledir;
			optimDeserializeBean deserialBean = new optimDeserializeBean(ofilepath);
			double[][][] beanRestored = null;
			
			try{
				
				//-- Get the Meta  Bean Specification --//
				String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean(metaname,null); //IBean-Meta-BeanArchSpecs-Opt-XL.ser			
				//System.out.println(" SpecBeanArch: " + beanArchSpecs.length);
				
				beanRestored = new double[beanArchSpecs.length][][];
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				//for (int slBean=0; slBean<2; slBean++ )
				{
										
													
					//--Init Bean Arch reading from serialized bean--//				
					beanRestored[slBean] = (double[][]) deserialBean.deserializeBeanAsPdouble(beanArchSpecs[slBean][0], null);
					System.out.println(" BeanName:Length= "+ beanArchSpecs[slBean][0] +" : " + beanRestored[slBean].length );
									
					
				} //-- All records MetBeanSpecs over --//


			}	//--try over--//	

			catch (Exception e) { 
				e.printStackTrace(); 
			}
			
			return beanRestored;
		}
		//////////////////////////////////////////////////////////
		
		
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Create Named Opt/Covar-Bean from its defined architecture wrt meta-specs-name: Meta-BeanArchSpecs-Covar: Meta-BeanArchSpecs-Opt //
		public double[][] restoreABeanFromMeta(String metaname, String abean){  
			
				//String deffile="OXL_Sensis_Szenarios.xlsx";		
				//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";				
				
				//String odeffile = initOptimAppConfig.outfilename;
				
				//String filename=ofilepath+odeffile;
				String ofilepath = initOptimAppConfig.outfiledir;
				optimDeserializeBean deserialBean = new optimDeserializeBean(ofilepath);
				double[][] beanRestored = null;
				
				try {
					
					//-- Get the Meta  Bean Specification --//
					String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean(metaname, null);
					//String[][] beanArchSpecs = (String[][]) com.jom.dfa.excel.genExcelBeanXSSF.STFE_Int_deserializeBean("Meta-BeanArchSpecs-Covar"); //IBean-Meta-BeanArchSpecs-XL.ser			
					//System.out.println(" SpecBeanArch: " + beanArchSpecs.length);
					
										
					for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
					//for (int slBean=0; slBean<2; slBean++ )
					{
											
						if (beanArchSpecs[slBean][0].equals(abean)) {
							beanRestored = (double[][]) deserialBean.deserializeBeanAsPdouble(beanArchSpecs[slBean][0], null);
							System.out.println(" BeanName:Length= "+ beanArchSpecs[slBean][0] +" : " + beanRestored.length );
							break;
						}
						else continue;						
						
					} //-- All records MetBeanSpecs over --//

				}	//--try over--//	

				catch (Exception e) { 
					e.printStackTrace(); 
				}
				
				return beanRestored;
		}
		//////////////////////////////////////////////////////////
		
	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    