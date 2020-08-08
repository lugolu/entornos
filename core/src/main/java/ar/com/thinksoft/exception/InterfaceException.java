package ar.com.thinksoft.exception;

@SuppressWarnings("serial")
public class InterfaceException extends Exception {	
	
	private String message;
	private Throwable cause;
	
	
	public InterfaceException(){		
	}

	public InterfaceException(String message) {	
		super();
		this.message = message;
	}
	
	public InterfaceException(String message, Throwable cause) {
		super();
		this.message = message;
		this.cause = cause;
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

}
