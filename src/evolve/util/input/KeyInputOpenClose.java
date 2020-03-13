package evolve.util.input;

import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import evolve.gui.Gui;
import evolve.gui.GuiHandler;

public class KeyInputOpenClose extends KeyAdapter{
	
	/**
	 * keeps track of if the 1 key is pressed
	 */
	private boolean down1;
	/**
	 * keeps track of if the 2 key is pressed
	 */
	private boolean down2;
	/**
	 * keeps track of if the 3 key is pressed
	 */
	private boolean down3;
	/**
	 * keeps track of if the 4 key is pressed
	 */
	private boolean down4;
	/**
	 * keeps track of if the 5 key is pressed
	 */
	private boolean down5;
	/**
	 * keeps track of if the 6 key is pressed
	 */
	private boolean down6;
	/**
	 * keeps track of if the 7 key is pressed
	 */
	private boolean down7;
	
	/**
	 * The GUI handler that this KeyInput controls
	 */
	private GuiHandler handler;
	
	public KeyInputOpenClose(GuiHandler handler){
		this.handler = handler;
		down1 = false;
		down2 = false;
		down3 = false;
		down4 = false;
		down5 = false;
		down6 = false;
		down7 = false;
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		//check to avoid an error when simulation is initially loading
		if(handler == null) return;
		
		int key = e.getKeyCode();
		Window gui = null;
		
		//if 1 is pressed, select the SavesGui
		if(key == KeyEvent.VK_1){
			if(!down1){
				Gui temp = handler.getSavesGui();
				if(temp != null) gui = temp.getFrame();
				down1 = true;
			}
		}
		//if 2 is pressed, select the NeuralNetGui
		else if(key == KeyEvent.VK_2){
			if(!down2){
				Gui temp = handler.getNeuralNetGui();
				if(temp != null) gui = temp.getFrame();
				down2 = true;
			}
		}
		//if 3 is pressed, select the NeuralNetGui
		else if(key == KeyEvent.VK_3){
			if(!down3){
				Gui temp = handler.getHelpGui();
				if(temp != null) gui = temp.getFrame();
				down3 = true;
			}
		}
		//if 4 is pressed, select the NeuralNetGui
		else if(key == KeyEvent.VK_4){
			if(!down4){
				Gui temp = handler.getSpeedGui();
				if(temp != null) gui = temp.getFrame();
				down4 = true;
			}
		}
		//if 5 is pressed, select the NeuralNetGui
		else if(key == KeyEvent.VK_5){
			if(!down5){
				Gui temp = handler.getSettingsGui();
				if(temp != null) gui = temp.getFrame();
				down5 = true;
			}
		}
		//if 6 is pressed, select the GraphGui
		else if(key == KeyEvent.VK_6){
			if(!down6){
				Gui temp = handler.getGraphGui();
				if(temp != null) gui = temp.getFrame();
				down6 = true;
			}
		}
		//if 6 is pressed, select the GraphGui
		else if(key == KeyEvent.VK_7){
			if(!down7){
				Gui temp = handler.getSortingGui();
				if(temp != null) gui = temp.getFrame();
				down7 = true;
			}
		}
		
		//show/hide the selected GUI
		if(gui != null){
			if(gui.isVisible()) gui.setVisible(false);
			else{
				gui.setVisible(true);
				gui.requestFocus();
				gui.toFront();
			}
		}
		
		//if F2 is pressed, close all windows
		if(key == KeyEvent.VK_F2) handler.closeAllExtraWindows();
	}
	
	@Override
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_1) down1 = false;
		else if(key == KeyEvent.VK_2) down2 = false;
		else if(key == KeyEvent.VK_3) down3 = false;
		else if(key == KeyEvent.VK_4) down4 = false;
		else if(key == KeyEvent.VK_5) down5 = false;
		else if(key == KeyEvent.VK_6) down6 = false;
		else if(key == KeyEvent.VK_7) down7 = false;
	}
	
}
