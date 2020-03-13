package gui.frame;

import org.junit.After;
import org.junit.Before;

import evolve.gui.frame.SimFrame;

public class SimFrameTest{
	
	private SimFrame frame;
	
	@Before
	public void setUp(){
		frame = new SimFrame();
	}
	
	@After
	public void end(){
		frame.setVisible(false);
	}
}
