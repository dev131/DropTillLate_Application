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
package ch.droptilllate.application.views;

import java.io.File;
import java.io.IOException;















import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import ch.droptilllate.application.controller.ViewController;
import ch.droptilllate.application.properties.Configuration;


public class TreeView{
	@Inject EModelService modelService;
	@Inject MApplication application;
	@Inject ESelectionService selectionService;
	@Inject EPartService partService;
	@Inject EMenuService menuService;
	
	public static final String ID = "ch.droptilllate.application.partdescriptor.InitialView";
	private TreeViewer viewer;
	private ViewController controller;
	private Shell shell;
	private Composite parent;
	
	@PostConstruct
	public void createPartControl(Composite parent, EMenuService menuService, Shell shell, EPartService partService, EModelService modelService, MApplication application) {
		// Treeviewer
		this.viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		menuService.registerContextMenu(this.viewer.getControl(),
				"ch.droptilllate.application.popupmenu.table");		
		
		
		this.shell = shell;
		this.parent = parent;
		this.controller = ViewController.getInstance();
		this.controller.initViewController(this.viewer, shell, partService, modelService, application);
		addListeners();
		addCloseListener(parent);
		
	}
	

	private void addCloseListener(Composite parent) {
		parent.addDisposeListener(new DisposeListener(){

			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				//TODO delete temp folder
				File file = new File(Configuration.getPropertieTempPath(true));
//				try {
//					FileUtils.cleanDirectory(file);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
			
		});
		
	}

	@Focus
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

	/**
	 * Adding listeners to corresponding GUI elements
	 */
	private void addListeners() {
		this.viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				// exportSelectionAction.run();
			}
		});

		this.viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection) event.getSelection();
				selectionService.setSelection(selection);
				TreeView.this.controller.selectionChanged(event);
			}
		});
	}
}