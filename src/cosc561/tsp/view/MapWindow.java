package cosc561.tsp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import cosc561.tsp.Controls;
import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;

public class MapWindow {
	
	public static final Color BACKGROUND = Color.WHITE;
	
	public static final int OUTPUT_WIDTH = 300;
	
	private Dimension dimensions;
	
	private Container graphPanel;
	private MapGraphics graphics;
	
	private JPanel outputPanel;
	
	private JCheckBox render;
	private JToggleButton pause;
	private JButton next;
	
	public MapWindow(int width, int height) {
		this(new Dimension(width, height));
	}

	public MapWindow(Dimension dimensions) {
		this.dimensions = new Dimension(dimensions);
		
		JFrame window = new JFrame();
		window.setSize(dimensions);
		
		Container content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		graphPanel = new JPanel();
		graphPanel.setPreferredSize(dimensions);
		content.add(graphPanel, BorderLayout.CENTER);
		
		JPanel controlPanel = createControlPanel();		
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
//		outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.PAGE_AXIS));
		
		return outputPanel;
	}

	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		render = new JCheckBox("Render");
		controlPanel.add(render);
		render.setSelected(true);

		
		pause = new JToggleButton("Pause");
		controlPanel.add(pause);
		pause.setSelected(true);

		
		next = new JButton("Next");
		controlPanel.add(next);

		
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

	public void setControls(Controls controls) {
		render.addItemListener(controls.renderToggle());
		pause.addItemListener(controls.pauseToggle());
		next.setAction(controls.nextButton());
	}

}


