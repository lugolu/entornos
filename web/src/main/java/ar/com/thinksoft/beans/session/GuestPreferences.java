package ar.com.thinksoft.beans.session;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import ar.com.thinksoft.utils.FacesUtils;

@ManagedBean(name = "guestPreferences")
@SessionScoped
public class GuestPreferences implements Serializable {

	private String contract = "babylon";
	private String layout = "custom";
	private String theme = "custom";
	private String menuMode = "layout-static";

	public GuestPreferences() {
	}

	private void changeTheme() {
		String aux = (String) FacesUtils.getSessionValue("t_theme");
		if(aux != null) {
			layout = aux;
			theme = aux;
		} else {
			layout = "custom";
			theme = "custom";
		}
	}

	public String getTheme() {
		changeTheme();
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getLayout() {
		changeTheme();
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getMenuMode() {
		return menuMode;
	}

	public void setMenuMode(String menuMode) {
		this.menuMode = menuMode;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

}
