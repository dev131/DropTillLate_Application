package ch.droptilllate.database.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.database.api.DBConnection;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.exceptions.DatabaseException;
import ch.droptilllate.database.exceptions.DatabaseStatus;
import ch.droptilllate.database.query.CloudAccountQuery;
import ch.droptilllate.database.query.ContainerQuery;
import ch.droptilllate.database.query.FileQuery;
import ch.droptilllate.database.query.GhostFolderQuery;
import ch.droptilllate.database.query.ShareMemberQuery;

public class XMLDatabase implements IDatabase {

	private Document oldDocument;
	private Document newDocument;
	
	@Override
	public DatabaseStatus createDatabase(String password, String propertiePath, DBSituation situation) {			
		if(situation == DBSituation.LOCAL_DATABASE){
			return createLocalDB(propertiePath, situation);	
		}
		else{
			return createUpdateDB(propertiePath, situation);
		}
		
	}
	
	private DatabaseStatus createUpdateDB(String propertiePath, DBSituation situation) {
		DBConnection con = new DBConnection();
		try {
			con.createFile(con.getPath(situation,propertiePath));
		} catch (DatabaseException e) {
			return DatabaseStatus.DATABASE_NOT_CREATED;
		}
		return DatabaseStatus.OK;
	}

	private DatabaseStatus createLocalDB(String propertiePath, DBSituation situation) {
		DBConnection con = new DBConnection();
		 try {
			con.createFile(con.getPath(situation,propertiePath));		
			openTransaction(propertiePath,situation);
			initDBEntries();
			closeTransaction(propertiePath, Messages.getIdSize(),situation);
			con.deleteTempDB(con.getPath(situation,propertiePath));			
		} catch (DatabaseException e) {
			return DatabaseStatus.DATABASE_NOT_CREATED;
		}
		 return DatabaseStatus.OK;		
	}

	@Override
	public DatabaseStatus openDatabase(String password,String propertiePath, Integer shareFolderID,DBSituation situation){
		DBConnection con = new DBConnection();
		FileHandler fileHandler = new FileHandler();
		File file = new File(con.getPath(situation,propertiePath));
		try {
			if(shareFolderID != null){
				fileHandler.delete(file);
				con.decryptDatabase(password, con.getPath(situation,propertiePath), situation, shareFolderID);
			}			
		} catch (DatabaseException | IOException e) {
			return DatabaseStatus.CANNOT_OPEN_DATABASE;
		}
		return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus openTransaction(String propertiePath,DBSituation situation) {
		DBConnection con = new DBConnection();
		try {
			 oldDocument = con.getDatabase(con.getPath(situation,propertiePath));
			 newDocument = con.getDatabase(con.getPath(situation, propertiePath));
		} catch (DatabaseException e) {
			return DatabaseStatus.CANNOT_OPEN_DATABASE;
		}
		return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus rollback() {
		newDocument = oldDocument;
		return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus closeTransaction(String propertiePath, Integer shareFolderID,DBSituation situation) {
		DBConnection con = new DBConnection();
		try {
			con.writeToXML(con.getPath(situation,propertiePath), newDocument);
		} catch (DatabaseException e) {
			return DatabaseStatus.CANNOT_OPEN_DATABASE;
		}
		try {
			con.encryptDatabase(propertiePath, situation, shareFolderID);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			return DatabaseStatus.CANNOT_WRITE_TO_XML;
		}
		return DatabaseStatus.OK;
	}

	@Override
	public synchronized List<?> createElement(Object obj) {
		List<Object> list = new ArrayList<Object>();
		list.add(obj);
		return createElement(list);
	}

	@Override
	public synchronized List<?> createElement(List<?> obj) {
		if(obj.get(0) instanceof EncryptedFileDob){
		   List<EncryptedFileDob> list = generateFileID((List<EncryptedFileDob>) obj);
		   FileQuery query = new FileQuery();
		   list = query.createElement(list, newDocument);
		   newDocument = query.getDocument();
		   return list;
		};				
		if(obj.get(0) instanceof TillLateContainer){
			 List<TillLateContainer> list = generateContainerID((List<TillLateContainer>) obj);
			 ContainerQuery query = new ContainerQuery();
			 list = query.createElement(list, newDocument);
			   newDocument = query.getDocument();
			   return list;
		};					
		if(obj.get(0) instanceof GhostFolderDob){
			 List<GhostFolderDob> list = generateFolderID((List<GhostFolderDob>) obj);
			 GhostFolderQuery query = new GhostFolderQuery();
			 list = query.createElement(list, newDocument);
			   newDocument = query.getDocument();
			   return list;
		};
		if(obj.get(0) instanceof ShareMember){
			ShareMemberQuery query = new ShareMemberQuery();
				obj = query.createElement((List<ShareMember>) obj, newDocument);
			   newDocument = query.getDocument();
			   return obj;
		};
		if(obj.get(0) instanceof CloudAccount){
			CloudAccountQuery query = new CloudAccountQuery();
			 obj = query.createElement((List<CloudAccount>) obj, newDocument);
			   newDocument = query.getDocument();
			   return obj;
		};
		return null;
	}

	@Override
	public DatabaseStatus deleteElement(Object obj) {
		List<Object> list = new ArrayList<Object>();
		list.add(obj);
		return deleteElement(list);
	}

	@Override
	public DatabaseStatus deleteElement(List<?> obj) {
		if(obj.get(0) instanceof EncryptedFileDob){
			   FileQuery query = new FileQuery();
			   newDocument = query.deleteElement((List<EncryptedFileDob>) obj, newDocument);
			};				
			if(obj.get(0) instanceof TillLateContainer){
				 ContainerQuery query = new ContainerQuery();
				   newDocument = query.deleteElement((List<TillLateContainer>)obj, newDocument);
			};					
			if(obj.get(0) instanceof GhostFolderDob){
				 GhostFolderQuery query = new GhostFolderQuery();
				   newDocument = query.deleteElement((List<GhostFolderDob>)obj, newDocument);
			};
			if(obj.get(0) instanceof ShareMember){
				ShareMemberQuery query = new ShareMemberQuery();
				   newDocument = query.deleteElement((List<ShareMember>)obj, newDocument);
			};
			if(obj.get(0) instanceof CloudAccount){
				CloudAccountQuery query = new CloudAccountQuery();
				   newDocument = query.deleteElement((List<CloudAccount>)obj, newDocument);
			};
			return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus updateElement(Object obj) {
		List<Object> list = new ArrayList<Object>();
		list.add(obj);
		return updateElement(list);
	}

	@Override
	public DatabaseStatus updateElement(List<?> obj) {
		if(obj.get(0) instanceof EncryptedFileDob){
			   FileQuery query = new FileQuery();
			   newDocument = query.updateElement((List<EncryptedFileDob>)obj, newDocument);};				
			if(obj.get(0) instanceof TillLateContainer){
				 ContainerQuery query = new ContainerQuery();
				  newDocument = query.updateElement((List<TillLateContainer>)obj, newDocument);};					
			if(obj.get(0) instanceof GhostFolderDob){
				 GhostFolderQuery query = new GhostFolderQuery();
				  newDocument = query.updateElement((List<GhostFolderDob>)obj, newDocument);};
			if(obj.get(0) instanceof ShareMember){
				ShareMemberQuery query = new ShareMemberQuery();
				  newDocument = query.updateElement((List<ShareMember>)obj, newDocument);};
			if(obj.get(0) instanceof CloudAccount){
				CloudAccountQuery query = new CloudAccountQuery();
				  newDocument = query.updateElement((List<CloudAccount>)obj, newDocument);};
			return DatabaseStatus.OK;
	}

	@Override
	public List<?> getElement(Object type, String argument, String value) {
		if(type == EncryptedFileDob.class){
			   FileQuery query = new FileQuery();
			   return   query.getElement(argument, value, newDocument);}
			if(type == TillLateContainer.class){
				 ContainerQuery query = new ContainerQuery();
				  return query.getElement(argument, value, newDocument);}			
			if(type == GhostFolderDob.class){
				 GhostFolderQuery query = new GhostFolderQuery();
				  return query.getElement(argument, value, newDocument);}
			if(type == ShareMember.class){
				ShareMemberQuery query = new ShareMemberQuery();
				  return  query.getElement(argument, value, newDocument);}
			if(type == CloudAccount.class){
				CloudAccountQuery query = new CloudAccountQuery();
				  return  query.getElement(argument, value, newDocument);}
			return null;
	}

	@Override
	public List<?> getElementAll(Object type) {
		if(type == EncryptedFileDob.class){
			   FileQuery query = new FileQuery();
			   return  query.getElementAll(newDocument);}
		if(type == TillLateContainer.class){
				 ContainerQuery query = new ContainerQuery();
				  return   query.getElementAll(newDocument);}		
		if(type == GhostFolderDob.class){
				 GhostFolderQuery query = new GhostFolderQuery();
				  return   query.getElementAll(newDocument);}
		if(type == ShareMember.class){
				ShareMemberQuery query = new ShareMemberQuery();
				  return   query.getElementAll(newDocument);}
		if(type == CloudAccount.class){
				CloudAccountQuery query = new CloudAccountQuery();
				  return  query.getElementAll(newDocument);}
			return null;
	}

	@Override
	public List<?> getElementByParent(Object type, GhostFolderDob folder) {
		if(type == EncryptedFileDob.class){
			   FileQuery query = new FileQuery();
			   return   query.getElementByParent(folder, newDocument);}
		if(type == TillLateContainer.class){
				  return  null;}		
		if(type == GhostFolderDob.class){
				 GhostFolderQuery query = new GhostFolderQuery();
				  return  query.getElementByParent(folder, newDocument);}
		if(type == ShareMember.class){
				  return  null;}
		if(type == CloudAccount.class){
				  return  null;}
			return null;
	}
	
	private List<EncryptedFileDob> generateFileID(List<EncryptedFileDob> obj){
		for(EncryptedFileDob dob: obj){
			if (dob.getId() == null) {
				int id = (int) (Math.random() * Messages.getIdSize() + 1);
				// Check if it exist
				while (!(getElement(EncryptedFileDob.class, XMLConstruct.AttId, Integer.toString(id)).isEmpty())) {
					id = (int) (Math.random() * Messages.getIdSize() + 1);
				}
				dob.setId(id);
			}
		}	
		return obj;
	}
	
	private List<GhostFolderDob> generateFolderID(List<GhostFolderDob> obj){
		for(GhostFolderDob dob: obj){
			if (dob.getId() == null) {
				int id = (int) (Math.random() * Messages.getIdSize() + 1);
				// Check if it exist
				while (!(getElement(EncryptedFileDob.class, XMLConstruct.AttId, Integer.toString(id)).isEmpty())) {
					id = (int) (Math.random() * Messages.getIdSize() + 1);
				}
				dob.setId(id);
			}
		}	
		return obj;
	}
	
	private List<TillLateContainer> generateContainerID(List<TillLateContainer> obj){
		for(TillLateContainer dob: obj){
			if (dob.getId() == null) {
				int id = (int) (Math.random() * Messages.getIdSize() + 1);
				// Check if it exist
				while (!(getElement(EncryptedFileDob.class, XMLConstruct.AttId, Integer.toString(id)).isEmpty())) {
					id = (int) (Math.random() * Messages.getIdSize() + 1);
				}
				dob.setId(id);
			}
		}	
		return obj;
	}


	private void initDBEntries(){
			StructureXmlDob sxml = new StructureXmlDob(DBSituation.LOCAL_DATABASE);
			EncryptedFileDob filedob = sxml.getEncryptedFileDob();
			ShareMember shareRelation= sxml.getShareMember();
			TillLateContainer encryptedContainer = sxml.getEncryptedContainer();
			//TODO error handling if not working target or whatever
			createElement(filedob);
			createElement(shareRelation);
			createElement(encryptedContainer);
	}
	
}
