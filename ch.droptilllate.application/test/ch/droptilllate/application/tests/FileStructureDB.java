package ch.droptilllate.application.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xml.sax.SAXException;

import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.EncryptedFolderDao;
import ch.droptilllate.application.dnb.EncryptedFile;
import ch.droptilllate.application.dnb.GhostFolder;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;

public class FileStructureDB {

	@Test
	public void test() {
		GhostFolderDob root = new GhostFolderDob(0, "Root-Folder", new Date(System.currentTimeMillis()), "");
		//GhostFolderDob child = new GhostFolderDob(1, "First-Folder", new Date(System.currentTimeMillis()), "");
		File folder = new File("TestFolder");
		//Create Temp File
		File file = new File("text.xml");
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.setLength(30);
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//File newFile, EncryptedFolderDob parent		
		GhostFolder f = new GhostFolder(null,file, root);
		GhostFolderDob child = new GhostFolderDob(f);
		EncryptedFile encfile = new EncryptedFile(null,file, child);
		EncryptedFileDob dobf = new EncryptedFileDob(encfile);
		
		//DAO
		//INSERT
		IXmlDatabase fileDao = new EncryptedFileDao();
		IXmlDatabase folderDao = new EncryptedFolderDao();
		GhostFolderDob tempFolderDob = (GhostFolderDob) folderDao.newElement(child);
		EncryptedFileDob tempFileDob = (EncryptedFileDob) fileDao.newElement(dobf);		
		//GET PRINT OUT
		List<GhostFolderDob> folderlist = ((EncryptedFolderDao) folderDao).getFoldersInFolder(root);
		System.out.println(folderlist.get(0).getName());		
		//GET PRINT OUT
		List<EncryptedFileDob> filelist = ((EncryptedFileDao) fileDao).getFilesInFolder(child);
		System.out.println(filelist.get(0).getName());
		System.out.println(filelist.get(0).getParent().getId());
		//Update
		filelist.get(0).setName("newName.xml");
		folderlist.get(0).setName("newName");
		folderDao.updateElement(folderlist.get(0));
		fileDao.updateElement(filelist.get(0));		
		//GET PRINT OUT
		folderlist = ((EncryptedFolderDao) folderDao).getFoldersInFolder(root);
		System.out.println(folderlist.get(0).getName());
		//GET PRINT OUT
		filelist = ((EncryptedFileDao) fileDao).getFilesInFolder(child);
		System.out.println(filelist.get(0).getName());
		//DeleteFile
		folderDao.deleteElement(folderlist);
		fileDao.deleteElement(filelist);
//		
	}

}
