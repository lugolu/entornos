package ar.com.thinksoft.utils;

public class Error {

	private Long lineNumber;
	private String description;
	
	public Error() {
		
	}

	public Error(Long lineNumber, String description) {
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
}