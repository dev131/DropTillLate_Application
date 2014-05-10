package ch.droptilllate.application.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.exceptions.DatabaseStatus;
import ch.droptilllate.database.xml.XMLDatabase;
import ch.droptilllate.keyfile.api.KeyFileHandlingSummary;
 
public class DatabaseTest {
	static String tmpPath = "/Users/marcobetschart/Documents/Eclipse_Test/DB";
	static String dbPath = "/Users/marcobetschart/Documents/Eclipse_Test/TMP";
	static String propertiePath= "";
	static Boolean local = true;
	GhostFolderDob root = new GhostFolderDob(0, "Root", null);
	
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
		//set KeyManager
		KeyManager keyManager = KeyManager.getInstance();
		keyManager.addKeyRelation(Messages.getIdSize(), "password");		
		
		IDatabase database = new XMLDatabase();
		DatabaseStatus status = database.createDatabase("password", propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
		status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		status =database.closeTransaction(propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
	
	@Test
	public void InsertDatabase(){
		
		IDatabase database = new XMLDatabase();
		DatabaseStatus status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		GhostFolderDob dob1 = new GhostFolderDob(null, "InsertTestFolder", root);
		database.createElement(dob1);
		
		status =database.closeTransaction(propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
	
	@Test
	public void rollBack(){		
		IDatabase database = new XMLDatabase();
		DatabaseStatus status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		GhostFolderDob dob1 = new GhostFolderDob(null, "RollBackTestFolder", root);
		database.createElement(dob1);
		database.rollback();
		
		status =database.closeTransaction(propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
	
	
	@Test
	public void getData(){		
		IDatabase database = new XMLDatabase();
		DatabaseStatus status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		List<EncryptedFileDob> dob = (List<EncryptedFileDob>) database.getElement(EncryptedFileDob.class, XMLConstruct.AttId, "100000");
		for(EncryptedFileDob dob1 :dob){
			System.out.println("////////////GET METHOD///////////");
			System.out.println(dob1.getName());
		}		
		status =database.closeTransaction(propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
	
	@Test
	public void deleteDatabase(){
		//INSERT
		IDatabase database = new XMLDatabase();
		DatabaseStatus status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		GhostFolderDob dob1 = new GhostFolderDob(null, "DeleteTestFolder", root);
		database.createElement(dob1);		
		status =database.closeTransaction(propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
		
		//DELETE
		database = new XMLDatabase();
		status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		status = database.deleteElement(dob1);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
		status =database.closeTransaction(propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
	
	@Test
	public void updateDatabase(){
		//INSERT
		IDatabase database = new XMLDatabase();
		DatabaseStatus status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		GhostFolderDob dob1 = new GhostFolderDob(null, "UpdateTestFolder", root);
		database.createElement(dob1);				
		status =database.closeTransaction(propertiePath, Messages.getIdSize(),DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
		//Update
		database = new XMLDatabase();
		status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		List<GhostFolderDob> dob  = (List<GhostFolderDob>) database.getElement(GhostFolderDob.class, XMLConstruct.AttFolderName, "UpdateTestFolder");
		if(dob.isEmpty()){
			fail("NO Entry found");
		}
		dob1 = dob.get(0);
		dob1.setName("UpdateTestFolder2");
		status = database.updateElement(dob1);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
		status =database.closeTransaction(propertiePath, Messages.getIdSize(),DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
	
	@Test
	public void getElementByParent(){
		//INSERT
		IDatabase database = new XMLDatabase();
		DatabaseStatus status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		GhostFolderDob dob1 = new GhostFolderDob(null, "UpdateTestFolder", root);
		database.createElement(dob1);				
		status =database.closeTransaction(propertiePath, Messages.getIdSize(),DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
		//GetElementByParent
		database = new XMLDatabase();
		status =database.openDatabase("password", propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}		
		status =database.openTransaction(propertiePath, DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}	
		List<GhostFolderDob> dob  = (List<GhostFolderDob>) database.getElementByParent(GhostFolderDob.class, root);
		if(dob.isEmpty()){
			fail("NO Entry found");
		}
		System.out.println("////////////GET METHOD by PARENT///////////");
		for(GhostFolderDob dob2: dob){
				System.out.println(dob2.getName());
			}
		status =database.closeTransaction(propertiePath, Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		if(status != DatabaseStatus.OK){
			fail(status.getMessage());
		}
	}
}
