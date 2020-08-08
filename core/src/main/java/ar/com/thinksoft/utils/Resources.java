package ar.com.thinksoft.utils;

import java.util.ResourceBundle;

public class Resources {

	private static ResourceBundle resource = ResourceBundle.getBundle("ar.com.thinksoft.resources.Resources");
	private static ResourceBundle redefinedResource = null;

	public static void setRedefinedResources(String clientName) {
		redefinedResource = ResourceBundle.getBundle("ar.com.thinksoft.resources.Resources" + clientName);
	}

	public static String getValue(String key) {
		if(key == null) {
			return "";
		}
		if(key.contains(Constants.STRING_WHITESPACE)) {
			return key;
		}
		String msg = null;
		if (redefinedResource != null) {
			try {
				msg = redefinedResource.getString(key);
			} catch (Exception e) {}
		}
		if (msg == null || msg.startsWith("???")) {
			try {
				msg = resource.getString(key);
			} catch (Exception e) {}
		}
		if(msg != null){
			return msg;
		} else {
			System.out.println("Resources no encontrado  [" + key + "]");
			return "#" + key;
		}
	}

	public static String getValueHtml(String key){
		String msg = getValue(key);

		if(!msg.startsWith("#")){
			msg = msg.replace("\u00E1", "&#225;");
			msg = msg.replace("\u00E9", "&#233;");
			msg = msg.replace("\u00ED", "&#237;");
			msg = msg.replace("\u00F3", "&#243;");
			msg = msg.replace("\u00FA", "&#250;");
			msg = msg.replace("\u00F1", "&#241;");
		}

		return msg;
	}
}
