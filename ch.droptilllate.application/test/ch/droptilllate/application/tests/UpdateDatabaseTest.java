package ch.droptilllate.application.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.api.XMLDatabase;

public class UpdateDatabaseTest {

	static String tmpPath = "/Users/marcobetschart/Documents/Eclipse_Test/DB";
	static String dbPath = "/Users/marcobetschart/Documents/Eclipse_Test/TMP";
	static String propertiePath= "";
	static GhostFolderDob root = new GhostFolderDob(0, "Root", null);
	
	@BeforeClass 
	public static void testInitDB() {
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
		DatabaseStatus status = database.createDatabase("password", propertiePath, DBSituation.UPDATE_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
		status =database.openDatabase("password", propertiePath, null, DBSituation.UPDATE_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.UPDATE_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		ShareRelation relation = new ShareRelation(30000, "password");
		//set KeyManager
		KeyManager keyManager = KeyManager.getInstance();
		keyManager.addKeyRelation(relation.getID(), "password");		
		//insert filelist
		List<EncryptedFileDob> list = generateFiles();
		database.createElement(list);		
		status =database.closeTransaction(propertiePath, relation.getID(), DBSituation.UPDATE_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
	
	
	@Test
	public void insertUpdateDatabase() {
		testInitLocalDB();
		ShareRelation relation = new ShareRelation(30000, "password");
		IDatabase database = new XMLDatabase();
		DatabaseStatus status =database.openDatabase("password", propertiePath, relation.getID(), DBSituation.UPDATE_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.UPDATE_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		List<EncryptedFileDob> list = (List<EncryptedFileDob>) database.getElementAll(EncryptedFileDob.class);
		System.out.println("//////////////UPDATE FILE LIST//////////////");
		for(EncryptedFileDob dob : list){
			System.out.println(dob.getName());
		}
		
	}

	public static List<EncryptedFileDob> generateFiles(){
		int i = 1;
		List<EncryptedFileDob> list = new ArrayList<EncryptedFileDob>();
		EncryptedFileDob dob;
		while(i <= 10){
			dob = new EncryptedFileDob(
					null, 
					"testfiles"+ i, 
					new Date(System.currentTimeMillis()), 
					"", 
					root, 
					100L, 
					null);
			i++;
			list.add(dob);
		}
		return list;
		
	}
	
	public static void testInitLocalDB() {

		//set KeyManager
		KeyManager keyManager = KeyManager.getInstance();
		keyManager.addKeyRelation(Messages.getIdSize(), "password");		
		
		IDatabase database = new XMLDatabase();
		DatabaseStatus status = database.createDatabase("password", propertiePath, DBSituation.UPDATE_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}


}
