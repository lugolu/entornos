package ar.com.thinksoft.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.cfg.AvailableSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoadConfig {

	private static LoadConfig instance;

	private Map<String, String> settings;

	private String CONNECTION_URL;
	private String SECURITY_CONNECTION_URL;
	private String SHOW_SQL;
	//
	private String PROJECT_NAME;
	private String CLIENT;
	private String AGE_URL;
	private String AGI_URL;
	private String AGP_URL;
	private String HOS_APP;
	private String HOS_APP_URL;
	private String ANMAT_URL;
	private String EQUIPO_TAREAS_PROGRAMADAS;
	private String SEGURIDAD_URL;
	private String VISUALIZADOR;
	private String HISTORIA_CLINICA;
	private String HISTORIA_CLINICA_URL;
	private String API_URL;
	private String JITSI_SERVER;

	private String ANUNCIADOR_PACIENTE_URL;

	private LoadConfig () {
	}

	public static LoadConfig getInstance() {
		if (instance == null) {
			instance = new LoadConfig();
			instance.loadFile();
		}
		return instance;
	}

	private void loadFile() {
		settings = new HashMap<String, String>();
		try {
			File config = new File(LoadConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			config = config.getParentFile(); //lib
			config = config.getParentFile(); //WEB-INF
			config = config.getParentFile(); //[NOMBRE DEL PROYECTO]
			PROJECT_NAME = config.getPath().substring(config.getPath().lastIndexOf(System.getProperty("file.separator")) + 1);
			config = config.getParentFile(); //[DIRECTORIO DESPLIEGUE]
			config = new File(config.getPath() + System.getProperty("file.separator") + "conexiones.xml");
			//
			if(config != null && config.exists()) {
				Document doc =  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(config);
				NodeList listProject = doc.getElementsByTagName("proyecto");
				for (int i=0; i<listProject.getLength(); i++) {
					Element project = (Element) listProject.item(i);
					if(PROJECT_NAME.equals(project.getAttributes().getNamedItem("name").getNodeValue())) {
						Node aux = null;
						// DB CONNECTION
						try { CONNECTION_URL =  project.getElementsByTagName("url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// DB SECURITY CONNECTION
						try { SECURITY_CONNECTION_URL =  project.getElementsByTagName("security_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// SHOW SQL
						try { SHOW_SQL =  project.getElementsByTagName("show_sql").item(0).getTextContent(); }
						catch(NullPointerException ex) { SHOW_SQL = "false"; }
						// CLIENTE
						try { CLIENT =  project.getElementsByTagName("client").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// AGE_URL
						try { AGE_URL =  project.getElementsByTagName("age_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// AGI_URL
						try { AGI_URL =  project.getElementsByTagName("agi_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// AGP_URL
						try { AGP_URL =  project.getElementsByTagName("agp_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// ANMAT_URL
						try { ANMAT_URL =  project.getElementsByTagName("anmat_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// HOS_APP
						try { HOS_APP =  project.getElementsByTagName("hos_app").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// HOS_APP_URL
						try { HOS_APP_URL =  project.getElementsByTagName("hos_app_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// EQUIPO_TAREAS_PROGRAMADAS
						try { EQUIPO_TAREAS_PROGRAMADAS =  project.getElementsByTagName("equipo_tareas_programadas").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// SEGURIDAD_URL
						try { SEGURIDAD_URL =  project.getElementsByTagName("seguridad_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// VISUALIZADOR
						try { VISUALIZADOR =  project.getElementsByTagName("visualizador").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// HISTORIA_CLINICA
						try { HISTORIA_CLINICA =  project.getElementsByTagName("historia_clinica").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// HISTORIA_CLINICA_URL
						try { HISTORIA_CLINICA_URL =  project.getElementsByTagName("historia_clinica_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// API URL
						try { API_URL =  project.getElementsByTagName("api_url").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						// API ANUNCIADOR PACIENTES
						try { ANUNCIADOR_PACIENTE_URL = project.getElementsByTagName("anunciador_paciente_url").item(0).getTextContent();}
						catch(NullPointerException ex) {}
						// JITSI SERVER
						try { JITSI_SERVER =  project.getElementsByTagName("jitsi_server").item(0).getTextContent(); }
						catch(NullPointerException ex) { }
						break;
					}
				}
				boolean error = false;
				if(CLIENT == null) {
					System.err.println("Error: cliente is NULL");
					error = true;
				}
				if(CONNECTION_URL == null) {
					System.err.println("Error: " + AvailableSettings.URL + " is NULL");
					error = true;
				}
				if(!error) {
					settings.put(AvailableSettings.URL, CONNECTION_URL);
					settings.put("hibernate.connection.security_url", SECURITY_CONNECTION_URL);
					settings.put(AvailableSettings.SHOW_SQL, SHOW_SQL);
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void genLogger () {
		System.out.println("LoadConfig.genLogger");
		System.setProperty("project_name", "_" + PROJECT_NAME);
		System.out.println("project_name: " + PROJECT_NAME);
		System.out.println("LoadConfig.genLogger end");
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public String getPROJECT_NAME() {
		return PROJECT_NAME;
	}

	public void setPROJECT_NAME(String pROJECT_NAME) {
		PROJECT_NAME = pROJECT_NAME;
	}

	public String getCLIENT() {
		return CLIENT;
	}

	public void setCLIENT(String cLIENT) {
		CLIENT = cLIENT;
	}

	public String getAGE_URL() {
		return AGE_URL;
	}

	public void setAGE_URL(String aGE_URL) {
		AGE_URL = aGE_URL;
	}

	public String getAGI_URL() {
		return AGI_URL;
	}

	public void setAGI_URL(String aGI_URL) {
		AGI_URL = aGI_URL;
	}

	public String getAGP_URL() {
		return AGP_URL;
	}

	public void setAGP_URL(String aGP_URL) {
		AGP_URL = aGP_URL;
	}

	public String getANMAT_URL() {
		return ANMAT_URL;
	}

	public void setANMAT_URL(String aNMAT_URL) {
		ANMAT_URL = aNMAT_URL;
	}

	public String getHOS_APP() {
		return HOS_APP;
	}

	public void setHOS_APP(String hOS_APP) {
		HOS_APP = hOS_APP;
	}

	public String getHOS_APP_URL() {
		return HOS_APP_URL;
	}

	public void setHOS_APP_URL(String hOS_APP_URL) {
		HOS_APP_URL = hOS_APP_URL;
	}

	public String getSEGURIDAD_URL() {
		return SEGURIDAD_URL;
	}

	public void setSEGURIDAD_URL(String sEGURIDAD_URL) {
		SEGURIDAD_URL = sEGURIDAD_URL;
	}

	public String getEQUIPO_TAREAS_PROGRAMADAS() {
		return EQUIPO_TAREAS_PROGRAMADAS;
	}

	public String getVISUALIZADOR() {
		return VISUALIZADOR;
	}

	/**
	 * @return the cONNECTION_URL
	 */
	public String getCONNECTION_URL() {
		return CONNECTION_URL;
	}

	/**
	 * @return the sECURITY_CONNECTION_URL
	 */
	public String getSECURITY_CONNECTION_URL() {
		return SECURITY_CONNECTION_URL;
	}

	/**
	 * @return the sHOW_SQL
	 */
	public String getSHOW_SQL() {
		return SHOW_SQL;
	}

	public String getHISTORIA_CLINICA() {
		return HISTORIA_CLINICA;
	}

	public String getHISTORIA_CLINICA_URL() {
		return HISTORIA_CLINICA_URL;
	}

	public String getAPI_URL() {
		return API_URL;
	}

	public String getJITSI_SERVER() {
		return JITSI_SERVER;
	}

	public String getANUNCIADOR_PACIENTE_URL() {
		return ANUNCIADOR_PACIENTE_URL;
	}


	public void setJITSI_SERVER(String jITSI_SERVER) {
		JITSI_SERVER = jITSI_SERVER;
	}


}
