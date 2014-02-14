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
import ch.droptilllate.application.dnb.EncryptedFolder;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.EncryptedFolderDob;

public class FileStructureDB {

	@Test
	public void test() {
		IXmlDatabase fileDao = new EncryptedFileDao();
		IXmlDatabase folderDao = new EncryptedFolderDao();
		EncryptedFolderDob root = new EncryptedFolderDob(0, "Root-Folder", new Date(System.currentTimeMillis()), "");
		EncryptedFolderDob child = new EncryptedFolderDob(1, "First-Folder", new Date(System.currentTimeMillis()), "");
		File folder = new File(
				"/Users/marcobetschart/Documents/BDA_project/TestFolder");
		//Create Temp File
		File file = new File(
				"/Users/marcobetschart/Documents/BDA_project/TestFolder/text.xml");
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
		EncryptedFile encfile = new EncryptedFile(file, child);
		EncryptedFolder encfold = new EncryptedFolder(folder, root);		
		//DAO
		//INSERT
			folderDao.newElement(encfold);
			fileDao.newElement(encfile);
		
		//GET PRINT OUT
		List<EncryptedFolderDob> folderlist = ((EncryptedFolderDao) folderDao).getFoldersForFolder(root);	
		System.out.println(folderlist.get(0).getName());		
		//GET PRINT OUT
		List<EncryptedFileDob> filelist = ((EncryptedFileDao) fileDao).getFilesForFolder(child);
		System.out.println(filelist.get(0).getName());
		//Update
		filelist.get(0).setName("newName.xml");
		folderlist.get(0).setName("newName");
		folderDao.updateElement(folderlist.get(0));
		fileDao.updateElement(filelist.get(0));
		
		//GET PRINT OUT
		folderlist = ((EncryptedFolderDao) folderDao).getFoldersForFolder(root);
		System.out.println(folderlist.get(0).getName());
		//GET PRINT OUT
		filelist = ((EncryptedFileDao) fileDao).getFilesForFolder(child);
		System.out.println(filelist.get(0).getName());
		
		folderDao.deleteElement(folderlist);
		fileDao.deleteElement(filelist);
//		
	}

}
