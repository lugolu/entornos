package ar.com.thinksoft.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLRecoverableException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.type.StandardBasicTypes;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.persistance.storedprocedures.HibernateResult;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.LoadConfig;
import ar.com.thinksoft.utils.MessageBundle;
import ar.com.thinksoft.utils.SeverityBundle;

public class HibernateSessionFactory implements SessionFactory {

	private static HibernateSessionFactory instance = null;

	// atributos de la instancia
	private final String CONFIG_FILE_LOCATION = "/hibernate.cfg.xml";
	private final String USER_NAME = "hibernate";
	private final String INTERFACE_NAME = "scheduler";
	private final String PASSWORD = "H183R_n4%32865$sw";
	private final String TS_PASSWORD = "TS#3739423415";
	private String USER_PASS;

	private ServiceRegistry serviceRegistry;
	private org.hibernate.SessionFactory sessionFactory;
	private Conexion conexion;
	private Conexion conexionAnunciador;
	private Conexion conexionEnfermera;
	private Conexion conexionImagenes;
	private Conexion conexionPaciente;
	private Conexion conexionTriage;
	//
	private String DRIVER_NAME;
	private String CONNECTION_URL;
	private String SECURITY_CONNECTION_URL;
	private String SHOW_SQL;

	private HibernateSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure(CONFIG_FILE_LOCATION);
			ServiceRegistryBuilder builder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
			//
			Map<String, String> settings = LoadConfig.getInstance().getSettings();
			LoadConfig.getInstance().genLogger();

			CONNECTION_URL = LoadConfig.getInstance().getCONNECTION_URL();
			SECURITY_CONNECTION_URL = LoadConfig.getInstance().getSECURITY_CONNECTION_URL();
			SHOW_SQL = LoadConfig.getInstance().getSHOW_SQL();

			if(!settings.isEmpty()) {
				System.out.println("Usando 'conexiones.xml'");
				builder.applySettings(settings);
				serviceRegistry = builder.buildServiceRegistry();
			} else {
				System.out.println("Usando 'hibernate.cfg.xml'");
				serviceRegistry = builder.buildServiceRegistry();
				//
				CONNECTION_URL = configuration.getProperty(AvailableSettings.URL);
				SECURITY_CONNECTION_URL = configuration.getProperty("hibernate.connection.security_url");
				SHOW_SQL = configuration.getProperty(AvailableSettings.SHOW_SQL);
			}
			DRIVER_NAME = configuration.getProperty(AvailableSettings.DRIVER);
			System.out.println("*****************************************************************************");
			System.out.println(LoadConfig.getInstance().getPROJECT_NAME());
			System.out.println("*****************************************************************************");
			System.out.println("CLIENT: " + LoadConfig.getInstance().getCLIENT());
			System.out.println("CONNECTION_URL: " + CONNECTION_URL);
			System.out.println("SECURITY_CONNECTION_URL: " + SECURITY_CONNECTION_URL);
			System.out.println("SHOW_SQL: " + SHOW_SQL);
			System.out.println("AGE_URL: " + LoadConfig.getInstance().getAGE_URL());
			System.out.println("AGI_URL: " + LoadConfig.getInstance().getAGI_URL());
			System.out.println("AGP_URL: " + LoadConfig.getInstance().getAGP_URL());
			System.out.println("ANMAT_URL: " + LoadConfig.getInstance().getANMAT_URL());
			System.out.println("HOS_APP: " + LoadConfig.getInstance().getHOS_APP());
			System.out.println("HOS_APP_URL: " + LoadConfig.getInstance().getHOS_APP_URL());
			System.out.println("EQUIPO_TAREAS_PROGRAMADAS: " + LoadConfig.getInstance().getEQUIPO_TAREAS_PROGRAMADAS());
			System.out.println("SEGURIDAD_URL: " + LoadConfig.getInstance().getSEGURIDAD_URL());
			System.out.println("VISUALIZADOR: " + LoadConfig.getInstance().getVISUALIZADOR());
			System.out.println("*****************************************************************************");
			System.out.println("*****************************************************************************");
			System.out.println("*****************************************************************************");
			//
			Class driverClass = null;
			try {
				driverClass = Class.forName(DRIVER_NAME);
			} catch (ClassNotFoundException cnfe) {
				try {
					driverClass = ReflectHelper.classForName(DRIVER_NAME);
				} catch (ClassNotFoundException e) {
					String msg = "No se encontro la clase del manejador de JDBC: " + DRIVER_NAME;
					Logger.getLogger(getClass()).fatal(msg, e);
					throw new HibernateException(msg, e);
				}
			}
			DriverManager.registerDriver((Driver)driverClass.newInstance());
			//
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Exception e) {
			System.err.println("%%%% Error Creating SessionFactory %%%%");
			e.printStackTrace();
			try {
				HandlerException.getInstancia().treateException(e, getClass());
			} catch (Exception e1) {}
			if (serviceRegistry != null) {
				ServiceRegistryBuilder.destroy(serviceRegistry);
			}
		}
	}

	public static HibernateSessionFactory getInstance() {
		return instance == null ? instance = new HibernateSessionFactory() : instance;
	}

	public void destroyServiceRegistry() {
		try {
			if(conexion != null) {
				conexion.close();
			}
		} catch(Exception ex) { ex.printStackTrace(); }
		try {
			if(conexionAnunciador != null) {
				conexionAnunciador.close();
			}
		} catch(Exception ex) { ex.printStackTrace(); }
		try {
			if(conexionEnfermera != null) {
				conexionEnfermera.close();
			}
		} catch(Exception ex) { ex.printStackTrace(); }
		try {
			if(conexionImagenes != null) {
				conexionImagenes.close();
			}
		} catch(Exception ex) { ex.printStackTrace(); }
		try {
			if(conexionPaciente != null) {
				conexionPaciente.close();
			}
		} catch(Exception ex) { ex.printStackTrace(); }
		try {
			if(conexionTriage != null) {
				conexionTriage.close();
			}
		} catch(Exception ex) { ex.printStackTrace(); }
		try {
			ServiceRegistryBuilder.destroy(serviceRegistry);
		} catch(Exception ex) { ex.printStackTrace(); }
	}

	@Override
	public StatelessSession getSession(Connection connection) throws BusinessException {
		return sessionFactory.openStatelessSession(connection);
	}

	public StatelessSession openPrivateSession() throws BusinessException {
		StatelessSession s = null;
		try {
			if (conexion == null || conexion.isClosed()) {
				conexion = openPrivateConexion();
			}
			return conexion.getSession();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return s;
	}

	public StatelessSession openAnunciadorSession() throws BusinessException {
		StatelessSession s = null;
		try {
			if (conexionAnunciador == null || conexionAnunciador.isClosed()) {
				conexionAnunciador = getConexionAnunciador();
			}
			return conexionAnunciador.getSession();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return s;
	}

	public StatelessSession openTriageSession() throws BusinessException {
		StatelessSession s = null;
		try {
			if (conexionTriage == null || conexionTriage.isClosed()) {
				conexionTriage = getConexionTriage();
			}
			return conexionTriage.getSession();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return s;
	}

	public Conexion openApiConnection(String user) throws BusinessException{
		Conexion retorno = null;
		try {
			retorno = new Conexion(this, user, TS_PASSWORD);
			Statement s = retorno.createStatement();
			s.execute("select * from dual");
			s.getResultSet();
			retorno.commit();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return retorno;
	}

	public Conexion getConexion(String user, String pass) throws BusinessException{
		Conexion retorno = null;
		try {
			retorno = new Conexion(this, user, pass.toLowerCase());
			Statement s = retorno.createStatement();
			s.execute("select * from dual");
			s.getResultSet();
			retorno.commit();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return retorno;
	}

	public Conexion getConexionNoSeed(String user, String pass) throws BusinessException{
		Conexion retorno = null;
		try {
			retorno = new Conexion(this, user, pass);
			Statement s = retorno.createStatement();
			s.execute("select * from dual");
			s.getResultSet();
			retorno.commit();
		}
		catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return retorno;
	}

	public Conexion openPrivateConexion() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, USER_NAME, USER_PASS);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion openInterfaceConexion(Long number) throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, INTERFACE_NAME + (number != null ? number.toString(): ""), USER_PASS);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion openInterfaceConexionTrazabilidad() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "TRAZABILIDAD_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public String getJndiName() {
		return null; // configuration.getProperty("hibernate.jndi");
	}

	public void clearTemporaryTables(StatelessSession s) throws BusinessException {
		try {
			s.beginTransaction().rollback();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e1) {}
		}
	}

	public String selectServiceName() throws BusinessException {
		String retorno = null;
		StatelessSession s = null;
		try {
			s = openPrivateSession();
			String sql=	"select sys_context('USERENV','DB_NAME') as resultado from dual";
			HibernateResult l = (HibernateResult)s.createSQLQuery(sql)
					.addEntity(HibernateResult.class)
					.uniqueResult();
			if (l != null) {
				retorno = l.getResultado();
			}
		} catch (Exception e) {
			if (e instanceof SQLRecoverableException) {
				try {conexion.close();} catch (Exception e1) {}
			}
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return retorno;
	}

	/**
	 * Este metodo solo debe usarse para obtener una conexion como paciente. NO valida si el paciente esta registrado.
	 * @return
	 * @throws BusinessException
	 */
	public Conexion openPacienteConexion() throws BusinessException {
		try {
			return new Conexion(this, "PACIENTE_TS", TS_PASSWORD);
		} catch (Exception e) {
			if (e instanceof SQLRecoverableException) {
				try {conexion.close();} catch (Exception e1) {}
			}
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return null;
	}

	/**
	 * Usar solo para consulta
	 * @return
	 * @throws BusinessException
	 */
	public StatelessSession openPacienteSession() throws BusinessException {
		StatelessSession s = null;
		try {
			if (conexionPaciente == null || conexionPaciente.isClosed()) {
				conexionPaciente = openPacienteConexion();
			}
			return conexionPaciente.getSession();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return s;
	}

	public Conexion openLaboratorioConexion() throws BusinessException {
		try {
			return new Conexion(this, "LABORATORIO_TS", TS_PASSWORD);
		} catch (Exception e) {
			if (e instanceof SQLRecoverableException) {
				try {conexion.close();} catch (Exception e1) {}
			}
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return null;
	}

	/**
	 * Este metodo solo debe usarse para obtener una conexion como prescriptor. NO valida si el prescriptor esta registrado.
	 * @return
	 * @throws BusinessException
	 */
	public Conexion openPrescriptorConexion() throws BusinessException {
		try {
			return new Conexion(this, "PRESCRIPTOR_TS", TS_PASSWORD);
		}
		catch (Exception e) {
			if (e instanceof SQLRecoverableException) {
				try {conexion.close();}
				catch (Exception e1) {}
			}
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return null;
	}

	public Conexion getConexionAnunciador() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "ANUNCIADOR_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion getConexionAutorecepcion() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "AUTORECEPCION_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion getConexionTriage() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "TRIAGE_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion getConexionImagenes() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "IMAGENES_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	/**
	 * Solo usar este metodo para consultas.
	 * @return
	 * @throws BusinessException
	 */
	public StatelessSession openImagenesSession() throws BusinessException {
		StatelessSession s = null;
		try {
			if (conexionImagenes == null || conexionImagenes.isClosed()) {
				conexionImagenes = getConexionImagenes();
			}
			return conexionImagenes.getSession();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return s;
	}

	public Conexion getConexionOtrosEstudios() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "OTROS_ESTUDIOS_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion openDosysConexion() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "DOSYS_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion openEnfermeraConexion() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "ENFERMERA_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	/**
	 * Solo usar este metodo para consultas.
	 * @return
	 * @throws BusinessException
	 */
	public StatelessSession openEnfermeraSession() throws BusinessException {
		StatelessSession s = null;
		try {
			if (conexionEnfermera == null || conexionEnfermera.isClosed()) {
				conexionEnfermera = openEnfermeraConexion();
			}
			return conexionEnfermera.getSession();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return s;
	}

	public Conexion openGuardiaConexion() throws BusinessException {
		Conexion ret = null;
		try {
			ret = new Conexion(this, "GUARDIA_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Conexion openAnunciadorEsperaServConexion() throws BusinessException{
		Conexion ret = null;
		try {
			ret = new Conexion(this, "ANUN_ESP_SERV_TS", TS_PASSWORD);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		}
		return ret;
	}

	public Long selectGenerarIdPk(Object serializable, StatelessSession s) throws BusinessException {
		Long nextId = null;
		CallableStatement callableStmt = null;
		String clase = null;
		try {
			clase = serializable.getClass().toString();
			clase = clase.substring(clase.lastIndexOf(".") + 1);
			clase = getTableNameForClass(clase);
			Connection connection = s.connection();
			callableStmt = connection.prepareCall("{? = call ts.general.f_next_id_tabla (?)}");
			callableStmt.registerOutParameter(1, java.sql.Types.BIGINT);
			callableStmt.setString(2, clase);
			callableStmt.executeQuery();
			// get result from out parameter #1
			nextId = callableStmt.getLong(1);
		} catch (Exception e) {
			System.out.println("clase: " + clase);
			System.out.println("tabla: " + clase);
			HandlerException.getInstancia().treateException(e, getClass());
		}
		finally{
			try{ callableStmt.close(); }
			catch(Exception ex){}
		}
		return nextId;
	}

	public Long selectGenerarIdPk(String tableName, StatelessSession s) throws BusinessException {
		Long nextId = null;
		CallableStatement callableStmt = null;
		try {
			Connection connection = s.connection();
			callableStmt = connection.prepareCall("{? = call ts.general.f_next_id_tabla (?)}");
			callableStmt.registerOutParameter(1, java.sql.Types.BIGINT);
			callableStmt.setString(2, tableName);
			callableStmt.executeQuery();
			// get result from out parameter #1
			nextId = callableStmt.getLong(1);
		} catch (Exception e) {
			System.out.println("clase: " + tableName);
			System.out.println("tabla: " + tableName);
			HandlerException.getInstancia().treateException(e, getClass());
		}
		finally{
			try{ callableStmt.close(); }
			catch(Exception ex){}
		}
		return nextId;
	}

	public Long selectGenerarIdCompuestaPk(Object serializable, String columnName1, Long columnValue1, String columnName2, StatelessSession s) throws BusinessException {
		Long nextId = null;
		CallableStatement callableStmt = null;
		String clase = null;
		try {
			if(serializable == null || columnName1 == null || columnValue1 ==null || columnName2 ==null){
				throw new BusinessException(MessageBundle.PARAMETROS_REQUERIDOS, SeverityBundle.FATAL);
			}

			clase = serializable.getClass().toString();
			clase = clase.substring(clase.lastIndexOf(".") + 1);
			clase = getTableNameForClass(clase);
			Connection connection = s.connection();
			callableStmt = connection.prepareCall("{? = call ts.general.f_next_id_compuesta_tabla (?, ?, ?, ?)}");
			callableStmt.registerOutParameter(1, java.sql.Types.BIGINT);
			callableStmt.setString(2, clase);
			callableStmt.setString(3, columnName1);
			callableStmt.setLong(4, columnValue1);
			callableStmt.setString(5, columnName2);
			callableStmt.executeQuery();
			// get result from out parameter #1
			nextId = callableStmt.getLong(1);
		} catch (Exception e) {
			System.out.println("clase: " + clase);
			System.out.println("tabla: " + clase);
			System.out.println("columna1: " + columnName1);
			System.out.println("columna2: " + columnName2);
			HandlerException.getInstancia().treateException(e, getClass());
		}
		finally{
			try{ callableStmt.close(); }
			catch(Exception ex){}
		}
		return nextId;
	}

	public Long selectGenerarIdCompuestaPk(Object serializable, String columnaPadre, String valorPadre, String columnaHijo, StatelessSession s) throws BusinessException {
		Long nextId = null;
		CallableStatement callableStmt = null;
		String clase = null;
		try {
			if(valorPadre == null){
				throw new BusinessException(MessageBundle.REQUIRED_FIELD_ERROR, SeverityBundle.WARN);
			}
			if(serializable == null || columnaPadre == null || valorPadre ==null || columnaHijo ==null){
				throw new BusinessException(MessageBundle.PARAMETROS_REQUERIDOS, SeverityBundle.FATAL);
			}

			clase = serializable.getClass().toString();
			clase = clase.substring(clase.lastIndexOf(".") + 1);
			clase = getTableNameForClass(clase);
			Connection connection = s.connection();
			callableStmt = connection.prepareCall("{? = call ts.general.f_next_id_compuesta_tabla_str (?, ?, ?, ?)}");
			callableStmt.registerOutParameter(1, java.sql.Types.BIGINT);
			callableStmt.setString(2, clase);
			callableStmt.setString(3, columnaPadre);
			callableStmt.setString(4, valorPadre);
			callableStmt.setString(5, columnaHijo);
			callableStmt.executeQuery();
			// get result from out parameter #1
			nextId = callableStmt.getLong(1);
		} catch (Exception e) {
			System.out.println("clase: " + clase);
			System.out.println("tabla: " + clase);
			System.out.println("columna1: " + columnaPadre);
			System.out.println("columna2: " + columnaHijo);
			HandlerException.getInstancia().treateException(e, getClass());
		}
		finally{
			try{ callableStmt.close(); }
			catch(Exception ex){}
		}
		return nextId;
	}

	private String getTableNameForClass (String clase) {
		StringBuffer table = null;

		char[] chars = clase.toCharArray();

		for (int i=0; i<chars.length; i++) {
			if (table == null) {
				table = new StringBuffer("");
			}
			else {
				if (chars[i] >= 65 && chars[i] <= 90) {
					table.append("_");
				}
			}
			table.append(chars[i]);
		}

		return table.toString().toUpperCase();
	}

	public Date selectSysdateNoSessionClose(StatelessSession s) throws BusinessException {
		try {
			String sql=	"select sysdate as FECHA from dual";
			Timestamp time = (Timestamp) s.createSQLQuery(sql)
					.addScalar("FECHA", StandardBasicTypes.TIMESTAMP)
					.uniqueResult();
			if (time != null) {
				return new Date(time.getTime());
			}
		}
		catch (Exception e) {
			HandlerException.getInstancia().treateException(e, HibernateSessionFactory.class);
		}
		return null;
	}

	/**
	 * Fecha actual en la base de dato CERRANDO la sesion al salir.
	 * @param s
	 * @return
	 * @throws BusinessException
	 */
	public Date selectSysdate(StatelessSession s) throws BusinessException {
		try {
			String sql=	"select sysdate as FECHA from dual";
			Timestamp time = (Timestamp) s.createSQLQuery(sql)
					.addScalar("FECHA", StandardBasicTypes.TIMESTAMP)
					.uniqueResult();
			if (time != null) {
				return new Date(time.getTime());
			}
		} catch (Exception e) {
			if (e instanceof SQLRecoverableException) {
				try {conexion.close();} catch (Exception e1) {}
			}
			HandlerException.getInstancia().treateException(e, HibernateSessionFactory.class);
		} finally {
			try {s.close();} catch (Exception e1) {}
		}
		return null;
	}

	public Date selectTruncSysdate(StatelessSession s) throws BusinessException {
		try {
			String sql=	"select trunc(sysdate) as FECHA from dual";
			Timestamp time = (Timestamp) s.createSQLQuery(sql)
					.addScalar("FECHA", StandardBasicTypes.TIMESTAMP)
					.uniqueResult();
			if (time != null) {
				return new Date(time.getTime());
			}
		} catch (Exception e) {
			if (e instanceof SQLRecoverableException) {
				try {conexion.close();} catch (Exception e1) {}
			}
			HandlerException.getInstancia().treateException(e, HibernateSessionFactory.class);
		} finally {
			try {s.close();} catch (Exception e1) {}
		}
		return null;
	}

	/**
	 * Fecha actual en la base de dato CERRANDO la sesion al salir.
	 * @return
	 * @throws BusinessException
	 */
	public Date selectSysdate() throws BusinessException {
		StatelessSession s = null;
		try {
			s = openPrivateSession();
			String sql=	"select sysdate as FECHA from dual";
			Timestamp time = (Timestamp) s.createSQLQuery(sql)
					.addScalar("FECHA", StandardBasicTypes.TIMESTAMP)
					.uniqueResult();
			if (time != null) {
				return new Date(time.getTime());
			}
		} catch (Exception e) {
			if (e instanceof SQLRecoverableException) {
				try {conexion.close();} catch (Exception e1) {}
			}
			HandlerException.getInstancia().treateException(e, HibernateSessionFactory.class);
		} finally {
			try {s.close();} catch (Exception e1) {}
		}
		return null;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public String getUSER_PASS() {
		return USER_PASS;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	@Override
	public String getConnectionUrl() {
		return CONNECTION_URL;
	}

	public String getSecurityConnectionUrl() {
		return SECURITY_CONNECTION_URL;
	}

	public String getDriverName() {
		return DRIVER_NAME;
	}

	//Propios del proyecto
	/**
	 * @param serializable
	 * @param propiedad
	 * @param session
	 * @return
	 * @throws BusinessException
	 */
	public Long selectNextId(Object serializable, String propiedad, StatelessSession session) throws BusinessException {
		Long result = (Long) session.createCriteria(serializable.getClass())
				.setProjection(Projections.max(propiedad))
				.uniqueResult();

		if (result == null) {
			result = 1L;
		}
		else {
			result += 1;
		}

		return result;
	}

	public String getCLIENT() {
		return LoadConfig.getInstance().getCLIENT();
	}

	public String getAGE_URL() {
		return LoadConfig.getInstance().getAGE_URL();
	}

	public String getAGI_URL() {
		return LoadConfig.getInstance().getAGI_URL();
	}

	public String getAGP_URL() {
		return LoadConfig.getInstance().getAGP_URL();
	}

	public String getANMAT_URL() {
		return LoadConfig.getInstance().getANMAT_URL();
	}

	public String getHOS_APP() {
		return LoadConfig.getInstance().getHOS_APP();
	}

	public String getHOS_APP_URL() {
		return LoadConfig.getInstance().getHOS_APP_URL();
	}

	public String getSEGURIDAD_URL() {
		return LoadConfig.getInstance().getSEGURIDAD_URL();
	}

	public String getEQUIPO_TAREAS_PROGRAMADAS() {
		return LoadConfig.getInstance().getEQUIPO_TAREAS_PROGRAMADAS();
	}

	public String getVISUALIZADOR() {
		return LoadConfig.getInstance().getVISUALIZADOR();
	}

	public String getHISTORIA_CLINICA() {
		return LoadConfig.getInstance().getHISTORIA_CLINICA();
	}

	public String getHISTORIA_CLINICA_URL() {
		return LoadConfig.getInstance().getHISTORIA_CLINICA_URL();
	}

	public String getAPI_URL() {
		return LoadConfig.getInstance().getAPI_URL();
	}

	public String getANUNCIADOR_PACIENTE_URL()
	{
		return LoadConfig.getInstance().getANUNCIADOR_PACIENTE_URL();
	}

	public String getJITSI_SERVER() {
		return LoadConfig.getInstance().getJITSI_SERVER();
	}
}
