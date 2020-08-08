package ar.com.thinksoft.utils;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SwappedColumn implements Serializable {
	private Long idMonitoreoPac;
	private Long idDetMonitoreoPac;
	//
	private Long idScorePac;
	//
	private String valor;
	private Date fecha;
	private boolean tieneRegistro;
	//
	private Long idPersonalCarga;

	public SwappedColumn() {
	}

	public Long getIdMonitoreoPac() {
		return idMonitoreoPac;
	}

	public void setIdMonitoreoPac(Long idMonitoreoPac) {
		this.idMonitoreoPac = idMonitoreoPac;
	}

	public Long getIdDetMonitoreoPac() {
		return idDetMonitoreoPac;
	}

	public void setIdDetMonitoreoPac(Long idDetMonitoreoPac) {
		this.idDetMonitoreoPac = idDetMonitoreoPac;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isTieneRegistro() {
		return tieneRegistro;
	}

	public void setTieneRegistro(boolean tieneRegistro) {
		this.tieneRegistro = tieneRegistro;
	}

	public Long getIdScorePac() {
		return idScorePac;
	}

	public void setIdScorePac(Long idScorePac) {
		this.idScorePac = idScorePac;
	}

	public Long getIdPersonalCarga() {
		return idPersonalCarga;
	}

	public void setIdPersonalCarga(Long idPersonalCarga) {
		this.idPersonalCarga = idPersonalCarga;
	}

}