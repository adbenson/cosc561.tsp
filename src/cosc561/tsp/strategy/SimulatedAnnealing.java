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
	
	/**
	 * STATIC
	 */
	
	/* Arbitrary constants */
	private static final long EXPECTED_RUN_TIME_SECONDS = (long) (12 * 60 * 60);
	
	private static final int ITERATIONS_PER_SECOND = 33000;	
	
	private static final int STEPS = 1000;
	
	private static final double START_TEMP = 100;
	
	private static final double ACCEPTABLE_REGRESSION_DIFFERENCE = 1.0;

	/* Calculated constants */
	private static final int COOLING_INTERVAL = getCoolingInterval(EXPECTED_RUN_TIME_SECONDS, ITERATIONS_PER_SECOND, STEPS);
	private static final double MIN_TEMP = getMinTemp(ACCEPTABLE_REGRESSION_DIFFERENCE, COOLING_INTERVAL);
	private static final double COOLING_RATE = getCoolingRate(STEPS, START_TEMP, MIN_TEMP);
	
	/**
	 * Returns the number of iterations between steps required to achieve the desired run time.
	 * 
	 * @param runTime
	 * @param iterationRate
	 * @param steps
	 * @return
	 */
	private static int getCoolingInterval(long runTime, int iterationRate, int steps) {
		long expectedIterations = runTime * iterationRate;
		return (int) (expectedIterations / steps);
	}
	
	/**
	 * This will return a temperature where the probability of a solution that is 
	 * {@code acceptableRegressionDifference} worse than the current best solution 
	 * is likely to be accepted once within {@code iterationsPerStep} iterations.
	 *  
	 * @param acceptableRegressionDifference
	 * @param iterationsPerStep
	 * @return
	 */
	private static double getMinTemp(double acceptableRegressionDifference, int iterationsPerStep) {
		 return acceptableRegressionDifference / Math.log(iterationsPerStep);
	}
	
	/**
	 * Returns the cooling rate which, when applied {@code steps - 1} times, will result in {@code minTemp}
	 * 
	 * @param steps
	 * @param startTemp
	 * @param minTemp
	 * @return
	 */
	private static double getCoolingRate(int steps, double startTemp, double minTemp) {		
		
		double tempRatio = minTemp / startTemp;
		
		long lastStep = steps - 1;
		
		return Math.pow(tempRatio, (1.0 / lastStep));
	}
	
	/**
	 * INSTANCE
	 */
	
	/* Member variables */
	private Random rand;
	
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
		temperature = START_TEMP;
		
		improvements = 0;
		acceptedRegressions = 0;
		
		stats.writeLogLine("Cooling Interval: "+COOLING_INTERVAL);
		stats.writeLogLine("Cooling Rate: "+COOLING_RATE);
		stats.writeLogLine("Min Temp.: "+MIN_TEMP);
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
		if (getIteration() % COOLING_INTERVAL == 0) {
			temperature *= COOLING_RATE;
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
