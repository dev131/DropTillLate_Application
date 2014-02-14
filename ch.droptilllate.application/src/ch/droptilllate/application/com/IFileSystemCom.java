package ch.droptilllate.application.com;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.filesystem.api.ContainerInfo;
import ch.droptilllate.filesystem.api.FileInfo;
import ch.droptilllate.filesystem.api.FileInfoDecrypt;
import ch.droptilllate.filesystem.api.FileInfoEncrypt;
import ch.droptilllate.filesystem.api.FileSystemHandler;
import ch.droptilllate.filesystem.api.IFileSystem;

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
