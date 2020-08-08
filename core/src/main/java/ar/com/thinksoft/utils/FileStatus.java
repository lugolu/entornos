package ar.com.thinksoft.utils;

import java.util.LinkedList;
import java.util.List;

public class FileStatus {

	private Long readedLines;
	private Long linesOK;
	private Long linesError;
	private List<Error> errors;
	private Boolean processing;
	private String error;
	private String mensaje;

	/**
	 * Constructor por defecto
	 */
	public FileStatus(){
		readedLines = new Long(0);
		linesOK = new Long(0);
		linesError = new Long(0);
		errors = new LinkedList<Error>();
		processing = new Boolean(true);
	}

	public Long getReadedLines() {
		return readedLines;
	}
	public void incrementReadedLines() {
		readedLines ++;
	}
	public Long getLinesOK() {
		return linesOK;
	}
	public void incrementLinesOK() {
		linesOK ++;
	}
	public Long getLinesError() {
		return linesError;
	}
	public void incrementLinesError() {
		linesError ++;
	}

	public void decreaseLinesOK(){
		linesOK --;
	}

	public void addError(Error error){
		if (!errors.contains(error)) {
			errors.add(error);
		}
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public Boolean getProcessing() {
		return processing;
	}

	public void setProcessing(Boolean processing) {
		this.processing = processing;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setReadedLines(Long readedLines) {
		this.readedLines = readedLines;
	}

	public void setLineasOK(Long linesOK) {
		this.linesOK = linesOK;
	}

	public void setLinesError(Long linesError) {
		this.linesError = linesError;
	}
}
