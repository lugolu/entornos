package ar.com.thinksoft.delegators;

import java.util.Date;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.jdbc.HibernateSessionFactory;

public class Seguridad {

	public static String getConnectionUrl() {
		return HibernateSessionFactory.getInstance().getConnectionUrl();
	}

	public static String getSecurityConnectionUrl() {
		return HibernateSessionFactory.getInstance().getSecurityConnectionUrl();
	}

	public static String getDriverName() {
		return HibernateSessionFactory.getInstance().getDriverName();
	}

	public static String getJndiName() {
		return HibernateSessionFactory.getInstance().getJndiName();
	}

	public static String getPrivatePassword() {
		return HibernateSessionFactory.getInstance().getUSER_PASS();
	}

	public static Date selectSysdate() throws BusinessException {
		return HibernateSessionFactory.getInstance().selectSysdate();
	}

	public static String selectServiceName() throws BusinessException {
		return HibernateSessionFactory.getInstance().selectServiceName();
	}

	public static String getPrivateUserName() throws BusinessException{
		return HibernateSessionFactory.getInstance().getUSER_NAME();
	}

	public static String getPassword() throws BusinessException{
		return HibernateSessionFactory.getInstance().getPASSWORD();
	}

}
