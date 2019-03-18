
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

/*--- XML beans includes ---*/  
import org.apache.xmlbeans.*;

import com.stfe.optim.config.initOptimAppConfig;
//import com.stfe.optim.config.defOptimXLSpecs;
import com.stfe.optim.util.*;

////////////////////////////////////////////////////////////

  

    public class createOXLFromIBean {
    	
		
    	public  int columnCharToNumber(String str) {
    	    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	    if(str.length() == 1) {
    	        return alphabet.indexOf(str);
    	    }
    	    if(str.length() == 2) {
    	        return ( alphabet.indexOf(str.substring(1)) + 26*(1+alphabet.indexOf(str.substring(0,1)))) ;
    	    }
    	    return -1;
    	}
    	
    	boolean isDouble(String str) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    	
		////////////////////////////////////////////////////////////
    	public void initOXLSheetForIBean(String filedir, String deffilename, String sheetname, boolean forceinit)  {
    		    		
    		String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;	
			
			//optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
			
			try{
				FileOutputStream outStreamXL = new FileOutputStream(new File(filename));
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = null;								
				
				if (forceinit){
					sheet = workbook.createSheet(sheetname);
				} else {
					sheet = workbook.getSheet(sheetname);
					if (sheet == null)
						sheet = workbook.createSheet(sheetname);
				}
				
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
    	public void genOXLMetaFromIBean(String filedir, String deffilename, String sheetname, String beanname)  {
    		
    		String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;				
			optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
			
			try{
				FileOutputStream outStreamXL = new FileOutputStream(new File(filename));
				Workbook workbook = new XSSFWorkbook();				
				Sheet sheet = workbook.getSheet(sheetname);
						
				Row row = null;		    
				Cell cell = null;
				
				/*
				Row row = sheet.createRow(0);		    
				Cell cell = row.createCell(0);		    
				cell.setCellType(Cell.CELL_TYPE_STRING); //CellType.STRING
				
				sheet.autoSizeColumn((short)2);
				
				//cell.setCellValue("Optimierung");
				*/
								
				String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBeanAsString(beanname, null); 
				System.out.println(" SpecBeanArch: " + beanArchSpecs.length);				
				//double[][][] beanRestored = new double[beanArchSpecs.length][][];
								
				//String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean("Meta-BeanArchSpecs-Opt", null);
				//String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean(beanname, null);
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 	{
									
					//--Specs of Bean Arch --//								
					int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) - 1;
					int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) -1 ;
					
					int startCol =  com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][1]);
					int endCol = com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][3]);
					//int startCol= Character.getNumericValue( (beanArchSpecs[slBean][1]).charAt(0) )  - Character.getNumericValue('A')  ;			
					//int endCol= Character.getNumericValue( (beanArchSpecs[slBean][3]).charAt(0) ) - Character.getNumericValue('A')  ;
					if (startCol <= 0) System.out.println("createOXLFromIBean: genOXLMetaForIBean: SpecBeanArch: startCol invalid! ");
					if (endCol <= 0) System.out.println("createOXLFromIBean: genOXLMetaForIBean: SpecBeanArch: endCol invalid! ");
					
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
		//1- Create Covar-Bean from its defined architecture //
		public void genOXLDataFromIBean(String filedir, String deffilename, String sheetname, String beanname)  {
	    		
	    	String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;				
			optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
			
				
			//Input Stream  
			//InputStream InStreamXL = null;
			//-- Generate the Out-Excel from persisted Bean --//
			//FileOutputStream foutStreamXL = null;
			//--Get the workbook instance for XLS file --//		
			//Workbook workbook = null;
			
			
			try  {
				
				//-- Get the Meta  Bean Specification --//
				//String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean("Meta-BeanArchSpecs-Covar", null); //IBean-Meta-BeanArchSpecs-XL.ser			
				String metBeanName = "Meta-BeanArchSpecs-" + beanname;
				String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean(metBeanName, null); 
				System.out.println(" SpecBeanArch: " + beanArchSpecs.length);
				
				double[][][] beanRestored = new double[beanArchSpecs.length][][];
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ ) 
				//for (int slBean=0; slBean<2; slBean++ )
				{
					InputStream InStreamXL = new FileInputStream(new File(filename));												 
					Workbook workbook = new XSSFWorkbook(InStreamXL);
					Sheet sheet = workbook.getSheet(sheetname);
					if (sheet == null) workbook.createSheet(sheetname);
					//sheet = workbook.getSheet(sheetname);
																		
					
					//--Specs of Bean Arch --//								
					int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) ;
					int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) ;
					int startCol =  com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][1]);
					int endCol = com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][3]);
					
					if (startCol < 0) System.out.println("createOXLFromIBean: genOXLMetaForIBean: SpecBeanArch: startCol invalid! ");
					if (endCol < 0) System.out.println("createOXLFromIBean: genOXLMetaForIBean: SpecBeanArch: endCol invalid! ");
				
					
					System.out.println(" SpecBeanArch: "+ beanArchSpecs[slBean][0] + ":  " + beanArchSpecs[slBean][1] +  ":  " + beanArchSpecs[slBean][2] 
							+  ":  " + beanArchSpecs[slBean][3] +  ":  " + beanArchSpecs[slBean][4] 
									+ " + startCol-endCol: " + startCol +" - " +endCol 
									+ " + startRow-endRow: " + startRow +" - " +endRow );  
					
					
					//--Init Bean Arch reading from serialized bean--//	
					double[][] beanImpl = null;					
					String[][] beanStrImpl = null;
					
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("DATE") ) {
						beanStrImpl = new String[endRow-startRow+1][endCol-startCol+1];
						beanStrImpl = (String[][]) deserialBean.deserializeBeanAsString(beanArchSpecs[slBean][0], null);
						System.out.println(" Meta Bean:  =" + beanArchSpecs[slBean][0] + "dim1-Row "+ beanStrImpl.length + " dim2-Col : " + beanStrImpl[0].length );
					}
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("STRING") ) {
						beanStrImpl = new String[endRow-startRow+1][endCol-startCol+1];
						beanStrImpl = (String[][]) deserialBean.deserializeBeanAsString(beanArchSpecs[slBean][0], null);
						System.out.println(" Meta Bean:  =" + beanArchSpecs[slBean][0] + "dim1-Row "+ beanStrImpl.length + " dim2-Col : " + beanStrImpl[0].length );
					}
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") ) {					
						beanImpl = new double[endRow-startRow+1][endCol-startCol+1];
						beanImpl = (double[][]) deserialBean.deserializeBeanAsPdouble(beanArchSpecs[slBean][0], null); //if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") )
						System.out.println(" Data Bean:  =" + beanArchSpecs[slBean][0] + "dim1-Row "+ beanImpl.length + " dim2-Col : " + beanImpl[0].length );
					}
					
										
					for (int rowNum = startRow, slRow=0; rowNum <= endRow; rowNum++, slRow++) {
						
						sheet = workbook.getSheet(sheetname);
						Row row = sheet.getRow(rowNum-1);
						if (row == null) row = sheet.createRow(rowNum-1);
							
						for (int colNum = startCol, slCol=0; colNum <= endCol; colNum++, slCol++) {
							  Cell cell = row.createCell(colNum-1);
							  CellStyle style = workbook.createCellStyle();
							  
							  if ( beanArchSpecs[slBean][5].toUpperCase().contains("STRING") ) {											
								  
								  cell.setCellType(Cell.CELL_TYPE_STRING);			
								  
								  //sheet.autoSizeColumn((short)2);
								  
								  //--Aqua background--//				    
								  //--style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
								  //--style.setFillPattern(CellStyle.BIG_SPOTS);						    
								  //--Orange "foreground", foreground being the fill foreground not the font color. --//				    
									
								  //style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
								  //style.setFillPattern(CellStyle.SOLID_FOREGROUND);					
								
								  //--Create a new font and alter it.--//								
								  Font font = workbook.createFont();								
								  font.setFontHeightInPoints((short)9);								
								  font.setFontName("Courier New");								
								  font.setItalic(true);								
								  //font.setBold(true);								
								  //--font.setStrikeout(true);
								  
								  style.setFont(font);
								  style.setWrapText(true);
								  cell.setCellStyle(style);									  
								  
								  if (slRow < 3) System.out.print("Restored Bean-Val: " + beanStrImpl[slRow][slCol] + " :\t");
								  
								  int strsz = 20;
								  if ( beanStrImpl[slRow][slCol].length() <= strsz) 
								  strsz = beanStrImpl[slRow][slCol].length() ;
									
								  cell.setCellValue(beanStrImpl[slRow][slCol].substring(0,strsz));
								  //cell.setCellValue(beanStrImpl[slRow][slCol]);								  
							  } 
							  
							  if ( beanArchSpecs[slBean][5].toUpperCase().contains("DATE") ) {							   
								  cell.setCellType(Cell.CELL_TYPE_STRING);
								  cell.setCellStyle(style);
								  
								  String fmtDate = beanStrImpl[slRow][slCol];
								  
								  //-- use POI - getJavaDate-getExcelDate-- //
								  if ( (beanStrImpl[slRow][slCol]) != null) {
									  
									  java.util.Date date = null;
									  java.text.SimpleDateFormat datestrfmt = new java.text.SimpleDateFormat("dd.MM.yyyy");
									  
									  if ( isDouble(beanStrImpl[slRow][slCol]) ) {
										  date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf( beanStrImpl[slRow][slCol]) );								  
										  fmtDate = (datestrfmt.format(date)).toString();
										  //cell.setCellValue( datestrfmt.format(date));
									  }
									  cell.setCellValue( fmtDate);
								  }
								  
							  } //-- DATE type done --//
							  
							  
							  if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") ) {							   
								  cell.setCellType(Cell.CELL_TYPE_NUMERIC);
								  cell.setCellStyle(style);								  
								  cell.setCellValue(beanImpl[slRow][slCol]);
								  //cell.setCellValue(beanRestored[slBean][slRow][slCol]);
							  }
												  
						} // -- all cols/row - for loop over --//
						
					} // row-iteration over//
					
					
					//-- Write to the Out-Excel from persisted Bean --//				
					FileOutputStream foutStreamXL = new FileOutputStream(new File(filename));						
					workbook.write(foutStreamXL);
					foutStreamXL.close();
					
					
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") ) 
						beanImpl=null;
					else beanStrImpl=null;
					workbook.close();
					InStreamXL.close();
				} //-- All records MetBeanSpecs over --//
				
				
			}	//--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			
			}	
			catch (Exception e) { 
				e.printStackTrace(); 
			}
						
			
		} // Method - genOXLDataFromIBean - Over //
		//////////////////////////////////////////////////////////
		
		
		//////////////////////////////////////////////////////////
		public void genTxtFileFrom1DData(String filedir, String deffilename, double []data)  {
			if (data == null) return;
			String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;
			String filetxtout=filepath+deffilename+".txt";
			PrintWriter btxtfile = null;
			
			try
			
			{
				FileOutputStream byteStream = new FileOutputStream(filetxtout);		       
				btxtfile = new PrintWriter( new OutputStreamWriter(byteStream, "UTF8"));
				
				for (int row=0; row<data.length; row++ ) {
					btxtfile.println(" ");
					btxtfile.print(" Row-Num:"+ String.valueOf(row+1) +": ");
					btxtfile.write( String.valueOf(data[row]) );
					btxtfile.write("   ");					
				}
				btxtfile.println(" ");
				btxtfile.println(" Textfile: All data streamed for total rows: = " + data.length + " : Done !");
				btxtfile.flush();		
				btxtfile.close();
				return;
				
			} //--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
		}
		
		//////////////////////////////////////////////////////////
		public void genTxtFileFrom2DDataFmt(String filedir, String deffilename, double [][]data, int types)  {
		
			if (data == null) return;
			String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;
			String filetxtout=filepath+deffilename+".txt";
			PrintWriter btxtfile = null;
		
			try
			
			{
				FileOutputStream byteStream = new FileOutputStream(filetxtout);		       
				btxtfile = new PrintWriter( new OutputStreamWriter(byteStream, "UTF8"));
				
				for (int row=0; row<data.length; row++) {
					btxtfile.println(" ");
					if (types>0) btxtfile.print(" Row-Num:"+ String.valueOf(row+1) +": ");
					
					for (int col=0; col<data[0].length; col++) {
						if (types==10) btxtfile.print(" Col:"+ String.valueOf(col+1) +": ");
						btxtfile.write( String.valueOf(data[row][col]) );								  
						btxtfile.write(" 	");
				
					}
					btxtfile.println(" ");
			
				}
				btxtfile.println(" ");
				btxtfile.println(" Textfile: All data streamed for total /rows: = " + data.length + " /cols: = " + data[0].length + " : Done !");
				btxtfile.flush();		
				btxtfile.close();
				return;
			
			} //--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
		}
		//////////////////////////////////////////////////////////

		
		//////////////////////////////////////////////////////////
		public void genTxtFileFrom2DData(String filedir, String deffilename, double [][]data)  {
			
			if (data == null) return;
			String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;
			String filetxtout=filepath+deffilename+".txt";
			PrintWriter btxtfile = null;
			
			try
			
			{
				FileOutputStream byteStream = new FileOutputStream(filetxtout);		       
				btxtfile = new PrintWriter( new OutputStreamWriter(byteStream, "UTF8"));
				
				for (int row=0; row<data.length; row++) {
					btxtfile.println(" ");
					btxtfile.print(" Row-Num:"+ String.valueOf(row+1) +": ");
					
					for (int col=0; col<data[0].length; col++) {
					
						btxtfile.write( String.valueOf(data[row][col]) );								  
						btxtfile.write(" 	");
						
					}
					btxtfile.println(" ");
					
				}
				btxtfile.println(" ");
				btxtfile.println(" Textfile: All data streamed for total /rows: = " + data.length + " /cols: = " + data[0].length + " : Done !");
				btxtfile.flush();		
				btxtfile.close();
				return;
				
			} //--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
		}
		//////////////////////////////////////////////////////////
		

		public void genTxtFileFrom2DStringData(String filedir, String deffilename, String [][]data)  {
			
			if (data == null) return;
			String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;
			String filetxtout=filepath+deffilename+".txt";
			PrintWriter btxtfile = null;
			
			try
			
			{
				FileOutputStream byteStream = new FileOutputStream(filetxtout);		       
				btxtfile = new PrintWriter( new OutputStreamWriter(byteStream, "UTF8"));
				
				for (int row=0; row<data.length; row++) {
					btxtfile.println(" ");
					btxtfile.print("RowNum= " + String.valueOf(row+1) + ": ");
					
					for (int col=0; col<data[0].length; col++) {
					
						//btxtfile.write("Row-Idx= " + String.valueOf(row) );
						//btxtfile.write( " : Value=" + String.valueOf(data[row][col]) );
						
						btxtfile.write( " : " + String.valueOf(data[row][col]) );
						btxtfile.write(" 	");
						
					}
					btxtfile.println(" ");
					
				}
				btxtfile.println(" ");
				btxtfile.flush();		
				btxtfile.close();
				return;
				
			} //--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			catch (Exception e) { e.printStackTrace(); }
		}
		//////////////////////////////////////////////////////////
		

		public void genTxtFileFrom2DDateAsString(String filedir, String deffilename, String [][]data)  {
			
			if (data == null) return;
			String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;
			String filetxtout=filepath+deffilename+".txt";
			PrintWriter btxtfile = null;
			
			try
			
			{
				FileOutputStream byteStream = new FileOutputStream(filetxtout);		       
				btxtfile = new PrintWriter( new OutputStreamWriter(byteStream, "UTF8"));
				
				java.util.Date date = null;
				String fmtDate;
				
				for (int row=0; row<data.length; row++) {
					btxtfile.println(" ");
					btxtfile.write("RowNum= " + String.valueOf(row+1) );
					
					for (int col=0; col<data[0].length; col++) {
					
						btxtfile.write("RowIdx= " + String.valueOf(row) );
						
						
						String xlDate=String.valueOf(data[row][col]);
						java.text.SimpleDateFormat datestrfmt = new java.text.SimpleDateFormat("dd.MM.yyyy");
						  
						if ( isDouble(xlDate) ) {
							date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf( xlDate) );								  
							  
						} else {
							  date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(xlDate) );
						}
						fmtDate = (datestrfmt.format(date)).toString();
						  
						btxtfile.write( " : StrValue=" + fmtDate  + ": XLValue=" + xlDate ) ;								  
						btxtfile.write(" 	");
						
					}
					btxtfile.println(" ");
					
				}
				btxtfile.println(" ");
				btxtfile.flush();		
				btxtfile.close();
				return;
				
			} //--try over--//
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
		//1- Create Txt Covar-Bean from its defined architecture //
		public void genTxtDataFromIBean(String filedir, String deffilename, String sheetname, String beanname)  {
			
			String filepath = optimValidateFile.validatePath(filedir);			
			String filename=filepath+deffilename;				
			optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
		
			String filetxtout=filepath+deffilename+beanname+".txt";
			PrintWriter btxtfile = null;

			
			//try ( OutputStreamWriter textoutstream = new OutputStreamWriter(new FileOutputStream(filename) );
				//	BufferedWriter btxtfile = new BufferedWriter(textoutstream) )
			
			try
					
			{
				FileOutputStream byteStream = new FileOutputStream(filetxtout);		       
				btxtfile = new PrintWriter( new OutputStreamWriter(byteStream, "UTF8"));
				
				
				/*
				FileInputStream byteStream = new FileInputStream(filename);
			    InputStreamReader characterStream = new InputStreamReader(byteStream, "UTF8");
			    BufferedWriter btxtfile = new BufferedWriter(characterStream);
				//BufferedWriter btxtfile = new BufferedWriter(new FileWriter(filename));
				*/
				 
				//-- Get the Meta  Bean Specification --//
				//String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean("Meta-BeanArchSpecs-Covar", null); //IBean-Meta-BeanArchSpecs-XL.ser			
				String metBeanName = "Meta-BeanArchSpecs-" + beanname;
				String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean(metBeanName, null); 
				System.out.println(" SpecBeanArch: " + beanArchSpecs.length);
				
				double[][][] beanRestored = new double[beanArchSpecs.length][][];
				
				for (int slBean=0; slBean<beanArchSpecs.length; slBean++ )
				{
					//InputStream InStreamXL = new FileInputStream(new File(filename));												 
																		
					
					//--Specs of Bean Arch --//								
					int startRow= Integer.parseInt(beanArchSpecs[slBean][2]) ;
					int endRow= Integer.parseInt(beanArchSpecs[slBean][4]) ;
					int startCol =  com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][1]);
					int endCol = com.stfe.optim.util.optimColRowNum.convColCharsToNum(beanArchSpecs[slBean][3]);
					
					if (startCol < 0) System.out.println("createOXLFromIBean: genOXLMetaForIBean: SpecBeanArch: startCol invalid! ");
					if (endCol < 0) System.out.println("createOXLFromIBean: genOXLMetaForIBean: SpecBeanArch: endCol invalid! ");
				
					
					System.out.println(" SpecBeanArch: "+ beanArchSpecs[slBean][0] + ":  " + beanArchSpecs[slBean][1] +  ":  " + beanArchSpecs[slBean][2] 
							+  ":  " + beanArchSpecs[slBean][3] +  ":  " + beanArchSpecs[slBean][4] 
									+ " + startCol-endCol: " + startCol +" - " +endCol 
									+ " + startRow-endRow: " + startRow +" - " +endRow );  
					
					
					//--Init Bean Arch reading from serialized bean--//	
					double[][] beanImpl = null;					
					String[][] beanStrImpl = null;
					
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("DATE") ) {
						beanStrImpl = new String[endRow-startRow+1][endCol-startCol+1];
						beanStrImpl = (String[][]) deserialBean.deserializeBeanAsString(beanArchSpecs[slBean][0], null);
						System.out.println(" Meta Bean:  =" + beanArchSpecs[slBean][0] + "dim1-Row "+ beanStrImpl.length + " dim2-Col : " + beanStrImpl[0].length );
					}
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("STRING") ) {
						beanStrImpl = new String[endRow-startRow+1][endCol-startCol+1];
						beanStrImpl = (String[][]) deserialBean.deserializeBeanAsString(beanArchSpecs[slBean][0], null);
						System.out.println(" Meta Bean:  =" + beanArchSpecs[slBean][0] + "dim1-Row "+ beanStrImpl.length + " dim2-Col : " + beanStrImpl[0].length );
					}
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") ) {					
						beanImpl = new double[endRow-startRow+1][endCol-startCol+1];
						beanImpl = (double[][]) deserialBean.deserializeBeanAsPdouble(beanArchSpecs[slBean][0], null); //if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") )
						System.out.println(" Data Bean:  =" + beanArchSpecs[slBean][0] + "dim1-Row "+ beanImpl.length + " dim2-Col : " + beanImpl[0].length );
					}
					
					
										
					for (int rowNum = startRow, slRow=0; rowNum <= endRow; rowNum++, slRow++) {
						
						// Write the complete row in txt file//
						
						btxtfile.println();
						btxtfile.print(" Row-"+ String.valueOf(slRow+1) +": ");
						
						for (int colNum = startCol, slCol=0; colNum <= endCol; colNum++, slCol++) {
							  
							  if ( beanArchSpecs[slBean][5].toUpperCase().contains("STRING") ) {											
								  
								  if (slRow < 3) System.out.print("Restored Bean-Val: " + beanStrImpl[slRow][slCol] + " :\t");
								  
								  
								//btxtfile.write( (beanStrImpl[slRow][slCol]).toString() );
								  
								  int strsz = 20;
								  if ( beanStrImpl[slRow][slCol].length() <= strsz) 
								  strsz = beanStrImpl[slRow][slCol].length() ;									
								  btxtfile.write(beanStrImpl[slRow][slCol].substring(0,strsz));								  								 
								  btxtfile.write("  ");
								  
							  } 
							  
							  if ( beanArchSpecs[slBean][5].toUpperCase().contains("DATE") ) {							   
								  
								  
								  
								  String fmtDate = beanStrImpl[slRow][slCol];
								  //System.out.println(" --++ " + beanStrImpl[slRow][slCol] );
								  
								  //-- use POI - getJavaDate-getExcelDate-- //
								  if ( (beanStrImpl[slRow][slCol]) != null) {
									  
									  java.util.Date date = null;
									  java.text.SimpleDateFormat datestrfmt = new java.text.SimpleDateFormat("dd.MM.yyyy");
									  if ( isDouble(beanStrImpl[slRow][slCol]) ) {
										  date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf( beanStrImpl[slRow][slCol]) );								  
										  fmtDate = (datestrfmt.format(date)).toString();
									  } else {
										  //date =  org.apache.poi.ss.usermodel.DateUtil.getJavaDate(beanStrImpl[slRow][slCol]);
									  }
										  
									  //cell.setCellValue( datestrfmt.format(date));									  
									  btxtfile.write(fmtDate);									  
									  btxtfile.write(" ");
								  }
								  
							  }
							  if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") ) {							   
								  
								  //btxtfile.write( beanImpl[slRow][slCol]) );
								  btxtfile.write( String.valueOf(beanImpl[slRow][slCol]) );								  
								  btxtfile.write(" ");
							  }
												  
						} // -- all cols/row - for loop over --//
						
					} // row-iteration over//
					
					
					btxtfile.println();
					btxtfile.println(" Textfile: All data streamed for BeanName = " + beanArchSpecs[slBean][0] + " : Done !");					
					btxtfile.flush();
					
					if ( beanArchSpecs[slBean][5].toUpperCase().contains("NUM") ) 
						beanImpl=null;
					else beanStrImpl=null;					
					//InStreamXL.close();
				} //-- All records MetBeanSpecs over --//
				

				//-- Close the buffered-txtfile --//								
				btxtfile.close();
				//textoutstream.close();
				byteStream.close();
				
			}	//--try over--//
			catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			
			}	
			catch (Exception e) { 
				e.printStackTrace(); 
			}
						
			
		} // Method - genOXLDataFromIBean - Over //
		//////////////////////////////////////////////////////////
		
		
    	
    	////////////////////////////////////////////////////////////
		public void InitOXLForBean()  {
			//String deffile="OXL_Sensis_Szenarios.xlsx";
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			
			String deffile=initOptimAppConfig.outfilename;			
			String filepath=initOptimAppConfig.outfiledir;
			
			filepath = optimValidateFile.validatePath(filepath);			
			String filename=filepath+deffile;	
			optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
			
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
				
				String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean("Meta-BeanArchSpecs-Opt", null);
				
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
		//////////////////////////////////////////////////////////
		//1- Create Covar-Bean from its defined architecture //
		public void genOXLFromBeanCovar() {
			//String deffile="OXL_Sensis_Szenarios.xlsx";		
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			
			String deffile=initOptimAppConfig.outfilename;			
			String filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);
			String filename=filepath+deffile;
			optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
			
			try{
				
				//Input Stream  
				InputStream InStreamXL = null;
				//-- Generate the Out-Excel from persisted Bean --//
				FileOutputStream foutStreamXL = null;
				//--Get the workbook instance for XLS file --//		
				Workbook workbook = null;
				
				
				//-- Get the Meta  Bean Specification --//
				String[][] beanArchSpecs = (String[][]) deserialBean.deserializeBean("Meta-BeanArchSpecs-Covar", null); //IBean-Meta-BeanArchSpecs-XL.ser			
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
					beanRestored[slBean] = (double[][]) deserialBean.deserializeBeanAsPdouble(beanArchSpecs[slBean][0], null);
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
		public void genOXLFromBeanOpt()  { //STFE_getExcelFromBeanOpt
		
			//String deffile="OXL_Sensis_Szenarios.xlsx";		
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			
			String deffile=initOptimAppConfig.outfilename;			
			String filepath=initOptimAppConfig.outfiledir;
			filepath = optimValidateFile.validatePath(filepath);			
			String filename=filepath+deffile;
			
			optimDeserializeBean deserialBean = new optimDeserializeBean(filepath);
			
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
					beanRestored[slBean] = (double[][]) deserialBean.deserializeBeanAsPdouble(beanArchSpecs[slBean][0], null);
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
		
		
		
	
	
} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    