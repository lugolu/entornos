package ar.com.thinksoft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.com.thinksoft.common.CustomTest;
import ar.com.thinksoft.common.IgnoreControlRetorno;

public class AnalyzeAnnotations {

	public static void main(String[] args) {
		Long time = 10000L;
		try {
			String path = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");

			// if (java) {
			path = path.split("/bin")[0] + "/src/ar/com/thinksoft/business/";
			// } else {
			// pathDestino =
			// path.split("/target")[0]+"/src/test/java/ar/com/thinksoft/business/persistance/";
			// }

			String[] carpetas = new String[6];
			carpetas[0] = path + "/bionexo/";
			carpetas[1] = path + "/business/";
			carpetas[2] = path + "/dtos/";
			carpetas[3] = path + "/dtosPersistance/";
			carpetas[4] = path + "/persistance/";
			carpetas[5] = path + "/ws/";

			Map<String, List<String>> map = new HashMap<String, List<String>>();

			for (String pathDestino : carpetas) {
				//				System.out.println(pathDestino);
				File fDestino = new File(pathDestino);
				if (!fDestino.exists()) {
					throw new Exception("caldito seas 2");
				}

				List<String> clases = getFiles(fDestino);

				//				System.out.println();
				for (String s : clases) {
					try {
						String clase = null;
						//														if (java) {
						clase = s.split("src")[1].replace("java", "").replaceAll("\\\\", ".");
						//				}
						//				else {
						//							clase = s.split("java")[1].replace("java", "").replaceAll("\\\\", ".");
						//					}
						clase = clase.substring(1, clase.length() - 1);
						Method[] methods = Class.forName(clase).getMethods();
						for (Method method : methods) {
							if (!method.getName().startsWith("test")) {
								continue;
							}

							CustomTest custom = method.getAnnotation(CustomTest.class);
							if (custom != null) {
								if (!map.containsKey(custom.motivo())) {
									map.put(custom.motivo(), new LinkedList<String>());
								}
								map.get(custom.motivo()).add(clase + "." + method.getName());
							}

							IgnoreControlRetorno ignore = method.getAnnotation(IgnoreControlRetorno.class);
							if (ignore != null) {
								if (!map.containsKey(ignore.motivo())) {
									map.put(ignore.motivo(), new LinkedList<String>());
								}
								map.get(ignore.motivo()).add(clase + "." + method.getName());
							}

							RemapTest remap = method.getAnnotation(RemapTest.class);
							if (remap != null) {
								if (!map.containsKey("remap")) {
									map.put("remap", new LinkedList<String>());
								}
								map.get("remap").add(clase + "." + method.getName());
							}
						}
					} catch (Exception e) {
						//						System.out.println("error procesando clase " + s + ": " + e.getMessage());
						e.printStackTrace();
					}
				}
			}

			for (String s : map.keySet()) {
				System.out.println(s + "(" + map.get(s).size() + ")");
				if ("NamedQuery".equals(s)
						|| "parametro".equals(s)
						|| "sobrecarga".equals(s)) {
					continue;
				}
				for (String m : map.get(s)) {
					System.out.println("\t" + m);
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileReader fr = null;
		BufferedReader br = null;
		try {
			String path = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");

			path = path.split("/bin")[0] + "/success.txt";
			System.out.println(path);

			File f = new File(path);
			if (!f.exists()) {
				throw new Exception("caldito seas 2");
			}

			Map<String, Integer> mapRepe = new HashMap<String, Integer>();

			Map<Long, List<String>> mapCtd = new HashMap<Long, List<String>>();
			String linea = null;
			String fecha = null;
			String test = null;
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date anterior = null;
			Date actual = null;
			while ((linea = br.readLine()) != null) {
				linea = linea.replaceAll("\t", "").replaceAll("\\p{Space}+", " ").trim();

				fecha = linea.substring(0, 19);
				actual = sdf.parse(fecha);
				if (anterior == null) {
					anterior = actual;
				}

				Long diff = actual.getTime() - anterior.getTime();
				test = linea.substring(20);

				if (!mapRepe.containsKey(test)) {
					mapRepe.put(test, -1);
				}
				mapRepe.put(test, mapRepe.get(test) + 1);

				test += "(" + fecha + ")";

				if (diff >= time) {
					if (!test.startsWith("HibernateSessionFactory.")) {
						if (!mapCtd.containsKey(diff)) {
							mapCtd.put(diff, new LinkedList<String>());
						}

						mapCtd.get(diff).add(test);
					}
				}

				anterior = actual;
			}

			for (Long l : mapCtd.keySet()) {
				System.out.println(l / 1000);

				for (String s : mapCtd.get(l)) {
					System.out.println("\t" + s);
				}
			}

			System.out.println();

			int repe = 0;
			for (String s : mapRepe.keySet()) {
				if (mapRepe.get(s) > 0) {
					if (!s.startsWith("HibernateSessionFactory.")) {
						repe += mapRepe.get(s);
					}
					System.out.println(s + " (" + mapRepe.get(s) + ")");
				}
			}

			System.out.println("(" + repe + ")");
		} catch (Exception e) {
			e.printStackTrace();
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

	private static List<String> getFiles(File fDestino) {
		List<String> clases = new LinkedList<String>();

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
						clases.add(fDestino.getAbsolutePath() + "." + x.getName() + "." + f.getName());
					}
				}
			}
			else {
				if (!x.getName().endsWith(".java")) {
					continue;
				}
				clases.add(fDestino.getAbsolutePath() + "." + x.getName());
			}
		}

		return clases;
	}

}