package ar.com.thinksoft.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.thinksoft.common.Commons;
import ar.com.thinksoft.common.Metodo;
import ar.com.thinksoft.common.Utils;

public class CreateDtoTests {

	private final String SALTO = "\n";

	/**
	 * Genera tests de dtos.
	 * @param java
	 * @param path
	 * @param pckGen
	 * @param core
	 * @throws Exception
	 */
	public CreateDtoTests(boolean java, String path, String pckGen, boolean core) throws Exception {
		String pathOrigen = "";
		String pathDestino = "";

		if (java) {
			pathOrigen = path.replace("-TEST", "").split("/bin")[0]+"/src/ar/com/thinksoft/" + pckGen + "/";
			pathDestino = path.split("/bin")[0]+"/src/ar/com/thinksoft/" + pckGen + "/";
		}
		else {
			pathOrigen = path.split("/target")[0]+"/src/main/java/ar/com/thinksoft/" + pckGen + "/";
			pathDestino = path.split("/target")[0]+"/src/test/java/ar/com/thinksoft/" + pckGen + "/";
		}

		generaTestsDto(java, false, pathOrigen, pathDestino, pckGen, core);
	}

	/**
	 * Genera tests de dtos.
	 * @param java
	 * @param persistance
	 * @param pathO
	 * @param pathD
	 * @throws Exception
	 */
	public CreateDtoTests(boolean java, boolean persistance, String pathO, String pathD) throws Exception {
		String pathOrigen = "";
		String pathDestino = "";

		if (java) {
			pathOrigen = pathO.replace("-TEST", "").split("/bin")[0]+"/src/ar/com/thinksoft/dtos";
			pathDestino = pathD.split("/bin")[0]+"/src/ar/com/thinksoft/business/dtos" + (persistance ? "Persistance" : "") + "/";
		}
		else {
			pathOrigen = pathO.split("/target")[0]+"/src/main/java/ar/com/thinksoft/dtos";
			pathDestino = pathD.split("/target")[0]+"/src/test/java/ar/com/thinksoft/business/dtos" + (persistance ? "Persistance" : "") + "/";
		}

		generaTestsDto(java, persistance, pathOrigen, pathDestino, null, false);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	/**
	 * Genera tests de dtos.
	 * @param java
	 * @param persistance
	 * @param pathOrigen
	 * @param pathDestino
	 * @param pckGen
	 * @param core
	 * @throws Exception
	 */
	private void generaTestsDto(boolean java, boolean persistance, String pathOrigen, String pathDestino, String pckGen, boolean core) throws Exception {
		System.out.println(pathOrigen + " -> " + pathDestino);

		File fOrigen = new File(pathOrigen);
		if (!fOrigen.exists()) {
			System.out.println(fOrigen.getAbsolutePath());
			throw new Exception("caldito seas 1");
		}
		File fDestino = new File(pathDestino);
		if (!fDestino.exists()) {
			fDestino.mkdirs();
		}

		File[] archivosOrigen = null;
		if (core) {
			archivosOrigen = fOrigen.listFiles(new MyFileFilter(false, "Common", ".java"));
		}
		else {
			archivosOrigen = fOrigen.listFiles(new MyFileFilter(true, null, ".java"));
		}

		for (File d : archivosOrigen) {
			if (".svn".equals(d.getName())
					/*|| !d.getName().startsWith("t")*/) {
				System.out.println("\tskip\t" + d.getName() + " is ignored");
				continue;
			}

			String dirTest = "";

			File[] archivos = null;

			if (d.isDirectory()) {
				if (core) {
					archivos = new File (d.getAbsolutePath()).listFiles(new MyFileFilter(false, "Common", ".java"));
				}
				else {
					archivos = new File (d.getAbsolutePath()).listFiles(new MyFileFilter(true, null, ".java"));
				}

				dirTest = d.getName();
			}
			else {
				archivos = new File[1];
				archivos[0] = d;
			}

			if (archivos == null) {
				System.out.println("\tno files\t" + d.getName());
				continue;
			}
			for (File f : archivos) {
				if (f.isDirectory()) {
					System.out.println("\tskip folder\t" + f.getName() + " is ignored");
					//generaTestsDto(java, persistance, f.getParent(), pathDestino + "/" + f.getParent().replace(pathOrigen, ""), pckGen, core);
					continue;
				}
				else if ("GeneraMapeos.java".equals(f.getName())
						|| "DefaultBuilder.java".equals(d.getName())) {
					System.out.println("\tskip\t" + f.getName() + " is ignored");
					continue;
				}

				//				System.out.println("\t\t" + f.getName());

				BufferedWriter bw = null;
				File destino = null;
				try {
					StringBuffer importCustom = new StringBuffer();
					StringBuffer m = new StringBuffer();
					StringBuffer imports = new StringBuffer();
					StringBuffer objects = new StringBuffer();
					StringBuffer objectsName = new StringBuffer();

					Map<String, StringBuffer> customTests = new HashMap<String, StringBuffer>();
					Map<String, String> ignoreControlRetorno = new HashMap<String, String>();

					List<String> metodosComplejos = new LinkedList<String>();

					destino = new File(pathDestino+dirTest+"/Test"+f.getName());

					String fileName = f.getName().replaceAll(".java", "");
					String archivo = "Test" + fileName;
					if (destino.exists()) {
						CreateTests.loadCustomTests(destino, customTests, ignoreControlRetorno, importCustom, metodosComplejos);
						destino.delete();
					}
					else {
						File dir = new File(pathDestino+dirTest);
						if (!dir.exists()) {
							dir.mkdirs();
						}
					}

					String clase = null;
					if (java) {
						clase = f.getAbsolutePath().split("src")[1].replace("java", "").replaceAll("\\\\", ".");
					} else {
						clase = f.getAbsolutePath().split("java")[1].replace("java", "").replaceAll("\\\\", ".");
					}
					clase = clase.substring(1, clase.length() - 1);

					if (!imports.toString().contains ("import " + clase + ";")) {
						imports.append("import ");
						imports.append(clase);
						imports.append(";");
						imports.append(SALTO);
					}

					Class c = Class.forName(clase);

					Annotation table = c.getAnnotation(Table.class);
					Annotation entity = c.getAnnotation(Entity.class);

					Field[] fi = c.getDeclaredFields();
					String idField = null;
					for (Field field : fi) {
						Id id = field.getAnnotation(Id.class);

						if (id != null) {
							Column col = field.getAnnotation(Column.class);
							if (col != null) {
								idField = CreateTests.getJavaClass(col.name());
							}
						}
					}

					List<Metodo> metodos = new LinkedList<Metodo>();

					Metodo met = new Metodo();

					if (!persistance) {
						Method[] me = c.getDeclaredMethods();
						for (Method method : me) {
							met = new Metodo();
							met.setPublico(Modifier.isPublic(method.getModifiers()));
							met.setName(method.getName());
							met.setIgnoreMethod(method.getAnnotation(IgnoreTest.class) != null);
							RemapTest remapMethod = method.getAnnotation(RemapTest.class);
							if (remapMethod != null) {
								met.setRemapName(remapMethod.name());
							}
							met.setParameters(method.getParameters());
							met.setReturnType(method.getReturnType().getName());
							met.setGenericReturnType(method.getGenericReturnType());

							String replace = null;
							//comentar para generar todos los tests
							if ("boolean".equals(met.getReturnType())
									|| core
									|| !method.getName().startsWith("get")
									|| !method.getName().startsWith("is")
									|| !method.getName().startsWith("set")) {
								if ("boolean".equals(met.getReturnType())
										|| core) {
									if (method.getName().startsWith("get")) {
										replace = method.getName().substring(3);
										met.setGetter(met.getName());
									}
									else if (method.getName().startsWith("is")) {
										replace = method.getName().substring(2);
										met.setGetter(met.getName());
									}
									else {
										replace = method.getName();
										met.setGetter(met.getName());
									}
								}
								else if (!method.getName().startsWith("get")
										&& !method.getName().startsWith("is")
										&& !method.getName().startsWith("set")
										&& !"clon".equals(method.getName())
										&& !"toString".equals(method.getName())) {
									replace = method.getName();
								}
							}

							if (replace != null) {
								for (Method s : me) {
									if (s.getName().equals("set" + replace)) {
										met.setSetter(s.getName());
										remapMethod = method.getAnnotation(RemapTest.class);
										if (remapMethod != null) {
											met.setRemapSetter(s.getName());
										}
										met.setSetterParameters(s.getParameters());
										break;
									}
								}
							}

							if ("hashCode".equals(method.getName())
									|| replace != null) {
								metodos.add(met);
							}
						}
					}

					if (metodos.size() == 0 && table == null) {
						continue;
					}

					if (!persistance) {
						Constructor[] constructors = c.getConstructors();
						List<Constructor> constructores = Arrays.asList(constructors);
						Utils.sortList(constructores, true, "ParameterCount");
						if (clase.contains("MensajeVa")) {
							System.out.println();
						}
						for (Constructor cons : constructores) {
							met = new Metodo();
							met.setPublico(true);
							met.setName(cons.getName().substring(cons.getName().lastIndexOf(".") + 1));
							met.setParameters(cons.getParameters());
							met.setReturnType("void");

							metodos.add(met);
						}
					}

					met = null;

					CommonFunctions.sortList(metodos, true, "Name", "RemapName");
					Long constructor = 0L;
					boolean esConstructor;
					for (Metodo method : metodos) {
						if (!method.isPublico()) {
							//							Commons.logInfo("\tskip\t" + f.getName().replaceAll(".java", "") + "." + method.getName() + " is not public");
							continue;
						}

						if (method.isIgnoreMethod()) {
							Commons.logInfo("\tskip\t" + f.getName().replaceAll(".java", "") + "." + method.getName());
							continue;
						}

						String remapMethodName = null;
						if (method.getRemapName() != null) {
							remapMethodName = method.getRemapName();
							//								System.out.println("\t\t" + f.getName().replace(".java", "") + "." + method.getName() + "--->" + remapMethodName);
						}

						String metodo = method.getName();
						String metodoTest = null;

						if (remapMethodName == null) {
							metodoTest = "test" + metodo.substring(0, 1).toUpperCase() + metodo.substring(1);
						}
						else {
							metodoTest = "test" + remapMethodName.substring(0, 1).toUpperCase() + remapMethodName.substring(1);
						}

						if (!persistance && (method.getGetter() != null || method.getSetter() != null)) {
							List<String> tmp = new LinkedList<String>();

							if(customTests.containsKey(metodoTest)) {
								m.append(customTests.get(metodoTest));
								customTests.remove(metodoTest);

								if (method.getSetter() == null) {
									continue;
								}
							}
							else {
								if ("testEquals".equals(metodoTest)) {
									tmp.add(metodoTest + "Null");
									tmp.add(metodoTest + "OtherClass");
									tmp.add(metodoTest + "Distinct");
									tmp.add(metodoTest + "Equal");
									tmp.add(metodoTest + "Object");
								}
								else {
									tmp.add(metodoTest);
								}
							}

							//TODO: remapSetter
							if (method.getSetter() != null) {
								method.setSetter("test" + method.getSetter().substring(0, 1).toUpperCase() + method.getSetter().substring(1));

								tmp.add(method.getSetter() + "Null");
								tmp.add(method.getSetter() + "NotNull");
							}

							objects = new StringBuffer();
							objectsName = new StringBuffer();

							for (Parameter p : method.getParameters()) {
								CreateTests.objetear(clase, method, p,
										objects, objectsName,
										importCustom, persistance, false);

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
										//									if ("AmbIntTodos".equals(p.getType().getSimpleName())) {
										//										importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
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

							String postCallTrue = null;
							String postCallFalse = null;
							String postCallNull = null;
							String postCallNotNull = null;
							String ret = "";
							if (!"void".equals(method.getReturnType())) {
								try {
									Type returnType = method.getGenericReturnType();
									if (returnType instanceof ParameterizedType) {
										ParameterizedType paramType = (ParameterizedType) returnType;
										Type[] argTypes = paramType.getActualTypeArguments();
										if (argTypes.length == 1) {
											String type = argTypes[0].getTypeName().replace("$", ".");
											type = type.substring(type.lastIndexOf(".") + 1);
											if (CreateTests.controlarRetorno(clase, metodo, type)) {
												ret = "List<" + type + ">";
												ret = ret + " ret = ";
												ret = ret.replace("ArrayList", "List");
												postCallTrue = "				if (ret != null && ret.size() > 0) {\n" +
														"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
														"				}\n";
											}
										}
										else if (argTypes.length == 2) {
											String type1 = argTypes[0].getTypeName().replace("$", ".");
											String type2 = argTypes[1].getTypeName().replace("$", ".");
											ret = "Map<" + type1 + ", " + type2 + ">";
											ret = ret + " ret = ";
											postCallTrue = "				if (ret != null && ret.size() > 0) {\n" +
													"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
													"				}\n";
										}
										else {
											ret = method.getReturnType().replace("$", ".");
											if (ret.contains("[B")) {
												ret = "byte[]";
											}
											else if (!"boolean".equals(ret) && !"double".equals(ret) && !"int".equals(ret) && !"long".equals(ret)) {
												String imp = "import " + ret.replace("$", ".") + ";";
												if (!importCustom.toString().contains(imp)) {
													importCustom.append(imp).append(SALTO);
												}
											}
											if (ret.contains(".")) {
												ret = ret.substring(ret.lastIndexOf(".") + 1);
											}
											ret = ret + " ret = ";
											postCallTrue = "				if (ret) {\n" +
													"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
													"				}\n";
											//										}
											//										else {
											//											ret = "";
											//										}
										}
									}
									else {
										ret = method.getReturnType().replace("$", ".");
										if (ret.contains("[B")) {
											ret = "byte[]";
										}
										else if (!"boolean".equals(ret) && !"double".equals(ret) && !"int".equals(ret) && !"long".equals(ret)) {
											String imp = "import " + ret.replace("$", ".") + ";";
											if (!importCustom.toString().contains(imp)) {
												importCustom.append(imp).append(SALTO);
											}
										}
										if (ret.contains(".")) {
											ret = ret.substring(ret.lastIndexOf(".") + 1);
										}
										ret = ret + " ret = ";
										postCallTrue = "				if (ret) {\n" +
												"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
												"				}\n";
										postCallFalse = "				if (!ret) {\n" +
												"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
												"				}\n";
										postCallNull = "				if (ret == null) {\n" +
												"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
												"				}\n";
										postCallNotNull = "				if (ret != null) {\n" +
												"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
												"				}\n";
									}
								} catch (Exception e) {
									System.out.println("que hago 2 " + method.getReturnType());
									e.printStackTrace();
								}
							}
							else {
								continue;
							}

							String tmpMethod = null;
							for (String s : tmp) {
								if(customTests.containsKey(s)) {
									m.append(customTests.get(s));
									customTests.remove(s);
									continue;
								}

								tmpMethod = s;
								//								if ("testEqualsOtherClass".equals(s)) {
								//									m.append("	@SuppressWarnings(\"unlikely-arg-type\")").append(SALTO);
								//								}
								if (core) {
									m.append("	@CustomTest(parent=\"" + tmpMethod + "\", motivo=\"parametro\")").append(SALTO);
								}
								m.append("	public void " + tmpMethod + "() {").append(SALTO);

								if (s.endsWith("NotNull")) {
									s = s.substring(0, s.length() - 7);
								}
								else if (s.endsWith("Null")) {
									s = s.substring(0, s.length() - 4);
								}

								m.append("		if (Commons.ignoreFailed (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);
								m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);

								m.append("		try {").append(SALTO);
								m.append("			try {").append(SALTO);

								if("testCompareTo".equals(tmpMethod)) {
									m.append("				" + clase + " objeto = new " + clase + "();").append(SALTO);
								}
								else {
									m.append("				" + clase + " objeto = new " + clase + "();").append(SALTO);
								}

								if (s.startsWith("testSet")) {
									if (ret.startsWith("boolean")) {
										if (tmpMethod.endsWith("NotNull")) {
											m.append("				objeto." + s.replace("testSet", "set") + "(true);");
										}
										else {
											m.append("			objeto." + s.replace("testSet", "set") + "(false);");
										}
									}
									else {
										objects = new StringBuffer();
										objectsName = new StringBuffer();

										if (tmpMethod.endsWith("NotNull")) {
											for (Parameter p : method.getSetterParameters()) {
												CreateTests.objetear(clase, method, p,
														objects, objectsName,
														importCustom, persistance, false);

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
														//									if ("AmbIntTodos".equals(p.getType().getSimpleName())) {
														//										importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
														//									}
														//									else {
														//														System.out.println("que se io " + p);
														//									}
														continue;
													}
												} catch (Exception e) {
													System.out.println("que hago " + p.getType());
													//									e.printStackTrace();
												}
											}
											if (ret.startsWith("byte[]")) {
												objects = new StringBuffer(objects.toString().replace("null", "new byte[0]"));
											}
											objects = new StringBuffer(objects.toString().replace("\t\t\t\t", "\t\t\t"));
											m.append(objects);
											String obj = objectsName.toString();
											if (obj.endsWith(", ")) {
												obj = obj.substring(0, obj.lastIndexOf(","));
											}
											m.append("				objeto." + s.replace("testSet", "set") + "(" + obj + ");");
										}
										else {
											m.append("				objeto." + s.replace("testSet", "set") + "(null);");
										}

										objects = new StringBuffer();
										objectsName = new StringBuffer();
									}
									m.append(SALTO);
								}

								String obj = null;
								String parametro = "null";
								String expected = null;

								/*if ("clon".equals(tmpMethod)) {
									expected = "NOT_NULL";
								}
								else */if (ret.startsWith("List<")) {
									expected = "LIST_EMPTY";
								}
								else if ((tmpMethod.startsWith("testGet")
										|| tmpMethod.startsWith("testIs")
										|| tmpMethod.startsWith("testTiene"))
										&& (tmpMethod.endsWith("Boolean")
												|| ret.startsWith("boolean"))) {
									expected = "FALSE";
								}
								else if (tmpMethod.startsWith("testSet")) {
									if (tmpMethod.endsWith("BooleanNotNull") || (tmpMethod.endsWith("NotNull") && ret.startsWith("boolean"))) {
										expected = "TRUE";
									}
									else if (tmpMethod.endsWith("BooleanNull") || (tmpMethod.endsWith("Null") && ret.startsWith("boolean"))) {
										expected = "FALSE";
									}
									else if (tmpMethod.endsWith("NotNull")) {
										expected = "NOT_NULL";
									}
									else {
										expected = "NULL";
									}
								}
								else if ("testEqualsNull".equals(tmpMethod)
										|| "testEqualsOtherClass".equals(tmpMethod)
										|| "testEqualsDistinct".equals(tmpMethod)) {
									expected = "FALSE";
								}
								else if ("testEqualsEqual".equals(tmpMethod)
										|| "testEqualsObject".equals(tmpMethod)) {
									if ("testEqualsEqual".equals(tmpMethod)) {
										parametro = "objeto.clon()";
									}
									else if ("testEqualsObject".equals(tmpMethod)) {
										parametro = "objeto";
									}
									expected = "TRUE";
								}
								else if (tmpMethod.endsWith("NotNull")) {
									expected = "NOT_NULL";
								}
								else if ("testGetPk".equals(tmpMethod)) {
									expected = "NOT_NULL";
								}
								else if (tmpMethod.endsWith("Null") || tmpMethod.startsWith("testGet") || core) {
									expected = "NULL";
								}
								else {
									System.out.println("contemplar expected " + tmpMethod + " - ret: " + ret);
								}

								if (core) {
									m.append("				error = Commons.methodTest(clase, testName, " + c.getName() + ".class, \"" + metodo + "\", objeto, " + "EXPECTED." + expected + ", " + parametro + ");").append(SALTO);
								} else {
									m.append("				error = Commons.methodTest(clase, testName, " + c.getName() + ".class, \"" + metodo + "\", objeto, " + parametro + ", EXPECTED." + expected + ");").append(SALTO);
								}

								m.append("				Commons.logInfoSuccess(clase, testName);").append(SALTO);
								m.append("			} catch (Exception e) {").append(SALTO);
								m.append("				HandlerException.getInstancia().treateException(e,getClass());").append(SALTO);
								m.append("			}").append(SALTO);
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

							if(customTests.containsKey(metodoTest)) {
								customTests.remove(metodoTest);
							}
						}
						else {
							List<String> tmp = new LinkedList<String>();

							if(customTests.containsKey(metodoTest)) {
								m.append(customTests.get(metodoTest));
								customTests.remove(metodoTest);

								if (method.getSetter() == null) {
									continue;
								}
							}
							else {
								tmp.add(metodoTest);
							}

							if (method.getSetter() != null) {
								method.setSetter("test" + method.getSetter().substring(0, 1).toUpperCase() + method.getSetter().substring(1));

								if(customTests.containsKey(method.getSetter())) {
									m.append(customTests.get(method.getSetter()));
									continue;
								}

								tmp.add(method.getSetter());
							}

							objects = new StringBuffer();
							objectsName = new StringBuffer();

							for (Parameter p : method.getParameters()) {
								CreateTests.objetear(clase, method, p,
										objects, objectsName,
										importCustom, persistance, false);

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
										//									if ("AmbIntTodos".equals(p.getType().getSimpleName())) {
										//										importCustom.append("import ar.com.thinksoft.utils.Constants.AmbIntTodos;").append(SALTO);
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

							String postCallNull = null;
							String postCallTrue = null;
							String postCallFalse = null;
							String postCallInt = null;
							String ret = "";
							if (!"void".equals(method.getReturnType())) {
								try {
									Type returnType = method.getGenericReturnType();
									if (returnType instanceof ParameterizedType) {
										ParameterizedType paramType = (ParameterizedType) returnType;
										Type[] argTypes = paramType.getActualTypeArguments();
										if (argTypes.length == 1) {
											String type = argTypes[0].getTypeName();
											type = type.substring(type.lastIndexOf(".") + 1);
											if (CreateTests.controlarRetorno(clase, metodo, type)) {
												ret = "List<" + type + ">";
												ret = ret + " ret = ";
												ret = ret.replace("ArrayList", "List");
												postCallTrue = "				if (ret == null || ret.size() == 0) {\n" +
														"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
														"				}\n";
											}
										}
										else if (argTypes.length == 2) {
											String type1 = argTypes[0].getTypeName();
											String type2 = argTypes[1].getTypeName();
											ret = "Map<" + type1 + ", " + type2 + ">";
											ret = ret + " ret = ";
											postCallTrue = "				if (ret == null || ret.size() == 0) {\n" +
													"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
													"				}\n";
										}
										else {
											ret = method.getReturnType();
											if (ret.contains(".")) {
												ret = ret.substring(ret.lastIndexOf(".") + 1);
											}
											ret = ret + " ret = ";
											postCallTrue = "				if (ret) {\n" +
													"					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);\n" +
													"				}\n";
											//										}
											//										else {
											//											ret = "";
											//										}
										}
									}
									else {
										ret = method.getReturnType().replace("$", ".");
										if (ret.contains("[B")) {
											ret = "byte[]";
										}
										else if (ret.contains("Object;")) {
											ret = "Object[]";
										}
										else if (!"boolean".equals(ret) && !"double".equals(ret) && !"int".equals(ret) && !"long".equals(ret)) {
											String imp = "import " + ret.replace("$", ".") + ";";
											if (!importCustom.toString().contains(imp)) {
												importCustom.append(imp).append(SALTO);
											}
										}
										if (ret.contains(".")) {
											ret = ret.substring(ret.lastIndexOf(".") + 1);
										}
										ret = ret + " ret = ";
										postCallNull = "				if (ret == null) {\n" +
												"					throw new Exception(MessageBundle.NO_HAY_REGISTROS_CARGADOS);\n" +
												"				}\n";
										postCallTrue = "				if (ret) {\n" +
												"					throw new Exception(MessageBundle.NO_HAY_REGISTROS_CARGADOS);\n" +
												"				}\n";
										postCallFalse = "				if (!ret) {\n" +
												"					throw new Exception(MessageBundle.NO_HAY_REGISTROS_CARGADOS);\n" +
												"				}\n";
										postCallInt = "				if (ret == 0) {\n" +
												"					throw new Exception(MessageBundle.NO_HAY_REGISTROS_CARGADOS);\n" +
												"				}\n";
									}
								} catch (Exception e) {
									System.out.println("que hago 2 " + method.getReturnType());
									e.printStackTrace();
								}
							}

							for (String s : tmp) {
								constructor++;

								esConstructor = s.equals("test" + fileName);

								if (esConstructor) {
									s += constructor;
									if(customTests.containsKey(s)) {
										m.append(customTests.get(s));
										customTests.remove(s);
										continue;
									}
								}

								m.append("	public void " + s + "() {").append(SALTO);

								m.append("		if (Commons.ignoreFailed (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);
								m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);

								m.append("		try {").append(SALTO);
								//						m.append("			StatelessSession session = null;").append(SALTO);
								m.append("			try {").append(SALTO);
								//						m.append("				session = CommonsTest.getSession();").append(SALTO);

								String obj = objects.toString();
								if ("testCompareTo".equals(s)) {
									obj = obj.replace("new Object()", "new " + clase + "()");
								}

								m.append(obj);
								obj = objectsName.toString();
								if (obj.endsWith(", ")) {
									obj = obj.substring(0, obj.lastIndexOf(","));
								}

								if (esConstructor) {
									m.append("				new " + clase + "(" + obj + ");").append(SALTO);
								}
								else {
									if ("testCompareTo".equals(s)) {
										m.append("				" + clase + " objeto = new " + clase + "();").append(SALTO);
									}
									else if (!"testDefaultBuilder".equals(s)) {
										m.append("				" + clase + " objeto = new " + clase + "();").append(SALTO);
									}

									if (s.startsWith("testSet")) {
										m.append("				objeto." + s.replace("testSet", "set") + "(true);");
										m.append(SALTO);
									}

									if ("testDefaultBuilder".equals(s)) {
										m.append("				" + ret + ret.replace(" ret = ", "") + "." + metodo + "(" + obj + ");").append(SALTO);
									}
									else {
										m.append("				" + ret + "objeto." + metodo + "(" + obj + ");").append(SALTO);
									}
									if (s.startsWith("testSet")) {
										if (postCallFalse != null) {
											m.append(postCallFalse);
										}
									}
									if ("testToString".equals(s)) {
										if (postCallNull != null) {
											m.append(postCallNull);
										}
									}
									else if (ret.startsWith("int ")) {
										if (postCallInt != null) {
											m.append(postCallInt);
										}
									}
									else if (ret.startsWith("boolean ")) {
										if (postCallTrue != null) {
											m.append(postCallTrue);
										}
									}
									else {
										if (postCallNull != null) {
											m.append(postCallNull);
										}
									}
								}
								m.append("				Commons.logInfoSuccess(clase, testName);").append(SALTO);
								m.append("			} catch (Exception e) {");
								m.append(SALTO);
								m.append("				HandlerException.getInstancia().treateException(e,getClass());");
								m.append(SALTO);
								//						m.append("			} finally {");
								//						m.append(SALTO);
								//						m.append("				try {session.close();} catch (Exception e) {}");
								//						m.append(SALTO);
								m.append("			}");
								m.append(SALTO);
								m.append("		} catch (Exception e) {");
								m.append(SALTO);
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
						}
					}

					if (!destino.exists()) {
						try {
							destino.createNewFile();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (persistance) {
						if (table != null) {
							if (customTests.containsKey("testInsert")) {
								m.append(customTests.get("testInsert"));
								customTests.remove("testInsert");
							}
							else {
								m.append("	@CustomTest(parent=\"testInsert\", motivo=\"insert\")").append(SALTO);
								m.append("	public void testInsert() {").append(SALTO);

								m.append("		if (Commons.ignoreFailed (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);
								m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);
								m.append("		if (Commons.ignoreInsert (clase + \".\" + testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);

								m.append("		try {").append(SALTO);
								m.append("			StatelessSession session = null;").append(SALTO);
								m.append("			Transaction t = null;").append(SALTO);
								m.append("			try {").append(SALTO);
								m.append("				session = CommonsTest.getSession();").append(SALTO);
								m.append("				t = session.beginTransaction();").append(SALTO);

								m.append("				" + clase + " objeto = " + clase + ".defaultBuilder();").append(SALTO);

								m.append("				PersistanceFactory.getIntDomain().insert(objeto, session, getClass());").append(SALTO);
								if (idField != null) {
									m.append("				assertNotNull(objeto.get" + idField + "());").append(SALTO);
								}
								m.append("				((TransactionContext) session).managedFlush();").append(SALTO);
								m.append("				t.rollback();").append(SALTO);
								m.append("			} catch (Exception e) {");
								m.append(SALTO);
								m.append("				if (t != null && t.isActive()) {").append(SALTO);
								m.append("					t.rollback();").append(SALTO);
								m.append("				}").append(SALTO);
								m.append("				HandlerException.getInstancia().treateException(e,getClass());");
								m.append(SALTO);
								m.append("			} finally {");
								m.append(SALTO);
								m.append("				try {session.close();} catch (Exception e) {}");
								m.append(SALTO);
								m.append("			}");
								m.append(SALTO);
								m.append("			Commons.logInfoSuccess (clase, testName);").append(SALTO);
								m.append("		} catch (Exception e) {");
								m.append(SALTO);
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

							if (idField != null && !f.getName().replace(".java", "").startsWith("Tmp")) {
								m.append("	@SuppressWarnings(\"unchecked\")").append(SALTO);
								if (customTests.containsKey("testUpdate")) {
									m.append(customTests.get("testUpdate"));
									customTests.remove("testUpdate");
								}
								else {
									m.append("	@CustomTest(parent=\"testUpdate\", motivo=\"update\")").append(SALTO);
									m.append("	public void testUpdate() {").append(SALTO);

									m.append("		if (Commons.ignoreFailed (clase, testName)) {").append(SALTO);
									m.append("			return;").append(SALTO);
									m.append("		}").append(SALTO).append(SALTO);
									m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
									m.append("			return;").append(SALTO);
									m.append("		}").append(SALTO).append(SALTO);
									m.append("		if (Commons.ignoreUpdate (clase + \".\" + testName)) {").append(SALTO);
									m.append("			return;").append(SALTO);
									m.append("		}").append(SALTO).append(SALTO);

									m.append("		try {").append(SALTO);
									m.append("			List<" + clase + "> list = null;").append(SALTO);
									m.append("			StatelessSession session = null;").append(SALTO);
									m.append("			Transaction t = null;").append(SALTO);
									m.append("			try {").append(SALTO);
									m.append("				session = CommonsTest.getSession();").append(SALTO);
									m.append("				t = session.beginTransaction();").append(SALTO).append(SALTO);
									m.append("				" + clase + " objeto = new " + clase + "();").append(SALTO);
									m.append("				objeto.set" + idField + "();").append(SALTO);
									m.append("				list = PersistanceFactory.getIntDomain().selectEqual(objeto, session, getClass());").append(SALTO).append(SALTO);
									m.append("				if (list == null || list.size() == 0) {").append(SALTO);
									m.append("					throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);").append(SALTO);
									m.append("				}").append(SALTO).append(SALTO);
									m.append("				if (list.size() > 1) {").append(SALTO);
									m.append("					throw new BusinessException(MessageBundle.TOO_MANY_RECORDS_FOUND_WARN, SeverityBundle.WARN);").append(SALTO);
									m.append("				}").append(SALTO).append(SALTO);
									m.append("				objeto = list.get(0);").append(SALTO);
									m.append("				PersistanceFactory.getIntDomain().update(objeto, session, getClass());").append(SALTO);
									m.append("				((TransactionContext) session).managedFlush();").append(SALTO);
									m.append("				t.rollback();").append(SALTO);
									m.append("			} catch (Exception e) {");
									m.append(SALTO);
									m.append("				if (t != null && t.isActive()) {").append(SALTO);
									m.append("					t.rollback();").append(SALTO);
									m.append("				}").append(SALTO);
									m.append("				HandlerException.getInstancia().treateException(e,getClass());");
									m.append(SALTO);
									m.append("			} finally {");
									m.append(SALTO);
									m.append("				try {session.close();} catch (Exception e) {}");
									m.append(SALTO);
									m.append("			}");
									m.append(SALTO);
									m.append("			Commons.logInfoSuccess (clase, testName);").append(SALTO);
									m.append("		} catch (Exception e) {");
									m.append(SALTO);
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
							}
						}

						if (entity != null) {
							Table tableAnnotation = (Table) table;
							if (!tableAnnotation.name().startsWith("TMP_")) {
								m.append("	@SuppressWarnings(\"unchecked\")").append(SALTO);
							}
							if (customTests.containsKey("testSelectEqualOne")) {
								m.append(customTests.get("testSelectEqualOne"));
								customTests.remove("testSelectEqualOne");
							}
							else if (tableAnnotation.name().startsWith("TMP_")) {
								m.append("	@CustomTest(parent=\"testSelectEqualOne\", motivo=\"selectEqualOne\")").append(SALTO);
								m.append("	public void testSelectEqualOne() {").append(SALTO);
								m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);
								m.append("		Commons.logInfoSuccess(clase, testName);").append(SALTO).append(SALTO);
								m.append("		assertNull(error);").append(SALTO);
								m.append("	}").append(SALTO).append(SALTO);
							}
							else {
								m.append("	@CustomTest(parent=\"testSelectEqualOne\", motivo=\"selectEqualOne\")").append(SALTO);
								m.append("	public void testSelectEqualOne() {").append(SALTO);

								m.append("		if (Commons.ignoreFailed (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);
								m.append("		if (Commons.ignoreSuccess (clase, testName)) {").append(SALTO);
								m.append("			return;").append(SALTO);
								m.append("		}").append(SALTO).append(SALTO);

								m.append("		try {").append(SALTO);
								m.append("			StatelessSession session = null;").append(SALTO);
								m.append("			List<" + clase + "> list = null;").append(SALTO);
								m.append("			try {").append(SALTO);
								m.append("				session = CommonsTest.getSession();").append(SALTO);
								m.append("				" + clase + " objeto = new " + clase +  "();").append(SALTO);
								m.append("				objeto.set" + idField + "();").append(SALTO);
								m.append("				list = PersistanceFactory.getIntDomain().selectEqual(objeto, session, getClass());").append(SALTO);
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

								m.append("			if (list == null || list.size() == 0) {").append(SALTO);
								m.append("				throw new BusinessException(MessageBundle.NO_HAY_REGISTROS_CARGADOS, SeverityBundle.WARN);").append(SALTO);
								m.append("			}").append(SALTO).append(SALTO);

								m.append("			if (list.size() > 1) {").append(SALTO);
								m.append("				throw new BusinessException(MessageBundle.TOO_MANY_RECORDS_FOUND_WARN, SeverityBundle.WARN);").append(SALTO);
								m.append("			}").append(SALTO).append(SALTO);

								m.append("			Commons.logInfoSuccess(clase, testName);");
								m.append(SALTO);
								m.append("		} catch (Exception e) {");
								m.append(SALTO);
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
						}
					}

					String pckTest = null;
					if (pckGen != null) {
						pckTest = "ar.com.thinksoft." + pckGen;
					}
					else if (persistance) {
						pckTest = "ar.com.thinksoft.business.dtosPersistance." + dirTest;
					}
					else {
						pckTest = "ar.com.thinksoft.business.dtos." + dirTest;
					}

					StringBuffer s = new StringBuffer();
					s.append("package " + pckTest + ";").append(SALTO).append(SALTO);

					s.append("import java.sql.Blob;").append(SALTO).append(SALTO);
					s.append("import java.sql.Clob;").append(SALTO).append(SALTO);

					s.append("import java.util.Date;").append(SALTO);
					s.append("import java.util.HashMap;").append(SALTO);
					s.append("import java.util.LinkedList;").append(SALTO);
					s.append("import java.util.List;").append(SALTO);
					s.append("import java.util.Map;").append(SALTO).append(SALTO);
					if (persistance) {
						s.append("import org.hibernate.StatelessSession;").append(SALTO);
						s.append("import org.hibernate.Transaction;").append(SALTO);
						s.append("import org.hibernate.engine.transaction.spi.TransactionContext;").append(SALTO);
					}
					s.append("import org.junit.experimental.categories.Category;").append(SALTO).append(SALTO);

					s.append("import ar.com.thinksoft.common.Commons;").append(SALTO);
					if (persistance) {
						s.append("import ar.com.thinksoft.utils.CommonsTest;").append(SALTO);
					}
					else {
						s.append("import ar.com.thinksoft.common.Commons.EXPECTED;").append(SALTO);
					}
					s.append("import ar.com.thinksoft.common.CustomTest;").append(SALTO);

					s.append(imports);
					s.append(importCustom);

					if (persistance) {
						s.append("import ar.com.thinksoft.exception.BusinessException;").append(SALTO);
						s.append("import ar.com.thinksoft.jdbc.HibernateSessionFactory;").append(SALTO);
					}
					s.append("import ar.com.thinksoft.common.Dto;").append(SALTO);
					if (!core) {
						s.append("import ar.com.thinksoft.dtos.compras.TmpDetLugarEntregaCompra;").append(SALTO);
					}
					s.append("import ar.com.thinksoft.utils.CommonFunctions;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.ConstantsTest;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.DateCommonFunctions;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.Constants;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.ConstantsTest;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.HandlerException;").append(SALTO);
					s.append("import ar.com.thinksoft.utils.MessageBundle;").append(SALTO);
					if (table != null && persistance) {
						s.append("import ar.com.thinksoft.persistance.PersistanceFactory;").append(SALTO);
					}
					s.append("import ar.com.thinksoft.utils.SeverityBundle;").append(SALTO);
					s.append("import junit.framework.Test;").append(SALTO);
					s.append("import junit.framework.TestCase;").append(SALTO);
					s.append("import junit.framework.TestSuite;").append(SALTO).append(SALTO);

					s.append("import java.util.Date;").append(SALTO).append(SALTO);

					s.append("@Category(Dto.class)").append(SALTO);
					s.append("public class " + archivo + " extends TestCase {").append(SALTO).append(SALTO);
					if (core) {
						s.append("	private static final String clase = \"ar.com.thinksoft.utils." + archivo + "\";").append(SALTO).append(SALTO);
					} else {
						s.append("	private static final String clase = \"ar.com.thinksoft.business.dtos" + (persistance ? "Persistance" : "") + "." + dirTest + "." + archivo + "\";").append(SALTO).append(SALTO);
					}
					s.append("	String error = null;").append(SALTO);
					s.append("	private String testName = null;").append(SALTO).append(SALTO);

					if (persistance) {
						s.append("	private StatelessSession session = null;").append(SALTO).append(SALTO);
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
						s.append("		super.setUp();").append(SALTO).append(SALTO);
						if (persistance) {
							s.append("		CommonsTest.getInstance();").append(SALTO).append(SALTO);
						}
						s.append("		testName = getName();").append(SALTO);

						if (persistance) {
							s.append("		session = CommonsTest.getSession();").append(SALTO);
						}

						s.append("	}").append(SALTO).append(SALTO);
					}

					s.append("	public static Test suite() {").append(SALTO);
					s.append("		return new TestSuite( " + archivo + ".class );").append(SALTO);
					s.append("	}").append(SALTO).append(SALTO);

					s.append(m);

					for (String test : customTests.keySet()) {
						System.out.println(archivo + "." + test);
						s.append(customTests.get(test));
					}

					s.append("	@Override").append(SALTO);
					s.append("	protected void tearDown() throws Exception {").append(SALTO);
					s.append("		super.tearDown();").append(SALTO).append(SALTO);

					if (persistance) {
						s.append("		if (session != null) {").append(SALTO);
						s.append("			session.close();").append(SALTO);
						s.append("		}").append(SALTO).append(SALTO);
					}

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

	class MyFileFilter implements FileFilter {

		private boolean acceptDir;
		private String name;
		private String extension;

		MyFileFilter(boolean acceptDir, String name, String extension) {
			this.acceptDir = acceptDir;
			this.name = name;
			this.extension = extension;
		}

		@Override
		public boolean accept(File file) {
			boolean bool = ((acceptDir && file.isDirectory()) || (!file.isDirectory())
					&& (name == null || file.getName().contains(name))
					&& file.getName().toLowerCase().endsWith(extension));

			return bool;
		}

	}

}
