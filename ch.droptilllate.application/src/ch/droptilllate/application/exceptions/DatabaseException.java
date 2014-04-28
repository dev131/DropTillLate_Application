package ch.droptilllate.application.exceptions;

/**
 * @author dev131
 * 
 */
@SuppressWarnings("serial")
public class DatabaseException extends Exception{

	private DatabaseStatus error;
	
	public DatabaseException(String msg)
	{
		super(msg);
	}
	
	public DatabaseException(DatabaseStatus error)
	{
		super(error.getMessage());
		this.error = error;
	}
	
	public DatabaseException(DatabaseStatus error, String msg)
	{
		super(msg);
		this.error = error;
		this.error.setMessage(msg);
	}

	public DatabaseException(String path, String msg)
	{
		super(msg + ": " + path);
	}

	public String getMessage() {
		return super.getMessage();
	}

	/**
	 * @return the error enumerator (FileError)
	 */
	public DatabaseStatus getError() {
		return error;
	}

	/**
	 * @param error the error enumerator to set (FileError)
	 */
	public void setError(DatabaseStatus error) {
		this.error = error;
	}
}