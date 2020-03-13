package gui;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GraphGui;
import evolve.gui.GuiHandler;
import evolve.util.options.Settings;

public class GraphGuiTest{
	
	private GuiHandler handler;
	private GraphGui gui;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		gui = handler.getGraphGui();
		handler.closeAllExtraWindows();
	}
	
	@Test
	public void testRenderSelectedGraph(){
		gui.renderSelectedGraph();
	}
	
	@Test
	public void testSelectGraph(){
		gui.selectGraph(GraphGui.OPTION_POPULATION);
		gui.selectGraph(GraphGui.OPTION_MUTABILITY);
		gui.selectGraph(GraphGui.OPTION_AGE);
	}
	
	@Test
	public void testGetSelectedHolder(){
		gui.selectGraph(GraphGui.OPTION_POPULATION);
	}
	
	@Test
	public void testSetPopulationGraph(){
		assertFalse(gui.getPopulationGraph() == null);
	}

	@Test
	public void testSetMutabilityGraph(){
		assertFalse(gui.getMutabilityGraph() == null);
	}

	@Test
	public void testSetAgeGraph(){
		assertFalse(gui.getAgeGraph() == null);
	}

	@Test
	public void testGetFrame(){
		gui.getFrame();
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
		handler.endThreadPool();
	}
	
}
