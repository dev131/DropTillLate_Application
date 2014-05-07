package ch.droptilllate.application.views;

import java.io.File;

import ch.droptilllate.application.error.ParamInitException;

public class ViewHelper
{
	public static final String SPECIAL_CHARACTERS = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final int MAX_PASSWORD_LENGTH = 25;
	public static final int MIN_UPPERCASE_CHARS = 1;
	public static final int MIN_DIGITS = 2;


	public static boolean isFieldAValidString(String field)
	{
		if (field == null)
		{
			return false;
		}
		return field.length() > 0;
	}

	/**
	 * Checks if a specified directory folder exists on the file system
	 * 
	 * @param dirPath path of the directory
	 * @return true if file or directory exists
	 */
	public static boolean doesDirExistOnFS(String dirPath)
	{
		try
		{
			File file = new File(dirPath);
			return file.exists();
		} catch (Exception e)
		{
			return false;
		}
	}

	public static boolean isValidDir(String dirPath, boolean createDir)
	{
		try
		{
			File dir = new File(dirPath);
			if (createDir) dir.mkdirs();
			return dir.isDirectory();
		} catch (Exception e)
		{
			return false;
		}

	}
	
	public static boolean isValidPassword(String password) throws ParamInitException {
		int qtyUppercase = 0;
		int qtyDigits = 0;
		
	    if (!isFieldAValidString(password)) {
	    	throw new ParamInitException("Missing Parameter", "Please provide a valid Dropbox Password");
	    }
	    // remove whitespaces
	    password = password.trim();
	    int len = password.length();
	    if(len < MIN_PASSWORD_LENGTH || len > MAX_PASSWORD_LENGTH) {
	    	throw new ParamInitException("Invalid Password", "Passsword must have at least " + MIN_PASSWORD_LENGTH +
	    			 " characters and less than "+ MAX_PASSWORD_LENGTH +".");
	    }
	    char[] aC = password.toCharArray();
	    for(char c : aC) {
	        if (Character.isUpperCase(c)) {
	        	qtyUppercase++;
//	            System.out.println(c + " is uppercase.");
	        } else
	        if (Character.isLowerCase(c)) {
//	            System.out.println(c + " is lowercase.");
	        } else
	        if (Character.isDigit(c)) {
	        	qtyDigits++;
//	            System.out.println(c + " is digit.");
	        } else
	        if (SPECIAL_CHARACTERS.indexOf(String.valueOf(c)) >= 0) {
//	            System.out.println(c + " is valid symbol.");
	        } else {
	        	throw new ParamInitException("Invalid Password", c + " is an invalid character in the password.");
	        }
	    }
	    if (qtyUppercase < MIN_UPPERCASE_CHARS) throw new ParamInitException("Invalid Password", "Password must contain at least " + MIN_UPPERCASE_CHARS+
	    		" uppercase characters"); 
	    if (qtyDigits < MIN_DIGITS) throw new ParamInitException("Invalid Password", "Password must contain at least " + MIN_DIGITS+
	    		" digits [0-9]"); 
	    return true;
	}

}
