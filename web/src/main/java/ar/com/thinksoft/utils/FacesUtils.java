package ar.com.thinksoft.utils;

import java.io.ObjectInputStream;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import ar.com.thinksoft.beans.session.BBSessionData;

public class FacesUtils {

	private static Logger logger = Logger.getLogger(FacesUtils.class);

	public static Object getSessionValue(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
	}

	public static Object getRequestAttribute(String key) {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getAttribute(key);
	}

	public static String getRequestParameter(String key) {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter(key);
	}

	@SuppressWarnings("rawtypes")
	public static Object getRequestObject(Class objectType) {
		Object aux = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getInputStream());
			aux = ois.readObject();
			while (aux != null) {
				if (objectType.getClass().equals(aux.getClass())) {
					break;
				}
				aux = ois.readObject();
			}
		} catch (Exception e) {
			//TODO capturar excepcion y loggear en el log
		}
		return aux;

	}

	public static void setSessionValue(String key, Object obj) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, obj);
	}

	public static void removeSessionValue(String value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(value);
	}

	public static FacesContext getCurrentInstance() {
		return FacesContext.getCurrentInstance();
	}

	public static ELContext getELContext() {
		return FacesContext.getCurrentInstance().getELContext();
	}

	public static ExpressionFactory getExpressionFactory() {
		return FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
	}

	public static Object getBBCurrentInstance(String name) {
		FacesContext fc = FacesContext.getCurrentInstance();
		return fc.getELContext().getELResolver().getValue(fc.getELContext(), null, name);
	}

	public static Object createComponent(String componentType) {
		return FacesContext.getCurrentInstance()
				.getApplication().createComponent(componentType);
	}

	public static ValueExpression createValueExpression(String expression, Class<?> expectedType) {
		return FacesContext.getCurrentInstance().getApplication().getExpressionFactory()
				.createValueExpression(FacesContext.getCurrentInstance().getELContext(),
						expression, expectedType);
	}

	public static MethodExpression createMethodExpression(String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
		return FacesContext.getCurrentInstance().getApplication().getExpressionFactory()
				.createMethodExpression(FacesContext.getCurrentInstance().getELContext(),
						expression, expectedReturnType, expectedParamTypes);
	}

	public static MethodExpression createActionExpression(String expression, Class<?> expectedReturnType) {
		return FacesContext.getCurrentInstance().getApplication().getExpressionFactory()
				.createMethodExpression(FacesContext.getCurrentInstance().getELContext(),
						expression, expectedReturnType, new Class<?>[] {});
	}

	public static MethodExpressionActionListener createActionListenerExpression(String expression) {
		MethodExpression methodExpression = createMethodExpression(expression, null,
				new Class[] { ActionEvent.class });
		return new MethodExpressionActionListener(methodExpression);
	}

	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
	}

	public static void responseComplete() {
		FacesContext.getCurrentInstance().responseComplete();
	}

	public static String getContextPath() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}

	public static String getCurrentPage() {
		String aux = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString();
		aux = aux.split("pages/").length > 1 ? "/pages/" + aux.split("pages/")[1] : (aux.split("mobile/").length > 1 ? "/mobile/" : null);
		return aux;
	}

	public static String getFullContextPath() {
		String aux = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString();
		aux = aux.split("pages").length > 1 ? aux.split("pages")[0] : (aux.split("mobile").length > 1 ? aux.split("mobile")[0] : aux);
		return aux;
	}

	public static String getFullContextPathAplicacion() {
		String aux = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString();
		aux = aux.split("pages").length > 1 ? aux.split("pages")[0] : aux;
		return aux;
	}

	public static String getRequestURL() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString();
	}

	public static void redirect(String pageName) {
		redirect(pageName, false, false);
	}

	public static void redirect(String pageName, boolean removeSessionValues) {
		redirect(pageName, removeSessionValues, false);
	}

	public static void redirect(String pageName, boolean removeSessionValues, boolean stopPropagation) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect((!pageName.startsWith("http") ? getContextPath() : "") + pageName);
			BBSessionData.removePrintFiles();
			if (removeSessionValues) {
				BBSessionData.removeSessionValues();
			}
		} catch (Exception e) {
			String err = "No se encontro la pagina: " + pageName + "\nNo es posible navegar ... redirigiendo a pagina de error.";
			logger.error(err, e);
			if (!stopPropagation) {
				Utils.desconectarLogin();
			}
		}
	}

	public static void clearRequestSession() {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().clear();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
	}

	public static Map<String, String> getRequestParameterMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}

	public static void clearRequest() {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().clear();
	}

	public static void clearSession() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
	}

	public static void invalidateSession() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	public static void execute(String command) {
		PrimeFaces.current().executeScript(command);
	}

	public static void update(String command) {
		PrimeFaces.current().ajax().update(command);
	}

	public static void hideDialogs(String ... dialogs) {
		for (String command : dialogs) {
			execute("PF('" + command + "').hide()");
		}
	}

	public static void showDialogs(String ... dialogs) {
		for (String command : dialogs) {
			execute("PF('" + command + "').show()");
		}
	}

	public static HttpServletRequest getRequest () {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static String getServerPath() {
		HttpServletRequest request = getRequest();
		return request.getRequestURL().toString().replace(request.getRequestURI().substring(0), "");
	}

}