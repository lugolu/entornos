package ar.com.thinksoft.business.business.entornos;

import org.junit.experimental.categories.Category;

import ar.com.thinksoft.common.Business;
import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.jdbc.HibernateSessionFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@Category(Business.class)
public class TestImpBusAplicacion extends TestCase {

	private static final String clase = "ar.com.thinksoft.business.business.entornos.TestImpBusAplicacion";

	String error = null;

	private String testName = null;

	public TestImpBusAplicacion( String testName )  {
		super( testName );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		HibernateSessionFactory.getInstance();

		testName = getName();
	}

	public static Test suite() {
		return new TestSuite( TestImpBusAplicacion.class );
	}

	public void testTestImpBusAplicacion() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			new ar.com.thinksoft.business.entornos.implementations.ImpBusAplicacion();
			Commons.logInfoSuccess(clase, testName);
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				error = "NullPointerException";
			}
			else {
				error = e.getMessage();
			}
			Commons.logWarn (clase, testName, error, e);
		}

		assertNull(error);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		CustomTest a = TestImpBusAplicacion.class.getMethod(testName).getAnnotation(CustomTest.class);
		Commons.logTestResult(clase + "." + testName, (a != null ? a.motivo() : null));
	}

}

