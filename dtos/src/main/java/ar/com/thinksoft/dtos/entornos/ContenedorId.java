package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class ContenedorId implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751381600008L;

	@Column(name="ID_CLIENTE")
	private Long idCliente;
	@Column(name="ID_SERVIDOR")
	private Long idServidor;
	@Column(name="CONTENEDOR")
	private String contenedor;

	public ContenedorId() {
	}

	public ContenedorId(Long idCliente, Long idServidor, String contenedor) {
		this.idCliente = idCliente;
		this.idServidor = idServidor;
		this.contenedor = contenedor;
	}

	public ContenedorId clon() throws CloneNotSupportedException {
		return (ContenedorId) super.clone();
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdServidor() {
		return idServidor;
	}

	public void setIdServidor(Long idServidor) {
		this.idServidor = idServidor;
	}

	public String getContenedor() {
		return contenedor;
	}

	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ContenedorId)) {
			return false;
		}
		ContenedorId that = (ContenedorId) o;
		return Objects.equals(getIdCliente(), that.getIdCliente()) &&
				Objects.equals(getIdServidor(), that.getIdServidor()) &&
				Objects.equals(getContenedor(), that.getContenedor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdCliente(), getIdServidor(), getContenedor());
	}

}
