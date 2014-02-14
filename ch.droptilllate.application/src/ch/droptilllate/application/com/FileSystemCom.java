package ch.droptilllate.application.com;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.filesystem.api.ContainerInfo;
import ch.droptilllate.filesystem.api.FileHandlingSummary;
import ch.droptilllate.filesystem.api.FileInfo;
import ch.droptilllate.filesystem.api.FileInfoDecrypt;
import ch.droptilllate.filesystem.api.FileInfoEncrypt;
import ch.droptilllate.filesystem.api.FileSystemHandler;
import ch.droptilllate.filesystem.api.IFileSystem;

/**
 * FileSystemCom
 * @author marcobetschart
 *
 */
public class FileSystemCom implements IFileSystemCom{	
	@Override
	public List<FileInfo> encryptFile(List<EncryptedFileDob> droppedFiles, String containerPath) {
		FileHandlingSummary filehandling_result = null;
		Iterator<EncryptedFileDob> itr = droppedFiles.iterator();
		 IFileSystem ifile = new FileSystemHandler(); 
		 List<FileInfoEncrypt> fileInfoList = new ArrayList<FileInfoEncrypt>();
		 while(itr.hasNext()) {
	   	  EncryptedFileDob droppedfile = (EncryptedFileDob) itr.next();
	   	  File f = new File(droppedfile.getPath());
	   	  //TODO Message Box
	 			if(f.exists()) {  System.out.println("file Exist"); }	  	
	 			//TODO SharePath
	 			fileInfoList.add(new FileInfoEncrypt(droppedfile.getId(), droppedfile.getPath(),Messages.getLocalPathDropBoxMaster())) ;
	     }			
		try {			
			filehandling_result = ifile.encryptFiles(fileInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filehandling_result.getFileInfoSuccessList();		
		}

	@Override
	public List<FileInfo> decryptFile(List<EncryptedFileDob> droppedFiles,
			String containerPath) {
		FileHandlingSummary filehandling_result = null;
		 Iterator itr = droppedFiles.iterator();
	 IFileSystem ifile = new FileSystemHandler(); 
		List<FileInfoDecrypt> fileInfoList = new ArrayList<FileInfoDecrypt>();
	 while(itr.hasNext()) {
  	  EncryptedFileDob droppedfile = (EncryptedFileDob) itr.next();
  	  File f = new File(droppedfile.getPath());
			if(f.exists()) {  System.out.println("file Exist"); }	  				  					
			fileInfoList.add(new FileInfoDecrypt(droppedfile.getId(), droppedfile.getType(), containerPath, droppedfile.getPath(), droppedfile.getContainerID()));
    }			
	try {
		filehandling_result = ifile.decryptFiles(fileInfoList);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return filehandling_result.getFileInfoSuccessList();
	}

	@Override
	public boolean deleteFile(List<EncryptedFileDob> fileList) {
		Iterator itr = fileList.iterator();
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		//TODO delete on filesystem
		boolean successful = false;
		IFileSystem ifile = new FileSystemHandler(); 
		 while(itr.hasNext()) {
	    	  EncryptedFileDob droppedfile = (EncryptedFileDob) itr.next();
	    	  File f = new File(droppedfile.getPath());
	  			if(f.exists()) {  System.out.println("file Exist"); }	
	  			fileInfoList.add(new FileInfo(droppedfile.getId(), new ContainerInfo(droppedfile.getContainerID(), droppedfile.getPath())));
	      }					
		ifile.deleteFiles(fileInfoList);
		return successful;

	}

	
}
	
	
