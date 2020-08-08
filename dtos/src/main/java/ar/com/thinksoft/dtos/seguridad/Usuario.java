package ar.com.thinksoft.dtos.seguridad;

import java.io.Serializable;

public class Usuario implements Serializable, Cloneable {

	private static final long serialVersionUID = 4076963362017998941L;

	private String usuario;
	private String password;
	private String faullName;

	public Usuario() {
	}

	public Usuario(String usuario, String password, String faullName) {
		this.usuario = usuario;
		this.password = password;
		this.faullName = faullName;
	}

	public Usuario clon() throws CloneNotSupportedException {
		return (Usuario) super.clone();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFaullName() {
		return faullName;
	}

	public void setFaullName(String faullName) {
		this.faullName = faullName;
	}

	@Override
	public String toString() {
		return "Usuario [usuario=" + usuario + ", password=" + password + ", faullName=" + faullName + "]";
	}

}
