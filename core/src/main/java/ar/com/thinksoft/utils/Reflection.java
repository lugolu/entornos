package ar.com.thinksoft.utils;

import java.lang.reflect.Method;

import ar.com.thinksoft.exception.BusinessException;

public class Reflection {
	
	private static Reflection instancia = new Reflection();
	
	private Reflection() {
	}

	public static Reflection getInstancia() {
		return instancia;
	}

	public Object invokeMethod(Object object, String property) throws BusinessException{ 
		try {
			Method method = object.getClass().getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1));
			return method.invoke(object);
		} catch (Exception e) {
		  HandlerException.getInstancia().treateException(e, getClass());
		}
		return null;
	}

}
