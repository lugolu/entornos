package ar.com.thinksoft.delegators;

import java.util.Date;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.business.BusinessFactory;
import ar.com.thinksoft.dtos.seguridad.Usuario;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.jdbc.Conexion;
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

	// --------------------------------------------------------------------------------
	// SEGURIDAD
	// --------------------------------------------------------------------------------
	public static Conexion getConexion(Usuario usuario) throws BusinessException {
		return HibernateSessionFactory.getInstance().getConexion(usuario);
	}

	public static String selectServiceName() throws BusinessException {
		return HibernateSessionFactory.getInstance().selectServiceName();
	}

	public static String getPrivateUserName() throws BusinessException{
		return HibernateSessionFactory.getInstance().getUSER_NAME();
	}

	// *************************************************************************************** //
	// Usuario
	// *************************************************************************************** //

	public static Usuario loginUsuario(Usuario usuario, StatelessSession s) throws BusinessException {
		return BusinessFactory.getIntBusUsuario().loginUsuario(usuario, s);
	}

}
