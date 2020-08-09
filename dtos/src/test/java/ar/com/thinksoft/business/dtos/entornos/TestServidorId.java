package ar.com.thinksoft.business.dtos.entornos;

import org.junit.experimental.categories.Category;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.Commons.EXPECTED;
import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.common.Dto;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.MessageBundle;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@Category(Dto.class)
public class TestServidorId extends TestCase {

	private static final String clase = "ar.com.thinksoft.business.dtos.entornos.TestServidorId";

	String error = null;
	private String testName = null;

	public TestServidorId( String testName )  {
		super( testName );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		testName = getName();
	}

	public static Test suite() {
		return new TestSuite( TestServidorId.class );
	}

	public void testServidorId1() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				new ar.com.thinksoft.dtos.entornos.ServidorId();
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

	public void testServidorId2() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				Long idCliente = 1L;
				Long idServidor = 1L;
				new ar.com.thinksoft.dtos.entornos.ServidorId(idCliente, idServidor);
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

	public void testEqualsNull() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				ar.com.thinksoft.dtos.entornos.ServidorId objeto = new ar.com.thinksoft.dtos.entornos.ServidorId();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.ServidorId.class, "equals", objeto, null, EXPECTED.FALSE);
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

	public void testEqualsOtherClass() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				ar.com.thinksoft.dtos.entornos.ServidorId objeto = new ar.com.thinksoft.dtos.entornos.ServidorId();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.ServidorId.class, "equals", objeto, null, EXPECTED.FALSE);
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

	public void testEqualsDistinct() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				ar.com.thinksoft.dtos.entornos.ServidorId objeto = new ar.com.thinksoft.dtos.entornos.ServidorId();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.ServidorId.class, "equals", objeto, null, EXPECTED.FALSE);
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

	public void testEqualsEqual() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				ar.com.thinksoft.dtos.entornos.ServidorId objeto = new ar.com.thinksoft.dtos.entornos.ServidorId();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.ServidorId.class, "equals", objeto, objeto.clon(), EXPECTED.TRUE);
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

	public void testEqualsObject() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				ar.com.thinksoft.dtos.entornos.ServidorId objeto = new ar.com.thinksoft.dtos.entornos.ServidorId();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.ServidorId.class, "equals", objeto, objeto, EXPECTED.TRUE);
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

	public void testHashCode() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				ar.com.thinksoft.dtos.entornos.ServidorId objeto = new ar.com.thinksoft.dtos.entornos.ServidorId();
				int ret = objeto.hashCode();
				if (ret == 0) {
					throw new Exception(MessageBundle.NO_HAY_REGISTROS_CARGADOS);
				}
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

		CustomTest a = TestServidorId.class.getMethod(testName).getAnnotation(CustomTest.class);
		Commons.logTestResult(clase + "." + testName, (a != null ? a.motivo() : null));
	}

}

