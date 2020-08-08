package ar.com.thinksoft.jsf.converters;

import java.text.DecimalFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import ar.com.thinksoft.utils.MessageBundle;

@FacesConverter("doubleConverter")
public class DoubleConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {
		try {
			if (string == null || string.equals("")) {
				return null;
			}

			string = string.replace(",", ".");
			return new Double(string);
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, "El valor no es un n\u00famero v\u00e1lido"));
		}
	}

	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		try {
			if (object != null) {
				return new DecimalFormat("###.##").format(object);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}

}