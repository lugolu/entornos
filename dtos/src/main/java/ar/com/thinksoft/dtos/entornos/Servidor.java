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
@Table(schema = "entornos", name = "SERVIDOR")
@IdClass(ServidorId.class)
public class Servidor implements Serializable, Cloneable {

	private static final long serialVersionUID = -159698751479900013L;

	@Id
	@AttributeOverrides(
			{
				@AttributeOverride(name = "idCliente", column = @Column(name = "ID_CLIENTE")),
				@AttributeOverride(name = "idServidor", column = @Column(name = "ID_SERVIDOR")),
			}
			)

	//FK
	private Long idCliente;
	private Long idServidor;
	@Column(name="SERVIDOR", nullable=false)
	private String servidor;
	@Column(name="IP", nullable=false)
	private String ip;

	public Servidor() {
	}

	public Servidor(Long idCliente, Long idServidor) {
		this.idCliente = idCliente;
		this.idServidor = idServidor;
	}

	public Servidor(Long idCliente, Long idServidor, String servidor, String ip) {
		this.idCliente = idCliente;
		this.idServidor = idServidor;
		this.servidor = servidor;
		this.ip = ip;
	}

	public Servidor clon() throws CloneNotSupportedException {
		return (Servidor) super.clone();
	}

	public ServidorId getId() {
		ServidorId pk = new ServidorId();

		pk.setIdCliente(idCliente);
		pk.setIdServidor(idServidor);

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

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = CommonFunctions.replaceAccentedChars(servidor);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = CommonFunctions.replaceAccentedChars(ip);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Servidor)) {
			return false;
		}
		Servidor that = (Servidor) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	public static Servidor defaultBuilder() {
		Servidor ret = new Servidor();


		return ret;
	}

}
