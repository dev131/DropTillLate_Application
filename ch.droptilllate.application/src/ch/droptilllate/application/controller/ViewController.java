package ch.droptilllate.application.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import ch.droptilllate.application.com.CloudDropboxCom;
import ch.droptilllate.application.com.FileSystemCom;
import ch.droptilllate.application.dao.CloudAccountDao;
import ch.droptilllate.application.dao.ContainerDao;
import ch.droptilllate.application.dao.EncryptedFileDao;
import ch.droptilllate.application.dao.GhostFolderDao;
import ch.droptilllate.application.dao.ShareFolderDao;
import ch.droptilllate.application.dao.ShareRelationDao;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.dnb.EncryptedContainer;
import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.dnb.ShareFolder;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.info.CRUDCryptedFileInfo;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.info.SuccessMessage;
import ch.droptilllate.application.key.KeyManager;
import ch.droptilllate.application.listener.TreeDragSourceListener;
import ch.droptilllate.application.listener.TreeDropTargetAdapter;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Configuration;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.application.provider.DropTillLateContentProvider;
import ch.droptilllate.application.provider.DropTillLateLabelProvider;
import ch.droptilllate.application.share.ShareManager;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.application.views.TableIdentifier;
import ch.droptilllate.cloudprovider.dropbox.DropboxFolderSharer;
import ch.droptilllate.cloudprovider.error.CloudError;
import ch.droptilllate.couldprovider.api.ICloudProviderCom;
import ch.droptilllate.couldprovider.api.IFileSystemCom;
import ch.droptilllate.application.views.ShareView;

public class ViewController {
	private EModelService modelService;
	private MApplication application;
	private EPartService partService;
	
	private Tree tree;
	private TreeViewer viewer;
	private GhostFolderDob root;
	private DroppedElement elementCurrent;
	private List<EncryptedFileDob> fileList;
	private List<GhostFolderDob> folderList;
	private static ViewController instance = null;
	private List<EncryptedFileDob> actualDropFiles;
	private Shell shell;
	private ShareManager shareManager;

	public ViewController() {
		// Exists only to defeat instantiation.
	}

	public static ViewController getInstance() {
		if (instance == null) {
			instance = new ViewController();
		}
		return instance;
	}

	public void initViewController(TreeViewer viewer, Shell shell, EPartService partService, EModelService modelService, MApplication application) {
		// Treeviewer
		this.viewer = viewer;
		this.shell = shell;
		this.partService = partService;
		this.modelService = modelService;
		this.application = application;
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
		
	}

	public void initController() {
		// Get InitialInput
		root = getInitialInput();
		viewer.setInput(root);
		viewer.expandToLevel(1);
		// Register Drag&Drop Listener
		registerDragDrop();
		
		//checkCloudAccount();
		
	}

	private void checkCloudAccount() {
		CloudAccountDao dao = new CloudAccountDao();
		CloudAccount account = (CloudAccount) dao.getElementAll(null);
		ICloudProviderCom com = new CloudDropboxCom();
		CloudError status = com.testCloudAccount(account.getUsername(), account.getPassword());
		if(status != CloudError.NONE){
			new ErrorMessage(shell, "Error", status.getMessage());
			  MHandledToolItem newFolderHandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledmenuitem.ShareFiles", application);
				newFolderHandler.setVisible(false);		
		}		
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
	public GhostFolderDob getInitialInput() {
		// Create RootDob
		// Integer id, String name, Date date, String path,GhostFolderDob
		// parent) {
		root = new GhostFolderDob(0, "Root-Folder", null);
		getFolderContent(root);
		return root;
	}

	/**
	 * Needed by getInitialInput() to fill all (sub)folders with files.
	 */
	private void getFolderContent(GhostFolderDob folder) {
		GhostFolderDao encryptedFolderDao = new GhostFolderDao();
		EncryptedFileDao encryptedFileDao = new EncryptedFileDao();
		folder.addFiles((encryptedFileDao).getFilesInFolder(folder, null));
		List<GhostFolderDob> childFolders = (encryptedFolderDao)
				.getFoldersInFolder(folder, null);
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
		// FIll selection in lists
		EncryptedFileDob fileToDelete = null;
		GhostFolderDob folderToDelete = null;
		Iterator<DroppedElement> droppedElementsIterator = currentSelection
				.iterator();
		while (droppedElementsIterator.hasNext()) {
			DroppedElement element = droppedElementsIterator.next();
			if (element instanceof GhostFolderDob) {
				folderToDelete = (GhostFolderDob) element;
				folderList.add(folderToDelete);
			}
			if (element instanceof EncryptedFileDob) {
				fileToDelete = (EncryptedFileDob) element;
				fileList.add(fileToDelete);
			}
		}
		// Insert subfolder/files
		getEntriesInFolders();
		// Delete on Filesystem
		IFileSystemCom com = FileSystemCom.getInstance();
		// Check successlist
		CRUDCryptedFileInfo result = com.deleteFile(fileList);
		for (EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()) {
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> successfully deleted");
		}
		// Delete on DB
		EncryptedFileDao fileDB = new EncryptedFileDao();
		fileDB.deleteElement(result.getEncryptedFileListSuccess(), null);
		// Check Folder
		for (EncryptedFileDob fileDob : result.getEncryptedFileListError()) {
			for (GhostFolderDob folderDob : folderList) {
				if (folderDob.getId() == fileDob.getParent().getId()) {
					folderList.remove(folderDob);
				}
			}
		}
		GhostFolderDao folderDb = new GhostFolderDao();
		folderDb.deleteElement(folderList, null);
		deleteTreeFiles(fileList);
		deleteTreeFolders(folderList);
		viewer.refresh();
	}

	private void getEntriesInFolders() {
		GhostFolderDao encryptedFolderDao = new GhostFolderDao();
		EncryptedFileDao encryptedFileDao = new EncryptedFileDao();
		List<EncryptedFileDob> tmpfilelist = new ArrayList<EncryptedFileDob>();
		List<GhostFolderDob> tmpfolderlist = new ArrayList<GhostFolderDob>();
		for (GhostFolderDob folder : folderList) {
			tmpfolderlist.addAll(encryptedFolderDao.getFoldersInFolder(folder,
					null));
		}
		for (GhostFolderDob folder : folderList) {
			tmpfilelist.addAll(encryptedFileDao.getFilesInFolder(folder, null));
		}
		folderList.addAll(tmpfolderlist);
		fileList.addAll(tmpfilelist);
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
		List<EncryptedFileDob> fileList = getSelectedFileList();

		// check succesfull list
		IFileSystemCom iFileSystem = FileSystemCom.getInstance();
		CRUDCryptedFileInfo result = iFileSystem.decryptFile(fileList);
		for (EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()) {
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> decryption worked");
			File file = new File(Configuration.getPropertieTempPath(true)
					+ fileDob.getId() + "." + fileDob.getType());
			FileHandler fileHanlder = new FileHandler();
			fileHanlder.setFileListener(file, fileDob);
			try {
				Desktop.getDesktop().edit(file);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (EncryptedFileDob fileDob : result.getEncryptedFileListError()) {
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> decryption not worked");
		}
	}

	private List<EncryptedFileDob> getSelectedFileList() {
		List<EncryptedFileDob> fileList = new ArrayList<EncryptedFileDob>();
		TreeItem[] treeItems = tree.getSelection();
		if (treeItems != null) {
			for (int i = 0; i < treeItems.length; i++) {
				Object obj = treeItems[i].getData();
				// IF selection a Folder
				if (obj.getClass() == GhostFolderDob.class) {
					// TODO fill with file in ordner entries
				}
				// IF selection a File
				if (obj.getClass() == EncryptedFileDob.class) {
					EncryptedFileDob f2 = (EncryptedFileDob) obj;
					fileList.add(f2);
				}
			}
		}
		return fileList;
	}

	/**
	 * Insert new Folder
	 * 
	 * @param name
	 */
	public void newFolder(String name) {
		GhostFolderDao folderDao = new GhostFolderDao();
		File newFile = new File(name);
		GhostFolderDob dob = new GhostFolderDob(null, newFile.getName(), root);
		// insert in DB and Treeview
		root.addFolder((GhostFolderDob) folderDao.newElement(dob, null));
	}

	/**
	 * DropElements dragOverFolder = ParentFolder Insert new DroppedFiles
	 * 
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

		// Insert in Filesystem and Error handling Results
		IFileSystemCom fileSystem = FileSystemCom.getInstance();
		ShareFolder shareFolder = new ShareFolder(Messages.getIdSize(), null);
		CRUDCryptedFileInfo result = fileSystem.encryptFile(actualDropFiles,
				shareFolder);
		// Update DB
		EncryptedFileDao fileDB = new EncryptedFileDao();
		for (EncryptedFileDob fileDob : result.getEncryptedFileListSuccess()) {
			fileDB.updateElement(fileDob, null);
			EncryptedContainer container = new EncryptedContainer(
					fileDob.getContainerId(), Messages.getIdSize());
			ContainerDao containerDB = new ContainerDao();
			containerDB.newElement(container, null);
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> encryption worked");
		}
		for (EncryptedFileDob fileDob : result.getEncryptedFileListError()) {
			Status status = Status.getInstance();
			status.setMessage(fileDob.getName() + " -> encryption not worked");
		}
		// Delete Error Files on DB
		fileDB.deleteElement(result.getEncryptedFileListError(), null);
		// TODO maybe delete dropped element Delete
		// droppedElement.delete();
		// droppedFile.setPath(Messages.getLocalPathDropbox());
	}

	/**
	 * Insert Node into treeview and DB
	 * 
	 * @param droppedElement
	 * @param parent
	 */
	private void dropTreeElements(File droppedElement, GhostFolderDob parent) {
		if (!droppedElement.isDirectory()) {
			if (droppedElement.getName().contains(".DS_Store")) {
				return;
			}
			// Integer id, String name, Date date, String path, GhostFolderDob
			// parent, String type, Long size, Integer containerId
			EncryptedFileDob fileDob = new EncryptedFileDob(null,
					droppedElement.getName(), new Date(
							System.currentTimeMillis()),
					droppedElement.getPath(), parent, droppedElement.length(),
					null);
			// Insert new Node in DB
			EncryptedFileDao fileDB = new EncryptedFileDao();
			EncryptedFileDob encryptedPersistedFile = (EncryptedFileDob) fileDB
					.newElement(fileDob, null);
			parent.addFile(encryptedPersistedFile);
			// add to list
			actualDropFiles.add(encryptedPersistedFile);
		} else {
			// Integer id, String name, Date date, String path, GhostFolderDob
			// parent
			GhostFolderDob folderDob = new GhostFolderDob(null,
					droppedElement.getName(), parent);
			GhostFolderDao folderDB = new GhostFolderDao();
			GhostFolderDob encryptedPersistedFolder = (GhostFolderDob) folderDB
					.newElement(folderDob, null);
			parent.addFolder(encryptedPersistedFolder);
			for (File file : droppedElement.listFiles()) {
				dropTreeElements(file, encryptedPersistedFolder);
			}
			// boolean success = droppedElement.delete();
			// log.debug("Sourcefolder deleted");
		}

	}

	/**
	 * Open Share Part perspective
	 */
	public void openShareContext() {
		fileList = new ArrayList<EncryptedFileDob>();
		List<EncryptedFileDob> fileList = getSelectedFileList();
		ShareView.getInstance().setInitialTree(
				(ArrayList<EncryptedFileDob>) fileList);
		ShareView.getInstance().setInitialInputMailList();
	}

	/**
	 * ShareFiles Return true if oke Return false for Manually
	 * @param mailList
	 * @param fileList
	 * @param password
	 * @return
	 */
	public boolean shareFiles(ArrayList<String> mailList,
			ArrayList<EncryptedFileDob> fileList, String password, boolean auto) {
		ShareFolder shareFolder = null;
		CloudError status = CloudError.NONE;
		shareManager = new ShareManager(fileList, password,
				mailList);
		if (shareManager.getSTATUS() == 0) {
			// ERROR
		}
		if (shareManager.getSTATUS() == 1) {
			// CREATE
			// Create and insert newShareFolder in DB and create Id
			ShareFolderDao shareDao = new ShareFolderDao();
			shareFolder = new ShareFolder(null, null);
			shareFolder = (ShareFolder) shareDao.newElement(shareFolder, null);
			shareFolder.setKey(password);
			shareDao.updateElement(shareFolder, null);
			shareFolder = shareManager.createNewSharedFolder(fileList,
					password, shareFolder);
			shareManager.insertShareRelation(shareFolder, mailList);
			shareManager.createUpdateFiles(shareFolder);
			//Share file Automatically
			if(!auto){
				status = shareFileToCloudManually(shareFolder, mailList, false);
			}
			else{
				status = shareFileToCloudAutomatically(shareFolder, mailList);
			}
		
		}
		if (shareManager.getSTATUS() == 2) {
			// USING EXISTING
			shareFolder = shareManager.useExistingSharedFolder(fileList,
					password);
			new SuccessMessage(shell, "MESSAGE", "Shared ->  password = "
					+ shareFolder.getKey());
			status = shareFileToCloudManually(shareFolder, mailList, false);
		}
			// TODO ERROR sharing
		if (status == CloudError.NONE) {
				// NO ERROR OCCURED
				// TODO JUST FOR TESTS
				FileHandler fileHandler = new FileHandler();
				File source = new File(Configuration.getPropertieTempPath(true)
						+ XMLConstruct.NameShareXML);
				File dest = new File(Configuration.getPropertieTempPath(true)
						+ XMLConstruct.NameShareXML);
				try {
					fileHandler.copyFile(source, dest);
					fileHandler.delete(source);
					// fileHandler.delete(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
				new SuccessMessage(shell, "Success", "shared");
				return true;
			} else {
				// ERROR ocured
				new ErrorMessage(shell, "ERROR", status.getMessage() +"  Share error -> Check files or Try Manually");
				return false;
			}
	}

	/**
	 * ShareFileAutomatically
	 * @param shareFolder
	 * @param mailList
	 * @return
	 */
	private CloudError shareFileToCloudAutomatically(ShareFolder shareFolder, ArrayList<String> mailList) {
		ICloudProviderCom com = new CloudDropboxCom();
		CloudError status = com.shareFolder(shareFolder.getID(), mailList);
		int i = 0;
		if(status != CloudError.NONE && i < 2){
			status = com.shareFolder(shareFolder.getID(), mailList);
		}		
		return status;
	}
	/**
	 * Open webbrowser for CloudProvider
	 * @param shareFolder
	 * @param mailList
	 * @param alreadyShared
	 * @return
	 */
	private CloudError shareFileToCloudManually(ShareFolder shareFolder, ArrayList<String> mailList, boolean alreadyShared){
		ICloudProviderCom com = new CloudDropboxCom();
		return com.shareFolderManuallyViaBrowser(shareFolder.getID(), alreadyShared);
	}

	public List<File> listFilesForFolder(final File folder) {
		List<File> fileList = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
				fileList.add(fileEntry);
			}
		}
		return fileList;
	}

	public void importFiles(String path, String foldername, String password) {
		String destinationPath = null;
		File source = null;
		String sharefolderName = "";
		FileHandler fileHandler = null;
		// MOVE SHAREFOLDER
		try {
			fileHandler = new FileHandler();
			source = new File(path);
			sharefolderName = source.getName();
			destinationPath = Configuration.getPropertieDropBoxPath(true)
					+ source.getName();
			File destination = new File(destinationPath);
			fileHandler.copyDirectory(source, destination);

		} catch (IOException e) {
			e.printStackTrace();
		}
		// Create ShareFolder
		ShareFolderDao shareFolderDao = new ShareFolderDao();
		ShareFolder sharefolder = new ShareFolder(Integer.parseInt(source
				.getName()), password);

		// Create GhostFolder
		GhostFolderDob ghostFolderDob = new GhostFolderDob(null, foldername,
				root);
		GhostFolderDao ghostfolderDao = new GhostFolderDao();
		// Encrypt UploadFile
		IFileSystemCom com = FileSystemCom.getInstance();
		if (!com.decryptFile(sharefolder, false)) {
			Status status = Status.getInstance();
			status.setMessage("Wrong Password");

		} else {
			// Delete if file decryption success
			try {
				if (!fileHandler.delete(source)) {
					throw new IOException("Unable to delete original folder");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Insert DB
			shareFolderDao.newElement(sharefolder, null);
			ghostFolderDob = (GhostFolderDob) ghostfolderDao.newElement(
					ghostFolderDob, null);
			// UpdateFile Import
			ShareManager shareManager = new ShareManager();
			List<EncryptedFileDob> fileDobList = shareManager
					.getUpdateFiles(sharefolder.getKey());
			List<EncryptedContainer> containerDobList = shareManager
					.getUpdateContainers(sharefolder.getKey());
			List<ShareRelation> shareRelationDobList = shareManager
					.getUpdateShareRelation(sharefolder.getKey());
			// Update/Insert fileList
			EncryptedFileDao fileDao = new EncryptedFileDao();
			for (int i = 0; i < fileDobList.size(); i++) {
				fileDobList.get(i).setParent(ghostFolderDob);
				fileDao.newElement(fileDobList.get(i), null);
			}
			// Insert ShareRelations
			ShareRelationDao shareDao = new ShareRelationDao();
			for (ShareRelation relation : shareRelationDobList) {
				shareDao.newElement(relation, null);
			}
			// Insert Containers
			ContainerDao containerDao = new ContainerDao();
			for (EncryptedContainer container : containerDobList) {
				containerDao.newElement(container, null);
			}
			// Update Tree
			root.addFolder(ghostFolderDob);
			ghostFolderDob.addFiles(fileDobList);
			viewer.refresh();
		}

	}
}
