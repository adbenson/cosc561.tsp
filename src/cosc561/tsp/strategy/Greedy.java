package cosc561.tsp.strategy;

import java.util.LinkedList;
import java.util.Queue;

import cosc561.tsp.Solver;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.PathBranch;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.view.MapWindow;

public class Greedy extends Strategy {
	
	public Greedy(Graph graph, MapWindow window) {
		super(graph, window);
	}

	Queue<PathBranch> branches;
	PathBranch current;
	
	Solver solver;
	
	public void init() {
		
		branches = new LinkedList<>();
		
		current = new PathBranch(graph.getRoot(), graph.getNodes());
	}

	@Override
	protected PathBranch next() {
		current = new PathBranch(current, current.getEnd().getNearest(current.getUnvisited()));
		
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
