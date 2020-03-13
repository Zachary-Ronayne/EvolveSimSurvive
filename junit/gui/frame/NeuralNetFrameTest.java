package gui.frame;

import org.junit.After;
import org.junit.Before;

import evolve.gui.frame.NeuralNetFrame;

public class NeuralNetFrameTest{
	
	private NeuralNetFrame frame;
	
	@Before
	public void setUp(){
		frame = new NeuralNetFrame();
	}
	
	@After
	public void end(){
		frame.setVisible(false);
	}
	
}
