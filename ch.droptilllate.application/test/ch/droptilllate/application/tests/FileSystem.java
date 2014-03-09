package ch.droptilllate.application.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.couldprovider.api.IFileSystemCom;

public class FileSystem {

	@Test
	public void test() {
//		IFileSystemCom ifileSystem = new FileSystemCom();
//		int id;
//		String name;
//		String type;
//		long size;
//		String path;
//		int containerID;
//		File file = new File(
//				"/Users/marcobetschart/Documents/BDA_project/TestFolder/text.xml");
//		try {
//			RandomAccessFile raf = new RandomAccessFile(file, "rw");
//			raf.setLength(30);
//			raf.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		id = 1;
//		name = "text.xml";
//		type = "xml";
//		size = file.getTotalSpace();
//		java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime()
//				.getTime());
//		path = file.getAbsolutePath();
//		containerID = 0;
		// EncryptedFileDob(int id, String name, String type, long size,Date
		// date, String path, int containerID)
		// List<EncryptedFileDob> droppedFiles, String containerPath
//		List<EncryptedFileDob> newFiles = new ArrayList<EncryptedFileDob>();
//		EncryptedFileDob fdob = new EncryptedFileDob(id, name, type, size,
//				date, path, containerID);
	//	newFiles.add(fdob);
//		ifileSystem.encryptFile(newFiles,
//				"/Users/marcobetschart/Documents/BDA_project/TestFolder/");
//		ifileSystem.decryptFile(newFiles,
//				"/Users/marcobetschart/Documents/BDA_project/TestFolder/");
//		ifileSystem.deleteFile(newFiles);
	}

}
