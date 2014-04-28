package ch.droptilllate.application.exceptions;

import ch.droptilllate.filesystem.error.FileError;

/**
 * @author dev131
 * 
 */
@SuppressWarnings("serial")
public class ApplicationException extends Exception{

private ApplicationError error;
	
	public ApplicationException(String msg)
	{
		super(msg);
	}

	public ApplicationException(ApplicationError error, String msg)
	{
		super(msg);
		this.error = error;
		this.error.setMessage(msg);
	}

	public ApplicationException(String path, String msg)
	{
		super(msg + ": " + path);
	}

	public String getMessage() {
		return super.getMessage();
	}

	/**
	 * @return the error enumerator (FileError)
	 */
	public ApplicationError getError() {
		return error;
	}

	/**
	 * @param error the error enumerator to set (FileError)
	 */
	public void setError(ApplicationError error) {
		this.error = error;
	}
	
}
