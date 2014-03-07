package ch.droptilllate.application.views;

import org.eclipse.osgi.util.NLS;

import ch.droptilllate.application.lifecycle.OSValidator;

public class Messages extends NLS {
	// =========================================================================
	private static String OS = System.getProperty("os.name").toLowerCase();
	public static String FilesTableIdentifier_NAME;
	public static String FilesTableIdentifier_TYPE;
	public static String FilesTableIdentifier_SIZE;
	public static String FilesTableIdentifier_DATE;
	public static String BUNDLE_NAME;
	public static String DATE_FORMAT;
	public static String FILE;
	public static String Encryptview_ID;
	public static String LoginPassword;
	public static String CreatePassword;
	public static String SaltMasterPassword;
	public static String CreateSharePasswordDialog;
	public static String PathDropBox;
	public static String PathLocalTemp;
	public static String ShareFolder0name;
	public static String OwnerMail;
	public static String Slash;
	
	public static String ImportDialog;
	
	static {
 
		if (OSValidator.isWindows()) {
			System.out.println("This is Windows");
			BUNDLE_NAME = "ch.droptilllate.application.views.messages_windows";
		} else if (OSValidator.isMac()) {
			System.out.println("This is Mac");
			BUNDLE_NAME = "ch.droptilllate.application.views.messages_osx";
		} else if (OSValidator.isUnix()) {
			System.out.println("This is Unix or Linux");
		} else if (OSValidator.isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String getImportDialog() {
		return ImportDialog;
	}

	public static String getOwnerMail() {
		return OwnerMail;
	}

	public static String getCreateSharePasswordDialog() {
		return CreateSharePasswordDialog;
	}

	public static String getSaltMasterPassword() {
		return SaltMasterPassword;
	}

	public static String getLoginPassword() {
		return LoginPassword;
	}

	public static String getCreatePassword() {
		return CreatePassword;
	}

	public static String getEncryptview_ID() {
		return Encryptview_ID;
	}

	public static String getPathDropBox() {
		return PathDropBox;
	}

	public static String getPathLocalTemp() {
		return PathLocalTemp;
	}


	public static String getShareFolder0name() {
		return ShareFolder0name;
	}

	public static String getSlash() {
		return Slash;
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
