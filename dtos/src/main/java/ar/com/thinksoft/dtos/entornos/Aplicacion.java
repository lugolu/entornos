package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.thinksoft.utils.CommonFunctions;


@Entity
@Table(schema = "entornos", name = "APLICACION")
public class Aplicacion implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751282100001L;

	@Id
	@Column(name="ID_APLICACION")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idAplicacionAux;
	@Column(name="ID_APLICACION", insertable=false, updatable=false)
	private Long idAplicacion;
	@Column(name="APLICACION", nullable=false)
	private String aplicacion;

	public Aplicacion() {
	}

	public Aplicacion(Long idAplicacion) {
		idAplicacionAux = idAplicacion;
		this.idAplicacion = idAplicacion;
	}

	public Aplicacion(Long idAplicacion, String aplicacion) {
		idAplicacionAux = idAplicacion;
		this.idAplicacion = idAplicacion;
		this.aplicacion = aplicacion;
	}

	public Aplicacion clon() throws CloneNotSupportedException {
		return (Aplicacion) super.clone();
	}

	public Long getIdAplicacion() {
		return idAplicacion != null ? idAplicacion : idAplicacionAux;
	}

	public void setIdAplicacion(Long idAplicacion) {
		idAplicacionAux = idAplicacion;
		this.idAplicacion = idAplicacion;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = CommonFunctions.replaceAccentedChars(aplicacion);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Aplicacion)) {
			return false;
		}
		Aplicacion that = (Aplicacion) o;
		return Objects.equals(getIdAplicacion(), that.getIdAplicacion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdAplicacion());
	}

	public static Aplicacion defaultBuilder() {
		Aplicacion ret = new Aplicacion();


		return ret;
	}

}
