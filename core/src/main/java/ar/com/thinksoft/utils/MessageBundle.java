package ar.com.thinksoft.utils;


/* ****************************  R E F E R E N C I A *************************** *
 * Nota: los caracteres unicode comienzan con '\' + 'u'
 * a:\u00E1     e:\u00E9     i:\u00ED     o:\u00F3     u:\u00FA     n:\u00F1
 * A:\u00C1     E:\u00C9     I:\u00CD     O:\u00D3     U:\u00DA     N:\u00D1
 * grado:\u00B0     pregunta:\u00BF admiracion:\u00A1
 * ***************************************************************************** */
public class MessageBundle {

	public static final String ACCOUNT_LOCKED_ERROR = new String("La cuenta a la que desea ingresar ha sido bloqueada.");
	public static final String ATENCION = new String("Atenci\u00f3n");
	public static final String CHAR_REQUIRED_INFO = new String("Debe ingresar al menos un caracter como valor de b\u00FAsqueda.");
	public static final String CHAR3_REQUIRED_INFO = new String("Debe ingresar al menos tres caracteres como valor de b\u00FAsqueda.");
	public static final String CHILD_FOUND_ERROR = new String("El registro que desea eliminar posee otros registros asociados.");
	public static final String CHILD_FOUND_UPDATE_ERROR = new String("El registro que desea actualizar posee otros registros asociados.");
	public static final String CONNECTION_CLOSED_FATAL = new String("No se pudo reestablecer la conexi\u00F3n con la Base de Datos.");
	public static final String DATE_AFTER_TODAY_ERROR = new String("La fecha de nacimiento no puede ser mayor a la fecha actual.");
	public static final String DATE_BEFORE_TODAY_ERROR = new String("La fecha de vigencia no puede ser menor a la fecha actual.");
	public static final String DATE_END_BEFORE_DATE_BEGIN = new String("La fecha de fin no puede ser menor a la fecha de inicio.");
	public static final String DATE_FORMAT_EXCEPTION = new String("Error en el formato de la fecha. Formato v\u00E1lido: DD/MM/AAAA");
	public static final String DATE_FORMAT_NOT_SETTED = new String("Debe definir el formato de la fecha.");
	public static final String DB_CONNECTION_FATAL = new String("No se pudo establecer la conexi\u00f3n con la Base de Datos.");
	public static final String DEBE_COMPLETAR_ALGUN_CAMPO = new String("Debe completar alg\u00FAn campo.");
	public static final String DOUBLE_FORMAT_EXCEPTION = new String("Error en el formato num\u00e9rico. Formato v\u00E1lido: 1234567890.1234");
	public static final String DROP_USER_ERROR = new String("El usuario que intenta eliminar se encuentra conectado. \nCierre la sesi\u00f3n con el usuario actual e ingrese con otro usuario.");
	public static final String ERROR = new String("Error");
	public static final String EXCEPTION_ERROR = new String("Se ha producido un error inesperado.");
	public static final String FALTA_SELECCIONAR_REDONDEO = new String("Falta Seleccionar el Tipo de Redondeo");
	public static final String FATAL = new String("Error Grave");
	public static final String HIBERNATE_EXCEPTION_ERROR = new String("Error al construir la sesi\u00F3n.");
	public static final String INFO = new String("Informaci\u00f3n");
	public static final String INTENTO_SUPERADOS = new String("El n\u00FAmero de intentos ha sido superado.");
	public static final String INVALID_DATA_ERROR = new String("Los datos ingresados son incorrectos.");
	public static final String INVALID_EMAIL_ERROR = new String("Debe ingresar un email v\u00E1lido");
	public static final String INVALID_FECHA_ERROR = new String("No se reconoci\u00f3 el formato de la fecha. Ejemplo: ");
	public static final String INVALID_MAIL_SERVER_ERROR = new String("No se ha podido conectar al servidor de correo para realizar el env\u00EDo");
	public static final String INVALID_PASSWORD_ERROR = new String("La contrase\u00F1a ingresada no es v\u00E1lida.");
	public static final String INVALID_USER_NAME_ERROR = new String("El usuario ingresado no es v\u00E1lido.");
	public static final String INVALID_USER_PASSWORD_ERROR = new String("Usuario y/o contrase\u00F1a incorrectos.");
	public static final String IO_EXCEPTION_ERROR = new String("Error de lectura / escritura.");
	public static final String LONG_FORMAT_EXCEPTION = new String("Error en el formato num\u00e9rico. Formato v\u00E1lido: 1234567890");
	public static final String MAXIMUM_LENGTH_EXCEEDED_ERROR = new String("Longitud m\u00E1xima del campo excedida. Long. m\u00E1x.: ");
	public static final String NO_HAY_REGISTROS_CARGADOS = new String("No hay registros cargados");
	public static final String NO_RECORDS_FOUND_WARN = new String("No se encontraron registros.");
	public static final String OUT_OF_RANGE_ERROR = new String("Alg\u00FAn valor se encuentra fuera de los valores permitidos.");
	public static final String PARAMETROS_REQUERIDOS = new String("Parametros Requeridos.");
	public static final String PASSWORD_CANT_BE_REUSED_WARN = new String("La contrase\u00f1a no se puede reutilizar.");
	public static final String PASSWORD_EXPIRED_WARN = new String("La contrase\u00f1a ha expirado.");
	public static final String PASSWORD_WILL_EXPIRE_SOON_WARN = new String("La contrase\u00f1a esta por vencer, por favor cambiela.");
	public static final String PASSWORD_SUCCESFULLY_UPDATED = new String("La contrase\u00F1a se modific\u00F3 exitosamente.");
	public static final String PRECISION_FIELD_ERROR = new String("Debe ingresar un valor de menor precisi\u00F3n.");
	public static final String PROPERTY_VALUE_FATAL = new String("Usuario no logueado.");
	public static final String REQUIRED_FIELD_ERROR = new String("Debe completar todos los campos requeridos.");
	public static final String ROW_ALREADY_EXIST_ERROR = new String("El registro ya existe.");
	public static final String ROW_DELETE_ERROR = new String("El registro no pudo ser eliminado.");
	public static final String ROW_DELETE_INFO = new String("Registro eliminado con \u00E9xito.");
	public static final String ROW_DISABLED_INFO = new String("Registro desactivado con \u00E9xito.");
	public static final String ROW_INSERT_ERROR = new String("El registro no pudo ser insertado.");
	public static final String ROW_INSERT_INFO = new String("Registro insertado con \u00E9xito.");
	public static final String ROW_UPDATE_ERROR = new String("El registro no pudo ser actualizado.");
	public static final String ROW_UPDATE_INFO = new String("Registro actualizado con \u00E9xito.");
	public static final String TABLE_DOES_NOT_EXIST_FATAL = new String("La tabla o vista no existe.");
	public static final String TOO_MANY_RECORDS_FOUND_WARN = new String("Se ha encontrado mas de un registro.");
	public static final String TRANSACTION_ERROR = new String("Se ha producido un error al completar la transacci\u00F3n. Contacte al administrador.");
	public static final String UNKNOWN_ERROR_FATAL = new String("Se ha producido un error desconocido.");
	public static final String USER_ALREADY_EXIST_ERROR = new String("El nombre de usuario solicitado ya existe.");
	public static final String USER_NOT_EXIST_WARN = new String("El usuario que desea eliminar no existe");
	public static final String USUARIO_NO_ACCESS_ERROR = new String("Ud. no tiene permisos para ingresar a la aplicaci\u00F3n. Contacte al administrador.");
	public static final String USUARIO_NOT_FOUND_ERROR = new String("No se pudo recuperar los datos del Usuario.");
	public static final String VALUE_NEGATIVE_ERROR = new String("No se pueden ingresar valores negativos.");
	public static final String VALOR_NO_ESPERADO = new String("Valor no esperado: ");
	public static final String VALOR_NO_VALIDO_VALORES_POSIBLES = new String("Valor invalido. Valores posibles: ");
	public static final String VALUE_NOT_ENTRY = new String("Debe ingresar un valor");
	public static final String VALUE_TOO_LARGE_ERROR = new String("Debe ingresar un valor de menos d\u00EDgitos.");
	public static final String WRONG_INTERVAL_DATE = new String("La fecha de cierre debe ser posterior a la fecha de inicio");
	public static final String WRONG_INTERVAL_DATE_1 = new String("La fecha debe ser anterior a la fecha de hoy");
	public static final String WRONG_INTERVAL_DATE_2 = new String("La fecha de fin debe ser posterior a la fecha de inicio");
	public static final String WRONG_INTERVAL_DATE_3 = new String("La fecha hasta debe ser posterior a la fecha desde");
	public static final String WRONG_INTERVAL_DATE_4 = new String("La fecha hasta debe ser posterior a la fecha desde");
	public static final String WRONG_INTERVAL_DATE_5 = new String("La fecha debe ser posterior a la fecha de la asignacion anterior");
	public static final String WRONG_INTERVAL_DATE_6 = new String("La fecha debe ser posterior a la fecha de internacion");
	public static final String WRONG_INTERVAL_DATE_7 = new String("La fecha no puede ser igual a la fecha de la asignacion anterior");
	public static final String WRONG_INTERVAL_DATE_8 = new String("La cama seleccionada estuvo ocupada en la fecha y hora seleccionada");
	public static final String WRONG_INTERVAL_DATE_9 = new String("La fecha seleccionada no puede ser anterior a la actual.");
	public static final String WRONG_INTERVAL_DATE_10 = new String("La fecha de vigencia debe ser posterior a la fecha de vigencia del grupo");
	public static final String WRONG_INTERVAL_DATE_11 = new String("La fecha de fin de vigencia no debe ser posterior a la fecha de fin de vigencia del grupo");
	public static final String WRONG_INTERVAL_DATE_12 = new String("La fecha de vigencia no debe ser posterior a la fecha de fin de vigencia del grupo");
	public static final String WRONG_INTERVAL_DATE_13 = new String("La nueva fecha de vigencia debe ser posterior a la fecha de vigencia actual");
	public static final String WRONG_INTERVAL_DATE_14 = new String("La fecha de egreso debe ser posterior a la fecha de ingreso");
	public static final String WRONG_INTERVAL_DATE_15 = new String("La fecha desde y la fecha hasta no deben ser menores a la fecha de hoy.");
	public static final String WRONG_INTERVAL_DATE_16 = new String("La fecha fin debe ser posterior a la fecha inicio.");
	public static final String WRONG_INTERVAL_DATE_17 = new String("La consulta no puede ser mayor a 60 d\u00EDas");
	public static final String WRONG_INTERVAL_DATE_HASTA = new String("La fecha Desde no puede ser igual a una fecha hasta de otro precio");
	public static final String WRONG_INTERVAL_DATE_HOUR_1 = new String("La fecha y hora debe ser anterior a la fecha de hoy");
	public static final String WRONG_INTERVAL_EDAD = new String("La edad m\u00EDnima no puede ser mayor a la edad m\u00E1xima.");
	public static final String WRONG_INTERVAL_HOUR = new String("El horario hasta debe ser posterior al horario desde");
	public static final String WRONG_INTERVAL_HORARIO_NOCTURNO = new String("La hora hasta del inicio del d\u00EDa debe ser menor a la hora desde del fin del d\u00EDa.");
	public static final String WRONG_INTERVAL_NUMBER = new String("El n\u00FAmero hasta debe ser mayor o igual al n\u00FAmero desde.");
	public static final String WRONG_INTERVAL_NUMBER_ORDEN = new String("El n\u00FAmero de orden hasta debe ser mayor o igual al n\u00FAmero de orden desde.");
	public static final String WRONG_INTERVAL_VIGENCIA = new String("La fecha de fin de vigencia debe ser posterior a la fecha de vigencia");
	public static final String WRONG_PASSWORD_ERROR = new String("La contrase\u00F1a no puede comenzar con un n\u00FAmero.");

}
