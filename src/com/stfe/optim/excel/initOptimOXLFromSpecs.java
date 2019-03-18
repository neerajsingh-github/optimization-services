
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
//import org.apache.xmlbeans.*;



import com.stfe.optim.config.initOptimAppConfig;
//import com.stfe.optim.config.defOptimXLSpecs;
//import com.stfe.optim.util.*;
////////////////////////////////////////////////////////////
import com.stfe.optim.util.optimDeserializeBean;
import com.stfe.optim.util.optimValidateFile;
  

    public class initOptimOXLFromSpecs {
    	
	
		
		////////////////////////////////////////////////////////////	
		public void InitOXLForBean()  {
			//String deffile="OXL_Sensis_Szenarios.xlsx";
			//String filepath="C:\\devdrv\\JOM\\JOMPRJ\\InExcel\\";
			
			String deffile=initOptimAppConfig.outfiledir;	 	
			String filepath=initOptimAppConfig.outfilename;
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
		////////////////////////////////////////////////////////////

	
	} // class over  //
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////
	

    