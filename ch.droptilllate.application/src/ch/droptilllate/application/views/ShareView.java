package ch.droptilllate.application.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

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
import ch.droptilllate.application.dao.ShareMembersDao;
import ch.droptilllate.application.dnb.ShareMember;
import ch.droptilllate.application.dnb.ShareRelation;
import ch.droptilllate.application.error.ParamInitException;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.model.EncryptedFileDob;
import ch.droptilllate.application.model.GhostFolderDob;
import ch.droptilllate.application.properties.Configuration;
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

public class ShareView implements SelectionListener
{
	@Inject
	EPartService partService;
	private Shell shell;
	private Boolean success = true;
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
	private Group grpShareSettings;
	private Group grpSelectedFiles;
	private Button btnManually;
	
	ResourceBundle bundle;

	public ShareView()
	{
		instance = this;
	}

	// Constants
	private static int SHARE_FORM_WIDTH = 800;
	private static int SHARE_FORM_HEIGHT = 100;

	/**
	 * Statische Methode, liefert die einzige Instanz dieser Klasse zurück
	 */
	public static ShareView getInstance()
	{
		return instance;
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent, EPartService partService, Shell shell)
	{
		// initialise language string bundle
		bundle = ResourceBundle.getBundle("ch.droptilllate.application.multilanguage.ShareView");
		
		// get shell
		this.shell = shell;
		this.partService = partService;

		SashForm sashForm = new SashForm(parent, SWT.NONE);
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		sashForm.setSize(SHARE_FORM_WIDTH, SHARE_FORM_WIDTH);

		// ******************************************* Composite 1 *******************************************
		Composite compositeLeft = new Composite(sashForm, SWT.NONE);
		compositeLeft.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeLeft.setLayout(new GridLayout(1, false));

		Label lblSelectedFiles = new Label(compositeLeft, SWT.NONE);
		lblSelectedFiles.setFont(SWTResourceManager.getFont("Arial", 14, SWT.BOLD));
		lblSelectedFiles.setText(bundle.getString("labelSelectedFiles"));

		treeViewer = new TreeViewer(compositeLeft, SWT.BORDER);
		tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// ******************************************* Composite 2 *******************************************
		Composite compositeRight = new Composite(sashForm, SWT.NONE);
		compositeRight.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeRight.setLayout(new GridLayout(1, false));

		grpShareSettings = new Group(compositeRight, SWT.NONE);
		GridData gd_grpShareSettings = new GridData(SWT.CENTER, SWT.TOP, true, true, 2, 1);
		grpShareSettings.setLayoutData(gd_grpShareSettings);
		grpShareSettings.setFont(SWTResourceManager.getFont("Arial", 14, SWT.BOLD));
		grpShareSettings.setLayout(new GridLayout(3, false));
		grpShareSettings.setText(bundle.getString("labelShareSettings"));

		// ------------- Row ---------------
		Label lblEmail = new Label(grpShareSettings, SWT.NONE);
		lblEmail.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
		lblEmail.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		lblEmail.setText(bundle.getString("labelAddEmail"));

		// ------------- Row ---------------
		comboViewer = new ComboViewer(grpShareSettings, SWT.NONE);
		combo_mail = comboViewer.getCombo();
		GridData gd_combo_mail = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		combo_mail.setLayoutData(gd_combo_mail);

		btnAdd = new Button(grpShareSettings, SWT.NONE);
		btnAdd.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAdd.setText(bundle.getString("buttonAddEmail"));

		// ------------- Row ---------------
		Label lblShareEmailList = new Label(grpShareSettings, SWT.NONE);
		lblShareEmailList.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
		lblShareEmailList.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		lblShareEmailList.setText(bundle.getString("labelEmailList"));
		

		// ------------- Row ---------------
		ListViewer listViewer = new ListViewer(grpShareSettings, SWT.BORDER | SWT.V_SCROLL);
		maillist = listViewer.getList();
		GridData gd_maillist = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_maillist.heightHint = 221;
		gd_maillist.widthHint = 263;
		maillist.setLayoutData(gd_maillist);

		
		btnDelete = new Button(grpShareSettings, SWT.NONE);
		btnDelete.setLayoutData( new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		btnDelete.setText(bundle.getString("buttonRemoveEmail"));
		
		// ------------- Row ---------------
//		new Label(grpShareSettings, SWT.NONE);
//		new Label(grpShareSettings, SWT.NONE);
//		new Label(grpShareSettings, SWT.NONE);

		// ------------- Row ---------------
		Label lblPassword = new Label(grpShareSettings, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
		lblPassword.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		lblPassword.setText(bundle.getString("labelPassword"));

		// ------------- Row ---------------
		text_password = new Text(grpShareSettings, SWT.PASSWORD | SWT.BORDER);
		GridData gd_text_password = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		text_password.setLayoutData(gd_text_password);
		
		new Label(grpShareSettings, SWT.NONE);
		
		// ------------- Row ---------------
//		new Label(grpShareSettings, SWT.NONE);
//		new Label(grpShareSettings, SWT.NONE);
//		new Label(grpShareSettings, SWT.NONE);
		
		// ------------- Row ---------------
		Label lblShareButtons = new Label(grpShareSettings, SWT.WRAP);
		lblShareButtons.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
		lblShareButtons.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 3, 1));
		lblShareButtons.setText(bundle.getString("labelShareButtons"));

		// ------------- Row ---------------
		btnShare = new Button(grpShareSettings, SWT.NONE);
		btnShare.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnShare.setText(bundle.getString("buttonShare"));
		
		btnManually = new Button(grpShareSettings, SWT.NONE);
		btnManually.setText(bundle.getString("buttonManualShare"));
		btnManually.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnManually.setVisible(false);		
		
		
		// ------------- Button cancel ---------------
		btnCancel = new Button(compositeRight, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCancel.setText(bundle.getString("buttonCancel"));
		
		
		
		// Listeners
		btnShare.addSelectionListener(this);
		btnDelete.addSelectionListener(this);
		btnAdd.addSelectionListener(this);
		btnCancel.addSelectionListener(this);
		btnManually.addSelectionListener(this);	
		getInitialTree();

		// ******************************************* Form Sizes *******************************************
		sashForm.setWeights(new int[]
		{ 40, 60 });
		// shell.setSize(SHARE_FORM_WIDTH, SHARE_FORM_HEIGHT);
	}

	// *********** public METHODS**************////
	public void setInitialInputMailList()
	{

		combo_mail.removeAll();
		ShareMembersDao dao = new ShareMembersDao();
		ArrayList<ShareMember> shareMemberList = (ArrayList<ShareMember>) dao.getElementAll(null);
		HashSet<String> hashset = new HashSet<String>();
		for (ShareMember shareMember : shareMemberList)
		{
			hashset.add(shareMember.getMail());
		}
		for (String mail : hashset)
		{
			combo_mail.add(mail);
		}
	}

	public void setInitialTree(ArrayList<EncryptedFileDob> fileList)
	{
		this.fileList = fileList;
		root = new GhostFolderDob(0, "Root-Folder", null);
		treeViewer.setInput(root);
		treeViewer.expandToLevel(1);
		root.addFiles(this.fileList);
	}

	// *********** private METHODS**************////
	private void getInitialTree()
	{
		treeViewer.setContentProvider(new ShareContentProvider());
		treeViewer.setLabelProvider(new ShareLabelProvider());

		// Expand the tree
		treeViewer.setAutoExpandLevel(2);
		// Change TreeTable
		tree = treeViewer.getTree();
		// Tree table specific code starts fill labels
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		for (TableIdentifierShare identifier : TableIdentifierShare.values())
		{
			new TreeColumn(tree, SWT.NONE).setText(Messages.getTableColumnTitleShare(identifier));
			tree.getColumn(identifier.ordinal()).setWidth(identifier.columnWidth);
		}
	}

	private void deleteMail()
	{
		String[] list = maillist.getSelection();
		for (String i : list)
		{
			maillist.remove(i);
		}

	}

	private void cancel()
	{
		deleteUnSuccessShareFolder();
		MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
		ownpart.setVisible(true);
		MPart mPart = partService.findPart("ch.droptilllate.application.part.sharepart");
		mPart.setVisible(false);

	}

	public void deleteUnSuccessShareFolder()
	{
		// DELETE FOLDER
		if (!success)
		{
			System.out.println("Delete unsuccess sharefolder");
			ViewController.getInstance().getLastShareRelation();
			FileHandler filehandler = new FileHandler();
			ShareRelation shareRleation = ViewController.getInstance().getLastShareRelation();
			File file = new File(Configuration.getPropertieDropBoxPath(true) + ViewController.getInstance().getLastShareRelation().getID());
			try
			{
				filehandler.delete(file);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void addMail()
	{
		maillist.add(combo_mail.getText());
	}

	// *********** OVERRIDE METHODS**************////
	@PreDestroy
	public void dispose()
	{
	}

	@Focus
	public void setFocus()
	{
		// TODO Set the focus to control
	}

	@Override
	public void widgetSelected(SelectionEvent e)
	{
		if (e.getSource() == btnAdd)
		{
			addMail();
		}
		if (e.getSource() == btnCancel)
		{
			cancel();
		}
		if (e.getSource() == btnDelete)
		{
			deleteMail();
		}
		if (e.getSource() == btnShare)
		{
			shareFile(true);
		}
		if (e.getSource() == btnManually)
		{
			shareFile(false);
		}

	}

	private void shareFile(boolean auto)
	{
		
		try{
			// CHECK if all data are available			
			ShareViewHelper.checkMailList(maillist);
			ShareViewHelper.checkFileList(fileList);
			ShareViewHelper.checkPassword(text_password.getText());
			
			// share the shizzle
			ArrayList<String> emailList = new ArrayList<String>();
			for (String temp : maillist.getItems())
			{
				emailList.add(temp);
			}
			if (auto)
			{
				success = ViewController.getInstance().shareFiles(emailList, fileList, text_password.getText(), true);
			} else
			{
				success = ViewController.getInstance().shareFiles(emailList, fileList, text_password.getText(), false);
			}
			if (!success)
			{
				btnManually.setVisible(true);
			} else
			{
				success = true;
				MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
				ownpart.setVisible(true);
				MPart mPart = partService.findPart("ch.droptilllate.application.part.sharepart");
				mPart.setVisible(false);
			}
			
		} catch (ParamInitException e)
		{
			new ErrorMessage(shell, e.getCategory(), e.getMessage()); 
		}

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void setInitView()
	{
		success = true;
		combo_mail.removeAll();
		btnManually.setVisible(true);
		text_password.setText("");
	}
}
