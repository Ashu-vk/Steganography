package com.stego;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	public static void main(String[] args) {
		String filePath = "C:\\Users\\avishwakarma\\Downloads\\invoice_with_org.xlsx";
		File file = new File(filePath);
		  try (FileInputStream fis = new FileInputStream(file);
		             Workbook workbook = new XSSFWorkbook(fis)) {

		            Sheet sheet = workbook.getSheetAt(0); // First sheet
		            Sheet mySheet = workbook.createSheet("invoice with contact");
		            for (int i=0; i<sheet.getPhysicalNumberOfRows(); i++) {
		            	Row row = sheet.getRow(i);
		            	Row myRow = mySheet.createRow(i);
		            	myRow.createCell(0).setCellValue(row.getCell(0) != null?row.getCell(0).toString():null);
		            	myRow.createCell(1).setCellValue(row.getCell(18) != null?row.getCell(18).toString():null);
//		                for (Cell cell : row) {
//		                    switch (cell.getCellType()) {
//		                        case STRING:
//		                            System.out.print(cell.getStringCellValue() + "\t");
//		                            break;
//		                        case NUMERIC:
//		                            System.out.print(cell.getNumericCellValue() + "\t");
//		                            break;
//		                        case BOOLEAN:
//		                            System.out.print(cell.getBooleanCellValue() + "\t");
//		                            break;
//		                        default:
//		                            System.out.print("UNKNOWN\t");
//		                    }
//		                }
//		                System.out.println(row.getCell(18)); // New line after each row
		            }
		            
		            try (FileOutputStream fos = new FileOutputStream(file)) {
		                workbook.write(fos);
		                System.out.println("Excel written successfully.");
		            }

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	}
}
