package ch.droptilllate.application.properties;

import org.eclipse.osgi.util.NLS;

public class XMLConstruct extends NLS {
	// =========================================================================
	public static String BUNDLE_NAME = "ch.droptilllate.application.properties.xmlConstruct";
	public static String ChildElementFile;
	public static String ChildElementGhostFolder;
	public static String ChildElementContainer;
	public static String ChildElementShareMember;
	public static String RootElement;
	public static String RootElementFile;
	public static String RootElementGhostFolder;
	public static String RootElementContainer;
	public static String RootElementShareMember;
	public static String NameLocalXML;
	public static String NameShareXML;
	public static String AttFileName;
	public static String AttContainerId;
	public static String AttDate;
	public static String AttParentId;
	public static String AttSize;
	public static String AttType;
	public static String AttId;
	public static String AttKey;
	public static String AttMail;
	public static String AttFolderName;
	public static String AttShareRelationID;
	public static String IdXMLContainer;
	public static String IdLocalXMLFiles;
	public static String IdShareXMLFiles;
	public static String AttCloudPassword;
	public static String AttCloudUsername;
	public static String ChildElementCloudAccount;
	public static String RootElementCloudAccount;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, XMLConstruct.class);
	}


	public static String getFileExpression() {
		return "/" + RootElement + "/" + RootElementFile + "/"
				+ ChildElementFile;
	}

	public static String getGhostFolderExpression() {
		return "/" + RootElement + "/" + RootElementGhostFolder + "/"
				+ ChildElementGhostFolder;
	}

	public static String getContainerExpression() {
		return "/" + RootElement + "/" + RootElementContainer + "/"
				+ ChildElementContainer;
	}

	public static String getShareRelationExpression() {
		return "/" + RootElement + "/" + RootElementShareMember
				+ "/" + ChildElementShareMember;
	}
}
