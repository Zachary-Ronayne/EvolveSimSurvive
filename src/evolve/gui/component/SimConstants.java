package evolve.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;

/**
 * A class that keeps track of some constants used by components
 */
public class SimConstants{
	/**
	 * The font type this component uses
	 */
	public static final String FONT_NAME = "Calibri";
	/**
	 * The color of the default background of this component
	 */
	public static final Color BG_COLOR = Color.WHITE;
	/**
	 * The color of the background of this component while the mouse is on it
	 */
	public static final Color BG_HOVER_COLOR = new Color(160, 160, 200);
	
	/**
	 * Set the style of the given component to be that of the simulation
	 * @param c
	 */
	public static void setSimStyle(JComponent c){
		//alignment
		c.setAlignmentX(Component.CENTER_ALIGNMENT);
		c.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		//text
		c.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 30));
		
		//general colors
		c.setBackground(SimConstants.BG_COLOR);
		c.setForeground(Color.BLACK);
	}
	
}
