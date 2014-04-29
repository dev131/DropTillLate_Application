package ch.droptilllate.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.ShareMembersDao;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.exceptions.DatabaseException;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;

public class XMLDatabase implements IDatabase {

	private Document oldDocument;
	private Document newDocument;
	private String password;
	
	@Override
	public DatabaseStatus createDatabase(String password, boolean local, String propertiePath) {		
		this.password = password;
		DBConnection con = new DBConnection();
		 try {
			con.createFile(con.getPath(local,propertiePath), password, local);		
			openTransaction(local, propertiePath);
			initDBEntries();
			closeTransaction(local, propertiePath);
			con.encryptDatabase(password, local);
			con.deleteTempDB(con.getPath(local,propertiePath));			
		} catch (DatabaseException e) {
			return DatabaseStatus.DATABASE_NOT_CREATED;
		}
		 return DatabaseStatus.OK;
	}
	
	@Override
	public DatabaseStatus openDatabase(String password, boolean local,String propertiePath){
		this.password = password;
		DBConnection con = new DBConnection();
		FileHandler fileHandler = new FileHandler();
		File file = new File(con.getPath(local,propertiePath));
		try {
			fileHandler.delete(file);
			con.decryptDatabase(password, con.getPath(local,propertiePath), local);
		} catch (DatabaseException | IOException e) {
			return DatabaseStatus.CANNOT_OPEN_DATABASE;
		}
		return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus openTransaction(boolean local, String propertiePath) {
		DBConnection con = new DBConnection();
		try {
			 oldDocument = con.getDatabase(con.getPath(local,propertiePath));
			 newDocument = con.getDatabase(con.getPath(local, propertiePath));
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
	public DatabaseStatus closeTransaction(boolean local, String propertiePath) {
		DBConnection con = new DBConnection();
		try {
			con.writeToXML(this.password, con.getPath(local,propertiePath), newDocument, local);
		} catch (DatabaseException e) {
			return DatabaseStatus.CANNOT_OPEN_DATABASE;
		}
		try {
			con.encryptDatabase(propertiePath, local);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			return DatabaseStatus.CANNOT_WRITE_TO_XML;
		}
		return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus createElement(Object obj) {
		List<Object> list = new ArrayList<Object>();
		list.add(obj);
		return createElement(list);
	}

	@Override
	public DatabaseStatus createElement(List<?> obj) {
		if(obj.get(0) instanceof EncryptedFileDob){
		   List<EncryptedFileDob> list = generateFileID((List<EncryptedFileDob>) obj);
		   FileQuery query = new FileQuery();
		   newDocument = query.createElement(list, newDocument);
		};				
		if(obj.get(0) instanceof EncryptedContainer){
			 List<EncryptedContainer> list = generateContainerID((List<EncryptedContainer>) obj);
			 ContainerQuery query = new ContainerQuery();
			  newDocument = query.createElement(list, newDocument);
		};					
		if(obj.get(0) instanceof GhostFolderDob){
			 List<GhostFolderDob> list = generateFolderID((List<GhostFolderDob>) obj);
			 GhostFolderQuery query = new GhostFolderQuery();
			  newDocument = query.createElement(list, newDocument);
		};
		if(obj.get(0) instanceof ShareMember){
			ShareMemberQuery query = new ShareMemberQuery();
			  newDocument = query.createElement((List<ShareMember>) obj, newDocument);
		};
		if(obj.get(0) instanceof CloudAccount){
			CloudAccountQuery query = new CloudAccountQuery();
			  newDocument = query.createElement((List<CloudAccount>) obj, newDocument);
		};
		return DatabaseStatus.OK;
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
			if(obj.get(0) instanceof EncryptedContainer){
				 ContainerQuery query = new ContainerQuery();
				   newDocument = query.deleteElement((List<EncryptedContainer>)obj, newDocument);
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
			if(obj.get(0) instanceof EncryptedContainer){
				 ContainerQuery query = new ContainerQuery();
				  newDocument = query.updateElement((List<EncryptedContainer>)obj, newDocument);};					
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
			if(type == EncryptedContainer.class){
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
		if(type == EncryptedContainer.class){
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
		if(type == EncryptedContainer.class){
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
	
	private List<EncryptedContainer> generateContainerID(List<EncryptedContainer> obj){
		for(EncryptedContainer dob: obj){
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
			StructureXmlDob sxml = new StructureXmlDob(true);
			EncryptedFileDob filedob = sxml.getEncryptedFileDob();
			ShareMember shareRelation= sxml.getShareMember();
			EncryptedContainer encryptedContainer = sxml.getEncryptedContainer();
			//TODO error handling if not working target or whatever
			createElement(filedob);
			createElement(shareRelation);
			createElement(encryptedContainer);
	}
	
}
