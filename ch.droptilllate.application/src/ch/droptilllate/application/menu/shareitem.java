 
package ch.droptilllate.application.menu;

import java.util.List;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class shareitem {
	@AboutToShow
	public void aboutToShow(List<MMenuElement> items, EModelService modelService) {
		 MDirectMenuItem dynamicItem = modelService
	                .createModelElement(MDirectMenuItem.class);
	    dynamicItem.setLabel("share selected files");
	    dynamicItem.setContributionURI("bundleclass://ch.droptilllate.application/ch.droptilllate.application.menu.shareitem"); 
	    dynamicItem.setEnabled(false);
	   // items.add(dynamicItem);
	}
		
}