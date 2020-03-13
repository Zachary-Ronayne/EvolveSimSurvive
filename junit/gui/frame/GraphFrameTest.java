package gui.frame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.gui.frame.GraphFrame;

public class GraphFrameTest{
	
	private GraphFrame frame;
	
	@Before
	public void setUp(){
		frame = new GraphFrame();
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
