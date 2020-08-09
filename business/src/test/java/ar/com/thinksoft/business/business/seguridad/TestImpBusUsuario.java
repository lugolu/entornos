package ar.com.thinksoft.business.business.seguridad;

import org.hibernate.SessionException;
import org.hibernate.StatelessSession;
import org.junit.experimental.categories.Category;

import ar.com.thinksoft.business.BusinessFactory;
import ar.com.thinksoft.common.Business;
import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.dtos.seguridad.Usuario;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.jdbc.HibernateSessionFactory;
import ar.com.thinksoft.utils.CommonsTest;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.SeverityBundle;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@Category(Business.class)
public class TestImpBusUsuario extends TestCase {

	private static final String clase = "ar.com.thinksoft.business.business.seguridad.TestImpBusUsuario";

	String error = null;

	private String testName = null;

	public TestImpBusUsuario( String testName )  {
		super( testName );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		HibernateSessionFactory.getInstance();

		testName = getName();
	}

	public static Test suite() {
		return new TestSuite( TestImpBusUsuario.class );
	}

	public void testTestImpBusUsuario() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			new ar.com.thinksoft.business.seguridad.implementations.ImpBusUsuario();
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

	public void testLoginUsuario() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			StatelessSession session = null;
			try {
				session = CommonsTest.getSession();
				Usuario usuario = new Usuario();
				BusinessFactory.getIntBusUsuario().loginUsuario(usuario, session);
				try {
					session.createCriteria(Usuario.class);
					throw new BusinessException("Session is not closed.", SeverityBundle.WARN);
				} catch (Exception e) {
					if (!(e instanceof SessionException)) {
						throw new BusinessException("Session is not closed.", SeverityBundle.WARN);
					}
				}
			} catch (Exception e) {
				HandlerException.getInstancia().treateException(e,getClass());
			} finally {
				try {session.close();} catch (Exception e) {}
			}
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

		CustomTest a = TestImpBusUsuario.class.getMethod(testName).getAnnotation(CustomTest.class);
		Commons.logTestResult(clase + "." + testName, (a != null ? a.motivo() : null));
	}

}

