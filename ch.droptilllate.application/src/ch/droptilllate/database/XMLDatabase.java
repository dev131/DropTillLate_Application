package ch.droptilllate.database;

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
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.model.StructureXmlDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;

public class XMLDatabase implements IDatabase {

	private Document oldDocument;
	private Document newDocument;
	
	@Override
	public DatabaseStatus createDatabase(String password, boolean local, String propertiePath) {		
		DBConnection con = new DBConnection();
		 try {
			con.createFile(con.getPath(local,propertiePath));
			con.encryptDatabase(password, local);	
			openTransaction(local, propertiePath);
			initDBEntries();
			con.writeToXML(con.getPath(local, propertiePath), newDocument);
			con.deleteTempDB(con.getPath(local,propertiePath));
		} catch (DatabaseException e) {
			return DatabaseStatus.DATABASE_NOT_CREATED;
		}
		 return DatabaseStatus.OK;
	}
	
	@Override
	public DatabaseStatus openDatabase(String password, boolean local,String propertiePath){
		DBConnection con = new DBConnection();
		try {
			con.decryptDatabase(password, con.getPath(local,propertiePath), local);
		} catch (DatabaseException e) {
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
			con.writeToXML(con.getPath(local,propertiePath), newDocument);
		} catch (DatabaseException e) {
			return DatabaseStatus.CANNOT_OPEN_DATABASE;
		}
		return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus createElement(Object obj) {
		List<Object> list = new ArrayList<Object>();
		return createElement(list);
	}

	@Override
	public DatabaseStatus createElement(List<Object> obj) {
		if(obj.get(0) instanceof EncryptedFileDob){
		   List<EncryptedFileDob> list = generateFileID((List<EncryptedFileDob>)(Object) obj);
		   FileQuery query = new FileQuery();
		   newDocument = query.createElement(list, newDocument);
		};				
		if(obj.get(0) instanceof EncryptedContainer){
			 List<EncryptedContainer> list = generateContainerID((List<EncryptedContainer>)(Object) obj);
			 ContainerQuery query = new ContainerQuery();
			  newDocument = query.createElement(list, newDocument);
		};					
		if(obj.get(0) instanceof GhostFolderDob){
			 List<GhostFolderDob> list = generateFolderID((List<GhostFolderDob>)(Object) obj);
			 GhostFolderQuery query = new GhostFolderQuery();
			  newDocument = query.createElement(list, newDocument);
		};
		if(obj.get(0) instanceof ShareMember){
			ShareMemberQuery query = new ShareMemberQuery();
			  newDocument = query.createElement((List<ShareMember>)(Object) obj, newDocument);
		};
		if(obj.get(0) instanceof CloudAccount){
			CloudAccountQuery query = new CloudAccountQuery();
			  newDocument = query.createElement((List<CloudAccount>)(Object) obj, newDocument);
		};
		return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus deleteElement(Object obj) {
		List<Object> list = new ArrayList<Object>();
		return deleteElement(list);
	}

	@Override
	public DatabaseStatus deleteElement(List<Object> obj) {
		if(obj.get(0) instanceof EncryptedFileDob){
			   FileQuery query = new FileQuery();
			   newDocument = query.deleteElement((List<EncryptedFileDob>)(Object)obj, newDocument);
			};				
			if(obj.get(0) instanceof EncryptedContainer){
				 ContainerQuery query = new ContainerQuery();
				   newDocument = query.deleteElement((List<EncryptedContainer>)(Object)obj, newDocument);
			};					
			if(obj.get(0) instanceof GhostFolderDob){
				 GhostFolderQuery query = new GhostFolderQuery();
				   newDocument = query.deleteElement((List<GhostFolderDob>)(Object)obj, newDocument);
			};
			if(obj.get(0) instanceof ShareMember){
				ShareMemberQuery query = new ShareMemberQuery();
				   newDocument = query.deleteElement((List<ShareMember>)(Object)obj, newDocument);
			};
			if(obj.get(0) instanceof CloudAccount){
				CloudAccountQuery query = new CloudAccountQuery();
				   newDocument = query.deleteElement((List<CloudAccount>)(Object)obj, newDocument);
			};
			return DatabaseStatus.OK;
	}

	@Override
	public DatabaseStatus updateElement(Object obj) {
		List<Object> list = new ArrayList<Object>();
		return updateElement(list);
	}

	@Override
	public DatabaseStatus updateElement(List<Object> obj) {
		if(obj.get(0) instanceof EncryptedFileDob){
			   FileQuery query = new FileQuery();
			   newDocument = query.updateElement((List<EncryptedFileDob>)(Object)obj, newDocument);};				
			if(obj.get(0) instanceof EncryptedContainer){
				 ContainerQuery query = new ContainerQuery();
				  newDocument = query.updateElement((List<EncryptedContainer>)(Object)obj, newDocument);};					
			if(obj.get(0) instanceof GhostFolderDob){
				 GhostFolderQuery query = new GhostFolderQuery();
				  newDocument = query.updateElement((List<GhostFolderDob>)(Object)obj, newDocument);};
			if(obj.get(0) instanceof ShareMember){
				ShareMemberQuery query = new ShareMemberQuery();
				  newDocument = query.updateElement((List<ShareMember>)(Object)obj, newDocument);};
			if(obj.get(0) instanceof CloudAccount){
				CloudAccountQuery query = new CloudAccountQuery();
				  newDocument = query.updateElement((List<CloudAccount>)(Object)obj, newDocument);};
			return DatabaseStatus.OK;
	}

	@Override
	public List<Object> getElement(Object type, String argument, String value) {
		if(type instanceof EncryptedFileDob){
			   FileQuery query = new FileQuery();
			   return  (List<Object>)(EncryptedFileDob) query.getElement(argument, value, newDocument);}
			if(type instanceof EncryptedContainer){
				 ContainerQuery query = new ContainerQuery();
				  return  (List<Object>)(EncryptedContainer) query.getElement(argument, value, newDocument);}			
			if(type instanceof GhostFolderDob){
				 GhostFolderQuery query = new GhostFolderQuery();
				  return  (List<Object>)(GhostFolderDob) query.getElement(argument, value, newDocument);}
			if(type instanceof ShareMember){
				ShareMemberQuery query = new ShareMemberQuery();
				  return  (List<Object>)(ShareMember) query.getElement(argument, value, newDocument);}
			if(type instanceof CloudAccount){
				CloudAccountQuery query = new CloudAccountQuery();
				  return  (List<Object>)(CloudAccount) query.getElement(argument, value, newDocument);}
			return null;
	}

	@Override
	public List<Object> getElementAll(Object type) {
		if(type instanceof EncryptedFileDob){
			   FileQuery query = new FileQuery();
			   return  (List<Object>)(EncryptedFileDob) query.getElementAll(newDocument);}
			if(type instanceof EncryptedContainer){
				 ContainerQuery query = new ContainerQuery();
				  return  (List<Object>)(EncryptedContainer) query.getElementAll(newDocument);}		
			if(type instanceof GhostFolderDob){
				 GhostFolderQuery query = new GhostFolderQuery();
				  return  (List<Object>)(GhostFolderDob) query.getElementAll(newDocument);}
			if(type instanceof ShareMember){
				ShareMemberQuery query = new ShareMemberQuery();
				  return  (List<Object>)(ShareMember) query.getElementAll(newDocument);}
			if(type instanceof CloudAccount){
				CloudAccountQuery query = new CloudAccountQuery();
				  return  (List<Object>)(CloudAccount) query.getElementAll(newDocument);}
			return null;
	}

	@Override
	public List<Object> getElementByParent(Object type, GhostFolderDob folder) {
		if(type instanceof EncryptedFileDob){
			   FileQuery query = new FileQuery();
			   return  (List<Object>)(EncryptedFileDob) query.getElementByParent(folder, newDocument);}
			if(type instanceof EncryptedContainer){
				 ContainerQuery query = new ContainerQuery();
				  return  null;}		
			if(type instanceof GhostFolderDob){
				 GhostFolderQuery query = new GhostFolderQuery();
				  return  (List<Object>)(GhostFolderDob) query.getElementByParent(folder, newDocument);}
			if(type instanceof ShareMember){
				ShareMemberQuery query = new ShareMemberQuery();
				  return  null;}
			if(type instanceof CloudAccount){
				CloudAccountQuery query = new CloudAccountQuery();
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
			createElement(filedob);
			createElement(shareRelation);
			createElement(encryptedContainer);
	}
	
}
