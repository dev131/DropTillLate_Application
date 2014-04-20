
package ch.droptilllate.application.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.cloudprovider.api.IFileSystemCom;
import ch.droptilllate.filesystem.info.FileInfo;

public class IntgryCheckHandler {
	@Execute
	public void execute(Shell shell) {
		IFileSystemCom com = FileSystemCom.getInstance();
		HashMap<Integer, List<EncryptedFileDob>> hashmap = com.fileIntegryCheck();
		List<EncryptedFileDob> fileListDB = getFileListDB();
		List<EncryptedFileDob> fileListFS = getFileListFS(hashmap);
		List<EncryptedFileDob> upgradeListDB = getUpgradeFileList(fileListDB, fileListFS);
		if(!upgradeListDB.isEmpty()){
			EncryptedFileDao dao = new EncryptedFileDao();
			dao.deleteElementAll(null);
			dao.newElement(upgradeListDB, null);
			ViewController.getInstance().getInitialInput();
		}
	}

	private List<EncryptedFileDob> getUpgradeFileList(List<EncryptedFileDob> fileListDB, List<EncryptedFileDob> fileListFS) {
		List<EncryptedFileDob> upgradeListDB = new ArrayList<EncryptedFileDob>();
		int mDobId = Messages.getIdSize();
		for(EncryptedFileDob fsDob : fileListFS){
			boolean available = false;
			int fsDobId = fsDob.getId();
			for(EncryptedFileDob dbDob : fileListDB){
				//Available in both site			
				int dbDobId = dbDob.getId();
				if(fsDobId == dbDobId){
					//ContainerInfo from Fileystem
					dbDob.setContainerId(fsDob.getContainerId());
					//MainFile not update
				
					if(fsDobId!= mDobId){
						upgradeListDB.add(dbDob);
						available = true;
					}
				}
			}
			if(!available && mDobId != fsDobId){
				upgradeListDB.add(fsDob);
			}
		}
		return upgradeListDB;
	}

	private List<EncryptedFileDob> getFileListFS(HashMap<Integer, List<EncryptedFileDob>> hashmap) {
		List<EncryptedFileDob> fileListFS = new ArrayList<EncryptedFileDob>();
		Set<Integer> idset = hashmap.keySet();
		for(Integer id : idset){
			for(EncryptedFileDob fsDob : hashmap.get(id)){
				fileListFS.add(fsDob);
			}
		}
		return fileListFS;
	}

	private List<EncryptedFileDob> getFileListDB() {
		EncryptedFileDao dao = new EncryptedFileDao();	
		List<EncryptedFileDob> listDB = new ArrayList<EncryptedFileDob>();
		listDB.addAll((List<EncryptedFileDob>) dao.getElementAll(null));
		return listDB;
	}
	

}
