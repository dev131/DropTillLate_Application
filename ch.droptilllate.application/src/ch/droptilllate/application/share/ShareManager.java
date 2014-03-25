package ch.droptilllate.application.share;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.droptilllate.application.com.CloudDropboxCom;
import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dao.ShareRelationDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.application.xml.UpdateXMLGenerator;
import ch.droptilllate.application.xml.UpdateXMLImporter;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.couldprovider.api.ICloudProviderCom;
import ch.droptilllate.couldprovider.api.IFileSystemCom;


public class ShareManager {

	private int ERROR = 0;
	private int CREATE = 1;
	private int USEEXIST = 2;
	private int STATUS = 0;
	
	private List<String> emailList;
	/**
	* Limited functionality
	*/
	public ShareManager(){
		
	}
	/**
	 * ShareManager
	 * @param fileList
	 * @param password
	 * @param emailList
	 */
	public ShareManager(List<EncryptedFileDob> fileList,
			String password, List<String> emailList) {
		this.emailList = emailList;
		
		ShareFolder shareFolder = new ShareFolder(null, null);
		// create HashSet with all ShareFolderId
		HashSet<Integer> hashSet_shareFolderList = getShareFolderList(fileList);
		// FILL Hashmap with key = sharedfolderID and filelist as value
		HashMap<Integer, ArrayList<EncryptedFileDob>> hashmap = getHashMap(
				fileList, hashSet_shareFolderList);
		// Check if entries available
		// TODO cancel sharing
		if (hashmap.isEmpty())
			STATUS = ERROR;

		// Check if All Files from one ShareFolder
		if (hashmap.size() == 1) {
			// Check if Files from same SharedFolder but not all
			if (!checkIfMoreFilesAvailable(hashSet_shareFolderList.iterator()
					.next(), fileList)) {	
					STATUS = CREATE;
			} else {
				if (hashmap.containsKey(Messages.getIdSize())) {
					// All Files from SharedFolder 0
					STATUS = CREATE;
				} else {
					// All Files from SharedFolder x not 0
					STATUS = USEEXIST;
				}
			}
		} else {
			// From more then one ShareFolder
			STATUS = CREATE;
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
	    
	public void insertShareRelation(ShareFolder shareFolder, ArrayList<String> mailList) {
		ShareRelationDao dao = new ShareRelationDao();
		ShareRelation sharerelation = new ShareRelation(shareFolder.getID(), Messages.OwnerMail);
		dao.newElement(sharerelation, null);
		for(String mail : mailList){
			sharerelation = new ShareRelation(shareFolder.getID(), mail);
			dao.newElement(sharerelation, null);
		}	
	}

	public ShareFolder useExistingSharedFolder(List<EncryptedFileDob> fileList,
			String password) {
		HashSet<Integer> hashFile = new HashSet<Integer>();
		for(EncryptedFileDob fileDob : fileList){
			hashFile.add(fileDob.getContainerId());
		}
		ContainerDao dao = new ContainerDao();
		EncryptedContainer container = null;
		for(Integer id : hashFile){
		 container = (EncryptedContainer) dao.getElementByID(id, null);			
		}		
		// Get existing sharefolder
		ShareFolderDao sDao = new ShareFolderDao();		
		return (ShareFolder) sDao.getElementByID(container.getShareFolderId(), null);
	}

	public ShareFolder createNewSharedFolder(
			ArrayList<EncryptedFileDob> fileList, String password, ShareFolder shareFolder) {
		// Move Files
		IFileSystemCom iFile = FileSystemCom.getInstance();	
		CRUDCryptedFileInfo result = iFile.moveFiles(fileList, shareFolder);
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
			EncryptedContainer container = new EncryptedContainer(i, shareFolder.getID());
			containerDB.newElement(container, null);
		}
		return shareFolder;
	}
	
	public void createUpdateFiles(ShareFolder sharedFolder) {
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
	IFileSystemCom com = FileSystemCom.getInstance();	
	//TODO use right encryptFile with right sharedFolder
	if(com.encryptFile(sharedFolder, false)){
		//TODO if true -> successfull
	}
	
	
	}

	public void alertMembers(HashSet<Integer> hashSet_shareFolderList){
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
		containerlist = (List<EncryptedContainer>) (dao)
				.getContainerBySharedFolderId(shareFolderId, null);
		List<Integer> tempList1 = new ArrayList<Integer>();
		List<Integer> tempList2 = new ArrayList<Integer>();
		for (EncryptedContainer container : containerlist) {
			tempList1 = (List<Integer>) (daof).getFileIdsByContainerId(container
					.getId(), null);
		}
		for (EncryptedFileDob dob : fileList) {
			tempList2.add(dob.getId());
		}
		if (tempList1.size() == tempList2.size())
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
	
	public CloudError shareFileToCloud(ShareFolder shareFolder, ArrayList<String> mailList){
		ICloudProviderCom com = new CloudDropboxCom();
		//TODO CHECK IF FOLDER AVAILABLE
//		while(com.checkIfFolderExists(shareFolder.getID()) != CloudError.NONE && i<10){	
//			//TEST IF FOLDER EXIST
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			i++;
//		}
		//WAIT a little
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CloudError state = com.shareFolder(shareFolder.getID(), mailList);
		return state;
	}

	public int getSTATUS() {
		return STATUS;
	}

	
	
}
