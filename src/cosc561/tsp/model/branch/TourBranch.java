package cosc561.tsp.model.branch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public class TourBranch extends RichBranch {
	
	/**
	 * Initial Constructor 
	 * 
	 * @param nodes
	 */
	public TourBranch(List<Node> path) {
		this(path, 1);
	}

	/**
	 * Permutation Constructor
	 * 
	 * @param that
	 * @param path
	 */
	public TourBranch(List<Node> path, int pivot) {
		super(path, calculateWeight(path), pivot);
	}

	/**
	 * Inflate Constructor
	 * 
	 * @param that
	 * @param graph
	 */
	public TourBranch(Branch that, Graph graph) {
		super(that, graph);
	}

	@Override
	public boolean isComplete() {
//		System.out.println(pivot +" - "+ this.path.size());
		return this.pivot >= this.path.size() - 1;
	}
	
	private static float calculateWeight(List<Node> nodes) {
		float weight = 0;
		
		Node previous = nodes.get(nodes.size() - 1);
		for (Node next : nodes) {
			weight += previous.distance(next);
			
			previous = next;
		}
		
		return weight;
	}

	public Collection<Branch> getPermutations() {
		Set<Branch> permutations = new HashSet<>();
		
		int size = this.path.size();
		for (int i = pivot + 1; i < size; i++) {
			List<Node> swapped = swap(pivot, i);
			
			float weight = calculateWeight(swapped);
			permutations.add(new Branch(swapped, weight, pivot + 1));
		}
		
		return permutations;
	}
	
	@Override
	public List<Node> getPath() {
		//Close the path before returning it
		List<Node> tour = super.getPath();
		tour.add(tour.get(0));
		return tour;
	}

	private List<Node> swap(int a, int b) {
		ArrayList<Node> swapped = new ArrayList<Node>(path);
		
		Node temp = swapped.get(a);
		swapped.set(a, swapped.get(b));
		swapped.set(b, temp);
		
		return swapped;
	}

}
