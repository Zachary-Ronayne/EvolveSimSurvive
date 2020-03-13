package evolve.gui.component.settings;

import evolve.gui.SettingsGui;
import evolve.util.options.Setting;

public class NumberBoxSetting extends TextBoxSetting{
	private static final long serialVersionUID = 1L;

	private boolean isInt;
	
	private double lowRange;
	private double highRange;
	
	/**
	 * Create a new Settings object that uses numbers
	 * @param lowRange the lowest this number can be
	 * @param highRange the highest this number can be
	 * @param setting the setting object
	 * @param isInt true if this should only accept integers, false if it should only accept doubles
	 */
	public NumberBoxSetting(Setting<?> setting, SettingsGui gui, boolean isInt, double lowRange, double highRange){
		super(setting, gui);
		this.isInt = isInt;
		this.lowRange = lowRange;
		this.highRange = highRange;
	}
	
	@Override
	public boolean isValid(String str){
		return super.isValid(str) && inRange(str);
	}
	
	/**
	 * Determines if the given string is a valid number, and if it's in the given range
	 * @param value the value to test
	 * @return true if the value is valid and in range, false otherwise
	 */
	public boolean inRange(String value){
		try{
			if(isInt){
				Integer num = Integer.parseInt(value);
				return num >= lowRange && num <= highRange;
			}
			else{
				Double num = Double.parseDouble(value);
				return num >= lowRange && num <= highRange;
			}
		}catch(NumberFormatException e){
			return false;
		}
	}
	
}
