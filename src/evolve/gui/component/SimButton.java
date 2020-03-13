package evolve.gui.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/**
 * A JButton that uses the default style of the simulator
 */
public class SimButton extends JButton{
	private static final long serialVersionUID = 1L;
	
	public SimButton(){
		super();
		
		SimConstants.setSimStyle(this);
		
		//mouse hovering color and clicking
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				setBackground(SimConstants.BG_HOVER_COLOR);
			}
			@Override
			public void mouseExited(MouseEvent e){
				setBackground(SimConstants.BG_COLOR);
			}
		});
	}
	
}
