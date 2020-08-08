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
@Table(schema = "entornos", name = "ENTORNO_CLIENTE")
@IdClass(EntornoClienteId.class)
public class EntornoCliente implements Serializable, Cloneable {

	private static final long serialVersionUID = -159686978246400003L;

	@Id
	@AttributeOverrides(
			{
				@AttributeOverride(name = "idEntorno", column = @Column(name = "ID_ENTORNO")),
				@AttributeOverride(name = "idCliente", column = @Column(name = "ID_CLIENTE")),
			}
			)

	private Long idEntorno;
	//FK
	private Long idCliente;
	@Column(name="entorno", nullable=false)
	private String entorno;

	public EntornoCliente() {
	}

	public EntornoCliente(Long idEntorno, Long idCliente) {
		this.idEntorno = idEntorno;
		this.idCliente = idCliente;
	}

	public EntornoCliente(Long idEntorno, Long idCliente, String entorno) {
		this.idEntorno = idEntorno;
		this.idCliente = idCliente;
		this.entorno = entorno;
	}

	public EntornoCliente clon() throws CloneNotSupportedException {
		return (EntornoCliente) super.clone();
	}

	public EntornoClienteId getId() {
		EntornoClienteId pk = new EntornoClienteId();

		pk.setIdEntorno(idEntorno);
		pk.setIdCliente(idCliente);

		return pk;
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

	public String getEntorno() {
		return entorno;
	}

	public void setEntorno(String entorno) {
		this.entorno = CommonFunctions.replaceAccentedChars(entorno);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof EntornoCliente)) {
			return false;
		}
		EntornoCliente that = (EntornoCliente) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	public static EntornoCliente defaultBuilder() {
		EntornoCliente ret = new EntornoCliente();


		return ret;
	}

}
