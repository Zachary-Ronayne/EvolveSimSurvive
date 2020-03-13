package gui.component.setting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.component.settings.NumberBoxSetting;
import evolve.util.options.DoubleSetting;
import evolve.util.options.IntSetting;
import evolve.util.options.Settings;

public class NumberBoxSettingTest{

	private DoubleSetting dSetting;
	private IntSetting iSetting;
	private NumberBoxSetting dNumSetting;
	private NumberBoxSetting iNumSetting;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		dSetting = new DoubleSetting(1.0, "test d", "d description");
		iSetting = new IntSetting(1, "test i", "i description");
		
		dNumSetting = new NumberBoxSetting(dSetting, handler.getSettingsGui(), false, 0, 2);
		iNumSetting = new NumberBoxSetting(iSetting, handler.getSettingsGui(), true, 0, 2);
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testIsValidInRange(){
		assertTrue(dNumSetting.isValid("1"));
		assertTrue(dNumSetting.isValid("1.0"));
		assertFalse(dNumSetting.isValid("-1.0"));
		assertFalse(dNumSetting.isValid("3.0"));
		assertFalse(dNumSetting.isValid("a"));
		assertFalse(dNumSetting.isValid(""));

		assertTrue(iNumSetting.isValid("1"));
		assertFalse(iNumSetting.isValid("1.0"));
		assertFalse(iNumSetting.isValid("-1.0"));
		assertFalse(iNumSetting.isValid("3.0"));
		assertFalse(iNumSetting.isValid("a"));
		assertFalse(iNumSetting.isValid(""));
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
	}
	
}
