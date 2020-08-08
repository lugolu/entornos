package ar.com.thinksoft.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("horaConverter")
public class HoraConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		String resultado = null;

		if (value != null && !"".equals(value)) {
			resultado = value.trim().replaceAll("\\p{Space}+", " ");
			if (resultado.length() == 1) {
				resultado = "0" + resultado + ":00";
			} else if (resultado.length() == 2) {
				resultado = resultado + ":00";
			} else if (resultado.length() == 3) {
				resultado = resultado.substring(0, 2) + ":00";
			} else if (resultado.length() == 4) {
				resultado = resultado.substring(0, 3) + "0" + resultado.substring(3, 4);
			} else if (resultado.length() > 5) {
				resultado = resultado.substring(0, 5);
			}
		}

		return resultado;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String resultado = null;

		if (value != null) {
			resultado = value.toString();
		}

		return resultado;
	}

}