package cosc561.tsp.model.branch;

import java.util.HashSet;
import java.util.Set;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public class PathBranch extends RichBranch {
	
	protected final Set<Node> unvisited;
	protected final Node end;

	/**
	 * Initial Constructor
	 * 
	 * @param start
	 * @param unvisited
	 */
	public PathBranch(Node start, Set<Node> unvisited) {
		super(start);
		
		this.unvisited = new HashSet<>(unvisited);
		this.end = this.path.get(this.path.size() - 1);
	}

	/**
	 * Visit Constructor
	 * 
	 * @param that
	 * @param visit
	 */
	public PathBranch(PathBranch that, Node visit) {
		super(that, visit);

		this.unvisited = new HashSet<>(that.unvisited);
		this.unvisited.remove(visit);

		this.end = visit;
	}

	/**
	 * Inflate Constructor
	 * 
	 * @param that
	 * @param graph
	 */
	public PathBranch(Branch that, Graph graph) {
		super(that, graph);
		
		this.unvisited = graph.getNodesNotIn(this.path);
		this.end = this.path.get(this.path.size() - 1);
	}

	public boolean isComplete() {
		return unvisited.isEmpty();
	}
	
	public Set<Node> getUnvisited() {
		return new HashSet<>(unvisited);
	}

	public Node getEnd() {
		return end;
	}
	
}