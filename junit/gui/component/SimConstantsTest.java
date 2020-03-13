package gui.component;

import javax.swing.JPanel;

import org.junit.Test;

import evolve.gui.component.SimConstants;

public class SimConstantsTest{
	
	@Test
	public void testSetSimStyle(){
		JPanel pan = new JPanel();
		
		SimConstants.setSimStyle(pan);
	}
	
}
