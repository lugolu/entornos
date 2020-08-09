package ar.com.thinksoft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.Metodo;

public class CreateTests {

	private static List<String> ignoreRetorno;
	private static List<String> revisar;

	public static final String SALTO = "\n";

	/**
	 * Crear tests del codigo.<br />
	 * Posibilidad de personalizar el contenido de los tests, ya se por sobrecarga, parametros o no testear.<br />
	 * Se generan tests de validador online para cada convenio que se encuentre parametrizado. Segun la parametrizacion se generan tests de elegibilidad, grupo familiar y prestacional.<br />
	 * Se generan tests de los metodos de la capa de persistencia.
	 * Se generan tests de los dtos. Por defecto solo los booleanos y equals (de encontrarse redefinido). En caso de mapeos con annotations se generan los metodos insert, update y selectEqual.<br />
	 * @param java Segun sea java o maven se usan direntes paths.
	 */
	public CreateTests(boolean java) {
		try {
			String path = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");

			initIgnoreRetorno();

			if (java) {
				new CreateBusinessTests(java, path);
				new CreateDtoTests(java, false, path, path);//dtos, persistance
				new CreatePersistanceTests(java, path);
				//				new CreateValidadorTests(java, path);
			}
			else {
				new CreateBusinessTests(java, path);
				//				new CreateBusinessTests(java, path.replace("business", "bionexo"));
				//				new CreateDtoTests(java, path.replace("business", "core"), "utils", true);//core
				new CreateDtoTests(java, false, path.replace("business", "dtos"), path.replace("business", "dtos"));//dtos
				new CreatePersistanceTests(java, path.replace("business", "persistance"));//persistance, dtosPersistance
				//				new CreateValidadorTests(java, path.replace("business", "validadores"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(revisar);
		for (String s : revisar) {
			System.out.println(s);
		}
		System.out.println("revisar: " + revisar.size());
	}

	/**
	 *
	 */
	private void initIgnoreRetorno () {
		revisar = new LinkedList<String>();

		ignoreRetorno = new LinkedList<String>();

		ignoreRetorno.add("boolean");
		ignoreRetorno.add("Boolean");
		ignoreRetorno.add("Date");
		ignoreRetorno.add("Double");
		ignoreRetorno.add("int");
		ignoreRetorno.add("Long");
		ignoreRetorno.add("String");
		System.out.println("Ignored: " + ignoreRetorno.size());
	}

	/**
	 *
	 * @param f
	 * @param customTests
	 * @throws Exception
	 */
	public static void loadCustomTests(File f, Map<String, StringBuffer> customTests) throws Exception {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			String customTest = null;
			String linea = null;
			String parentTest = null;
			int pos = 0;
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

				pos = linea.indexOf("@CustomTest");
				if (pos >= 0) {
					pos = linea.indexOf("\"");
					parentTest = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
					if (!customTests.containsKey(parentTest)) {
						customTests.put(parentTest, new StringBuffer());
					}
					customTests.get(parentTest).append(linea).append(SALTO);
				}
				if (parentTest != null) {
					if (linea.indexOf("private ") >= 0
							|| linea.indexOf("protected ") > 0
							|| linea.indexOf("public ") > 0) {
						String[] array = linea.replaceAll("\\p{Space}+", " ").split(" ");

						if (array[2].contains("(")
								|| array[3].startsWith("(")) {
							if (array[2].contains("(")) {
								customTest = array[2].split("\\(")[0];
							} else {
								customTest = array[2];
							}
						}
						else if (array[3].contains("(")
								|| array[4].startsWith("(")) {
							if (array[3].contains("(")) {
								customTest = array[3].split("\\(")[0];
							} else {
								customTest = array[3];
							}
						}
						else {
							throw new Exception("caldito seas 3 " + linea);
						}

						Commons.logInfo("\t\t" + f.getName().replace(".java", "") + " " + parentTest + "--->" + customTest);
						customTests.get(parentTest).append(linea).append(SALTO);

						while ((linea = br.readLine()) != null) {
							pos = linea.indexOf("public ");
							if (pos >= 0) {
								parentTest = null;
								customTest = null;
								break;
							}
							pos = linea.replaceAll("\t", "").indexOf("@");
							if (pos == 0) {
								parentTest = null;
								customTest = null;

								pos = linea.indexOf("@CustomTest");
								if (pos >= 0) {
									pos = linea.indexOf("\"");
									parentTest = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
									if (!customTests.containsKey(parentTest)) {
										customTests.put(parentTest, new StringBuffer());
									}
									customTests.get(parentTest).append(linea).append(SALTO);
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
							if (parentTest != null) {
								customTests.get(parentTest).append(linea).append(SALTO);
							}
						}
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

	/**
	 *
	 * @param f
	 * @param customTests
	 * @param ignoreControlRetorno
	 * @param importCustom
	 * @throws Exception
	 */
	public static void loadCustomTests(File f, Map<String, StringBuffer> customTests, Map<String, String> ignoreControlRetorno, StringBuffer importCustom, List<String> metodosComplejos) throws Exception {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			@SuppressWarnings("unused")
			String customTest = null;
			String linea = null;
			String parentTest = null;
			int pos = 0;
			int lineas = 0;
			int largo = 0;
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
				if (pos >= 0 && !importCustom.toString().contains(linea)) {
					importCustom.append(linea);
					importCustom.append(SALTO);
				}

				pos = linea.indexOf("@IgnoreControlRetorno");
				if (pos >= 0) {
					String[] motivo = linea.replaceAll("\\p{Space}+", " ").split("\"");
					linea = br.readLine();

					if (linea.indexOf("private ") >= 0
							|| linea.indexOf("protected ") > 0
							|| linea.indexOf("public ") > 0) {
						lineas = 0;
						largo = 0;
						String[] array = linea.replaceAll("\\p{Space}+", " ").split(" ");

						String ignore = null;
						if (array[2].contains("(")
								|| array[3].startsWith("(")) {
							if (array[2].contains("(")) {
								ignore = array[2].split("\\(")[0];
							} else {
								ignore = array[2];
							}
						}
						else if (array[3].contains("(")
								|| array[4].startsWith("(")) {
							if (array[3].contains("(")) {
								ignore = array[3].split("\\(")[0];
							} else {
								ignore = array[3];
							}
						}
						else {
							throw new Exception("caldito seas 3 " + linea);
						}
						if (motivo.length > 1) {
							ignoreControlRetorno.put(ignore, motivo[1]);
						} else {
							ignoreControlRetorno.put(ignore, "");
						}
					}

					continue;
				}

				pos = linea.indexOf("@CustomTest");
				if (pos >= 0) {
					pos = linea.indexOf("\"");
					parentTest = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
					if (!customTests.containsKey(parentTest)) {
						customTests.put(parentTest, new StringBuffer());
					}
					if (linea.contains("PersistanceFactory")) {
						for (String s : ignoreRetorno) {
							if ((linea.contains(s + " ") || linea.contains("<" + s + ">"))
									&& linea.indexOf(s) < linea.indexOf("PersistanceFactory")
									&& !linea.contains("getIntDomain")) {
								System.out.println("controlar " + f.getName() + " " + parentTest + ": " + s);
							}
						}
					}
					customTests.get(parentTest).append(linea).append(SALTO);
					if (!importCustom.toString().contains("import ar.com.thinksoft.utils.Constants;")) {
						importCustom.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
					}
				}
				if (parentTest != null) {
					if (linea.indexOf("private ") >= 0
							|| linea.indexOf("protected ") > 0
							|| linea.indexOf("public ") > 0) {
						lineas = 0;
						largo = 0;
						String[] array = linea.replaceAll("\\p{Space}+", " ").split(" ");

						if (array[2].contains("(")
								|| array[3].startsWith("(")) {
							if (array[2].contains("(")) {
								customTest = array[2].split("\\(")[0];
							} else {
								customTest = array[2];
							}
						}
						else if (array[3].contains("(")
								|| array[4].startsWith("(")) {
							if (array[3].contains("(")) {
								customTest = array[3].split("\\(")[0];
							} else {
								customTest = array[3];
							}
						}
						else if (array[4].contains("(")
								|| array[5].startsWith("(")) {
							if (array[4].contains("(")) {
								customTest = array[4].split("\\(")[0];
							} else {
								customTest = array[4];
							}
						}
						else {
							throw new Exception("caldito seas 3 " + linea);
						}

						//						System.out.println("\t\t" + f.getName().replace(".java", "") + " " + parentTest + "--->" + customTest);
						customTests.get(parentTest).append(linea).append(SALTO);

						while ((linea = br.readLine()) != null) {
							if (linea.indexOf("private ") >= 0
									|| linea.indexOf("protected ") > 0
									|| linea.indexOf("public ") > 0) {
								parentTest = null;
								customTest = null;
								break;
							}
							pos = linea.replaceAll("\t", "").indexOf("@");
							if (pos == 0) {
								parentTest = null;
								customTest = null;
								pos = linea.indexOf("@IgnoreControlRetorno");
								if (pos >= 0) {
									String[] motivo = linea.replaceAll("\\p{Space}+", " ").split("\"");
									linea = br.readLine();

									if (linea.indexOf("private ") >= 0
											|| linea.indexOf("protected ") > 0
											|| linea.indexOf("public ") > 0) {
										lineas = 0;
										largo = 0;
										array = linea.replaceAll("\\p{Space}+", " ").split(" ");

										String ignore = null;
										if (array[2].contains("(")
												|| array[3].startsWith("(")) {
											if (array[2].contains("(")) {
												ignore = array[2].split("\\(")[0];
											} else {
												ignore = array[2];
											}
										}
										else if (array[3].contains("(")
												|| array[4].startsWith("(")) {
											if (array[3].contains("(")) {
												ignore = array[3].split("\\(")[0];
											} else {
												ignore = array[3];
											}
										}
										else {
											throw new Exception("caldito seas 3 " + linea);
										}
										if (motivo.length > 1) {
											ignoreControlRetorno.put(ignore, motivo[1]);
										} else {
											ignoreControlRetorno.put(ignore, "");
										}
									}

									continue;
								}

								pos = linea.indexOf("@CustomTest");
								if (pos >= 0) {
									pos = linea.indexOf("\"");
									parentTest = linea.substring(pos + 1, linea.indexOf("\"", pos + 1));
									if (!customTests.containsKey(parentTest)) {
										customTests.put(parentTest, new StringBuffer());
									}
									if (linea.contains("PersistanceFactory")) {
										for (String s : ignoreRetorno) {
											if ((linea.contains(s + " ") || linea.contains("<" + s + ">"))
													&& linea.indexOf(s) < linea.indexOf("PersistanceFactory")
													&& !linea.contains("getIntDomain")) {
												System.out.println("controlar " + f.getName() + " " + parentTest + ": " + s);
											}
										}
									}
									customTests.get(parentTest).append(linea).append(SALTO);
									if (!importCustom.toString().contains("import ar.com.thinksoft.utils.Constants.AmbIntTodos;")) {
										importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
									}
									if (!importCustom.toString().contains("import ar.com.thinksoft.utils.Constants;")) {
										importCustom.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
									}
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
							if (parentTest != null) {
								if (linea.contains("PersistanceFactory")) {
									for (String s : ignoreRetorno) {
										if ((linea.contains(s + " ") || linea.contains("<" + s + ">"))
												&& linea.indexOf(s) < linea.indexOf("PersistanceFactory")
												&& !linea.contains("getIntDomain")) {
											System.out.println("controlar " + f.getName() + " " + parentTest + ": " + s);
										}
									}
								}
								customTests.get(parentTest).append(linea).append(SALTO);
							}
						}
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

	/**
	 *
	 * @param clase
	 * @param metodo
	 * @param type
	 * @return
	 */
	public static boolean controlarRetorno (String clase, String metodo, String type) {
		if (ignoreRetorno.contains (type)) {
			return false;
		} else {
			if (!type.endsWith("NotMapped")) {
				revisar.add(clase + "." + metodo + ": " + type);
			}
			return true;
		}
	}

	/**
	 *
	 * @param paquete
	 * @param m
	 * @param p
	 * @param objects
	 * @param objectsName
	 * @param importCustom
	 * @throws Exception
	 */
	public static void objetear(String paquete, Metodo m, Parameter p,
			StringBuffer objects, StringBuffer objectsName,
			StringBuffer importCustom, boolean usaSession, boolean business) throws Exception {
		String fecha = (usaSession ? "CommonsTest.getFechaActual(session)" : "new Date()");

		String tipoDatoFull = p.getType().getName();
		String tipoDato = tipoDatoFull;
		if (tipoDato.contains("$")) {
			tipoDato = tipoDato.replace("$", ".");
		}
		else if (tipoDato.contains(".")) {
			tipoDato = tipoDato.substring(tipoDato.lastIndexOf(".") + 1).replace(";", "");
		}
		if ("int".equals(tipoDato)) {
			tipoDato = "Integer";
		}
		String objeto = p.getName();
		String s = tipoDato + " " + p.getName();

		if ("StatelessSession".equals(p.getType().getSimpleName())) {
			objeto = "session";
		}
		else {
			if (p.isVarArgs()) {
				objects.append("				String " + objeto + " = \"PENDIENTE\";").append(SALTO);
			}
			else if (p.getType().isArray()) {
				if ("estado".equals(objeto) || "estados".equals(objeto)) {
					objects.append("				" + p + " = new String[] {\"AUTORIZADO\", \"GENERADO\"};").append(SALTO);
				} else {
					objects.append("				" + p + " = null;").append(SALTO);
				}
			}
			else if (tipoDato.equalsIgnoreCase("blob")) {
				objects.append("				" + s + " = null;").append(SALTO);
			}
			else if (tipoDato.equalsIgnoreCase("boolean")) {
				objects.append("				" + s + " = true;").append(SALTO);
			}
			else if (tipoDato.equals("Calendar")) {
				objects.append("				" + s + " = Calendar.getInstance();").append(SALTO);
			}
			else if (tipoDato.equalsIgnoreCase("Character")) {
				if (paquete.contains(".dtos.") && (m.getName().startsWith("set") || m.getName().startsWith("get"))) {
					objects.append("				" + s + " = Constants.CHAR_S;").append(SALTO);
				}
				else {
					objects.append("				" + s + " = null;").append(SALTO);
				}
			}
			else if (tipoDato.equalsIgnoreCase("clob")) {
				objects.append("				" + s + " = null;").append(SALTO);
			}
			else if (tipoDato.equalsIgnoreCase("Date")) {
				if (objeto.contains("fechaDesde")) {
					objects.append("				Date " + objeto + " = DateCommonFunctions.sumarMesAFecha(" + fecha + ", -12);").append(SALTO);
				}
				else if (objeto.contains("fechaHasta")) {
					if (!objects.toString().contains(objeto)) {
						objects.append("				Date " + objeto + " = " + fecha + ";").append(SALTO);
					}
				}
				else {
					objects.append("				" + s + " = " + fecha + ";").append(SALTO);
				}
			}
			else if (tipoDato.equalsIgnoreCase("Double")) {
				objects.append("				" + s + " = 1D;").append(SALTO);
			}
			else if (tipoDato.equalsIgnoreCase("Integer")) {
				objects.append("				" + s + " = 1;").append(SALTO);
			}
			else if (tipoDato.equalsIgnoreCase("Long")) {
				objects.append("				" + s + " = 1L;").append(SALTO);
			}
			else if (tipoDato.equals("String")) {
				if (paquete.contains(".dtos.") && (m.getName().startsWith("set") || m.getName().startsWith("get"))) {
					objects.append("				" + s + " = \"" + objeto + "\";").append(SALTO);
				}
				else {
					objects.append("				" + s + " = null;").append(SALTO);
				}
			}
			else if (tipoDato.contains("List")
					&& !tipoDato.contains("CheckList")
					&& !tipoDato.contains("Lista")) {
				String type = null;
				if (p.getParameterizedType() instanceof ParameterizedType) {
					ParameterizedType paramType = (ParameterizedType) p.getParameterizedType();
					Type[] argTypes = paramType.getActualTypeArguments();
					type = argTypes[0].getTypeName().replace("$", ".");
					type = "List<" + type/*.substring(type.lastIndexOf(".") + 1)*/ + ">";
				} else {
					System.out.println("que tipo " + p.getParameterizedType());
				}

				objects.append("				" + type + " " + objeto + " = new Linked" + type + "();").append(SALTO);
			}
			else if (tipoDato.contains("Map")
					&& !tipoDato.contains("Mapped")) {
				ParameterizedType paramType = (ParameterizedType) p.getParameterizedType();
				Type[] argTypes = paramType.getActualTypeArguments();
				String type1 = argTypes[0].getTypeName();
				String type2 = argTypes[1].getTypeName();
				objects.append("				Map<" + type1 + ", " + type2 + "> " + objeto + " = new HashMap<" + type1 + ", " + type2 + ">();").append(SALTO);
			}
			else {
				objects.append("				" + s + " = new " + tipoDato + "();").append(SALTO);
			}
		}

		if (p.isVarArgs()) {
			objectsName.append(objeto);
		}
		else if (s.contains("Map")
				&& s.contains("<")
				&& !s.contains("Mapped")) {
			try {
				objectsName.append(s.split(" ")[2] + ", ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			objectsName.append(objeto + ", ");
		}
	}

	/**
	 * Obtener nombre de la clase a partir del nombre de la tabla o un campo de la base de datos.
	 * @param str Nombre de la tabla o campo.
	 * @return Nombre de la clase resultante.
	 */
	public static String getJavaClass(String str) {
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
	 *
	 * @param persistance
	 * @param file
	 * @return
	 */
	public static String traducirImp(List<String> persistance, File file) {
		String str = file.getName();
		str = str.replace(".java", "");
		if (!persistance.contains("get" + str)) {
			if (persistance.contains("get" + str.replace("Imp", "Int"))) {
				str = str.replace("Imp", "Int");
			}
			else if (persistance.contains("get" + str.replace("Imp", ""))) {
				str = str.replace("Imp", "");
			}
		}
		return str;
	}

	/**
	 *
	 * @param business
	 * @param file
	 * @return
	 */
	public static String traducirInt(List<String> business, File file) {
		String str = file.getName();
		str = str.replace(".java", "");
		//		if (!business.contains("get" + str)) {
		//			if (business.contains("get" + str.replace("Imp", "Int"))) {
		str = str.replace("Imp", "Int");
		//			}
		//			else if (business.contains("get" + str.replace("Imp", ""))) {
		//				str = str.replace("Imp", "");
		//			}
		//		}
		return str;
	}

}