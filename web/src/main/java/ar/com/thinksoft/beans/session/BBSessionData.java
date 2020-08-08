package ar.com.thinksoft.beans.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.MessageManager;

@ManagedBean(name = "bbSessionData")
@SessionScoped
public class BBSessionData implements Serializable {

	private static final long serialVersionUID = 1117925069511153966L;

	private static final List<String> SESSION_VALUES;

	private String pathLogoInicioApp;
	private String pathLogoApp;
	private String pathLogoImpresion;
	private String pathLogoImpresionSmall;
	private String pathLogoCompInt;
	private String pathLogoTermAutoRecep;

	static {
		List<String> list = new LinkedList<String>();
		list.add("Paciente");
		list.add("PacienteUP");
		list.add("pedidoSeleccionado");
		list.add("detallesReceta");
		list.add("detallesPedido");
		list.add("matriculaSeleccionada");
		list.add("observaciones");
		SESSION_VALUES = Collections.unmodifiableList(list);
	}

	public BBSessionData() {
	}

	public void removeSessionValues(ActionEvent ae) {
		removeSessionValues();
	}

	public static void removeSessionValues() {
		removePrintFiles();
		for (String aux : SESSION_VALUES) {
			FacesUtils.removeSessionValue(aux);
		}
	}

	public static void removePrintFiles() {
		FacesUtils.removeSessionValue("PRINTER_Files2ExplorerView");
		FacesUtils.removeSessionValue("PRINTER_Files2Download");

		FacesUtils.removeSessionValue("Post_Param");
		FacesUtils.removeSessionValue("Post_URL");
		FacesUtils.removeSessionValue("Post_EMail");
	}

	public void actionBtnPrint() {
		BBSessionData.removePrintFiles();
	}

	public void unloadPackLogos(){
		pathLogoInicioApp = null;
		pathLogoApp = null;
		pathLogoImpresion = null;
		pathLogoImpresionSmall = null;
		pathLogoCompInt = null;
		pathLogoTermAutoRecep = null;
	}

	public void loadPackLogos(){
		try{
			unloadPackLogos();
		}
		catch(Exception ex){
			MessageManager.addToMessages(ex);
		}
	}

	// Getters && Setters
	public String getFiles2ExplorerView() {
		return (String) FacesUtils.getSessionValue("PRINTER_Files2ExplorerView");
	}

	public String getFiles2Download() {
		return (String) FacesUtils.getSessionValue("PRINTER_Files2Download");
	}

	public boolean isProcessClientFiles() {
		return getFiles2ExplorerView() != null || getFiles2Download() != null;
	}

	public boolean isDoPost() {
		return (getPostParam() != null);
	}

	public String getPostParam() {
		return (String) FacesUtils.getSessionValue("Post_Param");
	}

	public String getPostUrl() {
		return (String) FacesUtils.getSessionValue("Post_URL");
	}

	public String getPostEMail() {
		return (String) FacesUtils.getSessionValue("Post_EMail");
	}

	public String getPathLogoInicioApp() {
		return pathLogoInicioApp;
	}

	public String getPathLogoApp() {
		return pathLogoApp;
	}

	public String getPathLogoImpresion() {
		return pathLogoImpresion;
	}

	public String getPathLogoImpresionSmall() {
		return pathLogoImpresionSmall;
	}

	public String getPathLogoCompInt() {
		return pathLogoCompInt;
	}

	public String getPathLogoTermAutoRecep() {
		return pathLogoTermAutoRecep;
	}

}