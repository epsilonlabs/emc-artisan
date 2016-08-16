/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Hoacio Hoyos Rodriguez - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;

/**
 * The Class JawinCollection.
 */
public class JawinCollection extends AbstractCollection<JawinObject> {
	
	/** The source. */
	private final JawinObject source;
	
	/** The owner. */
	private final JawinObject owner;
	
	/** The association. */
	private final String association;
	
	/**
	 * Instantiates a new jawin collection.
	 *
	 * @param comCollection the source
	 * @param owner2 the owner
	 * @param association the association
	 */
	public JawinCollection(COMObject comCollection, COMObject owner, String association) {
		assert comCollection instanceof JawinObject;
		assert owner instanceof JawinObject;
		this.source = (JawinObject) comCollection;
		this.owner = (JawinObject) owner;
		this.association = association;
	}
	

	
	/**
	 * Important:  If you remove objects from an association that has its Propagate Delete flag set to TRUE,
	 * the objects will be deleted from the model. For example, a Class is related to its child Attributes
	 * through the Attribute association, which has its Propagate Delete flag set to TRUE.
	 * If you use the Remove function to remove owned Attributes from a Class, those Attributes will be
	 * deleted from the Model.
	 */
	@Override
	public void clear() {
		try {
			List<Object> args = new ArrayList<Object>();
			args.add(association);
			owner.invoke("Remove", args);
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
	}


	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(JawinObject e) {
		assert e.getId() != null;
		try {
			List<Object> args = new ArrayList<Object>();
			args.add(association);
			args.add(e.getDelegate());
			Object ret = owner.invoke("Add", args);
		} catch (EpsilonCOMException ex) {
			// TODO Can we check if message has 'Failed to add item' and do a
			// objItem.Property("ExtendedErrorInfo") to get more info?
			// Assuming owner and association are correct an exception means the item can not be added
			// But it would be nice to differentiate between, e.g. item already exists and wrong type.
			// FIXME Log the error?
			if (ex.getMessage().contains("Failed to add item")) {
				return false;
			}
			else {
				throw new IllegalArgumentException(ex);
			}
			
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<JawinObject> iterator() {
		Iterator<JawinObject> iterator = new JawinIterator(source);
		return iterator;
	}


	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		try {
			List<Object> args = new ArrayList<Object>();
			args.add(association);
			args.add(o);
			owner.invoke("Remove", args);
		} catch (EpsilonCOMException ex) {
			throw new IllegalStateException(ex);
		}
		return true;
	}


	/**
	 * The Artisan API does not provide this functionality.
	 *
	 * @param c the c
	 * @return true, if successful
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		Iterator<?> e = c.iterator();
		while (e.hasNext()) {
			modified |= remove(e.next());
		}
		return modified;
	}



	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		Object resCount;
		try {
			List<Object> args = new ArrayList<Object>();
			args.add(association);
			resCount = owner.invoke("ItemCount", args);
		} catch (EpsilonCOMException e) {
			throw new IllegalStateException(e);
		}
		return (Integer)resCount;
	}
}
