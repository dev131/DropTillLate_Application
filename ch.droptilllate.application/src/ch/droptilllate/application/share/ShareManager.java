package ch.droptilllate.application.share;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.cloudprovider.api.IFileSystemCom;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.XMLDatabase;


public class ShareManager {

	private int ERROR = 0;
	private int CREATE = 1;
	private int USEEXIST = 2;
	private int STATUS = 0;
	private int ShareExist = 3;
	private IDatabase database = null;
	private List<String> emailList;
	private List<TillLateContainer> contList;
	private List<EncryptedFileDob> dobfileList;
	private List<ShareMember> shareMemberList;
	
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
			String password, List<String> emailList,IDatabase database) {
		this.emailList = emailList;
		this.database = database;
		checkShareMembers(emailList);
		STATUS = 0;
		if(STATUS != ShareExist){
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
	}

	/**
	 * Check if All ShareMember share already a sharefolder
	 * @param emailList2
	 */
	private void checkShareMembers(List<String> emailList2) {
		List<ShareMember> list = new ArrayList<ShareMember>();
		for(String mail : emailList2){
			list.addAll( (List<ShareMember>) database.getElement(ShareMember.class, XMLConstruct.AttMail, mail));
		}
		if(!list.isEmpty()){
			for(ShareMember sharemember: list){
				List<ShareMember> tmp = (List<ShareMember>) database.getElement(ShareMember.class, XMLConstruct.AttShareRelationID, sharemember.getShareRelationId().toString());
				if(tmp.size() == list.size()){
					//Sharefolder contains just the sharemembers, share file into existing sharefolder
					STATUS = ShareExist;
				}
			}
		}
	
		
	}
	
	public ShareRelation useExistingSharedRelation(List<EncryptedFileDob> fileList,
			String password) {
		HashSet<Integer> hashFile = new HashSet<Integer>();
		for(EncryptedFileDob fileDob : fileList){
			hashFile.add(fileDob.getContainerId());
		}
		TillLateContainer container = null;
		for(Integer id : hashFile){
		 container = (TillLateContainer) database.getElement(TillLateContainer.class, XMLConstruct.AttId, id.toString()).get(0);			
		}		
		// Get existing ShareRelation
		KeyManager km = KeyManager.getInstance();
		return km.getShareRelation(container.getShareRelationId(), false);
	}

	/**
	 * Move the Sharefolder and insert in DB with containers
	 * @param fileList
	 * @param shareRelation
	 * @return ShareRelation
	 */
	public ShareRelation createNewSharedRelation(
			ArrayList<EncryptedFileDob> fileListShare,ShareRelation shareRelation) {
		// Move Files
		IFileSystemCom iFile = FileSystemCom.getInstance();	
		CRUDCryptedFileInfo result = iFile.moveFiles(fileListShare, shareRelation);
		// Handle Error
		for (EncryptedFileDob fileDob : result.getEncryptedFileListError()) {
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> sharing not worked");
		}
		// Update Database
		database.updateElement(result.getEncryptedFileListSuccess());
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()) {
			hashSet.add(fileDob.getContainerId());
		}
		//Update Containers
		List<TillLateContainer> containerlist = new ArrayList<TillLateContainer>();
		for(Integer i : hashSet){
			//TODO Delete not used container in db
			TillLateContainer container = new TillLateContainer(i, shareRelation.getID());
			containerlist.add(container);
		}
		database.createElement(containerlist);
		return shareRelation;
	}
	
	public void prepareUpdateDatabase(ShareRelation shareRelation,IDatabase database, List<EncryptedFileDob> list){
		this.dobfileList = list;
		contList = (List<TillLateContainer>) database.getElement(TillLateContainer.class, XMLConstruct.AttShareRelationID, shareRelation.getID().toString());
			List<EncryptedFileDob> dobfileList = new ArrayList<EncryptedFileDob>();
		for(TillLateContainer container : contList){
			List<EncryptedFileDob> dobfilelistTemp = (List<EncryptedFileDob>) database.getElement(EncryptedFileDob.class, XMLConstruct.AttContainerId, container.getId().toString());
			dobfileList.addAll(dobfilelistTemp);
		}	
		shareMemberList = (List<ShareMember>) database.getElement(ShareMember.class, XMLConstruct.AttShareRelationID, shareRelation.getID().toString());	
	}
	public void createUpdateFiles(IDatabase database) {
		database.createElement(contList);
		database.createElement(dobfileList);
		database.createElement(shareMemberList);
	}

	private boolean checkIfMoreFilesAvailable(Integer shareRelationID,
			List<EncryptedFileDob> fileList) {

		List<TillLateContainer> containerlist = new ArrayList<TillLateContainer>();
		containerlist = (List<TillLateContainer>) database.getElement(TillLateContainer.class, XMLConstruct.AttShareRelationID, shareRelationID.toString());
		List<EncryptedFileDob> dblist = new ArrayList<EncryptedFileDob>();
		for (TillLateContainer container : containerlist) {
			dblist.addAll((List<EncryptedFileDob>) database.getElement(EncryptedFileDob.class, XMLConstruct.AttContainerId, container.getId().toString()));
		}
		if (fileList.size() == dblist.size())
			return true;

		return false;
	}

	private HashSet<Integer> getShareRelationList(List<EncryptedFileDob> fileList) {

		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (EncryptedFileDob fileDob : fileList) {
			List<TillLateContainer> container = (List<TillLateContainer>) database.getElement(TillLateContainer.class, XMLConstruct.AttId, fileDob.getContainerId().toString());
			hashSet.add(container.get(0).getShareRelationId());
		}
		return hashSet;
	}

	private HashMap<Integer, ArrayList<EncryptedFileDob>> getHashMap(
			List<EncryptedFileDob> fileList, HashSet<Integer> hashSet) {
		
		HashMap<Integer, ArrayList<EncryptedFileDob>> hashmap = new HashMap<Integer, ArrayList<EncryptedFileDob>>();
		
		for (Integer shareRelationID : hashSet) {
			ArrayList<EncryptedFileDob> arraylist = new ArrayList<EncryptedFileDob>();
			for (EncryptedFileDob fileDob : fileList) {
				TillLateContainer container = (TillLateContainer) database.getElement(TillLateContainer.class, XMLConstruct.AttId, fileDob.getContainerId().toString()).get(0);
				if ((container.getShareRelationId() == shareRelationID)) {
					arraylist.add(fileDob);
				}
			}
			hashmap.put(shareRelationID, arraylist);
		}
		return hashmap;
	}
	
	public void insertShareMembers(ShareRelation shareRelation, ArrayList<String> mailList) {
		ShareMember sharerelation = new ShareMember(shareRelation.getID(), Messages.OwnerMail);
		database.createElement(sharerelation);
		for(String mail : mailList){
			sharerelation = new ShareMember(shareRelation.getID(), mail);
			database.createElement(sharerelation);
		}	
	}


	public int getSTATUS() {
		return STATUS;
	}

	
	
	
	
}
