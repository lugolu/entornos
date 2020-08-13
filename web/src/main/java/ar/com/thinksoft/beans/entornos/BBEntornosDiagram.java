package ar.com.thinksoft.beans.entornos;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ar.com.thinksoft.beans.session.UserSession;
import ar.com.thinksoft.delegators.Entornos;
import ar.com.thinksoft.dtos.diagram.DataNode;
import ar.com.thinksoft.dtos.diagram.Diagram;
import ar.com.thinksoft.dtos.diagram.Edge;
import ar.com.thinksoft.dtos.diagram.Node;
import ar.com.thinksoft.dtos.entornos.Servidor;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.utils.FacesUtils;

@ManagedBean(name = "bbEntornosDiagram")
@ViewScoped
public class BBEntornosDiagram implements Serializable {

	private static final long serialVersionUID = -8439330980044838974L;

	private UserSession user;

	private Diagram diagram;

	public BBEntornosDiagram() {
		user = FacesUtils.getUserSesssion();

		newDiagram();
	}

	private void newDiagram() {
		diagram = new Diagram();
		diagram.setNodes(new LinkedList<Node>());
		diagram.setEdges(new LinkedList<Edge>());
	}

	private void initDiagram() throws BusinessException {
		newDiagram();

		BBEntornos bbEntornos = (BBEntornos) FacesUtils.getBBCurrentInstance("bbEntornos");

		BBEntornosTopbar bbEntornosTopbar = bbEntornos.getBbEntornosTopbar();

		List<Servidor> listServidores = new LinkedList<Servidor>();
		if (bbEntornosTopbar.getSelectedCliente().getIdCliente() != null) {
			Servidor servidor = new Servidor();
			servidor.setIdCliente(bbEntornosTopbar.getSelectedCliente().getIdCliente());
			listServidores = Entornos.selectServidorEqual(servidor, user.getSession());

			for (Servidor s : listServidores) {
				addServerNode (s);
			}
		}

		System.out.println(diagram);
	}

	public void actChangeCliente () throws BusinessException {
		initDiagram();
	}

	public void agregarServidor (Servidor servidor) {
		addServerNode (servidor);
	}

	private void addServerNode(Servidor s) {
		DataNode data = new DataNode();
		data.setId(s.getServidor() + " (" + s.getIp() + ")");

		Node node = new Node();
		node.setData(data);

		diagram.getNodes().add(node);
	}

	public String getJson () {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(diagram);
	}

}
