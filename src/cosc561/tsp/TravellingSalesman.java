package cosc561.tsp;

import java.awt.Dimension;
import java.util.List;
import java.util.Random;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.NodeParser;
import cosc561.tsp.model.NodeParser.NodeParseException;
import cosc561.tsp.view.MapWindow;

public class TravellingSalesman {
	
	public static final Dimension DIMENSIONS = new Dimension(1000, 1000);
	
	public static void main(String[] args) throws InterruptedException, NodeParseException {
		MapWindow window = new MapWindow(DIMENSIONS);
		
		Graph graph = new Graph();
		window.setGraph(graph);
		
		List<Node> nodes = NodeParser.parse("TSPDataComma.txt");
		
		graph.addNodes(nodes);
				
		window.drawComplete();
		
	}

}
