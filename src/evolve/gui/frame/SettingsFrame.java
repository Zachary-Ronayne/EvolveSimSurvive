package evolve.gui.frame;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class SettingsFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	
	public SettingsFrame(){
		super();
		setLocation(new Point(200, 0));
		setResizable(false);
		setTitle("Settings");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(false);
	}
	
}
