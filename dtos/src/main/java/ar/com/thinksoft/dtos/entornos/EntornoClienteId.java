package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class EntornoClienteId implements Serializable, Cloneable {

	private static final long serialVersionUID = -159686978247400004L;

	@Column(name="id_entorno")
	private Long idEntorno;
	@Column(name="id_cliente")
	private Long idCliente;

	public EntornoClienteId() {
	}

	public EntornoClienteId(Long idEntorno, Long idCliente) {
		this.idEntorno = idEntorno;
		this.idCliente = idCliente;
	}

	public EntornoClienteId clon() throws CloneNotSupportedException {
		return (EntornoClienteId) super.clone();
	}

	public Long getIdEntorno() {
		return idEntorno;
	}

	public void setIdEntorno(Long idEntorno) {
		this.idEntorno = idEntorno;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EntornoClienteId)) {
			return false;
		}
		EntornoClienteId that = (EntornoClienteId) o;
		return Objects.equals(getIdEntorno(), that.getIdEntorno()) &&
				Objects.equals(getIdCliente(), that.getIdCliente());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdEntorno(), getIdCliente());
	}

}
