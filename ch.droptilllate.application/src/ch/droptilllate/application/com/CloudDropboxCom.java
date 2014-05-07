package ch.droptilllate.application.com;

import java.util.List;

import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.lifecycle.OSValidator;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.cloudprovider.api.ICloudProvider;
import ch.droptilllate.cloudprovider.api.ICloudProviderCom;
import ch.droptilllate.cloudprovider.dropbox.DropboxHandler;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.XMLDatabase;

public class CloudDropboxCom implements ICloudProviderCom {
	private ICloudProvider iprovider;

	@Override
	public CloudError shareFolder(int shareFolderID, List<String> shareEmailList) {
		iprovider = new DropboxHandler(); 	
		CloudAccount account = getCloudAccount();
		return iprovider.shareFolder(getDropboxPath(), shareFolderID, account.getUsername(), account.getPassword(), shareEmailList);		
	}

	@Override
	public CloudError testCloudAccount(String cloudUser, String cloundPW) {
		iprovider = new DropboxHandler(); 
		return iprovider.testCloudAccount(cloudUser, cloundPW);
	}

	@Override
	public CloudError checkIfFolderExists(int shareRelationID) {
		iprovider = new DropboxHandler();
		IDatabase database = new XMLDatabase();
		database.openTransaction("", DBSituation.LOCAL_DATABASE);	
		CloudAccount account = (CloudAccount) database.getElementAll(CloudAccount.class).get(0);
		database.closeTransaction("", Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		return iprovider.checkIfFolderExists(getDropboxPath(), shareRelationID, account.getUsername(), account.getPassword());
	}
	
	@Override
	public CloudError shareFolderManuallyViaBrowser(Integer shareRelationID, boolean alreadyShared) {
		iprovider = new DropboxHandler();
		return iprovider.shareFolderManuallyViaBrowser(getDropboxPath(), shareRelationID, alreadyShared);
	}
	
	/**
	 * OSX Convert /Users/dropbox/stuff/droptilllate in stuff/droptilllate
	 * WINDOWS Covert C:\\dropbox\\stuff\\droptilllate
	 * @return string path
	 */
	private String getDropboxPath() {
		// TODO Auto-generated method stub
		Boolean reached = false;
		int count = 0;
		String path ="";
		String tempPath = Configuration.getPropertieDropBoxPath("",false);
		String[] splitResult = tempPath.split(OSValidator.getSlashForSplit());
		for(String pathPart : splitResult){
			if(reached == true){
				if(count>0){
					path =path+"/"+ pathPart;
				}
				else{
					path = pathPart;
					count++;
				}		
			}
			if(pathPart.equalsIgnoreCase("dropbox")){
				reached = true;
			}
		}
		return path;
	}

	private CloudAccount getCloudAccount(){
		IDatabase database = new XMLDatabase();
		database.openTransaction("", DBSituation.LOCAL_DATABASE);	
		CloudAccount account =  (CloudAccount) database.getElementAll(CloudAccount.class).get(0);
		database.closeTransaction("", Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		return account;
	}


}
