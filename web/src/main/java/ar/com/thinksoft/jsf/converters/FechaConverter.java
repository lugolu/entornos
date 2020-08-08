package ar.com.thinksoft.jsf.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import ar.com.thinksoft.utils.MessageBundle;

@FacesConverter("fechaConverter")
public class FechaConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || "".equals(value)) {
			return null;
		}

		String error = null;
		try {
			SimpleDateFormat sdf = null;
			String fecha = null;
			String hora = null;

			if (value.indexOf(" ") > 0) {
				fecha = value.substring(0, value.indexOf(" "));
				hora = value.substring(value.indexOf(" "));
				if (hora.length() == 6) {
					sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					error = "01/01/2012 12:00";
				} else if (hora.length() == 9) {
					sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					error = "01/01/2012 12:00:00";
				} else {
					error = "01/01/2012 12:00:00";
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.INVALID_FECHA_ERROR + error));
				}
			} else if (value.length() > 7 && value.length() < 11) {
				fecha = value;
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				error = "01/01/2012";
			} else {
				error = "01/01/2012 12:00:00";
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, MessageBundle.INVALID_FECHA_ERROR + error));
			}
			String dia = fecha.substring(0, 2);
			String mes = fecha.substring(3, 5);
			String anio = fecha.substring(6);
			// Analizo el año para ver si se o� en 2 digitos
			if (anio.length() == 2) {
				int anioParte2 = Integer.valueOf(anio);

				int anioActual = Integer.valueOf(sdf.format(new Date()).substring(6, 10));
				// completo los dos 1ros digitos segun el a�o ingresado,
				// redondeando los valores al siglo mas pr�ximo
				anio = ((anioActual - anioParte2 <= 1950) ? "19" : "20") + anio;
			}
			String fechaCompleta = dia + "/" + mes + "/" + anio + ((hora != null) ? hora : "");
			Date fechaFinal = sdf.parse(fechaCompleta);
			return fechaFinal;
		} catch (ParseException e) {
			throw new ConverterException(new FacesMessage("*", error));
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		sdf.setTimeZone(java.util.TimeZone.getDefault());
		return (value != null) ? sdf.format(value) : "";
	}

}