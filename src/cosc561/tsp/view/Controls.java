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
import cosc561.tsp.strategy.classes.Strategies;

public abstract class Controls {

	public abstract void next(boolean manual);

	public abstract void reset(Strategies strategy, int nodes);

	public abstract void setPaused(boolean paused);

	public abstract void setRender(boolean render);
	
	public abstract void setShowBest(boolean best);

	public abstract void changeStrategy(Strategies strategy);

	private ControlPanel panel;

	private volatile JToggleButton pause;
	
	private volatile JComboBox<Strategies> strategies;
	
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
			
			JCheckBox best = new JCheckBox("Show Best");
			add(best);
			best.setSelected(false);
			best.addItemListener(bestToggle());

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

			strategies = new JComboBox<Strategies>(Strategies.ALL);
			strategies.setSelectedItem(Strategies.DEFAULT);
			add(strategies);
			strategies.addActionListener(strategiesComboBox());
			
			add(new JLabel("Node Cap"));
			model = new SpinnerNumberModel(TravellingSalesman.DEFAULT_NODES, 0, TravellingSalesman.MAX_NODES, 1);
			JSpinner nodeCount = new JSpinner(model);
			add(nodeCount);
		}

		private ItemListener bestToggle() {
			return new ItemListener() {
				public void itemStateChanged(final ItemEvent event) {
					new Thread(new Runnable() {
						public void run() {
							setShowBest(event.getStateChange() == ItemEvent.SELECTED);
						}
					}).start();
				}
			};
		}

		private ActionListener strategiesComboBox() {
			return new ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(final ActionEvent e) {
					new Thread(new Runnable() {
						public void run() {
							JComboBox<Strategies> strategies = (JComboBox<Strategies>) e.getSource();
							changeStrategy((Strategies) strategies.getSelectedItem());
						}
					});
				}
			};
		}

		@SuppressWarnings("serial")
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

		@SuppressWarnings("serial")
		public final Action resetButton() {
			return new AbstractAction("Reset") {
				public void actionPerformed(ActionEvent event) {
					new Thread(new Runnable() {
						public void run() {
							reset((Strategies)strategies.getSelectedItem(), (int)model.getValue());
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
