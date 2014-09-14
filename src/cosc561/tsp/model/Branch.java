package cosc561.tsp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Branch implements Comparable<Branch> {
	
	final List<Node> path;
	
	final HashSet<Node> unvisited;
	
	final int weight;
	
	final Node head;
	
	public Branch(Node node, Set<Node> unvisited) {
		this.path = new ArrayList<>();
		this.unvisited = new HashSet<>(unvisited);
		
		this.head = node;
		this.weight = 0;
				
		visit(node);
	}

	public Branch(Branch branch, Node node) {
		this.path = new ArrayList<>(branch.path);
		this.unvisited = new HashSet<>(branch.unvisited);

		this.head = node;
		this.weight = branch.weight + branch.head.distance(node);;

		this.visit(node);
	}
	
	public void visit(Node node) {
		this.path.add(node);
		this.unvisited.remove(node);
	}

	@Override
	public int compareTo(Branch that) {
		return that.weight - this.weight;
	}
	
	public boolean isComplete() {
		return unvisited.isEmpty();
	}
	
	public Set<Node> getUnvisited() {
		return unvisited;
	}
	
	public List<Node> getPath() {
		return path;
	}
}