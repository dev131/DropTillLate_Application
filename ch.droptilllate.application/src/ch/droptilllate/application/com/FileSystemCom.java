package ch.droptilllate.application.com;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import ch.droptilllate.application.converter.FileInfoConverter;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.application.views.XMLConstruct;
import ch.droptilllate.filesystem.info.*;
import ch.droptilllate.filesystem.security.KeyRelation;
import ch.droptilllate.filesystem.api.FileError;
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
	public CRUDCryptedFileInfo encryptFile(List<EncryptedFileDob> droppedFiles, String shareRelationPath) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfoEncrypt> fileInfoList = new ArrayList<FileInfoEncrypt>();		
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : droppedFiles) {
			//Check If File Exist
			checkFileExisting(fileDob);
			//If no new shareRelation -> File is already in a shareRelation
			if (shareRelationPath == null)
				shareRelationPath = fileDob.getShareRelationPath();				
			FileInfoEncrypt fileInfo;		
			//If it have a ContainerId -> it is already in a Container /UPDATE
			if(fileDob.getContainerId() != null){
				hashSet.add(fileDob.getContainerId());
				fileInfo = new FileInfoEncrypt(fileDob.getId(), fileDob.getTempPlainPath(), shareRelationPath, fileDob.getContainerId()); 
			}	
			else{
				fileInfo= new FileInfoEncrypt(fileDob.getId(), fileDob.getFileSystemPath(), shareRelationPath);	
			}
			fileInfoList.add(fileInfo);
		}
		IFileSystem ifile = new FileSystemHandler();
		ShareFolderDao shareDao = new ShareFolderDao();
		// MasterShareFolder
		// TODO ShareRelation
		KeyRelation relation = getKeyRelation(hashSet);	
		//if the File is not in a SharedFolder/First Drop,/Put it in master-folder local
		if (relation == null) {
			ShareFolder shareFolder = (ShareFolder) shareDao
					.getElementByID(Integer.parseInt(Messages
							.getShareFolder0name()), null);
			relation = new KeyRelation();
			relation.addKeyOfShareRelation(shareFolder.getPath() + Messages.getShareFolder0name(),
					shareFolder.getKey());
		}
		filehandling_result =ifile.encryptFiles(fileInfoList, relation);
	// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = new CRUDCryptedFileInfo();
		result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), droppedFiles);
		return result;
	}

	// TODO Key transfer
	@Override
	public CRUDCryptedFileInfo decryptFile(List<EncryptedFileDob> droppedFiles,String shareRelationPath) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfoDecrypt> fileInfoList = new ArrayList<FileInfoDecrypt>();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : droppedFiles) {
			//Check If File Exist
			checkFileExisting(fileDob);
			hashSet.add(fileDob.getContainerId());
			fileInfoList.add(new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), shareRelationPath, fileDob.getShareRelationPath(), fileDob
					.getContainerId()));
		}
		KeyRelation relation = getKeyRelation(hashSet);
		IFileSystem ifile = new FileSystemHandler();
		filehandling_result = ifile.decryptFiles(fileInfoList,relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), droppedFiles);
		return result;
	}

	@Override
	public CRUDCryptedFileInfo deleteFile(List<EncryptedFileDob> fileList) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : fileList) {
			//Check If File Exist
			checkFileExisting(fileDob);
			hashSet.add(fileDob.getContainerId());
			fileInfoList.add(new FileInfo(fileDob.getId(), new ContainerInfo(
					fileDob.getContainerId(), fileDob.getShareRelationPath())));
		}
		KeyRelation relation = getKeyRelation(hashSet);
		IFileSystem ifile = new FileSystemHandler();
		filehandling_result = ifile.deleteFiles(fileInfoList, relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter(); 
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}

	@Override
	public CRUDCryptedFileInfo moveFiles(List<EncryptedFileDob> fileList,
			ShareFolder sharedFolder) {
		// TODO Key transfer
		List<FileInfoMove> fileInfoList = new ArrayList<FileInfoMove>();
		FileHandlingSummary filehandling_result = null;
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : fileList) {
			hashSet.add(fileDob.getContainerId());
			fileInfoList.add(new FileInfoMove(fileDob.getId(), fileDob
					.getSize(), fileDob.getPath(), fileDob.getContainerId(),
					sharedFolder.getPath() + sharedFolder.getID()));
		}
		KeyRelation relation = getKeyRelation(hashSet);
		relation.addKeyOfShareRelation(sharedFolder.getPath() + sharedFolder.getID(), sharedFolder.getKey());
		IFileSystem ifile = new FileSystemHandler();
		filehandling_result = ifile.moveFiles(fileInfoList,relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}


	/**
	 * HashSet with ContainerIDs to KeyRelation (sharedID / key)
	 * @param hashSet
	 * @return KeyRelation(sharedID/key)
	 */
	private KeyRelation getKeyRelation(HashSet<Integer> hashSet) {
		// KeyRelation
		KeyRelation relation = new KeyRelation();
		if(hashSet.isEmpty()) relation = null;
		for (Integer ContainerId : hashSet) {
			ContainerDao containerDao = new ContainerDao();
			EncryptedContainer container = (EncryptedContainer) containerDao
					.getElementByID(ContainerId, null);
			ShareFolderDao shareFolderDao = new ShareFolderDao();
			ShareFolder shareFolder = (ShareFolder) shareFolderDao
					.getElementByID(container.getShareFolderId(), null);
			relation.addKeyOfShareRelation(shareFolder.getPath()+shareFolder.getID(),
					shareFolder.getKey());
		}
		return relation;
	}
	
	
	
	private boolean checkFileExisting(EncryptedFileDob dob){
		boolean exist = false;
		File f = new File(dob.getPath());			
		if (f.exists()) {
			System.out.println("file Exist");
			exist= true;
		}
		return exist;
	}

	@Override
	public boolean encryptFile(ShareFolder destinationShareFolder,  boolean local) {		
		FileInfoEncrypt fileInfo = null;
		//create ghost file
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		if(!local){
			 sxml = new StructureXmlDob(Messages.getPathLocalTemp() , destinationShareFolder.getKey(), false, true);
			
		}
		else{
			sxml = new StructureXmlDob(Messages.getPathLocalTemp() , destinationShareFolder.getKey(), true, true);
		}
		fileDob = sxml.getEncryptedFileDob();
		//Check if it exist
		checkFileExisting(fileDob);
		//TODO encrypt file !!!!!
		fileInfo = new FileInfoEncrypt(fileDob.getId(), fileDob.getTempPlainPath(), destinationShareFolder.getPath() + destinationShareFolder.getID(), fileDob.getContainerId()); 
		IFileSystem ifile = new FileSystemHandler();
		fileInfo = ifile.storeFileStructure(fileInfo, destinationShareFolder.getKey());
		if(fileInfo.getError() == FileError.NONE) return true;
		return false;
	}

	@Override
	public boolean decryptFile(ShareFolder sourceShareFolder,boolean local) {
		FileInfoDecrypt fileInfo = null;
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		if(!local){
			sxml = new StructureXmlDob(sourceShareFolder.getPath()+ sourceShareFolder.getID(), sourceShareFolder.getKey(), false, false);
		}
		else{
			sxml = new StructureXmlDob(sourceShareFolder.getPath()+ sourceShareFolder.getID(), sourceShareFolder.getKey(), true, false);			
		}		
		fileDob = sxml.getEncryptedFileDob();
			//Check If File Exist
			checkFileExisting(fileDob);
			fileInfo = new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), Messages.getPathLocalTemp(), fileDob.getShareRelationPath(), fileDob
					.getContainerId());
		
		IFileSystem ifile = new FileSystemHandler();		
		fileInfo = ifile.loadFileStructure(fileInfo,  sourceShareFolder.getKey());
		if(fileInfo.getError() == FileError.NONE) return true;
		return false;
	}

}
