package gui.component;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.SimSpeedButton;

public class SimSpeedButtonTest{
	
	private SimSpeedButton button;
	
	@Before
	public void setUp(){
		button = new SimSpeedButton(1234);
	}
	
	@Test
	public void testGetTime(){
		assertTrue(button.getTime() == 1234l);
	}
	
}
