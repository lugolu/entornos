package ar.com.thinksoft.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.com.thinksoft.common.Metodo;

public class CreateBusinessTests {

	private final String SALTO = "\n";

	/**
	 * Genera tests de business
	 * @param java
	 * @param path
	 * @throws Exception
	 */
	public CreateBusinessTests(boolean java, String path) throws Exception {
		generaTestsBusiness(java, path);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Genera tests de business
	 * @param java
	 * @param path
	 * @throws Exception
	 */
	private void generaTestsBusiness(boolean java, String path) throws Exception {
		String pathOrigen = "";
		String pathDestino = "";

		if (java) {
			pathOrigen = path.replace("-TEST", "").split("/bin")[0]+"/src/ar/com/thinksoft/business";
			pathDestino = path.split("/bin")[0]+"/src/ar/com/thinksoft/business/business/";
		}
		else {
			pathOrigen = path.split("/target")[0]+"/src/main/java/ar/com/thinksoft/business";
			pathDestino = path.split("/target")[0]+"/src/test/java/ar/com/thinksoft/business/business/";
		}

		File fOrigen = new File(pathOrigen);
		if (!fOrigen.exists()) {
			System.out.println(fOrigen.getAbsolutePath());
			throw new Exception("caldito seas 1");
		}
		File fDestino = new File(pathDestino);
		if (!fDestino.exists()) {
			fDestino.mkdirs();
		}

		List<String> business = new LinkedList<String>();

		String clase = null;
		if (java) {
			clase = (pathOrigen + "/BusinessFactory").split("src")[1].replaceAll("/", ".").replaceAll("\\\\", ".");
		}
		else {
			clase = (pathOrigen + "/BusinessFactory").split("java")[1].replaceAll("/", ".").replaceAll("\\\\", ".");
		}
		clase = clase.substring(1, clase.length());

		Method[] methods = Class.forName(clase).getMethods();
		for (Method method : methods) {
			business.add(method.getName());
		}

		File[] archivosOrigen = fOrigen.listFiles();
		for (File x : archivosOrigen) {
			if (!x.isDirectory()
					|| "generix".equals(x.getName())) {
				System.out.println("\tskip\t" + x.getName() + " is ignored");
				continue;
			}
			//			if (!"admision".equals(x.getName()) &&
			//					!"analisisDebitos".equals(x.getName()) &&
			//					!"anunciador".equals(x.getName()) &&
			//					!"atencionTurno".equals(x.getName()) &&
			//					!"autogestion".equals(x.getName()) &&
			//					!"cobranzaConvenios".equals(x.getName()) &&
			//					!"enfermeria".equals(x.getName()) &&
			//					!"envioMailSms".equals(x.getName()) &&
			//					!"imagenologia".equals(x.getName()) &&
			//					!"infectologia".equals(x.getName()) &&
			//					!"informes".equals(x.getName()) &&
			//					!"interfaces".equals(x.getName()) &&
			//					!"internacionDomiciliaria".equals(x.getName()) &&
			//					!"obstetricia".equals(x.getName()) &&
			//					!"recepcion".equals(x.getName()) &&
			//					!"seguridad".equals(x.getName())) {
			//				System.out.println("\tskip\t" + x.getName() + " is ignored");
			//				continue;
			//			}
			File[] archivos = new File (x.getAbsolutePath()+"/implementations").listFiles();
			if (archivos == null) {
				System.out.println("\tno files\t" + x.getName());
				continue;
			}
			for (File f : archivos) {
				if (f.isDirectory()) {
					System.out.println("\tskip\t" + f.getName() + " is directory");
					continue;
				}
				//				System.out.println(f.getName());

				BufferedWriter bw = null;
				FileReader fr = null;
				BufferedReader br = null;
				File destino = null;
				try {
					fr = new FileReader(f);
					br = new BufferedReader(fr);

					if (java) {
						clase = f.getAbsolutePath().split("src")[1].replace("java", "").replaceAll("\\\\", ".");
					}
					else {
						clase = f.getAbsolutePath().split("java")[1].replace("java", "").replaceAll("\\\\", ".");
					}
					clase = clase.substring(1, clase.length() - 1);

					Class c;
					try {
						c = Class.forName(clase);
					} catch (Exception e1) {
						throw e1;
					}

					IgnoreTestClass ignore = (IgnoreTestClass) c.getAnnotation(IgnoreTestClass.class);
					if (ignore != null) {
						System.out.println("\tskip\t" + f.getName().replaceAll(".java", "") + " IgnoreTestClass");
						continue;
					}

					String remapClassName = null;
					RemapTest remapClass1 = (RemapTest) c.getAnnotation(RemapTest.class);
					if (remapClass1 != null) {
						remapClassName = remapClass1.name();
						//							System.out.println("\t\t" + f.getName().replace(".java", "") + "--->" + remapClassName);
					}

					StringBuffer importCustom = new StringBuffer();
					StringBuffer m = new StringBuffer();
					StringBuffer imports = new StringBuffer();
					StringBuffer objects = new StringBuffer();
					StringBuffer objectsName = new StringBuffer();

					Map<String, StringBuffer> customTests = new HashMap<String, StringBuffer>();
					Map<String, String> ignoreControlRetorno = new HashMap<String, String>();

					List<String> metodosComplejos = new LinkedList<String>();

					destino = new File(pathDestino+x.getName()+"/Test"+f.getName());
					String archivo = "Test"+f.getName().replace(".java", "");
					if (destino.exists()) {
						CreateTests.loadCustomTests(destino, customTests, ignoreControlRetorno, importCustom, metodosComplejos);
						destino.delete();
					}
					else {
						File dir = new File(pathDestino+x.getName());
						if (!dir.exists()) {
							dir.mkdir();
						}
					}

					String linea = null;
					int pos = -1;
					boolean empezoClase = false;
					while ((linea = br.readLine()) != null && !empezoClase) {
						linea = linea.replaceAll("\t", "").replaceAll("\\p{Space}+", " ").trim();
						if (linea.length() == 0
								|| linea.startsWith("//")) {
							continue;
						}
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
							if (linea.contains("dtos")) {
								imports.append(linea);
								imports.append(SALTO);
							}
						}
						empezoClase = empezoClase || linea.contains("class ");
					}

					while ((linea = br.readLine()) != null) {
						linea = linea.replaceAll("\t", "").replaceAll("\\p{Space}+", " ").trim();
						if (linea.length() == 0
								|| linea.startsWith("//")) {
							continue;
						}
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

						if ((linea.contains("private ")
								|| linea.contains("public "))
								&& !linea.contains(" Logger l")
								&& !linea.contains(" SimpleDateFormat ")
								&& !linea.contains("final int CANT_MAX_ERRORES")
								&& !linea.contains("private EquipoTareaProgramada ")) {
							//							String metodo = null;
							//							String[] array = linea.replaceAll("\\p{Space}+", " ").split(" ");
							//
							//							try {
							//								if (array[2].contains("(")
							//										|| array[3].startsWith("(")) {
							//									if (array[2].contains("(")) {
							//										metodo = array[2].split("\\(")[0];
							//									} else {
							//										metodo = array[2];
							//									}
							//								}
							//								else if (array[3].contains("(")
							//										|| array[4].startsWith("(")) {
							//									if (array[3].contains("(")) {
							//										metodo = array[3].split("\\(")[0];
							//									} else {
							//										metodo = array[3];
							//									}
							//								}
							//								else {
							//									throw new Exception("caldito seas 3 " + linea);
							//								}
							//							} catch (Exception e) {
							//								throw e;
							//							}

							while ((linea = br.readLine()) != null) {
								linea = linea.replaceAll("\t", "").replaceAll("\\p{Space}+", " ").trim();
								if (linea.length() == 0
										|| linea.startsWith("//")) {
									continue;
								}
								if (linea.startsWith("/*")) {
									pos = linea.indexOf("*/");
									while (pos < 0) {
										linea = br.readLine();
										pos = linea.indexOf("*/");
									}
									continue;
								}

								if (linea.contains("catch")) {
									break;
								}
							}
						}
					}

					Method[] me = c.getDeclaredMethods();
					List<Metodo> metodos = new LinkedList<Metodo>();
					for (Method method : me) {
						//						System.out.println("\t" + method.getName() + "-" + method.getDeclaringClass());
						Metodo met = new Metodo();
						met.setPublico(Modifier.isPublic(method.getModifiers()));
						met.setName(method.getName());
						met.setIgnoreMethod(method.getAnnotation(IgnoreTest.class) != null);
						RemapTest remapMethod = method.getAnnotation(RemapTest.class);
						if (remapMethod != null) {
							met.setRemapName(remapMethod.name());
						}
						try {
							met.setParameters(method.getParameters());
						} catch (Exception e) {
							e.printStackTrace();
						}
						met.setReturnType(method.getReturnType().getName());
						met.setGenericReturnType(method.getGenericReturnType());
						metodos.add(met);
					}

					Metodo met = new Metodo();
					met.setPublico(true);
					met.setName(archivo);
					met.setParameters(new Parameter[] {});
					met.setReturnType("void");
					met.setGenericReturnType(null);
					metodos.add(met);

					CommonFunctions.sortList(metodos, true, "Name", "RemapName");
					boolean esConstructor;
					for (Metodo method : metodos) {
						esConstructor = method.getName().equals(archivo);

						if (!method.isPublico()) {
							//							System.out.println("\tskip\t" + f.getName().replaceAll(".java", "") + "." + method.getName() + " is not public");
							continue;
						}

						if (method.isIgnoreMethod()) {
							System.out.println("\tskip\t" + f.getName().replaceAll(".java", "") + "." + method.getName());
							continue;
						}

						String remapMethodName = null;
						if (method.getRemapName() != null) {
							remapMethodName = method.getRemapName();
							//							System.out.println("\t\t" + f.getName().replace(".java", "") + "." + method.getName() + "--->" + remapMethodName);
						}

						String metodo = method.getName();
						String metodoTest = null;

						if (remapMethodName == null) {
							metodoTest = "test" + metodo.substring(0, 1).toUpperCase() + metodo.substring(1);
						}
						else {
							metodoTest = "test" + remapMethodName.substring(0, 1).toUpperCase() + remapMethodName.substring(1);
						}

						objects = new StringBuffer();
						objectsName = new StringBuffer();

						boolean skipMethod = false;
						for (Parameter p : method.getParameters()) {
							CreateTests.objetear(clase, method, p,
									objects, objectsName,
									importCustom, true, true);

							if ("java.io.Serializable".equals(p.getType().getName())) {
								skipMethod = true;
								break;
							}

							if (p.getType().isArray()) {
								continue;
							}
							if (p.getType().isInterface()) {
								continue;
							}
							if (p.getType().isPrimitive()) {
								continue;
							}

							if ("Boolean".equals(p.getType().getSimpleName())
									|| "Character".equals(p.getType().getSimpleName())
									|| "Double".equals(p.getType().getSimpleName())
									|| "Integer".equals(p.getType().getSimpleName())
									|| "Long".equals(p.getType().getSimpleName())) {
								continue;
							}

							try {
								if (p.getType().isEnum()) {
									if ("AmbIntTodos".equals(p.getType().getSimpleName())) {
										importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
									}
									//									else if ("EstadoParte".equals(p.getType().getSimpleName())) {
									//										importCustom.append("import " + clase + "." + p.getType().getName() + ";").append(SALTO);
									//									}
									//									else if ("FormaPrescripcion".equals(p.getType().getSimpleName())) {
									//										importCustom.append("import " + clase.replace(".implementations", ".interfaces").replace(".Imp", ".Int") + "." + p.getType().getSimpleName() + ";").append(SALTO);
									//									}
									//									else if ("MODULO_APLICACION_ENUM".equals(p.getType().getSimpleName())) {
									//										importCustom.append("import ar.com.thinksoft.dtos.configuracion.ModuloAplicacionDf.MODULO_APLICACION_ENUM;").append(SALTO);
									//									}
									//									else if ("TIPO_BUSQUEDA".equals(p.getType().getSimpleName())) {
									//										importCustom.append("import ar.com.thinksoft.dtos.farmacia.TmpBusquedaItem.TIPO_BUSQUEDA;").append(SALTO);
									//									}
									//									else {
									//										System.out.println("que se io " + p);
									//									}
									continue;
								}
							} catch (Exception e) {
								System.out.println("que hago " + p.getType());
								//									e.printStackTrace();
							}
						}

						if (skipMethod) {
							//							System.out.println("skip business method " + clase + "." + method.getName());
							continue;
						}

						String obj = objectsName.toString();
						if (obj.endsWith(", ")) {
							obj = obj.substring(0, obj.lastIndexOf(","));
						}

						if(customTests.containsKey(metodoTest)) {
							m.append(customTests.get(metodoTest));
							continue;
						}

						if (metodoTest.startsWith("testAnula")
								|| metodoTest.startsWith("testCancela")
								|| metodoTest.startsWith("testDelete")
								|| metodoTest.startsWith("testElimina")
								|| metodoTest.startsWith("testGenera")
								|| metodoTest.startsWith("testGuarda")
								|| metodoTest.startsWith("testInsert")
								|| metodoTest.startsWith("testProcess")
								|| metodoTest.startsWith("testUpdate")) {
							m.append("	public void " + metodoTest + "() {").append(SALTO);
							m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
							m.append("			return;").append(SALTO);
							m.append("		}").append(SALTO).append(SALTO);

							m.append("		Commons.logInfoSuccess(clase, testName);").append(SALTO).append(SALTO);

							m.append("		assertNull(error);").append(SALTO);
							m.append("	}").append(SALTO).append(SALTO);

							continue;
						}

						if(ignoreControlRetorno.containsKey(metodoTest)) {
							m.append("\t@IgnoreControlRetorno(motivo=\"" + ignoreControlRetorno.get(metodoTest) + "\")").append(SALTO);
						}

						m.append("	public void " + metodoTest + "() {").append(SALTO);
						m.append("		if (Commons.ignoreFailed (clase, testName)) {").append(SALTO);
						//				m.append("			Commons.logInfo (\"ignoreFailed \" + clase + \".test" + getJavaClass(k) + "\");").append(SALTO);
						m.append("			return;").append(SALTO);
						m.append("		}").append(SALTO);
						m.append(SALTO);
						m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
						//				m.append("			Commons.logInfo (\"ignoreSuccess \" + clase + \".test" + getJavaClass(k) + "\");").append(SALTO);
						m.append("			return;").append(SALTO);
						m.append("		}").append(SALTO);
						m.append(SALTO);
						if (esConstructor) {
							m.append("		try {").append(SALTO);
							m.append("			new " + clase + "();").append(SALTO);
						}
						else {
							m.append("		try {").append(SALTO);
							m.append("			StatelessSession session = null;").append(SALTO);
							m.append("			try {").append(SALTO);
							m.append("				session = CommonsTest.getSession();").append(SALTO);

							remapMethodName = null;

							if (remapClassName == null) {
								remapClassName = CreateTests.traducirInt(business, f);
							}

							m.append(objects);
							m.append("				BusinessFactory.get" + remapClassName + "()." + metodo + "(" + obj + ");").append(SALTO);
							//controlar session close
							m.append("				try {").append(SALTO);
							m.append("					session.createCriteria(PersonalNotMapped.class);").append(SALTO);
							m.append("					throw new BusinessException(\"Session is not closed.\", SeverityBundle.WARN);").append(SALTO);
							m.append("				} catch (Exception e) {").append(SALTO);
							m.append("					if (!(e instanceof SessionException)) {").append(SALTO);
							m.append("						throw new BusinessException(\"Session is not closed.\", SeverityBundle.WARN);").append(SALTO);
							m.append("					}").append(SALTO);
							m.append("				}").append(SALTO);

							m.append("			} catch (Exception e) {");
							m.append(SALTO);
							m.append("				HandlerException.getInstancia().treateException(e,getClass());");
							m.append(SALTO);
							m.append("			} finally {");
							m.append(SALTO);
							m.append("				try {session.close();} catch (Exception e) {}");
							m.append(SALTO);
							m.append("			}");
							m.append(SALTO);
						}
						m.append("			Commons.logInfoSuccess(clase, testName);").append(SALTO);
						m.append("		} catch (Exception e) {").append(SALTO);
						m.append("			if (e instanceof NullPointerException) {").append(SALTO);
						m.append("				error = \"NullPointerException\";").append(SALTO);
						m.append("			}").append(SALTO);
						m.append("			else {").append(SALTO);
						m.append("				error = e.getMessage();").append(SALTO);
						m.append("			}").append(SALTO);
						m.append("			Commons.logWarn (clase, testName, error, e);").append(SALTO);
						m.append("		}");
						m.append(SALTO);
						m.append(SALTO);
						m.append("		assertNull(error);");
						m.append(SALTO);
						m.append("	}");
						m.append(SALTO);
						m.append(SALTO);
					}

					if (!destino.exists()) {
						try {
							destino.createNewFile();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					StringBuffer s = new StringBuffer();
					s.append("package ar.com.thinksoft.business.business." + x.getName() + ";").append(SALTO).append(SALTO);

					s.append("import java.io.Serializable;").append(SALTO);
					s.append("import java.util.Date;").append(SALTO);
					s.append("import java.util.HashMap;").append(SALTO);
					s.append("import java.util.LinkedList;").append(SALTO);
					s.append("import java.util.List;").append(SALTO);
					s.append("import java.util.Map;").append(SALTO);
					s.append(SALTO);

					s.append("import org.hibernate.SessionException;").append(SALTO);
					s.append("import org.hibernate.StatelessSession;").append(SALTO);
					s.append("import org.hibernate.criterion.Restrictions;").append(SALTO).append(SALTO);
					s.append("import org.junit.experimental.categories.Category;").append(SALTO).append(SALTO);

					s.append("import ar.com.thinksoft.common.Business;").append(SALTO);
					s.append("import ar.com.thinksoft.common.Commons;").append(SALTO);
					s.append("import ar.com.thinksoft.common.CustomTest;").append(SALTO);
					s.append("import ar.com.thinksoft.common.IgnoreControlRetorno;").append(SALTO);

					s.append(imports);

					s.append("import ar.com.thinksoft.business.BusinessFactory;").append(SALTO);
					s.append("import ar.com.thinksoft.dtos.configuracion.PersonalNotMapped;").append(SALTO);
					s.append("import ar.com.thinksoft.exception.BusinessException;").append(SALTO);
					s.append("import ar.com.thinksoft.jdbc.HibernateSessionFactory;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
					s.append(importCustom);
					s.append("import ar.com.thinksoft.utils.CommonFunctions;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.ConstantsTest;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.DateCommonFunctions;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.CommonsTest;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.HandlerException;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.MessageBundle;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.SeverityBundle;").append(SALTO);
					s.append("import junit.framework.Test;").append(SALTO);
					s.append("import junit.framework.TestCase;").append(SALTO);
					s.append("import junit.framework.TestSuite;").append(SALTO).append(SALTO);

					s.append("@Category(Business.class)").append(SALTO);
					s.append("public class " + archivo + " extends TestCase {").append(SALTO).append(SALTO);

					s.append("	private static final String clase = \"ar.com.thinksoft.business.business." + x.getName() + "." + archivo + "\";").append(SALTO).append(SALTO);

					s.append("	String error = null;").append(SALTO).append(SALTO);

					s.append("	private String testName = null;").append(SALTO).append(SALTO);

					if("TestImpBusInterfaceImagen".equals(archivo)
							|| "TestImpBusRecepcionInterfaceKern".equals(archivo)) {
						s.append("	private String path = null;").append(SALTO).append(SALTO);
					}

					s.append("	public " + archivo + "( String testName )  {").append(SALTO);
					s.append("		super( testName );").append(SALTO);
					s.append("	}").append(SALTO).append(SALTO);

					s.append("	@Override").append(SALTO);
					if(customTests.containsKey("setUp")) {
						s.append(customTests.get("setUp"));
					}
					else {
						s.append("	protected void setUp() throws Exception {").append(SALTO);
						s.append("		super.setUp();").append(SALTO);
						s.append("		HibernateSessionFactory.getInstance();").append(SALTO).append(SALTO);
						s.append("		testName = getName();").append(SALTO);
						s.append("	}").append(SALTO).append(SALTO);
					}

					s.append("	public static Test suite() {").append(SALTO);
					s.append("		return new TestSuite( " + archivo + ".class );").append(SALTO);
					s.append("	}").append(SALTO).append(SALTO);

					s.append(m);

					s.append("	@Override").append(SALTO);
					s.append("	protected void tearDown() throws Exception {").append(SALTO);
					s.append("		super.tearDown();").append(SALTO).append(SALTO);

					s.append("		CustomTest a = " + archivo + ".class.getMethod(testName).getAnnotation(CustomTest.class);").append(SALTO);
					s.append("		Commons.logTestResult(clase + \".\" + testName, (a != null ? a.motivo() : null));").append(SALTO);
					s.append("	}").append(SALTO).append(SALTO);
					s.append("}").append(SALTO);

					bw = new BufferedWriter(new FileWriter(destino, true));
					bw.write(s.toString());
					bw.newLine();
				} catch (Exception ex) {
					ex.printStackTrace();
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
		}
	}

}
