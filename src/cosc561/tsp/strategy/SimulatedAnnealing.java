package cosc561.tsp.strategy;

import java.util.Random;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.Path;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.classes.TourGenerationStrategies;
import cosc561.tsp.strategy.p_approx.RandomPath;
import cosc561.tsp.strategy.p_approx.TourGenerator;
import cosc561.tsp.view.MapWindow;

public class SimulatedAnnealing extends Strategy {
	
	private static final long MAX_ITERATIONS = 1000000000;
	private static final double MIN_TEMP = 0.001;

	private Random rand;
	
	private double coolingRate = 0.995;
	private int coolingSchedule = 2000000; 
	
	private double temperature;
		
	private RichBranch current;
	private RichBranch best;
	
	private long improvements;
	private long acceptedRegressions;

	public SimulatedAnnealing(Graph graph, MapWindow window) {
		super(graph, window);
		rand = new Random();
	}

	@Override
	public void init() throws Exception {
		best = current = generate(TourGenerationStrategies.RANDOM);
		temperature = 1.0;
		
		improvements = 0;
		acceptedRegressions = 0;
	}

	@Override
	protected RichBranch next() throws Exception {
		cool();
		
		RichBranch next = new RichBranch(perturb(current.getPath()), graph);
		
		if (next.weight < current.weight) {
			current = next;
			improvements++;
		}
		else if (merit(next.weight, current.weight, temperature) >= rand.nextDouble()) {
			current = next; 
			acceptedRegressions++;
		}

		if (current.weight < best.weight) {
			best = current;
		}

		return current;
	}

	private static double merit(float current, float best, double temperature) {
		double exp = -((current - best) / temperature); 
		return Math.pow(Math.E, exp);
	}

	private void cool() {
		if (getIteration() % coolingSchedule == 0) {
			temperature *= coolingRate;
		}
	}
	
	private Path perturb(Path path) {	
		int length = path.size();
		
		//Generate random number between 1 and path length (start will never move)
		int a = randomIndex(length, 0);
		//Generate random number between 1 and path length that is not equal to i
		int b = randomIndex(length, a);

		path.swapPath(a, b);

		return path;
	}

	private int randomIndex(int range, int except) {
		int r = except;
		
		while (r == except) {
			r = rand.nextInt(range - 1) + 1;
		}
		
		return r;
	}
	
	@Override
	public void updateStats() {
		super.updateStats();
		stats.output("Best Distance", best.weight);
		stats.output("Temperature", temperature);
		stats.output("Improvments", improvements);
		stats.output("Accepted Regressions", acceptedRegressions);
	}

	@Override
	public boolean isComplete() {
		return temperature < MIN_TEMP;
	}

	@Override
	public RichBranch getSolution() {
		return best;
	}

}
