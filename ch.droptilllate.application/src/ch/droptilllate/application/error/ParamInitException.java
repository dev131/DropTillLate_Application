package ch.droptilllate.application.error;

public class ParamInitException extends Exception
{
	private String category;
	
	
	/**
	 * Creates a new Exception for initalisation parameters.
	 * @param category Category of the Exception (Example: Missing Argument)
	 * @param message description of the Exception
	 */
	public ParamInitException(String category, String message) {
		super(message);
		this.category = category;		
	}

	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}
	
	

}
