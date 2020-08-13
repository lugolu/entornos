package ar.com.thinksoft.dtos.diagram;

public class DataNode {

	private String id;
	private String parent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "{id: '" + id + "'" + (parent != null ? (", parent: '" + parent + "'") : "") + "}";
	}

}
