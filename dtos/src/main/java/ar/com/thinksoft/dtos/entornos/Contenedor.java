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


@Entity
@Table(schema = "entornos", name = "CONTENEDOR")
@IdClass(ContenedorId.class)
public class Contenedor implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751377700007L;

	@Id
	@AttributeOverrides(
			{
				@AttributeOverride(name = "idCliente", column = @Column(name = "ID_CLIENTE")),
				@AttributeOverride(name = "idServidor", column = @Column(name = "ID_SERVIDOR")),
				@AttributeOverride(name = "contenedor", column = @Column(name = "CONTENEDOR")),
			}
			)

	//FK
	private Long idCliente;
	//FK
	private Long idServidor;
	private String contenedor;
	@Column(name="PUERTO")
	private Integer puerto;

	public Contenedor() {
	}

	public Contenedor(Long idCliente, Long idServidor, String contenedor) {
		this.idCliente = idCliente;
		this.idServidor = idServidor;
		this.contenedor = contenedor;
	}

	public Contenedor(Long idCliente, Long idServidor, String contenedor, Integer puerto) {
		this.idCliente = idCliente;
		this.idServidor = idServidor;
		this.contenedor = contenedor;
		this.puerto = puerto;
	}

	public Contenedor clon() throws CloneNotSupportedException {
		return (Contenedor) super.clone();
	}

	public ContenedorId getId() {
		ContenedorId pk = new ContenedorId();

		pk.setIdCliente(idCliente);
		pk.setIdServidor(idServidor);
		pk.setContenedor(contenedor);

		return pk;
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

	public Integer getPuerto() {
		return puerto;
	}

	public void setPuerto(Integer puerto) {
		this.puerto = puerto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Contenedor)) {
			return false;
		}
		Contenedor that = (Contenedor) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	public static Contenedor defaultBuilder() {
		Contenedor ret = new Contenedor();


		return ret;
	}

}
