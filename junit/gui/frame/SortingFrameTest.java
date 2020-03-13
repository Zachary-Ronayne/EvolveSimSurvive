package gui.frame;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.gui.frame.SortingFrame;

public class SortingFrameTest{
	
	public SortingFrame frame;
	
	@Before
	public void setUp(){
		frame = new SortingFrame();
	}
	
	@Test
	public void testParameters(){
		assertFalse(frame.isResizable());
		assertTrue(frame.getBackground().equals(Color.WHITE));
	}
	
	@After
	public void end(){
		frame.setVisible(false);
	}
}
