package evolve.gui.component.neuralNet;

import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JPanel;

import evolve.gui.NeuralNetGui;
import evolve.gui.component.SimConstants;
import evolve.gui.component.SimLabel;
import evolve.gui.layout.SimLayoutHandler;

/**
 * A {@link JPanel} that is designed specifically for use in {@link NeuralNetGui}
 */
public class NeuralNetGuiLabel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The name of this {@link NeuralNetGuiLabel}
	 */
	private String labelName;
	
	/**
	 * The numerical values displayed by this {@link NeuralNetGuiLabel}
	 */
	private double[] values;
	
	/**
	 * True if the numerical value for this {@link NeuralNetGuiLabel} is an integer, false otherwise
	 */
	private boolean integer;
	
	/**
	 * The label that this {@link NeuralNetGuiLabel} uses to display it's name
	 */
	private SimLabel nameLabel;
	
	/**
	 * The label that this {@link NeuralNetGuiLabel} uses to display it's value
	 */
	private SimLabel valueLabel;
	
	/**
	 * Create a mew {@link NeuralNetGuiLabel} with the specified name, value, and integer state
	 * @param labelName the to display for this label
	 * @param value the value to display on this label
	 * @param integer true of this label is an integer, false if it is a double value
	 */
	public NeuralNetGuiLabel(String labelName, boolean integer, double... values){
		super();
		nameLabel = new SimLabel("");
		nameLabel.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 18));
		valueLabel = new SimLabel("");
		valueLabel.setFont(new Font("Courier New", Font.PLAIN, 16));
		SimLayoutHandler.createVerticalLayout(this);
		add(nameLabel);
		add(valueLabel);
		
		setLabelName(labelName);
		setValues(values);
		setInteger(integer);
	}

	/**
	 * Create a mew {@link NeuralNetGuiLabel} with the specified name and integer state
	 * @param labelName the to display for this label
	 * @param integer true of this label is an integer, false if it is a double value
	 */
	public NeuralNetGuiLabel(String name, boolean integer){
		this(name, integer, 0);
	}
	
	/**
	 * Create an empty {@link NeuralNetGuiLabel}
	 */
	public NeuralNetGuiLabel(){
		this("", true, 0);
	}
	
	/**
	 * Get the name of this Label.
	 * @return the name
	 */
	public String getLabelName(){
		return labelName;
	}
	/**
	 * Set the name of this label. This method automatically updates the components
	 * @param labelName the new name
	 */
	public void setLabelName(String labelName){
		this.labelName = labelName;
		updateNameLabel();
	}
	
	/**
	 * Get the values of this label.
	 * @return the values
	 */
	public double[] getValues(){
		return values;
	}
	/**
	 * Set the value of this label. This method automatically updates the components
	 * @param value the new value
	 */
	public void setValues(double... values){
		this.values = values;
		updateValueLabel();
	}
	
	/**
	 * Get if this label is an integer
	 * @return true if this Label is an integer, false otherwise
	 */
	public boolean isInteger(){
		return integer;
	}
	/**
	 * Set the state of if this label is an integer. This method automatically updates the components
	 * @param integer the new state
	 */
	public void setInteger(boolean integer){
		this.integer = integer;
		updateValueLabel();
	}
	
	/**
	 * Update the nameLabel of this {@link NeuralNetGuiLabel} based on it's current state
	 */
	public void updateNameLabel(){
		setNameString(this.labelName);
	}
	
	/**
	 * Update the valueLabel of this {@link NeuralNetGuiLabel} based on it's current state
	 */
	public void updateValueLabel(){
		String s = "";
		for(int i =  0; i < values.length; i++){
			double d = values[i];
			String add;
			if(this.isInteger()) add = "" + (int)d;
			else{
				BigDecimal bd = new BigDecimal(d);
				bd = bd.setScale(5, RoundingMode.HALF_UP);
				add = "" + bd.doubleValue();
			}
			s += add;
			if(i != values.length - 1) s += "/";
		}
		setValueString(s);
	}
	
	/**
	 * Set the name label to the given string name. The name is automatically put into HTML and can use HTML to format it
	 * @param name the name to set
	 */
	public void setNameString(String name){
		nameLabel.setText("<html>" + name + ":</html>");
		repaint();
	}
	
	/**
	 * Set the value label to the given string value. The value is automatically put into HTML and can use HTML to format it
	 * @param value the value to set
	 */
	public void setValueString(String value){
		valueLabel.setText("<html>" + value + "</html>");
		repaint();
	}
	
}
