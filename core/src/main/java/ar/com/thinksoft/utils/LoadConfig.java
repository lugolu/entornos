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
						break;
					}
				}
				boolean error = false;
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

}
