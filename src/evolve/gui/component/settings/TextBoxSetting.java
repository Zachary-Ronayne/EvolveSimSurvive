package evolve.gui.component.settings;

import java.awt.Color;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import evolve.gui.SettingsGui;
import evolve.gui.component.SimTextBox;
import evolve.util.options.Setting;

/**
 * A SettingsComponent that uses a text box for input
 */
public class TextBoxSetting extends SettingsComponent{
	private static final long serialVersionUID = 1L;
	
	private SimTextBox inputBox;
	
	public TextBoxSetting(Setting<?> setting, SettingsGui gui){
		super(setting, gui);
		//set the input so that the box turns red when an invalid value is put in
		((AbstractDocument)inputBox.getDocument()).setDocumentFilter(new DocumentFilter(){
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
				super.replace(fb, offset, length, text, attrs);
				updateText();
			}
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException{
				super.insertString(fb, offset, string, attr);
				updateText();
			}
			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException{
				super.remove(fb, offset, length);
				updateText();
			}
		});
	}
	
	@Override
	public void initializeInputComponent(){
		inputBox = new SimTextBox();
	}
	
	@Override
	public void addInputComponent(){
		add(inputBox);
	}

	@Override
	public void setInputValue(String s){
		inputBox.setText(s);
	}

	@Override
	public String getInputValue(){
		return inputBox.getText();
	}
	
	/**
	 * Update the color of the text box as valid based on the current state of the input
	 */
	public void updateText(){
		if(isValid(getInputValue())) inputBox.setBackground(Color.WHITE);
		else inputBox.setBackground(new Color(255, 200, 200));
	}
	
	@Override
	public boolean setSetting(){
		if(isValid(getInputValue())){
			return super.setSetting();
		}
		return false;
	}
	
	/**
	 * Determine if the given string is invalid for this Setting<br>
	 * Override this method to add custom conditions
	 * @param str the string
	 * @return true if the given string is valid for this setting, false otherwise
	 */
	public boolean isValid(String str){
		return getSetting().validValue(str) != null;
	}
	
	/**
	 * Get the SimTextBox that this Setting uses for input
	 * @return
	 */
	public SimTextBox getInput(){
		return inputBox;
	}
	
}
