package evolve.gui.frame;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class SimSpeedFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	
	public SimSpeedFrame(){
		super();
		setLocation(new Point(0, 0));
		setResizable(false);
		setTitle("Sim Speed Controller");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(false);
	}
	
}
