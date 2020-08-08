package ar.com.thinksoft.jsf;

public class GrupoNestedTable {

	private String methodId;
	private String methodLabel;
	private boolean reqFooter;

	public GrupoNestedTable(String methodId, String methodLabel) {
		this.methodId = methodId;
		this.methodLabel = methodLabel;
	}

	public GrupoNestedTable(String methodId, String methodLabel, boolean reqFooter) {
		this.methodId = methodId;
		this.methodLabel = methodLabel;
		this.reqFooter = reqFooter;
	}

	public String getMethodId() {
		return methodId;
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}

	public String getMethodLabel() {
		return methodLabel;
	}

	public void setMethodLabel(String methodLabel) {
		this.methodLabel = methodLabel;
	}

	public boolean isReqFooter() {
		return reqFooter;
	}

	public void setReqFooter(boolean reqFooter) {
		this.reqFooter = reqFooter;
	}

}