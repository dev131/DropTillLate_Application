package ch.droptilllate.application.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.database.IDatabase;
import ch.droptilllate.database.XMLDatabase;

public class DatabaseTest {
	static String tmpPath = "/Users/marcobetschart/Documents/Eclipse_Test/DB";
	static String dbPath = "/Users/marcobetschart/Documents/Eclipse_Test/TMP";
	static String propertiePath= "";
	static Boolean local = true;
	@Test
	public void test() {
		//Set Properties
		Configuration conf = new Configuration();
		try {
			conf.setPropertieDropBoxPath(dbPath, propertiePath);
			conf.setPropertieTempPath(tmpPath, propertiePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IDatabase database = new XMLDatabase();
		DatabaseStatus status = database.createDatabase("password", local, propertiePath);
		if(status != DatabaseStatus.OK){
			System.out.println(status.getMessage());
		}
		database.openDatabase("password", local, propertiePath);
		if(status != DatabaseStatus.OK){
			System.out.println(status.getMessage());
		}		
		database.openTransaction(local, propertiePath);
		if(status != DatabaseStatus.OK){
			System.out.println(status.getMessage());
		}	
		database.closeTransaction(local, propertiePath);
		if(status != DatabaseStatus.OK){
			System.out.println(status.getMessage());
		}
	}

}
