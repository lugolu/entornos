package ar.com.thinksoft.beans.configuracion;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import ar.com.thinksoft.beans.session.UserSession;
import ar.com.thinksoft.delegators.Entornos;
import ar.com.thinksoft.dtos.entornos.Cliente;
import ar.com.thinksoft.dtos.entornos.EntornoCliente;
import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.MessageManager;
import ar.com.thinksoft.utils.SeverityBundle;

@ManagedBean(name = "bbCliente")
@ViewScoped
public class BBCliente implements Serializable {

	private UserSession user;

	private List<Cliente> listClientes;
	private Cliente addingCliente;
	private Cliente selectedCliente;

	private List<EntornoCliente> listEntornos;
	private EntornoCliente addingEntorno;

	public BBCliente() {
		user = FacesUtils.getUserSesssion();

		addingCliente = new Cliente();

		addingEntorno = new EntornoCliente();

		initListClientes();

		initListEntornos();
	}

	private void initListClientes() {
		try {
			listClientes = Entornos.selectCliente(user.getSession());
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	private void initListEntornos() {
		try {
			listEntornos = new LinkedList<EntornoCliente>();
			if (selectedCliente != null) {
				EntornoCliente entorno = new EntornoCliente();
				entorno.setIdCliente(selectedCliente.getIdCliente());
				listEntornos = Entornos.selectEntornoClienteEqual(entorno, user.getSession());
			}
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnModificarCliente (Cliente item) {
		try {
			Entornos.updateCliente(item, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_UPDATE_INFO);
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnEliminarCliente (Cliente item) {
		try {
			Entornos.deleteCliente(item, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_DELETE_INFO);
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnAgregarCliente (ActionEvent ae) {
		try {
			Entornos.insertCliente(addingCliente, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_INSERT_INFO);
			addingCliente = new Cliente();
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void onSelectCliente (SelectEvent se) {
		initListEntornos();
	}

	public void actBtnModificarEntorno (EntornoCliente item) {
		try {
			Entornos.updateEntornoCliente(item, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_UPDATE_INFO);
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnEliminarEntorno (EntornoCliente item) {
		try {
			Entornos.deleteEntornoCliente(item, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_DELETE_INFO);
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public void actBtnAgregarEntorno (ActionEvent ae) {
		try {
			addingEntorno.setIdCliente(selectedCliente.getIdCliente());
			Entornos.insertEntornoCliente(addingEntorno, user.getSession());
			MessageManager.addToMessages(SeverityBundle.INFO, MessageBundle.ROW_INSERT_INFO);
			addingEntorno = new EntornoCliente();
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	public List<Cliente> getListClientes() {
		return listClientes;
	}

	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	}

	public Cliente getAddingCliente() {
		return addingCliente;
	}

	public void setAddingCliente(Cliente addingCliente) {
		this.addingCliente = addingCliente;
	}

	public Cliente getSelectedCliente() {
		return selectedCliente;
	}

	public void setSelectedCliente(Cliente selectedCliente) {
		this.selectedCliente = selectedCliente;
	}

	public List<EntornoCliente> getListEntornos() {
		return listEntornos;
	}

	public void setListEntornos(List<EntornoCliente> listEntornos) {
		this.listEntornos = listEntornos;
	}

	public EntornoCliente getAddingEntorno() {
		return addingEntorno;
	}

	public void setAddingEntorno(EntornoCliente addingEntorno) {
		this.addingEntorno = addingEntorno;
	}

}
