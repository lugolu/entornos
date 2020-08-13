package ar.com.thinksoft.dtos.diagram;

public class Node {

	private DataNode data;
	private Position position;

	public DataNode getData() {
		return data;
	}

	public void setData(DataNode data) {
		this.data = data;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "{data: " + data + (position != null ? (", position: {" + position + "} ") : "") + " }\n";
	}

}
