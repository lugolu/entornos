package ar.com.thinksoft.utils;

import org.junit.experimental.categories.Category;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.common.Dto;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@Category(Dto.class)
public class TestDateCommonFunctions extends TestCase {

	private static final String clase = "ar.com.thinksoft.utils.TestDateCommonFunctions";

	String error = null;
	private String testName = null;

	public TestDateCommonFunctions( String testName )  {
		super( testName );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		testName = getName();
	}

	public static Test suite() {
		return new TestSuite( TestDateCommonFunctions.class );
	}

	public void testDateCommonFunctions1() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				new ar.com.thinksoft.utils.DateCommonFunctions();
				Commons.logInfoSuccess(clase, testName);
			} catch (Exception e) {
				HandlerException.getInstancia().treateException(e,getClass());
			}
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

		CustomTest a = TestDateCommonFunctions.class.getMethod(testName).getAnnotation(CustomTest.class);
		Commons.logTestResult(clase + "." + testName, (a != null ? a.motivo() : null));
	}

}

