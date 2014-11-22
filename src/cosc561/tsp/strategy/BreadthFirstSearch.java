package cosc561.tsp.strategy;

import cosc561.tsp.Solver;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.model.branch.SparseBranch;
import cosc561.tsp.util.PartitionedQueue;
import cosc561.tsp.view.MapWindow;

public class BreadthFirstSearch extends Strategy {
	
	private static final int QUEUE_SIZE = 100000;
	
	public BreadthFirstSearch(Graph graph, MapWindow window) {
		super(graph, window);
	}

	PartitionedQueue<SparseBranch> branches;
	RichBranch current;
	
	Solver solver;
	
	public void init() {
		
		branches = new PartitionedQueue<>(QUEUE_SIZE);
		
		current = new RichBranch(graph.getRoot(), graph);
	}

	@Override
	protected RichBranch next() {
		for(Node node : current.getUnvisited()) {
			branches.add(new SparseBranch(current, node));
		}
	
		current = new RichBranch(branches.poll());
		
		return current;
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
