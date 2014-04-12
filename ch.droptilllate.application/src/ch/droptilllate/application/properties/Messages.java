package ch.droptilllate.application.properties;

import java.io.File;








import org.eclipse.osgi.util.NLS;

import ch.droptilllate.application.provider.TableIdentifier;
import ch.droptilllate.application.provider.TableIdentifierShare;

public class Messages extends NLS {
	// =========================================================================
	public static String FilesTableIdentifier_NAME;
	public static String FilesTableIdentifier_TYPE;
	public static String FilesTableIdentifier_SIZE;
	public static String FilesTableIdentifier_DATE;
	public static String FileTableIdentifier_Shared;
	public static String BUNDLE_NAME = "ch.droptilllate.application.properties.messages";
	public static String DATE_FORMAT;
	public static String FILE;
	public static String Encryptview_ID;
	public static String LoginPassword;
	public static String CreatePassword;
	public static String CreateSharePasswordDialog;
	public static String OwnerMail;
	public static String KeyFile;
	
	public static String ImportDialog;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);

	}

	public static Integer getIdSize(){
		return 100000;
		
	}
	
	public static String getApplicationpath(){
		return System.getProperty("user.dir");
	}
	
	public static String getDropboxName(){
		File file = new File(Configuration.getPropertieDropBoxPath(false));
		return file.getName();
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
	
	public static String getTableColumnTitleShare(TableIdentifierShare identifier) {
		String title = null;
		switch (identifier) {

		case NAME:
			title = FilesTableIdentifier_NAME;
			break;
		case SHARE:
			title = FileTableIdentifier_Shared;
			break;	
		default:
			assert false : identifier + " is not a legal identifier!"; //$NON-NLS-1$
		}

		return title;
	}
	

}
