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
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import cosc561.tsp.TravellingSalesman;
import cosc561.tsp.strategy.Strategy;

public abstract class Controls {
	
	public abstract void next(boolean manual);
	
	public abstract void reset();
	
	public abstract void setPaused(boolean paused);
	
	public abstract void setRender(boolean render);
	
	public abstract void changeStrategy(Class<? extends Strategy> strategy);
	
	private ControlPanel panel;
		
	public final ControlPanel getPanel() {
		if (panel == null) {
			panel = new ControlPanel();
		}
		return panel;
	};
	
	private class ControlPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public ControlPanel() {
			super();
			
			setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			
			JCheckBox render = new JCheckBox("Render");
			add(render);
			render.setSelected(true);
			render.addItemListener(renderToggle());
			
			JToggleButton pause = new JToggleButton("Pause");
			add(pause);
			pause.setSelected(true);
			pause.addItemListener(pauseToggle());
			
			JButton next = new JButton("Next");
			add(next);
			next.setAction(nextButton());

			JButton reset = new JButton("Reset");
			add(reset);
			reset.setAction(resetButton());
			
			JComboBox<Class<?>> strategies = new JComboBox<>(TravellingSalesman.strategies);
			add(strategies);
			strategies.addActionListener(strategiesComboBox());
	}
		
		private ActionListener strategiesComboBox() {
			return new ActionListener() {
			    @SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					JComboBox<? extends Strategy> strategies = (JComboBox<? extends Strategy>)e.getSource();
			        changeStrategy((Class<? extends Strategy>)strategies.getSelectedItem());
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
							reset();	
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

		public final ItemListener renderToggle()  {
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
