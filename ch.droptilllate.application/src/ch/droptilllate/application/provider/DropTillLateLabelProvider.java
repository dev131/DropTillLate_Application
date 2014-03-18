package ch.droptilllate.application.provider;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;

import org.eclipse.swt.graphics.Image;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.eclipse.jface.viewers.ITableLabelProvider;

import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.views.EncryptedView;
import ch.droptilllate.application.views.TableIdentifier;

public class DropTillLateLabelProvider implements ITableLabelProvider {

	private SimpleDateFormat sdf = new SimpleDateFormat(Messages.DATE_FORMAT);

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TableIdentifier identifier = TableIdentifier.values()[columnIndex];
		String text = null;

		if (element instanceof EncryptedFileDob) {
			switch (identifier) {
			case DATE:
				text = this.sdf.format(((EncryptedFileDob) element).getDate());
				break;
			case NAME:
				text = ((EncryptedFileDob) element).getName().lastIndexOf(".") <= 0 ? ((EncryptedFileDob) element)
						.getName() : ((EncryptedFileDob) element).getName()
						.substring(
								0,
								((EncryptedFileDob) element).getName()
										.lastIndexOf("."));
				break;
			case SIZE:
				BigDecimal size = new BigDecimal(
						((EncryptedFileDob) element).getSize() / 1024);
				text = size.compareTo(new BigDecimal(1000)) > 0 ? size
						.divide(new BigDecimal(1024))
						.setScale(2, BigDecimal.ROUND_HALF_UP).toString()
						+ " MB" : size.setScale(0, BigDecimal.ROUND_HALF_UP)
						.toString() + " KB";
				break;
			case TYPE:
				text = ((EncryptedFileDob) element).getType().equalsIgnoreCase(
						"") ? "" : ((EncryptedFileDob) element).getType()
						+ Messages.FILE;
				break;

			default:
				assert false : identifier + " is not a legal identifier!"; //$NON-NLS-1$
			}
		}

		else if (element instanceof GhostFolderDob) {
			switch (identifier) {
			case DATE:
				text = "";
				break;
			case NAME:
				text = ((GhostFolderDob) element).getName();
				break;
			case SIZE:
				text = "";
				break;
			case TYPE:
				text = "";
				break;

			default:
				assert false : identifier + " is not a legal identifier!"; //$NON-NLS-1$
			}
		}

		return text;
	}

	public Image getColumnImage(Object element, int columnIndex) {

		String fileType = "";

		if (columnIndex == 0) {
			if (element instanceof GhostFolderDob) {
				return getImage("folder.png");
			} else if (element instanceof EncryptedFileDob) {
				fileType = ((EncryptedFileDob) element).getType();
				// fileType = fileType.substring(0, fileType.lastIndexOf("-"));

				try {
					return getImage("file-" + fileType + ".png");
				} catch (Exception e) {
					if (((EncryptedFileDob) element).getType().contains("html"))
						return getImage("file-htm.png");

					else if (((EncryptedFileDob) element).getType().contains(
							"docx")
							|| ((EncryptedFileDob) element).getType().contains(
									"pages")
							|| ((EncryptedFileDob) element).getType().contains(
									"odt"))
						return getImage("file-doc.png");

					else if (((EncryptedFileDob) element).getType().contains(
							"xlsx")
							|| ((EncryptedFileDob) element).getType().contains(
									"numbers")
							|| ((EncryptedFileDob) element).getType().contains(
									"ods"))
						return getImage("file-xls.png");

					else if (((EncryptedFileDob) element).getType().contains(
							"pptx")
							|| ((EncryptedFileDob) element).getType().contains(
									"key")
							|| ((EncryptedFileDob) element).getType().contains(
									"odp"))
						return getImage("file-ppt.png");

					else if (((EncryptedFileDob) element).getType().contains(
							"pptx"))
						return getImage("file-ppt.png");

					else
						return getImage("file.png");
				}
			}
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public Image getImage(String file) {
		// assume that the current class is called View.java
		Bundle bundle = FrameworkUtil.getBundle(EncryptedView.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		return image.createImage();
	}

}
