package com.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HistoryManager {
	private String mFile;

	public HistoryManager(String file) {
		mFile = file;
	}
	
	public String readData() {
		return null;
	}

	public void saveToTextFile(HistoryData data) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(mFile, true);
			writer.write(data.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveToExcelFile(HistoryData data) {
		try {
			File file = new File(mFile);
			XSSFWorkbook wb;
			XSSFSheet sheet;
			XSSFRow row;
			XSSFCell cell;
			int rowIndex = 0;
			
			if (file.exists()) {
				InputStream excelFile = new FileInputStream(file);
				wb = new XSSFWorkbook(excelFile);
				sheet = wb.getSheetAt(0);
				rowIndex = sheet.getLastRowNum();
				rowIndex += 1;
				
				row = sheet.createRow(rowIndex);
			}
			else {
				wb = new XSSFWorkbook();
				sheet = wb.createSheet();
				row = sheet.createRow(0);
				cell = row.createCell(0);
				cell.setCellValue("URL");
				cell = row.createCell(1);
				cell.setCellValue("PageRank");
				cell = row.createCell(2);
				cell.setCellValue("AlexaRank");
				cell = row.createCell(3);
				cell.setCellValue("Country");
				cell = row.createCell(4);
				cell.setCellValue("City");
				
				row = sheet.createRow(1);
			}

			CellStyle cs = wb.createCellStyle();
			cs.setFillForegroundColor(HSSFColor.GREEN.index);
			cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
			cell = row.createCell(0);
			cell.setCellValue(data.getURL());
			cell = row.createCell(1);
			if (data.getPageRank() >= 5)
				cell.setCellStyle(cs);
			cell.setCellValue(data.getPageRank());
			cell = row.createCell(2);
			cell.setCellValue(data.getAlexaRank());
			
			cell = row.createCell(3);
			cell.setCellValue(data.getCountry());
			
			cell = row.createCell(4);
			cell.setCellValue(data.getCity());
			
			FileOutputStream out = new FileOutputStream(mFile);
	        wb.write(out);
	        out.flush();
	        out.close();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
