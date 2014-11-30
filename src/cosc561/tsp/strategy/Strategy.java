package cosc561.tsp.strategy;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.classes.StrategyClass;
import cosc561.tsp.view.MapWindow;

public abstract class Strategy {
	
	protected Graph graph;
	public final Stats stats;
	
	private volatile long iteration;
	protected volatile float currentDistance;
	
	protected Strategy(Graph graph, MapWindow window) {
		this.graph = graph;
				
		stats = new Stats(window);
		
		reset();
	}
	
	public final RichBranch nextBranch() throws Exception {
		RichBranch branch = next();
		
		iteration++;
		currentDistance = branch.weight;
		
		return branch;
	};

	public abstract void init() throws Exception;
	
	protected abstract RichBranch next() throws Exception;

	public abstract boolean isComplete();
		
	public abstract RichBranch getSolution();
		
	public static Strategy instantiate(StrategyClass strategyClass, Graph graph, MapWindow window) throws StrategyInstantiationException {
		try {
			Constructor<? extends Strategy> constructor = strategyClass.getStrategyClass().getConstructor(Graph.class, MapWindow.class);
			return constructor.newInstance(graph, window);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new StrategyInstantiationException("Error instantiating ", e);
		}

	}
	
	public RichBranch generate(StrategyClass strategy) throws Exception {
		Strategy generator = instantiate(strategy, graph, null);
		return generator.generate();
	}
	
	public RichBranch generate() throws Exception {
		init();
		
		while(!isComplete()) {
			next();
		}
		
		return getSolution();
	}
	
	public static class StrategyInstantiationException extends Exception {
		private static final long serialVersionUID = 1L;
		public StrategyInstantiationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	public void updateStats() {
		stats.output("Attempted Paths", iteration);
		stats.output("Current Distance", currentDistance);
	}
	
	public long getIteration() {
		return iteration;
	}
	
	public void reset() {
		iteration = 0;
		currentDistance = 0;
		
		stats.reset();
	}
	
	protected void debug(String msg) {
		if (TravellingSalesman.DEBUG) {
			System.out.println(msg);
		}
	}
	
	public class Stats {
		
		private static final String LOGFILE_FOLDER = "log";
		private static final String LOGFILE_PREFIX = "tsp-";
		private static final String LOGFILE_EXTENSION = ".log";
		
		private Map<String, String> outputs;
		private boolean labelsCurrent;
		
		private MapWindow window;
		private PrintWriter logWriter;
		
		private String filename;
		
		private String description;
		
		public Stats(MapWindow window) {
			outputs = new LinkedHashMap<String, String>();
			this.window = window;
			labelsCurrent = false;
			
			if (window != null) {
				description = cleanseDescription(window.requestDescription());
				output("Description", description);
			}
			
			filename = getFilename() + LOGFILE_EXTENSION;
		}
		
		private String cleanseDescription(String desc) {
			desc = desc.replaceAll("\\s", "_");
			desc = desc.replaceAll("[^A-Za-z0-9_\\-\\.]", "-");
			return desc;
		}

		private String getFilename() {
			return LOGFILE_FOLDER + File.separator + 
					LOGFILE_PREFIX + Strategy.this.getClass().getSimpleName() + 
					"-" + description + 
					"-" + System.currentTimeMillis();
		}
		
		private PrintWriter getLogWriter() throws IOException {
			if (logWriter == null) {
				logWriter = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)), true);
			}
			
			return logWriter;
		}
		
		public void writeLogLine(String log) {
			PrintWriter writer;
			try {
				writer = getLogWriter();
			} catch (IOException e) {
				throw new RuntimeException("Exception opening log file for write : "+filename, e);
			}
			
			writer.println(log);
		}
		
		private void writeLog() {
			if (!labelsCurrent) {
				writeLogLine(buildLog(outputs.keySet()));
				labelsCurrent = true;
			}

			writeLogLine(buildLog(outputs.values()));
		}
		
		private String buildLog(Collection<String> values) {
			StringBuilder log = new StringBuilder();

			for (String value : values) {
				log.append(value + "|");
			}
			
			return log.toString();
		}

		//Handles all numeric types
		public void output(String label, double value) {
			output(label, MapWindow.DECIMAL_FORMAT.format(value));
		}

		public void output(String label, String value) {
			if (!outputs.containsKey(label)) {
				labelsCurrent = false;
			}
			outputs.put(label, value);
		}
		
		public void reset() {
			if (window != null) {
				window.clearOutput();
				window.refresh();
			}
		}
		
		public void show() {
			updateStats();
			
			if (window == null) {
				consoleOutput();
			}
			else {
				windowOutput();
			}

			writeLog();
		}

		private void windowOutput() {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
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

		private void consoleOutput() {
			for (Entry<String, String> entry : outputs.entrySet()) {
				System.out.println(entry.getKey() +": \t\t"+ entry.getValue());
			}
		}
		
		public void saveScreenshot() {

			BufferedImage img = window.getScreenShot();

			try {
				ImageIO.write(img, "png", new File(getFilename() + ".png"));
			} catch (IOException e) {
				System.err.println("Exception writing screenshot to disk.");
				System.err.println(e);
				e.printStackTrace();
			}
		}
		
	}
}
