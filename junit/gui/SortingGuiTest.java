package gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.SortingGui;
import evolve.util.options.Settings;

public class SortingGuiTest{
	
	private GuiHandler handler;
	private SortingGui gui;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		gui = handler.getSortingGui();
	}
	
	@Test
	public void testRenderCreaturePanel(){
		gui.renderCreaturePanel();
	}
	
	@Test
	public void testUpdateCreatureList(){
		gui.updateCreaturesList(SortingGui.getSortComparable(SortingGui.SORT_AGE));
		gui.updateCreaturesList(SortingGui.getSortComparable(SortingGui.SORT_ENERGY));
		gui.updateCreaturesList(SortingGui.getSortComparable(SortingGui.SORT_GENERATION));
		gui.updateCreaturesList(SortingGui.getSortComparable(SortingGui.SORT_CHILDREN));
	}
	
	@Test
	public void testSearchCreatureList(){
		gui.searchCreatureList();
	}
	
	@Test
	public void testGetFrame(){
		gui.getFrame();
	}

	@Test
	public void testSetsearchMessage(){
		gui.setSearchSortMessage("test message");
	}
	
	@Test
	public void testSetSearchSortError(){
		gui.setSearchSortError("test error");
	}
	
	@Test
	public void testgetSortComparable(){
		assertFalse(SortingGui.getSortComparable(SortingGui.SORT_AGE) == null);
		assertFalse(SortingGui.getSortComparable(SortingGui.SORT_ENERGY) == null);
		assertFalse(SortingGui.getSortComparable(SortingGui.SORT_GENERATION) == null);
		assertFalse(SortingGui.getSortComparable(SortingGui.SORT_CHILDREN) == null);
		assertFalse(SortingGui.getSortComparable("uixfyiet7") == null);
		assertTrue(SortingGui.getSortComparable(null) == null);
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
