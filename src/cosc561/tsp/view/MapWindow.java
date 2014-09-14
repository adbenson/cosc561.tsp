package cosc561.tsp.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public class MapWindow {
	
	public static final Color BACKGROUND = Color.WHITE;
	
	private Dimension dimensions;
	
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
		
		graphics = new MapGraphics(pane, 5);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	public void drawComplete(Graph graph) {
		graphics.initDraw();
				
		for(Node n1 : graph.getNodeList()) {
			for(Node n2 : graph.getNodeList()) {
				graphics.drawEdge(n1, n2);
				drawNodes(graph.getNodes());
				
				graphics.display();
			}
		}
	}
	
	private void drawNodes(Iterable<Node> nodes) {
		for(Node node : nodes) {
			graphics.drawNode(node);
		}
	}

	public void render(Branch branch) {
		graphics.initDraw();
		
		Node prev = null;
		for (Node next : branch.getPath()) {
			if (prev != null) {
				graphics.drawEdge(prev, next);
			}
			prev = next;
		}
		
		drawNodes(branch.getPath());
		drawNodes(branch.getUnvisited());
		
		graphics.display();
	}

}
