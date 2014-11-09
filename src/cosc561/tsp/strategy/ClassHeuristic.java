package cosc561.tsp.strategy;

import cosc561.tsp.model.Edge;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.Branch;
import cosc561.tsp.model.branch.PathBranch;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.util.PartitionedQueue;
import cosc561.tsp.view.MapWindow;

public class ClassHeuristic extends Strategy {
	
	private static final int QUEUE_SIZE = 100000;

	PartitionedQueue<Branch> branches;
	PathBranch current;
	
	private int rejected;
	
	public ClassHeuristic(Graph graph, MapWindow window) {
		super(graph, window);
	}
	
	public void init() {

		branches = new PartitionedQueue<>(QUEUE_SIZE + graph.getNodes().size());
		
		current = new PathBranch(graph.getRoot(), graph.getNodes());
		
		rejected = 0;
	}

	@Override
	protected PathBranch next() {
		for(Node node : current.getUnvisited()) {
			if (nonIntersecting(current, node)) {
				branches.add(new Branch(current, node));
			}
			else {
				rejected++;
				Thread.yield();
			}
		}
	
		current = new PathBranch(branches.poll(), graph);
		if (current == null) {
			System.err.println("branches empty");
		}
		
		return current;
	}
	
	@Override
	public void updateStats() {
		super.updateStats();
		stats.output("Rejected Paths", rejected);
	}

	private boolean nonIntersecting(PathBranch branch, Node node) {
		Edge newEdge = new Edge(branch.getEnd(), node);
		
		for (Edge edge : branch.getEdges()) {
			if (newEdge.intersects(edge)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean isComplete() {
		return current.isComplete();
	}

	@Override
	public RichBranch getSolution() {
		return current;
	}
}
