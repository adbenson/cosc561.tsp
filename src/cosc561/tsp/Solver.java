package cosc561.tsp;

import java.util.List;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
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
	
	private List<Node> allNodes;
	private int nodeCap = TravellingSalesman.DEFAULT_NODES;
	
	private volatile RichBranch currentBranch;
	
	private volatile boolean render;
	
	public Solver(List<Node> nodes, MapWindow window) {
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
				if (strategy != null) {
					strategy.updateStats();
				}
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
		
		if (manual) {
			render();
			strategy.updateStats();
		}
	}
	
	public void stop() {
		scheduler.end();
		currentBranch = strategy.getSolution();
		window.render(currentBranch);
		strategy.updateStats();
		setPauseButton(true);
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
