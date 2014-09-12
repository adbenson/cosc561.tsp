package cosc561.tsp.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public class MapWindow {
	
	public static final Color BACKGROUND = Color.WHITE;
	
	private Dimension dimensions;
	private Graph graph;
	
	private Container pane;
	private MapGraphics graphics;
	
	public MapWindow(int width, int height) {
		this(new Dimension(width, height));
	}

	public MapWindow(Dimension dimensions) {
		this.dimensions = new Dimension(dimensions);
		
		JFrame window = new JFrame();
		window.setSize(dimensions);
		
		
		pane = new JPanel();
		pane.setPreferredSize(dimensions);
		window.add(pane);
		
		window.pack();
		window.setVisible(true);
		
		graphics = new MapGraphics(pane, getScale(pane.getSize(), dimensions));
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	private static double getScale(Dimension bounds, Dimension dimensions) {
		return Math.min(dimensions.getWidth() / bounds.getWidth(), dimensions.getHeight() / bounds.getHeight());
	}
	
	public void refresh() {
		graphics.initDraw();
		
		graphics.setScale(getScale(graph.getBounds(), dimensions));
		
		for(Node node : graph.getNodes()) {
			graphics.drawNode(node);
		}
		
		graphics.display();
	}

}
