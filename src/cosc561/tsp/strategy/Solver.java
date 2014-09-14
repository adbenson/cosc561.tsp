package cosc561.tsp.strategy;

import cosc561.tsp.model.Branch;
import cosc561.tsp.view.MapWindow;

public class Solver implements Runnable {
	
	private MapWindow window;
	
	private Strategy strategy;
	
	private boolean paused;
	
	public Solver(MapWindow window) {
		this.window = window;
		paused = true;
	}
	
	public void start(Strategy strategy) {
		this.strategy = strategy;
		strategy.init();
	}
	
	public void next() {
//		if (!strategy.isComplete()) {
			Branch branch = strategy.next();
			window.render(branch);
//		}
	}
	
	public void run() {
		while(!paused) {
			
		}
	}
	
	public void setIncrement(int increment) {
		
	}
	
	public void setPause(boolean pause) {
		
	}
}
