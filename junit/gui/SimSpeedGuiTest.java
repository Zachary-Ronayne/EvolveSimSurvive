package gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.SimSpeedGui;
import evolve.util.options.Settings;

public class SimSpeedGuiTest{
	
	private GuiHandler handler;
	private SimSpeedGui gui;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		gui = handler.getSpeedGui();
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testLoopSim(){
		gui.loopSim(1000);
		gui.loopSim(-1);
	}
	
	@Test
	public void testGetSimTextBox(){
		gui.getSimTextBox();
	}
	
	@Test
	public void testGetFrame(){
		gui.getFrame();
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
