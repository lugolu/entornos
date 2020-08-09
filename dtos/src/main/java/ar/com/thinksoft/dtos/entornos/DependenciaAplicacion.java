package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import ar.com.thinksoft.utils.CommonFunctions;


@Entity
@Table(schema = "entornos", name = "DEPENDENCIA_APLICACION")
@IdClass(DependenciaAplicacionId.class)
public class DependenciaAplicacion implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751411700009L;

	@Id
	@AttributeOverrides(
			{
				@AttributeOverride(name = "idAplicacionCliente", column = @Column(name = "ID_APLICACION_CLIENTE")),
				@AttributeOverride(name = "idAplicacionDepende", column = @Column(name = "ID_APLICACION_DEPENDE")),
			}
			)

	//FK
	private Long idAplicacionCliente;
	//FK
	private Long idAplicacionDepende;
	@Column(name="NOMBRE", nullable=false)
	private String nombre;

	public DependenciaAplicacion() {
	}

	public DependenciaAplicacion(Long idAplicacionCliente, Long idAplicacionDepende) {
		this.idAplicacionCliente = idAplicacionCliente;
		this.idAplicacionDepende = idAplicacionDepende;
	}

	public DependenciaAplicacion(Long idAplicacionCliente, Long idAplicacionDepende, String nombre) {
		this.idAplicacionCliente = idAplicacionCliente;
		this.idAplicacionDepende = idAplicacionDepende;
		this.nombre = nombre;
	}

	public DependenciaAplicacion clon() throws CloneNotSupportedException {
		return (DependenciaAplicacion) super.clone();
	}

	public DependenciaAplicacionId getId() {
		DependenciaAplicacionId pk = new DependenciaAplicacionId();

		pk.setIdAplicacionCliente(idAplicacionCliente);
		pk.setIdAplicacionDepende(idAplicacionDepende);

		return pk;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = CommonFunctions.replaceAccentedChars(nombre);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DependenciaAplicacion)) {
			return false;
		}
		DependenciaAplicacion that = (DependenciaAplicacion) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	public static DependenciaAplicacion defaultBuilder() {
		DependenciaAplicacion ret = new DependenciaAplicacion();


		return ret;
	}

}
