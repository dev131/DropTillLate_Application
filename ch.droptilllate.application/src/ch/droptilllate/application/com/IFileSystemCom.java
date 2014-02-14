package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.model.EncryptedFileDob;

import ch.droptilllate.filesystem.api.FileInfo;

public interface IFileSystemCom {
	/**
	 * Encrypt Files
	 * @param droppedFiles
	 * @param containerPath
	 * @return List<FileInfo>
	 */
	public List<FileInfo> encryptFile(List<EncryptedFileDob> droppedFiles, String containerPath);
	/**
	 * Update Files List<FileInfo>
	 * @param filehandling_result  List<FileInfo>
	 */
	public List<FileInfo> decryptFile(List<EncryptedFileDob> droppedFiles, String containerPath);
	/**
	 * Delte File
	 * @param fileList
	 * @return List<EncryptedFileDob>
	 */
	public boolean deleteFile(List<EncryptedFileDob> fileList);

}
