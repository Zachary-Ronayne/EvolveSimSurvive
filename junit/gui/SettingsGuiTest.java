package gui;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.SettingsGui;
import evolve.util.options.Settings;

public class SettingsGuiTest{
	
	private GuiHandler handler;
	private SettingsGui gui;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		gui = handler.getSettingsGui();
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testUpdateSettings(){
		gui.updateSettings();	
	}
	
	@Test
	public void testUpdateSettingsLabels(){
		gui.updateSettingsLabels();	
	}
	
	@Test
	public void testSetDescriptionText(){
		gui.setDescriptionText("test");
	}
	
	@Test
	public void testLoadAllDefaultSettings(){
		gui.loadAllDefaultSettings();
	}
	
	@Test
	public void testGetFrame(){
		assertTrue(gui.getFrame().equals(handler.getSettingsGui().getFrame()));
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.closeAllExtraWindows();
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
		handler.endThreadPool();
	}
	
}
