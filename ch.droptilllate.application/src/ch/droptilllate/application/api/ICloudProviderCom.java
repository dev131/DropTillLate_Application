package ch.droptilllate.application.api;

import java.util.List;

import ch.droptilllate.cloudprovider.error.CloudError;

public interface ICloudProviderCom {
	/**
	 * Share Folder Automatically
	 * @param shareFolderID
	 * @param shareEmailList
	 * @return CloudError
	 */
	CloudError shareFolder(int shareFolderID, List<String> shareEmailList);
	
	/**
	 * Test if user exist
	 * @param cloudUser
	 * @param cloundPW
	 * @return CloudError
	 */
	CloudError testCloudAccount(String cloudUser, String cloundPW);
	
	/**
	 * Check if Folder Upload
	 * @param droptilllatePath
	 * @param shareRelationID
	 * @return CloudError
	 */
	CloudError checkIfFolderExists(int shareRelationID);
	
	/**
	 * Share Folder Manually
	 * @param droptilllatePath
	 * @param shareRelationID
	 * @param alreadyShared
	 * @return CloudError
	 */
	 CloudError shareFolderManuallyViaBrowser(Integer shareRelationID, boolean alreadyShared);
}
