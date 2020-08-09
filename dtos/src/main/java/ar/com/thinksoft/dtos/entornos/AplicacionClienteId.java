package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class AplicacionClienteId implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751317800004L;

	@Column(name="ID_APLICACION_CLIENTE")
	private Long idAplicacionCliente;

	public AplicacionClienteId() {
	}

	public AplicacionClienteId(Long idAplicacionCliente) {
		this.idAplicacionCliente = idAplicacionCliente;
	}

	public AplicacionClienteId clon() throws CloneNotSupportedException {
		return (AplicacionClienteId) super.clone();
	}

	public Long getIdAplicacionCliente() {
		return idAplicacionCliente;
	}

	public void setIdAplicacionCliente(Long idAplicacionCliente) {
		this.idAplicacionCliente = idAplicacionCliente;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AplicacionClienteId)) {
			return false;
		}
		AplicacionClienteId that = (AplicacionClienteId) o;
		return Objects.equals(getIdAplicacionCliente(), that.getIdAplicacionCliente());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdAplicacionCliente());
	}

}
