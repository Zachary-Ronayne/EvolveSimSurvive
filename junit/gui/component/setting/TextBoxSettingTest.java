package gui.component.setting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.component.settings.TextBoxSetting;
import evolve.util.options.Settings;
import evolve.util.options.StringSetting;

public class TextBoxSettingTest{
	
	private TextBoxSetting textSetting;
	private	StringSetting setting;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		setting = new StringSetting("test", "test name", "test description");
		textSetting = new TextBoxSetting(setting, handler.getSettingsGui());
		textSetting.initializeInputComponent();
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testAddInputComponent(){
		textSetting.addInputComponent();
	}
	
	@Test
	public void testGetSetInputValue(){
		textSetting.setInputValue("test2");
		assertTrue(textSetting.getInputValue().equals("test2"));
	}
	
	@Test
	public void testUpdateText(){
		textSetting.getSetting().setValue(null);
		textSetting.updateText();
		textSetting.getSetting().setStringValue("test3");
		textSetting.updateText();
	}
	
	@Test
	public void testisValid(){
		assertFalse(textSetting.isValid(null));
		assertTrue(textSetting.isValid("test"));
	}
	
	@Test
	public void testGetInput(){
		textSetting.getInput();
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
	}
	
}
