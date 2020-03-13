package gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.HelpGui;

public class HelpGuiTest{
	
	private GuiHandler handler;
	private HelpGui gui;
	
	@Before
	public void setUp(){
		handler = Main.crateHandler();
		gui = handler.getHelpGui();
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testMakeTile(){
		gui.makeTitle("test");
	}
	
	@Test
	public void getFrame(){
		gui.getFrame();	
	}
	
	@After
	public void end(){
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
		handler.endThreadPool();
	}
}
