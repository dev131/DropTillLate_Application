package ch.droptilllate.application.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	// =========================================================================
	public static String FilesTableIdentifier_NAME;
	public static String FilesTableIdentifier_TYPE;
	public static String FilesTableIdentifier_SIZE;
	public static String FilesTableIdentifier_DATE;
	public static String BUNDLE_NAME = "ch.droptilllate.application.views.messages";
	public static String TableView_ProgressMonitorDialog_Encrypt;
	public static String TableView_ProgressMonitor_SubTast_Handler;
	public static String DATE_FORMAT;
	public static String FILE;
	public static String LocalPathDropBox;
	public static String LocalPathTemp;
	public static String LocalSharePath;
	public static String Encryptview_ID;
	public static String FilesXMLpath;
	public static String FolderXMLpath;
	public static String ShareFolderXMLpath;
	public static String ContainerXMLpath;

	

	public static String getEncryptview_ID() {
		return Encryptview_ID;
	}


	public static String getFolderXMLpath() {
		return FolderXMLpath;
	}


	public static String getShareFolderXMLpath() {
		return ShareFolderXMLpath;
	}

	public static String getContainerXMLpath() {
		return ContainerXMLpath;
	}

	public static String getFilesXMLpath() {
		return FilesXMLpath;
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


	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String getLocalPathDropbox() {
		return LocalPathDropBox;
	}

	public static String getTempFolder() {
		return LocalPathTemp;
	}

	
	

	
	
	
	
	
	
	
	
}
