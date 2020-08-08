package ar.com.thinksoft.resources;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import ar.com.thinksoft.utils.Resources;

@ManagedBean(name="msgMgr")
@ApplicationScoped
public class MessageManager {

	public String getMessage(String key) {

		// evaluamos con JAVA
		return Resources.getValue(key);
		// evaluamos con JSF
		//return (String)FacesUtils.resolveExpression("#{msgstr['" + key + "']}");
	}

    public static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
            loader = fallbackClass.getClass().getClassLoader();
        return loader;
    }

}
