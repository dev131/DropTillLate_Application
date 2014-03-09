/**
 * 
 */
package ch.droptilllate.couldprovider.api;

import java.util.List;

/**
 * This Interface can be used to share a folder to a list of users.
 * Depending on the Instance, different provider can be choosen.
 * @author Roewn
 *
 */
public interface IShareFolder
{
	/**
	 * Shares the folder of the passed share Relation with the users specified in the userEmailList.	 * 
	 * @param droptilllatePath path to the root folder (DropTillLate Folder) which holds all the shareRelation directories.
	 * 			Example: C:\dropbox\droptilllate
	 * @param shareRelationID name of the folder to shared identified by the shareRelationID
	 * @param userList List of email addresses for all users the share relations hast to be shared with.
	 */
	void shareFolder(String droptilllatePath, int shareRelationID , List<String> userEmailList);
}
