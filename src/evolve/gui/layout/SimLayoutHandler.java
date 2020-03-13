package evolve.gui.layout;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BoxLayout;

/**
 * A class that creates a BoxLayout in the style of the simulation
 */
public class SimLayoutHandler{
	
	/**
	 * Create a BoxLayout as vertical and associate it with to the given container
	 * @param target
	 */
	public static void createVerticalLayout(Container target){
		BoxLayout layout = new BoxLayout(target, BoxLayout.Y_AXIS);
		target.setLayout(layout);
		target.setBackground(Color.WHITE);
	}

	/**
	 * Create a BoxLayout as horizontal and associate it with to the given container
	 * @param target
	 */
	public static void createHorizontalLayout(Container target){
		BoxLayout layout = new BoxLayout(target, BoxLayout.X_AXIS);
		target.setLayout(layout);
		target.setBackground(Color.WHITE);
	}

	/**
	 * Create a GridLayout with the given rows and cols and associate it with to the given container
	 * @param target
	 * @param row
	 * @param col
	 */
	public static void createGridlLayout(Container target, int row, int col){
		GridLayout layout = new GridLayout(row, col);
		target.setLayout(layout);
		target.setBackground(Color.WHITE);
	}
	
	
}
