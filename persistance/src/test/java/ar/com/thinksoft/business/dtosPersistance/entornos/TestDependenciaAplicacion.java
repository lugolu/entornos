package ar.com.thinksoft.business.dtosPersistance.entornos;

import java.util.List;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.spi.TransactionContext;
import org.junit.experimental.categories.Category;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.common.Dto;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.persistance.PersistanceFactory;
import ar.com.thinksoft.utils.CommonsTest;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.SeverityBundle;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@Category(Dto.class)
public class TestDependenciaAplicacion extends TestCase {

	private static final String clase = "ar.com.thinksoft.business.dtosPersistance.entornos.TestDependenciaAplicacion";

	String error = null;
	private String testName = null;

	private StatelessSession session = null;

	public TestDependenciaAplicacion( String testName )  {
		super( testName );
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		CommonsTest.getInstance();

		testName = getName();
		session = CommonsTest.getSession();
	}

	public static Test suite() {
		return new TestSuite( TestDependenciaAplicacion.class );
	}

	@CustomTest(parent="testInsert", motivo="insert")
	public void testInsert() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		if (Commons.ignoreInsert (clase + "." + testName)) {
			return;
		}

		try {
			StatelessSession session = null;
			Transaction t = null;
			try {
				session = CommonsTest.getSession();
				t = session.beginTransaction();
				ar.com.thinksoft.dtos.entornos.DependenciaAplicacion objeto = ar.com.thinksoft.dtos.entornos.DependenciaAplicacion.defaultBuilder();
				PersistanceFactory.getIntDomain().insert(objeto, session, getClass());
				((TransactionContext) session).managedFlush();
				t.rollback();
			} catch (Exception e) {
				if (t != null && t.isActive()) {
					t.rollback();
				}
				HandlerException.getInstancia().treateException(e,getClass());
			} finally {
				try {session.close();} catch (Exception e) {}
			}
			Commons.logInfoSuccess (clase, testName);
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

	@SuppressWarnings("unchecked")
	@CustomTest(parent="testSelectEqualOne", motivo="selectEqualOne")
	public void testSelectEqualOne() {
		if (Commons.ignoreFailed (clase, testName)) {
			return;
		}

		if (Commons.ignoreSuccess (clase, testName)) {
			return;
		}

		try {
			StatelessSession session = null;
			List<ar.com.thinksoft.dtos.entornos.DependenciaAplicacion> list = null;
			try {
				session = CommonsTest.getSession();
				ar.com.thinksoft.dtos.entornos.DependenciaAplicacion objeto = new ar.com.thinksoft.dtos.entornos.DependenciaAplicacion();
				objeto.setIdAplicacionCliente(1L);
				objeto.setIdAplicacionDepende(1L);
				list = PersistanceFactory.getIntDomain().selectEqual(objeto, session, getClass());
			} catch (Exception e) {
				HandlerException.getInstancia().treateException(e,getClass());
			} finally {
				try {session.close();} catch (Exception e) {}
			}
			if (list == null || list.size() == 0) {
				throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);
			}

			if (list.size() > 1) {
				throw new BusinessException(MessageBundle.TOO_MANY_RECORDS_FOUND_WARN, SeverityBundle.WARN);
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

		if (session != null) {
			session.close();
		}

		CustomTest a = TestDependenciaAplicacion.class.getMethod(testName).getAnnotation(CustomTest.class);
		Commons.logTestResult(clase + "." + testName, (a != null ? a.motivo() : null));
	}

}

