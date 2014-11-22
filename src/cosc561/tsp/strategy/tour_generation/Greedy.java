package cosc561.tsp.strategy.tour_generation;

import java.util.LinkedList;
import java.util.Queue;

import cosc561.tsp.Solver;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.view.MapWindow;

public class Greedy extends TourGenerator {
	
	public Greedy(Graph graph, MapWindow window) {
		super(graph, window);
	}

	Queue<RichBranch> branches;
	RichBranch current;
	
	Solver solver;
	
	public void init() {
		
		branches = new LinkedList<>();
		
		current = new RichBranch(graph.getRoot(), graph);
	}

	@Override
	protected RichBranch next() {
		current = new RichBranch(current, current.getEnd().getNearest(current.getUnvisited()));
		
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
