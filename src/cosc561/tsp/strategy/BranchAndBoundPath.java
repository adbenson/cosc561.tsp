package cosc561.tsp.strategy;

import java.util.PriorityQueue;
import java.util.Queue;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.model.branch.SparseBranch;
import cosc561.tsp.view.MapWindow;

public class BranchAndBoundPath extends Strategy {
	
	private static final int QUEUE_SIZE = 1000;

	private long rejected;

	private Queue<SparseBranch> pathsInProgress;
	
	private RichBranch bestTour;
	
	private RichBranch current;
	
	public BranchAndBoundPath(Graph graph, MapWindow window) {
		super(graph, window);
	}
	
	public void init() {
		
		pathsInProgress = new PriorityQueue<>(QUEUE_SIZE);
		
		//Naieve first candidate, just grab all nodes.
		//This can be improved by heuristic
		bestTour = new RichBranch(graph.getNodeList(), graph);

		pathsInProgress.add(new RichBranch(graph.getRoot(), graph));
		
		rejected = 0;
	}

	@Override
	protected RichBranch next() {
		
		current = new RichBranch(pathsInProgress.poll());
		
		if (current.isComplete()) {
			
			if (current.weight < bestTour.weight) {
				bestTour = current;
				
				debug("Improvement! "+current.weight);
			}
			else {
				rejected++;
			}
				
		}
		else {
			for (Node node : current.getUnvisited()) {
				RichBranch tour = new RichBranch(current, node);
				
				if (tour.weight < bestTour.weight) {
					pathsInProgress.add(new SparseBranch(tour));
				}
				else {
					rejected++;
				}
			}
		}

		return current;
	}
	
	@Override
	public void updateStats() {
		super.updateStats();
		stats.output("Rejected Paths", rejected);
		stats.output("Current Best Distance", bestTour.weight);
		stats.output("Queue Capacity", pathsInProgress.size());
	}

	@Override
	public boolean isComplete() {
		return pathsInProgress.isEmpty();
	}

	@Override
	public RichBranch getSolution() {
		return bestTour;
	}
}
