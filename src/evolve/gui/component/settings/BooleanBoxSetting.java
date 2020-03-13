package evolve.gui.component.settings;

import evolve.gui.SettingsGui;
import evolve.gui.component.SimCheckBox;
import evolve.util.options.Setting;

/**
 * A Settings Component that uses a check box for boolean input
 */
public class BooleanBoxSetting extends SettingsComponent{
	private static final long serialVersionUID = 1L;

	private SimCheckBox checkBox;
	
	public BooleanBoxSetting(Setting<?> setting, SettingsGui gui){
		super(setting, gui);
	}

	@Override
	public void initializeInputComponent(){
		checkBox = new SimCheckBox();
	}

	@Override
	public void addInputComponent(){
		add(checkBox);
	}

	@Override
	public void setInputValue(String s){
		checkBox.setSelected(s.equals("true"));
	}

	@Override
	public String getInputValue(){
		boolean state = checkBox.isSelected();
		if(state) return "true";
		else return "false";
	}
	
}
