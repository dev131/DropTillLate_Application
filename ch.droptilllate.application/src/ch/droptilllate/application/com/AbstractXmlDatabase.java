package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.model.EncryptedFileDob;


public abstract class AbstractXmlDatabase {
	/**
	 * Insert Element
	 * @param obj
	 * @return Dob
	 */
	public abstract Object newElement(Object obj);
	/**
	 * Get Element by ID
	 * @param id int
	 */
	public abstract Object getElementByID(int id);

	/**
	 * Update Element Data
	 * @param Dob 
	 */
	public abstract void updateElement(Object obj);
	
	/**
	 * Delete Element 
	 * @param obj list of Dob
	 */
	public abstract void deleteElement(Object obj);
	
	/**
	 * Check Database if Entries exists
	 * SuccessList = existing entries
	 * ErrorList = not existing entries
	 * @param obj List of Dob
	 * @return CRUDResults
	 */
	public abstract Object checkDatabase(Object obj);
	
}
