package ch.droptilllate.application.converter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.filesystem.info.FileInfo;

public class FileIntegryConverter {

	
	
	public HashMap<Integer, List<EncryptedFileDob>> convert(HashMap<Integer, List<FileInfo>> hashmap){
		Set<Integer> set = hashmap.keySet();
		HashMap<Integer, List<EncryptedFileDob>> newHashMap = new HashMap<Integer, List<EncryptedFileDob>>();
		for(Integer i : set){
			List<EncryptedFileDob> fileList = new ArrayList<EncryptedFileDob>();
			for(FileInfo fileInfo : hashmap.get(i)){
				EncryptedFileDob dob = new EncryptedFileDob(
						fileInfo.getFileID(), 
						Integer.toString(fileInfo.getFileID()), 
						new Date(fileInfo.getTimeStamp()), 
						null, 
						new GhostFolderDob(0, "Root-Folder", null), 
						fileInfo.getSize(), 
						fileInfo.getContainerInfo().getContainerID()
						);
				fileList.add(dob);
			}			
			newHashMap.put(i, fileList);
		}
		
		return newHashMap;
	}
}
