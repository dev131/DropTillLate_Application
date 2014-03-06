package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.model.EncryptedFileDob;


public abstract class AbstractXmlDatabase {
	/**
	 * Insert Element
	 * @param obj
	 * @param key if file already decrypted = null
	 * @return Dob
	 */
	public abstract Object newElement(Object obj, String key);
	/**
	 * Get Element by ID
	 * @param id int
	 * @param key if file already decrypted = null
	 */
	public abstract Object getElementByID(int id, String key);

	/**
	 * Update Element Data
	 * @param Dob 
	 * @param key if file already decrypted = null
	 */
	public abstract void updateElement(Object obj, String key);
	
	/**
	 * Delete Element 
	 * @param obj list of Dob
	 * @param key if file already decrypted = null
	 */
	public abstract void deleteElement(Object obj, String key);
	
	/**
	 * Check Database if Entries exists
	 * SuccessList = existing entries
	 * ErrorList = not existing entries
	 * @param obj List of Dob
	 * @param key if file already decrypted = null
	 * @return CRUDResults
	 */
	public abstract Object checkDatabase(Object obj, String key);
	
}