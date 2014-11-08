package cosc561.tsp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.PathBranch;
import cosc561.tsp.model.branch.RichBranch;

public class MapWindow {
	
	public static final Color BACKGROUND = Color.WHITE;
	
	public static final int OUTPUT_WIDTH = 300;
	
	private JFrame window;
	private Controls controls;
	
	private Container graphPanel;
	private MapGraphics graphics;
	
	private JPanel outputPanel;
	private JPanel controlPanel;
	private Container content;
	
	public MapWindow(Dimension dimensions) {
		
		window = new JFrame();
		window.setSize(dimensions);
		
		content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		graphPanel = new JPanel();
		graphPanel.setPreferredSize(dimensions);
		content.add(graphPanel, BorderLayout.CENTER);
		
		controlPanel = createControlPanel();		
		content.add(controlPanel, BorderLayout.SOUTH);
		
		
		outputPanel = createOutputPanel();
		content.add(outputPanel, BorderLayout.EAST);
				
		window.pack();
		window.setVisible(true);
		
		graphics = new MapGraphics(graphPanel, TravellingSalesman.SCALE);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	private JPanel createOutputPanel() {
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		outputPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH, 0));
		outputPanel.setLayout(new GridLayout(0, 2));
		
		return outputPanel;
	}
	
	public void setControls(final Controls controls) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					controlPanel.add(controls.getPanel());
					window.pack();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		return controlPanel;
	}
	
	public void addOutput(Output output) {
		outputPanel.add(new JLabel(output.getLabel() + ":"));
		outputPanel.add(output.getField());
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

	public void render(RichBranch branch) {
		graphics.initDraw();
		
		Node prev = null;
		for (Node next : branch.getPath()) {
			if (prev != null) {
				graphics.drawEdge(prev, next);
			}
			prev = next;
		}
		
		drawNodes(branch.getPath());
		if (branch instanceof PathBranch) {
			drawNodes(((PathBranch)branch).getUnvisited());
		}
		
		graphics.display();
	}

}


