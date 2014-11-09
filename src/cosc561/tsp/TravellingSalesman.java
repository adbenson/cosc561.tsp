package cosc561.tsp;

import java.awt.Dimension;
import java.util.List;

import javax.swing.SwingUtilities;

import cosc561.tsp.model.Node;
import cosc561.tsp.model.NodeParser;
import cosc561.tsp.strategy.*;
import cosc561.tsp.view.MapWindow;

public class TravellingSalesman {
	
	public static final boolean DEBUG = true;
	
	public static final int DIMENSION = 700;
	public static final int MAX_COORDS = 100;
	public static final int SCALE = DIMENSION/MAX_COORDS;
	
	public static final int FPS = 2;
	public static final int TOLERANCE = 10;
	
	public static final int DEFAULT_NODES = 11;
	public static final int MAX_NODES = 120;
	public static final Class<? extends Strategy> DEFAULT_STRATEGY = BranchAndBoundPermutation.class;
	
	private MapWindow window;
	
	public static final Class<?>[] strategies = {
		Greedy.class, 
		BreadthFirstSearch.class, 
		ClassHeuristic.class,
		BranchAndBoundPermutation.class
	};
	
	public static void main(String[] args) throws Exception {	
		new TravellingSalesman();
	}
		
	public TravellingSalesman() throws Exception {
		
		List<Node> nodes = NodeParser.parse("TSPDataComma.txt");
		
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				window = new MapWindow(new Dimension(DIMENSION, DIMENSION));
			};
		});
		
		new Solver(nodes, window);
	}
	
}
