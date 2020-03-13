package gui.component;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.SimLabel;

public class SimLabelTest{
	
	private SimLabel label;
	
	@Before
	public void setUp(){
		label = new SimLabel("Test");
	}
	
	@Test
	public void testSetFontSize(){
		label.setFontSize(10);
		assertTrue(label.getFont().getSize() == 10);
	}
	
}
