/**
 * 
 */
package ch.droptilllate.application.views;

import ch.droptilllate.application.error.ParamInitException;
import ch.droptilllate.application.info.ErrorMessage;

/**
 * @author Roewn
 * 
 */
public class InitalViewHelper
{

	public static boolean checkDropboxPassword(String value) throws ParamInitException
	{
		if (!isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid Dropbox Password");
		}
		return true;
	}
	
	public static boolean checkDropboxLoginName(String value) throws ParamInitException
	{
		if (!isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid Dropbox Username");
		}
		return true;
	}

	public static boolean checkDropboxFolderName(String value) throws ParamInitException
	{
		if (!isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid DropTillLate folder");
		}
		return true;
	}

	public static boolean checkDTLPassword(String value) throws ParamInitException
	{
		if (!isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid password");
		}
		return true;
	}

	public static boolean checkTempPath(String value) throws ParamInitException
	{
		if (!isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid temporary file path");
		}
		return true;
	}

	public static boolean checkDropboxPath(String value) throws ParamInitException
	{
		if (!isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid Dropbox path");
		}
		return true;
	}
	

	public static boolean checkForSamePath(String path1, String path2) throws ParamInitException
	{
		if (path1.equals(path2))
		{
			throw new ParamInitException("Conflict", "Path arguments can not be the same: \n" + path1 +" equals " + path2);
		}
		return true;
	}

	public static boolean isFieldAValidString(String field)
	{
		if (field == null)
		{
			return false;
		}
		return field.length() > 0;
	}
	
	

}
