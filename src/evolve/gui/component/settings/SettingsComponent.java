package evolve.gui.component.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import evolve.gui.SettingsGui;
import evolve.gui.component.SimButton;
import evolve.gui.component.SimLabel;
import evolve.gui.layout.SimLayoutHandler;
import evolve.util.options.Setting;

/**
 * A class that keeps track of everything needed to display and modify a Setting object.<br>
 * It is the responsibility of extentions of this class to call getGui().settingChanged() when the value of the input field of this object has changed
 */
public abstract class SettingsComponent extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The Setting object this Component uses
	 */
	private Setting<?> setting;
	
	/**
	 * The SettingsGui that this settings object communicates with
	 */
	private SettingsGui gui;
	
	/**
	 * The title label of this setting
	 */
	private SimLabel title;
	
	public SettingsComponent(Setting<?> setting, SettingsGui gui){
		super();
		this.setting = setting;
		this.gui = gui;
		
		//set up this component
		setBackground(Color.WHITE);
		SimLayoutHandler.createHorizontalLayout(this);
		setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1, false));
		
		//add components
		
		//add the title
		title = new SimLabel("");
		title.setBorder(new EmptyBorder(0, 5, 0, 5));
		Font f = title.getFont();
		title.setFont(new Font(f.getName(), f.getStyle(), 12));
		updateTitleValue();
		add(title);
		//add the input
		initializeInputComponent();
		addInputComponent();
		setInputValue(setting.toString());
		//add the default value button
		SimButton defaultValue = new SimButton();
		defaultValue.setText("<html>Default<br>(" + getSetting().getDefaultValueString() + ")<html>");
		f = defaultValue.getFont();
		defaultValue.setFont(new Font(f.getName(), f.getStyle(), 11));
		defaultValue.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				setInputValue(getSetting().getDefaultValueString());
			}
		});
		add(defaultValue);

		setPreferredSize(new Dimension(450, 28));
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				gui.setDescriptionText(getSetting().getDescription());
			}
			@Override
			public void mouseExited(MouseEvent e){
				gui.setDescriptionText("");
			}
		});
	}

	/**
	 * Set up the input component for this object.<br>
	 * Do not initialize the input component in the constructor
	 */
	public abstract void initializeInputComponent();
	
	/**
	 * Add the component that this object uses for input
	 */
	public abstract void addInputComponent();
	
	/**
	 * Set the input of this component based on the given string
	 * @param s the string
	 */
	public abstract void setInputValue(String s);
	
	/**
	 * Get a String representing the input value of this component
	 * @return
	 */
	public abstract String getInputValue();

	public Setting<?> getSetting(){
		return setting;
	}
	
	/**
	 * Updates the text in the title of this component to the current value in the setting
	 */
	public void updateTitleValue(){
		title.setText("<html>" + getSetting().getName() + "<br>(" + getSetting().toString() + ")</html>");
	}
	
	/**
	 * Attempt to take the input from this Component and set its associated setting with that input, 
	 * and set the setting to the input if it is able to
	 * @return true if the setting was set successfully, false otherwise
	 */
	public boolean setSetting(){
		if(setting.setStringValue(getInputValue())){
			setInputValue(setting.toString());
			return true;
		}
		updateTitleValue();
		return false;
	}
	public void setSetting(Setting<?> setting){
		this.setting = setting;
		updateTitleValue();
	}
	
	/**
	 * Get the SettingsGui that this object communicates with
	 * @return
	 */
	public SettingsGui getGui(){
		return gui;
	}
	
}
