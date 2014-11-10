package cosc561.tsp.strategy;

import java.util.PriorityQueue;
import java.util.Queue;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.model.branch.SparseBranch;
import cosc561.tsp.view.MapWindow;

public class BranchAndBoundClassHeuristic extends Strategy {
	
	private static final int QUEUE_SIZE = 1000;

	private long rejected;
	private long intersected;

	private Queue<SparseBranch> pathsInProgress;
	
	private RichBranch bestTour;
	
	private RichBranch current;
	
	public BranchAndBoundClassHeuristic(Graph graph, MapWindow window) {
		super(graph, window);
	}
	
	public void init() {
		
		pathsInProgress = new PriorityQueue<>(QUEUE_SIZE, new SparseBranch.ReverseComparator());
		
		//Naieve first candidate, just grab all nodes.
		//This can be improved by heuristic
		bestTour = new RichBranch(graph.getNodeList(), graph);

		pathsInProgress.add(new RichBranch(graph.getRoot(), graph));
		
		rejected = 0;
		intersected = 0;
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
				if (!current.wouldIntersect(node)) {
					RichBranch tour = new RichBranch(current, node);
					
					if (tour.weight < bestTour.weight) {
						pathsInProgress.add(new SparseBranch(tour));
					}
					else {
						rejected++;
					}
				}
				else {
					intersected++;
				}
			}
		}

		if (pathsInProgress.isEmpty()) {
			return bestTour;
		}
		else {
			return new RichBranch(pathsInProgress.peek());
		}
	}
	
	@Override
	public void updateStats() {
		super.updateStats();
		stats.output("Rejected by Weight", rejected);
		stats.output("Rejected by Intersection", intersected);
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
