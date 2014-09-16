package cosc561.tsp.view;

import java.awt.Component;

import javax.swing.JTextField;

public class Output {
	
	private final String label;
	private final JTextField field;
	
	private int value;
	
	public Output(String label) {
		this.label = label;
		this.field = new JTextField();
		
//		field.setMaximumSize());
		field.setEditable(false);
		
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
		field.setText(value);
	}
	

}
