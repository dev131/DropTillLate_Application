package ch.droptilllate.database;

import java.util.List;

import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.model.GhostFolderDob;


public interface IDatabase {
	
	/**
	 * Create Database xml file
	 * @param path ../../file.xml
	 * @param local boolean for local or share database
	 * @param path properties
	 * @return DatabaseStatus
	 */
	public DatabaseStatus createDatabase(String password, boolean local, String propertiePath);
	/**
	 * Open Database
	 * @param path (from xml file)
	 * @param key (key for encryption)
	 * @param local boolean for local or share database
	 * @param path properties
	 * @return DatabaseStatus
	 */
	public DatabaseStatus openDatabase(String password, boolean local, String propertiePath);
	
	/**
	 * Open new Transaction
	 * @param local boolean for local or share database
	 * @param path properties
	 * @return DatabaseStatus 
	 */
	public DatabaseStatus openTransaction(boolean local, String propertiePath);
	
	/**
	 * Reset Transaction
	 * @return DatabaseStatus 
	 */
	public DatabaseStatus rollback();
	
	/**
	 * Close and save Transaction
	 * @param path properties
	 * @param local boolean for local or share database
	 * @return DatabaseStatus 
	 */
	public DatabaseStatus closeTransaction(boolean local, String propertiePath);
	/**
	 * Create element
	 * @param obj Element
	 * @return  DatabaseStatus
	 */
	public DatabaseStatus createElement(Object obj);
	
	/**
	 * Create elements
	 * @param obj Lis<Element>
	 * @return Database status
	 */
	public DatabaseStatus createElement(List<Object> obj);
	
	/**
	 * Delete element
	 * @param obj Element
	 * @return  DatabaseStatus
	 */
	public DatabaseStatus deleteElement(Object obj);
	
	/**
	 * Delete elements
	 * @param obj Lis<Element>
	 * @return Database status
	 */
	public DatabaseStatus deleteElement(List<Object> obj);
	
	/**
	 * Update elements
	 * @param obj Element
	 * @return Database status
	 */
	public DatabaseStatus updateElement(Object obj);
	
	/**
	 * Update elements
	 * @param obj Lis<Element>
	 * @return Database status
	 */
	public DatabaseStatus updateElement(List<Object> obj);
	
	/**
	 * Get Element 
	 * @param type
	 * @param argument
	 * @param value
	 * @return Lis<Element>
	 */
	public List<Object> getElement(Object type, String argument, String value);
	
	/**
	 * Get Element by Parentfolder (just for files and folder)
	 * @param type
	 * @param argument
	 * @param value
	 * @return Lis<Element>
	 */
	public List<Object> getElementByParent(Object type, GhostFolderDob folder);
	
	/**
	 * Get Element all
	 * @return List<Object>
	 */
	public List<Object> getElementAll(Object Type);
}
