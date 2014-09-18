package cosc561.tsp;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

public abstract class Controls {
	
	public abstract void next();
	
	public abstract void setPaused(boolean paused);
	
	public abstract void setRender(boolean render);

	public final Action nextButton() {
		return new AbstractAction("Next") {
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					public void run() {
						next();	
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
	};

}
