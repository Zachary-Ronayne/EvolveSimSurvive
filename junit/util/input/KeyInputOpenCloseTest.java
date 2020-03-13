package util.input;

import static org.junit.Assert.assertFalse;

import java.awt.Window;
import java.awt.event.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.util.input.KeyInputOpenClose;
import evolve.util.options.Settings;

public class KeyInputOpenCloseTest{
	
	private GuiHandler handler;
	private KeyInputOpenClose keys;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		handler = Main.crateHandler();
		keys = new KeyInputOpenClose(handler);
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
	}
	
	@Test
	public void testKeyPressed(){
		Window simFrame = handler.getSimGui().getFrame();
		Window savesFrame = handler.getSavesGui().getFrame();
		Window netFrame = handler.getNeuralNetGui().getFrame();
		Window helpFrame = handler.getHelpGui().getFrame();
		Window speedFrame = handler.getSpeedGui().getFrame();
		Window settingsFrame = handler.getSettingsGui().getFrame();
		
		boolean visible = savesFrame.isVisible();
		KeyEvent e = new KeyEvent(simFrame, KeyEvent.KEY_PRESSED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_1, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(visible == savesFrame.isVisible());
		
		visible = netFrame.isVisible();
		e = new KeyEvent(simFrame, KeyEvent.KEY_PRESSED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_2, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(visible == netFrame.isVisible());
		
		visible = helpFrame.isVisible();
		e = new KeyEvent(simFrame, KeyEvent.KEY_PRESSED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_3, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(visible == helpFrame.isVisible());

		visible = speedFrame.isVisible();
		e = new KeyEvent(simFrame, KeyEvent.KEY_PRESSED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_4, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(visible == speedFrame.isVisible());
		
		visible = settingsFrame.isVisible();
		e = new KeyEvent(simFrame, KeyEvent.KEY_PRESSED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_5, KeyEvent.CHAR_UNDEFINED);
		keys.keyPressed(e);
		assertFalse(visible == settingsFrame.isVisible());
		
	}
	
	@Test
	public void testKeyReleased(){
		Window simFrame = handler.getSimGui().getFrame();
		
		KeyEvent e = new KeyEvent(simFrame, KeyEvent.KEY_RELEASED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_1, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		e = new KeyEvent(simFrame, KeyEvent.KEY_RELEASED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_2, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		e = new KeyEvent(simFrame, KeyEvent.KEY_RELEASED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_3, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		e = new KeyEvent(simFrame, KeyEvent.KEY_RELEASED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_4, KeyEvent.CHAR_UNDEFINED);
		keys.keyReleased(e);
		e = new KeyEvent(simFrame, KeyEvent.KEY_RELEASED, System.nanoTime(), KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_5, KeyEvent.CHAR_UNDEFINED);
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
