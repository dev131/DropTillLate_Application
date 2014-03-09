package ch.droptilllate.application.properties;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.osgi.util.NLS;

import ch.droptilllate.application.lifecycle.OSValidator;
import ch.droptilllate.application.views.TableIdentifier;

public class Messages extends NLS {
	// =========================================================================
	public static String FilesTableIdentifier_NAME;
	public static String FilesTableIdentifier_TYPE;
	public static String FilesTableIdentifier_SIZE;
	public static String FilesTableIdentifier_DATE;
	public static String BUNDLE_NAME = "ch.droptilllate.application.properties.messages";
	public static String DATE_FORMAT;
	public static String FILE;
	public static String Encryptview_ID;
	public static String LoginPassword;
	public static String CreatePassword;
	public static String SaltMasterPassword;
	public static String CreateSharePasswordDialog;
	public static String OwnerMail;
	
	public static String ImportDialog;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);

	}

	public static Integer getIdSize(){
		return 100000;
	}


	public static String getTableColumnTitle(TableIdentifier identifier) {
		String title = null;
		switch (identifier) {
		case DATE:
			title = FilesTableIdentifier_DATE;
			break;
		case NAME:
			title = FilesTableIdentifier_NAME;
			break;
		case SIZE:
			title = FilesTableIdentifier_SIZE;
			break;
		case TYPE:
			title = FilesTableIdentifier_TYPE;
			break;

		default:
			assert false : identifier + " is not a legal identifier!"; //$NON-NLS-1$
		}

		return title;
	}
	

}