package ar.com.thinksoft.exception;

import ar.com.thinksoft.utils.Error;

@SuppressWarnings("serial")
public class FileProcessError extends Exception {

	private Long lineNumber;
	private String description;
	
	public FileProcessError() {
	}

	public FileProcessError(Long lineNumber, String description) {
		this.lineNumber = lineNumber;
		this.description = description;
	}

	public Long getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Error getError() {
		return new Error(lineNumber, description);
	}

}