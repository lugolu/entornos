package ar.com.thinksoft.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class DateCommonFunctions {

	public static final String STRING_ddMMyyyy = new String("dd/MM/yyyy");
	public static final String STRING_HHmm = new String("HH:mm");
	public static final String STRING_ddMMyyHHmm = new String("dd/MM/yy HH:mm");

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String dateToString(Date fecha) {
		return dateToString(fecha, null);
	}

	/**
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	@RemapTest(name="dateToStringFormat")
	public static String dateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format != null ? format : STRING_ddMMyyyy);
		return date != null ? sdf.format(date) : "";
	}

	/**
	 *
	 * @param date
	 * @param format
	 * @parem locale
	 * @return
	 */
	@RemapTest(name="dateToStringFormat")
	public static String dateToString(Date date, String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format != null ? format : STRING_ddMMyyyy, locale);
		return date != null ? sdf.format(date) : "";
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static Date truncDate(Date date) {
		Date retorno = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(STRING_ddMMyyyy);
			retorno = sdf.parse(sdf.format(date));

		} catch (Exception e) {}
		return retorno;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static Date truncMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return truncDate(cal.getTime());
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static Date maxTimeOfDay(Date date) {
		Date retorno = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(STRING_ddMMyyyy);
			retorno = sdf.parse(sdf.format(date));

			Calendar c = Calendar.getInstance();
			c.setTime(retorno);

			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);

			retorno = c.getTime();

		} catch (Exception e) {}
		return retorno;
	}

	/**
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	@RemapTest(name="dateToORASQLStringFormat")
	public static String dateToORASQLString(Date date, String format) {
		String returnedValue;
		if (format != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			returnedValue = "TO_DATE('" + sdf.format(date) + "','" + format.replaceFirst("mm", "mi").replaceFirst("HH", "HH24") + "')";
		}
		else {
			returnedValue = dateToORASQLString(date);
		}
		return returnedValue;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String dateToORASQLString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(STRING_ddMMyyyy);
		return "TO_DATE('" + sdf.format(date) + "','dd/MM/yyyy')";
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String dateToORASQLStringFull(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return "TO_DATE('" + sdf.format(date) + "','dd/MM/yyyy HH24:mi:ss')";
	}

	/**
	 *
	 * @param string
	 * @param begin
	 * @param end
	 * @param format
	 * @return
	 */
	@RemapTest(name="stringToDateBeginEndFormat")
	public static Date stringToDate(String string, int begin, int end, String format) {
		if (string == null || string.trim().equals("") || begin > end || begin < 0) {
			return null;
		}
		Date aux = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			aux = sdf.parse(string.substring(begin, end).trim());
		} catch (Exception e) {
		}
		return aux;
	}

	/**
	 *
	 * @param string
	 * @return
	 */
	public static Date stringToDate(String string) {
		return stringToDate(string, 0, string.length(), STRING_ddMMyyyy);
	}

	/**
	 *
	 * @param string
	 * @param format
	 * @return
	 */
	@RemapTest(name="stringToDateFormat")
	public static Date stringToDate(String string, String format) {
		return stringToDate(string, 0, string.length(), format);
	}

	/**
	 *
	 * @param fechaActual
	 * @param fechaNacimiento
	 * @return
	 */
	public static long calcularEdad(Date fechaActual, Date fechaNacimiento) {
		int edad = 0;
		try {
			edad = (int) Math.floor((fechaActual.getTime() - fechaNacimiento.getTime()) / (1000 * 60 * 60 * 24 * 365.25));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return edad;
	}

	/**
	 *
	 * @param session
	 * @param fechaNacimiento
	 * @return
	 */
	public static String calcularEdadEnMeses(Date fechaActual, Date fechaNacimiento){
		int meses = 0;
		int dias = 0;
		try {
			meses = (int) Math.floor((fechaActual.getTime() - fechaNacimiento.getTime()) / (1000 * 60 * 60 * 24 * 30.50));
			if(meses < 2) {
				dias = (int) Math.floor((fechaActual.getTime() - fechaNacimiento.getTime()) / (1000 * 60 * 60 * 24));
				return dias == 1 ? dias + " " + Resources.getValue("dia") : dias + " " + Resources.getValue("dias");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return meses == 1 ? meses + " mes" : meses + " meses";
	}

	/**
	 *
	 * @param fechaIni
	 * @param fechaFin
	 * @return
	 */
	public static long ctdSemanasEntreFechas(Date fechaIni, Date fechaFin) {
		long fechaInicialMs = fechaIni.getTime();
		long fechaFinalMs = fechaFin.getTime();
		double diferencia = fechaFinalMs - fechaInicialMs;
		long dias = new Double(Math.floor(diferencia / (1000 * 60 * 60 * 24) / 7)).longValue();
		return dias;
	}

	/**
	 *
	 * @param fechaIni
	 * @param fechaFin
	 * @return
	 */
	public static double ctdSemanasEntreFechasDouble(Date fechaIni, Date fechaFin) {
		long fechaInicialMs = fechaIni.getTime();
		long fechaFinalMs = fechaFin.getTime();
		double diferencia = fechaFinalMs - fechaInicialMs;
		double dias = new Double(diferencia / (1000 * 60 * 60 * 24) / 7);
		return dias;
	}

	/**
	 *
	 * @param fechaIni
	 * @param fechaFin
	 * @return
	 */
	public static long ctdDiasEntreFechas(Date fechaIni, Date fechaFin) {
		long fechaInicialMs = fechaIni.getTime();
		long fechaFinalMs = fechaFin.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		long dias = diferencia / (1000 * 60 * 60 * 24);
		return dias;
	}

	/**
	 * @param fechaIni
	 * @param fechaFin
	 * @return
	 */
	public static long ctdDiasEntreFechasRoundUp(Date fechaIni, Date fechaFin) {
		long dias = (long) Math.ceil(ctdDiasEntreFechas(fechaIni, fechaFin));
		return dias;
	}

	/**
	 *
	 * @param fechaIni
	 * @param fechaFin
	 * @return
	 */
	public static double ctdDiasEntreFechasDouble(Date fechaIni, Date fechaFin) {
		long fechaInicialMs = fechaIni.getTime();
		long fechaFinalMs = fechaFin.getTime();
		double diferencia = fechaFinalMs - fechaInicialMs;
		double dias = diferencia / (1000 * 60 * 60 * 24);
		return dias;
	}

	/**
	 *
	 * @param fechaIni
	 * @param fechaFin
	 * @return
	 */
	public static long ctdHorasEntreFechas(Date fechaIni, Date fechaFin) {
		long fechaInicialMs = fechaIni.getTime();
		long fechaFinalMs = fechaFin.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		long horas = diferencia / (1000 * 60 * 60);
		return horas;
	}

	/**
	 *
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static long ctdMinutosEntreFechas(Date fechaInicial, Date fechaFinal) {
		if(fechaInicial == null || fechaFinal == null) {
			return 0l;
		}
		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		long horas = diferencia / (1000 * 60 );
		return horas;
	}

	/**
	 *
	 * @param date
	 * @param meses
	 * @return
	 */
	public static Date sumarMesAFecha(Date date, int meses) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.MONTH, meses);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 *
	 * @param date
	 * @param dias
	 * @return
	 */
	public static Date sumarDiasAFecha(Date date, int dias) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.DATE, dias);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 *
	 * @param date
	 * @param horas
	 * @return
	 */
	public static Date sumarHorasAFecha(Date date, int horas) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.HOUR, horas);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 *
	 * @param date
	 * @param minutos
	 * @return
	 */
	public static Date sumarMinutosAFecha(Date date, int minutos) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.add(Calendar.MINUTE, minutos);
		return new Date(cal.getTimeInMillis());
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String dateToStringddMMyyyy(Date date) {
		return dateToString(date, STRING_ddMMyyyy);
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String dateToStringddMMyyyyHHmm(Date date) {
		return dateToString(date, STRING_ddMMyyHHmm);
	}

	/**
	 *
	 * @param fechaActual
	 * @param fechaNacimiento
	 * @return
	 */
	public static String calcularEdadMesesString(Date fechaActual, Date fechaNacimiento) {
		try {
			long edad = calcularEdad(fechaActual, fechaNacimiento);
			if (edad > 1) {
				return edad + " " + Resources.getValue("anios");
			}
			else if (edad == 1) {
				return edad + " " + Resources.getValue("anio");
			}
			else {
				return calcularEdadEnMeses(fechaActual, fechaNacimiento);
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Retorna la edad para la fecha actual concatenando al final con anios, meses o dias segun coresponda.
	 * @param fechaActual
	 * @return
	 */
	@IgnoreTest
	public static String getEdadLabel(Date fechaActual, Date fechaNacimiento) {
		String edad="";
		if(fechaActual == null || fechaNacimiento == null) {
			return null;
		}
		Calendar calendarFechaNacimiento = Calendar.getInstance();
		calendarFechaNacimiento.setTime(fechaNacimiento);

		Calendar calendarFechaActual = Calendar.getInstance();
		calendarFechaActual.setTime(fechaActual);

		// Se restan la fecha actual y la fecha de nacimiento
		int anio = calendarFechaActual.get(Calendar.YEAR) - calendarFechaNacimiento.get(Calendar.YEAR);
		int mes = calendarFechaActual.get(Calendar.MONTH) - calendarFechaNacimiento.get(Calendar.MONTH);
		int dia = calendarFechaActual.get(Calendar.DATE) - calendarFechaNacimiento.get(Calendar.DATE);
		// Se ajusta el anio dependiendo el mes y el día
		anio = (mes < 0 || (mes == 0 && dia < 0)) ? anio-1 : anio;

		if(anio==0 && mes<0){
			mes = 12 + mes;
		}

		if(anio==0 && mes==0 && dia <0){
			mes = 11;
		}

		if(anio == 0 && mes == 0) {
			edad= Integer.toString(dia).concat(" ").concat(dia == 1? Resources.getValue("dia"): Resources.getValue("dias"));
		}else if(anio == 0) {
			edad= Integer.toString(mes).concat(" ").concat(mes == 1? Resources.getValue("mes"): Resources.getValue("meses"));
		}else {
			edad= Integer.toString(anio).concat(" ").concat(anio == 1? Resources.getValue("anio"): Resources.getValue("anios"));
		}
		return edad;
	}

	public static String minutosAHoraMinutos(long minutos) {
		NumberFormat nf = new DecimalFormat("#00");
		long horas = (long) Math.floor(minutos / 60);
		minutos = minutos % 60;
		return nf.format(horas) + ":" + nf.format(minutos);
	}

	public static Date getDate2359(Date fechaDesde) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaDesde);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);

		Date ret = cal.getTime();

		return ret;
	}

}
