package evolve.gui;

import java.awt.Window;

import evolve.util.input.KeyInputOpenClose;

/**
 * An object that keeps track of a JFrame. This GUI should specifically be used for this simulation
 */
public abstract class Gui{
	
	private GuiHandler handler;
	
	/**
	 * Create a GUI object and an empty JFrame that can be used
	 */
	public Gui(GuiHandler handler){
		this.handler = handler;
	}
	
	/**
	 * Allow this GUI to open and close the extra GUIs used by the simulation
	 */
	public void addOpenCloseControls(){
		getFrame().addKeyListener(new KeyInputOpenClose(handler));
	}
	
	/**
	 * Get the frame that this GUI object uses for display
	 * @return the frame
	 */
	public abstract Window getFrame();
	
	/**
	 * Get the object that allows different kinds of GUIs to communicate with each other
	 * @return the handler
	 */
	public GuiHandler getHandler(){
		return handler;
	}
}
