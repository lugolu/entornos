package ar.com.thinksoft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class Varios {

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param fec String de la forma "Sat Nov 03 00:00:00 GMT-03:00 2007"
	 * @return un objeto de la clase util.Date, con la fecha del parametro fec.
	 */
	public static Date armarFecha(String fec){
		String[] meses ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

		String dia=fec.substring(8, 10);
		String ano=fec.substring(fec.length()-4, fec.length());
		int mes=-1;
		for(int i=0;i<12;i++){
			if(meses[i].equalsIgnoreCase(fec.substring(4,7))){
				mes=i+1;
				break;
			}
		}

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return df.parse(dia+"/"+mes+"/"+ano);
		}
		catch (ParseException e) {
			return null;
		}
	}

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param fec String de la forma "Sat Nov 03 00:00:00 GMT-03:00 2007"
	 * @return Una String formateada con el patron "dd/MM/yyyy" Ej: "03/11/2007"
	 */
	public static String retornarFecha(String fec){
		String[] meses ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

		String dia=fec.substring(8, 10);
		String ano=fec.substring(fec.length()-4, fec.length());
		int mes=-1;
		for(int i=0;i<12;i++){
			if(meses[i].equalsIgnoreCase(fec.substring(4,7))){
				mes=i+1;
				break;
			}
		}

		return (dia+"/"+mes+"/"+ano);
	}

	public static double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param fecha Objeto del tipo java.util.Date, que quiere ser mostrado en formato String
	 * @return Un String que representa la fecha ingresada de la forma dd/MM/yyyy
	 */
	public static String parseDate(Date fecha){
		return parseDate(fecha, "dd/MM/yyyy");
	}

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param fecha Objeto del tipo java.util.Date, que quiere ser mostrado en formato String
	 * @return Un String que representa la fecha ingresada de la forma dd/MM/yyyy
	 */
	public static String parseDate(Date fecha, String formato){
		if (fecha == null) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(formato);
		return df.format(fecha);
	}

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param fecha
	 * @return
	 */
	public static Date parseFecha(String fec){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return df.parse(fec);
		} catch (ParseException e) {
			return null;
		}
	}

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param fecha
	 * @return
	 */
	public static String armarFechaHora(Date fecha){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		return df.format(fecha);
	}

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param date
	 * @return
	 */
	public static String dateToORASQLString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return "TO_DATE('" + sdf.format(date) + "','dd/MM/yyyy HH24:mi:ss')";
	}

	@Deprecated
	/**
	 * @deprecated usar metodo de clase DateCommonFunctions
	 * @param fec
	 * @return
	 */
	public static Timestamp parseFecha2(String fec){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fecha;
		try {
			fecha = df.parse(fec);
		} catch (ParseException e) {
			return null;
		}
		return new Timestamp(fecha.getTime());
	}

	/**
	 * Reemplaza cualquier cantidad de espacios por uno solo
	 * @param string
	 * @return
	 */
	public static String simplifySpaces(String string){
		return string.replaceAll("\\p{Space}+", " ");
	}

	/**
	 * Get the unaccented uppercased trimed version of the string
	 * @param string
	 * @return
	 */
	public static String replaceAccentedChars(String string) {
		if(string == null) {
			return null;
		}

		String tmpString = new String(string.toUpperCase().toString());
		char[] arrFind = {'\u00C7','\u00C0','\u00C8','\u00CC','\u00D2','\u00D9','\u00C1','\u00C9','\u00CD','\u00D3','\u00DA','\u00B4','\u00C4','\u00CB','\u00CF','\u00D6','\u00DC','\''};
		char[] arrReplace= {'C','A','E','I','O','U','A','E','I','O','U',' ','A','E','I','O','U',' '};

		for(int i=0; i < arrFind.length; i++){
			tmpString = tmpString.replaceAll(String.valueOf(arrFind[i]), String.valueOf(arrReplace[i]));
		}

		return simplifySpaces(tmpString.trim());
	}

	public static void copy(File origen, File destino) throws IOException {
		byte[] buf = new byte[1024];
		int cant;
		FileInputStream in = new FileInputStream(origen);
		FileOutputStream out = new FileOutputStream(destino);

		while ((cant = in.read(buf, 0, 1024)) != -1) {
			out.write(buf, 0, cant);
		}
		in.close();
		out.close();
	}

	public static void move(File origen, File destino) throws IOException{
		copy(origen, destino);
		origen.delete();
	}

	public static void copySmb(SmbFile origen, SmbFile destino) throws IOException {
		byte[] buf = new byte[1024];
		int cant;
		SmbFileInputStream in = new SmbFileInputStream(origen);
		SmbFileOutputStream out = new SmbFileOutputStream(destino);

		while ((cant = in.read(buf, 0, 1024)) != -1) {
			out.write(buf, 0, cant);
		}
		in.close();
		out.close();
	}

	public static void moveSmb(SmbFile origen, SmbFile destino) throws IOException{
		copySmb(origen, destino);
		origen.delete();
	}

	public static String reemplazarCaracterXml(String text) {
		if (text!=null){
			text=text.replace('&', 'Y');
			text=text.replace('<', ' ');
			text=text.replace('>', ' ');
		}
		return text;
	}

	public static String reemplazarCaracterXmlNullSafe(String text) {
		if (text == null) {
			return "";
		}
		if (text!=null){
			text=text.replace('&', 'Y');
			text=text.replace('<', ' ');
			text=text.replace('>', ' ');
		}
		return text;
	}

	public static boolean isNumber(String string){
		if (string == null || string.isEmpty()){
			return false;
		}
		int i = 0;
		if (string.charAt(0) == '-'){
			if (string.length() > 1){
				i++;
			}
			else{
				return false;
			}
		}
		for (; i < string.length(); i++){
			if (!Character.isDigit(string.charAt(i))){
				return false;
			}
		}
		return true;
	}

}
