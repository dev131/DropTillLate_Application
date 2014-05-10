package ch.droptilllate.application.listener;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;

import ch.droptilllate.application.api.IFileSystemCom;
import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.model.EncryptedFileDob;

public class FileChangeListener implements FileListener {
	private List<EncryptedFileDob> dob;

	public FileChangeListener(EncryptedFileDob dob) {
		this.dob = new ArrayList<EncryptedFileDob>();
		this.dob.add(dob);
	}

	@Override
	public void fileChanged(FileChangeEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("fileChanged");
		IFileSystemCom file = FileSystemCom.getInstance();	
//		if(dob.get(0).getId() == Integer.parseInt(XMLConstruct.getIdXMLFiles())){
//			file.encryptFile(destinationShareFolder, local)
//		}
		file.encryptFile(this.dob, true);

	}

	@Override
	public void fileCreated(FileChangeEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("fileCreated");

	}

	@Override
	public void fileDeleted(FileChangeEvent arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("fileDeleted");

	}

}
