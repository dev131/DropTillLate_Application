package ch.droptilllate.application.properties;

import org.eclipse.osgi.util.NLS;

public class XMLConstruct extends NLS {
	// =========================================================================
	public static String BUNDLE_NAME = "ch.droptilllate.application.properties.xmlConstruct";
	public static String ChildElementFile;
	public static String ChildElementGhostFolder;
	public static String ChildElementContainer;
	public static String ChildElementShareRelation;
	public static String ChildElementShareFolder;
	public static String RootElement;
	public static String RootElementFile;
	public static String RootElementGhostFolder;
	public static String RootElementContainer;
	public static String RootElementShareRelation;
	public static String RootElementShareFolder;
	public static String NameLocalXML;
	public static String NameShareXML;
	public static String AttFileName;
	public static String AttContainerId;
	public static String AttDate;
	public static String AttParentId;
	public static String AttPath;
	public static String AttSize;
	public static String AttType;
	public static String AttId;
	public static String AttShareFolderId;
	public static String AttKey;
	public static String AttMail;
	public static String AttFolderName;
	public static String IdXMLContainer;
	public static String IdLocalXMLFiles;
	public static String IdShareXMLFiles;
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
		return "/" + RootElement + "/" + RootElementShareRelation
				+ "/" + ChildElementShareRelation;
	}

	public static String getShareFolderExpression() {
		return "/" + RootElement + "/" + RootElementShareFolder + "/"
				+ ChildElementShareFolder;
	}

}
