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
import org.eclipse.wb.swt.SWTResourceManager;

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
	private ArrayList<EncryptedFileDob> fileList;
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
		sashForm.setBounds(0, 0, 725, 437);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		
		treeViewer = new TreeViewer(composite, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setBounds(10, 30, 390, 431);
		
		Label lblSharedFiles = new Label(composite, SWT.NONE);
		lblSharedFiles.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.NORMAL));
		lblSharedFiles.setBounds(10, 10, 146, 14);
		lblSharedFiles.setText("Shared Files:");
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		
		Label lblPassword = new Label(composite_1, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblPassword.setBounds(35, 39, 79, 19);
		lblPassword.setText("Password");
		
		text_password = new Text(composite_1,SWT.PASSWORD| SWT.BORDER);
		text_password.setBounds(120, 39, 145, 19);
		
		Label lblEmail = new Label(composite_1, SWT.NONE);
		lblEmail.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblEmail.setBounds(35, 73, 59, 19);
		lblEmail.setText("E-Mail");
		
		 btnAdd = new Button(composite_1, SWT.NONE);
		btnAdd.setBounds(291, 69, 62, 28);
		btnAdd.setText("Add");
		btnAdd.addSelectionListener(this);
		
		btnShare = new Button(composite_1, SWT.NONE);
		btnShare.setBounds(191, 431, 94, 28);
		btnShare.setText("Share");
		btnShare.addSelectionListener(this);
		
		 btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.setBounds(35, 431, 94, 28);
		btnCancel.setText("cancel");
		btnCancel.addSelectionListener(this);
		
		comboViewer = new ComboViewer(composite_1, SWT.NONE);
		combo_mail = comboViewer.getCombo();
		combo_mail.setBounds(91, 72, 194, 22);
		
		ListViewer listViewer = new ListViewer(composite_1, SWT.BORDER | SWT.V_SCROLL);
		 maillist = listViewer.getList();
		maillist.setBounds(39, 143, 246, 240);
		
		btnDelete= new Button(composite_1, SWT.NONE);
		btnDelete.setBounds(291, 143, 62, 28);
		btnDelete.setText("Delete");
		
		Label lblShareEmailList = new Label(composite_1, SWT.NONE);
		lblShareEmailList.setBounds(37, 128, 127, 14);
		lblShareEmailList.setText("Share e-mail list:");
		btnDelete.addSelectionListener(this);
		
		sashForm.setWeights(new int[] {1, 1});
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
		treeViewer.setContentProvider(new DropTillLateContentProvider());
		treeViewer.setLabelProvider(new DropTillLateLabelProvider());
		// Expand the tree
		treeViewer.setAutoExpandLevel(2);
		// Change TreeTable
		tree = treeViewer.getTree();
		// Tree table specific code starts fill labels
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		for (TableIdentifier identifier : TableIdentifier.values()) {
			if(identifier.name().equals("NAME")){
				identifier.columnWidth = 150;
			}
			new TreeColumn(tree, SWT.NONE).setText(Messages
					.getTableColumnTitle(identifier));
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

	private void share() {
		//CHECK if all data are available
		if(text_password.equals("") || maillist.getItems().length == 0 || this.fileList.isEmpty()  ){
			new ErrorMessage(shell, "Error", "Missing Argument");
		}
		else{
			ArrayList<String> emailList = new ArrayList<String>();
			for(String temp : maillist.getItems()){
				emailList.add(temp);
			  }
			MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
			ownpart.setVisible(true);
			MPart mPart = partService.findPart("ch.droptilllate.application.part.sharepart");
			mPart.setVisible(false);
			ViewController.getInstance().shareFiles(emailList ,fileList, text_password.getText() );
		
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
			share();
        } 
		if(e.getSource() == btnCancel){ 
			cancel();
        } 
		if(e.getSource() == btnDelete){ 
			deleteMail();
        } 
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
