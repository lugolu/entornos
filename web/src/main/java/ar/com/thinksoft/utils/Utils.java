package ar.com.thinksoft.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.omnifaces.util.selectitems.SelectItemsBuilder;

import ar.com.thinksoft.exception.BusinessException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Utils {

	public static final String LOGO_APP_CENTRO_ATENCION = Constants.STRING_LOGO_APP + "_" + Constants.STRING_CENTRO_ATENCION + "_";
	public static final String LOGO_INICIO_APP_CENTRO_ATENCION =  Constants.STRING_LOGO_INICIO_APP + "_" + Constants.STRING_CENTRO_ATENCION + "_";
	public static final String LOGO_IMPRESION_CENTRO_ATENCION = Constants.STRING_LOGO_IMPRESION + "_" + Constants.STRING_CENTRO_ATENCION + "_";
	public static final String LOGO_IMPRESION_SMALL_CENTRO_ATENCION = Constants.STRING_LOGO_IMPRESION_SMALL + "_" + Constants.STRING_CENTRO_ATENCION + "_";
	public static final String LOGO_TERM_AUTO_RECEP_CENTRO_ATENCION = Constants.STRING_LOGO_TERM_AUTO_RECEP + "_" + Constants.STRING_CENTRO_ATENCION + "_";
	public static final String LOGO_COMP_INT_CENTRO_ATENCION = Constants.STRING_LOGO_COMP_INT + "_" + Constants.STRING_CENTRO_ATENCION + "_";

	public static final String LOGO_APP_EMPRESA = Constants.STRING_LOGO_APP + "_" + Constants.STRING_EMPRESA + "_";
	public static final String LOGO_INICIO_APP_EMPRESA =  Constants.STRING_LOGO_INICIO_APP + "_" + Constants.STRING_EMPRESA + "_";
	public static final String LOGO_IMPRESION_EMPRESA = Constants.STRING_LOGO_IMPRESION + "_" + Constants.STRING_EMPRESA + "_";
	public static final String LOGO_IMPRESION_SMALL_EMPRESA = Constants.STRING_LOGO_IMPRESION_SMALL + "_" + Constants.STRING_EMPRESA + "_";
	public static final String LOGO_TERM_AUTO_RECEP_EMPRESA = Constants.STRING_LOGO_TERM_AUTO_RECEP + "_" + Constants.STRING_EMPRESA + "_";
	public static final String LOGO_COMP_INT_EMPRESA = Constants.STRING_LOGO_COMP_INT + "_" + Constants.STRING_EMPRESA + "_";

	public static final String LOGO_APP_PTO_VTA_EMPRESA = Constants.STRING_LOGO_APP + "_" + Constants.STRING_PTO_VTA_EMPRESA + "_";
	public static final String LOGO_INICIO_APP_PTO_VTA_EMPRESA =  Constants.STRING_LOGO_INICIO_APP + "_" + Constants.STRING_PTO_VTA_EMPRESA + "_";
	public static final String LOGO_IMPRESION_PTO_VTA_EMPRESA = Constants.STRING_LOGO_IMPRESION + "_" + Constants.STRING_PTO_VTA_EMPRESA + "_";
	public static final String LOGO_TERM_AUTO_RECEP_PTO_VTA_EMPRESA = Constants.STRING_LOGO_TERM_AUTO_RECEP + "_" + Constants.STRING_PTO_VTA_EMPRESA + "_";
	public static final String LOGO_COMP_INT_PTO_VTA_EMPRESA = Constants.STRING_LOGO_COMP_INT + "_" + Constants.STRING_PTO_VTA_EMPRESA + "_";

	private Utils() {}

	// METODOS DE SESSION
	public static void prepareNewUserLogin() {
		FacesUtils.clearRequestSession();
		FacesUtils.invalidateSession();
	}

	public static void desconectarError() {
		FacesUtils.clearRequestSession();
		FacesUtils.invalidateSession();
		FacesUtils.redirect("/error.faces", true, true);
		FacesUtils.responseComplete();
	}

	public static void desconectarLogin() {
		FacesUtils.clearRequestSession();
		FacesUtils.invalidateSession();
		FacesUtils.redirect("/pages/loginPrescriptor.faces", true, true);
		FacesUtils.responseComplete();
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	@Deprecated
	public static long ctdDiasEntreFechas(Date d1, Date d2) {
		long fechaInicialMs = d1.getTime();
		long fechaFinalMs = d2.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		long dias = diferencia / (1000 * 60 * 60 * 24);
		return dias;
	}

	@Deprecated
	public static long ctdDiasEntreFechasRoundUp(Date d1, Date d2) {
		long fechaInicialMs = d1.getTime();
		long fechaFinalMs = d2.getTime();
		double diferencia = fechaFinalMs - fechaInicialMs;
		long dias = (long) Math.ceil(diferencia / (1000 * 60 * 60 * 24));
		return dias;
	}

	@Deprecated
	public static long ctdMinutosEntreFechas(Date d1, Date d2) {
		long fechaInicialMs = d1.getTime();
		long fechaFinalMs = d2.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		long minutos = diferencia / (1000 * 60);
		return minutos;
	}

	@Deprecated
	public static long ctdSegundosEntreFechas(Date d1, Date d2) {
		return Math.abs(d1.getTime() - d2.getTime()) / 1000;
	}

	@Deprecated
	public static String minutosAHoraMinutos(long minutos) {
		NumberFormat nf = new DecimalFormat("#00");
		long horas = (long) Math.floor(minutos / 60);
		minutos = minutos % 60;
		return nf.format(horas) + ":" + nf.format(minutos);
	}

	@Deprecated
	public static Date sumarDiasAFecha(Date d, int ctdDias) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(d.getTime());
		cal.add(Calendar.DATE, ctdDias);
		return new Date(cal.getTimeInMillis());
	}

	@Deprecated
	public static Date sumarMinutosAFecha(Date d, int minutos) {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(d.getTime());
		cal.add(Calendar.MINUTE, minutos);
		return new Date(cal.getTimeInMillis());
	}

	public static String resumirNombreArchivo(String nombreArchivo, int longitudMax) {
		String resultado = nombreArchivo;

		if (nombreArchivo.length() > longitudMax) {
			int indicePunto = nombreArchivo.lastIndexOf('.');

			if (indicePunto == -1) {
				resultado = nombreArchivo.substring(0, longitudMax);
			} else {
				String nombre = nombreArchivo.substring(0, indicePunto);
				String extension = nombreArchivo.substring(indicePunto);

				nombre = nombre.substring(0, longitudMax - extension.length());

				resultado = nombre + extension;
			}
		}
		return resultado;
	}

	public static String responseHeaderNombreArchivo(String nombre) {
		return nombre.replace(" ", "_");
	}

	public static SelectItem[] populateSelectItem(List<?> elements, String dataFieldLabel, boolean ordered) throws Exception {
		return populateSelectItem(elements, dataFieldLabel, dataFieldLabel, ordered, false, null, null);
	}

	public static SelectItem[] populateSelectItemOrdered(List<?> elements, String dataFieldLabel, boolean addEmptyElement) throws Exception {
		return populateSelectItem(elements, dataFieldLabel, dataFieldLabel, true, false, addEmptyElement ? "" : null, null);
	}

	public static SelectItem[] populateSelectItem(List<?> elements, String dataFieldLabel, boolean ordered, boolean addEmptyElement) throws Exception {
		return populateSelectItem(elements, dataFieldLabel, dataFieldLabel, ordered, false, addEmptyElement ? "" : null, null);
	}

	public static SelectItem[] populateSelectItem(List<?> elements, String dataFieldLabel, String dataFieldValue, boolean ordered) throws Exception {
		return populateSelectItem(elements, dataFieldLabel, dataFieldValue, ordered, false, null, null);
	}

	public static SelectItem[] populateSelectItem(List<?> elements, String dataFieldLabel, String dataFieldValue, boolean ordered, boolean removeDuplicated) throws Exception {
		return populateSelectItem(elements, dataFieldLabel, dataFieldValue, ordered, removeDuplicated, null, null);
	}

	public static SelectItem[] populateSelectItem(List<?> elements, String dataFieldLabel, String dataFieldValue, boolean ordered, boolean removeDuplicated, String firstItemLabel) throws Exception {
		return populateSelectItem(elements, dataFieldLabel, dataFieldValue, ordered, removeDuplicated, firstItemLabel, null);
	}

	public static SelectItem[] populateSelectItem(List<?> elements, String dataFieldLabel, boolean ordered, boolean removeDuplicated, String firstItemLabel) throws Exception {
		return populateSelectItem(elements, dataFieldLabel, dataFieldLabel, ordered, removeDuplicated, firstItemLabel, null);
	}

	public static SelectItem[] populateSelectItem(List<?> elements, String dataFieldLabel, String dataFieldValue, boolean ordered, boolean removeDuplicated, String firstItemLabel, Object firstItemObject) throws Exception {
		SelectItemsBuilder sib = new SelectItemsBuilder();
		if (firstItemLabel != null || firstItemObject != null) {
			sib.add(firstItemObject, firstItemLabel != null ? firstItemLabel : "");
		}
		if (elements != null && elements.size() > 0) {
			Method labelMethod = getMethodFromList(elements, dataFieldLabel);
			if (removeDuplicated) {
				elements = Utils.removeDuplicated(elements, labelMethod);
			}
			if (ordered) {
				Utils.sortList(elements, dataFieldLabel, true);
			}
			if (dataFieldValue != null) {
				Method valueMethod = getMethodFromList(elements, dataFieldValue);
				for (Object obj : elements) {
					sib.add(valueMethod.invoke(obj), labelMethod.invoke(obj).toString());
				}
			} else {
				for (Object obj : elements) {
					sib.add(obj, labelMethod.invoke(obj).toString());
				}
			}
		}
		return sib.build();
	}


	public static List removeDuplicated(List elements, Method method) {
		try {
			if (elements != null) {
				List valores = new LinkedList();
				List retorno = new LinkedList();
				Object value = null;
				for (Object obj : elements) {
					value = method.invoke(obj);
					if (!valores.contains(value)) {
						valores.add(value);
						retorno.add(obj);
					}
				}
				return retorno;
			}
		} catch (Exception e) {
			System.err.println("ERROR AL BUSCAR ATRIBUTO DE LA LISTA: " + method.toString());
			e.printStackTrace();
		}
		return elements;
	}

	public static Method getMethodFromList(List<?> elements, String fieldName) throws Exception {
		if (elements != null && elements.size() > 0) {
			try {
				return elements.get(0).getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
			} catch (Exception e) {
				System.out.println("Error al ordenar: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Metodo que ordena una lista por nombre de propiedad.
	 * @param list lista a ordenar
	 * @param propertyName nombre de la propiedad
	 * @param asc ordenar ascendentemente
	 * @return lista ordenada
	 */
	public static List sortList(List list, String propertyName, boolean asc){
		return sortList(list, asc, propertyName);
	}

	public static List sortList(List list,boolean asc, String ... propertyNames) {
		if (list == null || list.size() < 2 || propertyNames == null || propertyNames.length == 0) {
			return list;
		}
		try {
			Method[] metodos = new Method[propertyNames.length];
			for (int i = 0; i < propertyNames.length; i++) {
				metodos[i] = list.get(0).getClass().getMethod("get" + propertyNames[i].substring(0, 1).toUpperCase() + propertyNames[i].substring(1));
			}
			Collections.sort(list, getComparator(asc,metodos));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private static Comparator getComparator(final boolean asc, final Method ... metodos) {
		Comparator comp = new Comparator() {
			@Override
			public int compare(Object arg0, Object arg1) {
				int retorno = 0;
				try {
					Comparable obj0 = null;
					Comparable obj1 = null;
					for (Method metodo : metodos) {
						obj0 = (Comparable)metodo.invoke(arg0);
						obj1 = (Comparable)metodo.invoke(arg1);
						if (obj0 == null && obj1 == null) {
							retorno = 0;
						} else {
							if (obj0 != null && obj1 != null) {
								retorno = asc ? obj0.compareTo(obj1) : obj1.compareTo(obj0);
							} else {
								retorno = (obj0 != null ? -1 : 1) * (asc ? 1 : -1);
							}
							if (retorno != 0) {
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retorno;
			}
		};
		return comp;
	}

	/**
	 * Método que indica si la fechaTest se encuentra entre la fechaInicio y fechaFin
	 * @param fechaInicio
	 * @param fechaFin
	 * @param fechaTest
	 * @return <ul> <li> true: fechaTest se encuentra adentro <li> false: fechaTest se encuentra afuera
	 * @throws BusinessException Si la fechaInicio es posterior a la fechaFin
	 */
	@Deprecated
	public static boolean isFechaInterna(Date fechaInicio, Date fechaFin, Date fechaTest) throws BusinessException {
		if(fechaInicio.after(fechaFin)) {
			throw new BusinessException(MessageBundle.WRONG_INTERVAL_DATE_2, SeverityBundle.ERROR);
		}
		return fechaInicio.before(fechaTest) && fechaFin.after(fechaTest);
	}

	/**
	 * Método que indica si la fechaTest se encuentra entre la fechaInicio y fechaFin, considerando hasta minutos
	 * @param fechaInicio
	 * @param fechaFin
	 * @param fechaTest
	 * @return <ul> <li> true: fechaTest se encuentra adentro <li> false: fechaTest se encuentra afuera
	 * @throws BusinessException Si la fechaInicio es posterior a la fechaFin
	 */
	@Deprecated
	public static boolean isFechaInternaMinutos(Date fechaInicio, Date fechaFin, Date fechaTest) throws BusinessException {
		Calendar c = Calendar.getInstance();
		c.setTime(fechaInicio);
		c.set(Calendar.SECOND, 0);

		Calendar c1 = Calendar.getInstance();
		c1.setTime(fechaFin);
		c1.set(Calendar.SECOND, 0);
		c1.add(Calendar.MINUTE, 1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(fechaTest);
		c2.set(Calendar.SECOND, 1);

		return isFechaInterna(c.getTime(), c1.getTime(), c2.getTime());
	}

	public static String capitalize(String word) {
		String result = "";

		if (word != null && !"".equals(word)) {
			result = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
		}

		return result;
	}

	public static String toJavaStyle(String sqlStyle) {
		StringBuffer result = new StringBuffer();

		if (sqlStyle != null && !"".equals(sqlStyle)) {
			String tmp = sqlStyle.trim().replace("_", " ").replaceAll("\\p{Space}+", " ");

			String[] words = tmp.split("\\s");

			for (int i = 0; i < words.length; i++) {
				if (i == 0) {
					result.append(words[i].toLowerCase());
				} else {
					result.append(capitalize(words[i]));
				}
			}
		}

		return result.toString();
	}

	public static String prepareStringToSearch(String busqueda) {
		busqueda = "%" + CommonFunctions.replaceAccentedChars(busqueda.replace('+', '%').replace(' ', '%') + "%");
		return busqueda;
	}

	public static String prepareStringToSearchStartWith(String busqueda) {
		busqueda =  CommonFunctions.replaceAccentedChars(busqueda.replace('+', '%').replace(' ', '%') + "%");
		return busqueda;
	}

	public static double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * Método que devuelve la fecha siguiente o anterior (dias negativos) de los
	 * dias ingresados
	 *
	 * @param fecha
	 *            fecha actual
	 * @param dias
	 *            cantidad de dias ha agregar
	 * @return un Date con la nueva fecha
	 */
	@Deprecated
	public static Date fechaSiguiente(Date fecha, int dias) {
		if (fecha != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			calendar.add(Calendar.DAY_OF_YEAR, dias);

			fecha = calendar.getTime();
		}
		return fecha;
	}

	@Deprecated
	public static Date anoSiguiente(Date fecha, int anos) {
		if (fecha != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			calendar.add(Calendar.YEAR, anos);

			fecha = calendar.getTime();
		}
		return fecha;
	}

	public static String getHash(String data, String key, String algorithm) throws NoSuchAlgorithmException {
		return getHash(key + data, algorithm);
	}

	public static String getHash(String data, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md;

		md = MessageDigest.getInstance(algorithm);
		md.update(data.getBytes());

		return convertByteToHex(md.digest());
	}

	public static String convertByteToHex(byte byteData[]) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	public static String cargarImagenByte(byte[] imagen, String prefijo) {
		return cargarImagenByte(imagen, prefijo, true);
	}

	public static String cargarImagenByte(byte[] imagen, String prefijo, boolean unica) {
		String path = null;
		ImageOutputStream out = null;
		try {
			String pathToFile = XMLVersionParser.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
			pathToFile = (System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/"
					: (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\"));

			if (unica) {
				File f = new File(pathToFile);
				File[] ficheros = f.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return (file.isDirectory() || file.getName().toLowerCase().endsWith("png"));
					}
				});
				for (int x = 0; x < ficheros.length; x++) {
					if (ficheros[x].getName().contains(prefijo)) {
						return "/tmp/" + ficheros[x].getName();
					}
				}
			}

			if (imagen != null) {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imagen));
				if (image != null) {
					File imageFile = File.createTempFile(prefijo, ".png", new File(pathToFile));
					imageFile.deleteOnExit();
					out = ImageIO.createImageOutputStream(imageFile);
					ImageIO.write(image, "png", out);
					out.flush();
					path = "/tmp/" + imageFile.getName();
				}
			} else {
				path = null;
			}
		} catch (Exception e) {
			MessageManager.addToMessages(e);
		} finally {
			if (out != null) {
				try {out.close();} catch (Exception e) {}
			}
		}
		return path;
	}

	public static String formatNumber(Double number) {
		NumberFormat nf = new DecimalFormat("0.00;(0.00)");
		return nf.format(number);
	}

	public static boolean validarMascara(String object) {
		String valorAValidar = object;
		String mascara = (String) FacesUtils.getSessionValue("Mascara");

		if(mascara==null) {
			return true;
		}
		if(valorAValidar==null || valorAValidar.contains("_")){
			return false;
		}

		mascara=mascara.replace("-","");
		mascara=mascara.replace("/","");

		valorAValidar=valorAValidar.replace("-","");
		valorAValidar=valorAValidar.replace("/","");

		if (mascara.length() != valorAValidar.length()) {
			return false;
		}

		for (int i = 0; (i < mascara.length() ); i++) {
			boolean bool1=Character.isLetter(mascara.charAt(i));
			boolean bool2=(mascara.charAt(i)=='A' || mascara.charAt(i)=='a');
			boolean bool3=mascara.charAt(i)=='#';
			if(bool1 && bool2){
				if(!Character.isLetter(valorAValidar.charAt(i))){
					return false;
				}
			}
			if(bool3){
				if(!Character.isDigit(valorAValidar.charAt(i))){
					return false;
				}
			}
		}
		return true;
	}

	public static boolean existsTmpFile(String path){
		String aux = XMLVersionParser.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
		aux = System.getProperty("file.separator").equals("/") ? "/" + aux.split("WEB-INF")[0] : (aux.split("WEB-INF")[0]).replace("/", "\\");
		File file = new File(aux + path);
		return file != null && file.exists();
	}

	public static List sortListNulosAlFinal(List list,Object[] ... propertyNames) {
		if (list == null || list.size() < 2 || propertyNames == null || propertyNames.length == 0) {
			return list;
		}
		try {
			List<Object[]> metodos = new LinkedList();
			for (int i = 0; i < propertyNames.length; i++) {
				Object[] aux = new Object[2];
				aux[0] = list.get(0).getClass().getMethod("get" + ((String)propertyNames[i][0]).substring(0, 1).toUpperCase() + ((String)propertyNames[i][0]).substring(1));
				aux[1] = propertyNames[i][1];
				metodos.add(aux);
			}
			Collections.sort(list, getComparatorNulosAlFinal(metodos));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private static Comparator getComparatorNulosAlFinal(final List<Object[]> metodos) {
		Comparator comp = new Comparator() {
			@Override
			public int compare(Object arg0, Object arg1) {
				int retorno = 0;
				try {
					Comparable obj0 = null;
					Comparable obj1 = null;
					for (Object[] metodo : metodos) {
						obj0 = (Comparable)((Method)metodo[0]).invoke(arg0);
						obj1 = (Comparable)((Method)metodo[0]).invoke(arg1);
						if (obj0 == null && obj1 == null) {
							retorno = Integer.MAX_VALUE;
						} else {
							if (obj0 != null && obj1 != null) {
								retorno = (Boolean)metodo[1] ? obj0.compareTo(obj1) : obj1.compareTo(obj0);
							} else {
								retorno = (obj0 != null ? -1 : 1) * ((Boolean)metodo[1] ? Integer.MAX_VALUE : Integer.MAX_VALUE);
							}
							if (retorno != 0) {
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retorno;
			}
		};
		return comp;
	}

}