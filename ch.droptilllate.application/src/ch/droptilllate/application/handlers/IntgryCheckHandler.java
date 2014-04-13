
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
import ch.droptilllate.cloudprovider.api.IFileSystemCom;
import ch.droptilllate.filesystem.info.FileInfo;

public class IntgryCheckHandler {
	@Execute
	public void execute(Shell shell) {
		IFileSystemCom com = FileSystemCom.getInstance();
		HashMap<Integer, List<EncryptedFileDob>> hashmap = com.fileIntegryCheck();
		List<EncryptedFileDob> fileListDB = getFileListDB();
		List<EncryptedFileDob> updateListDB = getUpdateFileList(fileListDB, hashmap);
		if(!updateListDB.isEmpty()){
			EncryptedFileDao dao = new EncryptedFileDao();
			dao.deleteElementAll(null);
			dao.newElement(updateListDB, null);
			ViewController.getInstance().getInitialInput();
		}
	}

	private List<EncryptedFileDob> getUpdateFileList(
			List<EncryptedFileDob> fileListDB,
			HashMap<Integer, List<EncryptedFileDob>> hashmap) {
		Set<Integer> idset = hashmap.keySet();
		List<EncryptedFileDob> updateListDB = new ArrayList<EncryptedFileDob>();
		for(Integer id : idset){			
			for(EncryptedFileDob fsDob : hashmap.get(id)){
				for(EncryptedFileDob dbDob : fileListDB){
					if(fsDob.getId() == dbDob.getId()){
						dbDob.setContainerId(fsDob.getContainerId());
						updateListDB.add(dbDob);
						//Remove udpatedfile from list
						hashmap.get(id).remove(fsDob);
					}
				}
			}
			//Check if container available on db
			for(EncryptedFileDob fsDob : hashmap.get(id)){
				ContainerDao dao = new ContainerDao();
				EncryptedContainer container = (EncryptedContainer) dao.getElementByID(fsDob.getContainerId(), null);
				if(container== null){
					hashmap.get(id).remove(fsDob);
				}	
			}
			//the rest of the files are not available on the datalayer insert on root
			updateListDB.addAll(hashmap.get(id));
		}
			return updateListDB;
	}

	private List<EncryptedFileDob> getFileListDB() {
		EncryptedFileDao dao = new EncryptedFileDao();	
		List<EncryptedFileDob> listDB = new ArrayList<EncryptedFileDob>();
		listDB.addAll((List<EncryptedFileDob>) dao.getElementAll(null));
		return listDB;
	}
	

}
