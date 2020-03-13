package gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GraphGui;
import evolve.gui.GuiHandler;
import evolve.gui.HelpGui;
import evolve.gui.NeuralNetGui;
import evolve.gui.SavesGui;
import evolve.gui.SettingsGui;
import evolve.gui.SimGui;
import evolve.gui.SimSpeedGui;
import evolve.gui.SortingGui;
import evolve.sim.Simulation;
import evolve.util.clock.GameClock;
import evolve.util.options.Settings;

public class GuiHandlerTest{
	
	private GuiHandler handler;
	private SimGui simGui;
	private SavesGui savesGui;
	private NeuralNetGui neuralNetGui;
	private HelpGui helpGui;
	private SimSpeedGui speedGui;
	private SettingsGui settingsGui;
	private GraphGui graphGui;
	private SortingGui sortingGui;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = new GuiHandler();
		simGui = new SimGui(handler);
		handler.setSimGui(simGui);
		savesGui = new SavesGui(handler);
		handler.setSavesGui(savesGui);
		neuralNetGui = new NeuralNetGui(handler);
		handler.setNeuralNetGui(neuralNetGui);
		helpGui = new HelpGui(handler);
		handler.setHelpGui(helpGui);
		speedGui = new SimSpeedGui(handler);
		handler.setSpeedGui(speedGui);
		settingsGui = new SettingsGui(handler);
		handler.setSettingsGui(settingsGui);
		graphGui = new GraphGui(handler);
		handler.setGraphGui(graphGui);
		sortingGui = new SortingGui(handler);
		handler.setSortingGui(sortingGui);
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testSetUpThreadPool(){
		handler.setUpThreadPool();
	}

	@Test
	public void testUpdateObjectsWithThreads(){
		handler.updateObjectsWithThreads(0);
		handler.updateObjectsWithThreads(1);
		handler.updateObjectsWithThreads(2);
	}
	
	@Test
	public void testUpdateSimFull(){
		handler.updateSimFull();
	}
	
	@Test
	public void testGetThreadPool(){
		Main.SETTINGS.numThreads.setValue(1);
		handler.setUpThreadPool();
		assertFalse(handler.getThreadPool() == null);
	}

	@Test
	public void testEndThreadpool(){
		handler.endThreadPool();
	}
	
	@Test
	public void testGetSetSimulation(){
		GameClock clock = handler.getClock();
		clock.setStopUpdates(false);
		
		Simulation sim = new Simulation();
		handler.setSimulation(sim);
		assertTrue(handler.getSimulation().equals(sim));
		
		handler.getClock().setStopUpdates(true);
		assertTrue(handler.getSimulation() == null);

		handler.getClock().setStopUpdates(false);
		assertFalse(handler.getSimulation() == null);
	}
	
	@Test
	public void testGetAbsoluteSimulation(){
		handler.getClock().setStopUpdates(true);
		assertFalse(handler.getAbsoluteSimulation() == null);

		handler.getClock().setStopUpdates(false);
		assertFalse(handler.getAbsoluteSimulation() == null);
	}
	
	@Test
	public void testDeleteSimulation(){
		handler.deleteSimulation();
		assertTrue(handler.getSimulation() == null);
	}
	
	@Test
	public void testGetClock(){
		assertFalse(handler.getClock() == null);
	}
	
	@Test
	public void testTogglePaused(){
		handler.getSimGui().setPaused(true);
		handler.togglePaused();
		assertFalse(handler.getSimGui().isPaused());
	}
	
	@Test
	public void testGetSetSimGui(){
		SimGui gui = new SimGui(handler);
		handler.setSimGui(gui);
		
		assertTrue(handler.getSimGui().equals(gui));
	}
	
	@Test
	public void testGetSetSavesGui(){
		SavesGui gui = new SavesGui(handler);
		handler.setSavesGui(gui);
		
		assertTrue(handler.getSavesGui().equals(gui));
	}
	
	@Test
	public void testGetSetNeuralNetGui(){
		NeuralNetGui gui = new NeuralNetGui(handler);
		handler.setNeuralNetGui(gui);
		
		assertTrue(handler.getNeuralNetGui().equals(gui));
	}
	
	@Test
	public void testGetSetHelpGui(){
		HelpGui gui = new HelpGui(handler);
		handler.setHelpGui(gui);
		
		assertTrue(handler.getHelpGui().equals(gui));
	}
	
	@Test
	public void testGetSetSpeedGui(){
		SimSpeedGui gui = new SimSpeedGui(handler);
		handler.setSpeedGui(gui);
		
		assertTrue(handler.getSpeedGui().equals(gui));
	}
	
	@Test
	public void testGetSetGraphGui(){
		GraphGui gui = new GraphGui(handler);
		handler.setGraphGui(gui);
		
		assertTrue(handler.getGraphGui().equals(gui));
	}
	
	@Test
	public void testGetSetSettingsGui(){
		SettingsGui gui = new SettingsGui(handler);
		handler.setSettingsGui(gui);
		
		assertTrue(handler.getSettingsGui().equals(gui));
	}
	
	@Test
	public void testSetUpGuiPinning(){
		handler.setUpGuiPinning();
	}
	
	@Test
	public void testCloseAllExtraWindows(){
		handler.closeAllExtraWindows();
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
