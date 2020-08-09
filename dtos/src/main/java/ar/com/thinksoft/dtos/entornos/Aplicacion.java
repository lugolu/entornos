package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(schema = "entornos", name = "APLICACION")
public class Aplicacion implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751282100001L;

	@Id
	@Column(name="APLICACION")
	private String aplicacionAux;
	@Column(name="APLICACION", insertable=false, updatable=false)
	private String aplicacion;

	public Aplicacion() {
	}

	public Aplicacion(String aplicacion) {
		aplicacionAux = aplicacion;
		this.aplicacion = aplicacion;
	}

	public Aplicacion clon() throws CloneNotSupportedException {
		return (Aplicacion) super.clone();
	}

	public String getAplicacion() {
		return aplicacion != null ? aplicacion : aplicacionAux;
	}

	public void setAplicacion(String aplicacion) {
		aplicacionAux = aplicacion;
		this.aplicacion = aplicacion;
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
		return Objects.equals(getAplicacion(), that.getAplicacion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getAplicacion());
	}

	public static Aplicacion defaultBuilder() {
		Aplicacion ret = new Aplicacion();


		return ret;
	}

}
