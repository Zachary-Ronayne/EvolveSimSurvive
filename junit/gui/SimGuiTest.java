package gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Window;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.SimGui;
import evolve.util.options.Settings;

public class SimGuiTest{
	
	private SimGui gui;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		gui = handler.getSimGui();
		gui.getFrame().setVisible(false);
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testRenderBuffer(){
		gui.renderBuffer();
	}
	
	@Test
	public void testUpdateSimInfo(){
		gui.updateSimInfo();
	}
	
	@Test
	public void testGetFrame(){
		Window frame = gui.getFrame();
		assertFalse(frame == null);
	}
	
	@Test
	public void isSetPaused(){
		gui.setPaused(true);
		assertTrue(gui.isPaused());
	}
	
	@Test
	public void testTogglePause(){
		Main.SETTINGS.numThreads.setValue(0);
		handler.setUpThreadPool();
		gui.setPaused(false);
		gui.togglePaused();
		assertTrue(gui.isPaused());
		gui.togglePaused();
		assertFalse(gui.isPaused());
	}
	
	@Test
	public void testSetSimSize(){
		gui.setSimSize(400, 200);
	}
	
	@Test
	public void testSetSimWidth(){
		gui.setSimWidth(400);
	}
	
	@Test
	public void testSetSimHeight(){
		gui.setSimHeight(200);
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
