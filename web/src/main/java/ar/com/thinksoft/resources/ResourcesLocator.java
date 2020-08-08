package ar.com.thinksoft.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourcesLocator {

	public ResourcesLocator() {
		abrirArchivoPropiedades();
	}

	private void abrirArchivoPropiedades() {
		File f = new File("paths_HOSPITAL.properties");
		try {
			if (f.exists()) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(f);
					br = new BufferedReader(fr);

					String linea;
					int pos = -1;
					while ((linea = br.readLine()) != null) {
						linea = linea.trim();
						if (linea != null && linea.length() > 0) {
							pos = linea.indexOf("dir=");
							if (pos >= 0) {
								dir = linea.substring(4);
								continue;
							}
							pos = linea.indexOf("dirJava=");
							if (pos >= 0) {
								dirJava = dir + linea.substring(8);
								continue;
							}
							pos = linea.indexOf("dirBusiness=");
							if (pos >= 0) {
								dirBusiness = linea.substring(12);
								continue;
							}
							pos = linea.indexOf("dirHtml=");
							if (pos >= 0) {
								dirHtml = dir + linea.substring(8);
								continue;
							}
							pos = linea.indexOf("resOriginal=");
							if (pos >= 0) {
								resOriginal = dirJava + linea.substring(12);
								continue;
							}
							pos = linea.indexOf("resCopia=");
							if (pos >= 0) {
								resCopia = linea.substring(9);
								continue;
							}
							pos = linea.indexOf("menu=");
							if (pos >= 0) {
								menu = linea.substring(5);
								continue;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fr) {
							fr.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			else {
				String str = "dir="+dir+"\n"
						+ "dirJava="+dirJava.substring(dir.length())+"\n"
						+ "dirBusiness="+dirBusiness+"\n"
						+ "dirHtml="+(dirHtml != null ? dirHtml.substring(dir.length()) : "")+"\n"
						+ "resOriginal="+resOriginal.substring(dirJava.length())+"\n"
						+ "resCopia="+(resCopia != null ? resCopia : "")+"\n"
						+ "menu="+(menu != null ? menu : "");
				BufferedWriter writer = null;
				try {
					f = new File("paths_HOSPITAL.properties");
					writer = new BufferedWriter(new FileWriter(f));
					writer.write(str);
					writer.newLine();
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					try {
						if (writer != null) {
							writer.flush();
							writer.close();
						}
						writer  = null;
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> listarMenu(List<String> result, File f, int proyecto) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			String linea;
			int pos = -1;
			String str = "";
			while ((linea = br.readLine()) != null) {
				pos = linea.indexOf("'----------'");
				if (pos >= 0) {
					continue;
				}
				pos = linea.indexOf("p_add_menu_aplicacion("+proyecto);
				if (pos >= 0) {
					pos += +("p_add_menu_aplicacion("+proyecto).length();
					str = linea.substring(linea.indexOf("'", pos+3)+1,linea.indexOf("'", linea.indexOf("'", pos+3)+1));
					pos = (result.contains(str) ? 1 : 0);
					if (pos == 0) {
						result.add(str);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public List<String> listarDirectorio(List<String> result, File f, FileFilter filter) {
		File[] ficheros = f.listFiles(filter);
		for (int x = 0; x < ficheros.length; x++) {
			if (".svn".equals(ficheros[x].getName())
					|| ficheros[x].getName().startsWith("FinallySearch")
					|| ficheros[x].getName().startsWith("GeneraDatosDto")) {
				continue;
			}

			if (ficheros[x].isDirectory()) {
				listarDirectorio(result, ficheros[x], filter);
			} else if (!"resources".equals(f.getName())) {
				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader(ficheros[x]);
					br = new BufferedReader(fr);

					String linea;
					int pos = 0;
					String str = "";
					boolean exists = false;
					while ((linea = br.readLine()) != null) {
						pos = linea.indexOf("//");
						if (pos >= 0) {
							continue;
						}
						pos = linea.indexOf("<!--");
						if (pos >= 0) {
							continue;
						}

						pos = linea.indexOf("/*");
						if (pos >= 0 && linea.indexOf("*/", pos+1) < 0) {
							pos = linea.indexOf("*/", pos+1);
							while (pos < 0) {
								linea = br.readLine();
								if (linea == null) {
									pos = 0;
								}
								else {
									pos = linea.indexOf("*/", pos+1);
								}
							}
						}

						Pattern p = Pattern.compile("^[^<a4j]*value=\"\\w[^bold][^xx\"][^mp3=]");
						Matcher m = p.matcher(linea);
						if (m.find()) {
							System.out.println(ficheros[x].getName() + ": " + linea.trim().replaceAll("\\p{Space}+", " "));
						}

						pos = 0;
						while (pos >= 0) {
							pos = linea.indexOf("#{msg", pos+1);
							if (pos >= 0) {
								str = linea.substring(pos+6, linea.indexOf("}", pos)).trim();
								exists = result.contains(str);
								if (!exists) {
									result.add(str);
								}
							}
						}
						pos = 0;
						while (pos >= 0) {
							pos = linea.indexOf("${msg", pos+1);
							if (pos >= 0) {
								str = linea.substring(pos+6, linea.indexOf("}", pos)).trim();
								exists = result.contains(str);
								if (!exists) {
									result.add(str);
								}
							}
						}
						pos = 0;
						while (pos >= 0) {
							pos = linea.indexOf("msg.", pos+1);
							if (pos >= 0) {
								int tmpPos = linea.indexOf("}", pos+1);
								if (tmpPos >= 0) {
									int esp = linea.indexOf(" ", pos+1);
									int llave = linea.indexOf("}", pos+1);
									int par = linea.indexOf(")", pos+1);
									int puntos = linea.indexOf(":", pos+1);
									int min = Integer.MAX_VALUE;
									if (esp >= 0 && esp < min) {
										min = esp;
									}
									if (par >= 0 && par < min) {
										min = par;
									}
									if (llave >= 0 && llave < min) {
										min = llave;
									}
									if (puntos >= 0 && puntos < min) {
										min = puntos;
									}
									str = linea.substring(pos+4, min).trim();
									exists = result.contains(str);
									if (!exists) {
										result.add(str);
									}
								}
							}
						}
						pos = 0;
						while (pos >= 0) {
							pos = linea.indexOf("Resources.getValue(Constants", pos+1);
							if (pos >= 0) {
								str = linea.substring(pos+36, linea.indexOf(")", pos+20)).trim();
								str = str.replace(".toLowerCase(", "");
								str = str.replace("equals(", "");
								exists = result.contains(str);
								if (!exists) {
									result.add(str);
								}
							}
						}
						pos = 0;
						while (pos >= 0) {
							pos = linea.indexOf("Resources.getValue(\"", pos+1);
							if (pos >= 0) {
								str = linea.substring(pos+20, linea.indexOf("\"", pos+20)).trim();
								exists = result.contains(str);
								if (!exists) {
									result.add(str);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fr) {
							fr.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	private Map<String, String> getResources (String file) {
		Map<String, String> result = new LinkedHashMap<>();

		if (file != null && !"".equals(file)) {
			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(new File (file));
				br = new BufferedReader(fr);

				String linea;
				int pos = -1;
				while ((linea = br.readLine()) != null) {
					pos = linea.indexOf("=");
					if (pos >= 0) {
						result.put(linea.substring(0, pos), linea.substring(pos+1));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fr) {
						fr.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		return orderForKeys(result);
	}

	//	private String dir = "/home/TS-149/Desarrollo/workspacejm2/HOSPITAL";
	//	private String dirJava = dir+"/src";
	//	private String dirBusiness = "/home/TS-149/Desarrollo/workspacejm2/HOSPITAL-BUSINESS";
	//	private String dirHtml = dir + "/WebRoot/";
	//	private String resOriginal = dirJava + "/ar/com/TS-149/resources/Resources.properties";
	//	private String resCopia = "/home/TS-149/Desarrollo/workspacejm2/HOSPITAL/src/ar/com/TS-149/resources/Resources.properties";
	//	private String menu = null;

	//	private String dir = "/home/TS-149/workspace_Hospital999/HOSPITAL";
	//	private String dirJava = dir+"/src";
	//	private String dirBusiness = "/home/TS-149/workspace_Hospital999/HOSPITAL-BUSINESS";
	//	private String dirHtml = dir + "/WebRoot/";
	//	private String resOriginal = dirJava + "/ar/com/TS-149/resources/Resources.properties";
	//	private String resCopia = "/home/TS-149/workspace_Hospital8/HOSPITAL_2/src/ar/com/TS-149/resources/Resources.properties";
	//	private String menu = "/home/TS-149/workspace_Hospital999/HOSPITAL/Codigo de DBMS/Carga Inicial/script_menu.sql";

	private String dir = "C:\\Users\\TS-149\\workspaceHospital95/HOSPITAL";
	private String dirJava = dir+"/src";
	private String dirBusiness = "C:\\Users\\TS-149\\workspaceHospital95/HOSPITAL-BUSINESS";
	private String dirHtml = dir + "/WebRoot/";
	private String resOriginal = dirJava + "/ar/com/TS-149/resources/Resources.properties";
	private String resCopia = "C:\\Users\\TS-149\\workspaceHospital95/HOSPITAL/src/ar/com/TS-149/resources/Resources.properties";
	private String menu = "C:\\Users\\TS-149\\workspaceHospital95\\Codigo de DBMS\\Carga_inicial/script_menu.sql";

	public static void main(String[] args) {
		ResourcesLocator m = new ResourcesLocator();
		m.analizar();
	}

	private void analizar() {
		List<String> result = new ArrayList<>();
		listarDirectorio(result, new File(dirJava), new MyFileFilter("java"));
		if (dirBusiness != null && !"".equals(dirBusiness)) {
			listarDirectorio(result, new File(dirBusiness), new MyFileFilter("java"));
		}
		if (dirHtml != null && !"".equals(dirHtml)) {
			listarDirectorio(result, new File(dirHtml), new MyFileFilter("xhtml"));
		}
		if (menu != null && !"".equals(menu)) {
			listarMenu(result, new File(menu), 2);
		}
		Collections.sort(result);
		Map<String, String> original = getResources(resOriginal);
		Map<String, String> copia = getResources(resCopia);

		List<String> resultadoCopia = new ArrayList<>();
		List<String> resultadoSinCopia = new ArrayList<>();

		for (String s : result) {
			if (!original.containsKey(s)) {
				if (copia.containsKey(s)) {
					original.put(s, copia.get(s));
					resultadoCopia.add(s + "=" + copia.get(s));
				}
				else {
					resultadoSinCopia.add(s);
				}
			}
		}

		if (resultadoSinCopia != null && resultadoSinCopia.size() > 0) {
			System.out.println("\nRecursos sin copia\n");
			Collections.sort(resultadoSinCopia);
			for (String s : resultadoSinCopia) {
				System.out.println(s);
			}
		}

		if (resultadoCopia != null && resultadoCopia.size() > 0) {
			original = orderForKeys(original);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(resOriginal));
				bw.write("#---------------------------------------------------------------------------\n# Nota: los caracteres unicode comienzan con \"\\u\"\n# a:\\u00E1     e:\\u00E9     i:\\u00ED     o:\\u00F3     u:\\u00FA     n:\\u00F1\n# A:\\u00C1     E:\\u00C9     I:\\u00CD     O:\\u00D3     U:\\u00DA     N:\\u00D1\n# grado:\\u00B0            pregunta:\\u00BF\n#---------------------------------------------------------------------------\n\n\n");
				Iterator<Entry<String, String>> it = original.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> e = it.next();
					bw.write(e.getKey()+"="+e.getValue()+"\n");
				}

				bw.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("\nRecursos copiados\n");
			Collections.sort(resultadoCopia);
			for (String s : resultadoCopia) {
				System.out.println(s);
			}
		}
	}

	private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
		@Override
		public int compare(String str1, String str2) {
			int res = str1.replace("_", "Z").compareTo(str2.replace("_", "Z"));
			return res;
		}
	};

	public static Map<String, String> orderForKeys(Map<String, String> map) {
		Map<String, String> newMap = new LinkedHashMap<>();
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys, ALPHABETICAL_ORDER);
		Iterator<String> it = keys.iterator();
		String tmp = null;
		while (it.hasNext()) {
			tmp = it.next();
			for (Map.Entry<String, String> k : map.entrySet()) {
				if (tmp.equals(k.getKey())) {
					newMap.put(k.getKey(), k.getValue());
				}
			}
		}
		return newMap;
	}

}

class MyFileFilter implements FileFilter {

	private String extension;

	public MyFileFilter(String extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(File file) {
		return (file.isDirectory() || file.getName().toLowerCase().endsWith(extension));
	}

}