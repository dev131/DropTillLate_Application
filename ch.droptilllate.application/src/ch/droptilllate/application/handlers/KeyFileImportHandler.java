/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package ch.droptilllate.application.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.lifecycle.OSValidator;
import ch.droptilllate.application.properties.Messages;

public class KeyFileImportHandler {
	private String path;
	
	@Execute
	public void execute(Shell shell) {
		chooseDestionation(shell);
		if(path != null){
			FileHandler fileHandler = new FileHandler();
			File destFile = new File(Messages.getApplicationpath() + OSValidator.getSlash()	+ Messages.KeyFile);
			File srcFile = new File(path + OSValidator.getSlash()+ Messages.KeyFile);
			fileHandler.moveFile(srcFile, destFile);
	
		}
	}
	
	private void chooseDestionation(Shell shell){
		FileDialog dialog = new FileDialog(shell);
		dialog.setText("Select key file");
		dialog.setFilterExtensions(new String[] { "*.key" });
	
	    try {
	    	path = dialog.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
