package gui.component;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.SimTextBox;

public class SimTextBoxTest{
	
	private SimTextBox box;
	
	@Before
	public void setUp(){
		box = new SimTextBox();
	}
	
	@Test
	public void testRestrictInput(){
		String test = "12345abcde!@#$%^&*()-_=+[]{}\\|,<.>/?";
		
		box.restrictInput(SimTextBox.RESTRICT_NONE);
		box.setText(test);
		assertTrue(box.getText().equals(test));
		
		box.restrictInput(SimTextBox.RESTRICT_INTEGER);
		box.setText(test);
		assertTrue(box.getText().equals("12345-"));
		
		box.restrictInput(SimTextBox.RESTRICT_DOUBLE);
		box.setText(test);
		assertTrue(box.getText().equals("12345-."));
		
		box.restrictInput(SimTextBox.RESTRICT_FILE);
		box.setText(test);
		assertTrue(box.getText().equals("12345abcde#$%^&()-_[]{}"));
	}
	
	@Test
	public void testSetFontSize(){
		box.setFontSize(10);
		assertTrue(box.getFont().getSize() == 10);
	}
	
	@Test
	public void tetsSetCharWidth(){
		box.setCharWidth(100);
		assertTrue(box.getPreferredSize().getWidth() == 100);
	}
	
}
