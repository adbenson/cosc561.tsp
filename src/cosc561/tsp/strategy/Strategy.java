package cosc561.tsp.strategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.view.MapWindow;

public abstract class Strategy {
	
	protected Graph graph;
	protected MapWindow window;
	public final Stats stats;
	
	private volatile long attempts;
	protected volatile float currentDistance;
	
	protected Strategy(Graph graph, MapWindow window) {
		this.graph = graph;
		this.window = window;
				
		stats = new Stats(window);
		
		reset();
	}
	
	public RichBranch nextBranch() throws Exception {
		RichBranch branch = next();
		
		attempts++;
		currentDistance = branch.weight;
		
		return branch;
	};
	
	protected abstract RichBranch next() throws Exception;

	public abstract void init();

	public abstract boolean isComplete();
		
	public abstract RichBranch getSolution();
		
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
		stats.output("Attempted Paths", attempts);
		stats.output("Current Distance", currentDistance);
	}
	
	public void reset() {
		attempts = 0;
		currentDistance = 0;
		
		stats.reset();
	}
	
	protected void debug(String msg) {
		if (TravellingSalesman.DEBUG) {
			System.out.println(msg);
		}
	}
	
	public class Stats {
		
		private Map<String, String> outputs;
		private MapWindow window;
		
		public Stats(MapWindow window) {
			outputs = new HashMap<String, String>();
			this.window = window;
		}
		
		public void output(String label, long value) {
			outputs.put(label, Long.toString(value));
		}
		
		public void output(String label, double value) {
			outputs.put(label, MapWindow.DECIMAL_FORMAT.format(value));
		}

		public void output(String label, String value) {
			outputs.put(label, value);
		}
		
		public void reset() {
			window.clearOutput();
			window.refresh();
		}
		
		public void show() {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						updateStats();
						window.clearOutput();
						
						for (Entry<String, String> entry : outputs.entrySet()) {
							window.addOutput(entry.getKey(), entry.getValue());
						}
						
						window.refresh();
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				System.err.println("SwingUtilities.invokeAndWait interrupted");
			}

		}
	}
}
