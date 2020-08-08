package ar.com.thinksoft.beans.seguridad;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.thinksoft.beans.session.UserSession;
import ar.com.thinksoft.dtos.seguridad.Usuario;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.MessageManager;
import ar.com.thinksoft.utils.SeverityBundle;
import ar.com.thinksoft.utils.Utils;

@ManagedBean(name = "bbLogin")
@ViewScoped
public class BBLogin implements Serializable {

	private static final long serialVersionUID = 102963092093395163L;

	private UserSession user;

	private String usuario;
	private String contrasena;

	private int contadorIntentos;

	public BBLogin() {
		try{
			FacesUtils.removeSessionValue("LoginExterno");
		} catch(Exception ex){
			MessageManager.addToMessages(ex);
		}

		Exception exception=(Exception) FacesUtils.getSessionValue("ExceptionLogin");
		if (exception != null){
			MessageManager.addToMessages(exception);
			FacesUtils.removeSessionValue("ExceptionLogin");
		}
		contadorIntentos = 0;
		usuario = "a";
		contrasena = "a";
	}

	// METHOD
	private void login(boolean hash) {
		try {
			if (usuario == null || usuario.isEmpty() || contrasena.isEmpty()) {
				throw new BusinessException(MessageBundle.INVALID_DATA_ERROR, SeverityBundle.INFO);
			}
			contador();

			Usuario tmp = new Usuario();
			if (hash) {
				tmp.setPassword(Utils.getHash(contrasena.toLowerCase(), "THINKSOFT", "SHA-256"));
			}
			else {
				tmp.setPassword(contrasena.toLowerCase());
			}
			tmp.setUsuario(usuario);
			user = new UserSession(tmp);
			Utils.prepareNewUserLogin();
			FacesUtils.setSessionValue("UserSession", user);
			FacesUtils.setSessionValue("LoginPage", new String("/pages/login.faces"));

			FacesUtils.redirect("/pages/inicio.faces");
		}
		catch (Exception e) {
			MessageManager.addToMessages(e);
		}
	}

	private void contador() throws BusinessException {
		if (contadorIntentos > 1) {
			contadorIntentos = 3;
			throw new BusinessException(MessageBundle.INTENTO_SUPERADOS, SeverityBundle.WARN);
		}
		contadorIntentos++;
	}

	// EVENT
	public void onLogin() {
		login(true);
	}

	public void onLoginNoHash() {
		login(false);
	}

	// GETTER Y SETTER
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public int getContadorIntentos() {
		return contadorIntentos;
	}

}
