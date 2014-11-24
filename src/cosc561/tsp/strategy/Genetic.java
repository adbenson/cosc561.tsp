package cosc561.tsp.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Path;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.classes.TourGenerationStrategies;
import cosc561.tsp.view.MapWindow;

public class Genetic extends Strategy {
	
	public static final int POPULATION_SIZE = 7;

	private static final long MAX_GENERATIONS = 1000000;
	
	private Random rand;
	
	private List<RichBranch> population;
	
	private RichBranch best;	

	public Genetic(Graph graph, MapWindow window) {
		super(graph, window);
	}

	@Override
	public void init() throws Exception {
		population = new ArrayList<RichBranch>(POPULATION_SIZE);
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			RichBranch child = generate(TourGenerationStrategies.RANDOM);
			population.add(child);
		}
		
		Collections.sort(population);
		best = population.get(0);
		rand = new Random();
	}

	@Override
	protected RichBranch next() throws Exception {
		double total = totalPopulationWeight();
		
		List<RichBranch> next = new ArrayList<>(POPULATION_SIZE);
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			RichBranch p1 = choose(total);
			RichBranch p2 = choose(total, p1);
			
			RichBranch child = reproduce(p1, p2);
			next.add(child);
		}
		
		Collections.sort(next);
		RichBranch bestChild = next.get(0);
		
		if (bestChild.weight < best.weight) {
			best = bestChild;
		}
		
		population = next;
		return bestChild;
	}

	private RichBranch choose(double total) {
		return choose(total, null);
	}

	private RichBranch choose(double total, RichBranch other) {
		RichBranch candidate = other;
		
		while (candidate == other) {
			candidate = random(total);
		}
		
		return candidate;
	}

	private RichBranch random(double total) {
		return population.get(rand.nextInt(POPULATION_SIZE));
	}

	private RichBranch reproduce(RichBranch p1, RichBranch p2) {
		//left will end up being used as the start of the path, right will be the end.
		Path left;
		Path right;
		
		//We want to avoid any accidental preference
		if (rand.nextBoolean()) {
			left = p1.getPath();
			right = p2.getPath();
		}
		else {
			left = p2.getPath();
			right = p1.getPath();
		}
		
		assert(left.size() == right.size());
		
		int length = left.size();
		
		//The pivot is the point where the two paths will be split
		int pivot = rand.nextInt(length - 2) + 2;
		
		//Any way we split it, one half of the path will be better preserved than the other.
		//So let's make sure we don't give preference to any particular half.
		if (rand.nextBoolean()) {
			left = left.subPath(0, pivot);
			right.removeAll(left);
		}
		else {
			right = right.subPath(pivot, length);
			left.removeAll(right);
		}
		
		//Put it back together!
		Path child = new Path(left);
		child.addAll(right);
		
		if (child.size() != length) {
			Path a = p1.getPath();
			Collections.sort(a);
			System.out.println(a.toString());
			Path b = p2.getPath();
			Collections.sort(b);
			System.out.println(b.toString());
			Collections.sort(left);
			System.out.println(left.toString());
			Collections.sort(right);
			System.out.println(right.toString());
			Collections.sort(child);
			System.out.println(child.toString());
			for (int i=0; i<length; i++) {
				System.out.print(i+", ");
			}
		}
		return new RichBranch(child, graph);	
	}

	private double totalPopulationWeight() {
		double total = 0;
		
		for (RichBranch branch : population) {
			total += branch.weight;
		}
		
		return total;
	}

	@Override
	public boolean isComplete() {
		return getIteration() > MAX_GENERATIONS;
	}

	@Override
	public RichBranch getSolution() {
		return best;
	}

}
