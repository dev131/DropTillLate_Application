package ch.droptilllate.cloudprovider.api;

import java.util.HashMap;
import java.util.List;

import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.filesystem.info.*;
import ch.droptilllate.filesystem.api.FileHandlingSummary;


public interface IFileSystemCom {

	/**
	 * Encrypt files
	 * @param droppedFiles
	 * @param exist if Exist == true = file is encrypted already
	 * @return
	 */
	public CRUDCryptedFileInfo encryptFile(List<EncryptedFileDob> droppedFiles, Boolean exist);
	/**
	 * Update Files List<FileInfo>
	 * @param filehandling_result  List<FileInfo>
	 * @param ShareRelationPath example Dropbox/DroptillLate/ShareRelation1
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
	 * @param ShareRelation
	 * @return FileInfoList
	 */
	public CRUDCryptedFileInfo moveFiles(List<EncryptedFileDob> fileList, ShareRelation destShareRelation);
	/**
	 *  Encrypt local/share File in specified Container ID
	 * @param local
	 * @param destinationShareRelation path = Dropbox/ShareRelation1
	 * @return true if ok
	 */
	public boolean encryptFile(ShareRelation destShareRelation, boolean local);

	/**
	 * Decrypte local/share XML files
	 * @param sourceShareRelation path = Dropbox/ShareRelation1
	 * @param local
	 * @return true if ok
	 */
	public boolean decryptFile(ShareRelation srcShareRelation, boolean local);
	
	/**
	 * Return Hashmap with ShareId and List of EncryptedFileDob as value
	 * @return
	 */
	public HashMap<Integer, List<EncryptedFileDob>>  fileIntegryCheck();
}