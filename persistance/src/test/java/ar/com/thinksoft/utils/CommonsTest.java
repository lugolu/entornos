package ar.com.thinksoft.utils;

import java.util.Date;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.jdbc.Conexion;
import ar.com.thinksoft.jdbc.HibernateSessionFactory;

public class CommonsTest {

	private static Date fechaActual;

	private CommonsTest () {
		Commons.logInfoSuccess("HibernateSessionFactory", "init");
		Commons.logTestResult("HibernateSessionFactory", "init");
		Commons.logInfo("HibernateSessionFactory.init");
		StatelessSession s = null;
		try {
			s = getSession();
		} catch (BusinessException e) {
			e.printStackTrace();
		} finally {
			try { s.close(); } catch (Exception e) { }
		}
		Commons.logInfoSuccess("HibernateSessionFactory", "end");
		Commons.logTestResult("HibernateSessionFactory", "end");
		Commons.logInfo("HibernateSessionFactory.end");
	}

	private static CommonsTest instance;
	public static CommonsTest getInstance() {
		return instance == null ? instance = new CommonsTest() : instance;
	}

	public static StatelessSession getSession() throws BusinessException {
		StatelessSession s = HibernateSessionFactory.getInstance().openPrivateSession();
		return s;
	}

	private static Conexion demoConexion;
	public static StatelessSession getDemoSession() throws BusinessException {
		if (demoConexion == null) {
			demoConexion = HibernateSessionFactory.getInstance().getConexion("demo", "test");
		}
		return demoConexion.getSession();
	}

	public static Date getFechaActual (StatelessSession session) throws BusinessException {
		if (fechaActual == null) {
			fechaActual = HibernateSessionFactory.getInstance().selectSysdateNoSessionClose(session);
		}
		return fechaActual;
	}

}
