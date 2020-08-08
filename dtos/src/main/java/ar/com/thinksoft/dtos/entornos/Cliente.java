package ar.com.thinksoft.dtos.entornos;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.thinksoft.utils.CommonFunctions;


@Entity
@Table(schema = "entornos", name = "CLIENTE")
public class Cliente implements Serializable, Cloneable {

	private static final long serialVersionUID = -159686875564300001L;

	@Id
	@Column(name="id_cliente")
	private Long idClienteAux;
	@Column(name="id_cliente", insertable=false, updatable=false)
	private Long idCliente;
	@Column(name="cliente", nullable=false)
	private String cliente;

	public Cliente() {
	}

	public Cliente(Long idCliente) {
		idClienteAux = idCliente;
		this.idCliente = idCliente;
	}

	public Cliente(Long idCliente, String cliente) {
		idClienteAux = idCliente;
		this.idCliente = idCliente;
		this.cliente = cliente;
	}

	public Cliente clon() throws CloneNotSupportedException {
		return (Cliente) super.clone();
	}

	public Long getIdCliente() {
		return idCliente != null ? idCliente : idClienteAux;
	}

	public void setIdCliente(Long idCliente) {
		idClienteAux = idCliente;
		this.idCliente = idCliente;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = CommonFunctions.replaceAccentedChars(cliente);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Cliente)) {
			return false;
		}
		Cliente that = (Cliente) o;
		return Objects.equals(getIdCliente(), that.getIdCliente());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIdCliente());
	}

	public static Cliente defaultBuilder() {
		Cliente ret = new Cliente();


		return ret;
	}

}
