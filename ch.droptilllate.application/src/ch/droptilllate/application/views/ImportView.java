package ch.droptilllate.application.views;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.properties.Configuration;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;

public class ImportView implements SelectionListener{
	@Inject EPartService partService;
	private Text text_path;
	private Text text_password;
	private Text text_foldername;
	private Button btnSearch;
	private Button btnImport;
	private Button btnCancel;
	private Shell shell;
	
	public ImportView()  {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent,Shell shell, EPartService partService) {
		this.shell = shell;
		SashForm sashForm = new SashForm(parent, SWT.NONE);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(2, false));
		
		Group grpImport = new Group(composite, SWT.NONE);
		grpImport.setLayout(new GridLayout(3, false));
		GridData gd_grpImport = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_grpImport.heightHint = 158;
		gd_grpImport.widthHint = 363;
		grpImport.setLayoutData(gd_grpImport);
		grpImport.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		grpImport.setText("Import");
		
		Label lblFolderName = new Label(grpImport, SWT.NONE);
		lblFolderName.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblFolderName.setText("Folder name");
		
		text_foldername = new Text(grpImport, SWT.BORDER);
		GridData gd_text_foldername = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_text_foldername.widthHint = 139;
		text_foldername.setLayoutData(gd_text_foldername);
		text_foldername.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		new Label(grpImport, SWT.NONE);
		new Label(grpImport, SWT.NONE);
		new Label(grpImport, SWT.NONE);
		new Label(grpImport, SWT.NONE);
		  
		  Label lblImportFolder = new Label(grpImport, SWT.NONE);
		  lblImportFolder.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		  lblImportFolder.setText("Import Folder ...");
		  
		  text_path = new Text(grpImport, SWT.BORDER);
		  text_path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		  text_path.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		  new Label(grpImport, SWT.NONE);
		  new Label(grpImport, SWT.NONE);
		  new Label(grpImport, SWT.NONE);
		  new Label(grpImport, SWT.NONE);
		  
		  Label lblPassword = new Label(grpImport, SWT.NONE);
		  lblPassword.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		  lblPassword.setText("Password");
		   
		   text_password = new Text(grpImport,SWT.PASSWORD| SWT.BORDER);
		   text_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		   text_password.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		  
		   btnSearch = new Button(grpImport, SWT.NONE);
		   btnSearch.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		   btnSearch.setText("search ...");
		   btnSearch.addSelectionListener(this);
		   
		    btnCancel = new Button(composite, SWT.NONE);
		    btnCancel.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		    btnCancel.setText("cancel");
		     
		      btnImport = new Button(composite, SWT.NONE);
		      btnImport.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		      btnImport.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		      btnImport.setText("Import");
		      btnImport.addSelectionListener(this);
		    btnCancel.addSelectionListener(this);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		sashForm.setWeights(new int[] {398, 329});
	}

	

	private void cancel() {
		MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
		ownpart.setVisible(true);
		MPart mPart = partService.findPart("ch.droptilllate.application.part.Import");
		mPart.setVisible(false);	
		ShareView.getInstance().deleteUnSuccessShareFolder();
	}

	private void importShareFolder() {
		if(text_path.getText().equals("") || text_foldername.getText().equals("") || text_password.getText().equals("")){
			new ErrorMessage(shell,"Error", "Argument Missing");
		}
		else{
			MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
			ownpart.setVisible(true);
			MPart mPart = partService.findPart("ch.droptilllate.application.part.Import");
			mPart.setVisible(false);	
			ViewController.getInstance().importFiles(text_path.getText(), text_foldername.getText(), text_password.getText());
		}
		
	}

	private void search() {
		DirectoryDialog dirDialog = new DirectoryDialog(shell);
	    dirDialog.setText("Select your home directory");   	
	    try {
			text_path.setText(dirDialog.open());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
	}
	

	//*********** OVERRIDE METHODS *******************
	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == btnSearch){ 
			search();
	        } 	
		if(e.getSource() == btnImport){ 
			importShareFolder();
        } 
		if(e.getSource() == btnCancel){ 
			cancel();
        } 
		
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
