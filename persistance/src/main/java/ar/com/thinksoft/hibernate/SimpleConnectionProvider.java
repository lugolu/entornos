package ar.com.thinksoft.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.service.jdbc.connections.internal.ConnectionProviderInitiator;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

import ar.com.thinksoft.utils.HandlerException;

public class SimpleConnectionProvider implements ConnectionProvider {

	private static final long serialVersionUID = -8645366349244315380L;
	
	private String url;
	private Properties connectionProps;
	private Connection conn;

	private static final Logger LOGGER = Logger.getLogger(SimpleConnectionProvider.class);

	public void configure(Properties props) throws HibernateException {
		String driverClass = props.getProperty(AvailableSettings.DRIVER);
		/*isolation = PropertiesHelper.getInteger(Environment.ISOLATION, props);
		if (isolation!=null)
		log.info( "JDBC isolation level: " + Environment.isolationLevelToString( isolation.intValue() ) );
		*/
		if (driverClass !=null) {
			try {
				// buscamos el driver
				Class.forName(driverClass);
			}
			catch (ClassNotFoundException cnfe) {
				try {
					ReflectHelper.classForName(driverClass);
				}
				catch (ClassNotFoundException e) {
					String msg = "No se encontro la clase del manejador de JDBC: " + driverClass;
					LOGGER.fatal(msg, e);
					throw new HibernateException(msg, e);
				}
			}
		} else {
			String msg = "No se especifico la clase del manejador de JDBC - " + AvailableSettings.DRIVER;
			LOGGER.fatal(msg);
			throw new HibernateException(msg);
		}
		url = props.getProperty(AvailableSettings.URL);
		if (url == null) {
			String msg = "No se especifico la URL para la cadena de conexion de JDBC - " + AvailableSettings.URL;
			LOGGER.fatal(msg);
			throw new HibernateException(msg);
		}
		connectionProps = ConnectionProviderInitiator.getConnectionProperties(props);
		//LOGGER.info("Usando el driver: " + driverClass + " con URL: " + url );
		//LOGGER.info("Propiedades de la conexion: " + PropertiesHelper.maskOut(connectionProps, "password"));
		/*System.out.println("Propiedades de la conexion: " + connectionProps );*/
	}

	@Override
	public Connection getConnection() throws SQLException {
		/*LOGGER.debug("Abriendo una nueva conexion con JDBC");
		System.out.println("Abriendo una nueva conexion con JDBC");*/
		if (conn != null && !conn.isClosed()) {
			conn.close();
			//System.out.println("\n======================================================================\n======================================================================\nExiste una conexion con JDBC: " + conn + "\nCerrada: " + conn.isClosed() + "\n======================================================================\n======================================================================");
		}
		conn = DriverManager.getConnection(url, connectionProps);
		// forzando el autocommit a falso para manejar manualmente todas las transacciones
		conn.setAutoCommit(false);
		return conn;
	}

	@Override
	public void closeConnection(Connection conn) throws SQLException {
		/*LOGGER.info("Cerrando la conexion con JDBC");
		System.out.println("Cerrando la conexion con JDBC");*/
		conn.close();
	}

	@Override
	protected void finalize() {
		close();
	}

	public void close() {
		url = null;
		connectionProps = null;
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				try {
					HandlerException.getInstancia().treateException(new HibernateException(e), SimpleConnectionProvider.class);
				} catch (Exception e1) {}
			}
		}
		conn = null;
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public boolean isUnwrappableAs(Class arg0) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) {
		return null;
	}
}
