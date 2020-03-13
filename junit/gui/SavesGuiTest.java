package gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.SavesGui;
import evolve.gui.component.SimTextBox;
import evolve.util.clock.GameClock;
import evolve.util.options.Settings;

public class SavesGuiTest{
	
	private SavesGui gui;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		
		gui = new SavesGui(handler);
		gui.getFrame().setVisible(false);
		
		handler.closeAllExtraWindows();
		handler.getSimGui().getFrame().setVisible(false);
		GameClock clock = handler.getClock();
		clock.setStopUpdates(false);
	}
	
	@Test
	public void testScanFiles(){
		SavesGui savesGui = handler.getSavesGui();
		SimTextBox box = savesGui.getSaveNameTextBox();
		box.setText("JUnitSavesGuiTest");
		handler.getSavesGui().handleSaveButtonPress(new ActionEvent(box, 0, ""));
		
		String[] files = gui.scanFiles();
		boolean found = false;
		for(String s : files){
			if(s.equals("JUnitSavesGuiTest")){
				found = true;
				break;
			}
		}
		assertTrue(found);
		File loc = new File(Main.SAVES_PATH + "JUnitSavesGuiTest.txt");
		loc.delete();
	}
	
	@Test
	public void updateSaveFileSelectorListTest(){
		gui.updateSaveFileSelectorList();
	}
	
	@Test
	public void handleScanFilesButtonPressTest(){
		JButton button = new JButton();
		ActionEvent e = new ActionEvent(button, 0, "");
		gui.handleScanFilesButtonPress(e);
	}
	
	@Test
	public void handleCreateNewSimuButtonPressTest(){
		JButton button = new JButton();
		ActionEvent e = new ActionEvent(button, 0, "");
		gui.handleCreateNewSimuButtonPress(e);
	}
	
	@Test
	public void handleSaveButtonPressTest(){
		JButton button = new JButton();
		
		ActionEvent e = new ActionEvent(button, 0, "");
		gui.handleSaveButtonPress(e);
		File loc = new File(Main.SAVES_PATH + ".txt");
		loc.delete();
	}
	
	@Test
	public void handleLoadButtonPressTest(){
		JButton button = new JButton();
		
		ActionEvent e = new ActionEvent(button, 0, "");
		gui.handleLoadButtonPress(e);
	}
	
	@Test
	public void handleDeleteSaveButtonTest(){
		JButton button = new JButton();
		ActionEvent e = new ActionEvent(button, 0, "");
		gui.handleDeleteSaveButton(e);
	}
	
	@Test
	public void testGetSaveNameTextBox(){
		assertFalse(gui.getSaveNameTextBox() == null);
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
