package evolve.gui.component.neuralNet;

/**
 * A specific case of a {@link NeuralNetGuiLabel} where the value is displayed as a string
 */
public class NeuralNetGuiStringLabel extends NeuralNetGuiLabel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The value kept track of in this {@link NeuralNetGuiLabel} as a string
	 */
	private String value;
	
	public NeuralNetGuiStringLabel(String title, String value){
		super(title, false);
		setValue(value);
	}
	
	/**
	 * Get the string that keeps track of the value for this {@link NeuralNetGuiLabel}
	 * @return the value
	 */
	public String getValue(){
		return value;
	}
	
	/**
	 * Set the string that keeps track of the value for this {@link NeuralNetGuiLabel} and updates the component
	 * @param value the new value
	 */
	public void setValue(String value){
		this.value = value;
		updateValueLabel();
	}
	
	@Override
	public void updateValueLabel(){
		setValueString(this.value);
	}
	
}
