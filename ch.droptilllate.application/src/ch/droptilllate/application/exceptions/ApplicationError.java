package ch.droptilllate.application.exceptions;

/**
 * @author dev131
 * 
 */
public enum ApplicationError {

	NONE("No error");
	
	private String error;
	private String message;


	private ApplicationError(String error) {
		this.error = error;
	}
	
	public String getError(){
		return this.error;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString(){
		return error +": "+ message;
	}
}
