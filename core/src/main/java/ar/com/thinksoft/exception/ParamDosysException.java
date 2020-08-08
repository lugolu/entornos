package ar.com.thinksoft.exception;

public class ParamDosysException extends BusinessException {

	private static final long serialVersionUID = 8116588151041526042L;

	public ParamDosysException(String messageBundle, String severityBundle) {
		super(messageBundle, severityBundle);
	}

}
