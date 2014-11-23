package cosc561.tsp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.Path;
import cosc561.tsp.model.branch.RichBranch;

public class MapWindow {
	
	public static final Color BACKGROUND = Color.WHITE;
	
	public static final int OUTPUT_WIDTH = 300;
	public static final int NODE_LIST_HEIGHT = 40;
	
	private static final String FORMAT_PATTERN = "###,###,###,###.###";
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(FORMAT_PATTERN);
	
	private JFrame window;
	
	private Container graphPanel;
	private MapGraphics graphics;
	
	private JPanel outputPanel;
	private JPanel controlPanel;
	private JTextArea nodeList;
	private Container content;
	
	public MapWindow(Dimension dimensions) {
		
		window = new JFrame();
		window.setSize(dimensions);
		
		content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		JPanel graphContainer = new JPanel(new BorderLayout());
		
		graphPanel = new JPanel();
		graphPanel.setPreferredSize(dimensions);
		graphContainer.add(graphPanel, BorderLayout.CENTER);
		
		nodeList = new JTextArea();
		nodeList.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(nodeList);
		scrollPane.setPreferredSize(new Dimension(dimensions.width, NODE_LIST_HEIGHT));
		scrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		graphContainer.add(scrollPane, BorderLayout.SOUTH);
		
		content.add(graphContainer, BorderLayout.CENTER);
		
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
	
	public void clearOutput() {
		outputPanel.removeAll();
	}

	public void addOutput(String label, String value) {
		outputPanel.add(new JLabel(label + ":"));
		outputPanel.add(new JLabel(value));
	}
	
	public void refresh() {
		window.pack();
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
		Path tour = branch.getTour();

		nodeList.setText(tour.toString());
		
		graphics.initDraw();
		
		Node prev = null;
		for (Node next : tour) {
			if (prev != null) {
				graphics.drawEdge(prev, next);
			}
			prev = next;
		}
		
		drawNodes(tour);
		drawNodes(branch.getUnvisited());
		
		graphics.display();
	}

	public void reset() {
		graphics.initDraw();
		graphics.display();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				clearOutput();
				nodeList.removeAll();
				refresh();
			}
		});

	}
	
	/*
	 * Thanks to Jarrod Roberson
	 * http://stackoverflow.com/questions/6710094/how-to-format-an-elapsed-time-interval-in-hhmmss-sss-format-in-java
	 */
    public static String formatTimeInterval(final long l) {
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
    }

}


