package ar.com.thinksoft.jsf.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ar.com.thinksoft.utils.MessageBundle;

@FacesValidator("horaValidator")
public class HoraValidator implements Validator {

	public void validate(FacesContext context, UIComponent component, Object object) throws ValidatorException {
		String valor = object.toString();

		if (valor.length() != 5) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.FORMATO_HORA_NO_VALIDO));
		}

		if (valor.indexOf(":") == -1) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.FORMATO_HORA_NO_VALIDO));
		}

		try {
			Integer hora = Integer.valueOf(valor.substring(0, 2));
			if (hora > 23) {
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.HORA_NO_VALIDA));
			}
		} catch (NumberFormatException e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.HORA_NO_VALIDA));
		}

		try {
			Integer minuto = Integer.valueOf(valor.substring(3, 5));
			if (minuto > 59) {
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.MINUTOS_NO_VALIDOS));
			}
		} catch (NumberFormatException e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.MINUTOS_NO_VALIDOS));
		}
	}
	
}