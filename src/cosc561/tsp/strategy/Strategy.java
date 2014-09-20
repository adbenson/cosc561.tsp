package cosc561.tsp.strategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.view.MapWindow;
import cosc561.tsp.view.Output;

public abstract class Strategy {
	
	protected Graph graph;
	protected MapWindow window;
	private Stats stats;
	
	private volatile long attempts;
	protected volatile int currentDistance;
	
	protected Strategy(Graph graph, MapWindow window) {
		this.graph = graph;
		this.window = window;
				
		stats = new Stats(window);
		
		reset();
	}
	
	public Branch nextBranch() {
		Branch branch = next();
		
		attempts++;
		currentDistance = branch.getWeight();
		
		return branch;
	};
	
	protected abstract Branch next();

	public abstract void init();

	public abstract boolean isComplete();
		
	public static Strategy instantiate(Class<? extends Strategy> strategyClass, Graph graph, MapWindow window) throws StrategyInstantiationException {
		try {
			Constructor<? extends Strategy> constructor = strategyClass.getConstructor(Graph.class, MapWindow.class);
			return constructor.newInstance(graph, window);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new StrategyInstantiationException("Error instantiating ", e);
		}

	}
	
	public static class StrategyInstantiationException extends Exception {
		private static final long serialVersionUID = 1L;
		public StrategyInstantiationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	public void updateStats() {
		stats.update();
	}
	
	public void reset() {
		attempts = 0;
		currentDistance = 0;
		
		stats.reset();
	}
	
	private class Stats {
		private Output attemptsOutput;
		private Output currentDistanceOutput;
		private Output pathsPerSecond;
		
		public Stats(MapWindow window) {
			
			attemptsOutput = new Output("Attempted Paths");
			window.addOutput(attemptsOutput);
			
			pathsPerSecond = new Output("Paths per Second");
			window.addOutput(pathsPerSecond);
			
			currentDistanceOutput = new Output("Current Distance");
			window.addOutput(currentDistanceOutput);
		}
		
		public void reset() {
			attemptsOutput.setValue(0);
			currentDistanceOutput.setValue(0);
		}
		
		public void update() {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						attemptsOutput.setValue(attempts);
						currentDistanceOutput.setValue(currentDistance);
//						pathsPerSecond.setValue(attempts);
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				System.err.println("SwingUtilities.invokeAndWait interrupted");
			}

		}
	}
}
