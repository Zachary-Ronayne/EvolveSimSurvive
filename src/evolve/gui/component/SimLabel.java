package evolve.gui.component;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * A JLabel set up in the style of the simulator
 */
public class SimLabel extends JLabel{

	private static final long serialVersionUID = 1L;
	
	public SimLabel(String text){
		super(text);
		SimConstants.setSimStyle(this);
		setFontSize(20);
	}
	
	/**
	 * Set the font to the given size
	 * @param size the size
	 */
	public void setFontSize(int size){
		setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, size));
	}
	
}
