package ch.droptilllate.application.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dao.ShareRelationDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.Status;


public class ShareManager {

	private List<String> emailList;
	/**
	 * Create new ShareFolder and Move fileList into it
	 * 
	 * @param fileList
	 * @param password
	 * @param email 
	 */
	public void newShareRelation(List<EncryptedFileDob> fileList,
			String password, List<String> emailList) {
		this.emailList = emailList;
		// create HashSet with all ShareFolderId
		HashSet<Integer> hashSet_shareFolderList = getShareFolderList(fileList);
		// FILL Hashmap with key = sharedfolderID and filelist as value
		HashMap<Integer, ArrayList<EncryptedFileDob>> hashmap = getHashMap(
				fileList, hashSet_shareFolderList);
		// Check if entries available
		// TODO cancel sharing
		if (hashmap.isEmpty())
			return;

		// Check if All Files from one ShareFolder
		if (hashmap.size() == 1) {
			// Check if Files from same SharedFolder but not all
			if (!checkIfMoreFilesAvailable(hashSet_shareFolderList.iterator()
					.next(), fileList)) {
				// NotAllFiles from this folder, there are some left
				if (hashmap.containsKey(Integer.parseInt(Messages
						.getShareFolder0name()))) {
					createNewSharedFolder(fileList, password);
					alertMembers(hashSet_shareFolderList);
				} else {
					createNewSharedFolder(fileList, password);
					alertMembers(hashSet_shareFolderList);
				}
			} else {
				if (hashmap.containsKey(Integer.parseInt(Messages
						.getShareFolder0name()))) {
					// All Files from SharedFolder 0
					createNewSharedFolder(fileList, password);
					alertMembers(hashSet_shareFolderList);
				} else {
					// All Files from SharedFolder x not 0
					useExistingSharedFolder(fileList, password);
					alertMembers(hashSet_shareFolderList);
				}
			}
		} else {
			// From more then one ShareFolder
			createNewSharedFolder(fileList, password);
			alertMembers(hashSet_shareFolderList);
		}
	}

	public  List<EncryptedFileDob> getUpdateFiles(String key){
		UpdateXMLImporter importer = new UpdateXMLImporter(key);
		return importer.getFileUpdateXML();	
	}
	
	public List<EncryptedContainer> getUpdateContainers(String key){
		UpdateXMLImporter importer = new UpdateXMLImporter(key);
		return importer.getContainerUpdateXML();
	}
	
	public List<ShareRelation> getUpdateShareRelation(String key){
		UpdateXMLImporter importer = new UpdateXMLImporter(key);
		return importer.getShareRelationUpdateXML();
	}
	    
	private void insertShareRelation(ShareFolder shareFolder) {
		ShareRelationDao dao = new ShareRelationDao();
		ShareRelation sharerelation = new ShareRelation(shareFolder.getID(), Messages.getOwnerMail());
		dao.newElement(sharerelation, null);
		for(String mail : emailList){
			sharerelation = new ShareRelation(shareFolder.getID(), mail);
			dao.newElement(sharerelation, null);
		}	
	}

	private void useExistingSharedFolder(List<EncryptedFileDob> fileList,
			String password) {
		
		
	}

	private void createNewSharedFolder(
			List<EncryptedFileDob> fileList, String password) {
		KeyManager km = new KeyManager();
		String key = null;		
		// Create and insert newShareFolder in DB and create Id
		ShareFolderDao shareDao = new ShareFolderDao();
		ShareFolder sharedFolder = new ShareFolder(null,
				Messages.getPathDropBox(), null);
		sharedFolder = (ShareFolder) shareDao.newElement(sharedFolder, null);
		key = km.generatePassword(password, sharedFolder.getPath());
		sharedFolder.setKey(key);
		shareDao.updateElement(sharedFolder, null);
		// Move Files
		IFileSystemCom iFile = new FileSystemCom();
		CRUDCryptedFileInfo result = iFile.moveFiles(fileList, sharedFolder);
		// Handle Error
		for (EncryptedFileDob fileDob : result.getEncryptedFileListError()) {
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> sharing not worked");
		}
		// Update Database
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()) {
			EncryptedFileDao fileDB = new EncryptedFileDao();
			fileDB.updateElement(fileDob, null);
			hashSet.add(fileDob.getContainerId());
		}
		//Update Containers
		ContainerDao containerDB = new ContainerDao();
		for(Integer i : hashSet){
			//TODO Delete not used container in db
			EncryptedContainer container = new EncryptedContainer(i, sharedFolder.getID());
			containerDB.newElement(container, null);
		}
		//Insert new ShareRelations
		insertShareRelation(sharedFolder);
		
		createUpdateFiles(sharedFolder);
	}
	
	private void createUpdateFiles(ShareFolder sharedFolder) {
	ContainerDao contDao = new ContainerDao();
	List<EncryptedContainer> contList = (List<EncryptedContainer>) contDao.getContainerBySharedFolderId(sharedFolder.getID(), null);
	EncryptedFileDao fileDao = new EncryptedFileDao();
	List<EncryptedFileDob> dobfileList = new ArrayList<EncryptedFileDob>();
	for(EncryptedContainer container : contList){
		List<EncryptedFileDob> dobfilelistTemp = fileDao.getFileByContainerId(container.getId(),null);
		for(EncryptedFileDob tempDob : dobfilelistTemp){
			dobfileList.add(tempDob);
		}
	}	
	ShareRelationDao shareDao = new ShareRelationDao();
	List<ShareRelation> shareRelationList = (List<ShareRelation>) shareDao.getElementByID(sharedFolder.getID(), null);
	UpdateXMLGenerator gen = new UpdateXMLGenerator(sharedFolder.getKey());
	gen.createContainerUpdateXML(contList);
	gen.createFileUpdateXML(dobfileList);
	gen.creatShareRelationUpdateXML(shareRelationList);
	//Encrypt XMLs
	IFileSystemCom com = new FileSystemCom();
	//TODO use right encryptFile with right sharedFolder
	if(com.encryptFile(sharedFolder, false)){
		//TODO if true -> successfull
	}
	
	
	}

	private void alertMembers(HashSet<Integer> hashSet_shareFolderList){
		List<ShareRelation> shareRelationList = new ArrayList<ShareRelation>();	
		for(Integer i : hashSet_shareFolderList){
			ShareRelationDao dao = new ShareRelationDao();
			//Return list of all shareRelation
			List<ShareRelation> shareRelationListTemp = (List<ShareRelation>) dao.getElementByID(i, null);
			for(ShareRelation relation : shareRelationListTemp){
				shareRelationList.add(relation);
			}
		}
		for(ShareRelation shareRelation : shareRelationList){
			Status status = Status.getInstance();
			status.setMessage("SharedWith:" + shareRelation.getMail());
		}		
	}

	private boolean checkIfMoreFilesAvailable(Integer shareFolderId,
			List<EncryptedFileDob> fileList) {
		ContainerDao dao = new ContainerDao();
		EncryptedFileDao daof = new EncryptedFileDao();
		List<EncryptedContainer> containerlist = new ArrayList<EncryptedContainer>();
		containerlist = (List<EncryptedContainer>) ((ContainerDao) dao)
				.getContainerBySharedFolderId(shareFolderId, null);
		List<Integer> tempList1 = new ArrayList<Integer>();
		List<Integer> tempList2 = new ArrayList<Integer>();
		for (EncryptedContainer container : containerlist) {
			tempList1 = (List<Integer>) ((EncryptedFileDao) daof).getFileIdsByContainerId(container
					.getId(), null);
		}
		for (EncryptedFileDob dob : fileList) {
			tempList2.add(dob.getId());
		}
		if (tempList1.containsAll(tempList2))
			return true;

		return false;
	}

	private HashSet<Integer> getShareFolderList(List<EncryptedFileDob> fileList) {
		ContainerDao dao = new ContainerDao();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : fileList) {
			EncryptedContainer container = (EncryptedContainer) dao
					.getElementByID(fileDob.getContainerId(), null);
			hashSet.add(container.getShareFolderId());
		}
		return hashSet;
	}

	private HashMap<Integer, ArrayList<EncryptedFileDob>> getHashMap(
			List<EncryptedFileDob> fileList, HashSet<Integer> hashSet) {
		HashMap<Integer, ArrayList<EncryptedFileDob>> hashmap = new HashMap<Integer, ArrayList<EncryptedFileDob>>();
		ContainerDao dao = new ContainerDao();
		for (Integer sharefolderId : hashSet) {
			ArrayList<EncryptedFileDob> arraylist = new ArrayList<EncryptedFileDob>();
			for (EncryptedFileDob fileDob : fileList) {
				if (((EncryptedContainer) dao.getElementByID(fileDob
						.getContainerId(), null)).getShareFolderId() == sharefolderId) {
					arraylist.add(fileDob);
				}
			}
			hashmap.put(sharefolderId, arraylist);
		}
		return hashmap;
	}

}
