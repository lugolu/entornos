package ar.com.thinksoft.beans.entornos;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import ar.com.thinksoft.beans.session.UserSession;
import ar.com.thinksoft.delegators.Entornos;
import ar.com.thinksoft.dtos.entornos.Cliente;
import ar.com.thinksoft.dtos.entornos.Servidor;
import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.MessageManager;
import ar.com.thinksoft.utils.Utils;

@ManagedBean(name = "bbEntornosTopbar")
@ViewScoped
public class BBEntornosTopbar implements Serializable {

	private static final long serialVersionUID = -8439330980044838974L;

	private UserSession user;

	private SelectItem[] selItemClientes;
	private Cliente selectedCliente;

	private Servidor addingServidor;
	private boolean displayPopupAgregarServidor;

	public BBEntornosTopbar() {
		user = FacesUtils.getUserSesssion();

		initSelItemCliente();

		selectedCliente = new Cliente();
	}

	private void initSelItemCliente() {
		try {
			List<Cliente> listClientes = Entornos.selectCliente(user.getSession());
			selItemClientes = Utils.populateSelectItem(listClientes, "cliente", "idCliente", true, false, "");
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void onChangeCliente (AjaxBehaviorEvent abe) {
		try {
			BBEntornos bbEntornos = (BBEntornos) FacesUtils.getBBCurrentInstance("bbEntornos");
			bbEntornos.getBbEntornosDiagram().actChangeCliente();
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void agregarServidor (ActionEvent ae) {
		try {
			addingServidor = new Servidor();
			displayPopupAgregarServidor = true;
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void aceptarAgregarServidor (ActionEvent ae) {
		try {
			addingServidor.setIdCliente(selectedCliente.getIdCliente());
			Entornos.insertServidor(addingServidor, user.getSession());

			BBEntornos bbEntornos = (BBEntornos) FacesUtils.getBBCurrentInstance("bbEntornos");
			bbEntornos.getBbEntornosDiagram().agregarServidor (addingServidor);

			addingServidor = new Servidor();

			displayPopupAgregarServidor = false;
			FacesUtils.hideDialogs("$popupAgregarServidor");
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public Cliente getSelectedCliente() {
		return selectedCliente;
	}

	public void setSelectedCliente(Cliente selectedCliente) {
		this.selectedCliente = selectedCliente;
	}

	public SelectItem[] getSelItemClientes() {
		return selItemClientes;
	}

	public Servidor getAddingServidor() {
		return addingServidor;
	}

	public void setAddingServidor(Servidor addingServidor) {
		this.addingServidor = addingServidor;
	}

	public boolean isDisplayPopupAgregarServidor() {
		return displayPopupAgregarServidor;
	}

}
