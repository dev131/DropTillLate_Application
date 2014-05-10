package ch.droptilllate.application.handlers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

import ch.droptilllate.application.api.IFileSystemCom;
import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.listener.FileChangeListener;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.properties.Configuration;

public class FileHandler {
	
	public FileHandler(){}
	
	public void copyFile(File source, File dest) throws IOException {
		
		if(!dest.exists()) {
			dest.createNewFile();
		}
        InputStream in = null;
        OutputStream out = null;
        try {
        	in = new FileInputStream(source);
        	out = new FileOutputStream(dest);
    
	        // Transfer bytes from in to out
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
        }
        finally {
        	in.close();
            out.close();
        }
        
	}
	
	public void moveFile(File source, File dest){
			 
    	InputStream inStream = null;
	OutputStream outStream = null;
 
    	try{
 
  
    	    inStream = new FileInputStream(source);
    	    outStream = new FileOutputStream(dest);
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    //copy the file content in bytes 
    	    while ((length = inStream.read(buffer)) > 0){
 
    	    	outStream.write(buffer, 0, length); 
    	    }
 
    	    inStream.close();
    	    outStream.close();
 
    	    //delete the original file
    	//    afile.delete();
 
    	    System.out.println("File is copied successful!");
 
    	}catch(IOException e){
    	    e.printStackTrace();
    	}
	}
	
	public void copyDirectory(File sourceDir, File destDir) throws IOException {
		
		if(!destDir.exists()) {
			destDir.mkdir();
		}
		
		File[] children = sourceDir.listFiles();
		
		for(File sourceChild : children) {
			String name = sourceChild.getName();
			File destChild = new File(destDir, name);
			if(sourceChild.isDirectory()) {
				copyDirectory(sourceChild, destChild);
			}
			else {
				copyFile(sourceChild, destChild);
			}
		}	
	}
	
	/**
	 * Delete file or folder
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public  boolean delete(File resource) throws IOException { 
		if(resource.isDirectory()) {
			File[] childFiles = resource.listFiles();
			for(File child : childFiles) {
				delete(child);
			}
						
		}
		return resource.delete();
	}
	
	public void setFileListener(File file, EncryptedFileDob dob){
		FileObject listendir = null;
		try {
			FileSystemManager fsManager = VFS.getManager();
			listendir = fsManager.resolveFile(file.getPath());
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DefaultFileMonitor fm = new DefaultFileMonitor(new FileChangeListener(dob));
		 fm.setRecursive(true);
		 fm.addFile(listendir);
		 fm.start();
	}

	public List<EncryptedFileDob> openFiles(List<EncryptedFileDob> filellist){
		IFileSystemCom iFileSystem = FileSystemCom.getInstance();
		CRUDCryptedFileInfo result = iFileSystem.decryptFile(filellist);
		for (EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()) {		
			File file = new File(Configuration.getPropertieTempPath("",true)
					+ fileDob.getId() + "." + fileDob.getType());
			FileHandler fileHanlder = new FileHandler();
			fileHanlder.setFileListener(file, fileDob);
			try {
				Desktop.getDesktop().edit(file);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.getEncryptedFileListError();
	}
}
 
