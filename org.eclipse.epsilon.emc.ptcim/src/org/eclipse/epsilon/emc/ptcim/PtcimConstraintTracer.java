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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcObject;
import org.eclipse.epsilon.emc.ptcim.ole.IPtcUserInterface;
import org.eclipse.epsilon.emc.ptcim.ole.impl.EpsilonCOMException;
import org.eclipse.epsilon.eol.dt.launching.EclipseContextManager;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.evl.dt.views.IConstraintTracer;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;

/**
 * The Class PtcimConstraintTracer provides the link between an element in the eclipse
 * Epsilon scripts and the PTC Integrity Modeler modeler.
 */
public class PtcimConstraintTracer implements IConstraintTracer {
	

	/* (non-Javadoc)
	 * @see org.eclipse.epsilon.evl.dt.views.IConstraintTracer#traceConstraint(org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint, org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void traceConstraint(UnsatisfiedConstraint constraint, ILaunchConfiguration configuration) {
		Object instance = constraint.getInstance();
		if (instance instanceof IPtcObject) {		// Ignore other objects
			NullProgressMonitor monitor = new NullProgressMonitor();
			EolContext context = new EolContext();
			try {
				EclipseContextManager.setup(context, configuration, monitor, null, true);
			} catch (EolRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			IPtcUserInterface<? extends IPtcObject> studio;
			try {
				studio = Activator.getDefault().getFactory().getUIManager();
			} catch (EpsilonCOMException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			try {
				showInModeler(instance, context, studio);
			} catch (EpsilonCOMException e) {
				e.printStackTrace();
				return;
			}
			context.getModelRepository().dispose();
		}
	}

	/**
	 * @param instance
	 * @param context
	 * @param studio
	 * @throws EpsilonCOMException 
	 */
	private void showInModeler(Object instance, EolContext context, IPtcUserInterface<? extends IPtcObject> studio) throws EpsilonCOMException {
		IModel model = context.getModelRepository().getOwningModel(instance);
		assert model instanceof PtcimModel;
		String modelId = ((PtcimModel) model).getModelId();
		String itemId = ((IPtcObject) instance).getId();
		IPtcObject item = (IPtcObject) ((PtcimModel) model).getElementById(itemId);
		studio.openModel(modelId);
		List<Object> args = new ArrayList<Object>();
		args.clear();
		args.add("Using Diagram");
		IPtcObject diag;
		diag = (IPtcObject) item.invoke("Item", args);
		if (diag != null) {
			String diagId = diag.getId();
			args.clear();
			args.add("Representing Symbol");
			IPtcObject objSymbol;
			objSymbol = (IPtcObject) item.invoke("Item", args);
			String symboldId = objSymbol.getId();
			args.clear();
			args.add(diagId);
			studio.openDiagram(diagId);
			studio.selectSymbol(diagId, symboldId);
		}
		else {		// There is no diagram, use the project tree
			studio.selectBrowserItem(itemId, "Packages");
		}
		studio.setForegroundWindow();
	}

}