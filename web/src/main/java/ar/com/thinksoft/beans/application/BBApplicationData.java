package ar.com.thinksoft.beans.application;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ar.com.thinksoft.utils.FacesUtils;
import ar.com.thinksoft.utils.XMLVersionParser;

@SuppressWarnings("serial")
@ManagedBean(name = "bbApplicationData")
@ApplicationScoped
public class BBApplicationData implements Serializable {


	public BBApplicationData() {
	}

	public String getAcercaDeClientNameFullTrim() {
		return XMLVersionParser.getClientNameFullTrim();
	}

	public String getAcercaDeClientName() {
		return XMLVersionParser.getClientName();
	}

	public String getAcercaDeProjectComments() {
		return XMLVersionParser.getProjectComments();
	}

	public String getAcercaDeProjectName() {
		return XMLVersionParser.getProjectName();
	}

	public String getAcercaDeVersionDate() {
		return XMLVersionParser.getVersionDate();
	}

	public String getAcercaDeVersionName() {
		return XMLVersionParser.getVersionName();
	}

	public String getApplicationName() {
		return FacesUtils.getContextPath();
	}

}
