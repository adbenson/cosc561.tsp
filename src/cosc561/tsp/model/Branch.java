package cosc561.tsp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cosc561.tsp.util.Partitionable;

public class Branch implements Comparable<Branch>, Partitionable {
	
	List<Node> path;
	List<Edge> edges;
	
	Set<Node> unvisited;
		
	Node start;
	Node end;
	
	int weight;
	
	public Branch(Node node, Set<Node> unvisited) {
		this.path = new ArrayList<>();
		this.path.add(node);
		
		this.edges = new ArrayList<>();
		
		this.unvisited = new HashSet<>(unvisited);
		this.unvisited.remove(node);
		
		this.start = node;
		this.end = node;
		this.weight = 0;
	}

	public Branch(Branch that, Node node) {
		this.path = new ArrayList<>(that.path);
		this.path.add(node);
		
		this.edges = new ArrayList<>(that.edges);
		this.edges.add(new Edge(that.end, node));
				
		this.unvisited = new HashSet<>(that.unvisited);
		this.unvisited.remove(node);
		
		this.start = that.start;
		this.end = node;
		this.weight = that.weight + that.end.distance(node);
	}
	
	public Branch(LightweightBranch sparse, Graph graph) {
		this.weight = sparse.weight;
		this.path = sparse.nodePath(graph);
		
		this.unvisited = graph.getNodesNotIn(path);

		this.edges = buildEdges(path);

		this.start = path.get(0);
		this.end = path.get(path.size() - 1);
	}

	private List<Edge> buildEdges(List<Node> path) {
		List<Edge> edges = new ArrayList<>();
		
		Node previous = null;
		for (Node node : path) {
			if (previous != null) {
				edges.add(new Edge(previous, node));
			}
			
			previous = node;
		}
		
		return edges;
	}

	@Override
	public int compareTo(Branch that) {
		return this.weight - that.weight;
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
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}

	public int getWeight() {
		return weight;
	}
	
	@Override
	public int getPartition() {
		return path.size() / 10;
	}
	
	public LightweightBranch getSparse() {
		return new LightweightBranch(this);
	}

}