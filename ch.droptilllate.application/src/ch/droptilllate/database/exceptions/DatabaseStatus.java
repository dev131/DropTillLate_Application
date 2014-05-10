package ch.droptilllate.database.exceptions;
/**
 * @author dev131
 * 
 */
public enum DatabaseStatus {

	NOENTRY("No enctry"),
	TRANSACTION_FAILED("Transaction failed"),
	OK("Transaction successful"),
	DATABASE_NOT_CREATED("Failure during database creation"),
	CANNOT_OPEN_DATABASE("Failed to open database"),
	WRONG_QUERY("Error while executing query"),
	CANNOT_WRITE_TO_XML("Failed to write into database"),
	DATABASE_ENCRYPTION_FAILED("Database encryption failed"),
	DATABASE_DECRYPTION_FAILED("Database decryption failed"),
	CANNOT_DELETE_TEMPFILE("Failed to delete database temp file");
	
	private String status;
	private String message;


	private DatabaseStatus(String status) {
		this.status = status;
	}
	
	public String getError(){
		return this.status;
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
	
}
