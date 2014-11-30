package cosc561.tsp.strategy;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Path;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.classes.TourGenerationStrategies;
import cosc561.tsp.view.MapWindow;

public class Genetic extends Strategy {
	
	public static final int POPULATION_SIZE = 30;

	private static final long MAX_GENERATIONS = 1000000;
	
	private static final double MUTATION_RATE = 0.1;
	
	private Random rand;
	
	private TreeMap<RichBranch, Double> population;
	
	private RichBranch best;	
	
	private long mutations;
	private long unaltered;
	private long children;

	public Genetic(Graph graph, MapWindow window) {
		super(graph, window);
	}

	@Override
	public void init() throws Exception {
		population = new TreeMap<>();
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			RichBranch child = generate(TourGenerationStrategies.RANDOM);
			population.put(child, 1.0/child.weight);
		}
		
		best = population.firstKey();
		rand = new Random();
		
		mutations = 0;
		unaltered = 0;
		
		stats.writeLogLine("Population Size: "+POPULATION_SIZE);
		stats.writeLogLine("Max Generations: "+MAX_GENERATIONS);
		stats.writeLogLine("Mutation Rate: "+MUTATION_RATE);
	}

	@Override
	protected RichBranch next() throws Exception {
		
		TreeMap<RichBranch, Double> next = new TreeMap<>();
		
		//Duplicate branches will be collapsed in the TreeMap 
		while (next.size() < POPULATION_SIZE) {
			RichBranch p1 = choose();
			RichBranch p2 = choose(p1);
			assert(!p1.equals(p2));
			
			RichBranch child = reproduce(p1, p2);
			
			if (child.equals(p1) || child.equals(p2)) {
				unaltered++;
				continue;
			}
			children++;
			
			if (rand.nextFloat() < MUTATION_RATE) {
				child = mutate(child);
			}

			next.put(child, 1.0/child.weight);
		}

		assert(next.size() == POPULATION_SIZE);
		
		RichBranch bestChild = next.firstKey();
		
		if (bestChild.weight < best.weight) {
			best = bestChild;
		}

		population = next;
		
		if (getIteration() % 100000 == 0) {
			stats.writeLogLine(getIteration()+" - "+best.weight+": "+best.getPath().toString());
		}
		
		return bestChild;
	}

	private RichBranch mutate(RichBranch child) {
		mutations++;
		
		Path path = child.getPath();
		int length = path.size();
		
		//Generate random number between 1 and path length (start will never move)
		int a = randomIndex(length, 0);
		//Generate random number between 1 and path length that is not equal to i
		int b = randomIndex(length, a);

		path.swapPath(a, b);

		return new RichBranch(path, graph);
	}
	
	private int randomIndex(int range, int except) {
		int r = except;
		
		while (r == except) {
			r = rand.nextInt(range - 1) + 1;
		}
		
		return r;
	}

	private RichBranch choose() {
		return choose(null);
	}

	private RichBranch choose(RichBranch other) {
		RichBranch candidate = other;
		
		while (candidate == other) {
			candidate = random();
		}
		
		return candidate;
	}

	private RichBranch random() {
		double totalInverse = 0;
		
		for (Map.Entry<RichBranch, Double> e : population.entrySet()) {
			totalInverse += e.getValue(); 
		}
		
		double choice = rand.nextFloat() * totalInverse;
		RichBranch chosen = null;
		
		double runningTotal = 0; 
		search:
		for (Map.Entry<RichBranch, Double> e : population.entrySet()) {
			runningTotal += e.getValue();
			
			if (runningTotal > choice) {
				chosen = e.getKey();
				break search;
			}
		}
		
		assert(chosen != null);
		return chosen;
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
		
		//Gaussian value will be along the normal distribution, so near-middle crossovers will be most chosen.
		double crossover = rand.nextGaussian();
		double midpoint = length / 2.0;
		//nextGaussian will be mostly within -1 to +1, but it can overshoot so we'll undershoot a bit.
		double scale = length / 4.0;
		int crossoverIndex = (int) Math.round((crossover * scale) + midpoint);
		crossoverIndex = bound(crossoverIndex, 3, length - 2);
		
		//Any way we split it, one half of the path will be better preserved than the other.
		//So let's make sure we don't give preference to any particular half.
		if (rand.nextBoolean()) {
			left = left.subPath(0, crossoverIndex);
			right.removeAll(left);
		}
		else {
			right = right.subPath(crossoverIndex, length);
			left.removeAll(right);
		}
		
		//Put it back together!
		Path child = new Path(left);
		child.addAll(right);
		
		assert(p1.getStart().equals(child.get(0)));
		assert(p2.getStart().equals(child.get(0)));

		assert(child.size() == length);
		return new RichBranch(child, graph);	
	}

	private int bound(int crossoverIndex, int min, int max) {
		return (int)Math.min(max, Math.max(min, crossoverIndex));
	}
	
	@Override
	public void updateStats() {
		super.updateStats();
		stats.output("Best Distance", best.weight);
		stats.output("Mutations", mutations);
		stats.output("Children", children);
		stats.output("Unaltered Children", unaltered);
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
