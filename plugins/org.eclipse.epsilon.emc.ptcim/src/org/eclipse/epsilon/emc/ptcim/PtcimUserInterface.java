/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Horacio Hoyos - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.ptcim;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;

public class PtcimUserInterface {
	
	PtcimObject studio;
	boolean isConnected = false;
	
	public PtcimUserInterface() {
	}
	
	public void connect() throws EolInternalException {
		if (!isConnected)
			//studio = bridge.connectByProgId("Studio.Editor");
		isConnected = true;
	}

	public String createModel(String server, String repository, String name) throws EolInternalException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public void disconnect() throws EolInternalException {
		if (isConnected) {
			isConnected = false;
		}
	}

	public boolean openDiagram(String id) {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		Object res = null;
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean openModel(String name) {
		List<Object> args = new ArrayList<Object>();
		args.add(name);
		Object res = null;
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean openModelLocation(String name, String directory) {
		List<Object> args = new ArrayList<Object>();
		args.add(name);
		args.add(directory);
		Object res = null;
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean selectBrowserItem(String itemId, String pane) {
		List<Object> args = new ArrayList<Object>();
		args.add(itemId);
		args.add(pane);
		Object res = null;
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public boolean selectSymbol(String diagramId, String itemId) {
		List<Object> args = new ArrayList<Object>();
		args.add(diagramId);
		args.add(itemId);
		Object res = null;
		if (res == null) {
			return false;
		}
		else {
			// FIXME See how boolean values are returned and wrap in a java boolean
			return true;
		}
	}

	public void setForegroundWindow() {
		
	}

	public void showMainWindow() {
		
	}
	
}
