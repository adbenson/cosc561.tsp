package cosc561.tsp;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Path;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.strategy.Strategy.StrategyInstantiationException;
import cosc561.tsp.util.EventScheduler;
import cosc561.tsp.view.Controls;
import cosc561.tsp.view.MapWindow;

public class Solver extends Controls {
	
	private MapWindow window;
	private EventScheduler scheduler;
	
	private Strategy strategy;
	
	private Path allNodes;
	private int nodeCap = TravellingSalesman.DEFAULT_NODES;
	
	private volatile RichBranch currentBranch;
	
	private volatile boolean render;
	
	public Solver(Path nodes, MapWindow window) {
		this.allNodes = nodes;
		this.window = window;
		
		render = true;		
		
		int intervalMs = EventScheduler.cyclesPerSecond(TravellingSalesman.FPS);
		scheduler = new EventScheduler(intervalMs, TravellingSalesman.TOLERANCE);
		
		scheduler.addEvent(scheduler.new ContinuousEvent(true, 1, new Runnable() {
			public void run() {
				next(false);
			}
		}));
		
		scheduler.addEvent(scheduler.new ConcurrentEvent(true, 20, Thread.MIN_PRIORITY, new Runnable() {
			public void run() {
				render();
			}
		}));
		
		scheduler.addEvent(scheduler.new ConcurrentEvent(true, 20, Thread.MAX_PRIORITY, new Runnable() {
			public void run() {
				updateStats();
			}
		}));
		
		window.setControls(this);
		
		reset(TravellingSalesman.DEFAULT_STRATEGY, TravellingSalesman.DEFAULT_NODES);
	}

	public void start() {
		try {
			scheduler.start();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void next(boolean manual) {
		//scheduler doesn't stop on a dime; it may call this a few more times after it's supposed to stop.
		if (!scheduler.isRunning() && !manual) {
			return;
		}
		
		if (strategy.isComplete()) {
			stop();
			return;
		}
		
		try {
			currentBranch = strategy.nextBranch();
		} catch (Exception e) {
			throw new RuntimeException("Exception encountered getting next branch", e);
		}
		currentBranch = strategy.getSolution();
		if (manual) {
			render();
			updateStats();
		}
	}

	public void stop() {
		scheduler.end();
		currentBranch = strategy.getSolution();
		window.render(currentBranch);
		updateStats();
		
		System.out.println("Done!");
		System.out.println("Distance: "+currentBranch.weight);
		System.out.println("Path: "+currentBranch.getTour());
		
		setPauseButton(true);
	}
	
	protected void updateStats() {
		if (strategy != null) {
			String runTime = MapWindow.formatTimeInterval(scheduler.getContinuousRunTime());
			strategy.stats.output("Running Time", runTime);
			strategy.stats.show();
		}
	}
	
	public void changeStrategy(Class<? extends Strategy> strategyClass) {
		Graph graph = new Graph(allNodes, nodeCap);
		
		try {
			strategy = Strategy.instantiate(strategyClass, graph, window);
		} catch (StrategyInstantiationException e) {
			System.err.println(e);
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void render() {
		if (render && currentBranch != null) {
			window.render(currentBranch);
		}
	}
	
	@Override
	public void reset(Class selectedStrategy, int nodes) {
		if (scheduler.isRunning()) {
			scheduler.end();
		}
		setPauseButton(true);
		
		nodeCap = nodes;
		changeStrategy(selectedStrategy);
		
		strategy.reset();
		strategy.init();
		
		window.reset();
		window.render(strategy.getSolution());
	}

	@Override
	public void setPaused(boolean paused) {
		if (!paused && !scheduler.isRunning()) {
			start();
		}
		scheduler.setPaused(paused);
	}

	@Override
	public void setRender(boolean render) {
		this.render = render;
	}
	

}
