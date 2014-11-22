package cosc561.tsp.strategy;

import java.util.PriorityQueue;
import java.util.Queue;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.model.branch.SparseBranch;
import cosc561.tsp.strategy.tour_generation.WorstFirstInsertion;
import cosc561.tsp.view.MapWindow;

public class BranchAndBoundPermutation extends Strategy {
	
	private static final int QUEUE_SIZE = 1000;

	private int rejected;

	private Queue<SparseBranch> permutationsInProgress;
	
	private SparseBranch bestTour;
	
	private RichBranch current;
	
	public BranchAndBoundPermutation(Graph graph, MapWindow window) {
		super(graph, window);
	}
	
	public void init() throws Exception {
		
		permutationsInProgress = new PriorityQueue<>(QUEUE_SIZE);
		
		//Naieve first candidate, just grab all nodes.
		//This can be improved by heuristic
		bestTour = new WorstFirstInsertion(graph, window).generate();

		permutationsInProgress.add(bestTour);
		
		rejected = 0;
	}

	@Override
	protected RichBranch next() {
		
		current = new RichBranch(permutationsInProgress.poll());
		
		if (current.isPermutationComplete()) {
				
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
		return new RichBranch(bestTour);
	}
}
