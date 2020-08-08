package ar.com.thinksoft.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.com.thinksoft.exception.BusinessException;


public class UnusedCode {

	@SuppressWarnings("unused")
	private List<String> listarBeans(Map<String, Map<String, Integer>> llamadas, List<String> result, File f, FileFilter filter) throws BusinessException {
		//		System.out.println("listarBeans");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		System.out.println("listarBeans(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			System.out.println(f.getAbsolutePath());
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		String archivoNombre = null;
		int lineNumber = 0;
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("Int")) {
				continue;
			}
			//system.out.println(",");
			archivoNombre = ficheros[x].getName().replace(".java", "");
			//System.out.println(f.getAbsolutePath() + "\\" + archivoNombre);
			//			boolean esArchivo = ficheros[x].getName().startsWith("BBInicioTurnos");
			boolean esArchivo = false;
			boolean debug = false;

			List<String> delegators = new LinkedList<String>();

			if (ficheros[x].isDirectory()) {
				listarBeans(llamadas, result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;
					int pos = 0;
					String metodo = null;
					boolean empezoClase = false;
					boolean empezoMetodo = false;

					int llaves = 0;
					while ((linea = br.readLine()) != null) {
						lineNumber++;

						//system.out.println("<");
						linea = linea.replaceAll("\t", "").trim();
						if (linea.length() == 0
								|| linea.startsWith("//")) {
							continue;
						}
						if (linea.startsWith("/*")) {
							pos = linea.indexOf("*/");
							while (pos < 0) {
								//system.out.println("z");
								linea = br.readLine();
								lineNumber++;
								pos = linea.indexOf("*/");
							}
							continue;
						}
						if (linea.contains("import ")) {
							if (linea.contains(".delegators.")) {
								String tmp = linea.substring(linea.indexOf(".delegators.") + 12, linea.length() - 1);
								delegators.add(tmp);
								if (!llamadas.containsKey(tmp)) {
									llamadas.put(tmp, new HashMap<String, Integer>());
								}
							}
							continue;
						}
						//system.out.println("\t" + linea);

						if (empezoClase) {
							if (linea.indexOf('@') >= 0) {
								continue;
							}
							if (esArchivo && debug) {
								System.out.println("debug");
							}
							if ((linea.contains("static") || linea.contains("public") || linea.contains("private"))
									&& linea.contains("enum")) {
								if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
										|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
									pos = linea.indexOf('}');
									while (pos < 0) {
										//system.out.println("x");
										linea = br.readLine();
										lineNumber++;
										pos = linea.indexOf('}');
										//system.out.println("\t" + linea);
									}
								}
								continue;
							}

							if (((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
									|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0))
									&& !linea.contains("actionSelectMenu")) {
								pos = linea.indexOf('{');
								if (pos >= 0) {
									if (esArchivo) {
										System.out.println("llaves++ 1");
									}
									llaves++;
									while (pos >= 0) {
										//system.out.println("b");
										pos = linea.indexOf('{', pos+1);
										if (pos >= 0) {
											if (esArchivo) {
												System.out.println("llaves++ 2");
											}
											llaves++;
										}
									}
								}
								pos = linea.indexOf('}');
								if (pos >= 0) {
									if (esArchivo) {
										System.out.println("llaves-- 1");
									}
									llaves--;
									while (pos >= 0) {
										//system.out.println("n");
										pos = linea.indexOf('}', pos+1);
										if (pos >= 0) {
											if (esArchivo) {
												System.out.println("llaves-- 2");
											}
											llaves--;
										}
									}
								}
							}
							pos = linea.indexOf('(');
							if (pos < 0) {
								if (empezoMetodo
										&& llaves == 0) {
									empezoMetodo = false;
									continue;
								}
								if (llaves == -1) {
									continue;
								}
							}

							if (empezoMetodo) {
								if (!linea.contains(".getContenido();")
										&& !linea.contains("actionSelectMenu")
										&& !linea.contains("FacesUtils.getBBCurrentInstance")
										&& !linea.contains("FacesUtils.getSessionValue")
										&& !linea.contains("FacesUtils.hideDialogs")
										&& !linea.contains("FacesUtils.redirect")
										&& !linea.contains("FacesUtils.removeSessionValue")
										&& !linea.contains("FacesUtils.setSessionValue")
										&& !linea.contains("header.add")
										&& !linea.contains("ReportManager.print")
										&& !linea.contains("XLSParser.getInstance")) {
									String encontro = null;
									String parece = null;
									int lastPos = 0;
									//									System.out.println(linea);
									for (String d : delegators) {
										do {
											//											System.out.println("\t" + pos + " " + lastPos + " " + d);
											pos = linea.indexOf(d, lastPos);
											lastPos = pos + 1;

											int ant = -1;
											if (pos > 0) {
												ant = linea.charAt(pos - 1);
											}

											if (pos >= 0
													&& ant != 34
													&& (ant < 65 || (ant > 90 && ant < 97) || ant > 122)
													&& linea.contains(".")
													&& linea.contains("(")
													&& !linea.contains("<" + d + ">")) {
												parece = d;
												if (linea.contains(d + ".")) {
													String tmp = linea.substring(linea.indexOf(d + "."));
													try {
														int parentesis = tmp.indexOf("(");
														if (parentesis < 0) {
															parentesis = Integer.MAX_VALUE;
														}

														int espacio = tmp.indexOf(" ");
														if (espacio < 0) {
															espacio = Integer.MAX_VALUE;
														}

														int igual = tmp.indexOf("=");
														if (igual < 0) {
															igual = Integer.MAX_VALUE;
														}

														if (igual < parentesis && igual < espacio) {
															String key = tmp.substring(0, igual);
															key = key.substring(d.length() + 1);
															if (!llamadas.get(d).containsKey(key)) {
																llamadas.get(d).put(key, 0);
															}
															llamadas.get(d).put(key, llamadas.get(d).get(key) + 1);
															encontro = d;
														}
														else if (parentesis < igual && parentesis < espacio) {
															String key = tmp.substring(0, parentesis);
															key = key.substring(d.length() + 1);
															if (!llamadas.get(d).containsKey(key)) {
																llamadas.get(d).put(key, 0);
															}
															llamadas.get(d).put(key, llamadas.get(d).get(key) + 1);
															encontro = d;
														}
														else if (espacio < igual && espacio < parentesis) {
															String key = tmp.substring(0, espacio);
															key = key.substring(d.length() + 1);
															if (!llamadas.get(d).containsKey(key)) {
																llamadas.get(d).put(key, 0);
															}
															llamadas.get(d).put(key, llamadas.get(d).get(key) + 1);
															encontro = d;
														}
														else {
															System.out.println("contemplar: " + tmp);
														}
													} catch (Exception e) {
														e.printStackTrace();
													}
													break;
												}
												//												else {
												//													System.out.println("\t<" + d + "> " + (ant) + ": " + linea);
												//												}
											}
										} while (pos >= 0 && lastPos >= 0);
									}
									if (parece != null && encontro == null && (!"Facturacion".equals(parece) || !linea.contains("FacturacionInternado"))) {
										System.out.println("que me falta: " + linea);
									}
								}
							}
							else {
								pos = linea.indexOf('(');
								if (pos < 0) {
									//									System.out.println("\tlinea no tratada: " + archivoNombre + " - " + linea);
									continue;
								}
								empezoMetodo = true;

								String str = linea.substring(0, pos+1);
								pos = str.indexOf('(');
								try {
									metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();
								} catch (Exception e) {
									System.out.println("e1 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
									e.printStackTrace();
								}
								if (esArchivo && "infoPrestacion".equals(metodo)) {
									System.out.println("infoPrestacion " + metodo);
								}

								pos = linea.indexOf('{');
								while (pos < 0) {
									//system.out.println("m");
									linea = br.readLine();
									lineNumber++;
									try {
										pos = linea.indexOf('{');
									} catch (Exception e) {
										pos = 0;
										System.out.println("e2 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
										e.printStackTrace();
									}
									//system.out.println("\t" + linea);
								}
								if (llaves == 0) {
									llaves++;
								}
							}
						}
						else {
							empezoClase = linea.contains("{") && !linea.contains("@");
							if (empezoClase && esArchivo) {
								System.out.println("es archivo");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (fr != null) {
							fr.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					try {
						if (br != null) {
							br.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	private Map<String, Map<String, Integer>> listarDelegators(Map<String, Map<String, Integer>> llamadas, List<String> result, File f, FileFilter filter) throws BusinessException {
		Map<String, Map<String, Integer>> salida = new HashMap<String, Map<String,Integer>>();

		//		System.out.println("listarDelegators");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		System.out.println("listarDelegators(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			System.out.println(f.getAbsolutePath());
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		String archivoNombre = null;
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())) {
				continue;
			}
			//system.out.println(",");
			archivoNombre = ficheros[x].getName().replace(".java", "");
			//System.out.println(f.getAbsolutePath() + "\\" + archivoNombre);

			Map<String, Integer> map = llamadas.get(archivoNombre);
			if (map == null) {
				result.add(archivoNombre);
			}

			//			boolean esArchivo = "Internaciones".equals(archivoNombre);
			boolean esArchivo = false;
			boolean debug = false;

			FileReader fr = null;
			BufferedReader br = null;
			int lineNumber = 0;
			try {
				fr = new FileReader(ficheros[x]);
				br = new BufferedReader(fr);

				String linea;
				int pos = 0;
				String metodo = null;
				boolean empezoClase = false;
				boolean empezoMetodo = false;

				int llaves = 0;
				while ((linea = br.readLine()) != null) {
					lineNumber++;

					//system.out.println("<");
					linea = linea.replaceAll("\t", "").trim();
					if (linea.length() == 0
							|| linea.startsWith("//")) {
						continue;
					}
					if (linea.startsWith("/*")) {
						pos = linea.indexOf("*/");
						while (pos < 0) {
							//system.out.println("z");
							linea = br.readLine();
							lineNumber++;
							pos = linea.indexOf("*/");
						}
						continue;
					}
					if (linea.contains("import ")) {
						continue;
					}
					//system.out.println("\t" + linea);

					if (empezoClase) {
						if (linea.indexOf('@') >= 0) {
							continue;
						}
						if (esArchivo && debug) {
							System.out.println("debug 2");
						}
						if ((linea.contains("static") || linea.contains("public") || linea.contains("private"))
								&& linea.contains("enum")) {
							if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
									|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
								pos = linea.indexOf('}');
								while (pos < 0) {
									//system.out.println("x");
									linea = br.readLine();
									lineNumber++;
									pos = linea.indexOf('}');
									//system.out.println("\t" + linea);
								}
							}
							continue;
						}

						if (((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
								|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0))
								&& !linea.contains("actionSelectMenu")) {
							pos = linea.indexOf('{');
							if (pos >= 0) {
								if (esArchivo) {
									System.out.println("llaves++ 1");
								}
								llaves++;
								while (pos >= 0) {
									//system.out.println("b");
									pos = linea.indexOf('{', pos+1);
									if (pos >= 0) {
										if (esArchivo) {
											System.out.println("llaves++ 2");
										}
										llaves++;
									}
								}
							}
							pos = linea.indexOf('}');
							if (pos >= 0) {
								if (esArchivo) {
									System.out.println("llaves-- 1");
								}
								llaves--;
								while (pos >= 0) {
									//system.out.println("n");
									pos = linea.indexOf('}', pos+1);
									if (pos >= 0) {
										if (esArchivo) {
											System.out.println("llaves-- 2");
										}
										llaves--;
									}
								}
							}
						}
						pos = linea.indexOf('(');
						if (pos < 0) {
							if (empezoMetodo
									&& llaves == 0) {
								empezoMetodo = false;
								continue;
							}
							if (llaves == -1) {
								continue;
							}
						}

						if (empezoMetodo) {
							if (esArchivo && "selectInternacionEqual".equals(metodo)) {
								System.out.println(linea);
							}
							pos = linea.indexOf("BusinessFactory.get");
							if (pos >= 0) {
								String[] array = null;
								String interfaz = null;
								array = linea.split("\\.");
								if (array.length < 3) {
									System.out.println("corregir delegator " + "ar.com.thinksoft.delegators." + archivoNombre + "." + metodo + "(" + archivoNombre + ".java:" + lineNumber + ")" + " " + linea);
								}
								else {
									interfaz = "Imp" + array[1].substring(6, array[1].length() - 2);

									if (!salida.containsKey(interfaz)) {
										salida.put(interfaz, new HashMap<String, Integer>());
									}

									metodo = array[2];
									int parentesis = metodo.indexOf("(");
									if (parentesis < 0) {
										parentesis = Integer.MAX_VALUE;
									}

									int espacio = metodo.indexOf(" ");
									if (espacio < 0) {
										espacio = Integer.MAX_VALUE;
									}

									int igual = metodo.indexOf("=");
									if (igual < 0) {
										igual = Integer.MAX_VALUE;
									}

									if (igual < parentesis && igual < espacio) {
										metodo = metodo.substring(0, igual);
									}
									else if (parentesis < igual && parentesis < espacio) {
										metodo = metodo.substring(0, parentesis);
									}
									else if (espacio < igual && espacio < parentesis) {
										metodo = metodo.substring(0, espacio);
									}
									else {
										System.out.println("contemplar metodo: " + metodo);
									}

									if (!salida.get(interfaz).containsKey(array[2])) {
										salida.get(interfaz).put(metodo, 0);
									}

									salida.get(interfaz).put(metodo, salida.get(interfaz).get(metodo) + 1);
								}
							}
						}
						else {
							pos = linea.indexOf('(');
							if (pos < 0) {
								//									System.out.println("\tlinea no tratada: " + archivoNombre + " - " + linea);
								continue;
							}
							empezoMetodo = true;

							String str = linea.substring(0, pos+1);
							pos = str.indexOf('(');
							try {
								metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();
							} catch (Exception e) {
								System.out.println("e1 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
								e.printStackTrace();
							}
							if (esArchivo && "selectInternacionEqual".equals(metodo)) {
								System.out.println("selectInternacionEqual 2 " + metodo);
							}

							if (map != null && !map.containsKey(metodo)) {
								String select = null;
								String selectEqual = null;
								String selectEqualOne = null;
								String selectLike = null;
								String selectLikeOne = null;
								String delete = null;
								String insert = null;
								String update = null;
								String tmp = null;

								//si no ignoro los metodos default de IntBusFacadeParent pasa de 800 a 3.600
								if (linea.contains("public static void")) {
									try {
										tmp = linea.substring(pos + 1, linea.indexOf(" ", pos + 1));
										delete = "delete" + tmp;
										insert = "insert" + tmp;
										update = "update" + tmp;
									} catch (Exception e) {
										System.out.println("e20 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
									}
								}
								else {
									if (linea.contains("public static List<")) {
										try {
											pos = linea.indexOf("List<");
											tmp = linea.substring(pos + 5, linea.indexOf(">"));
											select = "select" + tmp;
											selectEqual = "select" + tmp + "Equal";
											selectEqualOne = "select" + tmp + "EqualOne";
											selectLike = "select" + tmp + "Like";
											selectLikeOne = "select" + tmp + "LikeOne";
										} catch (Exception e) {
											System.out.println("e21 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
										}
									}
									else {
										try {
											pos = linea.indexOf("public static ");
											tmp = linea.substring(pos + 14, linea.indexOf(" ", pos + 15));
											select = "select" + tmp;
											selectEqual = "select" + tmp + "Equal";
											selectEqualOne = "select" + tmp + "EqualOne";
											selectLike = "select" + tmp + "Like";
											selectLikeOne = "select" + tmp + "LikeOne";
										} catch (Exception e) {
											System.out.println("e22 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
										}
									}
								}

								if (!metodo.equals(select)
										&& !metodo.equals(selectEqual)
										&& !metodo.equals(selectEqualOne)
										&& !metodo.equals(selectLike)
										&& !metodo.equals(selectLikeOne)
										&& !metodo.equals(delete)
										&& !metodo.equals(insert)
										&& !metodo.equals(update)) {
									result.add("ar.com.thinksoft.delegators." + archivoNombre + "." + metodo + "(" + archivoNombre + ".java:" + lineNumber + ")");
								}
							}

							pos = linea.indexOf('{');
							while (pos < 0) {
								//system.out.println("m");
								linea = br.readLine();
								lineNumber++;
								try {
									pos = linea.indexOf('{');
								} catch (Exception e) {
									pos = 0;
									System.out.println("e2 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
									e.printStackTrace();
								}
								//system.out.println("\t" + linea);
							}
							if (llaves == 0) {
								llaves++;
							}
						}
					}
					else {
						empezoClase = linea.contains("{") && !linea.contains("@");
						if (empezoClase && esArchivo) {
							System.out.println("es archivo 4");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fr != null) {
						fr.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					if (br != null) {
						br.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		return salida;
	}

	private Map<String, Map<String, Integer>> listarBusiness(Map<String, Map<String, Integer>> llamadas, List<String> result, File f, FileFilter filter) throws BusinessException {
		Map<String, Map<String, Integer>> salida = new HashMap<String, Map<String,Integer>>();

		//System.out.println("listarBusiness");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		System.out.println("listarBusiness(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		String archivoNombre = null;
		for (int x = 0; x < ficheros.length; x++) {
			archivoNombre = ficheros[x].getName().replace(".java", "");

			if (ficheros[x].isDirectory()) {
				if (".svn".equals(ficheros[x].getName())
						|| "generix".equals(ficheros[x].getName())) {
					continue;
				}
				addDelegators(salida, listarBusiness(llamadas, result, ficheros[x], filter));
			}
			else {
				if (ficheros[x].getName().startsWith("BusinessFactory")
						|| ficheros[x].getName().startsWith("ImpBusFacadeParent")
						|| ficheros[x].getName().startsWith("IntBus")
						|| ficheros[x].getName().startsWith("RespuestaValidacion")) {
					continue;
				}

				//				System.out.println(ficheros[x].getName());

				String pck = ficheros[x].getParent().split("business")[1];
				pck = pck.split("\\\\")[1];

				//				boolean esArchivo = "ImpBusInterfacesMigracion".equals(archivoNombre);
				boolean esArchivo = false;
				boolean debug = false;

				FileReader fr = null;
				BufferedReader br = null;
				int lineNumber = 0;
				Map<String, Integer> map = llamadas.get(archivoNombre);
				if (map == null) {
					result.add(archivoNombre);
				}
				if (esArchivo) {
					System.out.println("llaves++ 1");
				}
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;
					int pos = 0;
					String metodo = null;
					boolean empezoClase = false;
					boolean empezoMetodo = false;

					int llaves = 0;
					while ((linea = br.readLine()) != null) {
						lineNumber++;

						if (linea.indexOf('@') >= 0) {
							continue;
						}

						linea = linea.replaceAll("\t", "").trim();
						if (linea.length() == 0
								|| linea.startsWith("//")) {
							continue;
						}
						if (linea.startsWith("/*")) {
							pos = linea.indexOf("*/");
							while (pos < 0) {
								linea = br.readLine();
								lineNumber++;
								pos = linea.indexOf("*/");
							}
							continue;
						}

						if (empezoClase) {
							if (esArchivo && debug) {
								System.out.println("debug 2");
							}
							if ((linea.contains("static") || linea.contains("public") || linea.contains("private"))
									&& linea.contains("enum")) {
								if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
										|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
									pos = linea.indexOf('}');
									while (pos < 0) {
										//system.out.println("x");
										linea = br.readLine();
										lineNumber++;
										pos = linea.indexOf('}');
										//system.out.println("\t" + linea);
									}
								}
								continue;
							}

							if (((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
									|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0))
									&& !linea.contains("actionSelectMenu")) {
								pos = linea.indexOf('{');
								if (pos >= 0) {
									if (esArchivo) {
										System.out.println("llaves++ 1");
									}
									llaves++;
									while (pos >= 0) {
										pos = linea.indexOf('{', pos+1);
										if (pos >= 0) {
											if (esArchivo) {
												System.out.println("llaves++ 2");
											}
											llaves++;
										}
									}
								}
								pos = linea.indexOf('}');
								if (pos >= 0) {
									if (esArchivo) {
										System.out.println("llaves-- 1");
									}
									llaves--;
									while (pos >= 0) {
										pos = linea.indexOf('}', pos+1);
										if (pos >= 0) {
											if (esArchivo) {
												System.out.println("llaves-- 2");
											}
											llaves--;
										}
									}
								}
							}
							pos = linea.indexOf('(');
							if (pos < 0) {
								if (empezoMetodo
										&& llaves == 0) {
									empezoMetodo = false;
									continue;
								}
								if (llaves == -1) {
									continue;
								}
							}

							if (empezoMetodo) {
								pos = linea.indexOf("PersistanceFactory.get");
								if (pos >= 0
										&& !linea.contains("PersistanceFactory.getIntDomain")) {
									if (esArchivo) {
										System.out.println(linea);
									}
									String[] array = null;
									String interfaz = null;
									array = linea.split("PersistanceFactory")[1].split("\\.");
									if (array.length < 3) {
										System.out.println("corregir business " + "ar.com.thinksoft.business." + pck + ".implementations." + archivoNombre + "." + metodo + "(" + archivoNombre + ".java:" + lineNumber + ")" + " " + linea);
									}
									else {
										interfaz = "Imp" + array[1].substring(6, array[1].length() - 2);

										if (!salida.containsKey(interfaz)) {
											salida.put(interfaz, new HashMap<String, Integer>());
										}

										if(esArchivo) {
											System.out.println(linea);
										}

										metodo = array[2];
										int parentesis = metodo.indexOf("(");
										if (parentesis < 0) {
											parentesis = Integer.MAX_VALUE;
										}

										int espacio = metodo.indexOf(" ");
										if (espacio < 0) {
											espacio = Integer.MAX_VALUE;
										}

										int igual = metodo.indexOf("=");
										if (igual < 0) {
											igual = Integer.MAX_VALUE;
										}

										if (igual < parentesis && igual < espacio) {
											metodo = metodo.substring(0, igual);
										}
										else if (parentesis < igual && parentesis < espacio) {
											metodo = metodo.substring(0, parentesis);
										}
										else if (espacio < igual && espacio < parentesis) {
											metodo = metodo.substring(0, espacio);
										}
										else {
											System.out.println("contemplar metodo: " + metodo);
										}

										if (!salida.get(interfaz).containsKey(array[2])) {
											salida.get(interfaz).put(metodo, 0);
										}

										salida.get(interfaz).put(metodo, salida.get(interfaz).get(metodo) + 1);
									}
								}
							}
							else {
								pos = linea.indexOf('(');
								if (pos < 0) {
									//									System.out.println("\tlinea no tratada: " + archivoNombre + " - " + linea);
									continue;
								}
								empezoMetodo = true;

								String str = linea.substring(0, pos+1);
								pos = str.indexOf('(');
								try {
									metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();
								} catch (Exception e) {
									System.out.println("e1 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
									e.printStackTrace();
								}
								if (esArchivo && "infoPrestacion".equals(metodo)) {
									System.out.println("infoPrestacion 2 " + metodo);
								}

								if (map != null && !map.containsKey(metodo)) {
									result.add("ar.com.thinksoft.business." + pck + "." + archivoNombre + "." + metodo + "(" + archivoNombre + ".java:" + lineNumber + ")");
								}

								pos = linea.indexOf('{');
								while (pos < 0) {
									linea = br.readLine();
									lineNumber++;
									try {
										pos = linea.indexOf('{');
									} catch (Exception e) {
										pos = 0;
										System.out.println("e2 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
										e.printStackTrace();
									}
								}
								if (llaves == 0) {
									llaves++;
								}
							}
						}
						else {
							empezoClase = linea.contains("{") && !linea.contains("@");
							if (empezoClase && esArchivo) {
								System.out.println("es archivo 4");
							}
							pos = linea.indexOf(" ImpBusFacadeParent");
							if (pos >= 0) {
								try {
									String dto = linea.substring(linea.indexOf("<") + 1, linea.indexOf(">"));
									if (!("ImpBus" + dto).equals(archivoNombre)) {
										result.add(archivoNombre + " Mal nombrado");
									}
								} catch (Exception e) {
									System.out.println("e30 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
									e.printStackTrace();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (fr != null) {
							fr.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					try {
						if (br != null) {
							br.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}

		return salida;
	}

	private void listarPersistance(Map<String, Map<String, Integer>> llamadas, List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarPersistance");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		System.out.println("listarPersistance(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		String archivoNombre = null;
		for (int x = 0; x < ficheros.length; x++) {
			archivoNombre = ficheros[x].getName().replace(".java", "");

			if (ficheros[x].isDirectory()) {
				if (".svn".equals(ficheros[x].getName())
						|| "generix".equals(ficheros[x].getName())) {
					continue;
				}
				listarPersistance(llamadas, result, ficheros[x], filter);
			}
			else {
				if (ficheros[x].getName().startsWith("PersistanceFactory")
						|| ficheros[x].getName().startsWith("ImpDomain")
						|| ficheros[x].getName().startsWith("Int")) {
					continue;
				}
				System.out.println(ficheros[x].getParent());
				String pck = null;
				try {
					pck = ficheros[x].getParent().split("persistance")[1];
				} catch (Exception e1) {
					System.out.println(ficheros[x].getParent());
					e1.printStackTrace();
					continue;
				}
				pck = pck.split("\\\\")[1];

				boolean esArchivo = false;
				boolean debug = false;

				FileReader fr = null;
				BufferedReader br = null;
				int lineNumber = 0;
				Map<String, Integer> map = llamadas.get(archivoNombre);
				if (map == null) {
					result.add(archivoNombre);
				}
				if (esArchivo) {
					System.out.println("llaves++ 1");
				}
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;
					int pos = 0;
					String metodo = null;
					boolean empezoClase = false;
					boolean empezoMetodo = false;

					int llaves = 0;
					while ((linea = br.readLine()) != null) {
						lineNumber++;

						if (linea.indexOf('@') >= 0) {
							continue;
						}

						linea = linea.replaceAll("\t", "").trim();
						if (linea.length() == 0
								|| linea.startsWith("//")) {
							continue;
						}
						if (linea.startsWith("/*")) {
							pos = linea.indexOf("*/");
							while (pos < 0) {
								linea = br.readLine();
								lineNumber++;
								pos = linea.indexOf("*/");
							}
							continue;
						}

						if (empezoClase) {
							if (esArchivo && debug) {
								System.out.println("debug 2");
							}
							if ((linea.contains("static") || linea.contains("public") || linea.contains("private"))
									&& linea.contains("enum")) {
								if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
										|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
									pos = linea.indexOf('}');
									while (pos < 0) {
										//system.out.println("x");
										linea = br.readLine();
										lineNumber++;
										pos = linea.indexOf('}');
										//system.out.println("\t" + linea);
									}
								}
								continue;
							}

							if (((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
									|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0))
									&& !linea.contains("actionSelectMenu")) {
								pos = linea.indexOf('{');
								if (pos >= 0) {
									if (esArchivo) {
										System.out.println("llaves++ 1");
									}
									llaves++;
									while (pos >= 0) {
										pos = linea.indexOf('{', pos+1);
										if (pos >= 0) {
											if (esArchivo) {
												System.out.println("llaves++ 2");
											}
											llaves++;
										}
									}
								}
								pos = linea.indexOf('}');
								if (pos >= 0) {
									if (esArchivo) {
										System.out.println("llaves-- 1");
									}
									llaves--;
									while (pos >= 0) {
										pos = linea.indexOf('}', pos+1);
										if (pos >= 0) {
											if (esArchivo) {
												System.out.println("llaves-- 2");
											}
											llaves--;
										}
									}
								}
							}
							pos = linea.indexOf('(');
							if (pos < 0) {
								if (empezoMetodo
										&& llaves == 0) {
									empezoMetodo = false;
									continue;
								}
								if (llaves == -1) {
									continue;
								}
							}

							if (empezoMetodo) {
								//no hago nada
							}
							else {
								pos = linea.indexOf('(');
								if (pos < 0) {
									//									System.out.println("\tlinea no tratada: " + archivoNombre + " - " + linea);
									continue;
								}
								empezoMetodo = true;

								String str = linea.substring(0, pos+1);
								pos = str.indexOf('(');
								try {
									metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();
								} catch (Exception e) {
									System.out.println("e1 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
									e.printStackTrace();
								}
								if (esArchivo && "infoPrestacion".equals(metodo)) {
									System.out.println("infoPrestacion 2 " + metodo);
								}

								if (map != null && !map.containsKey(metodo)) {
									result.add("ar.com.thinksoft.persistance." + pck + "." + archivoNombre + "." + metodo + "(" + archivoNombre + ".java:" + lineNumber + ")");
								}

								pos = linea.indexOf('{');
								while (pos < 0) {
									linea = br.readLine();
									lineNumber++;
									try {
										pos = linea.indexOf('{');
									} catch (Exception e) {
										pos = 0;
										System.out.println("e2 " + archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
										e.printStackTrace();
									}
								}
								if (llaves == 0) {
									llaves++;
								}
							}
						}
						else {
							empezoClase = linea.contains("{") && !linea.contains("@");
							if (empezoClase && esArchivo) {
								System.out.println("es archivo 4");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (fr != null) {
							fr.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					try {
						if (br != null) {
							br.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		UnusedCode m = new UnusedCode();
		List<String> result = new ArrayList<String>();
		try {
			String business = "\\src\\ar\\com\\thinksoft\\business\\";
			String persistance = "\\src\\ar\\com\\thinksoft\\persistance\\";

			Map<String, Map<String, Integer>> delegatorsCalls = new HashMap<String, Map<String, Integer>>();

			m.analizarProyecto("AGE", result, delegatorsCalls);
			m.analizarProyecto("AGI", result, delegatorsCalls);
			m.analizarProyecto("AGP", result, delegatorsCalls);
			m.analizarProyecto("HOS-APP", result, delegatorsCalls);
			m.analizarProyecto("HOSPITAL", result, delegatorsCalls);
			m.analizarProyecto("SCHEDULER", result, delegatorsCalls);
			m.analizarProyecto("ANMAT", result, delegatorsCalls);
			m.analizarProyecto("WS-HOSPITAL", result, delegatorsCalls);

			System.out.println();
			System.out.println("*************************************************");
			System.out.println("\tlistarBusiness");
			System.out.println("*************************************************");
			System.out.println();
			result.add("\n\nBUSINESS");
			Map<String, Map<String, Integer>> businessCalls = m.listarBusiness(delegatorsCalls, result, new File(FinallySearch.dirWorkspace + "HOSPITAL-BUSINESS" + business), new OtherFileFilter("java"));

			System.out.println();
			System.out.println("*************************************************");
			System.out.println("\tlistarPersistance");
			System.out.println("*************************************************");
			System.out.println();
			result.add("\n\nPERSISTANCE");
			m.listarPersistance(businessCalls, result, new File(FinallySearch.dirWorkspace + "HOSPITAL-BUSINESS" + persistance), new OtherFileFilter("java"));

			//			for (String s : result) {
			//				System.out.println(s);
			//			}

			generarArchivo("9-unused", result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void analizarProyecto(String proyecto, List<String> result, Map<String, Map<String, Integer>> delegatorsCalls) throws BusinessException {
		Map<String, Map<String, Integer>> llamadas = new HashMap<String, Map<String, Integer>>();
		String beans = "\\src\\ar\\com\\thinksoft\\beans\\";
		String delegators = "\\src\\ar\\com\\thinksoft\\delegators\\";

		if (proyecto.equals("ANMAT")
				|| proyecto.equals("SCHEDULER")) {
			beans = "\\src\\ar\\com\\thinksoft\\scheduler\\jobs\\";
		}

		System.out.println();
		System.out.println("*************************************************");
		System.out.println("\tlistarBeans " + proyecto + "");
		System.out.println("*************************************************");
		System.out.println();

		listarBeans(llamadas, result, new File(FinallySearch.dirWorkspace + "" + proyecto + "" + beans), new OtherFileFilter("java"));
		System.out.println("llamadas post " + proyecto + " beans: " + llamadas.keySet().size());

		System.out.println();
		System.out.println("*************************************************");
		System.out.println("\tlistarDelegators " + proyecto + "");
		System.out.println("*************************************************");
		System.out.println();

		result.add("\n\n" + proyecto + "");
		llamadas = listarDelegators(llamadas, result, new File(FinallySearch.dirWorkspace + "" + proyecto + "" + delegators), new OtherFileFilter("java"));
		addDelegators(delegatorsCalls, llamadas);
		System.out.println("llamadas post " + proyecto + " delegators: " + delegatorsCalls.keySet().size());
	}

	private static void addDelegators(Map<String, Map<String, Integer>> delegatorsCalls, Map<String, Map<String, Integer>> llamadas) {
		for (String d : llamadas.keySet()) {
			if (!delegatorsCalls.containsKey(d)) {
				delegatorsCalls.put(d, new HashMap<String, Integer>());
			}

			for (String s : llamadas.get(d).keySet()) {
				if (!delegatorsCalls.get(d).containsKey(s)) {
					delegatorsCalls.get(d).put(s, 0);
				}

				delegatorsCalls.get(d).put(s, delegatorsCalls.get(d).get(s) + 1);
			}
		}
	}

	public static void generarArchivo(String nombre, List<String> result) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

		File outFile = null;
		BufferedWriter writer = null;
		String fileName = FinallySearch.pathOutput+"/"+nombre+"_"+sdf.format(new Date())+".txt";
		try {
			outFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outFile));
			for (String s : result) {
				writer.write(s+"\n");
			}
			writer.newLine();
			writer.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				outFile=null;
				writer  = null;
			} catch (IOException ex) {
			}
		}

		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			try {
				Runtime.getRuntime().exec ("C:\\Program Files (x86)\\Notepad++\\notepad++.exe " + fileName);
			} catch (Exception e) {
				try {
					Runtime.getRuntime().exec ("C:\\Program Files\\Notepad++\\notepad++.exe " + fileName);
				} catch (Exception e1) {
					try {
						Runtime.getRuntime().exec ("C:\\Archivos de programa (x86)\\Notepad++\\notepad++.exe " + fileName);
					} catch (Exception e2) {
						try {
							Runtime.getRuntime().exec ("C:\\Archivos de programa\\Notepad++\\notepad++.exe " + fileName);
						} catch (Exception e3) {
							try {
								Runtime.getRuntime().exec ("C:/Windows/System32/notepad.exe " + fileName);
							} catch (Exception e4) {
							}
						}
					}
				}
			}
		}
		else {
			try {
				Runtime.getRuntime().exec ("kate " + fileName);
			} catch (Exception e) {
			}
		}
	}

}

class OtherFileFilter implements FileFilter {

	private String extension;

	OtherFileFilter(String extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(File file) {
		boolean isDir = file.isDirectory();
		boolean esExt = file.getName().toLowerCase().endsWith(extension);
		boolean parentIsInterfaces = "interfaces".equals(file.getParentFile().getName());
		boolean startsWithInt = file.getName().startsWith("Int");
		boolean acepta = (isDir || (esExt && (!parentIsInterfaces || !startsWithInt)));
		//		if (acepta) {
		//			System.out.println(file + " " + isDir + " " + esExt + " " + parentIsInterfaces + " " + startsWithInt);
		//		}
		return acepta;
	}

}
