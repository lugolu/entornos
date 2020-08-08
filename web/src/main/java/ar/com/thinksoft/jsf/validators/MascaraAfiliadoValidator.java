package ar.com.thinksoft.jsf.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.MessageBundle;
@FacesValidator("mascaraAfiliadoValidator")
public class MascaraAfiliadoValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object object) throws ValidatorException {
		if (object == null) {
			return;
		}

		String mascara = (String) FacesUtils.getSessionValue("Mascara");
		String valor = object.toString();

		if(mascara == null || mascara.isEmpty() || valor == null || valor.isEmpty()) {
			return;
		}

		if (mascara.length() != valor.length()) {
			throw new ValidatorException(new FacesMessage(MessageBundle.NRO_AFILIADO_ERROR));
		}

		StringBuilder valorMascara= new StringBuilder(mascara);
		StringBuilder valorInput=new StringBuilder(valor);



		for (int i = 0; i < valorMascara.length(); i++) {

			if(valorMascara.charAt(i) == '#'  && !Character.isDigit(valorInput.charAt(i))) {
				throw new ValidatorException(new FacesMessage(MessageBundle.NRO_AFILIADO_ERROR));
			}
			if(valorMascara.charAt(i) == 'a'  && !Character.isLetterOrDigit(valorInput.charAt(i))) {
				throw new ValidatorException(new FacesMessage(MessageBundle.NRO_AFILIADO_ERROR));
			}
			if(valorMascara.charAt(i) == '-' && valorInput.charAt(i) != '-') {
				throw new ValidatorException(new FacesMessage(MessageBundle.NRO_AFILIADO_ERROR));
			}
			if(valorMascara.charAt(i) == '/' && valorInput.charAt(i) != '/') {
				throw new ValidatorException(new FacesMessage(MessageBundle.NRO_AFILIADO_ERROR));
			}
		}
	}
}