package cosc561.tsp.strategy;

import java.util.LinkedList;
import java.util.Queue;

import cosc561.tsp.Solver;
import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.view.MapWindow;

public class Greedy extends Strategy {
	
	public Greedy(Graph graph, MapWindow window) {
		super(graph, window);
	}

	Queue<Branch> branches;
	Branch current;
	
	Solver solver;
	
	public void init() {
		
		branches = new LinkedList<>();
		
		current = new Branch(graph.getRoot(), graph.getNodes());
	}

	@Override
	protected Branch next() {
		current = new Branch(current, current.getEnd().getNearest(current.getUnvisited()));
		
		return current;
	}

	@Override
	public boolean isComplete() {
		return current.isComplete();
	}
}
