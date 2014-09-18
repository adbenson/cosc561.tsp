package cosc561.tsp.strategy;

import cosc561.tsp.model.Branch;
import cosc561.tsp.view.MapWindow;
import cosc561.tsp.view.Output;

public class Solver implements Runnable {
	
	private MapWindow window;
	
	private Strategy strategy;
	
	private boolean paused;
	
	private Output attempts;
	private Output currentDistance;
	private Output bestDistance;
	
	public Solver(MapWindow window) {
		this.window = window;
		paused = true;
		attempts = new Output("Attempted Paths");
		window.addOutput(attempts);
		bestDistance = new Output("Best Distance");
		window.addOutput(bestDistance);
		currentDistance = new Output("Current Distance");
		window.addOutput(currentDistance);
	}
	
	public void start(Strategy strategy) {
		this.strategy = strategy;
		attempts.setValue(0);
		bestDistance.setValue(Integer.MAX_VALUE);
		currentDistance.setValue(0);
		strategy.init();
	}
	
	public Branch next() {
		attempts.increment();
		Branch next = strategy.next();
		bestDistance.setValue(Math.min(bestDistance.getIntValue(), next.getWeight()));
		currentDistance.setValue(next.getWeight());
		return next;
	}
	
	public void run() {
		while(!paused) {
			
		}
	}
}
