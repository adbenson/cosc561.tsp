package cosc561.tsp;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.NodeParser;
import cosc561.tsp.model.NodeParser.NodeParseException;
import cosc561.tsp.strategy.Solver;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.strategy.UniformCost;
import cosc561.tsp.view.MapWindow;
import cosc561.tsp.view.Output;

public class TravellingSalesman {
	
	public static final Dimension DIMENSIONS = new Dimension(800, 800);
	
	private boolean paused;
	private Solver solver;
	
	private Output attempts;
	
	public static void main(String[] args) throws NodeParseException {	
		new TravellingSalesman();
	}
		
	public TravellingSalesman() throws NodeParseException {
		MapWindow window = new MapWindow(DIMENSIONS);
		
		Graph graph = new Graph();		
		List<Node> nodes = NodeParser.parse("TSPDataComma.txt");
		
		graph.addNodes(nodes, 7);
		
		solver = new Solver(window);
		
		Strategy strategy = new UniformCost(graph);
		
		attempts = new Output("Attempted Paths");
		window.addOutput(attempts);
		
		solver.start(strategy);
		
		paused = true;
		
		window.setPauseHandler(new ItemListener() {
			public void itemStateChanged(final ItemEvent event) {
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						paused = (event.getStateChange() == ItemEvent.SELECTED);
					}
				});
			}
		});	
		
		window.setNextHandler(new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						next();	
					}
				});
			}			
		});
		
		while(!strategy.isComplete()) {
			if (!paused) {
				next();
			}
			Thread.yield();
		}
				
	}
	
	private void next() {
		solver.next();
		attempts.setValue(solver.getAttempts());
	}
	
}
