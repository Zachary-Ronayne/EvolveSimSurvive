package gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.NeuralNetGui;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.clock.GameClock;
import evolve.util.options.Settings;

public class NeuralNetGuiTest{
	
	private NeuralNetGui gui;
	
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		gui = new NeuralNetGui(handler);
		gui.getFrame().setVisible(false);
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
		GameClock clock = handler.getClock();
		clock.setStopUpdates(false);
	}
	
	@Test
	public void testGetSetSelectedCreature(){
		NeuralNetCreature creature = handler.getSimulation().getEvolvingCreature();
		gui.setSelectedCreature(creature);
		assertTrue(gui.getSelectedCreature().equals(creature));
	}
	
	@Test
	public void testUpDateNeuralNetDisplay(){
		gui.updateNeuralNetDisplay();
	}
	
	@Test
	public void testGetFrame(){
		assertFalse(gui.getFrame() == null);
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
