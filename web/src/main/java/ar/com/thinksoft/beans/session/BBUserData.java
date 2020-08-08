package ar.com.thinksoft.beans.session;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ar.com.thinksoft.delegators.Seguridad;
import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.MessageManager;
import ar.com.thinksoft.utils.Utils;

@ManagedBean(name="bbUserData")
@SessionScoped
public class BBUserData implements Serializable {

	private static final long serialVersionUID = 5700827058566892886L;

	public BBUserData() {
	}

	public void getDummy() {
		try {
			Seguridad.selectSysdate();
		} catch(Exception ex) {
			MessageManager.addToMessages(ex);
		}
	}

	public void redirectLogin() {
		Utils.desconectarLogin();
	}

	public String getCurrentPage() {
		return FacesUtils.getCurrentPage();
	}

}
