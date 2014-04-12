package ch.droptilllate.application.tests;

import org.junit.Test;

import ch.droptilllate.application.dao.ContainerDao;

import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.query.ContainerQuery;

import ch.droptilllate.application.xml.AbstractXmlDatabase;

public class KeyManagement {

	@Test
	public void test() {
		// Container(int id, int shareFolderID)
		// public ShareFolder(int id, String path, String key)
		int anzahl = 10;
		AbstractXmlDatabase cDao = new ContainerDao();
	//	AbstractXmlDatabase sDao = new ShareFolderDao();
//		ContainerQuery containerQuery = new ContainerQuery();
//		ShareFolderQuery sharefolderQuery = new ShareFolderQuery();
		// Create
//		for (int i = 0; i < anzahl; i++) {
//			EncryptedContainer container = new EncryptedContainer(i, i);
//			ShareFolder sharefolder = new ShareFolder(i, "testpath",
//					"a88aasdf8dsf8asdf:" + i);
//			cDao.newElement(container);
//			sDao.newElement(sharefolder);
//		}
//		// Get All Keys per container
//		for (int i = 0; i < anzahl; i++) {
//			EncryptedContainer container = (EncryptedContainer) cDao.getElementByID(i);
//			ShareFolder sharefolder = (ShareFolder) sDao.getElementByID(container
//					.getShareFolderId());
//			System.out.println("Container " + i + " in " + sharefolder.getID()
//					+ " with key: " + sharefolder.getKey());
//		}
//
//		// delete all Entrys
//		for (int i = 0; i < anzahl; i++) {
//			EncryptedContainer container = new EncryptedContainer(i, i);
//			ShareFolder sharefolder = new ShareFolder(i, "testpath",
//					"a88aasdf8dsf8asdf:" + i);
//			sDao.deleteElement(sharefolder);
//			cDao.deleteElement(container);
		}

}
