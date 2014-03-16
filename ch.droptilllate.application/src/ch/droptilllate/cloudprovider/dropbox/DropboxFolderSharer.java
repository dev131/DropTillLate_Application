package ch.droptilllate.cloudprovider.dropbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.couldprovider.api.IShareFolder;



/**
 * @author Roewn
 * 
 */
public class DropboxFolderSharer implements IShareFolder
{
	private final static String BASIC_URL = "https://www.dropbox.com/home/";
	private final static String URL_PARAMS = "?share=1";
//	private final static String URL_PARAMS = "";
	private final static String USER_LIMITER = ";";

	public static void shareFolder(String shareRelation)
	{
		URLInvoker.openWebPage(BASIC_URL + Messages.getDropboxName()+"/"+ shareRelation + URL_PARAMS);
	}

	@Override
	public void shareFolder(String droptilllatePath, int shareRelationID,
			List<String> userEmailList)
	{
		// Copy user list to clipboard
		StringBuilder sb = new StringBuilder();
		
		// TODO Check for valid email
		for (String user : userEmailList)
		{
			sb.append(user);
			sb.append(USER_LIMITER);
		}
		ClipboardHelper.copyToClipboard(sb.toString());

		// open the folder in the webbrowser and add the params
		shareFolder(Integer.toString(shareRelationID));

		// test to wait on web page
		WebDriver webDriver = new HtmlUnitDriver();
		
		// navigate to the web page
		webDriver.get(BASIC_URL + Integer.toString(shareRelationID));

//		WebDriverWait wait = new WebDriverWait(webDriver, 5);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("db-modal-box")));
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("db-modal-box")));
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@class='new-collab-input') and type='text']")));

		
		
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("global_share_button")));
		
//		final List<WebElement> iframes = webDriver.findElements(By.xpath("//input[contains(@type,'text')]"));
//		
//		System.out.println("page found");
		
//		webDriver.switchTo().frame("db-modal-content");
		
		
//		// paste in the commands
//		WebElement inviteTextField = webDriver.findElement(By.name("custom_message"));
//		if (inviteTextField != null)
//		{
//			System.out.println("textfield found");
//		}
//		

//		try
//		{
//			Robot r = new Robot();
//			r.keyPress(KeyEvent.VK_CONTROL);
//			r.keyPress(KeyEvent.VK_V);
//			r.keyRelease(KeyEvent.VK_CONTROL);
//			r.keyRelease(KeyEvent.VK_V);
//
//		} catch (AWTException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
