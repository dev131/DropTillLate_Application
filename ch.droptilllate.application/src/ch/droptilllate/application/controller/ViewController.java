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

import ch.droptilllate.application.com.CRUDCryptedFileResult;
import ch.droptilllate.application.com.FileHandler;
import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.com.IFileSystemCom;
import ch.droptilllate.application.com.IXmlDatabase;
import ch.droptilllate.application.core.KeyManager;
import ch.droptilllate.application.core.ShareManager;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.EncryptedFolderDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.dnb.EncryptedFile;
import ch.droptilllate.application.dnb.GhostFolder;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.listener.TreeDragSourceListener;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.provider.DropTillLateContentProvider;
import ch.droptilllate.application.provider.DropTillLateLabelProvider;
import ch.droptilllate.application.views.FileNameDialog;
import ch.droptilllate.application.views.LoginView;
import ch.droptilllate.application.views.Messages;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.application.views.TableIdentifier;
import ch.droptilllate.filesystem.info.FileInfo;

public class ViewController {
	private Tree tree;
	private TreeViewer viewer;
	private GhostFolderDob root;
	private DroppedElement elementCurrent;
	private List<EncryptedFileDob> fileList;
	private List<GhostFolderDob> folderList;
	private static ViewController instance = null;
	private List<EncryptedFileDob> actualDropFiles;
	private String password = "";
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
		// LOGIN AREA
		// Register Password

		// TODO init password
		KeyManager km = new KeyManager();
		if (!km.checkMasterPasswordExisting()) {
			dialog = new LoginView(shell, Messages.getCreatePassword());
			dialog.create();
			if (dialog.open() == Window.OK) {
				password = dialog.getPassword();
				km.initPassword(password, Messages.getSaltMasterPassword());
			}
		} else {
			while (!km.checkPassword(password, Messages.getSaltMasterPassword(),0)) {
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
	private GhostFolderDob getInitialInput() {
		// Create RootDob
		root = new GhostFolderDob(0, "Root-Folder", new Date(
				System.currentTimeMillis()), "");
		getFolderContent(root);
		return root;
	}

	/**
	 * Needed by getInitialInput() to fill all (sub)folders with files.
	 */
	private void getFolderContent(GhostFolderDob folder) {
		IXmlDatabase encryptedFolderDao = new EncryptedFolderDao();
		IXmlDatabase encryptedFileDao = new EncryptedFileDao();
		folder.addFiles(((EncryptedFileDao) encryptedFileDao)
				.getFilesInFolder(folder));
		List<GhostFolderDob> childFolders = ((EncryptedFolderDao) encryptedFolderDao)
				.getFoldersInFolder(folder);
		if (childFolders != null && childFolders.size() > 0) {
			folder.addFolders(childFolders);
			for (GhostFolderDob childFolder : childFolders) {
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
		folderList = new ArrayList<GhostFolderDob>();
		// List inserts and prepare for delete
		TreeSelection currentSelection = (TreeSelection) viewer.getSelection();
		//FIll selection in lists
		EncryptedFileDob fileToDelete= null;
		GhostFolderDob folderToDelete=null;
		Iterator<DroppedElement> droppedElementsIterator = currentSelection.iterator();
		while (droppedElementsIterator.hasNext()) {
			DroppedElement element = droppedElementsIterator.next();			
			if (element instanceof GhostFolderDob){
				 folderToDelete = (GhostFolderDob) element;
				folderList.add(folderToDelete);}
			if (element instanceof EncryptedFileDob){
				fileToDelete = (EncryptedFileDob) element;
				fileList.add(fileToDelete);}
		}
		// Delete on Filesystem
		FileHandler fileHandler = new FileHandler();
		// TODO Check successlist
		CRUDCryptedFileResult result = fileHandler.deleteFilesOnFileSystem(fileList);
		for(EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()){
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> successfully deleted");
		}
		//Delete on DB
		fileHandler.deleteFilesOnDatabase(result.getEncryptedFileListSuccess());
		// Check Folder
		for(EncryptedFileDob fileDob : result.getEncryptedFileListError()){
			for(GhostFolderDob folderDob : folderList){
				if(folderDob.getId() == fileDob.getParent().getId()){
					folderList.remove(folderDob);
				}
			}
		}
		fileHandler.deleteFolderOnDatabase(folderList);
		deleteTreeFiles(fileList);
		deleteTreeFolders(folderList);
		
	}

	private void deleteTreeFolders(List<GhostFolderDob> folderList2) {
			for (GhostFolderDob nextFolderToDelete : folderList2) {
				nextFolderToDelete.getParent().removeFolder(nextFolderToDelete);
			}		
	}

	private void deleteTreeFiles(List<EncryptedFileDob> fileList2) {
		for (EncryptedFileDob nextFileToDelete : fileList2) {
			nextFileToDelete.getParent().removeFile(nextFileToDelete);
		}
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
				if (obj.getClass() == GhostFolderDob.class) {
					// do Nothing
				}
				// IF selection a File
				if (obj.getClass() == EncryptedFileDob.class) {
					EncryptedFileDob f2 = (EncryptedFileDob) obj;
					fileList.add(f2);
				}
			}
		}
		// TODO check succesfull list
		FileHandler fileHandler = new FileHandler();
		CRUDCryptedFileResult result = fileHandler.decryptFiles(fileList, Messages.getTempFolder());
		for(EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()){
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> decryption worked");
		}
		for(EncryptedFileDob fileDob : result.getEncryptedFileListError()){
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> decryption not worked");
		}
	}

	/**
	 * Insert new Folder
	 * 
	 * @param name
	 */
	public void newFolder(String name) {
		IXmlDatabase folderDao = new EncryptedFolderDao();
		File newFile = new File(name);
		GhostFolder newfolder = new GhostFolder(null,newFile, root);
		FileHandler fileHandler = new FileHandler();
		// insert in DB and Treeview
		root.addFolder(fileHandler.newFolderDBEntry(newfolder));
	}

	/**
	 * DropElements dragOverFolder = ParentFolder
	 * Insert new DroppedFiles
	 * @param droppedFileInformation
	 * @param dragOverFolder
	 */
	public void encryptDropElements(String[] droppedFileInformation,
			GhostFolderDob dragOverFolder) {
		actualDropFiles = new ArrayList<EncryptedFileDob>();

		// insert DAO and update VIEW
		for (String currentDroppedElement : droppedFileInformation) {
			dropTreeElements(new File(currentDroppedElement), dragOverFolder);
		}
		
		// TODO Insert in Filesystem and Error handling Results
		FileHandler fileHandler = new FileHandler();
		CRUDCryptedFileResult result = fileHandler.encryptFiles(
				actualDropFiles, Messages.getLocalPathDropBoxMaster());
		//Update DB
		for(EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()){
			fileHandler.updateFileOnDatabase(fileDob);
			EncryptedContainer container = new EncryptedContainer(fileDob.getContainerID(),0);
			fileHandler.newContainer(container);
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> encryption worked");
		}
		for(EncryptedFileDob fileDob: result.getEncryptedFileListError()){
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> encryption not worked");
		}
		//Delete Error Files on DB
		fileHandler.deleteFilesOnDatabase(result.getEncryptedFileListError());
			//TODO maybe dropped element Delete
			// droppedElement.delete();
			// droppedFile.setPath(Messages.getLocalPathDropbox());
	}

	/**
	 * Insert Node into treeview
	 * @param droppedElement
	 * @param parent
	 */
	private void dropTreeElements(File droppedElement, GhostFolderDob parent) {
		if (!droppedElement.isDirectory()) {
			if (droppedElement.getName().contains(".DS_Store")) {
				// TODO Message
				// log.info(".ds_store File ignored!");
				return;
			}
			EncryptedFile droppedFile = new EncryptedFile(null,droppedElement,
					parent);
			// Insert new Node in DB
			FileHandler fileHandler = new FileHandler();
			EncryptedFileDob encryptedPersistedFile = fileHandler
					.newFileDBEntry(droppedFile);
			parent.addFile(encryptedPersistedFile);
			// add to list
			actualDropFiles.add(encryptedPersistedFile);
		} else {
			GhostFolder droppedFolder = new GhostFolder(null,droppedElement,parent);
			FileHandler fileHandler = new FileHandler();
			GhostFolderDob encryptedPersistedFolder = fileHandler
					.newFolderDBEntry(droppedFolder);
			parent.addFolder(encryptedPersistedFolder);
			for (File file : droppedElement.listFiles()) {
				dropTreeElements(file, encryptedPersistedFolder);
			}
			// boolean success = droppedElement.delete();
			// log.debug("Sourcefolder deleted");
		}

	}

	/**
	 * ShareFiles
	 */
	public void shareFiles() {
		fileList = new ArrayList<EncryptedFileDob>();
		String password = "";
		dialog = new LoginView(shell, Messages.getCreatePassword());
		dialog.create();
		if (dialog.open() == Window.OK) {
			password = dialog.getPassword();
		}
		// TODO Statusline
		List<EncryptedFileDob> fileList = new ArrayList<EncryptedFileDob>();
		// List inserts and prepare for delete
		TreeItem[] treeItems = tree.getSelection();
		if (treeItems != null) {
			for (int i = 0; i < treeItems.length; i++) {
				Object obj = treeItems[i].getData();
				// IF selection a Folder
				if (obj.getClass() == GhostFolderDob.class) {
					// do Nothing
				}
				// IF selection a File
				if (obj.getClass() == EncryptedFileDob.class) {
					EncryptedFileDob f2 = (EncryptedFileDob) obj;
					fileList.add(f2);
				}
			}
		}
		ShareManager shareManager = new ShareManager();
		shareManager.newShareRelation(fileList, password);

	}
}
