package gui.component;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.SimCheckBox;

public class SimCheckBoxTest{
	
	private SimCheckBox box;
	
	@Before
	public void setUp(){
		box = new SimCheckBox();
	}
	
	@Test
	public void test(){
		box.setSelected(true);
	}
	
}
