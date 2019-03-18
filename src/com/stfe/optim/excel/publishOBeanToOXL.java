
package com.stfe.optim.excel;

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

import org.apache.poi.ss.usermodel.*;

import com.jom.DoubleMatrixND;
import com.jom.OptimizationProblem;
import com.stfe.optim.config.initOptimAppConfig;
//import com.stfe.optim.config.defOptimXLSpecs;


import com.stfe.optim.util.optimRestoreIOBean;
import com.stfe.optim.util.optimDeserializeBean;
import com.stfe.optim.util.optimValidateFile;


////////////////////////////////////////////////////////////
  
    public class publishOBeanToOXL {
    			
		////////////////////////////////////////////////////////////
		//1- Create Opt-Bean from its defined architecture //	
		public  void publishSolutionOXL(String metaName, String resultBeanName)  { //STFE_getExcelFromBeanOpt
		
			//String deffile="OXL_Sensis_Szenarios.xlsx";		
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			
			String deffile=initOptimAppConfig.outfilename;	 	
			String filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			String filename=filepath+deffile;
			
			optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
			optimRestoreIOBean restoreIOBean = new optimRestoreIOBean();
			
			try {
						
				//--InputStream--//
				//InputStream InStreamXL = new FileInputStream(new File(filename));
				InputStream InStreamXL = null;			
				
				//-- Generate the Out-Excel from persisted Bean --//						
				//FileOutputStream foutStreamXL = new FileOutputStream(new File(filename));
				FileOutputStream foutStreamXL = null;
				
				//--Get the workbook instance for XLS file --//			
				//Workbook workbook = new XSSFWorkbook(InStreamXL);
				//Workbook workbook = WorkbookFactory.create(InStreamXL);
				Workbook workbook = null;
				
				
				//-- Get the Meta  Bean Specification --//
				//String[][] beanArchSpecs = (String[][]) STFE_Int_deserializeBean("IBean-Meta-BeanArchSpecs-XL.ser"); //IBean-Meta-BeanArchSpecs-XL.ser			
				String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean("Meta-BeanArchSpecs-Opt", null); //IBean-Meta-BeanArchSpecs-XL.ser
				
				
				double[][][] beanRestored = new double[beanArchSpecs.length][][];
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				//for (int slBean=0; slBean<2; slBean++ )
				{
					
					if (! beanArchSpecs[slBean][0].equals(resultBeanName) ) continue;
					
					//In-Stream Workbook, Sheet
					InStreamXL = new FileInputStream(new File(filename));
					workbook = WorkbookFactory.create(InStreamXL);		
					Sheet sheet = workbook.getSheet("Optimierung");;
					
					if (metaName.contains("-Opt"))
						sheet = workbook.getSheet("Optimierung");
					if (metaName.contains("-Covar"))
						sheet = workbook.getSheet("Covarianz");
					
					
					
					//--Init Bean Arch reading from serialized bean--//	 //- Meta-BeanArchSpecs-Opt , OptimalSolution, //			
					//Double[][] beanImpl =  (Double[][]) STFE_Int_deserializeBean(beanArchSpecs[slBean][0]); //new Double[endRow-startRow+1][endCol-startCol+1];
					beanRestored[slBean] = (double[][]) restoreIOBean.restoreABeanFromMeta(metaName, resultBeanName);
					//double[][] beanImpl = beanRestored[slBean];
														
					System.out.println(" BeanName:Length= "+ beanArchSpecs[slBean][0] +" : " + beanArchSpecs[slBean].length );
					
					//--Specs of Bean Arch --//								
					int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) - 1;
					int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) -1 ;			
					int startCol= Character.getNumericValue( (beanArchSpecs[slBean][1]).charAt(0) )  - Character.getNumericValue('A')  ;			
					int endCol= Character.getNumericValue( (beanArchSpecs[slBean][3]).charAt(0) ) - Character.getNumericValue('A')  ;
					
					System.out.println(" SpecBeanArch: "+ beanArchSpecs[slBean][0] + ":  " + beanArchSpecs[slBean][1] +  ":  " + beanArchSpecs[slBean][2] 
					+  ":  " + beanArchSpecs[slBean][3] +  ":  " + beanArchSpecs[slBean][4] 
					+ " + startCol-endCol: " + startCol +" - " +endCol 
					+ " + startRow-endRow: " + startRow +" - " +endRow );
					
					
					//SpecBeanArch: AllSevPC-1-10:C2:I11//				 
					//endRow=startRow +5;
					for (int rowNum = startRow, slRow=0; rowNum <= endRow; rowNum++, slRow++) {
					
						System.out.println(" RowNum: " +   rowNum  );
						
						Row row = sheet.getRow(rowNum);
						if (row == null) row = sheet.createRow(rowNum);
						
						
						for (int colNum = startCol, slCol=0; colNum <= endCol; colNum++, slCol++) {
							Cell cell = row.createCell(colNum);
							//CellStyle cs = workbook.createCellStyle();
							//cs.setWrapText(true);
							//cell.setCellStyle(cs);
							
							//XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
							CellStyle style = workbook.createCellStyle();
							//--Aqua background--//				    
						    //style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
						    //style.setFillPattern(CellStyle.BIG_SPOTS);						    
							//--Orange "foreground", foreground being the fill foreground not the font color. --//				    
						    style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
						    style.setFillPattern(CellStyle.SOLID_FOREGROUND);					
						    //--Create a new font and alter it.--//
						    Font font = workbook.createFont();
						    font.setFontHeightInPoints((short)10);
						    font.setFontName("Courier New");
						    font.setItalic(true);
						    //font.setStrikeout(true);
						    style.setFont(font);
						    cell.setCellStyle(style);
						    
						    
							System.out.print( " Val: " + beanRestored[slBean][slRow][slCol] );
							
							//if (beanRestored[slBean][slRow][slCol] != null)  
							cell.setCellValue(beanRestored[slBean][slRow][slCol]);
							
							
						} // -- all cols/row - for loop over --//
						
						System.out.println("  "   );
						
					} // row-iteration over//
					
					
					//-- Write to the Out-Excel from persisted Bean --//					
					foutStreamXL = new FileOutputStream(new File(filename));
					//sheet.autoSizeColumn((short)2);	
					workbook.write(foutStreamXL);				
					workbook.close();
					InStreamXL.close();
					foutStreamXL.close();			    							
					
				} //-- All records MetBeanSpecs over --//	
				
			
			} //--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
			
		} // function  over//
		//////////////////////////////////////////////////////////			
		
		
		
		
	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    