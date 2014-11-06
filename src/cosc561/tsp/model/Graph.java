package cosc561.tsp.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
	
	private Map<Integer, Node> nodesById;
	private List<Node> nodes;
	
	private int maxX = 0;
	private int maxY = 0;
	
	public Graph(List<Node> nodes) {
		this(nodes, nodes.size());
	}
		
	public Graph(List<Node> nodes, int nodeCap) {
		this.nodesById = new HashMap<>();
		this.nodes = new ArrayList<>();
		addNodes(nodes, nodeCap);
	}
	
	private void addNode(Node node) {
		this.nodes.add(node);
		this.nodesById.put(node.id, node);
		this.maxX = Math.max(this.maxX, node.x);
		this.maxX = Math.max(this.maxX, node.x);
	}
	
	private void addNodes(List<Node> nodes, int cap) {
		for(int i = 0; i < cap && i < nodes.size(); i++) {
			addNode(nodes.get(i));
		}
	}
	
	public Node getNode(int id) {
		return nodesById.get(id);
	}
	
	public Set<Node> getNodes() {
		return new HashSet<>(nodes);
	}
	
	public List<Node> getNodeList() {
		return nodes;
	}
	
	public Dimension getBounds() {
		return new Dimension(maxX, maxY);
	}

	
	public Node getRoot() {
		return nodes.get(0);
	}

	public Set<Node> getNodesNotIn(List<Node> path) {
		Set<Node> others = new HashSet<>(nodes);
		others.removeAll(path);
		return others;
	}

}
