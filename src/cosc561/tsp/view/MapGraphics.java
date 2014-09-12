package cosc561.tsp.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import cosc561.tsp.model.Node;

public class MapGraphics {

	private Graphics2D g;
	private double scale;
	private Image offScreenBuffer;
	private Container panel;
	
	public MapGraphics(Container panel, double scale) {
		this.panel = panel;
		this.scale = scale;
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
		int x = (int) (node.x * scale);
		int y = (int) (node.y * scale);
		
		g.setColor(Color.BLACK);
		g.fillOval(x, y, 10, 10);
	}

	public void setScale(double scale) {
		System.out.println(scale);
		this.scale = scale;
	}
	
}
