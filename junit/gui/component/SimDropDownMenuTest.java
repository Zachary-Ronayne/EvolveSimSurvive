package gui.component;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.SimDropDownMenu;

public class SimDropDownMenuTest{
	
	private SimDropDownMenu menu;
	
	@Before
	public void setUp(){
		menu = new SimDropDownMenu();
	}
	
	@Test
	public void testSetItems(){
		menu.setItems(new String[]{"1", "2", "3"});
		assertTrue(menu.getModel().getSize() == 3);
	}
	
}
