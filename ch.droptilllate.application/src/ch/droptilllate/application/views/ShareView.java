package ch.droptilllate.application.views;

import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.swing.JPasswordField;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ListViewer;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dao.ShareRelationDao;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.application.provider.DropTillLateContentProvider;
import ch.droptilllate.application.provider.DropTillLateLabelProvider;
import ch.droptilllate.application.provider.ShareContentProvider;
import ch.droptilllate.application.provider.ShareLabelProvider;
import ch.droptilllate.application.provider.TableIdentifier;
import ch.droptilllate.application.provider.TableIdentifierShare;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;

public class ShareView implements SelectionListener {
	@Inject EPartService partService;
	private Shell shell;
	private static ShareView instance = null;
	private Text text_password;
	private ComboViewer comboViewer;
	private TreeViewer treeViewer;
	private Tree tree;
	private GhostFolderDob root;
	private Button btnAdd;
	private Button btnShare;
	private Button btnCancel;
	private Button btnDelete;
	private List maillist;
	private Combo combo_mail;
	private Button btnShareManually;
	private ArrayList<EncryptedFileDob> fileList;
	private Group grpShareSettings;
	private Group grpSelectedFiles;
	public ShareView() {
		instance = this;
	}
	
	/**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zurück
     */
    public static ShareView getInstance() {
        return instance;
    }

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent, EPartService partService, Shell shell) {
		this.shell = shell;
		this.partService = partService;
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		sashForm.setBounds(0, 0, 725, 437);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(1, false));
		   
		   grpSelectedFiles = new Group(composite, SWT.NONE);
		   grpSelectedFiles.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		   grpSelectedFiles.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		   grpSelectedFiles.setFont(SWTResourceManager.getFont("Arial", 14, SWT.BOLD));
		   grpSelectedFiles.setText("Selected Files");
		   GridData gd_grpSelectedFiles = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		   gd_grpSelectedFiles.heightHint = 371;
		   gd_grpSelectedFiles.widthHint = 280;
		   grpSelectedFiles.setLayoutData(gd_grpSelectedFiles);
		   
		   treeViewer = new TreeViewer(grpSelectedFiles, SWT.BORDER);
		   tree = treeViewer.getTree();
		   tree.setLocation(10, 10);
		   tree.setSize(270, 361);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setLayout(new GridLayout(4, false));
		
		grpShareSettings = new Group(composite_1, SWT.NONE);
		grpShareSettings.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		grpShareSettings.setFont(SWTResourceManager.getFont("Arial", 14, SWT.BOLD));
		grpShareSettings.setLayout(new GridLayout(3, false));
		grpShareSettings.setText("Share Settings");
		
		Label lblEmail = new Label(grpShareSettings, SWT.NONE);
		lblEmail.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblEmail.setText("E-Mail");
		
		comboViewer = new ComboViewer(grpShareSettings, SWT.NONE);
		combo_mail = comboViewer.getCombo();
		GridData gd_combo_mail = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_mail.widthHint = 185;
		combo_mail.setLayoutData(gd_combo_mail);
		 
		  btnAdd = new Button(grpShareSettings, SWT.NONE);
		  btnAdd.setText("Add");
		  
		  Label lblShareEmailList = new Label(grpShareSettings, SWT.NONE);
		  lblShareEmailList.setText("Share e-mail list:");
		  new Label(grpShareSettings, SWT.NONE);
		  new Label(grpShareSettings, SWT.NONE);
		  
		  ListViewer listViewer = new ListViewer(grpShareSettings, SWT.BORDER | SWT.V_SCROLL);
		  maillist = listViewer.getList();
		  GridData gd_maillist = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		  gd_maillist.heightHint = 221;
		  gd_maillist.widthHint = 263;
		  maillist.setLayoutData(gd_maillist);
		  
		  btnDelete= new Button(grpShareSettings, SWT.NONE);
		  btnDelete.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		  btnDelete.setText("Delete");
		  new Label(grpShareSettings, SWT.NONE);
		  new Label(grpShareSettings, SWT.NONE);
		  new Label(grpShareSettings, SWT.NONE);
		  
		  Label lblPassword = new Label(grpShareSettings, SWT.NONE);
		  lblPassword.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		  lblPassword.setText("Password");
		  
		  text_password = new Text(grpShareSettings,SWT.PASSWORD| SWT.BORDER);
		  GridData gd_text_password = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		  gd_text_password.widthHint = 186;
		  text_password.setLayoutData(gd_text_password);
		  new Label(grpShareSettings, SWT.NONE);
		      new Label(composite_1, SWT.NONE);
		      
		       btnCancel = new Button(composite_1, SWT.NONE);
		       btnCancel.setText("cancel");
		       btnCancel.addSelectionListener(this);
		        
		         btnShareManually = new Button(composite_1, SWT.NONE);
		         GridData gd_btnShareManually = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		         gd_btnShareManually.widthHint = 223;
		         btnShareManually.setLayoutData(gd_btnShareManually);
		         btnShareManually.setText("Share manually");
		         btnShareManually.setVisible(false);
		         btnShareManually.addSelectionListener(this);
		        
		        btnShare = new Button(composite_1, SWT.NONE);
		        btnShare.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		        btnShare.setText("Share");
		        btnShare.addSelectionListener(this);
		      sashForm.setWeights(new int[] {301, 507});
		  btnDelete.addSelectionListener(this);
		  btnAdd.addSelectionListener(this);
		getInitialTree();
	}

	//*********** public METHODS**************////
	public void setInitialInputMailList(){		
		combo_mail.removeAll();
		ShareRelationDao dao = new ShareRelationDao();
		ArrayList<ShareRelation> shareRelationList = (ArrayList<ShareRelation>) dao.getElementAll(null);
		HashSet<String> hashset = new HashSet<String>();
		for(ShareRelation shareRelation : shareRelationList){
			hashset.add(shareRelation.getMail());
		}
		for(String mail: hashset){
			combo_mail.add(mail);
		}		
	}
	
	public void setInitialTree(ArrayList<EncryptedFileDob> fileList){
		this.fileList = fileList;
		root = new GhostFolderDob(0, "Root-Folder", null);
		treeViewer.setInput(root);
		treeViewer.expandToLevel(1);
		root.addFiles(this.fileList);
	}
	
	//*********** private METHODS**************////
	private void getInitialTree(){
		treeViewer.setContentProvider(new ShareContentProvider());
		treeViewer.setLabelProvider(new ShareLabelProvider());

		// Expand the tree
		treeViewer.setAutoExpandLevel(2);
		// Change TreeTable
		tree = treeViewer.getTree();
		// Tree table specific code starts fill labels
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		for (TableIdentifierShare identifier : TableIdentifierShare.values()) {
			new TreeColumn(tree, SWT.NONE).setText(Messages.getTableColumnTitleShare(identifier));
			tree.getColumn(identifier.ordinal()).setWidth(
					identifier.columnWidth);
		}
	}
	
	private void deleteMail() {
		String[] list = maillist.getSelection();
		for(String i : list){
			maillist.remove(i);
		}
		
	}

	private void cancel() {
		MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
		ownpart.setVisible(true);
		MPart mPart = partService.findPart("ch.droptilllate.application.part.sharepart");
		mPart.setVisible(false);		
		
	}

	private void share(boolean auto) {
		//CHECK if all data are available
		boolean state;
		if(text_password.equals("") || maillist.getItems().length == 0 || this.fileList.isEmpty()  ){
			new ErrorMessage(shell, "Error", "Missing Argument");
		}
		else{
			ArrayList<String> emailList = new ArrayList<String>();
			for(String temp : maillist.getItems()){
				emailList.add(temp);
			  }
			if(auto){
				state = ViewController.getInstance().shareFiles(emailList ,fileList, text_password.getText(), true);
			}
			else{
				state = ViewController.getInstance().shareFiles(emailList ,fileList, text_password.getText(), false);
			}
			if(state){
				MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
				ownpart.setVisible(true);
				MPart mPart = partService.findPart("ch.droptilllate.application.part.sharepart");
				mPart.setVisible(false);
			}
			else{
				btnShareManually.setVisible(true);
			}
		}		
	}

	private void addMail() {
		maillist.add(combo_mail.getText());		
	}

	
//*********** OVERRIDE METHODS**************////	
	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == btnAdd){ 
			addMail();
	        } 	
		if(e.getSource() == btnShare){ 
			share(true);
        } 
		if(e.getSource() == btnCancel){ 
			cancel();
        } 
		if(e.getSource() == btnDelete){ 
			deleteMail();
        } 
		if(e.getSource() == btnShareManually){ 		
			share(false);
        } 
	
	}


	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
