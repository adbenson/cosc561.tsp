package cosc561.tsp;

import java.awt.Dimension;
import java.util.Random;

import cosc561.tsp.model.Graph;
import cosc561.tsp.view.MapWindow;

public class TravellingSalesman {
	
	public static final Dimension DIMENSIONS = new Dimension(1000, 1000);
	
	public static void main(String[] args) throws InterruptedException {
		MapWindow window = new MapWindow(DIMENSIONS);
		
		Graph graph = new Graph();
		window.setGraph(graph);
		
		Random r = new Random();
		for(int i=1; i< 1000; i++) {
			graph.addNode(r.nextInt(i), r.nextInt(i));
			window.refresh();
			Thread.sleep(250);
			
		}
		
	}

}
