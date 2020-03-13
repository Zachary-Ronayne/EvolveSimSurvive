package gui.frame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.gui.frame.SimSpeedFrame;

public class SimSpeedFrameTest{
	
	private SimSpeedFrame frame;
	
	@Before
	public void setUp(){
		frame = new SimSpeedFrame();
	}
	
	@Test
	public void test(){
		frame.setVisible(false);
	}
	@After
	public void end(){
		frame.setVisible(false);
	}
	
}
