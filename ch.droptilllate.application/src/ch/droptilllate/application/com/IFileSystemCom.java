package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.filesystem.info.*;
import ch.droptilllate.filesystem.api.FileHandlingSummary;


public interface IFileSystemCom {
	/**
	 * Encrypt Files
	 * @param droppedFiles
	 * @param shareFolderPath example Dropbox/DroptillLate/Sharefolder1 
	 * shareFolderPath == null -> existingFile
	 * @return List<FileInfo>
	 */
	public CRUDCryptedFileInfo encryptFile(List<EncryptedFileDob> droppedFiles, ShareFolder sharefolder);
	/**
	 * Update Files List<FileInfo>
	 * @param filehandling_result  List<FileInfo>
	 * @param sharefolderPath example Dropbox/DroptillLate/Sharefolder1
	 * @return List<FileInfo>
	 */
	public CRUDCryptedFileInfo decryptFile(List<EncryptedFileDob> droppedFiles);
	/**
	 * Delte File
	 * @param fileList
	 * @return List<EncryptedFileDob>
	 */
	public CRUDCryptedFileInfo deleteFile(List<EncryptedFileDob> fileList);
	
	/**
	 * Move Files into new ShareRelation
	 * @param fileList
	 * @param ShareFolder
	 * @return FileInfoList
	 */
	public CRUDCryptedFileInfo moveFiles(List<EncryptedFileDob> fileList, ShareFolder sharedFolder);
	/**
	 *  Encrypt local/share File in specified Container ID
	 * @param local
	 * @param destinationShareFolder path = Dropbox/Sharefolder1
	 * @return true if ok
	 */
	public boolean encryptFile(ShareFolder destinationShareFolder, boolean local);

	/**
	 * Decrypte local/share XML files
	 * @param sourceShareFolder path = Dropbox/Sharefolder1
	 * @param local
	 * @return true if ok
	 */
	public boolean decryptFile(ShareFolder sourceShareFolder, boolean local);
	
	
	
}
