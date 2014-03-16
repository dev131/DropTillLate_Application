/**
 * 
 */
package ch.droptilllate.cloudprovider.dropbox;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author Roewn
 * 
 */
public class ClipboardHelper
{
	public static synchronized void copyToClipboard(String content)
	{
		// get clipboard context
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection str = new StringSelection(content);
		clipboard.setContents(str, null);
	}

	public static synchronized void pasteFromClipboard()
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		// get clipboard context
		Transferable data = clipboard.getContents(null);

		// is context string type

		boolean bIsText = ((data != null) && (data.isDataFlavorSupported(DataFlavor.stringFlavor)));

		// if yes, translate context to string type and write it

		if (bIsText)
		{

			try
			{
				String s = (String) data.getTransferData(DataFlavor.stringFlavor);
				System.out.println(s);

			} catch (UnsupportedFlavorException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
