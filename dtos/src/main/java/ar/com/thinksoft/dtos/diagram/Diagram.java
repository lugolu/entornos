package ar.com.thinksoft.dtos.diagram;

import java.util.List;

public class Diagram {

	private List<Node> nodes;
	private List<Edge> edges;

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "Diagram [nodes=" + nodes + ", edges=" + edges + "]";
	}

}
