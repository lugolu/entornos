package ar.com.thinksoft.business.dtos.entornos;

import org.junit.experimental.categories.Category;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.Commons.EXPECTED;
import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.common.Dto;
import ar.com.thinksoft.dtos.entornos.EntornoCliente;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.MessageBundle;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@Category(Dto.class)
public class TestEntornoCliente extends TestCase {

	private static final String clase = "ar.com.thinksoft.business.dtos.entornos.TestEntornoCliente";

	String error = null;
	private String testName = null;

	public TestEntornoCliente( String testName )  {
		super( testName );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		testName = getName();
	}

	public static Test suite() {
		return new TestSuite( TestEntornoCliente.class );
	}

	public void testEntornoCliente1() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				new ar.com.thinksoft.dtos.entornos.EntornoCliente();
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

	public void testEntornoCliente2() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				Long idEntorno = 1L;
				Long idCliente = 1L;
				new ar.com.thinksoft.dtos.entornos.EntornoCliente(idEntorno, idCliente);
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

	public void testEntornoCliente3() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				Long idEntorno = 1L;
				Long idCliente = 1L;
				String entorno = null;
				new ar.com.thinksoft.dtos.entornos.EntornoCliente(idEntorno, idCliente, entorno);
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

	public void testDefaultBuilder() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				EntornoCliente ret = EntornoCliente.defaultBuilder();
				if (ret == null) {
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

	public void testEqualsNull() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				ar.com.thinksoft.dtos.entornos.EntornoCliente objeto = new ar.com.thinksoft.dtos.entornos.EntornoCliente();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.EntornoCliente.class, "equals", objeto, null, EXPECTED.FALSE);
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
				ar.com.thinksoft.dtos.entornos.EntornoCliente objeto = new ar.com.thinksoft.dtos.entornos.EntornoCliente();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.EntornoCliente.class, "equals", objeto, null, EXPECTED.FALSE);
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
				ar.com.thinksoft.dtos.entornos.EntornoCliente objeto = new ar.com.thinksoft.dtos.entornos.EntornoCliente();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.EntornoCliente.class, "equals", objeto, null, EXPECTED.FALSE);
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
				ar.com.thinksoft.dtos.entornos.EntornoCliente objeto = new ar.com.thinksoft.dtos.entornos.EntornoCliente();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.EntornoCliente.class, "equals", objeto, objeto.clon(), EXPECTED.TRUE);
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
				ar.com.thinksoft.dtos.entornos.EntornoCliente objeto = new ar.com.thinksoft.dtos.entornos.EntornoCliente();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.EntornoCliente.class, "equals", objeto, objeto, EXPECTED.TRUE);
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
				ar.com.thinksoft.dtos.entornos.EntornoCliente objeto = new ar.com.thinksoft.dtos.entornos.EntornoCliente();
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

		CustomTest a = TestEntornoCliente.class.getMethod(testName).getAnnotation(CustomTest.class);
		Commons.logTestResult(clase + "." + testName, (a != null ? a.motivo() : null));
	}

}

