package ch.droptilllate.application.com;


public interface IXmlDatabase {
	/**
	 * Insert Element
	 * @param obj
	 * @return Dob
	 */
	public Object newElement(Object obj);
	/**
	 * Get Element by ID
	 * @param id int
	 */
	public Object getElementByID(int id);

	/**
	 * Update Element Data
	 * @param Dob 
	 */
	public void updateElement(Object obj);
	
	/**
	 * Delete Element 
	 * @param obj list of Dob
	 */
	public void deleteElement(Object obj);
	
	/**
	 * Check Database if Entries exists
	 * SuccessList = existing entries
	 * ErrorList = not existing entries
	 * @param obj List of Dob
	 * @return CRUDResults
	 */
	public Object checkDatabase(Object obj);
	
}
