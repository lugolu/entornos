package ar.com.thinksoft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ar.com.thinksoft.exception.BusinessException;


public class FetchSearch {

	private static final String dir = "d:\\Desarrollo\\Hospital\\Ver 9\\Codigo de DBMS\\";
	private static final String dirPck = dir + "Paquetes\\";
	private static final String dirTmp = dir + "Tablas_Temporales\\";
	private static final String dirTri = dir + "Disparadores_Especiales\\";

	private List<String> listarDirectorio(List<String> result, File f, FileFilter filter) throws BusinessException {
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("mantenimiento")
					|| ficheros[x].getName().startsWith("LUCAS")
					|| ficheros[x].getName().startsWith("lucas")
					|| ficheros[x].getName().startsWith("MODELO")
					|| ficheros[x].getName().startsWith("modelo")
					|| ficheros[x].getName().contains("~")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				continue;
			} else {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;
					int pos = 0;
					@SuppressWarnings("unused")
					int nroMetodo = 0;
					Map<String, Integer> fetchs = new HashMap<String, Integer>();
					String metodo = null;
					boolean empezoClase = false;
					boolean empezoMetodo = false;

					if (ficheros[x].getName().toLowerCase().contains("compra")) {
						System.out.println("oa");
					}
					int llaves = 0;
					while ((linea = br.readLine()) != null) {
						if (empezoClase) {
							linea = linea.replaceAll("\t", "").trim();
							if (linea.startsWith("/*")) {
								pos = linea.indexOf("*/");
								while (pos < 0) {
									linea = br.readLine();
									linea = linea.replaceAll("\t", "").trim();
									pos = linea.indexOf("*/");
								}
								continue;
							}
							if (linea.length() == 0
									|| linea.startsWith("--")) {
								continue;
							}
							//							pos = (metodo != null ? metodo.toLowerCase().indexOf("f_genera_digito_verificador") : -1);
							//							if (pos >= 0) {
							//								System.out.println(linea);
							//							}
							pos = linea.toLowerCase().indexOf("rollback");
							if (pos >= 0) {
								result.add(log(linea, llaves, ficheros[x], metodo, "rollback"));
								continue;
							}
							pos = linea.toLowerCase().indexOf("commit");
							if (pos >= 0) {
								result.add(log(linea, llaves, ficheros[x], metodo, "commit"));
								continue;
							}

							Iterator<Entry<String, Integer>> it = fetchs.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<String, Integer> e = it.next();
								if (linea.toLowerCase().contains(e.getKey())
										&& linea.toLowerCase().contains(":=")) {
									fetchs.put(e.getKey(), fetchs.get(e.getKey())+1);
								}
							}

							pos = linea.toLowerCase().indexOf("close");
							if (pos >= 0
									&& linea.toLowerCase().indexOf("close_cursor") < 0) {
								String aux = linea.toLowerCase().substring(pos+5, linea.toLowerCase().indexOf(";")).trim();
								if (fetchs.containsKey(aux)) {
									fetchs.put(aux, fetchs.get(aux)-1);
									continue;
								}
							}
							pos = linea.toLowerCase().indexOf("begin");
							if (pos >= 0) {
								llaves++;
								//								log(linea, llaves, ficheros[x], metodo, "begin");
								continue;
							}
							pos = linea.toLowerCase().indexOf("for ");
							if (pos >= 0
									&& linea.toLowerCase().indexOf("end") < 0
									&& linea.toLowerCase().indexOf("open") < 0
									&& linea.toLowerCase().indexOf("update") < 0) {
								llaves++;
								if (linea.indexOf("(") > 0) {
									pos = linea.indexOf(")");
									while (pos < 0) {
										linea = br.readLine();
										pos = linea.indexOf(")");
									}
								}
								pos = linea.toLowerCase().indexOf("loop");
								while (pos < 0) {
									linea = br.readLine();
									if (linea == null) {
										break;
									}
									pos = linea.toLowerCase().indexOf("loop");
								}
								continue;
							}
							pos = linea.toLowerCase().indexOf("loop");
							if (pos >= 0
									&& linea.toLowerCase().indexOf("end") < 0) {
								llaves++;
								continue;
							}
							pos = linea.toLowerCase().indexOf("end");
							if (pos >= 0
									&& (linea.toLowerCase().indexOf(";") > 0
											|| linea.toLowerCase().trim().equals("end"))
									&& linea.toLowerCase().indexOf("if;") < 0
									&& linea.toLowerCase().indexOf("if ;") < 0
									&& linea.toLowerCase().indexOf("select") < 0
									&& linea.toLowerCase().indexOf("=") < 0) {
								if (linea.toLowerCase().trim().equals("end")) {
									pos = linea.toLowerCase().indexOf("loop");
									while (pos < 0) {
										linea = br.readLine();
										pos = linea.toLowerCase().indexOf("loop");
									}
								}
								llaves--;
								if (empezoMetodo
										&& llaves <= 0) {
									empezoMetodo = false;
								}
								continue;
							}

							if (empezoMetodo) {
							}
							else {
								pos = linea.toLowerCase().indexOf("procedure");
								if (pos < 0) {
									pos = linea.toLowerCase().indexOf("function");
									if (pos < 0) {
										continue;
									}
								}
								empezoMetodo = true;

								it = fetchs.entrySet().iterator();
								while (it.hasNext()) {
									Map.Entry<String, Integer> e = it.next();
									if (e.getValue() != 0) {
										result.add(log(linea, llaves, ficheros[x], metodo, "fetch " + e.getKey() + "-" + e.getValue()));
									}
								}
								fetchs = new HashMap<String, Integer>();

								metodo = null;
								String aux = "";
								while (metodo == null) {
									try {
										aux += linea.toLowerCase().trim();
									} catch (Exception e) {
										e.printStackTrace();
									}
									pos = aux.indexOf(" is");
									if (pos > 0) {
										metodo = "";
										break;
									}
									else {
										pos = aux.indexOf(")is");
										if (pos > 0) {
											metodo = "";
											break;
										}
										else {
											pos = aux.indexOf("is");
											if (pos > 0
													&& linea.toLowerCase().trim().equals("is")) {
												metodo = "";
												break;
											}
											else {
												pos = aux.indexOf(" as");
												if (pos > 0) {
													metodo = "";
													break;
												}
												else {
													pos = aux.indexOf("as");
													if (pos > 0
															&& linea.toLowerCase().trim().equals("as")) {
														metodo = "";
														break;
													}
												}
											}
										}
									}
									linea = br.readLine();
								}
								aux = aux.trim();
								try {
									pos = aux.indexOf('(');
									if (pos > 0) {
										metodo = aux.substring(aux.indexOf(' '), pos).trim();
									}
									else {
										pos = aux.indexOf("return");
										if (pos > 0) {
											metodo = aux.substring(aux.indexOf(' '), pos).trim();
										}
										else {
											pos = aux.indexOf(" is");
											if (pos > 0) {
												metodo = aux.substring(aux.indexOf(' '), pos).trim();
											}
										}
									}
									nroMetodo++;
								} catch (Exception e) {
									System.out.println(e.getMessage() + " + " + metodo + " + " + linea +"[2]"+ e.getMessage());
								}

								pos = linea.toLowerCase().indexOf("result_set");
								if (pos >= 0
										&& linea.indexOf(metodo) < 0
										&& linea.indexOf("return") < 0) {
									aux = linea.trim();
									aux = aux.split(" ")[0];
									fetchs.put(aux, 0);
								}
								//parametro out
								if (pos >= 0
										&& (pos < linea.indexOf(")")
												|| (linea.indexOf("out ") >= 0 && linea.indexOf(")") < 0))) {
									aux = linea.trim();
									aux = aux.split(" ")[0];
									fetchs.put(aux, -1);
								}

								pos = linea.toLowerCase().indexOf("begin");
								while (pos < 0) {
									pos = linea.toLowerCase().indexOf("result_set");
									if (pos >= 0
											&& linea.indexOf(metodo) < 0
											&& linea.indexOf("return") < 0) {
										aux = linea.trim();
										aux = aux.split(" ")[0];
										fetchs.put(aux, 0);
									}
									//parametro out
									if (pos >= 0
											&& (pos < linea.indexOf(")")
													|| (linea.indexOf("out ") >= 0 && linea.indexOf(")") < 0))) {
										aux = linea.trim();
										aux = aux.split(" ")[0];
										fetchs.put(aux, -1);
									}

									linea = br.readLine();
									pos = linea.toLowerCase().indexOf("begin");
								}
								llaves++;
							}
						}
						else {
							linea = linea.replaceAll("\t", "").trim();
							if (linea.startsWith("/*")) {
								pos = linea.indexOf("*/");
								while (pos < 0) {
									linea = br.readLine();
									linea = linea.replaceAll("\t", "").trim();
									pos = linea.indexOf("*/");
								}
								continue;
							}
							if (linea.length() == 0
									|| linea.startsWith("--")) {
								continue;
							}
							empezoClase = linea.toLowerCase().contains("end")
									&& linea.toLowerCase().contains(ficheros[x].getName().toLowerCase().replace(".pck", ""));
							if (empezoClase) {
								nroMetodo= 0;
								pos = linea.toLowerCase().indexOf("/");
								while (pos < 0) {
									linea = br.readLine();
									pos = linea.toLowerCase().indexOf("/");
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
		return result;
	}

	private List<String> listarTmp(List<String> result, File f, FileFilter filter) throws BusinessException {
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().contains("~")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				continue;
			} else {
				result.add("@..\\tablas_temporales\\" + ficheros[x].getName() + ";");
			}
		}
		return result;
	}

	private List<String> listarPck(List<String> result, File f, FileFilter filter) throws BusinessException {
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("mantenimiento")
					|| ficheros[x].getName().startsWith("LUCAS")
					|| ficheros[x].getName().startsWith("lucas")
					|| ficheros[x].getName().startsWith("MODELO")
					|| ficheros[x].getName().startsWith("modelo")
					|| ficheros[x].getName().contains("~")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				continue;
			} else {
				result.add("@../Paquetes/" + ficheros[x].getName());
			}
		}
		return result;
	}

	private List<String> listarTri(List<String> result, File f, FileFilter filter) throws BusinessException {
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().contains("~")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				continue;
			} else {
				result.add("@..\\Disparadores_Especiales\\" + ficheros[x].getName());
			}
		}
		return result;
	}

	private String log(String linea, int llaves, File f, String metodo, String mensaje) {
		return ("\t" + f.getName().replace(".pck", "") + " - " + metodo + ": " + mensaje + "[" + llaves + "]" + (linea != null ? "[" + linea + "]" : ""));
	}

	public FetchSearch() {
		List<String> result = new ArrayList<String>();
		try {
			listarDirectorio(result, new File(dirPck), new MyFileFilter("pck"));
			Collections.sort(result);

			result.add("");
			result.add("");
			result.add("");
			List<String> tmp = new ArrayList<String>();
			listarTmp(tmp, new File(dirTmp), new MyFileFilter("sql"));
			Collections.sort(tmp, new SortIgnore());
			result.addAll(tmp);

			result.add("");
			result.add("");
			result.add("");
			tmp = new ArrayList<String>();
			listarPck(tmp, new File(dirPck), new MyFileFilter("pck"));
			Collections.sort(tmp, new SortIgnore());
			result.addAll(tmp);

			result.add("");
			result.add("");
			result.add("");
			tmp = new ArrayList<String>();
			listarTri(tmp, new File(dirTri), new MyFileFilter("sql"));
			Collections.sort(tmp, new SortIgnore());
			result.addAll(tmp);

			FinallySearch.generarArchivo(FinallySearch.pathOutput, "9-fetch", "txt", result, true);
			System.out.println(result.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new FetchSearch();
	}

	public class SortIgnore implements Comparator<Object> {
		@Override
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.replace("_", "").toLowerCase().compareTo(s2.toLowerCase().replace("_", ""));
		}
	}

}
