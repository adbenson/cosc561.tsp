package cosc561.tsp.view;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Output {
	
	private final String label;
	private final JLabel field;
	
	private long value;
	private long nextUpdate;
	private int updateInterval;
	
	public Output(String label) {
		this(label, 1);
	}
	
	public Output(String label, int updateInterval) {
		this.label = label;
		this.field = new JLabel("", JLabel.RIGHT);
		value = 0;
		nextUpdate = 1;
		this.updateInterval = updateInterval;
	}

	public String getLabel() {
		return label;
	}

	public Component getField() {
		return field;
	}

	public void setValue(long value) {
		this.value = value;
		setValue(Long.toString(value));
	}
	
	public void increment() {
		this.value++;
		setValue(this.value);
	}
	
	public void setValue(String value) {
		if (this.value >= this.nextUpdate) {
			setFieldValue(value);
			this.nextUpdate = this.nextUpdate + this.updateInterval;
		}
	}
	
	private void setFieldValue(final String value) {
		field.setText(value);
	}

	public long getLongValue() {
		return this.value;
	}
	

}
