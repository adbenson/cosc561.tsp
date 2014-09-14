package cosc561.tsp.strategy;

import java.util.PriorityQueue;

import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public class UniformCost extends Strategy {
	
	public UniformCost(Graph graph) {
		super(graph);
	}

	PriorityQueue<Branch> branches;
	Branch current;
	
	Solver solver;
	
	public void init() {
		
		branches = new PriorityQueue<>();
		
		current = new Branch(graph.getRoot(), graph.getNodes());
	}

	@Override
	protected Branch next() {
		for(Node node : current.getUnvisited()) {
			branches.add(new Branch(current, node));
		}
	
		current = branches.poll();
		
		return current;
	}
}
