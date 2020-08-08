package ar.com.thinksoft.jdbc;

import java.sql.Connection;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.exception.BusinessException;

public interface SessionFactory {

	public String getConnectionUrl();

	public StatelessSession getSession(Connection conn) throws BusinessException;

}
