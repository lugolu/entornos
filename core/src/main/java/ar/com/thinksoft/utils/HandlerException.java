package ar.com.thinksoft.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;

import org.apache.log4j.Logger;
import org.hibernate.MappingException;
import org.hibernate.PropertyValueException;
import org.hibernate.QueryTimeoutException;
import org.hibernate.TransactionException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;

import ar.com.thinksoft.exception.BusinessException;
import jcifs.smb.SmbException;

public class HandlerException {

	private static HandlerException instancia = new HandlerException();

	private HandlerException() {
	}

	public static HandlerException getInstancia() {
		return instancia;
	}

	public void treateException(Exception e, Class clase) throws BusinessException {
		StringBuffer messageError = new StringBuffer(((e.getMessage() != null) ? e.getMessage() : "Desconocido") + "\n Exception.Class: ");
		String causeError;
		if (e.getCause() != null && e.getCause().toString().indexOf("\n") >= 0) {
			causeError = "\n Cause:" + e.getCause().toString().substring(0,e.getCause().toString().indexOf("\n"));
		} else if (e.getCause() != null) {
			causeError = "\n Cause:" + e.getCause().toString();
		} else {
			causeError = "";
		}

		String stackTrace = "\n StackTrace:" + printStack(e);

		if (e instanceof BusinessException) {
			throw (BusinessException) e;
		} else if (e.getClass().equals(ConstraintViolationException.class)) {
			ConstraintViolationException cve = (ConstraintViolationException) e;
			// Logueo del error
			messageError.append("CONSTRAINT VIOLATION EXCEPTION" + causeError + "\n ErrorCode: " + cve.getErrorCode());
			if (cve.getSQLException() != null && !"".equals(cve.getSQLException().toString().trim())) {
				messageError.append("\n SQLException:" + cve.getSQLException().toString().trim());
			}
			if (cve.getSQL() != null && !"".equals(cve.getSQL().toString().trim()) && !"???".equals(cve.getSQL().toString().trim())) {
				messageError.append("\n LastSQL:" + cve.getSQL().toString().trim());
			}
			messageError.append(stackTrace);
			Logger logger = null;
			if (causeError.contains("NN_21_TMP_RECEPCION_AMB")) {
				logger = Logger.getLogger("otros.errores");
			}
			else {
				logger = Logger.getLogger(clase);
			}
			if (cve.getErrorCode() == 942 || cve.getErrorCode() == 17002 || cve.getErrorCode() == 17008) {
				logger.fatal(messageError);
			} else {
				logger.error(messageError);
			}
			translateError(cve.getErrorCode(), cve.getCause(), cve.getSQLException(), logger);
		} else if (e.getClass().equals(DataException.class)) {
			DataException de = (DataException) e;
			// Logueo del error
			messageError.append("DATA EXCEPTION" + causeError + "\n ErrorCode: " + de.getErrorCode());
			if (de.getSQLException() != null && !"".equals(de.getSQLException().toString().trim())) {
				messageError.append("\n SQLException:" + de.getSQLException().toString().trim());
			}
			if (de.getSQL() != null && !"".equals(de.getSQL().toString().trim()) && !"???".equals(de.getSQL().toString().trim())) {
				messageError.append("\n LastSQL:" + de.getSQL().toString().trim());
			}
			messageError.append(stackTrace);
			Logger logger = Logger.getLogger(clase);
			if (de.getErrorCode() == 942 || de.getErrorCode() == 17002 || de.getErrorCode() == 17008) {
				logger.fatal(messageError);
			} else {
				logger.error(messageError);
			}
			translateError(de.getErrorCode(), de.getCause(), de.getSQLException(), logger);
		} else if (e.getClass().equals(GenericJDBCException.class)) {
			GenericJDBCException jdbc = (GenericJDBCException) e;
			// Logueo del error
			messageError.append("GENERIC JDBC EXCEPTION" + causeError + "\n ErrorCode: " + jdbc.getErrorCode());
			if (jdbc.getSQLException() != null && !"".equals(jdbc.getSQLException().toString().trim())) {
				messageError.append("\n SQLException:" + jdbc.getSQLException().toString().trim());
			}
			if (jdbc.getSQL() != null && !"".equals(jdbc.getSQL().toString().trim()) && !"???".equals(jdbc.getSQL().toString().trim())) {
				messageError.append("\n LastSQL:" + jdbc.getSQL().toString().trim());
			}
			messageError.append(stackTrace);
			Logger logger = Logger.getLogger(clase);
			if (jdbc.getErrorCode() == 942 || jdbc.getErrorCode() == 17002 || jdbc.getErrorCode() == 17008) {
				logger.fatal(messageError);
			} else {
				logger.error(messageError);
			}
			translateError(jdbc.getErrorCode(), jdbc.getCause(), jdbc.getSQLException(), logger);
		} else if (e.getClass().equals(SQLGrammarException.class)) {
			SQLGrammarException ge = (SQLGrammarException) e;
			messageError.append("SQL GRAMMAR EXCEPTION" + causeError + "\n ErrorCode: " + ge.getErrorCode());
			if (ge.getSQLException() != null && !"".equals(ge.getSQLException().toString().trim())) {
				messageError.append("\n SQLException:" + ge.getSQLException().toString().trim());
			}
			if (ge.getSQL() != null && !"".equals(ge.getSQL().toString().trim()) && !"???".equals(ge.getSQL().toString().trim())) {
				messageError.append("\n LastSQL:" + ge.getSQL().toString().trim());
			}
			messageError.append(stackTrace);
			Logger logger = Logger.getLogger(clase);
			if (ge.getErrorCode() == 942 || ge.getErrorCode() == 17002 || ge.getErrorCode() == 17008) {
				logger.fatal(messageError);
			} else {
				logger.error(messageError);
			}
			translateError(ge.getErrorCode(), ge.getCause(), ge.getSQLException(), logger);
		} else if (e.getClass().equals(TransactionException.class)) {
			TransactionException te = (TransactionException) e;
			// Logueo del error
			messageError.append("TRANSACTION EXCEPTION");
			if (te.getCause() != null) {
				// si tiene una TransactionException como causa buscamos la causa original
				while (te.getCause().getClass().equals(TransactionException.class)) {
					te = (TransactionException) te.getCause();
				}
				if (te.getCause().getClass() == SQLException.class) {
					SQLException sql = (SQLException) te.getCause();
					messageError.append("  por SQL EXCEPTION" + causeError + "\n ErrorCode: " + sql.getErrorCode());
					if (sql != null && !"".equals(sql.toString().trim())) {
						messageError.append("\n SQLException:" + sql.toString().trim());
					}
					if (sql.getMessage() != null && !"".equals(sql.getMessage().trim()) && !"???".equals(sql.getMessage().trim())) {
						messageError.append("\n LastSQL:" + sql.getMessage().trim());
					}
					messageError.append(stackTrace);
					Logger logger = Logger.getLogger(clase);
					if (sql.getErrorCode() == 942 || sql.getErrorCode() == 17002 || sql.getErrorCode() == 17008) {
						logger.fatal(messageError);
					} else {
						logger.error(messageError);
					}
					translateError((sql.getMessage().indexOf("ORA-02292") > 0) ? 2292: sql.getErrorCode(), sql.getCause(), sql,logger);
				} else if (te.getCause().getClass() == SQLTransactionRollbackException.class) {
					SQLException sql = (SQLTransactionRollbackException) te.getCause();
					messageError.append("  por SQL TRANSACTION ROLLBACK EXCEPTION" + causeError + "\n ErrorCode: " + sql.getErrorCode());
					if (sql != null && !"".equals(sql.toString().trim())) {
						messageError.append("\n SQLException:" + sql.toString().trim());
					}
					if (sql.getMessage() != null && !"".equals(sql.getMessage().trim()) && !"???".equals(sql.getMessage().trim())) {
						messageError.append("\n LastSQL:" + sql.getMessage().trim());
					}
					messageError.append(stackTrace);
					Logger logger = Logger.getLogger(clase);
					logger.error(messageError);
					if (sql.getErrorCode() == 942 || sql.getErrorCode() == 17002 || sql.getErrorCode() == 17008) {
						logger.fatal(messageError);
					} else {
						logger.error(messageError);
					}
					translateError((sql.getMessage().indexOf("ORA-02292") > 0) ? 2292: sql.getErrorCode(), sql.getCause(), sql,logger);
				} else {
					messageError.append("  por " + te.getCause().getClass().getSimpleName().toUpperCase() + causeError + stackTrace);
					Logger logger = Logger.getLogger(clase);
					logger.fatal(messageError);
					throw new BusinessException(MessageBundle.UNKNOWN_ERROR_FATAL, SeverityBundle.FATAL,-999, te.getCause());
				}
			} else {
				messageError.append("  de origen desconocido " + causeError + stackTrace);
				Logger logger = Logger.getLogger(clase);
				logger.fatal(messageError);
				throw new BusinessException(MessageBundle.UNKNOWN_ERROR_FATAL, SeverityBundle.FATAL,-999, te.getCause());
			}
		} else if (e.getClass().equals(JDBCConnectionException.class)) {
			JDBCConnectionException jdbc = (JDBCConnectionException) e;
			// Logueo del error
			messageError.append("JDBC CONNECTION EXCEPTION" + causeError + "\n ErrorCode: " + jdbc.getErrorCode());
			if (jdbc.getSQLException() != null && !"".equals(jdbc.getSQLException().toString().trim())) {
				messageError.append("\n SQLException:" + jdbc.getSQLException().toString().trim());
			}
			if (jdbc.getSQL() != null && !"".equals(jdbc.getSQL().toString().trim()) && !"???".equals(jdbc.getSQL().toString().trim())) {
				messageError.append("\n LastSQL:" + jdbc.getSQL().toString().trim());
			}
			messageError.append(stackTrace);
			Logger logger = Logger.getLogger(clase);
			logger.fatal(messageError);
			throw new BusinessException(MessageBundle.DB_CONNECTION_FATAL,SeverityBundle.FATAL, jdbc.getErrorCode(), jdbc.getCause());
		} else if (e.getClass().equals(PropertyValueException.class)) {
			PropertyValueException pve = (PropertyValueException) e;
			// Logueo del error
			messageError.append("PROPERTY VALUE EXCEPTION" + causeError + "\n PropertyName :" + pve.getPropertyName()
			+ " EntityName: " + pve.getEntityName() + stackTrace);
			Logger logger = Logger.getLogger(clase);
			logger.fatal(messageError);
			throw new BusinessException(MessageBundle.PROPERTY_VALUE_FATAL,
					SeverityBundle.FATAL, -99, pve.getCause());
		} else if (e.getClass().equals(QueryTimeoutException.class)) {
			QueryTimeoutException qte = (QueryTimeoutException) e;
			messageError.append("QUERY TIMEOUT EXCEPTION" + causeError + "\n ErrorCode: " + qte.getErrorCode());
			messageError.append("\n SQLException:" + qte.getLocalizedMessage());
			if (qte.getSQL() != null) {
				messageError.append("\n LastSQL:" + qte.getSQL());
			}
			messageError.append(stackTrace);
			Logger logger = Logger.getLogger(clase);
			if (qte.getErrorCode() == 942 || qte.getErrorCode() == 17002 || qte.getErrorCode() == 17008) {
				logger.fatal(messageError);
			} else {
				logger.error(messageError);
			}
			translateError(qte.getErrorCode(), qte.getCause(), qte.getSQLException(), logger);
		} else if (e.getClass().equals(SQLException.class)) {
			SQLException ge = (SQLException) e;
			messageError.append("SQL EXCEPTION" + causeError + "\n ErrorCode: " + ge.getErrorCode());
			messageError.append("\n SQLException:" + ge.toString().trim());
			if (ge.getSQLState() != null) {
				messageError.append("\n LastSQL:" + ge.getSQLState());
			}
			messageError.append(stackTrace);
			Logger logger = Logger.getLogger(clase);
			if (ge.getErrorCode() == 942 || ge.getErrorCode() == 17002 || ge.getErrorCode() == 17008) {
				logger.fatal(messageError);
			} else {
				logger.error(messageError);
			}
			translateError(ge.getErrorCode(), ge.getCause(), ge, logger);
		} else if (e.getClass().equals(IOException.class)) {
			IOException ioe = (IOException) e;
			// Logueo del error
			messageError.append("IO EXCEPTION" + causeError + "\n HashCode: " + ioe.hashCode()
			+ " LocalizedMessage :" + ioe.getLocalizedMessage() + stackTrace);
			Logger logger = Logger.getLogger(clase);
			logger.error(messageError);
			throw new BusinessException(MessageBundle.IO_EXCEPTION_ERROR,
					SeverityBundle.ERROR, -999, ioe.getCause());
		} else if (e.getClass().equals(MappingException.class)) {
			MappingException he = (MappingException) e;
			if (he.getCause() == null) {
				// Logueo del error
				messageError.append("EXCEPTION  por  " + e.getClass().getSimpleName() + causeError + stackTrace);
				Logger logger = Logger.getLogger(clase);
				logger.error(messageError);
				throw new BusinessException(MessageBundle.EXCEPTION_ERROR,SeverityBundle.ERROR, -9999, e.getCause());
			}
			else {
				// Logueo del error
				messageError.append("HIBERNATE EXCEPTION" + causeError + "\n HashCode: " + he.hashCode()
				+ " LocalizedMessage :" + he.getLocalizedMessage()
				+ "\n Causa: " + ((InvocationTargetException)he.getCause().getCause()).getTargetException()
				+ stackTrace);
				Logger logger = Logger.getLogger(clase);
				logger.error(messageError);
				throw new BusinessException(MessageBundle.HIBERNATE_EXCEPTION_ERROR, SeverityBundle.ERROR, -999, he.getCause());
			}
		} else if (e.getClass().equals(SmbException.class)
				|| e.getClass().equals(FileNotFoundException.class)) {
			Logger logInterfaz = Logger.getLogger("org.quartz");
			messageError.append("SMBEXCEPTION  por  " + e.getClass().getSimpleName() + causeError + stackTrace);
			logInterfaz.error(messageError);
			throw new BusinessException(MessageBundle.EXCEPTION_ERROR,SeverityBundle.ERROR, -9999, e.getCause());
		} else {
			// Logueo del error
			messageError.append("EXCEPTION  por  " + e.getClass().getSimpleName() + causeError + stackTrace);
			Logger logger = Logger.getLogger(clase);
			logger.error(messageError);
			throw new BusinessException(MessageBundle.EXCEPTION_ERROR,SeverityBundle.ERROR, -9999, e.getCause());
		}
	}

	private void translateError(int error, Throwable cause,SQLException sqlError, Logger logger) throws BusinessException {
		if (error > 20099 && error < 20200 && sqlError.getMessage().startsWith("ORA-20")) {
			throw new BusinessException(sqlError.getMessage().substring(11).split("ORA-")[0],SeverityBundle.WARN, error, cause);
		}
		if (error > 20199 && error < 20300 && sqlError.getMessage().startsWith("ORA-20")) {
			throw new BusinessException(sqlError.getMessage().substring(11).split("ORA-")[0],SeverityBundle.INFO, error, cause);
		}
		if (error > 19999 && error < 21000 && sqlError.getMessage().startsWith("ORA-20")) {
			throw new BusinessException(sqlError.getMessage().substring(11).split("ORA-")[0],SeverityBundle.ERROR, error, cause);
		}
		if (error == 2290 && sqlError.getMessage().contains("TS.NN_")) {
			throw new BusinessException(MessageBundle.REQUIRED_FIELD_ERROR,SeverityBundle.WARN, error, cause);
		}
		switch (error) {
		case 1:	    throw new BusinessException(MessageBundle.ROW_ALREADY_EXIST_ERROR,SeverityBundle.ERROR, error, cause);
		case 911:
		case 922:   throw new BusinessException(MessageBundle.INVALID_PASSWORD_ERROR, SeverityBundle.WARN);
		case 942:   throw new BusinessException(MessageBundle.TABLE_DOES_NOT_EXIST_FATAL,SeverityBundle.FATAL, error, cause);
		case 988:	throw new BusinessException(MessageBundle.INVALID_PASSWORD_ERROR, SeverityBundle.WARN);
		case 1017:  throw new BusinessException(MessageBundle.INVALID_USER_PASSWORD_ERROR,SeverityBundle.WARN, error, cause);
		case 1045:  throw new BusinessException(MessageBundle.USUARIO_NO_ACCESS_ERROR, SeverityBundle.ERROR, error, cause);
		// error de SQL Server
		case 515:
		case 1400:  throw new BusinessException(MessageBundle.REQUIRED_FIELD_ERROR,SeverityBundle.WARN, error, cause);
		case 1401:	throw new BusinessException(MessageBundle.INVALID_USER_NAME_ERROR,SeverityBundle.WARN, error, cause);
		case 1403:	throw new BusinessException(MessageBundle.NO_RECORDS_FOUND_WARN,SeverityBundle.WARN, error, cause);
		case 1407:  throw new BusinessException(MessageBundle.REQUIRED_FIELD_ERROR,SeverityBundle.WARN, error, cause);
		case 1438: 	throw new BusinessException(MessageBundle.PRECISION_FIELD_ERROR,SeverityBundle.WARN, error, cause);
		case 1740:
		case 1756:  throw new BusinessException(MessageBundle.INVALID_USER_NAME_ERROR,SeverityBundle.WARN, error, cause);
		case 1918:	throw new BusinessException(MessageBundle.USER_NOT_EXIST_WARN,SeverityBundle.WARN, error, cause);
		case 1920:  throw new BusinessException(MessageBundle.USER_ALREADY_EXIST_ERROR,SeverityBundle.WARN, error, cause);
		case 1935:  throw new BusinessException(MessageBundle.INVALID_USER_NAME_ERROR,SeverityBundle.WARN, error, cause);
		case 1940:  throw new BusinessException(MessageBundle.DROP_USER_ERROR,SeverityBundle.WARN, error, cause);
		case 2091:  throw new BusinessException(MessageBundle.TRANSACTION_ERROR,SeverityBundle.WARN, error, cause);
		case 2290:
		case 2291:  throw new BusinessException(MessageBundle.OUT_OF_RANGE_ERROR,SeverityBundle.WARN, error, cause);
		case 2292:  throw new BusinessException(MessageBundle.CHILD_FOUND_ERROR,SeverityBundle.WARN, error, cause);
		case 1461:
		case 12899: throw new BusinessException(MessageBundle.VALUE_TOO_LARGE_ERROR,SeverityBundle.WARN, error, cause);
		case 17002:
		case 17008: throw new BusinessException(MessageBundle.DB_CONNECTION_FATAL,SeverityBundle.FATAL, error, cause);
		case 28000: throw new BusinessException(MessageBundle.ACCOUNT_LOCKED_ERROR,SeverityBundle.WARN, error, cause);
		case 28001: throw new BusinessException(MessageBundle.PASSWORD_EXPIRED_WARN,SeverityBundle.WARN, error, cause);
		case 28002: throw new BusinessException(MessageBundle.PASSWORD_WILL_EXPIRE_SOON_WARN,SeverityBundle.WARN, error, cause);
		case 28003:	throw new BusinessException(MessageBundle.INVALID_PASSWORD_ERROR, SeverityBundle.WARN);
		case 28007: throw new BusinessException(MessageBundle.PASSWORD_CANT_BE_REUSED_WARN,SeverityBundle.WARN, error, cause);
		case 28008: throw new BusinessException(MessageBundle.INVALID_USER_PASSWORD_ERROR,SeverityBundle.WARN, error, cause);
		case 28011: throw new BusinessException(MessageBundle.PASSWORD_WILL_EXPIRE_SOON_WARN,SeverityBundle.WARN, error, cause);

		default:    // Logueo del error
			String messageError = "TRANSLATE ERROR - UNIDENTIFIED (from previous exception)\t ErrorCode:" + error +
			"\n SQLException:" + sqlError.toString() + "\n Cause: " + cause;
			logger.fatal(messageError);
			throw new BusinessException(MessageBundle.UNKNOWN_ERROR_FATAL,SeverityBundle.FATAL, error, cause);
		}
	}

	public static String printStack(Exception e) {
		try {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			StringBuffer result = new StringBuffer();
			String[] lines = stringWriter.toString().split("\n");
			for (String line: lines) {
				line = line.trim();
				if (line.startsWith("at ar.com.thinksoft")) {
					result.append("\n\t" + line);
				}
			}
			return  result.toString();
		} catch (Exception e2) {
			return "Desconocido";
		}
	}

}
