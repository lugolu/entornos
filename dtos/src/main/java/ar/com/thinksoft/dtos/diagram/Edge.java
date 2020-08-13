package ar.com.thinksoft.dtos.diagram;

import java.util.List;

public class Edge {

	private List<DataEdge> data;

	public List<DataEdge> getData() {
		return data;
	}

	public void setData(List<DataEdge> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Edge [data=" + data + "]";
	}

}
