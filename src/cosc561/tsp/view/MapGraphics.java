package cosc561.tsp.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Random;

import cosc561.tsp.model.Node;

public class MapGraphics {

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
		//FIXME: Hardcoded max to invert y axis
		int y = ((int) ((100-node.y) * scale));
		
		g.setColor(Color.BLACK);
		g.fillOval(x, y, 5, 5);
	}

	public void drawEdge(Node n1, Node n2) {
		if (n1.equals(n2)) {
			return;
		}
		
		int x1 = ((int) (n1.x * scale));
		//FIXME: Hardcoded max to invert y axis
		int y1 = ((int) ((100-n1.y) * scale));
		int x2 = ((int) (n2.x * scale));
		//FIXME: Hardcoded max to invert y axis
		int y2 = ((int) ((100-n2.y) * scale));
		
		g.setColor(Color.getHSBColor(rand.nextFloat(), 1f, 1f));
		g.drawLine(x1, y1, x2, y2);
	}
	
}
