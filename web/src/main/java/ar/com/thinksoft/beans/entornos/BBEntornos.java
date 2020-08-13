package ar.com.thinksoft.beans.entornos;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ar.com.thinksoft.utils.FacesUtils;

@ManagedBean(name = "bbEntornos")
@ViewScoped
public class BBEntornos implements Serializable {

	private static final long serialVersionUID = -1153324977669803294L;

	private BBEntornosDiagram bbEntornosDiagram;
	private BBEntornosTopbar bbEntornosTopbar;

	public BBEntornos() {
		bbEntornosDiagram = (BBEntornosDiagram) FacesUtils.getBBCurrentInstance("bbEntornosDiagram");
		bbEntornosTopbar = (BBEntornosTopbar) FacesUtils.getBBCurrentInstance("bbEntornosTopbar");
	}

	public BBEntornosDiagram getBbEntornosDiagram() {
		return bbEntornosDiagram;
	}

	public BBEntornosTopbar getBbEntornosTopbar() {
		return bbEntornosTopbar;
	}

	public String getDummy() {
		return "";
	}

}
