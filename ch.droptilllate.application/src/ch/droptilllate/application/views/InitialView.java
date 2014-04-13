package ch.droptilllate.application.views;

import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.UIEvents.Part;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ch.droptilllate.application.controller.InitController;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.info.SuccessMessage;
import ch.droptilllate.cloudprovider.error.CloudError;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class InitialView implements SelectionListener, ModifyListener
{

	@Inject
	EPartService partService;
	@Inject
	EModelService modelService;
	@Inject
	MApplication application;
	private InitController controller;
	private Text text_tempPath;
	private Text text_dropboxPath;
	private Text text_password;
	private Button btnSearchDropFolder;
	private Button btnSearchTmpFolder;
	private Button btnLogin;
	private Label lblPassword;
	private Shell shell;
	private Text text_DropboxLoginName;
	private Text text_DropboxPassword;
	private Button btnTestDropbox;
	private Label lblPassword_1;
	private Label lblDropboxLoginname;
	private Label lblDropboxFolder;
	private Label lblTempFolder;
	private Button btnLogin_1;
	private Label lbldroptilllate;
	private Label lblDroptilllateFoldername;
	private Text txtDroptilllate;
	private Group grpDroptilllateSettings;
	private Group grpDropboxSettings;

	// STRINGS
	private String dropboxfoldername;
	private String password;
	private String dropboxLogin;
	private String dropboxPassword;
	private String dropboxPath;
	private String tempPath;

	// Constants
	private static int LOGO_HEIGHT = 300;
	private static int FORM_WIDTH = 800;
	private static int BUTTON_HEIGHT1 = 60;

	@PostConstruct
	public void createControls(Composite parent, Shell shell, EPartService partService, EModelService modelService, MApplication application)
	{
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		this.shell = shell;
		Bundle bundle = FrameworkUtil.getBundle(TreeView.class);
		// URL url = FileLocator.find(bundle, new Path("icons/icon_128x128.png"), null);
		URL url = FileLocator.find(bundle, new Path("icons/LOGO_BIG_V1.png"), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		sashForm.setLocation(0, 0);
		// shell.layout(true, true);

		// ******************************************* Composite *******************************************
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setLayout(new GridLayout(1, false));

		// ******************************************* LOGO *******************************************
		CLabel lblDroptilllate = new CLabel(composite_1, SWT.CENTER);
		// gd_lblDroptilllate.widthHint = 686;
		GridData logoGD = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		logoGD.heightHint = LOGO_HEIGHT;
		lblDroptilllate.setLayoutData(logoGD);
		lblDroptilllate.setTopMargin(20);
		lblDroptilllate.setText("");
		lblDroptilllate.setImage(image.createImage());

		// ******************************************* Composite *******************************************
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD | SWT.ITALIC));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 20;
		gl_composite.marginHeight = 20;
		gl_composite.marginWidth = 20;
		gl_composite.horizontalSpacing = 20;
		composite.setLayout(gl_composite);

		// ******************************************* DROPTILLLATE SETTINGS *******************************************
		grpDroptilllateSettings = new Group(composite, SWT.NONE);
		grpDroptilllateSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpDroptilllateSettings.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		grpDroptilllateSettings.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		grpDroptilllateSettings.setText("DropTillLate Settings");
		GridLayout gl_grpDroptilllateSettings = new GridLayout(3, false);
		gl_grpDroptilllateSettings.marginTop = 10;
		gl_grpDroptilllateSettings.marginBottom = 10;
		gl_grpDroptilllateSettings.verticalSpacing = 10;
		gl_grpDroptilllateSettings.horizontalSpacing = 10;
		grpDroptilllateSettings.setLayout(gl_grpDroptilllateSettings);

		// ------------- Row 1 ---------------
		lblDroptilllateFoldername = initLabel(grpDroptilllateSettings, "Arial", 14, "Foldername", false);

		txtDroptilllate = initTextField(grpDroptilllateSettings, "Arial", 14, "DropTillLate", true);

		new Label(grpDroptilllateSettings, SWT.NONE); // 3. slot

		// ------------- Row 2 ---------------
		lblPassword = initLabel(grpDroptilllateSettings, "Arial", 14, "Password", false);

		text_password = initTextField(grpDroptilllateSettings, "Arial", 14, "", true);

		btnLogin_1 = initButton(grpDroptilllateSettings, "Arial", 12, "login", false);
		btnLogin_1.addSelectionListener(this);

		// ------------- Row 3 ---------------
		lblDropboxFolder = initLabel(grpDroptilllateSettings, "Arial", 14, "Dropbox path ", false);

		text_dropboxPath = initTextField(grpDroptilllateSettings, "Arial", 14, "", true);

		btnSearchDropFolder = initButton(grpDroptilllateSettings, "Arial", 12, "search ...", false);
		btnSearchDropFolder.addSelectionListener(this);

		// ------------- Row 4 ---------------
		lblTempFolder = initLabel(grpDroptilllateSettings, "Arial", 14, "Temporary file path ", false);

		text_tempPath = initTextField(grpDroptilllateSettings, "Arial", 14, "", true);

		btnSearchTmpFolder = initButton(grpDroptilllateSettings, "Arial", 12, "search ...", false);
		btnSearchTmpFolder.addSelectionListener(this);		
		

		// ******************************************* DROPBOX SETTINGS *******************************************
		grpDropboxSettings = new Group(composite, SWT.NONE);
		grpDropboxSettings.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		grpDropboxSettings.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		grpDropboxSettings.setText("Dropbox Settings");
		GridLayout gl_grpDropboxSettings = new GridLayout(2, false);
		gl_grpDropboxSettings.verticalSpacing = 10;
		gl_grpDropboxSettings.horizontalSpacing = 10;
		gl_grpDropboxSettings.marginTop = 10;
		gl_grpDropboxSettings.marginBottom = 10;
		grpDropboxSettings.setLayout(gl_grpDropboxSettings);

		// ------------- Row 1 ---------------
		lblDropboxLoginname = initLabel(grpDropboxSettings, "Arial", 14, "Dropbox Login ", false);

		text_DropboxLoginName = initTextField(grpDropboxSettings, "Arial", 14, "", 300);

		// ------------- Row 2 ---------------
		lblPassword_1 = initLabel(grpDropboxSettings, "Arial", 14, "Password ", false);

		text_DropboxPassword = initTextField(grpDropboxSettings, "Arial", 14, "", 300);

		// ------------- Row 3 ---------------
		btnTestDropbox = new Button(grpDropboxSettings, SWT.PUSH);
		btnTestDropbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnTestDropbox.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnTestDropbox.setText("Test Dropbox Account");
		btnTestDropbox.addSelectionListener(this);

		// ******************************************* PARENT COMPOSIT *******************************************
		
		btnLogin = initButton(composite, "Arial", 12, "Startup DropTillLate", true);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.heightHint = BUTTON_HEIGHT1;
		btnLogin.setLayoutData(gd);
		btnLogin.addSelectionListener(this);

		// ******************************************* Visibilities *******************************************
		// Visible config
		btnSearchDropFolder.setVisible(false);
		btnSearchTmpFolder.setVisible(false);
		text_dropboxPath.setVisible(false);
		text_tempPath.setVisible(false);
		lblDropboxFolder.setVisible(false);
		lblTempFolder.setVisible(false);
		btnLogin.setVisible(false);
		btnLogin_1.setVisible(true);
		txtDroptilllate.setVisible(false);
		lblDroptilllateFoldername.setVisible(false);

		text_DropboxPassword.setVisible(false);
		btnTestDropbox.setVisible(false);
		text_DropboxLoginName.setVisible(false);
		lblPassword_1.setVisible(false);
		lblDropboxLoginname.setVisible(false);
		btnLogin.setVisible(false);
		btnLogin_1.setVisible(true);
		grpDropboxSettings.setVisible(false);
		// CHECK properties

		// ******************************************* Form Sizes *******************************************
		
		sashForm.setWeights(new int[]
		{ 15, 85 });

		Point sizeComp2 = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int x = FORM_WIDTH;
		int y = LOGO_HEIGHT + sizeComp2.y;
		shell.setSize(x, y);

		init();
	}

	private void init()
	{
		dropboxfoldername = "DropTillLate";
		controller = new InitController(shell);
		// Check if config files and filestructure file are available
		if (!controller.checkProperties() || !controller.checkIfFileStructureAvailable())
		{
			makePathPropertiesVisible();
		}
		// Check if an Exit error exist
		controller.checkExitError();

		text_DropboxPassword.addModifyListener(this);
		text_DropboxLoginName.addModifyListener(this);
		text_dropboxPath.addModifyListener(this);
		text_tempPath.addModifyListener(this);
		txtDroptilllate.addModifyListener(this);
		text_password.addModifyListener(this);
	}

	private void makePathPropertiesVisible()
	{
		// TODO Auto-generated method stub
		// If path not defined
		lblPassword.setText("Create Password");
		text_DropboxPassword.setVisible(true);
		btnTestDropbox.setVisible(true);
		text_DropboxLoginName.setVisible(true);
		lblPassword_1.setVisible(true);
		lblDropboxLoginname.setVisible(true);
		btnLogin.setVisible(true);
		btnLogin_1.setVisible(false);
		btnSearchDropFolder.setVisible(true);
		btnSearchTmpFolder.setVisible(true);
		text_dropboxPath.setVisible(true);
		text_tempPath.setVisible(true);
		lblDropboxFolder.setVisible(true);
		lblTempFolder.setVisible(true);
		btnLogin.setVisible(true);
		txtDroptilllate.setVisible(true);
		lblDroptilllateFoldername.setVisible(true);
		grpDropboxSettings.setVisible(true);
	}

	@Focus
	public void setFocus()
	{
	}

	// ********************** OPEN Folder Diaolg "*****************************************
	private void openFolderDialog(boolean dropbox)
	{
		DirectoryDialog dialog = new DirectoryDialog(shell);
		if (dropbox == true)
		{
			dialog.setText("Choose Dropbox Directory");
			text_dropboxPath.setText(dialog.open());

		} else
		{
			dialog.setText("Choose Local Temp Directory");
			text_tempPath.setText(dialog.open());
		}
	}

	/**
	 * Check Path
	 * 
	 * @return return true if equals
	 */
	private boolean checkPathofEquals()
	{
		if (tempPath.equals(dropboxPath))
		{
			new ErrorMessage(shell, "Error", "Dropbox-Path and Temp-Path can not be equals");
			return true;
		}
		return false;
	}

	public void loginPressed()
	{

		if (controller.getNewUser())
		{
			// Check if all fields are not empty
			if (checkAllFields())
			{
				if (controller.newUser(dropboxfoldername, password, dropboxPath, tempPath, dropboxLogin, dropboxPassword))
				{
					startApplication();
				}
			}
		} else
		{
			if (!password.isEmpty())
			{
				// Check if Login successful
				if (controller.login(password))
				{
					startApplication();
				}
			} else
			{
				new ErrorMessage(shell, "Error", "No Password");
			}

		}

	}

	private boolean checkAllFields()
	{

		if (dropboxPassword.isEmpty() || dropboxfoldername.isEmpty() || dropboxPath.isEmpty() || tempPath.isEmpty()
				|| dropboxLogin.isEmpty() || password.isEmpty() || checkPathofEquals())
		{
			new ErrorMessage(shell, "Error", "Missing Argument");
			return false;
		}
		return true;
	}

	private void startApplication()
	{
		MHandledToolItem newFolderHandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.newFolder",
				application);
		newFolderHandler.setVisible(true);
		MHandledToolItem importhandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.import",
				application);
		importhandler.setVisible(true);
		MHandledToolItem logviewhandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.logview",
				application);
		logviewhandler.setVisible(true);
		MPart part = partService.findPart("ch.droptilllate.application.part.decryptedview");
		part.setVisible(true);
		ViewController.getInstance().initController();
		MPart ownpart = partService.findPart("ch.droptilllate.application.part.InitialView");
		ownpart.setVisible(false);
	}

	@Override
	public void widgetSelected(SelectionEvent e)
	{
		if (e.getSource() == btnSearchDropFolder)
		{
			openFolderDialog(true);
		}
		if (e.getSource() == btnSearchTmpFolder)
		{
			openFolderDialog(false);
		}
		if (e.getSource() == btnLogin)
		{
			loginPressed();
		}
		if (e.getSource() == btnTestDropbox)
		{
			controller.checkDropboxAccount(dropboxLogin, dropboxPassword);
		}
		if (e.getSource() == btnLogin_1)
		{
			loginPressed();
		}

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyText(ModifyEvent e)
	{

		if (e.getSource() == text_DropboxPassword)
		{
			dropboxPassword = text_DropboxPassword.getText();
		}
		if (e.getSource() == text_DropboxLoginName)
		{
			dropboxLogin = text_DropboxLoginName.getText();
		}
		if (e.getSource() == text_dropboxPath)
		{
			dropboxPath = text_dropboxPath.getText();
		}
		if (e.getSource() == text_tempPath)
		{
			tempPath = text_tempPath.getText();
		}
		if (e.getSource() == txtDroptilllate)
		{
			dropboxfoldername = txtDroptilllate.getText();
		}
		if (e.getSource() == text_password)
		{
			password = text_password.getText();
		}

	}

	private Label initLabel(Composite parent, String font, int fontSize, String text, boolean fillAvailableSpace)
	{
		Label label = new Label(parent, SWT.NONE);
		label.setFont(SWTResourceManager.getFont(font, fontSize, SWT.NORMAL));
		label.setText(text);
		if (fillAvailableSpace)
		{
			label.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		}
		return label;
	}

	private Label initLabel(Composite parent, String font, int fontSize, String text, int width)
	{
		Label Label = initLabel(parent, font, fontSize, text, false);
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = width;
		Label.setLayoutData(gd);
		return Label;
	}

	private Text initTextField(Composite parent, String font, int fontSize, String text, boolean fillAvailableSpace)
	{
		Text textBox = new Text(parent, SWT.BORDER);
		textBox.setFont(SWTResourceManager.getFont(font, fontSize, SWT.NORMAL));
		textBox.setText(text);
		if (fillAvailableSpace)
		{
			textBox.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		}
		return textBox;
	}

	private Text initTextField(Composite parent, String font, int fontSize, String text, int width)
	{
		Text textBox = initTextField(parent, font, fontSize, text, false);
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = width;
		textBox.setLayoutData(gd);
		return textBox;
	}

	private Button initButton(Composite parent, String font, int fontSize, String text, boolean fillAvailableSpace)
	{
		Button button = new Button(parent, SWT.NONE);
		button.setFont(SWTResourceManager.getFont(font, fontSize, SWT.NORMAL));
		button.setText(text);
		if (fillAvailableSpace)
		{
			button.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		}
		return button;
	}

	private Button initButton(Composite parent, String font, int fontSize, String text, int width)
	{
		Button button = initButton(parent, font, fontSize, text, false);
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = width;
		button.setLayoutData(gd);
		return button;
	}
	


	
}
