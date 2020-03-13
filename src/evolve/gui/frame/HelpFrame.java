package evolve.gui.frame;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class HelpFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	
	public HelpFrame(){
		super();
		setLocation(new Point(0, 0));
		setResizable(false);
		setTitle("Help");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
	}
}
