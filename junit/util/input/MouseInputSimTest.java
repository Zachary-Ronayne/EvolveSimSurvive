package util.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.sim.Simulation;
import evolve.util.clock.GameClock;
import evolve.util.input.MouseInputSim;
import evolve.util.options.Settings;

public class MouseInputSimTest{

	public static final double DELTA = 0.000001;
	
	private MouseInputSim mouse;
	private Simulation sim;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();

		mouse = new MouseInputSim(handler);
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
		GameClock clock = handler.getClock();
		clock.setStopUpdates(false);
		sim = handler.getSimulation();
	}
	
	@Test
	public void testMouseWheelMovedTest(){
		JPanel pan = new JPanel();
		
		double camX = sim.getCamera().getXZoomFactor();
		MouseWheelEvent e = new MouseWheelEvent(pan, MouseWheelEvent.MOUSE_WHEEL, System.nanoTime(), 0, 0, 0, 1, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -1);
		mouse.mouseWheelMoved(e);
		
		assertTrue(camX < sim.getCamera().getXZoomFactor());
	}
	
	@Test
	public void testMouseReleased(){
		JPanel pan = new JPanel();
		
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON3);
		mouse.mouseReleased(e);

		assertFalse(sim.getCamera().isAchored());
	}
	
	@Test
	public void testMousePressed(){
		JPanel pan = new JPanel();
		
		sim.getCamera().releaseAnchor();
		
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_PRESSED, System.nanoTime(), 0, 101, 20, 0, 0, 1, false, MouseEvent.BUTTON3);
		mouse.mousePressed(e);
		
		assertTrue(sim.getCamera().isAchored());
		assertEquals(sim.getCamera().getAnchorX(), 101, DELTA);
		assertEquals(sim.getCamera().getAnchorY(), 20, DELTA);
		
		e = new MouseEvent(pan, MouseEvent.MOUSE_PRESSED, System.nanoTime(), 0, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON1);
		mouse.mouseReleased(e);
	}
	
	@Test
	public void testMouseDraged(){
		JPanel pan = new JPanel();
		
		sim.getCamera().releaseAnchor();
		sim.getCamera().setAnchor(101, 20);
		sim.getCamera().setX(100);
		sim.getCamera().setY(10);
		
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_DRAGGED, System.nanoTime(), 0, 150, 10, 0, 0, 1, false, MouseEvent.BUTTON3);
		mouse.mouseDragged(e);
		assertTrue(sim.getCamera().isAchored());
		assertEquals(sim.getCamera().getX(), -49, DELTA);
		assertEquals(sim.getCamera().getY(), 10, DELTA);
	}
	
	@Test
	public void testMouseMoved(){
		JPanel pan = new JPanel();
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_MOVED, System.nanoTime(), 0, 150, 10, 0, 0, 1, false, MouseEvent.BUTTON3);
		mouse.mouseMoved(e);
	}
	
	@Test
	public void testMouseOverCreature(){
		JPanel pan = new JPanel();
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_MOVED, System.nanoTime(), 0, 150, 10, 0, 0, 1, false, MouseEvent.BUTTON3);
		mouse.mouseOverCreature(e);
	}
	
	@Test
	public void testNearestCreature(){
		mouse.nearestCreature(100, 100);
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
