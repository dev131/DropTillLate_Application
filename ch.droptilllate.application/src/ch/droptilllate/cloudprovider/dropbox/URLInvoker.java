package ch.droptilllate.cloudprovider.dropbox;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Roewn
 *
 */
public class URLInvoker
{
	public static void openWebPage(String url)
	{
		if (Desktop.isDesktopSupported())
		{
			// For Windows
			Desktop desktop = Desktop.getDesktop();
			try
			{
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
}
