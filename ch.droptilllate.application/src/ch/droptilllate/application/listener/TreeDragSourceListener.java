package ch.droptilllate.application.listener;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import ch.droptilllate.application.dnb.DroppedElement;

public class TreeDragSourceListener implements DragSourceListener {
	private List<TreeItem> dragSourceItems;
	private Tree tree;
	public static List<DroppedElement> draggedDroppedElements;

	public TreeDragSourceListener(Tree tree) {
		this.tree = tree;
		this.dragSourceItems = new ArrayList<TreeItem>();
		TreeDragSourceListener.draggedDroppedElements = new ArrayList<DroppedElement>();
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		TreeItem[] selection = this.tree.getSelection();
		if (selection.length > 0 && selection[0].getItemCount() >= 0) {
			event.doit = true;
			for (TreeItem currentSelection : selection) {
				this.dragSourceItems.add(currentSelection);
			}
		} else {
			event.doit = false;
		}

	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		for (TreeItem currentDragSourceItem : this.dragSourceItems) {
			TreeDragSourceListener.draggedDroppedElements
					.add((DroppedElement) currentDragSourceItem.getData());
		}

	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE) {
			for (TreeItem currentDragSourceItem : this.dragSourceItems) {
				currentDragSourceItem.dispose();
			}
		}
		this.dragSourceItems.clear();
	}

}
