package cosc561.tsp;

import java.awt.Dimension;
import java.util.List;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.NodeParser;
import cosc561.tsp.model.NodeParser.NodeParseException;
import cosc561.tsp.strategy.Solver;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.strategy.UniformCost;
import cosc561.tsp.view.MapWindow;

public class TravellingSalesman {
	
	public static final Dimension DIMENSIONS = new Dimension(800, 800);
	
	public static void main(String[] args) throws InterruptedException, NodeParseException {
		MapWindow window = new MapWindow(DIMENSIONS);
		
		Graph graph = new Graph();		
		List<Node> nodes = NodeParser.parse("TSPDataComma.txt");
		
		graph.addNodes(nodes, 8);
		
		Solver solver = new Solver(window);
		
		Strategy uniformCost = new UniformCost(graph);
		
		solver.start(uniformCost);
		
		while(true)
		solver.next();
				
//		window.drawComplete(graph);
		
	}

}
