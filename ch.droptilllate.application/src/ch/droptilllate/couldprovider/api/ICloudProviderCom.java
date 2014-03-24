package ch.droptilllate.couldprovider.api;

import java.util.List;

import ch.droptilllate.cloudprovider.error.CloudError;

public interface ICloudProviderCom {
	/**
	 * Share Folder
	 * @param shareFolderID
	 * @param shareEmailList
	 * @return
	 */
	CloudError shareFolder(int shareFolderID, List<String> shareEmailList);
	
	/**
	 * Test if user exist
	 * @param cloudUser
	 * @param cloundPW
	 * @return
	 */
	CloudError testCloudAccount(String cloudUser, String cloundPW);
	
	/**
	 * Check if Folder Upload
	 * @param droptilllatePath
	 * @param shareRelationID
	 * @return
	 */
	CloudError checkIfFolderExists(int shareRelationID);
}
