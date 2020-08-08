package ar.com.thinksoft.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.exception.FileProcessError;

public class CommonFunctions {

	private static final String CR1 = new String("[CR]");
	private static final String CR2 = new String("\u00B4CR+");
	private static final String CR3 = new String("\u0060CR+");
	private static final String CR4 = new String("\u00A8CR\u00C7");
	private static final String GS = new String("@");
	private static final String[] GS_REPLACE = {
			new String("[GS]"), new String("[gs]"), new String("\"GS|"), new String("\u005C\u0022GS\u00C7"),
			new String("\"gs|"), new String("\u00A8GS\u00C7"), new String("\u00A8gs\u00E7"),
			new String("\u00C7GS\u00B4"), new String("\u00E7gs\u00B4"), new String("\u00B4GS+"),
			new String("\u0060GS+"), new String("\u00B4gs+"), /*new String("GS"),
		new String("gs"), */new String("\u005C\u0022GS\u00C7"), new String("\u005C\u0022gs\u00C7"),
		new String("\u201DGS\u00C7"), new String("\u201Dgs\u00C7"),
		new String("\u201CGS\u00C7"), new String("\u201Cgs\u00C7")};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Method getGenericMethod(String methodName, Class baseClass)
			throws Exception {
		Method m;
		m = baseClass.getMethod(methodName);
		return m;
	}

	public static Long stringToLong(String string) {
		if (string == null || string.trim().equals("")) {
			return null;
		}
		Long aux = null;
		try {
			aux = new Long(Long.parseLong(string));
		} catch (Exception e) {
		}
		return aux;
	}

	@RemapTest(name="stringToLongBeginEnd")
	public static Long stringToLong(String string, int begin, int end) {
		if (string == null || string.trim().equals("") || begin > end
				|| begin < 0) {
			return null;
		}
		Long aux = null;
		try {
			aux = new Long(Long.parseLong(string.substring(begin, end)));
		} catch (Exception e) {
		}
		return aux;
	}

	public static Double stringToDouble(String string, int begin, int end) {
		if (string == null || string.trim().equals("") || begin > end
				|| begin < 0) {
			return null;
		}
		Double aux = null;
		try {
			aux = new Double(Double.parseDouble(string
					.substring(begin, end - 2)
					+ "." + string.substring(end - 2, end)));
		} catch (Exception e) {
		}
		return aux;
	}

	public static String stringToString(String string, int begin, int end) {
		if (string == null || string.trim().equals("") || begin > end
				|| begin < 0) {
			return null;
		}
		String aux = null;
		try {
			aux = string.substring(begin, end).trim();
			if ("".equals(aux)) {
				aux = null;
			}
		} catch (Exception e) {
		}
		return aux;
	}

	public static String stringToSN(String string, int begin, int end) {
		if (string == null || string.trim().equals("") || begin > end
				|| begin < 0) {
			return null;
		}
		String aux = null;
		try {
			aux = string.substring(begin, end).trim();
			if ("0".equals(aux)) {
				aux = "N";
			} else if ("1".equals(aux)) {
				aux = "S";
			} else {
				aux = null;
			}
		} catch (Exception e) {
		}
		return aux;
	}

	public static String stringToStringFormat(String string) {
		if (string == null) {
			return null;
		}
		String format = null;
		if(string.length() > 0){
			for(int i = 0; i<string.length();i++){
				if(i == 0){
					format = "(" + string.substring(i, i+1);
				}
				if(i > 0 && i < 3){
					format = format + string.substring(i, i+1);
				}
				if(i == 3){
					format = format + ") " + string.substring(i, i+1);
				}
				if(i > 3 && i < 6){
					format = format + string.substring(i, i+1);
				}
				if(i == 5){
					format = format + "-";
				}
				if(i > 5 && i < 10){
					format = format + string.substring(i, i+1);
				}
				if(i == 10){
					format = format + " Ext. " + string.substring(i, string.length());;
				}
			}
		}
		return format;
	}

	public static Boolean stringToBoolean(String string) {
		return "S".equals(string) ? true : false;
	}

	public static String booleanToString(Boolean bool) {
		return bool? "S": "N";
	}

	public static String addLeadingZeros(int nroCeros, Object objeto) {
		return String.format("%0" + nroCeros + "d", objeto);

	}

	public static String addLeadingZerosStr(String template, String str) {
		StringBuffer sTest = new StringBuffer(template);
		sTest.append(String.valueOf(str));
		return sTest.substring(sTest.length()-template.length(), sTest.length());
	}

	/**
	 * Get the unaccented uppercased trimed version of the string
	 * @param string
	 * @return
	 */
	public static String replaceAccentedChars(String string){
		if(string==null) {
			return null;
		}
		char[] arrFind = {'\u00C7','\u00C0','\u00C8','\u00CC','\u00D2','\u00D9','\u00C1','\u00C9','\u00CD','\u00D3','\u00DA','\u00B4','\u00C4','\u00CB','\u00CF','\u00D6','\u00DC','\''};
		char[] arrReplace= {'C','A','E','I','O','U','A','E','I','O','U',' ','A','E','I','O','U',' '};
		string = string.toUpperCase().trim().replaceAll("\\p{Space}+", " ");
		for(int i=0; i<arrFind.length;i++){
			string = string.replace(arrFind[i],arrReplace[i]);
		}
		return "".equals(string) ? null : string;
	}

	public static String replaceAccentedCharsWithoutUpperCase(String string){
		if(string==null) {
			return null;
		}
		char[] arrFind = {'\u00C7','\u00C0','\u00C8','\u00CC','\u00D2','\u00D9','\u00C1','\u00C9','\u00CD','\u00D3','\u00DA','\u00B4','\u00C4','\u00CB','\u00CF','\u00D6','\u00DC','\''};
		char[] arrReplace= {'C','A','E','I','O','U','A','E','I','O','U',' ','A','E','I','O','U',' '};
		//string = string.trim().replaceAll("\\p{Space}+", " ");
		for(int i=0; i<arrFind.length;i++){
			string = string.replace(arrFind[i],arrReplace[i]);
		}
		return "".equals(string) ? null : string;
	}

	public static String replaceAccentedCharsWithUpperCase(String string){
		if(string==null) {
			return null;
		}
		char[] arrFind = {'\u00C7','\u00C0','\u00C8','\u00CC','\u00D2','\u00D9','\u00C1','\u00C9','\u00CD','\u00D3','\u00DA','\u00B4','\u00C4','\u00CB','\u00CF','\u00D6','\u00DC','\''};
		char[] arrReplace= {'C','A','E','I','O','U','A','E','I','O','U',' ','A','E','I','O','U',' '};
		string = string.toUpperCase().trim();
		for(int i=0; i<arrFind.length;i++){
			string = string.replace(arrFind[i],arrReplace[i]);
		}
		return "".equals(string) ? null : string;
	}

	public static String replaceAllAccentedSpaceChars(String string){
		return string == null ? null : replaceAllAccentedChars(string).replaceAll("\\p{Space}+", "").replaceAll("[^\\w\\s]+", "");
	}

	public static String replaceAllAccentedChars(String string){
		return string == null ? null : Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String escapeHtml(String string){
		if(string==null) {
			return null;
		}
		return StringEscapeUtils.escapeHtml4(string);
	}

	public static String unescapeHtml(String string){
		if(string==null) {
			return null;
		}
		return StringEscapeUtils.unescapeHtml4(string);
	}

	public static String lpad(String valueToPad, String filler, int size) {
		if(valueToPad == null) {
			valueToPad = "";
		}
		while (valueToPad.length() < size) {
			valueToPad = filler + valueToPad;
		}
		return valueToPad;
	}

	public static String rpad(String valueToPad, String filler, int size) {
		if(valueToPad == null) {
			valueToPad = "";
		}
		while (valueToPad.length() < size) {
			valueToPad = valueToPad + filler;
		}
		return valueToPad;
	}

	@RemapTest(name="stringFillerFiller")
	public static String stringFiller(String value, char filler, int length) {
		char[] fill = new char[length - (value != null ? value.length() : 0)];
		Arrays.fill(fill, filler);
		return (value != null ? value : "") + new String(fill);
	}

	public static String stringFiller(String value, int length) {
		return stringFiller(value, ' ', length);
	}

	public static String stringFillerWithLimit(String value, int length) {
		if(value!=null && value.length()>=length) {
			value = value.substring(0,length);
		}
		return stringFiller(value, ' ', length);
	}

	public static String longFormat(Long value, int length) {
		char[] fill = new char[length];
		Arrays.fill(fill, '0');
		return new DecimalFormat(new String(fill)).format(value != null ? value : 0l);
	}

	public static String stringNullValue(String value) {
		return value != null && !"".equals(value.trim()) ? value : null;
	}

	public static String toJava(String clasificacionImc) {
		return clasificacionImc == null ? null : clasificacionImc.toLowerCase().replaceAll(" ", "_");
	}

	@RemapTest(name="roundScale")
	public static Double round(Double value, int decimalPlaces, int scale) {
		if (value == null) {
			return null;
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(decimalPlaces, scale);
		return bd.doubleValue();
	}

	public static Double round(Double value, int decimalPlaces){
		return round(value, decimalPlaces, BigDecimal.ROUND_HALF_EVEN);
	}

	public static Long ceil(Double value) {
		if (value == null) {
			return null;
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(0,BigDecimal.ROUND_CEILING);
		return bd.longValue();
	}

	public static String capitalizeAll(String word) {
		if (word == null || "".equals(word)) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		for (String aux : word.split(" ")) {
			result.append(capitalize(aux) + " ");
		}
		return result.toString().trim();
	}

	public static String capitalize(String word) {
		String result = "";
		if (word != null && !"".equals(word)) {
			result = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
		}
		return result;
	}

	@RemapTest(name="toStringNullSafeString")
	public static String toStringNullSafe (String str) {
		return str == null ? "" : str;
	}

	@RemapTest(name="toStringNullSafeCharacter")
	public static String toStringNullSafe (Character str) {
		return str == null ? "" : str.toString();
	}

	public static String subString (String str, int maxLength) {
		if (str != null && str.length() > maxLength) {
			str = str.substring(0, maxLength);
		}
		return str;
	}

	/**
	 * Metodo que ordena una lista por nombre de propiedad.
	 * @param list lista a ordenar
	 * @param propertyName nombre de la propiedad
	 * @param asc ordenar ascendentemente
	 * @return lista ordenada
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List sortList(List list, String propertyName, boolean asc){
		if (list == null || list.size() < 2) {
			return list;
		}
		try {
			Method metodo = list.get(0).getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
			Collections.sort(list, getComparator(metodo, asc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RemapTest(name="sortListMultiple")
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Comparator getComparator(final Method metodo, final boolean asc) {
		Comparator comp = new Comparator() {
			@Override
			public int compare(Object arg0, Object arg1) {
				int retorno = 0;
				try {
					Comparable obj0 = (Comparable)metodo.invoke(arg0);
					Comparable obj1 = (Comparable)metodo.invoke(arg1);
					if (obj0 != null || obj1 != null) {
						if (asc) {
							retorno = obj0 != null ? obj0.compareTo(obj1) : 1;
						} else {
							retorno = obj1 != null ? obj1.compareTo(obj0) : -1;
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

	@SuppressWarnings("rawtypes")
	private static Comparator getComparator(final boolean asc, final Method ... metodos) {
		Comparator comp = new Comparator() {
			@SuppressWarnings("unchecked")
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

	public static String longToString (Long number) {
		if (number == null) {
			return null;
		} else {
			return number.toString();
		}
	}

	public static String longToStringNullSafe (Long number) {
		if (number == null) {
			return "";
		} else {
			return number.toString();
		}
	}

	public static String doubleToStringNullSafe (Double number) {
		if (number == null) {
			return "";
		} else {
			return number.toString();
		}
	}

	public static String getLabelComprobante(String s){

		if (s == null || s.length() == 0) {
			return null;
		}
		if ("FA".equals(s)) {
			return Resources.getValue("FACTURA");
		}
		if ("ND".equals(s)) {
			return Resources.getValue("NOTA_DEBITO");
		}
		if ("NC".equals(s)) {
			return Resources.getValue("NOTA_CREDITO");
		}

		return s;
	}

	public static String getLabelCompInterno(String s){

		if (s == null || s.length() == 0) {
			return null;
		}
		if ("AD".equals(s)) {
			return Resources.getValue("AJUSTE_DEBITO");
		}
		if ("AC".equals(s)) {
			return Resources.getValue("AJUSTE_CREDITO");
		}
		if ("RE".equals(s)) {
			return Resources.getValue("RECIBO");
		}
		if ("CD".equals(s)) {
			return Resources.getValue("COMPROBANTE_DEVOLUCION");
		}
		if ("EF".equals(s)) {
			return Resources.getValue("EGRESO_FONDO");
		}
		if ("IF".equals(s)) {
			return Resources.getValue("INGRESO_FONDO");
		}
		if ("CE".equals(s)) {
			return Resources.getValue("COMPROBANTE_EGRESO");
		}
		if ("CI".equals(s)) {
			return Resources.getValue("COMPROBANTE_INGRESO");
		}
		if ("CL".equals(s)) {
			return Resources.getValue("COMPROBANTE_LIQUIDACION");
		}
		if ("CP".equals(s)) {
			return Resources.getValue("COMPROBANTE_PAGO");
		}
		if ("AG".equals(s)) {
			return Resources.getValue("APLICACION_GARANTIA");
		}
		if ("DG".equals(s)) {
			return Resources.getValue("DEVOLUCION_GARANTIA");
		}
		if ("LH".equals(s)) {
			return Resources.getValue("LIQUIDACION_HONORARIO");
		}
		if ("OP".equals(s)) {
			return Resources.getValue("ORDEN_PAGO");
		}
		if ("RG".equals(s)) {
			return Resources.getValue("RECIBO_GARANTIA");
		}
		if ("SI".equals(s)) {
			return Resources.getValue("SALDO_INICIAL");
		}



		return s;
	}

	//Métodos para devolver los nombres de los días de la semana

	public static String diasDeLaSemana(Long nroDeDias){
		String result = null;
		if(nroDeDias == 1){
			result = "Domingo";
		}
		if(nroDeDias == 2){
			result = "Lunes";
		}
		if(nroDeDias == 3){
			result = "Martes";
		}
		if(nroDeDias == 4){
			result = "Miercoles";
		}
		if(nroDeDias == 5){
			result = "Jueves";
		}
		if(nroDeDias == 6){
			result = "Viernes";
		}
		if(nroDeDias == 7){
			result = "Sabado";
		}
		return result;
	}

	public static String cleanStringAndReplace(String string, String field, int lineNumber, int length, boolean required) throws FileProcessError {
		return cleanStringAndReplace(string, field, lineNumber, length, required, null);
	}

	@RemapTest(name="cleanStringAndReplaceCheck")
	public static String cleanStringAndReplace(String string, String field, int lineNumber, int length, boolean required, String[] checkValues) throws FileProcessError {
		return replaceAccentedChars(cleanString(string, field, lineNumber, length, required, checkValues));
	}

	public static String cleanString(String string, String field, int lineNumber, int length, boolean required) throws FileProcessError {
		return cleanString(string, field, lineNumber, length, required, null);
	}

	/**
	 * Get and uppercased trimed version of the string returning null if the string is empty ("")
	 * @param string the string to be cleaned
	 * @param field the message to be shown in the exception
	 * @param lineNumber the line number to be shown in the exception
	 * @param length the maximun length if the sting
	 * @param required indicates if the string can't be null
	 * @param checkValue indicates if the string value needs to be in the specified range
	 * @return the string cleaned
	 * @throws FileProcessError
	 */
	@RemapTest(name="cleanStringCheck")
	public static String cleanString(String string, String field, int lineNumber, int length, boolean required, String[] checkValue) throws FileProcessError {
		String tmpString = null;
		if(string != null) {
			tmpString = string.toUpperCase().trim().replaceAll("\\p{Space}+", " ");
			if ("".equals(tmpString)) {
				string = null;
			}
		}
		if (string == null && required) {
			throw new FileProcessError(new Long(lineNumber), field + MessageBundle.REQUIRED_FIELD_ERROR);
		}
		if (tmpString != null
				&& tmpString.length() > length) {
			throw new FileProcessError(new Long(lineNumber), field + MessageBundle.MAXIMUM_LENGTH_EXCEEDED_ERROR + length);
		}
		if (checkValue != null && checkValue.length > 0 && string != null) {
			boolean valido = false;
			for (String element : checkValue) {
				if (string.equals(element)) {
					valido = true;
					break;
				}
			}
			if (!valido) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.VALOR_NO_VALIDO_VALORES_POSIBLES + checkValue.toString());
			}
		}
		return  tmpString;
	}

	public static Date cleanDate(String string, String field, int lineNumber, boolean required) throws FileProcessError {
		return cleanDate(string, field, lineNumber, required, Constants.STRING_ddMMyyyy);
	}

	@RemapTest(name="cleanDateWithFormat")
	public static Date cleanDate(String string, String field, int lineNumber, boolean required, String format) throws FileProcessError {
		Date retorno = null;
		String tmpString = null;
		if(string != null) {
			tmpString = string.toUpperCase().trim().replaceAll("\\p{Space}+", "");
			if ("".equals(tmpString) || "//".equals(tmpString)) {
				tmpString = null;
			}
		}
		if (tmpString != null) {
			if (format == null) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.DATE_FORMAT_NOT_SETTED);
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format.replace("DD", "dd").replace("YY", "yy"));
			try {
				String anio = tmpString.substring(6);
				if(anio.length() > 4) {
					throw new FileProcessError(new Long(lineNumber), field + MessageBundle.DATE_FORMAT_EXCEPTION);
				} else {
					retorno = sdf.parse(tmpString);
				}
			} catch (Exception pe) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.DATE_FORMAT_EXCEPTION);
			}
		}
		else if (required) {
			throw new FileProcessError(new Long(lineNumber), field + MessageBundle.REQUIRED_FIELD_ERROR);
		}
		return retorno;
	}

	public static Long cleanLong(String string, String field, int lineNumber, int length, boolean required) throws FileProcessError {
		Long retorno = null;
		String tmpString = null;
		if(string != null) {
			tmpString = string.toUpperCase().trim().replaceAll("\\p{Space}+", "");
			if ("".equals(tmpString)) {
				string = null;
			}
			if (tmpString.endsWith(".0")) {
				tmpString = tmpString.substring(0, tmpString.length() - 2);
			}
		}
		if (string != null) {
			if (tmpString.length() > length) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.MAXIMUM_LENGTH_EXCEEDED_ERROR + length);
			}
			try {
				retorno =  Long.parseLong(tmpString);
			} catch (Exception pe) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.LONG_FORMAT_EXCEPTION);
			}
		}
		else if (required) {
			throw new FileProcessError(new Long(lineNumber), field + MessageBundle.REQUIRED_FIELD_ERROR);
		}
		return retorno;
	}

	public static Double cleanDouble(String string, String field, int lineNumber, int length, boolean required) throws FileProcessError {
		Double retorno = null;
		String tmpString = null;
		if(string != null) {
			tmpString = string.toUpperCase().trim().replaceAll("\\p{Space}+", "");
			if ("".equals(tmpString)) {
				string = null;
			}
		}
		if (string != null) {
			try {
				retorno =  Double.parseDouble(tmpString);
				if (retorno.toString().length() > length) {
					throw new FileProcessError(new Long(lineNumber), field + MessageBundle.MAXIMUM_LENGTH_EXCEEDED_ERROR + length);
				}
			} catch (Exception pe) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.DOUBLE_FORMAT_EXCEPTION);
			}
		}
		else if (required) {
			throw new FileProcessError(new Long(lineNumber), field + MessageBundle.REQUIRED_FIELD_ERROR);
		}
		return retorno;
	}

	@RemapTest(name="cleanDoubleLocale")
	public static Double cleanDouble(String string, String field, int lineNumber, int length, boolean required, Locale locale) throws FileProcessError {
		Double retorno = null;
		String tmpString = null;
		NumberFormat nf;
		nf = NumberFormat.getInstance(locale);

		if(string != null) {
			tmpString = string.toUpperCase().trim().replaceAll("\\p{Space}+", "");
			if ("".equals(tmpString)) {
				string = null;
			}
		}
		if (string != null) {
			try {
				retorno =  nf.parse(tmpString).doubleValue();
				if (retorno.toString().length() > length) {
					throw new FileProcessError(new Long(lineNumber), field + MessageBundle.MAXIMUM_LENGTH_EXCEEDED_ERROR + length);
				}
			} catch (Exception pe) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.DOUBLE_FORMAT_EXCEPTION);
			}
		}
		else if (required) {
			throw new FileProcessError(new Long(lineNumber), field + MessageBundle.REQUIRED_FIELD_ERROR);
		}
		return retorno;
	}

	@RemapTest(name="cleanDoubleRedondeo")
	public static Double cleanDouble(String string, String field,
			int lineNumber, int length, boolean required, Locale locale,
			boolean aplicaRedondeo, String redondeo) throws FileProcessError {
		Double retorno = null;
		String tmpString = null;
		NumberFormat nf;
		nf = NumberFormat.getInstance(locale);

		if(string != null) {
			tmpString = string.toUpperCase().trim().replaceAll("\\p{Space}+", "");
			if ("".equals(tmpString)) {
				string = null;
			}
		}
		if (string != null) {
			try {
				retorno =  nf.parse(tmpString).doubleValue();
				if (retorno.toString().length() > length) {
					throw new FileProcessError(new Long(lineNumber), field + MessageBundle.MAXIMUM_LENGTH_EXCEEDED_ERROR + length);
				}
				if (aplicaRedondeo){

					if (redondeo == null){
						throw new BusinessException(MessageBundle.FALTA_SELECCIONAR_REDONDEO, SeverityBundle.WARN);
					}
					if (redondeo.equals(Constants.STRING_0_10)){
						int parteEntera= (int)(double)retorno;
						String decimalesString = ((Double) redondearADecimales(retorno-parteEntera, 2))
								.toString().substring(0,3).concat("0");
						Double decimales = new Double(decimalesString);
						retorno = Math.floor(retorno)+decimales;
					} else if (redondeo.equals(Constants.STRING_0_50)){
						int parteEntera= (int)(double)retorno;
						if (redondearADecimales(retorno-parteEntera, 2) >= new Double("0.50")){
							String decimalesString = ((Double) redondearADecimales(retorno-parteEntera, 2))
									.toString().substring(0,2).concat("50");
							Double decimales = new Double(decimalesString);
							retorno = Math.floor(retorno)+decimales;
						} else {
							String decimalesString = ((Double) redondearADecimales(retorno-parteEntera, 2))
									.toString().substring(0,2).concat("00");
							Double decimales = new Double(decimalesString);
							retorno = Math.floor(retorno)+decimales;
						}
					} else {
						retorno = Math.floor(retorno);
					}
				}
			} catch (Exception pe) {
				throw new FileProcessError(new Long(lineNumber), field + MessageBundle.DOUBLE_FORMAT_EXCEPTION);
			}
		}
		else if (required) {
			throw new FileProcessError(new Long(lineNumber), field + MessageBundle.REQUIRED_FIELD_ERROR);
		}
		return retorno;
	}

	public static double redondearADecimales(Double numero,Integer digitos) {
		int cifras=(int) Math.pow(10,digitos);
		return Math.rint(numero*cifras)/cifras;
	}



	public static String replaceAccentedCharsAndKeepCase(String string){
		if(string==null) {
			return null;
		}

		char[] arrFind = {'\u00C7','\u00C0','\u00C8','\u00CC','\u00D2','\u00D9','\u00C1','\u00C9','\u00CD','\u00D3','\u00DA','\u00B4','\u00C4','\u00CB','\u00CF','\u00D6','\u00DC',
				'\u00E0','\u00E1','\u00E8','\u00E9','\u00EC','\u00ED','\u00F2','\u00F3','\u00F9','\u00FA'};
		char[] arrReplace= {'C'   ,'A'     ,'E'     ,'I'     ,'O'     ,'U'     ,'A'     ,'E'     ,'I'     ,'O'     ,'U'     ,'\''    ,'A'     ,'E'     ,'I'     ,'O'     ,'U'     ,
				'a'   ,'a'     ,'e'     ,'e'     ,'i'     ,'i'     ,'o'     ,'o'     ,'u'     ,'u'};

		for(int i=0; i<arrFind.length;i++){
			string = string.replace(arrFind[i],arrReplace[i]);
		}
		return "".equals(string) ? null : string;
	}

	public static String limpiarCodBarra(String codBarra) {
		if (codBarra != null) {
			for (String element : GS_REPLACE) {
				codBarra = codBarra.replace(element, GS);
			}
			codBarra = codBarra.replace("\"", Constants.STRING_EMPTY);
			codBarra = codBarra.replace("CR\u007C", Constants.STRING_EMPTY);

			if (codBarra.endsWith(CR1)
					|| codBarra.endsWith(CR2)
					|| codBarra.endsWith(CR3)
					|| codBarra.endsWith(CR4)) {
				codBarra = codBarra.substring(0, codBarra.length() - 4);
			}
		}
		return codBarra;
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

	public static Object getDataFieldValue(Object o, String dataFieldName) throws Exception {
		Object ret = null;

		if(o == null){
			return null;
		}

		String aux1 = "get";
		String aux2 = dataFieldName.substring(0, 1);
		aux2 = aux2.toUpperCase();
		String aux3 = dataFieldName.substring(1);

		String getMethodString = aux1 + aux2 + aux3;

		ret = o.getClass().getMethod(getMethodString).invoke(o);

		return ret;
	}

	public static InetAddress getLocalInetAddress() {
		InetAddress direccionIp = null;
		NetworkInterface network = null;
		byte[] mac;
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements())
			{
				NetworkInterface n = e.nextElement();

				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements())
				{
					InetAddress i = ee.nextElement();

					network = NetworkInterface.getByInetAddress(i);

					mac = network.getHardwareAddress();

					if(mac == null || mac.length < 1) {
						//System.out.println(" omitiendo...");
						continue;
					}

					if(i.isLoopbackAddress() || i.isMulticastAddress() || i.isLinkLocalAddress() || i.isAnyLocalAddress() ||  i.getClass() == Inet6Address.class) {
						//System.out.println(" omitiendo...");
						continue;
					}

					return i;
				}
			}

		}catch (Exception e) {
			System.out.println("Error Obteniendo direccion ip:" + e.getStackTrace());
			direccionIp = null;
		}

		return direccionIp;
	}


	public static String getIp() {
		String direccionIp = null;
		try {
			InetAddress ip;
			/////////////////////////////////////////////////////////////////////////////
			ip = getLocalInetAddress();
			if(ip!= null) {
				direccionIp = ip.getHostAddress();
			}
		}catch (Exception e) {
			System.out.println("Error Obteniendo direccion ip:" + e.getStackTrace());
			direccionIp = null;
		}

		return direccionIp;
	}

	public static String getMacAddress() {
		String direccionMac = null;
		try {
			InetAddress ip;
			/////////////////////////////////////////////////////////////////////////////
			ip = getLocalInetAddress();

			if(ip!= null) {
				NetworkInterface network = NetworkInterface.getByInetAddress(ip);
				if(network != null) {
					byte[] mac = network.getHardwareAddress();
					if(mac != null) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < mac.length; i++) {
							sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
						}
						direccionMac = sb.toString();
					}
				}
			}

		}catch (Exception e) {
			System.out.println("Error Obteniendo direccion Mac:" + e.getStackTrace());
			direccionMac = null;
		}

		return direccionMac;
	}

	public static void copiarValoresObjetos(Object origen, Object destino) throws Exception {

		if(origen == null || destino == null) {
			return;
		}

		if(!origen.getClass().getName().equals(destino.getClass().getName()) ) {
			throw new BusinessException("Los objetos son de distinta clase", SeverityBundle.ERROR);
		}

		Field[] fields = destino.getClass().getDeclaredFields();
		for (Field campo  : fields) {

			campo.setAccessible(true);
			Object value = campo.get(origen);
			campo.set(destino, value);
			campo.setAccessible(false);


		}



	}


}