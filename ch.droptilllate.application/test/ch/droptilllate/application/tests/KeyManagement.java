package ch.droptilllate.application.tests;

import org.junit.Test;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.Container;
import ch.droptilllate.application.dnb.ShareFolder;
public class KeyManagement {

	@Test
	public void test() {
		// Container(int id, int shareFolderID)
		// public ShareFolder(int id, String path, String key)
		int anzahl = 10;
		IXmlDatabase cDao = new ContainerDao();
		IXmlDatabase sDao = new ShareFolderDao();
//		ContainerQuery containerQuery = new ContainerQuery();
//		ShareFolderQuery sharefolderQuery = new ShareFolderQuery();
		// Create
		for (int i = 0; i < anzahl; i++) {
			Container container = new Container(i, i);
			ShareFolder sharefolder = new ShareFolder(i, "testpath",
					"a88aasdf8dsf8asdf:" + i);
			cDao.newElement(container);
			sDao.newElement(sharefolder);
		}
		// Get All Keys per container
		for (int i = 0; i < anzahl; i++) {
			Container container = (Container) cDao.getElementByID(i);
			ShareFolder sharefolder = (ShareFolder) sDao.getElementByID(container
					.getShareFolderID());
			System.out.println("Container " + i + " in " + sharefolder.getID()
					+ " with key: " + sharefolder.getKey());
		}

		// delete all Entrys
		for (int i = 0; i < anzahl; i++) {
			Container container = new Container(i, i);
			ShareFolder sharefolder = new ShareFolder(i, "testpath",
					"a88aasdf8dsf8asdf:" + i);
			sDao.deleteElement(sharefolder);
			cDao.deleteElement(container);
		}

	}

}
