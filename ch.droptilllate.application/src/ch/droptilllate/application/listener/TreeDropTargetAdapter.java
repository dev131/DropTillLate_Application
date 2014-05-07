package ch.droptilllate.application.listener;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.TreeItem;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dnb.DroppedElement;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.views.Status;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.AbstractXmlDatabase;
import ch.droptilllate.database.xml.XMLDatabase;

public class TreeDropTargetAdapter extends DropTargetAdapter {
	@Inject
	private EModelService service;
	private MApplication application;

	private FileTransfer fileTransfer = FileTransfer.getInstance();
	private TreeViewer treeViewer;
	private GhostFolderDob root;
	private GhostFolderDob dragOverFolder;

	public TreeDropTargetAdapter(TreeViewer treeViewer,
			GhostFolderDob dragOverFolder) {
		this.treeViewer = treeViewer;
		this.root = dragOverFolder;
		this.dragOverFolder = this.root;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT)
			event.detail = DND.DROP_COPY;
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT)
			event.detail = DND.DROP_COPY;
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
		if (event.item != null) {
			TreeItem item = (TreeItem) event.item;
			DroppedElement dragOverElement = (DroppedElement) item.getData();

			if (dragOverElement instanceof EncryptedFileDob) {
				event.feedback |= DND.FEEDBACK_INSERT_AFTER;
			} else {
				event.feedback |= DND.FEEDBACK_SELECT;
			}
		}

	}
	
	@Override
	public void drop(DropTargetEvent event) {
		IDatabase database = new XMLDatabase();
		database.openTransaction("", DBSituation.LOCAL_DATABASE);
		// Handle Drag'N'Drop from Desktop into tree
		if (this.fileTransfer.isSupportedType(event.currentDataType)) {
			final String[] droppedFileInformation = (String[]) event.data;
			TreeItem item = null;
			if (event.item != null) {
				item = (TreeItem) event.item;
				if (item.getData() instanceof GhostFolderDob) {
					this.dragOverFolder = (GhostFolderDob) item.getData();
					item.setExpanded(true);
				} else {
					this.dragOverFolder = ((DroppedElement) item.getData()).getParent();
				}
			}
			try {
				// TODO Monitor
				 ViewController viewcontroller = ViewController.getInstance();
				 viewcontroller.encryptDropElements(droppedFileInformation, this.dragOverFolder);

			} catch (Exception e) {
				//TODO Exception handling
				System.out.println(" EncryptDropRElement failed");
				// log.error("Error at proccessing dropped data. " + e);
			}
			// set dragOverFolder back to root for next drop. Otherwise dropping
			// to root wont be possible.
			this.dragOverFolder = this.root;
		}

		// This part handles Drag'N'Drop within the tree
		else {
			// DraggedElement within the tree
			List<DroppedElement> draggedElements = TreeDragSourceListener.draggedDroppedElements;
			int itemsNotMoved = 0;

			draggedElements: for (DroppedElement draggedElement : draggedElements) {
				TreeItem item = null;

				if (event.item != null) {
					item = (TreeItem) event.item;

					if (item.getData() instanceof GhostFolderDob) {
						this.dragOverFolder = (GhostFolderDob) item.getData();
						item.setExpanded(true);
					} else {
						this.dragOverFolder = ((DroppedElement) item.getData())
								.getParent();
					}
				}

				if (draggedElement instanceof EncryptedFileDob) {
					EncryptedFileDob draggedFile = (EncryptedFileDob) draggedElement;
					draggedFile.getParent().removeFile(draggedFile);
					draggedFile.setParent(this.dragOverFolder);
					//Write to Database
					database.updateElement(draggedFile);
					this.dragOverFolder.addFile(draggedFile);
				} else if (draggedElement instanceof GhostFolderDob) {
					GhostFolderDob draggedFolder = (GhostFolderDob) draggedElement;

					GhostFolderDob currentDragOverFolder = this.dragOverFolder;

					while (!currentDragOverFolder.equals(this.root)) {
						if (currentDragOverFolder.equals(draggedFolder)) {
							itemsNotMoved++;
							continue draggedElements;
						}
						currentDragOverFolder = currentDragOverFolder
								.getParent();
					}

					draggedFolder.getParent().removeFolder(draggedFolder);
					draggedFolder.setParent(this.dragOverFolder);
					database.updateElement(draggedFolder);
					this.dragOverFolder.addFolder(draggedFolder);
				}
				// set dragOverFolder back to root for next drop. Otherwise
				// dropping to root wont be possible.
				this.dragOverFolder = this.root;
			}

			if (itemsNotMoved > 0) {
				//Todo system line
				System.out.println(itemsNotMoved);
			}
			itemsNotMoved = 0;
		}
		TreeDragSourceListener.draggedDroppedElements.clear();
		this.treeViewer.refresh();
	}
	

}
