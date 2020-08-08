package ar.com.thinksoft.common;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Utils {

	public static List sortList(Object[] array, String propertyName, boolean asc){
		List list = Arrays.asList(array);
		return sortList(list, asc, propertyName);
	}

	public static List sortList(List list, String propertyName, boolean asc){
		return sortList(list, asc, propertyName);
	}

	public static List sortList(List list,boolean asc, String ... propertyNames) {
		if (list == null || list.size() < 2 || propertyNames == null || propertyNames.length == 0) {
			return list;
		}
		try {
			Method[] metodos = new Method[propertyNames.length];
			for (int i = 0; i < propertyNames.length; i++) {
				metodos[i] = list.get(0).getClass().getMethod("get" + propertyNames[i].substring(0, 1).toUpperCase() + propertyNames[i].substring(1));
			}
			Collections.sort(list, getComparator(asc,metodos));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private static Comparator getComparator(final boolean asc, final Method ... metodos) {
		Comparator comp = new Comparator() {
			@Override
			public int compare(Object arg0, Object arg1) {
				int retorno = 0;
				try {
					Comparable obj0 = null;
					Comparable obj1 = null;
					for (Method metodo : metodos) {
						obj0 = (Comparable)metodo.invoke(arg0);
						obj1 = (Comparable)metodo.invoke(arg1);
						if (obj0 == null && obj1 == null) {
							retorno = 0;
						} else {
							if (obj0 != null && obj1 != null) {
								retorno = asc ? obj0.compareTo(obj1) : obj1.compareTo(obj0);
							} else {
								retorno = (obj0 != null ? -1 : 1) * (asc ? 1 : -1);
							}
							if (retorno != 0) {
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retorno;
			}
		};
		return comp;
	}

}
