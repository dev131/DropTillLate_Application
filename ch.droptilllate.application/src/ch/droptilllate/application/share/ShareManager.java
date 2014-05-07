package ch.droptilllate.application.share;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.droptilllate.application.com.CloudDropboxCom;
import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.info.SuccessMessage;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.cloudprovider.api.ICloudProviderCom;
import ch.droptilllate.cloudprovider.api.IFileSystemCom;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.XMLDatabase;


public class ShareManager {

	private ShareStatus shareStatus;
	private IDatabase database = null;
	private DatabaseStatus dbstatus;
	private List<String> emailList;
	private String password;
	private ShareRelation shareRelation;
	private List<TillLateContainer> updatecontList;
	private List<EncryptedFileDob> sharefileList;
	private List<EncryptedFileDob> updatefileList;
	private List<ShareMember> updateshareMemberList;
	
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
	public ShareManager(List<EncryptedFileDob> sharefileList,
			String password, List<String> emailList) {
		this.emailList = emailList;
		this.sharefileList = sharefileList;
		this.password = password;
		database = new XMLDatabase();
		database.openTransaction("", DBSituation.LOCAL_DATABASE);
		//TODO Check If shareemails already together in a sharerelation
		checkShareMembers(this.emailList);
		// create HashSet with all shareRelationID
		HashSet<Integer> hashSet_shareRelationList = getShareRelationList(sharefileList);
		// FILL Hashmap with key = sharedfolderID and filelist as value
		HashMap<Integer, ArrayList<EncryptedFileDob>> hashmap = getHashMap(
				sharefileList, hashSet_shareRelationList);
		// Check if entries available
		// TODO cancel sharing
		if (hashmap.isEmpty())
			shareStatus = ShareStatus.ERROR;
		// Check if All Files from one ShareRelation
		if (hashmap.size() == 1) {
			// Check if Files from same ShareRelation but not all
			if (!checkIfMoreFilesAvailable(hashSet_shareRelationList.iterator()
					.next(), sharefileList)) {	
				shareStatus = ShareStatus.CREATE_NEW;
			} else {
				if (hashmap.containsKey(Messages.getIdSize())) {
					// All Files from ShareRelation 0
					shareStatus = ShareStatus.CREATE_NEW;
				} else {
					// All Files from ShareRelation x not 0
					shareStatus = ShareStatus.USE_EXISTING;
				}
			}
		} else {
			// From more then one ShareRelation
			shareStatus = ShareStatus.CREATE_NEW;
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
					shareStatus = ShareStatus.SHARERELATION_EXISTING;
				}
			}
		}
	
		
	}
	
	/**
	 * Use Existing ShareRelation
	 * @return ShareRelation
	 */
	private ShareRelation useExistingSharedRelation() {
		HashSet<Integer> hashFile = new HashSet<Integer>();
		for(EncryptedFileDob fileDob : this.sharefileList){
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
	 * @param shareRelation
	 * @return ShareRelation
	 */
	private ShareRelation createNewSharedRelation(ShareRelation shareRelation) {
		// Move Files
		IFileSystemCom iFile = FileSystemCom.getInstance();	
		CRUDCryptedFileInfo result = iFile.moveFiles(sharefileList, shareRelation);
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
	
	/**
	 * Preparte Files, Container and ShareMembers for Update (use local db)
	 * @param shareRelation
	 */
	private void prepareUpdateDatabase(ShareRelation shareRelation){
		updatefileList = new ArrayList<EncryptedFileDob>();
		GhostFolderDob root = new GhostFolderDob(0, "Root-Folder", null);
			updatecontList = (List<TillLateContainer>) database.getElement(TillLateContainer.class, XMLConstruct.AttShareRelationID, shareRelation.getID().toString());
			List<EncryptedFileDob> dobfileList = new ArrayList<EncryptedFileDob>();
		for(TillLateContainer container : updatecontList){
			dobfileList = (List<EncryptedFileDob>) database.getElement(EncryptedFileDob.class, XMLConstruct.AttContainerId, container.getId().toString());
			for(EncryptedFileDob dob : dobfileList){
				dob.setParent(root);
			}
			updatefileList.addAll(dobfileList);
		}	
		updateshareMemberList = (List<ShareMember>) database.getElement(ShareMember.class, XMLConstruct.AttShareRelationID, shareRelation.getID().toString());	
	}
	
	/**
	 * Create UpdateXML (Use update DB must be created before)
	 * @param database
	 */
	private DatabaseStatus createUpdateFiles(IDatabase database) {
		List<TillLateContainer> cdob  = (List<TillLateContainer>) database.createElement(updatecontList);
		List<EncryptedFileDob> fdob =(List<EncryptedFileDob>) database.createElement(updatefileList);
		List<EncryptedFileDob> sdob =(List<EncryptedFileDob>) database.createElement(updateshareMemberList);
		if(cdob.isEmpty() || fdob.isEmpty() || sdob.isEmpty()){
			return DatabaseStatus.TRANSACTION_FAILED;
		}
		return DatabaseStatus.OK;
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
	
	private void insertShareMembers(ShareRelation shareRelation) {
		ShareMember sharerelation = new ShareMember(shareRelation.getID(), Messages.OwnerMail);
		database.createElement(sharerelation);
		for(String mail : emailList){
			sharerelation = new ShareMember(shareRelation.getID(), mail);
			database.createElement(sharerelation);
		}	
	}


	/**
	 * ShareFiles Return true if oke Return false for Manually
	 * @param auto boolean if autosharing or manually
	 * @return ShareStatus
	 */
	public ShareStatus shareFiles(boolean auto) {
		//Check valid account
		database.openTransaction("", DBSituation.LOCAL_DATABASE);
		CloudAccount account = (CloudAccount) database.getElementAll(CloudAccount.class).get(0);
		if(account == null){
			return ShareStatus.ACCOUNT_NOT_EXISTING;
		}	
		CloudError cloudProviderStatus = CloudError.NONE;
		if (shareStatus == ShareStatus.ERROR) {
			return shareStatus;
		}
		//WHEN Sharerelation not exist
		if (shareStatus == ShareStatus.CREATE_NEW) {
			// CREATE
			// Create and insert newShareRelation
			KeyManager km = KeyManager.getInstance();			
			shareRelation = km.newShareRelation(password, null);
			//Create new ShareRelation on filesystem
			shareRelation = createNewSharedRelation(shareRelation);
			insertShareMembers(shareRelation);
			prepareUpdateDatabase(shareRelation);
			database.closeTransaction("", Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
			//CREATE NEW UPDATE DATABASE
			IDatabase updatedatabase = new XMLDatabase();
			updatedatabase.createDatabase(password, "", DBSituation.UPDATE_DATABASE);
			updatedatabase.openDatabase(password, "", null, DBSituation.UPDATE_DATABASE);			
			updatedatabase.openTransaction("", DBSituation.UPDATE_DATABASE);
			createUpdateFiles(updatedatabase);
			updatedatabase.closeTransaction("", shareRelation.getID(), DBSituation.UPDATE_DATABASE);		
			//Share files
			cloudProviderStatus = shareFilesToCloud(auto, false);
			shareStatus = ShareStatus.OK_NEW;
		
		}
		if (shareStatus == ShareStatus.USE_EXISTING) {
			// USING EXISTING			
			shareRelation = useExistingSharedRelation();
			//Use mannually, Headless browser supports no automatically sharing if file already shared
			cloudProviderStatus = shareFilesToCloud(false, true);
			shareStatus = ShareStatus.OK_Existing;

		}
		//NOT in use until now
		if(shareStatus == ShareStatus.SHARERELATION_EXISTING){
			//ALL MEMBERS ARE IN THE SAME SHARERELATION
			//TODO Update xml etc. not working in this release
			cloudProviderStatus = CloudError.FOLDER_ALREADY_SHARED;	
			shareStatus = ShareStatus.SHARERELATION_EXISTING;
		}
			// TODO ERROR sharing
		if (cloudProviderStatus == CloudError.NONE) {
				// NO ERROR OCCURED
				KeyManager keyManager = KeyManager.getInstance();
				keyManager.addKeyRelation(shareRelation.getID(), shareRelation.getKey());
			//	new SuccessMessage(shell, "Success", "shared");
			} else {
				// ERROR ocured
				shareStatus = ShareStatus.ERROR;
			}
		return shareStatus;
	}

	/**
	 * Share Files on cloud
	 * @param auto (Auto sharing or manually)
	 * @param alreadyshared (If file is shared for the first time, other webdialog)
	 * @return CloudError
	 */
	private CloudError shareFilesToCloud(boolean auto, boolean alreadyshared) {
		ICloudProviderCom com = new CloudDropboxCom();
		if(auto){		
			//TRY twice
			CloudError status = com.shareFolder(shareRelation.getID(), emailList);
			int i = 0;
			if(status != CloudError.NONE && i < 2){
				status = com.shareFolder(shareRelation.getID(), emailList);
			}		
			return status;
		}
		else{
			com.shareFolderManuallyViaBrowser(shareRelation.getID(), alreadyshared);
		}
		return null;
	}
	/**
	 * Return actual ShareRelation (key, id)
	 * @return ShareRelation
	 */
	public ShareRelation getShareRelation() {
		return shareRelation;
	}
	
}
