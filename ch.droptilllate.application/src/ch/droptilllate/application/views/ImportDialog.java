package ch.droptilllate.application.views;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;




import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ImportDialog extends TitleAreaDialog {
	private Text passwordText;
	private Text foldernameText;
	private String passwordString;
	private String foldernameString;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ImportDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Bundle bundle = FrameworkUtil.getBundle(EncryptedView.class);
	 	URL url = FileLocator.find(bundle, new Path("icons/icon_32x32.png"), null);
	 	ImageDescriptor image = ImageDescriptor.createFromURL(url);
		setTitleImage(image.createImage());
		setTitle("DropTillLate");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Password");
		label.setBounds(10, 12, 55, 14);
		
		this.passwordText = new Text(container, SWT.BORDER);
		this.passwordText.setBounds(87, 10, 333, 19);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setVisible(true);
		label_1.setText("Folder Name");
		label_1.setBounds(10, 60, 72, 14);
		
		this.foldernameText = new Text(container, SWT.BORDER);
		this.foldernameText.setVisible(true);
		this.foldernameText.setBounds(87, 58, 333, 19);

		return area;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	 @Override
	  protected void okPressed() {
		 saveInput();
		    if(this.passwordString == null || this.foldernameString == null){
		    	//TODO error messages
		    }
		    else{
			    super.okPressed();
		    }
	  }
	 
	  @Override
	  protected boolean isResizable() {
	    return true;
	  }
	  
	  // save content of the Text fields because they get disposed
	  // as soon as the Dialog closes
	  private void saveInput() {
		  this.passwordString = this.passwordText.getText();
		  this.foldernameString = this.foldernameText.getText();
	  }

	public String getPasswordString() {
		return this.passwordString;
	}

	public String getFoldernameString() {
		return this.foldernameString;
	}
	  

}
