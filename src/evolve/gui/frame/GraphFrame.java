package evolve.gui.frame;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class GraphFrame extends JDialog{
	private static final long serialVersionUID = 1L;

	public GraphFrame(){
		super();
		setLocation(new Point(100, 100));
		setResizable(false);
		setTitle("Graphs");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(false);
	}
}
