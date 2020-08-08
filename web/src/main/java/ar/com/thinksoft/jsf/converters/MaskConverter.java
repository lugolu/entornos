package ar.com.thinksoft.jsf.converters;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ar.com.thinksoft.utils.FacesUtils;

@FacesConverter("maskConverter")
public class MaskConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) {
		string=string.replace("-","");
		string=string.replace("_","");
		string=string.replace("/","");
		return string;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
		String mascara = (String) FacesUtils.getSessionValue("Mascara");
		String valor = object.toString();
		if (mascara == null || mascara.isEmpty()) {
			return valor;
		}
		mascara = mascara.replace('A', 'a');
		if (valor == null || valor.isEmpty()) {
			return mascara;
		}
		StringBuilder valorMascara=new StringBuilder(valor);

		if(valor.length()<mascara.length()){
			for (int i = (valor.length()-1); i < mascara.length(); i++) {
				if(i<valorMascara.length()){
					if(valorMascara.charAt(i)=='\u0000') {
						valorMascara.insert(i,mascara.charAt(i));
					}
				} else {
					valorMascara.insert(i,mascara.charAt(i));
				}
			}
		}

		for (int i = 0; i < mascara.length(); i++) {
			if((mascara.charAt(i)=='/' || mascara.charAt(i)=='-') ) {
				valorMascara.insert(i,mascara.charAt(i));
				valorMascara.deleteCharAt(valorMascara.length()-1);
			}
			if((mascara.charAt(i)=='a' || mascara.charAt(i)=='A') && !Character.isAlphabetic(valorMascara.charAt(i))) {
				valorMascara.deleteCharAt(i);
				valorMascara.insert(i,'_');
			}
			if(mascara.charAt(i)=='#' && !Character.isDigit(valorMascara.charAt(i))) {
				valorMascara.deleteCharAt(i);
				valorMascara.insert(i,'_');
			}
		}

		return valorMascara.toString();
	}
}