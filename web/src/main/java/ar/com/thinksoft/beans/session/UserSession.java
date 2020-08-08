package ar.com.thinksoft.beans.session;


import org.hibernate.StatelessSession;

import ar.com.thinksoft.delegators.Seguridad;
import ar.com.thinksoft.dtos.seguridad.Usuario;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.jdbc.Conexion;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.SeverityBundle;

public class UserSession {

	private Usuario usuario;
	private Conexion conexion;

	public UserSession(Usuario usuario) throws BusinessException {
		conexion = Seguridad.getConexion(usuario);
		usuario = Seguridad.loginUsuario(usuario, conexion.getSession());
		if (usuario == null) {
			throw new BusinessException(MessageBundle.INVALID_USER_PASSWORD_ERROR, SeverityBundle.ERROR);
		}
	}

	public StatelessSession getSession() throws BusinessException {
		return conexion.getSession();
	}

	public void closeSession() {
		try { conexion.close(); }
		catch (Exception e) {}
	}

	// Getters && Setters
	public Conexion getConexion() {
		return conexion;
	}

	public void setConexion(Conexion conexion) {
		this.conexion = conexion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
