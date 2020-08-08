package ar.com.thinksoft.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.SeverityBundle;

public class Conexion {

	private enum CONNECTION_TYPE {ORACLE};

	public static int QUERY_TIME_OUT = 30;

	private Connection connection = null;
	private SessionFactory sessionFactory = null;
	private String reportConn = null;
	private CONNECTION_TYPE tipo = null;

	// restringimos constructor por defecto
	@SuppressWarnings("unused")
	private Conexion() {};

	protected Conexion(SessionFactory sessionFactoryImpl, String user, String pass) throws SQLException {
		reportConn = pass;
		sessionFactory = sessionFactoryImpl;
		connection = DriverManager.getConnection (sessionFactory.getConnectionUrl(), user, reportConn);
		connection.setAutoCommit(false);
		if (sessionFactory.getConnectionUrl().contains("oracle")) {
			tipo = CONNECTION_TYPE.ORACLE;
		}
	}

	public void rollback() {
		try {
			if (connection != null) {
				connection.rollback();
			}
		} catch (Exception e) {
			System.out.println("Error en rollback: " + e.toString());
		}
	}

	public void commit() throws SQLException{
		connection.commit();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException{
		PreparedStatement ps = connection.prepareStatement(sql);
		switch (tipo) {
		case ORACLE:
			ps = connection.prepareStatement(sql);
			break;
		default:
			ps = connection.prepareStatement(sql);
		}
		ps.setQueryTimeout(QUERY_TIME_OUT);
		return ps;
	}

	public PreparedStatement prepareStatementLongRaw(String sql) throws SQLException {
		PreparedStatement ps = null;
		switch (tipo) {
		case ORACLE:
			ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			break;
		default:
			ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}
		ps.setQueryTimeout(QUERY_TIME_OUT);
		return ps;
	}

	public CallableStatement prepareCall(String sql) throws SQLException{
		CallableStatement cs = null;
		switch (tipo) {
		case ORACLE:
			cs = connection.prepareCall(sql);
			break;
		default:
			cs = connection.prepareCall(sql);
		}
		cs.setQueryTimeout(QUERY_TIME_OUT);
		return cs;
	}

	public Statement createStatement() throws SQLException{
		Statement s = null;
		switch (tipo) {
		case ORACLE:
			s = connection.createStatement();
			break;
		default:
			s = connection.createStatement();
		}
		s.setQueryTimeout(QUERY_TIME_OUT);
		return s;
	}

	public StatelessSession getSession() throws BusinessException{
		if (connection == null) {
			throw new BusinessException(MessageBundle.CONNECTION_CLOSED_FATAL, SeverityBundle.FATAL);
		}
		return sessionFactory.getSession(connection);
	}

	public void close() throws SQLException {
		connection.close();
	}

	protected boolean isClosed() {
		boolean retorno = false;
		try {
			retorno = connection.isClosed();
		} catch (Exception e) {}
		return retorno;
	}

	public String getReportConexion() {
		return reportConn;
	}

	public Connection getReportConnection() {
		return connection;
	}

	@Override
	public void finalize() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {}
	}

}
