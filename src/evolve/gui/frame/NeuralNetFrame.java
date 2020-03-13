package evolve.gui.frame;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class NeuralNetFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	
	public NeuralNetFrame(){
		super();
		setLocation(new Point(1000, 0));
		setResizable(false);
		setTitle("Selected Creature");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(false);
	}
	
}
