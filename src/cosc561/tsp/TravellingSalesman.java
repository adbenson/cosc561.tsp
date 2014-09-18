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
import cosc561.tsp.view.Output;

public class TravellingSalesman extends Controls {
	
	public static final Dimension DIMENSIONS = new Dimension(800, 800);
	
	private volatile boolean paused;
	private volatile boolean render;
	
	private Solver solver;
	private Strategy strategy;
	
	private Output attempts;
	
	private MapWindow window;
	
	private Branch currentBranch;
	
	public static void main(String[] args) throws Exception {	
		new TravellingSalesman();
	}
		
	public TravellingSalesman() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				window = new MapWindow(DIMENSIONS);
			};
		});
		
		window.setControls(this);
		
		Graph graph = new Graph();		
		List<Node> nodes = NodeParser.parse("TSPDataComma.txt");
		
		graph.addNodes(nodes, 10);
		
		solver = new Solver(window);
		
		strategy = new ClassHeuristic(graph, window);
		
		attempts = new Output("Attempted Paths");
		window.addOutput(attempts);
		
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
			attempts.setValue(solver.getAttempts());
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
