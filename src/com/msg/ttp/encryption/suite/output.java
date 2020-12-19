package com.msg.ttp.encryption.suite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class output {
	
	public static void listFilesForFolder(final File folder, String dbpath, String outpath, String newKey) throws IOException {
		for (final File fileEntry: folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry, dbpath, outpath, newKey);
			}
			else {
				String ime = fileEntry.getName();
				int index=ime.lastIndexOf(".");
				String check=ime.substring(index);
				String outName=ime.substring(0,index);
				
				Dbconnect app=new Dbconnect();
				//extract the key from database
				String kljuc=app.selectAll(dbpath,ime);
				
				//otvara textualni fajl
				if(check.equals(".txt")) {
					if (kljuc.equals("nema")) {
					//	System.out.println("There is no key for " + ime + " in the database.");
						continue;
					}
					String data="";
					data = new String(Files.readAllBytes(Paths.get(folder+"/"+ime)));
					//insert log entry
					app.createTbl(dbpath,ime,1);
					
					String decrypted = Encryptor.decrypt(kljuc, data);
					String encrypted = Encryptor.encrypt(newKey, decrypted);
					
					File theDir = new File(outpath+"/out");
					theDir.mkdir();
					
					BufferedWriter writer = new BufferedWriter(new FileWriter(outpath+"/out/"+outName+"out.txt"));
					writer.write(encrypted);
					writer.close();
				//	System.out.println(decrypted);
					app.createTbl(dbpath,ime,3);
					
			        
					  continue;
				}
				
				//otvara excel file
				if(check.equals(".xlsx")) {
					if (kljuc.equals("nema")) {
						System.out.println("There is no key for " + ime + " in the database.");
						continue;
					}
					String putanja=folder + "/" + ime;
					FileInputStream inputStream = new FileInputStream(new File(putanja));
					//input log entry
					app.createTbl(dbpath, ime, 1);
					
					Workbook workbook = new XSSFWorkbook(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					DataFormatter dataFormatter = new DataFormatter();
					
					//output .xlsx file variable
					
					Workbook workbook2=new XSSFWorkbook();
            	//	CreationHelper createHelper=workbook2.getCreationHelper();
            		Sheet sheet2=workbook2.createSheet("Encrypted");
            		int rowNr=0;
            		
					for (Row row: sheet) {
						
						Row row2=sheet2.createRow(rowNr++);
						int cellNr=0;
						
						for(Cell cell: row) {
							String cellValue = dataFormatter.formatCellValue(cell);
		                	String decrypted = Encryptor.decrypt(kljuc, cellValue);
		                	String encrypted = Encryptor.encrypt(newKey, decrypted);
		                	
		                	row2.createCell(cellNr++).setCellValue(encrypted);
		              }
		        
					}
					File theDir = new File(outpath+"/out");
					theDir.mkdir();
					FileOutputStream fileOut=new FileOutputStream(outpath+"/out/"+outName+"out.xlsx");
            		workbook2.write(fileOut);
            		app.createTbl(dbpath, ime, 3);
            		fileOut.close();
					workbook2.close();
					workbook.close();
					
					continue;
			}
				System.out.println("Data file type not supported. " + ime);
	   	}		
		}
	}
}
