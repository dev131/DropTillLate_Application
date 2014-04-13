package ch.droptilllate.application.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.filesystem.info.FileInfo;

public class FileIntegryConverter {

	
	
	public HashMap<Integer, List<EncryptedFileDob>> convert(HashMap<Integer, List<FileInfo>> hashmap){
		Set<Integer> set = hashmap.keySet();
		for(Integer i : set){
			for(FileInfo fileInfo : hashmap.get(i)){
				//EncryptedFileDob dob = new EncryptedFileDob();
					
				
			}
		}
		
		return null;
	}
}
