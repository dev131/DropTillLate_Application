package ch.droptilllate.database.api;

import java.util.List;

import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.database.exceptions.DatabaseStatus;


public interface IDatabase {
	
	/**
	 * Create Database xml file
	 * @param path ../../file.xml
	 * @param DBSituation
	 * @param path properties
	 * @return DatabaseStatus
	 */
	public DatabaseStatus createDatabase(String password, String propertiePath, DBSituation situation);
	/**
	 * Open Database
	 * @param password (password for encryption)
	 * @param DBSituation
	 * @param path properties
	 * @param if (shareFolderDI == null) -> decryptDatabase if (not)->database already decrypted in temp folder
	 * @return DatabaseStatus
	 */
	public DatabaseStatus openDatabase(String password, String propertiePath, Integer shareFolderID,DBSituation situation);
	
	/**
	 * Open new Transaction
	 * @param path properties additional
	 * @param DBSituation
	 * @return DatabaseStatus 
	 */
	public DatabaseStatus openTransaction(String propertiePath, DBSituation situation);
	
	/**
	 * Reset Transaction
	 * @return DatabaseStatus 
	 */
	public DatabaseStatus rollback();
	
	/**
	 * Close and save Transaction
	 * @param path properties additional
	 * @param shareRelationID id destination
	 * @param DBSituation
	 * @return DatabaseStatus 
	 */
	public DatabaseStatus closeTransaction(String propertiePath, Integer shareRelationID, DBSituation situation);
	
	/**
	 * Create element
	 * @param obj Element
	 * @return  DatabaseStatus
	 */
	public List<?> createElement(Object obj);
	
	/**
	 * Create elements
	 * @param obj Lis<Element>
	 * @return Database status
	 */
	public List<?> createElement(List<?> obj);
	
	/**
	 * Delete element with ID
	 * @param obj Element
	 * @return  DatabaseStatus
	 */
	public DatabaseStatus deleteElement(Object obj);
	
	/**
	 * Delete elements with ID
	 * @param obj Lis<Element>
	 * @return Database status
	 */
	public DatabaseStatus deleteElement(List<?> obj);
	
	/**
	 * Update elements with ID
	 * @param obj Element
	 * @return Database status
	 */
	public DatabaseStatus updateElement(Object obj);
	
	/**
	 * Update elements with ID
	 * @param obj Lis<Element>
	 * @return Database status
	 */
	public DatabaseStatus updateElement(List<?> obj);
	
	/**
	 * Get Element 
	 * @param type
	 * @param argument
	 * @param value
	 * @return Lis<Element>
	 */
	public List<?> getElement(Object type, String argument, String value);
	
	/**
	 * Get Element by Parentfolder (just for files and folder)
	 * @param type
	 * @param argument
	 * @param value
	 * @return Lis<Element>
	 */
	public List<?> getElementByParent(Object type, GhostFolderDob folder);
	
	/**
	 * Get Element all
	 * @return List<Object>
	 */
	public List<?> getElementAll(Object Type);
}
