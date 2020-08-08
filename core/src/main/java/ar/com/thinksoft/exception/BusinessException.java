package ar.com.thinksoft.exception;

@SuppressWarnings("serial")
public class BusinessException extends Exception {

	private String message;
	private String severity;
	private int errorCode;
	private Throwable cause;


	public BusinessException(){
	}

	public BusinessException(String messageBundle, String severityBundle) {
		message  = messageBundle;
		severity = severityBundle;
	}

	public BusinessException(String message, String severity, int errorCode, Throwable cause) {
		super();
		this.message = message;
		this.severity = severity;
		this.errorCode = errorCode;
		this.cause = cause;
	}

	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	@Override
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public Throwable getCause() {
		return cause;
	}
	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}

}
