package evolve.gui.frame;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class SavesFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	
	public SavesFrame(){
		super();
		setLocation(new Point(0, 0));
		setResizable(false);
		setTitle("Saves");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(false);
	}
	
}
