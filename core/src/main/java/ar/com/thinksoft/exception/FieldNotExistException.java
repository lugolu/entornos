package ar.com.thinksoft.exception;

public class FieldNotExistException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4825019942074309701L;

	@Override
	public String getMessage() {	
		return "El Campo ingresado en el constructor de la clase WrapperTable no existe para la clase ingresada";
	}
}
