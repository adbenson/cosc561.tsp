package cosc561.tsp.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.strategy.Strategy;

public abstract class Controls {

	public abstract void next(boolean manual);

	public abstract void reset(Class strategy, int nodes);

	public abstract void setPaused(boolean paused);

	public abstract void setRender(boolean render);

	public abstract void changeStrategy(Class<? extends Strategy> strategy);

	private ControlPanel panel;

	private volatile JToggleButton pause;
	
	private volatile JComboBox<Class<?>> strategies;
	
	private volatile SpinnerNumberModel model;

	public final ControlPanel getPanel() {
		if (panel == null) {
			panel = new ControlPanel();
		}
		return panel;
	};
	

	public synchronized void setPauseButton(final boolean paused) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pause.setSelected(paused);
			}
		});
	}

	private class ControlPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public ControlPanel() {
			super();

			setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

			JCheckBox render = new JCheckBox("Render");
			add(render);
			render.setSelected(true);
			render.addItemListener(renderToggle());

			pause = new JToggleButton("Pause");
			add(pause);
			pause.setSelected(true);
			pause.addItemListener(pauseToggle());

			JButton next = new JButton("Next");
			add(next);
			next.setAction(nextButton());

			JButton reset = new JButton("Reset");
			add(reset);
			reset.setAction(resetButton());

			strategies = new JComboBox<>(TravellingSalesman.strategies);
			strategies.setSelectedItem(TravellingSalesman.DEFAULT_STRATEGY);
			add(strategies);
			strategies.addActionListener(strategiesComboBox());
			
			add(new JLabel("Node Cap"));
			model = new SpinnerNumberModel(TravellingSalesman.DEFAULT_NODES, 0, TravellingSalesman.MAX_NODES, 1);
			JSpinner nodeCount = new JSpinner(model);
			add(nodeCount);
		}

		private ActionListener strategiesComboBox() {
			return new ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					JComboBox<? extends Strategy> strategies = (JComboBox<? extends Strategy>) e
							.getSource();
					changeStrategy((Class<? extends Strategy>) strategies
							.getSelectedItem());
				}
			};
		}

		public final Action nextButton() {
			return new AbstractAction("Next") {
				public void actionPerformed(ActionEvent event) {
					new Thread(new Runnable() {
						public void run() {
							next(true);
						}
					}).start();
				}
			};
		};

		public final Action resetButton() {
			return new AbstractAction("Reset") {
				public void actionPerformed(ActionEvent event) {
					new Thread(new Runnable() {
						public void run() {
							reset((Class)strategies.getSelectedItem(), (int)model.getValue());
						}
					}).start();
				}
			};
		};

		public final ItemListener pauseToggle() {
			return new ItemListener() {
				public void itemStateChanged(final ItemEvent event) {
					new Thread(new Runnable() {
						public void run() {
							setPaused(event.getStateChange() == ItemEvent.SELECTED);
						}
					}).start();
				}
			};
		};

		public final ItemListener renderToggle() {
			return new ItemListener() {
				public void itemStateChanged(final ItemEvent event) {
					new Thread(new Runnable() {
						public void run() {
							setRender(event.getStateChange() == ItemEvent.SELECTED);
						}
					}).start();
				}
			};
		}
	}

}
