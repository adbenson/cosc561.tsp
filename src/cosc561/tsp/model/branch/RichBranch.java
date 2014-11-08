package cosc561.tsp.model.branch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import cosc561.tsp.model.Edge;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public abstract class RichBranch extends Branch {

	protected final List<Node> path;
	protected final List<Edge> edges;
		
	protected final Node start;

	/**
	 * Initial Tour Constructor 
	 * 
	 * @param start
	 * @param path
	 */
	public RichBranch(Node start, List<Node> path) {
		super(path, calculateWeight(path));
		
		this.path = new ArrayList<>(path);
		this.edges = buildEdges(path);
		
		this.start = path.get(0);
	}

	/**
	 * Initial Path Constructor
	 * 
	 * @param start
	 */
	public RichBranch(Node start) {
		super(start);
		
		this.path = new ArrayList<>();
		path.add(start);
		
		this.edges = new ArrayList<>();
		
		this.start = start;
	}

	/**
	 * Visit Constructor
	 * 
	 * @param that
	 * @param visit
	 */
	public RichBranch(PathBranch that, Node visit) {
		super(that, visit);
		
		this.path = new ArrayList<>(that.path);
		this.path.add(visit);
		
		this.edges = new ArrayList<>(that.edges);
		this.edges.add(new Edge(that.end, visit));
		
		this.start = that.start;
	}

	/**
	 * Inflate Constructor
	 * 
	 * @param that
	 * @param graph
	 */
	public RichBranch(Branch that, Graph graph) {
		super(that);
		
		this.path = that.nodePath(graph);
		
		this.edges = buildEdges(path);

		this.start = path.get(0);
	}

	private static List<Edge> buildEdges(List<Node> path) {
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
	
	private static float calculateWeight(List<Node> nodes) {
		float weight = 0;
		
		Node previous = null;
		for (Node next : nodes) {
			if (previous != null) {
				weight += previous.distance(next);
			}
			
			previous = next;
		}
		
		return weight;
	}
	
	public abstract boolean isComplete();
	
	public List<Node> getPath() {
		return new ArrayList<>(path);
	}
	
	public List<Edge> getEdges() {
		return new ArrayList<>(edges);
	}
	
	public Node getStart() {
		return start;
	}
	
	@Override
	public int getPartition() {
		return path.size() / 10;
	}
	
	public Branch getLightweight() {
		return new Branch(this);
	}
	
}
