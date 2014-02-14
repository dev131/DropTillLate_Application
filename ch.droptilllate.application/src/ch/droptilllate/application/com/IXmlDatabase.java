package ch.droptilllate.application.com;


public interface IXmlDatabase {
	/**
	 * Insert Element
	 * @param obj
	 */
	public Object newElement(Object obj);
	/**
	 * Get Element by ID
	 * @param id
	 * @return
	 */
	public Object getElementByID(int id);

	/**
	 * Update Element Data
	 * @param obj
	 * @return Object
	 */
	public boolean updateElement(Object obj);
	
	/**
	 * Delete Element
	 * @param obj
	 * @return
	 */
	public boolean deleteElement(Object obj);
}
