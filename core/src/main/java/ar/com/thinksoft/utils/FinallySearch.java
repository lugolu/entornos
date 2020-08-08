package ar.com.thinksoft.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ar.com.thinksoft.exception.BusinessException;


public class FinallySearch {

	static final String dirWorkspace = "c:\\desarrollo\\Workspace\\hospital9\\";
	static final String pathOutput = "C:\\Users\\thinksoft\\Downloads\\ana\\";

	//	static final String dirWorkspace = "D:\\workspace\\neon2\\hospital9\\";
	//	static final String pathOutput = "C:\\Users\\lucas\\Downloads\\ana\\";

	final boolean ctrlCatchBusiness = false;

	private List<String> listarBeansRepetidos(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarBeansRepetidos");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarBeansRepetidos(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("Int")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				listarBeansRepetidos(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;

					List<String> bbs = new ArrayList<String>(0);
					while ((linea = br.readLine()) != null) {
						linea = linea.replaceAll("\t", "").trim();

						if (linea.indexOf("<!--") >= 0) {
							continue;
						}

						if (linea.indexOf("{bb") >= 0
								&& linea.indexOf(".") >= 0) {
							String str = null;
							try {
								str = linea.substring(linea.indexOf("{bb") + 1, linea.indexOf(".", linea.indexOf("{bb")));
							} catch (Exception e) {
								System.out.println(ficheros[x] + " " + e.getMessage() + " + " + linea);
								e.printStackTrace();
							}
							if ("bbUserData".equals(str)
									|| "bbApplicationData".equals(str)
									|| "bbBuscadorConvenio".equals(str)
									|| "bbDefaultBean".equals(str)
									|| "bbSessionData".equals(str)) {
								continue;
							}
							if (!bbs.contains(str)) {
								bbs.add(str);
							}
						}
						else if (linea.indexOf(" bb") >= 0) {
							String str = linea.substring(linea.indexOf(" bb") + 1, linea.indexOf(".", linea.indexOf(" bb")));
							if ("bbUserData".equals(str)
									|| "bbApplicationData".equals(str)
									|| "bbBuscadorConvenio".equals(str)
									|| "bbDefaultBean".equals(str)
									|| "bbSessionData".equals(str)) {
								continue;
							}
							if (!bbs.contains(str)) {
								bbs.add(str);
							}
						}
					}
					if (bbs.size() > 1) {
						for (String s : bbs) {
							result.add("\t" + ficheros[x].getName() + " - " + s);
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

	private List<String> listarRowsSinPaginator(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarRowsSinPaginator");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarRowsSinPaginator(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("Int")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				listarRowsSinPaginator(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;

					boolean encontroDataTable = false;
					String dataTable = null;
					boolean hasRows = false;
					String rows = null;
					boolean hasPaginator = false;

					while ((linea = br.readLine()) != null) {
						linea = linea.replaceAll("\t", "").trim();

						if (linea.indexOf("<!--") >= 0) {
							continue;
						}

						if (linea.indexOf("<p:dataTable") >= 0) {
							encontroDataTable = true;
						}
						if (encontroDataTable && dataTable == null && linea.indexOf("value=") >= 0) {
							int pos = linea.indexOf("value=");
							dataTable = linea.substring(linea.indexOf(".", pos + 10) + 1, linea.indexOf("}", pos + 10));
						}
						if (encontroDataTable && rows == null && linea.indexOf("rows=") >= 0) {
							hasRows = true;
							int pos = linea.indexOf("rows=");
							rows = linea.substring(pos + 6, linea.indexOf("\"", pos + 6));
						}
						if (encontroDataTable && linea.indexOf("paginator") >= 0) {
							hasPaginator = true;
						}
						if (linea.indexOf(">") >= 0) {
							if (hasRows && !hasPaginator) {
								result.add("\t" + ficheros[x].getName() + ": " + dataTable + " - rows (" + rows + ") sin paginator");
							}
							if (!hasRows && hasPaginator) {
								result.add("\t" + ficheros[x].getName() + ": " + dataTable + " - paginator sin rows");
							}

							encontroDataTable = false;
							dataTable = null;
							hasRows = false;
							rows = null;
							hasPaginator = false;
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

	private List<String> listarSort(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarSort");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarSort(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("Int")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				listarSort(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;

					while ((linea = br.readLine()) != null) {
						linea = linea.replaceAll("\t", "").trim();
						if (linea.indexOf("<p:column") >= 0) {
							while ((linea = br.readLine()) != null) {
								linea = linea.replaceAll("\t", "").trim();
								if (linea.indexOf("</p:column") >= 0) {
									break;
								}
							}
						}
						if (linea != null
								&& linea.indexOf("sortBy") >= 0
								&& linea.indexOf("column") < 0) {
							String str = linea.substring(linea.indexOf("sortBy") + 8, linea.indexOf("\"", linea.indexOf("sortBy") + 9));
							if ("".equals(str)) {
								result.add("\t" + ficheros[x].getName() + " - " + linea);
							}
							else if (!str.startsWith("#")) {
								result.add("\t" + ficheros[x].getName() + " - " + linea);
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

	private List<String> listarReports(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarReports");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarReports(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())) {
				continue;
			}

			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(ficheros[x]);
				br = new BufferedReader(fr);

				String linea;
				String dataSet = "";

				while ((linea = br.readLine()) != null) {
					linea = linea.replaceAll("\t", "").trim();

					int pos = linea.indexOf("<list-property name=\"images\">");
					if (pos >= 0) {
						result.add(ficheros[x].getName() + "\t\t\thas images");
					}

					pos = linea.indexOf("<oda-data-set");
					if (pos >= 0) {
						pos = linea.indexOf("name=");
						dataSet = linea.substring(pos + 6, linea.indexOf("\"", pos+8));
					}

					pos = linea.indexOf("<xml-property name=\"queryText\">");
					if (pos >= 0 && linea.toLowerCase().indexOf("select") >= 0) {
						String sql = linea.substring(linea.indexOf("[CDATA[") + 7);
						while ((linea = br.readLine()) != null) {
							linea = linea.replaceAll("\t", "").trim();
							if (linea.indexOf("></xml-property>") >= 0) {
								break;
							}

							sql += " " + linea;

							pos = sql.indexOf("--");
							if (pos >= 0) {
								sql = sql.substring(0, pos);
							}
						}

						sql += " " + linea;
						sql = sql.replace("]]></xml-property>", "");
						sql = sql.replace("?", "''");

						Statement st = null;
						ResultSet rs = null;
						try {
							Class.forName("oracle.jdbc.driver.OracleDriver");
							Connection conexion = DriverManager.getConnection("jdbc:oracle:thin:@192.168.150.25:1521:hosdesa2", "ts", "desarro");

							st = conexion.createStatement();
							rs = st.executeQuery(sql);
							rs.close();
						} catch (Exception ioe) {
							result.add(ficheros[x].getName() + "\t\t\t" + dataSet + "[" + ioe.getMessage() + "]");
						} finally {
							try {
								if (rs != null) {
									rs.close();
								}
								rs=null;
								if (st != null) {
									st.close();
								}
								st=null;
							} catch (Exception e2) {
								e2.printStackTrace();
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
		return result;
	}

	private List<String> listarSizeCalendar(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarSizeCalendar");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarSizeCalendar(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("Int")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				listarSizeCalendar(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;

					while ((linea = br.readLine()) != null) {
						linea = linea.replaceAll("\t", "").trim();
						if (linea.indexOf("<p:calendar") >= 0) {
							int size = -1;
							int pattern = -1;
							while ((linea = br.readLine()) != null) {
								if (linea.contains("p:ajax")) {
									while ((linea = br.readLine()) != null) {
										if (!linea.contains("/>")) {
											break;
										}
									}
								}
								linea = linea.replaceAll("\t", "").trim();
								if (linea != null
										&& linea.indexOf("size") >= 0) {
									String str = linea.substring(linea.indexOf("size") + 6, linea.indexOf("\"", linea.indexOf("size") + 7)).replaceAll("\"", "");
									size = Integer.parseInt(str);
								}
								if (linea != null
										&& linea.indexOf("pattern") >= 0) {
									String str = linea.substring(linea.indexOf("pattern") + 9, linea.indexOf("\"", linea.indexOf("pattern") + 10)).replaceAll("\"", "");
									pattern = str.length();
								}
								if (linea.contains("</p")
										|| linea.contains("/>")) {
									if (size > -1
											&& pattern > -1
											&& size < pattern) {
										result.add("\t" + ficheros[x].getName() + " " + size + "<" + pattern + " - " + linea);
									}
									break;
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

	@SuppressWarnings("unused")
	private List<String> listarBeans(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarBeans");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarBeans(" + f.getAbsolutePath() + ")");
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
					|| ficheros[x].getName().startsWith("Int")
					|| ficheros[x].getName().startsWith("BBBuscadorCodCieTermino")) {
				continue;
			}
			//system.out.println(",");
			archivoNombre = ficheros[x].getName().replace(".java", "");
			//System.out.println(f.getAbsolutePath() + "\\" + archivoNombre);
			//			boolean esArchivo = ficheros[x].getName().startsWith("BBInicioTurnos");
			boolean esArchivo = false;
			boolean debug = false;

			if (ficheros[x].isDirectory()) {
				listarBeans(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;
					int pos = 0;
					String metodo = null;
					String modAcceso = null;
					boolean empezoClase = false;
					boolean empezoMetodo = false;

					String transaction = null;

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
								if (linea.indexOf("@PostConstruct") >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": @PostConstruct");
								}
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
							if (linea.contains("private")
									&& (linea.contains(" SelectItem") || linea.contains("SelectItem ") || linea.contains("SelectItem[]"))
									&& !linea.contains("init")) {
								if (linea.indexOf('{') * linea.indexOf('}') <= 0) {
									pos = linea.indexOf('}');
									while (pos < 0) {
										//system.out.println("c");
										linea = br.readLine();

										linea = linea.replaceAll("\t", "").trim();
										if (linea.length() == 0
												|| linea.startsWith("//")) {
											continue;
										}
										if (linea.startsWith("/*")) {
											pos = linea.indexOf("*/");
											while (pos < 0) {
												//system.out.println("v");
												linea = br.readLine();
												pos = linea.indexOf("*/");
											}
											continue;
										}
										//system.out.println("\t" + linea);

										pos = linea.indexOf('}');
									}
								}
								continue;
							}
							if (linea.contains("private UserSession")) {
								continue;
							}
							if ((linea.contains("public") || linea.contains("private"))
									&& linea.contains(";")) {
								continue;
							}
							if (linea.contains("@SuppressWarnings")) {
								continue;
							}
							if (linea.contains("@Override")) {
								continue;
							}
							if (empezoMetodo
									&& linea.contains("return")) {
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
									transaction = null;
									continue;
								}
								if (llaves == -1) {
									continue;
								}
							}

							if (empezoMetodo) {
								pos = linea.indexOf("new Date");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Date");
								}
								pos = linea.indexOf("Calendar");
								if (pos >= 0 && linea.indexOf("getTime") > 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Calendar");
								}
								pos = linea.indexOf("Criteria");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Criteria");
								}
								pos = linea.indexOf("createQuery");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": createQuery");
								}
								pos = linea.indexOf("createSQLQuery");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": createSQLQuery");
								}
								pos = linea.indexOf("getNamedQuery");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": getNamedQuery");
								}
								pos = linea.indexOf(".list()");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": list()");
								}
								pos = linea.indexOf(".uniqueResult()");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": uniqueResult()");
								}

								pos = linea.indexOf("ImpBus");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": ImpBus");
								}
								pos = linea.indexOf("Business");
								if (pos >= 0 && linea.indexOf("BusinessException") < 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": capa Business");
								}
								pos = linea.indexOf("HibernateSessionFactory");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": HibernateSessionFactory");
								}

								pos = linea.indexOf("Transaction ");
								if (pos >= 0) {
									String str = linea.replace("Transaction", "");
									try {
										transaction = str.substring(0, str.indexOf('=')).trim();
										result.add("\t" + archivoNombre + " - " + metodo + ": transaction ("+transaction+")");
									} catch (Exception e) {
										System.out.println(archivoNombre + " " + e.getMessage() + " + " + metodo + " + " + linea);
										e.printStackTrace();
									}
								}
								if (ctrlCatchBusiness) {
									pos = linea.indexOf("catch");
									if (pos >= 0) {
										pos = linea.indexOf("BusinessException");
										if (pos >= 0) {
											result.add("\t" + archivoNombre + " - " + metodo + ": catch BusinessException");
										}
									}
								}
							}
							else {
								pos = linea.indexOf('(');
								if (pos < 0) {
									result.add("\tlinea no tratada: " + archivoNombre + " - " + linea);
									continue;
								}
								empezoMetodo = true;

								String str = linea.substring(0, pos+1);
								pos = str.indexOf('(');
								try {
									metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();
									modAcceso = str.substring(0, str.indexOf(' '));
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
								if ("public".equals(modAcceso)
										&& linea.contains("throws")
										&& !archivoNombre.contains("BBBuscador")) {
									result.add("\t" + archivoNombre + " - " + metodo + ": public throws");
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

	private List<String> listarBusiness(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarBusiness");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarBusiness(" + f.getAbsolutePath() + ")");
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
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("Int")
					|| ficheros[x].getName().startsWith("BusinessFactory")
					|| ficheros[x].getName().startsWith("ImpBusFacadeParent")
					|| ficheros[x].getName().startsWith("RespuestaValidacion")) {
				continue;
			}
			archivoNombre = ficheros[x].getName().replace(".java", "");

			if (ficheros[x].isDirectory()) {
				listarBusiness(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;
					int pos = 0;
					String metodo = null;
					boolean isPrivate = false;
					boolean empezoClase = false;
					boolean empezoMetodo = false;
					boolean deprecated = false;

					boolean hasFinally = false;
					boolean hasCatch = false;
					boolean hasSessionClose = false;
					String session = null;

					boolean hasTransaction = false;
					boolean esTemporal = false;
					boolean hasCommitTransaction = false;
					boolean hasActiveTransaction = false;
					boolean hasRollbackTransaction = false;
					String transaction = null;

					int llaves = 0;
					while ((linea = br.readLine()) != null) {
						if (empezoClase) {
							if (linea.indexOf('@') >= 0) {
								if (linea.indexOf("@Deprecated") >= 0) {
									deprecated = true;
								}
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
									pos = linea.indexOf("*/");
								}
								continue;
							}

							if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
									|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
								pos = linea.indexOf('{');
								if (pos >= 0) {
									llaves++;
									while (pos >= 0) {
										pos = linea.indexOf('{', pos+1);
										if (pos >= 0) {
											llaves++;
										}
									}
								}
								pos = linea.indexOf('}');
								if (pos >= 0) {
									llaves--;
									while (pos >= 0) {
										pos = linea.indexOf('}', pos+1);
										if (pos >= 0) {
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
									deprecated = false;
									if (metodo != null && !isPrivate) {
										if (!hasFinally) {
											result.add("\t" + archivoNombre + " - " + metodo + ": !hasFinally");
										}
										if (!hasCatch) {
											result.add("\t" + archivoNombre + " - " + metodo + ": !hasCatch");
										}
										else if (!hasSessionClose) {
											result.add("\t" + archivoNombre + " - " + metodo + ": !hasSessionClose ("+session+')');
										}
										if (hasTransaction) {
											if (!hasCommitTransaction) {
												result.add("\t" + archivoNombre + " - " + metodo + ": !hasCommitTransaction ("+transaction+')');
											}
											if (!hasActiveTransaction) {
												result.add("\t" + archivoNombre + " - " + metodo + ": !hasActiveTransaction ("+transaction+')');
											}
											if (!hasRollbackTransaction) {
												result.add("\t" + archivoNombre + " - " + metodo + ": !hasRollbackTransaction ("+transaction+')');
											}
										}
										else {
											if (esTemporal) {
												result.add("\t" + archivoNombre + " - " + metodo + ": !tmp sin transaction ("+transaction+')');
											}
										}
									}
									hasFinally = false;
									hasCatch = false;
									hasTransaction = false;
									transaction = null;
									hasCommitTransaction = false;
									hasActiveTransaction = false;
									hasRollbackTransaction = false;
									continue;
								}
								if (llaves == -1) {
									continue;
								}
							}

							if (empezoMetodo) {
								if (session == null) {
									pos = linea.indexOf("StatelessSession");
									if (pos >= 0) {
										String str = linea.replace("StatelessSession", "");
										session = str.substring(0, str.indexOf('=')).trim();
									}
									if (session == null) {
										pos = linea.indexOf("Session");
										if (pos >= 0) {
											String str = linea.replace("Session", "");
											try {
												session = str.substring(0, str.indexOf('=')).trim();
											} catch (Exception e) {
												System.out.println(e.getMessage() + " + " + metodo + " + " + linea);
											}
										}
									}
								}

								pos = linea.indexOf("new Date");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Date");
								}
								pos = linea.indexOf("Calendar");
								if (pos >= 0 && linea.indexOf("getTime") > 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Calendar");
								}
								pos = linea.indexOf("Criteria");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Criteria");
								}
								pos = linea.indexOf("createQuery");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": createQuery");
								}
								pos = linea.indexOf("createSQLQuery");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": createSQLQuery");
								}
								pos = linea.indexOf("getNamedQuery");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": getNamedQuery");
								}
								pos = linea.indexOf(".list()");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": list()");
								}
								pos = linea.indexOf(".uniqueResult()");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": uniqueResult()");
								}

								pos = linea.indexOf("ImpBus");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": ImpBus");
								}
								pos = linea.indexOf("Business");
								if (pos >= 0 && linea.indexOf("BusinessException") < 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": capa Business");
								}

								pos = linea.indexOf("Transaction ");
								if (pos >= 0) {
									hasTransaction = true;
									String str = linea.replace("Transaction", "");
									try {
										transaction = str.substring(0, str.indexOf('=')).trim();
									} catch (Exception e) {
										System.out.println(e.getMessage() + " + " + metodo + " + " + linea);
									}
								}
								if (hasTransaction) {
									pos = linea.indexOf(transaction+".commit");
									if (pos >= 0) {
										hasCommitTransaction = true;
									}
									pos = linea.indexOf(transaction+".isActive");
									if (pos >= 0) {
										hasActiveTransaction = true;
									}
									pos = linea.indexOf(transaction+".rollback");
									if (pos >= 0) {
										hasRollbackTransaction = true;
									}
								}
								pos = linea.indexOf("finally");
								if (pos >= 0) {
									hasFinally = true;
									if (hasTransaction) {
										if (!hasCommitTransaction) {
											result.add("\t" + archivoNombre + " - " + metodo + ": revisar transaction commit");
										}
										if (!hasRollbackTransaction) {
											result.add("\t" + archivoNombre + " - " + metodo + ": revisar transaction rollback");
										}
									}
								}
								pos = linea.indexOf("catch");
								if (!hasFinally && pos >= 0) {
									hasCatch = true;
								}
								pos = linea.indexOf(session+".close");
								if (pos >= 0) {
									hasSessionClose = true;
									if (!hasFinally) {
										result.add("\t" + archivoNombre + " - " + metodo + ": revisar session close");
									}
								}
								pos = linea.indexOf("catch");
								if (pos >= 0) {
									if (ctrlCatchBusiness) {
										pos = linea.indexOf("BusinessException");
										if (pos >= 0) {
											result.add("\t" + archivoNombre + " - " + metodo + ": catch BusinessException");
										}
									}
									pos = linea.indexOf("HibernateException");
									if (pos >= 0) {
										result.add("\t" + archivoNombre + " - " + metodo + ": catch HibernateException");
									}
								}
							}
							else {
								pos = linea.indexOf('(');
								if (pos < 0) {
									result.add("\tlinea no tratada: " + archivoNombre + " - " + linea);
									continue;
								}
								empezoMetodo = true;

								String str = linea.substring(0, pos+1);
								pos = str.indexOf("private");
								isPrivate = false;
								if (pos >= 0) {
									isPrivate = true;
								}
								pos = str.indexOf('(');
								try {
									metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();
								} catch (Exception e) {
									System.out.println(e.getMessage() + " + " + metodo + " + " + linea);
								}
								if (deprecated) {
									result.add("\t" + archivoNombre + " - " + metodo + ": @Deprecated");
								}
								pos = linea.indexOf("List<Tmp");
								esTemporal = false;
								if (pos >= 0) {
									esTemporal = true;
								}

								pos = linea.indexOf("Session");
								if (pos >= 0) {
									int min = Integer.MAX_VALUE;
									if (min > linea.indexOf(')', pos+8)
											&& linea.indexOf(')', pos+8) > -1) {
										min = linea.indexOf(')', pos+8);
									}
									if (min > linea.indexOf(' ', pos+8)
											&& linea.indexOf(' ', pos+8) > -1) {
										min = linea.indexOf(' ', pos+8);
									}
									if (min > linea.indexOf(',', pos+8)
											&& linea.indexOf(',', pos+8) > -1) {
										min = linea.indexOf(',', pos+8);
									}
									if (min == Integer.MAX_VALUE) {
										result.add("\tSession: " + archivoNombre + " - " + linea);
									}
									else {
										String aux = linea.substring(linea.indexOf("Session")+7, min);
										if (aux.length() == 0) {
											result.add("\tSession: " + archivoNombre + " - " + linea);
										}
										session = aux.trim();
									}
								}

								pos = linea.indexOf('{');
								while (pos < 0) {
									linea = br.readLine();

									pos = linea.indexOf("Session");
									if (pos >= 0) {
										int min = Integer.MAX_VALUE;
										if (min > linea.indexOf(')', pos+8)
												&& linea.indexOf(')', pos+8) > -1) {
											min = linea.indexOf(')', pos+8);
										}
										if (min > linea.indexOf(' ', pos+8)
												&& linea.indexOf(' ', pos+8) > -1) {
											min = linea.indexOf(' ', pos+8);
										}
										if (min > linea.indexOf(',', pos+8)
												&& linea.indexOf(',', pos+8) > -1) {
											min = linea.indexOf(',', pos+8);
										}
										if (min == Integer.MAX_VALUE) {
											result.add("\tSession: " + archivoNombre + " - " + linea);
										}
										else {
											String aux = linea.substring(linea.indexOf("Session")+7, min);
											if (aux.length() == 0) {
												result.add("\tSession: " + archivoNombre + " - " + linea);
											}
											session = aux.trim();
										}
									}

									pos = linea.indexOf('{');
								}
								if (llaves == 0) {
									llaves++;
								}
							}
						}
						else {
							empezoClase = linea.contains("{") && !linea.contains("@");
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

	@SuppressWarnings("unused")
	private List<String> listarInserts(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarInserts");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarInserts(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		String archivoNombre = null;
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("Int")
					|| ficheros[x].getName().startsWith("BusinessFactory")
					|| ficheros[x].getName().startsWith("ImpBusFacadeParent")
					|| ficheros[x].getName().startsWith("RespuestaValidacion")) {
				continue;
			}
			archivoNombre = ficheros[x].getName().replace(".java", "");

			if (ficheros[x].isDirectory()) {
				if (ficheros[x].getName().startsWith("interfaces")) {
					continue;
				}
				listarInserts(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
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
					String dto = null;
					Map<String, String> imports = new HashMap<String, String>();
					String variable = null;
					String variableList = null;
					boolean empezoClase = false;
					boolean empezoMetodo = false;
					boolean usaGenId = false;
					boolean usaGenIdCompuesta = false;
					boolean hasInsert = false;

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
							if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
									|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
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
								try {
									if (variableList != null) {
										if (linea.contains("for")
												&& linea.contains("int ")
												&& linea.contains(variableList)) {
											variable = linea.substring(linea.indexOf("<") + 1, linea.indexOf(".size")).trim();
										}
										else if (linea.contains("for")
												&& linea.contains(":")
												&& linea.contains(variableList)) {
											variable = linea.substring(linea.indexOf(" ") + 1, linea.indexOf(":")).trim();
										}
									}
									if (linea.contains("selectGenerarIdPk")) {
										if (linea.contains(variable + ".set")) {
											usaGenId = true;
										}
										else if (linea.contains("=")) {
											result.add("\trevisar selectGenerarIdPk: " + archivoNombre + (metodo != null ? " - " + metodo : "" ) + " - " + dto);
										}
									}
									if (linea.contains("selectGenerarIdCompuestaPk")) {
										if (linea.contains(variable + ".set")) {
											usaGenIdCompuesta = true;
										}
										else if (linea.contains("=")) {
											result.add("\trevisar selectGenerarIdCompuestaPk: " + archivoNombre + (metodo != null ? " - " + metodo : "" ) + " - " + dto);
										}
									}
									if (variable != null
											&& linea.contains("getIntDomain().insert")
											&& linea.contains(variable + ",")
											&& imports.containsKey(dto)) {
										hasInsert = true;
										recorrerXml(result, "implements", ficheros[x].getAbsolutePath(), imports, dto, usaGenId, usaGenIdCompuesta, archivoNombre, metodo);
									}
								} catch (Exception e) {
									System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
									System.out.println("error: " + e.getMessage() + " + " + metodo + " + " + linea);
									e.printStackTrace();
									System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								}
							}
							else {
								pos = linea.indexOf('(');
								if (pos < 0) {
									result.add("\tlinea no tratada: " + archivoNombre + " - " + linea);
									continue;
								}
								empezoMetodo = true;

								String str = linea.substring(0, pos+1);
								pos = str.indexOf('(');
								try {
									metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();

									if (dto != null
											&& metodo.contains("insert")
											&& linea.contains(dto + " ")) {
										pos = linea.indexOf(" ", linea.indexOf(dto + " ") - 3);
										variable = linea.substring(pos + 1, linea.indexOf(",", pos));
										usaGenId = false;
										usaGenIdCompuesta = false;
									}
									if (dto != null
											&& metodo.contains("insert")
											&& linea.contains(dto + ">")) {
										pos = linea.indexOf(" ", linea.indexOf(dto + ">") - 3);
										variableList = linea.substring(pos + 1, linea.indexOf(",", pos));
										usaGenId = false;
										usaGenIdCompuesta = false;
									}
								} catch (Exception e) {
									System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
									System.out.println("error 2: " + e.getMessage() + " + " + metodo + " + " + linea);
									System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
								}
								if (esArchivo && "infoPrestacion".equals(metodo)) {
									System.out.println("infoPrestacion 2 " + metodo);
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
							pos = linea.indexOf("import");
							if (pos >= 0) {
								pos = linea.indexOf("dtos");
								if (pos >= 0
										&& !linea.contains(".Tmp")
										&& !linea.contains("NotMapped")) {
									String tmp = linea.substring(linea.lastIndexOf(".") + 1, linea.length() - 1);
									String path = linea.substring(linea.indexOf(" "), linea.length() - 1);
									path = path.substring(path.indexOf("dtos"), path.length());
									imports.put (tmp, path);
								}
							}
							else {
								pos = linea.indexOf("ImpBusFacadeParent");
								if (pos >= 0) {
									dto = linea.substring(linea.indexOf("<") + 1, linea.indexOf(">"));
								}
							}
						}
					}

					if (!hasInsert
							&& imports.containsKey(dto)) {
						recorrerXml(result, "default", ficheros[x].getAbsolutePath(), imports, dto, true, false, archivoNombre, null);
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

	private void recorrerXml (List<String> result, String semilla, String pathXml, Map<String, String> imports, String dto, boolean usaGenId, boolean usaGenIdCompuesta, String archivoNombre, String metodo) throws Exception {
		pathXml = pathXml.substring(0, pathXml.indexOf("business")) + "\\" + imports.get(dto).replace(".", "\\") + ".hbm.xml";

		FileReader frXml = null;
		BufferedReader brXml = null;
		File fXml = null;
		try {
			fXml = new File (pathXml);
			if (fXml.exists()) {
				frXml = new FileReader(fXml);
				brXml = new BufferedReader(frXml);

				String lineaXml = null;
				int ctdMapeoPk = -1;
				while ((lineaXml = brXml.readLine()) != null) {
					if (lineaXml.indexOf("<!--") >= 0) {
						continue;
					}

					if (lineaXml.indexOf("<id") >= 0) {
						ctdMapeoPk = 1;
					}
					else if (lineaXml.indexOf("<composite-id") >= 0) {
						ctdMapeoPk = 2;
					}
				}

				if (usaGenId && ctdMapeoPk == 2) {
					result.add("\tusaGenId y tiene composite id " + semilla + ": " + archivoNombre + (metodo != null ? " - " + metodo : "" ) + " - " + dto);
				}
				if (usaGenIdCompuesta && ctdMapeoPk == 1) {
					result.add("\tusaGenIdCompuesta y tiene id " + semilla + ": " + archivoNombre + (metodo != null ? " - " + metodo : "" ) + " - " + dto);
				}
			}
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			try {
				if (brXml != null) {
					brXml.close();
				}
				brXml=null;
				if (frXml != null) {
					frXml.close();
				}
				frXml=null;
			} catch (Exception e2) {
				throw e2;
			}
		}
	}

	private List<String> listarPersistance(List<String> result, File f, FileFilter filter) throws BusinessException {
		//System.out.println("listarPersistance");
		if (f == null) {
			throw new BusinessException("file is null", SeverityBundle.ERROR);
		}
		//System.out.println("listarPersistance(" + f.getAbsolutePath() + ")");
		if (filter == null) {
			throw new BusinessException("filter is null", SeverityBundle.ERROR);
		}
		File[] ficheros = f.listFiles(filter);
		if (ficheros == null) {
			throw new BusinessException("ficheros is null", SeverityBundle.ERROR);
		}
		String archivoNombre = null;
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("HibernateDateResult")
					|| ficheros[x].getName().startsWith("HibernateResult")
					|| ficheros[x].getName().startsWith("ImpDomain")
					|| ficheros[x].getName().startsWith("Int")
					|| ficheros[x].getName().startsWith("PersistanceFactory")) {
				continue;
			}
			archivoNombre = ficheros[x].getName().replace(".java", "");

			if (ficheros[x].isDirectory()) {
				listarPersistance(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				String linea;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					int pos = 0;
					String metodo = null;
					boolean empezoClase = false;
					boolean empezoAlgunMetodo = false;
					boolean empezoMetodo = false;
					boolean deprecated = false;

					boolean hasTransaction = false;
					String transaction = null;
					Map<String, Integer> querys = new HashMap<String, Integer>();

					int llaves = 0;
					while ((linea = br.readLine()) != null) {
						if (empezoClase) {
							if (linea.indexOf('@') >= 0) {
								if (linea.indexOf("@Deprecated") >= 0) {
									deprecated = true;
								}
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
									pos = linea.indexOf("*/");
								}
								continue;
							}

							if ((linea.contains("static") || linea.contains("public") || linea.contains("private"))
									&& linea.contains("enum")) {
								if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
										|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
									pos = linea.indexOf('}');
									while (pos < 0) {
										linea = br.readLine();
										pos = linea.indexOf('}');
									}
								}
								continue;
							}
							if (linea.contains("return")) {
								continue;
							}

							if ((linea.indexOf('{') >= 0 && linea.indexOf('}') < 0)
									|| (linea.indexOf('{') < 0 && linea.indexOf('}') >= 0)) {
								pos = linea.indexOf('{');
								if (pos >= 0) {
									llaves++;
									while (pos >= 0) {
										pos = linea.indexOf('{', pos+1);
										if (pos >= 0) {
											llaves++;
										}
									}
								}
								pos = linea.indexOf('}');
								if (pos >= 0) {
									llaves--;
									while (pos >= 0) {
										pos = linea.indexOf('}', pos+1);
										if (pos >= 0) {
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
									deprecated = false;
									if (metodo != null) {
										if (hasTransaction) {
											result.add("\t" + archivoNombre + " - " + metodo + ": !hasTransaction ("+transaction+')');
										}
									}
									hasTransaction = false;
									transaction = null;
									continue;
								}
								if (llaves == -1) {
									continue;
								}
							}

							if (empezoMetodo) {
								pos = linea.indexOf("new Date");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Date");
								}
								pos = linea.indexOf("Calendar");
								if (pos >= 0 && linea.indexOf("getTime") > 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": Calendar");
								}
								pos = linea.indexOf("Transaction ");
								if (pos >= 0) {
									hasTransaction = true;
									String str = linea.replace("Transaction", "");
									try {
										transaction = str.substring(0, str.indexOf('=')).trim();
									} catch (Exception e) {
										System.out.println(archivoNombre + " - " + e.getMessage() + " + " + metodo + " + " + linea);
									}
								}
								pos = linea.indexOf("catch");
								if (pos >= 0) {
									if (ctrlCatchBusiness) {
										pos = linea.indexOf("BusinessException");
										if (pos >= 0) {
											result.add("\t" + archivoNombre + " - " + metodo + ": catch BusinessException");
										}
									}
									pos = linea.indexOf("Business");
									if (pos >= 0) {
										result.add("\t" + archivoNombre + " - " + metodo + ": capa Business");
									}
								}
								pos = linea.indexOf("close");
								if (pos >= 0) {
									result.add("\t" + archivoNombre + " - " + metodo + ": close");
								}
								pos = linea.indexOf("getNamedQuery");
								if (pos >= 0) {
									int comi = linea.indexOf("\"", pos + 10);
									pos = linea.indexOf("\"", comi + 5);
									if (pos > 0) {
										String tmp = linea.substring(comi + 1, linea.indexOf("\"", comi + 5));
										if (!querys.containsKey(tmp)) {
											querys.put(tmp, 0);
										}
										querys.put(tmp, querys.get(tmp) + 1);
									}
									else {
										result.add("\t" + archivoNombre + " - " + metodo + ": getNamedQuery cerrar");
									}
									if (!ficheros[x].getAbsolutePath().contains("stored")) {
										result.add("\t" + archivoNombre + " - " + metodo + ": getNamedQuery mal ubicada");
									}
								}
							}
							else {
								pos = linea.indexOf('(');
								if (pos < 0) {
									result.add("\tlinea no tratada: " + archivoNombre + " - " + linea);
									continue;
								}
								empezoMetodo = true;
								empezoAlgunMetodo = true;

								String str = linea.substring(0, pos+1);
								pos = str.indexOf('(');
								try {
									metodo = str.substring(str.lastIndexOf(' ', pos-2), pos).trim();
								} catch (Exception e) {
									System.out.println(archivoNombre + " - " + e.getMessage() + " + " + metodo + " + " + linea);
								}
								if (deprecated) {
									result.add("\t" + archivoNombre + " - " + metodo + ": @Deprecated");
								}

								pos = linea.indexOf('{');
								if (pos < 0 && llaves == 0) {
									llaves++;
								}
							}
						}
						else {
							empezoClase = linea.contains("{") && !linea.contains("@");
						}
					}

					Iterator<Entry<String, Integer>> it = querys.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Integer> e = it.next();
						if (e.getValue() > 1) {
							result.add("\t" + archivoNombre + " - " + e.getKey() + ": getNamedQuery repetida (" + e.getValue() + ")");
						}
					}

					if (!empezoAlgunMetodo) {
						result.add("\t" + archivoNombre + ": clase vacia");
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

	public static void main(String[] args) {
		FinallySearch m = new FinallySearch();
		List<String> result = new ArrayList<String>();
		try {
			String business = "\\src\\ar\\com\\thinksoft\\business\\";
			String persistance = "\\src\\ar\\com\\thinksoft\\persistance\\";

			controlPresentacion(result, m, "HOSPITAL");
			controlPresentacion(result, m, "AGE");
			controlPresentacion(result, m, "AGI");
			controlPresentacion(result, m, "AGP");
			controlPresentacion(result, m, "HOS-APP");
			controlPresentacion(result, m, "RECETAS");
			result.add("\n\nAFIP");
			m.listarBusiness(result, new File(dirWorkspace + "AFIP" + business), new MyFileFilter("java"));
			result.add("\n\nBIONEXO");
			m.listarBusiness(result, new File(dirWorkspace + "BIONEXO" + business), new MyFileFilter("java"));
			result.add("\n\nBUSINESS");
			m.listarBusiness(result, new File(dirWorkspace + "HOSPITAL-BUSINESS" + business), new MyFileFilter("java"));
			//			result.add("\n\nINSERTS");
			//			m.listarInserts(result, new File(dirWorkspace + "HOSPITAL-BUSINESS" + business), new MyFileFilter("java"));
			result.add("\n\nPERSISTANCE");
			m.listarPersistance(result, new File(dirWorkspace + "HOSPITAL-BUSINESS" + persistance), new MyFileFilter("java"));

			generarArchivo(pathOutput, "9-finally", "txt", result, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void controlPresentacion(List<String> result, FinallySearch m, String proyecto) throws Exception {
		String beans = "\\src\\ar\\com\\thinksoft\\beans\\";
		String pages = "\\WebRoot\\";

		result.add("\n\n" + proyecto);
		m.listarBeans(result, new File(dirWorkspace + proyecto + beans), new MyFileFilter("java"));
		result.add("\n\n" + proyecto + " beans");
		m.listarBeansRepetidos(result, new File(dirWorkspace + proyecto + pages), new MyFileFilter("xhtml"));
		result.add("\n\n" + proyecto + " rows");
		m.listarRowsSinPaginator(result, new File(dirWorkspace + proyecto + pages), new MyFileFilter("xhtml"));
		result.add("\n\n" + proyecto + " size");
		m.listarSizeCalendar(result, new File(dirWorkspace + proyecto + pages), new MyFileFilter("xhtml"));
		result.add("\n\n" + proyecto + " sort");
		m.listarSort(result, new File(dirWorkspace + proyecto + pages), new MyFileFilter("xhtml"));
		result.add("\n\n" + proyecto + " reports");
		m.listarReports(result, new File(dirWorkspace + proyecto + pages + "pages\\reports\\"), new MyFileFilter("rptdesign"));
	}

	/**
	 *
	 * @param path
	 * @param nombre
	 * @param extension
	 * @param result
	 * @param abrirArchivo
	 */
	public static void generarArchivo(String path, String nombre, String extension, List<String> result, boolean abrirArchivo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");

		File outFile = null;
		BufferedWriter writer = null;
		String fileName = path+"/"+nombre+"_"+sdf.format(new Date())+"." + extension;
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

		if (abrirArchivo) {
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

}

class MyFileFilter implements FileFilter {

	private String extension;

	MyFileFilter(String extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(File file) {
		return (file.isDirectory()
				|| file.getName().toLowerCase().endsWith(extension));
	}

}