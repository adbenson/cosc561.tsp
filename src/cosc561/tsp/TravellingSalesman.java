package cosc561.tsp;

import java.awt.Dimension;
import java.util.List;

import javax.swing.SwingUtilities;

import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.NodeParser;
import cosc561.tsp.strategy.ClassHeuristic;
import cosc561.tsp.strategy.Solver;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.view.MapWindow;

public class TravellingSalesman extends Controls {
	
	public static final int DIMENSION = 700;
	public static final int MAX_COORDS = 100;
	public static final int SCALE = DIMENSION/MAX_COORDS;
	
	private volatile boolean paused;
	private volatile boolean render;
	
	private Solver solver;
	private Strategy strategy;
	
	private MapWindow window;
	
	private Branch currentBranch;
	
	public static void main(String[] args) throws Exception {	
		new TravellingSalesman();
	}
		
	public TravellingSalesman() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				window = new MapWindow(new Dimension(DIMENSION, DIMENSION));
			};
		});
		
		window.setControls(this);
		
		Graph graph = new Graph();		
		List<Node> nodes = NodeParser.parse("TSPDataComma.txt");
		
		graph.addNodes(nodes, 10);
		
		solver = new Solver(window);
		
		strategy = new ClassHeuristic(graph, window);
		
		solver.start(strategy);
		
		paused = true;
		render = true;
		
		while(!strategy.isComplete()) {
			if (!paused) {
				next();
			}
			Thread.yield();
		}
				
	}
	
	@Override
	public void next() {
		if (!strategy.isComplete()) {
			currentBranch = solver.next();
			if (render) {
				window.render(currentBranch);
			}
		}
	}

	@Override
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	@Override
	public void setRender(boolean render) {
		this.render = render;
		if (render && currentBranch != null) {
			window.render(currentBranch);
		}
	}
	
}
