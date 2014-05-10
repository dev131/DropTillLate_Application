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
public class InitialViewHelper
{

	public static boolean checkDropboxPassword(String value) throws ParamInitException
	{
		if (!ViewHelper.isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid Dropbox Password");
		}
		return true;
	}
	
	public static boolean checkDropboxLoginName(String value) throws ParamInitException
	{
		if (!ViewHelper.isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid Dropbox Username");
		}
		return true;
	}

	public static boolean checkDropboxFolderName(String value) throws ParamInitException
	{
		boolean error = false;
		if (!ViewHelper.isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid DropTillLate folder");
		}		
		return true;
	}

	public static boolean checkDTLPassword(String value) throws ParamInitException
	{
		return ViewHelper.isValidPassword(value);
	}

	public static boolean checkTempPath(String value) throws ParamInitException
	{
		if (!ViewHelper.isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid temporary file path");
		}
		if (!ViewHelper.isValidDir(value, true)){
			throw new ParamInitException("Invalid Parameter", "Specified temporary folder path is not valid: \n" + value);
		}
		return true;
	}

	public static boolean checkForExistingFolder(String value, String errorMessageFolderName) throws ParamInitException
	{
		if (!ViewHelper.isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a valid "+errorMessageFolderName+" path");
		}		
		if (!ViewHelper.isValidDir(value, false)){
			throw new ParamInitException("Invalid Parameter", "Specified "+errorMessageFolderName+" folder path is not valid, please make sure it is the path to a valid directory: \n" + value);
		}
		if (!ViewHelper.doesDirExistOnFS(value)){
			throw new ParamInitException("Invalid Parameter", "Specified "+errorMessageFolderName+" folder path could not be found: \n" + value);
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

	
	
	

}
