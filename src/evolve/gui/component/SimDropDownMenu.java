package evolve.gui.component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * A {@code JComboBox<String>} that is set up in the style of the simulation
 */
public class SimDropDownMenu extends JComboBox<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SimDropDownMenu(){
		super();

		SimConstants.setSimStyle(this);
	}
	
	/**
	 * Set the options in the drop down menu to the given String array
	 * @param items the String array
	 */
	public void setItems(String[] items){
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(items);
		setModel(model);
	}
	
	public String getSelectedItem(){
		return (String)(getModel().getSelectedItem());
	}
	
}
