package ar.com.thinksoft.beans.entornos;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.thinksoft.beans.session.UserSession;
import ar.com.thinksoft.utils.FacesUtils;

@ManagedBean(name = "bbCliente")
@ViewScoped
public class BBEntornos implements Serializable {

	private static final long serialVersionUID = -1153324977669803294L;

	private UserSession user;

	public BBEntornos() {
		user = FacesUtils.getUserSesssion();
	}

}
