package cosc561.tsp;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import cosc561.tsp.model.NodeParser;
import cosc561.tsp.model.Path;
import cosc561.tsp.strategy.BranchAndBoundClassHeuristic;
import cosc561.tsp.strategy.BranchAndBoundPath;
import cosc561.tsp.strategy.BreadthFirstClassHeuristic;
import cosc561.tsp.strategy.BreadthFirstSearch;
import cosc561.tsp.strategy.SimulatedAnnealing;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.strategy.p_approx.Greedy;
import cosc561.tsp.strategy.p_approx.TwoOpt;
import cosc561.tsp.strategy.p_approx.WorstFirstInsertion;
import cosc561.tsp.view.MapWindow;

public class TravellingSalesman {
	
	public static final boolean DEBUG = true;
	
	public static final int DIMENSION = 700;
	public static final int MAX_COORDS = 100;
	public static final int SCALE = DIMENSION/MAX_COORDS;
	
	public static final int FPS = 2;
	public static final int TOLERANCE = 10;
	
	public static final int DEFAULT_NODES = 120;
	public static final int MAX_NODES = 120;
	
	private MapWindow window;
	
	public static void main(String[] args) throws Exception {	
		new TravellingSalesman();
	}
		
	public TravellingSalesman() throws Exception {
		
		Path nodes = NodeParser.parse("TSPDataComma.txt");
		
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				window = new MapWindow(new Dimension(DIMENSION, DIMENSION));
			};
		});
		
		new Solver(nodes, window);
	}
	
}
