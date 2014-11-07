package cosc561.tsp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import cosc561.tsp.model.Branch.SparseBranch.SparseBranchException;

public class Branch implements Comparable<Branch> {
	
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
	
	public Branch(SparseBranch sparse, Graph graph) throws SparseBranchException {
		this.weight = sparse.weight;
		this.path = sparse.nodes(graph);
		
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
	
	public SparseBranch getSparse() throws SparseBranchException {
		return new SparseBranch(this);
	}
	
	public static class SparseBranch implements Comparable<SparseBranch> {
		public final short weight;
		public final byte[] path;
		
		private SparseBranch(Branch branch) {
			this.weight = (short) branch.weight;
			this.path = new byte[branch.path.size()];
			
			for (int i = 0; i < path.length; i++) {
				this.path[i] = (byte) branch.path.get(i).id;
			}
		}
		
		private List<Node> nodes(Graph graph) {
			List<Node> nodes = new ArrayList<Node>();
			
			for (byte id : path) {
				nodes.add(graph.getNode(id));
			}
			
			return nodes;
		}
		
		@Override
		public int compareTo(SparseBranch that) {
			return this.weight - that.weight;
		}
		
		public class SparseBranchException extends Exception {
			private static final long serialVersionUID = 1L;
			
			public SparseBranchException(String msg) {			
				this(msg, null);
			}
			
			public SparseBranchException(String msg, Throwable t) {
				super(msg, t);
			}
		}
	}
}