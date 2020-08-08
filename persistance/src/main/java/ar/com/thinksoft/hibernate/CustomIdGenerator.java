package ar.com.thinksoft.hibernate;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomIdGenerator implements IdentifierGenerator {

	private static final String QUERY_CALL_STORE_PROC = "{? = call ts.general.f_next_id_tabla (?)}";

	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		CallableStatement callableStmt = null;
		Long result = null;
		String clase = null;
		try {
			clase = object.getClass().toString();
			clase = clase.substring(clase.lastIndexOf(".") + 1);
			clase = getTableNameForClass(clase);
			Connection connection = session.connection();
			callableStmt = connection.prepareCall(QUERY_CALL_STORE_PROC);
			callableStmt.registerOutParameter(1, java.sql.Types.BIGINT);
			callableStmt.setString(2, clase);
			callableStmt.executeQuery();
			// get result from out parameter #1
			result = callableStmt.getLong(1);
			//			connection.close();
		} catch (SQLException sqlException) {
			System.out.println("clase: " + clase);
			System.out.println("tabla: " + clase);
			throw new HibernateException(sqlException);
		}
		finally{
			try{ callableStmt.close(); }
			catch(Exception ex){ }
		}
		return result;
	}

	private String getTableNameForClass (String clase) {
		StringBuffer table = null;

		char[] chars = clase.toCharArray();

		for (int i=0; i<chars.length; i++) {
			if (table == null) {
				table = new StringBuffer("");
			}
			else {
				if (chars[i] >= 65 && chars[i] <= 90) {
					table.append("_");
				}
			}
			table.append(chars[i]);
		}

		return table.toString().toUpperCase();
	}

}
