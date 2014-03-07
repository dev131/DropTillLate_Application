package ch.droptilllate.application.views;

import org.eclipse.osgi.util.NLS;

public class XMLConstruct extends NLS {
	// =========================================================================
	public static String BUNDLE_NAME = "ch.droptilllate.application.views.xmlConstruct";
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

	public static String getChildElementFile() {
		return ChildElementFile;
	}

	public static String getChildElementGhostFolder() {
		return ChildElementGhostFolder;
	}

	public static String getChildElementContainer() {
		return ChildElementContainer;
	}

	public static String getChildElementShareRelation() {
		return ChildElementShareRelation;
	}

	public static String getChildElementShareFolder() {
		return ChildElementShareFolder;
	}

	public static String getRootElement() {
		return RootElement;
	}

	public static String getRootElementFile() {
		return RootElementFile;
	}

	public static String getRootElementGhostFolder() {
		return RootElementGhostFolder;
	}

	public static String getRootElementContainer() {
		return RootElementContainer;
	}

	public static String getRootElementShareRelation() {
		return RootElementShareRelation;
	}

	public static String getRootElementShareFolder() {
		return RootElementShareFolder;
	}

	public static String getNameShareXML() {
		return NameShareXML;
	}
	public static String getNameLocalXML() {
		return NameLocalXML;
	}



	public static String getAttFileName() {
		return AttFileName;
	}

	public static String getAttContainerId() {
		return AttContainerId;
	}

	public static String getAttDate() {
		return AttDate;
	}

	public static String getAttParentId() {
		return AttParentId;
	}

	public static String getAttPath() {
		return AttPath;
	}

	public static String getAttSize() {
		return AttSize;
	}

	public static String getAttType() {
		return AttType;
	}

	public static String getAttId() {
		return AttId;
	}

	public static String getAttShareFolderId() {
		return AttShareFolderId;
	}

	public static String getAttKey() {
		return AttKey;
	}

	public static String getAttMail() {
		return AttMail;
	}

	public static String getAttFolderName() {
		return AttFolderName;
	}

	public static String getIdXMLContainer() {
		return IdXMLContainer;
	}

	public static String getIdShareXMLFiles() {
		return IdShareXMLFiles;
	}

	public static String getIdLocalXMLFiles() {
		return IdLocalXMLFiles;
	}

	public static String getFileExpression() {
		return "/" + getRootElement() + "/" + getRootElementFile() + "/"
				+ getChildElementFile();
	}

	public static String getGhostFolderExpression() {
		return "/" + getRootElement() + "/" + getRootElementGhostFolder() + "/"
				+ getChildElementGhostFolder();
	}

	public static String getContainerExpression() {
		return "/" + getRootElement() + "/" + getRootElementContainer() + "/"
				+ getChildElementContainer();
	}

	public static String getShareRelationExpression() {
		return "/" + getRootElement() + "/" + getRootElementShareRelation()
				+ "/" + getChildElementShareRelation();
	}

	public static String getShareFolderExpression() {
		return "/" + getRootElement() + "/" + getRootElementShareFolder() + "/"
				+ getChildElementShareFolder();
	}

}
