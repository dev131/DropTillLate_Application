package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.dao.CloudAccountDao;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.cloudprovider.api.ICloudProvider;
import ch.droptilllate.cloudprovider.dropbox.DropboxHandler;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.couldprovider.api.ICloudProviderCom;
import ch.droptilllate.filesystem.api.IFileSystem;

public class CloudDropboxCom implements ICloudProviderCom {
	private ICloudProvider iprovider;

	@Override
	public CloudError shareFolder(int shareFolderID, List<String> shareEmailList) {
		iprovider = new DropboxHandler(); 	
		CloudAccountDao dao = new CloudAccountDao();
		CloudAccount account = (CloudAccount) dao.getElementAll(null);
		return iprovider.shareFolder(Configuration.getPropertieDropBoxPath(false), shareFolderID, account.getUsername(), account.getPassword(), shareEmailList);
	}

	@Override
	public CloudError testCloudAccount(String cloudUser, String cloundPW) {
		iprovider = new DropboxHandler(); 
		return iprovider.testCloudAccount(cloudUser, cloundPW);
	}

	@Override
	public CloudError checkIfFolderExists(int shareRelationID) {
		iprovider = new DropboxHandler();
		CloudAccountDao dao = new CloudAccountDao();
		CloudAccount account = (CloudAccount) dao.getElementAll(null);
		return iprovider.checkIfFolderExists(Configuration.getPropertieDropBoxPath(false), shareRelationID, account.getUsername(), account.getPassword());
	}
}
