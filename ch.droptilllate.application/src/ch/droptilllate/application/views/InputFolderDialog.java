package ch.droptilllate.application.views;


import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class InputFolderDialog extends TitleAreaDialog {

		  public InputFolderDialog(Shell parentShell) {
		super(parentShell);
		
	}

		  private Text FolderNameText;

		  private String FolderName;


		  @Override
		  public void create() {
		    super.create();
		    setTitle("Please insert the folder name:");
		  }

		  @Override
		  protected Control createDialogArea(Composite parent) {
		    Composite area = (Composite) super.createDialogArea(parent);
		    Composite container = new Composite(area, SWT.NONE);
		    container.setLayoutData(new GridData(GridData.FILL_BOTH));
		    GridLayout layout = new GridLayout(2, false);
		    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		    container.setLayout(layout);


		    createLastName(container);

		    return area;
		  }

		  
		  private void createLastName(Composite container) {
		    Label lbtLastName = new Label(container, SWT.NONE);
		    lbtLastName.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		    lbtLastName.setText("Folder Name");
		    
		    GridData dataLastName = new GridData();
		    dataLastName.grabExcessHorizontalSpace = true;
		    dataLastName.horizontalAlignment = GridData.FILL;
		    this.FolderNameText = new Text(container, SWT.BORDER);
		    FolderNameText.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		    this.FolderNameText.setLayoutData(dataLastName);
		  }



		  @Override
		  protected boolean isResizable() {
		    return true;
		  }

		  // save content of the Text fields because they get disposed
		  // as soon as the Dialog closes
		  private void saveInput() {
			  this.FolderName = this.FolderNameText.getText();

		  }

		  @Override
		  protected void okPressed() {
		    saveInput();
		    super.okPressed();
		  }

		  public String getLastName() {
		    return this.FolderName;
		  }
		} 

