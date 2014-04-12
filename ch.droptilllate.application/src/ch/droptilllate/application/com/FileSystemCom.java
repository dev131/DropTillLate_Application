package ch.droptilllate.application.com;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.crypto.KeyGenerator;

import ch.droptilllate.application.converter.FileInfoConverter;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.key.KeysGenerator;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.couldprovider.api.IFileSystemCom;
import ch.droptilllate.filesystem.error.FileError;
import ch.droptilllate.filesystem.info.*;
import ch.droptilllate.filesystem.api.FileHandlingSummary;
import ch.droptilllate.filesystem.api.FileSystemHandler;
import ch.droptilllate.filesystem.api.IFileSystem;
import ch.droptilllate.security.commons.KeyRelation;

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
	public synchronized CRUDCryptedFileInfo encryptFile(List<EncryptedFileDob> droppedFiles, Boolean exist) {
		FileHandlingSummary filehandling_result = null;
		List<FileInfoEncrypt> fileInfoList = new ArrayList<FileInfoEncrypt>();	
		KeyManager km = KeyManager.getInstance();
		ShareRelation shareRelation;
		FileInfoEncrypt fileInfo;
		KeyRelation relation = new KeyRelation();
		for (EncryptedFileDob fileDob : droppedFiles) {
			//If no new ShareRelation -> File is already in a ShareRelation
			if (exist){
				ContainerDao dao = new ContainerDao();
				EncryptedContainer container = (EncryptedContainer) dao.getElementByID(fileDob.getContainerId(), null);
				shareRelation = km.getShareRelation(container.getShareRelationId());
				fileInfo = new FileInfoEncrypt(fileDob.getId(), shareRelation.getID(), fileDob.getContainerId(), fileDob.getType());
				relation.addKeyOfShareRelation(shareRelation.getID(), shareRelation.getKey());
			}
			else{
				//file dropped in for the first time
				shareRelation = km.getShareRelation(Messages.getIdSize());
				fileInfo= new FileInfoEncrypt(fileDob.getId(), fileDob.getPath(), shareRelation.getID());
				relation.addKeyOfShareRelation(shareRelation.getID(), shareRelation.getKey());
			}
			fileInfoList.add(fileInfo);
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
		KeyManager km = KeyManager.getInstance();
		ShareRelation shareRelation;
		KeyRelation relation = new KeyRelation();
		for (EncryptedFileDob fileDob : droppedFiles) {
			//Check If File Exist
			ContainerDao cDao= new ContainerDao();
			EncryptedContainer container =(EncryptedContainer) cDao.getElementByID(fileDob.getContainerId(), null);
			shareRelation = km.getShareRelation(container.getShareRelationId());
			fileInfoList.add(new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), container.getShareRelationId(), fileDob
					.getContainerId()));
			relation.addKeyOfShareRelation(shareRelation.getID(), shareRelation.getKey());
			
		}
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
		KeyManager km = KeyManager.getInstance();
		ShareRelation shareRelation;
		KeyRelation relation = new KeyRelation();		
		for (EncryptedFileDob fileDob : fileList) {
			//Check If File Exist
			ContainerDao cDao= new ContainerDao();
			EncryptedContainer container = (EncryptedContainer) cDao.getElementByID(fileDob.getContainerId(), null);
			shareRelation = km.getShareRelation(container.getShareRelationId());
			fileInfoList.add(new FileInfo(fileDob.getId(), new ContainerInfo(
					fileDob.getContainerId(), container.getShareRelationId())));
			relation.addKeyOfShareRelation(shareRelation.getID(), shareRelation.getKey());
		}
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
			ShareRelation destShareRelation) {
		List<FileInfoMove> fileInfoList = new ArrayList<FileInfoMove>();
		FileHandlingSummary filehandling_result = null;
		KeyManager km = KeyManager.getInstance();
		ShareRelation oldShareRelation;
		KeyRelation relation = new KeyRelation();		
		for (EncryptedFileDob fileDob : fileList) {
			ContainerDao cDao= new ContainerDao();
			EncryptedContainer container = (EncryptedContainer) cDao.getElementByID(fileDob.getContainerId(), null);
			oldShareRelation = km.getShareRelation(container.getShareRelationId());
			fileInfoList.add(new FileInfoMove(fileDob.getId(), fileDob
					.getSize(), container.getShareRelationId(), fileDob.getContainerId(),
					destShareRelation.getID()));
			relation.addKeyOfShareRelation(oldShareRelation.getID(), oldShareRelation.getKey());
		}
		filehandling_result = ifile.moveFiles(fileInfoList,relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}



	@Override
	public synchronized boolean encryptFile(ShareRelation destShareRelation,  boolean local) {		
		FileInfoEncrypt fileInfo = null;
		//create ghost file
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		KeyManager keyManager = KeyManager.getInstance();
		if(!local){
			 sxml = new StructureXmlDob(false);	
		}
		else{
			sxml = new StructureXmlDob(true);		
			
		}
		ShareRelation shareRelation = keyManager.getShareRelation(destShareRelation.getID());
		fileDob = sxml.getEncryptedFileDob();
		// encrypt file !!!!!
		fileInfo = new FileInfoEncrypt(fileDob.getId(), destShareRelation.getID(), fileDob.getContainerId(), fileDob.getType());
		fileInfo = ifile.storeFileStructure(fileInfo, shareRelation.getKey());
		if(fileInfo.getError() == FileError.NONE ) return true;
		return false;
	}

	@Override
	public synchronized boolean decryptFile(ShareRelation srcShareRelation,boolean local) {
		FileInfoDecrypt fileInfo = null;
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		KeyManager keyManager = KeyManager.getInstance();
		if(!local){
			sxml = new StructureXmlDob(false);
		}
		else{
			sxml = new StructureXmlDob(true);
		}		
		srcShareRelation = keyManager.getShareRelation(srcShareRelation.getID());
		fileDob = sxml.getEncryptedFileDob();
			fileInfo = new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), srcShareRelation.getID(), fileDob
					.getContainerId());	
		
		fileInfo = ifile.loadFileStructure(fileInfo,  srcShareRelation.getKey());
		if(fileInfo.getError() == FileError.NONE) return true;
		return false;
	}
	
}
