package evolve.gui.frame;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class SortingFrame extends JDialog{
	private static final long serialVersionUID = 1L;

	public SortingFrame(){
		super();
		setLocation(new Point(800, 0));
		setResizable(false);
		setTitle("Sort and search for creatures");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
	}
}
