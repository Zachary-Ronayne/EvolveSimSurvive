package util.input;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.sim.Simulation;
import evolve.sim.obj.UserCreature;
import evolve.util.clock.GameClock;
import evolve.util.input.KeyInputSim;
import evolve.util.options.Settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KeyInputSimTest{
	
	private KeyInputSim keys;
	private Simulation sim;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		GameClock clock = handler.getClock();
		clock.setStopUpdates(false);
		
		sim = handler.getSimulation();
		keys = new KeyInputSim(handler);
		sim.setUserCreature(new UserCreature(sim));
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testKeyPressed(){
		JPanel pan = new JPanel();
		
		KeyEvent e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertTrue(sim.getUserCreature().getPressedButtons()[UserCreature.UP]);

		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertTrue(sim.getUserCreature().getPressedButtons()[UserCreature.DOWN]);
		
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertTrue(sim.getUserCreature().getPressedButtons()[UserCreature.LEFT]);
		
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertTrue(sim.getUserCreature().getPressedButtons()[UserCreature.RIGHT]);

		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_E, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);

		sim.setUserCreature(null);
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		
		sim.setUserCreature(new UserCreature(sim));
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		
		handler.getNeuralNetGui().setSelectedCreature(sim.getEvolvingCreature());
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_SPACE, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		
		boolean paused = keys.getHandler().getSimGui().isPaused();
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_P, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(paused == keys.getHandler().getSimGui().isPaused());
		
		boolean locked = keys.getHandler().getSimulation().getLockCamera();
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_T, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(locked == keys.getHandler().getSimulation().getLockCamera());
		
		e = new KeyEvent(pan, KeyEvent.KEY_PRESSED, System.nanoTime(), 0, KeyEvent.VK_R, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(sim.getUserCreature().isDead());
	}
	
	@Test
	public void testKeyReleased(){
		JPanel pan = new JPanel();
		
		KeyEvent e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		assertFalse(sim.getUserCreature().getPressedButtons()[UserCreature.UP]);
		
		e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		assertFalse(sim.getUserCreature().getPressedButtons()[UserCreature.DOWN]);
		
		e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_LEFT, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		assertFalse(sim.getUserCreature().getPressedButtons()[UserCreature.LEFT]);
		
		e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_RIGHT, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		assertFalse(sim.getUserCreature().getPressedButtons()[UserCreature.RIGHT]);

		e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_E, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		
		e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_P, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);

		e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_T, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		
		e = new KeyEvent(pan, KeyEvent.KEY_RELEASED, System.nanoTime(), 0, KeyEvent.VK_R, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.closeAllExtraWindows();
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
	}
	
}
