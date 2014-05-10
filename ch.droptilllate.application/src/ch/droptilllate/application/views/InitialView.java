package ch.droptilllate.application.views;

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import ch.droptilllate.application.controller.InitController;
import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.dnb.CloudAccount;
import ch.droptilllate.application.error.ParamInitException;
import ch.droptilllate.application.exceptions.DatabaseStatus;
import ch.droptilllate.application.handlers.FileHandler;
import ch.droptilllate.application.info.ErrorMessage;
import ch.droptilllate.application.os.OSValidator;
import ch.droptilllate.application.properties.Messages;
import ch.droptilllate.database.api.DBSituation;
import ch.droptilllate.database.api.IDatabase;
import ch.droptilllate.database.xml.XMLDatabase;

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
	private Button btnSearchKeyfile;
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
	private Button cbCloudProvider;
	private Button cbExistingDTLAccount;
	private Composite compositeContent;
	private Composite parent;

	private ResourceBundle bundle;

	// STRINGS
	private String dropboxfoldername = "";
	private String password = "";
	private String dropboxLogin = "";
	private String dropboxPassword = "";
	private String dropboxPath = "";
	private String tempPath = "";

	// Integer
	private int initialFormHeight;

	// Boolean
	private boolean cloudAccountProvided = false;
	private boolean cloudAccountAccepted = false;
	private boolean cbExistingDTLAccountChecked = false;
	// Constants
	private static int FORM_WIDTH = 800;
	private static int HEIGHT_OFFSET = 20;
	private static int B_STARTUP_HEIGHT = 60;
	private static int BUTTON_WIDTH = 100;

	@PostConstruct
	public void createControls(Composite parent, Shell shell, EPartService partService, EModelService modelService, MApplication application)
	{
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		this.parent = parent;

		// initialise language string bundle
		bundle = ResourceBundle.getBundle("ch.droptilllate.application.multilanguage.InitialView");

		this.shell = shell;
		Bundle bundleFW = FrameworkUtil.getBundle(TreeView.class);
		// URL url = FileLocator.find(bundle, new Path("icons/icon_128x128.png"), null);
		URL url = FileLocator.find(bundleFW, new Path("icons/LOGO_BIG_V1.png"), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(url);
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, true));

		// ******************************************* Composite 1 *******************************************
		Composite compositeLogo = new Composite(parent, SWT.NONE);
		compositeLogo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeLogo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeLogo.setLayout(new GridLayout(1, false));

		// ******************************************* LOGO *******************************************
		CLabel lblDroptilllate = new CLabel(compositeLogo, SWT.CENTER);
		lblDroptilllate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblDroptilllate.setTopMargin(20);
		lblDroptilllate.setText("");
		lblDroptilllate.setImage(image.createImage());

		// ******************************************* Composite 2 *******************************************
		compositeContent = new Composite(parent, SWT.NONE);
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeContent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		compositeContent.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD | SWT.ITALIC));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 20;
		gl_composite.marginHeight = 20;
		gl_composite.marginWidth = 20;
		gl_composite.horizontalSpacing = 20;
		compositeContent.setLayout(gl_composite);

		// ******************************************* DROPTILLLATE SETTINGS *******************************************
		grpDroptilllateSettings = new Group(compositeContent, SWT.NONE);
		grpDroptilllateSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpDroptilllateSettings.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		grpDroptilllateSettings.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		grpDroptilllateSettings.setText(bundle.getString("DTLSettings"));
		GridLayout gl_grpDroptilllateSettings = new GridLayout(3, false);
		gl_grpDroptilllateSettings.marginTop = 10;
		gl_grpDroptilllateSettings.marginBottom = 10;
		gl_grpDroptilllateSettings.verticalSpacing = 10;
		gl_grpDroptilllateSettings.horizontalSpacing = 10;
		grpDroptilllateSettings.setLayout(gl_grpDroptilllateSettings);

		// ------------- Row ---------------
		cbExistingDTLAccount = new Button(grpDroptilllateSettings, SWT.CHECK);
		cbExistingDTLAccount.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		GridData gdCbExistingDTLAccount = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		cbExistingDTLAccount.setLayoutData(gdCbExistingDTLAccount);
		cbExistingDTLAccount.setText(bundle.getString("cbExistingDTLAccount"));
		cbExistingDTLAccount.addSelectionListener(this);
		cbExistingDTLAccount.setToolTipText(bundle.getString("ttcbExistingDTLAccount"));

		// ------------- Row ---------------
		lblDroptilllateFoldername = initLabel(grpDroptilllateSettings, "Arial", 14, bundle.getString("labelDTLFolder"), false);
		lblDroptilllateFoldername.setToolTipText(bundle.getString("ttDTLFolder"));

		txtDroptilllate = initTextField(grpDroptilllateSettings, "Arial", 14, "DropTillLate", true, false);

		btnSearchKeyfile = initButton(grpDroptilllateSettings, "Arial", 12, bundle.getString("buttonImportKeyFile"), BUTTON_WIDTH);
		btnSearchKeyfile.setToolTipText(bundle.getString("ttImportKeyfile"));
		btnSearchKeyfile.setVisible(false);
		btnSearchKeyfile.addSelectionListener(this);

		// ------------- Row ---------------
		lblPassword = initLabel(grpDroptilllateSettings, "Arial", 14, bundle.getString("labelPassword"), false);
		lblPassword.setToolTipText(bundle.getString("ttPassword"));

		text_password = initTextField(grpDroptilllateSettings, "Arial", 14, "", true, true);

		btnLogin_1 = initButton(grpDroptilllateSettings, "Arial", 12, bundle.getString("buttonDTLLogin"), BUTTON_WIDTH);
		btnLogin_1.addSelectionListener(this);

		// ------------- Row ---------------
		lblDropboxFolder = initLabel(grpDroptilllateSettings, "Arial", 14, bundle.getString("labelDropboxPath"), false);
		lblDropboxFolder.setToolTipText(bundle.getString("ttDropboxPath"));
		text_dropboxPath = initTextField(grpDroptilllateSettings, "Arial", 14, "", true, false);

		btnSearchDropFolder = initButton(grpDroptilllateSettings, "Arial", 12, bundle.getString("buttonSearchPath"), BUTTON_WIDTH);
		btnSearchDropFolder.addSelectionListener(this);

		// ------------- Row ---------------
		lblTempFolder = initLabel(grpDroptilllateSettings, "Arial", 14, bundle.getString("labelTempPath"), false);
		lblTempFolder.setToolTipText(bundle.getString("ttTempPath"));
		String offeredTempDir = new File(System.getProperty("user.dir"), "temp").getAbsolutePath();
		text_tempPath = initTextField(grpDroptilllateSettings, "Arial", 14, offeredTempDir, true, false);

		btnSearchTmpFolder = initButton(grpDroptilllateSettings, "Arial", 12, bundle.getString("buttonSearchPath"), BUTTON_WIDTH);
		btnSearchTmpFolder.addSelectionListener(this);

		// ******************************************* Startup Button *******************************************

		btnLogin = initButton(compositeContent, "Arial", 12, bundle.getString("buttonStartupDTL"), true);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.heightHint = B_STARTUP_HEIGHT;
		btnLogin.setLayoutData(gd);
		btnLogin.addSelectionListener(this);

		// ******************************************* CloudPovider Selector *******************************************
		cbCloudProvider = new Button(compositeContent, SWT.CHECK);
		cbCloudProvider.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		cbCloudProvider.setText(bundle.getString("cbCloudProvider"));
		cbCloudProvider.addSelectionListener(this);
		cbCloudProvider.setToolTipText(bundle.getString("ttcbCloudProvider"));

		// ******************************************* DROPBOX SETTINGS *******************************************
		grpDropboxSettings = new Group(compositeContent, SWT.NONE);
		grpDropboxSettings.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		grpDropboxSettings.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		grpDropboxSettings.setText(bundle.getString("DBAccount"));
		GridLayout gl_grpDropboxSettings = new GridLayout(2, false);
		gl_grpDropboxSettings.verticalSpacing = 10;
		gl_grpDropboxSettings.horizontalSpacing = 10;
		gl_grpDropboxSettings.marginTop = 10;
		gl_grpDropboxSettings.marginBottom = 10;
		grpDropboxSettings.setLayout(gl_grpDropboxSettings);

		// ------------- Row ---------------
		lblDropboxLoginname = initLabel(grpDropboxSettings, "Arial", 14, bundle.getString("labelDBLogin"), false);

		text_DropboxLoginName = initTextField(grpDropboxSettings, "Arial", 14, "", 300, false);

		// ------------- Row ---------------
		lblPassword_1 = initLabel(grpDropboxSettings, "Arial", 14, bundle.getString("LabelDBPassword"), false);

		text_DropboxPassword = initTextField(grpDropboxSettings, "Arial", 14, "", 300, true);

		// ------------- Row ---------------
		btnTestDropbox = new Button(grpDropboxSettings, SWT.PUSH);
		btnTestDropbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnTestDropbox.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		btnTestDropbox.setText(bundle.getString("buttonTestCloudProvider"));
		btnTestDropbox.addSelectionListener(this);

		// ******************************************* Visibilities *******************************************
		// Visible config
		togglePathPropertiesVisible(false);
		toggleDropboxSettingVisibility(false);
		// CHECK properties

		// ******************************************* Form Sizes *******************************************

		initialFormHeight = shell.getSize().y;
		init();
	}

	private void init()
	{
		dropboxfoldername = "DropTillLate";
		controller = new InitController(shell);

		// Check if config files and filestructure file are not available
		if (!controller.checkProperties() || !controller.checkIfFileStructureAvailable())
		{
			togglePathPropertiesVisible(true);
			lblPassword.setText(bundle.getString("labelDTLPassword"));
			lblPassword.setToolTipText(bundle.getString("ttDTLPassword"));
			// toggleDropboxSettingVisibility(true);
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

	private void togglePathPropertiesVisible(boolean visible)
	{
		// If path not defined
		cbCloudProvider.setVisible(visible);
		btnLogin.setVisible(visible);
		btnLogin_1.setVisible(!visible);
		btnSearchDropFolder.setVisible(visible);
		btnSearchTmpFolder.setVisible(visible);
		text_dropboxPath.setVisible(visible);
		text_tempPath.setVisible(visible);
		lblDropboxFolder.setVisible(visible);
		lblTempFolder.setVisible(visible);
		btnLogin.setVisible(visible);
		txtDroptilllate.setVisible(visible);
		lblDroptilllateFoldername.setVisible(visible);
		cbExistingDTLAccount.setVisible(visible);
		btnSearchKeyfile.setVisible(visible);
	}

	private void toggleDropboxSettingVisibility(boolean visible)
	{
		grpDropboxSettings.setVisible(visible);
		lblPassword_1.setVisible(visible);
		text_DropboxPassword.setVisible(visible);
		btnTestDropbox.setVisible(visible);
		text_DropboxLoginName.setVisible(visible);
		lblDropboxLoginname.setVisible(visible);
	}

	private void toggleExistingDTLSettings(boolean visible)
	{
		cbCloudProvider.setVisible(!visible);
		txtDroptilllate.setVisible(!visible);
		btnSearchKeyfile.setVisible(visible);
		if (visible)
		{
			lblDroptilllateFoldername.setText(bundle.getString("labelKeyFile"));
			lblDropboxFolder.setText(bundle.getString("labelExistingDTLFolder"));
			lblDropboxFolder.pack();
			lblPassword.setText(bundle.getString("labelExistingDTLPassword"));
			lblDropboxFolder.setToolTipText(bundle.getString("ttExistingDTLFolder"));
			lblPassword.setToolTipText(bundle.getString("ttExistingDTLPassword"));
			lblDroptilllateFoldername.setToolTipText(bundle.getString("ttImportKeyfile"));
		} else
		{
			lblDroptilllateFoldername.setText(bundle.getString("labelDTLFolder"));
			lblDropboxFolder.setText(bundle.getString("labelDropboxPath"));
			lblPassword.setText(bundle.getString("labelDTLPassword"));
			lblDropboxFolder.setToolTipText("ttDropboxPath");
			lblPassword.setToolTipText(bundle.getString("ttDTLPassword"));
			lblDropboxFolder.setToolTipText(bundle.getString("ttDropboxPath"));
			lblPassword.setToolTipText(bundle.getString("ttDTLPassword"));
			lblDroptilllateFoldername.setToolTipText(bundle.getString("ttDTLFolder"));
		}
	}

	@Focus
	public void setFocus()
	{
	}

	// ********************** OPEN Folder Diaolg "*****************************************
	private void openFolderDialog(boolean dropbox)
	{
		try
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
		} catch (Exception e)
		{
		}
	}

	public void loginPressed()
	{
		try
		{

			if (controller.isNewUser())
			// Setup DTLA
			{

				if (!cbExistingDTLAccountChecked)
				{
					// Check if all parameters are valid
					if (checkAllFields(cbCloudProvider.getSelection()))
					{
						if (controller.newUser(dropboxfoldername, password, dropboxPath, tempPath, dropboxLogin, dropboxPassword,
								cloudAccountAccepted))
						{
							startApplication();
						}
					}
				} else
				// Reuse existing DTLA account from an other device
				{
					fetchFields();
					// check for valid DTLA folder path
					InitialViewHelper.checkForExistingFolder(dropboxPath, "DropTillLate");
					InitialViewHelper.checkTempPath(tempPath);
					// check for empty password field
					ViewHelper.isNotEmptyPassword(password);

					if (controller.useExistingAccount(password, dropboxPath, tempPath, dropboxPassword))
					{
						// check for valid password
						controller.login(password);
						// Start application
						startApplicationWithExistingAccount();
					}

				}

			} else
			// If DTLA is already setup, just the password has to be provided
			{
				// check for empty password field
				ViewHelper.isNotEmptyPassword(password);
				// check for valid password
				controller.login(password);
				// Start application
				startApplicationWithExistingAccount();

			}
		} catch (ParamInitException e)
		{
			new ErrorMessage(shell, e.getCategory(), e.getMessage());
		}

	}

	private void cbCloudProviderPressed()
	{
		boolean checked = cbCloudProvider.getSelection();
		toggleDropboxSettingVisibility(checked);
		int x = shell.getSize().x;
		int y = initialFormHeight;
		if (checked)
		{
			Point sizeFrame = grpDropboxSettings.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			y += sizeFrame.y;
		}
		shell.setSize(x, y);
	}

	private void cbExistingDTLAccountPressed()
	{
		cbExistingDTLAccountChecked = cbExistingDTLAccount.getSelection();
		if (cbExistingDTLAccountChecked)
		{
			// deselect Dropbox account infos
			cbCloudProvider.setSelection(false);
			cbCloudProviderPressed();
		}
		toggleExistingDTLSettings(cbExistingDTLAccountChecked);
	}

	private void btnSearchKeyfilePressed()
	{
		String path = chooseDestionation(shell);
		if (path != null)
		{
			FileHandler fileHandler = new FileHandler();
			File destFile = new File(Messages.getApplicationpath() + OSValidator.getSlash() + Messages.KeyFile);
			File srcFile = new File(path);
			fileHandler.moveFile(srcFile, destFile);

		}
	}

	private void startApplicationWithExistingAccount()
	{
		IDatabase database = new XMLDatabase();
		database.openDatabase(password, "", Messages.getIdSize(), DBSituation.LOCAL_DATABASE);
		startApplication();
	}

	private void startApplication()
	{
		MHandledToolItem aboutHandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.about",
				application);
		aboutHandler.setVisible(true);
		MHandledToolItem exportHandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.exportkeyfile",
				application);
		exportHandler.setVisible(true);

		MHandledToolItem openkeyfileHanlder = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.openkey",
				application);
		openkeyfileHanlder.setVisible(true);
		MHandledToolItem integryHandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.integrycheck",
				application);
		integryHandler.setVisible(true);

		MHandledToolItem newFolderHandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.newFolder",
				application);
		newFolderHandler.setVisible(true);
		MHandledToolItem importhandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.import",
				application);
		importhandler.setVisible(true);
		MHandledToolItem logviewhandler = (MHandledToolItem) modelService.find("ch.droptilllate.application.handledtoolitem.logview",
				application);
		// logviewhandler.setVisible(true);
		MPart part = partService.findPart("ch.droptilllate.application.part.decryptedview");
		part.setVisible(true);

		MPart ownpart = partService.findPart("ch.droptilllate.application.part.InitialView");
		ownpart.setVisible(false);

		ViewController.getInstance().initTree();
		ViewController.getInstance().setSharefunction(cbCloudProvider.getSelection());
		IDatabase database = new XMLDatabase();
		if (database.openTransaction("", DBSituation.LOCAL_DATABASE) != DatabaseStatus.OK)
		{
			// TODO ERROR HANDLING
		}
		List<CloudAccount> list = (List<CloudAccount>) database.getElementAll(CloudAccount.class);
		if (!list.isEmpty())
		{
			ViewController.getInstance().setSharefunction(true);
		}
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
		// Startup
		if (e.getSource() == btnLogin)
		{
			loginPressed();
		}
		if (e.getSource() == btnTestDropbox)
		{
			testDropboxAccount();

		}
		if (e.getSource() == btnLogin_1)
		{
			loginPressed();
		}
		if (e.getSource() == cbCloudProvider)
		{
			cbCloudProviderPressed();
		}
		if (e.getSource() == cbExistingDTLAccount)
		{
			cbExistingDTLAccountPressed();
		}
		if (e.getSource() == btnSearchKeyfile)
		{
			btnSearchKeyfilePressed();
		}
	}

	private String chooseDestionation(Shell shell)
	{
		FileDialog dialog = new FileDialog(shell);
		dialog.setText("Select key file");
		dialog.setFilterExtensions(new String[]
		{ "*.key" });

		try
		{
			return dialog.open();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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

	private void testDropboxAccount()
	{
		try
		{
			InitialViewHelper.checkDropboxLoginName(dropboxLogin);
			InitialViewHelper.checkDropboxPassword(dropboxPassword);
			cloudAccountAccepted = controller.checkDropboxAccount(dropboxLogin, dropboxPassword);
		} catch (ParamInitException e)
		{
			new ErrorMessage(shell, e.getCategory(), e.getMessage());
		}
	}

	private void fetchFields()
	{
		// Fetch content
		dropboxPath = text_dropboxPath.getText();
		tempPath = text_tempPath.getText();
		dropboxfoldername = txtDroptilllate.getText();
		password = text_password.getText();
	}

	private boolean checkAllFields(Boolean withCloudAccount)
	{
		fetchFields();

		if (withCloudAccount)
		{
			if (!cloudAccountAccepted)
			{
				new ErrorMessage(shell, "Dropbox Account Error", "Please verifiy your Dropbox account first.");
				return false;
			}
		}

		try
		{
			InitialViewHelper.checkDropboxFolderName(dropboxfoldername);
			InitialViewHelper.checkDTLPassword(password);
			InitialViewHelper.checkForExistingFolder(dropboxPath, "Dropbox");
			InitialViewHelper.checkTempPath(tempPath);

			if (InitialViewHelper.checkForSamePath(dropboxPath, tempPath))
			{
				new ParamInitException("Conflict", "Dropbox path and temporary file path can not be the same!");
			}
			return true;
		} catch (ParamInitException e)
		{
			new ErrorMessage(shell, e.getCategory(), e.getMessage());
		}
		return withCloudAccount;
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

	private Text initTextField(Composite parent, String font, int fontSize, String text, boolean fillAvailableSpace, boolean password)
	{
		Text textBox;
		if (password)
		{
			textBox = new Text(parent, SWT.PASSWORD | SWT.BORDER);
		} else
		{
			textBox = new Text(parent, SWT.BORDER);
		}
		textBox.setFont(SWTResourceManager.getFont(font, fontSize, SWT.NORMAL));
		textBox.setText(text);
		if (fillAvailableSpace)
		{
			textBox.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		}
		return textBox;
	}

	private Text initTextField(Composite parent, String font, int fontSize, String text, int width, boolean password)
	{
		Text textBox = initTextField(parent, font, fontSize, text, false, password);
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
