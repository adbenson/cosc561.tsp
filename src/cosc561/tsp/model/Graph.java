package cosc561.tsp.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Graph {
	
	private HashSet<Node> nodes;
	
	private int maxX = 0;
	private int maxY = 0;
	
	public Graph() {
		nodes = new HashSet<Node>();
	}
	
	public void addNode(Node node) {
		this.nodes.add(node);
		this.maxX = Math.max(this.maxX, node.x);
		this.maxX = Math.max(this.maxX, node.x);
	}
	
	public void addNode(int x, int y) {
		addNode(new Node(x, y));
	}
	
	public List<Node> getNodes() {
		return new ArrayList<Node>(nodes);
	}
	
	public Dimension getBounds() {
		return new Dimension(maxX, maxY);
	}

}
