package ar.com.thinksoft.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.utils.SeverityBundle;

public class Commons {

	private static Logger logger = Logger.getLogger(Commons.class);

	private static SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat sdfFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private static String pathOutput;

	public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static SimpleDateFormat sdfDia = new SimpleDateFormat("d");

	private static Map<String, String> failed = null;
	private static boolean ignoreFailed = false;

	private static Map<String, String> succcess = null;
	private static boolean ignoreSucccess = false;

	private static boolean ignoreMediosMagneticos = true;

	private static boolean ignoreInsert = true;
	private static boolean ignoreUpdate = false;

	private static boolean ignoreValidador = true;

	private static boolean testFailed;
	private static boolean testSuccess;
	private static String testPath;

	public static Long CEMIC_CUIT = 30546128403L;
	public static String CEMIC_ITC = "4daa8762-6b4b-11e8-9a9c-000c29a675b5";
	public static String CEMIC_TRADITUM_USER = "TRIA00000036";
	public static String CEMIC_TRADITUM_PASS = "IA000036";

	public static Long CPI_CUIT = 30546109360L;

	public static Long SJDD_CUIT = 30694044502L;
	public static String SJDD_CONEXIA_USER = "CXWS0023";
	public static String SJDD_ITC = "176fdc28-1c61-11e6-9031-000c29a675b5";
	public static String SJDD_OSMECON_USER = "5007";
	public static String SJDD_OSMECON_PASS = "443179";
	public static String SJDD_TRADITUM_USER = "TRIA00004138";
	public static String SJDD_TRADITUM_PASS = "IA004138";

	public static Long SMD_CUIT = 30545848534L;
	public static String SMD_ITC = "9cfbcf3e-fe25-11e3-96ff-000c29a675b5";
	public static String SMD_TRADITUM_USER = "TRIA00000842";
	public static String SMD_TRADITUM_PASS = "IA000842";

	public static Long UP_CUIT = 30708049928L;
	public static String UP_TRADITUM_USER = "TRIA00006870";
	public static String UP_TRADITUM_PASS = "IA006870";

	static {
		failed = new HashMap<String, String>();
		succcess = new HashMap<String, String>();

		try {
			String path = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");
			if (path.contains("bin")) {
				pathOutput = path.split("/bin")[0];
			}
			else if (path.contains("eslac/hospital9")) {
				pathOutput = path.split("eslac/hospital9")[0] + "eslac/";
			}
			else if (path.contains("hospital9/hospital9")) {
				pathOutput = path.split("hospital9/hospital9")[0] + "hospital9/";
			}
			else {
				pathOutput = path.split("/bin")[0];
			}
			logInfo("pathOutput " + pathOutput);

			loadFailed();
			logInfo("Failed " + failed.size());

			loadFailedSuccess();
			logInfo("FailedSuccess " + failed.size());

			loadSucccess();
			logInfo("Succcess " + succcess.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean controlarRetorno () {
		return false;
		//		return true;
	}

	public static void logInfo (String s) {
		logger.info(s);
	}

	public static void logWarn (String s, Exception e) {
		logger.warn(s, e);
	}

	public static Date parseDate (String str) throws ParseException {
		if (str == null || str.length() != 10) {
			throw new ParseException("Fecha invalida", -1);
		}
		return sdfFecha.parse(str);
	}

	public static Date parseDateHour (String str) throws ParseException {
		if (str == null || str.length() != 19) {
			throw new ParseException("Fecha invalida", -1);
		}
		return sdfFechaHora.parse(str);
	}

	/**
	 *
	 * @param path
	 * @param test
	 */
	public static void logSkip (String path, String test) {
		if (path != null) {
			logSkip(path + "." + test);
		}
	}

	/**
	 *
	 * @param path
	 * @param test
	 */
	public static void logInfoSuccess (String path, String test) {
		if (path != null) {
			testPath = (path + "." + test);
			testSuccess = true;
		}
	}

	/**
	 *
	 * @param path
	 * @param test
	 * @param msg
	 */
	public static void logWarn (String path, String test, String msg) {
		if (path != null) {
			testPath = (path + "." + test);
			testFailed = true;
		}
		logger.warn(msg);
	}

	/**
	 *
	 * @param path
	 * @param test
	 * @param msg
	 */
	public static void logWarnNoFile (String path, String test, String msg) {
		logger.warn(msg);
	}

	/**
	 *
	 * @param path
	 * @param test
	 * @param msg
	 * @param e
	 */
	public static void logWarn (String path, String test, String msg, Throwable e) {
		if (path != null) {
			testPath = (path + "." + test);
			testFailed = true;
		}
		logger.warn(msg, e);
	}

	/**
	 *
	 * @param auxPath
	 */
	public static void logTestResult (String auxPath, String motivo) {
		if (testFailed && testSuccess) {
			logTestFailedSuccess(testPath);
		}
		else if (testFailed) {
			if (testPath != null) {
				logFailed(testPath);
				testResult(testPath, TestResult.FAILED);
			}
		}
		else if (testSuccess) {
			if (testPath != null) {
				logSuccess(testPath);
				testResult(testPath, TestResult.SUCCESS);
			}
		} else {
			if (auxPath != null
					&& !(!failed.isEmpty() && failed.containsKey(auxPath))
					&& !(!succcess.isEmpty() && succcess.containsKey(auxPath))) {
				logUnknown(auxPath, motivo);
			}
		}
		testPath = null;
		testFailed = false;
		testSuccess = false;
	}

	public static boolean random () {
		//		return Math.random() <= 0.1;
		return true;
	}

	/**
	 *
	 * @param s
	 */
	private static void logUnknown (String s, String archivo) {
		File outFile = null;
		BufferedWriter writer = null;
		if (archivo == null || "".equals(archivo)) {
			archivo = "unknown";
		}
		String fileName = pathOutput + "\\" + archivo + ".txt";
		try {
			outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile, true));
			logger.warn(archivo + ": " + s.split("\n")[0]);
			writer.write(sdf.format(new Date()) + " " + s.split("\n")[0]+"\n");
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				outFile=null;
				writer  = null;
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @param s
	 */
	private static void logFailed (String s) {
		File outFile = null;
		BufferedWriter writer = null;
		String fileName = pathOutput + "\\failed.txt";
		//		try {
		//			int i=5/0;
		//		} catch (Exception ex) {
		//			ex.printStackTrace();
		//		}
		try {
			outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile, true));
			writer.write(sdf.format(new Date()) + " " + s.split("\n")[0]+"\n");
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				outFile=null;
				writer  = null;
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @param s
	 */
	private static void logTestFailedSuccess (String s) {
		File outFile = null;
		BufferedWriter writer = null;
		String fileName = pathOutput + "\\failedSuccess.txt";
		//		try {
		//			int i=5/0;
		//		} catch (Exception ex) {
		//			ex.printStackTrace();
		//		}
		try {
			outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile, true));
			writer.write(sdf.format(new Date()) + " " + s.split("\n")[0]+"\n");
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				outFile=null;
				writer  = null;
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @param msg
	 */
	public static void logRetry (String msg) {
		File outFile = null;
		BufferedWriter writer = null;
		String fileName = pathOutput + "\\retry.txt";
		try {
			outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile, true));
			writer.write(sdf.format(new Date()) + " " + msg+"\n");
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				outFile=null;
				writer  = null;
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @param s
	 */
	private static void logSuccess (String s) {
		File outFile = null;
		BufferedWriter writer = null;
		String fileName = pathOutput + "\\success.txt";
		//		try {
		//			int i=5/0;
		//		} catch (Exception ex) {
		//			ex.printStackTrace();
		//		}
		try {
			outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile, true));
			writer.write(sdf.format(new Date()) + " " + s.split("\n")[0]+"\n");
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				outFile=null;
				writer  = null;
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @param s
	 */
	private static void logSkip (String s) {
		File outFile = null;
		BufferedWriter writer = null;
		String fileName = pathOutput + "\\skip.txt";
		try {
			outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile, true));
			writer.write(sdf.format(new Date()) + " " + s.split("\n")[0]+"\n");
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				outFile=null;
				writer  = null;
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @param path
	 * @param test
	 * @return
	 */
	public static boolean ignoreFailed (String path, String test) {
		if (!ignoreFailed) {
			return false;
		}

		if (!failed.isEmpty() && failed.containsKey(path+"."+test)) {
			testResult(path, TestResult.SKIP_FAILED);
			return true;
		}

		return false;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static boolean ignoreInsert (String path) {
		if (ignoreInsert) {
			testResult(path, TestResult.SKIP_INSERT);
			testSuccess = true;
		}

		return ignoreInsert;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static boolean ignoreUpdate (String path) {
		if (ignoreUpdate) {
			testResult(path, TestResult.SKIP_UPDATE);
			testSuccess = true;
		}

		return ignoreUpdate;
	}

	private static Map<String, TestResult> mapTestResult = new HashMap<String, TestResult>();
	private static TestResult testResult = new TestResult();

	/**
	 *
	 * @param path
	 * @param tipo
	 */
	public static void testResult (String path, String tipo) {
		int pos = path.lastIndexOf(".");
		if ("t".equals(path.substring(pos, pos + 1))) {
			path = path.substring(0, path.lastIndexOf("."));
		}
		if (!mapTestResult.containsKey(path)) {
			//			System.out.println("add testResult " + path);
			mapTestResult.put(path, new TestResult());
		}
		switch (tipo) {
		case TestResult.FAILED:
			mapTestResult.get(path).addFailed();
			testResult.addFailed();
			break;
		case TestResult.SUCCESS:
			mapTestResult.get(path).addSuccess();
			testResult.addSuccess();
			break;
		case TestResult.SKIP_FAILED:
			mapTestResult.get(path).addSkipFailed();
			testResult.addSkipFailed();
			break;
		case TestResult.SKIP_SUCCESS:
			mapTestResult.get(path).addSkipSuccess();
			testResult.addSkipSuccess();
			break;
		case TestResult.SKIP_AGREGAR:
			mapTestResult.get(path).addSkipAgregar();
			testResult.addSkipAgregar();
			break;
		case TestResult.SKIP_GENERAL:
			mapTestResult.get(path).addSkipGeneral();
			testResult.addSkipGeneral();
			break;
		case TestResult.SKIP_INSERT:
			mapTestResult.get(path).addSkipInsert();
			testResult.addSkipInsert();
			break;
		case TestResult.SKIP_PAGE:
			mapTestResult.get(path).addSkipPage();
			testResult.addSkipPage();
			break;
		case TestResult.SKIP_UPDATE:
			mapTestResult.get(path).addSkipUpdate();
			testResult.addSkipUpdate();
			break;
		case TestResult.LOGIN:
			testResult.addLogin();
			break;
		default:
			logWarn("testResult (" + path + ", " + tipo + ")", new Exception("tipo desconocido"));
		}
	}

	/**
	 *
	 * @param path
	 */
	public static void printTestResult (String path) {
		System.out.println(path + ": "+ mapTestResult.get(path));
		System.out.println(testResult);
	}

	/**
	 *
	 * @throws IOException
	 */
	private static void loadFailed () throws IOException {
		File file = new File(pathOutput + "\\failed.txt");
		if (!file.exists()) {
			file.createNewFile();
			return;
		}

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String linea = null;

			while ((linea = br.readLine()) != null) {
				linea = linea.replaceAll("\t", "").replaceAll("\\p{Space}+", " ").trim();
				if (linea.length() == 0
						|| linea.startsWith("//")) {
					continue;
				}

				if (linea.contains(" ")) {
					linea = linea.split(" ")[2];
				}
				if (!failed.containsKey(linea)) {
					failed.put(linea, linea);
				}
			}
		} catch (Exception e) {
			logWarn(null, null, "Error leyendo fails", e);
		} finally {
			try {
				if (br != null) {
					br.close();
					br = null;
				}
			} catch (IOException ex) {
			}
			try {
				if (fr != null) {
					fr.close();
					fr=null;
				}
			} catch (IOException ex) {
			}
		}
	}

	private static void loadFailedSuccess () throws IOException {
		File file = new File(pathOutput + "\\failedSuccess.txt");
		if (!file.exists()) {
			file.createNewFile();
			return;
		}

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String linea = null;

			while ((linea = br.readLine()) != null) {
				linea = linea.replaceAll("\t", "").replaceAll("\\p{Space}+", " ").trim();
				if (linea.length() == 0
						|| linea.startsWith("//")) {
					continue;
				}

				if (linea.contains(" ")) {
					linea = linea.split(" ")[2];
				}
				if (!failed.containsKey(linea)) {
					failed.put(linea, linea);
				}
			}
		} catch (Exception e) {
			logWarn(null, null, "Error leyendo fails", e);
		} finally {
			try {
				if (br != null) {
					br.close();
					br = null;
				}
			} catch (IOException ex) {
			}
			try {
				if (fr != null) {
					fr.close();
					fr=null;
				}
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public static boolean ignoreMediosMagneticos () {
		return ignoreMediosMagneticos;
	}

	/**
	 *
	 * @param path
	 * @param test
	 * @return
	 */
	public static boolean ignoreSuccess (String path, String test) {
		if (!ignoreSucccess) {
			return false;
		}

		if (!succcess.isEmpty() && succcess.containsKey(path+"."+test)) {
			testResult(path, TestResult.SKIP_SUCCESS);
			return true;
		}

		return false;
	}

	/**
	 *
	 * @return
	 */
	public static boolean ignoreValidador () {
		if (ignoreValidador) {
			testSuccess = true;
		}

		return ignoreValidador;
	}

	/**
	 *
	 * @return
	 */
	public static boolean ignoreValidadorConvenioExterno () {
		return true;
	}

	/**
	 *
	 * @throws IOException
	 */
	private static void loadSucccess () throws IOException {
		File file = new File(pathOutput + "\\success.txt");
		if (!file.exists()) {
			file.createNewFile();
			return;
		}

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String linea = null;

			while ((linea = br.readLine()) != null) {
				linea = linea.replaceAll("\t", "").replaceAll("\\p{Space}+", " ").trim();
				if (linea.length() == 0
						|| linea.startsWith("//")) {
					continue;
				}

				if (linea.contains(" ")) {
					linea = linea.split(" ")[2];
				}
				if (!succcess.containsKey(linea)) {
					succcess.put(linea, linea);
				}
			}
		} catch (Exception e) {
			logWarn(null, null, "Error leyendo fails", e);
		} finally {
			try {
				if (br != null) {
					br.close();
					br = null;
				}
			} catch (IOException ex) {
			}
			try {
				if (fr != null) {
					fr.close();
					fr=null;
				}
			} catch (IOException ex) {
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public static String getDiaActual() {
		return sdfDia.format(new Date());
	}

	public enum EXPECTED {FALSE, TRUE, LIST_EMPTY, LIST_NOT_EMPTY, NULL, NOT_NULL};

	@SuppressWarnings("unchecked")
	public static String methodTest (String claseTest, String testName, Class c, String metodo, Object objeto, Object parameter, EXPECTED expected) throws ClassNotFoundException {
		String error = null;

		if (Commons.ignoreFailed (claseTest, testName)) {
			return null;
		}

		if (Commons.ignoreSuccess (claseTest, testName)) {
			return null;
		}

		try {
			//			try {
			Method m = null;
			Object ret = null;
			if (metodo.startsWith("equals") || parameter != null) {
				if (metodo.startsWith("equals")) {
					m = c.getMethod(metodo, Object.class);
				}
				else {
					m = c.getMethod(metodo, parameter.getClass());
				}
				ret = m.invoke(objeto, parameter);
			}
			else {
				m = c.getMethod(metodo);
				ret = m.invoke(objeto);
			}
			switch (expected) {
			case FALSE:
				if (((Boolean) ret)) {
					throw new BusinessException("!FALSE", SeverityBundle.WARN);
				}
				break;
			case TRUE:
				if (!((Boolean) ret)) {
					throw new BusinessException("!TRUE", SeverityBundle.WARN);
				}
				break;
			case LIST_EMPTY:
				if (ret != null && ((List) ret).size() > 0) {
					throw new BusinessException("!LIST_EMPTY", SeverityBundle.WARN);
				}
				break;
			case LIST_NOT_EMPTY:
				if (ret == null || ((List) ret).size() == 0) {
					throw new BusinessException("!LIST_NOT_EMPTY", SeverityBundle.WARN);
				}
				break;
			case NOT_NULL:
				if (ret == null) {
					throw new BusinessException("!NOT_NULL", SeverityBundle.WARN);
				}
				break;
			case NULL:
				if (ret != null && !"null".equals(ret)) {
					throw new BusinessException("!NULL", SeverityBundle.WARN);
				}
				break;
			default:
				logWarn("methodTest (" + claseTest + "." + testName + ", " + expected + ")", new Exception("expected desconocido"));
			}
			Commons.logInfoSuccess(claseTest, testName);
			//			} catch (Exception e) {
			//				HandlerException.getInstancia().treateException(e, c.getClass());
			//			}
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				error = "InvocationTargetException";
			}
			else if (e instanceof NullPointerException) {
				error = "NullPointerException";
			}
			else {
				error = e.getMessage();
			}
			Commons.logWarn (claseTest, testName, error, e);
		}

		return error;
	}

	@SuppressWarnings("unchecked")
	public static String methodTest (String claseTest, String testName, Class c, String metodo, Object objeto, EXPECTED expected, Object... parameters) throws ClassNotFoundException {
		String error = null;

		if (Commons.ignoreFailed (claseTest, testName)) {
			return null;
		}

		if (Commons.ignoreSuccess (claseTest, testName)) {
			return null;
		}

		try {
			//			try {
			Method m = null;
			Object ret = null;
			if (parameters.length == 0) {
				m = c.getMethod(metodo);
				ret = m.invoke(objeto);
			}
			else if (parameters.length == 1) {
				m = c.getMethod(metodo, parameters[0].getClass());
				ret = m.invoke(objeto, parameters[0]);
			}
			else if (parameters.length == 2) {
				m = c.getMethod(metodo, parameters[0].getClass(), parameters[1].getClass());
				ret = m.invoke(objeto, parameters[0], parameters[1]);
			}
			else if (parameters.length == 3) {
				m = c.getMethod(metodo, parameters[0].getClass(), parameters[1].getClass(), parameters[2].getClass());
				ret = m.invoke(objeto, parameters[0], parameters[1], parameters[2]);
			}
			else if (parameters.length == 4) {
				m = c.getMethod(metodo, parameters[0].getClass(), parameters[1].getClass(), parameters[2].getClass(), parameters[3].getClass());
				ret = m.invoke(objeto, parameters[0], parameters[1], parameters[2], parameters[3]);
			}
			else {
				throw new BusinessException("Metodo no contemplado" + parameters.length, SeverityBundle.ERROR);
			}
			switch (expected) {
			case FALSE:
				if (((Boolean) ret)) {
					throw new BusinessException("!FALSE", SeverityBundle.WARN);
				}
				break;
			case TRUE:
				if (!((Boolean) ret)) {
					throw new BusinessException("!TRUE", SeverityBundle.WARN);
				}
				break;
			case LIST_EMPTY:
				if (ret != null && ((List) ret).size() > 0) {
					throw new BusinessException("!LIST_EMPTY", SeverityBundle.WARN);
				}
				break;
			case LIST_NOT_EMPTY:
				if (ret == null || ((List) ret).size() == 0) {
					throw new BusinessException("!LIST_NOT_EMPTY", SeverityBundle.WARN);
				}
				break;
			case NOT_NULL:
				if (ret == null) {
					throw new BusinessException("!NOT_NULL", SeverityBundle.WARN);
				}
				break;
			case NULL:
				if (ret != null && !"null".equals(ret)) {
					throw new BusinessException("!NULL", SeverityBundle.WARN);
				}
				break;
			default:
				logWarn("methodTest (" + claseTest + "." + testName + ", " + expected + ")", new Exception("expected desconocido"));
			}
			Commons.logInfoSuccess(claseTest, testName);
			//			} catch (Exception e) {
			//				HandlerException.getInstancia().treateException(e, c.getClass());
			//			}
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				error = "InvocationTargetException";
			}
			else if (e instanceof NullPointerException) {
				error = "NullPointerException";
			}
			else {
				error = e.getMessage();
			}
			Commons.logWarn (claseTest, testName, error, e);
		}

		return error;
	}

}