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
@Table(schema = "entornos", name = "APLICACION_CLIENTE")
@IdClass(AplicacionClienteId.class)
public class AplicacionCliente implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751313100003L;

	@Id
	@AttributeOverrides(
			{
				@AttributeOverride(name = "idAplicacionCliente", column = @Column(name = "ID_APLICACION_CLIENTE")),
			}
			)

	private Long idAplicacionCliente;
	//FK
	@Column(name="ID_CLIENTE", nullable=false)
	private Long idCliente;
	//FK
	@Column(name="ID_SERVIDOR", nullable=false)
	private Long idServidor;
	//FK
	@Column(name="CONTENEDOR", nullable=false)
	private String contenedor;
	//FK
	@Column(name="ID_ENTORNO", nullable=false)
	private Long idEntorno;
	//FK
	@Column(name="ID_APLICACION", nullable=false)
	private Long idAplicacion;
	@Column(name="PUERTO")
	private Integer puerto;

	public AplicacionCliente() {
	}

	public AplicacionCliente(Long idAplicacionCliente) {
		this.idAplicacionCliente = idAplicacionCliente;
	}

	public AplicacionCliente(Long idAplicacionCliente, Long idCliente, Long idServidor, String contenedor, 
			Long idEntorno, Long idAplicacion, Integer puerto) {
		this.idAplicacionCliente = idAplicacionCliente;
		this.idCliente = idCliente;
		this.idServidor = idServidor;
		this.contenedor = contenedor;
		this.idEntorno = idEntorno;
		this.idAplicacion = idAplicacion;
		this.puerto = puerto;
	}

	public AplicacionCliente clon() throws CloneNotSupportedException {
		return (AplicacionCliente) super.clone();
	}

	public AplicacionClienteId getId() {
		AplicacionClienteId pk = new AplicacionClienteId();

		pk.setIdAplicacionCliente(idAplicacionCliente);

		return pk;
	}

	public Long getIdAplicacionCliente() {
		return idAplicacionCliente;
	}

	public void setIdAplicacionCliente(Long idAplicacionCliente) {
		this.idAplicacionCliente = idAplicacionCliente;
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

	public Long getIdEntorno() {
		return idEntorno;
	}

	public void setIdEntorno(Long idEntorno) {
		this.idEntorno = idEntorno;
	}

	public Long getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(Long idAplicacion) {
		this.idAplicacion = idAplicacion;
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
		if (!(o instanceof AplicacionCliente)) {
			return false;
		}
		AplicacionCliente that = (AplicacionCliente) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	public static AplicacionCliente defaultBuilder() {
		AplicacionCliente ret = new AplicacionCliente();


		return ret;
	}

}
