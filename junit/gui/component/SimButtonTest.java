package gui.component;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.SimButton;

public class SimButtonTest{
	
	private SimButton button;
	
	@Before
	public void setUp(){
		button = new SimButton();
	}
	
	@Test
	public void testMousePress(){
		button.doClick();
	}
}
