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

@FacesConverter("fechaConverterOnlyHours")
public class FechaConverterOnlyHours implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		
		String error = null;
		try {
			SimpleDateFormat sdf = null;
			String hora = null;
			String fecha = null;
			if (value.indexOf(" ") > 0) {
				fecha = value.substring(0, value.indexOf(" "));
				hora = value.substring(value.indexOf(" "));
				if (hora.length() == 6) {
					sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					error = "No se reconoci\u00f3 el formato de la fecha. Ejemplo: 01/01/2009 12:00";
				} else if (hora.length() == 9) {
					sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					error = "No se reconoci\u00f3 el formato de la fecha. Ejemplo: 01/01/2009 12:00:00";
				} else {
					error = "No se reconoci\u00f3 el formato de la fecha. Ejemplo: 01/01/2009 12:00:00";
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, error));
				}
			} else if (value.length() > 7 && value.length() < 11) {
				fecha = value;
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				error = "No se reconoci\u00f3 el formato de la fecha. Ejemplo: 01/01/2009";
			} else {
				error = "No se reconoci\u00f3 el formato de la fecha. Ejemplo: 01/01/2009 12:00:00";
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_INFO, MessageBundle.INFO, error));
			}
			String dia = fecha.substring(0, 2);
			String mes = fecha.substring(3, 5);
			String anio = fecha.substring(6);
			// Analizo el a�o para ver si se ingres� en 2 digitos
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
		}

		catch (ParseException e) {
			throw new ConverterException(new FacesMessage("*", error));
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sdf.setTimeZone(java.util.TimeZone.getDefault());
		return (value != null) ? sdf.format(value) : "";
	}

}