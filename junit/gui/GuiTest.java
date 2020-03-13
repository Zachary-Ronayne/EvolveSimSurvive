package gui;

import static org.junit.Assert.assertTrue;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.Gui;
import evolve.gui.GuiHandler;
import evolve.util.options.Settings;

public class GuiTest{
	
	private Gui gui;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		
		gui = new Gui(handler){
			private JFrame frame = new JFrame();
			@Override
			public JFrame getFrame(){
				return frame;
			}
		};
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testAddOpenCloseControls(){
		gui.addOpenCloseControls();
	}
	
	@Test
	public void testGetFrame(){
		gui.getFrame();
	}
	
	@Test
	public void testGetHandler(){
		assertTrue(gui.getHandler().equals(handler));
	}
	
	@After
	public void end(){
		gui.getFrame().setVisible(false);
		handler.closeAllExtraWindows();
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
		handler.endThreadPool();
	}
	
}
