package com.msg.ttp.encryption.suite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Readfile {
	

	
	//skenira dati folder za sve fajlove i otvara one ciji tipovi odgovaraju 
	public static String listFilesForFolder(final File folder, String dbpath) throws IOException {
		String out="";
		for (final File fileEntry: folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry, dbpath);
			}
			else {
				String ime = fileEntry.getName();
				int index=ime.lastIndexOf(".");
				String check=ime.substring(index);
				Dbconnect app=new Dbconnect();
				String kljuc=app.selectAll(dbpath,ime);
				
			
				//otvara textualni fajl
				if(check.equals(".txt")) {
					if (kljuc.equals("nema")) {
						System.out.println("There is no key for " + ime + " in the database.");
						continue;
					}
					String data="";
					data = new String(Files.readAllBytes(Paths.get(folder+"/"+ime)));
					app.createTbl(dbpath,ime,1);
					String decrypted = Encryptor.decrypt(kljuc, data);
					//System.out.println(decrypted);
					 out=out+ime+":\r\n \r\n";
					 out=out+decrypted+"\r\n";
					
					app.createTbl(dbpath,ime,2);
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
					app.createTbl(dbpath, ime, 1);
					Workbook workbook = new XSSFWorkbook(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					DataFormatter dataFormatter = new DataFormatter();
					out=out+ime+":\r\n \r\n";
					for (Row row: sheet) {
						for(Cell cell: row) {
							//sheet.getLastRowNum();
							String cellValue = dataFormatter.formatCellValue(cell);
		                	String decrypted = Encryptor.decrypt(kljuc, cellValue);
		                	//System.out.print(decrypted + "\t\t");
		                	out=out+decrypted+"\t";
		               
		            }
						out=out+"\r\n";
						//System.out.println();
		        }
					out=out+"\r\n";
					app.createTbl(dbpath, ime, 2);
					workbook.close();
					continue;
			}
	   	}		
		}
		return out;
	}
	
}

