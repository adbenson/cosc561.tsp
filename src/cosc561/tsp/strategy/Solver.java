package cosc561.tsp.strategy;

import cosc561.tsp.model.Branch;
import cosc561.tsp.view.MapWindow;

public class Solver implements Runnable {
	
	private MapWindow window;
	
	private Strategy strategy;
	
	private boolean paused;
	
	private int attempts;
	
	public Solver(MapWindow window) {
		this.window = window;
		paused = true;
	}
	
	public void start(Strategy strategy) {
		this.strategy = strategy;
		attempts = 0;
		strategy.init();
	}
	
	public void next() {
		Branch branch = strategy.next();
		attempts++;
		window.render(branch);
	}
	
	public void run() {
		while(!paused) {
			
		}
	}
	
	public int getAttempts() {
		return attempts;
	}
}
