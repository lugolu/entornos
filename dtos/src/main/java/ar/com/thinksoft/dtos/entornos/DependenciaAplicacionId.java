package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class DependenciaAplicacionId implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751415300010L;

	@Column(name="ID_APLICACION_CLIENTE")
	private Long idAplicacionCliente;
	@Column(name="ID_APLICACION_DEPENDE")
	private Long idAplicacionDepende;

	public DependenciaAplicacionId() {
	}

	public DependenciaAplicacionId(Long idAplicacionCliente, Long idAplicacionDepende) {
		this.idAplicacionCliente = idAplicacionCliente;
		this.idAplicacionDepende = idAplicacionDepende;
	}

	public DependenciaAplicacionId clon() throws CloneNotSupportedException {
		return (DependenciaAplicacionId) super.clone();
	}

	public Long getIdAplicacionCliente() {
		return idAplicacionCliente;
	}

	public void setIdAplicacionCliente(Long idAplicacionCliente) {
		this.idAplicacionCliente = idAplicacionCliente;
	}

	public Long getIdAplicacionDepende() {
		return idAplicacionDepende;
	}

	public void setIdAplicacionDepende(Long idAplicacionDepende) {
		this.idAplicacionDepende = idAplicacionDepende;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DependenciaAplicacionId)) {
			return false;
		}
		DependenciaAplicacionId that = (DependenciaAplicacionId) o;
		return Objects.equals(getIdAplicacionCliente(), that.getIdAplicacionCliente()) &&
				Objects.equals(getIdAplicacionDepende(), that.getIdAplicacionDepende());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdAplicacionCliente(), getIdAplicacionDepende());
	}

}
