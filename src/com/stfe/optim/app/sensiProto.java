
package com.stfe.optim.app;
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

import org.apache.poi.ss.usermodel.*;
/*--- XSSF includes ---*/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*--- XML beans includes ---*/  
import org.apache.xmlbeans.*;


import com.stfe.optim.config.*;
//import com.stfe.optim.util.*;
import com.stfe.optim.excel.*;
////////////////////////////////////////////////////////////
  
  

    public class sensiProto {
			
		private static String efile;
		private static int exist=0;
		private static int buildBeanFlag=0;
		private static Map<Integer, Object[]> Objrow; // = new HashMap<Integer, Object[]="">();
		//private static Map<Integer, Object[]=""> Objrow = new HashMap<Integer, Object[]="">();
		private static int restore_stream=0;
		private static int dumptrace=2;
		
				

		
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		

		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		//1- Create Covar Bean from its defined architecture //	
		public static void STFE_genBeanCovarFromSpecs()  {
		
			String deffile="Sensis und Szenarios.xlsx";
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			String filename=filepath+deffile;
			
			try {
			
				if (exist>0) deffile=efile; 
				//else filename="C:\\devdrv\\more4\\ExtAppObba\\PortfolioData.xlsx";
				
				FileInputStream fIStream = new FileInputStream(new File(filename));
				
				//--Get the workbook instance for XLS file --//
				Workbook workbook = new XSSFWorkbook(fIStream);
				//--Get sheet-2 named optimierung from the workbook--//
				Sheet sheet = workbook.getSheet("Kovarianz");
				
				
				//-- Serialize the Meta Bean Specification --//
				String[][] beanArchSpecs=initBeanCovarArchSpecs();
							
				STFE_Int_serializeBean( "Meta-BeanArchSpecs-Covar", beanArchSpecs);
			
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				{
						
					//--Specs of Bean Arch --//
					int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) - 1;
					int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) -1;			
					int startCol= Character.getNumericValue( (beanArchSpecs[slBean][1]).charAt(0) )  - Character.getNumericValue('A');			
					int endCol= Character.getNumericValue( (beanArchSpecs[slBean][3]).charAt(0) ) - Character.getNumericValue('A') ;
					
					System.out.println("Covar-SpecBeanArch: "+ beanArchSpecs[slBean][0] + ":  " + beanArchSpecs[slBean][1] +  ":  " + beanArchSpecs[slBean][2] 
					+  ":  " + beanArchSpecs[slBean][3] +  ":  " + beanArchSpecs[slBean][4] 
					+ " + startCol-endCol: " + startCol +" - " +endCol 
					+ " + startRow-endRow: " + startRow +" - " +endRow );
					
					
					
					//--Init Bean Arch --//
					double[][] beanImpl = new double[endRow-startRow+1][endCol-startCol+1];
					
					
					//SpecBeanArch: AllSevPC-1-10:C2:I11//				    
					for (int rowNum = startRow, slRow=0; rowNum <= endRow; rowNum++, slRow++) {
					
						System.out.println(" RowNum: " +   rowNum  );
						
						Row r = sheet.getRow(rowNum);
						if (r == null) {
						System.out.println("??? Blank Empty Row : RowNum: " +   rowNum  );
						continue;
					}
					
					
					
					for (int colNum = startCol, slCol=0; colNum <= endCol; colNum++, slCol++) {
						Cell cell = r.getCell(colNum, Row.RETURN_BLANK_AS_NULL);
						if (cell == null) {
						// The spreadsheet is empty in this cell
						continue;
						} else {						
							
							switch(cell.getCellType()) {
							
							case Cell.CELL_TYPE_BOOLEAN:
							if (dumptrace>1) System.out.print(cell.getBooleanCellValue() + "Error ??? \t\t");												
							break;
							
							case Cell.CELL_TYPE_NUMERIC:
							if (dumptrace>1) System.out.print(cell.getNumericCellValue() + "\t\t");							
							beanImpl[slRow][slCol]=cell.getNumericCellValue();
							break;
							
							case Cell.CELL_TYPE_STRING:
							if (dumptrace>1) System.out.print(cell.getStringCellValue() + " Error ??? \t\t");							
							break;
							}//--switch  over--//
							}// -- if block over--//
					
						} // -- for loop over --//
				
						System.out.println(" Over RowNum: " +   rowNum  );  			    
					} // row-iteration over//
								
						
					//-- Serialize the Bean Data --//
					STFE_Int_serializeBeanAsPdouble( beanArchSpecs[slBean][0], beanImpl);
					beanImpl=null;		
				
				} //-- Over for all beans - slBean --//
				
				workbook.close();
				fIStream.close();
				
			
			} //--try over--//
			catch (FileNotFoundException e) {
			e.printStackTrace();
			} 
			catch (IOException e) {
			e.printStackTrace();
			}	
			catch (Exception e) { 
			e.printStackTrace(); 
			}
		
		} // function STFE_genBeanCovarFromSpecs over//
		//////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		//1- Create Opt Bean from its defined architecture //	
		public static void STFE_genBeanOptFromSpecs()  {
			
			String deffile="Sensis und Szenarios.xlsx";
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			
			try {
				
				if (exist>0) deffile=efile; 
				//else filename="C:\\devdrv\\more4\\ExtAppObba\\PortfolioData.xlsx";
				
				String filename=filepath+deffile;

				FileInputStream fIStream = new FileInputStream(new File(filename));

				//--Get the workbook instance for XLS file --//
				Workbook workbook = new XSSFWorkbook(fIStream);
				//--Get sheet-2 named optimierung from the workbook--//
				Sheet sheet = workbook.getSheetAt(1);
				
				//-- Serialize the Meta Bean Specification --//
				String[][] beanArchSpecs=initBeanOptArchSpecs();
				STFE_Int_serializeBean( "Meta-BeanArchSpecs-Opt", beanArchSpecs);
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				{
								
				//--Specs of Bean Arch --//
					int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) - 1;
					int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) -1;			
					int startCol= Character.getNumericValue( (beanArchSpecs[slBean][1]).charAt(0) )  - Character.getNumericValue('A');			
					int endCol= Character.getNumericValue( (beanArchSpecs[slBean][3]).charAt(0) ) - Character.getNumericValue('A') ;
					
					System.out.println(" SpecBeanArch: "+ beanArchSpecs[slBean][0] + ":  " + beanArchSpecs[slBean][1] +  ":  " + beanArchSpecs[slBean][2] 
							+  ":  " + beanArchSpecs[slBean][3] +  ":  " + beanArchSpecs[slBean][4] 
									+ " + startCol-endCol: " + startCol +" - " +endCol 
									+ " + startRow-endRow: " + startRow +" - " +endRow );
					
					
					
					/*
					int startRow= Integer.parseInt(beanArchSpecs[0][2]) - 1;
					int endRow= Integer.parseInt(beanArchSpecs[0][4]) -1;			
					int startCol= Character.getNumericValue( (beanArchSpecs[0][1]).charAt(0) )  - Character.getNumericValue('A');			
					int endCol= Character.getNumericValue( (beanArchSpecs[0][3]).charAt(0) ) - Character.getNumericValue('A') ;
					
					System.out.println(" SpecBeanArch: "+ beanArchSpecs[0][0] + ":  " + beanArchSpecs[0][1] +  ":  " + beanArchSpecs[0][2] 
							+  ":  " + beanArchSpecs[0][3] +  ":  " + beanArchSpecs[0][4] 
									+ " + startCol-endCol: " + startCol +" - " +endCol 
									+ " + startRow-endRow: " + startRow +" - " +endRow );
					
					*/
							
					//--Init Bean Arch --//
					double[][] beanImpl = new double[endRow-startRow+1][endCol-startCol+1];
					
					
					//SpecBeanArch: AllSevPC-1-10:C2:I11//				    
					for (int rowNum = startRow, slRow=0; rowNum <= endRow; rowNum++, slRow++) {
						
						System.out.println(" RowNum: " +   rowNum  );
					
						Row r = sheet.getRow(rowNum);
						if (r == null) {
							System.out.println("??? Blank Empty Row : RowNum: " +   rowNum  );
							continue;
						}
						
					   
						for (int colNum = startCol, slCol=0; colNum <= endCol; colNum++, slCol++) {
							  Cell cell = r.getCell(colNum, Row.RETURN_BLANK_AS_NULL);
							  if (cell == null) {
								 // The spreadsheet is empty in this cell
								  continue;
							  } else {
								 // Do something useful with the cell's contents
								  
								  switch(cell.getCellType()) {
		
									case Cell.CELL_TYPE_BOOLEAN:
									if (dumptrace>1) System.out.print(cell.getBooleanCellValue() + "Error ??? \t\t");												
									break;
									
									case Cell.CELL_TYPE_NUMERIC:
									if (dumptrace>1) System.out.print(cell.getNumericCellValue() + "\t\t");							
									beanImpl[slRow][slCol]=cell.getNumericCellValue();
									
									//-- do not persist solution--//
									if (beanArchSpecs[slBean][0].contains("OptimalSolution")) beanImpl[slRow][slCol]=0.0;
																	
									break;
									
									case Cell.CELL_TYPE_STRING:
									if (dumptrace>1) System.out.print(cell.getStringCellValue() + " Error ??? \t\t");							
									break;
								}//--switch  over--//		
							  }// -- if block over--//
							  
						} // -- for loop over --//
						
						
						System.out.println(" Over RowNum: " +   rowNum  );  			    
					} // row-iteration over//
					
					
					
					//--Serialize Bean  --//
					/*
					String beanFileName=filepath+"IBean-"+beanArchSpecs[slBean][0]+"-XL.ser";
					ObjectOutputStream objStream = new ObjectOutputStream(new FileOutputStream(beanFileName));
					objStream.writeObject(beanImpl);
					objStream.close();			
					*/
					
					
					//-- Serialize the Bean Data --//
						STFE_Int_serializeBeanAsPdouble( beanArchSpecs[slBean][0], beanImpl);
						
						beanImpl=null;							
					
					} //-- Over for all beans - slBean --//
				
					workbook.close();
					fIStream.close();
					
				} //--try over--//
				catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}	
				catch (Exception e) { 
					e.printStackTrace(); 
				}
				
			} // function STFE_genBeanOptFromSpecs over//
		//////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////
		//1- Create Bean from its defined architecture //	
		public static void STFE_Int_serializeBeanAsPdouble(String name, double[][] beanImpl)  {
		
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			//String deffile="Sensis und Szenarios.xlsx";				
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			
			try {
			
				//--Serialize Bean  --//
				ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(beanFileName));
				objOutStream.writeObject(beanImpl);
				objOutStream.close();
				
			}
			catch (Exception e) { e.printStackTrace(); }
		} //--class STFE_Int_serializeBean over --//
		////////////////////////////////////////////////////////////

		
		
		////////////////////////////////////////////////////////////
		//1- Create Bean from its defined architecture //	
		public static void STFE_Int_serializeBean(String name, Object[][] beanImpl)  {

			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			//String deffile="Sensis und Szenarios.xlsx";				
			String beanFileName=filepath+"IBean-"+name+"-XL.ser";
			
			try {
				//--Serialize Bean  --//			
				//String beanFileName=filepath+"IBean-"+beanArchSpecs[slBean][0]+"-XL.ser";
				
				ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(beanFileName));
				objOutStream.writeObject(beanImpl);
				objOutStream.close();
				
			}
			   catch (Exception e) { e.printStackTrace(); }
		} //--class STFE_Int_serializeBean over --//
		////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////	
		public static void STFE_Int_InitExcelForBean()  {
			String deffile="OXL_Sensis_Szenarios.xlsx";
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			String filename=filepath+deffile;	
			try{
				FileOutputStream outStreamXL = new FileOutputStream(new File(filename));
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = null;
										
				
				sheet = workbook.createSheet("Optimierung");
				sheet = workbook.createSheet("Kovarianz");
				sheet = workbook.createSheet("Kosten 31.07.2016");
				
				sheet = workbook.getSheet("Optimierung");
				sheet.autoSizeColumn((short)2);	
				Row row = sheet.createRow(0);		    
				Cell cell = row.createCell(0);		    
				cell.setCellType(Cell.CELL_TYPE_STRING); //CellType.STRING		    
				cell.setCellValue("Optimierung");
				
				String[][] beanArchSpecs = (String[][]) STFE_Int_deserializeBean("Meta-BeanArchSpecs-Opt");
							
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 	{
											
										
						//--Specs of Bean Arch --//								
						int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) - 1;
						int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) -1 ;			
						int startCol= Character.getNumericValue( (beanArchSpecs[slBean][1]).charAt(0) )  - Character.getNumericValue('A')  ;			
						int endCol= Character.getNumericValue( (beanArchSpecs[slBean][3]).charAt(0) ) - Character.getNumericValue('A')  ;
						
						System.out.println(" SpecBeanArch: "+ beanArchSpecs[slBean][0] + ":  " + beanArchSpecs[slBean][1] +  ":  " + beanArchSpecs[slBean][2] 
								+  ":  " + beanArchSpecs[slBean][3] +  ":  " + beanArchSpecs[slBean][4] 
										+ "  startCol-endCol: " + startCol +" - " +endCol  
										+ "  startRow-endRow: " + startRow +" - " +endRow);
						
						
						if (startRow < 3) {
							
							cell = row.createCell(startCol);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							
							
							//XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
							CellStyle style = workbook.createCellStyle();
							//--Aqua background--//				    
							//style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
							//style.setFillPattern(CellStyle.BIG_SPOTS);						    
							//--Orange "foreground", foreground being the fill foreground not the font color. --//				    
							style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
							style.setFillPattern(CellStyle.SOLID_FOREGROUND);					
							//--Create a new font and alter it.--//
							Font font = workbook.createFont();
							font.setFontHeightInPoints((short)9);
							font.setFontName("Courier New");
							font.setItalic(true);
							font.setBold(true);
							//font.setStrikeout(true);
							style.setFont(font);
							cell.setCellStyle(style);
							
													 
							int strsz = 9;
							if ( beanArchSpecs[slBean][0].length() <= strsz) 
								strsz = beanArchSpecs[slBean][0].length() ;
							
							cell.setCellValue((beanArchSpecs[slBean][0]).substring(0,strsz)  );
							
						}
						
						
				} //-- All records MetBeanSpecs over --//	
				
				workbook.write(outStreamXL);
				workbook.close();
				outStreamXL.close();
				
			} //--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
		}
		////////////////////////////////////////////////////////////	
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////


		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////
		//1- Create Covar-Bean from its defined architecture //
		public static void STFE_getExcelFromBeanCovar() {
			String deffile="OXL_Sensis_Szenarios.xlsx";		
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			String filename=filepath+deffile;
			try{
				
				//Input Stream  
				InputStream InStreamXL = null;
				//-- Generate the Out-Excel from persisted Bean --//
				FileOutputStream foutStreamXL = null;
				//--Get the workbook instance for XLS file --//		
				Workbook workbook = null;
				
				
				//-- Get the Meta  Bean Specification --//
				String[][] beanArchSpecs = (String[][]) STFE_Int_deserializeBean("Meta-BeanArchSpecs-Covar"); //IBean-Meta-BeanArchSpecs-XL.ser			
				//System.out.println(" SpecBeanArch: " + beanArchSpecs.length);
				
				double[][][] beanRestored = new double[beanArchSpecs.length][][];
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				//for (int slBean=0; slBean<2; slBean++ )
				{
					
					//In-Stream Workbook, Sheet
					InStreamXL = new FileInputStream(new File(filename));
					workbook = WorkbookFactory.create(InStreamXL);								
					Sheet sheet = workbook.getSheet("Kovarianz");
					
													
					//--Init Bean Arch reading from serialized bean--//				
					beanRestored[slBean] = (double[][]) STFE_Int_deserializeBeanAsPdouble(beanArchSpecs[slBean][0]);
					double[][] beanImpl = beanRestored[slBean];
					
					//Double[][] beanImpl =  (Double[][]) STFE_Int_deserializeBean(beanArchSpecs[slBean][0]); //new Double[endRow-startRow+1][endCol-startCol+1];				
					System.out.println(" BeanName:Length= "+ beanArchSpecs[slBean][0] +" : " + beanImpl.length );
					
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
												
					
					beanImpl=null;
					
				} //-- All records MetBeanSpecs over --//	
					
				
			}	//--try over--//
			catch (FileNotFoundException e) {
			e.printStackTrace();
			} 
			catch (IOException e) {
			e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
			
		}
		//////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////
		//1- Create Opt-Bean from its defined architecture //	
		public static void STFE_getExcelFromBeanOpt()  { //STFE_getExcelFromBeanOpt
		
			String deffile="OXL_Sensis_Szenarios.xlsx";		
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			String filename=filepath+deffile;
			
			
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
				String[][] beanArchSpecs = (String[][]) STFE_Int_deserializeBean("Meta-BeanArchSpecs-Opt"); //IBean-Meta-BeanArchSpecs-XL.ser
				
				
				double[][][] beanRestored = new double[beanArchSpecs.length][][];
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				//for (int slBean=0; slBean<2; slBean++ )
				{
					
					//In-Stream Workbook, Sheet
					InStreamXL = new FileInputStream(new File(filename));
					workbook = WorkbookFactory.create(InStreamXL);								
					Sheet sheet = workbook.getSheet("Optimierung");
					
					
					/*
					if (slBean == 0) {
					//--Get sheet-2 named optimierung from the workbook--//		
						 //outStreamXL = new FileOutputStream(new File(filename));
						//--Get the workbook instance for XLS file --//
						 workbook = new XSSFWorkbook();
						sheet = workbook.createSheet();
						
					}  if (slBean > 0){					
						//--Get the workbook instance for XLS file --//					
						InStreamXL = new FileInputStream(new File(filename));
						workbook = WorkbookFactory.create(InStreamXL);								
						sheet = workbook.getSheet("Optimierung");
						//foutStreamXL = new FileOutputStream(new File(filename));
					}
					*/
									
					//--Init Bean Arch reading from serialized bean--//				
					beanRestored[slBean] = (double[][]) STFE_Int_deserializeBeanAsPdouble(beanArchSpecs[slBean][0]);
					//double[][] beanImpl = beanRestored[slBean];
					
					//Double[][] beanImpl =  (Double[][]) STFE_Int_deserializeBean(beanArchSpecs[slBean][0]); //new Double[endRow-startRow+1][endCol-startCol+1];				
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
					
				//-- Write to the Out-Excel from persisted Bean --//
				
				//FileOutputStream foutStreamXL = new FileOutputStream(new File(filename));			
				//sheet.autoSizeColumn((short)2);
				
				///workbook.write(foutStreamXL);
				
				//--Close the resources --//
				//InStreamXL.close();
				//workbook.close();
				//foutStreamXL.close();
				
							
				
			} //--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
			
		} // function genBeanImpl over//
		//////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		
		
			
		////////////////////////////////////////////////////////////
		//1- Create Bean from its defined architecture //	
		public static Object[][] STFE_Int_deserializeBean(String name)  {
			
			//--Filepath Bean  --//
			//String deffile="Sensis und Szenarios.xlsx";
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";

			Object[][] beanImpl=null;
			
			try {		
							
				ObjectInputStream objInStream = null;
				String beanFileName=filepath+"IBean-"+name+"-XL.ser";
				objInStream = new ObjectInputStream(new FileInputStream(beanFileName));
				
				//--Serialize Bean  --//									
				//if (name.equals("IBean-Meta-BeanArchSpecs-XL.ser")) { 				
				if (beanFileName.contains("IBean-Meta-BeanArchSpecs")) {
					beanImpl = (String[][]) objInStream.readObject();
				}
				else {
					beanImpl = (Double[][]) objInStream.readObject();
				}
				
				objInStream.close();
				
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		
		////////////////////////////////////////////////////////////
		public static Object[][] STFE_Int_deserializeBeanAsString(String name)  {
			
			//--Filepath Bean  --//
			//String deffile="Sensis und Szenarios.xlsx";
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";

			String[][] beanImpl=null;
			
			try {		
							
				ObjectInputStream objInStream = null;
				String beanFileName=filepath+"IBean-"+name+"-XL.ser";
				objInStream = new ObjectInputStream(new FileInputStream(beanFileName));
				
				//--Serialize Bean  --//									
				//if (name.equals("IBean-Meta-BeanArchSpecs-XL.ser")) { 				
				if (beanFileName.contains("IBean-Meta-BeanArchSpecs")) {
					beanImpl = (String[][]) objInStream.readObject();
				}
							
				objInStream.close();
				
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		////////////////////////////////////////////////////////////
		
		public static double[][] STFE_Int_deserializeBeanAsPdouble(String name)  {		
			
			//--Filepath Bean  --//
			//String deffile="Sensis und Szenarios.xlsx";
			String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";

			double[][] beanImpl=null;
			
			try {		
							
				ObjectInputStream objInStream = null;
				String beanFileName=filepath+"IBean-"+name+"-XL.ser";
				objInStream = new ObjectInputStream(new FileInputStream(beanFileName));
				
				//--Serialize Bean  --//									
				//if (name.equals("IBean-Meta-BeanArchSpecs-XL.ser")) { 				
				if (! beanFileName.contains("IBean-Meta-BeanArchSpecs")) {
					beanImpl = (double[][]) objInStream.readObject();
				}
							
				objInStream.close();
				
			}
			
			catch (Exception e) { e.printStackTrace(); }		
			return beanImpl;
		} //-- class STFE_Int_deserializeBean over --//
		////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////
		public static void STFE_initIBeanAndIXL()  {
			
			STFE_Int_InitExcelForBean();
			STFE_genBeanCovarFromSpecs();
			STFE_genBeanOptFromSpecs();
			STFE_getExcelFromBeanCovar();
			STFE_getExcelFromBeanOpt();
		}
		////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		//public static void main_poiGenExcelXSSF(String[] args) {
		public static void main(String[] args) {
			
			ed_Adjav();
			
			
			//STFE_initIBeanAndIXL();
			
					
		} // void main //
		
		
		
		
		
		
		
		//1-1 Define the size of beans based on row-cols
		//////////////////////////////////////////////////////////
		//private static Map<Integer, Object[]> sizeBean;	
		public static String[][] initBeanOptArchSpecs() {		
			//String[][] drc = new String[10][5];
			String[][] optDRL = {
					{"AllSevPC-1-10","C","2","I","11"},
					{"Bestand","J","2","J","11"},
					{"OptimalSolution","C","13","I","13"}, // Optimal - Solution//
					{"SumBestand","J","13","J","13"},
					{"UpLimit","C","16","J","16"},
					{"LowLimit","C","17","I","17"},
					
					//-- Risk In Data --//
					{"PCRP4","L","2","L","11"},
					{"PCRPZAp1","O","2","O","11"},
					
					//-- SumProd In Data --//
					{"Program","S","2","S","11"},
					{"BestandProgram","T","2","T","11"},
					
					//--Risk- Calc Out Data --//
					{"PCRP4Program","M","2","M","11"},
					{"PCRP4BestdProgram","N","2","N","11"},
					{"PCRPZAp1Program","P","2","P","11"},
					{"PCRPZAp1BestdProgram","Q","2","Q","11"},
					
					{"SumPCRP4Program","M","13","M","13"},
					{"SumPCRP4BestdProgram","N","13","N","13"},
					{"SumPCRPZAp1Program","P","13","P","13"},
					{"SumPCRPZAp1BestdProgram","Q","13","Q","13"},
									
					//--Risk-Covar Calc Out Data --//
					{"RiskCVProg","S","13","S","13"},
					{"RiskCVBestdProg","T","13","T","13"},
					
					//--Limit-Covar Calc Out Data --//
					{"LimitPCRP4BestdProgram","N","16","N","16"},
					
					//--Limit-Covar Calc Out Data --//
					{"LimitCVProgram","S","16","S","16"},
					{"LimitCVBestdProgram","T","16","T","16"}
					
			};				
			return optDRL;
		} //--function over--//
		
		
		//////////////////////////////////////////////////////////
		public static String[][] initBeanCovarArchSpecs() {
			String[][] covarDRL = {
					//-- Covariance --//
					{"Covarianz","A","1","J","10"}
					
			};				
			return covarDRL;
		} //--function over--//
		////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////
		public static void ed_Adjav(){
			
			System.out.println(" AirthOps- Div: " + 25/10 + " Remind:" + 25%10);
			
			sensiProto anobj = new sensiProto();
			if (anobj instanceof sensiProto) System.out.println(" anobj is an instanceof sensiProto ");
			
			int x=7, y=5, z=0;
			z= x | y;
			System.out.println (" x | y : " + z);
			
			z= x & y;
			System.out.println (" x AND y : " + z);
			
			
		}
		
		
		////////////////////////////////////////////////////////////
				
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    