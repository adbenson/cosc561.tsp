package cosc561.tsp.strategy;

import cosc561.tsp.model.Branch;
import cosc561.tsp.view.MapWindow;
import cosc561.tsp.view.Output;

public class Solver implements Runnable {
	
	private MapWindow window;
	
	private Strategy strategy;
	
	private boolean paused;
	
	private Stats stats;
	
	protected volatile long attempts;
	protected volatile int bestDistance;
	protected volatile int currentDistance;
	
	public Solver(MapWindow window) {
		this.window = window;
		paused = true;
		
		stats = new Stats(window);
	}
	
	public void start(Strategy strategy) {
		this.strategy = strategy;
		
		strategy.init();
	}
	
	public Branch next() {		
		Branch next = strategy.next();
		
		attempts++;
		bestDistance = Math.min(bestDistance, next.getWeight());
		currentDistance = next.getWeight();
		
		stats.run();
		
		return next;
	}
	
	public void run() {
		while(!paused) {
			
		}
	}
	
	private class Stats implements Runnable {
		private Output attemptsOutput;
		private Output currentDistanceOutput;
		private Output bestDistanceOutput;
		
		public Stats(MapWindow window) {
			
			attemptsOutput = new Output("Attempted Paths");
			window.addOutput(attemptsOutput);
			
			bestDistanceOutput = new Output("Best Distance");
			window.addOutput(bestDistanceOutput);
			
			currentDistanceOutput = new Output("Current Distance");
			window.addOutput(currentDistanceOutput);
		}
		
		public void reset() {
			attemptsOutput.setValue(0);
			bestDistanceOutput.setValue(Integer.MAX_VALUE);
			currentDistanceOutput.setValue(0);
		}
		
		public void run() {
			attemptsOutput.setValue(attempts);
			bestDistanceOutput.setValue(bestDistance);
			currentDistanceOutput.setValue(currentDistance);
		}
	}
}
