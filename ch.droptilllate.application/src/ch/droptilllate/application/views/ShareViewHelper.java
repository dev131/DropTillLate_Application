/**
 * 
 */
package ch.droptilllate.application.views;

import java.util.List;

import ch.droptilllate.application.error.ParamInitException;
import ch.droptilllate.application.info.ErrorMessage;

/**
 * @author Roewn
 * 
 */
public class ShareViewHelper
{

	public static boolean checkPassword(String value) throws ParamInitException
	{
		if (!ViewHelper.isFieldAValidString(value))
		{
			throw new ParamInitException("Missing Parameter", "Please provide a password for the shared files ");
		}
		return true;
	}
	
	public static boolean checkMailList(org.eclipse.swt.widgets.List value) throws ParamInitException
	{
		if (value == null || value.getItems().length == 0)
		{
			throw new ParamInitException("Missing Parameter", "Please provide at least one email address");
		}
		return true;
	}	
	
	public static boolean checkFileList(List value) throws ParamInitException
	{
		if (value == null || value.isEmpty())
		{
			throw new ParamInitException("Missing Parameter", "No files selected for sharing");
		}
		return true;
	}	
	
	
	

}
