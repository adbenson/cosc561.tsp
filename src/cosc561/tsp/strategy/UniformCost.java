package cosc561.tsp.strategy;

import java.util.PriorityQueue;

import cosc561.tsp.Solver;
import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.LightweightBranch;
import cosc561.tsp.util.PartitionedQueue;
import cosc561.tsp.view.MapWindow;

public class UniformCost extends Strategy {
	
	private static final int QUEUE_SIZE = 100000;
	
	public UniformCost(Graph graph, MapWindow window) {
		super(graph, window);
	}

	PartitionedQueue<LightweightBranch> branches;
	Branch current;
	
	Solver solver;
	
	public void init() {
		
		branches = new PartitionedQueue<>(100000);
		
		current = new Branch(graph.getRoot(), graph.getNodes());
	}

	@Override
	protected Branch next() {
		for(Node node : current.getUnvisited()) {
			branches.add(new Branch(current, node).getSparse());
		}
	
		current = new Branch(branches.poll(), graph);
		
		return current;
	}

	@Override
	public boolean isComplete() {
		return current.isComplete();
	}
}
