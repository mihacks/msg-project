package com.msg.ttp.encryption.suite;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
public class Dbconnect {
	private Connection connect(String dbpath) {
	        // SQLite connection string
	        String url = "jdbc:sqlite:"+ dbpath;
	        Connection conn = null;
	        try {
	            conn = DriverManager.getConnection(url);
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	        return conn;
	    }
	
	 public String selectAll(String dbpath, String nazivfajla){
	        String sql = "SELECT KEY FROM AES_KEYS "
	        			+"WHERE FILE='" + nazivfajla + "';";
	        
	        try (Connection conn = this.connect(dbpath);
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
	            	return rs.getString("KEY");
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
			return "nema";
	    }
	 public void createTbl(String dbpath,String ime, int tip) {
		 
		 String url = "jdbc:sqlite:"+ dbpath;
		 String timeStamp = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss S").format(Calendar.getInstance().getTime());
		 try (Connection conn = DriverManager.getConnection(url);
	            Statement stmt = conn.createStatement()) {
	            // create a new table
			  
			 	String sql="CREATE TABLE IF NOT EXISTS LOG_ENTRY (\n"
		                + "	Timestamp text PRIMARY KEY,\n"
		                + "	Name text NOT NULL, \n"
		                + "	Action text NOT NULL);";
		         stmt.execute(sql);
		         String action;
		         	
		         switch(tip) {
		         case 1: {
		        	 action="loaded";
		        	 break;
		        	}
		         case 2: {
		        	 action="decrypted";
		        	 break;
		         	}
		         case 3: {
		        	 action="encrypted";
		        	 break;
		         }
		         
		         default: action="none";
		 		}
		         String sql1="INSERT INTO LOG_ENTRY VALUES('" + timeStamp + "','"+ime+"','"+action+"')";
	        	 stmt.execute(sql1);
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	       }
	   }
 }

