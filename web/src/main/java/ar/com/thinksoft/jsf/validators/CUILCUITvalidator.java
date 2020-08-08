package ar.com.thinksoft.jsf.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ar.com.thinksoft.utils.MessageBundle;
@FacesValidator("cuilCuitValidator")
public class CUILCUITvalidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object object) throws ValidatorException {
		if(object == null) {
			return;
		}
		String cuit = object.toString();

		try {
			Long.decode(cuit);
		} catch (NumberFormatException e) {
			throw new ValidatorException(new FacesMessage(MessageBundle.CUIL_CUIT_ERROR));
		}

		if (cuit.length() != 11) {
			throw new ValidatorException(new FacesMessage(MessageBundle.CUIL_CUIT_ERROR));
		}

		int factor = 5;

		Integer[] c = new Integer[11];
		int resultado = 0;

		for (int i = 0; i < 10; i++) {
			c[i] = Integer.decode(String.valueOf(cuit.charAt(i)));
			resultado = resultado + c[i] * factor;
			factor = (factor == 2) ? 7 : factor - 1;
		}

		int control = (11 - (resultado % 11)) % 11;

		if (control != Integer.decode(String.valueOf(cuit.charAt(10)))) {
			throw new ValidatorException(new FacesMessage(MessageBundle.CUIL_CUIT_ERROR));
		}
	}
}