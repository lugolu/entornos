package ar.com.thinksoft.jsf.validators;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ar.com.thinksoft.utils.CommonFunctions;
import ar.com.thinksoft.utils.MessageBundle;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {
	private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

	public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
		if (facesContext == null) {
			throw new NullPointerException("facesContext");
		}
		if (uiComponent == null) {
			throw new NullPointerException("uiComponent");
		}

		if (value == null) {
			return;
		}

		if (!Pattern.matches(EMAIL_PATTERN, CommonFunctions.replaceAccentedChars(value.toString()))) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, MessageBundle.INFO, MessageBundle.INVALID_EMAIL_ERROR));
		}
	}

}