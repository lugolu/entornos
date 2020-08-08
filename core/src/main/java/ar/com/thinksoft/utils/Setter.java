package ar.com.thinksoft.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Setter {

	@SuppressWarnings("unchecked")
	public static void set(Serializable origen, Serializable destino) {
		if (origen == null) {
			return;
		}
		try {
			if (!origen.getClass().getSimpleName().equals(destino.getClass().getSimpleName())) {
				throw new Exception("Las clases a setear deben ser de la misma clase.");
			}
			Class clase = Class.forName(origen.getClass().getName());
			Field[] field = clase.getDeclaredFields();
			Method metodoOrigen;
			Method metodoDestino;
			String nombreMetodo;
			Object valor = null;
			for(int i = 0; i < field.length; i++) {
				nombreMetodo = field[i].getName().substring(0,1).toUpperCase() + field[i].getName().substring(1);
				metodoOrigen = clase.getMethod("get"+nombreMetodo);
				metodoDestino = clase.getMethod("set"+nombreMetodo,field[i].getType());
				valor = metodoOrigen.invoke(origen);
				if (valor != null) {
					metodoDestino.invoke(destino, valor);
				}
			}
		} catch (Exception e) {
			System.out.println("EXCEPCION SETTER");
			System.out.println(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean setChild(Serializable parent, Serializable child) {
		boolean setted = false;
		try {
			Class classParent = Class.forName(parent.getClass().getName());
			Class classChild = Class.forName(child.getClass().getName());
			Field[] field = classParent.getDeclaredFields();
			Method metodoParent;
			Method metodoChild;
			String nombreMetodo;
			for(int i = 0; i < field.length; i++) {
				try {
					nombreMetodo = field[i].getName().substring(0,1).toUpperCase() + field[i].getName().substring(1);
					if ("IdObjeto".equals(nombreMetodo)) {
						continue;
					}
					metodoParent = classParent.getMethod("get"+nombreMetodo);
					Object value = metodoParent.invoke(parent);
					if (value == null) {
						continue;
					}
					metodoChild = classChild.getMethod("set"+nombreMetodo,field[i].getType());
					metodoChild.invoke(child,value);
					setted = true;
				} catch (Exception e) {/*This is expected*/}
			}
		} catch (Exception e) {
			System.out.println("EXCEPCION SETTER");
			System.out.println(e);
		}
		return setted;
	}

}
