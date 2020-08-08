package ar.com.thinksoft.exception;

public class NoRowSelectedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6215450708060801436L;

	@Override
	public String getMessage() {	
		return "No hay filas seleccionadas";
	}	
}
