package cosc561.tsp.view;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Output {
	
	private final String label;
	private final JLabel field;
	
	private int value;
	
	public Output(String label) {
		this.label = label;
		this.field = new JLabel("", JLabel.RIGHT);
		
//		field.setMaximumSize());
//		field.setEditable(false);
		
		value = 0;
	}

	public String getLabel() {
		return label;
	}

	public Component getField() {
		return field;
	}

	public void setValue(int value) {
		this.value = value;
		setValue(Integer.toString(value));
	}
	
	public void increment() {
		this.value++;
		setValue(this.value);
	}
	
	public void setValue(String value) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				field.setText(value);
			}
		});
	}
	

}
