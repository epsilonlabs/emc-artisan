package org.eclipse.epsilon.emc.ptcim.test;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AttributeRelatedTests.class,
	AssociationRelatedTests.class,
	ElementRelatedTests.class})
public class PtcimTestSuite {

	public static Test suite() {
		return new JUnit4TestAdapter(PtcimTestSuite.class);
	}
}
