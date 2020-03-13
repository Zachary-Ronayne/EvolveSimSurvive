package evolve.util.options;

import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import evolve.util.Saveable;

/**
 * An object that stores one setting, it's name, and a description of what that setting does. 
 * Sub classes of this class determine how a string is converted to the value of this object
 */
public abstract class Setting<E> implements Saveable{
	
	/**
	 * The name of this setting
	 */
	private String name;
	/**
	 * The description of what this setting does
	 */
	private String description;
	
	/**
	 * The value of this setting
	 */
	private E value;
	
	/**
	 * The default value of this setting
	 */
	private E defaultValue;
	
	/**
	 * Create a Setting with the given name and description
	 * @param name
	 * @param description
	 */
	public Setting(E value, E defaultValue, String name, String description){
		this.value = value;
		this.defaultValue = defaultValue;
		
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Get the value of this Setting
	 * @return the value
	 */
	public E value(){
		return value;
	}
	/**
	 * Set the value of this setting based on the corresponding type of this setting
	 */
	public void setValue(E value){
		this.value = value;
	}
	
	public E getDefaultValue(){
		return defaultValue;
	}
	/**
	 * Get a string representation of this setting's default value
	 * @return the string
	 */
	public String getDefaultValueString(){
		return "" + getDefaultValue();
	}
	public void setDefaultValue(E defaultValue){
		this.defaultValue = defaultValue;
	}
	/**
	 * Set the value of this setting to the default value of this setting
	 */
	public void loadDefaultValue(){
		this.value = defaultValue;
	}
	
	/**
	 * Take the given value, try to convert it to the appropriate data type, 
	 * and return the converted data type.
	 * @param value the value to convert
	 * @return the converted data, null if the value was invalid
	 */
	public abstract E validValue(String value);
	
	/**
	 * Take the given value, convert it to the appropriate data type, then store the data in this object.
	 * @param value the value to set
	 * @return true if the value was set, false if the value was invalid and was not set
	 */
	public boolean setStringValue(String value){
		E newValue = validValue(value);
		if(newValue == null) return false;
		else{
			setValue(newValue);
			return true;
		}
	}
	
	@Override
	public String toString(){
		return value.toString();
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * Saves this setting to the given PrintWriter object
	 * @param write the PrintWriter that will save the Setting
	 */
	@Override
	public boolean save(PrintWriter write){
		String n = name.replace(' ', '_');
		write.println(n + ": " + toString());
		
		return true;
	}

	/**
	 * Loads the Setting in from the given Scanner object
	 * @param read the Scanner that will load the Setting
	 */
	@Override
	public boolean load(Scanner read){
		try{
			read.next();
			String value = read.nextLine().substring(1);
			setStringValue(value);
			return true;
		}catch(NoSuchElementException e){
			return false;
		}
	}
}
