package ar.com.thinksoft.beans.configuracion;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.thinksoft.beans.session.UserSession;
import ar.com.thinksoft.delegators.Entornos;
import ar.com.thinksoft.dtos.entornos.Aplicacion;
import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.MessageManager;
import ar.com.thinksoft.utils.SeverityBundle;

@ManagedBean(name = "bbAplicacion")
@ViewScoped
public class BBAplicacion implements Serializable {

	private static final long serialVersionUID = 6334106821882179743L;

	private UserSession user;

	private List<Aplicacion> listAplicaciones;
	private Aplicacion addingAplicacion;
	private Aplicacion selectedAplicacion;

	public BBAplicacion() {
		user = FacesUtils.getUserSesssion();

		addingAplicacion = new Aplicacion();

		initListAplicaciones();
	}

	private void initListAplicaciones() {
		try {
			listAplicaciones = Entornos.selectAplicacion(user.getSession());
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnModificarAplicacion (Aplicacion item) {
		try {
			Entornos.updateAplicacion(item, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_UPDATE_INFO);
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnEliminarAplicacion (Aplicacion item) {
		try {
			Entornos.deleteAplicacion(item, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_DELETE_INFO);
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnAgregarAplicacion (ActionEvent ae) {
		try {
			Entornos.insertAplicacion(addingAplicacion, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_INSERT_INFO);
			addingAplicacion = new Aplicacion();
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public List<Aplicacion> getListAplicaciones() {
		return listAplicaciones;
	}

	public void setListAplicaciones(List<Aplicacion> listAplicaciones) {
		this.listAplicaciones = listAplicaciones;
	}

	public Aplicacion getAddingAplicacion() {
		return addingAplicacion;
	}

	public void setAddingAplicacion(Aplicacion addingAplicacion) {
		this.addingAplicacion = addingAplicacion;
	}

	public Aplicacion getSelectedAplicacion() {
		return selectedAplicacion;
	}

	public void setSelectedAplicacion(Aplicacion selectedAplicacion) {
		this.selectedAplicacion = selectedAplicacion;
	}

}
