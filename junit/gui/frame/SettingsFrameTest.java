package gui.frame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.gui.frame.SettingsFrame;

public class SettingsFrameTest{
	
	private SettingsFrame frame;
	
	@Before
	public void setUp(){
		frame = new SettingsFrame();
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
