package cosc561.tsp.model.branch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cosc561.tsp.model.Edge;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public class RichBranch extends SparseBranch {

	protected final List<Node> path;
	protected final List<Edge> edges;

	protected final Node start;
	protected final Node end;
	
	protected final Set<Node> unvisited;

	/**
	 * Initial Path Constructor
	 * 
	 * @param start
	 * @param unvisited
	 */
	public RichBranch(Node start, Graph graph) {
		super(start, graph);
		
		this.path = new ArrayList<Node>();
		this.path.add(start);
		
		this.edges = new ArrayList<>();
		
		this.start = start;
		this.end = start;
		
		this.unvisited = new HashSet<>(graph.getNodes());
		this.unvisited.remove(start);
	}
	
	/**
	 * Initial Tour Constructor
	 * @param graph
	 */
	public RichBranch(List<Node> path, Graph graph) {
		super(path, graph);
		
		this.path = new ArrayList<>(path);
		
		this.edges = buildEdges(this.path);
		
		this.start = this.path.get(0);
		this.end = this.path.get(this.path.size() - 1);
		
		this.unvisited = new HashSet<>(graph.getNodesNotIn(this.path));
	}

	/**
	 * Visit Constructor
	 * 
	 * @param that
	 * @param visit
	 */
	public RichBranch(RichBranch that, Node visit) {
		super(that, visit);

		this.path = new ArrayList<>(that.path);
		this.path.add(visit);
		
		this.edges = buildEdges(this.path);
		
		this.start = that.start;
		this.end = visit;
		
		this.unvisited = new HashSet<>(that.unvisited);
		this.unvisited.remove(visit);
	}

	/**
	 * Inflate Constructor
	 * 
	 * @param that
	 * @param graph
	 */
	public RichBranch(SparseBranch that) {
		super(that);
		
		this.path = nodePath(that.path, graph);
		
		this.edges = buildEdges(this.path);
		
		this.start = this.path.get(0);
		this.end = this.path.get(this.path.size() - 1);
		
		this.unvisited = new HashSet<>(graph.getNodesNotIn(this.path));
	}
	
	private static List<Node> initPath(Node start, List<Node> path) {
		List<Node> newPath;
		
		if (path == null || path.isEmpty()) {
			newPath = new ArrayList<>();
			newPath.add(start);
		}
		else {
			newPath = new ArrayList<>(path);
		}
		
		return newPath;
	}

	public boolean isComplete() {
		return unvisited.isEmpty();
	}
	
	protected List<Node> getPath() {
		return new ArrayList<>(path);
	}
	
	public List<Node> getTour() {
		List<Node> tour = getPath();
		if (tour.size() > 1) {
			tour.add(start);
		}
		return tour;
	}
	
	public List<Edge> getEdges() {
		return new ArrayList<>(edges);
	}
	
	public Set<Node> getUnvisited() {
		return new HashSet<>(unvisited);
	}
	
	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}
	
	private static List<Node> nodePath(byte[] path, Graph graph) {
		List<Node> nodes = new ArrayList<Node>();
		
		for (byte id : path) {
			nodes.add(graph.getNode(id));
		}
		
		return nodes;
	}

	private static List<Edge> buildEdges(List<Node> path) {
		List<Edge> edges = new ArrayList<>();
		
		if (path.size() < 2) {
			return edges;
		}
		
		Node previous = path.get(path.size() - 1);
		for (Node node : path) {
			edges.add(new Edge(previous, node));
			
			previous = node;
		}
		
		return edges;
	}
	
}
