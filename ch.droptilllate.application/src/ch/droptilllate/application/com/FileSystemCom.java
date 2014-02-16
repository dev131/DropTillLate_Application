package ch.droptilllate.application.com;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.droptilllate.application.converter.FileInfoConverter;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.filesystem.info.*;
import ch.droptilllate.filesystem.api.FileHandlingSummary;
import ch.droptilllate.filesystem.api.FileSystemHandler;
import ch.droptilllate.filesystem.api.IFileSystem;

/**
 * FileSystemCom
 * 
 * @author marcobetschart
 * 
 */
public class FileSystemCom implements IFileSystemCom {
	// TODO Key transfer
	@Override
	public CRUDCryptedFileInfo encryptFile(List<EncryptedFileDob> droppedFiles,String containerPath) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfoEncrypt> fileInfoList = new ArrayList<FileInfoEncrypt>();
		for(EncryptedFileDob fileDob: droppedFiles){
			File f = new File(fileDob.getPath());
			//TODO MessageBox
			if (f.exists()) {
				System.out.println("file Exist");
			}
			// TODO SharePath
			fileInfoList.add(new FileInfoEncrypt(fileDob.getId(), fileDob
							.getPath(),containerPath));
		}
		IFileSystem ifile = new FileSystemHandler();
		filehandling_result = ifile.encryptFiles(fileInfoList);
		//Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = new CRUDCryptedFileInfo();
		result = converter.convertFileInfoList(filehandling_result.getFileInfoSuccessList(),filehandling_result.getFileInfoErrorList(), droppedFiles);
		return result;
	}

	// TODO Key transfer
	@Override
	public CRUDCryptedFileInfo decryptFile(List<EncryptedFileDob> droppedFiles,
			String containerPath) {
		FileHandlingSummary filehandling_result = null;		
		List<FileInfoDecrypt> fileInfoList = new ArrayList<FileInfoDecrypt>();
		for(EncryptedFileDob fileDob : droppedFiles){
			File f = new File(fileDob.getPath());
			if (f.exists()) {
				System.out.println("file Exist");
			}
			
			fileInfoList.add(new FileInfoDecrypt(fileDob.getId(),
					fileDob.getType(), containerPath,
					fileDob.getPath(), fileDob.getContainerId()));		
		}
		IFileSystem ifile = new FileSystemHandler();
		filehandling_result = ifile.decryptFiles(fileInfoList);
		//Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(filehandling_result.getFileInfoSuccessList(),filehandling_result.getFileInfoErrorList(), droppedFiles);
		return result;
	}

	@Override
	public CRUDCryptedFileInfo deleteFile(List<EncryptedFileDob> fileList) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		for(EncryptedFileDob fileDob : fileList){
			File f = new File(fileDob.getPath());
			if (f.exists()) {
				System.out.println("file Exist");
			}
			fileInfoList.add(new FileInfo(fileDob.getId(),
					new ContainerInfo(fileDob.getContainerId(), fileDob
							.getPath())));
		}		
		IFileSystem ifile = new FileSystemHandler();
		filehandling_result =ifile.deleteFiles(fileInfoList);
		//Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(filehandling_result.getFileInfoSuccessList(),filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}

	@Override
	public CRUDCryptedFileInfo moveFiles(List<EncryptedFileDob> fileList, ShareFolder sharedFolder) {
		// TODO Key transfer
		List<FileInfoMove> fileInfoList = new ArrayList<FileInfoMove>();
		FileHandlingSummary filehandling_result = null;
		for(EncryptedFileDob fileDob: fileList){
	  		fileInfoList.add(new FileInfoMove(fileDob.getId(), 
	  				fileDob.getSize(), 
	  				fileDob.getPath(), 
	  				fileDob.getContainerId(), 
	  				sharedFolder.getPath() + sharedFolder.getID()));
		}
		IFileSystem ifile = new FileSystemHandler(); 
		filehandling_result= ifile.moveFiles(fileInfoList);
		//Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(filehandling_result.getFileInfoSuccessList(),filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}
}
