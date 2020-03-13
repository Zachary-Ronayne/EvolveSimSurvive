package gui.component.setting;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.component.settings.SettingsComponent;
import evolve.util.options.DoubleSetting;
import evolve.util.options.Settings;
import evolve.util.options.StringSetting;

public class SettingsComponentTest{
	
	private SettingsComponent comp;
	private StringSetting setting;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		setting = new StringSetting("test", "name", "description");
		
		comp = new SettingsComponent(setting, handler.getSettingsGui()){
			private static final long serialVersionUID = 1L;
			@Override
			public void setInputValue(String s){
			}
			@Override
			public void initializeInputComponent(){
			}
			@Override
			public String getInputValue(){
				return "";
			}
			@Override
			public void addInputComponent(){
			}
		};
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testSetSetting(){
		comp.setSetting();
	}
	
	@Test
	public void testGetSetSetting(){
		assertTrue(comp.getSetting().equals(setting));
		DoubleSetting s = new DoubleSetting(1.0, "test double", "test description");
		comp.setSetting(s);
		assertTrue(comp.getSetting().equals(s));
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
	}
	
}
