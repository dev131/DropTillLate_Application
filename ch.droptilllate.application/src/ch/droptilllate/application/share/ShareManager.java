package ch.droptilllate.application.share;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.ShareMembersDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.application.xml.UpdateXMLGenerator;
import ch.droptilllate.application.xml.UpdateXMLImporter;
import ch.droptilllate.cloudprovider.api.IFileSystemCom;


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
	
		// create HashSet with all shareRelationID
		HashSet<Integer> hashSet_shareRelationList = getShareRelationList(fileList);
		// FILL Hashmap with key = sharedfolderID and filelist as value
		HashMap<Integer, ArrayList<EncryptedFileDob>> hashmap = getHashMap(
				fileList, hashSet_shareRelationList);
		// Check if entries available
		// TODO cancel sharing
		if (hashmap.isEmpty())
			STATUS = ERROR;

		// Check if All Files from one ShareRelation
		if (hashmap.size() == 1) {
			// Check if Files from same ShareRelation but not all
			if (!checkIfMoreFilesAvailable(hashSet_shareRelationList.iterator()
					.next(), fileList)) {	
					STATUS = CREATE;
			} else {
				if (hashmap.containsKey(Messages.getIdSize())) {
					// All Files from ShareRelation 0
					STATUS = CREATE;
				} else {
					// All Files from ShareRelation x not 0
					STATUS = USEEXIST;
				}
			}
		} else {
			// From more then one ShareRelation
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
	
	public List<ShareMember> getUpdateShareRelation(String key){
		UpdateXMLImporter importer = new UpdateXMLImporter(key);
		return importer.getShareRelationUpdateXML();
	}
	    
	public void insertShareMembers(ShareRelation shareRelation, ArrayList<String> mailList) {
		ShareMembersDao dao = new ShareMembersDao();
		ShareMember sharerelation = new ShareMember(shareRelation.getID(), Messages.OwnerMail);
		dao.newElement(sharerelation, null);
		for(String mail : mailList){
			sharerelation = new ShareMember(shareRelation.getID(), mail);
			dao.newElement(sharerelation, null);
		}	
	}

	public ShareRelation useExistingSharedRelation(List<EncryptedFileDob> fileList,
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
		// Get existing ShareRelation
		KeyManager km = KeyManager.getInstance();
		return km.getShareRelation(container.getShareRelationId());
	}

	public ShareRelation createNewSharedRelation(
			ArrayList<EncryptedFileDob> fileList,ShareRelation shareRelation) {
		// Move Files
		IFileSystemCom iFile = FileSystemCom.getInstance();	
		CRUDCryptedFileInfo result = iFile.moveFiles(fileList, shareRelation);
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
			EncryptedContainer container = new EncryptedContainer(i, shareRelation.getID());
			containerDB.newElement(container, null);
		}
		return shareRelation;
	}
	
	public void createUpdateFiles(ShareRelation shareRelation) {
	ContainerDao contDao = new ContainerDao();
	List<EncryptedContainer> contList = (List<EncryptedContainer>) contDao.getContainerByShareRelationId(shareRelation.getID(), null);
	EncryptedFileDao fileDao = new EncryptedFileDao();
	List<EncryptedFileDob> dobfileList = new ArrayList<EncryptedFileDob>();
	for(EncryptedContainer container : contList){
		List<EncryptedFileDob> dobfilelistTemp = fileDao.getFileByContainerId(container.getId(),null);
		for(EncryptedFileDob tempDob : dobfilelistTemp){
			dobfileList.add(tempDob);
		}
	}	
	ShareMembersDao shareDao = new ShareMembersDao();
	List<ShareMember> shareMemberList = (List<ShareMember>) shareDao.getElementByID(shareRelation.getID(), null);
	UpdateXMLGenerator gen = new UpdateXMLGenerator(shareRelation.getKey());
	gen.createContainerUpdateXML(contList);
	gen.createFileUpdateXML(dobfileList);
	gen.creatShareMembersUpdateXML(shareMemberList);
	//Encrypt XMLs
	IFileSystemCom com = FileSystemCom.getInstance();	
	//TODO use right encryptFile with right sharedFolder
	if(com.encryptFile(shareRelation, false)){
		//TODO if true -> successfull
	}
	
	
	}

	private boolean checkIfMoreFilesAvailable(Integer shareRelationID,
			List<EncryptedFileDob> fileList) {
		ContainerDao dao = new ContainerDao();
		EncryptedFileDao daof = new EncryptedFileDao();
		List<EncryptedContainer> containerlist = new ArrayList<EncryptedContainer>();
		containerlist = (List<EncryptedContainer>) (dao).getContainerByShareRelationId(shareRelationID, null);
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

	private HashSet<Integer> getShareRelationList(List<EncryptedFileDob> fileList) {
		ContainerDao dao = new ContainerDao();
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : fileList) {
			EncryptedContainer container = (EncryptedContainer) dao
					.getElementByID(fileDob.getContainerId(), null);
			hashSet.add(container.getShareRelationId());
		}
		return hashSet;
	}

	private HashMap<Integer, ArrayList<EncryptedFileDob>> getHashMap(
			List<EncryptedFileDob> fileList, HashSet<Integer> hashSet) {
		HashMap<Integer, ArrayList<EncryptedFileDob>> hashmap = new HashMap<Integer, ArrayList<EncryptedFileDob>>();
		ContainerDao dao = new ContainerDao();
		for (Integer shareRelationID : hashSet) {
			ArrayList<EncryptedFileDob> arraylist = new ArrayList<EncryptedFileDob>();
			for (EncryptedFileDob fileDob : fileList) {
				if (((EncryptedContainer) dao.getElementByID(fileDob
						.getContainerId(), null)).getShareRelationId() == shareRelationID) {
					arraylist.add(fileDob);
				}
			}
			hashmap.put(shareRelationID, arraylist);
		}
		return hashmap;
	}
	


	public int getSTATUS() {
		return STATUS;
	}

	
	
}
