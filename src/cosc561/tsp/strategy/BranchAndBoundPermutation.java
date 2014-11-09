package cosc561.tsp.strategy;

import java.util.PriorityQueue;
import java.util.Queue;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.Branch;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.model.branch.TourBranch;
import cosc561.tsp.view.MapWindow;
import cosc561.tsp.view.Output;

public class BranchAndBoundPermutation extends Strategy {
	
	private static final int QUEUE_SIZE = 100000;

	private int rejected;

	private Queue<Branch> permutationsInProgress;
	
	private Branch bestTour;
	
	private TourBranch current;
	
	public BranchAndBoundPermutation(Graph graph, MapWindow window) {
		super(graph, window);
	}
	
	public void init() {
		
		permutationsInProgress = new PriorityQueue<>(1000);
		
		//Naieve first candidate, just grab all nodes.
		//This can be improved by heuristic
		bestTour = new TourBranch(graph.getNodeList());

		permutationsInProgress.add(bestTour);
		
		rejected = 0;
	}

	@Override
	protected RichBranch next() {
		
		current = new TourBranch(permutationsInProgress.poll(), graph);
		
		if (current.isComplete()) {
				
			if (current.weight < bestTour.weight) {
				bestTour = current;
				
				debug("Improvement! "+current.weight);
				System.out.println(permutationsInProgress.size());
			}
			else {
				rejected++;
			}
				
		}
		else {
			permutationsInProgress.addAll(current.getPermutations());		
		}

		return current;
	}
	
	@Override
	public void updateStats() {
		super.updateStats();
		stats.output("Rejected Paths", rejected);
	}

	@Override
	public boolean isComplete() {
		return permutationsInProgress.isEmpty();
	}

	@Override
	public RichBranch getSolution() {
		return new TourBranch(bestTour, graph);
	}
}
