package ch.droptilllate.application.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ch.droptilllate.application.dao.ShareRelationDao;
import ch.droptilllate.application.dnb.ShareRelation;

public class ShareDialog extends TitleAreaDialog {

	 public ShareDialog(Shell parentShell) {	
		 super(parentShell);
	
}

	  private Text PasswordText;
	  private String passwordString;
	  private Text EmailText;
	  private Label lblEmail;
	  private List listsource;
	  private List listdestination;
	  private Button btnDelete;
	  private Composite composite_1;
	  private Button btnAdd;
	  private ArrayList<String> emailList;

	  @Override
	  public void create() {
	    super.create();
	    setTitle("DroptTillLate");
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
			Bundle bundle = FrameworkUtil.getBundle(EncryptedView.class);
		 	URL url = FileLocator.find(bundle, new Path("icons/icon_32x32.png"), null);
		 	ImageDescriptor image = ImageDescriptor.createFromURL(url);
		
	  	setTitleImage(image.createImage());
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    container.setLayout(new FormLayout());
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    
	    Composite composite = new Composite(container, SWT.NONE);
	    composite.setLayout(new GridLayout(3, false));
	    FormData fd_composite = new FormData();
	    fd_composite.left = new FormAttachment(0);
	    fd_composite.right = new FormAttachment(100);
	    fd_composite.bottom = new FormAttachment(100);
	    composite.setLayoutData(fd_composite);
	    
	    listsource = new List(composite, SWT.BORDER);
	    GridData gd_list = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
	    gd_list.heightHint = 97;
	    gd_list.widthHint = 180;
	    listsource.setLayoutData(gd_list);
	    listsource.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				for(String i : listsource.getSelection())
				listdestination.add(i);
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    listdestination = new List(composite, SWT.BORDER);
	    GridData gd_list_1 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
	    gd_list_1.heightHint = 97;
	    gd_list_1.widthHint = 189;
	    listdestination.setLayoutData(gd_list_1);
	    
	    btnDelete = new Button(composite, SWT.NONE);
	    btnDelete.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		for(String i : listdestination.getSelection())
	    		listdestination.remove(i);
	    	}
	    });
	    GridData gd_btnDelete = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
	    gd_btnDelete.widthHint = 62;
	    btnDelete.setLayoutData(gd_btnDelete);
	    btnDelete.setText("delete");
	    
	    composite_1 = new Composite(container, SWT.NONE);
	    FormData fd_composite_1 = new FormData();
	    fd_composite_1.bottom = new FormAttachment(composite, -6);
	    fd_composite_1.top = new FormAttachment(0);
	    fd_composite_1.right = new FormAttachment(0, 450);
	    fd_composite_1.left = new FormAttachment(0);
	    composite_1.setLayoutData(fd_composite_1);
	    Label lbtLastName = new Label(composite_1, SWT.NONE);
	    lbtLastName.setLocation(10, 6);
	    lbtLastName.setSize(55, 14);
	    lbtLastName.setText("Password");
	    
	    lblEmail = new Label(composite_1, SWT.NONE);
	    lblEmail.setLocation(10, 29);
	    lblEmail.setSize(40, 14);
	    lblEmail.setText("E-mail");
	    EmailText = new Text(composite_1, SWT.BORDER);
	    EmailText.setLocation(71, 26);
	    EmailText.setSize(314, 19);
	    PasswordText = new Text(composite_1, SWT.BORDER);
	    PasswordText.setLocation(71, 3);
	    PasswordText.setSize(314, 19);
	    
	    btnAdd = new Button(composite_1, SWT.NONE);
	    btnAdd.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		if(!EmailText.getText().equals(""))
	    		listdestination.add(EmailText.getText());	    		
	    	}
	    });
	    btnAdd.setBounds(391, 22, 59, 28);
	    btnAdd.setText("add..");
	    getInitialInput();
	    return area;
	  }
	  

	  @Override
	  protected boolean isResizable() {
	    return true;
	  }

	  // save content of the Text fields because they get disposed
	  // as soon as the Dialog closes
	  private void saveInput() {
		  emailList = new ArrayList<String>();
		  passwordString = PasswordText.getText();
		  for(String temp : listdestination.getItems()){
			  emailList.add(temp);
		  }
	  }

	  @Override
	  protected void okPressed() {
	    saveInput();
	    if((passwordString.equals("")) || !(emailList.size() >= 0)){
	    	//TODO error messages
	    }
	    else{
		    super.okPressed();
	    }
	  }

	  public String getPassword() {
	    return passwordString;
	  }

	public ArrayList<String> getEmailList() {
		return emailList;
	}
	
	public void getInitialInput(){
		ShareRelationDao dao = new ShareRelationDao();
		ArrayList<ShareRelation> shareRelationList = (ArrayList<ShareRelation>) dao.getElementAll(null);
		HashSet<String> hashset = new HashSet<String>();
		for(ShareRelation shareRelation : shareRelationList){
			hashset.add(shareRelation.getMail());
		}
		for(String mail: hashset){
			listsource.add(mail);
		}
	}
	  
	
	} 
