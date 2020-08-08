package ar.com.thinksoft.interfaces;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.exception.InterfaceException;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.SeverityBundle;
import jcifs.smb.SmbException;

public class ExceptionLogger {

	private static ExceptionLogger instancia = new ExceptionLogger();

	private ExceptionLogger() {
	}

	public static ExceptionLogger getInstancia() {
		return instancia;
	}

	public void logException(Throwable e) {
		logException(e, null);
	}

	public void logException(Throwable e, String loginName) {
		StringBuffer messageError = new StringBuffer(((e.getMessage() != null) ? e.getMessage() : "Desconocido") + "\n Exception.Class: ");
		StringBuffer stackTrace = new StringBuffer();
		if (loginName != null) {
			stackTrace.append("\n Usuario: " + loginName);
		} else {
			stackTrace.append("\n Usuario: desconocido");
		}
		printStack(stackTrace, e);
		String causeError;
		if (e.getCause() != null && e.getCause().toString().indexOf("\n") >= 0) {
			causeError = "\n Cause:" + e.getCause().toString().substring(0, e.getCause().toString().indexOf("\n"));
		} else if (e.getCause() != null) {
			causeError = "\n Cause:" + e.getCause().toString();
		} else {
			causeError = "";
		}

		Logger logger = null;
		if (messageError.toString().contains(MessageBundle.INVALID_USER_PASSWORD_ERROR)
				|| messageError.toString().contains(MessageBundle.PASSWORD_EXPIRED_WARN)
				|| messageError.toString().contains("changeUserPassword")
				|| messageError.toString().contains("createUser")) {
			logger = Logger.getLogger("otros.usuarios");
		}
		else if (causeError.contains("NN_21_TMP_RECEPCION_AMB")) {
			logger = Logger.getLogger("otros.errores");
		}
		else if (causeError.contains(MessageBundle.PACIENTE_NO_TIENE_PLAN_CONVENIO_CONFIGURADO)) {
			logger = Logger.getLogger("otros.errores");
		}
		else if (messageError.toString().contains("ImpScores.calcularScore")
				|| messageError.toString().contains("ImpScores.insertScorePac")) {
			logger = Logger.getLogger("otros.scores");
		}
		else {
			logger = Logger.getLogger(ExceptionLogger.class);
		}

		if (e instanceof BusinessException) {
			BusinessException be = (BusinessException) e;
			messageError.append("BUSINESS EXCEPTION" + causeError + "\n ErrorCode: " + be.getErrorCode());
			messageError.append(stackTrace);
			if (SeverityBundle.INFO.equals(((BusinessException) e).getSeverity())) {
				logger.info(messageError);
			} else if (SeverityBundle.WARN.equals(((BusinessException) e).getSeverity())) {
				logger.warn(messageError);
			} else if (SeverityBundle.ERROR.equals(((BusinessException) e).getSeverity())) {
				logger.error(messageError);
			} else if (SeverityBundle.FATAL.equals(((BusinessException) e).getSeverity())) {
				logger.fatal(messageError);
			}
		} else if (e.getClass().equals(InterfaceException.class)) {
			messageError.append("INTERFACE EXCEPTION" + causeError);
			messageError.append(stackTrace);
			logger.error(messageError);
		} else if (e.getClass().equals(NullPointerException.class)) {
			NullPointerException npe = (NullPointerException) e;
			messageError.append("NULL POINTER EXCEPTION" + causeError + "\n HashCode: " + npe.hashCode() + " LocalizedMessage :" + npe.getLocalizedMessage() + stackTrace);
			logger.error(messageError);
		} else if (e.getClass().equals(SmbException.class) || e.getClass().equals(FileNotFoundException.class)) {
			Logger logInterfaz = Logger.getLogger("ar.com.interfaz");
			messageError.append("SMBEXCEPTION  por  " + e.getClass().getSimpleName() + causeError + stackTrace);
			logInterfaz.error(messageError);
		} else {
			messageError.append("EXCEPTION  por  " + e.getClass().getSimpleName() + causeError + stackTrace);
			logger.error(messageError);
		}
	}

	public void logQuartzException(Exception e) {
		Logger logger = Logger.getLogger("org.quartz");
		StringBuffer messageError = new StringBuffer(((e.getMessage() != null) ? e.getMessage() : "Desconocido") + "\n Exception.Class: ");
		StringBuffer stackTrace = new StringBuffer();
		stackTrace.append("\n Usuario: desconocido");
		printStack(stackTrace, e);
		String causeError;
		if (e.getCause() != null && e.getCause().toString().indexOf("\n") >= 0) {
			causeError = "\n Cause:" + e.getCause().toString().substring(0, e.getCause().toString().indexOf("\n"));
		} else if (e.getCause() != null) {
			causeError = "\n Cause:" + e.getCause().toString();
		} else {
			causeError = "";
		}

		if (e instanceof BusinessException) {
			BusinessException be = (BusinessException) e;
			messageError.append("BUSINESS EXCEPTION" + causeError + "\n ErrorCode: " + be.getErrorCode());
			messageError.append(stackTrace);
			if (SeverityBundle.INFO.equals(((BusinessException) e).getSeverity())) {
				logger.info(messageError);
			} else if (SeverityBundle.WARN.equals(((BusinessException) e).getSeverity())) {
				logger.warn(messageError);
			} else if (SeverityBundle.ERROR.equals(((BusinessException) e).getSeverity())) {
				logger.error(messageError);
			} else if (SeverityBundle.FATAL.equals(((BusinessException) e).getSeverity())) {
				logger.fatal(messageError);
			}
		} else if (e instanceof InterfaceException) {
			messageError.append("INTERFACE EXCEPTION" + causeError);
			messageError.append(stackTrace);
			logger.error(messageError);
		} else if (e instanceof NullPointerException) {
			NullPointerException npe = (NullPointerException) e;
			messageError.append("NULL POINTER EXCEPTION" + causeError + "\n HashCode: " + npe.hashCode() + " LocalizedMessage :" + npe.getLocalizedMessage()
			+ stackTrace);
			logger.error(messageError);
		} else if (e.getClass().equals(SmbException.class) || e.getClass().equals(FileNotFoundException.class)) {
			Logger logInterfaz = Logger.getLogger("ar.com.interfaz");
			messageError.append("SMBEXCEPTION  por  " + e.getClass().getSimpleName() + causeError + stackTrace);
			logInterfaz.error(messageError);
		} else {
			messageError.append("EXCEPTION  por  " + e.getClass().getSimpleName() + causeError + stackTrace);
			logger.error(messageError);
		}
	}

	public static void printStack(StringBuffer appender, Throwable e) {
		try {
			appender.append("\n StackTrace:");
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			String[] lines = stringWriter.toString().split("\n");
			for (String line : lines) {
				line = line.trim();
				if (line.startsWith("at ar.com.thinksoft")) {
					appender.append("\n\t" + line);
				}
			}
		} catch (Exception e2) {
			appender.append(" Desconocido");
		}
	}

}
