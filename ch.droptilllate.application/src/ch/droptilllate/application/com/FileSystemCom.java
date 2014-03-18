package ch.droptilllate.application.com;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.droptilllate.application.converter.FileInfoConverter;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.couldprovider.api.IFileSystemCom;
import ch.droptilllate.filesystem.error.FileError;
import ch.droptilllate.filesystem.info.*;
import ch.droptilllate.filesystem.security.KeyRelation;
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
	private static FileSystemCom instance = null;
	private IFileSystem ifile;
	private FileSystemCom(){
        ifile = new FileSystemHandler(Configuration.getPropertieDropBoxPath(false), Configuration.getPropertieTempPath(false));
	}
	/* Static 'instance' method */
	   public static FileSystemCom getInstance( ) {
		   if(instance == null) {
		         instance = new FileSystemCom();
		      }
		      return instance;
	   }
	   
	@Override
	public synchronized CRUDCryptedFileInfo encryptFile(List<EncryptedFileDob> droppedFiles, ShareFolder sharefolder) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfoEncrypt> fileInfoList = new ArrayList<FileInfoEncrypt>();		
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : droppedFiles) {
			//If no new sharefolder -> File is already in a sharefolder
			if (sharefolder == null){
				ContainerDao dao = new ContainerDao();
				EncryptedContainer container = (EncryptedContainer) dao.getElementByID(fileDob.getContainerId(), null);
				sharefolder = new ShareFolder(container.getShareFolderId(), null);
			}								
			FileInfoEncrypt fileInfo;		
			//If it have a ContainerId -> it is already in a Container /UPDATE
			if(fileDob.getContainerId() != null){
				//Update
				hashSet.add(fileDob.getContainerId());
				fileInfo = new FileInfoEncrypt(fileDob.getId(), sharefolder.getID(), fileDob.getContainerId(), fileDob.getType());
			}	
			else{
				//new	
				fileInfo= new FileInfoEncrypt(fileDob.getId(), fileDob.getPath(), sharefolder.getID());
			}
			fileInfoList.add(fileInfo);
		}
		ShareFolderDao shareDao = new ShareFolderDao();
		// MasterShareFolder
		KeyRelation relation = getKeyRelation(hashSet);	
		//if the File is not in a SharedFolder/First Drop,/Put it in master-folder local
		if (relation == null) {
			ShareFolder shareFolder = (ShareFolder) shareDao
					.getElementByID(Messages.getIdSize(), null);
			relation = new KeyRelation();
			relation.addKeyOfShareRelation(shareFolder.getID(),
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

	@Override
	public synchronized CRUDCryptedFileInfo decryptFile(List<EncryptedFileDob> droppedFiles) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfoDecrypt> fileInfoList = new ArrayList<FileInfoDecrypt>();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : droppedFiles) {
			//Check If File Exist
			ContainerDao cDao= new ContainerDao();
			EncryptedContainer container =(EncryptedContainer) cDao.getElementByID(fileDob.getContainerId(), null);
			hashSet.add(fileDob.getContainerId());
			fileInfoList.add(new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), container.getShareFolderId(), fileDob
					.getContainerId()));
		}
		KeyRelation relation = getKeyRelation(hashSet);
		filehandling_result = ifile.decryptFiles(fileInfoList,relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), droppedFiles);
		return result;
	}

	@Override
	public synchronized CRUDCryptedFileInfo deleteFile(List<EncryptedFileDob> fileList) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : fileList) {
			//Check If File Exist
			ContainerDao cDao= new ContainerDao();
			EncryptedContainer container = (EncryptedContainer) cDao.getElementByID(fileDob.getContainerId(), null);
			hashSet.add(fileDob.getContainerId());
			fileInfoList.add(new FileInfo(fileDob.getId(), new ContainerInfo(
					fileDob.getContainerId(), container.getShareFolderId())));
		}
		KeyRelation relation = getKeyRelation(hashSet);
		filehandling_result = ifile.deleteFiles(fileInfoList, relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter(); 
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}

	@Override
	public synchronized CRUDCryptedFileInfo moveFiles(List<EncryptedFileDob> fileList,
			ShareFolder sharedFolder) {
		List<FileInfoMove> fileInfoList = new ArrayList<FileInfoMove>();
		FileHandlingSummary filehandling_result = null;
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : fileList) {
			hashSet.add(fileDob.getContainerId());
			ContainerDao cDao= new ContainerDao();
			EncryptedContainer container = (EncryptedContainer) cDao.getElementByID(fileDob.getContainerId(), null);
			fileInfoList.add(new FileInfoMove(fileDob.getId(), fileDob
					.getSize(), container.getShareFolderId(), fileDob.getContainerId(),
					sharedFolder.getID()));
		}
		KeyRelation relation = getKeyRelation(hashSet);
		relation.addKeyOfShareRelation(sharedFolder.getID(), sharedFolder.getKey());
		filehandling_result = ifile.moveFiles(fileInfoList,relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}



	@Override
	public synchronized boolean encryptFile(ShareFolder destinationShareFolder,  boolean local) {		
		FileInfoEncrypt fileInfo = null;
		//create ghost file
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		if(!local){
			 sxml = new StructureXmlDob(destinationShareFolder, false);		
		}
		else{
			sxml = new StructureXmlDob(destinationShareFolder, true);
		}
		fileDob = sxml.getEncryptedFileDob();
		// encrypt file !!!!!
		fileInfo = new FileInfoEncrypt(fileDob.getId(), destinationShareFolder.getID(), fileDob.getContainerId(), fileDob.getType());
		fileInfo = ifile.storeFileStructure(fileInfo, destinationShareFolder.getKey());
		if(fileInfo.getError() == FileError.NONE ) return true;
		return false;
	}

	@Override
	public synchronized boolean decryptFile(ShareFolder sourceShareFolder,boolean local) {
		FileInfoDecrypt fileInfo = null;
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		if(!local){
			sxml = new StructureXmlDob(sourceShareFolder ,false);
		}
		else{
			sxml = new StructureXmlDob(sourceShareFolder ,true);			
		}		
		fileDob = sxml.getEncryptedFileDob();
			fileInfo = new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), sourceShareFolder.getID(), fileDob
					.getContainerId());	
		fileInfo = ifile.loadFileStructure(fileInfo,  sourceShareFolder.getKey());
		if(fileInfo.getError() == FileError.NONE) return true;
		return false;
	}
	
////private/////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * HashSet with ContainerIDs to KeyRelation (sharedID / key)
	 * @param hashSet
	 * @return KeyRelation(sharedID/key)
	 */
	private synchronized KeyRelation getKeyRelation(HashSet<Integer> hashSet) {
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
			relation.addKeyOfShareRelation(shareFolder.getID(),
					shareFolder.getKey());
		}
		return relation;
	}

}
