package com.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import main.java.google.pagerank.PageRank;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import com.alexarank.AlexaRank;
import com.common.Strings;
import com.common.Utils;

public class ExcelOperator extends Operator {

	public ExcelOperator(String fileName) {
		super(fileName);
	}

	@Override
	public void getPageRankAndAlexaRank() {
		// TODO Auto-generated method stub		
		try {
			InputStream excelFile = new FileInputStream(mFileName);
			XSSFWorkbook wb = new XSSFWorkbook(excelFile);
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cellPR, cellAR;

			Iterator<Row> rows = sheet.rowIterator();

			int col = 0, colPR = 1, colAR = 2;
			int pageRank = 0, alexaRank = 0;
			String url = null;
			String log = null;
			
			// style for PR >= 5
			CellStyle cs = wb.createCellStyle();
			cs.setFillForegroundColor(HSSFColor.GREEN.index);
			cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
			int sumPR5 = 0;
			int rowIndex = 0;
			XSSFCell tmpCell;
			
			while (rows.hasNext()) {
				++rowIndex;
				row = (XSSFRow) rows.next();
				tmpCell = row.getCell(col);
				if (tmpCell == null || tmpCell.equals(""))
					break;

				url = tmpCell.getStringCellValue();
				System.out.println("URL: " + url);
				
				if (url.matches(Utils.REGEX)) { // check whether URL is valid
					pageRank = PageRank.get(url); // check page rank
					alexaRank = AlexaRank.getAlexaRank(url); // get alexa rank
					
					// write PageRank to excel
					cellPR = row.createCell(colPR);
					cellPR.setCellValue(pageRank);
					if (pageRank >= 5) {
						cellPR.setCellStyle(cs);
						sumPR5 += 1;
					}
					
					cellAR = row.createCell(colAR);
					cellAR.setCellValue(alexaRank);
					
					log = "PR = " + pageRank + ", AR = " + alexaRank;
					if (mEventListener != null) {
						mEventListener.log(log);
					}
					System.out.println(log);
				}
				else {
					System.out.println("URL not valid");
				}
				
				System.out.println("--------------------------");
			}
			
			// create a row for sum
			row = sheet.createRow(rowIndex);
			cellPR = row.createCell(colPR);
			String sPRTotal = ">=PR5 total: " + sumPR5;
			cellPR.setCellValue(sPRTotal);
			System.out.println("Done! " + sPRTotal);
					
			FileOutputStream out = new FileOutputStream(mFileName);
	        wb.write(out);
	        out.flush();
	        out.close();
	        
	        if (mEventListener != null) {
				mEventListener.log("Done!");
			}
	        
	        /*
	         *  Record file history in JSON
	         */
	        JSONObject obj = new JSONObject();
	        obj.put(Strings.JSON_KEY_PATH, mFileName);
	        
	        try {
	        	File config = new File(Strings.FILE_CONFIG);
				if (!config.exists()) {
					config.createNewFile();
				}
				
	    		FileWriter writer = new FileWriter(config);
	    		writer.write(obj.toString());
	    		writer.flush();
	    		writer.close();
	     
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
