package gui.frame;

import org.junit.After;
import org.junit.Before;

import evolve.gui.frame.HelpFrame;

public class HelpFrameTest{
	
	private HelpFrame frame;
	
	@Before
	public void setUp(){
		frame = new HelpFrame();
	}
	
	@After
	public void end(){
		frame.setVisible(false);
	}
}
