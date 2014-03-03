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
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.application.views.XMLConstruct;
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
			AbstractXmlDatabase containerDao = new ContainerDao();
			EncryptedContainer container = (EncryptedContainer) containerDao
					.getElementByID(ContainerId, null);
			AbstractXmlDatabase shareFolderDao = new ShareFolderDao();
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
	public CRUDCryptedFileInfo encryptFile(ShareFolder destinationShareFolder,  boolean local) {		
		FileHandlingSummary filehandling_result = null;
		List<FileInfoEncrypt> fileInfoList = new ArrayList<FileInfoEncrypt>();		
		//create ghost file
		EncryptedFileDob fileDob;
		if(!local){
			fileDob = buildXMLDob(Messages.getPathLocalTemp() + XMLConstruct.getPathShareXML(), 
					Integer.parseInt(XMLConstruct.getIdShareXMLContainer()), 
					Integer.parseInt(XMLConstruct.getIdShareXMLFiles()));	
		}
		else{
			fileDob = buildXMLDob(Messages.getPathLocalTemp() + XMLConstruct.getPathLocalXML(), 
					Integer.parseInt(XMLConstruct.getIdLocalXMLContainer()), 
					Integer.parseInt(XMLConstruct.getIdLocalXMLFiles()));
		}
		//Check if it exist
		checkFileExisting(fileDob);
		//TODO encrypt file !!!!!
		//fileInfo = new FileInfoEncrypt(fileDob.getId(), fileDob.getTempPlainPath(), destination path, fileDob.getContainerId()); 
		//fileInfoList.add(fileInfo);
		IFileSystem ifile = new FileSystemHandler();
		//Create KeyRelation
		KeyRelation relation = new KeyRelation();
		relation.addKeyOfShareRelation(destinationShareFolder.getPath() + destinationShareFolder.getID(), destinationShareFolder.getKey());
		filehandling_result =ifile.encryptFiles(fileInfoList, relation);
	// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = new CRUDCryptedFileInfo();
		List<EncryptedFileDob> fileList = new ArrayList<EncryptedFileDob>();
		fileList.add(fileDob);
		result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}
	
	/**
	 * Create Dob for share/localXML
	 * @param filePath
	 * @param containerId
	 * @param fileId
	 * @return EncryptedFileDob
	 */
	private EncryptedFileDob buildXMLDob(String filePath, Integer containerId, Integer fileId){
		File file = new File(filePath);
		EncryptedFileDob fileDob = new EncryptedFileDob(fileId, 
				file.getName(), 
				new Date(System.currentTimeMillis()), 
				file.getPath(), 
				null, 
				FilenameUtils.getExtension(file.getAbsolutePath()), 
				file.length(), 
				containerId);					
		return fileDob;	
	}

	@Override
	public CRUDCryptedFileInfo decryptFile(ShareFolder sourceShareFolder,boolean local) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfoDecrypt> fileInfoList = new ArrayList<FileInfoDecrypt>();
		EncryptedFileDob fileDob;
		if(!local){
			fileDob = buildXMLDob(sourceShareFolder.getPath(), 
					Integer.parseInt(XMLConstruct.getIdShareXMLContainer()), 
					Integer.parseInt(XMLConstruct.getIdShareXMLFiles()));	
		}
		else{
			fileDob = buildXMLDob(sourceShareFolder.getPath(), 
					Integer.parseInt(XMLConstruct.getIdLocalXMLContainer()), 
					Integer.parseInt(XMLConstruct.getIdLocalXMLFiles()));
		}
		
			//Check If File Exist
			checkFileExisting(fileDob);
			fileInfoList.add(new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), Messages.getPathLocalTemp(), fileDob.getShareRelationPath(), fileDob
					.getContainerId()));
		
		KeyRelation relation = new KeyRelation();
		relation.addKeyOfShareRelation(sourceShareFolder.getPath(), sourceShareFolder.getKey());
		IFileSystem ifile = new FileSystemHandler();
		filehandling_result = ifile.decryptFiles(fileInfoList,relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		List<EncryptedFileDob> fileList = new ArrayList<EncryptedFileDob>();
		fileList.add(fileDob);
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}

}
