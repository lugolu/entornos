package ar.com.thinksoft.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.interfaces.ExceptionLogger;

public class MessageManager {

	public static void addToMessage(String componentId, String severity, String message) {
		addMessage(componentId, severity, null, message);
	}

	public static void addToMessages(String severity, String message) {
		addMessage(null, severity, null, message);
	}

	public static void addToMessages(String severity, String head, String message) {
		addMessage(null, severity, head, message);
	}

	public static void addToMessage(String componentId, Exception e) {
		if (e instanceof BusinessException) {
			BusinessException be = (BusinessException) e;
			addMessage(componentId, be.getSeverity(), null, be.getMessage());
		}
		else {
			//			Utils.desconectarError();
			FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), e.getMessage()));
		}
		ExceptionLogger.getInstancia().logException(e);
	}

	public static void addToMessages(Exception e) {
		if (e instanceof BusinessException) {
			BusinessException be = (BusinessException) e;
			addToMessages(be.getSeverity(), be.getMessage());
		}
		else {
			//			Utils.desconectarError();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), e.getMessage()));
		}
		ExceptionLogger.getInstancia().logException(e);
	}

	// GENERIC METHOD
	public static void addMessage(String componentId, String severity, String head, String message) {
		if (severity.equals(SeverityBundle.ERROR)) {
			if(head == null) {
				head = MessageBundle.ERROR;
			}
			FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR, head, message));
		} else if (severity.equals(SeverityBundle.WARN)) {
			if(head == null) {
				head = MessageBundle.ATENCION;
			}
			FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_WARN, head, message));
		} else if (severity.equals(SeverityBundle.INFO)) {
			if(head == null) {
				head = MessageBundle.INFO;
			}
			FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_INFO, head, message));
		} else if (severity.equals(SeverityBundle.FATAL)) {
			if(head == null) {
				head = MessageBundle.FATAL;
			}
			//			Utils.desconectarError();
			FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_FATAL, head, message));
		}
	}
}
