
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
import java.util.*; 

import org.apache.poi.ss.usermodel.*;
/*--- XSSF includes ---*/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
/*--- SXSSF includes ---*/
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

//import com.monitorjbl.xlsx.StreamingReader;
//-- SAX Include--//
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.*; // -- CellAddress; CellReference; --//
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/*--- XML beans includes ---*/  
import org.apache.xmlbeans.*;

import com.stfe.optim.config.initOptimAppConfig;
import com.stfe.optim.config.defSensiOptimXLSpecs;
import com.stfe.optim.util.optimSerializeBean;
import com.stfe.optim.util.optimValidateFile;
////////////////////////////////////////////////////////////
  


    public class genIBeanFromXLSpecs {
    	
    	
    	private String BeanFilepath = initOptimAppConfig.infiledir;
  		private String BeanFilename = null;
		
  		public genIBeanFromXLSpecs(){}
  		
		public  genIBeanFromXLSpecs(String infiledir, String infilename){
			setStreamFilePath(infiledir);			 
			this.BeanFilename=infilename;
			
			//System.out.println("## infilename ## Filepath:Deffile" + infilename + this.BeanFilepath + this.BeanFilename  );
		}
		
  		public void setStreamFilePath(String filepath){  						
  			this.BeanFilepath=validatePath(filepath);
  		}
  		public String getStreamFilePath(){  						
  			return this.BeanFilepath; 
  		}
  		public String getStreamFileName(){  						
  			return this.BeanFilename; 
  		}  		
  		private String setTrailingSepChar(String filepath) {
  			if (filepath == null) return null;
  			if(filepath.charAt(filepath.length()-1)!=File.separatorChar)
				filepath += File.separator;
  			return filepath;
  		}
  		
  	    //validate filename //
  		private boolean validFile(String filename) {
  			java.io.File vfile = new File(filename); 
  			if ( vfile.exists() && vfile.isFile() ) {
  				//System.out.println("File is valid : " + vfile);
  				return true;
  			} else {
  				System.out.println("Error: validFile - not valid! " + vfile);
  				return false;  				
  			}
  		}
		
  	    //-- validate dir-path --//
  		private String validatePath(String filepath) {
  			if (filepath == null) {
  				System.out.println("Error: validatePath - Filepath null! ");
  				return null;
  			}  			
  			java.io.File vfile = new File(filepath); 
  			if ( vfile.exists() && vfile.isDirectory() ) {  				  				
  				return setTrailingSepChar(filepath);  				
  			} else {
  				System.out.println("Error: validatePath - Filepath as a directory does not exist! ");
  				return null;
  			}
  		}  	
  		
		
  		
		//1- Create Bean from its defined architecture //
		//beanSpecName=Covar,Opt ;  sheetName=Kovarianz/Optimierung
		public void genBeanWriteBigFileFromSpecs(String[][] beanArchSpecs, String beanSpecName, String sheetName, String infilename)  {
			XSSFWorkbook workbook = null;	
			//-- Use SXSSFWorkbook for very big file - Keep 100 rows in mem, rest flushed to disk--//
			//SXSSFWorkbook xworkbook = new SXSSFWorkbook();
			//xworkbook.setCompressTempFiles(true);				
			//workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096)‌​.sheetIndex(0).open(fIStream); 
			
			//Implement further to write huge file //
						
		}
		
		
		
		////////////////////////////////////////////////////////////
  		//1- Create Covar Bean from its defined architecture //
  		//beanSpecName=Covar,Opt ;  sheetName=Kovarianz/Optimierung
  		public void genBeanTypeFromSpecs(String[][] beanArchSpecs, String beanSpecName, String sheetName, String infilename)  {
			
			//-- set up in-file --//
			String deffile= getStreamFileName();
			String filepath = getStreamFilePath();
			
  			/*
			String deffile=initOptimAppConfig.infilename;	 	
			String filepath=initOptimAppConfig.infiledir;			
			filepath = optimValidateFile.validatePath(filepath);
			String filename=filepath+deffile;
			*/
			
			if (  (infilename == null) && (deffile == null) ){
				System.out.println("File name is not supplied. Supply the filename as parameter of class-constructor or this method !");
				return;
			}
			if ( infilename != null )
				deffile=infilename;
			
			String filename=filepath+deffile;
			
			if (! validFile(filename)) { 
				System.out.println("File is not valid -  " + filename + " - Check the property file for infile/inpath!");
				return;
			}
					System.out.println("Filepath:Deffile" + filename  );
			
			optimSerializeBean serialBean = new optimSerializeBean(filepath);
			
			//XSSFWorkbook workbook = null;			 			
			//FileInputStream fIStream = null;			
			//org.apache.poi.ss.usermodel.Workbook workbook = null;			
						
			int dumptrace=0;
			int bigfile=1; 
			System.gc();		
			
			try (FileInputStream fIStream = new FileInputStream(new File(filename)); XSSFWorkbook workbook = new XSSFWorkbook(fIStream);)  {
			
			//try  {				
				
				
				//Workbook workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(new File(filename));				
				//Workbook workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(fIStream);
								
				
				
				//--Get sheet-2 named optimierung from the workbook--// //--sheetName=Kovarianz/Optimierung--//
				Sheet sheet = workbook.getSheet(sheetName);
				if (sheet == null) { 
					System.out.println("Sheet is invalid :  " + sheetName + " - Unable to get the handle of this sheet!");
					return;
				} 
				 
				
				
				//-- beanSpecName=Covar,Opt --//
				String metaBeanSpec= "Meta-BeanArchSpecs-"+beanSpecName;
				System.out.println("--> Serializing metaBeanSpec = " + metaBeanSpec);
				serialBean.serializeBean( beanArchSpecs, metaBeanSpec,  null);
					
				
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				{
						
					//--Specs of Bean Arch --//
					int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) ; // Long.parseLong(beanArchSpecs[slBean][2]);  //  
					int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) ; // Long.parseLong(beanArchSpecs[slBean][4]); //
					
					int startCol =  com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][1]);
					int endCol = com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][3]);
					//int startCol= Character.getNumericValue( (beanArchSpecs[slBean][1]).charAt(0) )  - Character.getNumericValue('A');			
					//int endCol= Character.getNumericValue( (beanArchSpecs[slBean][3]).charAt(0) ) - Character.getNumericValue('A') ;
					
					System.out.println(metaBeanSpec + " : " + beanArchSpecs[slBean][0] + ":  " + beanArchSpecs[slBean][1] +  ":  " + beanArchSpecs[slBean][2] 
					+  ":  " + beanArchSpecs[slBean][3] +  ":  " + beanArchSpecs[slBean][4] 
					+ " + startCol-endCol: " + startCol +" - " +endCol 
					+ " + startRow-endRow: " + startRow +" - " +endRow );
															
					//--Init Bean Arch --//					
					double[][] beanImpl = new double[endRow-startRow+1][endCol-startCol+1];					
					String[][] beanStrImpl = new String[endRow-startRow+1][endCol-startCol+1];
					
									    
					for (int rowNum = startRow, slRow=0; rowNum <= endRow; rowNum++, slRow++) {
										
						Row r = sheet.getRow(rowNum-1);
						
						if (r == null) {
							System.out.println("??? Blank Empty Row : RowNum: " +   rowNum  );
							continue;
						}
					
						for (int colNum = startCol, slCol=0; colNum <= endCol; colNum++, slCol++) {
							Cell cell = r.getCell(colNum-1, Row.RETURN_BLANK_AS_NULL);
							if (cell == null) {
								// Empty spreadsheet in the cell //
								continue;
							} else {						
								
								/*
								switch(cell.getCellType()) {
								
									case Cell.CELL_TYPE_BOOLEAN:
									if (dumptrace>1) System.out.print(cell.getBooleanCellValue() + "Error ??? \t\t");												
									break;
										
									case Cell.CELL_TYPE_NUMERIC:
									if ((slRow < 3) && (dumptrace>1)) System.out.print(slCol + "-colIdx/" + colNum +"-ColNum : " + +  cell.getNumericCellValue() + "\t");							
									beanImpl[slRow][slCol]=cell.getNumericCellValue();
									break;
										
									case Cell.CELL_TYPE_STRING:
									if (slRow < 3) System.out.print(cell.getStringCellValue() + " :\t");	
									beanStrImpl[slRow][slCol]=cell.getStringCellValue();
									break;
								}//--switch  over--//
								*/
								
								if ( beanArchSpecs[slBean][5].toUpperCase().contains("DATE") ) {
									if ( cell.getCellType() ==  Cell.CELL_TYPE_NUMERIC ){
										//if (slRow < 3) System.out.print("+++  Date-Numeric:-" + cell.getNumericCellValue() + " :\t");
										beanStrImpl[slRow][slCol]= String.valueOf( cell.getNumericCellValue() );									
									} else	
									if ( cell.getCellType() == Cell.CELL_TYPE_STRING ) {
										//if (slRow < 3) System.out.print(" -+?.+? String:-" + cell.getStringCellValue() + " :\t");
										//beanStrImpl[slRow][slCol]=  String.valueOf( cell.getStringCellValue() );
										beanStrImpl[slRow][slCol]=  cell.getStringCellValue();
									} else 
										beanStrImpl[slRow][slCol]=  String.valueOf( cell.getStringCellValue() );
									
									
									//beanStrImpl[slRow][slCol]=  String.valueOf( cell.getStringCellValue() );
								} //-- Date Spec Done --//
								
								
								
								if ( beanArchSpecs[slBean][5].toUpperCase().contains("STRING") ) {
									if ( cell.getCellType() == Cell.CELL_TYPE_FORMULA ){
										//if (slRow < 3) System.out.print("-+-+-+String-Formula-Date:-" + cell.getStringCellValue() + " :\t");
										beanStrImpl[slRow][slCol]= String.valueOf( cell.getStringCellValue() );										
									} 
									if ( cell.getCellType() == Cell.CELL_TYPE_NUMERIC ){
										//if (slRow < 3) System.out.print(" ??? String-Numeric:-" + cell.getNumericCellValue() + " :\t");
										//if (dumptrace>0) System.out.print(" STR-> CELL_TYPE_NUMERIC" + slCol + "-colIdx/" + colNum +"-ColNum : +++ DEBUG:ColRow ++" + rowNum +"rowNum/SlRow"+ slRow +" VAL: "+ cell.getNumericCellValue() + "\t");
										beanStrImpl[slRow][slCol]= String.valueOf( cell.getNumericCellValue() );
										
									}
									if ( cell.getCellType() == Cell.CELL_TYPE_STRING ) {
										//if (slRow < 3) System.out.print(" -+?.+? String:-" + cell.getStringCellValue() + " :\t");
										//if (dumptrace>0) System.out.print(" STR-> CELL_TYPE_STRING" + slCol + "-colIdx/" + colNum +"-ColNum : +++ DEBUG:ColRow ++" + rowNum +"rowNum/SlRow"+ slRow +" VAL: "+ cell.getNumericCellValue() + "\t");
										beanStrImpl[slRow][slCol]= cell.getStringCellValue();
									}
								} //-- STRING Spec Done--//
								
								
								if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") ) {		
									
									if ( cell.getCellType() == Cell.CELL_TYPE_NUMERIC ){
										//if (slRow < 3) System.out.print(" ??? String-Numeric:-" + cell.getNumericCellValue() + " :\t");
										if (dumptrace>0) System.out.print(" NUM-NUM-> CELL_TYPE_NUMERIC" + slCol + "-colIdx/" + colNum +"-ColNum : +++ DEBUG:ColRow ++" + rowNum +"rowNum/SlRow"+ slRow +" VAL: "+ cell.getNumericCellValue() + "\n\t");
										beanImpl[slRow][slCol]= cell.getNumericCellValue();
										
									}
									if ( cell.getCellType() == Cell.CELL_TYPE_STRING ) {
										//if (slRow < 3) System.out.print(" -+?.+? String:-" + cell.getStringCellValue() + " :\t");
										if (dumptrace>0) System.out.print(" NUM-STR-> CELL_TYPE_STRING" + slCol + "-colIdx/" + colNum +"-ColNum : +++ DEBUG:ColRow ++" + rowNum +"rowNum/SlRow"+ slRow +" VAL: "+ cell.getStringCellValue() + "\n\t");
										beanImpl[slRow][slCol]= Double.parseDouble(cell.getStringCellValue());
									}
																		
								} // -- NUM Spec --//
								
							}// -- if block over--//
						
						} // -- for loop over --//
				
								    
					} // row-iteration over//
								
					
					System.out.println(" Bean : Dim1-Row " +   beanImpl.length + " : Dim2-Col" +  beanImpl[0].length ); 	
					
					//-- Serialize the Bean Data --//					
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("DATE") )	
						serialBean.serializeBean((String[][]) beanStrImpl, beanArchSpecs[slBean][0], null);
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("STRING") )	
						serialBean.serializeBean((String[][]) beanStrImpl, beanArchSpecs[slBean][0], null);
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") )
						serialBean.serializeBeanAsPdouble( beanImpl, beanArchSpecs[slBean][0], null); //if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") )
					
					beanImpl=null;	
					beanStrImpl=null;
				
				} //-- Over for all beans - slBean --//
				
				workbook.close();
				if (fIStream != null) fIStream.close();
			
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
			
		
		} // function STFE_genBeanTypeFromSpecs over//
		
  		////////////////////////////////////////////////////////////
  		
		
		
  		////////////////////////////////////////////////////////////
		//1- Create Covar Bean from its defined architecture //	
		public void genBeanCovarFromSpecs()  {
		
			
			String deffile=initOptimAppConfig.infilename;	 	
			String filepath=initOptimAppConfig.infiledir;			
			filepath = optimValidateFile.validatePath(filepath);
			String filename=filepath+deffile;
			if (! optimValidateFile.validFile(filename)) 
				System.out.println("File is not valid -  " + filename + " - Check the property file for infile/inpath!");
			
			optimSerializeBean serialBean = new optimSerializeBean(filepath);
									
			int dumptrace=0;
			
			try {
							
				FileInputStream fIStream = new FileInputStream(new File(filename));
				
				//--Get the workbook instance for XLS file --//
				Workbook workbook = new XSSFWorkbook(fIStream);
				//--Get sheet-2 named optimierung from the workbook--//
				Sheet sheet = workbook.getSheet("Kovarianz");
				
				
				//-- Serialize the Meta Bean Specification --//
				String[][] beanArchSpecs=defSensiOptimXLSpecs.getBeanCovarArchSpecs();
									
				serialBean.serializeBean( beanArchSpecs, "Meta-BeanArchSpecs-Covar",  null);
			
				
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
					serialBean.serializeBeanAsPdouble( beanImpl, beanArchSpecs[slBean][0], null);
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
		public void genBeanOptFromSpecs()  {
			
			String deffile=initOptimAppConfig.infilename;	 	
			String filepath=initOptimAppConfig.infiledir;
			filepath = optimValidateFile.validatePath(filepath);
			String filename=filepath+deffile;
			if (! optimValidateFile.validFile(filename)) 
				System.out.println("File is not valid -  " + filename + " - Check the property file for infile/inpath!");
						
			optimSerializeBean serialBean = new optimSerializeBean(filepath);
			
			int dumptrace=0;
			
			try {
			
				FileInputStream fIStream = new FileInputStream(new File(filename));

				//--Get the workbook instance for XLS file --//
				Workbook workbook = new XSSFWorkbook(fIStream);
				//--Get sheet-2 named optimierung from the workbook--//
				Sheet sheet = workbook.getSheetAt(1);
				
				//-- Serialize the Meta Bean Specification --//
				String[][] beanArchSpecs=defSensiOptimXLSpecs.getBeanOptArchSpecs();
				
				serialBean.serializeBean(beanArchSpecs, "Meta-BeanArchSpecs-Opt", null);
				
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
									if (beanArchSpecs[slBean][0].toUpperCase().contains("OptimalSolution")) beanImpl[slRow][slCol]=0.0;
																	
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
						serialBean.serializeBeanAsPdouble( beanImpl, beanArchSpecs[slBean][0], null);
						
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
		
		
	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    