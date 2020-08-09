package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class ServidorId implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751483200014L;

	@Column(name="ID_CLIENTE")
	private Long idCliente;
	@Column(name="ID_SERVIDOR")
	private Long idServidor;

	public ServidorId() {
	}

	public ServidorId(Long idCliente, Long idServidor) {
		this.idCliente = idCliente;
		this.idServidor = idServidor;
	}

	public ServidorId clon() throws CloneNotSupportedException {
		return (ServidorId) super.clone();
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ServidorId)) {
			return false;
		}
		ServidorId that = (ServidorId) o;
		return Objects.equals(getIdCliente(), that.getIdCliente()) &&
				Objects.equals(getIdServidor(), that.getIdServidor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdCliente(), getIdServidor());
	}

}
