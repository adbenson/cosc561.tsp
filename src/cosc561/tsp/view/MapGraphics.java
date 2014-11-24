package cosc561.tsp.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Random;

import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.model.Node;

public class MapGraphics {
	
	private final int TEXT_OFFSET = 3;

	private Graphics2D g;
	private double scale;
	private Image offScreenBuffer;
	private Container panel;
	
	private Random rand;
	
	public MapGraphics(Container panel, double scale) {
		this.panel = panel;
		this.scale = scale;
		
		rand = new Random();
	}
	
	public void initDraw() {
		if (offScreenBuffer == null){
			offScreenBuffer = panel.createImage(panel.getWidth(), panel.getHeight());
			if (offScreenBuffer == null) {
				System.err.println("offScreenBuffer is null");
				return;
			}
			else {
				g = (Graphics2D) offScreenBuffer.getGraphics();
			}
			
			g.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		g.setColor(Color.WHITE);
		g.fillRect (0, 0, panel.getWidth(), panel.getHeight());
	}
	
	public void display() {
	    Graphics graphics = null;
		try {
			graphics = panel.getGraphics();
			if ((graphics != null) && (offScreenBuffer != null)) {
				graphics.drawImage(offScreenBuffer, 0, 0, null);
			}
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e);  
		} finally {
			if (graphics != null) {
				graphics.dispose();
			}
		}
	}

	public void drawNode(Node node) {
		int x = ((int) (node.x * scale));
		int y = ((int) ((TravellingSalesman.MAX_COORDS-node.y) * scale));
			
		g.setColor(Color.BLACK);
		g.fillOval(x, y, 5, 5);
		
		g.drawString(Integer.toString(node.id), x, y - TEXT_OFFSET);
	}

	public void drawEdge(Node n1, Node n2) {
		if (n1.equals(n2)) {
			return;
		}
		
		g.setStroke(new BasicStroke(2));
		
		int x1 = ((int) (n1.x * scale));
		int y1 = ((int) ((TravellingSalesman.MAX_COORDS-n1.y) * scale));
		int x2 = ((int) (n2.x * scale));
		int y2 = ((int) ((TravellingSalesman.MAX_COORDS-n2.y) * scale));
		
		float color = ((n1.x + n2.x + n1.y + n2.y) % 100) / 100.0f;
		g.setColor(Color.getHSBColor(color, 1f, 0.75f));
		g.drawLine(x1, y1, x2, y2);
	}
	
}
