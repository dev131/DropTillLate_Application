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
		
		text_path = new Text(composite, SWT.BORDER);
		text_path.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_path.setBounds(115, 80, 179, 19);
		
		Label lblImportFolder = new Label(composite, SWT.NONE);
		lblImportFolder.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblImportFolder.setBounds(10, 80, 99, 19);
		lblImportFolder.setText("Import Folder ...");
		
		 btnSearch = new Button(composite, SWT.NONE);
		 btnSearch.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnSearch.setBounds(300, 80, 91, 25);
		btnSearch.setText("search ...");
		btnSearch.addSelectionListener(this);
		
		Label lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblPassword.setBounds(13, 130, 75, 17);
		lblPassword.setText("Password");
		
		text_password = new Text(composite,SWT.PASSWORD| SWT.BORDER);
		text_password.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_password.setBounds(115, 130, 179, 19);
		
		Label lblFolderName = new Label(composite, SWT.NONE);
		lblFolderName.setFont(SWTResourceManager.getFont("Lucida Grande", 14, SWT.NORMAL));
		lblFolderName.setBounds(13, 204, 96, 19);
		lblFolderName.setText("Folder name");
		
		text_foldername = new Text(composite, SWT.BORDER);
		text_foldername.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.NORMAL));
		text_foldername.setBounds(115, 204, 179, 19);
		
		 btnImport = new Button(composite, SWT.NONE);
		 btnImport.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnImport.setBounds(247, 286, 94, 28);
		btnImport.setText("Import");
		btnImport.addSelectionListener(this);
		
		 btnCancel = new Button(composite, SWT.NONE);
		 btnCancel.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		btnCancel.setBounds(57, 286, 94, 28);
		btnCancel.setText("cancel");
		
		Label lblImport = new Label(composite, SWT.NONE);
		lblImport.setFont(SWTResourceManager.getFont("Lucida Grande", 16, SWT.BOLD));
		lblImport.setBounds(151, 10, 78, 28);
		lblImport.setText("Import");
		btnCancel.addSelectionListener(this);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		sashForm.setWeights(new int[] {401, 382});
	}

	

	private void cancel() {
		MPart ownpart = partService.findPart("ch.droptilllate.application.part.decryptedview");
		ownpart.setVisible(true);
		MPart mPart = partService.findPart("ch.droptilllate.application.part.Import");
		mPart.setVisible(false);		
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
