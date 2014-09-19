package cosc561.tsp.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
	
	private ArrayList<Node> nodes;
	
	private int maxX = 0;
	private int maxY = 0;
		
	public Graph() {
		nodes = new ArrayList<>();
	}
	
	public void addNode(Node node) {
		this.nodes.add(node);
		this.maxX = Math.max(this.maxX, node.x);
		this.maxX = Math.max(this.maxX, node.x);
	}
	
	public void addNodes(List<Node> nodes) {
		addNodes(nodes, nodes.size());
	}
	
	public void addNodes(List<Node> nodes, int cap) {
		
		for(int i = 0; i < cap && i < nodes.size(); i++) {
			addNode(nodes.get(i));
		}
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

}
