package ar.com.thinksoft.dtos;

public class ClaseMapeo {

	private String className;
	private String fullClass;

	public ClaseMapeo() {
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className.toLowerCase();
	}

	public String getFullClass() {
		return fullClass;
	}

	public void setFullClass(String fullClass) {
		this.fullClass = fullClass;
	}

}
