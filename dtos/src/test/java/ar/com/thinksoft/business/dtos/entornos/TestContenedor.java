package ar.com.thinksoft.business.dtos.entornos;

import org.junit.experimental.categories.Category;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.Commons.EXPECTED;
import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.common.Dto;
import ar.com.thinksoft.dtos.entornos.Contenedor;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.MessageBundle;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@Category(Dto.class)
public class TestContenedor extends TestCase {

	private static final String clase = "ar.com.thinksoft.business.dtos.entornos.TestContenedor";

	String error = null;
	private String testName = null;

	public TestContenedor( String testName )  {
		super( testName );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		testName = getName();
	}

	public static Test suite() {
		return new TestSuite( TestContenedor.class );
	}

	public void testContenedor1() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			try {
				new ar.com.thinksoft.dtos.entornos.Contenedor();
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

	public void testContenedor2() {
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
				String contenedor = null;
				new ar.com.thinksoft.dtos.entornos.Contenedor(idCliente, idServidor, contenedor);
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

	public void testContenedor3() {
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
				String contenedor = null;
				Integer puerto = 1;
				new ar.com.thinksoft.dtos.entornos.Contenedor(idCliente, idServidor, contenedor, puerto);
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
				Contenedor ret = Contenedor.defaultBuilder();
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
				ar.com.thinksoft.dtos.entornos.Contenedor objeto = new ar.com.thinksoft.dtos.entornos.Contenedor();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.Contenedor.class, "equals", objeto, null, EXPECTED.FALSE);
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
				ar.com.thinksoft.dtos.entornos.Contenedor objeto = new ar.com.thinksoft.dtos.entornos.Contenedor();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.Contenedor.class, "equals", objeto, null, EXPECTED.FALSE);
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
				ar.com.thinksoft.dtos.entornos.Contenedor objeto = new ar.com.thinksoft.dtos.entornos.Contenedor();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.Contenedor.class, "equals", objeto, null, EXPECTED.FALSE);
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
				ar.com.thinksoft.dtos.entornos.Contenedor objeto = new ar.com.thinksoft.dtos.entornos.Contenedor();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.Contenedor.class, "equals", objeto, objeto.clon(), EXPECTED.TRUE);
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
				ar.com.thinksoft.dtos.entornos.Contenedor objeto = new ar.com.thinksoft.dtos.entornos.Contenedor();
				error = Commons.methodTest(clase, testName, ar.com.thinksoft.dtos.entornos.Contenedor.class, "equals", objeto, objeto, EXPECTED.TRUE);
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
				ar.com.thinksoft.dtos.entornos.Contenedor objeto = new ar.com.thinksoft.dtos.entornos.Contenedor();
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

		CustomTest a = TestContenedor.class.getMethod(testName).getAnnotation(CustomTest.class);
		Commons.logTestResult(clase + "." + testName, (a != null ? a.motivo() : null));
	}

}

