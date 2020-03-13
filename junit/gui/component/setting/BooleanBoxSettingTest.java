package gui.component.setting;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.component.settings.BooleanBoxSetting;
import evolve.util.options.BooleanSetting;
import evolve.util.options.Settings;

public class BooleanBoxSettingTest{
	
	private BooleanBoxSetting bSetting;
	private BooleanSetting setting;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		setting = new BooleanSetting(true, "test", "test description");
		bSetting = new BooleanBoxSetting(setting, handler.getSettingsGui());
		bSetting.initializeInputComponent();
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testAddInputComponent(){
		bSetting.addInputComponent();
	}
	
	@Test
	public void testGetSetInputValue(){
		bSetting.setInputValue("true");
		assertTrue(bSetting.getInputValue().equals("true"));
		bSetting.setInputValue("false");
		assertTrue(bSetting.getInputValue().equals("false"));
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
	}
	
}
