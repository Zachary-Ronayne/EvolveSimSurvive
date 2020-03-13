package evolve.gui.frame;

import javax.swing.JFrame;

/**
 * The frame that a SimGui uses
 */
public class SimFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new SimFrame
	 */
	public SimFrame(){
		super();
		setResizable(false);
		setTitle("Survival Sim");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
