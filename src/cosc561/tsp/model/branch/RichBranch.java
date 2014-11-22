package cosc561.tsp.model.branch;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cosc561.tsp.model.Edge;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.Path;

public class RichBranch extends SparseBranch {

	protected final Path path;
	
	protected final Set<Node> unvisited;

	/**
	 * Initial Path Constructor
	 * 
	 * @param start
	 * @param unvisited
	 */
	public RichBranch(Node start, Graph graph) {
		super(start, graph);
		
		this.path = new Path();
		this.path.add(start);
		
		this.unvisited = new HashSet<>(graph.getNodes());
		this.unvisited.remove(start);
	}
	
	/**
	 * Generic path constructor
	 * @param graph
	 */
	public RichBranch(Collection<Node> path, Graph graph) {
		this(new Path(path), graph);
	}

	
	/**
	 * Initial Tour Constructor
	 * @param graph
	 */
	public RichBranch(Path path, Graph graph) {
		super(path, graph);
		
		this.path = new Path(path);
		
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

		this.path = new Path(that.path);
		this.path.add(visit);

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
		
		this.path = new Path(that.path, that.graph);

		this.unvisited = new HashSet<>(graph.getNodesNotIn(this.path));
	}

	public boolean isComplete() {
		return unvisited.isEmpty();
	}
	
	public Path getPath() {
		return new Path(path);
	}
	
	public Path getTour() {
		return path.getTour();
	}
	
	public List<Edge> getEdges() {
		return path.getEdges();
	}
	
	public Set<Node> getUnvisited() {
		return new HashSet<>(unvisited);
	}
	
	public Node getStart() {
		return path.getStart();
	}

	public Node getEnd() {
		return path.getEnd();
	}
	
}
