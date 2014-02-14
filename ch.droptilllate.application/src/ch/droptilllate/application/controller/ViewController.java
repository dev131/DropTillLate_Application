package ch.droptilllate.application.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.xml.sax.SAXException;

import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.core.KeyManager;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.EncryptedFolderDao;
import ch.droptilllate.application.dnb.Container;
import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.dnb.EncryptedFile;
import ch.droptilllate.application.dnb.EncryptedFolder;
import ch.droptilllate.application.listener.TreeDragSourceListener;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.EncryptedFolderDob;
import ch.droptilllate.application.provider.DropTillLateContentProvider;
import ch.droptilllate.application.provider.DropTillLateLabelProvider;
import ch.droptilllate.application.views.FileNameDialog;
import ch.droptilllate.application.views.LoginView;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.application.views.TableIdentifier;
import ch.droptilllate.filesystem.api.FileInfo;

public class ViewController {
	private Tree tree;
	private TreeViewer viewer;
	private EncryptedFolderDob root;
	private DroppedElement elementCurrent;
	private List<EncryptedFileDob> fileList;
	private List<EncryptedFolderDob> folderList;
	private static ViewController instance = null;
	private List<EncryptedFileDob> actualDropFiles;
	private String password ="";
	private LoginView dialog;
	private Shell shell;
	
	public ViewController() {
		// Exists only to defeat instantiation.
	}

	public static ViewController getInstance() {
		if (instance == null) {
			instance = new ViewController();
		}
		return instance;
	}

	public void initViewController(TreeViewer viewer, Shell shell) {
		// Treeviewer
		this.viewer = viewer;
		this.shell = shell;
		// Set ContentProvider and Labels
		viewer.setContentProvider(new DropTillLateContentProvider());
		viewer.setLabelProvider(new DropTillLateLabelProvider());
		// Expand the tree
		viewer.setAutoExpandLevel(2);
		// Change TreeTable
		tree = viewer.getTree();
		// Tree table specific code starts fill labels
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		for (TableIdentifier identifier : TableIdentifier.values()) {
			new TreeColumn(tree, SWT.NONE).setText(Messages
					.getTableColumnTitle(identifier));
			tree.getColumn(identifier.ordinal()).setWidth(
					identifier.columnWidth);
		}
		//LOGIN AREA
		//Register Password

		//TODO init password
		KeyManager km = new KeyManager();
		if(!km.checkMasterPassword()){ 
			 dialog = new LoginView(shell, Messages.getCreatePassword());
				dialog.create();
				if (dialog.open() == Window.OK) {
				  password = dialog.getPassword();	  			
				  km.initPassword(password);
				}
		}
		else{
			while(!km.checkPassword(password)){
				 dialog = new LoginView(shell, Messages.getLoginPassword());
					dialog.create();
					if (dialog.open() == Window.OK) {
					  password = dialog.getPassword();	  
					} 
			}
		}
		System.out.println("Successfull login");	
		// Get InitialInput
		root = getInitialInput();
		viewer.setInput(root);
		viewer.expandToLevel(1);
		// Register Drag&Drop Listener
		registerDragDrop();
	
	}

	/**
	 * Register DragDropAdapter
	 */
	private void registerDragDrop() {
		// Drag-Part //////////////////////////////////////////////////////////
		DragSource source = new DragSource(tree, DND.DROP_COPY | DND.DROP_MOVE);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new TreeDragSourceListener(tree));

		// Drop-Part //////////////////////////////////////////////////////////
		DropTarget dropTarget = new DropTarget(tree, DND.DROP_COPY
				| DND.DROP_DEFAULT);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance(),
				FileTransfer.getInstance() });
		dropTarget.addDropListener(new TreeDropTargetAdapter(viewer, root));
	}

	/**
	 * Returns the initial input for the table, right after application start.
	 */
	private EncryptedFolderDob getInitialInput() {
		// Create RootDob
		root = new EncryptedFolderDob(0, "Root-Folder", new Date(
				System.currentTimeMillis()), "");
		getFolderContent(root);
		return root;
	}

	/**
	 * Needed by getInitialInput() to fill all (sub)folders with files.
	 */
	private void getFolderContent(EncryptedFolderDob folder) {
		IXmlDatabase encryptedFolderDao = new EncryptedFolderDao();
		IXmlDatabase encryptedFileDao = new EncryptedFileDao();
		folder.addFiles(((EncryptedFileDao) encryptedFileDao).getFilesForFolder(folder));
		List<EncryptedFolderDob> childFolders = ((EncryptedFolderDao) encryptedFolderDao)
				.getFoldersForFolder(folder);
		if (childFolders != null && childFolders.size() > 0) {
			folder.addFolders(childFolders);
			for (EncryptedFolderDob childFolder : childFolders) {
				getFolderContent(childFolder);
			}
		}
	}

	/**
	 * Selection has changed in EncryptedView
	 * 
	 * @param event
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection currentSelection = (IStructuredSelection) event
				.getSelection();
		// activate exand/collapse children actions if an element is
		// selected and the element is a folder
		for (Iterator<?> iteratorCurrent = currentSelection.iterator(); iteratorCurrent
				.hasNext();) {
			elementCurrent = (DroppedElement) iteratorCurrent.next();
		}
	}

	/**
	 * Delete File
	 */
	public void deleteFile() {
		fileList = new ArrayList<EncryptedFileDob>();
		folderList = new ArrayList<EncryptedFolderDob>();
		// List inserts and prepare for delete
		TreeSelection currentSelection = (TreeSelection) viewer.getSelection();
		// DELETE IN TREE
		Iterator<DroppedElement> droppedElementsIterator = currentSelection
				.iterator();
		while (droppedElementsIterator.hasNext()) {
			deleteTreeElement(droppedElementsIterator.next(), true);
		}
		IXmlDatabase folderDao = new EncryptedFolderDao();
		IXmlDatabase fileDao = new EncryptedFileDao();
		// Send to Dao
		folderDao.deleteElement(folderList);
		fileDao.deleteElement(fileList);
		//Delete on Filesystem
		FileSystemCom ifileSystem = new FileSystemCom();
		ifileSystem.deleteFile(fileList);
		
	}

	/**
	 * Decrypt selectedFile
	 */
	public void decrypteFile() {
		// TODO Statusline
		List<EncryptedFileDob> fileList = new ArrayList<EncryptedFileDob>();
		// List inserts and prepare for delete
		TreeItem[] treeItems = tree.getSelection();
		if (treeItems != null) {
			for (int i = 0; i < treeItems.length; i++) {
				Object obj = treeItems[i].getData();
				// IF selection a Folder
				if (obj.getClass() == EncryptedFolderDob.class) {
					// do Nothing
				}
				// IF selection a File
				if (obj.getClass() == EncryptedFileDob.class) {
					EncryptedFileDob f2 = (EncryptedFileDob) obj;
					fileList.add(f2);
				}
			}
		}
		FileSystemCom ifileSystem = new FileSystemCom();
		ifileSystem.decryptFile(fileList, Messages.getTempFolder());

	}

	/**
	 * Insert new Folder
	 * 
	 * @param name
	 */
	public void newFolder(String name) {
		IXmlDatabase folderDao = new EncryptedFolderDao();
		File newFile = new File(name);
		EncryptedFolder newfolder = new EncryptedFolder(newFile, root);
	
		EncryptedFolderDob encryptedPersistedFolder = 	(EncryptedFolderDob) folderDao.newElement(newfolder);
		root.addFolder(encryptedPersistedFolder);
		folderDao = new EncryptedFolderDao();
		folderDao.newElement(newfolder);
	}

	/**
	 * Delete Element in treeview
	 * 
	 * @param element
	 * DroppedElement
	 * @param rootFile
	 */
	private void deleteTreeElement(DroppedElement element, boolean rootFile) {
		if (element instanceof EncryptedFileDob) {
			EncryptedFileDob fileToDelete = (EncryptedFileDob) element;
			boolean success = true;
			if (success) {
				// element.getParent().removeFile(fileToDelete);
				fileList.add(fileToDelete);
				// // this.encryptedFileDao.deleteFile(fileToDelete);
				// // log.debug("database entry deleted");
				if (rootFile)
					(element).getParent().removeFile(fileToDelete);
			}
		} else if (element instanceof EncryptedFolderDob) {
			EncryptedFolderDob folderToDelete = (EncryptedFolderDob) element;
			for (EncryptedFolderDob nextFolderToDecrypt : folderToDelete
					.getFolders()) {
				deleteTreeElement(nextFolderToDecrypt, false);
			}
			for (EncryptedFileDob nextFileToDecrypt : folderToDelete.getFiles()) {
				deleteTreeElement(nextFileToDecrypt, false);
			}
			folderList.add(folderToDelete);
			// encryptedFolderDao.deleteFolder(folderToDelete);
			// log.debug("database entry deleted");
			if (rootFile)
				folderToDelete.getParent().removeFolder(folderToDelete);
		}
	}

	/**
	 * DropElements dragOverFolder = ParentFolder
	 * 
	 * @param droppedFileInformation
	 * @param dragOverFolder
	 */
	public void dropElements(String[] droppedFileInformation,
			EncryptedFolderDob dragOverFolder) {

		actualDropFiles = new ArrayList<EncryptedFileDob>();
		for (String currentDroppedElement : droppedFileInformation) {
			dropTreeElements(new File(currentDroppedElement),
					dragOverFolder);
		}
		// TODO delete file and set new Path
		FileSystemCom ifileSystem = new FileSystemCom();
		List<FileInfo> result = ifileSystem.encryptFile(actualDropFiles,
				Messages.getLocalPathDropbox());
		if (result == null) {
			// TODO MESSAGE
			System.out.println("some Files are not stored");
		} else {
			updateTreeElements(result, actualDropFiles);
			updateContainerTable(result);
			// droppedElement.delete();
			// droppedFile.setPath(Messages.getLocalPathDropbox());
		}

	}

	private void updateContainerTable(List<FileInfo> result) {
		for(int index = 0; index< result.size(); index++){
			ContainerDao dao = new ContainerDao();
			//All in mastersharefolder
			Container container = new Container(result.get(index).getContainerInfo().getContainerID(), 0);
			dao.newElement(container);
			
		}
		
	}

	private void updateTreeElements(List<FileInfo> result, List<EncryptedFileDob> droppedFiles) {
		for(int index = 0; index< droppedFiles.size(); index++){
			droppedFiles.get(index).setPath(result.get(index).getContainerInfo().getParentContainerPath());
			droppedFiles.get(index).setContainerID(result.get(index).getContainerInfo().getContainerID());
			IXmlDatabase encryptedFileDao = new EncryptedFileDao();
			encryptedFileDao.updateElement(droppedFiles.get(index));
		}
	}

	private void dropTreeElements(File droppedElement, EncryptedFolderDob parent) {
		IXmlDatabase encryptedFolderDao = new EncryptedFolderDao();
		IXmlDatabase encryptedFileDao = new EncryptedFileDao();
		List<EncryptedFolderDob> successfullyProcessedFolders = new ArrayList<EncryptedFolderDob>();
		List<EncryptedFileDob> successfullyProcessedFiles = new ArrayList<EncryptedFileDob>();
		
		if (!droppedElement.isDirectory()) {
			if (droppedElement.getName().contains(".DS_Store")) {
				// TODO Message
				// log.info(".ds_store File ignored!");
				return;
			}
			EncryptedFile droppedFile = new EncryptedFile(droppedElement,parent);
			// Insert new Node in DB
			EncryptedFileDob encryptedPersistedFile = (EncryptedFileDob) encryptedFileDao.newElement(droppedFile);
			parent.addFile(encryptedPersistedFile);
			// add to list
			actualDropFiles.add(encryptedPersistedFile);
		} else {
			EncryptedFolder droppedFolder = new EncryptedFolder(droppedElement,parent);
			EncryptedFolderDob encryptedPersistedFolder = (EncryptedFolderDob) encryptedFolderDao.newElement(droppedFolder);
			parent.addFolder(encryptedPersistedFolder);
			//persistedNonDeletedFolders.add(encryptedPersistedFolder);
			// log.debug("Folder persisted");
			for (File file : droppedElement.listFiles()) {
				dropTreeElements(file, encryptedPersistedFolder);
			}
			//boolean success = droppedElement.delete();
			// log.debug("Sourcefolder deleted");
		}

	}
}
