package ch.droptilllate.application.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	// =========================================================================
	public static String FilesTableIdentifier_NAME;
	public static String FilesTableIdentifier_TYPE;
	public static String FilesTableIdentifier_SIZE;
	public static String FilesTableIdentifier_DATE;
	public static String BUNDLE_NAME = "ch.droptilllate.application.views.messages";
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
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
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
	

	public static String getPathShareFolderXML() {		
		return getPathDropBox() +"XML/sharefolderxml.xml"; 
	}

	public static String getPathContainerXML() {
		return getPathDropBox()  +"XML/containerxml.xml";
	}

	public static String getPathFilesXML() {
		return getPathDropBox()  +"XML/filesxml.xml";
	}

	public static String getPathFolderXML() {
		return getPathDropBox() +"XML/folderxml.xml";
	}

	public static String getPathDropBox() {
		return PathDropBox;
	}

	public static String getPathLocalTemp() {
		return PathLocalTemp;
	}
	

	public static String getPathShareRealtionXML() {
		return getPathDropBox() +"XML/sharerelation.xml";
	}

	public static String getShareFolder0name() {
		return ShareFolder0name;
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
