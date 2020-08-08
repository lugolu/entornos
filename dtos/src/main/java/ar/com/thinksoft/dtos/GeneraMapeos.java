package ar.com.thinksoft.dtos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.utils.CommonFunctions;
import ar.com.thinksoft.utils.Constants;
import ar.com.thinksoft.utils.CustomMethod;
import ar.com.thinksoft.utils.CustomType;
import ar.com.thinksoft.utils.DontAddGenerator;
import ar.com.thinksoft.utils.ExcludeCommonFunctions;
import ar.com.thinksoft.utils.SeverityBundle;

@SuppressWarnings({"checkstyle:CustomImportControl", "checkstyle:IllegalInstantiation", "checkstyle:ModifiedControlVariable", "checkstyle:UnusedImports"})
public class GeneraMapeos {

	private final String SALTO = "\n";
	private final String url = "jdbc:postgresql://localhost:5432/db";
	//	private final String url = "jdbc:oracle:thin:@192.168.150.25:1521:HOSDESA2";

	private final String user = "demo";
	private final String pass = "example";
	private final String DEFAULT_SCHEMA = "public";
	private static int mapeoNumero = 0;

	@SuppressWarnings("unchecked")
	public static void main1(String[] args) {
		try {
			String path = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");

			boolean java = !path.contains("target");
			if (java) {
				path = path.split("/bin")[0] + "/src/ar/com/thinksoft/dtos/";
			} else {
				path = path.split("/target")[0]+"/src/main/java/ar/com/thinksoft/dtos/";
			}

			File fDestino = new File(path);
			List<ClaseMapeo> clases = getFiles(fDestino);

			CommonFunctions.sortList(clases, true, "className");

			//				System.out.println();
			String tmp = "new GeneraMapeos().genera(java, \"{schema}\", \"{tabla}\", {mapeo}, {id}, \"{pck}\", {commonFunctions}, false);";
			String tabla = "";
			String schema = "TS";
			String mapeo = "null";
			String id = "null";
			String pck = "";
			String commonFunctions = "true";
			for (ClaseMapeo clase : clases) {
				String s = clase.getFullClass();

				tabla = "";
				schema = "TS";
				mapeo = "null";
				id = "null";
				pck = "";

				try {
					Class c = Class.forName(s);
					Annotation table = c.getAnnotation(Table.class);
					if (table != null) {
						schema = ((Table) table).schema();
						if (schema == null || schema.isEmpty()) {
							schema = "TS";
						}
						tabla = ((Table) table).name();

						if (tabla.startsWith("TMP")) {
							try {
								Field[] fields = c.getDeclaredFields();
								for (Field f : fields) {
									Annotation annoId = f.getAnnotation(Id.class);
									if (annoId != null) {
										Column col = f.getAnnotation(Column.class);
										id = "\"" + col.name() + "\"";
									}
								}
							} catch (Exception e) {
							}
						}

						pck = s.replace("ar.com.thinksoft.dtos.", "");
						pck = pck.substring(0, pck.indexOf("."));

						commonFunctions = getCommonFunctions(path, s);

						System.out.println("//		" + tmp.replace("{schema}", schema).replace("{tabla}", tabla).replace("{mapeo}", mapeo).replace("{id}", id).replace("{pck}", pck).replace("{commonFunctions}", commonFunctions));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getCommonFunctions (String path, String clase) throws IOException {
		String file = path + clase.replace("ar.com.thinksoft.dtos.", "").replace(".", "/") + ".java";
		//		System.err.println(file);

		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String linea = null;

			int pos = 0;
			while ((linea = br.readLine()) != null) {
				pos = linea.indexOf("utils.CommonFunctions");
				if (pos >= 0) {
					return "true";
				}

				pos = linea.indexOf(" class ");
				if (pos >= 0) {
					return "false";
				}
			}
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

		return "false";
	}

	@SuppressWarnings("unchecked")
	private static List<ClaseMapeo> getFiles(File fDestino) throws ClassNotFoundException {
		List<ClaseMapeo> clases = new LinkedList<ClaseMapeo>();

		File[] archivosOrigen = fDestino.listFiles();
		for (File x : archivosOrigen) {
			if (x.isDirectory()) {
				if (".svn".equals(x.getName())) {
					continue;
				}
				//				System.out.println(x.getAbsolutePath());
				File[] archivos = new File(x.getAbsolutePath()).listFiles();
				if (archivos == null) {
					System.out.println("\tno files\t" + x.getName());
					continue;
				}
				for (File f : archivos) {
					if (f.isDirectory()) {
						clases.addAll(getFiles(f));
					}
					else {
						if (!f.getName().endsWith(".java")) {
							continue;
						}
						String fullName = fDestino.getAbsolutePath().split("src")[1].replaceAll("\\\\", ".");
						fullName = fullName.replaceAll("/", ".") + "." + x.getName() + "." + f.getName();
						fullName = fullName.replace("java", "");
						fullName = fullName.substring(1, fullName.length() - 1);
						Class c = Class.forName(fullName);
						Annotation entity = c.getAnnotation(Entity.class);
						if (entity != null) {
							ClaseMapeo clase = new ClaseMapeo();
							clase.setFullClass(fullName);
							clase.setClassName(f.getName().replace("java", ""));
							clases.add(clase);
						}
					}
				}
			}
			else {
				if (!x.getName().endsWith(".java")) {
					continue;
				}
				String fullName = fDestino.getAbsolutePath().split("src")[1].replaceAll("\\\\", ".");
				fullName = fullName.replaceAll("/", ".") + "." + x.getName();
				fullName = fullName.replace("java", "");
				fullName = fullName.substring(1, fullName.length() - 1);
				Class c = Class.forName(fullName);
				Annotation entity = c.getAnnotation(Entity.class);
				if (entity != null) {
					ClaseMapeo clase = new ClaseMapeo();
					clase.setFullClass(fullName);
					clase.setClassName(x.getName().replace("java", ""));
					clases.add(clase);
				}
			}
		}

		return clases;
	}
	/* */
	public static void main(String[] args) {
		Annotation annotation = null;
		Column column = null;
		Field field = null;
		Entity entity = null;
		Id id = null;
		Table table = null;

		try {
			boolean java = false;
			new GeneraMapeos().genera(java, "entornos", "cliente", null, null, "entornos", true, false);
			new GeneraMapeos().genera(java, "entornos", "entorno_cliente", null, null, "entornos", true, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Funcion para generar mapeo de las tablas usando Annotations.<br />
	 * Campos de formula deben mapearse con el annotation {@link Formula} y los metodos con {@link CustomMethod} para que se conserven al remapear.<br />
	 * Campos no mapeados deben mapearse con el annotation {@link Transient} y los metodos con {@link CustomMethkod} para que se conserven al remapear.<br />
	 * Campos a los que no deba aplicarse CommonFunctions deben anotarse con {@link ExcludeCommonFunctions}.<br />
	 * Campos a los que deba mapearse con un tipo en particular deben anotarse con {@link CustomType}.<br />
	 * En caso que la tabla tenga pk compuesta el genera la clase Id.
	 * @param java
	 * @param schema Esquema de la tabla a ser mapeada
	 * @param tabla Tabla a ser mapeada
	 * @param mapeo Nombre del mapeo, si no se pasa se asigna el mismo de la tabla
	 * @param pk Campo a mapear como pk si es que no tiene la tabla
	 * @param pck Package de java en el que se genera el mapeo
	 * @param usaCommonFunctions Asignar usando CommonFunctions
	 * @param generaBusiness Generar archivos IntBus e ImpBus en las carpetas del pck business
	 */
	private void genera (boolean java, String schema, String tabla, String mapeo, String pk, String pck, boolean usaCommonFunctions, boolean generaBusiness) throws Exception {
		Connection connection = null;
		try {
			boolean mysql = false;
			boolean oracle = false;
			boolean postgres = false;
			String sufijoPk = null;

			if (url.contains("mysql")) {
				mysql = true;
				sufijoPk = "_PK";
			}
			else  if (url.contains("oracle")) {
				oracle = true;
				sufijoPk = "_ID";
			}
			else  if (url.contains("postgres")) {
				postgres = true;
				sufijoPk = "_ID";
			}
			else {
				throw new BusinessException("No se pudo determinar el tipo de base.", SeverityBundle.FATAL);
			}

			StringBuffer mapeos = new StringBuffer();

			connection = DriverManager.getConnection (url, user, pass);
			connection.setAutoCommit(false);

			String path = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");
			if (java) {
				path = path.split("bin")[0];
			}
			else {
				path = path.split("target")[0];
			}

			String pathBusiness = null;
			String pathDtos = null;
			if (java) {
				pathBusiness = path + "/src/ar/com/thinksoft/business/";
				pathDtos = path + "/src/ar/com/thinksoft/dtos/";
			}
			else {
				pathBusiness = path.replace("dtos", "business") + "/src/main/java/ar/com/thinksoft/business/";
				pathDtos = path + "/src/main/java/ar/com/thinksoft/dtos/";
			}

			String sql = null;
			if (mysql) {
				sql = "SELECT DISTINCT TABLE_NAME, case when (SELECT count(*) FROM INFORMATION_SCHEMA.COLUMNS c WHERE TABLE_SCHEMA = 'consultorio'   AND TABLE_NAME = x.TABLE_NAME and COLUMN_KEY = 'PRI') <= 1 then 'NO' else 'SI' end PK_MULTIPLE " +
						"    FROM INFORMATION_SCHEMA.COLUMNS x" +
						"    WHERE TABLE_SCHEMA='" + schema + "'";

				if (tabla != null) {
					sql += " AND TABLE_NAME like '" + tabla + "' ";
				}

				if (pk == null) {
					sql += " UNION ALL " + sql.replace("DISTINCT TABLE_NAME", "DISTINCT concat(TABLE_NAME, '_PK')");
				}
			}
			else if (oracle) {
				sql = "SELECT DISTINCT TABLE_NAME, case when (select (select count(*) from all_cons_columns a where a.owner = p.owner and a.table_name = p.table_name and a.CONSTRAINT_name = p.CONSTRAINT_name) " +
						"						                  from all_constraints p " +
						"						                 where p.owner = c.owner " +
						"						                   and p.table_name = c.table_name " +
						"						                   and p.CONSTRAINT_TYPE = 'P') > 1 then 'SI' else 'NO' end PK_MULTIPLE " +
						"    FROM all_tables c" +
						"    WHERE owner='" + schema + "'";

				if (tabla != null) {
					sql += " AND TABLE_NAME like '" + tabla + "' ";
				}

				if (pk == null) {
					sql += " UNION ALL " + sql.replace("DISTINCT TABLE_NAME", "DISTINCT concat(TABLE_NAME, '_ID')");
				}
			}
			else if (postgres) {
				sql = "SELECT DISTINCT TABLE_NAME, case when (SELECT count(*) FROM INFORMATION_SCHEMA.key_column_usage c WHERE c.TABLE_SCHEMA = x.TABLE_SCHEMA AND c.TABLE_NAME = x.TABLE_NAME) <= 1 then 'NO' else 'SI' end PK_MULTIPLE " +
						"    FROM information_schema.columns x" +
						"    WHERE table_schema='" + schema + "'";

				if (tabla != null) {
					sql += " AND TABLE_NAME like '" + tabla + "' ";
				}

				if (pk == null) {
					sql += " UNION ALL " + sql.replace("DISTINCT TABLE_NAME", "DISTINCT concat(TABLE_NAME, '_ID')");
				}
			}
			System.out.println(sql);

			Statement stTablas = connection.createStatement();
			ResultSet rsTablas = stTablas.executeQuery(sql);

			String tableName = null;
			String tienePkMultiple = null;
			while (rsTablas.next()) {
				mapeoNumero++;

				tableName = rsTablas.getString("TABLE_NAME");

				if (pk != null) {
					tienePkMultiple = Constants.STRING_NO;
				}
				else {
					tienePkMultiple = rsTablas.getString("PK_MULTIPLE");
				}

				if (mapeo == null || tableName.endsWith(sufijoPk)) {
					mapeo = tableName;
				}

				if (tableName.endsWith(sufijoPk)) {
					if (Constants.STRING_NO.equals(tienePkMultiple)) {
						continue;
					}
				}
				else {
					mapeos.append("\t\t<mapping class=\"ar.com.thinksoft.dtos." + pck + "." + getJavaClass(mapeo) + "\" />").append(SALTO);
				}

				Map<String, StringBuffer> customFields = new HashMap<String, StringBuffer>();
				Map<String, StringBuffer> customMethods = new HashMap<String, StringBuffer>();
				Map<String, StringBuffer> customTypes = new HashMap<String, StringBuffer>();
				StringBuffer importCustom = new StringBuffer();
				StringBuffer serialVersionUID = new StringBuffer();
				List<String> excludeCommonFunctions = new LinkedList<>();
				List<String> defaults = new LinkedList<>();
				boolean dontAddGenerator = false;

				try {
					Class c = Class.forName("ar.com.thinksoft.dtos." + pck + "." + getJavaClass(mapeo));
					Annotation annotation = c.getAnnotation(DontAddGenerator.class);

					if (annotation != null) {
						dontAddGenerator = true;
					}
				} catch (ClassNotFoundException e) {
				}

				File destino = new File(pathDtos + pck + "/" + getJavaClass(mapeo) + ".java");
				System.out.println(destino);
				if (destino.exists()) {
					loadCustomMethods(destino, customFields, customTypes, customMethods, importCustom, serialVersionUID, excludeCommonFunctions);
					destino.delete();
				}
				else {
					File dir = new File(pathDtos + pck + "/");
					if (!dir.exists()) {
						dir.mkdirs();
					}
				}

				if (!destino.exists()) {
					try {
						destino.createNewFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				StringBuffer atributes = new StringBuffer();
				StringBuffer atributosConstructorPk = new StringBuffer();
				StringBuffer atributosConstructorCompleto = new StringBuffer();
				StringBuffer equalsAtributes = new StringBuffer();
				StringBuffer getPkAtributes = new StringBuffer();
				StringBuffer hashAtributes = new StringBuffer();
				StringBuffer idAtributes = new StringBuffer();
				StringBuffer methods = new StringBuffer();
				StringBuffer parametrosConstructorPk = new StringBuffer();
				StringBuffer parametrosConstructorCompleto = new StringBuffer();
				int ctdParametrosConstructorCompleto = 0;

				if (customMethods.containsKey("defaultConstructor")) {
					methods.append(customMethods.get("defaultConstructor"));
					customMethods.remove("defaultConstructor");
				}
				else {
					methods.append("\tpublic " + getJavaClass(mapeo) + "() {").append(SALTO);
					methods.append("\t}").append(SALTO).append(SALTO);
				}

				if (customMethods.containsKey("pkConstructor")) {
					methods.append(customMethods.get("pkConstructor"));
					customMethods.remove("pkConstructor");
				}
				else {
					methods.append("\tpublic " + getJavaClass(mapeo) + "({parametrosConstructorPk}) {").append(SALTO);
					methods.append("{atributosConstructorPk}");
					methods.append("\t}").append(SALTO).append(SALTO);
				}

				if (!tableName.endsWith(sufijoPk)) {
					if (customMethods.containsKey("completoConstructor")) {
						methods.append(customMethods.get("completoConstructor"));
						customMethods.remove("completoConstructor");
					}
					else {
						methods.append("{constructorCompleto}");
					}
				}

				methods.append("\tpublic " + getJavaClass(mapeo) + " clon() throws CloneNotSupportedException {").append(SALTO);
				methods.append("\t\treturn (" + getJavaClass(mapeo) + ") super.clone();").append(SALTO);
				methods.append("\t}").append(SALTO).append(SALTO);

				methods.append("{idMethods}");

				if (oracle) {
					sql = " SELECT COLUMN_NAME, " +
							"       c.DATA_DEFAULT COLUMN_DEFAULT, " +
							"       decode(c.nullable, 'Y', 'YES', 'N', 'NO', null) IS_NULLABLE, " +
							"       c.DATA_TYPE, " +
							"       (select 'PRI' " +
							"          from all_cons_columns a " +
							"         where a.owner = c.owner " +
							"           and a.table_name = c.table_name " +
							"           and a.column_name = c.column_name " +
							"           and exists (select 1 " +
							"                  from all_constraints p " +
							"                 where p.owner = c.owner " +
							"                   and p.table_name = a.table_name " +
							"                   and p.constraint_name = a.constraint_name " +
							"                   and p.CONSTRAINT_TYPE = 'P')) COLUMN_KEY, " +
							"       data_length CHARACTER_MAXIMUM_LENGTH, " +
							"       data_precision precission, " +
							"       data_scale scala, " +
							"       null EXTRA, " +
							(tableName.endsWith("_ID") ? " 'N' FK" : " (select 'S' " +
									"           from all_cons_columns a " +
									"          where a.OWNER = c.OWNER " +
									"            and a.TABLE_NAME = c.TABLE_NAME " +
									"            and a.COLUMN_NAME = c.COLUMN_NAME " +
									"            and exists (select 1 " +
									"                   from all_constraints f " +
									"                  where f.OWNER = a.OWNER " +
									"                    and f.TABLE_NAME = a.TABLE_NAME " +
									"                    and f.CONSTRAINT_NAME = a.CONSTRAINT_NAME " +
									"                    and f.CONSTRAINT_TYPE = 'R') "+
									"            		 and rownum = 1) FK ") +
							" FROM all_tab_columns c " +
							"WHERE owner = '" + schema + "' " +
							"  AND TABLE_NAME = '" + tableName.replace("_ID", "") + "' " +
							"  AND COLUMN_NAME not in ('ACTUALIZADO_POR', 'FECHA_LAST_UPDATE') " +
							"  AND COLUMN_NAME not like 'O\\_%' ESCAPE '\\' " +
							"  AND COLUMN_NAME not like 'OLD\\_%' ESCAPE '\\' " +
							" order by 5, column_id";
				}
				else if (mysql) {
					sql = " SELECT COLUMN_NAME, COLUMN_DEFAULT, IS_NULLABLE, upper(DATA_TYPE) as DATA_TYPE, COLUMN_KEY, CHARACTER_MAXIMUM_LENGTH, EXTRA, null as precission, null as scala," +
							(tableName.endsWith(sufijoPk) ? " 'N' FK" : " (select coalesce(min('S'), 'N') FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE k WHERE k.TABLE_SCHEMA = c.TABLE_SCHEMA AND k.TABLE_NAME = c.TABLE_NAME AND k.COLUMN_NAME = c.COLUMN_NAME AND k.REFERENCED_TABLE_NAME is not null) FK ") +
							" FROM INFORMATION_SCHEMA.COLUMNS c " +
							"WHERE TABLE_SCHEMA = '" + schema + "' " +
							"  AND TABLE_NAME = '" + tableName.replace(sufijoPk, "") + "' ";
				}
				else if (postgres) {
					sql = " SELECT COLUMN_NAME, COLUMN_DEFAULT, IS_NULLABLE, upper(DATA_TYPE) as DATA_TYPE, " +
							" (select 'PRI' from information_schema.key_column_usage x WHERE c.TABLE_SCHEMA = x.TABLE_SCHEMA AND c.TABLE_NAME = x.TABLE_NAME AND c.COLUMN_NAME = x.COLUMN_NAME and exists (select 1 from information_schema.table_constraints p where p.constraint_schema = x.constraint_schema and p.table_schema = x.table_schema and p.constraint_name = x.constraint_name and p.constraint_type = 'PRIMARY KEY')) COLUMN_KEY, " +
							" CHARACTER_MAXIMUM_LENGTH,  null EXTRA, null as precission, null as scala, " +
							(tableName.endsWith(sufijoPk) ? " 'N' FK" : " (select coalesce(min('S'), 'N') from information_schema.key_column_usage x WHERE c.TABLE_SCHEMA = x.TABLE_SCHEMA AND c.TABLE_NAME = x.TABLE_NAME AND c.COLUMN_NAME = x.COLUMN_NAME and exists (select 1 from information_schema.table_constraints p where p.constraint_schema = x.constraint_schema and p.table_schema = x.table_schema and p.constraint_name = x.constraint_name and p.constraint_type = 'FOREIGN KEY')) FK ") +
							" FROM INFORMATION_SCHEMA.COLUMNS c " +
							"WHERE TABLE_SCHEMA = '" + schema + "' " +
							"  AND TABLE_NAME = '" + tableName.replace(sufijoPk, "") + "' ";
				}
				else {
					throw new BusinessException("No se pudo determinar el tipo de base.", SeverityBundle.FATAL);
				}
				System.out.println(sql);

				Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while (rs.next()) {
					byte[] bytesDefault = rs.getBytes("COLUMN_DEFAULT");

					boolean esAtributo = false;
					boolean esPk = false;

					if (pk != null) {
						if (rs.getString("COLUMN_NAME").equals(pk)) {
							esPk = true;
						}
					}
					else {
						esPk = "PRI".equals(rs.getString("COLUMN_KEY"));
					}

					boolean esFk = "S".equals(rs.getString("FK"));

					String tipoDato = getTipoDato(oracle, mysql, postgres, rs.getString("DATA_TYPE"), rs.getLong("CHARACTER_MAXIMUM_LENGTH"), rs.getLong("precission"), rs.getLong("scala"));

					if (customTypes.containsKey(getJavaName(rs.getString("COLUMN_NAME")))) {
						tipoDato = customTypes.get(getJavaName(rs.getString("COLUMN_NAME"))).toString();
					}

					String defaultValue = null;
					if (bytesDefault != null) {
						defaultValue =  new String(bytesDefault, Charset.forName("UTF-8"));
						if (defaultValue != null) {
							defaultValue = defaultValue.replace("(", "").replace(")", "");
							defaultValue = defaultValue.replace("'", "").trim();

							if ("Date".equals(tipoDato)
									|| "NULL".equals(defaultValue)) {
								defaultValue = null;
							}

							if (defaultValue != null) {
								String prefixValue = "";
								String suffixValue = "";
								String suffix = "";
								if (Constants.STRING_N.equals(defaultValue)) {
									suffix = "Boolean";
									defaultValue = "false";
								}
								else if (Constants.STRING_S.equals(defaultValue)) {
									suffix = "Boolean";
									defaultValue = "true";
								}

								if ("Double".equals(tipoDato)) {
									suffixValue = "D";
								}
								else if ("Long".equals(tipoDato)) {
									suffixValue = "L";
								}
								else if ("String".equals(tipoDato)) {
									prefixValue = suffixValue = "\"";
								}

								defaults.add("ret.set" + getJavaClass(rs.getString("COLUMN_NAME")) + suffix + "(" + prefixValue + defaultValue + suffixValue + ");");
							}
						}
					}

					if (tableName.endsWith(sufijoPk) && esPk) {
						atributes.append("\t@Column(name=\"" + rs.getString("COLUMN_NAME") + "\")").append(SALTO);
						if ("auto_increment".equals(rs.getString("EXTRA"))) {
							atributes.append("\t@GeneratedValue(strategy=GenerationType.IDENTITY)").append(SALTO);
						}
						esAtributo = true;
						if (equalsAtributes.length() > 0) {
							equalsAtributes.append(" &&").append(SALTO).append("\t\t\t\t");
						}
						equalsAtributes.append("Objects.equals(get" + getJavaClass(rs.getString("COLUMN_NAME")) + "(), that.get" + getJavaClass(rs.getString("COLUMN_NAME")) + "())");
						if (hashAtributes.length() > 0) {
							hashAtributes.append(", ");
						}
						hashAtributes.append("get" + getJavaClass(rs.getString("COLUMN_NAME")) + "()");

						if (parametrosConstructorPk.length() > 0) {
							parametrosConstructorPk.append(", ");
						}
						parametrosConstructorPk.append(tipoDato + " " + getJavaName(rs.getString("COLUMN_NAME")));

						atributosConstructorPk.append("\t\tthis." + getJavaName(rs.getString("COLUMN_NAME")) + " = " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);
					}
					else if (!tableName.endsWith(sufijoPk)) {
						if ("S".equals(rs.getString("FK"))) {
							atributes.append("\t//FK").append(SALTO);
						}

						if (parametrosConstructorCompleto.length() > 0) {
							parametrosConstructorCompleto.append(", ");
						}
						if (ctdParametrosConstructorCompleto > 0 && (ctdParametrosConstructorCompleto % 4) == 0) {
							parametrosConstructorCompleto.append(SALTO);
							parametrosConstructorCompleto.append("\t\t\t");
						}
						ctdParametrosConstructorCompleto++;
						parametrosConstructorCompleto.append(tipoDato + " " + getJavaName(rs.getString("COLUMN_NAME")));

						if (Constants.STRING_NO.equals(tienePkMultiple) && esPk) {
							atributosConstructorCompleto.append("\t\t" + getJavaName(rs.getString("COLUMN_NAME")) + "Aux = " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);
						}
						atributosConstructorCompleto.append("\t\tthis." + getJavaName(rs.getString("COLUMN_NAME")) + " = " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);

						if (esPk) {
							idAtributes.append("\t\t\t\t@AttributeOverride(name = \"" + getJavaName(rs.getString("COLUMN_NAME")) + "\", column = @Column(name = \"" + (rs.getString("COLUMN_NAME").toUpperCase()) + "\")),").append(SALTO);
							getPkAtributes.append("\t\tpk.set" + getJavaClass(rs.getString("COLUMN_NAME")) + "(" + getJavaName(rs.getString("COLUMN_NAME")) + ");").append(SALTO);

							if (parametrosConstructorPk.length() > 0) {
								parametrosConstructorPk.append(", ");
							}
							parametrosConstructorPk.append(tipoDato + " " + getJavaName(rs.getString("COLUMN_NAME")));

							if (Constants.STRING_NO.equals(tienePkMultiple) && esPk) {
								atributosConstructorPk.append("\t\t" + getJavaName(rs.getString("COLUMN_NAME")) + "Aux = " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);
							}
							atributosConstructorPk.append("\t\tthis." + getJavaName(rs.getString("COLUMN_NAME")) + " = " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);

							if (Constants.STRING_NO.equals(tienePkMultiple)) {
								if (equalsAtributes.length() > 0) {
									equalsAtributes.append(" &&").append(SALTO).append("\t\t\t\t");
								}
								equalsAtributes.append("Objects.equals(get" + getJavaClass(rs.getString("COLUMN_NAME")) + "(), that.get" + getJavaClass(rs.getString("COLUMN_NAME")) + "())");
								if (hashAtributes.length() > 0) {
									hashAtributes.append(", ");
								}
								hashAtributes.append("get" + getJavaClass(rs.getString("COLUMN_NAME")) + "()");

								atributes.append("\t@Id").append(SALTO);
								atributes.append("\t@Column(name=\"" + rs.getString("COLUMN_NAME") + "\")").append(SALTO);
								if (pk == null && rs.getString("COLUMN_NAME").startsWith("ID_") && !dontAddGenerator) {
									if (oracle) {
										atributes.append("\t@GenericGenerator(name = \"CustomIdGenerator\", strategy = \"ar.com.thinksoft.hibernate.CustomIdGenerator\")").append(SALTO);
										atributes.append("\t@GeneratedValue(generator = \"CustomIdGenerator\")").append(SALTO);
									}
									else if (mysql) {
										atributes.append("\t@GeneratedValue(strategy=GenerationType.IDENTITY)").append(SALTO);
									}
									else {
										throw new BusinessException("No se pudo determinar el tipo de base.", SeverityBundle.FATAL);
									}
								}
								atributes.append("\tprivate " + tipoDato + " " + getJavaName(rs.getString("COLUMN_NAME")) + "Aux;").append(SALTO);
								atributes.append("\t@Column(name=\"" + rs.getString("COLUMN_NAME") + "\"");
								if (esPk) {
									atributes.append(", insertable=false");
									atributes.append(", updatable=false");
								}
								else if ("NO".equals(rs.getString("IS_NULLABLE"))) {
									atributes.append(", nullable=false");
								}
								atributes.append(")").append(SALTO);
							}
						} else {
							if (defaultValue != null) {
								atributes.append("\t//default [" + defaultValue + "]").append(SALTO);
							}
							atributes.append("\t@Column(name=\"" + rs.getString("COLUMN_NAME") + "\"");
							if (esPk) {
								atributes.append(", insertable=false");
								atributes.append(", updatable=false");
							}
							else if ("NO".equals(rs.getString("IS_NULLABLE"))) {
								atributes.append(", nullable=false");
							}
							atributes.append(")").append(SALTO);
							atributes.append("{ExcludeCommonFunctions." + getJavaName(rs.getString("COLUMN_NAME")) + "}");
							if (customTypes.containsKey(getJavaName(rs.getString("COLUMN_NAME")))) {
								atributes.append("\t@CustomType(type=\"" + customTypes.get(getJavaName(rs.getString("COLUMN_NAME"))) + "\")").append(SALTO);
							}
						}
						esAtributo = true;
					}

					if (esAtributo) {
						atributes.append("\tprivate " + tipoDato + " " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);

						boolean customGet = false;
						for (String s : customMethods.keySet()) {
							if (("get" + getJavaClass(rs.getString("COLUMN_NAME"))).equals(s.substring(3))) {
								methods.append(customMethods.get(s));
								customMethods.remove(s);
								customGet = true;
								break;
							}
						}
						if (!customGet) {
							methods.append("\tpublic " + tipoDato + " get" + getJavaClass(rs.getString("COLUMN_NAME")) + "() {").append(SALTO);
							if (Constants.STRING_NO.equals(tienePkMultiple) && esPk) {
								methods.append("\t\treturn " + getJavaName(rs.getString("COLUMN_NAME")) + " != null ? " + getJavaName(rs.getString("COLUMN_NAME")) + " : " + getJavaName(rs.getString("COLUMN_NAME")) + "Aux;").append(SALTO);
							} else {
								methods.append("\t\treturn " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);
							}
							methods.append("\t}").append(SALTO).append(SALTO);
						}

						boolean customSet = false;
						for (String s : customMethods.keySet()) {
							if (("set" + getJavaClass(rs.getString("COLUMN_NAME"))).equals(s.substring(3))) {
								methods.append(customMethods.get(s));
								customMethods.remove(s);
								customSet = true;
								break;
							}
						}
						if (!customSet) {
							methods.append("\tpublic void set" + getJavaClass(rs.getString("COLUMN_NAME")) + "(" + tipoDato + " " + getJavaName(rs.getString("COLUMN_NAME")) + ") {").append(SALTO);
							if (Constants.STRING_NO.equals(tienePkMultiple) && esPk) {
								methods.append("\t\t" + getJavaName(rs.getString("COLUMN_NAME")) + "Aux = " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);
							}
							if (!"CLOB".equals(rs.getString("DATA_TYPE")) && "String".equals(tipoDato) && usaCommonFunctions && !esPk && !esFk) {
								methods.append("\t\tthis." + getJavaName(rs.getString("COLUMN_NAME")) + " = " + (usaCommonFunctions ? "CommonFunctions.replaceAccentedChars(" : "") + getJavaName(rs.getString("COLUMN_NAME")) + (usaCommonFunctions ? ")" : "") + ";").append(SALTO);
							} else {
								methods.append("\t\tthis." + getJavaName(rs.getString("COLUMN_NAME")) + " = " + getJavaName(rs.getString("COLUMN_NAME")) + ";").append(SALTO);
							}
							methods.append("\t}").append(SALTO).append(SALTO);
						}

						if ("CHAR".equals(rs.getString("DATA_TYPE")) && rs.getLong("CHARACTER_MAXIMUM_LENGTH") == 1L && !customTypes.containsKey(getJavaName(rs.getString("COLUMN_NAME")))) {
							methods.append("\tpublic boolean get" + getJavaClass(rs.getString("COLUMN_NAME")) + "Boolean() {").append(SALTO);
							methods.append("\t\treturn Constants.CHAR_S.equals(" + getJavaName(rs.getString("COLUMN_NAME")) + ");").append(SALTO);
							methods.append("\t}").append(SALTO).append(SALTO);

							methods.append("\tpublic void set" + getJavaClass(rs.getString("COLUMN_NAME")) + "Boolean(boolean " + getJavaName(rs.getString("COLUMN_NAME")) + ") {").append(SALTO);
							methods.append("\t\tthis." + getJavaName(rs.getString("COLUMN_NAME")) + " = (" + getJavaName(rs.getString("COLUMN_NAME")) + " ? Constants.CHAR_S : Constants.CHAR_N);").append(SALTO);
							methods.append("\t}").append(SALTO).append(SALTO);
						}
					}
				}

				StringBuffer idMethods = new StringBuffer();
				if (!tableName.endsWith(sufijoPk) && Constants.STRING_SI.equals(tienePkMultiple)) {
					if (oracle || postgres) {
						equalsAtributes.append("Objects.equals(getId(), that.getId())");
						hashAtributes.append("getId()");
						idMethods.append("	public " + getJavaClass(mapeo) + "Id getId() {").append(SALTO);
						idMethods.append("		" + getJavaClass(mapeo) + "Id pk = new " + getJavaClass(mapeo) + "Id();").append(SALTO);
					}
					else if (mysql) {
						equalsAtributes.append("Objects.equals(getPk(), that.getPk())");
						hashAtributes.append("getPk()");
						idMethods.append("	public " + getJavaClass(mapeo) + "Pk getPk() {").append(SALTO);
						idMethods.append("		" + getJavaClass(mapeo) + "Pk pk = new " + getJavaClass(mapeo) + "Pk();").append(SALTO);
					}
					idMethods.append(SALTO);
					idMethods.append(getPkAtributes);
					idMethods.append(SALTO);
					idMethods.append("		return pk;").append(SALTO);
					idMethods.append("	}").append(SALTO).append(SALTO);
				}

				methods.append("	@Override").append(SALTO);
				boolean customEquals = false;
				for (String s : customMethods.keySet()) {
					if ("equals".equals(s.substring(3))) {
						methods.append(customMethods.get(s));
						customMethods.remove(s);
						customEquals = true;
						break;
					}
				}
				if (!customEquals) {
					methods.append("	public boolean equals(Object o) {").append(SALTO);
					methods.append("		if (this == o) {").append(SALTO);
					methods.append("			return true;").append(SALTO);
					methods.append("		}").append(SALTO);
					methods.append("		if (!(o instanceof " + getJavaClass(mapeo) + ")) {").append(SALTO);
					methods.append("			return false;").append(SALTO);
					methods.append("		}").append(SALTO);
					methods.append("		" + getJavaClass(mapeo) + " that = (" + getJavaClass(mapeo) + ") o;").append(SALTO);
					methods.append("		return " + equalsAtributes.toString() + ";").append(SALTO);
					methods.append("	}").append(SALTO).append(SALTO);
				}

				methods.append("	@Override").append(SALTO);
				boolean customHashcode = false;
				for (String s : customMethods.keySet()) {
					if ("hashCode".equals(s.substring(3))) {
						methods.append(customMethods.get(s));
						customMethods.remove(s);
						customHashcode = true;
						break;
					}
				}
				if (!customHashcode) {
					methods.append("	public int hashCode() {").append(SALTO);
					methods.append("		return Objects.hash(" + hashAtributes.toString() + ");").append(SALTO);
					methods.append("	}").append(SALTO).append(SALTO);
				}

				if (!tableName.endsWith(sufijoPk) /*&& defaults.size() > 0*/) {
					boolean customDefaultBuilder = false;
					for (String s : customMethods.keySet()) {
						if ("defaultBuilder".equals(s.substring(3))) {
							methods.append(customMethods.get(s));
							customMethods.remove(s);
							customDefaultBuilder = true;
							break;
						}
					}
					if (!customDefaultBuilder) {
						methods.append("	public static " + getJavaClass(mapeo) + " defaultBuilder() {").append(SALTO);
						methods.append("		" + getJavaClass(mapeo) + " ret = new " + getJavaClass(mapeo) + "();").append(SALTO).append(SALTO);
						for (String s : defaults) {
							methods.append("\t\t").append(s).append(SALTO);
						}
						methods.append(SALTO);
						methods.append("		return ret;").append(SALTO);
						methods.append("	}").append(SALTO).append(SALTO);
					}
				}

				st.close();
				connection.commit();

				StringBuffer dto = new StringBuffer();
				dto.append("package ar.com.thinksoft.dtos." + pck + ";").append(SALTO).append(SALTO);

				dto.append("import java.io.Serializable;").append(SALTO).append(SALTO);
				dto.append("import java.util.Date;").append(SALTO).append(SALTO);
				dto.append("import java.util.Objects;").append(SALTO).append(SALTO);

				if (tableName.endsWith(sufijoPk)) {
					dto.append("import javax.persistence.Column;").append(SALTO);
					dto.append("import javax.persistence.Embeddable;").append(SALTO);
				}
				else {
					if (Constants.STRING_SI.equals(tienePkMultiple)) {
						dto.append("import javax.persistence.AttributeOverride;").append(SALTO);
						dto.append("import javax.persistence.AttributeOverrides;").append(SALTO);
						dto.append("import javax.persistence.IdClass;").append(SALTO);
					}
					dto.append("import javax.persistence.Column;").append(SALTO);
					dto.append("import javax.persistence.Entity;").append(SALTO);
					dto.append("import javax.persistence.GeneratedValue;").append(SALTO);
					dto.append("import javax.persistence.GenerationType;").append(SALTO);
					dto.append("import javax.persistence.Id;").append(SALTO);
					dto.append("import javax.persistence.Table;").append(SALTO);
					dto.append("import javax.persistence.Transient;").append(SALTO).append(SALTO);
				}

				if (!tableName.endsWith(sufijoPk)) {
					dto.append("import ar.com.thinksoft.utils.CommonFunctions;").append(SALTO);
					dto.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
					dto.append("import ar.com.thinksoft.utils.CustomMethod;").append(SALTO).append(SALTO);

					dto.append("import org.hibernate.annotations.Formula;").append(SALTO).append(SALTO);
					if (oracle) {
						dto.append("import org.hibernate.annotations.GenericGenerator;").append(SALTO).append(SALTO);
					}
				}

				dto.append("{@CustomImports}").append(SALTO).append(SALTO);

				if (tableName.endsWith(sufijoPk)) {
					dto.append("@Embeddable").append(SALTO);
				}
				else {
					dto.append("@Entity").append(SALTO);
					if (!DEFAULT_SCHEMA.equals(schema)) {
						dto.append("@Table(schema = \"" + schema + "\", name = \"" + tableName.toUpperCase() + "\")").append(SALTO);
					} else {
						dto.append("@Table(name = \"" + tableName.toUpperCase() + "\")").append(SALTO);
					}
					if (Constants.STRING_SI.equals(tienePkMultiple)) {
						if (oracle || postgres) {
							dto.append("@IdClass(" + getJavaClass(mapeo) + "Id.class)").append(SALTO);
						}
						else if (mysql) {
							dto.append("@IdClass(" + getJavaClass(mapeo) + "Pk.class)").append(SALTO);
						}
					}
				}
				if (dontAddGenerator) {
					dto.append("@DontAddGenerator").append(SALTO);
				}
				dto.append("public class " + getJavaClass(mapeo) + " implements Serializable, Cloneable {").append(SALTO).append(SALTO);

				dto.append("{@SerialVersionUID}").append(SALTO).append(SALTO);

				if (Constants.STRING_SI.equals(tienePkMultiple) && !tableName.endsWith(sufijoPk)) {
					dto.append("\t@Id").append(SALTO);
					dto.append("\t@AttributeOverrides(").append(SALTO);
					dto.append("\t		{").append(SALTO);
					dto.append(idAtributes);
					dto.append("\t		}").append(SALTO);
					dto.append("\t		)").append(SALTO).append(SALTO);
				}

				dto.append(atributes);

				dto.append("{@Formula}");

				dto.append(methods);

				String contenido = dto.toString();

				contenido = contenido.replace("{parametrosConstructorPk}", parametrosConstructorPk);
				contenido = contenido.replace("{atributosConstructorPk}", atributosConstructorPk);

				if (!tableName.endsWith(sufijoPk)) {
					if (parametrosConstructorCompleto.length() > parametrosConstructorPk.length() && ctdParametrosConstructorCompleto < 250) {
						String tmp = "\tpublic " + getJavaClass(mapeo) + "(" + parametrosConstructorCompleto + ") {" + SALTO +
								atributosConstructorCompleto + "\t}" + SALTO
								+ SALTO;

						contenido = contenido.replace("{constructorCompleto}", tmp);
					}
					else {
						contenido = contenido.replace("{constructorCompleto}", "");
					}
				}

				contenido = contenido.replace("{idMethods}", idMethods);

				generateFile(destino, contenido,
						customFields, customTypes, customMethods,
						importCustom, serialVersionUID, excludeCommonFunctions);

				if (generaBusiness && !tableName.endsWith(sufijoPk)) {
					System.out.println(pathBusiness);

					destino = new File(pathBusiness + pck + "/interfaces/IntBus" + getJavaClass(mapeo) + ".java");
					System.out.println(destino);
					if (!destino.exists()) {
						destino.createNewFile();

						StringBuffer intBus = new StringBuffer();
						intBus.append("package ar.com.thinksoft.business." + pck + ".interfaces;").append(SALTO).append(SALTO);
						intBus.append("import ar.com.thinksoft.business.generix.interfaces.IntBusFacadeParent;").append(SALTO);
						intBus.append("import ar.com.thinksoft.dtos." + pck + "." + getJavaClass(mapeo) + ";").append(SALTO).append(SALTO);

						intBus.append("public interface IntBus" + getJavaClass(mapeo) + " extends IntBusFacadeParent<" + getJavaClass(mapeo) + "> {").append(SALTO).append(SALTO);

						generateFile(destino, intBus.toString(),
								customFields, customTypes, customMethods,
								importCustom, serialVersionUID, excludeCommonFunctions);
					}

					destino = new File(pathBusiness + pck + "/implementations/ImpBus" + getJavaClass(mapeo) + ".java");
					System.out.println(destino);
					if (!destino.exists()) {
						destino.createNewFile();
						StringBuffer impBus = new StringBuffer();
						impBus.append("package ar.com.thinksoft.business." + pck + ".implementations;").append(SALTO).append(SALTO);
						if (pck.compareTo("generix") < 0) {
							impBus.append("import ar.com.thinksoft.business." + pck + ".interfaces.IntBus" + getJavaClass(mapeo) + ";").append(SALTO);
						}
						impBus.append("import ar.com.thinksoft.business.generix.implementations.ImpBusFacadeParent;").append(SALTO);
						if (pck.compareTo("generix") > 0) {
							impBus.append("import ar.com.thinksoft.business." + pck + ".interfaces.IntBus" + getJavaClass(mapeo) + ";").append(SALTO);
						}
						impBus.append("import ar.com.thinksoft.dtos." + pck + "." + getJavaClass(mapeo) + ";").append(SALTO).append(SALTO);

						impBus.append("public class ImpBus" + getJavaClass(mapeo) + " extends ImpBusFacadeParent<" + getJavaClass(mapeo) + "> implements IntBus" + getJavaClass(mapeo) + " {").append(SALTO).append(SALTO);

						impBus.append("\t// *************************************************************************************** //").append(SALTO);
						impBus.append("\t// " + getJavaClass(mapeo) + "").append(SALTO);
						impBus.append("\t// *************************************************************************************** //").append(SALTO);
						impBus.append("\tpublic static List<" + getJavaClass(mapeo) + "> select" + getJavaClass(mapeo) + "(StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\treturn BusinessFactory.getIntBus" + getJavaClass(mapeo) + "().selectAll(session);").append(SALTO);
						impBus.append("\t}").append(SALTO);
						impBus.append("\t").append(SALTO);
						impBus.append("\tpublic static List<" + getJavaClass(mapeo) + "> select" + getJavaClass(mapeo) + "Equal(" + getJavaClass(mapeo) + " dto, StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\treturn BusinessFactory.getIntBus" + getJavaClass(mapeo) + "().selectEqual(dto, session);").append(SALTO);
						impBus.append("\t}").append(SALTO);
						impBus.append("\t").append(SALTO);
						impBus.append("\tpublic static " + getJavaClass(mapeo) + " select" + getJavaClass(mapeo) + "EqualOne(" + getJavaClass(mapeo) + " dto, StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\treturn BusinessFactory.getIntBus" + getJavaClass(mapeo) + "().selectEqualOne(dto, session);").append(SALTO);
						impBus.append("\t}").append(SALTO);
						impBus.append("\t").append(SALTO);
						impBus.append("\tpublic static List<" + getJavaClass(mapeo) + "> select" + getJavaClass(mapeo) + "Like(" + getJavaClass(mapeo) + " dto, StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\treturn BusinessFactory.getIntBus" + getJavaClass(mapeo) + "().selectLike(dto, session);").append(SALTO);
						impBus.append("\t}").append(SALTO);
						impBus.append("\t").append(SALTO);
						impBus.append("\tpublic static " + getJavaClass(mapeo) + " select" + getJavaClass(mapeo) + "LikeOne(" + getJavaClass(mapeo) + " dto, StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\treturn BusinessFactory.getIntBus" + getJavaClass(mapeo) + "().selectLikeOne(dto, session);").append(SALTO);
						impBus.append("\t}").append(SALTO);
						impBus.append("\t").append(SALTO);
						impBus.append("\tpublic static void insert" + getJavaClass(mapeo) + "(" + getJavaClass(mapeo) + " dto, StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\tBusinessFactory.getIntBus" + getJavaClass(mapeo) + "().insert(dto, session);").append(SALTO);
						impBus.append("\t}").append(SALTO);
						impBus.append("\t").append(SALTO);
						impBus.append("\tpublic static void update" + getJavaClass(mapeo) + "(" + getJavaClass(mapeo) + " dto, StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\tBusinessFactory.getIntBus" + getJavaClass(mapeo) + "().update(dto, session);").append(SALTO);
						impBus.append("\t}").append(SALTO);
						impBus.append("\t").append(SALTO);
						impBus.append("\tpublic static void delete" + getJavaClass(mapeo) + "(" + getJavaClass(mapeo) + " dto, StatelessSession session) throws BusinessException {").append(SALTO);
						impBus.append("\t\tBusinessFactory.getIntBus" + getJavaClass(mapeo) + "().delete(dto, session);").append(SALTO);
						impBus.append("\t}").append(SALTO);

						impBus.append("\tprivate static IntBus" + getJavaClass(mapeo) + " intBus" + getJavaClass(mapeo) + ";").append(SALTO);
						impBus.append("\tpublic static IntBus" + getJavaClass(mapeo) + " getIntBus" + getJavaClass(mapeo) + "() {").append(SALTO);
						impBus.append("\t\treturn (intBus" + getJavaClass(mapeo) + " == null) ? intBus" + getJavaClass(mapeo) + " = new ImpBus" + getJavaClass(mapeo) + "() : intBus" + getJavaClass(mapeo) + ";").append(SALTO);
						impBus.append("\t}").append(SALTO);

						generateFile(destino, impBus.toString(),
								customFields, customTypes, customMethods,
								importCustom, serialVersionUID, excludeCommonFunctions);
					}
				}
			}

			System.out.println(mapeos);
			System.out.println(mapeoNumero);
		} catch (Exception e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}

	/**
	 * Funcion para generar un archivo java.
	 * @destino archivo a generar
	 * @param content Contenido de la clase. En caso que el archivo exista previamente se le incrustan las formulas, campos no mapeados y metodos personalizados.
	 */
	private void generateFile (File destino, String content,
			Map<String, StringBuffer> customFields, Map<String, StringBuffer> customTypes, Map<String, StringBuffer> customMethods,
			StringBuffer importCustom, StringBuffer serialVersionUID, List<String> excludeCommonFunctions) {
		BufferedWriter bw = null;
		try {
			content = content.replace("{@CustomImports}", importCustom);

			if (customFields.size() > 0) {
				List<String> fields = new LinkedList<>();
				for (String s : customFields.keySet()) {
					fields.add(s);
				}
				Collections.sort(fields);

				StringBuffer tmp = new StringBuffer(SALTO);
				for (String s : fields) {
					tmp.append(customFields.get(s).toString());
				}

				content = content.replace("{@Formula}", tmp);
			} else {
				content = content.replace("{@Formula}", SALTO);
			}

			if (serialVersionUID.length() > 0) {
				content = content.replace("{@SerialVersionUID}", "	private static final long serialVersionUID = " + serialVersionUID.toString() + ";");
			} else {
				content = content.replace("{@SerialVersionUID}", "	private static final long serialVersionUID = -" + new Date().getTime() + CommonFunctions.lpad(new Integer(mapeoNumero).toString(), "0", 5) + "L;");
			}

			int pos = 0;
			for (String s : excludeCommonFunctions) {
				content = content.replace("{ExcludeCommonFunctions." + s + "}", "	@ExcludeCommonFunctions" + SALTO);
				pos = content.indexOf("CommonFunctions.replaceAccentedChars(" + s);
				if (pos >= 0) {
					try {
						content = content.substring(0, pos) + s + content.substring(content.indexOf(";", pos + 1));
					} catch (Exception e) {
						System.out.println(s);
						System.out.println(pos);
						//					System.out.println(content);
						e.printStackTrace();
					}
				}
			}
			pos = content.indexOf("{ExcludeCommonFunctions.");
			while (pos >= 0) {
				content = content.substring(0, pos) + content.substring(content.indexOf("}", pos + 1) + 1);

				pos = content.indexOf("{ExcludeCommonFunctions.");
			}

			bw = new BufferedWriter(new FileWriter(destino, true));
			bw.write(content);

			if (customMethods.size() > 0) {
				List<String> methods = new LinkedList<>();
				for (String s : customMethods.keySet()) {
					methods.add(s);
				}
				Collections.sort(methods);
				for (String s : methods) {
					bw.write(customMethods.get(s).toString());
				}
			}

			bw.write("}");
			bw.newLine();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.flush();
					bw.close();
				}
				bw=null;
				destino=null;
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * Obtener nombre de la clase a partir del nombre de la tabla o un campo de la base de datos.
	 * @param str Nombre de la tabla o campo.
	 * @return Nombre de la clase resultante.
	 */
	private String getJavaClass(String str) {
		String ret = "";
		for (int i = 0; i < str.length(); i++) {
			if (i == 0) {
				ret += str.toUpperCase().charAt(i);
			}
			else if (str.charAt(i) == '_') {
				i++;
				ret += str.toUpperCase().charAt(i);
			}
			else {
				ret += str.toLowerCase().charAt(i);
			}
		}
		return ret;
	}

	/**
	 * Obtener nombre de un objeto a partir del nombre de la tabla o un campo de la base de datos.
	 * @param str Nombre de la tabla o campo.
	 * @return Nombre del objeto resultante.
	 */
	private String getJavaName(String str) {
		String ret = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '_') {
				i++;
				ret += str.toUpperCase().charAt(i);
			}
			else {
				ret += str.toLowerCase().charAt(i);
			}
		}
		return ret;
	}

	/**
	 *
	 * Tipo de dato en java segun el de la base de datos.
	 * @param oracle
	 * @param mysql
	 * @param postgres
	 * @param tipoDato Tipo de dato en la base.
	 * @param length Longitud del campo.
	 * @param precision Precision del campo.
	 * @param scale Escala del campo.
	 * @return Tipo de dato en java.
	 */
	private String getTipoDato(boolean oracle, boolean mysql, boolean postgres, String tipoDato, Long length, Long precision, Long scale) {
		if (oracle) {
			tipoDato = tipoDato.toUpperCase();
			if ("NUMBER".equals(tipoDato)
					|| "NUMERIC".equals(tipoDato)) {
				if (scale > 0) {
					return "Double";
				}

				return "Long";
			}
			else if ("CHAR".equals(tipoDato) && length == 1L) {
				return "Character";
			}
			else if ("CHAR".equals(tipoDato) || "CLOB".equals(tipoDato) || "VARCHAR2".equals(tipoDato)) {
				return "String";
			}
			else if ("DATE".equals(tipoDato)) {
				return "Date";
			}
			else if ("BLOB".equals(tipoDato)) {
				return "byte[]";
			}
			else {
				return tipoDato;
			}
		}
		else if (mysql) {
			if ("BIGINT".equals(tipoDato)
					|| "DECIMAL".equals(tipoDato)
					|| "INT".equals(tipoDato)) {
				return "Long";
			}
			else if ("CHAR".equals(tipoDato) && length == 1L) {
				return "Character";
			}
			else if ("CHAR".equals(tipoDato) || "VARCHAR".equals(tipoDato)) {
				return "String";
			}
			else if ("DATE".equals(tipoDato)) {
				return "Date";
			}
			else {
				return tipoDato;
			}
		}
		else if (postgres) {
			tipoDato = tipoDato.toUpperCase();
			if ("NUMERIC".equals(tipoDato)) {
				return "Long";
			}
			else if ("CHARACTER VARYING".equals(tipoDato)) {
				return "String";
			}
			else {
				return tipoDato;
			}
		}
		else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	/**
	 * Recorre el archivo de manera secuencial para obtener formulas, campos transient y metodos personalizados.
	 * @param file Archivo a analizar.
	 * @param customFields Retorno de campos.
	 * @param customMethods Retorno de tipos.
	 * @param customMethods Retorno de metodos.
	 * @param importCustom Retorno de imports.
	 * @param serialVersionUID
	 * @param excludeCommonFunctions
	 * @throws Exception
	 */
	private void loadCustomMethods(File file, Map<String, StringBuffer> customFields, Map<String, StringBuffer> customTypes, Map<String, StringBuffer> customMethods,
			StringBuffer importCustom, StringBuffer serialVersionUID, List<String> excludeCommonFunctions) throws Exception {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String customMethod = null;
			String linea = null;
			String parentField = null;
			String parentMethod = null;
			int pos = 0;
			int llaves = -1;
			while ((linea = br.readLine()) != null) {
				if (linea.startsWith("/*")) {
					pos = linea.indexOf("*/");
					while (pos < 0) {
						linea = br.readLine();
						pos = linea.indexOf("*/");
					}
					continue;
				}
				if (linea.contains(" enum ")) {
					continue;
				}

				pos = linea.indexOf("import ");
				if (pos >= 0) {
					importCustom.append(linea);
					importCustom.append(SALTO);
				}

				if (parentMethod != null) {
					pos = linea.indexOf("{");
					if (pos > 0) {
						if (llaves == -1) {
							llaves = 0;
						}

						llaves++;
					}
					pos = linea.indexOf("}");
					if (pos > 0) {
						llaves--;
					}
				}

				pos = linea.indexOf("serialVersionUID");
				if (pos >= 0) {
					pos = linea.indexOf(" long ");
					serialVersionUID.append(linea.substring(pos + 25, linea.length() - 1));
				}

				pos = linea.indexOf("@Formula");
				if (pos >= 0) {
					pos = linea.indexOf("\"");
					parentField = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
					parentField = CommonFunctions.lpad(new Integer (customFields.size()).toString(), "0", 3) + parentField;
					if (!customFields.containsKey(parentField)) {
						customFields.put(parentField, new StringBuffer());
					}
					importCustom.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
				}
				pos = linea.indexOf("@Transient");
				if (pos >= 0) {
					pos = linea.indexOf("\"");
					parentField = "";
					parentField = CommonFunctions.lpad(new Integer (customFields.size()).toString(), "0", 3) + parentField;
					if (!customFields.containsKey(parentField)) {
						customFields.put(parentField, new StringBuffer());
					}
					importCustom.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
				}
				pos = linea.indexOf("@CustomType");
				if (pos >= 0) {
					pos = linea.indexOf("\"");
					parentField = "";
					parentField = CommonFunctions.lpad(new Integer (customTypes.size()).toString(), "0", 3) + parentField;
					customTypes.put(parentField, new StringBuffer(linea.substring(pos+1, linea.indexOf("\"", pos + 1))));
				}
				pos = linea.indexOf("@ExcludeCommonFunctions");
				if (pos >= 0) {
					linea = br.readLine();
					//ignoro la linea
					if (linea.contains("@Column")) {
						linea = br.readLine();
					}
					linea = linea.replaceAll("\t", "");

					String[] tmp = linea.split(" ");
					if (tmp.length == 3) {
						tmp[2] = tmp[2].substring(0, tmp[2].length() - 1);
						excludeCommonFunctions.add(tmp[2]);
					}
					importCustom.append("import ar.com.thinksoft.utils.ExcludeCommonFunctions;").append(SALTO);
				}
				if (parentField != null) {
					try {
						if (customFields.containsKey(parentField)) {
							customFields.get(parentField).append(linea).append(SALTO);
						}
					} catch (Exception e) {
						System.out.println(linea);
						throw e;
					}

					while ((linea = br.readLine()) != null) {
						pos = linea.indexOf("public ");
						if (pos >= 0) {
							parentField = null;
							break;
						}
						pos = linea.indexOf("private ");
						if (pos >= 0) {
							String tmp = linea.substring(linea.lastIndexOf(" ") + 1, linea.length() - 1);
							customTypes.put(tmp, customTypes.get(parentField));
							customTypes.remove(parentField);
						}

						pos = linea.replaceAll("\t", "").indexOf("@");
						if (pos == 0) {
							parentField = null;

							pos = linea.indexOf("@Formula");
							if (pos >= 0) {
								pos = linea.indexOf("\"");
								parentField = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
								parentField = CommonFunctions.lpad(new Integer (customFields.size()).toString(), "0", 3) + parentField;
								if (!customFields.containsKey(parentField)) {
									customFields.put(parentField, new StringBuffer());
								}
								customFields.get(parentField).append(linea).append(SALTO);
								importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
							}
							pos = linea.indexOf("@Transient");
							if (pos >= 0) {
								pos = linea.indexOf("\"");
								parentField = "";
								parentField = CommonFunctions.lpad(new Integer (customFields.size()).toString(), "0", 3) + parentField;
								if (!customFields.containsKey(parentField)) {
									customFields.put(parentField, new StringBuffer());
								}
								customFields.get(parentField).append(linea).append(SALTO);
								importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
							}
							pos = linea.indexOf("@CustomType");
							if (pos >= 0) {
								pos = linea.indexOf("\"");
								parentField = "";
								parentField = CommonFunctions.lpad(new Integer (customTypes.size()).toString(), "0", 3) + parentField;
								customTypes.put(parentField, new StringBuffer(linea.substring(pos+1, linea.indexOf("\"", pos + 1))));
							}
							pos = linea.indexOf("@ExcludeCommonFunctions");
							if (pos >= 0) {
								linea = br.readLine();
								//ignoro la linea
								if (linea.contains("@Column")) {
									linea = br.readLine();
								}
								linea = linea.replaceAll("\t", "");

								String[] tmp = linea.split(" ");
								if (tmp.length == 3) {
									tmp[2] = tmp[2].substring(0, tmp[2].length() - 1);
									excludeCommonFunctions.add(tmp[2]);
								}
								importCustom.append("import ar.com.thinksoft.utils.ExcludeCommonFunctions;").append(SALTO);
							}

							break;
						}

						if (linea.startsWith("/*")) {
							pos = linea.indexOf("*/");
							while (pos < 0) {
								linea = br.readLine();
								pos = linea.indexOf("*/");
							}
							continue;
						}
						if (parentField != null) {
							if (customFields.containsKey(parentField)) {
								customFields.get(parentField).append(linea).append(SALTO);
							}
						}

						pos = linea.indexOf("public ");
						if (llaves == 0) {
							customFields.get(parentField).append(SALTO);
							parentField = null;
							break;
						}
					}
				}

				pos = linea.indexOf("@CustomConstructor");
				if (pos >= 0) {
					pos = linea.indexOf("\"");
					parentMethod = linea.substring(pos + 1, linea.indexOf("\"", pos + 2));
					if (!customMethods.containsKey(parentMethod)) {
						customMethods.put(parentMethod, new StringBuffer());
					}
				}

				pos = linea.indexOf("@CustomMethod");
				if (pos >= 0) {
					pos = linea.indexOf("\"");
					try {
						if(pos != -1) {
							parentMethod = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
						} else {
							parentMethod = "";
						}
						parentMethod = CommonFunctions.lpad(new Integer (customMethods.size()).toString(), "0", 3) + parentMethod;
					} catch (Exception e) {
						throw e;
					}
					if (!customMethods.containsKey(parentMethod)) {
						customMethods.put(parentMethod, new StringBuffer());
					}
					//					customMethods.get(parentMethod).append(linea).append(SALTO);
					importCustom.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
				}
				if (parentMethod != null) {
					pos = linea.indexOf("public ");

					if (pos >= 0) {
						String[] array = linea.replaceAll("\\p{Space}+", " ").split(" ");

						if (array[2].contains("(")
								|| array[3].startsWith("(")) {
							if (array[2].contains("(")) {
								customMethod = array[2].split("\\(")[0];
							} else {
								customMethod = array[2];
							}
						}
						else if (array[3].contains("(")
								|| array[4].startsWith("(")) {
							if (array[3].contains("(")) {
								customMethod = array[3].split("\\(")[0];
							} else {
								customMethod = array[3];
							}
						}
						else if (array[4].contains("(")
								|| array[5].startsWith("(")) {
							if (array[4].contains("(")) {
								customMethod = array[4].split("\\(")[0];
							} else {
								customMethod = array[4];
							}
						}
						else {
							throw new Exception("caldito seas 3 " + linea);
						}

						//						System.out.println("\t\t" + f.getName().replace(".java", "") + " " + parentMethod + "--->" + customMethod);
						customMethods.get(parentMethod).append(linea).append(SALTO);

						while ((linea = br.readLine()) != null) {
							if (parentMethod != null) {
								pos = linea.indexOf("{");
								if (pos > 0) {
									if (llaves == -1) {
										llaves = 0;
									}

									llaves++;
								}
								pos = linea.indexOf("}");
								if (pos > 0) {
									llaves--;
								}
							}

							pos = linea.indexOf("public ");
							if (pos >= 0) {
								parentMethod = null;
								customMethod = null;
								break;
							}

							pos = linea.replaceAll("\t", "").indexOf("@");
							if (pos == 0) {
								parentMethod = null;
								customMethod = null;

								pos = linea.indexOf("@CustomConstructor");
								if (pos >= 0) {
									pos = linea.indexOf("\"");
									parentMethod = linea.substring(pos + 1, linea.indexOf("\"", pos + 2));
									if (!customMethods.containsKey(parentMethod)) {
										customMethods.put(parentMethod, new StringBuffer());
									}
								}

								pos = linea.indexOf("@CustomMethod");
								if (pos >= 0) {
									pos = linea.indexOf("\"");
									parentMethod = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
									parentMethod = CommonFunctions.lpad(new Integer (customMethods.size()).toString(), "0", 3) + parentMethod;
									if (!customMethods.containsKey(parentMethod)) {
										customMethods.put(parentMethod, new StringBuffer());
									}
									customMethods.get(parentMethod).append(linea).append(SALTO);
									importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
								}

								break;
							}

							if (linea.startsWith("/*")) {
								pos = linea.indexOf("*/");
								while (pos < 0) {
									linea = br.readLine();
									pos = linea.indexOf("*/");
								}
								continue;
							}
							if (parentMethod != null) {
								customMethods.get(parentMethod).append(linea).append(SALTO);
							}

							pos = linea.indexOf("public ");
							if (llaves == 0) {
								customMethods.get(parentMethod).append(SALTO);

								parentMethod = null;
								customMethod = null;
								break;
							}
						}
					}
					else {
						customMethods.get(parentMethod).append(linea).append(SALTO);
					}
				}
			}
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

}
