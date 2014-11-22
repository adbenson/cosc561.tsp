package cosc561.tsp.model;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
	
	private Map<Integer, Node> nodesById;
	private Path nodes;
	
	private int maxX = 0;
	private int maxY = 0;
	
	public Graph(Path nodes) {
		this(nodes, nodes.size());
	}
		
	public Graph(Path nodes, int nodeCap) {
		this.nodesById = new HashMap<>();
		this.nodes = new Path();
		addNodes(nodes, nodeCap);
	}
	
	private void addNode(Node node) {
		this.nodes.add(node);
		this.nodesById.put(node.id, node);
		this.maxX = Math.max(this.maxX, node.x);
		this.maxX = Math.max(this.maxX, node.x);
	}
	
	private void addNodes(Path nodes, int cap) {
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
	
	public Path getNodeList() {
		return new Path(nodes);
	}
	
	public Dimension getBounds() {
		return new Dimension(maxX, maxY);
	}

	
	public Node getRoot() {
		return nodes.get(0);
	}

	public Set<Node> getNodesNotIn(Path path) {
		Set<Node> others = new HashSet<>(nodes);
		others.removeAll(path);
		return others;
	}

}
