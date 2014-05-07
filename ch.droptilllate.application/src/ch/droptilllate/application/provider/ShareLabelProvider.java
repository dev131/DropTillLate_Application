package ch.droptilllate.application.provider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.droptilllate.application.dnb.TillLateContainer;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.properties.XMLConstruct;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.XMLDatabase;

public class ShareLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		DropTillLateLabelProvider prov = new DropTillLateLabelProvider();
		return prov.getColumnImage(element, columnIndex);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TableIdentifierShare identifier = TableIdentifierShare.values()[columnIndex];
		String text = null;

		if (element instanceof EncryptedFileDob) {
			switch (identifier) {
			case NAME:
				text = ((EncryptedFileDob) element).getName().lastIndexOf(".") <= 0 ? ((EncryptedFileDob) element)
						.getName() : ((EncryptedFileDob) element).getName()
						.substring(
								0,
								((EncryptedFileDob) element).getName()
										.lastIndexOf("."));
				break;
			case SHARE:
				IDatabase database = new XMLDatabase();
				if(database.openTransaction("", DBSituation.LOCAL_DATABASE)!= DatabaseStatus.OK){
				}
				else{
					text = "no";
					TillLateContainer container = (TillLateContainer) database.getElement(TillLateContainer.class, XMLConstruct.AttId,((EncryptedFileDob) element).getContainerId().toString() );
					if (container.getShareRelationId() != Messages.getIdSize()) {
						text = "yes";
						break;
					}
					else{
						text = "no";
						break;
					}
				}
				
			default:
				assert false : identifier + " is not a legal identifier!"; //$NON-NLS-1$
			}
		}

		return text;
	}

}
