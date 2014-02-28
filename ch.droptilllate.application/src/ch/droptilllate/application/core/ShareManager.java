package ch.droptilllate.application.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.swt.widgets.MessageBox;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.com.IXmlDatabase;
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

	/**
	 * Create new ShareFolder and Move fileList into it
	 * 
	 * @param fileList
	 * @param password
	 */
	public void newShareRelation(List<EncryptedFileDob> fileList,
			String password) {
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

	private void useExistingSharedFolder(List<EncryptedFileDob> fileList,
			String password) {
		
		
	}

	private void createNewSharedFolder(
			List<EncryptedFileDob> fileList, String password) {
		KeyManager km = new KeyManager();
		String key = null;		
		// Create and insert newShareFolder in DB and create Id
		IXmlDatabase shareDao = new ShareFolderDao();
		ShareFolder sharedFolder = new ShareFolder(null,
				Messages.getPathDropBox(), null);
		sharedFolder = (ShareFolder) shareDao.newElement(sharedFolder);
		key = km.generatePassword(password, sharedFolder.getPath());
		sharedFolder.setKey(key);
		shareDao.updateElement(sharedFolder);
		// Move Files
		IFileSystemCom iFile = new FileSystemCom();
		CRUDCryptedFileInfo result = iFile.moveFiles(fileList, sharedFolder);
		// Handle Error
		for (EncryptedFileDob fileDob : result.getEncryptedFileListError()) {
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> sharing not worked");
		}
		// Update Database
		for (EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()) {
			IXmlDatabase fileDB = new EncryptedFileDao();
			fileDB.updateElement(fileDob);
		}
		//
	}
	
	private void alertMembers(HashSet<Integer> hashSet_shareFolderList){
		List<ShareRelation> shareRelationList = null;	
		for(Integer i : hashSet_shareFolderList){
			ShareRelationDao dao = new ShareRelationDao();
			//Return list of all shareRelation
			shareRelationList.addAll((List<ShareRelation>) dao.getElementByID(i));
			JOptionPane.showMessageDialog(null,"Kein Icon","Titel", JOptionPane.PLAIN_MESSAGE);
		}
		for(ShareRelation shareRelation : shareRelationList){
			 JOptionPane.showMessageDialog(null,shareRelation.getMail(),"Please Contact the following poeple", JOptionPane.PLAIN_MESSAGE);
		}		
	}

	private boolean checkIfMoreFilesAvailable(Integer shareFolderId,
			List<EncryptedFileDob> fileList) {
		ContainerDao dao = new ContainerDao();
		EncryptedFileDao daof = new EncryptedFileDao();
		List<EncryptedContainer> containerlist = new ArrayList<EncryptedContainer>();
		containerlist = (List<EncryptedContainer>) dao
				.getContainerBySharedFolderId(shareFolderId);
		List<Integer> tempList1 = new ArrayList<Integer>();
		List<Integer> tempList2 = new ArrayList<Integer>();
		for (EncryptedContainer container : containerlist) {
			tempList1 = (List<Integer>) daof.getFileIdsByContainerId(container
					.getId());
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
					.getElementByID(fileDob.getContainerId());
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
						.getContainerId())).getShareFolderId() == sharefolderId) {
					arraylist.add(fileDob);
				}
			}
			hashmap.put(sharefolderId, arraylist);
		}
		return hashmap;
	}

}
