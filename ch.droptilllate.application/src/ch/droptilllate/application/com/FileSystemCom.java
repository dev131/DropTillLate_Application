package ch.droptilllate.application.com;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.crypto.KeyGenerator;

import ch.droptilllate.application.converter.FileInfoConverter;
import ch.droptilllate.application.converter.FileIntegryConverter;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.key.KeysGenerator;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.cloudprovider.api.IFileSystemCom;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.XMLDatabase;
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
        ifile = new FileSystemHandler(Configuration.getPropertieDropBoxPath("",false), Configuration.getPropertieTempPath("",false));
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
				TillLateContainer container = getTillLateContainerByID(fileDob.getContainerId());		
				shareRelation = km.getShareRelation(container.getShareRelationId(),true);
				fileInfo = new FileInfoEncrypt(fileDob.getId(), shareRelation.getID(), fileDob.getContainerId(), fileDob.getType());
				relation.addKeyOfShareRelation(shareRelation.getID(), shareRelation.getKey());
			}
			else{
				//file dropped in for the first time
				shareRelation = km.getShareRelation(Messages.getIdSize(),true);
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
			TillLateContainer container = getTillLateContainerByID(fileDob.getContainerId());
			shareRelation = km.getShareRelation(container.getShareRelationId(),true);
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
			TillLateContainer container = getTillLateContainerByID(fileDob.getContainerId());
			shareRelation = km.getShareRelation(container.getShareRelationId(),true);
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
		relation.addKeyOfShareRelation(destShareRelation.getID(), km.getShareRelation(destShareRelation.getID(),true).getKey());
		for (EncryptedFileDob fileDob : fileList) {
			TillLateContainer container = getTillLateContainerByID(fileDob.getContainerId());
			oldShareRelation = km.getShareRelation(container.getShareRelationId(),true);
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
	public synchronized boolean encryptFile(ShareRelation destShareRelation,  DBSituation situation) {		
		FileInfoEncrypt fileInfo = null;
		//create ghost file
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		KeyManager keyManager = KeyManager.getInstance();
		
		sxml = new StructureXmlDob(situation);	

		ShareRelation shareRelation = keyManager.getShareRelation(destShareRelation.getID(),true);
		fileDob = sxml.getEncryptedFileDob();
		// encrypt file !!!!!
		fileInfo = new FileInfoEncrypt(fileDob.getId(), destShareRelation.getID(), fileDob.getContainerId(), fileDob.getType());
		fileInfo = ifile.storeFileStructure(fileInfo, shareRelation.getKey());
		if(fileInfo.getError() == FileError.NONE ) return true;
		return false;
	}

	@Override
	public synchronized boolean decryptFile(ShareRelation srcShareRelation,  DBSituation situation) {
		FileInfoDecrypt fileInfo = null;
		EncryptedFileDob fileDob;
		StructureXmlDob sxml;
		KeyManager keyManager = KeyManager.getInstance();
		
		sxml = new StructureXmlDob(situation);
				
		srcShareRelation = keyManager.getShareRelation(srcShareRelation.getID(),true);
		fileDob = sxml.getEncryptedFileDob();
			fileInfo = new FileInfoDecrypt(fileDob.getId(), fileDob
					.getType(), srcShareRelation.getID(), fileDob
					.getContainerId());	
		
		fileInfo = ifile.loadFileStructure(fileInfo,  srcShareRelation.getKey());
		if(fileInfo.getError() == FileError.NONE) return true;
		return false;
	}
	
	@Override
	public HashMap<Integer, List<EncryptedFileDob>>  fileIntegryCheck(){
		KeyRelation keyRelation = KeyManager.getInstance().getKeyrelationWithHash();
		return new FileIntegryConverter().convert(ifile.getFilesPerRelation(keyRelation));
	}
	
	@Override
	public synchronized CRUDCryptedFileInfo exportFiles(List<EncryptedFileDob> fileList){
		FileHandlingSummary filehandling_result = null;
		List<FileInfoDecrypt> fileInfoList = new ArrayList<FileInfoDecrypt>();
		KeyManager km = KeyManager.getInstance();
		ShareRelation shareRelation;
		KeyRelation relation = new KeyRelation();
		for (EncryptedFileDob fileDob : fileList) {
			//Check If File Exist
			TillLateContainer container = getTillLateContainerByID(fileDob.getContainerId());
			shareRelation = km.getShareRelation(container.getShareRelationId(),true);
			fileInfoList.add(new FileInfoDecrypt(
					fileDob.getId(), 
					container.getShareRelationId(), 
					fileDob.getContainerId(), 
					fileDob.getPath()));
			relation.addKeyOfShareRelation(shareRelation.getID(), shareRelation.getKey());
			
		}
		filehandling_result = ifile.decryptFiles(fileInfoList,relation);
		// Convert to FileCRUDResults
		FileInfoConverter converter = new FileInfoConverter();
		CRUDCryptedFileInfo result = converter.convertFileInfoList(
				filehandling_result.getFileInfoSuccessList(),
				filehandling_result.getFileInfoErrorList(), fileList);
		return result;
	}
	
	private TillLateContainer getTillLateContainerByID(Integer id){
		IDatabase database = new XMLDatabase();
		database.openTransaction("", DBSituation.LOCAL_DATABASE);	
		TillLateContainer container = (TillLateContainer) database.getElement(TillLateContainer.class, XMLConstruct.AttId, id.toString()).get(0);
		database.closeTransaction("", Messages.getIdSize(),DBSituation.LOCAL_DATABASE);
		return container;
	}
}
