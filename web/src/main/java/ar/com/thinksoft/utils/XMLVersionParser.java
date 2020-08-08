package ar.com.thinksoft.utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.jdbc.HibernateSessionFactory;

public class XMLVersionParser {

	private static String PROYECT_NAME;
	private static String VERSION_NAME;
	private static String VERSION_DATE;
	private static String PROYECT_COMMENTS;
	private static String CLIENT_NAME;

	private static String HISTORIA_CLINICA;
	private static String HISTORIA_CLINICA_URL;

	private static String AGP;
	private static String URL_AGP;

	private static String VISUALIZADOR;

	private static String PATH_TO_ROOT;
	private static String PATH_TO_TMP_FILE;

	static {
		String pathToXMLFile = null;
		String pathToLog4jXMLFileBase = null;
		String pathToLog4jXMLFile = null;
		Logger logger = Logger.getLogger(XMLVersionParser.class);
		try {
			String FILE_SEPARATOR = new String("file.separator");
			String SLASHES = new String("\\");
			pathToXMLFile = XMLVersionParser.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", Constants.STRING_WHITESPACE);

			if (System.getProperty(FILE_SEPARATOR).equals(Constants.STRING_BACKSLASH)) {
				PATH_TO_ROOT = Constants.STRING_BACKSLASH + pathToXMLFile.split("WEB-INF")[0];
				PATH_TO_TMP_FILE = PATH_TO_ROOT + "tmp";
				pathToLog4jXMLFileBase = pathToXMLFile.substring(0,pathToXMLFile.indexOf("classes") + 7)  + "/log4j-BASE.xml";
				pathToLog4jXMLFile = PATH_TO_TMP_FILE  + "/log4j.xml";
				pathToXMLFile = pathToXMLFile.substring(0,pathToXMLFile.indexOf("classes") + 7)  + "/acercade.xml";
			} else {
				PATH_TO_ROOT = SLASHES + pathToXMLFile.split("WEB-INF")[0].replace(Constants.STRING_BACKSLASH, SLASHES);
				PATH_TO_TMP_FILE = PATH_TO_ROOT + "tmp";
				pathToLog4jXMLFileBase = SLASHES  + pathToXMLFile.substring(1,pathToXMLFile.indexOf("classes") + 7).replace(Constants.STRING_BACKSLASH, SLASHES) + "\\log4j-BASE.xml";
				pathToLog4jXMLFile = PATH_TO_TMP_FILE  + "\\log4j.xml";
				pathToXMLFile = SLASHES  + pathToXMLFile.substring(1,pathToXMLFile.indexOf("classes") + 7).replace(Constants.STRING_BACKSLASH, SLASHES) + "\\acercade.xml";
			}
			try {
				String content = FileUtils.readFileToString(new File(pathToLog4jXMLFileBase), "UTF-8");
				File tempFile = new File(pathToLog4jXMLFile);
				content = content.replaceAll("RECETAS", LoadConfig.getInstance().getPROJECT_NAME());
				FileUtils.writeStringToFile(tempFile, content, "UTF-8");
				DOMConfigurator.configure(pathToLog4jXMLFile);
				PropertyConfigurator.configure(pathToLog4jXMLFile);
			} catch (Exception e) {
				logger.error(e.getMessage() + " - Error al reemplazar log4j");
			}
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(pathToXMLFile));
			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList listOfProyectos = doc.getElementsByTagName("proyecto");
			if (listOfProyectos.getLength() == 0) {
				throw new BusinessException("Error al leer XML de version. No se encontraron datos en el archivo.",SeverityBundle.ERROR);
			}
			int s = 0;
			Node firstProjectNode = listOfProyectos.item(s);
			if (firstProjectNode.getNodeType() == Node.ELEMENT_NODE) {
				Element firstProjectElement = (Element) firstProjectNode;
				PROYECT_NAME = firstProjectElement.getAttribute("nombre");
				NodeList firstVersionList = firstProjectElement.getElementsByTagName("version");
				Element firstVersionElement = (Element) firstVersionList.item(0);
				VERSION_NAME = firstVersionElement.getAttribute("nombre");
				VERSION_DATE = firstVersionElement.getAttribute("fecha");
				try {
					CLIENT_NAME = firstVersionElement.getAttribute("cliente");
				} catch (Exception e) {
					CLIENT_NAME = "";
				}
				String clientAux = HibernateSessionFactory.getInstance().getCLIENT();
				if(clientAux != null) {
					CLIENT_NAME = clientAux;
				}
				NodeList firstCommentsList = firstProjectElement.getElementsByTagName("comentarios");
				Element firstCommentElement = (Element) firstCommentsList.item(0);
				NodeList textCommentList = firstCommentElement.getChildNodes();
				PROYECT_COMMENTS = (((textCommentList.item(0)) != null) ? (textCommentList.item(0)).getNodeValue() : "").trim();

				NodeList firstSecuenciaList = firstProjectElement.getElementsByTagName("visualizador");
				Element firstSecuenciaElement = (Element) firstSecuenciaList.item(0);
				VISUALIZADOR = firstSecuenciaElement.getAttribute("nombre");

				String auxVISUALIZADOR = HibernateSessionFactory.getInstance().getVISUALIZADOR();
				if(auxVISUALIZADOR != null) {
					VISUALIZADOR = auxVISUALIZADOR;
				}
				//
				NodeList firstAgpList = firstProjectElement.getElementsByTagName("agp");
				Element firstAgpElement = (Element) firstAgpList.item(0);
				AGP = firstAgpElement.getAttribute("nombre");
				URL_AGP = firstAgpElement.getAttribute("url");
				String auxUrlAGP = HibernateSessionFactory.getInstance().getAGP_URL();
				if(auxUrlAGP != null) {
					URL_AGP = auxUrlAGP;
				}

				NodeList firstComprasList = firstProjectElement.getElementsByTagName("historiaClinica");
				Element firstComprasElement = (Element) firstComprasList.item(0);
				HISTORIA_CLINICA = firstComprasElement.getAttribute("nombre");
				HISTORIA_CLINICA_URL = firstComprasElement.getAttribute("url");

				String auxHISTORIA_CLINICA = HibernateSessionFactory.getInstance().getHISTORIA_CLINICA();
				if(auxHISTORIA_CLINICA != null) {
					HISTORIA_CLINICA = auxHISTORIA_CLINICA;
				}
				String auxURL_HISTORIA_CLINICA = HibernateSessionFactory.getInstance().getHISTORIA_CLINICA_URL();
				if(auxURL_HISTORIA_CLINICA != null) {
					HISTORIA_CLINICA_URL = auxURL_HISTORIA_CLINICA;
				}
			}
			if (CLIENT_NAME != null && !"".equals(CLIENT_NAME)) {
				Resources.setRedefinedResources(getClientNameFullTrim());
			}
		} catch (Exception e) {
			logger.error(e.getMessage() + "user dir :" + System.getProperty("user.dir") + " Path " + pathToXMLFile);
		}
	}

	public static String getProjectName() {
		return PROYECT_NAME;
	}

	public static String getVersionName() {
		return VERSION_NAME;
	}

	public static String getVersionDate() {
		return VERSION_DATE;
	}

	public static String getProjectComments() {
		return PROYECT_COMMENTS;
	}

	public static String getClientName() {
		return CLIENT_NAME;
	}

	public static String getClientNameFullTrim() {
		return CLIENT_NAME.replaceAll("\\p{Space}+", "");
	}

	public static boolean esClienteCEMIC () {
		return "CEMIC".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteCPI () {
		return "CPI".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteEMME () {
		return "EMME".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteGEA () {
		return "GEA".equals(XMLVersionParser.getClientNameFullTrim())
				|| "NEUROS".equals(XMLVersionParser.getClientNameFullTrim())
				|| "OFTAL".equals(XMLVersionParser.getClientNameFullTrim())
				|| "SANATORIOCANADA".equals(XMLVersionParser.getClientNameFullTrim())
				|| "SEMEGER".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteMATERDEI () {
		return "MATERDEI".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteSJDD () {
		return "SANJUANDEDIOS".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteUNC () {
		return "UNC".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteUP () {
		return "UNIONPERSONAL".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static boolean esClienteTS () {
		return "TS".equals(XMLVersionParser.getClientNameFullTrim());
	}

	public static String getVISUALIZADOR() {
		return VISUALIZADOR;
	}

	public static String getAGP() {
		return AGP;
	}

	public static String getUrlAGP() {
		return URL_AGP;
	}

	public static String getHistoriaClinica() {
		return HISTORIA_CLINICA;
	}

	public static String getUrlHistoriaClinica() {
		return HISTORIA_CLINICA_URL;
	}

}
